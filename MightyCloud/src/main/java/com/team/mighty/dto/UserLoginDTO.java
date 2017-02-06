package com.team.mighty.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserLoginDTO  extends BaseResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Response Details
	private String userStatus;
	
	private long userId;
	private Date pwdChangedDate;
	private String deviceId;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String pwd;
	private String newPwd;
	
	private String userName;
	private List<String> lstMightyDeviceId;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public Date getPwdChangedDate() {
		return pwdChangedDate;
	}

	public void setPwdChangedDate(Date pwdChangedDate) {
		this.pwdChangedDate = pwdChangedDate;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public List<String> getLstMightyDeviceId() {
		return lstMightyDeviceId;
	}

	public void setLstMightyDeviceId(List<String> lstMightyDeviceId) {
		this.lstMightyDeviceId = lstMightyDeviceId;
	}

}
