package com.team.mighty.service;

import java.util.List;

import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.domain.MightyFeaturedPlaylist;
import com.team.mighty.domain.Mightyotadevice;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.exception.MightyAppException;

public interface AdminInstrumentService {

	public List<DeviceInfoDTO> getAllMightyDevice() throws MightyAppException;
	
	public MightyDeviceOrderInfo createDeviceOrder(MightyDeviceOrderInfo mightyDeviceOrderInfo) throws MightyAppException;
	
	public MightyDeviceFirmware createDeviceFirmware(MightyDeviceFirmware mightyDeviceFirmware) throws MightyAppException;
	
	public void insertDeviceFirmwareDetails(MightyDeviceFirmware mightyDevFirmware) throws Exception;

	public List<Object[]> getDeviceFirmware()throws Exception;

	public List<MightyFeaturedPlaylist> getMightyFeaturedPlaylist()throws Exception;

	public void insertMightyFeaturedPlaylistDetails(MightyFeaturedPlaylist mightyFeaturedPlaylist)throws Exception;

	public MightyDeviceFirmware getDeviceFirmwareById(String deviceFirmwareId) throws MightyAppException;

	public MightyDeviceFirmware getMightyDeviceFirmware()throws MightyAppException;

	public List<MightyDeviceOrderInfo> getOrderDevices()throws Exception;

	public void deleteFirmware(MightyDeviceFirmware mightyDeviceFirmware)throws Exception;

	public void saveMightyDeviceOrder(MightyDeviceOrderInfo mightyDeviceOrder)throws Exception;

	public MightyDeviceFirmware getMightyDeviceFirmware(String HWSerialNo, String SWVersion, String AppVersion, String AppBuild) throws MightyAppException; 
	
	public MightyDeviceFirmware getMightyLstDeviceFirmware();

	public List<MightyDeviceFirmware> getLatestOTA()throws Exception;

	public List<Mightyotadevice> getMightyForOTA(String hWSerialNumber)throws Exception;

	public void saveMightyOtaDevice(Mightyotadevice device)throws MightyAppException;

	public void deleteExistingEntryFromMightyOTADev()throws MightyAppException;

	public List<MightyDeviceInfo> getLatestOTACount(String version)throws MightyAppException;

	public List<Object[]> mightySwVerCount()throws MightyAppException;

	public MightyDeviceFirmware getMightyDeviceFirmware1(String HWSerialNo, String SWVersion, String AppVersion, String AppBuild) throws MightyAppException;

	public MightyDeviceFirmware getMDFIdByVersion(String version)throws MightyAppException;
}
