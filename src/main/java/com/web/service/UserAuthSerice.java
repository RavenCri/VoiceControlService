package com.web.service;

import com.web.dao.UserAuthResposity;
import com.web.pojo.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-16 15:14
 **/
@Service
public class UserAuthSerice {
    @Autowired
    UserAuthResposity userAuthResposity;
    public UserAuth getUserAuth(String username){
        UserAuth user = userAuthResposity.findByUsername(username);

        return user;
    }

    public void closeUser(String username, String reason) {
        UserAuth userAuth = new UserAuth();
        userAuth.setReason(reason);
        userAuth.setUsername(username);
        userAuth.setUsable(false);
        userAuthResposity.saveAndFlush(userAuth);
    }

    public void openUser(String username) {
        UserAuth userAuth = userAuthResposity.findByUsername(username);

        userAuthResposity.delete(userAuth);
    }
}
