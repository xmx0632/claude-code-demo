# 数据模型文档模板

## 文档元数据

```yaml
document:
  name: "data-models.md"
  version: "v1.0"
  status: "draft"
  created_at: "YYYY-MM-DD"
  updated_at: "YYYY-MM-DD"
  owner: "Architect"
  scenario: "new-project"

dependencies:
  - "requirements.md"
  - "architecture.md"
blocking:
  - "test-plan.md"

reviewers: []
```

---

# 数据模型文档

## 1. 数据库设计

### 1.1 数据库选型

| 数据库 | 用途 | 版本 | 部署方式 |
|--------|------|------|---------|
| MySQL | 主数据库 | 8.0 | 主从复制 |
| Redis | 缓存/会话 | 7.0 | 哨兵模式 |

### 1.2 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 表名 | 小写+下划线 | `user_account` |
| 字段名 | 小写+下划线 | `created_at` |
| 索引名 | `idx_表名_字段` | `idx_user_email` |
| 唯一索引 | `uk_表名_字段` | `uk_user_username` |
| 外键 | `fk_表名_字段` | `fk_order_user` |

## 2. ER图

### 2.1 核心实体关系

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    User      │       │    Order     │       │   Product    │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │──┐    │ id (PK)      │──┐    │ id (PK)      │
│ username     │  │    │ orderNo (U)  │  │    │ name         │
│ email (U)    │  │    │ userId (FK)  │◀─┘    │ price        │
│ password     │  │    │ totalAmount  │        │ stock        │
│ status       │  │    │ status       │        │ status       │
│ createdAt    │  │    │ createdAt    │        │ createdAt    │
└──────────────┘  │    └──────────────┘       └──────────────┘
                  │             │
                  ▼             ▼
            ┌──────────────────────────────┐
            │       OrderItem              │
            ├──────────────────────────────┤
            │ id (PK)                      │
            │ orderId (FK)                 │
            │ productId (FK)               │
            │ quantity                     │
            │ price                        │
            └──────────────────────────────┘
```

## 3. 表结构定义

### 3.1 用户表 (sys_user)

| 字段 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|------|------|------|---------|--------|------|
| id | BIGINT | - | NO | AUTO | 主键ID |
| username | VARCHAR | 50 | NO | - | 用户名 |
| email | VARCHAR | 100 | NO | - | 邮箱 |
| password | VARCHAR | 255 | NO | - | 密码(加密) |
| nickname | VARCHAR | 50 | YES | NULL | 昵称 |
| avatar | VARCHAR | 255 | YES | NULL | 头像URL |
| status | TINYINT | - | NO | 1 | 状态(1:正常,0:禁用) |
| last_login_at | DATETIME | - | YES | NULL | 最后登录时间 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |
| deleted | TINYINT | - | NO | 0 | 逻辑删除(0:正常,1:删除) |

**索引**

```sql
-- 主键
PRIMARY KEY (id)

-- 唯一索引
UNIQUE KEY uk_username (username)
UNIQUE KEY uk_email (email)

-- 普通索引
KEY idx_status (status)
KEY idx_created_at (created_at)

-- 联合索引
KEY idx_status_deleted (status, deleted)
```

**约束**

```sql
-- 用户名长度限制
CHECK (CHAR_LENGTH(username) >= 3 AND CHAR_LENGTH(username) <= 50)

-- 邮箱格式
CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')
```

### 3.2 订单表 (biz_order)

| 字段 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|------|------|------|---------|--------|------|
| id | BIGINT | - | NO | AUTO | 主键ID |
| order_no | VARCHAR | 32 | NO | - | 订单号 |
| user_id | BIGINT | - | NO | - | 用户ID |
| total_amount | DECIMAL | 10,2 | NO | - | 订单总金额 |
| status | TINYINT | - | NO | 0 | 状态 |
| remark | VARCHAR | 500 | YES | NULL | 备注 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引**

```sql
PRIMARY KEY (id)
UNIQUE KEY uk_order_no (order_no)
KEY idx_user_id (user_id)
KEY idx_status (status)
KEY idx_created_at (created_at)
```

### 3.3 订单商品表 (biz_order_item)

| 字段 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|------|------|------|---------|--------|------|
| id | BIGINT | - | NO | AUTO | 主键ID |
| order_id | BIGINT | - | NO | - | 订单ID |
| product_id | BIGINT | - | NO | - | 商品ID |
| product_name | VARCHAR | 200 | NO | - | 商品名称 |
| quantity | INT | - | NO | - | 数量 |
| price | DECIMAL | 10,2 | NO | - | 单价 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引**

```sql
PRIMARY KEY (id)
KEY idx_order_id (order_id)
KEY idx_product_id (product_id)
```

### 3.4 商品表 (biz_product)

| 字段 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|------|------|------|---------|--------|------|
| id | BIGINT | - | NO | AUTO | 主键ID |
| name | VARCHAR | 200 | NO | - | 商品名称 |
| description | TEXT | - | YES | NULL | 商品描述 |
| price | DECIMAL | 10,2 | NO | - | 价格 |
| stock | INT | - | NO | 0 | 库存 |
| category_id | BIGINT | - | YES | NULL | 分类ID |
| status | TINYINT | - | NO | 1 | 状态 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引**

```sql
PRIMARY KEY (id)
KEY idx_category_id (category_id)
KEY idx_status (status)
KEY idx_created_at (created_at)
```

## 4. 字典定义

### 4.1 订单状态

| 值 | 名称 | 说明 |
|----|------|------|
| 0 | 待支付 | 订单创建待支付 |
| 1 | 已支付 | 支付成功 |
| 2 | 已发货 | 商品已发货 |
| 3 | 已完成 | 订单完成 |
| 4 | 已取消 | 订单取消 |
| 5 | 已退款 | 订单退款 |

### 4.2 用户状态

| 值 | 名称 | 说明 |
|----|------|------|
| 0 | 禁用 | 用户被禁用 |
| 1 | 正常 | 用户正常 |

### 4.3 商品状态

| 值 | 名称 | 说明 |
|----|------|------|
| 0 | 下架 | 商品已下架 |
| 1 | 上架 | 商品已上架 |

## 5. 缓存设计

### 5.1 缓存策略

| 数据类型 | 缓存方式 | 过期时间 | 更新策略 |
|---------|---------|---------|---------|
| 用户信息 | String | 1h | 写入时更新 |
| 用户列表 | Hash | 10min | 定时刷新 |
| 热门商品 | ZSet | 30min | 定时刷新 |
| 计数器 | String | 永久 | 增量更新 |

### 5.2 缓存Key规范

```
格式: {app}:{module}:{type}:{id}
示例:
  - myapp:user:info:1001
  - myapp:product:hot:list
  - myapp:order:count:20260319
```

### 5.3 缓存更新策略

```
[读取]
  │
  ▼
[查询缓存] ──命中──▶ [返回数据]
  │
  └──miss
      │
      ▼
  [查询DB]
      │
      ├──▶ [写入缓存]
      │
      └──▶ [返回数据]

[写入]
  │
  ├──▶ [更新DB]
  │
  └──▶ [删除缓存]  (使用 Cache Aside 模式)
```

## 6. 数据分区策略

### 6.1 分表策略

| 表名 | 分表方式 | 分表字段 | 说明 |
|------|---------|---------|------|
| 订单表 | 按月 | created_at | order_202601, order_202602 |
| 日志表 | 按日 | created_at | log_20260319 |

### 6.2 分库策略

| 数据 | 分库方式 | 分库字段 | 说明 |
|------|---------|---------|------|
| 用户数据 | 水平分片 | user_id % 4 | 4个库 |
| 订单数据 | 水平分片 | user_id % 4 | 4个库 |

## 7. 数据迁移

### 7.1 迁移脚本命名

```
格式: V{version}__{description}.sql
示例: V1__init_schema.sql
     V2__add_user_avatar.sql
```

### 7.2 回滚脚本

```
格式: V{version}__{description}_rollback.sql
示例: V2__add_user_avatar_rollback.sql
```

## 8. 数据安全

### 8.1 敏感字段加密

| 字段 | 加密方式 | 密钥管理 |
|------|---------|---------|
| password | BCrypt | 应用配置 |
| mobile | AES | KMS |
| id_card | AES | KMS |

### 8.2 数据脱敏

```sql
-- 手机号脱敏
CONCAT(LEFT(mobile, 3), '****', RIGHT(mobile, 4))

-- 身份证脱敏
CONCAT(LEFT(id_card, 6), '********', RIGHT(id_card, 4))

-- 邮箱脱敏
CONCAT(LEFT(email, 2), '***', SUBSTRING_INDEX(email, '@', -1))
```

### 8.3 备份策略

| 类型 | 频率 | 保留期 | 存储位置 |
|------|------|--------|---------|
| 全量备份 | 每天 | 30天 | 对象存储 |
| 增量备份 | 每小时 | 7天 | 对象存储 |
| 日志备份 | 每天归档 | 90天 | 对象存储 |

## 9. 性能优化

### 9.1 索引优化建议

```sql
-- 避免在 WHERE 中使用 OR
-- 不推荐: WHERE status = 1 OR deleted = 0
-- 推荐: WHERE status = 1 AND deleted = 0

-- 避免在索引列上使用函数
-- 不推荐: WHERE DATE(created_at) = '2026-03-19'
-- 推荐: WHERE created_at >= '2026-03-19' AND created_at < '2026-03-20'

-- 避免隐式转换
-- 不推荐: WHERE user_id = '1001'  (字符串)
-- 推荐: WHERE user_id = 1001     (数字)
```

### 9.2 查询优化

```sql
-- 使用 EXPLAIN 分析查询
EXPLAIN SELECT * FROM biz_order WHERE user_id = 1001;

-- 避免SELECT *
SELECT id, order_no, total_amount FROM biz_order;

-- 使用 LIMIT 分页
SELECT * FROM biz_order ORDER BY id LIMIT 10 OFFSET 0;
```
