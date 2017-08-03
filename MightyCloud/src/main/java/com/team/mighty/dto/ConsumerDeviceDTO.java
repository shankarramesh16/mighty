package com.team.mighty.dto;

import java.io.Serializable;
import java.util.Date;

public class ConsumerDeviceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	private Long id;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private String appOS;
	public String getAppOS() {
		return appOS;
	}

	public void setAppOS(String appOS) {
		this.appOS = appOS;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	private String appType;
	
	private String deviceId;
	public Date getPwdChangedDate() {
		return pwdChangedDate;
	}

	public void setPwdChangedDate(Date pwdChangedDate) {
		this.pwdChangedDate = pwdChangedDate;
	}

	private Date pwdChangedDate;
	
	private String deviceName;
	
	private String deviceModel;
	
	private String deviceType;
	
	private String deviceOs;
	
	private String deviceOsVersion;
	
	private String mightyDeviceId;
	private String usrdevReg;
	private String devReg;
	
	public String getUsrdevReg() {
		return usrdevReg;
	}

	public void setUsrdevReg(String usrdevReg) {
		this.usrdevReg = usrdevReg;
	}

	public String getDevReg() {
		return devReg;
	}

	public void setDevReg(String devReg) {
		this.devReg = devReg;
	}

	private String emailId;
	private Date createdDt;
	private Date updatedDt;
	
	

	private String password;
	private String userStatus;
	private String facebookID;
	
	
	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	private String userIndicator;
	private String gender;
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	private String age;
	
	private long userId;
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserIndicator() {
		return userIndicator;
	}

	public void setUserIndicator(String userIndicator) {
		this.userIndicator = userIndicator;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public String getDeviceOsVersion() {
		return deviceOsVersion;
	}

	public void setDeviceOsVersion(String deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}

	public String getMightyDeviceId() {
		return mightyDeviceId;
	}

	public void setMightyDeviceId(String mightyDeviceId) {
		this.mightyDeviceId = mightyDeviceId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
