package com.web.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.web.jwt.annotation.PassToken;
import com.web.jwt.annotation.UserLoginToken;
import com.web.jwt.util.TokenUtil;
import com.web.pojo.User;
import com.web.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 14:26
 **/

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserAccountService userAccountService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //Request method 'GET' not supported 则不进入拦截器

        if(method.isAnnotationPresent(PassToken.class) ){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if( passToken.required() ){
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.getDeclaringClass().isAnnotationPresent(UserLoginToken.class)
                || method.isAnnotationPresent(UserLoginToken.class)
        ) {
            UserLoginToken userLoginToken =
                    (method.getDeclaringClass().getAnnotation(UserLoginToken.class)==null ?
                            method.getAnnotation(UserLoginToken.class):
                            method.getDeclaringClass().getAnnotation(UserLoginToken.class));
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("您还未登录，请先登录。");
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = TokenUtil.decode(token);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }

               /* if(!token.equals(UserAccountController.tokens.get(userId))){
                    throw new RuntimeException("token信息已刷新，该token已失效，请重新登录！");
                }*/
                User user = userAccountService.findUserById(userId);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUsername())).build();

                try {
                    DecodedJWT verify = jwtVerifier.verify(token);
                    /*Long time = verify.getClaim("data").asLong();
                    if(time - System.currentTimeMillis() >= 1000*60*30){
                        throw new RuntimeException("token已失效，请重新登录！");
                    }*/
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
