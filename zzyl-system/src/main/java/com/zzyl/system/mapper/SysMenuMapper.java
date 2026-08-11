package com.zzyl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单 Mapper
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询用户可见的目录与菜单（用于动态路由）
     */
    @Select("select distinct m.* from sys_menu m " +
            "inner join sys_role_menu rm on m.id = rm.menu_id " +
            "inner join sys_user_role ur on rm.role_id = ur.role_id " +
            "where ur.user_id = #{userId} and m.menu_type in ('M','C') " +
            "and m.status = 0 and m.visible = 0 order by m.sort")
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 查询用户全部权限标识（含按钮级）
     */
    @Select("select distinct m.perms from sys_menu m " +
            "inner join sys_role_menu rm on m.id = rm.menu_id " +
            "inner join sys_user_role ur on rm.role_id = ur.role_id " +
            "where ur.user_id = #{userId} and m.status = 0 and m.perms is not null and m.perms != ''")
    List<String> selectPermsByUserId(Long userId);
}
