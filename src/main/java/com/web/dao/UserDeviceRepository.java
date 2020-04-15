package com.web.dao;

import com.web.pojo.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice,Long>, CrudRepository<UserDevice,Long>, JpaSpecificationExecutor<UserDevice> {
    List<UserDevice> findUserDeviceByUserId(String userId);
    void deleteByUserIdAndDeviceId(String userId,String deviceId);
    UserDevice findUserDeviceByUserIdAndDeviceId(String userId,String deviceId);

    List<UserDevice> findUserDeviceByDeviceId(String deviceId);
}
