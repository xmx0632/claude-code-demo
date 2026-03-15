# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a demonstration project for **Claude Code + Skills + Subagents** focused on enterprise Java development using the **Ruoyi Framework** (Spring Boot 3.2.0 with Java 17). It showcases how to extend Claude Code with custom skills and structured workflows for production development.

The project consists of:
- **SDLC-Framework**: Complete 15-stage software development lifecycle framework with 8 subagent roles
- **Skills-Collection**: Reusable skills for common development tasks (CRUD generation, code review, testing, etc.)
- **ruoyi-example**: Sample Spring Boot application demonstrating the framework
- **database-migrations**: Flyway-based database migration utilities
- **docker**: Docker Compose setup for MySQL 8.0 and Redis 7

## Common Commands

### Ruoyi Example Application (ruoyi-example/)

```bash
# Build the project
cd ruoyi-example
mvn clean install

# Run the application
mvn spring-boot:run
# Application runs on port 8080
# API documentation available at http://localhost:8080/doc.html

# Run tests
mvn test

# Package for deployment
mvn clean package
```

### Docker Services (docker/)

```bash
cd docker
# Start all services (MySQL, Redis, Application)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Restart services
docker-compose restart
```

### Database Migrations

```bash
cd database-migrations/scripts

# Create a new migration
./flyway-create.sh migration_name

# Validate migrations
./flyway-validate.sh

# View migration status
./flyway-info.sh
```

## Architecture

### SDLC Framework Structure

The SDLC Framework is organized around **15 sequential stages**:

1. Requirements Analysis → Product Design → Architecture Design → Detailed Design
2. Database Migration → Code Development → Unit Testing → Integration Testing
3. System Testing → Test Cases → System Acceptance → Documentation
4. Deployment → Upgrade

Each stage contains:
- `stage.md`: Stage description and deliverables
- `template.md`: Professional template for that stage's output
- Subagent role definitions for task execution

### Subagent Roles

The framework defines 8 specialized subagent roles in `SDLC-Framework/subagents/`:

- **Product Manager**: Requirements gathering, stakeholder management
- **Architect**: System architecture, technology choices
- **Backend Developer**: API development, business logic
- **Frontend Developer**: UI components, user experience
- **QA Engineer**: Testing strategy, quality assurance
- **DevOps Engineer**: Deployment, CI/CD, infrastructure
- **DB Administrator**: Database design, migrations, optimization
- **Technical Writer**: Documentation, manuals, guides

### Skills Integration

Skills in `Skills-Collection/` integrate with the SDLC framework:

- **ruoyi-crud**: Generates complete CRUD code (Entity, Mapper, Service, Controller) for Ruoyi tables
- **code-review**: Performs code quality analysis using predefined checklists
- **test-gen**: Generates JUnit test cases for specified classes
- **api-doc**: Generates API documentation from controller code
- **sql-optimizer**: Analyzes and suggests SQL optimizations
- **flyway-migration**: Manages Flyway database migrations

Skills are invoked via slash commands (e.g., `/ruoyi-crud sys_user`) and follow the pattern defined in their `skill.md` file.

### Ruoyi Example Application

The `ruoyi-example/` directory demonstrates a typical Spring Boot application structure:

- **Dependencies**: MyBatis-Plus 3.5.5, Druid 1.2.20, Hutool 5.8.24, Knife4j 4.3.0
- **Configuration**: Application properties in `src/main/resources/application.yml`
- **Code Structure**: Standard Maven layout with domain, mapper, service, controller packages
- **API Documentation**: Knife4j UI accessible at `/doc.html`

### Workflow Orchestration

The framework supports three workflow patterns defined in `SDLC-Framework/workflows/`:

- **Full SDLC**: Execute all 15 stages sequentially
- **Agile Sprint**: 2-week sprint workflow with story-based development
- **Bug Fix**: Accelerated workflow for bug fixes and patches

Workflows are invoked via commands like `/sdlc-full`, `/agile-sprint`, or `/bug-fix`.

## Key Conventions

### File Naming

- SDLC stages: `01-requirements-analysis/`, `02-product-design/`, etc.
- Migration scripts: `V{version}__{description}.sql` (Flyway convention)
- Skill definitions: `skill.md` in each skill directory

### Documentation Language

All documentation is in **Chinese** as this project targets Chinese developers. When generating documentation or user-facing content, use Chinese.

### Code Style

- Java 17 features enabled
- Lombok annotations for boilerplate reduction
- MyBatis-Plus for database operations
- RESTful API design with Knife4j documentation

### Quality Gates

Each SDLC stage has quality gates defined in `SDLC-Framework/config/quality-gates.yaml`. Ensure all checks pass before proceeding to the next stage.

## Development Context

This project is designed to demonstrate **Claude Code's capabilities** when enhanced with:
1. **Custom Skills** - Domain-specific development workflows
2. **Subagents** - Role-based task delegation
3. **Structured Templates** - Consistent documentation output
4. **Workflow Orchestration** - Multi-stage development processes

When working with this codebase, consider which components can be leveraged to automate or streamline the task at hand.
