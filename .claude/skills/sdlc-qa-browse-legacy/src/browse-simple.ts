#!/usr/bin/env bun
/**
 * browse-simple - Chromium CLI + shell-based browser for macOS 11 compatibility
 *
 * Architecture:
 * - chromium CLI for screenshots
 * - State file for URL/session tracking
 * - Optional: CDP (Chrome DevTools Protocol) for interactions
 */

import * as fs from 'fs';
import * as path from 'path';
import { spawn } from 'child_process';

// ─── Config ─────────────────────────────────────────────────────────────
const STATE_DIR = process.env.BROWSE_STATE_DIR || path.join(process.env.HOME || '', '.gstack');
const STATE_FILE = path.join(STATE_DIR, 'browse-simple.json');

const CHROMIUM_PATHS = [
  // Playwright's chromium-1019 (compatible with macOS 11)
  path.join(process.env.HOME || '', '.cache', 'ms-playwright', 'chromium-1019', 'chrome-mac', 'Chromium.app', 'Contents', 'MacOS', 'Chromium'),
  // System chromium
  '/Applications/Chromium.app/Contents/MacOS/Chromium',
  '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
];

// ─── State Management ────────────────────────────────────────────────────
interface BrowseState {
  currentUrl: string;
  history: string[];
  cookies: Array<{ name: string; value: string; domain: string }>;
  screenshots: string[];
  lastCommand: string;
}

function readState(): BrowseState {
  try {
    const data = fs.readFileSync(STATE_FILE, 'utf-8');
    return JSON.parse(data);
  } catch {
    return {
      currentUrl: 'about:blank',
      history: [],
      cookies: [],
      screenshots: [],
      lastCommand: '',
    };
  }
}

function writeState(state: BrowseState) {
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
    '  bun install\n' +
    '  bunx playwright install chromium'
  );
}

// ─── Screenshot with Chromium CLI ───────────────────────────────────────
interface ScreenshotOptions {
  url: string;
  outputPath: string;
  width?: number;
  height?: number;
  timeout?: number;
}

async function screenshot(options: ScreenshotOptions): Promise<string> {
  const chromium = findChromium();
  const {
    url,
    outputPath,
    width = 1280,
    height = 720,
    timeout = 30000,
  } = options;

  // Ensure output directory exists
  const outputDir = path.dirname(outputPath);
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
  }

  return new Promise((resolve, reject) => {
    const args = [
      '--headless',
      '--disable-gpu',
      '--no-sandbox',
      '--disable-dev-shm-usage',
      '--disable-software-rasterizer',
      `--window-size=${width},${height}`,
      '--screenshot=' + outputPath,
      '--virtual-time-budget=1000', // Fast forward JS timers
      url,
    ];

    const proc = spawn(chromium, args, {
      stdio: ['ignore', 'pipe', 'pipe'],
    });

    let stderr = '';
    proc.stderr?.on('data', (data) => {
      stderr += data.toString();
    });

    proc.on('close', (code) => {
      if (code === 0 && fs.existsSync(outputPath)) {
        resolve(outputPath);
      } else {
        reject(new Error(`Screenshot failed (exit ${code}): ${stderr || 'unknown error'}`));
      }
    });

    setTimeout(() => {
      proc.kill();
      reject(new Error(`Screenshot timeout after ${timeout}ms`));
    }, timeout);
  });
}

// ─── Commands ───────────────────────────────────────────────────────────

// goto <url> - Navigate to URL and take screenshot
async function cmdGoto(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: goto <url>');
    process.exit(1);
  }

  const url = normalizeUrl(args[0]);
  const state = readState();

  // Update state
  state.currentUrl = url;
  state.history.push(url);
  state.lastCommand = 'goto';
  writeState(state);

  console.log(`Navigating to: ${url}`);

  // Take screenshot
  const timestamp = Date.now();
  const screenshotPath = path.join(STATE_DIR, `screenshot-${timestamp}.png`);

  try {
    await screenshot({
      url,
      outputPath: screenshotPath,
    });
    console.log(`Screenshot saved: ${screenshotPath}`);
    state.screenshots.push(screenshotPath);
    writeState(state);
  } catch (err: any) {
    console.error(`Screenshot failed: ${err.message}`);
  }
}

// screenshot [path] - Take screenshot of current URL
async function cmdScreenshot(args: string[]): Promise<void> {
  const state = readState();
  let outputPath = args[0];

  if (!outputPath) {
    const timestamp = Date.now();
    outputPath = path.join(STATE_DIR, `screenshot-${timestamp}.png`);
  } else {
    // Support paths relative to current directory
    outputPath = path.resolve(process.cwd(), outputPath);
  }

  try {
    await screenshot({
      url: state.currentUrl,
      outputPath,
    });
    console.log(`Screenshot saved: ${outputPath}`);
    state.screenshots.push(outputPath);
    writeState(state);
  } catch (err: any) {
    console.error(`Screenshot failed: ${err.message}`);
    process.exit(1);
  }
}

// url - Show current URL
function cmdUrl(): void {
  const state = readState();
  console.log(state.currentUrl);
}

// status - Show current state
function cmdStatus(): void {
  const state = readState();
  console.log(JSON.stringify(state, null, 2));
}

// help - Show help
function cmdHelp(): void {
  console.log(`browse-simple - Chromium CLI browser for macOS 11

Navigation:
  goto <url>          Navigate to URL and take screenshot
  url                 Show current URL
  back                Go back in history

Screenshots:
  screenshot [path]   Take screenshot of current URL
                     Default path: ~/.gstack/screenshot-<timestamp>.png

State:
  status              Show current state
  clear               Clear state and history

Help:
  help                Show this message

Examples:
  browse-simple goto https://example.com
  browse-simple screenshot /tmp/output.png
  browse-simple url`);
}

// back - Go back in history
function cmdBack(): void {
  const state = readState();
  if (state.history.length <= 1) {
    console.log('No previous URL in history');
    return;
  }

  // Remove current URL
  state.history.pop();
  // Get previous URL
  const prevUrl = state.history[state.history.length - 1];
  state.currentUrl = prevUrl;
  writeState(state);

  console.log(`Back to: ${prevUrl}`);
}

// clear - Clear state
function cmdClear(): void {
  try {
    fs.unlinkSync(STATE_FILE);
    console.log('State cleared');
  } catch {
    console.log('No state to clear');
  }
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

  switch (command) {
    case 'goto':
      await cmdGoto(commandArgs);
      break;
    case 'screenshot':
      await cmdScreenshot(commandArgs);
      break;
    case 'url':
      cmdUrl();
      break;
    case 'status':
      cmdStatus();
      break;
    case 'back':
      cmdBack();
      break;
    case 'clear':
      cmdClear();
      break;
    case 'help':
    case '--help':
    case '-h':
      cmdHelp();
      break;
    default:
      console.error(`Unknown command: ${command}`);
      console.error('Run "browse-simple help" for usage');
      process.exit(1);
  }
}

if (import.meta.main) {
  main().catch((err) => {
    console.error(`[browse] ${err.message}`);
    process.exit(1);
  });
}
