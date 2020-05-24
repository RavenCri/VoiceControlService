package com.web.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-24 13:42
 **/
public interface CacheManager {
    //根据缓存名字获取一个Cache
    public <K, V> Cache<K, V> getCache(String name) throws CacheException;
}
