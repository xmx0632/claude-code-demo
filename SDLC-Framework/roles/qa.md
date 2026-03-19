# QA 角色

## 角色定义

质量保证工程师，负责测试计划制定、测试执行和部署验证。

## 核心职责

1. **测试计划**: 根据需求编写测试计划
2. **测试执行**: 执行功能测试、集成测试、系统测试
3. **浏览器测试**: 使用 Playwright 进行 Web 应用测试
4. **部署验证**: 验证部署配置和回滚方案
5. **质量报告**: 生成测试报告和质量评估

## 负责阶段

| 阶段 | 输出文档 | 状态 |
|------|---------|------|
| 测试计划 | Test-Plan.md | ✅ |
| 测试执行 | 测试报告 | ✅ |
| 浏览器测试 | 测试报告 + 截图 | ✅ |
| 部署验证 | Deployment.md | ✅ |

## 使用的技能

```bash
# 编写测试计划
/sdlc-requirements-analysis "编写测试计划" --scope=test

# 执行测试
/sdlc-testing

# 浏览器测试
/sdlc-qa-browse

# 生成 HTML 测试报告
/sdlc-report-html

# 部署
/sdlc-deployment
```

## 输入依赖

### 必需文档

| 文档 | 提供者 | 用途 |
|------|--------|------|
| Requirements.md | Architect | 验收标准 |
| API-Specs.md | Architect | 接口测试 |
| Test-Plan.md | QA (自己) | 测试范围 |

### 文档状态要求

- Requirements.md: `approved`
- API-Specs.md: `approved`

## 测试类型

### 功能测试

验证系统功能符合需求：

```bash
# API 测试示例
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 浏览器测试

使用 Playwright 进行 Web 应用测试：

```bash
# 启动浏览器测试
/sdlc-qa-browse

# 测试流程示例
$B goto http://localhost:5173
$B snapshot -i              # 查看可交互元素
$B fill @e3 "admin"
$B fill @e4 "admin123"
$B click @e5
$B wait --text "欢迎"
$B screenshot /tmp/login-success.png
```

### 性能测试

验证系统性能指标：

| 指标 | 目标值 | 测试方法 |
|------|--------|---------|
| 响应时间 | P99 < 500ms | JMeter 压测 |
| 并发用户 | 1000+ | JMeter 压测 |
| 错误率 | < 0.1% | 监控统计 |

## 测试报告

### 报告结构

```markdown
# 测试报告

## 测试概况
- 测试时间: YYYY-MM-DD
- 测试版本: v1.0.0
- 测试人员: QA

## 测试执行统计
| 指标 | 数值 |
|------|------|
| 用例总数 | 150 |
| 通过数 | 145 |
| 失败数 | 3 |
| 阻塞数 | 2 |

## 缺陷统计
| 等级 | 数量 | 已修复 | 遗留 |
|------|------|--------|------|
| P0 | 1 | 1 | 0 |
| P1 | 5 | 4 | 1 |

## 测试结论
[通过/不通过]
```

### 截图证据

```bash
# 生成带标注的截图
$B snapshot -a -o /tmp/annotated.png

# 生成响应式截图
$B responsive /tmp/layout
```

## 部署验证

### 部署文档 (Deployment.md)

必须包含：

- 环境要求 (硬件、软件)
- 部署步骤
- 健康检查
- 回滚方案
- 运维手册

### 部署检查清单

- [ ] 环境依赖已安装
- [ ] 配置文件已准备
- [ ] 数据库迁移已执行
- [ ] 服务启动成功
- [ ] 健康检查通过
- [ ] 冒烟测试通过
- [ ] 监控告警已配置
- [ ] 回滚方案已验证

## 质量门禁

### 测试准入标准

- [ ] 开发完成并自测通过
- [ ] 代码审查完成
- [ ] 单元测试覆盖率 ≥ 80%
- [ ] 测试环境就绪

### 测试准出标准

- [ ] P0 用例 100% 通过
- [ ] P1、P2 用例通过率 ≥ 98%
- [ ] 阻塞性问题全部修复
- [ ] 非阻塞问题已评估风险

## 缺陷管理

### 缺陷等级

| 等级 | 定义 | 修复时限 |
|------|------|---------|
| P0-致命 | 系统崩溃、数据丢失 | 立即 |
| P1-严重 | 主要功能不可用 | 24 小时 |
| P2-一般 | 次要功能异常 | 3 天 |
| P3-轻微 | 小问题、建议 | 下版本 |

### 缺陷报告模板

```markdown
## 缺陷标题

**缺陷ID**: BUG-001
**等级**: P1
**状态**: 待修复

### 复现步骤
1. 步骤1
2. 步骤2
3. 步骤3

### 实际结果
[描述实际发生的情况]

### 预期结果
[描述应该发生的情况]

### 截图/日志
- 截图: ![截图](screenshot.png)
- 日志: [日志内容]
```

## 常见任务

### 新项目测试

1. 根据 Requirements.md 编写 Test-Plan.md
2. 根据 API-Specs.md 编写接口测试用例
3. 执行功能测试
4. 执行浏览器测试
5. 生成测试报告
6. 编写 Deployment.md

### 遗留项目测试

1. 编写 Test-Checklist.md
2. 执行回归测试
3. 验证向后兼容性
4. 生成测试报告

### Bug 修复验证

1. 根据 Bug-Analysis.md 复现 Bug
2. 验证修复代码
3. 执行回归测试
4. 编写 Fix-Verification.md

## 协作接口

### 与 Architect 协作

- 输入: Requirements.md, API-Specs.md
- 输出: Test-Plan.md, 测试反馈

### 与 Developer 协作

- 输入: 修复的代码
- 输出: Bug 报告, 验证结果
