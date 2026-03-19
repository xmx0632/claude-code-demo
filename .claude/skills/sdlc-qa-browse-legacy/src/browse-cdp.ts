#!/usr/bin/env bun
/**
 * browse-cdp - Chromium with CDP support for macOS 11 compatibility
 *
 * Uses Chrome DevTools Protocol (CDP) for:
 * - Navigation (goto, back, forward)
 * - Screenshot
 * - Element interaction (click, fill, etc.)
 * - JavaScript evaluation
 *
 * Architecture:
 * - Launch chromium with --remote-debugging-port
 * - Connect via chrome-remote-interface (CDP client)
 * - Maintain state in JSON file
 */

import * as fs from 'fs';
import * as path from 'path';
import * as crypto from 'crypto';
import CDP from 'chrome-remote-interface';
import type { Protocol } from 'chrome-remote-interface';

// ─── Config ─────────────────────────────────────────────────────────────
const STATE_DIR = process.env.BROWSE_STATE_DIR || path.join(process.env.HOME || '', '.gstack');
const STATE_FILE = path.join(STATE_DIR, 'browse-cdp.json');

const CHROMIUM_PATHS = [
  path.join(process.env.HOME || '', '.cache', 'ms-playwright', 'chromium-1019', 'chrome-mac', 'Chromium.app', 'Contents', 'MacOS', 'Chromium'),
  '/Applications/Chromium.app/Contents/MacOS/Chromium',
  '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
];

// ─── State Management ────────────────────────────────────────────────────
interface BrowserState {
  pid?: number;
  port: number;
  currentUrl: string;
  history: string[];
  historyIndex: number;
  lastCommand: string;
}

function readState(): BrowserState | null {
  try {
    const data = fs.readFileSync(STATE_FILE, 'utf-8');
    return JSON.parse(data);
  } catch {
    return null;
  }
}

function writeState(state: BrowserState) {
  try {
    fs.mkdirSync(STATE_DIR, { recursive: true });
    fs.writeFileSync(STATE_FILE, JSON.stringify(state, null, 2));
  } catch (err) {
    console.error(`[browse] Warning: Could not write state: ${err}`);
  }
}

// ─── Chromium Path Detection ────────────────────────────────────────────
function findChromium(): string {
  for (const p of CHROMIUM_PATHS) {
    if (fs.existsSync(p)) {
      return p;
    }
  }
  throw new Error(
    'Chromium not found. Install with:\n' +
    '  bunx playwright install chromium\n' +
    'Or download Chromium and place it in Applications.'
  );
}

// ─── Browser Lifecycle ──────────────────────────────────────────────────
async function launchBrowser(): Promise<BrowserState> {
  const chromium = findChromium();
  const port = 9200 + Math.floor(Math.random() * 1000);
  const userDataDir = path.join(STATE_DIR, `chromium-profile-${port}`);

  // Create user data directory
  fs.mkdirSync(userDataDir, { recursive: true });

  // Launch chromium with remote debugging
  const proc = Bun.spawn([
    chromium,
    '--headless',
    '--disable-gpu',
    '--no-sandbox',
    '--disable-dev-shm-usage',
    `--remote-debugging-port=${port}`,
    `--user-data-dir=${userDataDir}`,
  ], {
    stdio: ['ignore', 'pipe', 'pipe'],
    detached: true,
  });

  proc.unref();

  const state: BrowserState = {
    pid: proc.pid,
    port,
    currentUrl: 'about:blank',
    history: [],
    historyIndex: -1,
    lastCommand: '',
  };

  writeState(state);

  // Wait for CDP to be ready
  const maxWait = 10000;
  const start = Date.now();
  while (Date.now() - start < maxWait) {
    try {
      await CDP.Version({ port });
      console.log(`[browse] Browser ready on port ${port}`);
      return state;
    } catch {
      await new Promise(r => setTimeout(r, 100));
    }
  }

  throw new Error('Browser failed to start within timeout');
}

async function ensureBrowser(): Promise<{ state: BrowserState; client: CDP.Client }> {
  let state = readState();

  // Check if browser is still running
  if (state?.pid) {
    try {
      process.kill(state.pid, 0);
      // Try to connect
      try {
        const client = await CDP({ port: state.port });
        return { state, client };
      } catch {
        // Browser exists but CDP not responding, restart
        console.log('[browse] Browser exists but not responding, restarting...');
      }
    } catch {
      // Process is dead, restart
      console.log('[browse] Browser process dead, restarting...');
    }
  }

  // Launch new browser
  state = await launchBrowser();
  const client = await CDP({ port: state.port });
  return { state, client };
}

// ─── CDP Helpers ────────────────────────────────────────────────────────
async function withClient<T>(fn: (client: CDP.Client) => Promise<T>): Promise<T> {
  const { client } = await ensureBrowser();
  try {
    return await fn(client);
  } finally {
    await client.close();
  }
}

// ─── Commands ───────────────────────────────────────────────────────────

// goto <url> - Navigate to URL
async function cmdGoto(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: goto <url>');
    process.exit(1);
  }

  const url = normalizeUrl(args[0]);
  const { state, client } = await ensureBrowser();

  try {
    const { Page, Runtime } = client;
    await Page.enable();
    await Runtime.enable();

    // Navigate
    const result = await Page.navigate({ url });

    if (result.errorText) {
      throw new Error(`Navigation error: ${result.errorText}`);
    }

    // Wait for load event with timeout
    await Promise.race([
      Page.loadEventFired(),
      new Promise((_, reject) =>
        setTimeout(() => reject(new Error('Navigation timeout after 15s')), 15000)
      ),
    ]) as any;

    // Update state
    state.currentUrl = url;
    state.historyIndex++;
    state.history = state.history.slice(0, state.historyIndex);
    state.history.push(url);
    state.lastCommand = 'goto';
    writeState(state);

    console.log(`Navigated to: ${url}`);
  } catch (err: any) {
    console.error(`Navigation failed: ${err.message}`);
    throw err;
  } finally {
    await client.close();
  }
}

// screenshot [path] - Take screenshot
async function cmdScreenshot(args: string[]): Promise<void> {
  let outputPath = args[0];

  if (!outputPath) {
    const timestamp = Date.now();
    fs.mkdirSync(STATE_DIR, { recursive: true });
    outputPath = path.join(STATE_DIR, `screenshot-${timestamp}.png`);
  } else {
    outputPath = path.resolve(process.cwd(), outputPath);
  }

  // Ensure output directory exists
  const outputDir = path.dirname(outputPath);
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
  }

  const { client } = await ensureBrowser();
  try {
    const { Page } = client;
    await Page.enable();

    const { data } = await Page.captureScreenshot();

    // Convert base64 to binary
    const buffer = Buffer.from(data, 'base64');
    fs.writeFileSync(outputPath, buffer);

    console.log(`Screenshot saved: ${outputPath}`);
  } catch (err: any) {
    console.error(`Screenshot failed: ${err.message}`);
    throw err;
  } finally {
    await client.close();
  }
}

// text - Get page text
async function cmdText(): Promise<void> {
  await withClient(async (client) => {
    const { Runtime, DOM } = client;
    await DOM.enable();
    await Runtime.enable();

    const { result } = await Runtime.evaluate({
      expression: 'document.body.innerText',
      returnByValue: true,
    });

    const text = (result as any).value || '';
    console.log(text);
  });
}

// html [selector] - Get HTML
async function cmdHtml(args: string[]): Promise<void> {
  await withClient(async (client) => {
    const { Runtime, DOM } = client;
    await DOM.enable();
    await Runtime.enable();

    const selector = args[0];
    const expr = selector
      ? `document.querySelector('${selector}').innerHTML`
      : 'document.body.innerHTML';

    const { result } = await Runtime.evaluate({
      expression: expr,
      returnByValue: true,
    });

    const html = (result as any).value || '';
    console.log(html);
  });
}

// click <selector> - Click element
async function cmdClick(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: click <selector>');
    process.exit(1);
  }

  const selector = args[0];

  await withClient(async (client) => {
    const { Runtime, DOM } = client;
    await DOM.enable();
    await Runtime.enable();

    // Click using JavaScript
    const { result } = await Runtime.evaluate({
      expression: `
        (() => {
          const el = document.querySelector('${selector}');
          if (!el) throw new Error('Element not found: ${selector}');
          el.click();
          return 'Clicked';
        })()
      `,
      awaitPromise: true,
    });

    console.log('Clicked:', selector);
  });
}

// fill <selector> <value> - Fill input
async function cmdFill(args: string[]): Promise<void> {
  if (args.length < 2) {
    console.error('Usage: fill <selector> <value>');
    process.exit(1);
  }

  const selector = args[0];
  const value = args.slice(1).join(' ');

  await withClient(async (client) => {
    const { Runtime } = client;
    await Runtime.enable();

    const { result } = await Runtime.evaluate({
      expression: `
        (() => {
          const el = document.querySelector('${selector}');
          if (!el) throw new Error('Element not found: ${selector}');
          el.value = '${value.replace(/'/g, "\\'")}';
          el.dispatchEvent(new Event('input', { bubbles: true }));
          el.dispatchEvent(new Event('change', { bubbles: true }));
          return 'Filled';
        })()
      `,
      awaitPromise: true,
    });

    console.log(`Filled ${selector} with: ${value}`);
  });
}

// js <expression> - Evaluate JavaScript
async function cmdJs(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: js <expression>');
    process.exit(1);
  }

  const expr = args.join(' ');

  await withClient(async (client) => {
    const { Runtime } = client;
    await Runtime.enable();

    const { result } = await Runtime.evaluate({
      expression: expr,
      returnByValue: true,
    });

    console.log(JSON.stringify((result as any).value, null, 2));
  });
}

// url - Show current URL
async function cmdUrl(): Promise<void> {
  const state = readState();
  if (state) {
    console.log(state.currentUrl);
  }
}

// status - Show status
async function cmdStatus(): Promise<void> {
  const state = readState();
  if (!state) {
    console.log('Browser not running');
    return;
  }

  try {
    process.kill(state.pid!, 0);
    console.log(`Browser running (PID: ${state.pid}, Port: ${state.port})`);
    console.log(`Current URL: ${state.currentUrl}`);
    console.log(`History: ${state.history.length} pages`);
  } catch {
    console.log('Browser process not running');
  }
}

// stop - Stop browser
async function cmdStop(): Promise<void> {
  const state = readState();
  if (!state?.pid) {
    console.log('Browser not running');
    return;
  }

  try {
    process.kill(state.pid, 'SIGTERM');
    fs.unlinkSync(STATE_FILE);
    console.log('Browser stopped');
  } catch (err: any) {
    console.error(`Error stopping browser: ${err.message}`);
  }
}

// help - Show help
function cmdHelp(): void {
  console.log(`browse-cdp - Chromium CDP browser for macOS 11

Navigation:
  goto <url>          Navigate to URL
  url                 Show current URL
  status              Show browser status
  stop                Stop browser

Reading:
  text                Get page text
  html [selector]     Get HTML (optionally from selector)

Interaction:
  click <selector>    Click element
  fill <sel> <value>  Fill input field
  js <expression>     Evaluate JavaScript

Visual:
  screenshot [path]   Take screenshot
                     Default: ~/.gstack/screenshot-<timestamp>.png

Help:
  help                Show this message

Examples:
  browse-cdp goto https://example.com
  browse-cdp click "#submit-button"
  browse-cdp fill "input[name='email']" "test@example.com"
  browse-cdp screenshot /tmp/output.png
  browse-cdp js "document.title"`);
}

// ─── URL Normalization ───────────────────────────────────────────────────
function normalizeUrl(url: string): string {
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  if (url.startsWith('localhost:') || url.match(/^\d+\.\d+\.\d+\.\d+/)) {
    return 'http://' + url;
  }
  return 'https://' + url;
}

// ─── Main ───────────────────────────────────────────────────────────────
async function main() {
  const args = process.argv.slice(2);

  if (args.length === 0) {
    cmdHelp();
    process.exit(0);
  }

  const command = args[0];
  const commandArgs = args.slice(1);

  try {
    switch (command) {
      case 'goto':
        await cmdGoto(commandArgs);
        break;
      case 'screenshot':
        await cmdScreenshot(commandArgs);
        break;
      case 'text':
        await cmdText();
        break;
      case 'html':
        await cmdHtml(commandArgs);
        break;
      case 'click':
        await cmdClick(commandArgs);
        break;
      case 'fill':
        await cmdFill(commandArgs);
        break;
      case 'js':
        await cmdJs(commandArgs);
        break;
      case 'url':
        await cmdUrl();
        break;
      case 'status':
        await cmdStatus();
        break;
      case 'stop':
        await cmdStop();
        break;
      case 'help':
      case '--help':
      case '-h':
        cmdHelp();
        break;
      default:
        console.error(`Unknown command: ${command}`);
        console.error('Run "browse-cdp help" for usage');
        process.exit(1);
    }
  } catch (err: any) {
    console.error(`[browse] ${err.message}`);
    process.exit(1);
  }
}

if (import.meta.main) {
  main().catch((err) => {
    console.error(`[browse] ${err.message}`);
    process.exit(1);
  });
}
