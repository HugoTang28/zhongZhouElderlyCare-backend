package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.CustomerInfo;
import com.zzyl.system.mapper.CustomerInfoMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "customerInfo", perm = "customer:list")
@Service
public class CustomerInfoService extends ServiceImpl<CustomerInfoMapper, CustomerInfo> {
}
