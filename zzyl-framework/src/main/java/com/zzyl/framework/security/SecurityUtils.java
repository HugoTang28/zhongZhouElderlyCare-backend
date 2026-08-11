package com.zzyl.framework.security;

import com.zzyl.common.constant.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser;
        }
        return null;
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUser().getId();
    }

    /**
     * 是否为超级管理员
     */
    public static boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null
                && loginUser.getRoles() != null
                && loginUser.getRoles().contains(Constants.SUPER_ADMIN);
    }
}
