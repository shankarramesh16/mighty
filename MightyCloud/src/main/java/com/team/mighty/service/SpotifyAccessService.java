package com.team.mighty.service;

import com.team.mighty.exception.MightyAppException;

/**
 * 
 * @author Shankara
 *
 */
public interface SpotifyAccessService {

	public String getAccessToken(String code, String error, String state) throws MightyAppException;

	public void spotifyAccessToken()throws MightyAppException;
}
