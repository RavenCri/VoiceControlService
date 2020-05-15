package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.web.pojo.UserPermission;
import com.web.service.UserAccountService;
import com.web.service.UserPermissioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-15 14:12
 **/
@RestController
@RequestMapping("permission")
@Api(value = "UserPermissionController|用户获取权限的接口")
public class UserPermissionController {

    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserPermissioService userPermissioService;
    @GetMapping("user")
    @ApiOperation("用于获取用户所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
    })
    /**
    * @Description: 根据token获取用户权限
    * @Param: [token]
    * @return: com.alibaba.fastjson.JSONObject
    * @Author: raven
    * @Date: 2020/5/15
    */
    public JSONObject pagePermissions(@RequestHeader(value = "token",required = false) String token){
        JSONObject jsonObject = new JSONObject();
        List<String> list = new ArrayList<>();

        String username = null;
        if(token != null){
            String userId = JWT.decode(token).getAudience().get(0);
            username = userAccountService.findUserById(userId).getUsername();
        }

        if(username!=null ){
            List<UserPermission> permissions = userPermissioService.findPermissionByUserName(username);
            permissions.forEach(permission->{
                list.add(permission.getUserPermission());
            });

        }
        jsonObject.put("data",list);
        return jsonObject;
    }
}
