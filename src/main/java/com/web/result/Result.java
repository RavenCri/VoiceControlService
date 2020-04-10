package com.web.result;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 17:16
 **/
import java.io.Serializable;
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;
    private String msg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }


    //成功 并且加上返回数据
    public static Result success(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    //成功 自定义成功返回状态 加上数据
    public static Result success(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    // 单返回失败的状态码
    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    // 返回失败的状态码及数据
    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }
}