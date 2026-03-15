# 集成测试方案

**项目**: TodoList 应用
**版本**: v1.0.0
**日期**: 2026-03-16

---

## 1. 测试目标

验证 TodoList 应用各模块之间的集成是否正常工作，确保前后端交互正确。

---

## 2. 测试范围

### 2.1 后端集成测试

| 模块 | 测试内容 | 优先级 |
|------|----------|--------|
| Controller-Service | API 请求到业务逻辑 | P0 |
| Service-Mapper | 业务逻辑到数据库 | P0 |
| Security-Controller | JWT 认证流程 | P0 |

### 2.2 前后端集成测试

| 功能 | 测试内容 | 优先级 |
|------|----------|--------|
| 用户注册 | 前端表单 → 后端 API → 数据库 | P0 |
| 用户登录 | 前端表单 → JWT Token → 状态存储 | P0 |
| 任务 CRUD | 前端操作 → API 调用 → 数据持久化 | P0 |
| 任务状态切换 | 前端点击 → API 更新 → UI 刷新 | P1 |

---

## 3. 测试环境

```yaml
后端:
  - Spring Boot Test
  - H2 内存数据库
  - MockMvc

前端:
  - Vitest
  - @vue/test-utils
  - MSW (Mock Service Worker)
```

---

## 4. 测试用例

### 4.1 用户认证集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerThenLogin_ShouldReturnToken() throws Exception {
        // 1. 注册
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk());

        // 2. 登录
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists());
    }
}
```

### 4.2 任务管理集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // 注册并登录获取 token
        token = obtainToken();
    }

    @Test
    void createTodo_ShouldPersist() throws Exception {
        // 创建任务
        mockMvc.perform(post("/api/todos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Task\",\"priority\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

        // 验证可以查询到
        mockMvc.perform(get("/api/todos")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records[0].title").value("Test Task"));
    }
}
```

---

## 5. 测试数据

### 5.1 用户测试数据

| 场景 | 邮箱 | 密码 | 预期结果 |
|------|------|------|----------|
| 正常注册 | new@test.com | 123456 | 成功 |
| 重复注册 | test@test.com | 123456 | 失败 |
| 错误密码 | test@test.com | wrong | 失败 |

### 5.2 任务测试数据

| 场景 | 标题 | 优先级 | 预期结果 |
|------|------|--------|----------|
| 正常创建 | Test Task | 1 | 成功 |
| 空标题 | "" | 1 | 失败 |
| 超长标题 | 200字符... | 1 | 失败 |

---

## 6. 测试执行

### 6.1 执行命令

```bash
# 后端集成测试
cd backend
./mvnw test -Dtest=*IntegrationTest

# 前端集成测试
cd frontend
npm run test:integration
```

### 6.2 测试报告

测试完成后生成：
- `target/surefire-reports/` - JUnit XML 报告
- `coverage/` - 覆盖率报告

---

## 7. 验收标准

| 指标 | 目标 | 实际 |
|------|------|------|
| 集成测试通过率 | 100% | - |
| 代码覆盖率 | ≥ 70% | - |
| API 响应时间 | < 200ms | - |
| 前端构建成功 | ✅ | - |
