package com.web.service;

import com.web.dao.UserAccountRepository;
import com.web.dao.UserMqttAccountRepository;
import com.web.jwt.util.JwtUtils;
import com.web.pojo.User;
import com.web.result.Result;
import com.web.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        user.setPassword(password);
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
        user.setPassword(password);
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
    /**
     * 保存user登录信息，返回token
     * @param username
     */
    public String generateJwtToken(String username) {
        String salt = "12345";//JwtUtils.generateSalt();
        /**
         * @todo 将salt保存到数据库或者缓存中
         * redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);
         */
        return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }

    public User getJwtTokenInfo(String username) {

        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        User user = findUserByUserName(username);
        user.setSalt("12345");
        return user;
    }

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    public List<String> getUserRoles(Long userId){
        return Arrays.asList("admin");
    }
}
