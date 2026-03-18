# 图书库存管理系统

一个基于 Spring Boot + Vue 3 的全栈图书库存管理系统，适用于教学演示。

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Data JPA
- Flyway (数据库版本管理)
- SQLite (数据库)
- Maven

### 前端
- Vue 3.4
- Vite 5.0
- Element Plus 2.5
- Vue Router 4.2
- Pinia 2.1
- Axios 1.6

## 项目结构

```
library-management-system/
├── backend/                 # Spring Boot 后端
│   ├── src/main/
│   │   ├── java/com/example/library/
│   │   │   ├── controller/      # 控制器
│   │   │   ├── service/         # 业务逻辑
│   │   │   ├── entity/          # 实体类
│   │   │   ├── repository/      # 数据访问
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── common/          # 公共模块
│   │   │   └── config/          # 配置
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/    # Flyway 迁移脚本
│   └── pom.xml
│
├── frontend/                # Vue 前端
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   ├── components/      # 公共组件
│   │   ├── api/             # API 封装
│   │   ├── router/          # 路由配置
│   │   └── stores/          # 状态管理
│   ├── package.json
│   └── vite.config.js
│
└── docs/                    # 文档
    ├── 需求设计文档.md
    └── 教学提示词文档.md
```

## 功能模块

- **图书管理**: 新增、编辑、删除、查询图书
- **库存管理**: 入库、出库、库存预警
- **分类管理**: 管理图书分类
- **统计概览**: 数据统计仪表盘

## 快速开始

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

访问: http://localhost:8080

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问: http://localhost:5173

## API 接口

| 接口 | 方法 | 描述 |
|------|------|------|
| /api/books | GET | 获取图书列表 |
| /api/books | POST | 新增图书 |
| /api/books/{id} | PUT | 更新图书 |
| /api/books/{id} | DELETE | 删除图书 |
| /api/books/{id}/stock-in | POST | 入库操作 |
| /api/books/{id}/stock-out | POST | 出库操作 |
| /api/categories | GET | 获取分类列表 |
| /api/categories | POST | 新增分类 |
| /api/stats/summary | GET | 获取统计概览 |

## 数据库

数据库文件位置: `backend/data/library.db`

首次启动会自动创建数据库并执行 Flyway 迁移脚本，插入测试数据。

## 开发说明

本项目作为教学演示项目，展示了：

1. **前后端分离架构**
2. **RESTful API 设计**
3. **数据库版本管理 (Flyway)**
4. **Vue 3 组合式 API**
5. **Element Plus 组件使用**

详细说明请查看 `docs/` 目录下的文档。
