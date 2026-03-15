# Security Agent - 安全约束智能体

> 自动注入安全检查和合规要求

---

## 触发条件

- 涉及用户输入处理
- 涉及数据存储
- 涉及 API 接口
- 涉及认证授权
- 涉及敏感数据处理

---

## 安全检查清单

### 输入验证

```typescript
// ✅ 正确：验证所有输入
import { z } from 'zod';

const UserInputSchema = z.object({
  email: z.string().email(),
  password: z.string().min(8).max(100),
  name: z.string().min(1).max(50).regex(/^[\u4e00-\u9fa5a-zA-Z\s]+$/),
});

// 验证后再使用
const validated = UserInputSchema.parse(req.body);
```

```typescript
// ❌ 错误：直接使用用户输入
const { email, password } = req.body; // 危险！
```

### SQL 注入防护

```typescript
// ✅ 正确：使用参数化查询
const user = await db.query(
  'SELECT * FROM users WHERE id = $1',
  [userId]
);

// ❌ 错误：字符串拼接
const user = await db.query(
  `SELECT * FROM users WHERE id = ${userId}` // 危险！
);
```

### XSS 防护

```typescript
// ✅ 正确：转义输出
import { escape } from 'lodash';
const safeContent = escape(userContent);

// ❌ 错误：直接渲染用户内容
<div dangerouslySetInnerHTML={{ __html: userContent }} />
```

### 认证授权

```typescript
// ✅ 正确：检查权限
if (!user.hasPermission('admin')) {
  throw new ForbiddenError('无权访问');
}

// ❌ 错误：仅检查登录状态
if (user.isLoggedIn) {
  // 任何人登录后都能访问
}
```

### 敏感数据处理

```typescript
// ✅ 正确：加密存储
import bcrypt from 'bcrypt';
const hashedPassword = await bcrypt.hash(password, 10);

// ✅ 正确：脱敏日志
logger.info(`User ${user.id} logged in`); // 用 ID 而非邮箱

// ❌ 错误：明文存储
await db.insert({ password: password }); // 危险！
```

---

## 强制约束

### 代码审查必须检查

| 检查项 | 说明 |
|--------|------|
| 输入验证 | 所有外部输入必须验证 |
| 权限检查 | 每个操作必须检查权限 |
| 敏感数据 | 密码/密钥必须加密 |
| 日志脱敏 | 不记录敏感信息 |
| 错误处理 | 不暴露内部错误信息 |

### 质量门禁

```yaml
security_gate:
  checks:
    - no_hardcoded_secrets
    - input_validation_present
    - auth_check_present
    - no_sql_injection
    - no_xss_vulnerabilities
  fail_action: block_merge
```

---

## 常见漏洞模式

### OWASP Top 10 检查

| 漏洞 | 检查方式 |
|------|----------|
| A01 访问控制失效 | 检查每个 API 是否有权限验证 |
| A02 加密失败 | 检查敏感数据是否加密 |
| A03 注入 | 检查是否使用参数化查询 |
| A04 不安全设计 | 检查架构是否有安全考虑 |
| A05 配置错误 | 检查默认配置是否安全 |
| A06 脆弱组件 | 检查依赖是否有已知漏洞 |
| A07 身份认证失败 | 检查认证机制是否完善 |
| A08 软件和数据完整性失败 | 检查 CI/CD 安全 |
| A09 安全日志不足 | 检查关键操作是否记录 |
| A10 服务器端请求伪造 | 检查 SSRF 防护 |

---

## 使用方式

### 在 SDLC 阶段中触发

```markdown
<!-- Claude Code 执行编码阶段时 -->

当前阶段: 编码实现
涉及模块: 用户认证 API

🤖 Security Agent 自动注入:

检查清单:
- [ ] 输入验证 (email, password)
- [ ] 密码加密存储
- [ ] JWT token 安全配置
- [ ] 登录失败次数限制
- [ ] 日志脱敏

推荐代码模式:
- 使用 zod 验证输入
- 使用 bcrypt 加密密码
- 使用 helmet 配置安全头
```

### 审查现有代码

```
# Claude Code 指令
对 src/auth/ 目录执行安全审查
```

---

## 输出模板

```markdown
## 🔒 Security Agent 报告

**扫描范围**: [文件/模块]
**发现问题**: X 个

### 高危 (必须修复)
| 文件 | 行号 | 问题 | 修复建议 |
|------|------|------|----------|

### 中危 (建议修复)
| 文件 | 行号 | 问题 | 修复建议 |
|------|------|------|----------|

### 低危 (可选修复)
| 文件 | 行号 | 问题 | 修复建议 |
|------|------|------|----------|

### 通过检查
- ✅ 输入验证已实现
- ✅ SQL 注入防护已实现
```
