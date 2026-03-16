# API 规范文档 (API Specifications)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **创建日期**: 2026-01-26
> **作者**: 后端开发工程师
> **状态**: 草稿 | 评审中 | 已批准

---

## 文档修订历史

| 版本 | 日期 | 作者 | 修订说明 | 审批人 |
|------|------|------|----------|--------|
| 1.0  | 2026-01-26 | 后端开发工程师 | 初始版本 | 待审批 |

---

## 1. API 设计原则

### 1.1 RESTful 设计规范

**URL 结构**:
```
/api/{version}/{resource}/{id}
```

**版本管理**:
- 使用 URL 路径版本控制: `/api/v1/`
- 主要版本变更时升级版本号
- 保持向后兼容性

**HTTP 方法语义**:
| 方法 | 操作 | 幂等性 | 安全性 | 请求体 | 响应体 |
|------|------|--------|--------|--------|--------|
| GET | 查询 | ✓ | ✓ | ✗ | ✓ |
| POST | 创建 | ✗ | ✗ | ✓ | ✓ |
| PUT | 全量更新 | ✓ | ✗ | ✓ | ✓ |
| PATCH | 部分更新 | ✓ | ✗ | ✓ | ✓ |
| DELETE | 删除 | ✓ | ✗ | ✗ | ✓ |

### 1.2 统一响应格式

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "title",
      "message": "标题不能为空"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**分页响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [ ... ],
    "total": 100,
    "page": 1,
    "size": 20,
    "pages": 5
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

### 1.3 HTTP 状态码规范

| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| 200 | OK | 查询成功、更新成功 |
| 201 | Created | 创建成功 |
| 204 | No Content | 删除成功 |
| 400 | Bad Request | 参数错误、验证失败 |
| 401 | Unauthorized | 未认证、Token 无效 |
| 403 | Forbidden | 无权限访问 |
| 404 | Not Found | 资源不存在 |
| 409 | Conflict | 资源冲突（如用户名重复） |
| 500 | Internal Server Error | 服务器内部错误 |

### 1.4 认证机制

**JWT Token 认证**:
```
Authorization: Bearer <token>
```

**Token 获取**:
- 登录成功后返回 Token
- Token 有效期: 24 小时
- Token 刷新: 使用 Refresh Token

---

## 2. 认证模块 API

### 2.1 用户注册

**接口描述**: 新用户注册账户

**请求信息**:
```
POST /api/v1/auth/register
Content-Type: application/json
```

**请求参数**:
```json
{
  "username": "testuser",
  "password": "Test1234",
  "confirmPassword": "Test1234"
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| username | String | ✓ | 用户名 | 3-50 字符, 只能包含字母、数字、下划线 |
| password | String | ✓ | 密码 | 8-20 字符, 必须包含字母和数字 |
| confirmPassword | String | ✓ | 确认密码 | 必须与 password 相同 |

**响应示例 - 成功 (201)**:
```json
{
  "code": 201,
  "message": "注册成功",
  "data": {
    "userId": 123,
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 用户名已存在 (409)**:
```json
{
  "code": 409,
  "message": "用户名已存在",
  "errors": [
    {
      "field": "username",
      "message": "该用户名已被注册"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 参数验证失败 (400)**:
```json
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "password",
      "message": "密码必须至少 8 位,包含字母和数字"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 2.2 用户登录

**接口描述**: 用户登录获取 Token

**请求信息**:
```
POST /api/v1/auth/login
Content-Type: application/json
```

**请求参数**:
```json
{
  "username": "testuser",
  "password": "Test1234"
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | ✓ | 用户名 |
| password | String | ✓ | 密码 |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 123,
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 用户名或密码错误 (401)**:
```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 账户已锁定 (403)**:
```json
{
  "code": 403,
  "message": "账户已锁定,请 30 分钟后再试",
  "data": {
    "lockTimeRemaining": 1800
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 2.3 用户登出

**接口描述**: 用户登出,使 Token 失效

**请求信息**:
```
POST /api/v1/auth/logout
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**: 无

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "登出成功",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 未认证 (401)**:
```json
{
  "code": 401,
  "message": "Token 无效或已过期",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 2.4 刷新 Token

**接口描述**: 使用 Refresh Token 获取新的 Access Token

**请求信息**:
```
POST /api/v1/auth/refresh
Content-Type: application/json
```

**请求参数**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "Token 刷新成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - RefreshToken 无效 (401)**:
```json
{
  "code": 401,
  "message": "RefreshToken 无效或已过期,请重新登录",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

## 3. 待办事项管理 API

### 3.1 查询待办列表

**接口描述**: 查询当前用户的待办事项列表,支持分页、搜索、过滤和排序

**请求信息**:
```
GET /api/v1/todos/list
Authorization: Bearer <token>
```

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| page | Integer | ✗ | 页码,从 1 开始 | 1 |
| size | Integer | ✗ | 每页大小,默认 20 | 20 |
| status | String | ✗ | 状态过滤: PENDING/DONE | PENDING |
| priority | String | ✗ | 优先级: HIGH/MEDIUM/LOW | HIGH |
| dueDateFilter | String | ✗ | 截止日期过滤: today/week/month/overdue | today |
| categoryId | Long | ✗ | 分类 ID | 1 |
| keyword | String | ✗ | 搜索关键词(标题和描述) | 项目 |
| sortBy | String | ✗ | 排序字段: createdAt/dueDate/priority | createdAt |
| sortOrder | String | ✗ | 排序方向: asc/desc | desc |

**请求示例**:
```
GET /api/v1/todos/list?page=1&size=20&status=PENDING&priority=HIGH&sortBy=createdAt&sortOrder=desc
```

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "完成项目文档",
        "description": "编写详细的技术文档",
        "status": "PENDING",
        "priority": "HIGH",
        "dueDate": "2026-01-30T23:59:59Z",
        "completedAt": null,
        "categories": [
          {
            "id": 1,
            "name": "工作",
            "color": "#FF0000"
          }
        ],
        "createdAt": "2026-01-26T10:00:00Z",
        "updatedAt": "2026-01-26T10:00:00Z"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 20,
    "pages": 5
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 3.2 获取待办详情

**接口描述**: 根据 ID 获取待办事项详情

**请求信息**:
```
GET /api/v1/todos/{id}
Authorization: Bearer <token>
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | ✓ | 待办事项 ID |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写详细的技术文档",
    "status": "PENDING",
    "priority": "HIGH",
    "dueDate": "2026-01-30T23:59:59Z",
    "completedAt": null,
    "categories": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF0000"
      }
    ],
    "createdAt": "2026-01-26T10:00:00Z",
    "updatedAt": "2026-01-26T10:00:00Z"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 资源不存在 (404)**:
```json
{
  "code": 404,
  "message": "待办事项不存在",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 无权限访问 (403)**:
```json
{
  "code": 403,
  "message": "无权限访问该待办事项",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 3.3 创建待办事项

**接口描述**: 创建新的待办事项

**请求信息**:
```
POST /api/v1/todos
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**:
```json
{
  "title": "完成项目文档",
  "description": "编写详细的技术文档",
  "priority": "HIGH",
  "dueDate": "2026-01-30T23:59:59Z",
  "categoryIds": [1, 2]
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| title | String | ✓ | 标题 | 1-200 字符 |
| description | String | ✗ | 描述 | 最多 1000 字符 |
| priority | String | ✗ | 优先级 | HIGH/MEDIUM/LOW,默认 MEDIUM |
| dueDate | DateTime | ✗ | 截止日期 | ISO 8601 格式 |
| categoryIds | Array | ✗ | 分类 ID 列表 | 数组,最多 10 个 |

**响应示例 - 成功 (201)**:
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写详细的技术文档",
    "status": "PENDING",
    "priority": "HIGH",
    "dueDate": "2026-01-30T23:59:59Z",
    "completedAt": null,
    "categories": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF0000"
      }
    ],
    "createdAt": "2026-01-26T12:00:00Z",
    "updatedAt": "2026-01-26T12:00:00Z"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 参数验证失败 (400)**:
```json
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "title",
      "message": "标题不能为空"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 3.4 更新待办事项

**接口描述**: 更新待办事项信息

**请求信息**:
```
PUT /api/v1/todos
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**:
```json
{
  "id": 1,
  "title": "完成项目文档(更新)",
  "description": "编写详细的技术文档,包括API和数据库设计",
  "priority": "HIGH",
  "dueDate": "2026-01-31T23:59:59Z",
  "categoryIds": [1]
}
```

**请求参数说明**: 与创建接口相同,但需要提供 `id` 字段

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "完成项目文档(更新)",
    "description": "编写详细的技术文档,包括API和数据库设计",
    "status": "PENDING",
    "priority": "HIGH",
    "dueDate": "2026-01-31T23:59:59Z",
    "categories": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF0000"
      }
    ],
    "updatedAt": "2026-01-26T12:30:00Z"
  },
  "timestamp": "2026-01-26T12:30:00Z"
}
```

**响应示例 - 资源不存在 (404)**:
```json
{
  "code": 404,
  "message": "待办事项不存在",
  "timestamp": "2026-01-26T12:30:00Z"
}
```

---

### 3.5 删除待办事项

**接口描述**: 批量删除待办事项(软删除)

**请求信息**:
```
DELETE /api/v1/todos/{ids}
Authorization: Bearer <token>
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | String | ✓ | 待办事项 ID 列表,逗号分隔 | 1,2,3 |

**请求示例**:
```
DELETE /api/v1/todos/1,2,3
```

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": {
    "deletedCount": 3
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 部分删除失败 (207)**:
```json
{
  "code": 207,
  "message": "部分删除成功",
  "data": {
    "successCount": 2,
    "failedCount": 1,
    "failedIds": [999],
    "reason": "ID 999 不存在或无权限"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 3.6 切换待办状态

**接口描述**: 切换待办事项的完成状态

**请求信息**:
```
PATCH /api/v1/todos/{id}/toggle
Content-Type: application/json
Authorization: Bearer <token>
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | ✓ | 待办事项 ID |

**响应示例 - 标记为完成 (200)**:
```json
{
  "code": 200,
  "message": "已标记为完成",
  "data": {
    "id": 1,
    "status": "DONE",
    "completedAt": "2026-01-26T12:00:00Z"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 标记为待处理 (200)**:
```json
{
  "code": 200,
  "message": "已标记为待处理",
  "data": {
    "id": 1,
    "status": "PENDING",
    "completedAt": null
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

## 4. 分类管理 API

### 4.1 查询分类列表

**接口描述**: 查询当前用户的分类列表

**请求信息**:
```
GET /api/v1/categories/list
Authorization: Bearer <token>
```

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sortBy | String | ✗ | 排序字段: name/createdAt,默认 createdAt |
| sortOrder | String | ✗ | 排序方向: asc/desc,默认 desc |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF0000",
      "todoCount": 10,
      "createdAt": "2026-01-26T10:00:00Z"
    },
    {
      "id": 2,
      "name": "个人",
      "color": "#00FF00",
      "todoCount": 5,
      "createdAt": "2026-01-26T11:00:00Z"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 4.2 获取分类详情

**接口描述**: 根据 ID 获取分类详情

**请求信息**:
```
GET /api/v1/categories/{id}
Authorization: Bearer <token>
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | ✓ | 分类 ID |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF0000",
    "todoCount": 10,
    "createdAt": "2026-01-26T10:00:00Z",
    "updatedAt": "2026-01-26T10:00:00Z"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 4.3 创建分类

**接口描述**: 创建新分类

**请求信息**:
```
POST /api/v1/categories
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**:
```json
{
  "name": "工作",
  "color": "#FF0000"
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| name | String | ✓ | 分类名称 | 1-50 字符,用户内唯一 |
| color | String | ✗ | 颜色标识 | 十六进制颜色代码,默认 #000000 |

**响应示例 - 成功 (201)**:
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF0000",
    "todoCount": 0,
    "createdAt": "2026-01-26T12:00:00Z"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 分类名称已存在 (409)**:
```json
{
  "code": 409,
  "message": "分类名称已存在",
  "errors": [
    {
      "field": "name",
      "message": "该分类名称已被使用"
    }
  ],
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 4.4 更新分类

**接口描述**: 更新分类信息

**请求信息**:
```
PUT /api/v1/categories
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**:
```json
{
  "id": 1,
  "name": "工作任务",
  "color": "#FF5733"
}
```

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "name": "工作任务",
    "color": "#FF5733",
    "todoCount": 10,
    "updatedAt": "2026-01-26T12:30:00Z"
  },
  "timestamp": "2026-01-26T12:30:00Z"
}
```

---

### 4.5 删除分类

**接口描述**: 批量删除分类

**请求信息**:
```
DELETE /api/v1/categories/{ids}
Authorization: Bearer <token>
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | String | ✓ | 分类 ID 列表,逗号分隔 | 1,2,3 |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": {
    "deletedCount": 3
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 分类被使用无法删除 (409)**:
```json
{
  "code": 409,
  "message": "分类已被使用,无法删除",
  "data": {
    "failedIds": [1, 2],
    "reason": "有关联的待办事项"
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

## 5. 用户管理 API

### 5.1 获取用户信息

**接口描述**: 获取当前登录用户的个人信息

**请求信息**:
```
GET /api/v1/user/profile
Authorization: Bearer <token>
```

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "userId": 123,
    "username": "testuser",
    "createdAt": "2026-01-01T10:00:00Z",
    "statistics": {
      "totalTodos": 100,
      "completedTodos": 60,
      "pendingTodos": 40,
      "completionRate": 0.6
    }
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 5.2 更新用户信息

**接口描述**: 更新用户信息(当前版本不支持修改用户名)

**请求信息**:
```
PUT /api/v1/user/profile
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**: 当前版本为空,预留扩展

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "更新成功",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

### 5.3 修改密码

**接口描述**: 修改用户密码

**请求信息**:
```
PUT /api/v1/user/password
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**:
```json
{
  "oldPassword": "Test1234",
  "newPassword": "NewPass567",
  "confirmPassword": "NewPass567"
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| oldPassword | String | ✓ | 旧密码 | 必须正确 |
| newPassword | String | ✓ | 新密码 | 8-20 字符,必须包含字母和数字 |
| confirmPassword | String | ✓ | 确认新密码 | 必须与 newPassword 相同 |

**响应示例 - 成功 (200)**:
```json
{
  "code": 200,
  "message": "密码修改成功,请重新登录",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 旧密码错误 (400)**:
```json
{
  "code": 400,
  "message": "旧密码错误",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

**响应示例 - 两次密码不一致 (400)**:
```json
{
  "code": 400,
  "message": "两次输入的密码不一致",
  "timestamp": "2026-01-26T12:00:00Z"
}
```

---

## 6. 数据字典

### 6.1 待办事项状态 (TodoStatus)

| 值 | 说明 | 描述 |
|----|------|------|
| PENDING | 待处理 | 待办事项未完成 |
| DONE | 已完成 | 待办事项已完成 |

### 6.2 待办事项优先级 (TodoPriority)

| 值 | 说明 | 描述 | 权重 |
|----|------|------|------|
| HIGH | 高 | 高优先级 | 3 |
| MEDIUM | 中 | 中优先级(默认) | 2 |
| LOW | 低 | 低优先级 | 1 |

### 6.3 颜色值规范 (Color)

分类颜色使用十六进制颜色代码:
- 格式: `#RRGGBB`
- 示例: `#FF0000`(红), `#00FF00`(绿), `#0000FF`(蓝)
- 默认值: `#000000`(黑)

---

## 7. 错误码说明

### 7.1 通用错误码

| 错误码 | 说明 | HTTP 状态码 |
|--------|------|-------------|
| 200 | 操作成功 | 200 |
| 400 | 参数错误 | 400 |
| 401 | 未认证 | 401 |
| 403 | 无权限 | 403 |
| 404 | 资源不存在 | 404 |
| 409 | 资源冲突 | 409 |
| 500 | 服务器错误 | 500 |
| 503 | 服务不可用 | 503 |

### 7.2 业务错误码

| 错误码 | 说明 | HTTP 状态码 |
|--------|------|-------------|
| 1001 | 用户名已存在 | 409 |
| 1002 | 用户名或密码错误 | 401 |
| 1003 | 账户已锁定 | 403 |
| 1004 | Token 无效或已过期 | 401 |
| 2001 | 待办事项不存在 | 404 |
| 2002 | 无权限访问该待办事项 | 403 |
| 3001 | 分类名称已存在 | 409 |
| 3002 | 分类已被使用,无法删除 | 409 |
| 3003 | 分类不存在 | 404 |

---

## 8. API 文档访问

### 8.1 Swagger UI

**访问地址**:
```
http://localhost:8080/swagger-ui.html
```

**功能**:
- 在线测试 API
- 查看请求/响应示例
- 查看数据模型定义
- 下载 API 文档

### 8.2 OpenAPI JSON

**访问地址**:
```
http://localhost:8080/v3/api-docs
```

**用途**:
- 自动生成客户端 SDK
- API 版本管理
- API 文档集成

---

## 9. API 使用示例

### 9.1 完整的用户认证流程

```bash
# 1. 用户注册
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test1234",
    "confirmPassword": "Test1234"
  }'

# 响应: 获取 token
# {
#   "code": 201,
#   "data": {
#     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
#   }
# }

# 2. 使用 token 访问受保护的 API
curl -X GET http://localhost:8080/api/v1/todos/list \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 3. 创建待办事项
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成项目文档",
    "priority": "HIGH"
  }'

# 4. 刷新 token
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'

# 5. 登出
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 9.2 待办事项管理流程

```bash
# 1. 创建待办事项
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成 API 设计文档",
    "description": "编写详细的 API 规范",
    "priority": "HIGH",
    "dueDate": "2026-01-30T23:59:59Z",
    "categoryIds": [1]
  }'

# 2. 查询待办列表(过滤高优先级)
curl -X GET "http://localhost:8080/api/v1/todos/list?priority=HIGH&page=1&size=20" \
  -H "Authorization: Bearer <token>"

# 3. 查看待办详情
curl -X GET http://localhost:8080/api/v1/todos/1 \
  -H "Authorization: Bearer <token>"

# 4. 更新待办事项
curl -X PUT http://localhost:8080/api/v1/todos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "title": "完成 API 设计文档(已更新)",
    "priority": "MEDIUM"
  }'

# 5. 标记为完成
curl -X PATCH http://localhost:8080/api/v1/todos/1/toggle \
  -H "Authorization: Bearer <token>"

# 6. 批量删除
curl -X DELETE http://localhost:8080/api/v1/todos/1,2,3 \
  -H "Authorization: Bearer <token>"
```

---

## 10. 注意事项

### 10.1 认证要求

除了认证模块的注册和登录接口外,所有接口都需要在请求头中携带 JWT Token:
```
Authorization: Bearer <token>
```

### 10.2 时间格式

所有时间字段使用 **ISO 8601** 格式:
```
2026-01-26T12:00:00Z
```

时区统一使用 **UTC**。

### 10.3 分页默认值

- `page`: 默认 1
- `size`: 默认 20,最大 100

### 10.4 数据隔离

所有数据查询和操作都自动基于当前登录用户进行过滤,用户无法访问其他用户的数据。

### 10.5 软删除

待办事项和分类的删除操作为软删除,数据不会从数据库中物理删除,只是标记为已删除。

---

## 11. 相关文档

- 数据模型设计: `docs/detailed-design/data-models.md`
- 数据库设计: `docs/detailed-design/database-design.md`
- 类设计: `docs/detailed-design/class-design.md`
- 架构设计: `docs/architecture/architecture.md`
- 需求规格: `docs/requirements/requirements-spec.md`

---

## 批准

| 角色 | 姓名 | 签名 | 日期 |
|------|------|------|------|
| 技术负责人 | | | |
| 后端开发负责人 | | | |
| 前端开发负责人 | | | |
