package com.team.mighty.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.dto.DeviceFirmWareDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.service.MightyCommonService;
import com.team.mighty.utils.JsonUtil;

@RestController
@RequestMapping(MightyAppConstants.ADMIN_API)
public class AdminInstrumentController {

	private static final MightyLogger logger = MightyLogger.getLogger(AdminInstrumentController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	@Autowired
	private MightyCommonService mightyCommonServiceImpl;
	
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
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_THRIDPARTY, xToken);
			
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
	
	/*@RequestMapping(value = "/deviceFirmware", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare() {
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try {
			//Validate X-MIGHTY-TOKEN Value
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			logger.info(request.getServerName());
			logger.info(request.getServerPort());
			logger.info(request.getProtocol());
			logger.info(request.isSecure());
			logger.info(request.getContextPath());
			logger.info(request.getHeader("x-forwarded-proto"));
			mightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware();
			if(mightyDeviceFirmware!=null){
				deviceFirmWareDTO=new DeviceFirmWareDTO();
				deviceFirmWareDTO.setLatestVersion(mightyDeviceFirmware.getVersion());
				passing downloading API...
				String URL = "http://mighty2.cloudaccess.host/MightyCloud/rest/admin/download/"+mightyDeviceFirmware.getId();
				if(request.isSecure()) {
					URL = "https://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				} else {
					URL = "http://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				}
				deviceFirmWareDTO.setFileDownloadUrl(URL);
				deviceFirmWareDTO.setHashValue(mightyDeviceFirmware.getHashValue());
				deviceFirmWareDTO.setHashType(mightyDeviceFirmware.getHashType());
				try{
				deviceFirmWareDTO.setFileSize(String.valueOf(mightyDeviceFirmware.getFile().length()));
				logger.debug("size",deviceFirmWareDTO.getFileSize());
				}catch(SQLException e){
					logger.error(e);
				}
				logger.debug("hashValue",mightyDeviceFirmware.getHashValue());
				logger.debug("hashType",mightyDeviceFirmware.getHashType());
				logger.debug("version",mightyDeviceFirmware.getVersion());
				logger.debug("id",mightyDeviceFirmware.getId());
				String response = JsonUtil.objToJson(deviceFirmWareDTO);
				responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
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
	
	@RequestMapping(value = "/deviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestBody String received) {
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
			//JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			//mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			logger.info(request.getServerName());
			logger.info(request.getServerPort());
			logger.info(request.getProtocol());
			logger.info(request.isSecure());
			logger.info(request.getContextPath());
			logger.info(request.getHeader("x-forwarded-proto"));
			if(HWSerialNumber!=null && SWVersion!=null && AppVersion!=null && AppBuild!=null && !HWSerialNumber.isEmpty() 
					&& !SWVersion.isEmpty() && !AppVersion.isEmpty()  &&  !AppBuild.isEmpty()){
			reqMightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware(HWSerialNumber,SWVersion,AppVersion,AppBuild);
					
			latestMightyDeviceFirmware=adminInstrumentServiceImpl.getMightyLstDeviceFirmware();
			
			deviceFirmWareDTO=new DeviceFirmWareDTO();
			if(reqMightyDeviceFirmware!=null){
				
				deviceFirmWareDTO.setReqLatestVersion(reqMightyDeviceFirmware.getVersion().trim());
				/*passing downloading API...*/
				String URL = "https://mighty2.cloudaccess.host/testing/rest/admin/download/"+reqMightyDeviceFirmware.getId();
				/*if(request.isSecure()) {
					URL = "https://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				} else {
					URL = "http://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				}*/
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
			
			/*Latest Firmware*/
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
			

			
			/*logger.debug("ReqhashValue",reqMightyDeviceFirmware.getHashValue());
			logger.debug("ReqHT",reqMightyDeviceFirmware.getHashType());
			logger.debug("Reqversion",reqMightyDeviceFirmware.getVersion());
			logger.debug("Reqid",reqMightyDeviceFirmware.getId());
			logger.debug("ReqIOS",reqMightyDeviceFirmware.getCompatibleIOS());
			logger.debug("ReqAND",reqMightyDeviceFirmware.getCompatibleAND());
			logger.debug("ReqHW",reqMightyDeviceFirmware.getCompatibleHW());
			logger.debug("Requires",reqMightyDeviceFirmware.getRequires());
			
			logger.debug("hashValue",latestMightyDeviceFirmware.getHashValue());
			logger.debug("Ht",latestMightyDeviceFirmware.getHashType());
			logger.debug("version",latestMightyDeviceFirmware.getVersion());
			logger.debug("id",latestMightyDeviceFirmware.getId());
			logger.debug("IOS",latestMightyDeviceFirmware.getCompatibleIOS());
			logger.debug("AND",latestMightyDeviceFirmware.getCompatibleAND());
			logger.debug("HW",latestMightyDeviceFirmware.getCompatibleHW());
			logger.debug("latestRequires",latestMightyDeviceFirmware.getRequires());*/
			String response = JsonUtil.objToJson(deviceFirmWareDTO);
			responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
		
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
	}
	
	
	/*@RequestMapping(value = "/deviceFirmware", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare(@RequestHeader(value = MightyAppConstants.HTTP_HEADER_TOKEN_NAME) String xToken) {
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try {
			//Validate X-MIGHTY-TOKEN Value
			JWTKeyGenerator.validateXToken(xToken);
			
			// Validate Expriy Date
			mightyCommonServiceImpl.validateXToken(MightyAppConstants.KEY_MIGHTY_MOBILE, xToken);
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			logger.info(request.getServerName());
			logger.info(request.getServerPort());
			logger.info(request.getProtocol());
			logger.info(request.isSecure());
			logger.info(request.getContextPath());
			logger.info(request.getHeader("x-forwarded-proto"));
			mightyDeviceFirmware = adminInstrumentServiceImpl.getMightyDeviceFirmware();
			if(mightyDeviceFirmware!=null){
				deviceFirmWareDTO=new DeviceFirmWareDTO();
				deviceFirmWareDTO.setLatestVersion(mightyDeviceFirmware.getVersion());
				passing localhost API...
				String URL = null;
				if(request.isSecure()) {
					URL = "https://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				} else {
					URL = "http://" +request.getServerName() + ":" +request.getServerPort()+ request.getContextPath() +"/rest/admin/download/"+mightyDeviceFirmware.getId();
				}
				deviceFirmWareDTO.setFileDownloadUrl(URL);
				deviceFirmWareDTO.setHashValue(mightyDeviceFirmware.getHashValue());
				deviceFirmWareDTO.setHastType(mightyDeviceFirmware.getHashType());
				logger.debug("hashValue",mightyDeviceFirmware.getHashValue());
				logger.debug("hashType",mightyDeviceFirmware.getHashType());
				String response = JsonUtil.objToJson(deviceFirmWareDTO);
				responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
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
	
		
	@RequestMapping(value="/download/{deviceFirmwareId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> download(@PathVariable("deviceFirmwareId") String deviceFirmwareId,HttpServletResponse response) throws IOException, SQLException {
		ResponseEntity<String> responseEntity = null;
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try {
			mightyDeviceFirmware=adminInstrumentServiceImpl.getDeviceFirmwareById(deviceFirmwareId);
			if(mightyDeviceFirmware!= null && mightyDeviceFirmware.getFile() != null){
					//response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
				 String headerKey = "Content-Disposition";
			        String headerValue = String.format("attachment; filename=\"%s\"",mightyDeviceFirmware.getFileName());
			        response.setHeader(headerKey, headerValue);
						OutputStream out = response.getOutputStream();
							response.setContentType("text/plain");
								IOUtils.copy(mightyDeviceFirmware.getFile().getBinaryStream(), out);
									out.flush();
										out.close();
											responseEntity = new ResponseEntity<String>(HttpStatus.OK);
			}else{
				responseEntity = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}
		}
		catch(MightyAppException e) {
			String errorMessage = e.getMessage();
			responseEntity = new ResponseEntity<String>(errorMessage, e.getHttpStatus());
			logger.errorException(e, e.getMessage());
		}
				
		return responseEntity;
	}	
}
