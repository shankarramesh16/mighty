package com.team.mighty.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyUserInfo;

public interface MightyUserInfoDao extends JpaRepository<MightyUserInfo, Long> {
	
	/*@Query("SELECT m FROM MightyUserInfo m WHERE UPPER(m.password)=:password and m.userName=:userName and m.userIndicator=:userIndicator")
	MightyUserInfo getMightyUserLogin(@Param("password") String password,@Param("userName") String userName, @Param("userIndicator") String userIndicator);
	*/
		
	@Query("SELECT m FROM MightyUserInfo m WHERE m.userName=:userName or m.emailId=:emailId")
	MightyUserInfo getUserByNameAndEmail(@Param("userName") String userName, @Param("emailId") String emailId);

	@Query("SELECT m FROM MightyUserInfo m WHERE m.id=:userId")
	MightyUserInfo getUserById(@Param("userId") long l);

	@Query("SELECT m FROM MightyUserInfo m WHERE (m.userName=:userName or m.emailId=:emailId) and m.userIndicator=:userIndicator" )
	List<MightyUserInfo> getUserByNameAndEmailWithIndicator(@Param("userName") String userName, @Param("emailId") String emailId,@Param("userIndicator") String userIndicator);

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.emailId=:emailId")
	MightyUserInfo getUserByEmail(@Param("emailId") String emailId);
	
	@Query(value="SELECT * FROM TBL_MIGHTY_USER_INFO m WHERE BINARY m.password=?1 and m.user_name=?2 and m.user_indicator=?3",nativeQuery = true)
	MightyUserInfo getMightyUserLogin(@Param("password") String password,@Param("userName") String userName, @Param("userIndicator") String userIndicator);
	

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.userName=:userName")
	MightyUserInfo getUserByName(@Param("userName") String userName);
	
	@Query("SELECT m FROM MightyUserInfo m WHERE (m.userFBId=:userFBId or m.emailId=:emailId) and m.userIndicator=:userIndicator" )
	List<MightyUserInfo> getUserByUserFBAndEmailWithIndicator(@Param("userFBId") String userFBId, @Param("emailId") String emailId,@Param("userIndicator") String userIndicator);
	
	

}
