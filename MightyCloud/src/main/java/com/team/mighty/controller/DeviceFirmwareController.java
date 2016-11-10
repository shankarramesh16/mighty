package com.team.mighty.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.utils.DateUtil;

@Controller
public class DeviceFirmwareController {
	private static final MightyLogger logger = MightyLogger.getLogger(DeviceFirmwareController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	@RequestMapping(value = "/addDeviceFirmware")
	public String addDeviceFirmwareHandler(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Adding DeviceFirmware");
				return "addDeviceFirmware";
	}
 
 
 		
	@RequestMapping(value = "/deviceFirmwareSubmit",method=RequestMethod.POST)
	public String deviceFirmwareSubmitHandler(HttpServletRequest request,Map<String,Object> map,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("In submitting DeviceFirmware details");
		String effectiveDate=request.getParameter("fromDate");
		
		
		MightyDeviceFirmware mightyDevFirmware=new MightyDeviceFirmware();
		mightyDevFirmware.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDevFirmware.setEffectiveDt(DateUtil.convertStringToDate(effectiveDate, "dd/MM/yyyy","yyyy-MM-dd hh:mm::ss"));
		mightyDevFirmware.setStatus(MightyAppConstants.IND_A);
		mightyDevFirmware.setUpdatedDt(new Date(System.currentTimeMillis()));
		mightyDevFirmware.setFile(new javax.sql.rowset.serial.SerialBlob(file1.getBytes()));
		String[] str1=new String(file2.getBytes()).split("\\n");
		String[] str2=str1[0].split(":");
		String versionContain=str2[1];
		mightyDevFirmware.setVersion(versionContain);
		
		try{
			adminInstrumentServiceImpl.insertDeviceFirmwareDetails(mightyDevFirmware);
			redirectAttributes.addFlashAttribute("status", "Device firmware added successfully..");
			}catch(Exception ex){
				redirectAttributes.addFlashAttribute("status", "Device firmware addition Failed..");
				logger.error(ex);
		}
		
			return "redirect:/addDeviceFirmware";
	}
 	
 	@RequestMapping(value = "/deviceFirmwareReport", method = RequestMethod.GET)
	public String deviceFirmwareInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting device Firmware inform");
		List<MightyDeviceFirmware> mightDeviceFirmware=adminInstrumentServiceImpl.getDeviceFirmware();
		map.put("mightDeviceFirmware", mightDeviceFirmware);
		return "deviceFirmwareInfo";
	}
 	
 	
		
}
