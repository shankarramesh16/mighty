package com.team.mighty.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.service.MightyCommonService;
import com.team.mighty.utils.JWTKeyGenerator;
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
	
	@Autowired
	private MightyCommonService mightyCommonServiceImpl;
	
	private static final MightyLogger logger = MightyLogger.getLogger(ConsumerInstrumentController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> userLoginFromApp(@RequestBody UserLoginDTO userLoginDTO)  {
		logger.info(" /POST User Login API ", userLoginDTO);
		logger.debug("userId as",userLoginDTO.getUserId());
		logger.debug("deviceId as",userLoginDTO.getDeviceId());
		
		ResponseEntity<String> responseEntity = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			userLoginDTO = consumerInstrumentServiceImpl.userLogin(userLoginDTO);
			String response = JsonUtil.objToJson(userLoginDTO);
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_TOKEN_NAME, userLoginDTO.getApiToken());
			httpHeaders.add("AccessTokenExpiration:", userLoginDTO.getAccessTokenExpDate().toString());
			
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_BASE_TOKEN_NAME, userLoginDTO.getBaseToken());
			httpHeaders.add("BaseTokenExpiration:", userLoginDTO.getBaseTokenExpDate().toString());
			/*try {
			httpHeaders.add("BaseToken expiration:", String.valueOf(new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz")
			.parse(userLoginDTO.getBaseTokenExpDate().toString()).getTime()));
			httpHeaders.add("APIToken expiration:", String.valueOf(new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz")
			.parse(userLoginDTO.getAccessTokenExpDate().toString()).getTime()));
			}catch(ParseException pe){
				logger.error(pe);
			}*/
			
			responseEntity = new ResponseEntity<String>(response,httpHeaders, HttpStatus.OK);
		}catch(MightyAppException e) {
			logger.errorException(e, e.getMessage());
			userLoginDTO.setStatusCode(e.getHttpStatus().toString());
			userLoginDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(userLoginDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/getRefreshToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRefreshTokenHandler(@RequestHeader(value = MightyAppConstants.HTTP_HEADER_BASE_TOKEN_NAME) String refreshToken) {
		logger.debug("IN POST Refresh Token");
		ResponseEntity<String> responseEntity = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		UserLoginDTO userLoginDTO=null;
		try {
			//Validate BASE-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(refreshToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, refreshToken);
			
			userLoginDTO = consumerInstrumentServiceImpl.getRefreshTokenOnBaseToken();
			String response = JsonUtil.objToJson(userLoginDTO);
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_TOKEN_NAME, userLoginDTO.getApiToken());
			httpHeaders.add("APITokenExpiration:", userLoginDTO.getAccessTokenExpDate().toString());
			responseEntity = new ResponseEntity<String>(response,httpHeaders, HttpStatus.OK);
		}catch(MightyAppException e) {
			logger.errorException(e, e.getMessage());
			userLoginDTO.setStatusCode(e.getHttpStatus().toString());
			userLoginDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(userLoginDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}
	

	
	@RequestMapping(value="/mightyAppLogin",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mightyUserLoginHandler(@RequestBody String received) {
		logger.info(" /POST mightyAppLogin API");
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyUserInfo mightyUserInfo=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON",e);
		}
		
				
		try {
			ConsumerDeviceDTO consumerDeviceDTO=null;
			String userIndicator=obj.get("UserIndicator").toString();
			logger.debug("UserIndicator",userIndicator);
			if(userIndicator.equalsIgnoreCase("L")){
				consumerDeviceDTO=new ConsumerDeviceDTO();
				consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
			    consumerDeviceDTO.setPassword(obj.get("Password").toString());
			    	mightyUserInfo=consumerInstrumentServiceImpl.mightyUserLogin(consumerDeviceDTO);
			}else if(userIndicator.equalsIgnoreCase("F")){
				consumerDeviceDTO=new ConsumerDeviceDTO();
				consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
				consumerDeviceDTO.setEmailId(obj.get("EmailID").toString());
				consumerDeviceDTO.setPassword(obj.get("Password").toString());
				consumerDeviceDTO.setUserIndicator(obj.get("UserIndicator").toString());
				consumerDeviceDTO.setDeviceModel(obj.get("DeviceModel").toString());
				consumerDeviceDTO.setDeviceId(obj.get("DeviceID").toString());
				consumerDeviceDTO.setDeviceName(obj.get("DeviceName").toString());
				consumerDeviceDTO.setDeviceOs(obj.get("DeviceOS").toString());
				consumerDeviceDTO.setDeviceOsVersion(obj.get("DeviceOSVersion").toString());
				consumerDeviceDTO.setDeviceType(obj.get("DeviceType").toString());
				consumerDeviceDTO.setAge(obj.get("Age").toString());
				consumerDeviceDTO.setGender(obj.get("Gender").toString());	
					mightyUserInfo=consumerInstrumentServiceImpl.mightyFBUserLogin(consumerDeviceDTO);
			}
						
			responseEntity = new ResponseEntity<String>(String.valueOf(mightyUserInfo.getId()), HttpStatus.OK);
			} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	/*@RequestMapping(value="/grafana",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> IoITesting(@RequestBody String received) {
		logger.info(" /POST mightyAppLogin API");
		logger.info("hiiii",received);
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyUserInfo mightyUserInfo=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON",e);
		}
		
				
		try {
			
			logger.debug("temperature Value",obj.get("temperature").toString());	
			
			} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}*/
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doRegistration(@RequestBody String received) {
		logger.info(" /POST Consumer API");
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
			
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON ",e);
		}
				
				
		try {
			ConsumerDeviceDTO consumerDeviceDTO=new ConsumerDeviceDTO();
			consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
			consumerDeviceDTO.setEmailId(obj.get("EmailID").toString());
			consumerDeviceDTO.setPassword(obj.get("Password").toString());
			consumerDeviceDTO.setUserIndicator(obj.get("UserIndicator").toString());
			//consumerDeviceDTO.setMightyDeviceId(obj.get("MightyDeviceID").toString());
			consumerDeviceDTO.setDeviceModel(obj.get("DeviceModel").toString());
			consumerDeviceDTO.setDeviceId(obj.get("DeviceID").toString());
			consumerDeviceDTO.setDeviceName(obj.get("DeviceName").toString());
			consumerDeviceDTO.setDeviceOs(obj.get("DeviceOS").toString());
			consumerDeviceDTO.setDeviceOsVersion(obj.get("DeviceOSVersion").toString());
			consumerDeviceDTO.setDeviceType(obj.get("DeviceType").toString());
			consumerDeviceDTO.setAge(obj.get("Age").toString());
			consumerDeviceDTO.setGender(obj.get("Gender").toString());
			consumerInstrumentServiceImpl.registerDevice(consumerDeviceDTO);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	@RequestMapping(value="/fbLogin",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doFacebookLogin(@RequestBody String received,@RequestHeader String xToken) {
		logger.info(" /POST FacebookLogin API");
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
			
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON ",e);
		}
				
				
		try {
			ConsumerDeviceDTO consumerDeviceDTO=new ConsumerDeviceDTO();
			consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
			consumerDeviceDTO.setFirstName(obj.get("FirstName").toString());	
			consumerDeviceDTO.setLastName(obj.get("LastName").toString());
			consumerDeviceDTO.setEmailId(obj.get("EmailID").toString());
			consumerDeviceDTO.setPassword(obj.get("Password").toString());
			consumerDeviceDTO.setUserIndicator(obj.get("UserIndicator").toString());
			//consumerDeviceDTO.setMightyDeviceId(obj.get("MightyDeviceID").toString());
			consumerDeviceDTO.setDeviceModel(obj.get("DeviceModel").toString());
			consumerDeviceDTO.setDeviceId(obj.get("DeviceID").toString());
			consumerDeviceDTO.setDeviceName(obj.get("DeviceName").toString());
			consumerDeviceDTO.setDeviceOs(obj.get("DeviceOS").toString());
			consumerDeviceDTO.setDeviceOsVersion(obj.get("DeviceOSVersion").toString());
			consumerDeviceDTO.setDeviceType(obj.get("DeviceType").toString());
			consumerInstrumentServiceImpl.registerDevice(consumerDeviceDTO);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	@RequestMapping(value="/mightyRegistration",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doMightyRegistration(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) {
		logger.info(" /POST Consumer API");
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
			
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON ",e);
		}
				
				
		try {
			
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_THRIDPARTY, xToken);
			
			DeviceInfoDTO deviceInfoDTO=new DeviceInfoDTO();
			deviceInfoDTO.setUserId(obj.get("UserID").toString());
			deviceInfoDTO.setDeviceId(obj.get("HWSerialNumber").toString());
			deviceInfoDTO.setDeviceName(obj.get("DeviceName").toString());
			deviceInfoDTO.setDeviceType(obj.get("DeviceType").toString());
			deviceInfoDTO.setSwVersion(obj.get("SWVersion").toString());
			deviceInfoDTO.setAppVersion(obj.get("AppVersion").toString());
			deviceInfoDTO.setAppBuild(obj.get("AppBuild").toString());
			deviceInfoDTO.setIsActive(MightyAppConstants.IND_Y);
			deviceInfoDTO.setIsRegistered(MightyAppConstants.IND_Y);
			
			consumerInstrumentServiceImpl.registerMightyDevice(deviceInfoDTO);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
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

	
}
	