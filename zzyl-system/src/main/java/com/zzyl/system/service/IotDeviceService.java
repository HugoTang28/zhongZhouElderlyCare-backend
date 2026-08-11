package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.IotDevice;
import com.zzyl.system.mapper.IotDeviceMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "iotDevice", perm = "iot:device")
@Service
public class IotDeviceService extends ServiceImpl<IotDeviceMapper, IotDevice> {
}
