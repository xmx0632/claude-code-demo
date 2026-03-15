# API 规范

## RESTful 原则

### HTTP 方法映射

| 方法 | 操作 | 幂等 | 示例 |
|------|------|------|------|
| GET | 查询 | 是 | `GET /api/users` |
| POST | 创建 | 否 | `POST /api/users` |
| PUT | 全量更新 | 是 | `PUT /api/users/1` |
| PATCH | 部分更新 | 是 | `PATCH /api/users/1` |
| DELETE | 删除 | 是 | `DELETE /api/users/1` |

### URL 设计规范

```
# 资源命名使用名词复数
GET /api/users          # 正确
GET /api/user           # 错误

# 层级不超过 3 层
GET /api/users/1/orders         # 正确
GET /api/users/1/orders/2/items # 错误

# 使用查询参数过滤
GET /api/users?status=active&age_gt=18

# 分页
GET /api/users?page=1&size=20

# 排序
GET /api/users?sort=createdAt:desc
```

## 请求格式

### 请求头

```
Content-Type: application/json
Authorization: Bearer {token}
Accept: application/json
```

### 请求体 (POST/PUT)

```json
{
  "userName": "zhangsan",
  "email": "zhangsan@example.com",
  "phoneNumber": "13800138000"
}
```

## 响应格式

### 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "userName": "zhangsan"
  }
}
```

### 列表响应

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {"id": 1, "userName": "zhangsan"},
    {"id": 2, "userName": "lisi"}
  ],
  "total": 100
}
```

### 错误响应

```json
{
  "code": 400,
  "msg": "请求参数错误",
  "data": {
    "fieldName": "用户名不能为空"
  }
}
```

## 状态码

| 状态码 | 说明 | 使用场景 |
|--------|------|----------|
| 200 | 成功 | 请求成功 |
| 201 | 已创建 | POST 创建成功 |
| 204 | 无内容 | DELETE 成功 |
| 400 | 请求错误 | 参数验证失败 |
| 401 | 未认证 | Token 无效 |
| 403 | 禁止访问 | 无权限 |
| 404 | 未找到 | 资源不存在 |
| 500 | 服务器错误 | 系统异常 |

## 分页规范

### 请求参数

```
page: 页码，从 1 开始
size: 每页数量，默认 20，最大 100
sort: 排序字段，如 create_time
order: 排序方向，asc/desc
```

### 响应结构

```json
{
  "code": 200,
  "msg": "success",
  "rows": [...],
  "total": 100,
  "page": 1,
  "size": 20
}
```

## 版本管理

### URL 版本

```
/api/v1/users
/api/v2/users
```

### Header 版本

```
Accept: application/vnd.myapi.v1+json
```

## API 文档注解

### Controller 示例

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户增删改查")
public class UserController {

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    @Parameter(name = "id", description = "用户ID", required = true)
    public R<UserVO> getById(@PathVariable Long id) {
        return R.ok(userService.getById(id));
    }
}
```

---

**最后更新**: 2026-03-15
