# 测试指南

## 测试策略

### 测试金字塔

```
        /\
       /  \      E2E 测试 (10%)
      /────\
     /      \    集成测试 (30%)
    /────────\
   /          \  单元测试 (60%)
  /____________\
```

## 单元测试

### 测试框架

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Service 层测试

```java
@SpringBootTest
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_whenUserExists_returnsUser() {
        // Given
        Long userId = 1L;
        User user = new User(userId, "张三");
        when(userMapper.selectById(userId)).thenReturn(user);

        // When
        UserVO result = userService.getById(userId);

        // Then
        assertNotNull(result);
        assertEquals("张三", result.getName());
        verify(userMapper).selectById(userId);
    }
}
```

### Controller 层测试

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Test
    void getById_returnsUser() throws Exception {
        // Given
        UserVO userVO = new UserVO(1L, "张三");
        when(userService.getById(1L)).thenReturn(userVO);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("张三"));
    }
}
```

## 集成测试

### 测试配置

```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
```

### API 集成测试

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_returnsCreated() throws Exception {
        UserDTO userDTO = new UserDTO("张三", "zhangsan@example.com");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("张三"));
    }
}
```

## 测试最佳实践

### 命名规范

```java
// 格式: methodName_scenario_expectedResult
@Test
void createUser_withValidData_returnsSuccess() { }

@Test
void createUser_withDuplicateUsername_throwsException() { }

@Test
void createUser_withInvalidEmail_returnsValidationError() { }
```

### Given-When-Then 模式

```java
@Test
void updateUser_withValidData_updatesSuccessfully() {
    // Given - 准备测试数据
    Long userId = 1L;
    UserUpdateDTO dto = new UserUpdateDTO("李四");

    // When - 执行被测试方法
    userService.update(userId, dto);

    // Then - 验证结果
    UserVO result = userService.getById(userId);
    assertEquals("李四", result.getName());
}
```

### Mock 使用原则

- 外部依赖必须 Mock (数据库、API)
- 复杂逻辑可以 Mock
- 简单对象直接使用真实对象

## 覆盖率要求

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</plugin>
```

## 运行测试

```bash
# 所有测试
mvn test

# 特定测试类
mvn test -Dtest=UserServiceTest

# 特定测试方法
mvn test -Dtest=UserServiceTest#createUser

# 生成覆盖率报告
mvn jacoco:report
```

---

**最后更新**: 2026-03-15
