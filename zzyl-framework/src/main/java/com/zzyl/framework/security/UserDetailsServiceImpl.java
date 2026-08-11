package com.zzyl.framework.security;

import com.zzyl.system.domain.SysUser;
import com.zzyl.system.service.SysMenuService;
import com.zzyl.system.service.SysRoleService;
import com.zzyl.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * 加载登录用户
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return buildLoginUser(user);
    }

    public LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setRoles(new HashSet<>(roleService.selectRoleKeysByUserId(user.getId())));
        loginUser.setPermissions(new HashSet<>(menuService.selectPermsByUserId(user.getId())));
        return loginUser;
    }
}
