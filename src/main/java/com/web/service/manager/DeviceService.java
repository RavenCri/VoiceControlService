package com.web.service.manager;

import com.web.dao.DeviceRepository;
import com.web.pojo.Device;
import com.web.result.Result;
import com.web.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-15 21:12
 **/
@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    public Result addDevice(Device device){
        Device de = deviceRepository.saveAndFlush(device);
        if(de == null){
            return Result.failure(ResultCode.addDeviceFail);
        }else{
            return Result.success(ResultCode.addDeviceSuccess);
        }
    }
    public void deleteDevice(){

    }
    public Device getDevice(String deviceId){
        return deviceRepository.findDeviceByDeviceId(deviceId);
    }
    public void updateDevice(){

    }

    public List<Device> findAllDevice() {
        return deviceRepository.findAll();
    }
}
