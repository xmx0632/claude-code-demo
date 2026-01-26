-- ========================================
-- V7: 创建数据字典表
-- ========================================
-- 演示如何创建数据字典系统

USE ruoyi_example;

-- ----------------------------
-- 字典类型表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_dict_type (
    dict_id      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    dict_name    VARCHAR(100)    DEFAULT '' COMMENT '字典名称',
    dict_type    VARCHAR(100)    DEFAULT '' COMMENT '字典类型',
    status       CHAR(1)         DEFAULT '0' COMMENT '状态（0正常 1停用）',
    remark       VARCHAR(500)    DEFAULT NULL COMMENT '备注',
    create_time  DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dict_id),
    UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- 字典数据表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_dict_data (
    dict_code    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    dict_sort    INT             DEFAULT 0 COMMENT '字典排序',
    dict_label   VARCHAR(100)    DEFAULT '' COMMENT '字典标签',
    dict_value   VARCHAR(100)    DEFAULT '' COMMENT '字典键值',
    dict_type    VARCHAR(100)    DEFAULT '' COMMENT '字典类型',
    css_class    VARCHAR(100)    DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    list_class   VARCHAR(100)    DEFAULT NULL COMMENT '表格回显样式',
    is_default   CHAR(1)         DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    status       CHAR(1)         DEFAULT '0' COMMENT '状态（0正常 1停用）',
    remark       VARCHAR(500)    DEFAULT NULL COMMENT '备注',
    create_time  DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dict_code),
    KEY idx_dict_type (dict_type)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- ----------------------------
-- 初始化字典类型数据
-- ----------------------------
INSERT INTO sys_dict_type (dict_name, dict_type, status)
VALUES
('用户性别', 'sys_user_sex', '0'),
('用户状态', 'sys_normal_disable', '0'),
('菜单状态', 'sys_show_hide', '0')
ON DUPLICATE KEY UPDATE dict_name = VALUES(dict_name);

-- ----------------------------
-- 初始化字典数据
-- ----------------------------
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status)
VALUES
(1, '男', '0', 'sys_user_sex', NULL, 'default', 'Y', '0'),
(2, '女', '1', 'sys_user_sex', NULL, 'success', 'N', '0'),
(3, '未知', '2', 'sys_user_sex', NULL, 'info', 'N', '0'),
(1, '正常', '0', 'sys_normal_disable', NULL, 'primary', 'Y', '0'),
(2, '停用', '1', 'sys_normal_disable', NULL, 'danger', 'N', '0'),
(1, '显示', '0', 'sys_show_hide', NULL, 'primary', 'Y', '0'),
(2, '隐藏', '1', 'sys_show_hide', NULL, 'danger', 'N', '0')
ON DUPLICATE KEY UPDATE dict_label = VALUES(dict_label);
