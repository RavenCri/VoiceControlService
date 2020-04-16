package com.web.controller;

import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.web.jwt.util.TokenUtil;
import com.web.pojo.Device;
import com.web.pojo.User;
import com.web.pojo.UserAuth;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserAccountService;
import com.web.service.UserAuthSerice;
import com.web.service.UserDeviceService;
import com.web.service.UserMqttAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import util.MqttUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(value = "UserAccountController|用户账号操作接口")
@RequestMapping("account")
@Validated
public class UserAccountController {
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserMqttAccountService userMqttAccountService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAuthSerice userAuthSerice;
    public static Map<String,String> tokens = new HashMap<>();
    @PostMapping("login")
    @ApiOperation("用户提供账号密码，调用该接口即可")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),

    })

    public Result userLogin( @RequestParam String username,
                             @RequestParam String password,
                            HttpServletResponse response) {
        User user = userAccountService.findUserByUsernameAndPassword(username,password);
        if(user == null){
            return Result.failure(ResultCode.loginFail);

        }
        UserAuth userAuth = userAuthSerice.getUserAuth(user.getUsername());
        if(userAuth!=null &&!userAuth.isUsable()){
            Result result = new Result();
            result.setCode(400);
            result.setMsg("账号被封禁了无法登陆,封禁原因："+userAuth.getReason());
            return result;
        }
        String token = TokenUtil.encode(user);

        tokens.put(user.getId(),token);
        response.addHeader("token",token);
        response.addHeader("Access-Control-Expose-Headers","token");
        return Result.success(ResultCode.loginSuccess);
    }
    @PostMapping("register")
    @ApiOperation("注册新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password2", value = "用户确认密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "nickname", value = "用户昵称", required = true, dataType = "String"),
    })
    public Result registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String password2,
                               @RequestParam String nickname) {


        Result result = userAccountService.registerUser(username,password,password2,nickname);
        if(result != null){
            return Result.failure(ResultCode.registerError);
        }
        String mqttUserName = RandomUtil.randomString(8);
        String mqttPassword = RandomUtil.randomString(16);

        while(!MqttUtil.checkUsername(mqttUserName)){
            mqttUserName = RandomUtil.randomString(8);
        }
        MqttUtil.CreateUser(mqttUserName,mqttPassword);
        MqttUtil.setPermission(mqttUserName);
        //保存创建的mqtt账号密码到数据表
        userMqttAccountService.userAddMqTTInfo(username,mqttUserName,mqttPassword);
        return Result.success(ResultCode.registerSuccess);
    }
    @PostMapping("update")
    @ApiOperation("用来更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "currentPassword", value = "当前密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password1", value = "修改密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password2", value = "确认修改密码", required = true, dataType = "String"),
    })
    public Result updateInfo(@RequestHeader String token, @RequestParam String currentPassword,
                             @RequestParam String password2, @RequestParam String password1){
        String userId = TokenUtil.decode(token);
        // 如果要改的密码与当前密码一样
        if(currentPassword.equals(password1)){
            return Result.failure(ResultCode.updateUserInfoFail);
        }
        // 如果修改的密码不一致
        if(!password2.equals(password1)){
            return Result.failure(ResultCode.updateUserInfoFail);
        }
        return userAccountService.updateUserInfo(userId,currentPassword,password1);

    }

    @GetMapping("userInfo")
    @ApiOperation("用来获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
    })
    public Result getUserInfo(@RequestHeader String token){
        String userId = JWT.decode(token).getAudience().get(0);
        User us = userAccountService.getUserById(userId);
        Result result = new Result();
        result.setData(us);
        result.setCode(200);
        result.setMsg("用户信息");
        return result;
    }

    @GetMapping("userList")
    @ApiOperation("获取所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
    })
    public Result getUserList(@RequestHeader String token){
        String userId = JWT.decode(token).getAudience().get(0);
        List<User> users = userAccountService.getUserList();
        // 设置用户设备组
        users.forEach(user -> {
            List<Device> devices = userDeviceService.findDeviceByUserId(user.getId());
            UserAuth userAuth = userAuthSerice.getUserAuth(user.getUsername());
            if (userAuth == null){
                userAuth = new UserAuth();
                userAuth.setUsable(true);
            }
            user.setUserAuth(userAuth);
            user.setDevices(devices);
        });

        Result result = new Result();
        result.setData(users);
        result.setCode(200);
        result.setMsg("用户信息");
        return result;
    }

    @PostMapping("close")
    @ApiOperation("封禁用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "username", value = "要封禁的用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "reason", value = "封禁原因", required = true, dataType = "String"),
    })
    public Result closeUser(@RequestHeader String token,
                              @RequestParam String username,
                              @RequestParam String reason){
        String userId = JWT.decode(token).getAudience().get(0);

        userAuthSerice.closeUser(username,reason);
        Result result = new Result();
        result.setCode(200);
        result.setMsg("封禁成功！");
        return result;
    }
}
