package com.team.mighty.service;

import java.util.List;

import com.team.mighty.domain.AdminUser;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;

/**
 * 
 * @author Shankara
 *
 */
public interface ConsumerInstrumentService {
	
	public void validateDevice(String deviceId)throws MightyAppException; 
	
	public UserLoginDTO userLogin(UserLoginDTO userLoginDTO) throws MightyAppException;
	
	public UserDeviceRegistrationDTO registerDevice(ConsumerDeviceDTO consumerDeviceDto) throws MightyAppException;
	
	public void deRegisterDevice(ConsumerDeviceDTO consumerDeviceDto);
	
	public void deregisterDevice(String deviceId);
	
	public void updateReistrationToken();
	
	public void retriveDevices();
	
	public List<MightyUserInfo> getMightyUserInfo() throws Exception;

	public List<MightyDeviceInfo> getMightyDeviceInfo() throws Exception;

	public MightyUserInfo mightyUserLogin(ConsumerDeviceDTO consumerDeviceDTO) throws MightyAppException;

	public void registerMightyDevice(DeviceInfoDTO deviceInfoDTO) throws MightyAppException;

	public MightyUserInfo mightyFBUserLogin(ConsumerDeviceDTO consumerDeviceDTO)throws MightyAppException;

	public MightyDeviceInfo getMightyDeviceOnId(long mightyDeviceId) throws MightyAppException;

	public UserLoginDTO getRefreshTokenOnBaseToken() throws MightyAppException;

	public void updatePwd(UserLoginDTO userLoginDTO) throws MightyAppException;

	public MightyUserInfo getUserByEmail(String email) throws MightyAppException;

	public MightyUserInfo setGeneratedPwd(MightyUserInfo mightyUserInfo) throws MightyAppException;

	public String getPasswordResetMessage(MightyUserInfo mightyUser) throws MightyAppException;

	public void changePwd(UserLoginDTO userLoginDTO) throws MightyAppException;

	public List<MightyUserInfo> getSearchUsers(String searchStr) throws Exception;

	public List<MightyDeviceInfo> getMightySearchDevice(String searchDev)throws Exception;

	public List<MightyUserInfo> getAllMightyUsers()throws Exception;

	public List<MightyDeviceInfo> getAllMightyDev()throws Exception;

	public String getUserAccountMessage(UserDeviceRegistrationDTO dto) throws Exception;

	
	
}
