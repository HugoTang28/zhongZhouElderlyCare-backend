package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ServicePlan;
import com.zzyl.system.mapper.ServicePlanMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "servicePlan", perm = "service:plan")
@Service
public class ServicePlanService extends ServiceImpl<ServicePlanMapper, ServicePlan> {
}
