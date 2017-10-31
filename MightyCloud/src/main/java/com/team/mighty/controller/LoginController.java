package com.team.mighty.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.mighty.constant.PasswordGenerator;
import com.team.mighty.domain.AdminUser;
import com.team.mighty.domain.MightyDeviceFirmware;
import com.team.mighty.domain.MightyDeviceInfo;
import com.team.mighty.domain.MightyUserInfo;
import com.team.mighty.dto.MightyUsersByVersionDto;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.notification.SendMail;
import com.team.mighty.service.AdminInstrumentService;
import com.team.mighty.service.ConsumerInstrumentService;
import com.team.mighty.service.LoginService;

@Controller
public class LoginController {
	private static final MightyLogger logger = MightyLogger.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ConsumerInstrumentService consumerInstrumentServiceImpl;
	
	@Autowired
	private AdminInstrumentService adminInstrumentServiceImpl;
	

	@RequestMapping(value= {"/"})
	public String defaultURL(){
		return "index";
	}
		
	
	@RequestMapping(value= {"/inValid"})
	public String inValidCredentials(){
		return "index";
	}
	
	@RequestMapping(value= {"/forgotPassword"})
	public String forgetPasswordHandler(){
		return "forgotPassword";
	}
	
	@RequestMapping(value= {"/onSubmitlogin"}, method=RequestMethod.POST)
	public ModelAndView loginUser(HttpServletRequest request, HttpSession session, HttpServletResponse response,RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("login user");
		String username = request.getParameter("uname") == null ? "" : request
				.getParameter("uname");
		String password = request.getParameter("pass") == null ? "" : request
				.getParameter("pass");
		
        String userMenu = "";
        AdminUser adminUser=null;
        boolean needToChangePwd=false;
		
        if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
        	redirectAttributes.addFlashAttribute("status",
					"<div class='failure'>Enter User Name/Password!!</div");
			return new ModelAndView("redirect:/");
		} else {			
			adminUser=loginService.getLoginUser(username,password);
		}
        
        if(adminUser!=null){
        	session.setAttribute("adminUser", adminUser);
        	         	
        }
        
        
        if (adminUser!=null) {
			if (adminUser.getPwdChangedDate()== null || adminUser.getPwdChangedDate().equals("")) {
				needToChangePwd = true;
			}
		}
        
        if (needToChangePwd) {
			session.setAttribute("username", username);
			session.setAttribute("password", password);
				return new ModelAndView("redirect:/changePasswordReq");
			
		} 
        
        if(adminUser!=null){
        	 	return new ModelAndView("redirect:/adminHome");
        	
        }else{
        	session.setAttribute("adminUser", "");
        	redirectAttributes.addFlashAttribute("status","<div class='failure'>Invalid User Name/Password !</div");
        	return new ModelAndView("redirect:/");
        }
		
				
		
	}
	
	
	
	@RequestMapping(value= {"/changePasswordReq"})
	public String changePwdReqHandler() throws Exception{
		logger.debug("IN ChangePassword");
		return "changePassword";
	}
	
	
	@RequestMapping(value= {"/changePasswordSubmit"},method=RequestMethod.POST)
	public String changePwdSubmitHandler(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("IN ChangePassword Controller....");
		HttpSession session =request.getSession();
		AdminUser adminUser=(AdminUser)session.getAttribute("adminUser");
		String password=request.getParameter("pwd");
		
		AdminUser admin=loginService.getUserById(String.valueOf(adminUser.getId()));
		admin.setPassword(password);
		admin.setPwdChangedDate(new Date(System.currentTimeMillis()));
		
		AdminUser adminUsr=loginService.setGeneratedPwd(admin);
        if (adminUsr!=null ) {
        	redirectAttributes.addFlashAttribute("status",
					"<div class='success'>Password Updated Successfully</div");
		} else {
			redirectAttributes.addFlashAttribute("status",
					"<div class='failure'>Password Updation Failed</div");
		}
        
		return "redirect:/";
	}
	
	
	
		
		 @RequestMapping(value= {"/adminHome"}, method=RequestMethod.GET)
		 public String home(Map<String,Object> map) throws Exception{
			 logger.debug("///adminHome");
			 List<MightyUserInfo> mightyUserList=consumerInstrumentServiceImpl.getAllMightyUsers();
			 List<MightyDeviceInfo> mightyDeviceList=consumerInstrumentServiceImpl.getAllMightyDev();
			 List<MightyDeviceFirmware> latestOTA=adminInstrumentServiceImpl.getLatestOTA();
			 List<Object[]> mightySwVerCount=adminInstrumentServiceImpl.mightySwVerCount();
			 List<MightyUsersByVersionDto>  mightySwVerCounts=null;
			 				mightySwVerCounts=new ArrayList<MightyUsersByVersionDto>();
			 
			 List<MightyDeviceInfo> latestOTACount=null;
			 if(latestOTA!=null && !latestOTA.isEmpty()){
				 logger.debug("///latest ota VERSION",latestOTA.get(0).getVersion());
				 latestOTACount=adminInstrumentServiceImpl.getLatestOTACount(latestOTA.get(0).getVersion().trim());
			 }
			 
			 if(mightySwVerCount!=null && !mightySwVerCount.isEmpty()){
				for(Object[] m: mightySwVerCount){
					MightyUsersByVersionDto dto=null;
						dto=new MightyUsersByVersionDto();
					logger.debug("inside for loop /adminHome");
					Double val=0.0;
					try{
						val=Double.parseDouble(m[0].toString());
					}catch(Exception e){
						;
					}
					if(val>0.5){
						dto.setVersion(m[0].toString());
						dto.setUserCount(m[1].toString());
						mightySwVerCounts.add(dto);
					}
					
					
				}
			 }
			 
			 logger.debug("///latest ota",latestOTACount.size());	
			 logger.debug("///latest ota count",latestOTACount.get(0));
			 	 			 		    	
			 map.put("mightyUserList",mightyUserList);
			 map.put("mightyDeviceList",mightyDeviceList);
			 map.put("latestOTA",latestOTA);
			 map.put("latestOTACount",latestOTACount);
			 map.put("mightySwVerCounts",mightySwVerCounts);
			 return "AdminView";
		    	
		}
		 
		 	@RequestMapping(value= {"/logout"})
			public String goToLogout(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map){
				logger.debug("In gotoLogout Page......");
					HttpSession session = request.getSession(true);
						session.invalidate();
							response.setHeader("Cache-Control",	"no-cache, no-store, must-revalidate");
								response.setHeader("Pragma", "co-cache");
									response.setDateHeader("Expires", 0);
										return "redirect:/";
			}
		 	
		 	@RequestMapping(value= {"/resetPassword"}, method=RequestMethod.POST)
			public String forgetPasswordAction(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception{
				String email = request.getParameter("email");
				logger.debug("Email address "+email);

				String password = new PasswordGenerator().randomString(10);
				logger.debug("Password generator "+password);
				String subject = "Password Reset";
				AdminUser adminUser=loginService.getUserByEmail(email);
				
				if(adminUser!=null){
					adminUser.setPassword(password);
					adminUser.setPwdChangedDate(null);
					AdminUser admin=loginService.setGeneratedPwd(adminUser);
						if (admin!=null) {
							String message = loginService.getPasswordResetMessage(admin);
						 SendMail mail = com.team.mighty.notification.SendMailFactory.getMailInstance();
						try{
						mail.send(admin.getEmailaddress(), subject, message);
						redirectAttributes.addFlashAttribute("status",
								"<div class='success'>Password Reset Successfull</div");
						}catch(Exception ex){
							logger.error("System Error,",ex);
						}
					} else {
						redirectAttributes.addFlashAttribute("status",	"<div class='failure'>Operation Failed</div");
					}
				}else{
					redirectAttributes.addFlashAttribute("status",	"<div class='failure'>Invalid Email Address</div");
					return "redirect:/forgotPassword";
				}
					return "redirect:/";
				
				
			}
		 	
		 	
				

			
}
