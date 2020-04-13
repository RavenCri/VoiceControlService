package com.web.dao;

import com.web.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserAccountRepository extends JpaRepository<User,Long>, CrudRepository<User,Long>, JpaSpecificationExecutor<User> {
    User findUserByUsernameAndPassword(String username,String password);
    User findById(String userId);
    User findByUsername(String username);


}
