package com.team.mighty.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TBL_MIGHTY_DEVICE_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MightyDeviceInfo extends BaseEntityInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	
	@Column(name = "DEVICE_ID")
	private String deviceId;
	
	@Column(name = "DEVICE_NAME")
	private String deviceName;
	
	@Column(name = "DEVICE_TYPE")
	private String deviceType;
	
	@Column(name = "SW_VERSION")
	private String swVersion;
	
	@Column(name = "HW_SERIALNO")
	private String hwSerialNo;
	
	@Column(name ="IS_REGISTERED")
	private String isRegistered;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDeviceType() {
		return deviceType;
	}
	
	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	public String getHwSerialNo() {
		return hwSerialNo;
	}

	public void setHwSerialNo(String hwSerialNo) {
		this.hwSerialNo = hwSerialNo;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(String isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
