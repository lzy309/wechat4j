package com.wechat4j.util.http;

/**
 * @author lizhaoyang
 */
public enum HttpContentTypeEnum {

    /**
     * POST请求头
     */
    FORM_URL_ENCODED("application/x-www-form-urlencoded"),
    JSON("application/json");

    private String content;

    HttpContentTypeEnum(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
