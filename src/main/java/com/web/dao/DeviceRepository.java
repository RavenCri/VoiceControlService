package com.web.dao;

import com.web.pojo.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>, CrudRepository<Device,Long>, JpaSpecificationExecutor<Device> {

    Device findDeviceByDeviceId(String deviceId);

}
