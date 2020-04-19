package com.web.service.manager;

import cn.hutool.core.util.RandomUtil;
import com.web.dao.DeviceRepository;
import com.web.pojo.Device;
import com.web.pojo.UserDevice;
import com.web.result.Result;
import com.web.result.ResultCode;
import com.web.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    UserDeviceService userDeviceService;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void updateDevice(){

    }
    public Result addDevice(String  deviceId,String time,String type){
        Device device = new Device();
        device.setDeviceId(deviceId);
        System.out.println(time);
        time = time.replace("Z", "");
        time = time.replace("T", " ");
        System.out.println(time);
        try {
            Date tm = sf.parse(time);
            System.out.println(tm.getTime());
            device.setCreateTime(tm);
            device.setDeviceId(deviceId);
            device.setType(type);
            device.setDeviceKey(RandomUtil.randomString(16));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Device de = deviceRepository.saveAndFlush(device);
        if(de == null){
            return Result.failure(ResultCode.addDeviceFail);
        }else{
            return Result.success(ResultCode.addDeviceSuccess);
        }
    }
    /**
    * @Description: 删除设备并删除用户所绑定该设备的数据
    * @Param: [deviceId]
    * @return: com.web.result.Result
    * @Author: raven
    * @Date: 2020/4/19
    */
    public Result removeDevice(String deviceId){
        Device device = findDeviceByDeviceId(deviceId);
        deleteDevice(device);
        List<UserDevice> userDevices = userDeviceService.findUserDeviceByDeviceId(deviceId);
        userDevices.forEach(userDevice -> {
            userDeviceService.deleteDevice(userDevice);
        });
        Result result = new Result();
        result.setMsg("success");
        result.setCode(200);
        return result;
    }
    public List<Device> findAllDevice() {
        return deviceRepository.findAll();
    }

    public void deleteDevice(Device device){
        deviceRepository.delete(device);
    }
    public Device findDeviceByDeviceId(String deviceId){
        return deviceRepository.findByDeviceId(deviceId);
    }
}
