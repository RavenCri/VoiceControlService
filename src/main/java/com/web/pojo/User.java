package com.web.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    @JsonIgnore
    private String username;
    @Column(nullable = false )
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String nickname;

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
