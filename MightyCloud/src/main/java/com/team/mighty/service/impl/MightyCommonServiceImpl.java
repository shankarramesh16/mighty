package com.team.mighty.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dao.MightyKeyConfigDAO;
import com.team.mighty.domain.MightyKeyConfig;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.service.MightyCommonService;
import com.team.mighty.utils.JWTKeyGenerator;

@Service("mightyCommonServiceImpl")
public class MightyCommonServiceImpl implements MightyCommonService {

	@Autowired
	private MightyKeyConfigDAO mightyKeyConfigDAO;

	@Override
	public MightyKeyConfig getKeyConfigByKey(String keyName) {
		// TODO Auto-generated method stub
		return mightyKeyConfigDAO.getKeyConfigValue(keyName);
	}

	@Override
	public void validateXToken(String servicInvoker, String jwtToken) throws MightyAppException {
		MightyKeyConfig mightyConfig = getKeyConfigByKey(servicInvoker);
		
		if(mightyConfig == null || mightyConfig.getMightyKeyValue() == null) {
			throw new MightyAppException("Invalid Service Invoker Value", HttpStatus.UNAUTHORIZED);
		}
		
		if(mightyConfig.getStatus().equalsIgnoreCase(MightyAppConstants.IND_D)) {
			throw new MightyAppException("Service Invoker Config is invalid", HttpStatus.NOT_IMPLEMENTED);
		}
		
		JWTKeyGenerator.validateJWTToken(mightyConfig.getMightyKeyValue(), jwtToken);
		
	}

}
