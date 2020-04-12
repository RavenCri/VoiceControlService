package com.web.pojo;

import javax.persistence.*;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-12 22:02
 **/
@Entity
@Table(name="user_device")
public class UserDevice {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    public int id;
    @Column(nullable = false )
    public String userId;
    @Column(nullable = false )
    public String deviceId;
    public UserDevice(){

    }

    public UserDevice(String userId, String deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
