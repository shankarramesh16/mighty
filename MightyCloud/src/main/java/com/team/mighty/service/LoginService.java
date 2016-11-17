package com.team.mighty.service;

import com.team.mighty.domain.AdminUser;

public interface LoginService {

	AdminUser getLoginUser(String username, String password) throws Exception;

}
