package com.web.service;

import com.web.dao.DeviceRepository;
import com.web.pojo.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-12 22:46
 **/
@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    public void addDevice(){

    }
    public void deleteDevice(){

    }
    public Device getDevice(String deviceId){
        return deviceRepository.findDeviceByDeviceId(deviceId);
    }
    public void updateDevice(){

    }
}
