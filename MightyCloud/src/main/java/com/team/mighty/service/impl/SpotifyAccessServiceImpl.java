package com.team.mighty.service.impl;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.team.mighty.constant.MightyConfigConstants;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.SpotifyAccessService;
import com.team.mighty.utils.JsonUtil;
import com.team.mighty.utils.SpringPropertiesUtil;
import com.team.mighty.utils.StringUtil;

@Component("spotifyAccessService")
public class SpotifyAccessServiceImpl implements SpotifyAccessService {

	private static final MightyLogger logger = MightyLogger.getLogger(SpotifyAccessServiceImpl.class);
	
	public String getAccessToken(String code, String error, String state) throws MightyAppException {
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
	}

	
	public void spotifyAccessToken() throws MightyAppException {
		
		try{
				 String client_id="8cda18d9034947759f0b09e68e17c7c1";
				 String response_type="code";
				 String redirect_uri="http://localhost:8088/MightyCloud/spotifyaccess/RedirectedSpotifyAccess";
				 
				 String url = "https://accounts.spotify.com/authorize?client_id="+client_id+"&response_type="+response_type+"&redirect_uri="+redirect_uri;
			
					
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
			
				// optional default is GET
				con.setRequestMethod("GET");
			
				//add request header
			
				int responseCode = con.getResponseCode();
				logger.debug("\nSending 'GET' request to URL : " + url);
				logger.debug("Response Code : " + responseCode);
			
				/*BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
			
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();*/
			
			

		
  			}catch(Exception e){
  						throw new MightyAppException("Invalid Spotify access token", HttpStatus.EXPECTATION_FAILED);
  			}

		}
	
}	
