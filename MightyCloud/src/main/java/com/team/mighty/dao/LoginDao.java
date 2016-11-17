package com.team.mighty.dao;

import com.team.mighty.domain.AdminUser;

public interface LoginDao {

	AdminUser getLoginUser(String username, String password) throws Exception;

}
