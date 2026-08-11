package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.VisitRegistration;
import com.zzyl.system.mapper.VisitRegistrationMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "visitRegistration", perm = "visit:registration")
@Service
public class VisitRegistrationService extends ServiceImpl<VisitRegistrationMapper, VisitRegistration> {
}
