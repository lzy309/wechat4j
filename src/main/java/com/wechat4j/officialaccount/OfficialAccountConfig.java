package com.wechat4j.officialaccount;

import java.io.Serializable;

/**
 * 微信公众号配置类
 *
 * @author lizhaoyang
 */
public class OfficialAccountConfig implements Serializable {

    private static final long serialVersionUID = -6598930458921802918L;

    private String appId;

    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
