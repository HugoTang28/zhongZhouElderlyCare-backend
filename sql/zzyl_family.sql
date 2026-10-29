-- 家属端-家属用户表
-- 家属以手机号登录，手机号与 checkin_admission.family_phone 关联，从而过滤出其在住老人
CREATE TABLE IF NOT EXISTS family_user (
  id          BIGINT        NOT NULL COMMENT '主键（雪花ID）',
  phone       VARCHAR(20)   NOT NULL COMMENT '手机号（登录账号，关联 checkin_admission.family_phone）',
  openid      VARCHAR(64)   DEFAULT NULL COMMENT '微信 openid（预留，接微信登录时填充）',
  password    VARCHAR(100)  DEFAULT NULL COMMENT 'BCrypt密码（验证码登录可为空）',
  status      TINYINT       DEFAULT 0 COMMENT '状态 0正常 1停用',
  create_time DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_time DATETIME      DEFAULT NULL COMMENT '更新时间',
  create_by   BIGINT        DEFAULT NULL COMMENT '创建人',
  update_by   BIGINT        DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (id),
  UNIQUE KEY uk_family_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属端-家属用户';
