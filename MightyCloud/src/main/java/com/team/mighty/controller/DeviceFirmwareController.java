package com.team.mighty.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.dto.DeviceFirmWareDTO;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.utils.DateUtil;

@Controller
public class DeviceFirmwareController {
	private static final MightyLogger logger = MightyLogger.getLogger(DeviceFirmwareController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	@RequestMapping(value = "/uploadDeviceFirmware")
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
		mightyDevFirmware.setFileName(file1.getOriginalFilename());
		mightyDevFirmware.setFile(new javax.sql.rowset.serial.SerialBlob(file1.getBytes()));
		String[] str1=new String(file2.getBytes()).split("\\n");
		String versionContain="";
		String hashValue="";
		int hashType=0;
		String requires="";
		String compatibleIOS="";
		String compatibleAND="";
		String compatibleHW="";
		
		
		try{
			String[] str2=str1[0].split(":");
			versionContain=str2[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		
		try{
			String[] str3=str1[1].split(":");
			hashValue=str3[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		
			
		try{
			String[] str4=str1[2].split(":");
			hashType=Integer.valueOf(str4[1].trim());
			
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}catch(NumberFormatException ne){
			logger.error(ne);
			
		}
		
		try{
			String[] str5=str1[3].split(":");
			requires=str5[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		
		try{
			String[] str6=str1[4].split(":");
			compatibleIOS=str6[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		catch(NumberFormatException ne){
		logger.error(ne);
		
		}
		
		try{
			String[] str7=str1[5].split(":");
			compatibleAND=str7[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		catch(NumberFormatException ne){
			logger.error(ne);
	
		}
		
		try{
			String[] str8=str1[6].split(":");
			compatibleHW=str8[1];
		}catch(ArrayIndexOutOfBoundsException ae){
			logger.error(ae);
		}
		
		
				
		logger.debug("version",versionContain);
		logger.debug("hashValue",hashValue);
		logger.debug("hashType",hashType);
		logger.debug("requires",requires);
		logger.debug("compatibleIOS",compatibleIOS);
		logger.debug("compatibleAND",compatibleAND);
		logger.debug("compatibleHW",compatibleHW);
		
		
		
		mightyDevFirmware.setHashType(hashType);
		mightyDevFirmware.setVersion(versionContain);
		mightyDevFirmware.setHashValue(hashValue);
		mightyDevFirmware.setRequires(Float.valueOf(requires));
		mightyDevFirmware.setCompatibleIOS(Float.valueOf(compatibleIOS));
		mightyDevFirmware.setCompatibleAND(compatibleAND);
		mightyDevFirmware.setCompatibleHW(compatibleHW);
		try{
			adminInstrumentServiceImpl.insertDeviceFirmwareDetails(mightyDevFirmware);
			redirectAttributes.addFlashAttribute("status", "Device firmware added successfully..");
			}catch(Exception ex){
				redirectAttributes.addFlashAttribute("status", "Device firmware addition Failed..");
				logger.error(ex);
		}
		
			return "redirect:/deviceFirmwareReport";
	}
 	
 	@RequestMapping(value = "/deviceFirmwareReport", method = RequestMethod.GET)
	public String deviceFirmwareInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting device Firmware inform");
		List<Object[]> mightDeviceFirmware=adminInstrumentServiceImpl.getDeviceFirmware();
		
		DeviceFirmWareDTO dto=null;
		List<DeviceFirmWareDTO> firmwareList= null;
		firmwareList= new ArrayList<DeviceFirmWareDTO>();
		for(Object[] obj: mightDeviceFirmware){
			dto=new DeviceFirmWareDTO();
			dto.setId((String)obj[0]);
			dto.setVersion((String)obj[1]);
			dto.setFileName((String)obj[2]);
			dto.setHashValue((String)obj[3]);
			dto.setHashType((Integer)obj[4]);
			dto.setStatus((String)obj[5]);
			dto.setEffectiveDt((Date)(obj[6]));
			dto.setCreatedDt((Date)obj[7]);
			dto.setUpdatedDt((Date)obj[8]);
			dto.setRequires((Float)obj[9]);
			dto.setCompatibleIOS((Float)obj[10]);
			dto.setCompatibleAND((String)obj[11]);
			dto.setCompatibleHW((String)obj[12]);
			
			firmwareList.add(dto);
			
			
		}
				
		map.put("mightDeviceFirmware", firmwareList);
		return "deviceFirmwareInfo";
	}
 	
 	@RequestMapping(value= {"/deleteFirmware"}, method=RequestMethod.POST)
	public @ResponseBody String ajaxForDeleteFirmware(HttpServletRequest request) throws Exception{
		String id =request.getParameter("id");
		
		MightyDeviceFirmware mightyDeviceFirmware=adminInstrumentServiceImpl.getDeviceFirmwareById(id);
		if(mightyDeviceFirmware!=null){
			adminInstrumentServiceImpl.deleteFirmware(mightyDeviceFirmware);
			return "Operation successful.";
		}else{
			return "Operation Failed.";
		}
		
 	}
}
