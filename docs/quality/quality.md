# 质量标准

## 代码质量

### 复杂度限制

| 指标 | 阈值 | 说明 |
|------|------|------|
| 圈复杂度 | <= 10 | 单个方法 |
| 方法行数 | <= 50 | 不含注释和空行 |
| 类行数 | <= 500 | 不含注释和空行 |
| 方法参数 | <= 5 | 超过考虑使用对象 |
| 嵌套层级 | <= 3 | if/for 嵌套 |

### 代码覆盖率

| 类型 | 最低覆盖率 |
|------|-----------|
| 行覆盖率 | 80% |
| 分支覆盖率 | 70% |
| Service 层 | 90% |
| Controller 层 | 80% |
| Repository 层 | 70% |

### Lint 规则

```xml
<!-- Checkstyle 配置 -->
<module name="Checker">
    <module name="TreeWalker">
        <module name="AvoidStarImport"/>
        <module name="NeedBraces"/>
        <module name="LeftCurly"/>
        <module name="RightCurly"/>
        <module name="EmptyBlock"/>
    </module>
</module>
```

## 测试质量

### 单元测试要求

1. **测试命名**: `methodName_scenario_expectedResult`
   ```java
   @Test
   void createUser_withValidData_returnsSuccess() { }
   ```

2. **测试结构**: Given-When-Then
   ```java
   // Given
   UserDTO userDTO = new UserDTO("张三", "zhangsan@example.com");

   // When
   UserVO result = userService.create(userDTO);

   // Then
   assertNotNull(result);
   assertEquals("张三", result.getName());
   ```

3. **断言完整**: 每个测试至少一个断言

### 集成测试要求

- 测试 API 契约
- 测试数据库交互
- 测试事务边界

## 安全质量

### 必须检查项

| 检查项 | 说明 | 验证方法 |
|--------|------|----------|
| 硬编码密钥 | 禁止 | 代码扫描 |
| SQL 注入 | 参数化查询 | 审查 Mapper |
| XSS | 输入验证+输出编码 | 安全测试 |
| CSRF | Token 验证 | 安全测试 |
| 敏感数据 | 加密存储 | 审查代码 |

### 安全扫描

```bash
# 依赖漏洞扫描
mvn org.owasp:dependency-check-maven:check

# 密钥扫描
git-secrets scans
```

## 性能标准

### 响应时间

| 端点类型 | 最大响应时间 |
|----------|-------------|
| 简单查询 | < 200ms |
| 复杂查询 | < 1s |
| 写操作 | < 500ms |
| 批量操作 | < 2s |

### 数据库查询

| 指标 | 阈值 |
|------|------|
| 单表查询 | < 100ms |
| 关联查询 | < 500ms |
| 全表扫描 | 禁止 |
| N+1 查询 | 禁止 |

### 内存使用

| 指标 | 阈值 |
|------|------|
| 堆内存 | < 512MB |
| 单次请求 | < 10MB |
| 缓存大小 | < 256MB |

## 文档质量

### API 文档

- 所有接口必须有 Swagger 注解
- 包含请求示例和响应示例
- 标注可能的错误码

### 代码注释

```java
/**
 * 创建用户
 *
 * @param userDTO 用户信息
 * @return 创建的用户信息
 * @throws BusinessException 当用户名已存在时抛出
 */
UserVO create(UserDTO userDTO);
```

## 质量门禁

### 提交前检查

```bash
# 1. 代码格式化
mvn spotless:apply

# 2. Lint 检查
mvn checkstyle:check

# 3. 运行测试
mvn test

# 4. 覆盖率检查
mvn jacoco:check
```

### 合并前检查

- 代码审查通过
- 所有测试通过
- 覆盖率达到要求
- 安全扫描无高危

---

**最后更新**: 2026-03-15
