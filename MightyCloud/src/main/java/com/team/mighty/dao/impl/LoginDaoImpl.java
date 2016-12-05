package com.team.mighty.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.team.mighty.dao.AbstractDao;
import com.team.mighty.dao.LoginDao;
import com.team.mighty.domain.AdminUser;
import com.team.mighty.logger.MightyLogger;

@Repository
public class LoginDaoImpl extends AbstractDao implements LoginDao {
	private static final MightyLogger logger = MightyLogger.getLogger(LoginDaoImpl.class);
	
	public AdminUser getLoginUser(String username, String password)	throws Exception {
		
		String query="from AdminUser where loginId=:loginId and password=:password";
		Query q=getEntityManager().createQuery(query);
			q.setParameter("loginId", username);
			q.setParameter("password", password);
				List<AdminUser>	adminUserList=q.getResultList();
				if(adminUserList!=null && !adminUserList.isEmpty()){
					return adminUserList.get(0);
				}else{
					return null;
				}
			
	}

	
	public AdminUser getUserByEmail(String email) throws Exception {
		String query="from AdminUser where emailaddress=:email";
		Query q=getEntityManager().createQuery(query);
			q.setParameter("email", email);
				List<AdminUser>	adminUserList=q.getResultList();
				if(adminUserList!=null && !adminUserList.isEmpty()){
					return adminUserList.get(0);
				}else{
					return null;
				}
	}


	
	public AdminUser setPassword(AdminUser adminUser) throws Exception {
			return (AdminUser)update(adminUser);
		
	}


	public AdminUser getUserById(String id) throws Exception {
		String query="from AdminUser where id=:id";
		Query q=getEntityManager().createQuery(query);
			q.setParameter("id", Integer.valueOf(id));
				List<AdminUser>	adminUserList=q.getResultList();
				if(adminUserList!=null && !adminUserList.isEmpty()){
					return adminUserList.get(0);
				}else{
					return null;
				}
	}		

}
