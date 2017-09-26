package com.team.mighty.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
import com.team.mighty.constant.PasswordGenerator;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightySpotify;
import com.team.mighty.domain.MightyUpload;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.domain.Mightydlauditlog;
import com.team.mighty.domain.Mightylog;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.notification.SendMail;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.service.LoginService;
import com.team.mighty.service.MightyCommonService;
import com.team.mighty.utils.JWTKeyGenerator;
import com.team.mighty.utils.JsonUtil;

/**
 * 
 * @author Shankara,Vikky
 *
 */

@RestController
@RequestMapping(MightyAppConstants.CONSUMER_API)
public class ConsumerInstrumentController {
	
	@Autowired
	private ConsumerInstrumentService consumerInstrumentServiceImpl;
	
	@Autowired
	private MightyCommonService mightyCommonServiceImpl;
	
	@Autowired
	private LoginService loginService;
	
	/*@Autowired
	private MailMail mail;*/
	
	private static final MightyLogger logger = MightyLogger.getLogger(ConsumerInstrumentController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> userLoginFromApp(@RequestBody UserLoginDTO userLoginDTO){
		logger.info(" /POST User Login API ", userLoginDTO);
		logger.debug("userId as",userLoginDTO.getUserId());
				
		ResponseEntity<String> responseEntity = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			userLoginDTO = consumerInstrumentServiceImpl.userLogin(userLoginDTO);
			String response = JsonUtil.objToJson(userLoginDTO);
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_TOKEN_NAME, userLoginDTO.getApiToken());
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_BASE_TOKEN_NAME, userLoginDTO.getBaseToken());
			
			try {
				/*Epoch format for Access,Base Token Expiration Date*/
			httpHeaders.add("BaseTokenExpiration", String.valueOf(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
										.parse(userLoginDTO.getBaseTokenExpDate().toString()).getTime()));
			
			httpHeaders.add("AccessTokenExpiration", String.valueOf(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
										.parse(userLoginDTO.getAccessTokenExpDate().toString()).getTime()));
			}catch(Exception pe){
				logger.error(pe);
			}
			
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
	public ResponseEntity<String> getRefreshTokenHandler(@RequestHeader(value = MightyAppConstants.HTTP_HEADER_BASE_TOKEN_NAME) String refreshToken){
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
			httpHeaders.add(MightyAppConstants.HTTP_HEADER_BASE_TOKEN_NAME, userLoginDTO.getBaseToken());
			//httpHeaders.add("APITokenExpiration:", userLoginDTO.getAccessTokenExpDate().toString().trim());
			try{
			httpHeaders.add("BaseTokenExpiration", String.valueOf(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
				.parse(userLoginDTO.getBaseTokenExpDate().toString()).getTime()));	
			
			httpHeaders.add("AccessTokenExpiration", String.valueOf(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
			.parse(userLoginDTO.getAccessTokenExpDate().toString()).getTime()));
			}catch(Exception e){
				logger.error(e);
			}
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
			logger.debug("userindicator",obj.get("UserIndicator"));
			if(obj.get("UserIndicator")!=null){
				if(obj.get("UserIndicator").toString().equalsIgnoreCase("F")){
					consumerDeviceDTO=new ConsumerDeviceDTO();
					consumerDeviceDTO.setFacebookID(obj.get("FacebookID").toString());	
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
				}else if(obj.get("UserIndicator").toString().equalsIgnoreCase("L")){
					consumerDeviceDTO=new ConsumerDeviceDTO();
					consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
				    consumerDeviceDTO.setPassword(obj.get("Password").toString());
				    consumerDeviceDTO.setUserIndicator(obj.get("UserIndicator").toString());
				    mightyUserInfo=consumerInstrumentServiceImpl.mightyUserLogin(consumerDeviceDTO);
				}
			}else{
				
					consumerDeviceDTO=new ConsumerDeviceDTO();
					consumerDeviceDTO.setUserName(obj.get("UserName").toString());	
				    consumerDeviceDTO.setPassword(obj.get("Password").toString());
				    consumerDeviceDTO.setUserIndicator("L");
				    mightyUserInfo=consumerInstrumentServiceImpl.mightyUserLogin(consumerDeviceDTO);
				}
			
						
			responseEntity = new ResponseEntity<String>(String.valueOf(mightyUserInfo.getId()), HttpStatus.OK);
			} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	/*@RequestMapping(value="/httpRequest",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	private void httpRequestChecking() throws MightyAppException{
		try{
			String url="http://agri-insurance.gov.in/cbs_integration.asmx?username=vikky&pwd=123456";
			logger.debug("URLConn",url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int  hashcode= con.hashCode();
			int responseCode=con.getResponseCode();
			logger.debug("GET Response Code :: " + responseCode);
			logger.debug("GET hashcode Code :: " + hashcode);
			
		
		}catch(Exception e){
			logger.debug("Error in response",e);
		}
		
	}*/
		
	@RequestMapping(value="/grafana",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doRegistration(@RequestBody String received){
		logger.info(" /POST Consumer API");
		logger.debug("received",received);
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
			consumerDeviceDTO.setUserName(obj.get("UserName").toString().trim());	
			consumerDeviceDTO.setEmailId(obj.get("EmailID").toString().trim());
			consumerDeviceDTO.setPassword(obj.get("Password").toString().trim());
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
			UserDeviceRegistrationDTO dto=consumerInstrumentServiceImpl.registerDevice(consumerDeviceDTO);
				try{
								
						if(dto!=null){
									logger.debug("/inside user account send Mail");
									String subject = "Your brand new Mighty account";
									String message = consumerInstrumentServiceImpl.getUserAccountMessage(dto);
												
										SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
										mail.send(dto.getEmail(), subject, message);
						}
					
					}catch(Exception e){
						logger.error("/Sending user account notification",e);
					}
			
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			
		}
		return responseEntity;
	}
	
	
		
	
	
	/*@RequestMapping(value="/mightyRegistration",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			DeviceInfoDTO deviceInfoDTO=new DeviceInfoDTO();
			deviceInfoDTO.setUserId(obj.get("UserID").toString());
			deviceInfoDTO.setDeviceId(obj.get("HWSerialNumber").toString());
			deviceInfoDTO.setDeviceName(obj.get("DeviceName").toString());
			deviceInfoDTO.setDeviceType(obj.get("DeviceType").toString());
			deviceInfoDTO.setSwVersion(obj.get("SWVersion").toString());
			deviceInfoDTO.setAppVersion(obj.get("AppVersion").toString());
			deviceInfoDTO.setAppBuild(obj.get("AppBuild").toString());
			logger.debug("Appbuild",obj.get("AppBuild").toString());
			deviceInfoDTO.setIsActive(MightyAppConstants.IND_Y);
			deviceInfoDTO.setIsRegistered(MightyAppConstants.IND_Y);
			
			String res=consumerInstrumentServiceImpl.registerMightyDevice(deviceInfoDTO);
			if(res.equalsIgnoreCase("409")){
				responseEntity = new ResponseEntity<String>(HttpStatus.CONFLICT);
				return responseEntity;
			}
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			
		}
		return responseEntity;
	}*/
	
	
	
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
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			DeviceInfoDTO deviceInfoDTO=new DeviceInfoDTO();
			deviceInfoDTO.setUserId(obj.get("UserID").toString());
			deviceInfoDTO.setDeviceId(obj.get("HWSerialNumber").toString());
			deviceInfoDTO.setDeviceName(obj.get("DeviceName").toString());
			deviceInfoDTO.setDeviceType(obj.get("DeviceType").toString());
			deviceInfoDTO.setSwVersion(obj.get("SWVersion").toString());
			deviceInfoDTO.setAppVersion(obj.get("AppVersion").toString());
			deviceInfoDTO.setAppBuild(obj.get("AppBuild").toString());
			logger.debug("Appbuild",obj.get("AppBuild").toString());
			deviceInfoDTO.setIsActive(MightyAppConstants.IND_Y);
			deviceInfoDTO.setIsRegistered(MightyAppConstants.IND_Y);
			deviceInfoDTO.setRegisterAt(new Date(System.currentTimeMillis()));
			deviceInfoDTO.setUpgradedAt(new Date(System.currentTimeMillis()));
			consumerInstrumentServiceImpl.registerMightyDevice(deviceInfoDTO);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		}catch(MightyAppException e){
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage,e.getHttpStatus());
			
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> validateDevice(@PathVariable String deviceId){
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
	
	@RequestMapping(value = "/mightyDeReg",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doDeRegistration(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) {
		logger.info(" /POST Consumer API for MightyDeReg");
		
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
			
			//Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			logger.debug("TestDev",obj.get("deviceID").toString().trim());
			consumerInstrumentServiceImpl.deregisterDevice(obj.get("deviceID").toString().trim());
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	@RequestMapping(value="/changePwd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changePasswordHandler(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken){
		logger.info(" /POST mightyChange pwd request API");
		UserLoginDTO userLoginDTO=null;
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
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			
			logger.debug("userId",obj.get("userId").toString());
			logger.debug("Password",obj.get("Password").toString());
			logger.debug("NewPassord",obj.get("NewPassword").toString());
				userLoginDTO=new UserLoginDTO();
				userLoginDTO.setUserId(Long.valueOf(obj.get("userId").toString()));	
				userLoginDTO.setPwd(obj.get("Password").toString());
				userLoginDTO.setNewPwd(obj.get("NewPassword").toString());
				consumerInstrumentServiceImpl.updatePwd(userLoginDTO);
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;
	}
	
	
	
	/*@RequestMapping(value="/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changePwdHandler(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken)  {
		logger.info(" /POST mightyChange pwd request API");
		UserLoginDTO userLoginDTO=null;
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
			
				//Validate X-MIGHTY-TOKEN Value
				JWTKeyGenerator.validateXToken(xToken);
				
				// Validate Expriy Date
				mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
				
				logger.debug("UserName",obj.get("UserName").toString());
				logger.debug("NewPassord",obj.get("NewPassword").toString());
				userLoginDTO=new UserLoginDTO();
				userLoginDTO.setUserName(obj.get("UserName").toString());	
				userLoginDTO.setNewPwd(obj.get("NewPassword").toString());
				userLoginDTO.setPwdChangedDate(new Date(System.currentTimeMillis()));
				consumerInstrumentServiceImpl.changePwd(userLoginDTO);
				responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;
	}*/
	
	
	@RequestMapping(value= {"/resetPassword"}, method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> resetPasswordHandler(@RequestBody String received) throws Exception{
			logger.info("/POST ResetPassword API");
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyUserInfo mightyUserInfo=null;
		UserLoginDTO userLoginDTO=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON",e);
		}

		
				
		try {
			String password = new PasswordGenerator().randomString(6);
			logger.debug("Password generator "+password);
			logger.debug("UserName "+String.valueOf(obj.get("Email")));
			String subject = "Your brand new Mighty password";
		
			mightyUserInfo=consumerInstrumentServiceImpl.getUserByEmail(String.valueOf(obj.get("Email")));
				
				if(mightyUserInfo!=null){
					mightyUserInfo.setPassword(password);
					mightyUserInfo.setPwdChangedDate(null);
						MightyUserInfo mightyUser= null;
							mightyUser=consumerInstrumentServiceImpl.setGeneratedPwd(mightyUserInfo);
						if(mightyUser!=null){
							logger.debug("/inside send Mail");
							String message = consumerInstrumentServiceImpl.getPasswordResetMessage(mightyUser);
								//mail.sendMail("mightynotifications@gmail.com",mightyUser.getEmailId(), subject, message);
							
														
								SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
								try{
									logger.debug("/inside try/catch send Mail");
								mail.send(mightyUser.getEmailId(), subject, message);
								
								}catch(Exception ex){
									logger.error("/Mail System Error,",ex);
								}
								userLoginDTO=new UserLoginDTO();
								userLoginDTO.setPwdChangedDate(mightyUser.getPwdChangedDate());	
								String response = JsonUtil.objToJson(userLoginDTO);	
								responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
						}else{
								responseEntity = new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
						}
						
						
		}else{
					
					responseEntity = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
					
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}
	
	
	/*@RequestMapping(value = "/getMightyLogs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDebugHandler(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken ) throws Exception{
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyDeviceInfo mightyDeviceInfo=null;
		List<MightyDeviceUserMapping> md=null;
		
		
		try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
					responseEntity = new ResponseEntity<String>("Empty received body", HttpStatus.EXPECTATION_FAILED);
		}

		
				
		try {
			//logger.debug("file_content",file_content);
			logger.debug("file_content",Base64.decodeBase64(obj.get("file_content").toString()).length);
			logger.debug("log_type",obj.get("log_type").toString());
			logger.debug("desc",obj.get("desc").toString());
			logger.debug("deviceId",obj.get("deviceId").toString());
			logger.debug("userId",obj.get("userId").toString());
			Mightylog log=null;
			
			//Validate X-MIGHTY-TOKEN Value
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyOnHwId(obj.get("deviceId").toString());
			if(mightyDeviceInfo!=null){
				md=consumerInstrumentServiceImpl.getMightyDeviceUserMappingOndevId(mightyDeviceInfo.getId());
					if(md!=null && !md.isEmpty()){
						for(MightyDeviceUserMapping m: md){
							if(String.valueOf(m.getMightyUserInfo().getId()).equalsIgnoreCase(obj.get("userId").toString()) && 
									m.getRegistrationStatus().equalsIgnoreCase("Y")){
								logger.debug("Helloooo");
									Mightylog lg=null;
									lg=consumerInstrumentServiceImpl.getExistingMightylog(obj.get("deviceId").toString(),m.getMightyUserInfo().getUserName());
										if(lg!=null){
												lg.setFileName("MightyLogs");
												lg.setFileContent(new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(obj.get("file_content").toString())));
												lg.setLogType(obj.get("log_type").toString());
												lg.setTicket(obj.get("ticket").toString());
												lg.setDescription(obj.get("desc").toString());
												lg.setUsername(m.getMightyUserInfo().getUserName());
												lg.setEmailId(m.getMightyUserInfo().getEmailId());
												lg.setDevReg(m.getRegistrationStatus());
												lg.setDeviceId(obj.get("deviceId").toString());
												lg.setDeviceType(obj.get("DeviceType").toString());
												lg.setPhoneDeviceOSVersion(obj.get("DeviceOSVersion").toString());
												lg.setUpdatedDt(new Date(System.currentTimeMillis()));
											Mightylog logs=consumerInstrumentServiceImpl.updateMightyLogs(lg);
											try{	
												if(logs!=null){
														logger.debug("/inside MightyLogs send Mail");
														String subject="";
															if(logs.getTicket()!=null && !logs.getTicket().isEmpty()){
																subject = "Log received from "+logs.getUsername()+""+"-"+""+"Ticket#"+logs.getTicket();
															}else{
																subject = "Log received from "+logs.getUsername();
															}
														String message = consumerInstrumentServiceImpl.getMightyLogsMsg(logs);
																	
															SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
															String[] arr={"heyo@bemighty.com","mightynotification@gmail.com"};
																									
															for(String s :arr){
																logger.debug("mailing dest",s);
																mail.send(s, subject, message);
															}
												}
										
											}catch(Exception e){
												logger.error("/Sending Mightylog notification",e);
											}
												responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
												return responseEntity;
										}else{
											logger.debug("elseeeeeeeee");
												log=new Mightylog();
												log.setFileName("MightyLogs");
												log.setFileContent(new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(obj.get("file_content").toString())));
												log.setLogType(obj.get("log_type").toString());
												log.setTicket(obj.get("ticket").toString());
												log.setDescription(obj.get("desc").toString());
												log.setUsername(m.getMightyUserInfo().getUserName());
												log.setEmailId(m.getMightyUserInfo().getEmailId());
												log.setDevReg(m.getRegistrationStatus());
												log.setDeviceId(obj.get("deviceId").toString());
												log.setDeviceType(obj.get("DeviceType").toString());
												log.setPhoneDeviceOSVersion(obj.get("DeviceOSVersion").toString());
												log.setCreatedDt(new Date(System.currentTimeMillis()));
												log.setUpdatedDt(new Date(System.currentTimeMillis()));
											Mightylog logs=consumerInstrumentServiceImpl.updateMightyLogs(log);
											
												try{	
													if(logs!=null){
															logger.debug("/inside MightyLogs send Mail");
															String subject="";
																if(logs.getTicket()!=null && !logs.getTicket().isEmpty()){
																	subject = "Log received from "+logs.getUsername()+""+"-"+""+"Ticket#"+logs.getTicket();
																}else{
																	subject = "Log received from "+logs.getUsername();
																}
															String message = consumerInstrumentServiceImpl.getMightyLogsMsg(logs);
																		
																SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
																String[] arr={"heyo@bemighty.com","mightynotification@gmail.com"};
																logger.debug("subject",subject);
																for(String s :arr){
																	logger.debug("mailing dest",s);
																	mail.send(s, subject, message);
																}
																String dest[]={"vikky.softengi@gmail.com","mightynotification@gmail.com"};
																String msg[]={message};
																String[] cc = new String[0];
																mail.send(dest, subject, msg,cc);
													}
											
												}catch(Exception e){
													logger.error("/Sending Mightylog notification",e);
												}
											
												responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
												return responseEntity;
										}
							}else{
								responseEntity = new ResponseEntity<String>("DeviceId not associated with user or registerstatus is N ", HttpStatus.BAD_REQUEST);
							}
						}
					}else{
						responseEntity = new ResponseEntity<String>("DeviceId not mapped", HttpStatus.BAD_REQUEST);
					}
			}else{
				responseEntity = new ResponseEntity<String>("Empty deviceId", HttpStatus.BAD_REQUEST);
			}
							
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}*/
	
	
	@RequestMapping(value = "/getMightyLogs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDebugHandler(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken ) throws Exception{
		
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyDeviceInfo mightyDeviceInfo=null;
		List<MightyDeviceUserMapping> md=null;
		
		
		try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
					responseEntity = new ResponseEntity<String>("Empty received body", HttpStatus.EXPECTATION_FAILED);
		}

		
				
		try {
			//logger.debug("file_content",file_content);
			logger.debug("file_content",Base64.decodeBase64(obj.get("file_content").toString()).length);
			logger.debug("log_type",obj.get("log_type").toString());
			logger.debug("desc",obj.get("desc").toString());
			logger.debug("deviceId",obj.get("deviceId").toString());
			logger.debug("userId",obj.get("userId").toString());
			Mightylog log=null;
			
			//Validate X-MIGHTY-TOKEN Value
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyOnHwId(obj.get("deviceId").toString());
			if(mightyDeviceInfo!=null){
				md=consumerInstrumentServiceImpl.getMightyDeviceUserMappingOndevId(mightyDeviceInfo.getId());
					if(md!=null && !md.isEmpty()){
						for(MightyDeviceUserMapping m: md){
							if(String.valueOf(m.getMightyUserInfo().getId()).equalsIgnoreCase(obj.get("userId").toString()) && 
									m.getRegistrationStatus().equalsIgnoreCase("Y")){
								        logger.debug("Helloooo");
												log=new Mightylog();
												log.setFileName("MightyLogs");
												log.setFileContent(new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(obj.get("file_content").toString())));
												log.setLogType(obj.get("log_type").toString());
												log.setTicket(obj.get("ticket").toString());
												log.setDescription(obj.get("desc").toString());
												log.setUsername(m.getMightyUserInfo().getUserName());
												log.setEmailId(m.getMightyUserInfo().getEmailId());
												log.setDevReg(m.getRegistrationStatus());
												log.setDeviceId(obj.get("deviceId").toString());
												log.setDeviceType(obj.get("DeviceType").toString());
												log.setPhoneDeviceOSVersion(obj.get("DeviceOSVersion").toString());
												log.setCreatedDt(new Date(System.currentTimeMillis()));
												log.setUpdatedDt(new Date(System.currentTimeMillis()));
											Mightylog logs=consumerInstrumentServiceImpl.updateMightyLogs(log);
											
												try{	
													if(logs!=null){
															logger.debug("/inside MightyLogs send Mail");
															String subject="";
																if(logs.getTicket()!=null && !logs.getTicket().isEmpty()){
																	subject = "Log received from "+logs.getUsername()+""+"-"+""+"Ticket#"+logs.getTicket();
																}else{
																	subject = "Log received from "+logs.getUsername();
																}
															String message = consumerInstrumentServiceImpl.getMightyLogsMsg(logs);
																		
																SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
																String[] arr={"heyo@bemighty.com","mightynotification@gmail.com"};
																logger.debug("subject",subject);
																for(String s :arr){
																	logger.debug("mailing dest",s);
																	mail.send(s, subject, message);
																}
															
													}
											
												}catch(Exception e){
													logger.error("/Sending Mightylog notification",e);
												}
											
												responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
												return responseEntity;
										
							}else{
								responseEntity = new ResponseEntity<String>("DeviceId not associated with user or registerstatus is N ", HttpStatus.BAD_REQUEST);
							}
						}
					}else{
						responseEntity = new ResponseEntity<String>("DeviceId not mapped", HttpStatus.BAD_REQUEST);
					}
			}else{
				responseEntity = new ResponseEntity<String>("Empty deviceId", HttpStatus.BAD_REQUEST);
			}
							
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}
	
	
	@RequestMapping(value = "/mightyInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getMightyInfoFromMighty(@RequestBody String received) throws Exception{
		logger.debug("INside /mightyInfo ");
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
					
		
		try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
					responseEntity = new ResponseEntity<String>("Empty received body /mightyInfo", HttpStatus.METHOD_NOT_ALLOWED);
		}

		
				
		try {		
						
			if(obj.get("deviceId").toString()!=null && !obj.get("deviceId").toString().isEmpty() && 
					obj.get("file_content").toString()!=null && !obj.get("file_content").toString().isEmpty()){	
				
				logger.debug("file_content for MightyUpload",Base64.decodeBase64(obj.get("file_content").toString()).length);
				logger.debug("deviceId",obj.get("deviceId").toString());
				
				MightyDeviceInfo mightyDeviceInfo=null;
				mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyOnHwId(obj.get("deviceId").toString());
						if(mightyDeviceInfo!=null){
							logger.debug("INside /MightyUpload ");
							MightyUpload mup=consumerInstrumentServiceImpl.getMightyUploadByDevId(mightyDeviceInfo.getDeviceId());
							if(mup!=null){
								mup.setFileContent(new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(obj.get("file_content").toString())));
								mup.setUpdatedDt(new Date(System.currentTimeMillis()));
								MightyUpload m1=consumerInstrumentServiceImpl.updateMightyUpload(mup);
								if(m1!=null){					
									responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
								}else{
									responseEntity = new ResponseEntity<String>("File persist failed in /mightyInfo",HttpStatus.INTERNAL_SERVER_ERROR);	
								}
							}else{
										MightyUpload mu=null;
													mu=new MightyUpload();
													mu.setFileName("MightyUploads");
													mu.setFileContent(new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(obj.get("file_content").toString())));
													mu.setDeviceId(obj.get("deviceId").toString());
													mu.setCreatedDt(new Date(System.currentTimeMillis()));
													mu.setUpdatedDt(new Date(System.currentTimeMillis()));
													MightyUpload m=consumerInstrumentServiceImpl.updateMightyUpload(mu);
													if(m!=null){					
														responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
													}else{
														responseEntity = new ResponseEntity<String>("File persist failed in /mightyInfo",HttpStatus.INTERNAL_SERVER_ERROR);	
													}
							     }		
								
						}else{
								responseEntity = new ResponseEntity<String>("DeviceId not mapped", HttpStatus.BAD_REQUEST);
						}
			}else{
				responseEntity = new ResponseEntity<String>("deviceId/file_content any or both null", HttpStatus.EXPECTATION_FAILED);
			}
							
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}
	
	
	@RequestMapping(value = "/mightyDlAudioLog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mightyDlAudioLogHandler(@RequestBody String received) throws Exception{
		logger.debug("INside /mightyDlAudioLog controller");
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyDeviceInfo mightyDeviceInfo=null;
				
		
		try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
					responseEntity = new ResponseEntity<String>("Empty received body", HttpStatus.EXPECTATION_FAILED);
		}

		
				
		try {			
			
			logger.debug("app_OS:",obj.get("app_OS").toString());
			logger.debug("app_ver:",obj.get("app_ver").toString());
			logger.debug("wifi_status:",obj.get("wifi_status").toString());
			logger.debug("ble_status:",obj.get("ble_status").toString());
			logger.debug("internet_conn:",obj.get("internet_conn").toString());
			logger.debug("error_code:",obj.get("error_code").toString());
			logger.debug("download_perc:",obj.get("download_perc").toString());
			logger.debug("download_curr_ver:",obj.get("download_curr_ver").toString());
			logger.debug("deviceId:",obj.get("deviceId").toString());
			
									
			mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyOnHwId(obj.get("deviceId").toString());
					if(mightyDeviceInfo!=null){
						logger.debug("INside /MightyUpload list");
						Mightydlauditlog mlog=null;
									mlog=new Mightydlauditlog();
									mlog.setDeviceId(obj.get("deviceId").toString());
									mlog.setApp_OS(obj.get("app_OS").toString());
									mlog.setAppVer(obj.get("app_ver").toString());
									mlog.setWifiStatus(obj.get("wifi_status").toString());
									mlog.setBleStatus(obj.get("ble_status").toString());
									mlog.setInternetConn(obj.get("internet_conn").toString());
									mlog.setErrorCode(obj.get("error_code").toString());
									mlog.setDownloadPerc(obj.get("download_perc").toString());
									mlog.setDownloadCurrVer(obj.get("download_curr_ver").toString());
									mlog.setCreateddt(new Date(System.currentTimeMillis()));
									mlog.setUpdateddt(new Date(System.currentTimeMillis()));
												
									Mightydlauditlog mauditlog=consumerInstrumentServiceImpl.updateMightydlauditlog(mlog);
																	
										responseEntity = new ResponseEntity<String>(HttpStatus.OK);	
										
										
							
					}else{
						responseEntity = new ResponseEntity<String>("DeviceId not mapped", HttpStatus.BAD_REQUEST);
					}
			
							
		}catch(MightyAppException e){
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}
	
	
	@RequestMapping(value = "/mightySpotifyInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mightySpotifyInfoHandler(@RequestBody String received) throws Exception{
		logger.debug("In mightySpotifyInfo controller");
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
		MightyDeviceInfo mightyDeviceInfo=null;
		List<MightySpotify> md=null;
		
		
		try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
					responseEntity = new ResponseEntity<String>("Empty received body", HttpStatus.EXPECTATION_FAILED);
		}

		
				
		try {
			
			if(obj.get("deviceId").toString()!=null && !obj.get("deviceId").toString().isEmpty() && 
					obj.get("m_username").toString()!=null && !obj.get("m_username").toString().isEmpty() &&
						obj.get("sp_username").toString()!=null && !obj.get("sp_username").toString().isEmpty() && 
							obj.get("sw_version").toString()!=null && !obj.get("sw_version").toString().isEmpty() && 
								obj.get("status").toString()!=null && !obj.get("status").toString().isEmpty()){
							
						
						
						logger.debug("deviceId",obj.get("deviceId").toString());
						logger.debug("m_username",obj.get("m_username").toString());
						logger.debug("sp_username",obj.get("sp_username").toString());
						logger.debug("status",obj.get("status").toString());
						logger.debug("sw_version",obj.get("sw_version").toString());
						
						mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyOnHwId(obj.get("deviceId").toString());
						if(mightyDeviceInfo!=null){
							md=consumerInstrumentServiceImpl.getMightySpotifyDetails(mightyDeviceInfo.getDeviceId());
									if(md!=null && !md.isEmpty()){
										MightySpotify ms=md.get(0);
										ms.setMUsername(obj.get("m_username").toString());
										ms.setSpUsername(obj.get("sp_username").toString());
										ms.setStatus(obj.get("status").toString());
										ms.setSwVersion(obj.get("sw_version").toString());
										ms.setUpdateddt(new Date(System.currentTimeMillis()));
											consumerInstrumentServiceImpl.updateMightySpotify(ms);
											responseEntity = new ResponseEntity<String>("succefully added/updated info", HttpStatus.OK);
													
									}else{
										MightySpotify m=null;
											m=new MightySpotify();
											m.setDeviceId(obj.get("deviceId").toString());
											m.setMUsername(obj.get("m_username").toString());
											m.setSpUsername(obj.get("sp_username").toString());
											m.setStatus(obj.get("status").toString());
											m.setSwVersion(obj.get("sw_version").toString());
											m.setCreateddt(new Date(System.currentTimeMillis()));
											m.setUpdateddt(new Date(System.currentTimeMillis()));
												consumerInstrumentServiceImpl.updateMightySpotify(m);
												responseEntity = new ResponseEntity<String>("succefully added/updated info", HttpStatus.OK);
									}
						
						}else{
							responseEntity = new ResponseEntity<String>("deviceId not found", HttpStatus.NOT_FOUND);
						}
			}else{
				responseEntity = new ResponseEntity<String>("mobile info null Expectation failed", HttpStatus.EXPECTATION_FAILED);
			}
							
		}catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;

	}
	
	
	
	
	
}
	