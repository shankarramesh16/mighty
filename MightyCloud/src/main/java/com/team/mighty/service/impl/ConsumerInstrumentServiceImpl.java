package com.team.mighty.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
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
import com.team.mighty.dao.MightyOTADeviceDao;
import com.team.mighty.dao.MightySpotifyDao;
import com.team.mighty.dao.MightyUploadDao;
import com.team.mighty.dao.MightyUserInfoDao;
import com.team.mighty.dao.MightydlauditlogDao;
import com.team.mighty.dao.MightylogDao;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyKeyConfig;
import com.team.mighty.domain.MightySpotify;
import com.team.mighty.domain.MightyUpload;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.domain.Mightydlauditlog;
import com.team.mighty.domain.Mightylog;
import com.team.mighty.domain.Mightyotadevice;
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
 * @author Vikky
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
	private MightyOTADeviceDao mightyOTADeviceDao;
	
	@Autowired
	private MightyUploadDao mightyUploadDao;
	
	@Autowired
	private MightydlauditlogDao mightydlauditlogDao;
	
	@Autowired
	private MightyUserInfoDao mightyUserInfoDAO;
	
	@Autowired
	private MightyKeyConfigDAO mightyKeyConfigDAO;
	
	@Autowired
	private MightyDeviceOrderDAO mightyDeviceOrderDAO;
	
	
	@Autowired
	private MightylogDao mightylogDao;
	
	@Autowired
	private MightySpotifyDao mightySpotifyDao;
	
	
	
	@Transactional
	public UserLoginDTO userLogin(UserLoginDTO userLoginDTO) {
		if(userLoginDTO == null) {
			throw new MightyAppException("Invalid Request, User Login Request is empty", HttpStatus.NO_CONTENT);
		}
		
		if(userLoginDTO.getUserId() <=0 ){
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
			/*if(mightDeviceUser.getRegistrationStatus().equalsIgnoreCase(MightyAppConstants.IND_N)){
				throw new MightyAppException(" User Registerd Device Status is In Active ", HttpStatus.UNAUTHORIZED);
			}*/
			
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
			
			/*long ttlMillis=TimeUnit.HOURS.toMillis(2);
			long ttlBaseMillis=TimeUnit.DAYS.toMillis(60);*/
			long ttlMillis=TimeUnit.DAYS.toMillis(150);
			long ttlBaseMillis=TimeUnit.DAYS.toMillis(240);
			
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
		MightyUserInfo mightyUserInfo=null;
		List<MightyUserInfo> mightyUsers = null;
			mightyUsers=getUserByNameAndEmailWithIndicator(consumerDeviceDto.getUserName(),consumerDeviceDto.getEmailId(),consumerDeviceDto.getUserIndicator());
		
		if(mightyUsers!=null && !mightyUsers.isEmpty()){
				mightyUserInfo=mightyUsers.get(0);
					if(mightyUserInfo.getEmailId().equalsIgnoreCase(consumerDeviceDto.getEmailId())){
						throw new MightyAppException(" Email address is already registered", HttpStatus.CONFLICT);
					}else if(mightyUserInfo.getUserName().equalsIgnoreCase(consumerDeviceDto.getUserName())){
						throw new MightyAppException(" Username is already registered", HttpStatus.CONFLICT);
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
		} catch(Exception e){
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
		//validateFacebookToken(consumerDeviceDto.getPassword());
		MightyUserInfo mightyUserInfo = null;
		
		List<MightyUserInfo> mightyUsers = null;
		if(consumerDeviceDto.getEmailId().equalsIgnoreCase("NoEmail")){
			mightyUsers=getUserByUserFBAndEmailAndUsrWithIndicator(consumerDeviceDto.getFacebookID(),consumerDeviceDto.getUserName(),consumerDeviceDto.getUserIndicator());
		}else{
			mightyUsers=getUserByUserFBAndEmailWithIndicator(consumerDeviceDto.getFacebookID(),consumerDeviceDto.getEmailId(),consumerDeviceDto.getUserIndicator());
		}		
		
		if(mightyUsers!=null && !mightyUsers.isEmpty()) {
			return mightyUsers.get(0);
			// Check any de-activated account
			/*logger.debug("IN If -before to check active/deactive");
			MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount1(mightyUserInfo.getId());
			if(mightyDeviceUserMapping != null && mightyDeviceUserMapping.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
					logger.info(" Already Disbaled account is there and activating that one ------- ");
					mightyDeviceUserMapping.setRegistrationStatus(MightyAppConstants.IND_Y);
					mightyDeviceUserMapDAO.save(mightyDeviceUserMapping);
					mightyUserInfo=mightyDeviceUserMapping.getMightyUserInfo();
					return mightyUserInfo;
			} else{
				return mightyUserInfo;
			}*/
			 
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
			mightyUserInfo.setUserFBId(consumerDeviceDto.getFacebookID());
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

	private List<MightyUserInfo> getUserByUserFBAndEmailAndUsrWithIndicator(String facebookID, String userName, String userIndicator) {
		
		return mightyUserInfoDAO.getUserByUserFBAndEmailAndUsrWithIndicator(facebookID,userName,userIndicator);
	}

	private List<MightyUserInfo> getUserByUserFBAndEmailWithIndicator(String facebookID, String emailId, String userIndicator) {
		return mightyUserInfoDAO.getUserByUserFBAndEmailWithIndicator(facebookID,emailId,userIndicator);	
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

	private List<MightyUserInfo> getUserByNameAndEmailWithIndicator(String userName, String emailId, String userIndicator) {
		
		return mightyUserInfoDAO.getUserByNameAndEmailWithIndicator(userName,emailId,userIndicator);
	}

	private UserDeviceRegistrationDTO constructResponse(MightyUserInfo mightyUserInfo) {
		UserDeviceRegistrationDTO userDeviceRegistrationDTO = new UserDeviceRegistrationDTO();
		userDeviceRegistrationDTO.setUserId(mightyUserInfo.getId());
		userDeviceRegistrationDTO.setUserName(mightyUserInfo.getUserName());
		userDeviceRegistrationDTO.setStatus(mightyUserInfo.getUserStatus());
		userDeviceRegistrationDTO.setEmail(mightyUserInfo.getEmailId());
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

	@Transactional
	public void deregisterDevice(String deviceId) {
		
		MightyDeviceInfo mightDeviceInfo = getDeviceDetails(deviceId);
		
		if(mightDeviceInfo == null ) {
			throw new MightyAppException("Device not exist", HttpStatus.NOT_FOUND);
		}
		
			
		MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.getDeviceInfo(mightDeviceInfo.getId());
		
		if(mightyDeviceUserMapping != null) {
			logger.debug("Inside",mightyDeviceUserMapping.getMightyUserInfo().getId());
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

	
	public void validateDevice(String deviceId, String swVersion) throws MightyAppException{
		logger.info(" === ConsumerInstrumentServiceImpl, ValidateDevice, Device Id ", deviceId);
		if(null == deviceId ||  "".equalsIgnoreCase(deviceId)){
			throw new MightyAppException(" Device ID or Input is empty", HttpStatus.BAD_REQUEST);
		}
				
						
		MightyDeviceInfo mightyDeviceInfo = null;
		try{
			logger.debug("deviceId:",deviceId);
			mightyDeviceInfo = mightyDeviceInfoDAO.getDeviceInfo(deviceId);
		}catch(Exception e){
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
					
				logger.debug("78678611",swVersion);
		if(mightyDeviceInfo!=null){
			String isActive = mightyDeviceInfo.getIsActive();
			String isRegistered = mightyDeviceInfo.getIsRegistered();
				if(!mightyDeviceInfo.getSwVersion().equalsIgnoreCase(swVersion) && !swVersion.contains("UPDATING")){
					mightyDeviceInfo.setSwVersion(swVersion.trim());
					mightyDeviceInfoDAO.save(mightyDeviceInfo);
				}	
			
			
			
			if(null == isActive || isActive.equalsIgnoreCase(MightyAppConstants.IND_N) ) {
				throw new MightyAppException(" Device is not active or status is empty", HttpStatus.GONE);
			}
			
			
			
					
			if(null != isRegistered && isRegistered.equalsIgnoreCase(MightyAppConstants.IND_Y)) {
				throw new MightyAppException(" Deivce already registered ", HttpStatus.CONFLICT);
			}
		}
		
		
	}
	
	
	/*@Transactional
	public String validateDevice(String deviceId) throws MightyAppException {
		logger.info(" === ConsumerInstrumentServiceImpl, ValidateDevice, Device Id ", deviceId);
		if(null == deviceId ||  "".equalsIgnoreCase(deviceId)) {
			throw new MightyAppException(" Device ID or Input is empty", HttpStatus.BAD_REQUEST);
		}
		
		
						
		MightyDeviceInfo mightyDeviceInfo = null;
		try {
			logger.debug("deviceId:",deviceId);
			mightyDeviceInfo = mightyDeviceInfoDAO.getDeviceInfo(deviceId);
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(null == mightyDeviceInfo) {
			throw new MightyAppException(" Device Details not found", HttpStatus.NOT_FOUND);
		}
		
				
		if(mightyDeviceInfo!=null){
			String isActive = mightyDeviceInfo.getIsActive();
			String isRegistered = mightyDeviceInfo.getIsRegistered();
			if(null == isActive || isActive.equalsIgnoreCase(MightyAppConstants.IND_N) ) {
				throw new MightyAppException(" Device is not active or status is empty", HttpStatus.GONE);
			}
			
					
			if(null != isRegistered && isRegistered.equalsIgnoreCase(MightyAppConstants.IND_Y)) {
				//throw new MightyAppException(" Deivce already registered ", HttpStatus.CONFLICT);
				return "409";
			}
		}
		
		return "";
	}*/

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
			mightyUserInfo = mightyUserInfoDAO.getMightyUserLogin(consumerDeviceDTO.getPassword(),consumerDeviceDTO.getUserName(),consumerDeviceDTO.getUserIndicator());
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(null == mightyUserInfo) {
			throw new MightyAppException(" Invalid Username or Password", HttpStatus.NOT_FOUND);
		}
		return mightyUserInfo;
	}

	/*@Transactional
	public String registerMightyDevice(DeviceInfoDTO deviceInfoDTO) throws MightyAppException {
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
		
		 String val= validateDevice(deviceInfoDTO.getDeviceId());
		 if(val.equalsIgnoreCase("409")){
			 logger.debug("Debugging val",val);
			 return val;
		 }
		registerMightyWithUser(deviceInfoDTO);
		
		return "";
				
	}*/
	
	
	public void registerMightyDevice(DeviceInfoDTO deviceInfoDTO) throws MightyAppException {
		if(null == deviceInfoDTO) {
			logger.debug("Register Device, Consumer Device DTO object is null");
			throw new MightyAppException("Invalid request Object", HttpStatus.BAD_REQUEST);
		}
		
		 /*Validation on Mighty Device*/
		 validateDevice(deviceInfoDTO.getDeviceId(),deviceInfoDTO.getSwVersion());
		 
		 /*Registration process*/
		 registerMightyWithUser(deviceInfoDTO);
		
		
				
	}

	@Transactional
	private MightyDeviceUserMapping registerMightyWithUser(DeviceInfoDTO deviceInfoDTO) throws MightyAppException {
		MightyUserInfo mightyUserInfo = null;
					   mightyUserInfo=getUserById(deviceInfoDTO.getUserId());
		
		if(mightyUserInfo!=null) {
			List<MightyDeviceUserMapping> mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(mightyUserInfo.getId());
			if(mightyDeviceUserMapping!= null && !mightyDeviceUserMapping.isEmpty()){
				int val=0;
				logger.debug("Size of list",mightyDeviceUserMapping.size());
				for(MightyDeviceUserMapping m : mightyDeviceUserMapping){
						MightyDeviceInfo mightyDeviceInfo=mightyDeviceInfoDAO.getMightyDeviceOnId(m.getMightyDeviceId());
							if(mightyDeviceInfo!=null && mightyDeviceInfo.getDeviceId().equalsIgnoreCase(deviceInfoDTO.getDeviceId())){
								logger.debug("In existing device registration");							
									if(mightyDeviceInfo.getIsRegistered().equals(MightyAppConstants.IND_N)){
										logger.info(" Device is de-registered and registering that one ------- ");	
										    mightyDeviceInfo.setIsRegistered(MightyAppConstants.IND_Y);
											mightyDeviceInfoDAO.save(mightyDeviceInfo);	
									}
									
									if(m.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
										logger.info(" Already Disbaled account is there and activating that one ------- ");
											m.setRegistrationStatus(MightyAppConstants.IND_Y);
											mightyDeviceUserMapDAO.save(m);
									}
							}
							else
							{ 
											logger.debug("In new device registration into exiting user");
											MightyDeviceInfo mighty=mightyDeviceInfoDAO.getDeviceInfo(deviceInfoDTO.getDeviceId());
											
								   if(mighty!=null){
																				
									   			logger.debug("987651");
									   			
												if(mighty.getIsRegistered().equalsIgnoreCase(MightyAppConstants.IND_N)){
													logger.debug("22222");
													mighty.setIsRegistered(MightyAppConstants.IND_Y);
													mightyDeviceInfoDAO.save(mighty);
												}
												
												logger.debug("loggerdevice1",mighty.getId());	
											List<MightyDeviceUserMapping>	mList=mightyDeviceUserMapDAO.getDeviceInfoList(mighty.getId(),mightyUserInfo.getId());
											logger.debug("loggerdevice1list",mList);
											
											if(mList!=null && !mList.isEmpty()){
												logger.debug("loggerdevice1list",mList.size());
												for(MightyDeviceUserMapping md: mList) {
													if(md.getMightyDeviceId()==m.getMightyDeviceId()){
														if(md.getRegistrationStatus().equals(MightyAppConstants.IND_N)){
															m.setRegistrationStatus(MightyAppConstants.IND_Y);
															return mightyDeviceUserMapDAO.save(m);
															
														}
													
												}
											 }
											}else{
												logger.debug("786786");
													if(m.getMightyDeviceId()==0){
														logger.debug("1786786");
														m.setMightyDeviceId(mighty.getId());
														mightyDeviceUserMapDAO.save(m);
													}else if(m.getMightyDeviceId()!=mighty.getId()){
														logger.debug("2786786");
														if(val==0){
															logger.debug("3786786");
												MightyDeviceUserMapping mdum1=new MightyDeviceUserMapping();
												mdum1.setMightyDeviceId(mighty.getId());
												mdum1.setMightyUserInfo(mightyUserInfo);
												mdum1.setPhoneDeviceId(m.getPhoneDeviceId());
												mdum1.setPhoneDeviceOSVersion(m.getPhoneDeviceOSVersion());
												mdum1.setPhoneDeviceType(m.getPhoneDeviceType());
												mdum1.setPhoneDeviceVersion(m.getPhoneDeviceVersion());
												mdum1.setRegistrationStatus(MightyAppConstants.IND_Y);
												mdum1.setCreatedDt(new Date(System.currentTimeMillis()));
												mdum1.setUpdatedDt(new Date(System.currentTimeMillis()));
													MightyDeviceUserMapping deviceRegistered=mightyDeviceUserMapDAO.save(mdum1);
															 if(deviceRegistered!=null){
																	 logger.debug("INsert into M:M");
																	 	Set<MightyUserInfo> setUserInfo = new HashSet<MightyUserInfo>();
																		Set<MightyDeviceUserMapping> setMightyUserDevice = mightyUserInfo.getMightyDeviceUserMapping();
																		if(setMightyUserDevice == null || mightyUserInfo.getMightyDeviceUserMapping().isEmpty()) {
																			setMightyUserDevice = new HashSet<MightyDeviceUserMapping>();
																		}
																		setMightyUserDevice.add(deviceRegistered);
																		mightyUserInfo.setMightyDeviceUserMapping(setMightyUserDevice);
																		
																		setUserInfo.add(mightyUserInfo);
																		consumerInstrumentDAO.save(mightyUserInfo);
															 }
															 
															val=val+1; 
											}
												}			
										  }	
										}
								   else
								   {
										MightyDeviceInfo mightyDeviceInfo1=new MightyDeviceInfo();
										mightyDeviceInfo1.setDeviceId(deviceInfoDTO.getDeviceId());
										mightyDeviceInfo1.setDeviceName(deviceInfoDTO.getDeviceName());
										mightyDeviceInfo1.setDeviceType(deviceInfoDTO.getDeviceType());
										mightyDeviceInfo1.setSwVersion(deviceInfoDTO.getSwVersion());
										mightyDeviceInfo1.setIsActive(deviceInfoDTO.getIsActive());
										mightyDeviceInfo1.setIsRegistered(deviceInfoDTO.getIsRegistered());
										mightyDeviceInfo1.setAppVersion(Float.valueOf(deviceInfoDTO.getAppVersion()));
										mightyDeviceInfo1.setAppBuild(deviceInfoDTO.getAppBuild());
										mightyDeviceInfo1.setRegisterAt(deviceInfoDTO.getRegisterAt());
										mightyDeviceInfo1.setUpgradeAt(deviceInfoDTO.getUpgradedAt());
										//mightyDeviceInfo.setDeviceOrderInfo(mightyDeviceOrderInfo);
										MightyDeviceInfo mightyDevice=null;
									try{
										mightyDevice=mightyDeviceInfoDAO.save(mightyDeviceInfo1);
									}catch(Exception e){
										logger.error(e.getMessage());
										throw new MightyAppException("Unable to save User Mighty Device Info ", HttpStatus.INTERNAL_SERVER_ERROR);
									}
									
									if(m.getMightyDeviceId()==0){
										m.setMightyDeviceId(mightyDevice.getId());
										mightyDeviceUserMapDAO.save(m);
									}else if(m.getMightyDeviceId()!=mightyDevice.getId()){
										logger.debug("1111111 Inside new mighty Registered");
										MightyDeviceUserMapping mdum=new MightyDeviceUserMapping();
													mdum.setMightyDeviceId(mightyDevice.getId());
													mdum.setMightyUserInfo(mightyUserInfo);
													mdum.setPhoneDeviceId(m.getPhoneDeviceId());
													mdum.setPhoneDeviceOSVersion(m.getPhoneDeviceOSVersion());
													mdum.setPhoneDeviceType(m.getPhoneDeviceType());
													mdum.setPhoneDeviceVersion(m.getPhoneDeviceVersion());
													mdum.setRegistrationStatus(MightyAppConstants.IND_Y);
													mdum.setCreatedDt(new Date(System.currentTimeMillis()));
													mdum.setUpdatedDt(new Date(System.currentTimeMillis()));
														MightyDeviceUserMapping deviceRegistered=mightyDeviceUserMapDAO.save(mdum);
																 if(deviceRegistered!=null){
																		 logger.debug("INsert into M:M");
																		 	Set<MightyUserInfo> setUserInfo = new HashSet<MightyUserInfo>();
																			Set<MightyDeviceUserMapping> setMightyUserDevice = mightyUserInfo.getMightyDeviceUserMapping();
																			if(setMightyUserDevice == null || mightyUserInfo.getMightyDeviceUserMapping().isEmpty()) {
																				setMightyUserDevice = new HashSet<MightyDeviceUserMapping>();
																			}
																			setMightyUserDevice.add(deviceRegistered);
																			mightyUserInfo.setMightyDeviceUserMapping(setMightyUserDevice);
																			
																			setUserInfo.add(mightyUserInfo);
																			consumerInstrumentDAO.save(mightyUserInfo);
																 }
									 }
								 }
																
								
								} /*else loop or new device into existing user ended*/
						}/* loop ended*/	
				}
		  }else{
				throw new MightyAppException("Invalid request Parameters [UserId] ", HttpStatus.NOT_FOUND);
		  }
		return null;
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

	@Transactional
	public UserLoginDTO getRefreshTokenOnBaseToken() throws MightyAppException {
				
		MightyKeyConfig mightyKeyConfig = mightyKeyConfigDAO.getKeyConfigValue(MightyAppConstants.KEY_MIGHTY_MOBILE);
		UserLoginDTO userLoginDTO=null;
		
		if(null != mightyKeyConfig && (mightyKeyConfig.getIsEnabled() != null && 
				mightyKeyConfig.getIsEnabled().equalsIgnoreCase(MightyAppConstants.IND_Y))) {
			
			userLoginDTO=new UserLoginDTO();
			userLoginDTO.setStatusCode(HttpStatus.OK.toString());
			
			//long ttlMillis = Long.parseLong(SpringPropertiesUtil.getProperty(MightyAppConstants.TTL_LOGIN_KEY));
			/*long ttlMillis=TimeUnit.HOURS.toMillis(2);
			long ttlBaseMillis=TimeUnit.DAYS.toMillis(60);*/
			long ttlMillis=TimeUnit.DAYS.toMillis(150);
			long ttlBaseMillis=TimeUnit.DAYS.toMillis(240);
			//long ttlMillis=TimeUnit.MINUTES.toMillis(1);
					
			logger.debug("ttlMillisVal",ttlMillis);
						
			UserLoginDTO newAccessToken = JWTKeyGenerator.createJWTAccessToken(mightyKeyConfig.getMightyKeyValue(), MightyAppConstants.TOKEN_LOGN_ID,
					MightyAppConstants.SUBJECT_SECURE, ttlMillis);
			
			UserLoginDTO newBaseToken = JWTKeyGenerator.createJWTBaseToken(mightyKeyConfig.getMightyKeyValue(), MightyAppConstants.TOKEN_LOGN_ID,
					MightyAppConstants.SUBJECT_SECURE, ttlBaseMillis);
						
			userLoginDTO.setApiToken(newAccessToken.getApiToken());
			userLoginDTO.setAccessTokenExpDate(newAccessToken.getAccessTokenExpDate());
			
			userLoginDTO.setBaseToken(newBaseToken.getBaseToken());
			userLoginDTO.setBaseTokenExpDate(newBaseToken.getBaseTokenExpDate());
						
		}
		
		return userLoginDTO;
	}

	@Transactional
	public void updatePwd(UserLoginDTO userLoginDTO) throws MightyAppException {
		try{
			MightyUserInfo mightyUserInfo=mightyUserInfoDAO.getUserById(userLoginDTO.getUserId());
				if(mightyUserInfo.getPassword().trim().equalsIgnoreCase(userLoginDTO.getPwd().trim())){
						mightyUserInfo.setPassword(userLoginDTO.getNewPwd());
						mightyUserInfo.setPwdChangedDate(new Date(System.currentTimeMillis()));
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
		return "Heyo "
				+mightyUser.getUserName().toLowerCase()	
				+",<br/><br/>I heard that you forgot your Mighty account password. Fear not, we're here to help."
				+"<br/><br/>I've generated a new password for you below. It has some crazy characters, so I recommend that you change your password after you log back into the Mighty mobile app. To change your password, navigate to the User tab and click the Change Password link.<br/><br/> "
				+"Username - "+mightyUser.getUserName().toLowerCase()
				+"<br/>Password - "+mightyUser.getPassword()
				+"<br/><br/>Much love,"
				+"<br/>The Mighty Robot"
				+"<br/><br/><em>I'm a robot and my owners won't let me receive inbound messages. If you have any questions, please send my owners an email at heyo@bemighty.com.</em>"; 
				
		
	}
	
	public String getUserAccountMessage(UserDeviceRegistrationDTO dto) throws MightyAppException {
		return "Heyo "
				+dto.getUserName().toLowerCase()	
				+",<br/><br/>I heard that you just created a Mighty account. You're just a few steps away from taking your favorite playlists on-the-go, no phone or connection required. Super exciting."
				+"<br/><br/>You'll remain logged into your Mighty account unless you login with a different account or uninstall the app. If you happen to get logged out and can't remember your password, click the Forgot Password link on the Mighty login screen to reset it."
				+"<br/><br/>If you have any questions about anything related to Mighty, please check out our Help Center. You can also email the Mighty team at heyo@bemighty.com"
				+"<br/><br/>Much love,"
				+"<br/>The Mighty Robot"
				+"<br/><br/><em>I'm a robot and my owners won't let me receive inbound messages. If you have any questions, please send my owners an email at heyo@bemighty.com.</em>"; 
				
		
	}

	
	public void changePwd(UserLoginDTO userLoginDTO) throws MightyAppException {
		MightyUserInfo mightyUserInfo=mightyUserInfoDAO.getUserByName(userLoginDTO.getUserName());
		mightyUserInfo.setPassword(userLoginDTO.getNewPwd());
		mightyUserInfo.setPwdChangedDate((Date)userLoginDTO.getPwdChangedDate());
		mightyUserInfoDAO.save(mightyUserInfo);
	}

	
	public List<MightyUserInfo> getSearchUsers(String searchStr) throws Exception {
			return mightyUserInfoDAO.getSearchUsers(searchStr);
	}

	
	public List<MightyDeviceInfo> getMightySearchDevice(String searchDev) throws Exception {
		   return mightyDeviceInfoDAO.getMightySearchDevice(searchDev);
	}

	
	public List<MightyUserInfo> getAllMightyUsers() throws Exception {
		return mightyUserInfoDAO.getAllMightyUsers();
	}

	
	public List<MightyDeviceInfo> getAllMightyDev() throws Exception {
		return mightyDeviceInfoDAO.getAllMightyDev();
	}

	
	public List<MightyDeviceUserMapping> getMightyDeviceUserMappingOndevId(long devId) throws Exception {
		return mightyDeviceUserMapDAO.getMightyDeviceUserMappingOndevId(devId);
		 
	}

	
	public MightyUserInfo getMightyUserById(long userId) throws Exception {
		
		return mightyUserInfoDAO.getMightyUserById(userId);
	}

	
	public MightyUserInfo updateUserEmail(MightyUserInfo user) throws Exception {
		return mightyUserInfoDAO.save(user);
		
	}

	
	public MightyDeviceInfo getMightyOnHwId(String deviceId) throws MightyAppException {
		return mightyDeviceInfoDAO.getDeviceInfo(deviceId);
	}

	
	public Mightylog updateMightyLogs(Mightylog logs) throws Exception {
		return mightylogDao.save(logs);	
	}


	public Set<String> getMightyLogs() throws MightyAppException {
		return mightylogDao.getMightyLogs();
	}

	
	public List<Mightylog> getMightyLogsOndevId(String devId) throws MightyAppException {
		return mightylogDao.getMightyLogsOndevId(devId);
	}

	
	public List<Mightylog> getExistingMightylog(String deviceId, String username) throws MightyAppException {
		return mightylogDao.getExistingMightylog(deviceId,username);
	}

	
	public String getMightyLogsMsg(Mightylog logs) throws Exception {
		String ticket="";
		 Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String s = formatter.format(logs.getUpdatedDt())+".0";
		
		String link="https://mighty2.cloudaccess.host/test1/downloadMightyLogs/?devId="+logs.getDeviceId()+"&dat="+URLEncoder.encode(s, "UTF-8")+"&usrId="+URLEncoder.encode(logs.getUsername(), "UTF-8");
		//String link="http://192.168.1.107:8088/MightyCloud/getLogs/?devId="+logs.getDeviceId()+"&usrId="+logs.getUsername()+"&dat="+s;
			logger.debug("link as",link);
		if(logs.getTicket()!=null && !logs.getTicket().isEmpty()){
			ticket="<br/>Ticket Number:"+logs.getTicket();
		}
		return "Username:"+logs.getUsername()
				+"<br/>Email:"+logs.getEmailId()
				+"<br/>Mighty device:"+logs.getDeviceId()
				+"<br/>Category:"+logs.getLogType()
				+ticket
				+"<br/>Description:"+logs.getDescription()
				+"<br/><a href="+link+">Download Mightylog</a>";
				//+"<br/>Download Log URL:  "+link;
				
				}

	
	public List<MightyUserInfo> getUserByUserName(String username) throws MightyAppException {
		return mightyUserInfoDAO.getUserByUserName(username);
	}

	
	public List<MightyDeviceUserMapping> getMightyUserDeviceMappingByUserId(long id) throws MightyAppException {
		return mightyDeviceUserMapDAO.checkAnyDeActivatedAccount(id);
	}

	
	public MightyDeviceInfo getMightyDeviceInfoOnMappingDevice(long mightyDeviceId) throws MightyAppException {
		
		return mightyDeviceInfoDAO.getMightyDeviceOnId(mightyDeviceId);
	}

	
	public MightyUpload updateMightyUpload(MightyUpload mu) throws MightyAppException {
		return mightyUploadDao.save(mu);
	}

	
	public Mightydlauditlog updateMightydlauditlog(Mightydlauditlog mlog) throws MightyAppException {
		return mightydlauditlogDao.save(mlog);
	}


	public List<Mightydlauditlog> getMightyDlAuditLog() throws MightyAppException {
		return mightydlauditlogDao.getMightyDlAuditLog();
	}

	
	public List<Mightyotadevice> getExcelUploadMightyInfo() throws MightyAppException {
			return mightyOTADeviceDao.getExcelUploadMightyInfo();
	}


	public Set<String> getMightyToCloudLogs() throws MightyAppException {
			return mightyUploadDao.getMightyToCloudLogs();
	}

	
	public List<MightyUpload> getMightyToCloudLogsOndevId(String devId) throws MightyAppException {
		return mightyUploadDao.getMightyToCloudLogsOndevId(devId);
	}

	
	public MightyUpload getMightyUploadByDevId(String deviceId) throws MightyAppException {
		return mightyUploadDao.getMightyUploadByDevId(deviceId);
	}

	
	public MightyDeviceInfo getDeviceOnDeviceId(String devId) throws MightyAppException {
		return mightyDeviceInfoDAO.getDeviceInfo(devId);
	}


	public List<MightySpotify> getMightySpotifyDetails(String deviceId) throws MightyAppException {
		 return mightySpotifyDao.getMightySpotifyDetails(deviceId);
	}

	
	public void updateMightySpotify(MightySpotify ms) throws MightyAppException {
		mightySpotifyDao.save(ms);		
	}

	
	public void validateDevice(String deviceId) throws MightyAppException {
				
	}

	
	public List<Object[]> getMightyUserDetails() throws MightyAppException {
			return mightyUserInfoDAO.getMightyUserDetails();
	}


	

	
}