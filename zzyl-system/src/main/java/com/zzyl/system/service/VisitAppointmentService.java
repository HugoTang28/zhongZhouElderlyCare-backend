package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.VisitAppointment;
import com.zzyl.system.mapper.VisitAppointmentMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "visitAppointment", perm = "visit:appointment")
@Service
public class VisitAppointmentService extends ServiceImpl<VisitAppointmentMapper, VisitAppointment> {
}
