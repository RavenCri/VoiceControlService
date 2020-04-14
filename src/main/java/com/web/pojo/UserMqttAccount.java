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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMqtt_username() {
        return mqtt_username;
    }

    public void setMqtt_username(String mqtt_username) {
        this.mqtt_username = mqtt_username;
    }

    public String getMqtt_password() {
        return mqtt_password;
    }

    public void setMqtt_password(String mqtt_password) {
        this.mqtt_password = mqtt_password;
    }
}
