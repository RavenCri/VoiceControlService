package com.web.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-24 13:43
 **/
public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return null;
    }
}
