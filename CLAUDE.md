# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Demonstration project for **Claude Code + Skills** focused on enterprise Java development using the **Ruoyi Framework** (Spring Boot 3.2.0 with Java 17).

**Structure**:
- **SDLC-Framework**: Framework resources (roles, workflows, guards, config) - see [SDLC-Framework/README.md](SDLC-Framework/README.md)
- **.claude/skills/**: Reusable skills for development tasks
- **ruoyi-example**: Sample Spring Boot application

## Core Skills

All skills are invoked via `/sdlc-{skill-name}`:

| Skill | Purpose |
|-------|---------|
| `sdlc-requirements-analysis` | 需求分析，生成需求规格说明书 |
| `sdlc-architecture-design` | 系统架构设计，技术选型 |
| `sdlc-detailed-design` | 详细设计，API/数据模型 |
| `sdlc-code-development` | 代码开发 |
| `sdlc-testing` | 测试 |
| `sdlc-code-review` | 代码审查 |
| `sdlc-ruoyi-crud` | 快速生成 CRUD 代码 |
| `sdlc-flyway-migration` | 数据库迁移 |
| `ui-ux-pro-max` | UI/UX 设计 |

## Common Commands

### Ruoyi Example

```bash
cd ruoyi-example
mvn clean install        # Build
mvn spring-boot:run      # Run (port 8080)
mvn test                 # Test
```

### Docker

```bash
cd docker
docker-compose up -d     # Start MySQL, Redis
docker-compose down      # Stop
```

## Conventions

- **Language**: Chinese for user-facing documentation
- **Code**: Java 17, Lombok, MyBatis-Plus, RESTful APIs
- **Migrations**: Flyway `V{version}__{description}.sql`

## Framework Resources

Located in `SDLC-Framework/`:
- `roles/` - Subagent role definitions
- `workflows/` - Workflow patterns
- `guards/` - Security/performance constraints
- `guidance/` - Constitutional docs

详见 [SDLC-Framework/README.md](SDLC-Framework/README.md)
