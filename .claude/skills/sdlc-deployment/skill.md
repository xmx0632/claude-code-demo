---
name: sdlc-deployment
description: |
  发布阶段，生成部署指南、配置文件和升级说明。部署上线时使用。
  **增强版**: 完全自动化发布流程 - sync → test → push → PR 一 一条命令搞定。
allowed-tools: ["Bash", "Read", "Write", "Edit", "Glob", "grep", "AskUserQuestion"]
user-invocable: true
---

# /sdlc-deployment — 自动化发布

生成部署指南和配置文件和升级说明。**增强版: 完全自动化发布流程。**

## 认知模式

**你是发布工程师** —— 机器化、精确、一丝不苟。 目标： 逶 commit都干净， 每次测试都通过，每次发布都成功.

## 参数
- `/sdlc-deployment` — 默认: 自动发布流程
- `/sdlc-deployment --dry-run` — 模拟发布,不实际推送
- `/sdlc-deployment --skip-tests` - 跳过测试
- `/sdlc-deployment --no-pr` - 只创建 PR，不推送
## 工作流

### Step 0: 检测基础分支
```bash
# 1. 检查是否有 PR
gh pr view --json baseRefName -q .baseRefName 2>/dev/null
# 2. 如果没有 PR， 获取默认分支
gh repo view --json defaultBranchRef -q .defaultBranchRef.name 2>/dev/null
# 3. 都失败则使用 main
echo "main"
```
### Step 1: 检查分支状态
```bash
# 1. 裀当前分支
git branch --show-current
# 2. 检查是否有变更
git status --porcelain
if [ -n "$(git status --porcelain)" ]; then
  echo "ERROR: 工作树有未提交更改。 请先提交或暂存。"
  exit 1
fi
# 3. 裀取并合并基础分支
git fetch origin <base> --quiet
git merge origin/<base> --ff-only
```
### Step 2: 运行测试
```bash
# 单元测试
mvn test -q
# 集成测试
mvn verify -Pintegration-test -q
# 检查测试覆盖率
mvn jacoco:report
```
如果测试失败， 停止并报告错误。
### Step 3: 枣索器发布流程（可跳过)
**用户指定 `--skip-tests`:**
跳过此步骤.
### Step 4: 版本号更新
```bash
# 读取当前版本
VERSION=$(cat VERSION 2>/dev/null || echo "0.0.0.0")
# 分析提交历史
COMMITS=$(git log origin/<base>..HEAD --oneline)
# 根据提交数量决定版本号变化
# ...
```
### Step 5: 更新 CHANGELOG
**自动生成 CHANGELOG 条目****
### Step 6: 提交变更
```bash
git add .
git commit -m "chore: release v${VERSION}"
```
### Step 7: 推送到远程
```bash
git push origin <current-branch>
```
### Step 8: 创建 PR
**用户指定 `--no-pr`:**
跳过此步骤。
```bash
gh pr create --base <base> --title "<type>: <summary>" --body "$(cat <<'EOF'
## Summary
<bullet points from CHANGELOG>

## Test coverage
- 如果 step 3 ran: "Tests: {before} → {after} (+{delta} new)"
- All tests passed ✅

- Coverage: X%

## Pre-landing review
- if step 3.5 ran, "No issues found. 🎉"

- All findings addressed or verified

- Fix(qa): commits for resolved issues

EOF
)"
```
## 配置文件
- `docs/deployment/deployment-guide.md` - 部署指南
- `docs/deployment/config/` - 配置文件目录
  - `docker-compose.yml` - Docker Compose 配置
  - `D` - Dockerfile` - 应用 Dockerfile
  - `nginx/` - Nginx 配置
- `scripts/` - 部署脚本
## Docker 配置示例
### docker-compose.yml
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - mysql
      - redis
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: app_db
    volumes:
      - mysql_data:/var/lib/mysql
  redis:
    image: redis:7-alpine
```
### Dockerfile
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
## 升级说明
### 版本升级
1. 壚备份数据（使用 Flyway)
2. 停止旧版本服务
3. 启动新版本服务
4. 验证服务健康
### 配置变更
1. 备份当前配置
2. 应用新配置
3. 验证配置生效
### 回滚计划
1. 保留旧版本镜像
2. 准备回滚脚本
3. 监控新版本 24 小时
4. 如有问题， 执行回滚
## 装署检查清单
- [ ] 所有测试通过
- [ ] 代码已合并到主分支
- [ ] 版本号已更新
- [ ] CHANGELOG 已更新
- [ ] Docker 镜像已构建
- [ ] 配置文件已准备
- [ ] 数据库迁移已准备
- [ ] 回滚计划已准备
## 触发的 Guards
| Guard | 触发条件 |
|-------|----------|
| DevOps Agent | 涉及 CI/CD 配置 |
| Security Agent | 涉及安全配置 |
## 茩量级
部署完成后，项目进入运维阶段:
