package com.zzyl.system.service;

import com.zzyl.system.domain.CheckinAdmission;
import com.zzyl.system.domain.FinanceBill;
import com.zzyl.system.domain.FinancePrestore;
import com.zzyl.system.domain.MessagePush;
import com.zzyl.system.domain.ServicePlan;
import com.zzyl.system.domain.ServiceTask;
import com.zzyl.system.domain.VisitAppointment;
import com.zzyl.system.domain.VisitRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 家属端业务聚合 Service。
 * 核心关联逻辑：手机号 -> checkin_admission.family_phone -> elder_name 集合 -> 各业务表按 elder_name 过滤。
 * 注：当前数据模型以 elder_name 文本关联（无独立老人主键/外键），属现存技术债，本次不重构。
 */
@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyUserService familyUserService;
    private final CheckinAdmissionService checkinAdmissionService;
    private final VisitAppointmentService visitAppointmentService;
    private final VisitRegistrationService visitRegistrationService;
    private final FinanceBillService financeBillService;
    private final FinancePrestoreService financePrestoreService;
    private final ServicePlanService servicePlanService;
    private final ServiceTaskService serviceTaskService;
    private final MessagePushService messagePushService;
    private final IotAlarmRuleService iotAlarmRuleService;
    private final IotDeviceService iotDeviceService;

    /**
     * 家属关联的老人姓名集合（按手机号匹配入住记录的 family_phone，不限在住状态）
     */
    public List<String> getElderNames(String phone) {
        return checkinAdmissionService.lambdaQuery()
                .eq(CheckinAdmission::getFamilyPhone, phone)
                .list()
                .stream()
                .map(CheckinAdmission::getElderName)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /** 我的老人（入住记录） */
    public List<CheckinAdmission> getMyElders(String phone) {
        return checkinAdmissionService.lambdaQuery()
                .eq(CheckinAdmission::getFamilyPhone, phone)
                .list();
    }

    /** 探视记录：预约 + 登记 */
    public Map<String, Object> getVisits(String phone) {
        List<String> names = getElderNames(phone);
        Map<String, Object> result = new HashMap<>();
        if (names.isEmpty()) {
            result.put("appointments", Collections.emptyList());
            result.put("registrations", Collections.emptyList());
            return result;
        }
        result.put("appointments",
                visitAppointmentService.lambdaQuery().in(VisitAppointment::getElderName, names).list());
        result.put("registrations",
                visitRegistrationService.lambdaQuery().in(VisitRegistration::getElderName, names).list());
        return result;
    }

    /** 账单与预存：流水 + 账户余额（各关联老人最近一次预存余额之和） */
    public Map<String, Object> getBills(String phone) {
        List<String> names = getElderNames(phone);
        Map<String, Object> result = new HashMap<>();
        if (names.isEmpty()) {
            result.put("bills", Collections.emptyList());
            result.put("prestores", Collections.emptyList());
            result.put("balance", BigDecimal.ZERO);
            return result;
        }
        result.put("bills",
                financeBillService.lambdaQuery().in(FinanceBill::getElderName, names).list());
        List<FinancePrestore> prestores =
                financePrestoreService.lambdaQuery().in(FinancePrestore::getElderName, names).list();
        result.put("prestores", prestores);

        BigDecimal balance = BigDecimal.ZERO;
        for (String name : names) {
            FinancePrestore latest = financePrestoreService.lambdaQuery()
                    .eq(FinancePrestore::getElderName, name)
                    .orderByDesc(FinancePrestore::getCreateTime)
                    .last("limit 1")
                    .one();
            if (latest != null && latest.getBalance() != null) {
                balance = balance.add(latest.getBalance());
            }
        }
        result.put("balance", balance);
        return result;
    }

    /** 护理动态：计划 + 任务 */
    public Map<String, Object> getCare(String phone) {
        List<String> names = getElderNames(phone);
        Map<String, Object> result = new HashMap<>();
        if (names.isEmpty()) {
            result.put("plans", Collections.emptyList());
            result.put("tasks", Collections.emptyList());
            return result;
        }
        result.put("plans",
                servicePlanService.lambdaQuery().in(ServicePlan::getElderName, names).list());
        result.put("tasks",
                serviceTaskService.lambdaQuery().in(ServiceTask::getElderName, names).list());
        return result;
    }

    /** 消息推送：按接收人（家属手机号）过滤 */
    public List<MessagePush> getMessages(String phone) {
        return messagePushService.lambdaQuery()
                .eq(MessagePush::getReceiver, phone)
                .orderByDesc(MessagePush::getCreateTime)
                .list();
    }

    /** 预警与设备概览：机构级，不绑定具体老人（device/alarm_rule 表无 elder_name） */
    public Map<String, Object> getAlarms(String phone) {
        Map<String, Object> result = new HashMap<>();
        result.put("rules", iotAlarmRuleService.lambdaQuery().list());
        result.put("devices", iotDeviceService.lambdaQuery().list());
        return result;
    }
}
