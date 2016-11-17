package com.team.mighty.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.mighty.dao.LoginDao;
import com.team.mighty.domain.AdminUser;
import com.team.mighty.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;
	
	public AdminUser getLoginUser(String username, String password)	throws Exception {
		
		return loginDao.getLoginUser(username,password);
	}

}
