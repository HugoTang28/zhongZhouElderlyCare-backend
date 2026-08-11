package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ResidentContract;
import com.zzyl.system.mapper.ResidentContractMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "residentContract", perm = "resident:contract")
@Service
public class ResidentContractService extends ServiceImpl<ResidentContractMapper, ResidentContract> {
}
