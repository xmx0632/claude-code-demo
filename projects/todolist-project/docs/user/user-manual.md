# TodoList 待办事项管理系统 - 用户手册

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0.0
> **最后更新**: 2026-01-26
> **目标读者**: 系统终端用户

---

## 目录

1. [系统概述](#1-系统概述)
2. [快速开始](#2-快速开始)
3. [功能说明](#3-功能说明)
4. [API 使用指南](#4-api-使用指南)
5. [常见问题](#5-常见问题)
6. [故障排除](#6-故障排除)
7. [最佳实践](#7-最佳实践)

---

## 1. 系统概述

### 1.1 系统简介

TodoList 待办事项管理系统是一个轻量级的任务管理工具，帮助用户高效地组织和管理日常任务。系统采用 RESTful API 架构，支持多平台访问（Web、移动端、桌面客户端）。

### 1.2 核心功能

- ✅ **用户管理**: 注册、登录、密码修改
- ✅ **待办事项管理**: 创建、编辑、删除、标记完成
- ✅ **分类管理**: 自定义分类和颜色标签
- ✅ **搜索过滤**: 按状态、优先级、日期、分类过滤
- ✅ **数据安全**: 用户数据隔离，密码加密存储

### 1.3 技术特点

- 基于 JWT Token 的无状态认证
- RESTful API 设计，易于集成
- 完整的 API 文档（Swagger/Knife4j）
- 响应速度快，支持高并发

---

## 2. 快速开始

### 2.1 访问系统

**系统地址**: `http://your-server:8080`

**API 文档**: `http://your-server:8080/doc.html`

**默认账户**:
- 用户名: `admin`
- 密码: `admin123`

### 2.2 注册账户

如果您是首次使用，请按以下步骤注册：

#### 步骤 1: 准备注册信息

- 用户名（3-50 字符，只能包含字母、数字、下划线）
- 密码（8-20 字符，必须包含字母和数字）

#### 步骤 2: 发送注册请求

**使用 curl**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "myusername",
    "password": "MyPass123",
    "confirmPassword": "MyPass123"
  }'
```

**使用 Swagger UI**:
1. 访问 `http://localhost:8080/doc.html`
2. 找到"认证模块" → "用户注册"
3. 点击"试一试"
4. 填写请求体并执行

#### 步骤 3: 保存 Token

注册成功后，系统会返回 Token，请妥善保管：

```json
{
  "code": 201,
  "message": "注册成功",
  "data": {
    "userId": 123,
    "username": "myusername",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 2.3 登录系统

**使用 curl**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "myusername",
    "password": "MyPass123"
  }'
```

**成功响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 123,
    "username": "myusername",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  }
}
```

### 2.4 使用 Token 访问 API

在后续的 API 请求中，需要在请求头中携带 Token：

```bash
curl -X GET http://localhost:8080/api/v1/todos/list \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 3. 功能说明

### 3.1 待办事项管理

#### 3.1.1 创建待办事项

**功能描述**: 创建新的待办事项，记录需要完成的任务。

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| title | String | ✓ | 标题 | 1-200 字符 |
| description | String | ✗ | 描述 | 最多 1000 字符 |
| priority | String | ✗ | 优先级 | HIGH/MEDIUM/LOW，默认 MEDIUM |
| dueDate | DateTime | ✗ | 截止日期 | ISO 8601 格式 |
| categoryIds | Array | ✗ | 分类 ID 列表 | 数组，最多 10 个 |

**示例请求**:
```bash
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成项目报告",
    "description": "编写 Q1 季度项目进展报告，包括数据分析和图表",
    "priority": "HIGH",
    "dueDate": "2026-01-31T23:59:59Z",
    "categoryIds": [1, 2]
  }'
```

**成功响应**:
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 1,
    "title": "完成项目报告",
    "description": "编写 Q1 季度项目进展报告，包括数据分析和图表",
    "status": "PENDING",
    "priority": "HIGH",
    "dueDate": "2026-01-31T23:59:59Z",
    "completedAt": null,
    "categories": [
      {
        "id": 1,
        "name": "工作",
        "color": "#FF5733"
      }
    ],
    "createdAt": "2026-01-26T10:00:00Z",
    "updatedAt": "2026-01-26T10:00:00Z"
  }
}
```

**注意事项**:
- 标题不能为空
- 优先级默认为 MEDIUM
- 截止日期格式必须正确
- 分类 ID 必须是已存在的分类

#### 3.1.2 查询待办列表

**功能描述**: 查询当前用户的待办事项列表，支持分页、搜索和过滤。

**查询参数**:

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| page | Integer | ✗ | 页码，从 1 开始 | 1 |
| size | Integer | ✗ | 每页大小，默认 20 | 20 |
| status | String | ✗ | 状态过滤 | PENDING/DONE |
| priority | String | ✗ | 优先级过滤 | HIGH/MEDIUM/LOW |
| dueDateFilter | String | ✗ | 截止日期过滤 | today/week/month/overdue |
| categoryId | Long | ✗ | 分类 ID | 1 |
| keyword | String | ✗ | 搜索关键词 | 项目 |
| sortBy | String | ✗ | 排序字段 | createdAt/dueDate/priority |
| sortOrder | String | ✗ | 排序方向 | asc/desc |

**示例请求**:

```bash
# 查询所有待处理的高优先级任务
curl -X GET "http://localhost:8080/api/v1/todos/list?status=PENDING&priority=HIGH&page=1&size=20" \
  -H "Authorization: Bearer <token>"

# 查询今天截止的任务
curl -X GET "http://localhost:8080/api/v1/todos/list?dueDateFilter=today" \
  -H "Authorization: Bearer <token>"

# 搜索包含"报告"的任务
curl -X GET "http://localhost:8080/api/v1/todos/list?keyword=报告" \
  -H "Authorization: Bearer <token>"
```

**成功响应**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "完成项目报告",
        "status": "PENDING",
        "priority": "HIGH",
        "dueDate": "2026-01-31T23:59:59Z"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 20,
    "pages": 5
  }
}
```

#### 3.1.3 查看待办详情

**功能描述**: 根据 ID 获取单个待办事项的详细信息。

**示例请求**:
```bash
curl -X GET http://localhost:8080/api/v1/todos/1 \
  -H "Authorization: Bearer <token>"
```

#### 3.1.4 更新待办事项

**功能描述**: 修改已创建的待办事项信息。

**示例请求**:
```bash
curl -X PUT http://localhost:8080/api/v1/todos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "title": "完成项目报告(已更新)",
    "priority": "MEDIUM",
    "dueDate": "2026-02-01T23:59:59Z"
  }'
```

**注意事项**:
- 只能修改自己的待办事项
- 需要提供待办事项的 ID
- 系统使用乐观锁，并发修改时可能失败

#### 3.1.5 删除待办事项

**功能描述**: 批量删除不需要的待办事项（软删除）。

**示例请求**:
```bash
# 删除单个待办
curl -X DELETE http://localhost:8080/api/v1/todos/1 \
  -H "Authorization: Bearer <token>"

# 批量删除多个待办
curl -X DELETE http://localhost:8080/api/v1/todos/1,2,3 \
  -H "Authorization: Bearer <token>"
```

**成功响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": {
    "deletedCount": 3
  }
}
```

#### 3.1.6 标记完成状态

**功能描述**: 快速切换待办事项的完成状态。

**示例请求**:
```bash
curl -X PATCH http://localhost:8080/api/v1/todos/1/toggle \
  -H "Authorization: Bearer <token>"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "已标记为完成",
  "data": {
    "id": 1,
    "status": "DONE",
    "completedAt": "2026-01-26T12:00:00Z"
  }
}
```

### 3.2 分类管理

#### 3.2.1 创建分类

**功能描述**: 创建自定义分类，用于组织待办事项。

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| name | String | ✓ | 分类名称 | 1-50 字符，用户内唯一 |
| color | String | ✗ | 颜色标识 | 十六进制，默认 #000000 |

**示例请求**:
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "工作",
    "color": "#FF5733"
  }'
```

**常用颜色参考**:
- 红色: `#FF5733`
- 绿色: `#33FF57`
- 蓝色: `#3357FF`
- 黄色: `#F3FF33`
- 紫色: `#A833FF`

#### 3.2.2 查询分类列表

**示例请求**:
```bash
curl -X GET http://localhost:8080/api/v1/categories/list \
  -H "Authorization: Bearer <token>"
```

**成功响应**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF5733",
      "todoCount": 10,
      "createdAt": "2026-01-26T10:00:00Z"
    },
    {
      "id": 2,
      "name": "个人",
      "color": "#33FF57",
      "todoCount": 5,
      "createdAt": "2026-01-26T11:00:00Z"
    }
  ]
}
```

#### 3.2.3 更新分类

**示例请求**:
```bash
curl -X PUT http://localhost:8080/api/v1/categories \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "工作任务",
    "color": "#FF5733"
  }'
```

#### 3.2.4 删除分类

**注意事项**: 分类已被待办事项使用时，无法删除。

**示例请求**:
```bash
curl -X DELETE http://localhost:8080/api/v1/categories/1,2 \
  -H "Authorization: Bearer <token>"
```

**错误响应（分类被使用）**:
```json
{
  "code": 409,
  "message": "分类已被使用，无法删除",
  "data": {
    "failedIds": [1, 2],
    "reason": "有关联的待办事项"
  }
}
```

### 3.3 个人信息管理

#### 3.3.1 查看个人信息

**示例请求**:
```bash
curl -X GET http://localhost:8080/api/v1/user/profile \
  -H "Authorization: Bearer <token>"
```

**成功响应**:
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
  }
}
```

#### 3.3.2 修改密码

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | String | ✓ | 旧密码 |
| newPassword | String | ✓ | 新密码（8-20 字符，字母+数字） |
| confirmPassword | String | ✓ | 确认新密码 |

**示例请求**:
```bash
curl -X PUT http://localhost:8080/api/v1/user/password \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "OldPass123",
    "newPassword": "NewPass456",
    "confirmPassword": "NewPass456"
  }'
```

**注意事项**:
- 修改密码成功后需要重新登录
- 新密码必须符合复杂度要求
- 旧密码必须正确

---

## 4. API 使用指南

### 4.1 认证机制

系统使用 **JWT (JSON Web Token)** 进行认证。

#### Token 结构
```
Header.Payload.Signature
```

#### Token 使用方式

**在请求头中携带**:
```http
Authorization: Bearer <your-token-here>
```

#### Token 有效期

- **Access Token**: 24 小时
- **Refresh Token**: 7 天

#### Token 刷新

当 Access Token 过期时，使用 Refresh Token 获取新的 Token：

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

### 4.2 统一响应格式

所有 API 接口都使用统一的响应格式。

#### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 返回数据
  },
  "timestamp": "2026-01-26T12:00:00Z"
}
```

#### 错误响应

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

#### 分页响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [...],
    "total": 100,
    "page": 1,
    "size": 20,
    "pages": 5
  }
}
```

### 4.3 HTTP 状态码

| 状态码 | 说明 | 使用场景 |
|--------|------|----------|
| 200 | OK | 查询成功、更新成功 |
| 201 | Created | 创建成功 |
| 400 | Bad Request | 参数错误、验证失败 |
| 401 | Unauthorized | 未认证、Token 无效 |
| 403 | Forbidden | 无权限访问 |
| 404 | Not Found | 资源不存在 |
| 409 | Conflict | 资源冲突（如用户名重复） |
| 500 | Internal Server Error | 服务器内部错误 |

### 4.4 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户名已存在 |
| 1002 | 用户名或密码错误 |
| 1003 | 账户已锁定 |
| 1004 | Token 无效或已过期 |
| 2001 | 待办事项不存在 |
| 2002 | 无权限访问该待办事项 |
| 3001 | 分类名称已存在 |
| 3002 | 分类已被使用，无法删除 |

### 4.5 使用 Swagger UI

Swagger UI 提供了可视化的 API 测试界面。

**访问地址**: `http://localhost:8080/doc.html`

**使用步骤**:
1. 在浏览器中打开 Swagger UI
2. 找到需要测试的接口
3. 点击"试一试"按钮
4. 填写请求参数（如需要认证，先在右上角输入 Token）
5. 点击"执行"按钮
6. 查看响应结果

---

## 5. 常见问题

### 5.1 账户相关

#### Q1: 忘记密码怎么办？

**A**: 当前版本暂不支持自助找回密码功能。请联系系统管理员重置密码。

#### Q2: 用户名有什么要求？

**A**:
- 长度: 3-50 字符
- 只能包含字母、数字、下划线
- 不能与其他用户重复

#### Q3: 密码有什么要求？

**A**:
- 长度: 8-20 字符
- 必须包含字母和数字
- 建议使用大小写字母+数字+符号组合

#### Q4: 账户被锁定怎么办？

**A**:
- 连续登录失败 5 次后，账户会被锁定 30 分钟
- 等待 30 分钟后自动解锁
- 或联系管理员手动解锁

### 5.2 待办事项相关

#### Q5: 待办事项可以设置提醒吗？

**A**: 当前版本暂不支持提醒功能。此功能已在计划中。

#### Q6: 如何批量操作待办事项？

**A**:
- 批量删除: 使用 `DELETE /api/v1/todos/{ids}` 接口，多个 ID 用逗号分隔
- 批量标记完成: 需要逐个调用切换接口

#### Q7: 删除的待办事项可以恢复吗？

**A**: 当前版本不支持恢复功能。删除是软删除，但暂未提供恢复接口。

#### Q8: 待办事项可以分享给其他用户吗？

**A**: 当前版本不支持。每个用户只能看到和管理自己的待办事项。

### 5.3 分类相关

#### Q9: 一个待办事项可以属于多个分类吗？

**A**: 可以。创建或更新待办事项时，可以指定多个分类 ID。

#### Q10: 如何删除已有待办事项的分类？

**A**: 需要先移除该分类下的所有待办事项，或更改它们的分类，然后才能删除。

### 5.4 API 使用相关

#### Q11: Token 过期后怎么办？

**A**:
- 方法 1: 使用 Refresh Token 刷新（推荐）
- 方法 2: 重新登录获取新 Token

#### Q12: 如何获取完整的 API 文档？

**A**: 访问 `http://localhost:8080/doc.html` 查看完整的 Swagger 文档。

#### Q13: API 有调用频率限制吗？

**A**: 当前版本没有实现频率限制。但请合理使用，避免频繁调用。

#### Q14: 支持 CORS 跨域访问吗？

**A**: 支持。已配置 CORS 允许跨域访问。

---

## 6. 故障排除

### 6.1 连接问题

#### 问题: 无法连接到服务器

**可能原因**:
1. 服务器未启动
2. 网络连接问题
3. 防火墙阻止
4. 端口配置错误

**解决方案**:
```bash
# 1. 检查服务器是否启动
curl http://localhost:8080/actuator/health

# 2. 检查端口是否被占用
netstat -tunlp | grep 8080

# 3. 检查防火墙
sudo iptables -L -n

# 4. 检查服务器日志
tail -f /var/log/todolist/todolist.log
```

#### 问题: API 响应很慢

**可能原因**:
1. 数据库查询慢
2. 网络延迟
3. 服务器负载高

**解决方案**:
- 检查数据库慢查询日志
- 使用分页减少数据量
- 优化查询条件

### 6.2 认证问题

#### 问题: Token 无效错误 (401)

**可能原因**:
1. Token 已过期
2. Token 格式错误
3. Token 被篡改

**解决方案**:
```bash
# 1. 检查 Token 格式
Authorization: Bearer <token>

# 2. 使用 Refresh Token 刷新
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "<refresh-token>"}'

# 3. 重新登录
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "xxx", "password": "xxx"}'
```

#### 问题: 账户被锁定 (403)

**解决方案**:
- 等待 30 分钟自动解锁
- 或联系管理员解锁

### 6.3 数据操作问题

#### 问题: 创建待办事项失败 (400)

**可能原因**:
1. 参数验证失败
2. 标题为空
3. 分类 ID 不存在

**解决方案**:
```json
// 检查响应中的错误信息
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "title",
      "message": "标题不能为空"
    }
  ]
}
```

#### 问题: 更新失败 (404 或 409)

**可能原因**:
- 404: 待办事项不存在
- 409: 乐观锁冲突（版本号不匹配）

**解决方案**:
- 检查待办事项 ID 是否正确
- 重新获取最新数据后再更新

### 6.4 错误日志查看

**应用日志位置**: `/var/log/todolist/todolist.log`
**错误日志位置**: `/var/log/todolist/todolist-error.log`

```bash
# 查看实时日志
tail -f /var/log/todolist/todolist.log

# 查看错误日志
tail -f /var/log/todolist/todolist-error.log

# 搜索特定错误
grep "ERROR" /var/log/todolist/todolist.log
```

---

## 7. 最佳实践

### 7.1 待办事项管理技巧

#### 1. 使用优先级

- **HIGH**: 紧急且重要的任务
- **MEDIUM**: 重要但不紧急的任务（默认）
- **LOW**: 不重要也不紧急的任务

#### 2. 合理设置截止日期

- 为所有任务设置明确的截止日期
- 使用 `dueDateFilter` 快速查找今天/本周截止的任务

#### 3. 使用分类组织

- 创建有意义的分类（如：工作、个人、学习）
- 使用不同颜色区分分类
- 一个任务可以属于多个分类

#### 4. 定期回顾

- 每周查看一次待办列表
- 完成的任务可以删除
- 过期的任务重新规划

### 7.2 API 使用建议

#### 1. Token 管理

```javascript
// 推荐: 在客户端缓存 Token
localStorage.setItem('token', response.data.token);

// 使用时从缓存读取
const token = localStorage.getItem('token');
axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

// 处理 Token 过期
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response.status === 401) {
      // Token 过期，使用 Refresh Token
      return refreshToken().then(response => {
        // 重新发送原请求
        error.config.headers['Authorization'] = `Bearer ${response.data.token}`;
        return axios(error.config);
      });
    }
    return Promise.reject(error);
  }
);
```

#### 2. 分页查询

```javascript
// 推荐: 使用合理的分页大小
const pageSize = 20; // 推荐 20-50

// 不要一次查询所有数据
// ❌ 错误: GET /api/v1/todos/list?size=10000
// ✅ 正确: GET /api/v1/todos/list?page=1&size=20
```

#### 3. 错误处理

```javascript
// 推荐: 统一的错误处理
axios.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response) {
      // 服务器返回错误
      const { code, message } = error.response.data;
      console.error(`API Error ${code}: ${message}`);

      // 根据错误码处理
      switch (code) {
        case 401:
          // Token 过期，刷新或重新登录
          break;
        case 403:
          // 无权限
          break;
        case 404:
          // 资源不存在
          break;
        default:
          // 其他错误
          break;
      }
    } else if (error.request) {
      // 请求发送但没有收到响应
      console.error('Network Error');
    } else {
      // 其他错误
      console.error('Error', error.message);
    }
    return Promise.reject(error);
  }
);
```

#### 4. 批量操作

```javascript
// 推荐: 批量删除代替逐个删除
// ❌ 错误: 循环删除
for (let id of ids) {
  await deleteTodo(id);
}

// ✅ 正确: 批量删除
await deleteTodos(ids.join(','));
```

### 7.3 安全建议

#### 1. 密码安全

- 不要使用简单的密码（如 123456、password）
- 定期更换密码
- 不要在不安全的网络环境下登录

#### 2. Token 安全

```javascript
// 推荐: 使用 httpOnly Cookie 存储 Token（后端支持）
// 或使用 sessionStorage（关闭浏览器即清除）

// ❌ 不推荐: localStorage (容易受到 XSS 攻击)
localStorage.setItem('token', token);

// ✅ 推荐: sessionStorage
sessionStorage.setItem('token', token);
```

#### 3. 数据验证

```javascript
// 推荐: 前端也进行数据验证
function validateTodo(todo) {
  if (!todo.title || todo.title.length > 200) {
    throw new Error('标题长度必须在 1-200 字符之间');
  }
  if (todo.description && todo.description.length > 1000) {
    throw new Error('描述最多 1000 字符');
  }
  // ... 其他验证
}
```

### 7.4 性能优化

#### 1. 减少不必要的请求

```javascript
// 推荐: 使用缓存
const cache = new Map();

async function getTodo(id) {
  if (cache.has(id)) {
    return cache.get(id);
  }
  const todo = await fetchTodo(id);
  cache.set(id, todo);
  return todo;
}
```

#### 2. 使用节流和防抖

```javascript
// 搜索输入防抖
const searchInput = document.getElementById('search');
const debouncedSearch = debounce(async (keyword) => {
  const results = await searchTodos(keyword);
  renderResults(results);
}, 300);

searchInput.addEventListener('input', (e) => {
  debouncedSearch(e.target.value);
});
```

---

## 附录

### A. 完整 API 列表

详见 Swagger 文档: `http://localhost:8080/doc.html`

### B. 数据字典

#### 待办事项状态 (TodoStatus)

| 值 | 说明 |
|----|------|
| PENDING | 待处理 |
| DONE | 已完成 |

#### 待办事项优先级 (TodoPriority)

| 值 | 说明 |
|----|------|
| HIGH | 高 |
| MEDIUM | 中 |
| LOW | 低 |

### C. 联系支持

如有问题或建议，请联系：
- **技术支持**: support@todolist.com
- **问题反馈**: https://github.com/todolist/issues

---

**文档版本**: 1.0
**最后更新**: 2026-01-26
**下次审核**: 2026-04-26
