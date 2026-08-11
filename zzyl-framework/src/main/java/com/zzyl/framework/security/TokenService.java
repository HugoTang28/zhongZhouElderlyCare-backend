package com.zzyl.framework.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zzyl.common.constant.Constants;
import com.zzyl.framework.cache.CacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Token 服务：签发、解析、刷新登录态。
 * 登录态存储在 CacheService（本地缓存/Redis），JWT 仅携带 uuid。
 */
@Component
@RequiredArgsConstructor
public class TokenService {

    /** JWT 签名密钥（生产环境务必通过配置覆盖） */
    @Value("${zzyl.token.secret:zhongzhou-elderly-care-secret-key-2026}")
    private String secret;

    @Value("${zzyl.token.expire-minutes:720}")
    private long expireMinutes;

    private final CacheService cacheService;

    /**
     * 创建 token 并缓存登录用户
     */
    public String createToken(LoginUser loginUser) {
        String uuid = IdUtil.fastSimpleUUID();
        loginUser.setUuid(uuid);
        refreshToken(loginUser);

        SecretKey key = Keys.hmacShaKeyFor(paddingSecret());
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireMinutes * 60_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从请求中解析登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = resolveToken(request);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(paddingSecret()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return cacheService.get(Constants.LOGIN_USER_KEY + claims.getSubject(), LoginUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 刷新缓存有效期
     */
    public void refreshToken(LoginUser loginUser) {
        cacheService.set(Constants.LOGIN_USER_KEY + loginUser.getUuid(),
                loginUser, expireMinutes, TimeUnit.MINUTES);
    }

    /**
     * 删除登录态
     */
    public void removeToken(String uuid) {
        if (StrUtil.isNotBlank(uuid)) {
            cacheService.delete(Constants.LOGIN_USER_KEY + uuid);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.TOKEN_HEADER);
        if (StrUtil.isNotBlank(header) && header.startsWith(Constants.TOKEN_PREFIX)) {
            return header.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    /** HS256 要求密钥 >= 32 字节，不足则补长 */
    private byte[] paddingSecret() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= 32) {
            return bytes;
        }
        byte[] padded = new byte[32];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }
}
