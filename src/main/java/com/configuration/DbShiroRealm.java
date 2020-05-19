package com.configuration;


import com.web.pojo.User;
import com.web.pojo.UserPermission;
import com.web.service.UserAccountService;
import com.web.service.UserPermissioService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DbShiroRealm extends AuthorizingRealm {
	private UserPermissioService userPermissioService = new UserPermissioService();
	private final Logger log = LoggerFactory.getLogger(DbShiroRealm.class);
	
	private static final String encryptSalt = "F12839WhsnnEV$#23b";
	private UserAccountService userAccountService;
	
	public DbShiroRealm(UserAccountService userService) {
		this.userAccountService = userService;
		this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
	}
	
	@Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
		String username = userpasswordToken.getUsername();
		User user = userAccountService.findUserByUserName(username);
		if(user == null)
			throw new AuthenticationException("用户名或者密码错误");
		
		return new SimpleAuthenticationInfo(user, user.getEncryptPassword(), ByteSource.Util.bytes(encryptSalt), "dbRealm");
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {      
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
		String username = user.getUsername();

		List<UserPermission> userPermissions = userPermissioService.findPermissionByUserName(username);
		List<String> roles = new ArrayList<>();
		if(userPermissions != null){
			userPermissions.forEach(e->{
				roles.add(e.getUserPermission());
			});
		}
        if (roles.size() > 0)
            simpleAuthorizationInfo.addRoles(roles);

        return simpleAuthorizationInfo;
	}

	
}
