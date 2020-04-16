package com.web.pojo;

import javax.persistence.*;

/**
 * @description: 用户账户封禁
 * @author: raven
 * @create: 2020-04-16 15:08
 **/
@Entity
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**用户名**/
    @Column(nullable = false, unique = true)
    private String username;
    /**是否可用**/
    @Column(nullable = false, unique = true)
    private boolean usable;
    /**封禁原因**/
    private String reason;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
