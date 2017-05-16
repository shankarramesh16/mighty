package com.team.mighty.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dao.MightyDeviceFirmwareDAO;
import com.team.mighty.dao.MightyDeviceInfoDAO;
import com.team.mighty.dao.MightyDeviceOrderDAO;
import com.team.mighty.dao.MightyFeaturedPlaylistDAO;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.domain.MightyFeaturedPlaylist;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;

@Service("adminInstrumentServiceImpl")
public class AdminInstrumentServiceImpl implements AdminInstrumentService {

	private static final MightyLogger logger = MightyLogger.getLogger(AdminInstrumentServiceImpl.class);
	
	@Autowired
	private MightyDeviceInfoDAO mightyDeviceInfoDAO;
	
	@Autowired
	private MightyDeviceOrderDAO mightyDeviceOrderDAO;
	
	@Autowired
	private MightyDeviceFirmwareDAO mightyDeviceFirmwareDAO;
	
	@Autowired
	private MightyFeaturedPlaylistDAO mightyFeaturedPlaylistDAO;
	
	public MightyDeviceFirmware mightyLstDeviceFirmware=null;
	
	public MightyDeviceFirmware getMightyLstDeviceFirmware() {
		return mightyLstDeviceFirmware;
	}

	
	public List<DeviceInfoDTO> getAllMightyDevice() throws MightyAppException {
		logger.info("AdminInstrumentServiceImpl,getAllMightyDevice");
		List<MightyDeviceInfo> lstMightDeviceInfo = new ArrayList<MightyDeviceInfo>();
		List<DeviceInfoDTO> lstDeviceDTO = new ArrayList<DeviceInfoDTO>();
		try {
			lstMightDeviceInfo = mightyDeviceInfoDAO.findAll();
			
			if(lstMightDeviceInfo != null && !lstMightDeviceInfo.isEmpty()) {
				for(MightyDeviceInfo mightyDevice : lstMightDeviceInfo){
					DeviceInfoDTO deviceInfoDTO = new DeviceInfoDTO();
					deviceInfoDTO.setDeviceId(mightyDevice.getDeviceId());
					deviceInfoDTO.setIsActive(mightyDevice.getIsActive());
					deviceInfoDTO.setIsRegistered(mightyDevice.getIsRegistered());
					lstDeviceDTO.add(deviceInfoDTO);
				}
			}
			
		} catch(MightyAppException e) {
			throw new MightyAppException("Unable to retrive device", HttpStatus.INTERNAL_SERVER_ERROR, e); 
		}
		return lstDeviceDTO;
	}

	public MightyDeviceOrderInfo createDeviceOrder(MightyDeviceOrderInfo mightyDeviceOrderInfo)
			throws MightyAppException {
		if(mightyDeviceOrderInfo == null) {
			throw new MightyAppException("Invalid Request, Request is empty", HttpStatus.BAD_REQUEST);
		}
		
		try {
			Assert.notNull(mightyDeviceOrderInfo.getOrderId(), "Invalid Request, Order Id is empty");
			Assert.notNull(mightyDeviceOrderInfo.getAdminUserId(), "Invalid Request, Admin User Id is empty");
			Assert.notNull(mightyDeviceOrderInfo.getNoOfDevice(), "Invalid Request, No of Device is empty");
		} catch(Exception e) {
			throw new MightyAppException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		mightyDeviceOrderInfo.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceOrderDAO.save(mightyDeviceOrderInfo);
		logger.info(" Order Created successfully and Mighty Order Id ", mightyDeviceOrderInfo.getId());
		return mightyDeviceOrderInfo;
	}

	public MightyDeviceFirmware createDeviceFirmware(MightyDeviceFirmware mightyDeviceFirmware)
			throws MightyAppException {
		try {
			Assert.notNull(mightyDeviceFirmware, "Invalid Request, Device Firmware is Empty");
			Assert.notNull(mightyDeviceFirmware.getVersion(),"Invalid Request, Firmware Version is Empty" );
		} catch(Exception e) {
			throw new MightyAppException(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		mightyDeviceFirmware.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceFirmware.setUpdatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceFirmware.setStatus(MightyAppConstants.IND_A);
		
		mightyDeviceFirmware = mightyDeviceFirmwareDAO.save(mightyDeviceFirmware);
		return mightyDeviceFirmware;
	}
	
	public void insertDeviceFirmwareDetails(MightyDeviceFirmware mightyDevFirmware) throws Exception {
		mightyDeviceFirmwareDAO.save(mightyDevFirmware);
	}

	
	public List<Object[]> getDeviceFirmware() throws Exception {
		return mightyDeviceFirmwareDAO.getDeviceFirmware("A");
	}

	
	public List<MightyFeaturedPlaylist> getMightyFeaturedPlaylist()	throws Exception {
		return mightyFeaturedPlaylistDAO.getMightyFeaturedPlaylist();
	}

	
	public void insertMightyFeaturedPlaylistDetails(MightyFeaturedPlaylist mightyFeaturedPlaylist) throws Exception {
		mightyFeaturedPlaylistDAO.save(mightyFeaturedPlaylist);
		
	}

	public MightyDeviceFirmware getDeviceFirmwareById(String deviceFirmwareId) throws MightyAppException {
		return mightyDeviceFirmwareDAO.getDeviceFirmwareById(deviceFirmwareId);
	}


	public MightyDeviceFirmware getMightyDeviceFirmware() throws MightyAppException {
		List<MightyDeviceFirmware> mightyDeviceFirmware=null;
		try {
			mightyDeviceFirmware = mightyDeviceFirmwareDAO.getDeviceFirmwareByStatus("A");
			if(mightyDeviceFirmware!=null && !mightyDeviceFirmware.isEmpty()){
				return mightyDeviceFirmware.get(0);
			}
		} catch (Exception e) {
			throw new MightyAppException(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return null;
	}


	public List<MightyDeviceOrderInfo> getOrderDevices() throws Exception {
		return mightyDeviceOrderDAO.getOrderDevices();
		
	}

	
	public void deleteFirmware(MightyDeviceFirmware mightyDeviceFirmware) throws Exception {
		mightyDeviceFirmwareDAO.delete(mightyDeviceFirmware);
		
	}


	public void saveMightyDeviceOrder(MightyDeviceOrderInfo mightyDeviceOrder)	throws Exception {
		mightyDeviceOrderDAO.save(mightyDeviceOrder);
		
	}
	
	@Transactional
	public synchronized MightyDeviceFirmware getMightyDeviceFirmware(String HWSerialNo,String SWVersion,String AppVersion, String AppBuild) throws MightyAppException{
		MightyDeviceFirmware mightyDeviceFirmware=null;
		
		List<MightyDeviceFirmware> compatibleWithExistRequires=null;
		try {
			MightyDeviceInfo mightyInfo=null; 
			mightyInfo=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNo);
			if(mightyInfo!=null){
				mightyInfo.setSwVersion(SWVersion);
				mightyInfo.setAppVersion(Float.valueOf(AppVersion));
				mightyInfo.setAppBuild(AppBuild);
				MightyDeviceInfo mightyDeviceInfo=mightyDeviceInfoDAO.save(mightyInfo);
				if(mightyDeviceInfo!=null){
						logger.debug("HwSerialNo",mightyDeviceInfo.getDeviceId());
					String compatibleHw=mightyDeviceInfo.getDeviceId().trim().substring(3, 4);
						logger.debug("existingHwCode",compatibleHw);
						logger.debug("appversion",mightyDeviceInfo.getAppVersion());
						logger.debug("swVersion",Float.valueOf(mightyDeviceInfo.getSwVersion()));
					
					List<MightyDeviceFirmware>  mightyLastestDeviceFirmwares= mightyDeviceFirmwareDAO.getDeviceFirmwareByStatusAsc("A");
					mightyLstDeviceFirmware=new  MightyDeviceFirmware();
					if(mightyLastestDeviceFirmwares!=null && !mightyLastestDeviceFirmwares.isEmpty()){
						   for(MightyDeviceFirmware mdf : mightyLastestDeviceFirmwares){
							      mightyLstDeviceFirmware=mdf;   
							   
							  /* if(mdf.getCompatibleHW().contains(compatibleHw)){
								   mightyLstDeviceFirmware=mdf;   
							   }*/
						   }
					   }
					
					
					
					compatibleWithExistRequires=mightyDeviceFirmwareDAO.getFirmwareByCompatible(mightyDeviceInfo.getAppVersion(),Float.valueOf(mightyDeviceInfo.getSwVersion()),"A");
					
					   if(compatibleWithExistRequires!=null && !compatibleWithExistRequires.isEmpty()){
							logger.debug("Size if",compatibleWithExistRequires.size());
								return compatibleWithExistRequires.get(0);
						/*if(compatibleWithExistRequires.get(0).getCompatibleHW().contains(compatibleHw)){
								
						}*/
					   }else {
						   logger.debug("vikkky",mightyDeviceInfo.getAppVersion());
							List<MightyDeviceFirmware> compatibleWithVersion= mightyDeviceFirmwareDAO.getFirmwareByReqVersion(mightyDeviceInfo.getAppVersion(),"A");
							
							if(compatibleWithVersion!=null && !compatibleWithVersion.isEmpty()){
								logger.debug("Size else",compatibleWithVersion.size());
								
								for(MightyDeviceFirmware mdf : compatibleWithVersion){
									
										float firmwareVer=0;
										float requestedVer=0;
										float requiredLatest=0;
												try{
													firmwareVer=Float.parseFloat(mdf.getVersion().trim());
													requestedVer=Float.parseFloat(mightyDeviceInfo.getSwVersion().trim());
													requiredLatest=mdf.getRequires();
												}catch(NullPointerException npe){
													logger.error("Line 193",npe);
												}
									logger.debug("swVersin",firmwareVer);
									logger.debug("latestRequired",requestedVer);
										if(firmwareVer > requestedVer){
											if(requestedVer>=requiredLatest ){
											return mdf;
											}	
										}
									
									
									/*if(mdf.getCompatibleHW().contains(compatibleHw)){
										float firmwareVer=0;
										float requestedVer=0;
										float requiredLatest=0;
												try{
													firmwareVer=Float.parseFloat(mdf.getVersion().trim());
													requestedVer=Float.parseFloat(mightyDeviceInfo.getSwVersion().trim());
													requiredLatest=mdf.getRequires();
												}catch(NullPointerException npe){
													logger.error("Line 193",npe);
												}
									logger.debug("swVersin",firmwareVer);
									logger.debug("latestRequired",requestedVer);
										if(firmwareVer > requestedVer){
											if(requestedVer>=requiredLatest ){
											return mdf;
											}	
										}
									}*/
																
							   }		
						   
					     }	
					  }
					   
					
					logger.debug("objectttt",mightyLstDeviceFirmware);
				}	   
			}else{
				  throw new MightyAppException("HwSerialNo mighty be not exist or not in correct format",HttpStatus.EXPECTATION_FAILED);
				  }
			
			
		} catch (Exception e) {
			throw new MightyAppException(e.getMessage(),HttpStatus.BAD_REQUEST);
			//throw new MightyAppException(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	
	/*public MightyDeviceFirmware getMightyDeviceFirmware(String HWSerialNo){
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try {
			MightyDeviceInfo mightyDeviceInfo=null; 
			List<MightyDeviceFirmware> mightyDeviceFirmwaresByAppComp=null;
			mightyDeviceInfo=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNo);
			
			if(mightyDeviceInfo!=null){
						logger.debug("HwSerialNo",mightyDeviceInfo.getDeviceId());
					String compatibleHw=mightyDeviceInfo.getDeviceId().substring(3, 4);
						logger.debug("existingHwCode",compatibleHw);
						mightyDeviceFirmwaresByAppComp=mightyDeviceFirmwareDAO.getFirmwareByAppComp(mightyDeviceInfo.getAppVersion(),"A");	
						if(mightyDeviceFirmwaresByAppComp!=null){
							for(MightyDeviceFirmware mdf : mightyDeviceFirmwaresByAppComp){
								if(mdf.getCompatibleHW().contains(compatibleHw)){
									mightyDeviceFirmware=mightyDeviceFirmwareDAO.getDeviceFirmwareById(deviceFirmwareId)
								}
							}
						}
				
			}
			
			mightyDeviceFirmware = mightyDeviceFirmwareDAO.getDeviceFirmwareByStatus("A");
			if(mightyDeviceFirmware!=null && !mightyDeviceFirmware.isEmpty()){
				return mightyDeviceFirmware.get(0);
			}
		} catch (Exception e) {
			throw new MightyAppException(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return null;
	}*/

	

}
