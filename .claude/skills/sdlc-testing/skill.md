---
name: testing
description: 测试阶段，执行单元测试、集成测试和系统测试。测试验证时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Grep", "Bash"]
user-invocable: true
---

# 测试助手

执行 SDLC 阶段 7-11：测试验证，确保系统质量。

## 阶段目标

执行各级测试，确保系统满足需求和质量标准。

## 测试阶段

| 阶段 | 编号 | 说明 |
|------|------|------|
| 单元测试 | 07 | 测试单个函数/方法 |
| 集成测试 | 08 | 测试模块间交互 |
| 系统测试 | 09 | 端到端测试 |
| 测试用例 | 10 | 测试用例编写 |
| 系统验收 | 11 | 用户验收测试 |

## 输出

| 产出物 | 文件路径 | 说明 |
|--------|----------|------|
| 测试代码 | `src/test/java/` | JUnit 测试 |
| 测试报告 | `docs/testing/test-summary.md` | 测试总结 |
| 覆盖率报告 | `coverage/` | 覆盖率 HTML |

## 执行步骤

### 1. 单元测试

```bash
# 运行所有单元测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成覆盖率报告
mvn jacoco:report
```

### 2. 集成测试

```bash
# 运行集成测试
mvn verify -Pintegration-test

# 使用 Testcontainers
mvn test -Dtest=*IntegrationTest
```

### 3. 系统测试

```bash
# 端到端测试
mvn test -Dtest=*E2ETest

# API 测试
mvn test -Dtest=*ApiTest
```

## 使用方法

### 生成单元测试

```
/testing --unit UserService
```

### 生成集成测试

```
/testing --integration UserController
```

### 运行所有测试

```
/testing --run-all
```

### 生成测试报告

```
/testing --report
```

## 测试模板

### 单元测试模板

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("创建用户 - 成功")
    void createUser_success() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setUsername("test");

        // Act
        userService.createUser(dto);

        // Assert
        verify(userMapper).insert(any(User.class));
    }

    @Test
    @DisplayName("创建用户 - 用户名已存在")
    void createUser_duplicateUsername() {
        // Arrange
        when(userMapper.selectByUsername("existing")).thenReturn(new User());

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            userService.createUser(dto);
        });
    }
}
```

### 集成测试模板

```java
@SpringBootTest
@Testcontainers
class UserControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUser_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(1));
    }
}
```

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 测试涉及安全功能 |
| Performance Agent | 测试涉及性能场景 |

## 质量门禁

### 单元测试
- [ ] 测试覆盖率 ≥ 80%
- [ ] 所有测试通过
- [ ] 无跳过的测试

### 集成测试
- [ ] 关键流程已覆盖
- [ ] 数据库事务正确
- [ ] API 响应正确

### 系统测试
- [ ] 用户场景已覆盖
- [ ] 性能指标达标
- [ ] 安全检查通过

## 相关 Skills

- `/test-gen` - 生成单元测试
- `/code-review` - 代码审查
- `/sql-optimizer` - SQL 优化

## 下一步

测试通过后，执行：
```
/documentation
```
