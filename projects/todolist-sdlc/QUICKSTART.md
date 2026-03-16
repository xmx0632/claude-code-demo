# TodoList 快速启动指南

## 启动服务

### 方式一：手动启动

#### 1. 启动后端

```bash
cd backend

# 使用 Java 17 启动（H2 数据库）
# 请根据实际环境设置 JAVA_HOME，例如：
# export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### 2. 启动前端

```bash
cd frontend

# 首次运行需要安装依赖
npm install

# 启动开发服务器
npm run dev
```

---

## 服务访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端应用 | http://localhost:5173/ | Vue3 前端页面 |
| 后端 API | http://localhost:8080/ | Spring Boot API |
| API 文档 | http://localhost:8080/doc.html | Knife4j 接口文档 |
| H2 控制台 | http://localhost:8080/h2-console | 数据库管理 |

---

## H2 数据库控制台登录

### 访问步骤

1. 浏览器打开：**http://localhost:8080/h2-console**

2. 填写登录信息：
   - **JDBC URL**: `jdbc:h2:mem:todolist`
   - **用户名**: `sa`
   - **密码**: (留空)

3. 点击「连接」按钮

### 数据库表说明

登录后可以查看以下表：
- `t_user` - 用户表
- `t_todo` - 待办事项表

### 常用 SQL

```sql
-- 查看所有表
SHOW TABLES;

-- 查看用户
SELECT * FROM t_user;

-- 查看待办事项
SELECT * FROM t_todo;

-- 查看表结构
DESCRIBE t_user;
```

---

## 用户登录

### 方式一：通过前端页面

1. 打开 http://localhost:5173/
2. 点击「注册」按钮创建账号
3. 使用注册的账号密码登录

### 方式二：通过 API 文档

1. 打开 http://localhost:8080/doc.html
2. 找到「认证接口」- `/api/auth/register`
3. 点击「调试」，输入用户名和密码
4. 注册成功后使用 `/api/auth/login` 登录

---

## 停止服务

### 停止后端

在启动后端的终端按 `Ctrl + C`

### 停止前端

在启动前端的终端按 `Ctrl + C`

---

## 常见问题

### Q: 后端启动失败，提示连接 MySQL？

A: 确保使用 `dev` profile 启动：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Q: 前端启动失败，提示依赖找不到？

A: 删除 node_modules 重新安装：
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Q: H2 数据库数据丢失？

A: H2 使用内存数据库，重启后数据会清空。如需持久化，请配置 MySQL。

---

## 技术栈

- **后端**: Spring Boot 3.2.3 + Java 17 + H2 Database
- **前端**: Vue 3 + Vite + Element Plus
- **API 文档**: Knife4j
