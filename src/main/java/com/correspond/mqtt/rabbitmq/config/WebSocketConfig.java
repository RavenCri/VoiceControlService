package com.correspond.mqtt.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author Anumbrella
 * 通过EnableWebSocketMessageBroker
 * 开启使用STOMP协议来传输基于代理(message broker)的消息,
 * 此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);


    @Autowired
    private MyChannelInterceptor inboundChannelInterceptor;

    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;

    @Autowired
    private MyHandshakeHandler myHandshakeHandler;
    // 配置消息代理，哪种路径的消息会进行代理处理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //并将其目的地前缀设置为“/topic”这样的话，设置"/topic"
        // Spring就能知道所有目的地前缀为“/topic”的消息都会发送到STOMP代理中。
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")      // rabbitmq-host服务器地址
                .setRelayPort(61613)     // rabbitmq-stomp 服务器服务端口
                .setClientLogin("guest")   // 登陆账户
                .setClientPasscode("guest"); // 登陆密码

        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix("/user/");
        //所有目的地以“/message”打头的消息都将会路由到带有@MessageMapping注解的方法中，而不会发布到代理队列或主题中
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");
    }

    /**
     * 添加这个Endpoint，这样在网页中就可以通过websocket连接上服务,也就是我们配置websocket的服务地址,
     *      并且可以指定是否使用socketjs
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .setHandshakeHandler(myHandshakeHandler)
                .addInterceptors(authHandshakeInterceptor) //添加拦截处理，这里authHandshakeInterceptor 封装的认证用户信息
                .withSockJS();

        LOGGER.info("com.init rabbitmq websocket endpoint ");
    }


    /**
     * 设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(inboundChannelInterceptor);
        registration.taskExecutor()    // 线程信息
                .corePoolSize(400)     // 核心线程池
                .maxPoolSize(800)      // 最多线程池数
                .keepAliveSeconds(60); // 超过核心线程数后，空闲线程超时60秒则杀死
    }

    /**
     * 配置发送与接收的消息参数，可以指定消息字节大小，缓存大小，发送超时时间
     *
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(15 * 1000)    // 超时时间
                .setSendBufferSizeLimit(512 * 1024) // 缓存空间
                .setMessageSizeLimit(128 * 1024);   // 消息大小
    }


}
