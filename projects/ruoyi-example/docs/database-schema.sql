-- ========================================
-- Ruoyi 示例项目数据库结构
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ruoyi_example DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ruoyi_example;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id        BIGINT          NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    user_name      VARCHAR(30)     NOT NULL COMMENT '用户账号',
    nick_name      VARCHAR(30)     NOT NULL COMMENT '用户昵称',
    email          VARCHAR(50)     DEFAULT '' COMMENT '用户邮箱',
    phonenumber    VARCHAR(11)     DEFAULT '' COMMENT '手机号码',
    sex            CHAR(1)         DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    dept_id        BIGINT          DEFAULT NULL COMMENT '部门ID',
    status         CHAR(1)         DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    deleted        TINYINT(1)      DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
    create_time    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (user_name),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 部门表
-- ----------------------------
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
    dept_id        BIGINT          NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    parent_id      BIGINT          DEFAULT 0 COMMENT '父部门ID',
    dept_name      VARCHAR(30)     NOT NULL COMMENT '部门名称',
    sort_order     INT             DEFAULT 0 COMMENT '显示顺序',
    leader         VARCHAR(20)     DEFAULT NULL COMMENT '负责人',
    phone          VARCHAR(11)     DEFAULT NULL COMMENT '联系电话',
    email          VARCHAR(50)     DEFAULT NULL COMMENT '邮箱',
    status         CHAR(1)         DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    deleted        TINYINT(1)      DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
    create_time    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dept_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id        BIGINT          NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name      VARCHAR(30)     NOT NULL COMMENT '角色名称',
    role_key       VARCHAR(100)    NOT NULL COMMENT '角色权限字符串',
    sort_order     INT             DEFAULT 0 COMMENT '显示顺序',
    status         CHAR(1)         DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
    deleted        TINYINT(1)      DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
    create_time    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (role_id),
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    menu_id        BIGINT          NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    menu_name      VARCHAR(50)     NOT NULL COMMENT '菜单名称',
    parent_id      BIGINT          DEFAULT 0 COMMENT '父菜单ID',
    sort_order     INT             DEFAULT 0 COMMENT '显示顺序',
    path           VARCHAR(200)    DEFAULT '' COMMENT '路由地址',
    component      VARCHAR(255)    DEFAULT NULL COMMENT '组件路径',
    is_frame       TINYINT(1)      DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    menu_type      CHAR(1)         DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible        CHAR(1)         DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    status         CHAR(1)         DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    perms          VARCHAR(100)    DEFAULT NULL COMMENT '权限标识',
    icon           VARCHAR(100)    DEFAULT '#' COMMENT '菜单图标',
    create_time    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (menu_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id        BIGINT          NOT NULL COMMENT '用户ID',
    role_id        BIGINT          NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id        BIGINT          NOT NULL COMMENT '角色ID',
    menu_id        BIGINT          NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO sys_dept VALUES(100,  0,   'XX科技',   0, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW());
INSERT INTO sys_dept VALUES(101,  100, '深圳总公司', 1, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW());
INSERT INTO sys_dept VALUES(102,  100, '长沙分公司', 2, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW());
INSERT INTO sys_dept VALUES(103,  101, '研发部门',   1, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW());
INSERT INTO sys_dept VALUES(104,  101, '市场部门',   2, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW());

INSERT INTO sys_user VALUES(1,  'admin', '管理员', 'admin@ruoyi.com', '15888888888', '1', 103, '0', 0, NOW(), NOW());
INSERT INTO sys_user VALUES(2,  'ry',    '若依',    'admin@ruoyi.com', '15666666666', '1', 103, '0', 0, NOW(), NOW());

INSERT INTO sys_role VALUES(1, '超级管理员', 'admin', 1, '0', 0, NOW(), NOW());
INSERT INTO sys_role VALUES(2, '普通角色',   'common', 2, '0', 0, NOW(), NOW());

INSERT INTO sys_user_role VALUES (1, 1);
INSERT INTO sys_user_role VALUES (2, 2);
