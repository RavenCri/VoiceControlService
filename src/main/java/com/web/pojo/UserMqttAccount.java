package com.web.pojo;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-13 18:45
 **/
@Table(name="user_mqtt_account")
@Entity
public class UserMqttAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Ignore
    public int id;
    @Column(nullable = false, unique = true)
    public String username;
    @Column(nullable = false, unique = true)
    public String mqtt_username;
    @Column(nullable = false, unique = true)
    public String mqtt_password;

    @Override
    public String toString() {
        return "UserMqttAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mqtt_username='" + mqtt_username + '\'' +
                ", mqtt_password='" + mqtt_password + '\'' +
                '}';
    }
}
