package com.zzyl.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzyl.common.constant.Constants;
import com.zzyl.system.domain.FamilyUser;
import com.zzyl.system.domain.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户（Spring Security 上下文主体）。
 * userType = admin 时为管理后台用户（user 非空）；
 * userType = family 时为家属端用户（familyUser 非空）。
 */
@Data
public class LoginUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 本次登录的唯一标识（JWT 中携带） */
    private String uuid;

    /** 管理后台用户信息（admin 类型时非空） */
    private SysUser user;

    /** 家属端用户信息（family 类型时非空） */
    private FamilyUser familyUser;

    /** 用户类型：admin / family */
    private String userType = Constants.USER_TYPE_ADMIN;

    /** 角色 key 集合 */
    private Set<String> roles;

    /** 权限标识集合 */
    private Set<String> permissions;

    @JsonIgnore
    @Override
    public String getPassword() {
        if (Constants.USER_TYPE_FAMILY.equals(userType) && familyUser != null) {
            return familyUser.getPassword();
        }
        return user != null ? user.getPassword() : null;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        if (Constants.USER_TYPE_FAMILY.equals(userType) && familyUser != null) {
            return familyUser.getPhone();
        }
        return user != null ? user.getUsername() : null;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
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
        if (Constants.USER_TYPE_FAMILY.equals(userType)) {
            return familyUser == null || familyUser.getStatus() == null || familyUser.getStatus() == 0;
        }
        return user == null || user.getStatus() == null || user.getStatus() == 0;
    }
}
