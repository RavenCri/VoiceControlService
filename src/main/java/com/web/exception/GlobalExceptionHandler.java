package com.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.web.group.ToolValidated;
import com.web.result.Result;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 全局异常类
 * @author: raven
 * @create: 2020-04-10 17:09
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
   // @ExceptionHandler(RuntimeException.class)

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

    @ExceptionHandler({OwnException.class})

    public String jwtVerification( OwnException  e){
        e.printStackTrace();
        JSONObject obj = new JSONObject();
        obj.put("code",e.getErrorCode());
        obj.put("msg",e.getMessage());
        return obj.toJSONString();
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public Result handle401(ShiroException e) {
        System.out.println("无权访问");
        Result result = new Result();
        result.setMsg( "权限不足！(Unauthorized):" + e.getMessage());
        result.setCode(HttpStatus.UNAUTHORIZED.value());
        return result;
    }

    @ExceptionHandler(value = BindException.class)

    public Result BindException(BindException bindingResult) {

        // 验证参数信息是否有效
        Result messageBean = ToolValidated.myValidate(bindingResult);
        return messageBean;
    }
}
