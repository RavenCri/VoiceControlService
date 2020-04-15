package com.web.controller;

import cn.hutool.core.util.RandomUtil;
import com.web.jwt.util.TokenUtil;
import com.web.pojo.Device;
import com.web.pojo.User;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserAccountService;
import com.web.service.UserDeviceService;
import com.web.service.manager.DeviceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-15 21:09
 **/
@RequestMapping("devicePlus")
@RestController
public class ManagerDeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAccountService userAccountService;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @ApiOperation("获取所有出厂设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "管理员登录后的token", required = true, dataType = "String"),
    })
    @GetMapping("list")
    public Result getAllDevices(@RequestHeader("token") String token){
        String userId = TokenUtil.decode(token);
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
    })
    @PostMapping("addDevice")
    public Result addDevice(@RequestHeader("token") String token,
                            @RequestParam String id,
                            @RequestParam String type,
                            @RequestParam String time){
        String userId = TokenUtil.decode(token);
        Device device = new Device();
        device.setDeviceId(id);
        System.out.println(time);
        time = time.replace("Z", "");
        time = time.replace("T", " ");
        System.out.println(time);
        try {
            Date tm = sf.parse(time);
            System.out.println(tm.getTime());
            device.setCreateTime(tm);
            device.setDeviceId(id);
            device.setType(type);
            device.setDeviceKey(RandomUtil.randomString(16));
            return deviceService.addDevice(device);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }
}
