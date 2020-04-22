package com.correspond.mqtt.rabbitmq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 *
 * @author Anumbrella
 */
@Controller
public class ReceiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveController.class);

    /**
     * 可以转发通过  前端 通过 Stomp对象==>
     *           client.send("/message/welcome", {}, JSON.stringify({'name': "123456"}));
     * 发送消息到后台，这里可接收到消息
     * @param message
     * @return
     */
    @MessageMapping("/test")
    @SendTo("/topic/notice")
    public String all(String message) {

        LOGGER.info("*** 来自客户端的消息 ***:" + message);
        return "有人发消息啦";
    }

    @MessageMapping("/web") // client.send("/message/web", {"token":"123"}, JSON.stringify({ 'name': 'anumbrella' }));
    @SendTo("/topic/notice")
    public String webSystem(String message, @Header String token) {

        System.out.println("token:"+token+"用户来消息啦");
        return "有人发消息啦";
    }
    @MessageMapping("/web.{name}") //client.send("/message/web.raven", {}, JSON.stringify({ 'name': 'anumbrella' }));
    @SendTo("/topic/notice")
    public String say(String message, @DestinationVariable("name") String name) throws Exception {
        System.out.println("name:"+name+"用户来消息啦");
        return name+"发消息啦"+message;
    }
    @SubscribeMapping("/notice")
    public String sub(Principal p) {

        LOGGER.info("有用户订阅了notice，token为："+p.getName());
        return  "感谢你订阅了我。。。";
    }

}
