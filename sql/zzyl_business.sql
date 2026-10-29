-- ============================================================
-- 中州养老管理系统 · 业务模块数据表
-- 说明：所有表均含审计字段 id / create_time / update_time / create_by / update_by
--       id 使用 BIGINT（与 BaseEntity 的 IdType.ASSIGN_ID 雪花算法一致）
-- 字符集：utf8mb4
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 来访管理
-- ----------------------------

-- 预约登记
CREATE TABLE visit_appointment (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    visitor_name VARCHAR(50)  NOT NULL COMMENT '访客姓名',
    phone        VARCHAR(20)  DEFAULT '' COMMENT '访客手机号',
    elder_name   VARCHAR(50)  DEFAULT '' COMMENT '探访老人姓名',
    visit_time   DATETIME     DEFAULT NULL COMMENT '预约探访时间',
    purpose      VARCHAR(200) DEFAULT '' COMMENT '来访事由',
    status       TINYINT      DEFAULT 0 COMMENT '状态：0待接待 1已接待 2已取消',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '来访-预约登记';

-- 来访登记
CREATE TABLE visit_registration (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    visitor_name VARCHAR(50)  NOT NULL COMMENT '访客姓名',
    phone        VARCHAR(20)  DEFAULT '' COMMENT '访客手机号',
    id_card      VARCHAR(20)  DEFAULT '' COMMENT '证件号码',
    elder_name   VARCHAR(50)  DEFAULT '' COMMENT '被探访老人姓名',
    relation     VARCHAR(20)  DEFAULT '' COMMENT '与老人关系',
    arrive_time  DATETIME     DEFAULT NULL COMMENT '到达时间',
    leave_time   DATETIME     DEFAULT NULL COMMENT '离开时间',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '来访-来访登记';

-- ----------------------------
-- 入退管理
-- ----------------------------

-- 入住管理
CREATE TABLE checkin_admission (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_name   VARCHAR(50)  NOT NULL COMMENT '老人姓名',
    id_card      VARCHAR(20)  DEFAULT '' COMMENT '身份证号',
    sex          TINYINT      DEFAULT 0 COMMENT '性别：0男 1女',
    age          INT          DEFAULT 0 COMMENT '年龄',
    bed_id       BIGINT       DEFAULT NULL COMMENT '床位ID',
    contract_no  VARCHAR(50)  DEFAULT '' COMMENT '合同编号',
    family_name  VARCHAR(50)  DEFAULT '' COMMENT '家属姓名',
    family_phone VARCHAR(20)  DEFAULT '' COMMENT '家属电话',
    check_in_time DATETIME    DEFAULT NULL COMMENT '入住时间',
    status       TINYINT      DEFAULT 0 COMMENT '状态：0在住 1已退住',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '入退-入住管理';

-- 退住管理
CREATE TABLE checkin_checkout (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_name    VARCHAR(50)  NOT NULL COMMENT '老人姓名',
    bed_id        BIGINT       DEFAULT NULL COMMENT '床位ID',
    check_out_time DATETIME    DEFAULT NULL COMMENT '退住时间',
    reason        VARCHAR(255) DEFAULT '' COMMENT '退住原因',
    refund_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '退款金额',
    status        TINYINT      DEFAULT 0 COMMENT '状态：0待审批 1已退住',
    remark        VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by     BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by     BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '入退-退住管理';

-- ----------------------------
-- 在住管理
-- ----------------------------

-- 房型管理
CREATE TABLE resident_room (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    room_no    VARCHAR(20)  NOT NULL COMMENT '房号',
    type_name  VARCHAR(50)  DEFAULT '' COMMENT '房型名称（如：标准单间）',
    floor      VARCHAR(10)  DEFAULT '' COMMENT '楼层',
    bed_count  INT          DEFAULT 1 COMMENT '床位数量',
    area       DECIMAL(8,2) DEFAULT 0.00 COMMENT '面积(㎡)',
    status     TINYINT      DEFAULT 0 COMMENT '状态：0启用 1停用',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by  BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by  BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '在住-房型管理';

-- 床位管理
CREATE TABLE resident_bed (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    bed_no      VARCHAR(20)  NOT NULL COMMENT '床位号',
    room_id     BIGINT       DEFAULT NULL COMMENT '所属房型ID',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0空闲 1占用 2维修',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '在住-床位管理';

-- 合同管理
CREATE TABLE resident_contract (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    contract_no  VARCHAR(50)  NOT NULL COMMENT '合同编号',
    elder_name   VARCHAR(50)  DEFAULT '' COMMENT '老人姓名',
    bed_id       BIGINT       DEFAULT NULL COMMENT '床位ID',
    start_date   DATETIME     DEFAULT NULL COMMENT '合同开始日期',
    end_date     DATETIME     DEFAULT NULL COMMENT '合同结束日期',
    monthly_fee  DECIMAL(10,2) DEFAULT 0.00 COMMENT '月费',
    deposit      DECIMAL(10,2) DEFAULT 0.00 COMMENT '押金',
    status       TINYINT      DEFAULT 0 COMMENT '状态：0生效 1到期 2作废',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '在住-合同管理';

-- 请假管理
CREATE TABLE resident_leave (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_name  VARCHAR(50)  NOT NULL COMMENT '老人姓名',
    bed_id      BIGINT       DEFAULT NULL COMMENT '床位ID',
    start_time  DATETIME     DEFAULT NULL COMMENT '请假开始时间',
    end_time    DATETIME     DEFAULT NULL COMMENT '请假结束时间',
    reason      VARCHAR(255) DEFAULT '' COMMENT '请假事由',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0待审批 1已批准 2已驳回',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '在住-请假管理';

-- ----------------------------
-- 服务管理
-- ----------------------------

-- 护理计划
CREATE TABLE service_plan (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    plan_name   VARCHAR(50)  NOT NULL COMMENT '计划名称',
    elder_name  VARCHAR(50)  DEFAULT '' COMMENT '老人姓名',
    start_date  DATETIME     DEFAULT NULL COMMENT '开始日期',
    end_date    DATETIME     DEFAULT NULL COMMENT '结束日期',
    content     VARCHAR(500) DEFAULT '' COMMENT '计划内容',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0启用 1停用',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '服务-护理计划';

-- 护理任务
CREATE TABLE service_task (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    task_name   VARCHAR(50)  NOT NULL COMMENT '任务名称',
    elder_name  VARCHAR(50)  DEFAULT '' COMMENT '老人姓名',
    executor    VARCHAR(50)  DEFAULT '' COMMENT '执行人',
    plan_time   DATETIME     DEFAULT NULL COMMENT '计划执行时间',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0待执行 1执行中 2已完成',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '服务-护理任务';

-- ----------------------------
-- 财务管理
-- ----------------------------

-- 账单管理
CREATE TABLE finance_bill (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    bill_no     VARCHAR(50)  NOT NULL COMMENT '账单编号',
    elder_name  VARCHAR(50)  DEFAULT '' COMMENT '老人姓名',
    bill_month  VARCHAR(10)  DEFAULT '' COMMENT '账期(YYYY-MM)',
    bill_type   VARCHAR(20)  DEFAULT '' COMMENT '账单类型（护理/餐饮/医疗）',
    amount      DECIMAL(10,2) DEFAULT 0.00 COMMENT '金额',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0未缴 1已缴 2已退费',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '财务-账单管理';

-- 预存管理
CREATE TABLE finance_prestore (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_name  VARCHAR(50)  NOT NULL COMMENT '老人姓名',
    amount      DECIMAL(10,2) DEFAULT 0.00 COMMENT '本次金额',
    balance     DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    trans_type  TINYINT      DEFAULT 0 COMMENT '类型：0充值 1消费',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '财务-预存管理';

-- ----------------------------
-- 订单管理
-- ----------------------------

-- 订单管理
CREATE TABLE order_info (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no      VARCHAR(50)  NOT NULL COMMENT '订单编号',
    customer_name VARCHAR(50)  DEFAULT '' COMMENT '客户姓名',
    product_name  VARCHAR(100) DEFAULT '' COMMENT '商品/服务名称',
    amount        DECIMAL(10,2) DEFAULT 0.00 COMMENT '订单金额',
    status        TINYINT      DEFAULT 0 COMMENT '状态：0待支付 1已支付 2已取消 3已完成',
    remark        VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by     BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by     BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单-订单管理';

-- 退款管理
CREATE TABLE order_refund (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no    VARCHAR(50)  NOT NULL COMMENT '原订单编号',
    refund_no   VARCHAR(50)  DEFAULT '' COMMENT '退款单号',
    amount      DECIMAL(10,2) DEFAULT 0.00 COMMENT '退款金额',
    status      TINYINT      DEFAULT 0 COMMENT '状态：0待退款 1已退款 2已拒绝',
    remark      VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by   BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单-退款管理';

-- ----------------------------
-- 客户管理
-- ----------------------------

CREATE TABLE customer_info (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name       VARCHAR(50)  NOT NULL COMMENT '客户姓名',
    phone      VARCHAR(20)  DEFAULT '' COMMENT '手机号',
    id_card    VARCHAR(20)  DEFAULT '' COMMENT '证件号',
    cus_type   VARCHAR(20)  DEFAULT '' COMMENT '客户类型（潜在/签约/流失）',
    source     VARCHAR(50)  DEFAULT '' COMMENT '客户来源',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by  BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by  BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '客户管理';

-- ----------------------------
-- 消息中心
-- ----------------------------

CREATE TABLE message_push (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    title      VARCHAR(100) NOT NULL COMMENT '消息标题',
    content    VARCHAR(500) DEFAULT '' COMMENT '消息内容',
    receiver   VARCHAR(100) DEFAULT '' COMMENT '接收人/角色',
    msg_type   VARCHAR(20)  DEFAULT '' COMMENT '消息类型（通知/提醒/预警）',
    status     TINYINT      DEFAULT 0 COMMENT '状态：0待发送 1已发送 2已读',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by  BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by  BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '消息中心-消息推送';

-- ----------------------------
-- 智能监测
-- ----------------------------

-- 设备管理
CREATE TABLE iot_device (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    device_name  VARCHAR(50)  NOT NULL COMMENT '设备名称',
    device_no    VARCHAR(50)  DEFAULT '' COMMENT '设备编号',
    device_type  VARCHAR(50)  DEFAULT '' COMMENT '设备类型（手环/床垫/摄像头）',
    location     VARCHAR(100) DEFAULT '' COMMENT '安装位置',
    status       TINYINT      DEFAULT 0 COMMENT '状态：0离线 1在线 2故障',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '智能监测-设备管理';

-- 报警规则
CREATE TABLE iot_alarm_rule (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    rule_name     VARCHAR(50)  NOT NULL COMMENT '规则名称',
    device_type   VARCHAR(50)  DEFAULT '' COMMENT '适用设备类型',
    alarm_condition VARCHAR(50) DEFAULT '' COMMENT '触发条件（如：心率>120）',
    threshold     VARCHAR(50)  DEFAULT '' COMMENT '阈值',
    alarm_level   TINYINT      DEFAULT 0 COMMENT '报警级别：0提示 1警告 2严重',
    status        TINYINT      DEFAULT 0 COMMENT '状态：0启用 1停用',
    remark        VARCHAR(255) DEFAULT '' COMMENT '备注',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by     BIGINT       DEFAULT NULL COMMENT '创建人',
    update_by     BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '智能监测-报警规则';

SET FOREIGN_KEY_CHECKS = 1;
