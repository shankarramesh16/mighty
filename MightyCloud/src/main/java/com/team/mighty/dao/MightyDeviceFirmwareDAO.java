package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceFirmware;

public interface MightyDeviceFirmwareDAO extends JpaRepository<MightyDeviceFirmware, Serializable> {
	
	@Query("select m FROM MightyDeviceFirmware m where m.status = :status order By m.version desc")
	List<MightyDeviceFirmware> getDeviceFirmware(@Param("status") String status) throws Exception;

	@Query("FROM MightyDeviceFirmware mdf where mdf.id=:deviceFirmwareId")
	MightyDeviceFirmware getDeviceFirmwareById(@Param("deviceFirmwareId") String deviceFirmwareId);

}
