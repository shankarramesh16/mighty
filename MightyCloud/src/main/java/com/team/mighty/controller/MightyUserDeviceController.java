package com.team.mighty.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyDeviceUserMapping;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.ConsumerDeviceDTO;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.ConsumerInstrumentService;

@Controller
public class MightyUserDeviceController {
private static final MightyLogger logger = MightyLogger.getLogger(MightyUserDeviceController.class);
	
 @Autowired
 private ConsumerInstrumentService consumerInstrumentServiceImpl;
	
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
}
