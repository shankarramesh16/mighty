package com.team.mighty.dto;

import java.io.Serializable;
import java.util.Date;

public class BaseResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String statusDesc;
	
	private String statusCode;
	
	private String apiToken;
	private String baseToken;
	private String accessToken;
	
	private Date accessTokenExpDate;
	public Date getAccessTokenExpDate() {
		return accessTokenExpDate;
	}

	public void setAccessTokenExpDate(Date accessTokenExpDate) {
		this.accessTokenExpDate = accessTokenExpDate;
	}

	public Date getBaseTokenExpDate() {
		return baseTokenExpDate;
	}

	public void setBaseTokenExpDate(Date baseTokenExpDate) {
		this.baseTokenExpDate = baseTokenExpDate;
	}

	private Date baseTokenExpDate;

	public String getBaseToken() {
		return baseToken;
	}

	public void setBaseToken(String baseToken) {
		this.baseToken = baseToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
}
