package com.zzyl.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.system.domain.SysMenu;
import com.zzyl.system.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 Service
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    /**
     * 查询用户可见菜单并组装成树
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = baseMapper.selectMenusByUserId(userId);
        return buildTree(menus);
    }

    /**
     * 查询用户全部权限标识
     */
    public List<String> selectPermsByUserId(Long userId) {
        return baseMapper.selectPermsByUserId(userId);
    }

    /**
     * 列表查询（条件）
     */
    public List<SysMenu> selectMenuList(SysMenu query) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(query.getMenuName()), SysMenu::getMenuName, query.getMenuName())
                .eq(query.getStatus() != null, SysMenu::getStatus, query.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort)
                .list();
    }

    /**
     * 菜单下拉树（全部）
     */
    public List<SysMenu> treeselect() {
        List<SysMenu> menus = lambdaQuery()
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort)
                .list();
        return buildTree(menus);
    }

    /**
     * 构建菜单树
     */
    public List<SysMenu> buildTree(List<SysMenu> menus) {
        if (CollUtil.isEmpty(menus)) {
            return CollUtil.newArrayList();
        }
        List<SysMenu> roots = menus.stream()
                .filter(m -> m.getParentId() == null || m.getParentId() == 0)
                .collect(Collectors.toList());
        roots.forEach(root -> root.setChildren(getChildren(root, menus)));
        return roots;
    }

    private List<SysMenu> getChildren(SysMenu parent, List<SysMenu> menus) {
        List<SysMenu> children = menus.stream()
                .filter(m -> parent.getId().equals(m.getParentId()))
                .collect(Collectors.toList());
        children.forEach(child -> child.setChildren(getChildren(child, menus)));
        return children;
    }

    /**
     * 同父节点下菜单名称唯一
     */
    public boolean checkMenuNameUnique(SysMenu menu) {
        SysMenu info = lambdaQuery()
                .eq(SysMenu::getMenuName, menu.getMenuName())
                .eq(SysMenu::getParentId, menu.getParentId())
                .one();
        return info == null || info.getId().equals(menu.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertMenu(SysMenu menu) {
        if (!checkMenuNameUnique(menu)) {
            throw new ServiceException("新增菜单'" + menu.getMenuName() + "'失败，同目录下菜单名称已存在");
        }
        baseMapper.insert(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenu menu) {
        if (!checkMenuNameUnique(menu)) {
            throw new ServiceException("修改菜单'" + menu.getMenuName() + "'失败，同目录下菜单名称已存在");
        }
        baseMapper.updateById(menu);
    }

    /**
     * 删除菜单（有子节点不允许删）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuById(Long menuId) {
        long count = lambdaQuery().eq(SysMenu::getParentId, menuId).count();
        if (count > 0) {
            throw new ServiceException("存在子菜单，不允许删除");
        }
        removeById(menuId);
    }

    public SysMenu selectMenuById(Long menuId) {
        return getById(menuId);
    }
}
