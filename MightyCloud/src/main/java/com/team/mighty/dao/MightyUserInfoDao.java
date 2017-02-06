package com.team.mighty.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.AdminUser;
import com.team.mighty.domain.MightyUserInfo;

public interface MightyUserInfoDao extends JpaRepository<MightyUserInfo, Long> {
	
	@Query("SELECT m FROM MightyUserInfo m WHERE m.password=:password and m.userName=:userName")
	MightyUserInfo getMightyUserLogin(@Param("password") String password,@Param("userName") String userName);

	@Query("SELECT m FROM MightyUserInfo m WHERE m.userName=:userName or m.emailId=:emailId")
	MightyUserInfo getUserByNameAndEmail(@Param("userName") String userName, @Param("emailId") String emailId);

	@Query("SELECT m FROM MightyUserInfo m WHERE m.id=:userId")
	MightyUserInfo getUserById(@Param("userId") long l);

	@Query("SELECT m FROM MightyUserInfo m WHERE (m.userName=:userName or m.emailId=:emailId) and m.userIndicator=:userIndicator" )
	MightyUserInfo getUserByNameAndEmailWithIndicator(@Param("userName") String userName, @Param("emailId") String emailId,@Param("userIndicator") String userIndicator);

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.emailId=:emailId")
	MightyUserInfo getUserByEmail(@Param("emailId") String emailId);

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.userName=:userName")
	MightyUserInfo getUserByName(@Param("userName") String userName);

}
