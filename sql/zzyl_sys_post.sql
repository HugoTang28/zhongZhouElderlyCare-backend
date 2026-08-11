-- ============================================================
-- 中州养老管理系统 - 岗位信息表
-- 关联：权限管理 -> 岗位管理（route.path = /system/post）
-- 后端：Post(@BizModule value=post, perm=system:post) -> /biz/post/...
-- ============================================================

DROP TABLE IF EXISTS sys_post;
CREATE TABLE sys_post (
    id          BIGINT       NOT NULL COMMENT '岗位ID',
    post_code   VARCHAR(64)  NOT NULL COMMENT '岗位编码',
    post_name   VARCHAR(64)  NOT NULL COMMENT '岗位名称',
    post_sort   INT          DEFAULT 0 COMMENT '显示顺序',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0正常 1停用',
    remark      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_code (post_code),
    UNIQUE KEY uk_post_name (post_name)
) ENGINE = InnoDB COMMENT = '岗位信息表';

-- 种子数据
INSERT INTO sys_post (id, post_code, post_name, post_sort, status, remark) VALUES
(1, 'ceo',     '院长',      1, 0, '养老机构院长'),
(2, 'director','部门主管',  2, 0, '各职能部门主管'),
(3, 'nurse',   '护理员',    3, 0, '一线护理人员'),
(4, 'doctor',  '驻院医生',  4, 0, '医疗护理'),
(5, 'finance', '财务专员',  5, 0, '财务收支管理'),
(6, 'recept',  '前台接待',  6, 0, '来访接待登记'),
(7, 'cleaner', '保洁人员',  7, 1, '环境卫生（停用示例）');
