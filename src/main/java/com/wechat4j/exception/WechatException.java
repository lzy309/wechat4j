package com.wechat4j.exception;

/**
 * @author lizhaoyang
 */
public class WechatException extends RuntimeException {

    private static final long serialVersionUID = -2742935531852618743L;

    private int errorCode;

    private String errorMessage;

    public WechatException() {
        super();
    }

    public WechatException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public WechatException(int errorCode, String errorMessage) {
        this(errorMessage);
        this.errorCode = errorCode;
    }

    public WechatException(Exception e) {
        super(e);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return this.errorCode + ":" + this.errorMessage;
    }
}
