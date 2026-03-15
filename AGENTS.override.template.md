# AGENTS.override.md
# 子目录级 Agent 规则覆盖模板

## 用途

将此文件复制到子目录中，用于覆盖根目录 AGENTS.md 的规则。

**优先级**: 子目录规则 > 根目录规则

---

## 复制命令

```bash
# 在特定子目录中创建覆盖文件
cp AGENTS.override.template.md ruoyi-example/AGENTS.override.md
```

---

## 覆盖规则示例

### 覆盖测试命令

```bash
# 原命令 (根目录): mvn test
# 覆盖命令 (子目录): mvn test -Dtest=UserServiceTest
```

### 覆盖架构约束

```yaml
# 子项目可能有不同的架构规则
# 例如: frontend 项目不需要遵循分层架构
```

### 覆盖代码规范

```yaml
# 前端项目可能使用不同的命名规范
```

---

## 典型使用场景

### 场景 1: ruoyi-example/

```markdown
## 项目特定规则

- 使用 Ruoyi 框架规范
- 继承 BaseEntity
- 使用 @Data 注解
- Controller 继承 BaseController
```

### 场景 2: todolist-project/

```markdown
## 项目特定规则

- JWT 认证
- RESTful API 设计
- MyBatis-Plus 操作数据库
```

### 场景 3: frontend/

```markdown
## 项目特定规则

- Vue 3 Composition API
- TypeScript 严格模式
- 组件命名使用 PascalCase
```

---

## 覆盖配置项

| 配置项 | 说明 | 示例 |
|--------|------|------|
| test_command | 测试命令 | `npm test` |
| build_command | 构建命令 | `mvn clean package` |
| lint_command | Lint 命令 | `eslint .` |
| architecture_rules | 架构规则 | 组件特定规则 |
| coding_standards | 编码规范 | 项目特定规范 |

---

**注意**: 覆盖文件应该只包含需要修改的规则，不需要重复根目录 AGENTS.md 的所有内容。
