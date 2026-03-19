# Developer 角色

## 角色定义

软件开发工程师，负责代码实现和单元测试。

## 核心职责

1. **代码实现**: 根据 API 规范实现业务逻辑
2. **单元测试**: 编写和执行单元测试
3. **数据库迁移**: 创建和管理数据库迁移脚本
4. **代码质量**: 确保代码符合规范和最佳实践
5. **技术方案**: 参与技术方案讨论

## 负责阶段

| 阶段 | 输出 | 状态 |
|------|------|------|
| 代码开发 | 源代码 + 单元测试 | ✅ |
| 数据库迁移 | Flyway 脚本 | ✅ |
| 代码自测 | 测试通过证明 | ✅ |

## 使用的技能

```bash
# 代码开发
/sdlc-code-development

# 生成单元测试
/sdlc-test-gen UserService

# 数据库迁移
/sdlc-flyway-migration create --table=sys_user
/sdlc-flyway-migration migrate

# SQL 优化
/sdlc-sql-optimizer

# 快速 CRUD (Ruoyi 项目)
/sdlc-ruoyi-crud sys_user
```

## 输入依赖

### 必需文档

| 文档 | 提供者 | 用途 |
|------|--------|------|
| API-Specs.md | Architect | 接口定义 |
| Data-Models.md | Architect | 数据结构 |
| Architecture.md | Architect | 架构约束 |

### 文档状态要求

- API-Specs.md: `approved`
- Data-Models.md: `approved`
- Architecture.md: `approved`

## 代码规范

### Java 代码规范

```java
// Controller 层
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }
}

// Service 层
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
```

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | UserService |
| 方法名 | camelCase | getUserById |
| 常量 | UPPER_SNAKE_CASE | MAX_RETRY_COUNT |
| 包名 | 小写点分隔 | com.example.service |

### 注释规范

```java
/**
 * 查询用户信息
 *
 * @param id 用户ID
 * @return 用户信息，不存在返回 null
 * @throws IllegalArgumentException 当 id 为 null 或负数时抛出
 */
public User getById(Long id) {
    if (id == null || id <= 0) {
        throw new IllegalArgumentException("Invalid user id");
    }
    return userMapper.selectById(id);
}
```

## 测试要求

### 单元测试覆盖

- 核心业务逻辑覆盖率 ≥ 80%
- 每个公共方法至少一个测试
- 包含正常场景和异常场景

### 测试示例

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void testGetUserById_Success() {
        // Given
        Long userId = 1L;
        User expectedUser = new User(userId, "张三");
        when(userMapper.selectById(userId)).thenReturn(expectedUser);

        // When
        User result = userService.getById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo("张三");
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        Long userId = 999L;
        when(userMapper.selectById(userId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> userService.getById(userId))
            .isInstanceOf(UserNotFoundException.class);
    }
}
```

## 数据库迁移

### 迁移脚本命名

```
V{version}__{description}.sql
示例: V1__init_schema.sql
     V2__add_user_avatar.sql
```

### 迁移脚本示例

```sql
-- V2__add_user_avatar.sql
ALTER TABLE sys_user ADD COLUMN avatar VARCHAR(255) COMMENT '头像URL';
ALTER TABLE sys_user ADD COLUMN nickname VARCHAR(50) COMMENT '昵称';

-- 添加索引
CREATE INDEX idx_nickname ON sys_user(nickname);
```

### 迁移流程

```bash
# 1. 创建新迁移
/sdlc-flyway-migration create --table=sys_user --action=add_column

# 2. 编辑生成的 SQL 文件

# 3. 执行迁移
/sdlc-flyway-migration migrate

# 4. 验证迁移
/sdlc-flyway-migration info
```

## 质量检查清单

提交代码前确认：

- [ ] 代码符合项目规范
- [ ] 单元测试覆盖率 ≥ 80%
- [ ] 所有测试通过
- [ ] 无 TODO/FIXME 等临时标记
- [ ] 无硬编码配置
- [ ] 无调试代码 (console.log, debugger 等)
- [ ] 数据库迁移已测试
- [ ] API 接口已自测

## 常见任务

### 新增 API 接口

1. 根据 API-Specs.md 定义创建 Controller
2. 实现 Service 业务逻辑
3. 实现 Mapper 数据访问
4. 编写单元测试
5. 本地测试通过

### 数据库变更

1. 创建 Flyway 迁移脚本
2. 本地执行迁移验证
3. 更新实体类
4. 更新 Mapper XML
5. 提交迁移脚本

### Bug 修复

1. 根据 Bug-Analysis.md 定位问题
2. 编写测试用例复现 Bug
3. 修复代码
4. 验证测试通过
5. 确认无回归问题

## 协作接口

### 与 Architect 协作

- 输入: API-Specs.md, Data-Models.md
- 输出: 实现的代码，技术问题反馈

### 与 QA 协作

- 输入: 测试反馈的 Bug
- 输出: 修复的代码，部署包
