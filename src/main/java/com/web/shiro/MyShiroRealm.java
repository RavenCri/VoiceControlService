package com.web.shiro;

import com.web.jwt.util.TokenUtil;
import com.web.pojo.User;
import com.web.pojo.UserPermission;
import com.web.redis.util.RedisUtil;
import com.web.service.UserAccountService;
import com.web.service.UserAuthSerice;
import com.web.service.UserPermissioService;
import io.netty.util.internal.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-23 13:15
 **/
public class MyShiroRealm extends AuthorizingRealm {
    Logger log = LoggerFactory.getLogger(MyShiroRealm.class);


    @Autowired
    private UserAccountService userService;
    @Autowired
    private UserPermissioService permissionService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserAuthSerice userAuthSerice;


    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }
    /**
     * 访问控制。比如某个用户是否具有某个操作的使用权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String token  = (String) principalCollection.getPrimaryPrincipal();
        String userId = TokenUtil.getClaim(token, "userId");
        User user = userService.findUserById(userId);

        if (user == null) {
            log.error("授权失败，用户信息为空！！！");
            return null;
        }
        try {
            //获取用户角色集
            List<String> listRole= new ArrayList<>();
            listRole.add(user.getAccountLevel()+"");
            simpleAuthorizationInfo.addRoles(listRole);
            System.out.println("拥有角色=>"+listRole);
            List<UserPermission> permissions = permissionService.findPermissionByUserName(user.getUsername());
            List<String> permission  = permissions.stream().map(UserPermission::getUserPermission).collect(Collectors.toList());
            System.out.println("拥有权限=>"+permission);
            simpleAuthorizationInfo.addStringPermissions(permission);
            return simpleAuthorizationInfo;
        } catch (Exception e) {
            log.error("授权失败，请检查系统内部错误!!!", e);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 用户身份识别(登录")
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)   {
        System.out.println("————身份认证方法————");

        String token = (String) authenticationToken.getCredentials();
        String userId = TokenUtil.getClaim(token,"userId");
        if (StringUtil.isNullOrEmpty(userId)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        if(RedisUtil.hasKey("token_"+user.getId()) && TokenUtil.verify(token)){
            System.out.println("验证成功！");

            return new SimpleAuthenticationInfo(token, token,getName());
        }

        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");

    }
}
