package com.web.service;

import com.web.dao.UserAccountRepository;
import com.web.dao.UserMqttAccountRepository;
import com.web.pojo.User;
import com.web.result.Result;
import com.web.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 14:35
 **/
@Service
public class UserAccountService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserMqttAccountRepository userMqttAccountRepository;

    public User getUserById(String userId) {
        return userAccountRepository.findById(userId);
    }

    public User registerUser(User user) {

        User us = userAccountRepository.saveAndFlush(user);
        return us;
    }

    public User getUserByUserName(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public Result updateUserInfo(String userId,String currentPassword, String password) {
        User user = userAccountRepository.findById(userId);
        // 判断数据库的密码是否与当前输入的一致
        if(!currentPassword.equals(user.getPassword())){
            return Result.failure(ResultCode.updateUserInfoCurrPassError);
        }
        user.setPassword(password);
        userAccountRepository.saveAndFlush(user);
        return Result.success(ResultCode.updateUserInfoSuccess);
    }
    public User findUserByUsernameAndPassword(String username, String password){
            return userAccountRepository.findUserByUsernameAndPassword(username,password);
    }

    public User findUserByUserId(String userId) {
        return userAccountRepository.findUserById(userId);
    }
}
