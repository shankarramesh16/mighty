package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyUpload;

public interface MightyUploadDao extends JpaRepository<MightyUpload, Serializable> {

	@Query("Select mu.deviceId FROM MightyUpload mu")
	Set<String> getMightyToCloudLogs();

	@Query("Select mu FROM MightyUpload mu where mu.deviceId=:deviceId")
	List<MightyUpload> getMightyToCloudLogsOndevId(@Param("deviceId") String deviceId);

	@Query("Select mu FROM MightyUpload mu where mu.deviceId=:deviceId")
	MightyUpload getMightyUploadByDevId(@Param("deviceId")String deviceId);
	

}
