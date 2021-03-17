package com.wechat4j.util.http;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * HTTP请求结果封装
 *
 * @author lizhaoyang
 */
public class HttpResult implements Serializable {

    private static final long serialVersionUID = -2958946534331911477L;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 响应内容
     */
    private String responseContent;

    /**
     * 响应状态
     */
    private boolean status;

    public HttpResult() {
    }

    public HttpResult(int statusCode, String responseContent, boolean status) {
        this.statusCode = statusCode;
        this.responseContent = responseContent;
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
