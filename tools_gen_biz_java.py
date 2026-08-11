# 生成业务模块 Java 文件：entity / mapper / service
import os

BASE = "zzyl-system/src/main/java/com/zzyl/system"

# (entity, table, moduleKey, permPrefix, classDesc, [(field, javatype), ...])
MODULES = [
    ("VisitAppointment", "visit_appointment", "visitAppointment", "visit:appointment", "来访-预约登记",
     [("visitorName","String"),("phone","String"),("elderName","String"),("visitTime","LocalDateTime"),("purpose","String"),("status","Integer")]),
    ("VisitRegistration", "visit_registration", "visitRegistration", "visit:registration", "来访-来访登记",
     [("visitorName","String"),("phone","String"),("idCard","String"),("elderName","String"),("relation","String"),("arriveTime","LocalDateTime"),("leaveTime","LocalDateTime")]),
    ("CheckinAdmission", "checkin_admission", "checkinAdmission", "checkin:manage", "入退-入住管理",
     [("elderName","String"),("idCard","String"),("sex","Integer"),("age","Integer"),("bedId","Long"),("contractNo","String"),("familyName","String"),("familyPhone","String"),("checkInTime","LocalDateTime"),("status","Integer")]),
    ("CheckinCheckout", "checkin_checkout", "checkinCheckout", "checkin:checkout", "入退-退住管理",
     [("elderName","String"),("bedId","Long"),("checkOutTime","LocalDateTime"),("reason","String"),("refundAmount","BigDecimal"),("status","Integer")]),
    ("ResidentRoom", "resident_room", "residentRoom", "resident:room", "在住-房型管理",
     [("roomNo","String"),("typeName","String"),("floor","String"),("bedCount","Integer"),("area","BigDecimal"),("status","Integer")]),
    ("ResidentBed", "resident_bed", "residentBed", "resident:bed", "在住-床位管理",
     [("bedNo","String"),("roomId","Long"),("status","Integer")]),
    ("ResidentContract", "resident_contract", "residentContract", "resident:contract", "在住-合同管理",
     [("contractNo","String"),("elderName","String"),("bedId","Long"),("startDate","LocalDateTime"),("endDate","LocalDateTime"),("monthlyFee","BigDecimal"),("deposit","BigDecimal"),("status","Integer")]),
    ("ResidentLeave", "resident_leave", "residentLeave", "resident:leave", "在住-请假管理",
     [("elderName","String"),("bedId","Long"),("startTime","LocalDateTime"),("endTime","LocalDateTime"),("reason","String"),("status","Integer")]),
    ("ServiceTask", "service_task", "serviceTask", "service:task", "服务-护理任务",
     [("taskName","String"),("elderName","String"),("executor","String"),("planTime","LocalDateTime"),("status","Integer")]),
    ("ServicePlan", "service_plan", "servicePlan", "service:plan", "服务-护理计划",
     [("planName","String"),("elderName","String"),("startDate","LocalDateTime"),("endDate","LocalDateTime"),("content","String"),("status","Integer")]),
    ("FinanceBill", "finance_bill", "financeBill", "finance:bill", "财务-账单管理",
     [("billNo","String"),("elderName","String"),("billMonth","String"),("billType","String"),("amount","BigDecimal"),("status","Integer")]),
    ("FinancePrestore", "finance_prestore", "financePrestore", "finance:prestored", "财务-预存管理",
     [("elderName","String"),("amount","BigDecimal"),("balance","BigDecimal"),("transType","Integer")]),
    ("OrderInfo", "order_info", "orderInfo", "order:list", "订单-订单管理",
     [("orderNo","String"),("customerName","String"),("productName","String"),("amount","BigDecimal"),("status","Integer")]),
    ("OrderRefund", "order_refund", "orderRefund", "order:refund", "订单-退款管理",
     [("orderNo","String"),("refundNo","String"),("amount","BigDecimal"),("status","Integer")]),
    ("CustomerInfo", "customer_info", "customerInfo", "customer:list", "客户管理",
     [("name","String"),("phone","String"),("idCard","String"),("cusType","String"),("source","String")]),
    ("MessagePush", "message_push", "messagePush", "message:push", "消息中心-消息推送",
     [("title","String"),("content","String"),("receiver","String"),("msgType","String"),("status","Integer")]),
    ("IotDevice", "iot_device", "iotDevice", "iot:device", "智能监测-设备管理",
     [("deviceName","String"),("deviceNo","String"),("deviceType","String"),("location","String"),("status","Integer")]),
    ("IotAlarmRule", "iot_alarm_rule", "iotAlarmRule", "iot:alarm", "智能监测-报警规则",
     [("ruleName","String"),("deviceType","String"),("alarmCondition","String"),("threshold","String"),("alarmLevel","Integer"),("status","Integer")]),
]

ENTITY_TPL = """package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
__IMPORTS__

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("{TABLE}")
@Schema(description = "{DESC}")
public class {ENTITY} extends BaseEntity {__FIELDS__
}
"""

MAPPER_TPL = """package com.zzyl.system.mapper;

import com.zzyl.system.domain.{ENTITY};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface {ENTITY}Mapper extends BaseMapper<{ENTITY}> {
}
"""

SERVICE_TPL = """package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.{ENTITY};
import com.zzyl.system.mapper.{ENTITY}Mapper;
import org.springframework.stereotype.Service;

@BizModule(value = "{KEY}", perm = "{PERM}")
@Service
public class {ENTITY}Service extends ServiceImpl<{ENTITY}Mapper, {ENTITY}> {
}
"""

def imports_block(fields):
    imps = set()
    for _, t in fields:
        if t == "BigDecimal":
            imps.add("import java.math.BigDecimal;")
        elif t == "LocalDateTime":
            imps.add("import java.time.LocalDateTime;")
    return "\n".join(sorted(imps))

def fields_block(fields):
    lines = []
    for f, t in fields:
        lines.append(f"\n    @Schema(description = \"{f}\")")
        lines.append(f"    private {t} {f};")
    return "".join(lines)

for entity, table, key, perm, desc, fields in MODULES:
    imps = imports_block(fields)
    flds = fields_block(fields)
    entity_code = ENTITY_TPL.replace("{TABLE}", table).replace("{DESC}", desc).replace("{ENTITY}", entity).replace("__IMPORTS__", imps).replace("__FIELDS__", flds)
    mapper_code = MAPPER_TPL.replace("{ENTITY}", entity)
    service_code = SERVICE_TPL.replace("{ENTITY}", entity).replace("{KEY}", key).replace("{PERM}", perm)

    d_dir = f"{BASE}/domain"
    m_dir = f"{BASE}/mapper"
    s_dir = f"{BASE}/service"
    os.makedirs(d_dir, exist_ok=True)
    os.makedirs(m_dir, exist_ok=True)
    os.makedirs(s_dir, exist_ok=True)
    with open(f"{d_dir}/{entity}.java", "w", encoding="utf-8") as f:
        f.write(entity_code)
    with open(f"{m_dir}/{entity}Mapper.java", "w", encoding="utf-8") as f:
        f.write(mapper_code)
    with open(f"{s_dir}/{entity}Service.java", "w", encoding="utf-8") as f:
        f.write(service_code)

print("generated", len(MODULES)*3, "files")
