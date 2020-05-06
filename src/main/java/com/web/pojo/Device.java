package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @JsonIgnore
    public int id;

    @Column(nullable = false,name = "device_id")
    /*设备唯一id*/
    public String deviceId;
    @JsonIgnore
    @Column(nullable = false,name = "device_key")
    /*设备密码*/
    public String deviceKey;
    @Column(nullable = false,name = "create_time")

    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    /*设备出厂时间*/
    public Date createTime;
    @Column(nullable = false)
    /*设备类型*/
    public String type;
    @Transient
    // 设备状态
    public boolean status;
    @Transient
    //绑定用户名
    public List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Device() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date create_time) {
        this.createTime = create_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
