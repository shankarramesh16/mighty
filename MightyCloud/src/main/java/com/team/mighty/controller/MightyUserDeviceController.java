package com.team.mighty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyUserInfo;
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
		List<MightyUserInfo> mightUserList=consumerInstrumentServiceImpl.getMightyUserInfo();
		map.put("mightydeviceuserlist", mightUserList);
		return "deviceInfo";
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
