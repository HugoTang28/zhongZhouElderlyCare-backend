package com.zzyl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select r.role_key from sys_role r " +
            "inner join sys_user_role ur on r.id = ur.role_id " +
            "where ur.user_id = #{userId} and r.status = 0")
    List<String> selectRoleKeysByUserId(Long userId);
}
