package com.web.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description: 这个就类似UsernamePasswordToken
 * @author: raven
 * @create: 2020-05-23 13:19
 **/
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
}