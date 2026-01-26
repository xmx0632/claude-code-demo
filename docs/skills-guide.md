# Skills 开发指南

## Skills 核心概念

Skills 是扩展 Claude Code 能力的自定义指令，通过 `SKILL.md` 文件定义。

### 基本结构

```
skill-name/
├── SKILL.md           # 必需：技能定义
├── template.md        # 可选：模板文件
├── examples/          # 可选：示例输出
└── scripts/           # 可选：执行脚本
```

### YAML 配置

```yaml
---
name: skill-name              # 技能名称，自动创建 /skill-name 命令
description: 何时使用此技能    # Claude 的触发条件
disable-model-invocation: false  # true 表示只能手动调用
user-invocable: true           # false 表示只能 Claude 调用
allowed-tools: [...]           # 限制可用的工具
context: fork                  # 在子代理中运行
agent: general-purpose         # 子代理类型
---
```

## 参数传递

### 基本语法

使用 `$ARGUMENTS` 获取用户传入的参数：

```markdown
---
name: greet
description: 向用户打招呼
---

你好 $ARGUMENTS！
```

**使用**：
```
/greet 张三
# 输出：你好 张三！
```

### 多个参数

```markdown
---
name: create-controller
description: 创建 Controller
---

创建 $ARGUMENTS Controller，包含以下方法：
- 列表查询
- 详情查询
- 新增
- 修改
- 删除
```

**使用**：
```
/create-controller 用户
```

## 实用 Skills 示例

### 示例 1：ruoyi-crud - CRUD 代码生成

**文件结构**：
```
Skills-Collection/ruoyi-crud/
├── SKILL.md
├── templates/
│   ├── entity.java.ftl
│   ├── mapper.java.ftl
│   ├── service.java.ftl
│   └── controller.java.ftl
└── examples/
    └── user/
```

**SKILL.md**：
```yaml
---
name: ruoyi-crud
description: 为 Ruoyi 项目生成完整的 CRUD 代码。用于快速开发新模块。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Bash"]
---

# Ruoyi CRUD 代码生成器

为表名 `$ARGUMENTS` 生成完整的 CRUD 代码。

## 生成内容

1. **实体类 (Entity)**
2. **Mapper 接口**
3. **Service 层**
4. **Controller 层**

## 使用方法

/ruoyi-crud sys_user

生成用户管理模块的所有代码。
```

### 示例 2：code-review - 代码审查

**SKILL.md**：
```yaml
---
name: code-review
description: 审查 Java 代码，检查规范、潜在问题和优化建议
allowed-tools: ["Read", "Grep", "Glob"]
---

# Java 代码审查

审查当前项目或指定文件的代码质量。

## 检查项

- [ ] 代码规范（命名、格式）
- [ ] 潜在 Bug（空指针、资源泄漏）
- [ ] 性能问题（SQL 查询、循环）
- [ ] 安全问题（SQL 注入、XSS）
- [ ] 测试覆盖

## 使用方法

/code-review

审查整个项目

/code-review src/main/java/com/example/UserController.java

审查单个文件
```

### 示例 3：api-doc - API 文档生成

**SKILL.md**：
```yaml
---
name: api-doc
description: 解析 Spring Boot Controller 并生成 API 文档
allowed-tools: ["Read", "Grep", "Glob"]
---

# API 文档生成器

自动解析项目中的 Controller 类，生成 Markdown 格式的 API 文档。

## 输出内容

- 接口列表
- 请求方法和路径
- 请求参数
- 响应格式
- 示例

## 使用方法

/api-doc

生成所有 API 文档
```

### 示例 4：test-gen - 单元测试生成

**SKILL.md**：
```yaml
---
name: test-gen
description: 为 Service 层方法生成单元测试
allowed-tools: ["Read", "Write", "Edit"]
---

# 单元测试生成器

为指定的 Service 类生成 JUnit + Mockito 测试用例。

## 生成内容

- 测试类框架
- 方法测试用例
- Mock 对象设置
- 断言语句

## 使用方法

/test-gen UserService

为 UserService 生成测试
```

### 示例 5：sql-optimizer - SQL 优化

**SKILL.md**：
```yaml
---
name: sql-optimizer
description: 分析 MyBatis SQL 并提供优化建议
allowed-tools: ["Read", "Grep", "Glob"]
---

# SQL 优化助手

分析 MyBatis Mapper XML 文件，提供性能优化建议。

## 检查项

- 缺少索引的查询
- N+1 查询问题
- 全表扫描
- 未使用绑定变量

## 使用方法

/sql-optimizer

分析所有 Mapper
```

## 高级技巧

### 动态上下文注入

使用 `!`command"` 执行命令并注入结果：

```markdown
---
name: list-endpoints
description: 列出所有 API 端点
---

# API 端点列表

当前项目的所有 API 端点：

!`grep -r "@RequestMapping" src/ --include="*.java" -n`
```

### 工具权限控制

限制 Skill 只能使用特定工具：

```yaml
---
allowed-tools: ["Read", "Write"]  # 只能读写文件
---
```

### 子代理模式

使用 `context: fork` 在独立环境中执行：

```yaml
---
context: fork
agent: Explore
---

# 代码探索任务

此 Skill 在 Explore 子代理中运行，不会影响主会话。
```

## 开发规范

### 命名约定

- 使用小写字母和连字符：`my-skill`
- 避免使用特殊字符
- 名称应该描述功能

### 文档注释

```markdown
---
name: my-skill
description: 一句话描述何时使用此技能

# 技能标题

详细说明技能的作用、使用方法和注意事项。
```

### 错误处理

```markdown
## 错误处理

如果遇到以下错误：
- **文件不存在**：请先创建文件
- **权限不足**：请检查文件权限
```

## 下一步

- [完整示例项目](../ruoyi-example/)
- [最佳实践](./best-practices.md)
