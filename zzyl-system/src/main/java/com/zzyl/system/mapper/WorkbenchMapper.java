package com.zzyl.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 工作台（数据看板）聚合查询
 */
@Mapper
public interface WorkbenchMapper {

    @Select("SELECT COUNT(*) FROM checkin_admission WHERE status = 0")
    Long countResidentInHouse();

    @Select("SELECT COUNT(*) FROM checkin_admission")
    Long countResidentTotal();

    @Select("SELECT COUNT(*) FROM checkin_admission WHERE DATE(check_in_time) = CURDATE()")
    Long countCheckinToday();

    @Select("SELECT COUNT(*) FROM visit_appointment WHERE DATE(visit_time) = CURDATE()")
    Long countVisitToday();

    @Select("SELECT COUNT(*) FROM visit_appointment WHERE status = 0")
    Long countVisitPending();

    @Select("SELECT COUNT(*) FROM finance_bill WHERE status = 0")
    Long countBillUnpaid();

    @Select("SELECT COALESCE(SUM(amount), 0) FROM finance_bill WHERE status = 0")
    BigDecimal sumBillUnpaidAmount();

    @Select("SELECT COUNT(*) FROM iot_device WHERE status = 1")
    Long countDeviceOnline();

    @Select("SELECT COUNT(*) FROM iot_alarm_rule WHERE status = 0")
    Long countAlarmRuleEnabled();

    @Select("SELECT COUNT(*) FROM checkin_checkout WHERE status = 0")
    Long countCheckoutPending();

    @Select("SELECT COUNT(*) FROM service_task WHERE status = 0")
    Long countTaskPending();

    @Select("SELECT COUNT(*) FROM customer_info")
    Long countCustomer();
}
