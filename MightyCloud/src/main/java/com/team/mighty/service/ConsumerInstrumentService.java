package com.team.mighty.service;

import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;

/**
 * 
 * @author Shankara
 *
 */
public interface ConsumerInstrumentService {
	
	public void validateDevice(String deviceId) throws MightyAppException; 
	
	public UserLoginDTO userLogin(UserLoginDTO userLoginDTO) throws MightyAppException;
	
	public UserDeviceRegistrationDTO registerDevice(ConsumerDeviceDTO consumerDeviceDto) throws MightyAppException;
	
	public void deRegisterDevice(ConsumerDeviceDTO consumerDeviceDto);
	
	public void deregisterDevice(String deviceId);
	
	public void updateReistrationToken();
	
	public void retriveDevices();
	

}
