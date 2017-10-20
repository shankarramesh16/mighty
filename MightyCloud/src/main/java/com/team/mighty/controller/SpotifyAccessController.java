package com.team.mighty.controller;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.domain.SpotifyInfo;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.SpotifyAccessService;
import com.team.mighty.utils.JsonUtil;
import com.team.mighty.utils.StringUtil;

/**
 * 
 * @author Shankara
 *
 */
@RestController
@RequestMapping(MightyAppConstants.SPOTIFY_API)
public class SpotifyAccessController {
	
	@Autowired
	private SpotifyAccessService spotifyAccessService;
	
	private static final MightyLogger logger = MightyLogger.getLogger(SpotifyAccessController.class);
	
	@RequestMapping(value = "/spotifyToken", method = RequestMethod.POST)
	public ResponseEntity<String> spotifyTokenOnCodeHandler(@RequestBody String received) throws Exception {
		logger.info(" /POST /spotifyToken");
		logger.debug("/spotifyToken Received",received);
		
		JSONObject json=null;
		ResponseEntity<String> responseEntity = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		
		try{		
			json=new JSONObject();
			json=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			logger.error("System Exception during parsing JSON",e);
		}
		
		
		
		try {

				String code=(String)json.get("code");
				String client_secret=(String)json.get("client_secret");
				String client_id=(String)json.get("client_id");
				String redirect_uri=(String)json.get("redirect_uri");
				
				logger.debug("code/",code);
				logger.debug("client_secret/",client_secret);
				logger.debug("client_id/",client_id);
				logger.debug("redirect_uri/",redirect_uri);
				
				String tokens=spotifyAccessService.spotifyAccessToken(code,client_id,client_secret,redirect_uri);
				 if(code!=null && client_secret!=null && client_id!=null && redirect_uri!=null){
				logger.debug("Result",tokens);
				
						JSONObject obj=null;
						try{		
								obj=new JSONObject();
								obj=(JSONObject)new JSONParser().parse(tokens);
						}catch(Exception e){
							logger.error("System Exception during parsing JSON",e);
						}
					
				logger.debug("access_token",obj.get("access_token"));
				logger.debug("refresh_token",obj.get("refresh_token"));
				logger.debug("expires_in",obj.get("expires_in"));
				
				SpotifyInfo spotifyInfo=spotifyAccessService.save(String.valueOf(obj.get("access_token")),String.valueOf(obj.get("refresh_token")),String.valueOf(obj.get("expires_in")));
				
				if(spotifyInfo!=null){
										String response=JsonUtil.objToJson(spotifyInfo);
				responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
				}else{
					responseEntity = new ResponseEntity<String>("System error",HttpStatus.BAD_REQUEST);
				}
				
		}else{
			responseEntity = new ResponseEntity<String>("Empty or null / code / client_id / client_secret / redirect_uri",HttpStatus.EXPECTATION_FAILED);
		}
			
		} catch(MightyAppException e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getHttpStatus());
		}
		
		return responseEntity;
	}

	@RequestMapping(value = "/RedirectedSpotifyAccess", method = RequestMethod.GET)
	public ResponseEntity<String> getSpotifyClientCode(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "state", required = false) String state) throws Exception {
		logger.info(" Code","[" ,code ,"]", "Error ", error, "State", state);
		logger.info(" /Get IN RedirectedSpotifyAccess");
		ResponseEntity<String> responseEntity = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		try {

			if(StringUtil.isEmpty(error) ) {
				String tokens=spotifyAccessService.getAccessToken(code, error, state);
				logger.debug("Result",tokens);
				
						JSONObject obj=null;
						try{		
								obj=new JSONObject();
								obj=(JSONObject)new JSONParser().parse(tokens);
						}catch(Exception e){
							logger.error("System Exception during parsing JSON",e);
						}
					
				logger.debug("access_token",obj.get("access_token"));
				logger.debug("refresh_token",obj.get("refresh_token"));
				logger.debug("expires_in",obj.get("expires_in"));
				
				SpotifyInfo spotifyInfo=spotifyAccessService.save(String.valueOf(obj.get("access_token")),String.valueOf(obj.get("refresh_token")),String.valueOf(obj.get("expires_in")));
				
				if(spotifyInfo!=null){
					httpHeaders.add("id", String.valueOf(spotifyInfo.getId()));
					httpHeaders.add("access_token", spotifyInfo.getAccessToken());
					httpHeaders.add("refresh_token", spotifyInfo.getRefreshToken());	
					httpHeaders.add("expire", spotifyInfo.getExpiresIn());
				responseEntity = new ResponseEntity<String>("Spotify Login Successful "+tokens+"<div><input type=hidden value="+spotifyInfo.getId()+"></div>",httpHeaders,HttpStatus.OK);
				}else{
					responseEntity = new ResponseEntity<String>("System error",HttpStatus.BAD_REQUEST);
				}
			}else{
				throw new MightyAppException(error, HttpStatus.FORBIDDEN);
			}
		} catch(MightyAppException e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getHttpStatus());
		}
		
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/authorizeSpotify", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> spotifyAccessTokenHandler(HttpServletResponse response) throws Exception {
		logger.info("/GET authorizeSpotify");
		ResponseEntity<String> responseEntity = null;
		try{
			String client_id="8cda18d9034947759f0b09e68e17c7c1";
			String response_type="code";
			String scope="user-read-private user-read-email streaming";
			String redirect_uri="https://mighty2.cloudaccess.host/test1/spotifyaccess/RedirectedSpotifyAccess";
			//String redirect_uri="http://192.168.0.11:8080/MightyCloud/spotifyaccess/RedirectedSpotifyAccess";
			String url = "https://accounts.spotify.com/authorize?client_id="+client_id+"&response_type="+response_type+"&redirect_uri="+redirect_uri+"&scope="+scope;
			logger.debug("status",response.getStatus());
			response.sendRedirect(url);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		}catch(Exception e){
			responseEntity = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			logger.debug("Error in",e);
		}
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/getRefreshSpotifyToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRefreshSpotifyTokenhandler(@RequestBody String received) throws Exception {
		logger.info(" /Get IN SpotifyRefreshToken");
		ResponseEntity<String> responseEntity = null;
		JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			responseEntity = new ResponseEntity<String>("System exception while json received parser ",HttpStatus.METHOD_NOT_ALLOWED);
		}
		
				
		
		
		try {		
				logger.debug("Refresh Token",obj.get("refresh_token").toString());
				logger.debug("ID",obj.get("id").toString());
								
				String newAccestoken=spotifyAccessService.getRefreshSpotifyToken(obj.get("refresh_token").toString());
				
									JSONObject obj1=null;
									try{		
										obj1=new JSONObject();
										obj1=(JSONObject)new JSONParser().parse(newAccestoken);
									}catch(Exception e){
										responseEntity = new ResponseEntity<String>("System exception while json newAccessToken parser ",HttpStatus.METHOD_NOT_ALLOWED);
									}
									
				SpotifyInfo spotifyInfo=spotifyAccessService.getSpotifyInfoByTokenId(obj.get("id").toString());
				if(spotifyInfo!=null){
					spotifyInfo.setAccessToken(obj1.get("access_token").toString());
					SpotifyInfo spotify=spotifyAccessService.update(spotifyInfo);
					String response = JsonUtil.objToJson(spotify);
					responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
				}else{
					responseEntity = new ResponseEntity<String>("Refresh Token Id is not found",HttpStatus.BAD_REQUEST);
				}
				
				
			
		}catch(MightyAppException e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getHttpStatus());
		}
		
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/refreshSpotifyAccessToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> reNewSpotifyAccessTokenHanlder(@RequestBody String received) throws Exception {
		logger.info(" /Get IN SpotifyRefreshToken");
		ResponseEntity<String> responseEntity = null;
		JSONObject obj=null;
		try{		
				obj=new JSONObject();
				obj=(JSONObject)new JSONParser().parse(received);
		}catch(Exception e){
			responseEntity = new ResponseEntity<String>("System exception while json received parser ",HttpStatus.METHOD_NOT_ALLOWED);
		}
		
				
		
		
		try {		
				logger.debug("Refresh Token",obj.get("refresh_token").toString());
				logger.debug("ID",obj.get("id").toString());
				logger.debug("client_id",obj.get("client_id").toString());
				logger.debug("client_secret",obj.get("client_secret").toString());
				
				if(obj.get("refresh_token").toString()!=null && obj.get("client_id").toString()!=null && obj.get("client_secret").toString()!=null && obj.get("id").toString()!=null){
					String refresh_token=obj.get("refresh_token").toString();
					String client_id=obj.get("client_id").toString();
					String client_secret=obj.get("client_secret").toString();
				String newAccestoken=spotifyAccessService.refreshSpotifyAccessToken(refresh_token,client_id,client_secret);
				
									JSONObject obj1=null;
									try{		
										obj1=new JSONObject();
										obj1=(JSONObject)new JSONParser().parse(newAccestoken);
									}catch(Exception e){
										responseEntity = new ResponseEntity<String>("System exception while json newAccessToken parser ",HttpStatus.METHOD_NOT_ALLOWED);
									}
									
				SpotifyInfo spotifyInfo=spotifyAccessService.getSpotifyInfoByTokenId(obj.get("id").toString());
				if(spotifyInfo!=null){
					spotifyInfo.setAccessToken(obj1.get("access_token").toString());
					SpotifyInfo spotify=spotifyAccessService.update(spotifyInfo);
					String response = JsonUtil.objToJson(spotify);
					responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
				}else{
					responseEntity = new ResponseEntity<String>("Refresh Token Id is not found",HttpStatus.NOT_FOUND);
				}
				
		}else{
			responseEntity = new ResponseEntity<String>("Empty or null /refresh_token /id /client_id /client_secret",HttpStatus.BAD_REQUEST);
		}
			
		}catch(Exception e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/getTokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getSpotifyTokensHandler(@RequestBody String received) throws Exception {
		logger.info(" /POST IN SpotifyTokens");
		JSONObject obj=null;
		ResponseEntity<String> responseEntity = null;
	
			   try{		
						obj=new JSONObject();
						obj=(JSONObject)new JSONParser().parse(received);
				}catch(Exception e){
					logger.error("System Exception during parsing JSON",e);
				}
		
						
		try{		
			logger.error("Token ID as",String.valueOf(obj.get("tokenID")));
			SpotifyInfo spotifyInfo=spotifyAccessService.getSpotifyInfoByTokenId(String.valueOf(obj.get("tokenID")));
			if(spotifyInfo!=null){
				String response = JsonUtil.objToJson(spotifyInfo);
				responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
			}else{
				responseEntity = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		}catch(MightyAppException e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getHttpStatus());
		}
		
		return responseEntity;
	}
}
