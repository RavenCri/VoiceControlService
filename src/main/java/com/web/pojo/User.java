package com.web.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.group.ToolValidated;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")

public class User {
    /*strategy=GenerationType.IDENTITY*/
    @GeneratedValue(generator = "jpa-uuid" )
    @Column(length = 32)
    @Id
    @JsonIgnore
    private String id;
    @Column(nullable = false, unique = true)
    //@JsonIgnore
    @NotBlank(message = "账号不能为空",groups = {ToolValidated.login.class})
    private String username;
    @Column(nullable = false )
    @JsonIgnore
    @NotBlank(message = "密码不能为空",groups = {ToolValidated.login.class})
    private String password;
    @Column(nullable = false)
    private String nickname;
    /**用户等级 0普通用户**/
    @Column(nullable = false,name = "account_level")
    private int accountLevel;
    @Transient
    private List<Device> devices;
    @Transient
    private UserAuth userAuth;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }


    public UserAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(UserAuth userAuth) {
        this.userAuth = userAuth;
    }

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(int accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }


}
