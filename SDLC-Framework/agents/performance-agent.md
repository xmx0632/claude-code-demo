# Performance Agent - 性能约束智能体

> 自动注入性能优化和基准要求

---

## 触发条件

- 涉及数据库查询
- 涉及大量数据处理
- 涉及 API 响应时间要求
- 涉及前端渲染性能
- 涉及资源加载

---

## 性能基准

### API 响应时间

| 接口类型 | 目标响应时间 | 告警阈值 |
|----------|--------------|----------|
| 简单查询 | < 50ms | > 100ms |
| 复杂查询 | < 200ms | > 500ms |
| 批量操作 | < 1s | > 3s |
| 文件上传 | < 5s | > 10s |

### 数据库查询

| 操作类型 | 目标时间 | 最大时间 |
|----------|----------|----------|
| 单条查询 | < 5ms | > 20ms |
| 列表查询 | < 50ms | > 200ms |
| 聚合查询 | < 100ms | > 500ms |
| 写入操作 | < 20ms | > 100ms |

### 前端性能

| 指标 | 目标值 | 告警值 |
|------|--------|--------|
| FCP (首次内容绘制) | < 1.8s | > 3s |
| LCP (最大内容绘制) | < 2.5s | > 4s |
| TTI (可交互时间) | < 3.8s | > 7.3s |
| CLS (累积布局偏移) | < 0.1 | > 0.25 |

---

## 性能优化模式

### 数据库优化

```typescript
// ✅ 正确：使用索引查询
await db.query(
  'SELECT * FROM users WHERE email = $1',  // email 有索引
  [email]
);

// ✅ 正确：分页查询
const users = await db.query(
  'SELECT * FROM users ORDER BY created_at DESC LIMIT $1 OFFSET $2',
  [limit, offset]
);

// ✅ 正确：批量插入
await db.query(
  'INSERT INTO logs (user_id, action) VALUES ($1, $2)',
  logs.map(l => [l.userId, l.action])
);

// ❌ 错误：全表扫描
const users = await db.query('SELECT * FROM users');
for (const user of users) {
  if (user.email === email) return user;
}
```

### 缓存策略

```typescript
// ✅ 正确：多级缓存
const getUser = async (id: string) => {
  // L1: 本地缓存
  const local = localCache.get(`user:${id}`);
  if (local) return local;

  // L2: Redis 缓存
  const cached = await redis.get(`user:${id}`);
  if (cached) {
    localCache.set(`user:${id}`, cached, { ttl: 60 });
    return cached;
  }

  // L3: 数据库
  const user = await db.query('SELECT * FROM users WHERE id = $1', [id]);
  await redis.set(`user:${id}`, user, 'EX', 3600);
  return user;
};

// ❌ 错误：每次都查数据库
const getUser = async (id: string) => {
  return await db.query('SELECT * FROM users WHERE id = $1', [id]);
};
```

### N+1 查询问题

```typescript
// ✅ 正确：批量加载关联数据
const posts = await db.query('SELECT * FROM posts LIMIT 10');
const userIds = [...new Set(posts.map(p => p.author_id))];
const users = await db.query(
  'SELECT * FROM users WHERE id = ANY($1)',
  [userIds]
);
const userMap = new Map(users.map(u => [u.id, u]));
const postsWithAuthors = posts.map(p => ({
  ...p,
  author: userMap.get(p.author_id)
}));

// ❌ 错误：N+1 查询
const posts = await db.query('SELECT * FROM posts LIMIT 10');
for (const post of posts) {
  post.author = await db.query(
    'SELECT * FROM users WHERE id = $1',
    [post.author_id]
  ); // 每个 post 都查一次！
}
```

### 前端优化

```typescript
// ✅ 正确：懒加载组件
const HeavyComponent = lazy(() => import('./HeavyComponent'));

// ✅ 正确：虚拟列表
import { VirtualList } from 'react-window';
<VirtualList
  height={600}
  itemCount={10000}
  itemSize={50}
>
  {Row}
</VirtualList>

// ✅ 正确：防抖/节流
const debouncedSearch = useMemo(
  () => debounce(fetchResults, 300),
  []
);

// ❌ 错误：一次性渲染大量数据
{items.map(item => <Item key={item.id} {...item} />)} // 10000+ 项
```

---

## 强制约束

### 代码审查必须检查

| 检查项 | 说明 |
|--------|------|
| 查询性能 | 是否有 N+1 查询 |
| 索引使用 | 查询字段是否有索引 |
| 缓存策略 | 热点数据是否缓存 |
| 分页实现 | 列表是否分页 |
| 懒加载 | 大组件是否懒加载 |

### 性能测试门禁

```yaml
performance_gate:
  api_response_time:
    p50: 50ms
    p95: 200ms
    p99: 500ms
  db_query_time:
    p95: 50ms
  frontend_metrics:
    lcp: 2.5s
    tti: 3.8s
  fail_action: warn  # 警告但不阻止
```

---

## 性能分析清单

### 数据库层

- [ ] 慢查询日志已开启
- [ ] 关键查询有索引
- [ ] 大表有分区策略
- [ ] 连接池配置合理

### 应用层

- [ ] 热点数据有缓存
- [ ] 批量操作有优化
- [ ] 异步任务有队列
- [ ] 大文件有流处理

### 前端层

- [ ] 首屏资源已优化
- [ ] 图片有懒加载
- [ ] 代码已分割
- [ ] 静态资源有 CDN

---

## 使用方式

### 在 SDLC 阶段中触发

```markdown
<!-- Claude Code 执行编码阶段时 -->

当前阶段: 编码实现
涉及模块: 商品列表 API

🤖 Performance Agent 自动注入:

性能要求:
- 列表查询 < 200ms
- 必须分页 (默认 20 条)
- 商品图片需缓存
- 支持 sorting/filtering

检查清单:
- [ ] 添加数据库索引 (category_id, price)
- [ ] 实现分页查询
- [ ] 添加 Redis 缓存 (5分钟 TTL)
- [ ] 避免 N+1 查询 (商品-分类关联)
```

### 性能分析

```
# Claude Code 指令
分析 src/api/products/ 的性能瓶颈
```

---

## 输出模板

```markdown
## ⚡ Performance Agent 报告

**分析范围**: [文件/模块]
**发现问题**: X 个

### 性能瓶颈

| 文件 | 行号 | 问题 | 影响 | 优化建议 |
|------|------|------|------|----------|

### 优化建议

1. **[高优先级]** 简要说明
   - 当前: ...
   - 建议: ...
   - 预期提升: ...

2. **[中优先级]** 简要说明

### 性能基准

| 指标 | 当前值 | 目标值 | 状态 |
|------|--------|--------|------|
| API 响应 | 350ms | < 200ms | ⚠️ 需优化 |
| DB 查询 | 45ms | < 50ms | ✅ 通过 |
```
