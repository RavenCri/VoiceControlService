
package com.correspond.mqtt.rabbitmq.controller;

import com.web.pojo.Device;
import com.web.pojo.User;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserAccountService;
import com.web.service.UserDeviceService;
import com.web.service.manager.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可以利用普通http request来主动推送广播消息
 * @author raven
 */
@RestController
@RequestMapping("/push_msg")
public class SendController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    DeviceService deviceService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAccountService userAccountService;
    Logger logger = LoggerFactory.getLogger(SendController.class);

    /**
     * 通知消息
     */
    @GetMapping("/notice")
    public void notice() {
        messagingTemplate.convertAndSend("/topic/notice", "这是通知消息！！");


    }

    /**
     * 给特定用户推送消息
     * @param username
     * @param type
     */
    @GetMapping("/user/{username}/{type}")
    public void notice(@PathVariable String username, @PathVariable String type) {
        messagingTemplate.convertAndSendToUser(username+"@"+type
                , "/topic/reply","这是通知消息！！");
    }
    /**
     * 设备可以发送给具体用户消息
     */
    @PostMapping("/user")
    public Result user(
                     @RequestParam String deviceId,
                     @RequestParam String deviceKey,
                     @RequestParam String username,
                     @RequestParam String platForm,
                     @RequestParam String msg) {

        Device device = deviceService.findDeviceByDeviceId(deviceId);
        //先判断设备信息是否正确
        if (device != null && device.getDeviceKey().equals(deviceKey)) {
            AtomicBoolean flag = new AtomicBoolean(false);
            List<UserDevice> userDevices = userDeviceService.findUserDeviceByDeviceId(deviceId);
            User user = userAccountService.findUserByUserName(username);
            //判断该用户是否拥有该设备
            userDevices.forEach(e->{
                if(e.getUserId().equals(user.getId())){
                    flag.set(true);
                }
            });
            if(flag.get()){
                messagingTemplate.convertAndSendToUser(user.getUsername()+"@"+platForm, "/topic/reply",
                        msg);


                return Result.success(ResultCode.Success);
            }
        }

        logger.warn(String.format("设备{%s}身份验证失败！",deviceId));
        return  Result.failure(ResultCode.Fail);
    }
}
