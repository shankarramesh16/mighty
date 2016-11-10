package com.team.mighty.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MIGHTY_USR_DEV_MAP")
public class MightyDeviceUserMapping extends BaseEntityInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private MightyUserInfo mightyUserInfo;
	
	@ManyToMany(mappedBy="mightyUsrDevMaps1")
	private List<MightyUserInfo> mightyUserInfos;
		
	@Column(name = "PHONE_DEVICE_ID")
	private String phoneDeviceId;
	
	@Column(name = "PHONE_DEVICE_TYPE")
	private String phoneDeviceType;
	
	@Column(name = "PHONE_DEVICE_VERSION")
	private String phoneDeviceVersion;
	
	@Column(name = "PHONE_DEVICE_OS_VERSION")
	private String phoneDeviceOSVersion;
	
	@Column(name = "MIGHTY_DEVICE_ID")
	private long mightyDeviceId;
	
	@Column(name = "STATUS")
	private String registrationStatus;
	
	@Column(name = "CREATED_DT")
	private Date createdDt;
	
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhoneDeviceId() {
		return phoneDeviceId;
	}

	public void setPhoneDeviceId(String phoneDeviceId) {
		this.phoneDeviceId = phoneDeviceId;
	}

	public String getPhoneDeviceType() {
		return phoneDeviceType;
	}

	public void setPhoneDeviceType(String phoneDeviceType) {
		this.phoneDeviceType = phoneDeviceType;
	}

	public String getPhoneDeviceVersion() {
		return phoneDeviceVersion;
	}

	public void setPhoneDeviceVersion(String phoneDeviceVersion) {
		this.phoneDeviceVersion = phoneDeviceVersion;
	}

	public String getPhoneDeviceOSVersion() {
		return phoneDeviceOSVersion;
	}

	public void setPhoneDeviceOSVersion(String phoneDeviceOSVersion) {
		this.phoneDeviceOSVersion = phoneDeviceOSVersion;
	}

	public long getMightyDeviceId() {
		return mightyDeviceId;
	}

	public void setMightyDeviceId(long mightyDeviceId) {
		this.mightyDeviceId = mightyDeviceId;
	}

	public MightyUserInfo getMightyUserInfo() {
		return mightyUserInfo;
	}
	
	public List<MightyUserInfo> getMightyUserInfos() {
		return mightyUserInfos;
	}

	public void setMightyUserInfos(List<MightyUserInfo> mightyUserInfos) {
		this.mightyUserInfos = mightyUserInfos;
	}


	public void setMightyUserInfo(MightyUserInfo mightyUserInfo) {
		this.mightyUserInfo = mightyUserInfo;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

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
	
	
	
}
