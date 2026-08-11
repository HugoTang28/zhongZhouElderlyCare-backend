package com.zzyl.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzyl.system.domain.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户（Spring Security 上下文主体）
 */
@Data
public class LoginUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 本次登录的唯一标识（JWT 中携带） */
    private String uuid;

    /** 用户信息 */
    private SysUser user;

    /** 角色 key 集合 */
    private Set<String> roles;

    /** 权限标识集合 */
    private Set<String> permissions;

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) {
            return Set.of();
        }
        // 将业务权限标识同步为 Spring Security 的 GrantedAuthority，
        // 供 @PreAuthorize("hasAuthority('xxx')") 校验使用
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return user.getStatus() == null || user.getStatus() == 0;
    }
}
