package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.FinancePrestore;
import com.zzyl.system.mapper.FinancePrestoreMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "financePrestore", perm = "finance:prestored")
@Service
public class FinancePrestoreService extends ServiceImpl<FinancePrestoreMapper, FinancePrestore> {
}
