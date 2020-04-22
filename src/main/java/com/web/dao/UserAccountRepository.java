package com.web.dao;

import com.web.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<User,Long>, CrudRepository<User,Long>, JpaSpecificationExecutor<User> {
    /**
    * @Description:  通过用户名和密码找到用户
    * @Param: [username, password]
    * @return: com.web.pojo.User
    * @Author: raven
    * @Date: 2020/4/16
    */
    User findByUsernameAndPassword(@Param("username")String username, @Param("password")String password);
    /**
    * @Description: 通过用户id找到用户
    * @Param: [userId]
    * @return: com.web.pojo.User
    * @Author: raven
    * @Date: 2020/4/16
    */
    User findById(String userId);
    /**
    * @Description: 通过用户名找到用户
    * @Param: [username]
    * @return: com.web.pojo.User
    * @Author: raven
    * @Date: 2020/4/16
    */
    User findByUsername(String username);
    /**
    * @Description: 获取某一权限下的所有用户
    * @Param: [account_leavl]
    * @return: java.com.util.List<com.web.pojo.User>
    * @Author: raven
    * @Date: 2020/4/16
    */
    List<User> findByAccountLevelNot(int account_leavl);

}
