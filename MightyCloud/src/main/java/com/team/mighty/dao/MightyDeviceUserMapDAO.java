package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceUserMapping;

public interface MightyDeviceUserMapDAO extends JpaRepository<MightyDeviceUserMapping, Serializable>{

	@Query("SELECT m FROM MightyDeviceUserMapping m WHERE m.mightyDeviceId = :deviceId and m.registrationStatus='Y' ")
	MightyDeviceUserMapping getDeviceInfo(@Param("deviceId") long deviceId);
	
	@Query("SELECT m FROM MightyDeviceUserMapping m join m.mightyUserInfo userInfo WHERE userInfo.id = :userId")
	MightyDeviceUserMapping checkAnyDeActivatedAccount1(@Param("userId") long userId);
	
	@Query("SELECT m FROM MightyDeviceUserMapping m join m.mightyUserInfo userInfo WHERE userInfo.id = :userId")
	List<MightyDeviceUserMapping> checkAnyDeActivatedAccount(@Param("userId") long userId);
	
	@Query("SELECT m FROM MightyDeviceUserMapping m join m.mightyUserInfo userInfo WHERE m.phoneDeviceId = :phoneDeviceId and userInfo.id = :userId")
	Set<MightyDeviceUserMapping> checkUserAndPhoneDeviceId(@Param("userId") long userId, @Param("phoneDeviceId") String phoneDeviceId);
	
	@Query("SELECT m FROM MightyDeviceUserMapping m join m.mightyUserInfo userInfo WHERE m.mightyDeviceId = :mightyDeviceId and m.phoneDeviceId = :phoneDeviceId and userInfo.userName = :userName")
	MightyDeviceUserMapping checkAnyDeActivatedAccountByUserName(@Param("userName") String userName, @Param("mightyDeviceId") long mightyDeviceId
			, @Param("phoneDeviceId") String phoneDeviceId);

	@Query("FROM MightyDeviceUserMapping m WHERE m.mightyDeviceId = :deviceId ")
	List<MightyDeviceUserMapping> getMightyDeviceUserMappingOndevId( @Param("deviceId") long deviceId);
	
	
	@Query("SELECT m FROM MightyDeviceUserMapping m WHERE m.mightyDeviceId = :deviceId and m.mightyUserInfo.id=:userId")
	List<MightyDeviceUserMapping> getDeviceInfoList(@Param("deviceId") long deviceId,@Param("userId") long userId);
	
	
}
