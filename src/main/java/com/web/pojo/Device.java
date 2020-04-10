package com.web.pojo;

import javax.persistence.*;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 17:36
 **/

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    public int id;
    @Column(nullable = false)
    public String userId;
    @Column(nullable = false)
    public String deviceId;

    public Device() {
    }
    public Device(String userId,String deviceId){
        this.userId = userId;
        this.deviceId = deviceId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
