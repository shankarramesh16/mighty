package com.team.mighty.service;

import com.team.mighty.domain.SpotifyInfo;
import com.team.mighty.exception.MightyAppException;


/**
 * 
 * @author Shankara
 *
 */
public interface SpotifyAccessService {

	public String getAccessToken(String code, String error, String state) throws Exception;
	public String getRefreshSpotifyToken(String refreshToken)throws Exception;
	public SpotifyInfo save(String access_token, String refresh_token, String expires_in) throws MightyAppException;
	public SpotifyInfo getSpotifyInfoByTokenId(String tokenId)throws MightyAppException;
	public SpotifyInfo update(SpotifyInfo spotifyInfo)throws MightyAppException;
	
}
