package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.system.domain.FamilyUser;
import com.zzyl.system.mapper.FamilyUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 家属端-家属用户 Service
 */
@Service
@RequiredArgsConstructor
public class FamilyUserService extends ServiceImpl<FamilyUserMapper, FamilyUser> {

    public FamilyUser getByPhone(String phone) {
        return lambdaQuery().eq(FamilyUser::getPhone, phone).one();
    }

    /**
     * 家属首次登录自动建档（无需后台预注册），返回持久化后的对象
     */
    public FamilyUser getOrCreate(String phone) {
        FamilyUser existing = getByPhone(phone);
        if (existing != null) {
            return existing;
        }
        FamilyUser user = new FamilyUser();
        user.setPhone(phone);
        user.setStatus(0);
        save(user);
        return getByPhone(phone);
    }
}
