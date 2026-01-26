---
name: test-gen
description: 为 Service 层方法生成单元测试。补充测试用例时使用。
allowed-tools: ["Read", "Write", "Edit", "Grep"]
---

# 单元测试生成器

为 Java Service 层方法生成 JUnit + Mockito 测试用例。

## 功能特性

- 自动识别 Service 方法
- 生成 Mock 对象
- 创建测试数据
- 编写断言语句
- 支持事务回滚

## 使用方法

### 为单个 Service 生成测试

```
/test-gen UserService
```

### 为多个 Service 生成测试

```
/test-gen UserService RoleService MenuService
```

### 指定包名

```
/test-gen UserService --package=com.example.service
```

## 测试框架

生成的代码使用：
- **JUnit 5** - 测试框架
- **Mockito** - Mock 框架
- **Spring Boot Test** - 集成测试支持

## 测试覆盖

为每个方法生成：
- 正常场景测试
- 边界条件测试
- 异常情况测试
- 数据验证测试

## 代码示例

生成格式：

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void testSelectUserList() {
        // Given
        List<User> users = Arrays.asList(
            new User(1L, "张三"),
            new User(2L, "李四")
        );
        when(userMapper.selectList(null)).thenReturn(users);

        // When
        List<User> result = userService.selectUserList(null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserName()).isEqualTo("张三");
    }
}
```

## 注意事项

- 需要添加 JUnit 5 和 Mockito 依赖
- 测试类放在 src/test/java 目录
- 测试数据应该独立（不依赖真实数据库）
- 复杂业务逻辑可能需要手动完善

## 依赖配置

确保 pom.xml 包含：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

## 后续步骤

1. 检查生成的测试用例
2. 补充边界条件测试
3. 运行测试确保通过
4. 检查代码覆盖率
