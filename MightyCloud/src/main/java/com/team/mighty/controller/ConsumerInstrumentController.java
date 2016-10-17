package com.team.mighty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.utils.JsonUtil;

/**
 * 
 * @author Shankara
 *
 */

@RestController
@RequestMapping(MightyAppConstants.CONSUMER_API)
public class ConsumerInstrumentController {
	
	@Autowired
	private ConsumerInstrumentService consumerInstrumentServiceImpl;
	
	private static final MightyLogger logger = MightyLogger.getLogger(ConsumerInstrumentController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> userLoginFromApp(@RequestBody UserLoginDTO userLoginDTO) {
		logger.info(" /POST User Login API ", userLoginDTO);
		ResponseEntity<String> responseEntity = null;
		try {
			userLoginDTO = consumerInstrumentServiceImpl.userLogin(userLoginDTO);
			String response = JsonUtil.objToJson(userLoginDTO);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
		} catch(MightyAppException e) {
			logger.errorException(e, e.getMessage());
			userLoginDTO.setStatusCode(e.getHttpStatus().toString());
			userLoginDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(userLoginDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doRegistration(@RequestBody ConsumerDeviceDTO consumerDeviceDto) {
		logger.info(" /POST Consumer API",  consumerDeviceDto);
		ResponseEntity<String> responseEntity = null;
		try {
			UserDeviceRegistrationDTO userInfo = consumerInstrumentServiceImpl.registerDevice(consumerDeviceDto);
			String response = JsonUtil.objToJson(userInfo);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> validateDevice(@PathVariable String deviceId) {
		logger.info("/GET Validate Devoce", deviceId);
		ResponseEntity<String> responseEntity = null;
		try {
			consumerInstrumentServiceImpl.validateDevice(deviceId);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		
		return responseEntity;
	}
	
	@RequestMapping(value = "/{deviceId}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doDeRegistration(@PathVariable String deviceId) {
		logger.info(" /POST Consumer API for MightyDeReg",  deviceId);
			
		ResponseEntity<String> responseEntity = null;
		try {
			consumerInstrumentServiceImpl.deregisterDevice(deviceId);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}

	public ConsumerInstrumentService getConsumerInstrumentServiceImpl() {
		return consumerInstrumentServiceImpl;
	}

	public void setConsumerInstrumentServiceImpl(ConsumerInstrumentService consumerInstrumentServiceImpl) {
		this.consumerInstrumentServiceImpl = consumerInstrumentServiceImpl;
	}
}
	