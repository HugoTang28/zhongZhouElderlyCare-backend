package com.zzyl.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.constant.Constants;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.system.domain.SysRole;
import com.zzyl.system.domain.SysRoleMenu;
import com.zzyl.system.mapper.SysRoleMapper;
import com.zzyl.system.mapper.SysRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 Service
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    public List<String> selectRoleKeysByUserId(Long userId) {
        return baseMapper.selectRoleKeysByUserId(userId);
    }

    /**
     * 角色下拉选项（仅正常状态）
     */
    public List<SysRole> optionSelect() {
        return lambdaQuery().eq(SysRole::getStatus, 0).orderByAsc(SysRole::getSort).list();
    }

    /**
     * 分页条件查询
     */
    public IPage<SysRole> selectPage(Page<SysRole> page, SysRole query) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(query.getRoleName()), SysRole::getRoleName, query.getRoleName())
                .like(StrUtil.isNotBlank(query.getRoleKey()), SysRole::getRoleKey, query.getRoleKey())
                .eq(query.getStatus() != null, SysRole::getStatus, query.getStatus())
                .orderByAsc(SysRole::getSort)
                .page(page);
    }

    public boolean checkRoleKeyUnique(SysRole role) {
        SysRole info = lambdaQuery().eq(SysRole::getRoleKey, role.getRoleKey()).one();
        return info == null || info.getId().equals(role.getId());
    }

    public boolean checkRoleNameUnique(SysRole role) {
        SysRole info = lambdaQuery().eq(SysRole::getRoleName, role.getRoleName()).one();
        return info == null || info.getId().equals(role.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertRole(SysRole role, Long[] menuIds) {
        if (!checkRoleKeyUnique(role)) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        if (!checkRoleNameUnique(role)) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        baseMapper.insert(role);
        insertRoleMenu(role.getId(), menuIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role, Long[] menuIds) {
        if (!checkRoleKeyUnique(role)) {
            throw new ServiceException("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        if (!checkRoleNameUnique(role)) {
            throw new ServiceException("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        baseMapper.updateById(role);
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, role.getId()));
        insertRoleMenu(role.getId(), menuIds);
    }

    private void insertRoleMenu(Long roleId, Long[] menuIds) {
        if (menuIds == null || menuIds.length == 0) {
            return;
        }
        List<SysRoleMenu> list = Arrays.stream(menuIds)
                .map(menuId -> new SysRoleMenu() {{
                    setRoleId(roleId);
                    setMenuId(menuId);
                }})
                .collect(Collectors.toList());
        for (SysRoleMenu rm : list) {
            roleMenuMapper.insert(rm);
        }
    }

    /**
     * 删除角色（拦截超级管理员角色）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(Long[] ids) {
        for (Long id : ids) {
            SysRole role = getById(id);
            if (role == null) {
                continue;
            }
            if (Constants.SUPER_ADMIN.equals(role.getRoleKey())) {
                throw new ServiceException("不允许删除超级管理员角色");
            }
            removeById(id);
            roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        }
    }

    /**
     * 查询角色已分配菜单ID
     */
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    /**
     * 分配菜单权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void authMenu(Long roleId, Long[] menuIds) {
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        insertRoleMenu(roleId, menuIds);
    }
}
