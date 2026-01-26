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
├── database-migrations/         # 独立数据库迁移组件
│   ├── migrations/              # Flyway 迁移脚本
│   ├── scripts/                 # 辅助脚本
│   └── docs/                    # 组件文档
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
├── Skills-Collection/           # Skills 示例集合
│   ├── ruoyi-crud/              # CRUD 代码生成
│   ├── code-review/             # 代码审查助手
│   ├── api-doc/                 # API 文档生成器
│   ├── test-gen/                # 单元测试生成器
│   ├── sql-optimizer/           # SQL 优化建议
│   └── flyway-migration/        # Flyway 数据库迁移管理
├── ruoyi-example/               # Ruoyi 示例项目
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
- **50+ 专业模板**: 需求规格说明书、API 规范、测试用例、部署指南等
- **质量门禁**: 每个阶段都有严格的质量检查点
- **灵活工作流**: 支持完整 SDLC、敏捷 Sprint、Bug 修复等多种场景

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

### Skills-Collection

可重用的技能集合，展示 Claude Code 在实际开发中的应用：

- `ruoyi-crud` - 快速生成 CRUD 代码
- `code-review` - 代码质量审查
- `api-doc` - API 文档生成
- `test-gen` - 单元测试生成
- `sql-optimizer` - SQL 优化建议
- `flyway-migration` - 数据库迁移管理

### database-migrations

独立的数据库迁移组件，使用 Flyway 管理数据库版本。

### docker

Docker Compose 配置，一键启动 MySQL、Redis 等基础服务。

## 参考资源

- [Happy 官网](https://happy.engineering/)
- [智谱 AI 文档](https://docs.bigmodel.cn/)
- [Claude Code 文档](https://code.claude.com/docs)
- [Ruoyi 官网](https://ruoyi.vip/)

## 许可证

MIT
