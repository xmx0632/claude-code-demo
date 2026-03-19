#!/bin/bash
# Build script for browse binary
# Usage: ./build.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "Installing dependencies..."
bun install

echo "Compiling binary..."
mkdir -p dist
bun build ./src/cli.ts --compile --outfile dist/browse

echo "Installing Playwright browser..."
bunx playwright install chromium

echo ""
echo "✓ Build complete!"
echo "Binary: $SCRIPT_DIR/dist/browse"
ls -lh dist/browse
