package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.web.pojo.UserPermission;
import com.web.service.UserAccountService;
import com.web.service.UserPermissioService;
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
public class UserPermissionController {

    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserPermissioService userPermissioService;
    @GetMapping("user")
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
