package com.web.dao;

import com.web.pojo.UserMqttAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserMqttAccountRepository extends JpaRepository<UserMqttAccount,Long>, CrudRepository<UserMqttAccount,Long>, JpaSpecificationExecutor<UserMqttAccount> {
     /**
     * @Description: 设备通过用户名找到mqtt账户
     * @Param: [username]
     * @return: com.web.pojo.UserMqttAccount
     * @Author: raven
     * @Date: 2020/4/16
     */

     UserMqttAccount findByUsername(String username);

}
