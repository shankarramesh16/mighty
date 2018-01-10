package com.team.mighty.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyDeviceInfo;
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

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.emailId=:emailId and m.userIndicator='L'")
	MightyUserInfo getUserByEmail(@Param("emailId") String emailId);
	
	@Query(value="SELECT * FROM TBL_MIGHTY_USER_INFO m WHERE BINARY m.password=?1 and m.user_name=?2 and m.user_indicator=?3",nativeQuery = true)
	MightyUserInfo getMightyUserLogin(@Param("password") String password,@Param("userName") String userName, @Param("userIndicator") String userIndicator);
	

	@Query("SELECT m FROM MightyUserInfo m WHERE  m.userName=:userName")
	MightyUserInfo getUserByName(@Param("userName") String userName);
	
	@Query("SELECT m FROM MightyUserInfo m WHERE (m.userFBId=:userFBId or m.emailId=:emailId) and m.userIndicator=:userIndicator" )
	List<MightyUserInfo> getUserByUserFBAndEmailWithIndicator(@Param("userFBId") String userFBId, @Param("emailId") String emailId,@Param("userIndicator") String userIndicator);

	@Query("SELECT m FROM MightyUserInfo m WHERE ((m.userName like '%'||:userName||'%') or (m.emailId like '%'||:userName||'%')) ")
	List<MightyUserInfo> getSearchUsers(@Param("userName") String userName);
	
	@Query("SELECT count(m.id) FROM MightyUserInfo m")
	List<MightyUserInfo> getAllMightyUsers();
	
	@Query("SELECT m FROM MightyUserInfo m WHERE m.id=:userId")
	MightyUserInfo getMightyUserById(@Param("userId") long userId);

	@Query("SELECT m FROM MightyUserInfo m WHERE m.userName=:userName")
	List<MightyUserInfo> getUserByUserName(@Param("userName") String userName);

	@Query("SELECT m FROM MightyUserInfo m WHERE (m.userFBId=:userFBId or m.userName=:userName) and m.userIndicator=:userIndicator" )
	List<MightyUserInfo> getUserByUserFBAndEmailAndUsrWithIndicator(@Param("userFBId") String userFBId,@Param("userName") String userName,@Param("userIndicator") String userIndicator);

	@Query(value="SELECT u.user_id,u.user_name,u.email_id,u.user_indicator,u.created_dt,m.device_id,m.is_registered,m.sw_version,m.appbuild,m.appversion,m.register_at,m.upgrade_at,um.status,um.phone_device_os_version,um.phone_device_type FROM TBL_MIGHTY_USER_INFO u join TBL_MIGHTY_USR_DEV_MAP um on u.user_id=um.user_id join TBL_MIGHTY_DEVICE_INFO m on um.mighty_device_id=m.id",nativeQuery = true)
	List<Object[]> getMightyUserDetails();
	
	
	

}
