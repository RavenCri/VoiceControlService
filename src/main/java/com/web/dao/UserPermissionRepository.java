package com.web.dao;

import com.web.pojo.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission,Long>, CrudRepository<UserPermission,Long>, JpaSpecificationExecutor<UserPermission> {
    List<UserPermission> findByUsername(String username);
}
