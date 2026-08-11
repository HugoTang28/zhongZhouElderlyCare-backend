package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ResidentBed;
import com.zzyl.system.mapper.ResidentBedMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "residentBed", perm = "resident:bed")
@Service
public class ResidentBedService extends ServiceImpl<ResidentBedMapper, ResidentBed> {
}
