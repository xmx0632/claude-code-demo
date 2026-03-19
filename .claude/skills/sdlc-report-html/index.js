#!/usr/bin/env node

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Parse arguments
const args = process.argv.slice(2);
let markdownFile = '';
let outputFile = '';
let imgDir = '';
let title = '测试报告';

for (let i = 0; i < args.length; i++) {
  switch (args[i]) {
    case '--output':
      outputFile = args[++i];
      break;
    case '--img-dir':
      imgDir = args[++i];
      break;
    case '--title':
      title = args[++i];
      break;
    default:
      if (!args[i].startsWith('-')) {
        markdownFile = args[i];
      }
  }
}

if (!markdownFile) {
  console.error('Usage: sdlc-report-html <markdown-file> [--output html-file] [--img-dir dir] [--title title]');
  process.exit(1);
}

// Resolve paths
const markdownPath = path.resolve(markdownFile);
const reportDir = path.dirname(markdownPath);
const imageDir = imgDir ? path.resolve(imgDir) : reportDir;

// Default output file
if (!outputFile) {
  const basename = path.basename(markdownFile, path.extname(markdownFile));
  outputFile = path.join(reportDir, `${basename}.html`);
}
const outputPath = path.resolve(outputFile);

// Read markdown content
let markdown = fs.readFileSync(markdownPath, 'utf-8');

// Convert markdown to HTML
function markdownToHTML(md) {
  let html = md;

  // Escape HTML special characters first (for code blocks content)
  html = html.replace(/</g, '&lt;');
  html = html.replace(/>/g, '&gt;');

  // Headers (must be done after escaping)
  html = html.replace(/^###### (.*$)/gim, '<h6>$1</h6>');
  html = html.replace(/^##### (.*$)/gim, '<h5>$1</h5>');
  html = html.replace(/^#### (.*$)/gim, '<h4>$1</h4>');
  html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>');
  html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>');
  html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>');

  // Bold and Italic
  html = html.replace(/\*\*\*(.*?)\*\*\*/g, '<strong><em>$1</em></strong>');
  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
  html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');

  // Code blocks (before inline code to avoid conflicts)
  html = html.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, lang, code) => {
    // Unescape code content
    const unescaped = code.replace(/&lt;/g, '<').replace(/&gt;/g, '>');
    return `<pre><code class="language-${lang || ''}">${unescaped}</code></pre>`;
  });

  // Inline code
  html = html.replace(/`(.*?)`/g, '<code>$1</code>');

  // Images (must be before links to avoid conflicts)
  html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, (match, alt, imgPath) => {
    return `<img src="${imgPath}" alt="${alt}" class="markdown-img">`;
  });

  // Links
  html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank">$1</a>');

  // Unordered lists
  html = html.replace(/^\- (.*$)/gim, '<li>$1</li>');
  // Wrap consecutive lis in ul
  html = html.replace(/(<li>.*<\/li>\n?)+/g, '<ul>$&</ul>');

  // Ordered lists
  html = html.replace(/^\d+\. (.*$)/gim, '<li>$1</li>');

  // Tables
  const lines = html.split('\n');
  let inTable = false;
  let tableRows = [];
  let result = [];

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i].trim();
    if (line.startsWith('|') && line.endsWith('|')) {
      if (!inTable) {
        inTable = true;
        tableRows = [];
      }
      // Skip separator line
      if (!line.match(/^\|[\s\-:]+\|[\s\-:]+\|/)) {
        const cells = line.slice(1, -1).split('|').map(c => c.trim());
        tableRows.push(cells);
      }
    } else {
      if (inTable) {
        result.push(convertTableToHTML(tableRows));
        tableRows = [];
        inTable = false;
      }
      result.push(lines[i]);
    }
  }
  if (inTable) {
    result.push(convertTableToHTML(tableRows));
  }
  html = result.join('\n');

  // Horizontal rule
  html = html.replace(/^---$/gim, '<hr>');

  // Blockquotes
  html = html.replace(/^&gt; (.*$)/gim, '<blockquote>$1</blockquote>');

  // Line breaks and paragraphs
  html = html.replace(/\n\n+/g, '</p><p>');
  html = html.replace(/\n/g, '<br>');

  return html;
}

function convertTableToHTML(rows) {
  if (rows.length === 0) return '';
  let html = '<table>';
  rows.forEach((row, index) => {
    const tag = index === 0 ? 'th' : 'td';
    html += '<tr>';
    row.forEach(cell => {
      html += `<${tag}>${cell}</${tag}>`;
    });
    html += '</tr>';
  });
  html += '</table>';
  return html;
}

// Convert images to base64
function embedImages(html, baseDir) {
  return html.replace(/<img\s+src="([^"]+)"\s+alt="([^"]*)"[^>]*>/g, (match, imagePath, alt) => {
    const fullPath = path.join(baseDir, imagePath);
    if (fs.existsSync(fullPath)) {
      try {
        const ext = path.extname(imagePath).toLowerCase().slice(1);
        const imageData = fs.readFileSync(fullPath);
        const base64 = imageData.toString('base64');
        const mimeType = {
          'png': 'image/png',
          'jpg': 'image/jpeg',
          'jpeg': 'image/jpeg',
          'gif': 'image/gif',
          'webp': 'image/webp',
          'svg': 'image/svg+xml'
        }[ext] || 'image/png';
        return `<img src="data:${mimeType};base64,${base64}" alt="${alt}" style="max-width: 100%; height: auto; margin: 10px 0; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">`;
      } catch (err) {
        console.error(`Failed to read image: ${fullPath}`, err.message);
        return `<p style="color: red;">[图片加载失败: ${imagePath}]</p>`;
      }
    }
    return `<p style="color: orange;">[图片不存在: ${imagePath}]</p>`;
  });
}

// Generate complete HTML
function generateHTML(content, title) {
  const date = new Date().toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' });
  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
            line-height: 1.8;
            color: #333;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 50px;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        h1 {
            color: #2c3e50;
            border-bottom: 4px solid #667eea;
            padding-bottom: 15px;
            margin-bottom: 30px;
            font-size: 2.2em;
        }
        h2 {
            color: #34495e;
            margin-top: 40px;
            margin-bottom: 20px;
            border-left: 5px solid #667eea;
            padding-left: 20px;
            font-size: 1.8em;
        }
        h3 {
            color: #555;
            margin-top: 30px;
            margin-bottom: 15px;
            font-size: 1.4em;
        }
        h4, h5, h6 {
            color: #666;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        p {
            margin: 15px 0;
            text-align: justify;
        }
        code {
            background: #f4f4f4;
            padding: 3px 8px;
            border-radius: 4px;
            font-family: "Monaco", "Menlo", "Consolas", monospace;
            font-size: 0.9em;
            color: #e74c3c;
        }
        pre {
            background: #2d2d2d;
            color: #f8f8f2;
            padding: 20px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 20px 0;
            border-left: 4px solid #667eea;
        }
        pre code {
            background: transparent;
            padding: 0;
            color: #f8f8f2;
        }
        ul, ol {
            margin: 15px 0;
            padding-left: 30px;
        }
        li {
            margin: 8px 0;
            line-height: 1.6;
        }
        img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            margin: 20px 0;
            display: block;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        th, td {
            border: 1px solid #ddd;
            padding: 15px;
            text-align: left;
        }
        th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            font-weight: 600;
        }
        tr:nth-child(even) {
            background: #f9f9f9;
        }
        tr:hover {
            background: #f0f0f0;
        }
        a {
            color: #667eea;
            text-decoration: none;
            border-bottom: 1px dotted #667eea;
        }
        a:hover {
            color: #764ba2;
            border-bottom-style: solid;
        }
        blockquote {
            border-left: 4px solid #667eea;
            padding-left: 20px;
            margin: 20px 0;
            color: #666;
            font-style: italic;
            background: #f9f9f9;
            padding: 15px;
            border-radius: 0 8px 8px 0;
        }
        hr {
            border: none;
            height: 2px;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            margin: 40px 0;
        }
        .status-pass {
            color: #27ae60;
            font-weight: bold;
        }
        .status-fail {
            color: #e74c3c;
            font-weight: bold;
        }
        .status-warning {
            color: #f39c12;
            font-weight: bold;
        }
        .footer {
            margin-top: 50px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            text-align: center;
            color: #999;
            font-size: 0.9em;
        }
        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }
            h1 {
                font-size: 1.8em;
            }
            h2 {
                font-size: 1.5em;
            }
            table {
                font-size: 0.9em;
            }
            th, td {
                padding: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        ${content}
        <div class="footer">
            <p>生成时间: ${date}</p>
        </div>
    </div>
</body>
</html>`;
}

// Main execution
try {
  console.log(`Reading markdown: ${markdownPath}`);
  console.log(`Image directory: ${imageDir}`);
  console.log(`Output file: ${outputPath}`);

  let htmlContent = markdownToHTML(markdown);
  htmlContent = embedImages(htmlContent, imageDir);
  const fullHTML = generateHTML(htmlContent, title);

  fs.writeFileSync(outputPath, fullHTML, 'utf-8');

  const stats = fs.statSync(outputPath);
  console.log(`\n✅ HTML report generated successfully!`);
  console.log(`   File: ${outputPath}`);
  console.log(`   Size: ${(stats.size / 1024).toFixed(2)} KB`);
} catch (err) {
  console.error(`Error: ${err.message}`);
  process.exit(1);
}
