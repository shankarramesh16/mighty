package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.Mightylog;

public interface MightylogDao extends JpaRepository<Mightylog, Serializable> {
	
	@Query("select m.deviceId FROM Mightylog m")
	Set<String> getMightyLogs();
	
	@Query("SELECT l FROM Mightylog l WHERE l.deviceId = :deviceId")
	List<Mightylog> getMightyLogsOndevId(@Param("deviceId") String deviceId);
	
	@Query("SELECT l FROM Mightylog l WHERE l.deviceId = :deviceId and l.username=:username")
	List<Mightylog> getExistingMightylog(@Param("deviceId") String deviceId, @Param("username") String username);

}
