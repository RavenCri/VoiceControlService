package com.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.web.pojo.User;
@Repository
public interface UserAccountRepository extends JpaRepository<User,Long>, CrudRepository<User,Long>, JpaSpecificationExecutor<User> {
    User findByUsernameAndPassword(String userName,String password);
    User findById(String userId);
    User findByUsername(String username);

}