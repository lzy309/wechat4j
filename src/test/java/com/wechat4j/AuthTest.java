package com.wechat4j;

import com.wechat4j.base.auth.Authorize;
import com.wechat4j.cache.GuavaCache;
import com.wechat4j.officialaccount.OfficialAccountAuthorize;
import com.wechat4j.officialaccount.OfficialAccountConfig;

/**
 * @author lizhaoyang
 */
public class AuthTest {

    public static void main(String[] args) {
        OfficialAccountConfig config = new OfficialAccountConfig();
        config.setAppId("wx09fa204a04fbe839");
        config.setSecret("effc9988fd2568153076b36638403f0c");
        Authorize authorize = new OfficialAccountAuthorize(config.getAppId(), config.getSecret(), new GuavaCache());
        authorize.getAccessToken(false);
    }
}
