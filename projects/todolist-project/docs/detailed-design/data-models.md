# 数据模型设计文档 (Data Models)

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

## 1. 数据模型概述

### 1.1 分层设计

系统采用分层的数据模型设计:

```
┌─────────────────────────────────────┐
│     DTO (Data Transfer Object)      │  ← API 请求/响应
├─────────────────────────────────────┤
│      VO (View Object)               │  ← 视图展示
├─────────────────────────────────────┤
│      Entity (实体类)                │  ← 数据库映射
├─────────────────────────────────────┤
│      Query (查询对象)               │  ← 查询条件
└─────────────────────────────────────┘
```

### 1.2 命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| Entity | 实体名 | `User`, `Todo`, `Category` |
| DTO | 实体名 + DTO | `UserDTO`, `TodoDTO` |
| VO | 实体名 + VO | `UserVO`, `TodoVO` |
| Query | 实体名 + Query | `TodoQuery`, `CategoryQuery` |
| 枚举 | 实体名 + 状态名 | `TodoStatus`, `TodoPriority` |

### 1.3 包结构

```
com.example.todolist
├── domain           # 领域层
│   ├── entity       # 实体类
│   ├── enums        # 枚举类
│   └── vo           # 视图对象
├── dto              # 数据传输对象
│   ├── request      # 请求 DTO
│   └── response     # 响应 DTO
└── query            # 查询对象
```

---

## 2. 实体类 (Entity)

### 2.1 User (用户实体)

**完整路径**: `com.example.todolist.domain.entity.User`

**说明**: 用户实体,映射数据库表 `sys_user`

```java
package com.example.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码(加密存储)
     */
    @TableField("password")
    private String password;

    /**
     * 账户是否锁定
     */
    @TableField("locked")
    private Boolean locked;

    /**
     * 锁定时间
     */
    @TableField("locked_at")
    private LocalDateTime lockedAt;

    /**
     * 最后登录时间
     */
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除标记(0-正常, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
```

**字段说明**:
| 字段 | 类型 | 长度 | 必填 | 说明 |
|------|------|------|------|------|
| id | BIGINT | - | ✓ | 主键,自增 |
| username | VARCHAR | 50 | ✓ | 用户名,唯一 |
| password | VARCHAR | 255 | ✓ | 密码(BCrypt 加密) |
| locked | BOOLEAN | - | ✓ | 是否锁定,默认 false |
| lockedAt | DATETIME | - | ✗ | 锁定时间 |
| lastLoginAt | DATETIME | - | ✗ | 最后登录时间 |
| createdAt | DATETIME | - | ✓ | 创建时间 |
| updatedAt | DATETIME | - | ✓ | 更新时间 |
| deleted | TINYINT | - | ✓ | 删除标记 |

---

### 2.2 Todo (待办事项实体)

**完整路径**: `com.example.todolist.domain.entity.Todo`

**说明**: 待办事项实体,映射数据库表 `sys_todo`

```java
package com.example.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待办事项实体
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_todo")
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待办事项ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 优先级
     */
    @TableField("priority")
    private String priority;

    /**
     * 截止日期
     */
    @TableField("due_date")
    private LocalDateTime dueDate;

    /**
     * 完成时间
     */
    @TableField("completed_at")
    private LocalDateTime completedAt;

    /**
     * 版本号(乐观锁)
     */
    @Version
    @TableField("version")
    private Integer version;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除标记(0-正常, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
```

**字段说明**:
| 字段 | 类型 | 长度 | 必填 | 说明 |
|------|------|------|------|------|
| id | BIGINT | - | ✓ | 主键,自增 |
| userId | BIGINT | - | ✓ | 用户ID(外键) |
| title | VARCHAR | 200 | ✓ | 标题 |
| description | TEXT | - | ✗ | 描述 |
| status | VARCHAR | 20 | ✓ | 状态(PENDING/DONE) |
| priority | VARCHAR | 20 | ✓ | 优先级(HIGH/MEDIUM/LOW) |
| dueDate | DATETIME | - | ✗ | 截止日期 |
| completedAt | DATETIME | - | ✗ | 完成时间 |
| version | INT | - | ✓ | 版本号,乐观锁 |
| createdAt | DATETIME | - | ✓ | 创建时间 |
| updatedAt | DATETIME | - | ✓ | 更新时间 |
| deleted | TINYINT | - | ✓ | 删除标记 |

---

### 2.3 Category (分类实体)

**完整路径**: `com.example.todolist.domain.entity.Category`

**说明**: 分类实体,映射数据库表 `sys_category`

```java
package com.example.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 颜色
     */
    @TableField("color")
    private String color;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除标记(0-正常, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
```

**字段说明**:
| 字段 | 类型 | 长度 | 必填 | 说明 |
|------|------|------|------|------|
| id | BIGINT | - | ✓ | 主键,自增 |
| userId | BIGINT | - | ✓ | 用户ID(外键) |
| name | VARCHAR | 50 | ✓ | 分类名称 |
| color | VARCHAR | 7 | ✓ | 颜色(十六进制) |
| createdAt | DATETIME | - | ✓ | 创建时间 |
| updatedAt | DATETIME | - | ✓ | 更新时间 |
| deleted | TINYINT | - | ✓ | 删除标记 |

---

### 2.4 TodoCategory (待办-分类关联实体)

**完整路径**: `com.example.todolist.domain.entity.TodoCategory`

**说明**: 待办事项与分类的多对多关联表实体

```java
package com.example.todolist.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待办-分类关联实体
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_todo_category")
public class TodoCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 待办事项ID
     */
    @TableField("todo_id")
    private Long todoId;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
```

**字段说明**:
| 字段 | 类型 | 长度 | 必填 | 说明 |
|------|------|------|------|------|
| id | BIGINT | - | ✓ | 主键,自增 |
| todoId | BIGINT | - | ✓ | 待办事项ID |
| categoryId | BIGINT | - | ✓ | 分类ID |
| createdAt | DATETIME | - | ✓ | 创建时间 |

---

## 3. 枚举类 (Enums)

### 3.1 TodoStatus (待办状态)

**完整路径**: `com.example.todolist.domain.enums.TodoStatus`

```java
package com.example.todolist.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待办事项状态枚举
 *
 * @author todolist
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum TodoStatus {

    /**
     * 待处理
     */
    PENDING("PENDING", "待处理"),

    /**
     * 已完成
     */
    DONE("DONE", "已完成");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据状态码获取枚举
     */
    public static TodoStatus fromCode(String code) {
        for (TodoStatus status : TodoStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }
}
```

---

### 3.2 TodoPriority (待办优先级)

**完整路径**: `com.example.todolist.domain.enums.TodoPriority`

```java
package com.example.todolist.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待办事项优先级枚举
 *
 * @author todolist
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum TodoPriority {

    /**
     * 高优先级
     */
    HIGH("HIGH", "高", 3),

    /**
     * 中优先级
     */
    MEDIUM("MEDIUM", "中", 2),

    /**
     * 低优先级
     */
    LOW("LOW", "低", 1);

    /**
     * 优先级代码
     */
    private final String code;

    /**
     * 优先级描述
     */
    private final String description;

    /**
     * 权重
     */
    private final Integer weight;

    /**
     * 根据代码获取枚举
     */
    public static TodoPriority fromCode(String code) {
        for (TodoPriority priority : TodoPriority.values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("无效的优先级代码: " + code);
    }
}
```

---

## 4. DTO (Data Transfer Object)

### 4.1 认证模块 DTO

#### 4.1.1 UserRegisterDTO (用户注册请求)

**完整路径**: `com.example.todolist.dto.request.UserRegisterDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户注册请求")
public class UserRegisterDTO {

    @Schema(description = "用户名", example = "testuser", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在 3-50 字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字、下划线")
    private String username;

    @Schema(description = "密码", example = "Test1234", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在 8-20 字符之间")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "密码必须包含字母和数字")
    private String password;

    @Schema(description = "确认密码", example = "Test1234", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
```

#### 4.1.2 UserLoginDTO (用户登录请求)

**完整路径**: `com.example.todolist.dto.request.UserLoginDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录请求")
public class UserLoginDTO {

    @Schema(description = "用户名", example = "testuser", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "Test1234", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
```

#### 4.1.3 RefreshTokenDTO (刷新Token请求)

**完整路径**: `com.example.todolist.dto.request.RefreshTokenDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新Token请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "刷新Token请求")
public class RefreshTokenDTO {

    @Schema(description = "刷新Token", required = true)
    @NotBlank(message = "刷新Token不能为空")
    private String refreshToken;
}
```

#### 4.1.4 AuthResponseVO (认证响应)

**完整路径**: `com.example.todolist.dto.response.AuthResponseVO`

```java
package com.example.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应 VO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "认证响应")
public class AuthResponseVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "访问Token")
    private String token;

    @Schema(description = "刷新Token")
    private String refreshToken;

    @Schema(description = "Token过期时间(秒)")
    private Long expiresIn;
}
```

---

### 4.2 待办事项模块 DTO

#### 4.2.1 TodoDTO (待办事项请求)

**完整路径**: `com.example.todolist.dto.request.TodoDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "待办事项请求")
public class TodoDTO {

    @Schema(description = "待办事项ID(更新时必填)")
    private Long id;

    @Schema(description = "标题", example = "完成项目文档", required = true)
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过 200 字符")
    private String title;

    @Schema(description = "描述", example = "编写详细的技术文档")
    @Size(max = 1000, message = "描述长度不能超过 1000 字符")
    private String description;

    @Schema(description = "优先级", example = "HIGH")
    private String priority;

    @Schema(description = "截止日期", example = "2026-01-30T23:59:59Z")
    private LocalDateTime dueDate;

    @Schema(description = "分类ID列表", example = "[1, 2]")
    private List<Long> categoryIds;
}
```

#### 4.2.2 TodoVO (待办事项响应)

**完整路径**: `com.example.todolist.dto.response.TodoVO`

```java
package com.example.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项响应 VO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "待办事项响应")
public class TodoVO {

    @Schema(description = "待办事项ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "优先级")
    private String priority;

    @Schema(description = "截止日期")
    private LocalDateTime dueDate;

    @Schema(description = "完成时间")
    private LocalDateTime completedAt;

    @Schema(description = "分类列表")
    private List<CategoryVO> categories;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
```

#### 4.2.3 TodoQuery (待办事项查询)

**完整路径**: `com.example.todolist.query.TodoQuery`

```java
package com.example.todolist.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待办事项查询对象
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "待办事项查询")
public class TodoQuery {

    @Schema(description = "页码", example = "1")
    private Integer page;

    @Schema(description = "每页大小", example = "20")
    private Integer size;

    @Schema(description = "状态过滤", example = "PENDING")
    private String status;

    @Schema(description = "优先级过滤", example = "HIGH")
    private String priority;

    @Schema(description = "截止日期过滤", example = "today")
    private String dueDateFilter;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "搜索关键词")
    private String keyword;

    @Schema(description = "排序字段", example = "createdAt")
    private String sortBy;

    @Schema(description = "排序方向", example = "desc")
    private String sortOrder;
}
```

---

### 4.3 分类模块 DTO

#### 4.3.1 CategoryDTO (分类请求)

**完整路径**: `com.example.todolist.dto.request.CategoryDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类请求")
public class CategoryDTO {

    @Schema(description = "分类ID(更新时必填)")
    private Long id;

    @Schema(description = "分类名称", example = "工作", required = true)
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 1, max = 50, message = "分类名称长度必须在 1-50 字符之间")
    private String name;

    @Schema(description = "颜色", example = "#FF0000")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式错误,必须为十六进制颜色代码")
    private String color;
}
```

#### 4.3.2 CategoryVO (分类响应)

**完整路径**: `com.example.todolist.dto.response.CategoryVO`

```java
package com.example.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分类响应 VO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类响应")
public class CategoryVO {

    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "待办事项数量")
    private Integer todoCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
```

#### 4.3.3 CategoryQuery (分类查询)

**完整路径**: `com.example.todolist.query.CategoryQuery`

```java
package com.example.todolist.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类查询对象
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类查询")
public class CategoryQuery {

    @Schema(description = "排序字段", example = "createdAt")
    private String sortBy;

    @Schema(description = "排序方向", example = "desc")
    private String sortOrder;
}
```

---

### 4.4 用户模块 DTO

#### 4.4.1 UpdatePasswordDTO (修改密码请求)

**完整路径**: `com.example.todolist.dto.request.UpdatePasswordDTO`

```java
package com.example.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改密码请求 DTO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "修改密码请求")
public class UpdatePasswordDTO {

    @Schema(description = "旧密码", required = true)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在 8-20 字符之间")
    private String newPassword;

    @Schema(description = "确认新密码", required = true)
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
}
```

#### 4.4.2 UserVO (用户信息响应)

**完整路径**: `com.example.todolist.dto.response.UserVO`

```java
package com.example.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户信息响应 VO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息响应")
public class UserVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "注册时间")
    private LocalDateTime createdAt;

    @Schema(description = "待办事项统计")
    private UserStatisticsVO statistics;
}
```

#### 4.4.3 UserStatisticsVO (用户统计信息)

**完整路径**: `com.example.todolist.dto.response.UserStatisticsVO`

```java
package com.example.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户统计信息 VO
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户统计信息")
public class UserStatisticsVO {

    @Schema(description = "总待办事项数")
    private Integer totalTodos;

    @Schema(description = "已完成数")
    private Integer completedTodos;

    @Schema(description = "待处理数")
    private Integer pendingTodos;

    @Schema(description = "完成率")
    private BigDecimal completionRate;
}
```

---

## 5. 统一响应对象

### 5.1 R<T> (通用响应)

**完整路径**: `com.example.todolist.common.response.R`

```java
package com.example.todolist.common.response;

import com.example.todolist.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用响应对象
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通用响应")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private Integer code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    private LocalDateTime timestamp;

    /**
     * 成功响应
     */
    public static <T> R<T> ok() {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message("操作成功")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 成功响应(带数据)
     */
    public static <T> R<T> ok(T data) {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message("操作成功")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 成功响应(自定义消息)
     */
    public static <T> R<T> ok(String message, T data) {
        return R.<T>builder()
                .code(HttpStatus.SUCCESS)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 失败响应
     */
    public static <T> R<T> fail(String message) {
        return R.<T>builder()
                .code(HttpStatus.ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 失败响应(自定义状态码)
     */
    public static <T> R<T> fail(Integer code, String message) {
        return R.<T>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
```

### 5.2 TableDataInfo<T> (分页响应)

**完整路径**: `com.example.todolist.common.response.TableDataInfo`

```java
package com.example.todolist.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应对象
 *
 * @author todolist
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应")
public class TableDataInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页数据
     */
    @Schema(description = "当前页数据")
    private List<T> records;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private Long total;

    /**
     * 当前页
     */
    @Schema(description = "当前页")
    private Long page;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小")
    private Long size;

    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private Long pages;

    /**
     * 从 MyBatis-Plus 分页对象构建
     */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        return TableDataInfo.<T>builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .page(page.getCurrent())
                .size(page.getSize())
                .pages(page.getPages())
                .build();
    }
}
```

---

## 6. 对象转换工具

### 6.1 BeanConv (对象转换器)

**完整路径**: `com.example.todolist.common.util.BeanConv`

```java
package com.example.todolist.common.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象转换工具类
 *
 * @author todolist
 * @since 1.0.0
 */
public class BeanConv {

    /**
     * 单个对象转换
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换失败", e);
        }
    }

    /**
     * 列表对象转换
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> convert(source, targetClass))
                .collect(Collectors.toList());
    }
}
```

---

## 7. 数据验证注解总结

### 7.1 常用验证注解

| 注解 | 说明 | 示例 |
|------|------|------|
| @NotNull | 不能为 null | `private Long id;` |
| @NotBlank | 字符串不能为空 | `private String username;` |
| @Size | 长度限制 | `@Size(min=3, max=50)` |
| @Min | 最小值 | `@Min(1)` |
| @Max | 最大值 | `@Max(100)` |
| @Pattern | 正则表达式 | `@Pattern(regexp="^[a-zA-Z0-9_]+$")` |
| @Email | 邮箱格式 | `@Email` |
| @Past | 过去时间 | `@Past` |
| @Future | 未来时间 | `@Future` |

### 7.2 自定义验证注解

**密码匹配验证**:

```java
package com.example.todolist.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码匹配验证注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {

    String message() default "两次密码不一致";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String firstField();

    String secondField();
}
```

---

## 8. 相关文档

- API 规范: `docs/detailed-design/api-specs.md`
- 数据库设计: `docs/detailed-design/database-design.md`
- 类设计: `docs/detailed-design/class-design.md`
- 架构设计: `docs/architecture/architecture.md`

---

## 批准

| 角色 | 姓名 | 签名 | 日期 |
|------|------|------|------|
| 技术负责人 | | | |
| 后端开发负责人 | | | |
