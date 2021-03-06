package com.web.service;

import com.web.dao.UserAccountRepository;
import com.web.dao.UserMqttAccountRepository;
import com.web.pojo.User;
import com.web.result.Result;
import com.web.result.ResultCode;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 用户账户服务
 * @author: raven
 * @create: 2020-04-10 14:35
 **/
@Service
public class UserAccountService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserMqttAccountRepository userMqttAccountRepository;

    @Autowired
    HashedCredentialsMatcher hashedCredentialsMatcher;

    public User findUserById(String userId) {
        return userAccountRepository.findById(userId);
    }

    public Result registerUser(String username,
                              String password,
                             String password2,
                             String nickname) {
        if(!password.equals(password2)){
            return Result.failure(ResultCode.registerUserPasswordNotEqual);
        }

        User user = new User();
        user.setUsername(username);
        String passSlat = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), password,user.getUsername(), hashedCredentialsMatcher.getHashIterations()).toHex();
        user.setPassword(passSlat);
        user.setNickname(nickname);
        user.setAccountLevel(0);
        User existUser = findUserByUserName(username);
        // 拿用户名 判断用户是否存在
        if(existUser != null){
            return Result.failure(ResultCode.registerUserNameExist);
        }
        User us = userAccountRepository.saveAndFlush(user);
        return us==null?Result.failure(ResultCode.registerError):null;
    }

    public User findUserByUserName(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public Result updateUserInfo(String userId,String currentPassword, String password) {
        User user = userAccountRepository.findById(userId);
        // 判断数据库的密码是否与当前输入的一致
        if(!currentPassword.equals(user.getPassword())){
            return Result.failure(ResultCode.updateUserInfoCurrPassError);
        }
        String passSlat = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), password,user.getUsername(), hashedCredentialsMatcher.getHashIterations()).toHex();
        user.setPassword(passSlat);
        userAccountRepository.saveAndFlush(user);
        return Result.success(ResultCode.updateUserInfoSuccess);
    }
    public User findUserByUsernameAndPassword(String username, String password){
            return userAccountRepository.findByUsernameAndPassword(username,password);
    }

    public User findUserByUserId(String userId) {
        return userAccountRepository.findById(userId);
    }

    public List<User> getUserList() {
        List<User> all = userAccountRepository.findByAccountLevelNot(1);
        return all;
    }
}
