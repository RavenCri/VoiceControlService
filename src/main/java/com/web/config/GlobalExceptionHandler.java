package com.web.config;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleException(HttpServletRequest request, Exception e){
        //e.printStackTrace();
        JSONObject obj = new JSONObject();
        obj.put("code",500);
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        String msg = stackTraceElement.getClassName()+":"+
                stackTraceElement.getMethodName()+":"+
                stackTraceElement.getLineNumber()+":"+
                e.getMessage();
        obj.put("msg",e.getMessage()==null?"服务器异常，请通知管理员":msg);
        logger.error(msg);
        return obj.toJSONString();
    }

    @ExceptionHandler({JWTVerificationException.class})
    @ResponseBody
    public String jwtVerification( Exception e){
        e.printStackTrace();
        JSONObject obj = new JSONObject();
        obj.put("code",-1);
        obj.put("msg",e.getMessage());
        return obj.toJSONString();
    }
}
