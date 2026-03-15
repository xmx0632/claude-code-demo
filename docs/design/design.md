# 设计规范

## API 设计原则

### RESTful 规范

```
GET    /api/users          # 列表
GET    /api/users/{id}     # 详情
POST   /api/users          # 创建
PUT    /api/users/{id}     # 全量更新
PATCH  /api/users/{id}     # 部分更新
DELETE /api/users/{id}     # 删除
```

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| URL | 小写，连字符分隔 | `/api/user-profiles` |
| 字段名 | 小写下划线 | `user_name` |
| 枚举 | 大写下划线 | `USER_STATUS_ACTIVE` |

### 响应格式

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "name": "张三"
  }
}
```

## 数据模型设计

### 实体 (Entity)
- 映射数据库表
- 使用 MyBatis-Plus 注解
- 字段与表列一致

### DTO (Data Transfer Object)
- 接收请求参数
- 使用 `@Valid` 验证
- 字段名使用驼峰命名

### VO (View Object)
- 返回响应数据
- 脱敏处理（密码等）
- 格式化（日期、金额）

## 命名约定

### Java 类命名

| 类型 | 后缀 | 示例 |
|------|------|------|
| Controller | `Controller` | `UserController` |
| Service 接口 | `IXxxService` | `IUserService` |
| Service 实现 | `XxxServiceImpl` | `UserServiceImpl` |
| Mapper | `XxxMapper` | `UserMapper` |
| Entity | 实体名 | `User` |
| DTO | `XxxDTO` | `UserDTO` |
| VO | `XxxVO` | `UserVO` |
| Query | `XxxQuery` | `UserQuery` |

### 数据库命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 表名 | 小写下划线 | `sys_user` |
| 列名 | 小写下划线 | `user_name` |
| 主键 | `id` | `id` |
| 创建时间 | `create_time` | `create_time` |
| 更新时间 | `update_time` | `update_time` |

## 安全设计

### 认证授权
- JWT Token 认证
- Token 过期时间: 2小时
- Refresh Token: 7天

### 数据验证
```java
@NotNull(message = "用户名不能为空")
@Size(min = 2, max = 20, message = "用户名长度2-20字符")
private String username;

@Email(message = "邮箱格式不正确")
private String email;
```

### 敏感数据脱敏
```java
// 密码不返回
@JsonProperty(ignore = true)
private String password;

// 手机号脱敏
public String getPhone() {
    return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
}
```

## UI 设计规范

### 线框图要求
- 明确页面布局
- 标注交互元素
- 说明数据流向

### 原型规范
- 使用统一的组件库
- 保持视觉一致性
- 标注交互状态

---

**最后更新**: 2026-03-15
