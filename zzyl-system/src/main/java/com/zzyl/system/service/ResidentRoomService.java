package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.ResidentRoom;
import com.zzyl.system.mapper.ResidentRoomMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "residentRoom", perm = "resident:room")
@Service
public class ResidentRoomService extends ServiceImpl<ResidentRoomMapper, ResidentRoom> {
}
