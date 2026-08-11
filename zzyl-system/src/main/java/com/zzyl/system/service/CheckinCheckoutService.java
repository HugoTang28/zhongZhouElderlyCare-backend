package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.CheckinCheckout;
import com.zzyl.system.mapper.CheckinCheckoutMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "checkinCheckout", perm = "checkin:checkout")
@Service
public class CheckinCheckoutService extends ServiceImpl<CheckinCheckoutMapper, CheckinCheckout> {
}
