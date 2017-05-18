package com.team.mighty.interceptor;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.team.mighty.domain.AdminUser;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.utils.OtherFunctions;

public class RequestProcessingInterceptor extends HandlerInterceptorAdapter {

	private static final MightyLogger logger = MightyLogger.getLogger(RequestProcessingInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		
				
		logger.debug(">> FILTER <<");
		String indexPage="/";
		String forwardPage="/";
		String queryString="";
		HttpSession session = request.getSession();
			String preUrl = request.getServletPath();
					String actualPrePage=getURI(request);			
						String page=preUrl;
							queryString= getQueryString(request)==null?"": getQueryString(request);
								logger.debug("preUrl as "+page);
								logger.debug("actualPrePage as "+actualPrePage);
		page=page==null?"":page;
		if(page.equals("")==false )
		{			
			if(page.equals("/")){
				page=page.substring(0);
			}
			else{
			page=page.substring(1);
			}
			
			if(page.contains("?"))
			{ 
				System.out.println("page in ???"+page);
				request.getSession().invalidate();
				page=page.substring(0,page.indexOf("?"));
				
			}
			
			queryString=queryString==""?"":"?"+queryString;
			forwardPage=page+queryString;
			
		}else
		{
			page=indexPage;
			forwardPage=page;
		}

	
		if(page.equals("/") || page.equals("forgotPassword") || page.equals("resetPassword"))
		{	
			logger.debug("IN preURL '/'':");
			return true;
		}
		else if (page.equals("onSubmitlogin")){ 
				return true;
		}
		else if(OtherFunctions.isEitherJspOrServlet(actualPrePage))
		{
						AdminUser user=(AdminUser) session.getAttribute("adminUser");
						
						if (user == null || user.equals("null")) 
						{
							logger.debug("NO user Id page : ", page);
							String oPage="/";
							if(actualPrePage.equals("logout")) {
								response.sendRedirect(oPage);
							}
							else{
									logger.debug("Actual Prepage = "+ actualPrePage);
									if(actualPrePage.substring(actualPrePage.length()-3).equals(".do")==false)  
									{
											String param = actualPrePage + "___"+ request.getQueryString();
											oPage="/MightyCloud/?prePage=" + param;
											response.sendRedirect(oPage);
											return false;
									}
								
							}
					    }
						else{
							logger.debug("IN user Id page : ", page);
								return true;
						}
		}
		else {
			logger.debug("//WebServices Call");
			request.getRequestDispatcher("/"+page).forward(request, response);
		}
	
	return true;
	}

    

private String getURI(HttpServletRequest request ) {
	
	String returnString="";
	try {
		returnString = request.getAttribute("javax.servlet.forward.request_uri").toString();
		if(returnString.trim().equals("")) {
			returnString = request.getServletPath().substring(1);
		} else {
			returnString.substring(returnString.lastIndexOf("/")+1);
		}
	}catch(Exception ex) {
		returnString = request.getServletPath().substring(1); 
	}
	
	return returnString;
}

public String getQueryString( HttpServletRequest servletRequest  )
{
	
	StringBuilder queryString = new StringBuilder();
	  Map<String, String[]> params= servletRequest.getParameterMap();
	  Set<String> pset= params.keySet();
	  boolean isFirst=true;
	  if(pset!=null&&pset.size()>0) {
	  	  for(String name:pset) {
	  		  for(String value:params.get(name) ) {
				 StringBuilder tocken=new StringBuilder();
        		   tocken.append(name).append("=").append(value);
        		   				if(isFirst) {
        		   						queryString.append(tocken);
        		   						isFirst=false;
        		   				} else {
        		   						queryString.append("&").append(tocken);
        		   }
        		    
			  }
      		 
		  }
	  }
	  return queryString.toString();
}
}
