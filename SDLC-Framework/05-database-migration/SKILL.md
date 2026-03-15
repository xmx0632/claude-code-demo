# Database Migration Skill

> 阶段 5: 数据库迁移脚本

---

## 触发命令

```bash
/flyway-migration create --table=<table_name>
```

---

## 阶段目标

创建和管理数据库迁移脚本，确保数据库结构版本可控。

---

## 输入

- 数据模型设计 (阶段 4 产出)
- 现有数据库结构 (如有)

---

## 输出

| 产出物 | 文件 | 说明 |
|--------|------|------|
| 迁移脚本 | db/migration/V{version}__{description}.sql | Flyway 格式 |
| 回滚脚本 | db/migration/rollback/V{version}__{description}.sql | 可选 |

---

## 命名规范

```
V{版本号}__{操作类型}_{表名}.sql

示例:
V1.0.1__create_user_table.sql
V1.0.2__add_email_column_to_user.sql
V1.0.3__create_index_on_user_email.sql
```

---

## 执行步骤

### 1. 分析变更

- 确定需要创建/修改的表
- 识别索引需求
- 评估数据迁移需求

### 2. 编写脚本

- 编写正向迁移脚本
- 编写回滚脚本 (可选)
- 添加注释说明

### 3. 本地测试

- 在本地环境执行
- 验证表结构正确
- 测试回滚脚本

### 4. 代码审查

- SQL 语法检查
- 性能影响评估
- 安全检查

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Performance Agent | 创建索引、大表操作 |
| Security Agent | 涉及敏感数据字段 |

---

## 质量门禁

- [ ] 脚本命名规范
- [ ] 包含回滚方案
- [ ] 本地测试通过
- [ ] 代码审查通过
- [ ] 性能影响已评估

---

## 相关文件

- 模板目录: `05-database-migration/templates/`
- 角色定义: `roles/backend-developer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
