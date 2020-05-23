package com.web.group;

import com.web.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-23 09:43
 **/
public class ToolValidated {
    private static final Logger LOG = LoggerFactory.getLogger(ToolValidated.class);
    public interface login{}
    public static Result myValidate(BindingResult bindingResult) {
        Result messageBean = new Result();

        if(bindingResult.hasErrors()) {
            // 设置验证结果状态码
            messageBean.setCode(400);
            // 获取错误字段信息集合
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            // 使用TreeSet是为了让输出的内容有序输出(默认验证的顺序是随机的)
            Set<String> errorInfoSet = new TreeSet<String>();
            for (FieldError fieldError : fieldErrorList) {
                // 遍历错误字段信息
                errorInfoSet.add(fieldError.getDefaultMessage());
                LOG.debug("[{}.{}]{}", fieldError.getObjectName() , fieldError.getField(), fieldError.getDefaultMessage());
            }

            StringBuffer sbf = new StringBuffer();
            for (String errorInfo : errorInfoSet) {
                sbf.append(errorInfo);
                sbf.append(",");
            }
            messageBean.setMsg(sbf.substring(0, sbf.length() - 1));
        }

        return messageBean;
    }
}
