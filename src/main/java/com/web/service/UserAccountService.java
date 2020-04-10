package com.web.service;

import com.web.dao.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.pojo.User;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 14:35
 **/
@Service
public class UserAccountService {
    @Autowired
    UserAccountRepository userAccountRepository;
    public User userLogin(String username,String password){
        return userAccountRepository.findByUsernameAndPassword(username,password);
    }

    public User getUserById(String userId) {
        return userAccountRepository.findById(userId);
    }

    public User registerUser(User user) {
        if(userAccountRepository.findByUsername(user.getUsername()) != null ){
            return null;
        }
        User us = userAccountRepository.saveAndFlush(user);
        return us;
    }
}
