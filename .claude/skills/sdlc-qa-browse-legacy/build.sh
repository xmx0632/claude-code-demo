#!/bin/bash
# Build script for browse binary
# Usage: ./build.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "Installing dependencies..."
bun install

echo "Compiling browse (original Playwright)..."
mkdir -p dist
bun build ./src/cli.ts --compile --outfile dist/browse

echo "Compiling browse-cdp (CDP-based, macOS 11 compatible)..."
bun build ./src/browse-cdp.ts --compile --outfile dist/browse-cdp

echo "Compiling browse-simple (Chromium CLI only)..."
bun build ./src/browse-simple.ts --compile --outfile dist/browse-simple

echo "Installing Playwright browser..."
bunx playwright install chromium

echo ""
echo "✓ Build complete!"
echo ""
echo "Binaries:"
echo "  $SCRIPT_DIR/dist/browse       - Original (Playwright, requires macOS 12+)"
echo "  $SCRIPT_DIR/dist/browse-cdp   - CDP-based (macOS 11 compatible)"
echo "  $SCRIPT_DIR/dist/browse-simple - Chromium CLI only (lightweight)"
echo ""
ls -lh dist/
