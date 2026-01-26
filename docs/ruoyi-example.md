# 完整示例项目：Ruoyi 管理系统

## 项目概述

本项目是一个基于 Ruoyi 框架的简化版管理系统，展示了如何在实际开发中运用 Claude Code + Skills 提升开发效率。

### 技术栈

- **后端框架**: Spring Boot 3.x
- **基础框架**: Ruoyi-Vue-Plus
- **持久层**: MyBatis-Plus 3.5+
- **数据库**: MySQL 8.0
- **工具库**: Lombok, Hutool, Knife4j

### 项目结构

```
ruoyi-example/
├── pom.xml                          # Maven 配置
├── src/main/java/com/example/ruoyi/
│   ├── controller/                  # 控制器层
│   │   └── UserController.java
│   ├── service/                     # 服务层
│   │   ├── IUserService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   ├── mapper/                      # 数据访问层
│   │   └── UserMapper.java
│   └── domain/                      # 实体类
│       └── User.java
├── src/main/resources/
│   ├── application.yml              # 应用配置
│   └── mapper/
│       └── UserMapper.xml           # MyBatis XML
└── docs/
    ├── api.md                       # API 文档
    └── database-schema.md           # 数据库结构
```

### 功能模块

- **用户管理**: 用户 CRUD、状态管理、角色分配
- **部门管理**: 组织架构树形结构
- **菜单管理**: 动态菜单配置
- **角色管理**: 权限控制

## 环境搭建

### 1. 克隆项目

```bash
git clone https://github.com/your-org/claude-code-demo.git
cd claude-code-demo/ruoyi-example
```

### 2. 数据库初始化

本项目使用独立的 **database-migrations** 组件管理数据库版本。

**方式一：使用 Flyway 命令行工具（推荐）**

```bash
# 1. 进入数据库迁移组件目录
cd database-migrations

# 2. 配置数据库连接
cp flyway.conf flyway.local.conf
vi flyway.local.conf  # 修改数据库连接信息

# 3. 初始化数据库（可选）
./scripts/init-db.sh

# 4. 执行迁移
./scripts/migrate.sh

# 5. 查看迁移状态
./scripts/info.sh
```

**方式二：手动导入 SQL**

```bash
# 创建数据库并导入初始结构
mysql -u root -p -e "CREATE DATABASE ruoyi_example DEFAULT CHARSET utf8mb4"
mysql -u root -p ruoyi_example < docs/database-schema.sql
```

**注意**：database-migrations 是独立组件，可在任何项目中复用。

### 3. 配置文件

修改 `application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ruoyi_example
    username: root
    password: your_password
```

### 4. 启动项目

```bash
# 使用 Maven
mvn spring-boot:run

# 或使用 IDE 运行 RuoyiExampleApplication
```

访问: http://localhost:8080

## Skills 应用场景

### 场景 1: 快速开发新模块

**需求**: 开发一个新的"公告管理"模块，包含完整的 CRUD 功能。

#### 步骤 1: 使用 /ruoyi-crud 生成代码

```bash
# 在项目根目录执行
/ruoyi-crud sys_notice
```

**Claude Code 自动生成**:

1. **实体类** (`domain/Notice.java`):

```java
package com.example.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_notice")
public class Notice {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String noticeType; // 公告类型：1通知 2公告

    private String status; // 状态：0正常 1关闭

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

2. **Mapper 接口** (`mapper/NoticeMapper.java`):

```java
package com.example.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ruoyi.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
```

3. **Service 层** (`service/INoticeService.java`, `service/impl/NoticeServiceImpl.java`):

```java
public interface INoticeService extends IService<Notice> {
}

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
        implements INoticeService {
}
```

4. **Controller 层** (`controller/NoticeController.java`):

```java
@RestController
@RequestMapping("/system/notice")
public class NoticeController extends BaseController {

    @Autowired
    private INoticeService noticeService;

    @GetMapping("/list")
    public TableDataInfo list(Notice notice) {
        startPage();
        List<Notice> list = noticeService.selectList(notice);
        return getDataTable(list);
    }

    @PostMapping
    public AjaxResult add(@RequestBody Notice notice) {
        return toAjax(noticeService.save(notice));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Notice notice) {
        return toAjax(noticeService.updateById(notice));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(noticeService.removeByIds(Arrays.asList(ids)));
    }
}
```

#### 步骤 2: 自定义调整

根据实际业务需求调整生成的代码：

```java
// 添加业务方法
@GetMapping("/{id}")
public AjaxResult getInfo(@PathVariable Long id) {
    return success(noticeService.getById(id));
}

// 添加条件查询
private LambdaQueryWrapper<Notice> buildWrapper(Notice notice) {
    return new LambdaQueryWrapper<Notice>()
            .like(StringUtils.isNotBlank(notice.getTitle()),
                 Notice::getTitle, notice.getTitle())
            .eq(StringUtils.isNotBlank(notice.getNoticeType()),
                Notice::getNoticeType, notice.getNoticeType());
}
```

#### 步骤 3: 测试验证

```bash
# 启动项目
mvn spring-boot:run

# 测试 API
curl http://localhost:8080/system/notice/list
```

**效率提升**: 从手动编写 200+ 行代码 → 只需 1 条命令 + 少量调整

---

### 场景 2: 代码重构优化

**需求**: 对现有用户管理模块进行代码审查和优化。

#### 步骤 1: 使用 /code-review 审查代码

```bash
/code-review src/main/java/com/example/ruoyi/controller/UserController.java
```

**审查报告示例**:

```
## 代码审查报告 - UserController.java

### 总体评分: B

### 问题 1: 潜在的空指针风险（严重）

**位置**: UserController.java:45
**问题**: 直接使用 user.getUserName() 而未检查 null
```java
String userName = user.getUserName(); // 可能 NPE
```
**建议**: 添加空值检查
```java
if (user != null && StringUtils.isNotBlank(user.getUserName())) {
    String userName = user.getUserName();
}
```

### 问题 2: SQL 注入风险（严重）

**位置**: UserController.java:78
**问题**: 拼接 SQL 查询条件
```java
String sql = "SELECT * FROM sys_user WHERE user_name LIKE '%" + keyword + "%'";
```
**建议**: 使用参数化查询
```java
@Select("SELECT * FROM sys_user WHERE user_name LIKE CONCAT('%', #{keyword}, '%')")
```

### 问题 3: 缺少事务注解（中等）

**位置**: UserService.java:120
**问题**: 批量删除操作没有事务控制
**建议**: 添加 @Transactional(rollbackFor = Exception.class)

### 优化建议:

1. **性能优化**: 使用 @Cacheable 注解缓存用户列表
2. **代码规范**: 方法名应符合 Java 命名规范
3. **异常处理**: 统一使用全局异常处理器
```

#### 步骤 2: 使用 /sql-optimizer 优化查询

```bash
/sql-optimizer UserMapper.xml
```

**优化建议**:

```
## SQL 优化报告

### 问题 1: 缺少索引（严重）

**SQL**: SELECT * FROM sys_user WHERE dept_id = 10
**问题**: dept_id 字段无索引，全表扫描
**建议**:
```sql
CREATE INDEX idx_dept_id ON sys_user(dept_id);
```
**收益**: 查询速度提升 10-50 倍

### 问题 2: SELECT * 性能问题（中等）

**SQL**: SELECT * FROM sys_user WHERE status = '0'
**建议**: 只查询需要的字段
```sql
SELECT id, user_name, nick_name, email FROM sys_user WHERE status = '0'
```

### 问题 3: N+1 查询问题（严重）

**位置**: UserServiceImpl.java:85
**问题**: 在循环中查询用户角色
```java
for (User user : users) {
    List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
    user.setRoles(roles);
}
```
**建议**: 使用批量查询或 JOIN
```sql
SELECT u.*, r.* FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.dept_id = #{deptId}
```
```

#### 步骤 3: 使用 /test-gen 补充测试

```bash
/test-gen UserService --package=com.example.ruoyi.service
```

**生成的测试类**:

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void testSelectUserById() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserName("admin");
        when(userMapper.selectById(userId)).thenReturn(user);

        // When
        User result = userService.selectUserById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserName()).isEqualTo("admin");
    }

    @Test
    void testSelectUserById_NotFound() {
        // Given
        Long userId = 999L;
        when(userMapper.selectById(userId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> userService.selectUserById(userId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("用户不存在");
    }
}
```

**重构成果**:
- 修复 3 个严重安全问题
- 性能提升 50%+
- 测试覆盖率达到 70%

---

### 场景 3: API 文档自动生成

**需求**: 为现有接口生成 API 文档供前端团队使用。

#### 步骤 1: 使用 /api-doc 生成文档

```bash
/api-doc --output=docs/api.md
```

**生成的文档示例**:

```markdown
# API 文档

## 用户管理

### 用户列表

- **接口**: `GET /system/user/list`
- **描述**: 分页查询用户列表
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | int | 否 | 页码，默认 1 |
| pageSize | int | 否 | 每页数量，默认 10 |
| userName | string | 否 | 用户名（模糊查询） |
| status | string | 否 | 用户状态（0正常 1停用） |

- **响应示例**:

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "userId": 1,
      "userName": "admin",
      "nickName": "管理员",
      "email": "admin@example.com",
      "status": "0",
      "createTime": "2024-01-01 12:00:00"
    }
  ],
  "total": 100
}
```

### 新增用户

- **接口**: `POST /system/user`
- **描述**: 创建新用户
- **请求体**:

```json
{
  "userName": "zhangsan",
  "nickName": "张三",
  "email": "zhangsan@example.com",
  "password": "123456",
  "deptId": 103
}
```

- **响应**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 删除用户

- **接口**: `DELETE /system/user/{ids}`
- **描述**: 批量删除用户
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | long | 是 | 用户 ID，多个用逗号分隔 |

```

#### 步骤 2: 导出并分享

```bash
# 导出为 PDF
pandoc docs/api.md -o docs/api.pdf

# 或部署到 GitLab Pages
mv docs/api.md docs/public/api.md
git push
```

---

### 场景 4: 数据库版本控制

**需求**: 为用户表添加一个"最后登录时间"字段，需要使用 Flyway 进行增量迁移。

#### 步骤 1: 使用 /flyway-migration 创建迁移脚本

```bash
/flyway-migration create --table=sys_user --type=add_column --column=last_login_time
```

**Claude Code 自动生成迁移脚本** (`V8__add_last_login_time_to_user.sql`):

```sql
-- ========================================
-- V8: 为用户表添加最后登录时间字段
-- ========================================
USE ruoyi_example;

-- 添加最后登录时间字段
ALTER TABLE sys_user
ADD COLUMN last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间';

-- 添加索引优化查询
CREATE INDEX idx_last_login_time ON sys_user(last_login_time);
```

#### 步骤 2: 验证迁移脚本

```bash
/flyway-migration validate V8__add_last_login_time_to_user.sql
```

**验证结果**：
- ✅ SQL 语法正确
- ✅ 使用增量变更（ALTER TABLE）
- ✅ 版本号正确递增
- ✅ 包含必要的注释
- ✅ 添加了性能优化索引

#### 步骤 3: 生成回滚脚本

```bash
/flyway-migration rollback V8__add_last_login_time_to_user.sql
```

**生成的回滚脚本**:

```sql
-- 回滚 V8 迁移
DROP INDEX idx_last_login_time ON sys_user;
ALTER TABLE sys_user DROP COLUMN last_login_time;
```

#### 步骤 4: 执行迁移

启动项目，Flyway 自动执行迁移：

```bash
mvn spring-boot:run
```

**控制台输出**：
```
Flyway Community Edition 9.22.3 by Redgate
Database: jdbc:mysql://localhost:3306/ruoyi_example (MySQL 8.0)
Successfully validated 8 migrations
Creating Schema History table `ruoyi_example`.`flyway_schema_history` ...
Current version of schema `ruoyi_example`: << Empty Schema >>
Migrating schema `ruoyi_example` to version "1 - init schema"
Migrating schema `ruoyi_example` to version "2 - insert init data"
...
Migrating schema `ruoyi_example` to version "8 - add last login time to user"
Successfully applied 8 migrations to schema `ruoyi_example`
```

#### 步骤 5: 验证结果

```sql
-- 检查表结构
DESC sys_user;

-- 查看 Flyway 迁移历史
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

**迁移历史示例**：

| installed_rank | version | description | type | script | execution_time | success |
|----------------|---------|-------------|------|--------|----------------|---------|
| 1 | 1 | init schema | SQL | V1__init_schema.sql | 543 | true |
| 2 | 2 | insert init data | SQL | V2__insert_init_data.sql | 123 | true |
| ... | ... | ... | ... | ... | ... | ... |
| 8 | 8 | add last login time to user | SQL | V8__add_last_login_time_to_user.sql | 45 | true |

**开发流程总结**：

```
1. 使用 /flyway-migration 生成迁移脚本
   ↓
2. 在测试环境验证迁移脚本
   ↓
3. 执行迁移并验证结果
   ↓
4. 提交迁移脚本到版本控制
   ↓
5. 代码审查后合并到主分支
   ↓
6. 生产环境自动执行迁移
```

---

## 完整工作流示例

### 开发新功能的全流程

```
1. 需求分析
   ↓
2. /ruoyi-crud sys_feedback  → 生成基础 CRUD 代码
   ↓
3. 根据业务逻辑调整代码
   ↓
4. /test-gen FeedbackService → 生成单元测试
   ↓
5. 运行测试确保功能正确
   ↓
6. /code-review controller/FeedbackController.java → 代码审查
   ↓
7. /sql-optimizer FeedbackMapper.xml → SQL 优化
   ↓
8. /api-doc → 生成 API 文档
   ↓
9. 提交代码 + Code Review
```

**时间对比**:

| 阶段 | 传统开发 | 使用 Claude Code + Skills | 节省时间 |
|------|---------|---------------------------|---------|
| CRUD 代码 | 2-3 小时 | 10 分钟 | 90% |
| 单元测试 | 1-2 小时 | 5 分钟 | 95% |
| 代码审查 | 30 分钟 | 5 分钟 | 83% |
| API 文档 | 1 小时 | 2 分钟 | 97% |
| **总计** | **4-6 小时** | **25 分钟** | **90%+** |

## 实战技巧

### 技巧 1: 组合使用 Skills

```bash
# 生成代码 → 审查 → 优化
/ruoyi-crud sys_product && \
/code-review src/main/java/com/example/ruoyi/domain/Product.java && \
/sql-optimizer ProductMapper.xml
```

### 技巧 2: 在 Happy 中远程操作

通过 Happy 手机 App：

1. 打开会话: 选择 "ruoyi-example" 项目
2. 输入命令: `/ruoyi-crud sys_order`
3. 查看结果: 实时查看生成的代码
4. 语音调整: "给订单表添加支付状态字段"

### 技巧 3: 版本控制集成

```bash
# 生成代码后自动提交
/ruoyi-crud sys_log
git add .
git commit -m "feat: 添加日志管理模块"
git push
```

## 下一步

- 探索更多自定义 Skills
- 参与社区贡献
- 分享你的实战经验

更多内容请参考 [最佳实践](./best-practices.md) 和 [FAQ](./appendix.md)。
