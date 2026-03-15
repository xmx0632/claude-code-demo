# 系统增量升级说明

**项目**: TodoList 应用
**版本**: v1.0.0
**更新日期**: 2026-03-16

---

## 1. 版本说明

### 1.1 版本号规则

采用语义化版本：`MAJOR.MINOR.PATCH`

- **MAJOR**: 不兼容的 API 变更
- **MINOR**: 向下兼容的功能新增
- **PATCH**: 向下兼容的问题修复

### 1.2 当前版本

| 组件 | 版本 | 说明 |
|------|------|------|
| 后端 | v1.0.0 | 初始版本 |
| 前端 | v1.0.0 | 初始版本 |
| 数据库 | v1 | 初始 Schema |

---

## 2. 升级策略

### 2.1 滚动升级（推荐）

适用于 **PATCH** 和 **MINOR** 版本升级，零停机。

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ 实例 1  │────▶│ 实例 2  │────▶│ 实例 3  │
│ v1.0.0  │     │ v1.0.1  │     │ v1.0.1  │
└─────────┘     └─────────┘     └─────────┘
     ↓
  升级中
```

**步骤**:
1. 从负载均衡移除实例 1
2. 更新实例 1 代码
3. 健康检查通过后重新加入
4. 重复步骤 1-3 升级其他实例

### 2.2 蓝绿部署

适用于 **MAJOR** 版本升级。

```
┌─────────────────┐     ┌─────────────────┐
│    蓝环境       │     │    绿环境       │
│    v1.0.0       │     │    v2.0.0       │
│   (生产中)      │     │   (待切换)      │
└─────────────────┘     └─────────────────┘
           │                    │
           └────────────────────┘
                  切换流量
```

---

## 3. 数据库迁移

### 3.1 Flyway 迁移脚本

```
backend/src/main/resources/db/migration/
├── V1__Initial_schema.sql           # v1.0.0
├── V2__Add_category_table.sql       # v1.1.0
└── V3__Add_task_tags.sql            # v1.2.0
```

### 3.2 迁移规则

| 规则 | 说明 |
|------|------|
| 版本号递增 | V{N} 必须连续递增 |
| 不可修改 | 已执行的脚本不可修改 |
| 向下兼容 | 新增字段必须有默认值 |
| 回滚脚本 | 提供 U{N} 回滚脚本 |

### 3.3 迁移示例

```sql
-- V2__Add_category_table.sql
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(7) DEFAULT '#3498db',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- U2__Rollback_category_table.sql (回滚)
DROP TABLE IF EXISTS category;
```

---

## 4. 配置变更

### 4.1 配置版本控制

```yaml
# application-v1.1.0.yml
app:
  version: 1.1.0
  new-feature:
    enabled: true  # 新功能开关
```

### 4.2 配置迁移

```bash
# 对比配置差异
diff application-v1.0.0.yml application-v1.1.0.yml

# 更新配置
kubectl apply -f configmap-v1.1.0.yaml
```

---

## 5. 升级检查清单

### 5.1 升级前

- [ ] 备份数据库
- [ ] 通知相关方
- [ ] 准备回滚方案
- [ ] 确认新版本兼容性
- [ ] 检查依赖版本

### 5.2 升级中

- [ ] 执行数据库迁移
- [ ] 更新应用代码
- [ ] 验证健康检查
- [ ] 监控错误日志

### 5.3 升级后

- [ ] 功能验证测试
- [ ] 性能指标检查
- [ ] 用户验收测试
- [ ] 更新文档

---

## 6. 常见升级场景

### 6.1 添加新字段

```sql
-- V4__Add_user_avatar.sql
ALTER TABLE user ADD COLUMN avatar_url VARCHAR(255) DEFAULT NULL;
```

```java
// 实体类添加字段
@Column(name = "avatar_url")
private String avatarUrl;
```

### 6.2 添加新表

```sql
-- V5__Add_task_comment.sql
CREATE TABLE task_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES todo(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### 6.3 修改索引

```sql
-- V6__Add_task_status_index.sql
CREATE INDEX idx_todo_status ON todo(user_id, status);
```

---

## 7. 回滚操作

### 7.1 应用回滚

```bash
# Kubernetes 回滚
kubectl rollout undo deployment/todolist-backend

# Docker Compose 回滚
docker-compose down
docker tag todolist-backend:v1.0.1 todolist-backend:v1.0.0
docker-compose up -d
```

### 7.2 数据库回滚

```bash
# 执行回滚脚本
mysql -u root -p todolist < U6__Rollback_task_status_index.sql
mysql -u root -p todolist < U5__Rollback_task_comment.sql
```

---

## 8. 版本历史

| 版本 | 发布日期 | 主要变更 |
|------|----------|----------|
| v1.0.0 | 2026-03-16 | 初始版本发布 |
| - | - | - |

---

## 9. 联系支持

升级过程中遇到问题，请联系：
- 📧 Email: devops@example.com
- 💬 飞书群: 技术支持群
