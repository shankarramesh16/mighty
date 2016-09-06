package com.team.mighty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.mighty.constant.MightyAppConstants;
import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;
import com.team.mighty.service.SpotifyAccessService;

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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> getSpotifyClientCode(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "state", required = false) String state) {
		logger.info(" Code","[" ,code ,"]", "Error ", error, "State", state);
		ResponseEntity<String> responseEntity = null;
		try {
			spotifyAccessService.getAccessToken(code, error, state);
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
		} catch(MightyAppException e) {
			logger.error(e);
			responseEntity = new ResponseEntity<String>(e.getHttpStatus());
		}
		
		return responseEntity;
	}
}
