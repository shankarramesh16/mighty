package com.team.mighty.service;

import com.team.mighty.domain.MightyKeyConfig;
import com.team.mighty.exception.MightyAppException;

public interface MightyCommonService {

	public MightyKeyConfig getKeyConfigByKey(String keyName);
	
	public void validateXToken(String servicInvoker, String jwtToken) throws MightyAppException;
}
