package com.web.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "账号不能为空哦")
    private String username;
    @Column(nullable = false )
    @JsonIgnore
    @NotEmpty(message = "密码不能为空哦")
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
    @Transient
    private String salt;
    @Transient
    private String encryptPassword;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
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
