package com.wechat4j.util.http;

/**
 * @author lizhaoyang
 */
public enum HttpMethodEnum {

    /**
     * 请求方式
     */
    GET(1, "GET"),

    POST(2, "POST");

    private int type;
    private String message;

    private HttpMethodEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }
}
