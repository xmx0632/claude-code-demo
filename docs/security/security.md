# 安全指南

## 认证授权

### JWT Token 认证

```java
// Token 结构
{
  "sub": "user_id",
  "name": "user_name",
  "roles": ["ROLE_USER"],
  "exp": 1678888888
}

// Token 过期时间
Access Token:  2 小时
Refresh Token: 7 天
```

### 权限控制

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/users/{id}")
public R<Void> delete(@PathVariable Long id) {
    // 只有 ADMIN 角色可以删除
}
```

## 数据安全

### 敏感数据加密

```java
// 密码使用 BCrypt 加密
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// 敏感字段加密存储
@Column(name = "id_card")
@Convert(converter = EncryptConverter.class)
private String idCard;
```

### 响应数据脱敏

```java
public class UserVO {
    private String username;

    @JsonIgnore  // 不返回密码
    private String password;

    @JsonSerialize(using = PhoneDesensitizer.class)
    private String phone;  // 138****8000
}
```

## 输入验证

### 参数验证

```java
@PostMapping("/users")
public R<UserVO> create(@Valid @RequestBody UserDTO dto) {
    // @Valid 自动验证
}

public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20)
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
```

### SQL 注入防护

```java
// ✅ 正确 - 使用参数化查询
@Select("SELECT * FROM sys_user WHERE user_name = #{userName}")
User selectByUserName(@Param("userName") String userName);

// ❌ 错误 - 字符串拼接
@Select("SELECT * FROM sys_user WHERE user_name = '${userName}'")
User selectByUserName(@Param("userName") String userName);
```

## XSS 防护

### 输入过滤

```java
// 使用 HTMLUtils 转义
String safeInput = HtmlUtils.htmlEscape(userInput);
```

### 输出编码

```java
// 响应时转义
@ResponseBody
public String getContent(@RequestParam String content) {
    return HtmlUtils.htmlEscape(content);
}
```

### 配置

```yaml
server.servlet.encoding:
  force: true
  charset: UTF-8
```

## CSRF 防护

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            );
        return http.build();
    }
}
```

## 安全扫描

```bash
# 依赖漏洞扫描
mvn org.owasp:dependency-check-maven:check

# 密钥扫描
git-secrets --scan

# 代码安全扫描
mvn sonar:sonar
```

## 安全检查清单

- [ ] 密码使用 BCrypt 加密
- [ ] 敏感数据不记录日志
- [ ] SQL 使用参数化查询
- [ ] 输入进行验证和转义
- [ ] API 有适当的权限控制
- [ ] Token 有过期机制
- [ ] HTTPS 部署
- [ ] 定期安全扫描

---

**最后更新**: 2026-03-15
