# Claude Code + Happy + glm4.7 实战指南

> Windows + Java Spring Boot + Ruoyi 快速上手

## 简介

本指南面向有 Java 开发经验的开发者，介绍如何：
- 在 Windows 上搭建 Claude Code + glm4.7 开发环境
- 配置 Happy 实现手机远程开发
- 开发自定义 Skills 应用于实际项目
- 在 Java Spring Boot + Ruoyi 项目中实践

## 目录结构

```
claude-code-demo/
├── README.md                    # 本文件
├── docker/                      # Docker 基础服务配置
│   ├── docker-compose.yml       # Docker Compose 配置
│   ├── services/                # 服务配置（MySQL、Redis）
│   ├── app/                     # 应用容器配置
│   └── scripts/                 # 管理脚本
├── SDLC-Framework/              # SDLC 完整工作流框架
│   ├── 01-15*/                  # 15 个开发阶段
│   ├── subagents/               # Subagent 角色定义
│   ├── workflows/               # 工作流编排
│   ├── guides/                  # 框架指南
│   └── config/                  # 配置文件
├── .claude/skills/              # SDLC Skills 集合
│   ├── sdlc-*/                  # 18 个 SDLC 专业技能
│   └── doc-convert/             # 文档转换工具
├── projects/                    # 项目目录
│   ├── ruoyi-example/           # Ruoyi 示例项目
│   └── todolist-sdlc/           # TodoList 完整示例
└── docs/                        # 详细文档
    ├── quick-start.md           # 快速入门
    ├── happy-remote.md          # Happy 远程开发
    ├── skills-guide.md          # Skills 开发指南
    ├── best-practices.md        # 最佳实践
    └── appendix.md              # 附录
```

## 快速开始

1. [环境搭建](./docs/quick-start.md) - 安装和配置 Claude Code + glm4.7
2. [Happy 远程开发](./docs/happy-remote.md) - 手机端配置和使用
3. [Skills 开发](./docs/skills-guide.md) - 创建自定义 Skills
4. [完整示例项目](./docs/ruoyi-example.md) - Ruoyi 项目实战演示
5. [最佳实践](./docs/best-practices.md) - 开发规范和技巧
6. [FAQ](./docs/appendix.md) - 常见问题和参考资料

## SDLC Framework - 完整工作流框架

本项目包含一个生产级的软件开发生命周期（SDLC）框架，提供从需求分析到系统部署的完整开发流程。

### 核心特性

- **15 个开发阶段**: 需求分析 → 产品设计 → 架构设计 → 详细设计 → 数据库迁移 → 代码开发 → 测试 → 验收 → 文档 → 部署 → 升级
- **8 种 Subagent 角色**: 产品经理、架构师、开发工程师、QA 工程师、DevOps、DBA、技术文档工程师
- **18 个 SDLC Skills**: 覆盖完整开发生命周期的专业技能集合
- **50+ 专业模板**: 需求规格说明书、API 规范、测试用例、部署指南等
- **质量门禁**: 每个阶段都有严格的质量检查点
- **灵活工作流**: 支持完整 SDLC、敏捷 Sprint、Bug 修复等多种场景

### SDLC Skills

**新增 Skills**：
- `sdlc-ceo-review` - CEO/创始人视角的计划审视，四种模式（范围扩展/选择性扩展/保持范围/范围缩减）
- `sdlc-qa-browse` - 快速无头浏览器 QA 测试，支持页面交互、截图、响应式布局验证
- `sdlc-qa-report` - 只报告模式的 QA 测试，生成结构化测试报告
- `sdlc-retro` - 周度工程复盘，分析提交历史和代码质量指标

**完整 Skills 列表**（18 个）：需求分析、架构设计、详细设计、代码开发、测试、部署、文档、代码审查、QA 测试、复盘、Mermaid 图表、数据库迁移、API 文档、SQL 优化、测试生成、Ruoyi CRUD、文档转换。

详见 `.claude/skills/` 目录。

### 快速使用

```bash
# 执行完整的 SDLC
/sdlc-full "创建用户认证系统"

# 分阶段执行
/requirements-analysis "用户认证需求"
/architecture-design
/ruoyi-crud sys_user
/test-gen UserService
```

### 详细文档

- [SDLC Framework README](./SDLC-Framework/README.md) - 框架概览
- [快速开始指南](./SDLC-Framework/guides/getting-started.md) - 如何使用框架
- [完整工作流文档](./SDLC-Framework/workflows/full-sdlc-workflow.md) - 15 个阶段详解

## 项目组件

### database-migrations

数据库迁移组件位于 `projects/ruoyi-example/database-migrations/`，使用 Flyway 管理数据库版本。

### docker

Docker Compose 配置，一键启动 MySQL、Redis 等基础服务。

## 参考资源

- [Happy 官网](https://happy.engineering/)
- [智谱 AI 文档](https://docs.bigmodel.cn/)
- [Claude Code 文档](https://code.claude.com/docs)
- [Ruoyi 官网](https://ruoyi.vip/)

## 致谢

本项目在开发过程中参考和使用了以下优秀的开源项目：

- **[gstack](https://github.com/garrytan/gstack)** - 感谢 garrytan 开发的 gstack 项目，本项目的 `sdlc-qa-browse` skill 基于 gstack 的 browse 工具进行了本地化适配和扩展，为 QA 测试提供了强大的无头浏览器支持。

感谢所有为开源社区做出贡献的开发者！

## 许可证

GPL-3.0 license
