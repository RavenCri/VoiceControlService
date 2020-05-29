package com.correspond.mqtt.rabbitmq.config;

import com.web.jwt.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author raven
 */
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {

        LOGGER.info("===============before handshake=============");
        // 在beforeHandshake中可以获取socket连接URL中的参数

        // HttpSession session = SpringContextUtils.getSession();
        // session.getAttribute("session_key") 判断具体的session存在
        // 比如，只有登录后，才可以进行websocket连接
        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
        String token = serverRequest.getServletRequest().getParameter("Authorization");
        if (token != null &&!TokenUtil.verify(token) ) {

                LOGGER.error("Token验证失败，连接失败！");
                return false;
        }

        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        LOGGER.info("===============after handshake=============");
    }
}
