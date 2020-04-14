package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

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

    @Column(nullable = false)
    public String deviceId;
    @JsonIgnore
    @Column(nullable = false)
    public String deviceKey;
    @Column(nullable = false)
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date create_time;
    @Column(nullable = false)
    public String type;
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
