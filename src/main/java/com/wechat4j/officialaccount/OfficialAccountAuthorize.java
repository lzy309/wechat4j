package com.wechat4j.officialaccount;

import com.google.common.collect.Maps;
import com.wechat4j.base.auth.AbstractAuthorize;
import com.wechat4j.cache.Cache;
import com.wechat4j.constant.OfficialAccountApi;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lizhaoyang
 */
public class OfficialAccountAuthorize extends AbstractAuthorize {

    private static final String GRANT_TYPE = "client_credential";

    private String appId;

    private String secret;

    public OfficialAccountAuthorize(String appId, String secret, Cache cache) {
        super(cache);
        this.appId = appId;
        this.secret = secret;
    }

    @Override
    protected String getAccessTokenApi() {
        return OfficialAccountApi.GET_ACCESS_TOKEN_API;
    }

    @Override
    protected Map<String, Serializable> getCredential() {
        Map<String, Serializable> credential = Maps.newHashMap();
        credential.put("grant_type", GRANT_TYPE);
        credential.put("appid", this.appId);
        credential.put("secret", this.secret);

        return credential;
    }
}
