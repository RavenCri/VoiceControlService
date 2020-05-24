package com.web.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.web.redis.util.RedisUtil;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private  static String expires = "0";
    private  static String withIssuer = null;

    private static RedisUtil redisUtil;
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

   @Value("expires")
   public void setExpires(String expires) {

        this.expires = expires;
    }
    @Value("withIssuer")
    public void setWithIssuer(String withIssuer) {

        this.withIssuer = withIssuer;
    }
    /**
    * @Description: 生成Token
    * @Param: [userId, username]
    * @return: java.lang.String
    * @Author: raven
    * @Date: 2020/5/23
    */
    public static String getToken(String userId  ) {
        String secret ;
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        secret = secureRandomNumberGenerator.nextBytes(16).toHex();
        redisUtil.set("salt_"+userId,secret);
        String token="";
        // 也可以添加自定义声明值 这么直接设置过期时间.withClaim("data", System.currentTimeMillis())
        Long date = System.currentTimeMillis();
        token= JWT.create()

                .withClaim("userId",String.valueOf(userId))
                .withIssuedAt(new Date(date)) // 签发时间 非必要
                .withExpiresAt(new Date(date + 1000*60*Integer.parseInt(expires)))// jwt的过期时间
              //  .withSubject(user.getUsername())  //主题 非必要
                .withIssuer(withIssuer)//发布者 非必要
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(secret));//签名
        return token;
    }
    /**
    * @Description: 获取封装在token的信息
    * @Param: [token]
    * @return: java.lang.String
    * @Author: raven
    * @Date: 2020/5/23
    */
    public static String getClaim(String token,String claim){

        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        }  catch (JWTDecodeException e) {
            e.printStackTrace();
            logger.error("解密Token中的公共信息出现JWTDecodeException异常:{}", e.getMessage());
            throw new RuntimeException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }

    }
    /**
    * @Description: token校验
    * @Param: [token, username, secret]
    * @return: boolean
    * @Author: raven
    * @Date: 2020/5/23
    */
    public static boolean verify(String token, String userId) {
       try {
            String secret = (String)redisUtil.get("salt_"+userId);
            secret = secret.replaceAll("\"","");

            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(withIssuer).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            logger.info(jwt.getToken()+":-token is valid");
            return true;
        } catch (Exception e) {

            logger.info("The token is invalid{}",e.getMessage());
            return false;
        }
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