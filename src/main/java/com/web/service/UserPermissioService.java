package com.web.service;

import com.web.dao.UserPermissionRepository;
import com.web.pojo.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-05-15 14:13
 **/
@Service
public class UserPermissioService {
    @Autowired
    UserPermissionRepository userPermissionRepository;
    public List<UserPermission> findPermissionByUserName(String username){
        return userPermissionRepository.findByUsername(username);
    }
}
