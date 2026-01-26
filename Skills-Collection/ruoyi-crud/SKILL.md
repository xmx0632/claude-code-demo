---
name: ruoyi-crud
description: 为 Ruoyi 项目生成完整的 CRUD 代码。快速开发新模块时使用。
allowed-tools: ["Read", "Write", "Edit", "Glob", "Bash", "Grep"]
---

# Ruoyi CRUD 代码生成器

为表名 `$ARGUMENTS` 生成完整的 CRUD 代码。

## 生成内容

1. **实体类 (Entity.java)** - JPA 实体，包含字段映射
2. **Mapper 接口 (Mapper.java)** - MyBatis-Plus 接口
3. **Service 层 (Service.java, IService.java)** - 业务逻辑层
4. **Controller 层 (Controller.java)** - REST API 接口

## 项目结构

```
src/main/java/com/example/ruoyi/
├── domain/      # 实体类
├── mapper/      # Mapper 接口
├── service/     # Service 层
└── controller/  # Controller 层
```

## 使用方法

### 基础用法

```
/ruoyi-crud sys_user
```

生成用户表的完整 CRUD 代码。

### 指定包名

```
/ruoyi-crud sys_user --package=com.example.system
```

### 包含字典翻译

```
/ruoyi-crud sys_config --with-dict
```

## 代码规范

生成的代码遵循 Ruoyi 框架规范：
- 使用 MyBatis-Plus CRUD 接口
- Service 层实现事务管理
- Controller 使用统一响应结果
- 支持数据权限注解

## 注意事项

- 表名使用下划线命名（如：sys_user）
- 实体类使用驼峰命名（如：SysUser）
- 确保数据库表已创建
- 主键字段命名为 id 或 表名_id

## 后续步骤

代码生成后：
1. 检查字段类型是否正确
2. 补充业务逻辑
3. 添加权限注解
4. 编写单元测试
