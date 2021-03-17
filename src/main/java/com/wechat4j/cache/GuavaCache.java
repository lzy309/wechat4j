package com.wechat4j.cache;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author lizhaoyang
 */
public class GuavaCache implements Cache {

    private CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();

    @Override
    public Object get(Object key) {
        return cacheBuilder.build().getIfPresent(key);
    }

    @Override
    public void put(Object key, Object value, long duration, TimeUnit unit) {
        cacheBuilder.expireAfterWrite(duration, unit).build().put(key, value);
    }

    @Override
    public void remove(Object key) {
        cacheBuilder.build().invalidate(key);
    }
}
