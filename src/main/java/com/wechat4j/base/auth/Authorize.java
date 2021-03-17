package com.wechat4j.base.auth;

/**
 * @author lizhaoyang
 */
public interface Authorize {

    /**
     * 全局唯一接口调用凭据
     *
     * @param refresh 是否刷新缓存
     * @return
     */
    AccessToken getAccessToken(boolean refresh);

    /**
     * 删除全局唯一接口调用凭据
     */
    void removeAccessToken();
}
