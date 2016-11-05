package com.team.mighty.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.dto.DeviceFirmWareDTO;
import com.team.mighty.dto.DeviceInfoDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.utils.JsonUtil;

@RestController
@RequestMapping(MightyAppConstants.ADMIN_API)
public class AdminInstrumentController {

	private static final MightyLogger logger = MightyLogger.getLogger(AdminInstrumentController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
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
	
	@RequestMapping(value = "/createDeviceOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mightyDeviceOrder(@RequestBody MightyDeviceOrderInfo mightyDeviceOrderInfo) {
		logger.info("/ POST Create Device Order ", mightyDeviceOrderInfo);
		ResponseEntity<String> responseEntity = null;
		try {
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
	
	@RequestMapping(value = "/createDeviceFirmware", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewDeviceFirmWare(@RequestBody MightyDeviceFirmware mightyDeviceFirmware) {
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
	
	@RequestMapping(value = "/deviceFirmware", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDeviceFirmWare() {
		ResponseEntity<String> responseEntity = null;
		DeviceFirmWareDTO deviceFirmWareDTO = null;
		MightyDeviceFirmware mightyDeviceFirmware=null;
		try {
			mightyDeviceFirmware=adminInstrumentServiceImpl.getMightyDeviceFirmware();
			if(mightyDeviceFirmware!=null){
				deviceFirmWareDTO=new DeviceFirmWareDTO();
				deviceFirmWareDTO.setLatestVersion(mightyDeviceFirmware.getVersion());
				/*passing localhost API...*/
				deviceFirmWareDTO.setFileDownloadUrl("http://192.168.1.135:8011/MightyCloud/rest/admin/download/"+mightyDeviceFirmware.getId());
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
	}
	
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
			if(mightyDeviceFirmware!= null){
					response.setHeader("Content-Disposition", "attachment;filename=\binaryEncodedFile");
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
