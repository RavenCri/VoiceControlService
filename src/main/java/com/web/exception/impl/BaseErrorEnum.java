package com.web.exception.impl;


import com.web.exception.BaseErrorInfoInterface;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-15 23:45
 **/
public enum  BaseErrorEnum implements BaseErrorInfoInterface {
    TokenExpireException(-1,"token已过期,请您重新登陆"),
    NotLoginException(300,"当前状态还未登陆，请先登陆"),
    TokenDecodeException(-1,"token信息异常，请你重新登陆"),
    UserNotExist(301,"当前用户信息不存在，请求失败！");

    /** 错误码 */
    private int resultCode;

    /** 错误描述 */
    private String resultMsg;

    @Override
    public int getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMsg() {
        return this.resultMsg;
    }

    BaseErrorEnum(int resultCode,String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "BaseErrorEnum{" +
                "resultCode=" + resultCode +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
