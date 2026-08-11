package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ResidentLeave;
import com.zzyl.system.mapper.ResidentLeaveMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "residentLeave", perm = "resident:leave")
@Service
public class ResidentLeaveService extends ServiceImpl<ResidentLeaveMapper, ResidentLeave> {
}
