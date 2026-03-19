#!/bin/bash
# Setup script for sdlc-qa-puppeteer
# Usage: ./setup.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "========================================="
echo "sdlc-qa-puppeteer Setup"
echo "========================================="
echo ""

# Check if bun is installed
if ! command -v bun &> /dev/null; then
  echo "Bun not found. Installing Bun..."
  curl -fsSL https://bun.sh/install | bash -s "bun-v1.0.2"
  export PATH="$HOME/.bun/bin:$PATH"
fi

echo "1. Installing dependencies (puppeteer 13.7.0)..."
bun install

echo ""
echo "2. Installing Chromium (via Playwright)..."
bunx playwright install chromium

echo ""
echo "3. Verifying Chromium installation..."
CHROMIUM_PATH="/Users/xmx0632/Library/Caches/ms-playwright/chromium-1019/chrome-mac/Chromium.app"
if [ -d "$CHROMIUM_PATH" ]; then
  echo "✓ Chromium found at: $CHROMIUM_PATH"
else
  echo "✗ Chromium not found. Installing..."
  bunx playwright install chromium
fi

echo ""
echo "4. Making browse wrapper executable..."
chmod +x browse

echo ""
echo "✓ Setup complete!"
echo ""
echo "Binary: $SCRIPT_DIR/browse"
echo ""
echo "Test with:"
echo "  ./browse help"
echo "  ./browse goto https://example.com"
echo "  ./browse screenshot /tmp/test.png"
echo "  ./browse js \"document.title\""
