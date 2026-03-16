# 任务标签功能 - API 文档

| 版本 | 1.0 |
|------|-----|
| 发布日期 | 2026-03-16 |
| API 版本 | v1 |

---

## 基础信息

### Base URL

```
开发环境: http://localhost:8080/api
生产环境: https://api.todolist.com/api
```

### 认证方式

```
Authorization: Bearer {access_token}
```

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## API 列表

### 标签管理 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /tags | 创建标签 |
| GET | /tags | 查询标签列表 |
| GET | /tags/all | 查询所有标签 |
| GET | /tags/{id} | 获取标签详情 |
| PUT | /tags/{id} | 更新标签 |
| DELETE | /tags/{id} | 删除标签 |

### 任务标签 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /todos/{todoId}/tags | 为任务添加标签 |
| DELETE | /todos/{todoId}/tags/{tagId} | 移除任务标签 |
| GET | /todos/{todoId}/tags | 查询任务标签 |
| PUT | /todos/{todoId}/tags | 批量更新任务标签 |

---

## API 详情

### 1. 创建标签

**请求**

```http
POST /api/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**

```json
{
  "name": "工作",
  "color": "#FF6B6B"
}
```

**参数说明**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 标签名称，1-20字符 |
| color | String | 否 | 标签颜色，HEX格式 |

**响应**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF6B6B",
    "taskCount": 0,
    "createdAt": "2026-03-16T10:00:00"
  }
}
```

---

### 2. 查询标签列表

**请求**

```http
GET /api/tags?page=1&size=10&name=工作
Authorization: Bearer {token}
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页数量，默认10 |
| name | String | 否 | 标签名称（模糊搜索） |

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF6B6B",
        "taskCount": 5,
        "createdAt": "2026-03-16T10:00:00"
      }
    ],
    "total": 8,
    "size": 10,
    "current": 1
  }
}
```

---

### 3. 查询所有标签

**请求**

```http
GET /api/tags/all
Authorization: Bearer {token}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B",
      "taskCount": 5
    },
    {
      "id": 2,
      "name": "个人",
      "color": "#4ECDC4",
      "taskCount": 3
    }
  ]
}
```

---

### 4. 获取标签详情

**请求**

```http
GET /api/tags/{id}
Authorization: Bearer {token}
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 标签ID |

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF6B6B",
    "taskCount": 5,
    "createdAt": "2026-03-16T10:00:00",
    "updatedAt": "2026-03-16T12:00:00"
  }
}
```

---

### 5. 更新标签

**请求**

```http
PUT /api/tags/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**

```json
{
  "name": "工作事务",
  "color": "#FF5555"
}
```

**响应**

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "name": "工作事务",
    "color": "#FF5555",
    "taskCount": 5,
    "updatedAt": "2026-03-16T14:00:00"
  }
}
```

---

### 6. 删除标签

**请求**

```http
DELETE /api/tags/{id}
Authorization: Bearer {token}
```

**响应**

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 7. 为任务添加标签

**请求**

```http
POST /api/todos/{todoId}/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**

```json
{
  "tagIds": [1, 3, 5]
}
```

**参数说明**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tagIds | Array[Long] | 是 | 标签ID列表 |

**响应**

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
      "id": 3,
      "name": "重要",
      "color": "#45B7D1"
    },
    {
      "id": 5,
      "name": "紧急",
      "color": "#EF4444"
    }
  ]
}
```

---

### 8. 移除任务标签

**请求**

```http
DELETE /api/todos/{todoId}/tags/{tagId}
Authorization: Bearer {token}
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| todoId | Long | 是 | 任务ID |
| tagId | Long | 是 | 标签ID |

**响应**

```json
{
  "code": 200,
  "message": "移除成功",
  "data": null
}
```

---

### 9. 查询任务标签

**请求**

```http
GET /api/todos/{todoId}/tags
Authorization: Bearer {token}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B"
    },
    {
      "id": 3,
      "name": "重要",
      "color": "#45B7D1"
    }
  ]
}
```

---

### 10. 批量更新任务标签

**请求**

```http
PUT /api/todos/{todoId}/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**

```json
{
  "tagIds": [1, 3, 5]
}
```

**说明**
- 完全替换任务的标签列表
- 传入空数组 `[]` 则清空所有标签

**响应**

```json
{
  "code": 200,
  "message": "更新成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B"
    },
    {
      "id": 3,
      "name": "重要",
      "color": "#45B7D1"
    },
    {
      "id": 5,
      "name": "紧急",
      "color": "#EF4444"
    }
  ]
}
```

---

## 错误码

| 错误码 | HTTP状态 | 说明 | 示例 |
|--------|----------|------|------|
| TAG_001 | 409 | 标签名称已存在 | 创建标签时名称重复 |
| TAG_002 | 404 | 标签不存在 | 更新/删除的标签不存在 |
| TAG_003 | 409 | 标签名称重复 | 更新时名称与其他标签重复 |
| TAG_004 | 403 | 无权限操作标签 | 尝试操作其他用户的标签 |
| TODO_TAG_001 | 409 | 标签已存在 | 任务上已有该标签 |
| TODO_TAG_002 | 404 | 标签不存在 | 任务上没有该标签 |
| TODO_TAG_003 | 403 | 无权限操作任务标签 | 尝试操作其他任务的标签 |

**错误响应示例**

```json
{
  "code": 409,
  "message": "标签名称已存在",
  "data": null
}
```

---

## 在线文档

访问 Swagger UI 查看交互式 API 文档：

```
开发环境: http://localhost:8080/doc.html
生产环境: https://api.todolist.com/doc.html
```

---

## 变更记录

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| 1.0 | 2026-03-16 | 初始版本 | Claude Code |
