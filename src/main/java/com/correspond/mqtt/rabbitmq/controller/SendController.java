
package com.correspond.mqtt.rabbitmq.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@Api(value = "SendController|用于长连接推送消息")
public class SendController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    DeviceService deviceService;
    @Autowired
    UserDeviceService userDeviceService;
    @Autowired
    UserAccountService userAccountService;

    @Autowired
    public RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(SendController.class);

    /**
     * 通知消息
     */
    @ApiOperation("对所有用户推送的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "msg", value = "要推送的消息", required = true, dataType = "String"),

    })
    @GetMapping("/notice")
    public void notice(String msg) {
        messagingTemplate.convertAndSend("/topic/notice", msg);
    }

    /**
     * 给特定用户推送消息
     * @param username
     * @param type
     */
    @ApiOperation("对特定用户推送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "要推送给的用户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "推送的类型，是web还是android?", required = true, dataType = "String"),
    })
    @GetMapping("/user/{username}/{type}")
    public void notice(@PathVariable String username, @PathVariable String type,String msg) {
        messagingTemplate.convertAndSendToUser(username+"@"+type
                , "/topic/reply",msg);
    }
    /**
     * 设备可以发送给具体用户消息
     */
    @ApiOperation("设备对用户回馈消息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "deviceId", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "deviceKey", value = "设备key，是web还是android?", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "username", value = "要推送给的用户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "platForm", value = "推送的类型，是web还是android?", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "msg", value = "推送的消息", required = true, dataType = "String"),

    })
    @PostMapping("/user")
    public Result user(
                     @RequestParam String deviceId,
                     @RequestParam String deviceKey,
                     @RequestParam String username,
                     @RequestParam String platForm,
                     @RequestParam String msg) {
        System.out.println(msg);
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

                System.out.println("已推送至："+user.getUsername()+"@"+platForm);
                return Result.success(ResultCode.Success);
            }
        }

        logger.warn(String.format("设备{%s}身份验证失败！",deviceId));
        return  Result.failure(ResultCode.Fail);
    }
}
