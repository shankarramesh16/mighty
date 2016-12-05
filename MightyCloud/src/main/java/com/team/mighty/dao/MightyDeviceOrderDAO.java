package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceOrderInfo;

public interface MightyDeviceOrderDAO extends JpaRepository<MightyDeviceOrderInfo,Serializable>{
	@Query("FROM MightyDeviceOrderInfo")
	List<MightyDeviceOrderInfo> getOrderDevices();
	
	@Query("Select m FROM MightyDeviceOrderInfo m where m.deviceSerialNo=:deviceId")
	MightyDeviceOrderInfo getDeviceOrderById(@Param("deviceId") String deviceId);

}
