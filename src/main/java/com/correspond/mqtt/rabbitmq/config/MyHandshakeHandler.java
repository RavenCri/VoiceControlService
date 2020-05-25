
package com.correspond.mqtt.rabbitmq.config;

import com.web.jwt.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @author Anumbrella
 */
@Component
public class MyHandshakeHandler extends DefaultHandshakeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletServerHttpRequest.getServletRequest();
            /**
             * 这边就获取你最熟悉的陌生人,携带参数，你可以cookie，请求头，或者url携带，这边我采用url携带
             */
            final String token = httpRequest.getParameter("Authorization");
            if (StringUtils.isEmpty(token) ) {
                LOGGER.error("未登录系统，禁止登录websocket!");
                return null;
            }

            return () -> token;
        }
        return null;

    }

}
