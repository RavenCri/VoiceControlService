package com.web.controller.manager;

import com.web.controller.UserDeviceController;
import com.web.jwt.util.JwtUtils;
import com.web.pojo.Device;
import com.web.pojo.User;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserAccountService;
import com.web.service.UserDeviceService;
import com.web.service.manager.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用来管理设备的接口
 * @author: raven
 * @create: 2020-04-15 21:09
 **/
@RequestMapping("devicePlus")
@RestController
@Api(value = "ManagerDeviceController|用来管理设备的接口")
public class ManagerDeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAccountService userAccountService;


    @ApiOperation("获取所有出厂设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "管理员登录后的token", required = true, dataType = "String"),
    })
    @GetMapping("list")
    public Result getAllDevices(@RequestHeader("token") String token){
        String userId = JwtUtils.decode(token);
        List<Device> device = deviceService.findAllDevice();

        device.forEach(e->{
            // 用设备id找到 所有绑定此设备的用户
            List<UserDevice> userDevices = userDeviceService.findUserDeviceByDeviceId(e.getDeviceId());
            List<User> users = new ArrayList<>();
            userDevices.forEach(userDevice -> {
                //拿用户id获取到用户信息
                User userByUserId = userAccountService.findUserByUserId(userDevice.getUserId());
                users.add(userByUserId);
            });
            // 封装绑定该设备的用户
            e.setUsers(users);
            //封装设备在线状态
            if(UserDeviceController.OnlineDevice.containsKey(e.getDeviceId())){
                e.setStatus(true);
            }else e.setStatus(false);

        });
        return Result.success(ResultCode.getDeviceList,device);
    }

    @ApiOperation("增加出厂设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "管理员登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "deviceId", value = "添加的设备id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "time", value = "设备的出厂时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "添加的设备类型", required = true, dataType = "String"),
    })
    @PostMapping("addDevice")
    public Result addDevice(@RequestHeader("token") String token,
                            @NotEmpty @RequestParam String deviceId,
                            @NotEmpty @RequestParam String type,
                            @NotEmpty @RequestParam String time){
        String userId = JwtUtils.decode(token);
        return deviceService.addDevice(deviceId,time,type);

    }

    @ApiOperation("删除出厂设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "管理员登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="header", name = "deviceId", value = "设备编号", required = true, dataType = "String"),
    })
    @PostMapping("deleteDevice")
    public Result deleteDevice(@RequestHeader("token") String token,
                               @NotEmpty @RequestParam String deviceId
                            ){
        String userId = JwtUtils.decode(token);
        return deviceService.removeDevice(deviceId);
    }
}
