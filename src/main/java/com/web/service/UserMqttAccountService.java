package com.web.service;

import com.web.dao.UserMqttAccountRepository;
import com.web.pojo.UserMqttAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-13 18:48
 **/
@Service
public class UserMqttAccountService {

    @Autowired
    public UserMqttAccountRepository userMqttAccountRepository;
    public UserMqttAccount getMqttInfoByUsername(String username){
        return userMqttAccountRepository.findByUsername(username);
    }
    public void userAddMqTTInfo(String username,String mqttUsername,String mqttPassword){
        UserMqttAccount userMqttAccount = new UserMqttAccount();
        userMqttAccount.setUsername(username);
        userMqttAccount.setMqttUsername(mqttUsername);
        userMqttAccount.setMqttPassword(mqttPassword);
        userMqttAccountRepository.saveAndFlush(userMqttAccount);
    }
}
