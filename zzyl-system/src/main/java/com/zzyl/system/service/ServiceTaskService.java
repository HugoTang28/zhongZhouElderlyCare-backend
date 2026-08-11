package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ServiceTask;
import com.zzyl.system.mapper.ServiceTaskMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "serviceTask", perm = "service:task")
@Service
public class ServiceTaskService extends ServiceImpl<ServiceTaskMapper, ServiceTask> {
}
