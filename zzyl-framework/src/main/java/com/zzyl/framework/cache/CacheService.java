package com.zzyl.framework.cache;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务抽象。
 * <p>
 * 默认实现为本地缓存 {@link LocalCacheServiceImpl}，无需中间件即可运行；
 * 后续接入 Redis 时，新增 RedisCacheServiceImpl 并设置配置项
 * {@code zzyl.cache.type=redis} 即可无侵入切换。
 */
public interface CacheService {

    /**
     * 写入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 读取缓存
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     */
    void delete(String key);
}
