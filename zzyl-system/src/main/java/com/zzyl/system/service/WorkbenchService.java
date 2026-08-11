package com.zzyl.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyl.system.domain.VisitAppointment;
import com.zzyl.system.mapper.WorkbenchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作台（数据看板）统计服务
 */
@Service
public class WorkbenchService {

    @Autowired
    private WorkbenchMapper workbenchMapper;

    @Autowired
    private VisitAppointmentService visitAppointmentService;

    public Map<String, Object> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("residentInHouse", workbenchMapper.countResidentInHouse());
        m.put("residentTotal", workbenchMapper.countResidentTotal());
        m.put("checkinToday", workbenchMapper.countCheckinToday());
        m.put("visitToday", workbenchMapper.countVisitToday());
        m.put("visitPending", workbenchMapper.countVisitPending());
        m.put("billUnpaid", workbenchMapper.countBillUnpaid());
        m.put("billUnpaidAmount", workbenchMapper.sumBillUnpaidAmount());
        m.put("deviceOnline", workbenchMapper.countDeviceOnline());
        m.put("alarmRuleEnabled", workbenchMapper.countAlarmRuleEnabled());
        m.put("checkoutPending", workbenchMapper.countCheckoutPending());
        m.put("taskPending", workbenchMapper.countTaskPending());
        m.put("customerTotal", workbenchMapper.countCustomer());

        // 今日来访（最新 6 条）
        QueryWrapper<VisitAppointment> qw = new QueryWrapper<>();
        qw.apply("DATE(visit_time) = CURDATE()").orderByDesc("visit_time");
        IPage<VisitAppointment> page = new Page<>(1, 6);
        m.put("recentVisits", visitAppointmentService.page(page, qw).getRecords());
        return m;
    }
}
