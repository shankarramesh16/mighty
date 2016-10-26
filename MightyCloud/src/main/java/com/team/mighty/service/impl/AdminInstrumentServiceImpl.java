package com.team.mighty.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

	
	public List<MightyDeviceFirmware> getDeviceFirmware() throws Exception {
		
		return mightyDeviceFirmwareDAO.getDeviceFirmware();
	}

	
	public List<MightyFeaturedPlaylist> getMightyFeaturedPlaylist()	throws Exception {
		return mightyFeaturedPlaylistDAO.getMightyFeaturedPlaylist();
	}

	
	public void insertMightyFeaturedPlaylistDetails(MightyFeaturedPlaylist mightyFeaturedPlaylist) throws Exception {
		mightyFeaturedPlaylistDAO.save(mightyFeaturedPlaylist);
		
	}

}
