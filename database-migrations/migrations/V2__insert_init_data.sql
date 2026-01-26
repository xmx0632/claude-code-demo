-- ========================================
-- V2: 插入初始数据
-- ========================================

USE ruoyi_example;

-- ----------------------------
-- 初始化部门数据
-- ----------------------------
INSERT INTO sys_dept (dept_id, parent_id, dept_name, sort_order, leader, phone, email, status, deleted, create_time, update_time)
VALUES
(100,  0,   'XX科技',   0, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW()),
(101,  100, '深圳总公司', 1, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW()),
(102,  100, '长沙分公司', 2, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW()),
(103,  101, '研发部门',   1, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW()),
(104,  101, '市场部门',   2, '若依', '15888888888', 'admin@ruoyi.com', '0', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE dept_name = VALUES(dept_name);

-- ----------------------------
-- 初始化角色数据
-- ----------------------------
INSERT INTO sys_role (role_id, role_name, role_key, sort_order, status, deleted, create_time, update_time)
VALUES
(1, '超级管理员', 'admin', 1, '0', 0, NOW(), NOW()),
(2, '普通角色',   'common', 2, '0', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- ----------------------------
-- 初始化用户数据（密码：admin123）
-- ----------------------------
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, dept_id, status, deleted, create_time, update_time)
VALUES
(1,  'admin', '管理员', 'admin@ruoyi.com', '15888888888', '1', 103, '0', 0, NOW(), NOW()),
(2,  'ry',    '若依',    'ry@ruoyi.com',     '15666666666', '1', 103, '0', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE nick_name = VALUES(nick_name);

-- ----------------------------
-- 初始化用户角色关联
-- ----------------------------
INSERT INTO sys_user_role (user_id, role_id)
VALUES
(1, 1),
(2, 2)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
