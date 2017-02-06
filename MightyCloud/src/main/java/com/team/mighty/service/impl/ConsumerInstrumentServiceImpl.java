package com.team.mighty.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dao.ConsumerInstrumentDAO;
import com.team.mighty.dao.MightyDeviceInfoDAO;
import com.team.mighty.dao.MightyDeviceOrderDAO;
import com.team.mighty.dao.MightyDeviceUserMapDAO;
import com.team.mighty.dao.MightyKeyConfigDAO;
import com.team.mighty.dao.MightyUserInfoDao;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyKeyConfig;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.utils.JWTKeyGenerator;
import com.team.mighty.utils.SpringPropertiesUtil;

/**
 * 
 * @author Shankara
 *
 */
@Service("consumerInstrumentServiceImpl")
public class ConsumerInstrumentServiceImpl implements ConsumerInstrumentService {

	private final MightyLogger logger = MightyLogger.getLogger(ConsumerInstrumentServiceImpl.class);
	
	@Autowired
	private MightyDeviceInfoDAO mightyDeviceInfoDAO;
	
	@Autowired
	private ConsumerInstrumentDAO consumerInstrumentDAO;
	
	@Autowired
	private MightyDeviceUserMapDAO mightyDeviceUserMapDAO;
	
	@Autowired
	private MightyUserInfoDao mightyUserInfoDAO;
	
	@Autowired
	private MightyKeyConfigDAO mightyKeyConfigDAO;
	
	@Autowired
	private MightyDeviceOrderDAO mightyDeviceOrderDAO;
	
	
	

	public UserLoginDTO userLogin(UserLoginDTO userLoginDTO) {
		if(userLoginDTO == null) {
			throw new MightyAppException("Invalid Request, User Login Request is empty", HttpStatus.BAD_REQUEST);
		}
		
		if(userLoginDTO.getUserId() <=0 ) {
			throw new MightyAppException("Invalid Request, User Id or Phone Device Id is empty", HttpStatus.BAD_REQUEST);
		}
		
		MightyUserInfo mightyUserInfo = consumerInstrumentDAO.findOne(userLoginDTO.getUserId());
		
		if(null == mightyUserInfo) {
			throw new MightyAppException(" User Id not found in system ", HttpStatus.NOT_FOUND);
		}
		
		Set<MightyDeviceUserMapping> userMapping = mightyUserInfo.getMightyDeviceUserMapping();
		
		if(userMapping == null) {
			throw new MightyAppException(" Phone device not found in system ", HttpStatus.NOT_FOUND);
		}
		
		/*Set<MightyDeviceUserMapping> deviceMap = mightyDeviceUserMapDAO.checkUserAndPhoneDeviceId(mightyUserInfo.getId(), userLoginDTO.getDeviceId());
		
		if(deviceMap == null) {
			throw new MightyAppException(" Phone device not found in system ", HttpStatus.NOT_FOUND);
		}
		*/		
		Iterator<MightyDeviceUserMapping> it = userMapping.iterator();
		
		List<String> lstMightyDevice = new ArrayList<String>();
		
		while(it.hasNext()) {
			MightyDeviceUserMapping mightDeviceUser = it.next();
			  if(mightDeviceUser.getRegistrationStatus().equalsIgnoreCase(MightyAppConstants.IND_N)){
				throw new MightyAppException(" User Registerd Device Status is In Active ", HttpStatus.UNAUTHORIZED);
			}
			
			long mightyDeviceId = mightDeviceUser.getMightyDeviceId();
			
			MightyDeviceInfo mightyDeviceInfo = mightyDeviceInfoDAO.findOne(mightyDeviceId);
			if(mightyDeviceInfo != null) {
				lstMightyDevice.add(mightyDeviceInfo.getDeviceId());
			}
		}
		
		userLoginDTO.setUserStatus(mightyUserInfo.getUserStatus());
		userLoginDTO.setLstMightyDeviceId(lstMightyDevice);
		
		userLoginDTO.setStatusCode(HttpStatus.OK.toString());
		
		MightyKeyConfig mightyKeyConfig = mightyKeyConfigDAO.getKeyConfigValue(MightyAppConstants.KEY_MIGHTY_MOBILE);
		
		if(null != mightyKeyConfig && (mightyKeyConfig.getIsEnabled() != null && 
				mightyKeyConfig.getIsEnabled().equalsIgnoreCase(MightyAppConstants.IND_Y))) {
			
			//long ttlMillis = Long.parseLong(SpringPropertiesUtil.getProperty(MightyAppConstants.TTL_LOGIN_KEY));
			
			long ttlMillis=TimeUnit.HOURS.toMillis(2);
			long ttlBaseMillis=TimeUnit.DAYS.toMillis(60);
			
			//long ttlMillis=TimeUnit.MINUTES.toMillis(1);
			//long ttlBaseMillis=TimeUnit.MINUTES.toMillis(5);
			
			logger.debug("ttlMillisVal",ttlMillis);
			logger.debug("ttlBaseMillisVal",ttlBaseMillis);
			
			UserLoginDTO accessToken = JWTKeyGenerator.createJWTAccessToken(mightyKeyConfig.getMightyKeyValue(), MightyAppConstants.TOKEN_LOGN_ID,
					MightyAppConstants.SUBJECT_SECURE, ttlMillis);
			
			UserLoginDTO baseToken = JWTKeyGenerator.createJWTBaseToken(mightyKeyConfig.getMightyKeyValue(), MightyAppConstants.TOKEN_LOGN_ID,
					MightyAppConstants.SUBJECT_SECURE, ttlBaseMillis);
						
			userLoginDTO.setApiToken(accessToken.getApiToken());
			userLoginDTO.setAccessTokenExpDate(accessToken.getAccessTokenExpDate());
			
			userLoginDTO.setBaseToken(baseToken.getBaseToken());
			userLoginDTO.setBaseTokenExpDate(baseToken.getBaseTokenExpDate());
		}
		
		return userLoginDTO;
	}

	/*@Transactional
	private MightyUserInfo registerUserAndDevice(ConsumerDeviceDTO consumerDeviceDto, MightyDeviceInfo mightyDeviceInfo) throws MightyAppException {
		
		MightyUserInfo mightyUserInfo = null;
		String phoneDeviceId = consumerDeviceDto.getDeviceId();
		if(consumerDeviceDto.getUserId() > 0 ) {
			mightyUserInfo = consumerInstrumentDAO.findOne(consumerDeviceDto.getUserId());
			
			if(mightyUserInfo != null) {
				// Check any de-activated device registered
				MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(consumerDeviceDto.getUserId(), mightyDeviceInfo.getId(), phoneDeviceId);
				
				if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
					logger.info(" Already Disbaled account is there and activating that one ------- ");
					mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
					mightyDeviceInfo.setIsRegistered(MightyAppConstants.IND_Y);
					mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
					mightyDeviceInfoDAO.save(mightyDeviceInfo);
					return mightyUserInfo;
				} else if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_Y)) {
					throw new MightyAppException(" User Id, Device Id and Phone Device is already registered", HttpStatus.CONFLICT);
				}
			}
		}*/
	
	@Transactional
	private MightyUserInfo registerUserAndDevice(ConsumerDeviceDTO consumerDeviceDto) throws MightyAppException {
		
		MightyUserInfo mightyUserInfo = null;
		mightyUserInfo=getUserByNameAndEmailWithIndicator(consumerDeviceDto.getUserName(),consumerDeviceDto.getEmailId(),consumerDeviceDto.getUserIndicator());
		String phoneDeviceId = consumerDeviceDto.getDeviceId();
		if(mightyUserInfo!=null) {
			if(mightyUserInfo.getEmailId().equalsIgnoreCase(consumerDeviceDto.getEmailId())){
				throw new MightyAppException(" Emailaddress is already registered", HttpStatus.CONFLICT);
			}else if(mightyUserInfo.getUserName().equalsIgnoreCase(consumerDeviceDto.getUserName())){
				throw new MightyAppException(" Username is already registered", HttpStatus.CONFLICT);
			}
			/*
			// Check any de-activated account---Not Required right now during Mighty User registration. 
			MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(mightyUserInfo.getId());
			if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
					logger.info(" Already Disbaled account is there and activating that one ------- ");
					mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
					mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
					mightyUserInfo=mightyDeviceUserMapping.getMightyUserInfo();
					return mightyUserInfo;
				} else if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_Y)) {
					throw new MightyAppException("User details is already registered", HttpStatus.CONFLICT);
				}
			 */
		 }
					
		
		if(mightyUserInfo == null) {
			logger.info(" User information not found in database, hence creating new one ");
			mightyUserInfo = new MightyUserInfo();
			
			logger.info("----------------- "+consumerDeviceDto.getUserName());
			mightyUserInfo.setUserName(consumerDeviceDto.getUserName());
			mightyUserInfo.setUserStatus(MightyAppConstants.IND_A);
			mightyUserInfo.setFirstName(consumerDeviceDto.getFirstName());
			mightyUserInfo.setLastName(consumerDeviceDto.getLastName());
			mightyUserInfo.setEmailId(consumerDeviceDto.getEmailId());
			mightyUserInfo.setPassword(consumerDeviceDto.getPassword());
			mightyUserInfo.setAge(consumerDeviceDto.getAge());
			mightyUserInfo.setGender(consumerDeviceDto.getGender());
			mightyUserInfo.setUserIndicator(consumerDeviceDto.getUserIndicator());
			mightyUserInfo.setCreatedDt(new Date(System.currentTimeMillis()));
			mightyUserInfo.setUpdatedDt(new Date(System.currentTimeMillis()));
			mightyUserInfo.setPwdChangedDate(new Date(System.currentTimeMillis()));
		}
		MightyDeviceUserMapping mightyDeviceUserMapping=null;
		mightyDeviceUserMapping = new MightyDeviceUserMapping();
		mightyDeviceUserMapping.setMightyUserInfo(mightyUserInfo);
		mightyDeviceUserMapping.setPhoneDeviceOSVersion(consumerDeviceDto.getDeviceOs());
		mightyDeviceUserMapping.setPhoneDeviceType(consumerDeviceDto.getDeviceType());
		mightyDeviceUserMapping.setPhoneDeviceId(consumerDeviceDto.getDeviceId());
		mightyDeviceUserMapping.setPhoneDeviceVersion(consumerDeviceDto.getDeviceOsVersion());
		mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
		mightyDeviceUserMapping.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceUserMapping.setUpdatedDt(new Date(System.currentTimeMillis()));
		
		Set<MightyUserInfo> setUserInfo = new HashSet<MightyUserInfo>();
		
		Set<MightyDeviceUserMapping> setMightyUserDevice = mightyUserInfo.getMightyDeviceUserMapping();
		if(setMightyUserDevice == null || mightyUserInfo.getMightyDeviceUserMapping().isEmpty()) {
			setMightyUserDevice = new HashSet<MightyDeviceUserMapping>();
		}
		setMightyUserDevice.add(mightyDeviceUserMapping);
		mightyUserInfo.setMightyDeviceUserMapping(setMightyUserDevice);
		
		setUserInfo.add(mightyUserInfo);
		
		MightyUserInfo mightyUserInfo_1 = null;
		try {
			mightyUserInfo_1 = consumerInstrumentDAO.save(mightyUserInfo);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new MightyAppException("Unable to save User Device Mapping", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		logger.info(" Mighty USer ID ",mightyUserInfo_1.getId());
		
		return mightyUserInfo_1;
	}
	
	private MightyUserInfo getUserByNameAndEmail(String userName,String emailId) {
		return mightyUserInfoDAO.getUserByNameAndEmail(userName,emailId);
	}

	private MightyDeviceInfo getDeviceDetails(String deviceId) {
		return mightyDeviceInfoDAO.getDeviceInfo(deviceId);
	}
	
	public UserDeviceRegistrationDTO registerDevice(ConsumerDeviceDTO consumerDeviceDto) throws MightyAppException {
		if(null == consumerDeviceDto) {
			logger.debug("Register Device, Consumer Device DTO object is null");
			throw new MightyAppException("Invalid request Object", HttpStatus.BAD_REQUEST);
		}
		
		if((null == consumerDeviceDto.getUserName() || "".equalsIgnoreCase(consumerDeviceDto.getUserName()))
				|| (null == consumerDeviceDto.getDeviceId() || "".equals(consumerDeviceDto.getDeviceId())))
				 {
			logger.debug("Register Device, Anyone of the object is empty [UserName, DeviceId] ", consumerDeviceDto.getUserName(), 
					",",consumerDeviceDto.getDeviceId());
			throw new MightyAppException("Invalid request Parameters [UserName or Device Id ] ", HttpStatus.BAD_REQUEST);
		}
		
		MightyUserInfo mightyUserInfo=registerUserAndDevice(consumerDeviceDto);
		
		return constructResponse(mightyUserInfo);
		
	}

	
	@Transactional
	private MightyUserInfo registerFBUserAndDevice(ConsumerDeviceDTO consumerDeviceDto) throws MightyAppException{
		/*Validating on FB Token*/
		validateFacebookToken(consumerDeviceDto.getPassword());
		
		
		MightyUserInfo mightyUserInfo = null;
		mightyUserInfo=getUserByNameAndEmailWithIndicator(consumerDeviceDto.getUserName(),consumerDeviceDto.getEmailId(),consumerDeviceDto.getUserIndicator());
		String phoneDeviceId = consumerDeviceDto.getDeviceId();
		if(mightyUserInfo!=null) {
			// Check any de-activated account
			logger.debug("IN If -before to check active/deactive");
			MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(mightyUserInfo.getId());
			if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
					logger.info(" Already Disbaled account is there and activating that one ------- ");
					mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
					mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
					mightyUserInfo=mightyDeviceUserMapping.getMightyUserInfo();
					return mightyUserInfo;
			} else{
				logger.debug("IN Else -registered already");
				return mightyUserInfo;
			}
			 
		 }
					
		
		if(mightyUserInfo == null) {
			logger.info(" User information not found in database, hence creating new one ");
			mightyUserInfo = new MightyUserInfo();
			
			logger.info("----------------- "+consumerDeviceDto.getUserName());
			mightyUserInfo.setUserName(consumerDeviceDto.getUserName());
			mightyUserInfo.setUserStatus(MightyAppConstants.IND_A);
			mightyUserInfo.setFirstName(consumerDeviceDto.getFirstName());
			mightyUserInfo.setLastName(consumerDeviceDto.getLastName());
			mightyUserInfo.setEmailId(consumerDeviceDto.getEmailId());
			//mightyUserInfo.setPassword(consumerDeviceDto.getPassword());
			mightyUserInfo.setAge(consumerDeviceDto.getAge());
			mightyUserInfo.setGender(consumerDeviceDto.getGender());
			mightyUserInfo.setUserIndicator(consumerDeviceDto.getUserIndicator());
			mightyUserInfo.setCreatedDt(new Date(System.currentTimeMillis()));
			mightyUserInfo.setUpdatedDt(new Date(System.currentTimeMillis()));
			
		}
		MightyDeviceUserMapping mightyDeviceUserMapping=null;
		mightyDeviceUserMapping = new MightyDeviceUserMapping();
		mightyDeviceUserMapping.setMightyUserInfo(mightyUserInfo);
		mightyDeviceUserMapping.setPhoneDeviceOSVersion(consumerDeviceDto.getDeviceOs());
		mightyDeviceUserMapping.setPhoneDeviceType(consumerDeviceDto.getDeviceType());
		mightyDeviceUserMapping.setPhoneDeviceId(consumerDeviceDto.getDeviceId());
		mightyDeviceUserMapping.setPhoneDeviceVersion(consumerDeviceDto.getDeviceOsVersion());
		mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
		mightyDeviceUserMapping.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceUserMapping.setUpdatedDt(new Date(System.currentTimeMillis()));
		
		Set<MightyUserInfo> setUserInfo = new HashSet<MightyUserInfo>();
		
		Set<MightyDeviceUserMapping> setMightyUserDevice = mightyUserInfo.getMightyDeviceUserMapping();
		if(setMightyUserDevice == null || mightyUserInfo.getMightyDeviceUserMapping().isEmpty()) {
			setMightyUserDevice = new HashSet<MightyDeviceUserMapping>();
		}
		setMightyUserDevice.add(mightyDeviceUserMapping);
		mightyUserInfo.setMightyDeviceUserMapping(setMightyUserDevice);
		
		setUserInfo.add(mightyUserInfo);
		
		MightyUserInfo mightyUserInfo_1 = null;
		try {
			mightyUserInfo_1 = consumerInstrumentDAO.save(mightyUserInfo);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new MightyAppException("Unable to save User Device Mapping", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		logger.info(" Mighty USer ID ",mightyUserInfo_1.getId());
		
		return mightyUserInfo_1;
	}

	private void validateFacebookToken(String fbToken) throws MightyAppException{
		try{
			String url="https://graph.facebook.com/me?access_token="+fbToken;
			logger.debug("URLConn",url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			logger.debug("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				logger.debug("Token valid,GET Response with 200");
			} else {
				throw new MightyAppException("Invalid Fb access token, get request is not worked", HttpStatus.BAD_REQUEST);
			}

		
		}catch(Exception e){
			throw new MightyAppException("Invalid Facebook token", HttpStatus.EXPECTATION_FAILED);
		}
		
	}

	private MightyUserInfo getUserByNameAndEmailWithIndicator(String userName, String emailId, String userIndicator) {
		
		return mightyUserInfoDAO.getUserByNameAndEmailWithIndicator(userName,emailId,userIndicator);
	}

	private UserDeviceRegistrationDTO constructResponse(MightyUserInfo mightyUserInfo) {
		UserDeviceRegistrationDTO userDeviceRegistrationDTO = new UserDeviceRegistrationDTO();
		userDeviceRegistrationDTO.setUserId(mightyUserInfo.getId());
		userDeviceRegistrationDTO.setUserName(mightyUserInfo.getUserName());
		userDeviceRegistrationDTO.setStatus(mightyUserInfo.getUserStatus());
		return userDeviceRegistrationDTO;
	}
	
	public void deRegisterDevice(ConsumerDeviceDTO consumerDeviceDto) {
		if(null == consumerDeviceDto) {
			logger.debug("De Register Device, Consumer Device DTO object is null");
			throw new MightyAppException("Invalid request Object", HttpStatus.BAD_REQUEST);
		}
		
		if((null == consumerDeviceDto.getUserName() || "".equalsIgnoreCase(consumerDeviceDto.getUserName()))
				|| (null == consumerDeviceDto.getDeviceId() || "".equals(consumerDeviceDto.getDeviceId()))
				|| (null == consumerDeviceDto.getMightyDeviceId() || "".equals(consumerDeviceDto.getMightyDeviceId()))) {
			logger.debug(" De RegisterDevice, Anyone of the object is empty [UserName, DeviceId, MightyDeviceId] ", consumerDeviceDto.getUserName(), 
					",",consumerDeviceDto.getDeviceId(), ",",consumerDeviceDto.getMightyDeviceId() );
			throw new MightyAppException("Invalid request Parameters [UserName or Device Id or Mighty Device Id] ", HttpStatus.BAD_REQUEST);
		}
		
	}

	public void deregisterDevice(String deviceId) {
		if(null == deviceId) {
			logger.debug("DeRegister Device, Consumer DeviceId is null");
			throw new MightyAppException("Invalid request Object", HttpStatus.BAD_REQUEST);
		}
		
			
		MightyDeviceInfo mightDeviceInfo = getDeviceDetails(deviceId);
		
		if(mightDeviceInfo == null ) {
			throw new MightyAppException("Device Details Not Found in System", HttpStatus.NOT_FOUND);
		}
		
		if(mightDeviceInfo.getIsActive().equalsIgnoreCase(MightyAppConstants.IND_N)) {
			throw new MightyAppException("Device is In Active, So cannot update", HttpStatus.PRECONDITION_FAILED);
		}
		
		MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.getDeviceInfo(mightDeviceInfo.getId());
		
		if(mightyDeviceUserMapping != null) {
			logger.debug(mightyDeviceUserMapping);
			logger.debug("Inside",mightyDeviceUserMapping.getMightyUserInfo());
			logger.debug(mightyDeviceUserMapping.getMightyUserInfo().getId());
			mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_N);
			updateUserDeviceMap(mightyDeviceUserMapping);
		}
		
		mightDeviceInfo.setIsRegistered(MightyAppConstants.IND_N);
		
		updateForDeRegisterDevice(mightDeviceInfo);
		
	}
	
	private void updateUserDeviceMap(MightyDeviceUserMapping mightyDevUsrMap) {
		try {
			mightyDeviceUserMapDAO.save(mightyDevUsrMap);
		} catch(Exception e) {
			throw new MightyAppException("Unable to update User Device Mapping", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}
	
	public void updateForDeRegisterDevice(MightyDeviceInfo mightDeviceInfo) {
		try {
			mightyDeviceInfoDAO.save(mightDeviceInfo);
		} catch(Exception e) {
			throw new MightyAppException("Unable to update ", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
	}

	public void updateReistrationToken() {
		// TODO Auto-generated method stub
		
	}

	public void retriveDevices() {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void validateDevice(String deviceId) throws MightyAppException {
		logger.info(" === ConsumerInstrumentServiceImpl, ValidateDevice, Device Id ", deviceId);
		if(null == deviceId ||  "".equalsIgnoreCase(deviceId)) {
			throw new MightyAppException(" Device ID or Input is empty", HttpStatus.BAD_REQUEST);
		}
		
		/*checking for attaching device a order placed or not*/
		/*MightyDeviceOrderInfo mightyDeviceOrder=null;
		try{
			mightyDeviceOrder=mightyDeviceOrderDAO.getDeviceOrderById(deviceId);
		}catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(mightyDeviceOrder==null){
			throw new MightyAppException( "For this device Id, order is not place in system ", HttpStatus.GONE);
		}*/
		
						
		MightyDeviceInfo mightyDeviceInfo = null;
		try {
			logger.debug("deviceId:",deviceId);
			mightyDeviceInfo = mightyDeviceInfoDAO.getDeviceInfo(deviceId);
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		/*if(null == mightyDeviceInfo) {
			throw new MightyAppException(" Device Details not found", HttpStatus.NOT_FOUND);
		}*/
		
				
		if(mightyDeviceInfo!=null){
			String isActive = mightyDeviceInfo.getIsActive();
			String isRegistered = mightyDeviceInfo.getIsRegistered();
			if(null == isActive || isActive.equalsIgnoreCase(MightyAppConstants.IND_N) ) {
				throw new MightyAppException(" Device is not active or status is empty", HttpStatus.GONE);
			}
			
					
			if(null != isRegistered && isRegistered.equalsIgnoreCase(MightyAppConstants.IND_Y)) {
				throw new MightyAppException(" Deivce already registered ", HttpStatus.CONFLICT);
			}
		}
		
		
	}

	public MightyDeviceInfoDAO getMightyDeviceInfoDAO() {
		return mightyDeviceInfoDAO;
	}

	public void setMightyDeviceInfoDAO(MightyDeviceInfoDAO mightyDeviceInfoDAO) {
		this.mightyDeviceInfoDAO = mightyDeviceInfoDAO;
	}
	
	public List<MightyUserInfo> getMightyUserInfo() throws Exception {
		return mightyDeviceInfoDAO.getMightyUserInfo();
	}

	
	public List<MightyDeviceInfo> getMightyDeviceInfo() throws Exception {
		return mightyDeviceInfoDAO.getMightyDeviceInfo();
	}

	
	public MightyUserInfo mightyUserLogin(ConsumerDeviceDTO consumerDeviceDTO) throws MightyAppException {
	
		MightyUserInfo mightyUserInfo = null;
		try {
			mightyUserInfo = mightyUserInfoDAO.getMightyUserLogin(consumerDeviceDTO.getPassword(),consumerDeviceDTO.getUserName());
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(null == mightyUserInfo) {
			throw new MightyAppException(" Invalid Username or Password", HttpStatus.NOT_FOUND);
		}
		return mightyUserInfo;
	}

	
	public void registerMightyDevice(DeviceInfoDTO deviceInfoDTO) throws MightyAppException {
		if(null == deviceInfoDTO) {
			logger.debug("Register Device, Consumer Device DTO object is null");
			throw new MightyAppException("Invalid request Object", HttpStatus.BAD_REQUEST);
		}
		
		if((null == deviceInfoDTO.getUserId() || "".equalsIgnoreCase(deviceInfoDTO.getUserId()))
				|| (null == deviceInfoDTO.getDeviceId() || "".equals(deviceInfoDTO.getDeviceId())))
				 {
			logger.debug("Register Device, Anyone of the object is empty [UserId, DeviceId] ", deviceInfoDTO.getUserId(), 
					",",deviceInfoDTO.getDeviceId());
			throw new MightyAppException("Invalid request Parameters [UserId or Device Id ] ", HttpStatus.BAD_REQUEST);
		}
		
		validateDevice(deviceInfoDTO.getDeviceId());
		registerMightyWithUser(deviceInfoDTO);
		
		//MightyUserInfo mightyUserInfo =  registerUserAndDevice(consumerDeviceDto);
		
				
	}

	private void registerMightyWithUser(DeviceInfoDTO deviceInfoDTO) throws MightyAppException {
		MightyUserInfo mightyUserInfo = null;
		mightyUserInfo=getUserById(deviceInfoDTO.getUserId());
		if(mightyUserInfo!=null) {
			// Check any de-activated account
			MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(mightyUserInfo.getId());
			
			if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
							logger.info(" Already Disbaled account is there and activating that one ------- ");
							logger.info("active and de-active status",mightyDeviceUserMapping.getRegistrationStatus());
							mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
							mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
							
							MightyDeviceInfo mightyDeviceInfo=mightyDeviceInfoDAO.getMightyDeviceOnId(mightyDeviceUserMapping.getMightyDeviceId());
							if(mightyDeviceInfo!=null && mightyDeviceInfo.getIsRegistered().equals(MightyAppConstants.IND_N)){
								mightyDeviceInfo.setIsRegistered(MightyAppConstants.IND_Y);
								mightyDeviceInfoDAO.save(mightyDeviceInfo);	
							}
			} else if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_Y)) {
					//MightyDeviceOrderInfo mightyDeviceOrderInfo=mightyDeviceOrderDAO.getDeviceOrderById(deviceInfoDTO.getDeviceId());
					logger.info("active and de-active status",mightyDeviceUserMapping.getRegistrationStatus());
					MightyDeviceInfo mightyDeviceInfo=new MightyDeviceInfo();
					mightyDeviceInfo.setDeviceId(deviceInfoDTO.getDeviceId());
					mightyDeviceInfo.setDeviceName(deviceInfoDTO.getDeviceName());
					mightyDeviceInfo.setDeviceType(deviceInfoDTO.getDeviceType());
					mightyDeviceInfo.setSwVersion(deviceInfoDTO.getSwVersion());
					mightyDeviceInfo.setIsActive(deviceInfoDTO.getIsActive());
					mightyDeviceInfo.setIsRegistered(deviceInfoDTO.getIsRegistered());
					mightyDeviceInfo.setAppVersion(Float.valueOf(deviceInfoDTO.getAppVersion()));
					logger.debug("AppVersion",deviceInfoDTO.getAppVersion());
					mightyDeviceInfo.setAppBuild(deviceInfoDTO.getAppBuild());
					//mightyDeviceInfo.setDeviceOrderInfo(mightyDeviceOrderInfo);
					MightyDeviceInfo mightyDevice=null;
					try{
						mightyDevice=mightyDeviceInfoDAO.save(mightyDeviceInfo);
					}catch(Exception e){
						logger.error(e.getMessage());
						throw new MightyAppException("Unable to save User Mighty Device Info ", HttpStatus.INTERNAL_SERVER_ERROR, e);
					}
					
						mightyDeviceUserMapping.setMightyDeviceId(mightyDevice.getId());
						mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
										
				}
		  }
					
		
		
	}

	private MightyUserInfo getUserById(String userId) {
		return mightyUserInfoDAO.getUserById(Long.parseLong(userId.trim()));
	}

	
	public MightyUserInfo mightyFBUserLogin(ConsumerDeviceDTO consumerDeviceDTO) throws MightyAppException {
		return registerFBUserAndDevice(consumerDeviceDTO);
	}

	
	public MightyDeviceInfo getMightyDeviceOnId(long mightyDeviceId) throws MightyAppException {
		return mightyDeviceInfoDAO.getMightyDeviceOnId(mightyDeviceId);
	}

	
	public static void main(String[] args) {
		String ttlMillis = SpringPropertiesUtil.getProperty("mighty.token.login.ttlmillis");
		System.out.println("ttlMillisVal"+ttlMillis);
	}

	
	public UserLoginDTO getRefreshTokenOnBaseToken() throws MightyAppException {
				
		MightyKeyConfig mightyKeyConfig = mightyKeyConfigDAO.getKeyConfigValue(MightyAppConstants.KEY_MIGHTY_MOBILE);
		UserLoginDTO userLoginDTO=null;
		
		if(null != mightyKeyConfig && (mightyKeyConfig.getIsEnabled() != null && 
				mightyKeyConfig.getIsEnabled().equalsIgnoreCase(MightyAppConstants.IND_Y))) {
			
			userLoginDTO=new UserLoginDTO();
			userLoginDTO.setStatusCode(HttpStatus.OK.toString());
			
			//long ttlMillis = Long.parseLong(SpringPropertiesUtil.getProperty(MightyAppConstants.TTL_LOGIN_KEY));
			long ttlMillis=TimeUnit.HOURS.toMillis(2);
			//long ttlMillis=TimeUnit.MINUTES.toMillis(1);
					
			logger.debug("ttlMillisVal",ttlMillis);
						
			UserLoginDTO newAccessToken = JWTKeyGenerator.createJWTAccessToken(mightyKeyConfig.getMightyKeyValue(), MightyAppConstants.TOKEN_LOGN_ID,
					MightyAppConstants.SUBJECT_SECURE, ttlMillis);
						
						
			userLoginDTO.setApiToken(newAccessToken.getApiToken());
			userLoginDTO.setAccessTokenExpDate(newAccessToken.getAccessTokenExpDate());
						
		}
		
		return userLoginDTO;
	}

	
	public void updatePwd(UserLoginDTO userLoginDTO) throws MightyAppException {
		try{
			MightyUserInfo mightyUserInfo=mightyUserInfoDAO.getUserById(userLoginDTO.getUserId());
				if(mightyUserInfo.getPassword().trim().equalsIgnoreCase(userLoginDTO.getPwd().trim())){
						mightyUserInfo.setPassword(userLoginDTO.getNewPwd());
						mightyUserInfoDAO.save(mightyUserInfo);
				}else{
						throw new MightyAppException("Invalid password", HttpStatus.EXPECTATION_FAILED);
				}
		}catch(Exception e){
			throw new MightyAppException("Invalid password", HttpStatus.EXPECTATION_FAILED);
		}
		
	}

	public MightyUserInfo getUserByEmail(String email) throws MightyAppException {
		return mightyUserInfoDAO.getUserByEmail(email);
	}

	
	public MightyUserInfo setGeneratedPwd(MightyUserInfo mightyUserInfo) throws MightyAppException {
		return mightyUserInfoDAO.save(mightyUserInfo);
	}

	
	public String getPasswordResetMessage(MightyUserInfo mightyUser) throws MightyAppException {
		return "Hi "
				+mightyUser.getUserName()				
				+",<br/><br/>Your Password has been reset. To access Mighty App use the below information.<br/><br/> "
				+ ".<br/><br/>"
				+"<br/><br/>Login Id - "+mightyUser.getUserName()
				+"<br/><br/>Password - "+mightyUser.getPassword()
				+"<br/><br/>Regards,<br/>" 
				+ "<a href='https://bemighty.com/'>https://bemighty.com/</a>\n"
						+" Mighty Team."
						+"</a>"+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	
		
	}

	
	public void changePwd(UserLoginDTO userLoginDTO) throws MightyAppException {
		MightyUserInfo mightyUserInfo=mightyUserInfoDAO.getUserByName(userLoginDTO.getUserName());
		mightyUserInfo.setPassword(userLoginDTO.getNewPwd());
		mightyUserInfo.setPwdChangedDate((Date)userLoginDTO.getPwdChangedDate());
		mightyUserInfoDAO.save(mightyUserInfo);
	}

	
}