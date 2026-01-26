# SDLC Framework 辅助脚本

本目录包含 SDLC Framework 的辅助脚本，用于简化日常开发工作流程。

## 脚本列表

### 1. start-feature.sh

功能开发启动脚本 - 从 main 分支创建新的功能分支。

**用法**:
```bash
./scripts/start-feature.sh <feature-name>
```

**示例**:
```bash
./scripts/start-feature.sh user-authentication
./scripts/start-feature.sh payment-integration
./scripts/start-feature.sh order-management
```

**功能**:
- ✅ 检查未提交的更改
- ✅ 切换到 main 分支并拉取最新代码
- ✅ 创建 `feature/<feature-name>` 分支
- ✅ 自动切换到新分支
- ✅ 显示下一步操作提示

**输出示例**:
```
ℹ 正在从 main 分支创建功能分支...
ℹ 切换到 main 分支...
ℹ 拉取 main 分支最新代码...
ℹ 创建功能分支: feature/user-authentication
✓ 功能分支创建成功！

分支名称: feature/user-authentication
当前分支: feature/user-authentication

下一步：
  1. 开始开发功能
  2. 提交代码: git add . && git commit -m 'feat: 描述'
  3. 推送到远程: git push -u origin feature/user-authentication
  4. 完成功能后运行: ./scripts/finish-feature.sh
```

---

### 2. finish-feature.sh

功能开发完成脚本 - 推送代码并创建 Pull Request。

**用法**:
```bash
./scripts/finish-feature.sh
```

**功能**:
- ✅ 验证当前在功能分支上
- ✅ 检查未提交的更改
- ✅ 推送代码到远程仓库
- ✅ 自动创建 Pull Request（需安装 GitHub CLI）
- ✅ 生成标准 PR 标题和描述模板

**依赖**:
- GitHub CLI (gh): https://cli.github.com/

**安装 GitHub CLI**:
```bash
# macOS
brew install gh

# Linux
# 参考 https://cli.github.com/
```

**登录 GitHub**:
```bash
gh auth login
```

**输出示例**:
```
ℹ 推送分支到远程仓库...
✓ 推送成功
ℹ 创建 Pull Request...

✓ Pull Request 创建成功！

下一步：
  1. 等待代码审查
  2. 根据反馈修改代码
  3. 审查通过后合并 PR
  4. 合并后删除功能分支: git checkout main && git branch -d feature/user-authentication
```

**未安装 gh CLI 时**:
脚本会提示手动创建 PR 的步骤和链接。

---

### 3. update-main.sh

更新 main 分支脚本 - 切换到 main 并拉取最新代码。

**用法**:
```bash
./scripts/update-main.sh
```

**功能**:
- ✅ 保存当前分支状态
- ✅ 检查未提交的更改
- ✅ 切换到 main 分支
- ✅ 拉取最新代码
- ✅ 显示返回原分支的命令

**使用场景**:
- 开始新功能前确保 main 是最新的
- 定期同步 main 分支的变更

**输出示例**:
```
ℹ 从 feature/user-authentication 切换到 main 分支...
ℹ 拉取 main 分支最新代码...
✓ main 分支已更新到最新

当前分支: main

下一步：
  1. 返回原分支: git checkout feature/user-authentication
  2. 或创建新功能分支: ./scripts/start-feature.sh <feature-name>
  3. 或使用便利脚本: git checkout -
```

---

## 完整工作流程示例

### 场景 1: 开发新功能

```bash
# 1. 确保主分支是最新
./scripts/update-main.sh

# 2. 创建功能分支
./scripts/start-feature.sh user-authentication

# 3. 开发功能...
# 编写代码、测试、提交
git add .
git commit -m "feat: 添加用户登录功能"

# 4. 推送并创建 PR
./scripts/finish-feature.sh

# 5. 等待审查...

# 6. 审查通过后，合并 PR（在 GitHub 上操作）

# 7. 清理本地分支
git checkout main
git pull origin main
git branch -d feature/user-authentication
```

### 场景 2: 修复 Bug

```bash
# 1. 更新 main
./scripts/update-main.sh

# 2. 创建修复分支（手动）
git checkout -b fix/login-error-001

# 3. 修复 Bug...
git add .
git commit -m "fix: 修复登录页面的验证错误"

# 4. 推送
git push -u origin fix/login-error-001

# 5. 在 GitHub 创建 PR（手动或使用 gh pr create）

# 6. 合并后清理
git checkout main
git pull origin main
git branch -d fix/login-error-001
```

### 场景 3: 紧急修复（Hotfix）

```bash
# 1. 从 main 创建 hotfix 分支
git checkout main
git pull origin main
git checkout -b hotfix/security-patch-001

# 2. 修复问题...
git add .
git commit -m "hotfix: 修复安全漏洞"

# 3. 快速审查和合并
git push -u origin hotfix/security-patch-001
# 在 GitHub 加急审查和合并

# 4. 立即部署
```

---

## Git 工作流程规范

### 分支命名规范

| 类型 | 命名格式 | 示例 |
|------|----------|------|
| 功能开发 | `feature/<name>` | `feature/user-authentication` |
| Bug 修复 | `fix/<name>` | `fix/login-error-001` |
| 紧急修复 | `hotfix/<name>` | `hotfix/security-patch-001` |
| 发布分支 | `release/<version>` | `release/v1.0.0` |

### 提交规范

遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<类型>[可选的作用域]: <描述>

[可选的正文]

[可选的脚注]
```

**类型**:
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式
- `refactor`: 重构
- `perf`: 性能优化
- `test`: 测试
- `chore`: 构建/工具

**示例**:
```bash
git commit -m "feat(auth): 添加用户登录功能"
git commit -m "fix(payment): 修复支付超时问题"
git commit -m "docs(readme): 更新安装说明"
```

### 分支保护规则

**main 分支**:
- ✅ 禁止直接推送
- ✅ 只能通过 PR 合并
- ✅ 至少 1 个审查批准
- ✅ 所有 CI 检查通过
- ✅ 解决所有审查评论

---

## 脚本依赖

### 必需
- **Git**: 版本控制工具
- **Bash**: Unix shell（macOS/Linux 自带）

### 可选
- **GitHub CLI (gh)**: 用于自动创建 PR
  - 安装: `brew install gh` (macOS)
  - 文档: https://cli.github.com/

---

## 故障排除

### 问题 1: 脚本无法执行

**错误**: `Permission denied: ./scripts/start-feature.sh`

**解决**:
```bash
chmod +x ./scripts/*.sh
```

### 问题 2: gh CLI 未安装

**错误**: `gh: command not found`

**解决**: 安装 GitHub CLI
```bash
# macOS
brew install gh

# 登录
gh auth login
```

或者手动在 GitHub 网页创建 PR。

### 问题 3: 分支已存在

**错误**: `分支 feature/xxx 已存在`

**解决**:
```bash
# 切换到现有分支
git checkout feature/xxx

# 或删除后重新创建（注意：会丢失未推送的提交）
git branch -D feature/xxx
./scripts/start-feature.sh xxx
```

### 问题 4: 未提交的更改

**警告**: `检测到未提交的更改`

**解决**:
```bash
# 方案 1: 提交更改
git add .
git commit -m "描述"

# 方案 2: 暂存更改
git stash
# [执行操作]
git stash pop

# 方案 3: 放弃更改
git checkout -- .
```

---

## 最佳实践

1. **频繁提交**: 小步快跑，频繁提交代码
2. **原子提交**: 每次提交只做一件事
3. **清晰描述**: 提交信息清晰描述变更内容
4. **及时同步**: 定期从 main 拉取最新代码
5. **保持简洁**: 不要在功能分支上停留太久
6. **代码审查**: 认真对待审查反馈
7. **及时清理**: 合并后及时删除已完成的分支

---

## 相关文档

- [Git 工作流程规范](../guides/git-workflow.md)
- [完整 SDLC 工作流](../workflows/full-sdlc-workflow.md)
- [框架使用指南](../guides/getting-started.md)
