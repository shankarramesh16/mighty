package com.team.mighty.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyUserInfo;

public interface MightyUserInfoDao extends JpaRepository<MightyUserInfo, Long> {
	
	@Query("SELECT m FROM MightyUserInfo m WHERE m.password=:password and m.userIndicator=:userIndicator")
	MightyUserInfo getMightyUserLogin(@Param("password") String password,@Param("userIndicator") String userIndicator);

}
