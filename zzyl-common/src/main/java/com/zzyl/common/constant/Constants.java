package com.zzyl.common.constant;

/**
 * 通用常量
 */
public class Constants {

    /** 请求头中的 Token 键 */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 登录用户在缓存中的 key 前缀 */
    public static final String LOGIN_USER_KEY = "login_user:";

    /** Token 有效期（毫秒），默认 12 小时 */
    public static final long TOKEN_EXPIRE = 12 * 60 * 60 * 1000L;

    /** 超级管理员角色编码 */
    public static final String SUPER_ADMIN = "admin";
}
