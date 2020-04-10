package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.web.jwt.util.TokenUtil;
import com.web.pojo.User;
import com.web.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "UserAccountController|用户账号操作接口")
@RequestMapping("api")
public class UserAccountController {
    @Autowired
    UserAccountService userAccountService;
    public static Map<String,String> tokens = new HashMap<>();
    @PostMapping("login")
    @ApiOperation("用户提供账号密码，调用该接口即可")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),

    })
    public String userLogin(@RequestParam String username, @RequestParam String password) {
        User user = userAccountService.userLogin(username,password);
        JSONObject res = new JSONObject();
        if(user != null){
            res.put("status","success");
            String token = TokenUtil.getToken(user);
            res.put("token", token);
            tokens.put(user.getId(),token);
        }else{
            res.put("status","error");
        }
        return res.toJSONString();
    }
    @GetMapping("register")
    @ApiOperation("注册新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "用户密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "nickname", value = "用户昵称", required = true, dataType = "String"),
    })
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String nickname) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        User us = userAccountService.registerUser(user);
        JSONObject res = new JSONObject();
        if(us != null){
            res.put("status","success");
        }else{
            res.put("status","error");
        }
        return res.toJSONString();
    }

}
