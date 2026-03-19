# API规范文档模板

## 文档元数据

```yaml
document:
  name: "api-specs.md"
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

# API规范文档

## 1. API设计原则

### 1.1 RESTful规范

- 使用名词复数: `/users`, `/orders`
- 使用HTTP动词: GET, POST, PUT, DELETE
- 版本控制: `/api/v1/`
- 统一响应格式

### 1.2 URL设计规范

```
资源集合:    GET    /api/v1/users
单个资源:    GET    /api/v1/users/{id}
子资源:      GET    /api/v1/users/{id}/orders
```

### 1.3 HTTP状态码规范

| 状态码 | 说明 | 使用场景 |
|--------|------|---------|
| 200 | OK | 请求成功 |
| 201 | Created | 创建成功 |
| 204 | No Content | 删除成功 |
| 400 | Bad Request | 参数错误 |
| 401 | Unauthorized | 未认证 |
| 403 | Forbidden | 无权限 |
| 404 | Not Found | 资源不存在 |
| 409 | Conflict | 资源冲突 |
| 500 | Internal Server Error | 服务器错误 |

## 2. 统一响应格式

### 2.1 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 业务数据
  },
  "timestamp": "2026-03-19T10:30:00Z"
}
```

### 2.2 错误响应

```json
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "username",
      "message": "用户名不能为空"
    }
  ],
  "timestamp": "2026-03-19T10:30:00Z"
}
```

### 2.3 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "pageSize": 10,
    "current": 1,
    "pages": 10
  }
}
```

## 3. 业务错误码

### 3.1 错误码规范

```
格式: {模块}{错误类型}{具体错误}
示例: USER_001_001 (用户模块-参数错误-用户名为空)
```

### 3.2 通用错误码

| 错误码 | 说明 | HTTP状态 |
|--------|------|---------|
| COMMON_001 | 参数错误 | 400 |
| COMMON_002 | 未认证 | 401 |
| COMMON_003 | 无权限 | 403 |
| COMMON_004 | 资源不存在 | 404 |
| COMMON_005 | 系统错误 | 500 |
| COMMON_006 | 服务降级 | 503 |

### 3.3 业务错误码

| 模块 | 错误码 | 说明 | HTTP状态 |
|------|--------|------|---------|
| 用户 | USER_001 | 用户不存在 | 404 |
| 用户 | USER_002 | 用户已存在 | 409 |
| 用户 | USER_003 | 密码错误 | 401 |
| 订单 | ORDER_001 | 订单不存在 | 404 |
| 订单 | ORDER_002 | 库存不足 | 400 |

## 4. API接口定义

### 4.1 用户模块

#### 4.1.1 用户列表

**请求**

```http
GET /api/v1/users?page=1&size=10&keyword=admin
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认1 |
| size | int | 否 | 每页数量，默认10 |
| keyword | string | 否 | 搜索关键词 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "admin",
        "email": "admin@example.com",
        "status": "active",
        "createdAt": "2026-03-19T10:00:00Z"
      }
    ],
    "total": 100,
    "pageSize": 10,
    "current": 1,
    "pages": 10
  }
}
```

#### 4.1.2 创建用户

**请求**

```http
POST /api/v1/users
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "roleIds": [2, 3]
}
```

| 字段 | 类型 | 必填 | 验证规则 |
|------|------|------|---------|
| username | string | 是 | 3-20字符，字母数字 |
| email | string | 是 | 邮箱格式 |
| password | string | 是 | 6-20字符 |
| roleIds | array | 否 | 角色ID列表 |

**响应**

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 10,
    "username": "testuser",
    "email": "test@example.com",
    "status": "active",
    "createdAt": "2026-03-19T10:30:00Z"
  }
}
```

#### 4.1.3 更新用户

**请求**

```http
PUT /api/v1/users/{id}
Content-Type: application/json

{
  "email": "newemail@example.com",
  "status": "inactive"
}
```

**响应**

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 10,
    "email": "newemail@example.com",
    "status": "inactive",
    "updatedAt": "2026-03-19T10:35:00Z"
  }
}
```

#### 4.1.4 删除用户

**请求**

```http
DELETE /api/v1/users/{id}
```

**响应**

```json
{
  "code": 204,
  "message": "删除成功"
}
```

### 4.2 认证模块

#### 4.2.1 用户登录

**请求**

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "captcha": "1234",
  "captchaKey": "uuid-key"
}
```

**响应**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "user": {
      "id": 1,
      "username": "admin",
      "roles": ["admin"],
      "permissions": ["*:*:*"]
    }
  }
}
```

#### 4.2.2 刷新令牌

**请求**

```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "refresh_token_value"
}
```

**响应**

```json
{
  "code": 200,
  "message": "刷新成功",
  "data": {
    "token": "new_access_token",
    "expiresIn": 7200
  }
}
```

#### 4.2.3 用户登出

**请求**

```http
POST /api/v1/auth/logout
Authorization: Bearer {token}
```

**响应**

```json
{
  "code": 200,
  "message": "登出成功"
}
```

### 4.3 订单模块

#### 4.3.1 创建订单

**请求**

```http
POST /api/v1/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "items": [
    {
      "productId": 100,
      "quantity": 2,
      "price": 99.00
    }
  ],
  "shippingAddress": {
    "province": "北京市",
    "city": "北京市",
    "district": "朝阳区",
    "detail": "xxx街道xxx号"
  }
}
```

**响应**

```json
{
  "code": 201,
  "message": "订单创建成功",
  "data": {
    "orderId": "ORD20260319001",
    "totalAmount": 198.00,
    "status": "pending",
    "createdAt": "2026-03-19T10:40:00Z"
  }
}
```

#### 4.3.2 订单详情

**请求**

```http
GET /api/v1/orders/{orderId}
Authorization: Bearer {token}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": "ORD20260319001",
    "status": "pending",
    "items": [
      {
        "productId": 100,
        "productName": "商品名称",
        "quantity": 2,
        "price": 99.00
      }
    ],
    "totalAmount": 198.00,
    "shippingAddress": {
      "province": "北京市",
      "city": "北京市",
      "district": "朝阳区",
      "detail": "xxx街道xxx号"
    },
    "createdAt": "2026-03-19T10:40:00Z",
    "updatedAt": "2026-03-19T10:40:00Z"
  }
}
```

## 5. 请求头规范

### 5.1 标准请求头

```http
Content-Type: application/json
Accept: application/json
User-Agent: MyApp/1.0
Accept-Language: zh-CN
```

### 5.2 认证请求头

```http
Authorization: Bearer {access_token}
```

### 5.3 追踪请求头

```http
X-Request-ID: {uuid}
X-Trace-ID: {trace_id}
```

## 6. 数据类型规范

### 6.1 日期时间

```json
{
  "createdAt": "2026-03-19T10:30:00Z",
  "updatedAt": "2026-03-19T10:30:00+08:00",
  "date": "2026-03-19",
  "time": "10:30:00"
}
```

### 6.2 金额

```json
{
  "amount": 99.99,
  "currency": "CNY"
}
```

### 6.3 枚举

```json
{
  "status": "active",  // active, inactive, pending
  "type": 1            // 1:普通, 2:VIP, 3:SVIP
}
```

## 7. 限流规范

### 7.1 限流策略

| 接口类型 | 限流规则 |
|---------|---------|
| 公开接口 | 100 req/min/IP |
| 认证接口 | 10 req/min/IP |
| 业务接口 | 1000 req/min/User |

### 7.2 限流响应

```http
HTTP/1.1 429 Too Many Requests
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1710834600

{
  "code": 429,
  "message": "请求过于频繁，请稍后再试"
}
```

## 8. 版本控制

### 8.1 版本策略

- URL版本控制: `/api/v1/`, `/api/v2/`
- 向后兼容原则
- 废弃接口保留3个月

### 8.2 版本迁移

```http
# v1接口 (即将废弃)
GET /api/v1/users/{id}

# v2接口 (推荐)
GET /api/v2/users/{id}?include=profile,settings
```
