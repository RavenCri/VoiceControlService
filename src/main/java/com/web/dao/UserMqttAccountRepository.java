package com.web.dao;

import com.web.pojo.UserMqttAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserMqttAccountRepository extends JpaRepository<UserMqttAccount,Long>, CrudRepository<UserMqttAccount,Long>, JpaSpecificationExecutor<UserMqttAccount> {
     UserMqttAccount findUserMqttAccountByUsername(String username);

}
