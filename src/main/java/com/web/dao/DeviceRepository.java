package com.web.dao;

import com.web.pojo.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
* @Description: 用于dao层设备管理
* @Param:
* @return:
* @Author: raven
* @Date: 2020/4/16
*/
@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>, CrudRepository<Device,Long>, JpaSpecificationExecutor<Device> {
    /**
    * @Description: 通过设备id找到设备
    * @Param: [deviceId]
    * @return: com.web.pojo.Device
    * @Author: raven
    * @Date: 2020/4/16
    */
    Device findByDeviceId(String deviceId);
    /**
    * @Description:  通过设备id和设备密码找到设备
    * @Param: [deviceId, deviceKey]
    * @return: com.web.pojo.Device
    * @Author: raven
    * @Date: 2020/4/16
    */
    Device findByDeviceIdAndDeviceKey(String deviceId, String deviceKey);

}
