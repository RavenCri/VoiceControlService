package com.correspond.mqtt.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 *
 * @author Anumbrella
 */
@Controller
public class ReceiveController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveController.class);


    @MessageMapping("toFriend")
    public void toFriendMsg(String message){
        JSONObject msgJSON = JSON.parseObject(message);
        String username  = msgJSON.getString("toUser");
        String msg  = msgJSON.getString("message");
        messagingTemplate.convertAndSendToUser(username+"@web", "/topic/reply",
                msg);
    }
    /**
     * 可以转发通过  前端 通过 Stomp对象==>
     *           client.send("/message/test", {}, JSON.stringify({'name': "123456"}));
     * 发送消息到后台，这里可接收到消息
     * @param message
     * @return
     */
    @MessageMapping("/test")
    @SendTo("/topic/notice")
    public String all(String message) {

        LOGGER.info("*** 来自客户端的消息 ***:" + message);
        return "有人发消息啦："+ message;
    }

    @MessageMapping("/web")

    public String webSystem(String message, @Header String token) {

        System.out.println("token:"+token+"用户来消息啦");
        return "有人发消息："+"有人发消息啦"+ message;

        // client.send("/message/web", {"token":"123"}, JSON.stringify({ 'name': 'anumbrella' }));
    }
    @MessageMapping("/web.{name}")

    //@SendTo("/topic/notice")
    @SendToUser("/topic/reply")
    public String say(String message, @DestinationVariable("name") String name) throws Exception {
        System.out.println("name:"+name+"用户来消息啦");

        return name+"发送："+message;
        //stomp调用形式
        //client.send("/message/web.raven", {}, JSON.stringify({ 'name': 'anumbrella' }));
    }

   // @SubscribeMapping("/notice")
    public String sub(Principal p) {

        LOGGER.info("有用户订阅了notice，token为："+p.getName());
        return  "感谢你订阅了我。。。";
    }

}
