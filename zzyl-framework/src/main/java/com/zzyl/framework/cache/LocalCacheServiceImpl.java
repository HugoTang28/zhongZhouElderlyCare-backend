package com.zzyl.framework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存实现（默认）。
 * 单体/开发环境使用；集群部署需切换为 Redis 实现。
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "zzyl.cache.type", havingValue = "local", matchIfMissing = true)
public class LocalCacheServiceImpl implements CacheService {

    private record Entry(Object value, long expireAt) {

        boolean expired() {
                return expireAt > 0 && System.currentTimeMillis() > expireAt;
            }
        }

    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        long expireAt = timeout > 0 ? System.currentTimeMillis() + unit.toMillis(timeout) : -1;
        store.put(key, new Entry(value, expireAt));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Entry entry = store.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.expired()) {
            store.remove(key);
            return null;
        }
        return (T) entry.value();
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }
}
