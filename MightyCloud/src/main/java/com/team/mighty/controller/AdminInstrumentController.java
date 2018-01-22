package com.team.mighty.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.dao.MightyDeviceInfoDAO;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.domain.Mightyotadevice;
import com.team.mighty.dto.DeviceFirmWareDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.service.MightyCommonService;
import com.team.mighty.utils.JWTKeyGenerator;
import com.team.mighty.utils.JsonUtil;

@RestController
@RequestMapping(MightyAppConstants.ADMIN_API)
public class AdminInstrumentController {

	private static final MightyLogger logger = MightyLogger.getLogger(AdminInstrumentController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	@Autowired
	private ConsumerInstrumentService consumerInstrumentServiceImpl;
	
	@Autowired
	private MightyCommonService mightyCommonServiceImpl;
	
	@Autowired
	private MightyDeviceInfoDAO mightyDeviceInfoDAO;
	
	@RequestMapping(value = "/device", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllMightyDevices() {
		ResponseEntity<String> responseEntity = null;
		try {
			List<DeviceInfoDTO> lstMightyDeviceInfo = adminInstrumentServiceImpl.getAllMightyDevice();
			String response = JsonUtil.objToJson(lstMightyDeviceInfo);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch(MightyAppException e) {
			logger.errorException(e, e.getMessage());
			responseEntity = new ResponseEntity<String>(e.getMessage(), e.getHttpStatus());
		}
		return responseEntity;
	}
	
	// To Validate X-MIGHTY-TOKEN
	
	//JWTKeyGenerator.validateXToken(xToken);
	//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_THRIDPARTY, xToken);
	
	@RequestMapping(value = "/createDeviceOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mightyDeviceOrder(@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken,
			@RequestBody MightyDeviceOrderInfo mightyDeviceOrderInfo) {
		logger.info("/ POST Create Device Order ", mightyDeviceOrderInfo);
		ResponseEntity<String> responseEntity = null;
		try {
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_THRIDPARTY, xToken);
			
			mightyDeviceOrderInfo = adminInstrumentServiceImpl.createDeviceOrder(mightyDeviceOrderInfo);
			String response = JsonUtil.objToJson(mightyDeviceOrderInfo);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
		} catch(MightyAppException e) {
			mightyDeviceOrderInfo.setErrorCode(e.getHttpStatus().toString());
			mightyDeviceOrderInfo.setErrorDesc(e.getMessage());
			String response = JsonUtil.objToJson(mightyDeviceOrderInfo);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	// validateJWTToken
	@RequestMapping(value = "/createDeviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewDeviceFirmWare(@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken,
			@RequestBody MightyDeviceFirmware mightyDeviceFirmware) {
		
		ResponseEntity<String> responseEntity = null;
		try {
			mightyDeviceFirmware = adminInstrumentServiceImpl.createDeviceFirmware(mightyDeviceFirmware);
			String response = JsonUtil.objToJson(mightyDeviceFirmware);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
		} catch(MightyAppException e) {
			mightyDeviceFirmware.setErrorCode(e.getHttpStatus().toString());
			mightyDeviceFirmware.setErrorDesc(e.getMessage());
			String response = JsonUtil.objToJson(mightyDeviceFirmware);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
		return responseEntity;
	}
	
	
	/*Device Firmware updrade API 31th Oct,2017*/
	/*@RequestMapping(value = "/deviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) throws Exception {
		logger.info(" /POST /deviceFirmware");
		logger.debug("/deviceFirmware Received",received);
		logger.debug("/deviceFirmware token",xToken);
		
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware reqMightyDeviceFirmware=null;
		MightyDeviceFirmware latestMightyDeviceFirmware=null;
		
				JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.debug("Exception during parser '/deviceFirmware'");
			return new ResponseEntity<String>("Empty received body '/deviceFirmware' ", HttpStatus.NO_CONTENT);
		}
					
					
		String mighty="";			
					
		
					
		try {
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
						
				if(obj.get("HWSerialNumber").toString()!=null && !obj.get("HWSerialNumber").toString().isEmpty() && 
						obj.get("SWVersion").toString()!=null && !obj.get("SWVersion").toString().isEmpty() &&
							obj.get("AppVersion").toString()!=null && !obj.get("AppVersion").toString().isEmpty() && 
								obj.get("AppBuild").toString()!=null && !obj.get("AppBuild").toString().isEmpty()){
					
						mighty=obj.get("HWSerialNumber").toString();
						logger.debug("//HWSerialNumber",obj.get("HWSerialNumber").toString());
						logger.debug("//SWVersion",obj.get("SWVersion").toString());
						logger.debug("//AppVersion",obj.get("AppVersion").toString());
						logger.debug("//AppBuild",obj.get("AppBuild").toString());
						
						String HWSerialNumber=obj.get("HWSerialNumber").toString();
						String SWVersion=obj.get("SWVersion").toString();
						String AppVersion=obj.get("AppVersion").toString();
						String AppBuild=obj.get("AppBuild").toString();
					
						Checking UPDATING SWV
						MightyDeviceInfo md=null; 
							md=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNumber);
						if(SWVersion.contains("UPDATING")){
							deviceFirmWareDTO=new DeviceFirmWareDTO();					
							deviceFirmWareDTO.setLatestVersion(md.getSwVersion());
							deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleHW("MX");
							String res = JsonUtil.objToJson(deviceFirmWareDTO);
							return new ResponseEntity<String>(res, HttpStatus.OK);
						}else{					
							if(md!=null){
								md.setSwVersion(SWVersion);
								md.setAppVersion(Float.valueOf(AppVersion));
								md.setAppBuild(AppBuild);
								md.setUpgradeAt(new Date(System.currentTimeMillis()));
								mightyDeviceInfoDAO.save(md);
							}	
						}
						
						Double val=0.0;
						try{
							val=Double.parseDouble(SWVersion);
						}catch(Exception e){
							;
						}
				
				    OTA for limited release condition checker
					if(val>=0.972){
						List<Mightyotadevice> ota=adminInstrumentServiceImpl.getMightyForOTA(HWSerialNumber);
						
							if(ota!=null && !ota.isEmpty()){
								logger.debug("OTA LIST SIZE",ota.size());
								reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
										
								latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
								
								deviceFirmWareDTO=new DeviceFirmWareDTO();
								
								if(reqMightyDeviceFirmware!=null){
																		
									String URL = "http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;						
									
									deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());									
									deviceFirmWareDTO.setFileDownloadUrl(URL);
									deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
									deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
									deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
									
										try{
											deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
											logger.debug("size",deviceFirmWareDTO.getFileSize());
										}catch(SQLException e){
												logger.error(e);
										}
									
									
									logger.debug("/ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
									logger.debug("/ReqHT",reqMightyDeviceFirmware.getHashType());
									logger.debug("/Reqversion",reqMightyDeviceFirmware.getVersion());
									logger.debug("/Reqid",reqMightyDeviceFirmware.getId());
									logger.debug("/ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("/ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
									logger.debug("/ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
									logger.debug("/Requires",reqMightyDeviceFirmware.getRequires());
									logger.debug("/downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
									
									
									
								}
								
							
								if(latestMightyDeviceFirmware!=null){
									
									deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
									deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
									deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
									deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
									
									logger.debug("/hashValue",latestMightyDeviceFirmware.getHashValue());
									logger.debug("/Ht",latestMightyDeviceFirmware.getHashType());
									logger.debug("/version",latestMightyDeviceFirmware.getVersion());
									logger.debug("/id",latestMightyDeviceFirmware.getId());
									logger.debug("/IOS",latestMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("/AND",latestMightyDeviceFirmware.getCompatibleAND());
									logger.debug("/HW",latestMightyDeviceFirmware.getCompatibleHW());
									logger.debug("/latestRequires",latestMightyDeviceFirmware.getRequires());
									
									
								}
											
								String response = JsonUtil.objToJson(deviceFirmWareDTO);
								responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
								return responseEntity;
							}	
							
							
						deviceFirmWareDTO=new DeviceFirmWareDTO();
						deviceFirmWareDTO.setLatestVersion(SWVersion);
						deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
						deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
						deviceFirmWareDTO.setCompatibleHW("MX");
						String res = JsonUtil.objToJson(deviceFirmWareDTO);
						responseEntity = new ResponseEntity<String>(res, HttpStatus.OK);
						return responseEntity;
						
					}else{
						Public release conditions
						logger.debug("Else Part for HWSerialNumber: ",HWSerialNumber);
						reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
						
						latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
						
						deviceFirmWareDTO=new DeviceFirmWareDTO();
						if(reqMightyDeviceFirmware!=null){							
							
							String URL = "http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;			
							
							deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
							deviceFirmWareDTO.setFileDownloadUrl(URL);
							deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
							deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
							deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
							deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
							deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
							deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
								try{
									deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
									logger.debug("size",deviceFirmWareDTO.getFileSize());
								}catch(SQLException e){
										logger.error(e);
								}
							
							
							logger.debug("//ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
							logger.debug("//ReqHT",reqMightyDeviceFirmware.getHashType());
							logger.debug("//Reqversion",reqMightyDeviceFirmware.getVersion());
							logger.debug("//Reqid",reqMightyDeviceFirmware.getId());
							logger.debug("//ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
							logger.debug("//ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
							logger.debug("//ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
							logger.debug("//Requires",reqMightyDeviceFirmware.getRequires());
							logger.debug("//downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
							
							
							
						}
						
						
						if(latestMightyDeviceFirmware!=null){
							
							deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
							deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
							deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
							deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
							deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
							deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
							deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
							
							logger.debug("//hashValue",latestMightyDeviceFirmware.getHashValue());
							logger.debug("//Ht",latestMightyDeviceFirmware.getHashType());
							logger.debug("//version",latestMightyDeviceFirmware.getVersion());
							logger.debug("//id",latestMightyDeviceFirmware.getId());
							logger.debug("//IOS",latestMightyDeviceFirmware.getCompatibleIOS());
							logger.debug("//AND",latestMightyDeviceFirmware.getCompatibleAND());
							logger.debug("//HW",latestMightyDeviceFirmware.getCompatibleHW());
							logger.debug("//latestRequires",latestMightyDeviceFirmware.getRequires());
							
							
						}
									
						String response = JsonUtil.objToJson(deviceFirmWareDTO);
							responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
						return responseEntity;
					}
				
				
			}else{
						responseEntity = new ResponseEntity<String>("Null/Empty value passing '/deviceFirmware'", HttpStatus.NOT_ACCEPTABLE);
			}
			
			
		} catch(MightyAppException e) {
			logger.error("/Exception in as '/deviceFirmware' ",e);
			logger.error("/OTA Mighty as '/deviceFirmware' ",mighty);
			deviceFirmWareDTO = new DeviceFirmWareDTO();
			deviceFirmWareDTO.setStatusCode(e.getHttpStatus().toString());
			deviceFirmWareDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(deviceFirmWareDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}*/
	
	
	/*06th Nov,2017*/
	
	@RequestMapping(value = "/deviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) throws Exception {
		logger.info(" /POST /deviceFirmware");
		logger.debug("/deviceFirmware Received",received);
		logger.debug("/deviceFirmware token",xToken);
		
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware reqMightyDeviceFirmware=null;
		MightyDeviceFirmware latestMightyDeviceFirmware=null;
		
				JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.debug("Exception during parser '/deviceFirmware'");
			return new ResponseEntity<String>("Empty received body '/deviceFirmware' ", HttpStatus.NO_CONTENT);
		}
					
					
		String mighty="";			
					
		
					
		try {
			//Validate X-MIGHTY-TOKEN Value
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
									
				if(obj.get("HWSerialNumber").toString()!=null && !obj.get("HWSerialNumber").toString().isEmpty() && 
						obj.get("SWVersion").toString()!=null && !obj.get("SWVersion").toString().isEmpty() &&
							obj.get("AppVersion").toString()!=null && !obj.get("AppVersion").toString().isEmpty() && 
								obj.get("AppBuild").toString()!=null && !obj.get("AppBuild").toString().isEmpty()){
					
						mighty=obj.get("HWSerialNumber").toString();
						logger.debug("//HWSerialNumber",obj.get("HWSerialNumber").toString());
						logger.debug("//SWVersion",obj.get("SWVersion").toString());
						logger.debug("//AppVersion",obj.get("AppVersion").toString());
						logger.debug("//AppBuild",obj.get("AppBuild").toString());
						
						String HWSerialNumber=obj.get("HWSerialNumber").toString();
						String SWVersion=obj.get("SWVersion").toString();
						String AppVersion=obj.get("AppVersion").toString();
						String AppBuild=obj.get("AppBuild").toString();
					
						//Checking UPDATING SWV
						MightyDeviceInfo md=null; 
							md=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNumber);
						if(SWVersion.contains("UPDATING")){
							deviceFirmWareDTO=new DeviceFirmWareDTO();					
							deviceFirmWareDTO.setLatestVersion(md.getSwVersion());
							deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleHW("MX");
							String res = JsonUtil.objToJson(deviceFirmWareDTO);
							return new ResponseEntity<String>(res, HttpStatus.OK);
						}else{					
							if(md!=null){
								md.setSwVersion(SWVersion);
								md.setAppVersion(Float.valueOf(AppVersion));
								md.setAppBuild(AppBuild);
								md.setUpgradeAt(new Date(System.currentTimeMillis()));
								mightyDeviceInfoDAO.save(md);
							}	
						}
						
						Double val=0.0;
						try{
							val=Double.parseDouble(SWVersion);
						}catch(Exception e){
							;
						}
				
				   // OTA for limited release condition checker
					if(val>=0.64){
						List<Mightyotadevice> ota=adminInstrumentServiceImpl.getMightyForOTA(HWSerialNumber);
						
							if(ota!=null && !ota.isEmpty()){
								logger.debug("OTA LIST SIZE",ota.size());
													
								
								reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware1(HWSerialNumber,SWVersion,AppVersion,AppBuild);
										
								latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
								
								deviceFirmWareDTO=new DeviceFirmWareDTO();
								
								if(reqMightyDeviceFirmware!=null){
																	
									if(reqMightyDeviceFirmware.getVersion().trim().equals(ota.get(0).getVersion().trim())){
										logger.debug("Inside If no need to execute else");											
										String URL="http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;
										deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
										deviceFirmWareDTO.setFileDownloadUrl(URL);
										deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
										deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
										deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
										deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
										deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
										deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
										
											try{
												deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
												logger.debug("size",deviceFirmWareDTO.getFileSize());
											}catch(SQLException e){
													logger.error(e);
											}
										
										
										logger.debug("/ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
										logger.debug("/ReqHT",reqMightyDeviceFirmware.getHashType());
										logger.debug("/Reqversion",reqMightyDeviceFirmware.getVersion());
										logger.debug("/Reqid",reqMightyDeviceFirmware.getId());
										logger.debug("/ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
										logger.debug("/ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
										logger.debug("/ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
										logger.debug("/Requires",reqMightyDeviceFirmware.getRequires());
										logger.debug("/downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
											
									}else{
										
										logger.debug("Inside Else need to execute firmware query");
										MightyDeviceFirmware otaReq=adminInstrumentServiceImpl.getMDFIdByVersion(ota.get(0).getVersion().trim());
										   if(otaReq!=null){
											String URL="http://mighty2.cloudaccess.host/test1/rest/admin/download/"+otaReq.getId()+"/devId/"+HWSerialNumber;
											deviceFirmWareDTO.setReqLatestVersion(otaReq.getVersion().trim());
											deviceFirmWareDTO.setFileDownloadUrl(URL);
											deviceFirmWareDTO.setReqHashValue(otaReq.getHashValue().trim());
											deviceFirmWareDTO.setReqHT(String.valueOf(otaReq.getHashType()));
											deviceFirmWareDTO.setReqCompatibleIOS(otaReq.getCompatibleIOS());
											deviceFirmWareDTO.setReqCompatibleLatestAND(otaReq.getCompatibleAND());
											deviceFirmWareDTO.setReqCompatibleLatestHW(otaReq.getCompatibleHW().trim());
											deviceFirmWareDTO.setRequires(otaReq.getRequires());
											
												try{
													deviceFirmWareDTO.setFileSize(String.valueOf(otaReq.getFile().length()));
													logger.debug("size otaReq",deviceFirmWareDTO.getFileSize());
												}catch(SQLException e){
														logger.error(e);
												}
											
											
											logger.debug("/ReqhashValue otaReq",otaReq.getHashValue().trim());
											logger.debug("/ReqHT otaReq",otaReq.getHashType());
											logger.debug("/Reqversion otaReq",otaReq.getVersion());
											logger.debug("/Reqid otaReq",otaReq.getId());
											logger.debug("/ReqIOS otaReq",otaReq.getCompatibleIOS());
											logger.debug("/ReqAND otaReq",otaReq.getCompatibleAND());
											logger.debug("/ReqHW otaReq",otaReq.getCompatibleHW());
											logger.debug("/Requires otaReq",otaReq.getRequires());
											logger.debug("/downloadingUrl otaReq",deviceFirmWareDTO.getFileDownloadUrl());
										  }
											
									}
																	
									
									
									
									
								}
								
							
								if(latestMightyDeviceFirmware!=null){
									
									deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
									deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
									deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
									deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
									
									logger.debug("/hashValue",latestMightyDeviceFirmware.getHashValue());
									logger.debug("/Ht",latestMightyDeviceFirmware.getHashType());
									logger.debug("/version",latestMightyDeviceFirmware.getVersion());
									logger.debug("/id",latestMightyDeviceFirmware.getId());
									logger.debug("/IOS",latestMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("/AND",latestMightyDeviceFirmware.getCompatibleAND());
									logger.debug("/HW",latestMightyDeviceFirmware.getCompatibleHW());
									logger.debug("/latestRequires",latestMightyDeviceFirmware.getRequires());
									
									
								}
											
								String response = JsonUtil.objToJson(deviceFirmWareDTO);
								responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
								return responseEntity;
							} else if(val<1.0){
								//Public release conditions
								logger.debug("Else Part for HWSerialNumber: ",HWSerialNumber);
								reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
								
								latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
								
								deviceFirmWareDTO=new DeviceFirmWareDTO();
								if(reqMightyDeviceFirmware!=null){							
									
									String URL = "http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;			
									
									deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
									deviceFirmWareDTO.setFileDownloadUrl(URL);
									deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
									deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
									deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
										try{
											deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
											logger.debug("size",deviceFirmWareDTO.getFileSize());
										}catch(SQLException e){
												logger.error(e);
										}
									
									
									logger.debug("//ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
									logger.debug("//ReqHT",reqMightyDeviceFirmware.getHashType());
									logger.debug("//Reqversion",reqMightyDeviceFirmware.getVersion());
									logger.debug("//Reqid",reqMightyDeviceFirmware.getId());
									logger.debug("//ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("//ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
									logger.debug("//ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
									logger.debug("//Requires",reqMightyDeviceFirmware.getRequires());
									logger.debug("//downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
									
									
									
								}
								
								
								if(latestMightyDeviceFirmware!=null){
									
									deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
									deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
									deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
									deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
									
									logger.debug("//hashValue",latestMightyDeviceFirmware.getHashValue());
									logger.debug("//Ht",latestMightyDeviceFirmware.getHashType());
									logger.debug("//version",latestMightyDeviceFirmware.getVersion());
									logger.debug("//id",latestMightyDeviceFirmware.getId());
									logger.debug("//IOS",latestMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("//AND",latestMightyDeviceFirmware.getCompatibleAND());
									logger.debug("//HW",latestMightyDeviceFirmware.getCompatibleHW());
									logger.debug("//latestRequires",latestMightyDeviceFirmware.getRequires());
									
									
								}
											
								String response = JsonUtil.objToJson(deviceFirmWareDTO);
									responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
								return responseEntity;
							}else{
								deviceFirmWareDTO=new DeviceFirmWareDTO();
								deviceFirmWareDTO.setLatestVersion(SWVersion);
								deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
								deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
								deviceFirmWareDTO.setCompatibleHW("MX");
								String res = JsonUtil.objToJson(deviceFirmWareDTO);
								responseEntity = new ResponseEntity<String>(res, HttpStatus.OK);
								return responseEntity;
							}
													
					}//if<main condition> end here
				
				
			}else{
						responseEntity = new ResponseEntity<String>("Null/Empty value passing '/deviceFirmware'", HttpStatus.NOT_ACCEPTABLE);
			}
			
			
		} catch(MightyAppException e) {
			logger.error("/Exception in as '/deviceFirmware' ",e);
			logger.error("/OTA Mighty as '/deviceFirmware' ",mighty);
			deviceFirmWareDTO = new DeviceFirmWareDTO();
			deviceFirmWareDTO.setStatusCode(e.getHttpStatus().toString());
			deviceFirmWareDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(deviceFirmWareDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}
	
	
	//10th Nov
	
	/*@RequestMapping(value = "/deviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) throws Exception {
		logger.info(" /POST /deviceFirmware");
		logger.debug("/deviceFirmware Received",received);
		logger.debug("/deviceFirmware token",xToken);
		
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware reqMightyDeviceFirmware=null;
		MightyDeviceFirmware latestMightyDeviceFirmware=null;
		
				JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.debug("Exception during parser '/deviceFirmware'");
			return new ResponseEntity<String>("Empty received body '/deviceFirmware' ", HttpStatus.NO_CONTENT);
		}
					
					
		String mighty="";			
					
		
					
		try {
			//Validate X-MIGHTY-TOKEN Value
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
					
				if(obj.get("HWSerialNumber").toString()!=null && !obj.get("HWSerialNumber").toString().isEmpty() && 
						obj.get("SWVersion").toString()!=null && !obj.get("SWVersion").toString().isEmpty() &&
							obj.get("AppVersion").toString()!=null && !obj.get("AppVersion").toString().isEmpty() && 
								obj.get("AppBuild").toString()!=null && !obj.get("AppBuild").toString().isEmpty()){
					
						mighty=obj.get("HWSerialNumber").toString();
						logger.debug("//HWSerialNumber",obj.get("HWSerialNumber").toString());
						logger.debug("//SWVersion",obj.get("SWVersion").toString());
						logger.debug("//AppVersion",obj.get("AppVersion").toString());
						logger.debug("//AppBuild",obj.get("AppBuild").toString());
						
						String HWSerialNumber=obj.get("HWSerialNumber").toString();
						String SWVersion=obj.get("SWVersion").toString();
						String AppVersion=obj.get("AppVersion").toString();
						String AppBuild=obj.get("AppBuild").toString();
					
						//Checking UPDATING SWV
						MightyDeviceInfo md=null; 
							md=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNumber);
						if(SWVersion.contains("UPDATING")){
							deviceFirmWareDTO=new DeviceFirmWareDTO();					
							deviceFirmWareDTO.setLatestVersion(md.getSwVersion());
							deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
							deviceFirmWareDTO.setCompatibleHW("MX");
							String res = JsonUtil.objToJson(deviceFirmWareDTO);
							return new ResponseEntity<String>(res, HttpStatus.OK);
						}
						
								reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
								
								latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
								
								deviceFirmWareDTO=new DeviceFirmWareDTO();
								if(reqMightyDeviceFirmware!=null){							
									
									String URL = "http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;			
									
									deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
									deviceFirmWareDTO.setFileDownloadUrl(URL);
									deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
									deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
									deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
										try{
											deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
											logger.debug("size",deviceFirmWareDTO.getFileSize());
										}catch(SQLException e){
												logger.error(e);
										}
									
									
									logger.debug("//ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
									logger.debug("//ReqHT",reqMightyDeviceFirmware.getHashType());
									logger.debug("//Reqversion",reqMightyDeviceFirmware.getVersion());
									logger.debug("//Reqid",reqMightyDeviceFirmware.getId());
									logger.debug("//ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("//ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
									logger.debug("//ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
									logger.debug("//Requires",reqMightyDeviceFirmware.getRequires());
									logger.debug("//downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
									
									
									
								}
								
								
								if(latestMightyDeviceFirmware!=null){
									
									deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
									deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
									deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
									deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
									deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
									deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
									deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
									
									logger.debug("//hashValue",latestMightyDeviceFirmware.getHashValue());
									logger.debug("//Ht",latestMightyDeviceFirmware.getHashType());
									logger.debug("//version",latestMightyDeviceFirmware.getVersion());
									logger.debug("//id",latestMightyDeviceFirmware.getId());
									logger.debug("//IOS",latestMightyDeviceFirmware.getCompatibleIOS());
									logger.debug("//AND",latestMightyDeviceFirmware.getCompatibleAND());
									logger.debug("//HW",latestMightyDeviceFirmware.getCompatibleHW());
									logger.debug("//latestRequires",latestMightyDeviceFirmware.getRequires());
									
									
								}
											
								String response = JsonUtil.objToJson(deviceFirmWareDTO);
								responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
								return responseEntity;
					}else{
								responseEntity = new ResponseEntity<String>("Null/Empty value passing '/deviceFirmware'", HttpStatus.NOT_ACCEPTABLE);
					}
			
			
		} catch(MightyAppException e) {
			logger.error("/Exception in as '/deviceFirmware' ",e);
			logger.error("/OTA Mighty as '/deviceFirmware' ",mighty);
			deviceFirmWareDTO = new DeviceFirmWareDTO();
			deviceFirmWareDTO.setStatusCode(e.getHttpStatus().toString());
			deviceFirmWareDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(deviceFirmWareDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}*/
	
	
	/*@RequestMapping(value = "/deviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestBody String received,@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) throws Exception {
		logger.error("In device Firmware,");
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware reqMightyDeviceFirmware=null;
		MightyDeviceFirmware latestMightyDeviceFirmware=null;
		
		JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON",e);
		}
		 
		
					String HWSerialNumber=(String)obj.get("HWSerialNumber");
					String SWVersion=(String)obj.get("SWVersion");
					String AppVersion=(String)obj.get("AppVersion");
					String AppBuild=(String)obj.get("AppBuild");
					logger.debug("HWSerialNumber",obj.get("HWSerialNumber"));
					logger.debug("SWVersion",obj.get("SWVersion"));
					logger.debug("AppVersion",obj.get("AppVersion"));
					logger.debug("AppBuild",obj.get("AppBuild"));
		
					
		try {
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			
			if(HWSerialNumber!=null && SWVersion!=null && AppVersion!=null && AppBuild!=null && !HWSerialNumber.isEmpty() 
					&& !SWVersion.isEmpty() && !AppVersion.isEmpty()  &&  !AppBuild.isEmpty()){	
				
				if(SWVersion.contains("UPDATING")){
					MightyDeviceInfo md=null; 
						md=mightyDeviceInfoDAO.getDeviceInfo(HWSerialNumber);
					deviceFirmWareDTO=new DeviceFirmWareDTO();					
					deviceFirmWareDTO.setLatestVersion(md.getSwVersion());
					deviceFirmWareDTO.setLatestRequired(Float.valueOf(AppVersion));
					deviceFirmWareDTO.setCompatibleIOS(Float.valueOf(AppVersion));
					deviceFirmWareDTO.setCompatibleHW("MX");
					String res = JsonUtil.objToJson(deviceFirmWareDTO);
					return new ResponseEntity<String>(res, HttpStatus.OK);
				}
				
				
				
				reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
				latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
				
				deviceFirmWareDTO=new DeviceFirmWareDTO();
				if(reqMightyDeviceFirmware!=null){
					
					deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
					
					String URL = "http://mighty2.cloudaccess.host/test1/rest/admin/download/"+reqMightyDeviceFirmware.getId()+"/devId/"+HWSerialNumber;
					//String URL = "http://192.168.1.109:8088/MightyCloud/rest/admin/download/"+reqMightyDeviceFirmware.getId();
					if(request.isSecure()) {
						URL = "https://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
					} else {
						URL = "http://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
					}
					deviceFirmWareDTO.setFileDownloadUrl(URL);
					deviceFirmWareDTO.setReqHashValue(reqMightyDeviceFirmware.getHashValue().trim());
					deviceFirmWareDTO.setReqHT(String.valueOf(reqMightyDeviceFirmware.getHashType()));
					deviceFirmWareDTO.setReqCompatibleIOS(reqMightyDeviceFirmware.getCompatibleIOS());
					deviceFirmWareDTO.setReqCompatibleLatestAND(reqMightyDeviceFirmware.getCompatibleAND());
					deviceFirmWareDTO.setReqCompatibleLatestHW(reqMightyDeviceFirmware.getCompatibleHW().trim());
					deviceFirmWareDTO.setRequires(reqMightyDeviceFirmware.getRequires());
					try{
						deviceFirmWareDTO.setFileSize(String.valueOf(reqMightyDeviceFirmware.getFile().length()));
						logger.debug("size",deviceFirmWareDTO.getFileSize());
					}catch(SQLException e){
							logger.error(e);
					}
					
					
					logger.debug("ReqhashValue",reqMightyDeviceFirmware.getHashValue().trim());
					logger.debug("ReqHT",reqMightyDeviceFirmware.getHashType());
					logger.debug("Reqversion",reqMightyDeviceFirmware.getVersion());
					logger.debug("Reqid",reqMightyDeviceFirmware.getId());
					logger.debug("ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
					logger.debug("ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
					logger.debug("ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
					logger.debug("Requires",reqMightyDeviceFirmware.getRequires());
					logger.debug("downloadingUrl",deviceFirmWareDTO.getFileDownloadUrl());
					
					
					
				}
				
			
				if(latestMightyDeviceFirmware!=null){
					
					deviceFirmWareDTO.setHashValue(latestMightyDeviceFirmware.getHashValue());
					deviceFirmWareDTO.setHt(String.valueOf(latestMightyDeviceFirmware.getHashType()));
					deviceFirmWareDTO.setCompatibleIOS(latestMightyDeviceFirmware.getCompatibleIOS());
					deviceFirmWareDTO.setCompatibleAND(latestMightyDeviceFirmware.getCompatibleAND());
					deviceFirmWareDTO.setCompatibleHW(latestMightyDeviceFirmware.getCompatibleHW());
					deviceFirmWareDTO.setLatestRequired(latestMightyDeviceFirmware.getRequires());
					deviceFirmWareDTO.setLatestVersion(latestMightyDeviceFirmware.getVersion().trim());
					
					logger.debug("hashValue",latestMightyDeviceFirmware.getHashValue());
					logger.debug("Ht",latestMightyDeviceFirmware.getHashType());
					logger.debug("version",latestMightyDeviceFirmware.getVersion());
					logger.debug("id",latestMightyDeviceFirmware.getId());
					logger.debug("IOS",latestMightyDeviceFirmware.getCompatibleIOS());
					logger.debug("AND",latestMightyDeviceFirmware.getCompatibleAND());
					logger.debug("HW",latestMightyDeviceFirmware.getCompatibleHW());
					logger.debug("latestRequires",latestMightyDeviceFirmware.getRequires());
					
					
				}
							
				String response = JsonUtil.objToJson(deviceFirmWareDTO);
				responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
				return responseEntity;
			
				
				
			}else{
				throw new MightyAppException("Passing  empty or null values of HwSerialNo/SWVersion/AppVersion/AppBuild ",HttpStatus.METHOD_NOT_ALLOWED);
				 
			}
			
			
		} catch(MightyAppException e) {
			logger.errorException(e);
			deviceFirmWareDTO = new DeviceFirmWareDTO();
			deviceFirmWareDTO.setStatusCode(e.getHttpStatus().toString());
			deviceFirmWareDTO.setStatusDesc(e.getMessage());
			String response = JsonUtil.objToJson(deviceFirmWareDTO);
			responseEntity = new ResponseEntity<String>(response, e.getHttpStatus());
		}
		return responseEntity;
	}*/
	
	
	
	
	@RequestMapping(value = "/device/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadDeviceListCSV(@RequestParam("file") MultipartFile file) {
		return null;
	}
	
	
	
		
 @RequestMapping(value="/download/{deviceFirmwareId}/devId/{deviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> download(@PathVariable("deviceFirmwareId") String deviceFirmwareId,@PathVariable("deviceId") String deviceId,HttpServletResponse response){
	logger.debug("/In downloading firmware");	
	logger.debug("/downloadingDeviceId",deviceId);
	 ResponseEntity<String> responseEntity = null;
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try{
			mightyDeviceFirmware=adminInstrumentServiceImpl.getDeviceFirmwareById(deviceFirmwareId);
			if(mightyDeviceFirmware!= null && mightyDeviceFirmware.getFile() != null){
					//response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
				 String headerKey = "Content-Disposition";
			       String headerValue = String.format("attachment; filename=\"%s\"",mightyDeviceFirmware.getFileName());
			        response.setHeader(headerKey, headerValue);
			        response.setHeader("Content-Length", Long.toString(mightyDeviceFirmware.getFile().length()));  
						OutputStream out = response.getOutputStream();
							  IOUtils.copy(mightyDeviceFirmware.getFile().getBinaryStream(), out);
							  out.flush();
							  out.close();
							  responseEntity = new ResponseEntity<String>(deviceId,HttpStatus.OK);
			}else{
				responseEntity = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}
		}
		catch(MightyAppException e) {
			logger.error(e, e.getMessage());
		}catch (IOException e) {
			responseEntity = new ResponseEntity<String>("IO /OTA failed",HttpStatus.EXPECTATION_FAILED);
			logger.error(e, e.getMessage());
		} catch (SQLException e) {
			responseEntity = new ResponseEntity<String>("SQL Exception /OTA failed",HttpStatus.EXPECTATION_FAILED);
			logger.error(e, e.getMessage());
		}
		
		logger.debug("Done downloading...");
				
		return responseEntity;
	}	
	
	
 		/*For testing prospective using*/
	@RequestMapping(value="/download1/{deviceFirmwareId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> download1(@PathVariable("deviceFirmwareId") String deviceFirmwareId,HttpServletResponse response)  {
		ResponseEntity<String> responseEntity = null;
		logger.debug("IN file downloading....");
		try {
				 String headerKey = "Content-Disposition";
				 File file =new File("C:\\Users\\Dell\\Desktop\\file\\mighty_ota_upgrade_v0.93_kavalan_build_2.zip");
				 byte[] fileData = new byte[(int) file.length()];
				 FileInputStream in = new FileInputStream(file);
	/*			 in.read(fileData);
				 in.close();*/
			     String headerValue = String.format("attachment; filename=\"%s\"",file.getName());
			     response.setHeader(headerKey, headerValue);
				 OutputStream out = response.getOutputStream();
							response.setContentType("text/plain");
								IOUtils.copy(in, out);
									out.flush();
										out.close();
										responseEntity = new ResponseEntity<String>(HttpStatus.OK);
			
		}
		catch (IOException ex){
        	logger.error("Exceptions IO as/,",ex);
        }
		catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> upload(HttpServletRequest request,HttpServletResponse response)  {
		logger.debug("IN file uploading");
		
		ResponseEntity<String> responseEntity = null;
		List<Mightyotadevice> otaList=null;
				
		try {
			
			otaList=new ArrayList<Mightyotadevice>();
				 boolean flag = false;
				// File file =new File("C:\\Users\\Dell\\Desktop\\upload\\mighty_ota_upgrade.xlsx");
				 File file =new File("/mnt/data/vhosts/casite-733550.cloudaccess.net/uploadfiles/mighty_ota_upgrade.xlsx");
				 //byte[] fileData = new byte[(int) file.length()];
				 FileInputStream in = new FileInputStream(file);
				 
					
					XSSFWorkbook workbook = new XSSFWorkbook(in);
					XSSFSheet sheet = workbook.getSheetAt(0);
										  
					Iterator<Row> rowIterator = sheet.iterator();
					  
					rowIterator.next();
					while (rowIterator.hasNext()) {
						logger.debug("row ");
						
						if (flag)
							break;

						Row row = rowIterator.next();
						// For each row, iterate through each columns

						Iterator<Cell> cellIterator = row.cellIterator();
						
						//boolean isScheduleAlter = false;
						while (cellIterator.hasNext()) {
							Mightyotadevice ota=null;
								ota=new Mightyotadevice();
							Cell cell = cellIterator.next();
											
							logger.debug("col ", cell.getColumnIndex());
							
							if (cell.getColumnIndex() <1) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
																
							} else {
								break;
							}
		 
							if (cell.getColumnIndex() == 0) {
									String value = cell.getStringCellValue();
									logger.debug(" cell getColumnIndex==0 value=",value);
								
								if (value == null || value.equals("")) {
									logger.debug("End of file");
									flag = true;
									break;
								}
								
								ota.setUsername(cell.getStringCellValue().trim());
							} 
							
							otaList.add(ota);	
							
							

					}
					
				}	
					logger.debug("Listsize",otaList.size());
					if(otaList!=null && !otaList.isEmpty()){
							adminInstrumentServiceImpl.deleteExistingEntryFromMightyOTADev();
						for(Mightyotadevice ota : otaList){
							List<MightyUserInfo> mightyUsers=consumerInstrumentServiceImpl.getUserByUserName(ota.getUsername());
								if(mightyUsers!=null && !mightyUsers.isEmpty()){
									MightyUserInfo user=mightyUsers.get(0);
									List<MightyDeviceUserMapping> md=consumerInstrumentServiceImpl.getMightyUserDeviceMappingByUserId(user.getId());
										if(md!=null & !md.isEmpty()){
											logger.debug("hiiiii size",md.size());
											for(MightyDeviceUserMapping m : md){
												logger.debug("deviceMapping id",m.getMightyDeviceId());
												MightyDeviceInfo dev=consumerInstrumentServiceImpl.getMightyDeviceInfoOnMappingDevice(m.getMightyDeviceId());
												if(dev!=null){
													Mightyotadevice ota1=null;
															ota1=new Mightyotadevice();
													 ota1.setDevices(dev.getDeviceId());
													 ota1.setUsername(ota.getUsername());
													 adminInstrumentServiceImpl.saveMightyOtaDevice(ota1);
												 }else if(m.getMightyDeviceId()==0 && dev==null){
													 Mightyotadevice ota1=null;
													 		ota1=new Mightyotadevice();
													 ota1.setDevices("0");
													 ota1.setUsername(ota.getUsername());
													 adminInstrumentServiceImpl.saveMightyOtaDevice(ota1);
												 }
											}
										}
								}
							
						}
					}	
					
				 
				 in.close();
			     responseEntity = new ResponseEntity<String>(HttpStatus.OK);
			
		}
		catch (IOException ex){
        	logger.error("Exceptions IO as/,",ex);
        }
		catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;
	}	
	
		
}
