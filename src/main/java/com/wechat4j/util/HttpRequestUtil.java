package com.wechat4j.util;

import com.wechat4j.exception.WechatException;
import com.wechat4j.util.http.HttpResult;
import com.wechat4j.util.http.HttpUtil;
import org.apache.http.HttpException;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.Serializable;
import java.util.Map;

/**
 * 网络请求工具类
 *
 * @author lizhaoyang
 */
public final class HttpRequestUtil {

    private static final int HTTP_TIMEOUT = 300;

    private static final CloseableHttpClient httpClient;

    static {
        httpClient = HttpUtil.getHttpClient(HTTP_TIMEOUT);
    }

    /**
     * Get请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws WechatException
     */
    public static HttpResult get(String url, Map<String, Serializable> params, Map<String, String> headers) throws WechatException {
        try {
            return HttpUtil.get(httpClient, url, params, headers);
        } catch (HttpException e) {
            throw new WechatException(e);
        } finally {
            closeHttpClient();
        }
    }

    /**
     * Post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static HttpResult post(String url, Map<String, Serializable> params, Map<String, String> headers) throws WechatException {
        try {
            return HttpUtil.post(httpClient, url, params, headers);
        } catch (HttpException e) {
            throw new WechatException(e);
        } finally {
            closeHttpClient();
        }
    }

    private static void closeHttpClient() {
        try {
            HttpUtil.closeHttpClient(httpClient);
        } catch (HttpException e) {
            throw new WechatException(e);
        }
    }
}
