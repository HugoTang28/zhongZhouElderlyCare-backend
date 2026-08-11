package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.MessagePush;
import com.zzyl.system.mapper.MessagePushMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "messagePush", perm = "message:push")
@Service
public class MessagePushService extends ServiceImpl<MessagePushMapper, MessagePush> {
}
