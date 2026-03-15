# API 设计文档模板

## 1. API 概述

### 1.1 基础信息

| 项目 | 值 |
|------|-----|
| 基础路径 | /api/v1 |
| 认证方式 | Bearer Token |
| 响应格式 | JSON |

### 1.2 通用响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": "2026-03-16T10:00:00Z"
}
```

### 1.3 错误码定义

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 1001 | 参数错误 |
| 1002 | 认证失败 |
| 1003 | 权限不足 |
| 2001 | 资源不存在 |
| 5001 | 服务器错误 |

---

## 2. API 端点

### 2.1 [模块名称]

#### 创建 [资源]

```
POST /api/v1/{resource}
```

**请求头**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer {token} |
| Content-Type | string | 是 | application/json |

**请求体**

```json
{
  "field1": "value1",
  "field2": "value2"
}
```

**响应**

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": "xxx",
    "field1": "value1",
    "field2": "value2",
    "createdAt": "2026-03-16T10:00:00Z"
  }
}
```

#### 查询 [资源] 列表

```
GET /api/v1/{resource}
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 20 |
| keyword | string | 否 | 搜索关键词 |

**响应**

```json
{
  "code": 0,
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "size": 20
  }
}
```

#### 查询 [资源] 详情

```
GET /api/v1/{resource}/{id}
```

#### 更新 [资源]

```
PUT /api/v1/{resource}/{id}
```

#### 删除 [资源]

```
DELETE /api/v1/{resource}/{id}
```

---

## 3. 数据模型

### 3.1 [资源名称]

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | string | 是 | 唯一标识 |
| name | string | 是 | 名称 |
| createdAt | datetime | 是 | 创建时间 |
| updatedAt | datetime | 是 | 更新时间 |

---

## 4. 安全考虑

- [ ] 所有接口需要认证
- [ ] 敏感数据需要加密
- [ ] 输入参数需要验证
- [ ] 需要权限检查

---

## 5. 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 1.0.0 | 2026-03-16 | 初始版本 | |
