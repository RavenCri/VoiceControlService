package com.web.pojo;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

/**
 * @description: 用户权限表
 * @author: raven
 * @create: 2020-05-15 14:08
 **/
@Table(name="user_permission")
@Entity
public class UserPermission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Ignore
    public int id;
    @Column(nullable = false,columnDefinition = "varchar(10) comment '用户名'")

    public String username;
    @Column(nullable = false,columnDefinition = "varchar(20) comment '用户权限'")
    public String userPermission;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }
}
