# 组件图文档 (Component Diagram)

> **项目**: TodoList 待办事项管理系统
> **版本**: 1.0
> **最后更新**: 2026-01-26

---

## 1. 概述

本文档详细描述 TodoList 系统的组件结构、组件职责、组件间的依赖关系和交互方式。

### 1.1 组件分层

系统采用分层架构，组件按职责划分为以下层次:

```
┌────────────────────────────────────────┐
│     Presentation Layer (表现层)        │
│     REST Controllers + DTOs             │
├────────────────────────────────────────┤
│      Business Layer (业务层)           │
│      Service Interfaces + Impl         │
├────────────────────────────────────────┤
│    Persistence Layer (持久层)          │
│    MyBatis-Plus Mappers                │
├────────────────────────────────────────┤
│      Domain Layer (领域层)             │
│      Entities + Enums                  │
└────────────────────────────────────────┘
```

### 1.2 模块划分

系统按业务领域划分为以下模块:

| 模块 | 职责 | 包路径 |
|------|------|--------|
| **auth-module** | 认证授权 | `com.todolist.auth` |
| **todo-module** | 待办管理 | `com.todolist.todo` |
| **category-module** | 分类管理 | `com.todolist.category` |
| **user-module** | 用户管理 | `com.todolist.user` |
| **common-module** | 通用功能 | `com.todolist.common` |

---

## 2. 表现层组件

### 2.1 Controller 组件

#### AuthController（认证控制器）

**包路径**: `com.todolist.auth.controller`

**职责**:
- 处理用户注册请求
- 处理用户登录请求
- 处理 Token 刷新请求
- 验证请求参数

**主要方法**:
```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // 用户注册
    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request);

    // 用户登录
    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request);

    // 刷新 Token
    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request);

    // 登出
    @PostMapping("/logout")
    public Result<Void> logout();
}
```

**依赖**:
- AuthService: 认证服务
- JwtUtil: JWT 工具类

#### TodoController（待办控制器）

**包路径**: `com.todolist.todo.controller`

**职责**:
- 处理待办事项 CRUD 请求
- 处理待办状态切换请求
- 处理搜索和过滤请求
- 验证请求参数和权限

**主要方法**:
```java
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    // 创建待办
    @PostMapping
    public Result<TodoDTO> create(@Valid @RequestBody TodoDTO dto);

    // 获取待办详情
    @GetMapping("/{id}")
    public Result<TodoDTO> getById(@PathVariable Long id);

    // 获取待办列表
    @GetMapping
    public Result<PageResult<TodoDTO>> list(TodoQuery query);

    // 更新待办
    @PutMapping("/{id}")
    public Result<TodoDTO> update(@PathVariable Long id, @Valid @RequestBody TodoDTO dto);

    // 删除待办
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id);

    // 标记完成
    @PostMapping("/{id}/complete")
    public Result<TodoDTO> complete(@PathVariable Long id);

    // 标记未完成
    @PostMapping("/{id}/incomplete")
    public Result<TodoDTO> incomplete(@PathVariable Long id);

    // 批量删除
    @PostMapping("/batch-delete")
    public Result<Integer> batchDelete(@RequestBody List<Long> ids);
}
```

**依赖**:
- TodoService: 待办服务
- CurrentUserProvider: 当前用户提供者

#### CategoryController（分类控制器）

**包路径**: `com.todolist.category.controller`

**职责**:
- 处理分类 CRUD 请求
- 验证分类所有权
- 检查分类使用情况

**主要方法**:
```java
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    // 创建分类
    @PostMapping
    public Result<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto);

    // 获取分类列表
    @GetMapping
    public Result<List<CategoryDTO>> list();

    // 更新分类
    @PutMapping("/{id}")
    public Result<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto);

    // 删除分类
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id);
}
```

### 2.2 DTO 组件

#### Request DTO（请求对象）

| DTO 类 | 用途 | 字段 |
|--------|------|------|
| RegisterRequest | 用户注册 | username, password, confirmPassword |
| LoginRequest | 用户登录 | username, password |
| TodoDTO | 待办数据 | title, description, priority, dueDate, categoryIds |
| CategoryDTO | 分类数据 | name, color |

#### Response DTO（响应对象）

| DTO 类 | 用途 | 字段 |
|--------|------|------|
| AuthResponse | 认证响应 | token, user |
| TodoDTO | 待办数据 | id, title, description, status, priority, dueDate, categories |
| CategoryDTO | 分类数据 | id, name, color, todoCount |
| PageResult | 分页结果 | records, total, page, size, pages |

---

## 3. 业务层组件

### 3.1 Service 接口和实现

#### AuthService（认证服务）

**接口**: `com.todolist.auth.service.AuthService`
**实现**: `com.todolist.auth.service.impl.AuthServiceImpl`

**职责**:
- 用户注册业务逻辑
- 用户登录业务逻辑
- Token 生成和验证
- 密码加密和验证

**主要方法**:
```java
public interface AuthService {

    // 用户注册
    AuthResponse register(RegisterRequest request);

    // 用户登录
    AuthResponse login(LoginRequest request);

    // 刷新 Token
    TokenResponse refreshToken(String token);

    // 登出
    void logout();

    // 验证 Token
    boolean validateToken(String token);

    // 从 Token 获取用户信息
    UserDetails getUserDetails(String token);
}
```

**实现要点**:
- 使用 BCrypt 加密密码
- 使用 JWT 生成 Token
- 登录失败次数限制（使用 Redis）
- 用户名唯一性验证

#### TodoService（待办服务）

**接口**: `com.todolist.todo.service.TodoService`
**实现**: `com.todolist.todo.service.impl.TodoServiceImpl`

**职责**:
- 待办事项 CRUD 业务逻辑
- 权限验证（用户只能访问自己的待办）
- 状态转换业务规则
- 分类关联管理

**主要方法**:
```java
public interface TodoService {

    // 创建待办
    TodoDTO insert(TodoDTO dto);

    // 更新待办
    TodoDTO updateById(Long id, TodoDTO dto);

    // 删除待办
    void deleteById(Long id);

    // 获取待办详情
    TodoDTO getById(Long id);

    // 获取待办列表
    PageResult<TodoDTO> list(TodoQuery query);

    // 标记完成
    TodoDTO complete(Long id);

    // 标记未完成
    TodoDTO incomplete(Long id);

    // 批量删除
    int batchDelete(List<Long> ids);
}
```

**实现要点**:
- 自动设置 `user_id`（从当前用户获取）
- 默认 `status = PENDING`
- 权限验证: `todo.userId == currentUserId`
- 软删除: `deleted = true`
- 乐观锁: 使用 `version` 字段

#### CategoryService（分类服务）

**接口**: `com.todolist.category.service.CategoryService`
**实现**: `com.todolist.category.service.impl.CategoryServiceImpl`

**职责**:
- 分类 CRUD 业务逻辑
- 分类名称唯一性验证
- 分类使用情况检查

**主要方法**:
```java
public interface CategoryService {

    // 创建分类
    CategoryDTO insert(CategoryDTO dto);

    // 更新分类
    CategoryDTO updateById(Long id, CategoryDTO dto);

    // 删除分类
    void deleteById(Long id);

    // 获取分类列表
    List<CategoryDTO> list();

    // 检查分类是否被使用
    boolean isUsed(Long categoryId);
}
```

### 3.2 事务管理

**事务策略**:
- Service 层方法添加 `@Transactional` 注解
- 默认隔离级别: `READ_COMMITTED`
- 默认传播行为: `REQUIRED`
- 查询方法: `@Transactional(readOnly = true)`

**事务示例**:
```java
@Service
public class TodoServiceImpl implements TodoService {

    @Transactional
    @Override
    public TodoDTO insert(TodoDTO dto) {
        // 1. 保存待办
        Todo todo = dtoToEntity(dto);
        todoMapper.insert(todo);

        // 2. 保存分类关联
        if (dto.getCategoryIds() != null) {
            for (Long categoryId : dto.getCategoryIds()) {
                TodoCategory tc = new TodoCategory(todo.getId(), categoryId);
                todoCategoryMapper.insert(tc);
            }
        }

        // 3. 返回结果
        return entityToDTO(todo);
    }
}
```

---

## 4. 持久层组件

### 4.1 Mapper 接口

#### UserMapper（用户 Mapper）

**接口**: `com.todolist.user.mapper.UserMapper`

**职责**:
- 用户数据 CRUD
- 按用户名查询

**主要方法**:
```java
public interface UserMapper extends BaseMapper<User> {

    // 按用户名查询
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = false")
    User findByUsername(@Param("username") String username);

    // 检查用户名是否存在
    @Select("SELECT COUNT(1) FROM sys_user WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);
}
```

#### TodoMapper（待办 Mapper）

**接口**: `com.todolist.todo.mapper.TodoMapper`

**职责**:
- 待办数据 CRUD
- 动态查询和过滤

**主要方法**:
```java
public interface TodoMapper extends BaseMapper<Todo> {

    // 查询用户的待办列表
    @Select("<script>" +
            "SELECT * FROM todo " +
            "WHERE user_id = #{userId} AND deleted = false " +
            "<if test='status != null'>AND status = #{status}</if> " +
            "<if test='priority != null'>AND priority = #{priority}</if> " +
            "<if test='keyword != null'>AND (title LIKE #{keyword} OR description LIKE #{keyword})</if> " +
            "ORDER BY created_at DESC " +
            "</script>")
    List<Todo> selectByUserId(@Param("userId") Long userId,
                               @Param("status") String status,
                               @Param("priority") String priority,
                               @Param("keyword") String keyword);

    // 统计用户待办数量
    @Select("SELECT COUNT(*) FROM todo WHERE user_id = #{userId} AND deleted = false")
    int countByUserId(@Param("userId") Long userId);
}
```

#### CategoryMapper（分类 Mapper）

**接口**: `com.todolist.category.mapper.CategoryMapper`

**职责**:
- 分类数据 CRUD
- 检查分类名称唯一性

**主要方法**:
```java
public interface CategoryMapper extends BaseMapper<Category> {

    // 检查分类名称是否存在
    @Select("SELECT COUNT(1) FROM category " +
            "WHERE user_id = #{userId} AND name = #{name} AND deleted = false")
    boolean existsByName(@Param("userId") Long userId, @Param("name") String name);

    // 统计分类下的待办数量
    @Select("SELECT COUNT(*) FROM todo_category WHERE category_id = #{categoryId}")
    int countTodosByCategoryId(@Param("categoryId") Long categoryId);
}
```

### 4.2 MyBatis-Plus 配置

**配置类**: `com.todolist.common.config.MybatisPlusConfig`

```java
@Configuration
@MapperScan("com.todolist.*.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setMaxLimit(100L); // 最大分页限制
        paginationInterceptor.setOverflow(false); // 溢出总页数后是否进行处理
        interceptor.addInnerInterceptor(paginationInterceptor);

        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }
}
```

---

## 5. 领域层组件

### 5.1 实体类（Entity）

#### User（用户实体）

```java
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password; // BCrypt 加密

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("deleted")
    @TableLogic // 逻辑删除
    private Boolean deleted;
}
```

#### Todo（待办实体）

```java
@Data
@TableName("todo")
public class Todo {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("status")
    private String status; // PENDING, COMPLETED

    @TableField("priority")
    private String priority; // HIGH, MEDIUM, LOW

    @TableField("due_date")
    private LocalDate dueDate;

    @TableField("completed_at")
    private LocalDateTime completedAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @Version
    @TableField("version")
    private Long version; // 乐观锁
}
```

#### Category（分类实体）

```java
@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("name")
    private String name;

    @TableField("color")
    private String color;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;
}
```

### 5.2 枚举类（Enum）

#### TodoStatus（待办状态）

```java
public enum TodoStatus {
    PENDING("待处理"),
    COMPLETED("已完成"),
    OVERDUE("已过期");

    private final String description;

    TodoStatus(String description) {
        this.description = description;
    }
}
```

#### TodoPriority（待办优先级）

```java
public enum TodoPriority {
    HIGH("高"),
    MEDIUM("中"),
    LOW("低");

    private final String description;

    TodoPriority(String description) {
        this.description = description;
    }
}
```

---

## 6. 通用组件

### 6.1 配置类

#### SecurityConfig（安全配置）

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter,
                           UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### 6.2 工具类

#### JwtUtil（JWT 工具）

```java
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // 生成 Token
    public String generateToken(UserDetails userDetails);

    // 验证 Token
    public boolean validateToken(String token);

    // 从 Token 获取用户名
    public String getUsernameFromToken(String token);

    // 从 Token 获取过期时间
    public Date getExpirationDateFromToken(String token);
}
```

#### PasswordUtil（密码工具）

```java
public class PasswordUtil {

    // 加密密码
    public static String encode(String rawPassword);

    // 验证密码
    public static boolean matches(String rawPassword, String encodedPassword);
}
```

### 6.3 异常处理

#### GlobalExceptionHandler（全局异常处理器）

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex);

    // 认证异常
    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException ex);

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex);

    // 系统异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex);
}
```

---

## 7. 组件交互流程

### 7.1 请求处理流程

```
Client Request
    ↓
Nginx (可选)
    ↓
Spring Boot Application
    ↓
JwtAuthenticationFilter (认证过滤器)
    ↓
Controller (参数验证)
    ↓
Service (业务逻辑 + 事务)
    ↓
Mapper (数据访问)
    ↓
Database
    ↓
Response (Entity → DTO → JSON)
```

### 7.2 组件依赖图

```
┌─────────────────────────────────────────────────────────┐
│                    Controllers                          │
│  AuthController  TodoController  CategoryController     │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                      Services                            │
│  AuthService  TodoService  CategoryService              │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                       Mappers                            │
│  UserMapper  TodoMapper  CategoryMapper                 │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                      Database                            │
│            MySQL Database                                │
└─────────────────────────────────────────────────────────┘
```

---

## 8. 组件清单

### 8.1 Controller 组件清单

| 组件名称 | 包路径 | 职责 | 依赖 |
|----------|--------|------|------|
| AuthController | com.todolist.auth.controller | 认证授权 | AuthService |
| TodoController | com.todolist.todo.controller | 待办管理 | TodoService |
| CategoryController | com.todolist.category.controller | 分类管理 | CategoryService |
| UserController | com.todolist.user.controller | 用户管理 | UserService |

### 8.2 Service 组件清单

| 组件名称 | 包路径 | 职责 | 依赖 |
|----------|--------|------|------|
| AuthService | com.todolist.auth.service | 认证业务逻辑 | UserMapper, JwtUtil |
| TodoService | com.todolist.todo.service | 待办业务逻辑 | TodoMapper, CategoryMapper |
| CategoryService | com.todolist.category.service | 分类业务逻辑 | CategoryMapper |
| UserService | com.todolist.user.service | 用户业务逻辑 | UserMapper |

### 8.3 Mapper 组件清单

| 组件名称 | 包路径 | 职责 | 对应表 |
|----------|--------|------|--------|
| UserMapper | com.todolist.user.mapper | 用户数据访问 | sys_user |
| TodoMapper | com.todolist.todo.mapper | 待办数据访问 | todo |
| CategoryMapper | com.todolist.category.mapper | 分类数据访问 | category |
| TodoCategoryMapper | com.todolist.todo.mapper | 待办-分类关联 | todo_category |

---

## 9. 相关文档

- 系统架构设计: `docs/architecture/architecture.md`
- 架构图集: `docs/architecture/diagrams.md`
- API 设计: （待创建）
- 数据库设计: （待创建）
