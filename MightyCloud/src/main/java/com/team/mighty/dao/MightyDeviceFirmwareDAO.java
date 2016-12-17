package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceFirmware;

public interface MightyDeviceFirmwareDAO extends JpaRepository<MightyDeviceFirmware, Serializable> {
	
	@Query("select m.id,m.version,m.fileName,m.hashValue,m.hashType,m.status,m.effectiveDt,m.createdDt,m.updatedDt,m.requires,m.compatibleIOS,m.compatibleAND,m.compatibleHW FROM MightyDeviceFirmware m where m.status = :status order By m.version desc")
	List<Object[]> getDeviceFirmware(@Param("status") String status) throws Exception;

	@Query("FROM MightyDeviceFirmware mdf where mdf.id=:deviceFirmwareId")
	MightyDeviceFirmware getDeviceFirmwareById(@Param("deviceFirmwareId") String deviceFirmwareId);

	@Query("FROM MightyDeviceFirmware m where m.status=:status order By m.version desc")
	List<MightyDeviceFirmware> getDeviceFirmwareByStatus(@Param("status") String status) throws Exception;

	@Query("FROM MightyDeviceFirmware m where m.compatibleIOS=:compatibleIOS and m.status=:status")
	List<MightyDeviceFirmware> getFirmwareByAppComp(@Param("compatibleIOS") String compatibleIOS, @Param("status") String status);

	@Query("FROM MightyDeviceFirmware m where m.requires=:requires and m.compatibleIOS=:compatibleIOS and m.status = :status order By m.version desc")
	List<MightyDeviceFirmware> getFirmwareByCompatible(@Param("compatibleIOS") Float compatibleIOS, @Param("requires") Float requires, @Param("status") String status);

	@Query("FROM MightyDeviceFirmware m where m.compatibleIOS=:compatibleIOS and m.status = :status order By m.version asc")
	List<MightyDeviceFirmware> getFirmwareByReqVersion(@Param("compatibleIOS") Float compatibleIOS, @Param("status") String status);

	@Query("FROM MightyDeviceFirmware m where m.status=:status order By m.version asc")	
	List<MightyDeviceFirmware> getDeviceFirmwareByStatusAsc(@Param("status") String string);

	/*@Query("FROM MightyDeviceFirmware m where m.compatibleHw like %:compatibleHw%  and m.status = :status order By m.version desc")
	MightyDeviceFirmware getFirmwareByLatestVersion(@Param("compatibleHw") String compatibleHw, @Param("status") String status);
*/
}
