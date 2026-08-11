package com.zzyl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户-角色关联 Mapper
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select role_id from sys_user_role where user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(Long userId);
}
