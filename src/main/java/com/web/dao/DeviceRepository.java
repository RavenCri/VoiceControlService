package com.web.dao;

import com.web.pojo.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>, CrudRepository<Device,Long>, JpaSpecificationExecutor<Device> {
    List<Device> findByUserId(String userId);

    Device findByUserIdAndDeviceId(String userId,String device);
}
