package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.OrderInfo;
import com.zzyl.system.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "orderInfo", perm = "order:list")
@Service
public class OrderInfoService extends ServiceImpl<OrderInfoMapper, OrderInfo> {
}
