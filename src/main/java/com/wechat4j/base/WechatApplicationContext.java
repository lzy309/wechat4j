package com.wechat4j.base;

import com.wechat4j.base.auth.AccessToken;
import com.wechat4j.base.auth.Authorize;

/**
 * @author lizhaoyang
 */
public abstract class WechatApplicationContext {

    private Authorize authorize;

    public WechatApplicationContext(Authorize authorize) {
        this.authorize = authorize;
    }

    public AccessToken getAccessToken() {
        return authorize.getAccessToken(false);
    }

    public void refreshAccessToken() {
        authorize.getAccessToken(true);
    }
}
