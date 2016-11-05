package com.team.mighty.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dao.ConsumerInstrumentDAO;
import com.team.mighty.dao.MightyDeviceInfoDAO;
import com.team.mighty.dao.MightyDeviceUserMapDAO;
import com.team.mighty.dao.MightyUserInfoDao;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.dto.UserDeviceRegistrationDTO;
import com.team.mighty.dto.UserLoginDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.ConsumerInstrumentService;

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
	
	
	

	public UserLoginDTO userLogin(UserLoginDTO userLoginDTO) {
		if(userLoginDTO == null) {
			throw new MightyAppException("Invalid Request, User Login Request is empty", HttpStatus.BAD_REQUEST);
		}
		
		if(userLoginDTO.getUserId() <=0 || (userLoginDTO.getDeviceId().equals(null) || "".equals(userLoginDTO.getDeviceId()))) {
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
		
		return userLoginDTO;
	}

	@Transactional
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
		}
		
		// Check any de-activated device registered
		MightyDeviceUserMapping mightyDeviceUserMapping = mightyDeviceUserMapDAO.checkAnyDeActivatedAccountByUserName(consumerDeviceDto.getUserName(), mightyDeviceInfo.getId(), phoneDeviceId);
		
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
			mightyUserInfo.setUserIndicator(consumerDeviceDto.getUserIndicator());
			mightyUserInfo.setCreatedDt(new Date(System.currentTimeMillis()));
			mightyUserInfo.setUpdatedDt(new Date(System.currentTimeMillis()));
			
		}
		
		mightyDeviceUserMapping = new MightyDeviceUserMapping();
		mightyDeviceUserMapping.setMightyDeviceId(mightyDeviceInfo.getId());
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
		
		mightyDeviceInfo.setIsRegistered(MightyAppConstants.IND_Y);
		//mightyDeviceInfo.setMightyUserInfo(setUserInfo);
		
		MightyUserInfo mightyUserInfo_1 = null;
		try {
			mightyUserInfo_1 = consumerInstrumentDAO.save(mightyUserInfo);
			mightyDeviceInfoDAO.save(mightyDeviceInfo);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new MightyAppException("Unable to save User Device Mapping", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		logger.info(" Mighty USer ID ",mightyUserInfo_1.getId());
		
		return mightyUserInfo_1;
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
				|| (null == consumerDeviceDto.getDeviceId() || "".equals(consumerDeviceDto.getDeviceId()))
				|| (null == consumerDeviceDto.getMightyDeviceId() || "".equals(consumerDeviceDto.getMightyDeviceId()))) {
			logger.debug("Register Device, Anyone of the object is empty [UserName, DeviceId, MightyDeviceId] ", consumerDeviceDto.getUserName(), 
					",",consumerDeviceDto.getDeviceId(), ",",consumerDeviceDto.getMightyDeviceId() );
			throw new MightyAppException("Invalid request Parameters [UserName or Device Id or Mighty Device Id] ", HttpStatus.BAD_REQUEST);
		}
		
		validateDevice(consumerDeviceDto.getMightyDeviceId());
		
		MightyDeviceInfo mightDeviceInfo = getDeviceDetails(consumerDeviceDto.getMightyDeviceId());
		
		MightyUserInfo mightyUserInfo =  registerUserAndDevice(consumerDeviceDto, mightDeviceInfo);
		
		return constructResponse(mightyUserInfo);
		
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
		
		MightyDeviceInfo mightyDeviceInfo = null;
		try {
			mightyDeviceInfo = mightyDeviceInfoDAO.getDeviceInfo(deviceId);
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(null == mightyDeviceInfo) {
			throw new MightyAppException(" Device Details not found", HttpStatus.NOT_FOUND);
		}
		
		String isActive = mightyDeviceInfo.getIsActive();
		
		if(null == isActive || isActive.equalsIgnoreCase(MightyAppConstants.IND_N)) {
			throw new MightyAppException(" Device is not active or status is empty", HttpStatus.GONE);
		}
		
		String isRegistered = mightyDeviceInfo.getIsRegistered();
		
		if(null != isRegistered && isRegistered.equalsIgnoreCase(MightyAppConstants.IND_Y)) {
			throw new MightyAppException(" Deivce already registered ", HttpStatus.CONFLICT);
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

	
	public void mightyUserLogin(ConsumerDeviceDTO consumerDeviceDTO) throws MightyAppException {
	
		MightyUserInfo mightyUserInfo = null;
		try {
			mightyUserInfo = mightyUserInfoDAO.getMightyUserLogin(consumerDeviceDTO.getPassword(),consumerDeviceDTO.getUserName());
		} catch(Exception e) {
			throw new MightyAppException("System Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		if(null == mightyUserInfo) {
			throw new MightyAppException(" Invalid Username or Password", HttpStatus.NOT_FOUND);
		}
	}

}