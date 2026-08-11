package com.zzyl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select * from sys_user where username = #{username} limit 1")
    SysUser selectByUsername(String username);
}
