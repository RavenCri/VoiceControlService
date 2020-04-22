package com.web.dao;

import com.web.pojo.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice,Long>, CrudRepository<UserDevice,Long>, JpaSpecificationExecutor<UserDevice> {
    /**
    * @Description: 通过用户id查找到 用户-设备 对象列表
    * @Param: [userId]
    * @return: java.com.util.List<com.web.pojo.UserDevice>
    * @Author: raven
    * @Date: 2020/4/16
    */
    List<UserDevice> findByUserId(String userId);
    /**
    * @Description: 删除设备
    * @Param: [userId, deviceId]
    * @return: void
    * @Author: raven
    * @Date: 2020/4/16
    */
    void deleteByUserIdAndDeviceId(String userId,String deviceId);
    /**
    * @Description: 找到用户是否拥有此设备
    * @Param: [userId, deviceId]
    * @return: com.web.pojo.UserDevice
    * @Author: raven
    * @Date: 2020/4/16
    */
    UserDevice findByUserIdAndDeviceId(String userId, String deviceId);
    /**
    * @Description: 通过设备id找到所有绑定此设备的 用户-设备 对象
    * @Param: [deviceId]
    * @return: java.com.util.List<com.web.pojo.UserDevice>
    * @Author: raven
    * @Date: 2020/4/16
    */
    List<UserDevice> findByDeviceId(String deviceId);
}
