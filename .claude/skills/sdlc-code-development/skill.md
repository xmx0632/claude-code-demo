---
name: code-development
description: 代码开发阶段，实现业务逻辑和 API 接口。编码实现时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Grep", "Bash"]
user-invocable: true
---

# 代码开发助手

执行 SDLC 阶段 6：代码开发，实现业务逻辑。

## 阶段目标

根据详细设计文档，实现完整的业务代码。

## 输入

- API 规范（阶段 4 产出）
- 数据模型（阶段 4 产出）
- 类设计（阶段 4 产出）
- 数据库迁移脚本（阶段 5 产出）

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 实体类 | `src/main/java/.../entity/` | 数据实体 |
| Mapper | `src/main/java/.../mapper/` | MyBatis Mapper |
| Service | `src/main/java/.../service/` | 业务逻辑 |
| Controller | `src/main/java/.../controller/` | API 控制器 |
| DTO | `src/main/java/.../dto/` | 数据传输对象 |

## 执行步骤

### 1. 实体层开发

```markdown
- 创建实体类
- 添加字段和注解
- 定义关联关系
- 添加验证规则
```

### 2. 数据访问层

```markdown
- 创建 Mapper 接口
- 编写 SQL 映射
- 实现复杂查询
- 添加缓存支持
```

### 3. 业务逻辑层

```markdown
- 创建 Service 接口
- 实现业务方法
- 添加事务管理
- 实现业务规则
```

### 4. 控制器层

```markdown
- 创建 Controller
- 实现 RESTful API
- 添加参数验证
- 统一异常处理
```

## 使用方法

### 完整开发流程

```
/code-development --entity User --api /api/v1/users
```

### 只生成实体和 Mapper

```
/code-development --entity User --layers entity,mapper
```

### 基于详细设计文档

```
/code-development --from docs/detailed-design/
```

### 集成 Ruoyi CRUD

```
/ruoyi-crud sys_user
```

## 代码规范

### 实体类示例

```java
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
```

### Service 示例

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDTO dto) {
        // 业务逻辑
    }
}
```

### Controller 示例

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/{id}")
    public R<UserVO> getUser(@PathVariable Long id) {
        return R.ok(userService.getUserById(id));
    }
}
```

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 涉及认证、权限、输入处理 |
| Performance Agent | 涉及数据库查询、缓存 |

## 质量门禁

- [ ] 代码编译通过
- [ ] 代码格式化检查通过
- [ ] 无静态分析错误
- [ ] 日志记录完整
- [ ] 异常处理完善

## 相关 Skills

- `/ruoyi-crud` - 快速生成 CRUD 代码
- `/code-review` - 代码审查
- `/test-gen` - 生成单元测试

## 下一步

代码开发完成后，执行：
```
/test-gen UserService
```
