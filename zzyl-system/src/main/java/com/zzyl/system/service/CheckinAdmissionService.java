package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.CheckinAdmission;
import com.zzyl.system.mapper.CheckinAdmissionMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "checkinAdmission", perm = "checkin:manage")
@Service
public class CheckinAdmissionService extends ServiceImpl<CheckinAdmissionMapper, CheckinAdmission> {
}
