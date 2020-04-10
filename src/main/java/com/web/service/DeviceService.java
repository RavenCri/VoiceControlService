package com.web.service;

import com.web.dao.DeviceRepository;
import com.web.pojo.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 17:35
 **/
@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    /**
    * @Description: 为用户名下添加设备
    * @Param: [userId, deviceId]
    * @return: void
    * @Author: raven
    * @Date: 2020/4/10
    */
    public Device addDevice(String userId, String deviceId) {
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setUserId(userId);
        return deviceRepository.saveAndFlush(device);
    }
    /**
    * @Description: 通过用户ID找到所有在线设备
    * @Param: [userId]
    * @return: java.util.List<com.web.pojo.Device>
    * @Author: raven
    * @Date: 2020/4/10
    */
    public List<Device> findDevice(String userId){
        return deviceRepository.findByUserId(userId);
    }

    public void deleteDevice(String userId, String deviceId) {
        deviceRepository.delete(new Device(userId,deviceId));
    }

    public boolean updateUserDevice(String userId, String oldDeviceId, String newDeviceId) {
        Device dv = deviceRepository.findByUserIdAndDeviceId(userId, newDeviceId);
        if(dv != null){
            return false;
        }
        Device dev = deviceRepository.findByUserIdAndDeviceId(userId, oldDeviceId);
        dev.setDeviceId(newDeviceId);
        deviceRepository.saveAndFlush(dev);
        return true;
    }
}
