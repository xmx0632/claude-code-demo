# SDD 增强功能使用示例

> 演示 SDLC Framework v1.1 的新增功能

---

## 1. 本地需求管理

### 创建新需求

```bash
# 1. 复制模板
cp requirements/TEMPLATE.md requirements/backlog/REQ-003-用户登录功能.md

# 2. 编辑需求文件
# 填写需求描述、验收标准、技术要点
```

### 在 Claude Code 中处理需求

```markdown
<!-- 在 Claude Code 对话中 -->

处理需求 REQ-003
```

**Claude 会自动**:
1. 读取 `requirements/backlog/REQ-003-用户登录功能.md`
2. 分析需求内容
3. 初始化 SDLC 进度追踪
4. 将需求移动到 `active/` 目录

### 需求进度追踪

需求文件内置进度表，自动更新：

```markdown
| 阶段 | 状态 | 开始日期 | 完成日期 | 产出物 |
|------|------|----------|----------|--------|
| 需求分析 | ✅ 完成 | 2026-03-16 | 2026-03-16 | REQ-003.md |
| 架构设计 | 🔄 进行中 | 2026-03-16 | - | - |
| 详细设计 | ⬜ 待开始 | - | - | - |
```

---

## 2. 智能体自动注入

### 场景：实现用户登录 API

```markdown
<!-- Claude Code 对话 -->

实现用户登录 API，支持邮箱密码登录
```

**自动触发的智能体**:

```
🤖 检测到关键词: 登录、API

自动激活:
- 🔒 Security Agent (检测到: 登录、认证)
- ⚡ Performance Agent (检测到: API)
```

**Security Agent 注入的约束**:

```typescript
// Claude 生成的代码会自动包含:

// ✅ 输入验证
const LoginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
});

// ✅ 密码加密
const isValid = await bcrypt.compare(password, user.passwordHash);

// ✅ 速率限制
if (loginAttempts > 5) {
  throw new TooManyRequestsError('登录失败次数过多');
}

// ✅ 日志脱敏
logger.info({ userId: user.id }, 'User logged in');
// 而非 logger.info(`User ${user.email} logged in`);
```

**Performance Agent 注入的约束**:

```typescript
// ✅ 索引查询
const user = await db.query(
  'SELECT * FROM users WHERE email = $1',  // email 有索引
  [email]
);

// ✅ 用户信息缓存
await redis.set(`user:${user.id}`, userData, 'EX', 300);
```

---

## 3. 阶段完成报告

### 编码阶段完成后

```markdown
## 阶段完成: 编码实现

🤖 智能体汇总报告:

### 🔒 Security Agent
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 输入验证 | ✅ | zod schema |
| 密码加密 | ✅ | bcrypt |
| 权限检查 | ✅ | JWT 验证 |
| 速率限制 | ✅ | 5次/分钟 |
| 日志脱敏 | ✅ | 仅记录 ID |

### ⚡ Performance Agent
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 查询优化 | ✅ | email 索引 |
| 缓存策略 | ✅ | Redis 5min TTL |
| N+1 问题 | ✅ | 无关联查询 |

✅ 所有检查通过，可以进入下一阶段
```

---

## 4. 完整工作流示例

### 从需求到部署

```markdown
<!-- 在 Claude Code 中执行完整流程 -->

## 处理需求 REQ-003: 用户登录功能

### Phase 1: 需求分析
- 读取需求文件
- 确认验收标准
- 更新进度表

### Phase 2: 架构设计
- 设计认证方案 (JWT)
- 🤖 Performance Agent: 评估性能影响

### Phase 3: 详细设计
- API 接口设计
- 数据模型设计
- 🤖 Security Agent: 安全设计评审

### Phase 4: 编码实现
- 后端 API 实现
- 🤖 Security Agent: 代码安全检查
- 🤖 Performance Agent: 代码性能检查
- 🤖 Infra Agent: 容器化配置

### Phase 5: 测试验证
- 单元测试
- 集成测试
- 🤖 Security Agent: 安全测试

### Phase 6: 部署上线
- 🤖 Infra Agent: 部署配置检查
- 部署到生产环境
- 配置监控告警

### Phase 7: 完成需求
- 更新需求状态为 done
- 移动到 completed/ 目录
```

---

## 5. 手动触发智能体

### 安全审查

```markdown
<!-- 对特定代码执行安全审查 -->

对 src/auth/ 目录执行 Security Agent 审查
```

### 性能分析

```markdown
<!-- 对特定模块执行性能分析 -->

对 API 模块执行 Performance Agent 分析
```

### 部署检查

```markdown
<!-- 检查部署配置 -->

执行 Infra Agent 部署检查
```

---

## 6. 框架反馈循环

### Bug 修复后反思

```markdown
<!-- Bug 修复完成后 -->

## Bug 修复: 登录接口 SQL 注入漏洞

### 框架反思

**缺口类型**: A (规范→实现)

**原因分析**:
- 规范要求使用参数化查询
- 实现时使用了字符串拼接

**框架改进建议**:
1. 在 Security Agent 中增加 SQL 注入检查示例
2. 在编码阶段模板中强调参数化查询
3. 添加自动化检测规则

**记录到**: feedback/records/2026-03-16_SQL注入反思.md
```

---

## 7. Constitution 引用

### 在阶段执行时自动引用

```markdown
## 开始阶段: 编码实现

📋 读取项目宪法 (guidance/CONSTITUTION.md)

**遵循的宪法条款**:
- 技术原则: 模块化设计, API 优先
- 安全要求: 输入验证, 权限检查
- 质量门禁: 单元测试通过, 覆盖率 ≥ 80%

**约束已应用**:
- ✅ 使用 TypeScript
- ✅ 所有 API 有 JSDoc 注释
- ✅ 统一错误处理机制
```

---

## 命令速查

| 功能 | 指令 |
|------|------|
| 处理需求 | `处理需求 REQ-XXX` |
| 查看需求状态 | `需求 REQ-XXX 当前状态` |
| 完成阶段 | `完成需求 REQ-XXX 的架构设计阶段` |
| 安全审查 | `对 [路径] 执行 Security Agent 审查` |
| 性能分析 | `对 [模块] 执行 Performance Agent 分析` |
| 部署检查 | `执行 Infra Agent 部署检查` |
| 完整审查 | `执行完整智能体审查` |
