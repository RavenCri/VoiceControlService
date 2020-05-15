package com.web.exception;

public interface BaseErrorInfoInterface {
    /** 错误码*/
    int getResultCode();

    /** 错误描述*/
    String getResultMsg();
}
