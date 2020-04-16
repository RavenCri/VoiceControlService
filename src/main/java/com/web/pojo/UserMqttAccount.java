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
    @Column(nullable = false, unique = true,name = "mqtt_username")
    public String mqttUsername;
    @Column(nullable = false, unique = true,name = "mqt_password")
    public String mqttPassword;

    @Override
    public String toString() {
        return "UserMqttAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mqtt_username='" + mqttUsername + '\'' +
                ", mqtt_password='" + mqttPassword + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMqttUsername() {
        return mqttUsername;
    }

    public void setMqttUsername(String mqtt_username) {
        this.mqttUsername = mqtt_username;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqtt_password) {
        this.mqttPassword = mqtt_password;
    }
}
