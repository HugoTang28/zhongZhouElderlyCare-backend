package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.IotAlarmRule;
import com.zzyl.system.mapper.IotAlarmRuleMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "iotAlarmRule", perm = "iot:alarm")
@Service
public class IotAlarmRuleService extends ServiceImpl<IotAlarmRuleMapper, IotAlarmRule> {
}
