package com.wechat4j.base.auth;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.wechat4j.cache.Cache;
import com.wechat4j.exception.WechatException;
import com.wechat4j.util.HttpRequestUtil;
import com.wechat4j.util.http.HttpResult;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhaoyang
 */
public abstract class AbstractAuthorize implements Authorize {

    public static final String ACCESS_TOKEN_CACHE_PREFIX = "wechat4j:access_token:";

    private Cache cache;

    public AbstractAuthorize(Cache cache) {
        this.cache = cache;
    }

    @Override
    public AccessToken getAccessToken(boolean refresh) {
        String cacheKey = this.getCacheKey();
        if (refresh) {
           removeAccessToken();
        } else {
            AccessToken accessToken = (AccessToken) cache.get(cacheKey);
            if (accessToken != null) {
                return accessToken;
            }
        }

        AccessToken accessToken = doGetAccessToken(getAccessTokenApi(), getCredential());
        if (accessToken == null) {
            throw new WechatException("获取全局唯一接口调用凭据失败");
        } else if (accessToken.getErrorCode() != 0) {
            throw new WechatException(accessToken.getErrorCode(), accessToken.getErrorMessage());
        }

        cache.put(cacheKey, accessToken, accessToken.getExpiresIn(), TimeUnit.SECONDS);

        return accessToken;
    }

    @Override
    public void removeAccessToken() {
        cache.remove(getCacheKey());
    }

    /**
     * 全局唯一接口调用凭据请求地址
     *
     * @return
     */
    protected abstract String getAccessTokenApi();

    /**
     * 全局唯一接口调用凭据请求参数
     *
     * @return
     */
    protected abstract Map<String, Serializable> getCredential();

    /**
     * AccessToken缓存key
     *
     * @return
     */
    protected String getCacheKey() {
        return ACCESS_TOKEN_CACHE_PREFIX + SecureUtil.md5(JSON.toJSONString(getCredential()));
    }

    private AccessToken doGetAccessToken(String accessTokenApi, Map<String, Serializable> params) {
        HttpResult httpResult = HttpRequestUtil.get(accessTokenApi, params, null);
        if (httpResult.getStatus()) {
            return JSON.parseObject(httpResult.getResponseContent(), AccessToken.class);
        }
        return null;
    }
}
