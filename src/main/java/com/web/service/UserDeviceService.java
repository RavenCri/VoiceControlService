package com.web.service;

import com.web.dao.DeviceRepository;
import com.web.dao.UserDeviceRepository;
import com.web.pojo.Device;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import com.web.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-10 17:35
 **/
@Service
public class UserDeviceService {
    @Autowired
    UserDeviceRepository userDeviceRepository;
    @Autowired
    DeviceRepository deviceRepository;

    /**
    * @Description: 为用户名下添加设备
    * @Param: [userId, deviceId]
    * @return: void
    * @Author: raven
    * @Date: 2020/4/10
    */
    public Result userAddDevice(String userId, String deviceId) {

        Device findDevice = deviceRepository.findDeviceByDeviceId(deviceId);
        if(findDevice == null){
            return Result.failure(ResultCode.addDeviceNoExist);
        }
        UserDevice device = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(userId,findDevice.getDeviceId());
        // 如果为空，说明该设备没有被该用户二次注册。设备可以二次注册，但是不能被一个人二次注册
        if(device == null){

            UserDevice userDevice = new UserDevice();
            userDevice.setDeviceId(findDevice.getDeviceId());
            userDevice.setUserId(userId);
            userDeviceRepository.saveAndFlush(userDevice);
            return Result.success(ResultCode.addDeviceSuccess);
        }
        return Result.failure(ResultCode.addDeviceExist);
    }
    /**
    * @Description: 通过用户ID找到所有在线设备
    * @Param: [userId]
    * @return: java.util.List<com.web.pojo.Device>
    * @Author: raven
    * @Date: 2020/4/10
    */
    public List<Device> findDevice(String userId){
        List<Device> devices = new ArrayList<>();
        // 先查出用户所有的设备id
        List<UserDevice> userDevices = userDeviceRepository.findUserDeviceByUserId(userId);
        // 用每个id查出所有设备信息
        userDevices.forEach(devive->{
            Device device = deviceRepository.findDeviceByDeviceId(devive.getDeviceId());
            devices.add(device);
        });
        return devices;
    }
    @Transactional
    public boolean deleteDevice(String userId, String deviceId) {

        userDeviceRepository.deleteByUserIdAndDeviceId(userId,deviceId);
        return true;
    }

    public Result updateUserDevice(String userId, String oldDeviceId, String newDeviceId) {
        UserDevice dv = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(userId, oldDeviceId);
        // 如果找不到当前要修改的
        if(dv == null){
            return Result.failure(ResultCode.updateDeviceFail);
        }
        Device dev = deviceRepository.findDeviceByDeviceId(newDeviceId);
        if(dev == null){
            return Result.failure(ResultCode.addDeviceNoExist);
        }

        // 设置为更新后的id
        dv.setDeviceId(newDeviceId);
        userDeviceRepository.saveAndFlush(dv);
        return Result.success(ResultCode.updateDeviceSuccess);
    }
}
