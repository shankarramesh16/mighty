package com.team.mighty.dto;

import java.io.Serializable;

public class MightyUsersByVersionDto extends BaseResponseDTO implements Serializable {
	static final long serialVersionUID = 1L;

	private String userCount;
	
	private String version;

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
