package com.team.mighty.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyUpload;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.domain.Mightydlauditlog;
import com.team.mighty.domain.Mightylog;
import com.team.mighty.domain.Mightyotadevice;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.service.ConsumerInstrumentService;

@Controller
public class MightyUserDeviceController {
private static final MightyLogger logger = MightyLogger.getLogger(MightyUserDeviceController.class);
	
	 @Autowired
	 private ConsumerInstrumentService consumerInstrumentServiceImpl;
 
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	/*@RequestMapping(value = "/deviceUserInfo", method = RequestMethod.GET)
	public String getAllMightyDevicesUserInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty device User inform");
		
		List<ConsumerDeviceDTO> consumerDeviceDTOList=null;
		consumerDeviceDTOList=new ArrayList<ConsumerDeviceDTO>();
		
		ConsumerDeviceDTO consumerDeviceDTO=null;
		
		try{
			
		List<MightyUserInfo> mightUserList=consumerInstrumentServiceImpl.getMightyUserInfo();
			
		if(mightUserList!=null && !mightUserList.isEmpty()){
			for(MightyUserInfo m:mightUserList){
				List<MightyDeviceUserMapping> mList=null;
				mList=new ArrayList<MightyDeviceUserMapping>();
				
				mList.addAll(m.getMightyDeviceUserMapping());
					//mList=m.getMightyUsrDevMaps1();
					
					if(mList!=null && !mList.isEmpty()){
						
						for(MightyDeviceUserMapping md : mList){
							
								MightyDeviceInfo mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyDeviceOnId(md.getMightyDeviceId());
								
								consumerDeviceDTO= new ConsumerDeviceDTO();
								consumerDeviceDTO.setId(m.getId());
								consumerDeviceDTO.setUserName(m.getUserName());
								consumerDeviceDTO.setEmailId(m.getEmailId());
								consumerDeviceDTO.setUserIndicator(m.getUserIndicator());
								consumerDeviceDTO.setUserStatus(m.getUserStatus());
								consumerDeviceDTO.setCreatedDt(m.getCreatedDt());
								consumerDeviceDTO.setUpdatedDt(m.getUpdatedDt());
								consumerDeviceDTO.setUsrdevReg(md.getRegistrationStatus());
								consumerDeviceDTO.setAppOS(md.getPhoneDeviceOSVersion());
								consumerDeviceDTO.setAppType(md.getPhoneDeviceType());
								if(mightyDeviceInfo!=null){
										consumerDeviceDTO.setDeviceId(mightyDeviceInfo.getDeviceId());
										
								}else{
										consumerDeviceDTO.setDeviceId("0");
								}
								consumerDeviceDTOList.add(consumerDeviceDTO);		
						}
					}else{
						
								consumerDeviceDTO= new ConsumerDeviceDTO();
								consumerDeviceDTO.setId(m.getId());
								consumerDeviceDTO.setUserName(m.getUserName());
								consumerDeviceDTO.setEmailId(m.getEmailId());
								consumerDeviceDTO.setUserIndicator(m.getUserIndicator());
								consumerDeviceDTO.setUserStatus(m.getUserStatus());
								consumerDeviceDTO.setCreatedDt(m.getCreatedDt());
								consumerDeviceDTO.setUpdatedDt(m.getUpdatedDt());
								consumerDeviceDTO.setDeviceId("0");
								consumerDeviceDTOList.add(consumerDeviceDTO);
					}
				
				
			}
		}	
				
		}catch(ArrayIndexOutOfBoundsException  e){
			logger.error("Exception in,",e);
		}
		map.put("mightydeviceuserlist", consumerDeviceDTOList);
		logger.debug("mightydeviceuserlist",consumerDeviceDTOList.size());
		return "MightyUser";
	}*/
	
	
	@RequestMapping(value = "/deviceUserInfo", method = RequestMethod.GET)
	public String getAllMightyDevicesUserInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty device User inform");
		
		List<ConsumerDeviceDTO> consumerDeviceDTOList=null;
		consumerDeviceDTOList=new ArrayList<ConsumerDeviceDTO>();
		
		List<Object[]> mightUserList=null;
		
				
		try{
			
			mightUserList=consumerInstrumentServiceImpl.getMightyUserDetails();
				/*for(Object[] mu : mightUserList){
					ConsumerDeviceDTO consumerDeviceDTO=null;
							consumerDeviceDTO= new ConsumerDeviceDTO();
									consumerDeviceDTO.setId(m.getId());
									consumerDeviceDTO.setUserName(m.getUserName());
									consumerDeviceDTO.setEmailId(m.getEmailId());
									consumerDeviceDTO.setUserIndicator(m.getUserIndicator());
									consumerDeviceDTO.setUserStatus(m.getUserStatus());
									consumerDeviceDTO.setCreatedDt(m.getCreatedDt());
									consumerDeviceDTO.setUpdatedDt(m.getUpdatedDt());
									consumerDeviceDTO.setUsrdevReg(md.getRegistrationStatus());
									consumerDeviceDTO.setAppOS(md.getPhoneDeviceOSVersion());
									consumerDeviceDTO.setAppType(md.getPhoneDeviceType());
									consumerDeviceDTO.setDeviceId(mightyDeviceInfo.getDeviceId());
									consumerDeviceDTOList.add(consumerDeviceDTO);		
						
				}*/
				
					
			
					
		}catch(Exception  e){
			logger.error("Exception in /deviceUserInfo,",e);
			e.printStackTrace();
		}
		
		map.put("mightydeviceuserlist", mightUserList);
		
		return "MightyUser";
	}
	
	@RequestMapping(value = "/mightyDeviceInfo", method = RequestMethod.GET)
	public String getAllMightyDevicesInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty device inform");
		List<MightyDeviceInfo> mightyDeviceList=consumerInstrumentServiceImpl.getMightyDeviceInfo();
		logger.debug("Mighty device List"+mightyDeviceList.size());
		map.put("mightyDeviceList", mightyDeviceList);
		return "mightyDeviceInfo";
	}
	

	
	@RequestMapping(value = "/mightyToCloudLog", method = RequestMethod.GET)
	public String mightyToCloudLogHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting /mightyToCloudLog ");
		Set<String> mightyLogs=consumerInstrumentServiceImpl.getMightyToCloudLogs();
		map.put("mightyLogs", mightyLogs);
		  return "mightyToCloudLogsReport";
	}
	@RequestMapping(value = "/otaFileUploading", method = RequestMethod.GET)
	public String otaFileUploadingHandler() throws Exception {
		logger.debug("Getting otaFileUploading ");
			return "uploadfile";
	}
	
	
	@RequestMapping(value = "/otaFileUploadedReport", method = RequestMethod.GET)
	public String otaFileUploadingReportHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting otaFileUploadedReport ");
		List<Mightyotadevice> mightyOTAList=consumerInstrumentServiceImpl.getExcelUploadMightyInfo();
		map.put("mightyOTAList", mightyOTAList);
			return "otaExcelFileUploadReport";
	}
	
	@RequestMapping(value = "/mightyDlAuditLog", method = RequestMethod.GET)
	public String mightyDlAuditLogHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mightyDlAuditLog inform");
		List<Mightydlauditlog> mightydlauditlog=consumerInstrumentServiceImpl.getMightyDlAuditLog();		
		map.put("mightydlauditlog", mightydlauditlog);
		return "mightydlauditlog";
	}
	
	
	@RequestMapping(value = "/userMgmt", method = RequestMethod.GET)
	public String userMgmtHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty's as per mighty ");	
		/*List<MightyDeviceInfo> mightyList=consumerInstrumentServiceImpl.getMightyDeviceInfo();
			map.put("mightyList", mightyList);*/
		return "viewMighty";
	}
	
	
	@RequestMapping(value = "/getUserByDevId", method = RequestMethod.GET)
	public @ResponseBody String ajaxForGetUserByDevId(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Getting User as per mighty id ");	
		String devId=request.getParameter("devId").trim();
		String retVal="";
		logger.debug("DevId as"+devId);
		//MightyDeviceInfo m=consumerInstrumentServiceImpl.getMightyDeviceOnId(Long.parseLong(devId));
		MightyDeviceInfo m=consumerInstrumentServiceImpl.getDeviceOnDeviceId(devId);
		if(m!=null){
			List<MightyDeviceUserMapping> mdList=consumerInstrumentServiceImpl.getMightyDeviceUserMappingOndevId(m.getId());
			
			
			for(MightyDeviceUserMapping md : mdList){
				MightyUserInfo usr=md.getMightyUserInfo();
				if(md.getRegistrationStatus().equalsIgnoreCase("Y")){
					retVal=retVal+"<tr>"
								+"<td>"
								+usr.getUserName()
								+"</td>"
								+"<td>"
								+ "<input type=\"hidden\" id=\"usrId\"  name=\"usrId\" value="+"\""+usr.getId()+"\""+"/>"
								+ "<input type=\"text\" id=\"emailId\"  name=\"emailId\" value="+"\""+usr.getEmailId()+"\""+"/>"
								+"</td>"
								+"<td>"
								+m.getDeviceId()
								+"</td>"
								+"<td>"
								+m.getSwVersion()
								+"</td>"
								+"<td>"
								+usr.getUserIndicator()
								+"</td>"
								+"<td>"
								+md.getPhoneDeviceOSVersion()
								+"</td>"
								+"<td>"
								+md.getPhoneDeviceType()
								+"</td>"
								+"<td>"
								+md.getRegistrationStatus()
								+"</td>"
								+"<td>"
								+"<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"updateUserInfo() \" >Submit</button>"
								+"</td>"
								+"</tr>";
				}	
				
				if(md.getRegistrationStatus().equalsIgnoreCase("N")){
					retVal=retVal+"<tr>"
								+"<td>"
								+usr.getUserName()
								+"</td>"
								+"<td>"
								+usr.getEmailId()
								+"</td>"
								+"<td>"
								+m.getDeviceId()
								+"</td>"
								+"<td>"
								+m.getSwVersion()
								+"</td>"
								+"<td>"
								+usr.getUserIndicator()
								+"</td>"
								+"<td>"
								+md.getPhoneDeviceOSVersion()
								+"</td>"
								+"<td>"
								+md.getPhoneDeviceType()
								+"</td>"
								+"<td>"
								+md.getRegistrationStatus()
								+"</td>"
								+"</tr>";
				}					
				
			}
		}
		return retVal;
	}
	
	

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.GET)
	public @ResponseBody String ajaxUpdateUserInfo(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Getting User as per mighty id ");	
		String userId=request.getParameter("userId");
		String emailId=request.getParameter("emailId");
		String retVal="";
		MightyUserInfo user=consumerInstrumentServiceImpl.getMightyUserById(Long.parseLong(userId));
			user.setEmailId(emailId);
			MightyUserInfo usr=consumerInstrumentServiceImpl.updateUserEmail(user);
			if(usr!=null){
				retVal="User updated successfully.";
			}else{
				retVal="User updation failure!";
			}
		
		return retVal;
	}
	
	@RequestMapping(value= {"/searchByUser"},method=RequestMethod.POST)
	public String searchByUserHandler(HttpServletRequest request,Map<String,Object> map) throws Exception{
		logger.debug("IN searchhandler Controller....");
		String searchStr=request.getParameter("searchText");
		logger.debug("/Search",searchStr);
		
		List<ConsumerDeviceDTO> consumerDeviceDTOList=null;
		consumerDeviceDTOList=new ArrayList<ConsumerDeviceDTO>();
		
		ConsumerDeviceDTO consumerDeviceDTO=null;
		
		try{
			
		List<MightyUserInfo> mightUserList=consumerInstrumentServiceImpl.getSearchUsers(searchStr);	
			
			
		if(mightUserList!=null && !mightUserList.isEmpty()){
			for(MightyUserInfo m:mightUserList){
				List<MightyDeviceUserMapping> mList=null;
				mList=new ArrayList<MightyDeviceUserMapping>();
				
				mList.addAll(m.getMightyDeviceUserMapping());
					//mList=m.getMightyUsrDevMaps1();
					
					if(mList!=null && !mList.isEmpty()){
						
						for(MightyDeviceUserMapping md : mList){
							
								MightyDeviceInfo mightyDeviceInfo=consumerInstrumentServiceImpl.getMightyDeviceOnId(md.getMightyDeviceId());
								
								consumerDeviceDTO= new ConsumerDeviceDTO();
								consumerDeviceDTO.setId(m.getId());
								consumerDeviceDTO.setUserName(m.getUserName());
								consumerDeviceDTO.setEmailId(m.getEmailId());
								consumerDeviceDTO.setUserIndicator(m.getUserIndicator());
								consumerDeviceDTO.setUserStatus(m.getUserStatus());
								consumerDeviceDTO.setCreatedDt(m.getCreatedDt());
								consumerDeviceDTO.setUpdatedDt(m.getUpdatedDt());
								consumerDeviceDTO.setUsrdevReg(md.getRegistrationStatus());
								if(mightyDeviceInfo!=null){
										consumerDeviceDTO.setDeviceId(mightyDeviceInfo.getDeviceId());
										
								}else{
										consumerDeviceDTO.setDeviceId("0");
								}
								consumerDeviceDTOList.add(consumerDeviceDTO);		
						}
					}else{
						
								consumerDeviceDTO= new ConsumerDeviceDTO();
								consumerDeviceDTO.setId(m.getId());
								consumerDeviceDTO.setUserName(m.getUserName());
								consumerDeviceDTO.setEmailId(m.getEmailId());
								consumerDeviceDTO.setUserIndicator(m.getUserIndicator());
								consumerDeviceDTO.setUserStatus(m.getUserStatus());
								consumerDeviceDTO.setCreatedDt(m.getCreatedDt());
								consumerDeviceDTO.setUpdatedDt(m.getUpdatedDt());
								consumerDeviceDTO.setDeviceId("0");
								consumerDeviceDTOList.add(consumerDeviceDTO);
					}
				
				
			}
		}	
				
		}catch(ArrayIndexOutOfBoundsException  e){
			logger.error("Exception in,",e);
		}
		map.put("mightydeviceuserlist", consumerDeviceDTOList);
		logger.debug("mightydeviceuserlist",consumerDeviceDTOList.size());
		return "MightySearchUser";
	}
	
	
	@RequestMapping(value= {"/searchDevice"},method=RequestMethod.POST)
	public String searchDeviceHandler(HttpServletRequest request,Map<String,Object> map) throws Exception{
		logger.debug("IN searchDevice Controller....");
		String searchDev=request.getParameter("searchDev");
		logger.debug("/Search",searchDev);
		
		List<MightyDeviceInfo> mightyDeviceList=consumerInstrumentServiceImpl.getMightySearchDevice(searchDev);
		map.put("mightyDeviceList", mightyDeviceList);
		return "mightySearchDevice";
	}
	
	
	@RequestMapping(value= {"/mightyLog"},method=RequestMethod.GET)
	public String mightyLogHandler(HttpServletRequest request,Map<String,Object> map) throws Exception{
		logger.debug("IN mightyLogHandler Controller....");
		Set<String> mightyLogs=consumerInstrumentServiceImpl.getMightyLogs();
		map.put("mightyLogs", mightyLogs);
		return "mightylog";
	}
	
	@RequestMapping(value = "/getLogByDevId", method = RequestMethod.GET)
	public @ResponseBody String ajaxForGetLogByDevId(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Getting Log as per mighty id ");	
		String devId=request.getParameter("devId");
		logger.debug("DevId as"+devId);
		MightyDeviceInfo m=consumerInstrumentServiceImpl.getMightyOnHwId(devId);
		List<Mightylog> logList=consumerInstrumentServiceImpl.getMightyLogsOndevId(devId);
		String retVal="";
		if(logList!=null){
			int i=1;
			for(Mightylog log : logList){
				retVal=retVal+"<tr>"
								+"<td>"
								+log.getLogType()
								+"</td>"
								+"<td>"
								+log.getTicket()
								+"</td>"
								+"<td>"
								+log.getDescription()
								+"</td>"
								+"<td>"
								+log.getDeviceId()
								+"</td>"
								+"<td>"
								+log.getDeviceType()
								+"</td>"
								+"<td>"
								+log.getPhoneDeviceOSVersion()
								+"</td>"
								+"<td>"
								+log.getUsername()
								+"</td>"
								+"<td>"
								+log.getEmailId()
								+"</td>"
								+"<td>"
								+log.getDevReg()
								+"</td>"
								+"<td>"
								+log.getCreatedDt()
								+"</td>"
								+"<td>"
								+log.getUpdatedDt()
								+"</td>"
								+"<td>"
								//+"<input type=\"file\" id=\"file1\" name=\"file1\" value="+"\""+log.getFileContent()+"\""+"/>"
								//+"<input type=\"submit\" class=\"btn btn-primary btn-xs\" value=\"Download\" onclick=\"downloadMightyLog(this.form.dat"+i+".value)\";>"
								+"<input type=\"hidden\" id=\"device\" name=\"device\" value="+"\""+log.getDeviceId()+"\""+"/>"
								+"<input type=\"hidden\" id=\"usrId\" name=\"usrId\" value="+"\""+log.getUsername()+"\""+"/>"
								+"<input type=\"hidden\" id=\"dat"+i+"\" value="+"\""+log.getUpdatedDt()+"\""+"/>"
								+"<input type=\"button\" class=\"btn btn-primary btn-xs\" value=\"Download\" onclick=\"downloadMightyLog(this.form.dat"+i+".value)\";/>"
								+"</td>"
								+"</tr>";
				i++;
				}	
		}
		return retVal;
	}
	
	@RequestMapping(value = "/getMightyToCloudLogByDevId", method = RequestMethod.GET)
	public @ResponseBody String ajaxForGetMightyToCloudLogByDevId(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Getting Log /getMightyToCloudLogByDevId");	
		String devId=request.getParameter("devId");
		logger.debug("DevId ass"+devId);
		List<MightyUpload> logList=consumerInstrumentServiceImpl.getMightyToCloudLogsOndevId(devId);
		String retVal="";
		if(logList!=null){
			for(MightyUpload log : logList){
				retVal=retVal+"<tr>"
								+"<td>"
								+log.getId()
								+"</td>"
								+"<td>"
								+log.getDeviceId()
								+"</td>"
								+"<td>"
								+log.getFileName()
								+"</td>"
								+"<td>"
								+log.getCreatedDt()
								+"</td>"
								+"<td>"
								+log.getUpdatedDt()
								+"</td>"
								+"<td>"
								+"<input type=\"hidden\" id=\"device\" name=\"device\" value="+"\""+log.getDeviceId()+"\""+"/>"
								+"<input type=\"hidden\" id=\"dat\" name=\"dat\" value="+"\""+log.getUpdatedDt()+"\""+"/>"
								+"<input type=\"submit\" class=\"btn btn-primary btn-xs\" value=\"Download\" />"
								//+"<input type=\"button\" class=\"btn btn-primary btn-xs\" value=\"Download\" onclick=\"downloadMighty()\" />"
								+"</td>"
								+"</tr>";
				}	
		}
		return retVal;
	}
	@RequestMapping(value = "/getMightyToCloudLogs",method={RequestMethod.POST,RequestMethod.GET})
	public void downloadMighyToCloudlogHandler(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map,RedirectAttributes redirectAttributes) throws IOException, SQLException {
		logger.debug("In submitting downloadMighylog ");
		String devId=request.getParameter("devId");
		String dat=request.getParameter("dat");
		logger.debug("deviceIddd",devId);
		logger.debug("updated_dateee",dat);
	
		  try {
			  
			  List<MightyUpload> mlogList=consumerInstrumentServiceImpl.getMightyToCloudLogsOndevId(devId);
			  if(mlogList!=null && !mlogList.isEmpty()){
				 logger.debug("entrysize",mlogList.size());
				 if(mlogList.size()==1){ 
					 logger.debug("IF size 1");
					 MightyUpload mlog=mlogList.get(0);
				   //response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
					 String headerKey = "Content-Disposition";
				        String headerValue = String.format("attachment; filename=\"%s\"","MightyToCloudlogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
				        response.setHeader(headerKey, headerValue);
							OutputStream out = response.getOutputStream();
								response.setContentType("text/plain");
								  logger.debug("Size of file content"+mlog.getFileContent().length());
								  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
								  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
									IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
										out.flush();
											out.close();
			      }else if(mlogList.size()>1){
			    	  logger.debug("Else IF size >1");
					  for(MightyUpload mlog : mlogList){
						  logger.debug("updated_sqldate_as",mlog.getUpdatedDt());
											 
						  Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						  String s = formatter.format(mlog.getUpdatedDt());
						  	
						  logger.debug("updated_date_request",s);
						  logger.debug("updated_date_request_0_added",s+".0");
						  
						  if(dat.equalsIgnoreCase(s+".0")){
							  logger.debug("inside loop updated_date_requestted");
					//response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
						 String headerKey = "Content-Disposition";
					        String headerValue = String.format("attachment; filename=\"%s\"","Mightylogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
					        response.setHeader(headerKey, headerValue);
								OutputStream out = response.getOutputStream();
									response.setContentType("text/plain");
									  logger.debug("Size of file content"+mlog.getFileContent().length());
									  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
									  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
										IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
											out.flush();
												out.close();
						  	} 
					  }  
				    }
			    }	//if main ended here							
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
	
	}
	
	@RequestMapping(value = "/getLogs",method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody void downloadMighylogHandler(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map,RedirectAttributes redirectAttributes) throws IOException, SQLException {
		logger.debug("In submitting downloadMighylog ");
		String devId=request.getParameter("devId");
		String username=request.getParameter("usrId");
		String dat=request.getParameter("dat");
		logger.debug("deviceIddd",devId);
		logger.debug("usernameee",username);
		logger.debug("updated_dateee",dat);
		
		
		  try {
			  
			  List<Mightylog> mlogList=consumerInstrumentServiceImpl.getExistingMightylog(devId, username);
			  if(mlogList!=null && !mlogList.isEmpty()){
				 logger.debug("entrysize",mlogList.size());
				 if(mlogList.size()==1){ 
					 logger.debug("IF size 1");
				  Mightylog mlog=mlogList.get(0);
				   //response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
					 String headerKey = "Content-Disposition";
				        String headerValue = String.format("attachment; filename=\"%s\"","Mightylogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
				        response.setHeader(headerKey, headerValue);
							OutputStream out = response.getOutputStream();
								response.setContentType("text/plain");
								  logger.debug("Size of file content"+mlog.getFileContent().length());
								  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
								  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
									IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
										out.flush();
											out.close();
			      }else if(mlogList.size()>1){
			    	  logger.debug("Else IF size >1");
					  for(Mightylog mlog : mlogList){
						  logger.debug("updated_sqldate_as",mlog.getUpdatedDt());
											 
						  Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						  String s = formatter.format(mlog.getUpdatedDt());
						  	
						  logger.debug("updated_date_request",s);
						  logger.debug("updated_date_request_0_added",s+".0");
						  
						  if(dat.equalsIgnoreCase(s+".0")){
							  logger.debug("inside loop updated_date_requestted");
					//response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
						 String headerKey = "Content-Disposition";
					        String headerValue = String.format("attachment; filename=\"%s\"","Mightylogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
					        response.setHeader(headerKey, headerValue);
								OutputStream out = response.getOutputStream();
									//response.setContentType("text/plain");
									  logger.debug("Size of file content"+mlog.getFileContent().length());
									  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
									  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
										IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
											out.flush();
												out.close();
						  	} 
					  }  
				    }
			    }	//if main ended here	
						
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}catch(Exception e){
				logger.debug("Error while downloading",e);
			}

	}
	
	
	@RequestMapping(value = "/downloadMightyLogs",method=RequestMethod.GET)
	public void downloadMightyLogsHandler(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map,RedirectAttributes redirectAttributes) throws IOException, SQLException {
		logger.debug("In submitting downloadMighylog ");
		String devId=request.getParameter("devId");
		//String username=request.getParameter("usrId");
		String username=URLDecoder.decode(request.getParameter("usrId"), "UTF-8" );
		String dat=URLDecoder.decode(request.getParameter("dat"), "UTF-8" );
		logger.debug("deviceIddd",devId);
		logger.debug("usernameee",username);
		logger.debug("updated_dateee",dat);
	
		  try {
			  
			  List<Mightylog> mlogList=consumerInstrumentServiceImpl.getExistingMightylog(devId, username);
			  if(mlogList!=null && !mlogList.isEmpty()){
				 logger.debug("entrysize",mlogList.size());
				 if(mlogList.size()==1){ 
					 logger.debug("IF size 1");
				  Mightylog mlog=mlogList.get(0);
				   //response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
					 String headerKey = "Content-Disposition";
				        String headerValue = String.format("attachment; filename=\"%s\"","Mightylogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
				        response.setHeader(headerKey, headerValue);
							OutputStream out = response.getOutputStream();
								response.setContentType("text/plain");
								  logger.debug("Size of file content"+mlog.getFileContent().length());
								  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
								  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
									IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
										out.flush();
											out.close();
			      }else if(mlogList.size()>1){
			    	  logger.debug("Else IF size >1");
					  for(Mightylog mlog : mlogList){
						  logger.debug("updated_sqldate_as",mlog.getUpdatedDt());
											 
						  Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						  String s = formatter.format(mlog.getUpdatedDt());
						  	
						  logger.debug("updated_date_request",s);
						  logger.debug("updated_date_request_0_added",s+".0");
						  
						  if(dat.equalsIgnoreCase(s+".0")){
							  logger.debug("inside loop updated_date_requestted");
					//response.setHeader("Content-Disposition", "attachment;filename=Firmware_V_"+mightyDeviceFirmware.getVersion()+".zip");
						 String headerKey = "Content-Disposition";
					        String headerValue = String.format("attachment; filename=\"%s\"","Mightylogs "+" "+dat+" "+mlog.getDeviceId()+".gz");
					        response.setHeader(headerKey, headerValue);
								OutputStream out = response.getOutputStream();
									response.setContentType("text/plain");
									  logger.debug("Size of file content"+mlog.getFileContent().length());
									  //logger.debug("Size of file content"+new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString())));
									  //new javax.sql.rowset.serial.SerialBlob(Base64.decodeBase64(mlog.getFileContent().toString()));
										IOUtils.copy(mlog.getFileContent().getBinaryStream(), out);
											out.flush();
												out.close();
						  	} 
					  }  
				    }
			    }	//if main ended here							
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
	
	}
	
	@RequestMapping(value="/mightyOTAFileUploadSubmit", method = RequestMethod.POST)
	public String mightyOTAFileUploadSubmitHandler(HttpServletRequest request,Map<String,Object> map,@RequestParam("file1") MultipartFile file1)  {
		logger.debug("IN file uploading");
		
		List<Mightyotadevice> otaList=null;
			
		try {
			
			otaList=new ArrayList<Mightyotadevice>();
				 boolean flag = false;
				// File file =new File("C:\\Users\\Dell\\Desktop\\upload\\mighty_ota_upgrade.xlsx");
				// File file =new File("/mnt/data/vhosts/casite-733550.cloudaccess.net/uploadfiles/mighty_ota_upgrade.xlsx");
				 //byte[] fileData = new byte[(int) file.length()];
				 //FileInputStream in = new FileInputStream(file1);
				 InputStream in= file1.getInputStream();
				 logger.debug("size uploading",file1.getBytes().length);
				 
					
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
													 ota1.setCreatedDt(new Date(System.currentTimeMillis()));
													 adminInstrumentServiceImpl.saveMightyOtaDevice(ota1);
												 }else if(m.getMightyDeviceId()==0 && dev==null){
													 Mightyotadevice ota1=null;
													 		ota1=new Mightyotadevice();
													 ota1.setDevices("0");
													 ota1.setUsername(ota.getUsername());
													 ota1.setCreatedDt(new Date(System.currentTimeMillis()));
													 adminInstrumentServiceImpl.saveMightyOtaDevice(ota1);
												 }
											}
										}
								}
							
						}
					}	
					
				 
				 in.close();
			   
			
		}
		catch (IOException ex){
        	logger.error("Exceptions IO as/,",ex);
        }
		catch(MightyAppException e) {
			logger.error("Exceptions MightyAppException as/", e);
		}
				
		return "redirect:/otaFileUploadedReport";
	}	
}
