package com.team.mighty.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyUserInfo;

public interface MightyDeviceInfoDAO extends JpaRepository<MightyDeviceInfo, Long> {

	@Query("SELECT m FROM MightyDeviceInfo m WHERE m.deviceId=:deviceId ")
	MightyDeviceInfo getDeviceInfo(@Param("deviceId") String deviceId);
	
	@Query("SELECT m FROM MightyDeviceInfo m WHERE m.deviceId = :deviceId and m.isRegistered='Y' ")
	MightyDeviceInfo getDeviceInfoByStatusAndID(@Param("deviceId") String deviceId);
	
	@Query("FROM MightyUserInfo")
	List<MightyUserInfo> getMightyUserInfo() throws Exception;

	@Query("FROM MightyDeviceInfo")
	List<MightyDeviceInfo> getMightyDeviceInfo() throws Exception;

	@Query("SELECT m FROM MightyDeviceInfo m WHERE m.id=:id")
	MightyDeviceInfo getMightyDeviceOnId(@Param("id") long id) ;
	
	@Query("SELECT m FROM MightyDeviceInfo m WHERE ((m.deviceId like '%'||:deviceId||'%') or (m.swVersion like '%'||:deviceId||'%'))")
	List<MightyDeviceInfo> getMightySearchDevice(@Param("deviceId") String deviceId);
	
	@Query("SELECT count(m.id) FROM MightyDeviceInfo m ")
	List<MightyDeviceInfo> getAllMightyDev();

	@Query("SELECT count(m.id) FROM MightyDeviceInfo m where m.swVersion=:swVersion")
	List<MightyDeviceInfo> getLatestOTACount(@Param("swVersion") String swVersion);
	
	@Query(value="select m.sw_version,count(m.sw_version) as upgraded FROM TBL_MIGHTY_DEVICE_INFO m GROUP BY m.sw_version HAVING COUNT(m.sw_version )>1",nativeQuery = true)
	List<Object[]> mightySwVerCount();

	
}
