package com.zzyl.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.system.domain.SysUser;
import com.zzyl.system.domain.SysUserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import com.zzyl.system.mapper.SysUserMapper;
import com.zzyl.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 Service
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public SysUser getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    /**
     * 分页条件查询
     */
    public IPage<SysUser> selectPage(Page<SysUser> page, SysUser query) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StrUtil.isNotBlank(query.getNickName()), SysUser::getNickName, query.getNickName())
                .eq(query.getDeptId() != null, SysUser::getDeptId, query.getDeptId())
                .eq(query.getStatus() != null, SysUser::getStatus, query.getStatus())
                .orderByDesc(SysUser::getCreateTime)
                .page(page);
    }

    /**
     * 账号唯一性校验（排除自身）
     */
    public boolean checkUsernameUnique(SysUser user) {
        SysUser info = lambdaQuery().eq(SysUser::getUsername, user.getUsername()).one();
        return info == null || info.getId().equals(user.getId());
    }

    /**
     * 查询用户已分配角色ID
     */
    public List<Long> selectRoleIdsByUserId(Long userId) {
        return userRoleMapper.selectRoleIdsByUserId(userId);
    }

    /**
     * 新增用户 + 角色关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(SysUser user, Long[] roleIds) {
        if (!checkUsernameUnique(user)) {
            throw new ServiceException("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        baseMapper.insert(user);
        insertUserRoles(user.getId(), roleIds);
    }

    /**
     * 修改用户 + 角色关联（先删后插）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user, Long[] roleIds) {
        baseMapper.updateById(user);
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()));
        insertUserRoles(user.getId(), roleIds);
    }

    private void insertUserRoles(Long userId, Long[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        List<SysUserRole> list = Arrays.stream(roleIds)
                .map(roleId -> new SysUserRole() {{
                    setUserId(userId);
                    setRoleId(roleId);
                }})
                .collect(Collectors.toList());
        for (SysUserRole ur : list) {
            userRoleMapper.insert(ur);
        }
    }

    /**
     * 删除用户（拦截超级管理员与当前登录用户）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserByIds(Long[] ids) {
        Long currentUserId = currentUserId();
        for (Long id : ids) {
            if (currentUserId != null && id.equals(currentUserId)) {
                throw new ServiceException("当前登录用户不可删除");
            }
            // admin 角色 id = 1
            if (userRoleMapper.selectRoleIdsByUserId(id).contains(1L)) {
                throw new ServiceException("不允许删除超级管理员用户");
            }
            removeById(id);
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        }
    }

    /**
     * 从 Spring Security 上下文解析当前登录用户ID
     */
    private Long currentUserId() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (username == null || "anonymousUser".equals(username)) {
                return null;
            }
            SysUser u = getByUsername(username);
            return u == null ? null : u.getId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 重置密码
     */
    public void resetPwd(Long userId, String password) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(password));
        baseMapper.updateById(user);
    }

    /**
     * 修改状态
     */
    public void updateStatus(Long userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        baseMapper.updateById(user);
    }
}
