package com.web.dao;

import com.web.pojo.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-16 15:16
 **/
@Repository
public interface UserAuthResposity extends JpaRepository<UserAuth,Long>, CrudRepository<UserAuth,Long> {
    /** 
    * @Description: 通过用户名查找是否被封禁 
    * @Param: [username] 
    * @return: com.web.pojo.UserAuth 
    * @Author: raven
    * @Date: 2020/4/16 
    */
    UserAuth findByUsername(String username);


}
