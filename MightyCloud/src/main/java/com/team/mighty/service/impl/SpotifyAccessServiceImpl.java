package com.team.mighty.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.team.mighty.dao.SpotifyDao;
import com.team.mighty.domain.SpotifyInfo;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.SpotifyAccessService;

@Component("spotifyAccessService")
public class SpotifyAccessServiceImpl implements SpotifyAccessService {

	private static final MightyLogger logger = MightyLogger.getLogger(SpotifyAccessServiceImpl.class);
	
	@Autowired
	private SpotifyDao spotifyDao;
	
	
	
	/*public String getAccessToken(String code, String error, String state) throws MightyAppException {
		String refreshToken = null;
		if(StringUtil.isEmpty(error)) {
			String url = SpringPropertiesUtil.getProperty(MightyConfigConstants.SPOTIFY_URL);
			String api = SpringPropertiesUtil.getProperty(MightyConfigConstants.ACCESS_TOKEN_API);
			
			String clientId = SpringPropertiesUtil.getProperty(MightyConfigConstants.SPOTIFY_CLIENT_ID);
			String clientSecret = SpringPropertiesUtil.getProperty(MightyConfigConstants.SPOTIFY_CLIENT_SECRET);
			
			String authorization = new String(Base64.encodeBase64((clientId+clientSecret).getBytes()));
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(MightyConfigConstants.AUTHORIZATION_HEADER, "Basic "+authorization);
			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put(MightyConfigConstants.KEY_GRANT_TYPE, 
					SpringPropertiesUtil.getProperty(MightyConfigConstants.SPOTIFY_GRANT_TYPE));
			requestMap.put(MightyConfigConstants.KEY_CODE, code);
			requestMap.put(MightyConfigConstants.KEY_REDIRECT_URL, "http%3A%2F%2Flocalhost%3A8080%2FMightyCloud%2Fspotifyaccess");
			
			String request = JsonUtil.objToJson(requestMap);
			logger.info(" Request Json ", request);
			logger.info(" Header ", httpHeaders);
			
			HttpEntity<String> entity = new HttpEntity<String>(request,httpHeaders);
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(url+api, HttpMethod.POST, entity, String.class);
			
			logger.info(response);
		} else {
			throw new MightyAppException(error, HttpStatus.FORBIDDEN);
		}
			
		return null;
	}*/
	
	public String getAccessToken(String code, String error, String state) throws Exception{
				String tokens=null;
		
				String url = "https://accounts.spotify.com/api/token";
			 SSLContext context = SSLContext.getInstance("TLS"); 
	         context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
	                 public void checkClientTrusted(X509Certificate[] chain, 
	                                 String authType) throws CertificateException {} 
	                 public void checkServerTrusted(X509Certificate[] chain, 
	                                 String authType) throws CertificateException {} 
	                 public X509Certificate[] getAcceptedIssuers() { 
	                         return new X509Certificate[0]; 
	                 }}}, new SecureRandom()); 
	         HttpsURLConnection.setDefaultSSLSocketFactory( 
	                         context.getSocketFactory()); 
	      
	       	URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
		    Map<String,String> arguments = new HashMap<String, String>();
		    arguments.put("code", code);
		    arguments.put("grant_type", "authorization_code");
		    arguments.put("client_id", "8cda18d9034947759f0b09e68e17c7c1");
		    arguments.put("client_secret", "5d38b745cb0445f793b950b36eec95aa");
		    arguments.put("redirect_uri", "https://mighty2.cloudaccess.host/test4/spotifyaccess/RedirectedSpotifyAccess");
		    //arguments.put("redirect_uri", "http://192.168.0.11:8080/MightyCloud/spotifyaccess/RedirectedSpotifyAccess");
		    StringBuilder sj = new StringBuilder();
		    for(Map.Entry<String,String> entry : arguments.entrySet()) {
		        sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
		    }
		    byte[] out = sj.toString().getBytes();

		    con.setFixedLengthStreamingMode(out.length);
		    con.connect();
		    
				    try
				    {
				        OutputStream os = con.getOutputStream();
				        os.write(out);
				        
				        BufferedReader in = new BufferedReader(
						        new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
		
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
		
						//print result
						//logger.debug(response.toString());
						
						tokens=response.toString();
		
				    }
				    catch (Exception e)
				    {
				    	logger.error(e);
				    }
			
		return tokens;
	}

	
	


	


	
	public String getRefreshSpotifyToken(String refreshToken) throws Exception {
		String newAccessToken=null;
		logger.debug("refreshTOken",refreshToken);
		String url = "https://accounts.spotify.com/api/token";
	 SSLContext context = SSLContext.getInstance("TLS"); 
     context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
             public void checkClientTrusted(X509Certificate[] chain, 
                             String authType) throws CertificateException {} 
             public void checkServerTrusted(X509Certificate[] chain, 
                             String authType) throws CertificateException {} 
             public X509Certificate[] getAcceptedIssuers() { 
                     return new X509Certificate[0]; 
             }}}, new SecureRandom()); 
     HttpsURLConnection.setDefaultSSLSocketFactory( 
                     context.getSocketFactory()); 
  
   	URL obj = new URL(url);
	HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

	con.setRequestMethod("POST");
	con.setDoOutput(true);
    Map<String,String> arguments = new HashMap<String, String>();
    arguments.put("grant_type", "refresh_token");
    arguments.put("client_id", "8cda18d9034947759f0b09e68e17c7c1");
    arguments.put("client_secret", "5d38b745cb0445f793b950b36eec95aa");
    //arguments.put("redirect_uri", "https://mighty2.cloudaccess.host/test1/spotifyaccess/RedirectedSpotifyAccess");
    //arguments.put("refresh_token ",refreshToken);
    StringBuilder sj = new StringBuilder();
    for(Map.Entry<String,String> entry : arguments.entrySet()) {
    	logger.debug(" ref",entry.getKey());
    	if(entry.getKey().toString().equalsIgnoreCase("refresh_token")){
    		logger.debug("inside refreshTOken",entry.getValue());
    		sj.append(entry.getKey() + "=" + entry.getValue() + "&");
    	}else{
    		logger.debug("outside refreshTOken",entry.getValue());
    		sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
    	}
    }
    
    sj.append("refresh_token="+refreshToken);
    byte[] out = sj.toString().getBytes();
 
    con.setFixedLengthStreamingMode(out.length);
   
    con.connect();
  /*  try{
    int response=con.getResponseCode();
    logger.debug("REsu",response);
    }catch(Exception e){
    	logger.error("REs",e);
    }*/
    	try{
    		
		        OutputStream os = con.getOutputStream();
		        os.write(out);
		        
		        BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				//print result
				logger.debug(response.toString());
				
				newAccessToken=response.toString();

		    }catch (Exception e)
		    {
		    	logger.error(e);
		    }
   		
		
		return newAccessToken;
	}








	
	public SpotifyInfo save(String access_token, String refresh_token,String expires_in) throws MightyAppException {
		SpotifyInfo  spotify=null;
					 spotify=new SpotifyInfo();
					 spotify.setAccessToken(access_token);
					 spotify.setRefreshToken(refresh_token);
					 spotify.setExpiresIn(expires_in);
					 					 
		return spotifyDao.save(spotify);
	}




	public SpotifyInfo getSpotifyInfoByTokenId(String tokenId)	throws MightyAppException {
			return spotifyDao.getSpotifyInfoByTokenId(Integer.valueOf(tokenId));
	}


	public SpotifyInfo update(SpotifyInfo spotifyInfo) throws MightyAppException {
			return spotifyDao.save(spotifyInfo);
	}



	
	public String spotifyAccessToken(String code, String client_id,String client_secret, String redirect_uri) throws Exception {
		String tokens=null;
		
		 String url = "https://accounts.spotify.com/api/token";
		 SSLContext context = SSLContext.getInstance("TLS"); 
	     context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
	             public void checkClientTrusted(X509Certificate[] chain, 
	                             String authType) throws CertificateException {} 
	             public void checkServerTrusted(X509Certificate[] chain, 
	                             String authType) throws CertificateException {} 
	             public X509Certificate[] getAcceptedIssuers() { 
	                     return new X509Certificate[0]; 
	             }}}, new SecureRandom()); 
	     HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory()); 
	  
	   	URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setDoOutput(true);
	    Map<String,String> arguments = new HashMap<String, String>();
	    arguments.put("code", code);
	    arguments.put("grant_type", "authorization_code");
	    arguments.put("client_id", client_id);
	    arguments.put("client_secret", client_secret);
	    arguments.put("redirect_uri", redirect_uri);
	    StringBuilder sj = new StringBuilder();
	    for(Map.Entry<String,String> entry : arguments.entrySet()) {
	        sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
	    }
	    byte[] out = sj.toString().getBytes();

	    con.setFixedLengthStreamingMode(out.length);
	    con.connect();
	    
			    try
			    {
			        OutputStream os = con.getOutputStream();
			        os.write(out);
			        
			        BufferedReader in = new BufferedReader(
					        new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					logger.debug("HiiiiiVikkki",response.toString());
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					//print result
					logger.debug("Hiiiiii",response.toString());
					
					tokens=response.toString();

			    }
			    catch (Exception e)
			    {
			    	logger.error(e);
			    	throw new Exception(e.getMessage()+HttpStatus.BAD_REQUEST);
			    }
		
			    	return tokens;
	}


	public String refreshSpotifyAccessToken(String refreshToken,String client_id, String client_secret) throws Exception {
		String newAccessToken=null;
		logger.debug("refreshTOken",refreshToken);
		String url = "https://accounts.spotify.com/api/token";
	 SSLContext context = SSLContext.getInstance("TLS"); 
     context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
             public void checkClientTrusted(X509Certificate[] chain, 
                             String authType) throws CertificateException {} 
             public void checkServerTrusted(X509Certificate[] chain, 
                             String authType) throws CertificateException {} 
             public X509Certificate[] getAcceptedIssuers() { 
                     return new X509Certificate[0]; 
             }}}, new SecureRandom()); 
     HttpsURLConnection.setDefaultSSLSocketFactory( 
                     context.getSocketFactory()); 
  
   	URL obj = new URL(url);
	HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

	con.setRequestMethod("POST");
	con.setDoOutput(true);
    Map<String,String> arguments = new HashMap<String, String>();
    arguments.put("grant_type", "refresh_token");
    arguments.put("client_id", client_id);
    arguments.put("client_secret", client_secret);
    //arguments.put("redirect_uri", "https://mighty2.cloudaccess.host/test1/spotifyaccess/RedirectedSpotifyAccess");
    //arguments.put("refresh_token ",refreshToken);
    StringBuilder sj = new StringBuilder();
    for(Map.Entry<String,String> entry : arguments.entrySet()) {
    	logger.debug(" ref",entry.getKey());
    	if(entry.getKey().toString().equalsIgnoreCase("refresh_token")){
    		logger.debug("inside refreshTOken",entry.getValue());
    		sj.append(entry.getKey() + "=" + entry.getValue() + "&");
    	}else{
    		logger.debug("outside refreshTOken",entry.getValue());
    		sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
    	}
    }
    
    sj.append("refresh_token="+refreshToken);
    byte[] out = sj.toString().getBytes();
 
    con.setFixedLengthStreamingMode(out.length);
   
    con.connect();
  /*  try{
    int response=con.getResponseCode();
    logger.debug("REsu",response);
    }catch(Exception e){
    	logger.error("REs",e);
    }*/
    	try{
    		
		        OutputStream os = con.getOutputStream();
		        os.write(out);
		        
		        BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				//print result
				logger.debug(response.toString());
				
				newAccessToken=response.toString();

		    }catch (Exception e)
		    {
		    	logger.error(e);
		    }
   		
		
		return newAccessToken;
	}

	
	
}	
