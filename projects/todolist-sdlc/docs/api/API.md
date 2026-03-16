# TodoList API 文档

| 版本 | 1.0.0 |
|------|-------|
| 基础URL | `http://localhost:8080` |
| 内容类型 | `application/json` |
| 字符编码 | `UTF-8` |

---

## 目录

- [认证接口](#认证接口)
- [任务标签管理](#任务标签管理)
- [任务管理](#任务管理)
- [任务标签关联](#任务标签关联)

---

## 统一响应格式

所有接口返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200 表示成功 |
| message | String | 响应消息 |
| data | Object/Array | 响应数据 |

---

## 认证接口

### 1. 用户注册

**接口**: `POST /api/auth/register`

**描述**: 使用邮箱和密码注册新用户

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | String | 是 | 邮箱地址 |
| password | String | 是 | 密码（6-20位） |
| nickname | String | 否 | 昵称（最长50字符） |

**请求示例**:

```json
{
  "email": "user@example.com",
  "password": "123456",
  "nickname": "张三"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "张三",
    "avatar": null,
    "createdAt": "2026-03-16T10:00:00"
  }
}
```

---

### 2. 用户登录

**接口**: `POST /api/auth/login`

**描述**: 使用邮箱和密码登录，返回 JWT Token

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | String | 是 | 邮箱地址 |
| password | String | 是 | 密码 |

**请求示例**:

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "nickname": "张三",
      "avatar": null,
      "createdAt": "2026-03-16T10:00:00"
    }
  }
}
```

**认证方式**: 后续请求需要在请求头中携带 Token

```
Authorization: Bearer {token}
```

---

### 3. 获取当前用户信息

**接口**: `GET /api/auth/info`

**描述**: 获取当前登录用户的详细信息

**请求头**:

```
Authorization: Bearer {token}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "张三",
    "avatar": null,
    "createdAt": "2026-03-16T10:00:00"
  }
}
```

---

### 4. 退出登录

**接口**: `POST /api/auth/logout`

**描述**: 退出登录（客户端需清除本地 Token）

**响应示例**:

```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

---

## 任务标签管理

### 1. 分页查询标签列表

**接口**: `GET /api/tags`

**描述**: 分页查询当前用户的标签列表

**请求参数** (Query Params):

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| name | String | 否 | - | 标签名称（模糊搜索） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF6B6B",
        "taskCount": 5,
        "createdAt": "2026-03-16T10:00:00",
        "updatedAt": "2026-03-16T10:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

---

### 2. 查询所有标签

**接口**: `GET /api/tags/all`

**描述**: 查询当前用户的所有标签（不分页）

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B",
      "taskCount": 5,
      "createdAt": "2026-03-16T10:00:00",
      "updatedAt": "2026-03-16T10:00:00"
    },
    {
      "id": 2,
      "name": "个人",
      "color": "#4ECDC4",
      "taskCount": 3,
      "createdAt": "2026-03-16T10:00:00",
      "updatedAt": "2026-03-16T10:00:00"
    }
  ]
}
```

---

### 3. 获取标签详情

**接口**: `GET /api/tags/{id}`

**描述**: 获取指定标签的详细信息

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 标签ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF6B6B",
    "taskCount": 5,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T10:00:00"
  }
}
```

---

### 4. 创建标签

**接口**: `POST /api/tags`

**描述**: 创建新标签

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 标签名称（1-20字符） |
| color | String | 否 | 颜色值（#RRGGBB格式） |

**请求示例**:

```json
{
  "name": "工作",
  "color": "#FF6B6B"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF6B6B",
    "taskCount": 0,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T10:00:00"
  }
}
```

---

### 5. 更新标签

**接口**: `PUT /api/tags/{id}`

**描述**: 更新指定标签的信息

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 标签ID |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 标签名称（1-20字符） |
| color | String | 否 | 颜色值（#RRGGBB格式） |

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "name": "工作任务",
    "color": "#FF5555",
    "taskCount": 5,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T11:00:00"
  }
}
```

---

### 6. 删除标签

**接口**: `DELETE /api/tags/{id}`

**描述**: 删除指定标签

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 标签ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 任务管理

### 1. 分页查询任务列表

**接口**: `GET /api/todos`

**描述**: 分页查询当前用户的任务列表，支持多条件筛选

**请求参数** (Query Params):

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| status | Integer | 否 | - | 状态（0-待办, 1-进行中, 2-已完成） |
| priority | Integer | 否 | - | 优先级（0-低, 1-中, 2-高） |
| categoryId | Long | 否 | - | 分类ID |
| keyword | String | 否 | - | 搜索关键词（标题模糊匹配） |
| tagIds | Array | 否 | - | 标签ID列表（AND逻辑） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "完成项目文档",
        "description": "编写API接口文档",
        "status": 0,
        "priority": 2,
        "categoryId": 1,
        "categoryName": "工作",
        "categoryColor": "#FF6B6B",
        "dueDate": "2026-03-20",
        "completedAt": null,
        "createdAt": "2026-03-16T10:00:00",
        "updatedAt": "2026-03-16T10:00:00",
        "tags": [
          {
            "id": 1,
            "name": "工作",
            "color": "#FF6B6B"
          }
        ]
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

---

### 2. 获取任务统计

**接口**: `GET /api/todos/stats`

**描述**: 获取当前用户的任务统计数据

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "all": 20,
    "pending": 5,
    "inProgress": 8,
    "completed": 7
  }
}
```

---

### 3. 获取任务详情

**接口**: `GET /api/todos/{id}`

**描述**: 获取指定任务的详细信息

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 任务ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写API接口文档",
    "status": 0,
    "priority": 2,
    "categoryId": 1,
    "categoryName": "工作",
    "categoryColor": "#FF6B6B",
    "dueDate": "2026-03-20",
    "completedAt": null,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T10:00:00",
    "tags": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF6B6B"
      }
    ]
  }
}
```

---

### 4. 创建任务

**接口**: `POST /api/todos`

**描述**: 创建新任务

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 任务标题（最长200字符） |
| description | String | 否 | 任务描述（最长2000字符） |
| priority | Integer | 否 | 优先级（0-低, 1-中, 2-高，默认0） |
| categoryId | Long | 否 | 分类ID |
| dueDate | String | 否 | 截止日期（YYYY-MM-DD） |

**请求示例**:

```json
{
  "title": "完成项目文档",
  "description": "编写API接口文档",
  "priority": 2,
  "categoryId": 1,
  "dueDate": "2026-03-20"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "description": "编写API接口文档",
    "status": 0,
    "priority": 2,
    "categoryId": 1,
    "categoryName": "工作",
    "categoryColor": "#FF6B6B",
    "dueDate": "2026-03-20",
    "completedAt": null,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T10:00:00",
    "tags": []
  }
}
```

---

### 5. 更新任务

**接口**: `PUT /api/todos/{id}`

**描述**: 更新指定任务的信息

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 任务ID |

**请求参数**: 同创建任务

**响应示例**: 同创建任务

---

### 6. 删除任务

**接口**: `DELETE /api/todos/{id}`

**描述**: 删除指定任务

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 任务ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 7. 完成任务

**接口**: `PUT /api/todos/{id}/complete`

**描述**: 标记任务为已完成

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 任务ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "status": 2,
    "completedAt": "2026-03-16T15:30:00",
    ...
  }
}
```

---

### 8. 取消完成

**接口**: `PUT /api/todos/{id}/uncomplete`

**描述**: 取消任务的完成状态

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 任务ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "完成项目文档",
    "status": 0,
    "completedAt": null,
    ...
  }
}
```

---

## 任务标签关联

### 1. 为任务添加标签

**接口**: `POST /api/todos/{todoId}/tags`

**描述**: 为指定任务添加多个标签（不会重复添加已存在的标签）

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tagIds | Array | 是 | 标签ID列表 |

**请求示例**:

```json
{
  "tagIds": [1, 2, 3]
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "添加成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B"
    },
    {
      "id": 2,
      "name": "紧急",
      "color": "#FF0000"
    }
  ]
}
```

---

### 2. 移除任务标签

**接口**: `DELETE /api/todos/{todoId}/tags/{tagId}`

**描述**: 从指定任务中移除某个标签

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |
| tagId | Long | 标签ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "移除成功",
  "data": null
}
```

---

### 3. 查询任务的所有标签

**接口**: `GET /api/todos/{todoId}/tags`

**描述**: 查询指定任务关联的所有标签

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B"
    },
    {
      "id": 2,
      "name": "紧急",
      "color": "#FF0000"
    }
  ]
}
```

---

### 4. 批量更新任务标签

**接口**: `PUT /api/todos/{todoId}/tags`

**描述**: 完全替换任务的标签（先清空所有标签，再添加新标签）

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |

**请求参数**: 同添加标签

**响应示例**: 同添加标签

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未认证或Token无效 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

**错误响应示例**:

```json
{
  "code": 400,
  "message": "邮箱已被注册",
  "data": null
}
```

---

## 数据模型说明

### 状态枚举

| 任务状态 | 值 | 说明 |
|----------|-----|------|
| 待办 | 0 | 待处理 |
| 进行中 | 1 | 正在处理 |
| 已完成 | 2 | 已完成 |

### 优先级枚举

| 优先级 | 值 | 说明 |
|--------|-----|------|
| 低 | 0 | 低优先级 |
| 中 | 1 | 中优先级 |
| 高 | 2 | 高优先级 |

---

## 更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2026-03-16 | 初始版本 |

---

**生成时间**: 2026-03-16
**文档版本**: 1.0.0
