-- ============================================================
-- 家家乐养老管理系统 - 数据库初始化脚本
-- 数据库：zzyl    字符集：utf8mb4
-- 初始账号：admin / 123456
-- ============================================================

CREATE DATABASE IF NOT EXISTS zzyl DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE zzyl;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 部门表
-- ----------------------------
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
    id          BIGINT       NOT NULL COMMENT '部门ID',
    parent_id   BIGINT       DEFAULT 0 COMMENT '父部门ID',
    dept_name   VARCHAR(64)  NOT NULL COMMENT '部门名称',
    leader      VARCHAR(32)  DEFAULT NULL COMMENT '负责人',
    phone       VARCHAR(32)  DEFAULT NULL COMMENT '联系电话',
    sort        INT          DEFAULT 0 COMMENT '显示顺序',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0正常 1停用',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT = '部门表';

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT       NOT NULL COMMENT '用户ID',
    username    VARCHAR(64)  NOT NULL COMMENT '登录账号',
    password    VARCHAR(128) NOT NULL COMMENT '密码（BCrypt）',
    nick_name   VARCHAR(64)  DEFAULT NULL COMMENT '用户昵称',
    real_name   VARCHAR(64)  DEFAULT NULL COMMENT '真实姓名',
    phone       VARCHAR(32)  DEFAULT NULL COMMENT '手机号',
    email       VARCHAR(64)  DEFAULT NULL COMMENT '邮箱',
    avatar      VARCHAR(255) DEFAULT NULL COMMENT '头像',
    sex         TINYINT      DEFAULT 0 COMMENT '性别：0男 1女',
    dept_id     BIGINT       DEFAULT NULL COMMENT '部门ID',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0正常 1停用',
    remark      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_user_username (username)
) ENGINE = InnoDB COMMENT = '用户表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id          BIGINT       NOT NULL COMMENT '角色ID',
    role_name   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    role_key    VARCHAR(64)  NOT NULL COMMENT '角色权限字符串',
    sort        INT          DEFAULT 0 COMMENT '显示顺序',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0正常 1停用',
    remark      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建者',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_role_key (role_key)
) ENGINE = InnoDB COMMENT = '角色表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    id          BIGINT       NOT NULL COMMENT '菜单ID',
    parent_id   BIGINT       DEFAULT 0 COMMENT '父菜单ID',
    menu_name   VARCHAR(64)  NOT NULL COMMENT '菜单名称',
    menu_type   CHAR(1)      NOT NULL COMMENT '类型：M目录 C菜单 F按钮',
    path        VARCHAR(128) DEFAULT '' COMMENT '路由地址',
    component   VARCHAR(128) DEFAULT '' COMMENT '组件路径',
    perms       VARCHAR(128) DEFAULT '' COMMENT '权限标识',
    icon        VARCHAR(64)  DEFAULT '' COMMENT '菜单图标',
    sort        INT          DEFAULT 0 COMMENT '显示顺序',
    visible     TINYINT      DEFAULT 0 COMMENT '是否可见：0显示 1隐藏',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0正常 1停用',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建者',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT = '菜单权限表';

-- ----------------------------
-- 用户-角色关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE = InnoDB COMMENT = '用户角色关联表';

-- ----------------------------
-- 角色-菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE = InnoDB COMMENT = '角色菜单关联表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO sys_dept (id, parent_id, dept_name, leader, sort) VALUES
(1, 0, '家家乐养老集团', '张总', 0),
(2, 1, '院长办公室', '李院长', 1),
(3, 1, '护理部', '王主管', 2),
(4, 1, '财务部', '赵会计', 3),
(5, 1, '后勤部', '钱师傅', 4);

-- 初始管理员：admin / 123456
INSERT INTO sys_user (id, username, password, nick_name, real_name, phone, email, sex, dept_id, status, remark) VALUES
(1, 'admin', '$2b$10$1PFiFl/NQFVJM2oLSEGFJ.UfrEZjnXQqwOqp4Cvxkz76ZAdUGxjou',
 '系统管理员', '管理员', '13800000000', 'admin@zzyl.com', 0, 2, 0, '系统初始化账号');

INSERT INTO sys_role (id, role_name, role_key, sort, remark) VALUES
(1, '超级管理员', 'admin', 1, '拥有全部权限'),
(2, '院长', 'dean', 2, '院长角色'),
(3, '护理主管', 'nursingManager', 3, '护理部主管'),
(4, '护理员', 'nurse', 4, '一线护理人员'),
(5, '财务', 'finance', 5, '财务人员'),
(6, '前台接待', 'reception', 6, '前台接待人员');

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 菜单：M目录 / C菜单 / F按钮
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort) VALUES
-- 工作台
(1,  0, '工作台',   'M', 'workbench',    '',                 '',                      'Odometer',     1),
(101, 1, '数据看板', 'C', 'dashboard',    'dashboard/index',  '',                      'DataAnalysis', 1),
-- 来访管理
(2,  0, '来访管理', 'M', 'visit',        '',                 '',                      'User',         2),
(201, 2, '预约登记', 'C', 'appointment',  'placeholder/index', 'visit:appointment:list',  'Calendar',  1),
(202, 2, '来访登记', 'C', 'registration', 'placeholder/index', 'visit:registration:list', 'EditPen',   2),
-- 入退管理
(3,  0, '入退管理', 'M', 'checkin',      '',                 '',                      'SwitchButton', 3),
(301, 3, '入住管理', 'C', 'manage',       'placeholder/index', 'checkin:manage:list',    'House',     1),
(302, 3, '退住管理', 'C', 'checkout',     'placeholder/index', 'checkin:checkout:list',  'Right',     2),
-- 在住管理
(4,  0, '在住管理', 'M', 'resident',     '',                 '',                      'HomeFilled',   4),
(401, 4, '合同管理', 'C', 'contract',     'placeholder/index', 'resident:contract:list', 'Document',  1),
(402, 4, '床位管理', 'C', 'bed',          'placeholder/index', 'resident:bed:list',      'Grid',      2),
(403, 4, '房型管理', 'C', 'room',         'placeholder/index', 'resident:room:list',     'OfficeBuilding', 3),
(404, 4, '请假管理', 'C', 'leave',        'placeholder/index', 'resident:leave:list',    'Notebook',  4),
-- 服务管理
(5,  0, '服务管理', 'M', 'service',      '',                 '',                      'Service',      5),
(501, 5, '护理任务', 'C', 'task',         'placeholder/index', 'service:task:list',      'List',      1),
(502, 5, '护理计划', 'C', 'plan',         'placeholder/index', 'service:plan:list',      'Memo',      2),
-- 财务管理
(6,  0, '财务管理', 'M', 'finance',      '',                 '',                      'Wallet',       6),
(601, 6, '账单管理', 'C', 'bill',         'placeholder/index', 'finance:bill:list',      'Tickets',   1),
(602, 6, '预存管理', 'C', 'prestored',    'placeholder/index', 'finance:prestored:list', 'Coin',      2),
-- 订单管理
(7,  0, '订单管理', 'M', 'order',        '',                 '',                      'ShoppingCart', 7),
(701, 7, '订单管理', 'C', 'list',         'placeholder/index', 'order:list:list',        'Tickets',   1),
(702, 7, '退款管理', 'C', 'refund',       'placeholder/index', 'order:refund:list',      'RefreshLeft', 2),
-- 客户管理
(8,  0, '客户管理', 'M', 'customer',     '',                 '',                      'UserFilled',   8),
(801, 8, '客户列表', 'C', 'list',         'placeholder/index', 'customer:list:list',     'User',      1),
-- 权限管理
(9,  0, '权限管理', 'M', 'system',       '',                 '',                      'Setting',      9),
(901, 9, '用户管理', 'C', 'user',         'system/user/index',  'system:user:list',       'User',      1),
(902, 9, '角色管理', 'C', 'role',         'system/role/index',  'system:role:list',       'Avatar',    2),
(903, 9, '菜单管理', 'C', 'menu',         'system/menu/index',  'system:menu:list',       'Menu',      3),
(904, 9, '部门管理', 'C', 'dept',         'system/dept/index',  'system:dept:list',       'OfficeBuilding', 4),
(905, 9, '岗位管理', 'C', 'post',         'placeholder/index', 'system:post:list',       'Postcard',  5),
-- 消息中心
(10,   0, '消息中心', 'M', 'message',    '',                 '',                      'Bell',         10),
(1001, 10, '消息推送', 'C', 'push',       'placeholder/index', 'message:push:list',      'Promotion', 1),
-- 智能监测
(11,   0, '智能监测', 'M', 'iot',        '',                 '',                      'Monitor',      11),
(1101, 11, '设备管理', 'C', 'device',     'placeholder/index', 'iot:device:list',        'Cpu',       1),
(1102, 11, '报警规则', 'C', 'alarm',      'placeholder/index', 'iot:alarm:list',         'Warning',   2);

-- 权限管理：按钮级权限（F）。因下方 sys_role_menu 用 `SELECT 1, id FROM sys_menu` 授权，
-- 新增的按钮记录会自动授予超级管理员，无需单独插入 role_menu。
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort) VALUES
(906, 901, '用户查询', 'F', '', '', 'system:user:query',   '', 1),
(907, 901, '用户新增', 'F', '', '', 'system:user:add',     '', 2),
(908, 901, '用户修改', 'F', '', '', 'system:user:edit',    '', 3),
(909, 901, '用户删除', 'F', '', '', 'system:user:remove',  '', 4),
(910, 901, '重置密码', 'F', '', '', 'system:user:resetPwd', '', 5),
(911, 902, '角色查询', 'F', '', '', 'system:role:query',   '', 1),
(912, 902, '角色新增', 'F', '', '', 'system:role:add',     '', 2),
(913, 902, '角色修改', 'F', '', '', 'system:role:edit',    '', 3),
(914, 902, '角色删除', 'F', '', '', 'system:role:remove',  '', 4),
(915, 903, '菜单查询', 'F', '', '', 'system:menu:query',   '', 1),
(916, 903, '菜单新增', 'F', '', '', 'system:menu:add',     '', 2),
(917, 903, '菜单修改', 'F', '', '', 'system:menu:edit',    '', 3),
(918, 903, '菜单删除', 'F', '', '', 'system:menu:remove',  '', 4),
(919, 904, '部门查询', 'F', '', '', 'system:dept:query',   '', 1),
(920, 904, '部门新增', 'F', '', '', 'system:dept:add',     '', 2),
(921, 904, '部门修改', 'F', '', '', 'system:dept:edit',    '', 3),
(922, 904, '部门删除', 'F', '', '', 'system:dept:remove',  '', 4);

-- 超级管理员拥有全部菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

SET FOREIGN_KEY_CHECKS = 1;
