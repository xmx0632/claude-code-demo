#!/usr/bin/env bun
/**
 * browse-puppeteer - Puppeteer-based browser for macOS 11 compatibility
 *
 * Uses Puppeteer 19.11.1 which bundles a compatible Chromium version.
 * Supports full UI interaction testing.
 */

import * as fs from 'fs';
import * as path from 'path';
import puppeteer from 'puppeteer';

// ─── Config ─────────────────────────────────────────────────────────────
const STATE_DIR = process.env.BROWSE_STATE_DIR || path.join(process.env.HOME || '', '.gstack');
const STATE_FILE = path.join(STATE_DIR, 'browse-puppeteer.json');

// ─── State Management ────────────────────────────────────────────────────
interface BrowserState {
  pid?: number;
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

// ─── Browser Lifecycle ──────────────────────────────────────────────────
let browser: Awaited<ReturnType<typeof puppeteer.launch>> | null = null;
let page: Awaited<ReturnType<typeof puppeteer.Browser.prototype.newPage>> | null = null;

async function ensureBrowser(): Promise<typeof page> {
  if (page) {
    return page;
  }

  // Use chromium-1019 from Playwright (compatible with macOS 11)
  const chromiumPath = '/Users/xmx0632/Library/Caches/ms-playwright/chromium-1019/chrome-mac/Chromium.app/Contents/MacOS/Chromium';

  browser = await puppeteer.launch({
    headless: true,
    executablePath: chromiumPath,
    args: [
      '--no-sandbox',
      '--disable-setuid-sandbox',
      '--disable-dev-shm-usage',
    ],
  });

  page = await browser.newPage();
  await page.setViewport({ width: 1280, height: 720 });

  // Set default timeout
  page.setDefaultTimeout(30000);

  // Initialize state
  const state: BrowserState = {
    currentUrl: 'about:blank',
    history: [],
    historyIndex: -1,
    lastCommand: 'launch',
  };
  writeState(state);

  return page;
}

async function closeBrowser() {
  if (browser) {
    await browser.close();
    browser = null;
    page = null;
  }
  // Clean up state file
  try {
    fs.unlinkSync(STATE_FILE);
  } catch {}
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

// ─── Commands ───────────────────────────────────────────────────────────

// goto <url> - Navigate to URL
async function cmdGoto(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: goto <url>');
    process.exit(1);
  }

  const url = normalizeUrl(args[0]);
  const p = await ensureBrowser();

  await p.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 });

  // Update state
  const state = readState() || { currentUrl: '', history: [], historyIndex: -1, lastCommand: '' };
  state.currentUrl = url;
  state.historyIndex++;
  state.history = state.history.slice(0, state.historyIndex);
  state.history.push(url);
  state.lastCommand = 'goto';
  writeState(state);

  console.log(`Navigated to: ${url}`);
}

// url - Show current URL
async function cmdUrl(): Promise<void> {
  const p = await ensureBrowser();
  console.log(p.url());
}

// back - Go back
async function cmdBack(): Promise<void> {
  const p = await ensureBrowser();
  await p.goBack();
  console.log(`Back to: ${p.url()}`);
}

// forward - Go forward
async function cmdForward(): Promise<void> {
  const p = await ensureBrowser();
  await p.goForward();
  console.log(`Forward to: ${p.url()}`);
}

// reload - Reload page
async function cmdReload(): Promise<void> {
  const p = await ensureBrowser();
  await p.reload();
  console.log(`Reloaded: ${p.url()}`);
}

// text - Get page text
async function cmdText(): Promise<void> {
  const p = await ensureBrowser();
  const text = await p.evaluate(() => document.body.innerText);
  console.log(text);
}

// title - Get page title
async function cmdTitle(): Promise<void> {
  const p = await ensureBrowser();
  const title = await p.title();
  console.log(title);
}

// html [selector] - Get HTML
async function cmdHtml(args: string[]): Promise<void> {
  const p = await ensureBrowser();
  const selector = args[0];

  if (selector) {
    const html = await p.$eval(selector, el => el.innerHTML);
    console.log(html);
  } else {
    const html = await p.evaluate(() => document.body.innerHTML);
    console.log(html);
  }
}

// click <selector> - Click element
async function cmdClick(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: click <selector>');
    process.exit(1);
  }

  const selector = args[0];
  const p = await ensureBrowser();

  await p.click(selector);
  console.log(`Clicked: ${selector}`);
}

// fill <selector> <value> - Fill input
async function cmdFill(args: string[]): Promise<void> {
  if (args.length < 2) {
    console.error('Usage: fill <selector> <value>');
    process.exit(1);
  }

  const selector = args[0];
  const value = args.slice(1).join(' ');
  const p = await ensureBrowser();

  await p.type(selector, value);
  console.log(`Filled ${selector} with: ${value}`);
}

// type <text> - Type text into focused element
async function cmdType(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: type <text>');
    process.exit(1);
  }

  const text = args.join(' ');
  const p = await ensureBrowser();

  await p.keyboard.type(text);
  console.log(`Typed: ${text}`);
}

// select <selector> <value> - Select dropdown option
async function cmdSelect(args: string[]): Promise<void> {
  if (args.length < 2) {
    console.error('Usage: select <selector> <value>');
    process.exit(1);
  }

  const selector = args[0];
  const value = args[1];
  const p = await ensureBrowser();

  await p.select(selector, value);
  console.log(`Selected ${value} in ${selector}`);
}

// hover <selector> - Hover over element
async function cmdHover(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: hover <selector>');
    process.exit(1);
  }

  const selector = args[0];
  const p = await ensureBrowser();

  await p.hover(selector);
  console.log(`Hovered: ${selector}`);
}

// press <key> - Press key
async function cmdPress(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: press <key>');
    process.exit(1);
  }

  const key = args[0];
  const p = await ensureBrowser();

  await p.keyboard.press(key);
  console.log(`Pressed: ${key}`);
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

  const p = await ensureBrowser();
  await p.screenshot({ path: outputPath, fullPage: false });

  console.log(`Screenshot saved: ${outputPath}`);
}

// pdf [path] - Save as PDF
async function cmdPdf(args: string[]): Promise<void> {
  let outputPath = args[0];

  if (!outputPath) {
    const timestamp = Date.now();
    fs.mkdirSync(STATE_DIR, { recursive: true });
    outputPath = path.join(STATE_DIR, `page-${timestamp}.pdf`);
  } else {
    outputPath = path.resolve(process.cwd(), outputPath);
  }

  const p = await ensureBrowser();
  await p.pdf({ path: outputPath, format: 'A4' });

  console.log(`PDF saved: ${outputPath}`);
}

// js <expression> - Execute JavaScript
async function cmdJs(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: js <expression>');
    process.exit(1);
  }

  const expr = args.join(' ');
  const p = await ensureBrowser();

  const result = await p.evaluate(expr);
  console.log(JSON.stringify(result, null, 2));
}

// css <selector> <property> - Get computed style
async function cmdCss(args: string[]): Promise<void> {
  if (args.length < 2) {
    console.error('Usage: css <selector> <property>');
    process.exit(1);
  }

  const selector = args[0];
  const property = args[1];
  const p = await ensureBrowser();

  const value = await p.$eval(selector, (el, prop) => {
    return window.getComputedStyle(el).getPropertyValue(prop);
  }, property);

  console.log(value);
}

// attrs <selector> - Get element attributes
async function cmdAttrs(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: attrs <selector>');
    process.exit(1);
  }

  const selector = args[0];
  const p = await ensureBrowser();

  const attrs = await p.$eval(selector, (el) => {
    const result: Record<string, string> = {};
    for (let i = 0; i < el.attributes.length; i++) {
      const attr = el.attributes[i];
      result[attr.name] = attr.value;
    }
    return result;
  });

  console.log(JSON.stringify(attrs, null, 2));
}

// status - Show browser status
async function cmdStatus(): Promise<void> {
  const state = readState();
  if (!state) {
    console.log('Browser not running');
    return;
  }

  console.log(`Current URL: ${state.currentUrl}`);
  console.log(`History: ${state.history.length} pages`);
  console.log(`Last command: ${state.lastCommand || 'none'}`);

  if (page) {
    const title = await page.title();
    console.log(`Page title: ${title}`);
  }
}

// stop - Stop browser
async function cmdStop(): Promise<void> {
  await closeBrowser();
  console.log('Browser stopped');
}

// cookies - Show cookies
async function cmdCookies(): Promise<void> {
  const p = await ensureBrowser();
  const cookies = await p.cookies();
  console.log(JSON.stringify(cookies, null, 2));
}

// help - Show help
function cmdHelp(): void {
  console.log(`browse-puppeteer - Puppeteer browser for macOS 11

Navigation:
  goto <url>          Navigate to URL
  back                Go back
  forward             Go forward
  reload              Reload page
  url                 Show current URL

Reading:
  text                Get page text
  title               Get page title
  html [selector]     Get HTML

Interaction:
  click <selector>    Click element
  fill <sel> <value>  Fill input field
  type <text>         Type text into focused element
  select <sel> <val>  Select dropdown option
  hover <selector>    Hover over element
  press <key>         Press key (Enter, Tab, Escape, etc)

Inspection:
  screenshot [path]   Take screenshot
  pdf [path]          Save as PDF
  js <expression>     Execute JavaScript
  css <sel> <prop>    Get computed style
  attrs <selector>    Get element attributes

State:
  status              Show browser status
  stop                Stop browser
  cookies             Show cookies

Help:
  help                Show this message

Examples:
  browse-puppeteer goto https://example.com
  browse-puppeteer fill "#email" "test@example.com"
  browse-puppeteer click "button[type='submit']"
  browse-puppeteer screenshot /tmp/screenshot.png
  browse-puppeteer js "document.title"`);
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
      case 'url':
        await cmdUrl();
        break;
      case 'back':
        await cmdBack();
        break;
      case 'forward':
        await cmdForward();
        break;
      case 'reload':
        await cmdReload();
        break;
      case 'text':
        await cmdText();
        break;
      case 'title':
        await cmdTitle();
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
      case 'type':
        await cmdType(commandArgs);
        break;
      case 'select':
        await cmdSelect(commandArgs);
        break;
      case 'hover':
        await cmdHover(commandArgs);
        break;
      case 'press':
        await cmdPress(commandArgs);
        break;
      case 'screenshot':
      case 'shot':
        await cmdScreenshot(commandArgs);
        break;
      case 'pdf':
        await cmdPdf(commandArgs);
        break;
      case 'js':
        await cmdJs(commandArgs);
        break;
      case 'css':
        await cmdCss(commandArgs);
        break;
      case 'attrs':
        await cmdAttrs(commandArgs);
        break;
      case 'status':
        await cmdStatus();
        break;
      case 'stop':
        await cmdStop();
        break;
      case 'cookies':
        await cmdCookies();
        break;
      case 'help':
      case '--help':
      case '-h':
        cmdHelp();
        break;
      default:
        console.error(`Unknown command: ${command}`);
        console.error('Run "browse-puppeteer help" for usage');
        process.exit(1);
    }
  } catch (err: any) {
    console.error(`[browse] ${err.message}`);
    await closeBrowser();
    process.exit(1);
  }
}

if (import.meta.main) {
  main().catch((err) => {
    console.error(`[browse] ${err.message}`);
    process.exit(1);
  });
}
