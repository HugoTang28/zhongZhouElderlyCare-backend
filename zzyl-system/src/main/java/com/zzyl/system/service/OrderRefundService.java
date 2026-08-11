package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.OrderRefund;
import com.zzyl.system.mapper.OrderRefundMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "orderRefund", perm = "order:refund")
@Service
public class OrderRefundService extends ServiceImpl<OrderRefundMapper, OrderRefund> {
}
