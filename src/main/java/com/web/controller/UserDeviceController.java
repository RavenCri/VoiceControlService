package com.web.controller;

import com.auth0.jwt.JWT;
import com.web.jwt.annotation.UserLoginToken;
import com.web.pojo.Device;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 17:05
 **/
@RestController
@RequestMapping("device")
@Api(value = "UserDeviceController|用户设备操作接口")
@UserLoginToken
public class UserDeviceController {
    @Autowired
    DeviceService deviceService;

    @ApiOperation("获取用户账户下的设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
    })
    @GetMapping("list")
    public Result getDevices(@RequestHeader("token") String token){
        String userId = JWT.decode(token).getAudience().get(0);
        List<Device> device = deviceService.findDevice(userId);
        return Result.success(ResultCode.getDeviceList,device);
    }

    @ApiOperation("增加用户设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "deviceId", value = "设备唯一id", required = true, dataType = "String"),
    })
    @GetMapping("add")
    public Result addDevice(@RequestHeader("token") String token, String deviceId){
        String userId = JWT.decode(token).getAudience().get(0);
        Device device = deviceService.addDevice(userId, deviceId);
        if( device != null  ){
            return Result.success(ResultCode.addDeviceSuccess);
        }
        return Result.success(ResultCode.addDeviceFail);
    }

    @ApiOperation("删除用户设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "deviceId", value = "设备唯一id", required = true, dataType = "String"),
    })
    @GetMapping("delete")
    public Result deleteDevice(@RequestHeader("token") String token, String deviceId){
        String userId = JWT.decode(token).getAudience().get(0);
        deviceService.deleteDevice(userId, deviceId);
        return Result.success(ResultCode.deleteDeviceSuccess);
    }

    @ApiOperation("更新用户设备id")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "oldDeviceId", value = "更新前的设备id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "newDeviceId", value = "更新后的设备id", required = true, dataType = "String"),
    })
    @GetMapping("update")
    /**
    * @Description: 用来更新用户设备id
    * @Param: [token, oldDeviceId, newDeviceId]
     *              第一个参数为登录后的token
     *              第二个参数为旧设备id
     *              第三个参数为新设备id
    * @return: com.web.result.Result
    * @Author: raven
    * @Date: 2020/4/10
    */
    public Result updateUserDevice(@RequestHeader("token") String token,
                                       String oldDeviceId,String newDeviceId){
        String userId = JWT.decode(token).getAudience().get(0);
        boolean flag = deviceService.updateUserDevice(userId, oldDeviceId, newDeviceId);
        if(!flag){
            return Result.success(ResultCode.updateDeviceFail);
        }
        return Result.success(ResultCode.updateDeviceSuccess);
    }
}
