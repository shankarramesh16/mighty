package com.team.mighty.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.domain.AdminUser;
import com.team.mighty.domain.MightyDeviceOrderInfo;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.AdminInstrumentService;

@Controller
public class OrderDeviceController {
	private static final MightyLogger logger = MightyLogger.getLogger(OrderDeviceController.class);
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	
	
	@RequestMapping(value = "/addOrderDevice", method = RequestMethod.GET)
	public String addOrderDeviceHandler(Map<String,Object> map) throws Exception {
		logger.debug("IN orderDevice Handler");
		List<MightyDeviceOrderInfo> mightyDeviceOrderlist=adminInstrumentServiceImpl.getOrderDevices();
		map.put("mightyDeviceOrder", mightyDeviceOrderlist);
			return "addOrderDevice";
	}
	
	/*@RequestMapping(value = "/getOrderDevice", method = RequestMethod.GET)
	public String getOrderDeviceHandler(Map<String,Object> map) throws Exception {
		logger.debug("Getting orderDevice List");
		List<MightyDeviceOrderInfo> mightyDeviceOrder=adminInstrumentServiceImpl.getOrderDevices();
		map.put("mightyDeviceOrder", mightyDeviceOrder);
		return "redirect:/addOrderDevice";
	}*/
	
	@RequestMapping(value = "/mightyDeviceOrdersubmit", method = RequestMethod.POST)
	public String mightyDeviceOrdersubmitHandler(HttpServletRequest request,HttpSession session,Map<String,Object> map,RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("Getting for adding order Device");
		AdminUser adminUser=(AdminUser)session.getAttribute("adminUser");
		
		MightyDeviceOrderInfo mightyDeviceOrder=new MightyDeviceOrderInfo();
		mightyDeviceOrder.setOrderId(request.getParameter("orderId"));
		mightyDeviceOrder.setIsGift(request.getParameter("giftId"));
		mightyDeviceOrder.setDeviceSerialNo(request.getParameter("deviceId"));
		mightyDeviceOrder.setCreatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceOrder.setUpdatedDt(new Date(System.currentTimeMillis()));
		mightyDeviceOrder.setCreatedBy(adminUser.getDisplayname());
		mightyDeviceOrder.setUpdatedBy(adminUser.getDisplayname());
		try{
			adminInstrumentServiceImpl.saveMightyDeviceOrder(mightyDeviceOrder);
				
			redirectAttributes.addFlashAttribute("status", "Device Order added successfully.");
			}catch(Exception ex){
				redirectAttributes.addFlashAttribute("status", "Device Order addition Failed.");
				logger.error(ex);
		}
		
		
		return "redirect:/addOrderDevice";
		
	}
		
}
