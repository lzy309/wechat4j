package com.wechat4j.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author lizhaoyang
 */
public interface Cache {

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return
     */
    Object get(Object key);

    /**
     * 插入缓存
     *
     * @param key 缓存key
     * @param value 缓存value
     * @param duration 过期时间
     * @param unit 时间单位
     */
    void put(Object key, Object value, long duration, TimeUnit unit);

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    void remove(Object key);
}
