#!/usr/bin/env bun
/**
 * browse-hybrid - Hybrid browser for macOS 11 compatibility
 *
 * Uses:
 * - chromium CLI for screenshots
 * - State file for session tracking
 * - Shell commands for basic operations
 *
 * This is a simplified, reliable approach that works on macOS 11.
 */

import * as fs from 'fs';
import * as path from 'path';
import { spawn } from 'child_process';

// ─── Config ─────────────────────────────────────────────────────────────
const STATE_DIR = process.env.BROWSE_STATE_DIR || path.join(process.env.HOME || '', '.gstack');
const STATE_FILE = path.join(STATE_DIR, 'browse-hybrid.json');

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
  historyIndex: number;
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
      historyIndex: -1,
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
      '--hide-scrollbars',
      '--mute-audio',
      `--window-size=${width},${height}`,
      '--screenshot=' + outputPath,
      '--virtual-time-budget=2000',
      url,
    ];

    const proc = spawn(chromium, args, {
      stdio: ['ignore', 'pipe', 'pipe'],
    });

    let stderr = '';
    let stdout = '';
    proc.stdout?.on('data', (data) => {
      stdout += data.toString();
    });
    proc.stderr?.on('data', (data) => {
      stderr += data.toString();
    });

    proc.on('close', (code) => {
      if (code === 0 && fs.existsSync(outputPath)) {
        resolve(outputPath);
      } else {
        reject(new Error(`Screenshot failed (exit ${code}): ${stderr || stdout || 'unknown error'}`));
      }
    });

    setTimeout(() => {
      proc.kill('SIGTERM');
      reject(new Error(`Screenshot timeout after ${timeout}ms`));
    }, timeout);
  });
}

// ─── Commands ───────────────────────────────────────────────────────────

// goto <url> - Navigate to URL
async function cmdGoto(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: goto <url>');
    process.exit(1);
  }

  const url = normalizeUrl(args[0]);
  const state = readState();

  // Update state
  state.currentUrl = url;
  state.historyIndex++;
  state.history = state.history.slice(0, state.historyIndex);
  state.history.push(url);
  state.lastCommand = 'goto';
  writeState(state);

  console.log(`Navigated to: ${url}`);
}

// screenshot [path] - Take screenshot
async function cmdScreenshot(args: string[]): Promise<void> {
  const state = readState();
  let outputPath = args[0];

  if (!outputPath) {
    const timestamp = Date.now();
    fs.mkdirSync(STATE_DIR, { recursive: true });
    outputPath = path.join(STATE_DIR, `screenshot-${timestamp}.png`);
  } else {
    outputPath = path.resolve(process.cwd(), outputPath);
  }

  if (state.currentUrl === 'about:blank') {
    console.error('No URL to screenshot. Use "goto <url>" first.');
    process.exit(1);
  }

  try {
    await screenshot({
      url: state.currentUrl,
      outputPath,
    });
    console.log(`Screenshot saved: ${outputPath}`);
  } catch (err: any) {
    console.error(`Screenshot failed: ${err.message}`);
    process.exit(1);
  }
}

// snap <url> [path] - Goto and screenshot in one command
async function cmdSnap(args: string[]): Promise<void> {
  if (args.length === 0) {
    console.error('Usage: snap <url> [path]');
    process.exit(1);
  }

  const url = normalizeUrl(args[0]);
  let outputPath = args[1];

  if (!outputPath) {
    const timestamp = Date.now();
    fs.mkdirSync(STATE_DIR, { recursive: true });
    outputPath = path.join(STATE_DIR, `snap-${timestamp}.png`);
  } else {
    outputPath = path.resolve(process.cwd(), outputPath);
  }

  // Update state
  const state = readState();
  state.currentUrl = url;
  state.historyIndex++;
  state.history = state.history.slice(0, state.historyIndex);
  state.history.push(url);
  state.lastCommand = 'snap';
  writeState(state);

  console.log(`Snap: ${url}`);

  try {
    await screenshot({
      url,
      outputPath,
    });
    console.log(`Screenshot saved: ${outputPath}`);
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
  console.log(`Current URL: ${state.currentUrl}`);
  console.log(`History: ${state.history.length} pages`);
  console.log(`Last command: ${state.lastCommand || 'none'}`);

  // Check if chromium is available
  try {
    const chromium = findChromium();
    console.log(`Chromium: ${chromium}`);
  } catch {
    console.log('Chromium: not found');
  }
}

// history - Show navigation history
function cmdHistory(): void {
  const state = readState();
  if (state.history.length === 0) {
    console.log('No history');
    return;
  }

  state.history.forEach((url, i) => {
    const prefix = i === state.historyIndex ? '>' : ' ';
    console.log(`${prefix} ${i + 1}. ${url}`);
  });
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

// help - Show help
function cmdHelp(): void {
  console.log(`browse-hybrid - Chromium CLI browser for macOS 11

Navigation:
  goto <url>          Navigate to URL (state only, no browser launch)
  url                 Show current URL
  status              Show current state
  history             Show navigation history
  clear               Clear state

Screenshots:
  screenshot [path]   Take screenshot of current URL
                     Default: ~/.gstack/screenshot-<timestamp>.png
  snap <url> [path]   Navigate and screenshot in one command

Help:
  help                Show this message

Examples:
  browse-hybrid goto https://example.com
  browse-hybrid screenshot /tmp/output.png
  browse-hybrid snap https://example.com /tmp/example.png
  browse-hybrid status`);
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
      case 'shot':
        await cmdScreenshot(commandArgs);
        break;
      case 'snap':
        await cmdSnap(commandArgs);
        break;
      case 'url':
        cmdUrl();
        break;
      case 'status':
        cmdStatus();
        break;
      case 'history':
        cmdHistory();
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
        console.error('Run "browse-hybrid help" for usage');
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
