package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.FinanceBill;
import com.zzyl.system.mapper.FinanceBillMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "financeBill", perm = "finance:bill")
@Service
public class FinanceBillService extends ServiceImpl<FinanceBillMapper, FinanceBill> {
}
