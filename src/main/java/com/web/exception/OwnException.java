package com.web.exception;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-15 23:47
 **/
public class OwnException extends RuntimeException {
    /**
     * 错误码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public OwnException(BaseErrorInfoInterface errorInfoInterface){
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public OwnException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public OwnException(int errorCode, String errorMsg) {
        super(errorCode+"");
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public OwnException(int errorCode, String errorMsg, Throwable cause) {
        super(errorCode+"", cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
