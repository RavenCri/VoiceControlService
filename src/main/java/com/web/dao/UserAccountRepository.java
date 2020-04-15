package com.web.dao;

import com.web.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserAccountRepository extends JpaRepository<User,Long>, CrudRepository<User,Long>, JpaSpecificationExecutor<User> {

    User findUserByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

    User findById(String userId);
    User findByUsername(String username);


    User findUserById(String userId);
}
