package com.web.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 全局异常类
 * @author: raven
 * @create: 2020-04-10 17:09
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        JSONObject obj = new JSONObject();
        obj.put("code",-1);
        obj.put("msg",e.getMessage()==null?"服务器异常，请通知管理员":e.getMessage());
        return obj.toJSONString();
    }
}
