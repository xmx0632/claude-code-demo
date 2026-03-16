# 任务标签功能 - API 接口设计

| 版本 | 1.0 |
|------|-----|
| 日期 | 2026-03-16 |

---

## 1. 接口概览

### 1.1 基础信息

- **Base URL**: `/api`
- **认证方式**: JWT Bearer Token
- **请求格式**: `application/json`
- **响应格式**: `application/json`

### 1.2 统一响应格式

```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

| code | 说明 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 2. 标签管理接口

### 2.1 创建标签

```http
POST /api/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数：**
```json
{
  "name": "工作",
  "color": "#FF6B6B"
}
```

**参数说明：**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 标签名称，1-20字符 |
| color | String | 否 | 标签颜色，HEX格式，默认#999999 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "创建成功",
  "data": {
    "id": 1,
    "name": "工作",
    "color": "#FF6B6B",
    "taskCount": 0,
    "createdAt": "2026-03-16T10:00:00"
  }
}
```

**错误响应：**
```json
{
  "code": 400,
  "msg": "标签名称已存在",
  "data": null
}
```

---

### 2.2 更新标签

```http
PUT /api/tags/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数：**
| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 标签ID |

**请求参数：**
```json
{
  "name": "工作事务",
  "color": "#FF5555"
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "更新成功",
  "data": {
    "id": 1,
    "name": "工作事务",
    "color": "#FF5555",
    "taskCount": 5,
    "createdAt": "2026-03-16T10:00:00"
  }
}
```

---

### 2.3 删除标签

```http
DELETE /api/tags/{id}
Authorization: Bearer {token}
```

**路径参数：**
| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 标签ID |

**响应示例：**
```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

**说明：**
- 删除标签时自动删除所有 `t_todo_tag` 关联记录
- 不影响任务本身，只移除标签关联

---

### 2.4 查询标签列表

```http
GET /api/tags?pageNum=1&pageSize=10
Authorization: Bearer {token}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| name | String | 否 | 标签名称（模糊搜索） |

**响应示例：**
```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "name": "工作",
      "color": "#FF6B6B",
      "taskCount": 5,
      "createdAt": "2026-03-16T10:00:00"
    },
    {
      "id": 2,
      "name": "个人",
      "color": "#4ECDC4",
      "taskCount": 3,
      "createdAt": "2026-03-16T11:00:00"
    }
  ],
  "total": 8
}
```

---

### 2.5 查询标签详情

```http
GET /api/tags/{id}
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "查询成功",
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

## 3. 任务标签接口

### 3.1 为任务添加标签

```http
POST /api/todos/{todoId}/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数：**
| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |

**请求参数：**
```json
{
  "tagIds": [1, 3, 5]
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "添加成功",
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

**错误响应：**
```json
{
  "code": 400,
  "msg": "标签已存在于任务上",
  "data": null
}
```

---

### 3.2 移除任务标签

```http
DELETE /api/todos/{todoId}/tags/{tagId}
Authorization: Bearer {token}
```

**路径参数：**
| 参数 | 类型 | 说明 |
|------|------|------|
| todoId | Long | 任务ID |
| tagId | Long | 标签ID |

**响应示例：**
```json
{
  "code": 200,
  "msg": "移除成功",
  "data": null
}
```

---

### 3.3 查询任务的所有标签

```http
GET /api/todos/{todoId}/tags
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "查询成功",
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

### 3.4 批量更新任务标签

```http
PUT /api/todos/{todoId}/tags
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数：**
```json
{
  "tagIds": [1, 3, 5]
}
```

**说明：**
- 完全替换任务的标签列表
- 传入空数组则清空所有标签

**响应示例：**
```json
{
  "code": 200,
  "msg": "更新成功",
  "data": [
    {"id": 1, "name": "工作", "color": "#FF6B6B"},
    {"id": 3, "name": "重要", "color": "#45B7D1"},
    {"id": 5, "name": "紧急", "color": "#EF4444"}
  ]
}
```

---

## 4. 标签筛选接口

### 4.1 按标签筛选任务

```http
GET /api/todos?tagIds=1,2,3&status=0&pageNum=1&pageSize=10
Authorization: Bearer {token}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tagIds | String | 是 | 标签ID列表，逗号分隔（AND逻辑） |
| status | Integer | 否 | 任务状态：0-待办, 1-进行中, 2-已完成 |
| priority | Integer | 否 | 优先级：0-低, 1-中, 2-高 |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

**筛选逻辑：**
- `tagIds` 之间使用 **AND 逻辑**：任务必须包含所有选中的标签
- 其他条件与标签条件也使用 **AND 逻辑**

**响应示例：**
```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "title": "完成项目需求文档并评审",
      "description": "需要完成用户管理模块的详细需求文档...",
      "status": 0,
      "priority": 2,
      "dueDate": "2026-03-20",
      "tags": [
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
      ],
      "createdAt": "2026-03-16T09:00:00",
      "updatedAt": "2026-03-16T09:00:00"
    }
  ],
  "total": 5
}
```

---

## 5. DTO 定义

### 5.1 TagDTO（请求）

```java
@Data
public class TagDTO {
    @NotBlank(message = "标签名称不能为空")
    @Length(min = 1, max = 20, message = "标签名称长度为1-20字符")
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式不正确")
    private String color;
}
```

### 5.2 TagVO（响应）

```java
@Data
public class TagVO {
    private Long id;
    private String name;
    private String color;
    private Integer taskCount;
    private LocalDateTime createdAt;
}
```

### 5.3 TodoVO（带标签）

```java
@Data
public class TodoVO {
    private Long id;
    private String title;
    private String description;
    private Integer status;
    private Integer priority;
    private LocalDate dueDate;
    private List<TagVO> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 6. 错误码定义

| 错误码 | 说明 | 示例 |
|--------|------|------|
| TAG_001 | 标签名称已存在 | 创建标签时名称重复 |
| TAG_002 | 标签不存在 | 更新/删除的标签不存在 |
| TAG_003 | 标签名称重复 | 更新时名称与其他标签重复 |
| TAG_004 | 无权限操作标签 | 尝试操作其他用户的标签 |
| TODO_TAG_001 | 标签已存在 | 任务上已有该标签 |
| TODO_TAG_002 | 标签不存在 | 任务上没有该标签 |
| TODO_TAG_003 | 无权限操作任务标签 | 尝试操作其他任务的标签 |

---

## 7. 接口时序图

### 7.1 标签筛选时序

```mermaid
sequenceDiagram
    participant C as Client
    participant F as Filter
    participant API as API Gateway
    participant S as Service
    participant M as Mapper
    participant DB as Database
    participant Cache as Redis

    C->>F: 选择标签1,2,3
    F->>API: GET /api/todos?tagIds=1,2,3
    API->>S: queryTodos(tagIds)

    S->>Cache: 查询缓存
    alt 缓存命中
        Cache-->>S: 返回结果
    else 缓存未命中
        S->>M: selectByTagIds(tagIds)
        M->>DB: EXISTS 查询
        DB-->>M: Todo列表
        M->>DB: 查询标签关联
        DB-->>M: Tag列表
        M-->>S: TodoWithTags
        S->>Cache: 写入缓存
    end

    S-->>API: TodoVO列表
    API-->>F: JSON响应
    F-->>C: 显示任务列表
```

---

## 8. 变更记录

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| 1.0 | 2026-03-16 | 初始版本 | Claude Code |
