package com.web.shiro;

import com.alibaba.fastjson.JSON;
import com.web.jwt.util.TokenUtil;
import com.web.redis.util.RedisUtil;
import com.web.result.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-23 13:28
 **/

public class JwtFilter extends BasicHttpAuthenticationFilter {
    Logger log = LoggerFactory.getLogger(JwtFilter.class);



    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)   {
        //判断请求的请求头是否带上 "token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            }catch (AuthenticationException e){

                // 执行到这里说明token过期
                if(referToken((HttpServletRequest) request)){
                    //referToken未过期，则刷新token
                    log.info("refer未过期即将刷新token");
                    String token = ((HttpServletRequest) request).getHeader("Authorization");
                    String userId = TokenUtil.getClaim(token,"userId");
                    String newToken = TokenUtil.getToken(userId);
                    // 更新token到redis
                    RedisUtil.set("token_"+userId,newToken,Long.valueOf(TokenUtil.expires)*60);
                    ((HttpServletResponse)response).addHeader("Authorization",newToken);
                    ((HttpServletResponse)response).addHeader("Access-Control-Expose-Headers","Authorization");
                    return true;
                }
                log.info("refer过期");
                //token 过期
                LoginError(response,e.getMessage());
                return false;
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    private boolean referToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String createTime = TokenUtil.getClaim(token, "createTime");

        // 如果redis还包含refresh,则进行刷新token
        if(RedisUtil.hasKey("refresh_"+TokenUtil.getClaim(token, "userId"))){
            String accessTime = (String) RedisUtil.get("refresh_"+TokenUtil.getClaim(token, "userId"));
            // 如果和redis的值相同（也就是说只允许更新一次token）因为更新token后createTime会改变！，而accessTime是第一次token
            // 携带的
            if( createTime == accessTime){
                //未过期
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }
    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)  {
        this.sendChallenge(request, response);
        return false;
    }
    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)    {
    //    HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        //String token = httpServletRequest.getHeader("Authorization");
        JwtToken jwtToken = new JwtToken(getAuthzHeader(request));

        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);



        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    private void LoginError(ServletResponse response,String msg) {


        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");

        try  {
            PrintWriter out = httpServletResponse.getWriter();
            Result result = new Result();
            result.setCode(-1);
            result.setMsg(msg);
            out.write(JSON.toJSONString(result));
            out.flush();
        } catch (IOException e) {
            log.error("直接返回Response信息出现IOException异常:{}", e.getMessage());
            throw new RuntimeException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 必须加这个
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }


}
