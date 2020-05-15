package com.web.controller;

import com.web.pojo.User;
import com.web.pojo.UserMqttAccount;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserAccountService;
import com.web.service.UserMqttAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-13 18:40
 **/
@RequestMapping("mqtt")
@Api(value = "DeviceController|用于设备访问的接口")
@RestController
@Validated
public class DeviceController {
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserMqttAccountService userMqttAccountService;

    @ApiOperation("设备提供用户的账号和密码，获取mqtt账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),
    })
    @PostMapping("getMqttInfo")
    public Result getMQTTInfo(@NotBlank String username, @NotBlank String password){
       // 如果有引号应该去掉
        username = username.replaceAll("\"","");
        password = password.replaceAll("\"","");
        User user = userAccountService.findUserByUsernameAndPassword(username, password);
        System.out.println("有设备要获取mqtt信息："+user);
        if( user == null){
            return Result.failure(ResultCode.loginFail);
        }

        UserMqttAccount mqttInfo = userMqttAccountService.getMqttInfoByUsername(username);
        Result result = new Result();
        result.setData(mqttInfo);
        result.setMsg("success");
        result.setCode(200);

        return result;
    }
}
