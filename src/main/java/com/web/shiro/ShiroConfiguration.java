package com.web.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-23 13:15
 **/
@Configuration
public class ShiroConfiguration {
    private static String encryption = "MD5";
    private static int encryptionNum = 1024;
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm= new MyShiroRealm();
        // shiro 整合 jwt 不能 使用加密算法类来验证密文
        //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(MyShiroRealm myShiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
            // 设置自定义Cache缓存
        //securityManager.setCacheManager(new CustomCacheManager());
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    //如果没有此name,将会找不到shiroFilter的Bean
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //shiroFilterFactoryBean.setLoginUrl("/login");         //表示指定登录页面 (前后分离不适用)
        //shiroFilterFactoryBean.setSuccessUrl("/user/list");   // 登录成功后要跳转的链接 (前后分离不适用)

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();//拦截器, 配置不会被拦截的链接 顺序判断
        //filterChainDefinitionMap.put("/login","anon");    //所有匿名用户均可访问到Controller层的该方法下
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        //filterChainDefinitionMap.put("/**", "anon");

        filterChainDefinitionMap.put("/account/login", "anon");

        //filterChainDefinitionMap.put("/**", "user");   //user表示配置记住我或认证通过可以访问的地址

        // 添加自己的过滤器并且取名为jwt
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", jwtFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        // 过滤链定义，从上向下顺序执行，一般将放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    /**
     * SpringShiroFilter首先注册到spring容器
     * 然后被包装成FilterRegistrationBean
     * 最后通过FilterRegistrationBean注册到servlet容器
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    // 盐值加密不适用于Token验证！！！！！！！
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(encryption);
        hashedCredentialsMatcher.setHashIterations(encryptionNum);// 设置加密次数
        return hashedCredentialsMatcher;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {//@Qualifier("hashedCredentialsMatcher")
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    // 会导致多次调用doGetAuthorizationInfo 方法
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
}
