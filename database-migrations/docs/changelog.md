# 变更日志

本文档记录数据库迁移的重要变更。

## 版本规范

遵循 [语义化版本 2.0.0](https://semver.org/lang/zh-CN/)：

- **主版本号**：不兼容的数据库结构变更
- **次版本号**：向下兼容的功能性新增
- **修订号**：向下兼容的问题修正

## [未发布]

### 新增
- 将迁移脚本从 Spring Boot 项目中分离为独立组件

### 变更
- 无

### 修复
- 无

## [1.0.0] - 2024-01-26

### 新增
- V1: 初始化数据库结构（用户、部门、角色、菜单等表）
- V2: 插入初始数据（默认部门、角色、用户）
- V3: 添加用户头像字段
- V4: 为多个表添加备注字段
- V5: 创建操作日志表
- V6: 添加性能优化索引
- V7: 创建数据字典表

### 数据库表清单

| 表名 | 说明 | 版本 |
|------|------|------|
| sys_user | 用户表 | V1 |
| sys_dept | 部门表 | V1 |
| sys_role | 角色表 | V1 |
| sys_menu | 菜单表 | V1 |
| sys_user_role | 用户角色关联表 | V1 |
| sys_role_menu | 角色菜单关联表 | V1 |
| sys_oper_log | 操作日志表 | V5 |
| sys_dict_type | 字典类型表 | V7 |
| sys_dict_data | 字典数据表 | V7 |

### 索引清单

| 索引名 | 表 | 字段 | 版本 |
|--------|-----|------|------|
| uk_username | sys_user | user_name | V1 |
| idx_dept_id | sys_user | dept_id | V1 |
| idx_status | sys_user | status | V1 |
| idx_avatar | sys_user | avatar | V3 |
| idx_email | sys_user | email | V6 |
| idx_phonenumber | sys_user | phonenumber | V6 |
| idx_status_deleted_dept | sys_user | status, deleted, dept_id | V6 |

## 计划中

### V8 - 用户表增强
- 添加最后登录时间字段
- 添加密码修改时间字段
- 添加登录失败次数字段

### V9 - 通知模块
- 创建通知表
- 创建用户通知关联表

### V10 - 定时任务
- 创建定时任务表
- 创建任务日志表

## 变更类型说明

- **新增** (Added): 新增表、字段、索引等
- **变更** (Changed): 修改表结构、字段类型等
- **废弃** (Deprecated): 标记为废弃，后续版本可能删除
- **删除** (Removed): 删除表、字段等（向下不兼容）
- **修复** (Fixed): 修复数据问题
- **安全** (Security): 安全相关的变更
