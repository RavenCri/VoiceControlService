package com.web.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.util.MqttUtil;
import com.web.group.ToolValidated;
import com.web.jwt.util.TokenUtil;
import com.web.pojo.Device;
import com.web.pojo.User;
import com.web.pojo.UserAuth;
import com.web.redis.util.RedisUtil;
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
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@Api(value = "UserAccountController|用户账号操作接口")
@RequestMapping("account")

public class UserAccountController {
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserMqttAccountService userMqttAccountService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAuthSerice userAuthSerice;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    HashedCredentialsMatcher hashedCredentialsMatcher;

    @PostMapping("login")
    @ApiOperation("用户提供账号密码，调用该接口即可")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),

    })

    public Result userLogin(@Validated(ToolValidated.login.class)User userLogin,
                            HttpServletResponse response) {
//        Subject subject = SecurityUtils.getSubject();
//        try{
//            UsernamePasswordToken token = new UsernamePasswordToken(userLogin.getUsername(), userLogin.getPassword());
//
//            subject.login(token);
//        }catch (AuthenticationException e){
//            e.printStackTrace();
//            return Result.failure(ResultCode.loginFail);
//        }
//        User user = (User) subject.getPrincipal();
//        String token = TokenUtil.getToken(user.getId());
//        redisUtil.set("token_"+user.getId(),token,3600);
//        response.addHeader("token",token);
//        response.addHeader("Access-Control-Expose-Headers","token");
//        return Result.success(ResultCode.loginSuccess, JSONObject.toJSONString(user));

        User user = userAccountService.findUserByUserName(userLogin.getUsername());
        if(user == null){
            return Result.failure(ResultCode.loginFail);
        }
        UserAuth userAuth = userAuthSerice.getUserAuth(userLogin.getUsername());
        if(userAuth!=null &&!userAuth.isUsable()){
            Result result = new Result();
            result.setCode(400);
            result.setMsg("账号被封禁了无法登陆,封禁原因："+userAuth.getReason());
            return result;
        }

        SimpleHash simpleHash = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), userLogin.getPassword(),user.getUsername(), hashedCredentialsMatcher.getHashIterations());
        System.out.println("==>"+simpleHash);
        if(!simpleHash.toHex().equals(user.getPassword())){
             return Result.failure(ResultCode.loginFail);
        }
        String token = TokenUtil.getToken(user.getId());
        redisUtil.set("token_"+user.getId(),token,3600);

        response.addHeader("Authorization",token);
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        return Result.success(ResultCode.loginSuccess, JSONObject.toJSONString(user));

    }
    @PostMapping("register")
    @ApiOperation("注册新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password2", value = "用户确认密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "nickname", value = "用户昵称", required = true, dataType = "String"),
    })
    public Result registerUser(@NotBlank @RequestParam String username,
                               @NotBlank @RequestParam String password,
                               @NotBlank @RequestParam String password2,
                               @NotBlank @RequestParam String nickname) {


        Result result = userAccountService.registerUser(username,password,password2,nickname);
        if(result != null){
            return result;
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
            @ApiImplicitParam(paramType="header", name = "authorization", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "currentPassword", value = "当前密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password1", value = "修改密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password2", value = "确认修改密码", required = true, dataType = "String"),
    })
    public Result updateInfo(@NotBlank @RequestHeader("authorization") String authorization, @NotBlank @RequestParam String currentPassword,
                             @NotBlank @RequestParam String password2, @NotBlank @RequestParam String password1){
        String userId = TokenUtil.getClaim(authorization,"userId");;
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
            @ApiImplicitParam(paramType="header", name = "authorization", value = "登录后的token", required = true, dataType = "String"),
    })
    public Result getUserInfo(@NotBlank @RequestHeader("authorization") String authorization){
        String userId = TokenUtil.getClaim(authorization,"userId");
        User us = userAccountService.findUserById(userId);
        Result result = new Result();
        result.setData(us);
        result.setCode(200);
        result.setMsg("用户信息");
        return result;
    }

    @GetMapping("userList")
    @ApiOperation("获取所有用户")
    @RequiresPermissions(value = {"manager:user"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "authorization", value = "登录后的token", required = true, dataType = "String"),
    })
    public Result getUserList(@NotBlank @RequestHeader("authorization") String authorization){
        String userId = TokenUtil.getClaim(authorization,"userId");
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
            @ApiImplicitParam(paramType="header", name = "authorization", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "username", value = "要封禁的用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "reason", value = "封禁原因", required = true, dataType = "String"),
    })
    @RequiresPermissions(value = {"manager:user"})
    public Result closeUser(@NotBlank @RequestHeader("authorization") String authorization,
                            @NotBlank @RequestParam String username,
                            @NotBlank @RequestParam String reason){
        String userId = TokenUtil.getClaim(authorization,"userId");

        userAuthSerice.closeUser(username,reason);
        Result result = new Result();
        result.setCode(200);
        result.setMsg("封禁成功！");
        return result;
    }

    @PostMapping("open")
    @ApiOperation("解封用户接口")
    @RequiresPermissions(value = {"manager:user"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "authorization", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "username", value = "解封用户名", required = true, dataType = "String"),

    })
    public Result openUser(@RequestHeader("authorization") String authorization,
                           @RequestParam String username
                           ){
        String userId =  TokenUtil.getClaim(authorization,"userId");

        userAuthSerice.openUser(username);
        Result result = new Result();
        result.setCode(200);
        result.setMsg("解封成功！");
        return result;
    }


}
