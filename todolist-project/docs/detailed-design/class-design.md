# 类设计文档 (Class Design)

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

## 1. 类设计概述

### 1.1 分层架构

系统采用经典的分层架构,各层职责清晰:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │  ← Controllers
├─────────────────────────────────────────┤
│          Business Layer                 │  ← Services
├─────────────────────────────────────────┤
│       Persistence Layer                 │  ← Mappers
├─────────────────────────────────────────┤
│          Domain Layer                   │  ← Entities, DTOs, VOs
└─────────────────────────────────────────┘
```

### 1.2 包结构设计

```
com.example.todolist
├── common                    # 通用模块
│   ├── config               # 配置类
│   ├── constant            # 常量定义
│   ├── exception           # 自定义异常
│   ├── response            # 响应对象
│   └── util                # 工具类
│
├── domain                   # 领域层
│   ├── entity              # 实体类
│   ├── enums               # 枚举类
│   └── vo                  # 视图对象
│
├── dto                      # 数据传输对象
│   ├── request             # 请求 DTO
│   └── response            # 响应 DTO
│
├── query                    # 查询对象
│
├── mapper                   # 数据访问层
│   ├── UserMapper
│   ├── TodoMapper
│   └── CategoryMapper
│
├── service                  # 业务层
│   ├── auth                # 认证服务
│   │   ├── AuthStrategy
│   │   ├── JwtService
│   │   └── UserService
│   ├── todo                # 待办服务
│   │   ├── TodoService
│   │   └── TodoCategoryService
│   └── category            # 分类服务
│       └── CategoryService
│
├── controller               # 控制层
│   ├── AuthController
│   ├── TodoController
│   ├── CategoryController
│   └── UserController
│
├── security                 # 安全模块
│   ├── JwtAuthenticationFilter
│   ├── SecurityConfig
│   └── UserDetailsServiceImpl
│
└── TodolistApplication      # 启动类
```

### 1.3 设计原则

1. **单一职责原则 (SRP)**: 每个类只负责一个功能
2. **开闭原则 (OCP)**: 对扩展开放,对修改关闭
3. **依赖倒置原则 (DIP)**: 依赖抽象而不是具体实现
4. **接口隔离原则 (ISP)**: 使用小而精的接口
5. **迪米特法则 (LoD)**: 最少知识原则

---

## 2. Controller 层设计

### 2.1 基础控制器

**BaseController**

**完整路径**: `com.example.todolist.controller.BaseController`

```java
package com.example.todolist.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.todolist.common.response.TableDataInfo;

/**
 * 控制器基类
 *
 * @author todolist
 * @since 1.0.0
 */
public class BaseController {

    /**
     * 设置请求分页数据
     */
    protected <T> Page<T> startPage() {
        Page<T> page = new Page<>(getPageNum(), getPageSize());
        return page;
    }

    /**
     * 获取当前页码
     */
    protected Integer getPageNum() {
        // 从 ThreadLocal 或 Request 获取
        return 1;
    }

    /**
     * 获取每页大小
     */
    protected Integer getPageSize() {
        // 从 ThreadLocal 或 Request 获取
        return 20;
    }

    /**
     * 构建分页响应
     */
    protected <T> TableDataInfo<T> getDataTable(Page<T> page) {
        return TableDataInfo.build(page);
    }
}
```

---

### 2.2 AuthController (认证控制器)

**完整路径**: `com.example.todolist.controller.AuthController`

```java
package com.example.todolist.controller;

import com.example.todolist.common.response.R;
import com.example.todolist.dto.request.RefreshTokenDTO;
import com.example.todolist.dto.request.UserLoginDTO;
import com.example.todolist.dto.request.UserRegisterDTO;
import com.example.todolist.dto.response.AuthResponseVO;
import com.example.todolist.service.auth.JwtService;
import com.example.todolist.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author todolist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、登出等认证接口")
public class AuthController extends BaseController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<AuthResponseVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        AuthResponseVO response = userService.register(dto);
        return R.ok("注册成功", response);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<AuthResponseVO> login(@Valid @RequestBody UserLoginDTO dto) {
        AuthResponseVO response = userService.login(dto);
        return R.ok("登录成功", response);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public R<Void> logout() {
        // TODO: 实现登出逻辑(如将Token加入黑名单)
        return R.ok("登出成功");
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public R<AuthResponseVO> refreshToken(@Valid @RequestBody RefreshTokenDTO dto) {
        AuthResponseVO response = jwtService.refreshToken(dto.getRefreshToken());
        return R.ok("Token刷新成功", response);
    }
}
```

---

### 2.3 TodoController (待办事项控制器)

**完整路径**: `com.example.todolist.controller.TodoController`

```java
package com.example.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.todolist.common.response.R;
import com.example.todolist.common.response.TableDataInfo;
import com.example.todolist.dto.request.TodoDTO;
import com.example.todolist.dto.response.TodoVO;
import com.example.todolist.query.TodoQuery;
import com.example.todolist.service.todo.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 待办事项控制器
 *
 * @author todolist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@Tag(name = "待办事项管理", description = "待办事项的增删改查接口")
public class TodoController extends BaseController {

    private final TodoService todoService;

    /**
     * 查询待办列表(分页)
     */
    @GetMapping("/list")
    @Operation(summary = "查询待办列表")
    public TableDataInfo<TodoVO> list(TodoQuery query) {
        IPage<TodoVO> page = todoService.selectList(query);
        return getDataTable(page);
    }

    /**
     * 获取待办详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取待办详情")
    public R<TodoVO> getInfo(@PathVariable Long id) {
        TodoVO vo = todoService.getById(id);
        return R.ok(vo);
    }

    /**
     * 创建待办事项
     */
    @PostMapping
    @Operation(summary = "创建待办事项")
    public R<Void> add(@Valid @RequestBody TodoDTO dto) {
        todoService.insert(dto);
        return R.ok();
    }

    /**
     * 更新待办事项
     */
    @PutMapping
    @Operation(summary = "更新待办事项")
    public R<Void> edit(@Valid @RequestBody TodoDTO dto) {
        todoService.update(dto);
        return R.ok();
    }

    /**
     * 删除待办事项(批量)
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除待办事项")
    public R<Void> remove(@PathVariable Long[] ids) {
        todoService.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 切换待办状态
     */
    @PatchMapping("/{id}/toggle")
    @Operation(summary = "切换待办状态")
    public R<TodoVO> toggle(@PathVariable Long id) {
        TodoVO vo = todoService.toggleStatus(id);
        return R.ok("状态切换成功", vo);
    }
}
```

---

### 2.4 CategoryController (分类控制器)

**完整路径**: `com.example.todolist.controller.CategoryController`

```java
package com.example.todolist.controller;

import com.example.todolist.common.response.R;
import com.example.todolist.dto.request.CategoryDTO;
import com.example.todolist.dto.response.CategoryVO;
import com.example.todolist.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author todolist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "分类的增删改查接口")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    /**
     * 查询分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询分类列表")
    public R<List<CategoryVO>> list() {
        List<CategoryVO> list = categoryService.selectList();
        return R.ok(list);
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情")
    public R<CategoryVO> getInfo(@PathVariable Long id) {
        CategoryVO vo = categoryService.getById(id);
        return R.ok(vo);
    }

    /**
     * 创建分类
     */
    @PostMapping
    @Operation(summary = "创建分类")
    public R<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.insert(dto);
        return R.ok();
    }

    /**
     * 更新分类
     */
    @PutMapping
    @Operation(summary = "更新分类")
    public R<Void> edit(@Valid @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return R.ok();
    }

    /**
     * 删除分类(批量)
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除分类")
    public R<Void> remove(@PathVariable Long[] ids) {
        categoryService.deleteByIds(ids);
        return R.ok();
    }
}
```

---

### 2.5 UserController (用户控制器)

**完整路径**: `com.example.todolist.controller.UserController`

```java
package com.example.todolist.controller;

import com.example.todolist.common.response.R;
import com.example.todolist.dto.request.UpdatePasswordDTO;
import com.example.todolist.dto.response.UserVO;
import com.example.todolist.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author todolist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息管理接口")
public class UserController extends BaseController {

    private final UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    @Operation(summary = "获取用户信息")
    public R<UserVO> getProfile() {
        UserVO vo = userService.getCurrentUserProfile();
        return R.ok(vo);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public R<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto);
        return R.ok("密码修改成功,请重新登录");
    }
}
```

---

## 3. Service 层设计

### 3.1 UserService (用户服务)

**接口**: `com.example.todolist.service.auth.UserService`

```java
package com.example.todolist.service.auth;

import com.example.todolist.dto.request.UpdatePasswordDTO;
import com.example.todolist.dto.request.UserLoginDTO;
import com.example.todolist.dto.request.UserRegisterDTO;
import com.example.todolist.dto.response.AuthResponseVO;
import com.example.todolist.dto.response.UserVO;

/**
 * 用户服务接口
 *
 * @author todolist
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 用户注册
     */
    AuthResponseVO register(UserRegisterDTO dto);

    /**
     * 用户登录
     */
    AuthResponseVO login(UserLoginDTO dto);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUserProfile();

    /**
     * 修改密码
     */
    void updatePassword(UpdatePasswordDTO dto);

    /**
     * 根据用户名查询用户
     */
    UserVO getByUsername(String username);
}
```

**实现类**: `com.example.todolist.service.auth.impl.UserServiceImpl`

```java
package com.example.todolist.service.auth.impl;

import com.example.todolist.dto.request.UpdatePasswordDTO;
import com.example.todolist.dto.request.UserLoginDTO;
import com.example.todolist.dto.request.UserRegisterDTO;
import com.example.todolist.dto.response.AuthResponseVO;
import com.example.todolist.dto.response.UserVO;
import com.example.todolist.domain.entity.User;
import com.example.todolist.mapper.UserMapper;
import com.example.todolist.service.auth.JwtService;
import com.example.todolist.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 *
 * @author todolist
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO register(UserRegisterDTO dto) {
        // 1. 验证用户名唯一性
        if (userMapper.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 密码加密
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. 创建用户
        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .locked(false)
                .build();

        userMapper.insert(user);

        // 4. 生成Token
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return AuthResponseVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .build();
    }

    @Override
    public AuthResponseVO login(UserLoginDTO dto) {
        // 1. 查询用户
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 检查账户锁定状态
        if (user.getLocked()) {
            throw new RuntimeException("账户已锁定");
        }

        // 4. 生成Token
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return AuthResponseVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .build();
    }

    @Override
    public UserVO getCurrentUserProfile() {
        // TODO: 从 SecurityContext 获取当前用户
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UpdatePasswordDTO dto) {
        // TODO: 实现密码修改逻辑
    }

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        // TODO: 转换为 VO
        return null;
    }
}
```

---

### 3.2 TodoService (待办事项服务)

**接口**: `com.example.todolist.service.todo.TodoService`

```java
package com.example.todolist.service.todo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.todolist.domain.entity.Todo;
import com.example.todolist.dto.request.TodoDTO;
import com.example.todolist.dto.response.TodoVO;
import com.example.todolist.query.TodoQuery;

/**
 * 待办事项服务接口
 *
 * @author todolist
 * @since 1.0.0
 */
public interface TodoService {

    /**
     * 查询待办列表(分页)
     */
    IPage<TodoVO> selectList(TodoQuery query);

    /**
     * 根据ID查询待办
     */
    TodoVO getById(Long id);

    /**
     * 插入待办
     */
    void insert(TodoDTO dto);

    /**
     * 更新待办
     */
    void update(TodoDTO dto);

    /**
     * 批量删除待办
     */
    void deleteByIds(Long[] ids);

    /**
     * 切换待办状态
     */
    TodoVO toggleStatus(Long id);
}
```

---

### 3.3 CategoryService (分类服务)

**接口**: `com.example.todolist.service.category.CategoryService`

```java
package com.example.todolist.service.category;

import com.example.todolist.dto.request.CategoryDTO;
import com.example.todolist.dto.response.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 *
 * @author todolist
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * 查询分类列表
     */
    List<CategoryVO> selectList();

    /**
     * 根据ID查询分类
     */
    CategoryVO getById(Long id);

    /**
     * 插入分类
     */
    void insert(CategoryDTO dto);

    /**
     * 更新分类
     */
    void update(CategoryDTO dto);

    /**
     * 批量删除分类
     */
    void deleteByIds(Long[] ids);
}
```

---

## 4. Mapper 层设计

### 4.1 UserMapper

**完整路径**: `com.example.todolist.mapper.UserMapper`

```java
package com.example.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolist.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问接口
 *
 * @author todolist
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(@Param("username") String username);
}
```

**XML 映射文件**: `src/main/resources/mapper/UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.todolist.mapper.UserMapper">

    <select id="selectByUsername" resultType="com.example.todolist.domain.entity.User">
        SELECT id, username, password, locked, locked_at, last_login_at, created_at, updated_at, deleted
        FROM sys_user
        WHERE username = #{username}
          AND deleted = 0
    </select>

    <select id="existsByUsername" resultType="boolean">
        SELECT COUNT(1) > 0
        FROM sys_user
        WHERE username = #{username}
          AND deleted = 0
    </select>

</mapper>
```

---

### 4.2 TodoMapper

**完整路径**: `com.example.todolist.mapper.TodoMapper`

```java
package com.example.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.todolist.domain.entity.Todo;
import com.example.todolist.query.TodoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 待办事项数据访问接口
 *
 * @author todolist
 * @since 1.0.0
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {

    /**
     * 查询待办列表(分页)
     */
    IPage<Todo> selectListPage(IPage<Todo> page, @Param("query") TodoQuery query);
}
```

**XML 映射文件**: `src/main/resources/mapper/TodoMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.todolist.mapper.TodoMapper">

    <select id="selectListPage" resultType="com.example.todolist.domain.entity.Todo">
        SELECT *
        FROM sys_todo
        <where>
            deleted = 0
            <if test="query.userId != null">
                AND user_id = #{query.userId}
            </if>
            <if test="query.status != null and query.status != ''">
                AND status = #{query.status}
            </if>
            <if test="query.priority != null and query.priority != ''">
                AND priority = #{query.priority}
            </if>
            <if test="query.keyword != null and query.keyword != ''">
                AND (title LIKE CONCAT('%', #{query.keyword}, '%')
                     OR description LIKE CONCAT('%', #{query.keyword}, '%'))
            </if>
            <if test="query.dueDateFilter == 'today'">
                AND DATE(due_date) = CURDATE()
            </if>
            <if test="query.dueDateFilter == 'week'">
                AND YEARWEEK(due_date, 1) = YEARWEEK(CURDATE(), 1)
            </if>
            <if test="query.dueDateFilter == 'month'">
                AND DATE_FORMAT(due_date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')
            </if>
            <if test="query.dueDateFilter == 'overdue'">
                AND due_date &lt; NOW()
            </if>
        </where>
        <choose>
            <when test="query.sortBy == 'dueDate'">
                ORDER BY due_date ${query.sortOrder}
            </when>
            <when test="query.sortBy == 'priority'">
                ORDER BY FIELD(priority, 'HIGH', 'MEDIUM', 'LOW') ${query.sortOrder}
            </when>
            <otherwise>
                ORDER BY created_at DESC
            </otherwise>
        </choose>
    </select>

</mapper>
```

---

### 4.3 CategoryMapper

**完整路径**: `com.example.todolist.mapper.CategoryMapper`

```java
package com.example.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolist.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类数据访问接口
 *
 * @author todolist
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    // 继承 BaseMapper,拥有 CRUD 方法
}
```

---

## 5. 安全模块设计

### 5.1 JwtService (JWT 服务)

**完整路径**: `com.example.todolist.service.auth.JwtService`

```java
package com.example.todolist.service.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 服务
 *
 * @author todolist
 * @since 1.0.0
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成访问Token
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .subject(username)
                .claim("user_id", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成刷新Token
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 7 * 1000); // 7天

        return Jwts.builder()
                .claim("user_id", userId)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 验证Token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * 从Token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("user_id", Long.class);
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

---

### 5.2 JwtAuthenticationFilter (JWT 认证过滤器)

**完整路径**: `com.example.todolist.security.JwtAuthenticationFilter`

```java
package com.example.todolist.security;

import com.example.todolist.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT 认证过滤器
 *
 * @author todolist
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头中获取Token
        String token = getTokenFromRequest(request);

        // 2. 验证Token并设置认证信息
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            Long userId = jwtService.getUserIdFromToken(token);
            String username = jwtService.getUsernameFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, username, new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

---

### 5.3 SecurityConfig (安全配置)

**完整路径**: `com.example.todolist.config.SecurityConfig`

```java
package com.example.todolist.config;

import com.example.todolist.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 配置
 *
 * @author todolist
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 配置会话管理为无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        // 公开接口: 注册、登录
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        // Swagger 文档公开
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 其他接口需要认证
                        .anyRequest().authenticated()
                )

                // 添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 生产环境应限制具体域名
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## 6. 类关系图

### 6.1 分层关系

```
┌──────────────────────────────────────────────────────────┐
│                    Controller Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   Auth       │  │    Todo      │  │  Category    │   │
│  │  Controller  │  │  Controller  │  │  Controller  │   │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘   │
│         │                  │                  │           │
└─────────┼──────────────────┼──────────────────┼───────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌──────────────────────────────────────────────────────────┐
│                     Service Layer                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │    User      │  │    Todo      │  │  Category    │   │
│  │   Service    │  │   Service    │  │   Service    │   │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘   │
│         │                  │                  │           │
└─────────┼──────────────────┼──────────────────┼───────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌──────────────────────────────────────────────────────────┐
│                    Mapper Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │    User      │  │    Todo      │  │  Category    │   │
│  │   Mapper     │  │   Mapper     │  │   Mapper     │   │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘   │
│         │                  │                  │           │
└─────────┼──────────────────┼──────────────────┼───────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌──────────────────────────────────────────────────────────┐
│                   Domain Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │    User      │  │    Todo      │  │  Category    │   │
│  │   Entity     │  │   Entity     │  │   Entity     │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└──────────────────────────────────────────────────────────┘
```

### 6.2 依赖关系

**Controller → Service**:
- AuthController → UserService, JwtService
- TodoController → TodoService
- CategoryController → CategoryService
- UserController → UserService

**Service → Mapper**:
- UserService → UserMapper
- TodoService → TodoMapper, TodoCategoryMapper
- CategoryService → CategoryMapper

**Service → Service**:
- TodoService → CategoryService(查询分类信息)

---

## 7. 设计模式应用

### 7.1 工厂模式

**JwtService**: 使用工厂方法创建 Token

### 7.2 策略模式

**认证策略**: 可扩展多种认证方式(用户名密码、手机号、邮箱等)

### 7.3 模板方法模式

**BaseController**: 提供公共的分页方法

### 7.4 依赖注入

**Spring IoC**: 通过构造器注入依赖

---

## 8. 异常处理设计

### 8.1 自定义异常

**BusinessException**: 业务异常

```java
package com.example.todolist.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author todolist
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
```

### 8.2 全局异常处理器

**GlobalExceptionHandler**

```java
package com.example.todolist.common.exception;

import com.example.todolist.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author todolist
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数验证失败: {}", errors);
        return R.fail(400, "参数验证失败: " + errors);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail(500, "系统异常,请联系管理员");
    }
}
```

---

## 9. 日志设计

### 9.1 日志级别

- **ERROR**: 错误日志,异常情况
- **WARN**: 警告日志,潜在问题
- **INFO**: 信息日志,关键业务流程
- **DEBUG**: 调试日志,开发调试使用

### 9.2 日志配置

**logback-spring.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <springProperty scope="context" name="APP_NAME" source="spring.application.name"/>
    <property name="LOG_PATH" value="logs"/>

    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/${APP_NAME}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${APP_NAME}-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 根日志配置 -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>

    <!-- 应用日志配置 -->
    <logger name="com.example.todolist" level="DEBUG"/>
</configuration>
```

---

## 10. 相关文档

- API 规范: `docs/detailed-design/api-specs.md`
- 数据模型: `docs/detailed-design/data-models.md`
- 数据库设计: `docs/detailed-design/database-design.md`
- 架构设计: `docs/architecture/architecture.md`
- 技术栈: `docs/architecture/technology-stack.md`

---

## 批准

| 角色 | 姓名 | 签名 | 日期 |
|------|------|------|------|
| 技术负责人 | | | |
| 后端开发负责人 | | | |
| 架构师 | | | |
