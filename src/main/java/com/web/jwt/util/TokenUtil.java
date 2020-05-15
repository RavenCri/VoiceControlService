package com.web.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.web.pojo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 14:24
 **/
@ConfigurationProperties(prefix = "jwt")
@Component
public class TokenUtil {

    private  static String expires = "0";

   @Value("expires")
   public void setExpires(String expires) {
        System.out.println("==========================>");
        this.expires = expires;
    }

    public static String encode(User user) {
        String token="";
        // 也可以添加自定义声明值 这么直接设置过期时间.withClaim("data", System.currentTimeMillis())
        Long date = System.currentTimeMillis();
        token= JWT.create().withAudience(String.valueOf(user.getId()))//接受者
                .withIssuedAt(new Date(date)) // 签发时间 非必要
                .withExpiresAt(new Date(date + 1000*60*Integer.parseInt(expires)))// jwt的过期时间
                .withSubject(user.getUsername())  //主题 非必要
                .withIssuer("raven")//发布者 非必要
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(user.getUsername()));//签名
        return token;
    }

    public static String decode(String token){
        return JWT.decode(token).getAudience().get(0);
    }
}
 /*
        iss: jwt签发者
        sub: jwt所面向的用户
        aud: 接收jwt的一方
        exp: jwt的过期时间，这个过期时间必须要大于签发时间
        nbf: 定义在什么时间之前，该jwt都是不可用的.
        iat: jwt的签发时间
        jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

        .withNotBefore(new Date())  //生效时间,定义在什么时间之前，该jwt都是不可用的
        .withJWTId(UUID.randomUUID().toString())    //编号
        .withIssuer("rstyro")   //发布者
        .withSubject("test")    //主题
        .withAudience(audience)     //观众，相当于接受者
        .withIssuedAt(new Date())   // 生成签名的时间
    */