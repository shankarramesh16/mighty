package com.team.mighty.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
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
	
	@RequestMapping(value = "/deviceUserInfo", method = RequestMethod.GET)
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
	}
	
	@RequestMapping(value = "/mightyDeviceInfo", method = RequestMethod.GET)
	public String getAllMightyDevicesInfoHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty device inform");
		List<MightyDeviceInfo> mightyDeviceList=consumerInstrumentServiceImpl.getMightyDeviceInfo();
		logger.debug("Mighty device List"+mightyDeviceList.size());
		map.put("mightyDeviceList", mightyDeviceList);
		return "mightyDeviceInfo";
	}
	
	
	@RequestMapping(value = "/userMgmt", method = RequestMethod.GET)
	public String userMgmtHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting mighty's as per mighty ");	
		List<MightyDeviceInfo> mightyList=consumerInstrumentServiceImpl.getMightyDeviceInfo();
			map.put("mightyList", mightyList);
		return "viewMighty";
	}
	
	
	@RequestMapping(value = "/getUserByDevId", method = RequestMethod.GET)
	public @ResponseBody String ajaxForGetUserByDevId(HttpServletRequest request,Map<String,Object> map) throws Exception {
		logger.debug("Getting User as per mighty id ");	
		String devId=request.getParameter("devId");
		logger.debug("DevId as"+devId);
		MightyDeviceInfo m=consumerInstrumentServiceImpl.getMightyDeviceOnId(Long.parseLong(devId));
		List<MightyDeviceUserMapping> mdList=consumerInstrumentServiceImpl.getMightyDeviceUserMappingOndevId(Long.parseLong(devId));
		String retVal="";
		
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
							+usr.getUserIndicator()
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
							+usr.getUserIndicator()
							+"</td>"
							+"<td>"
							+md.getRegistrationStatus()
							+"</td>"
							+"</tr>";
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
}
