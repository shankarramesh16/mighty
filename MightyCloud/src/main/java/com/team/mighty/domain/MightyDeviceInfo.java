package com.team.mighty.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="register_at")
	private Date registerAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upgrade_at")
	private Date upgradeAt;
	
	public Date getRegisterAt() {
		return registerAt;
	}

	public void setRegisterAt(Date registerAt) {
		this.registerAt = registerAt;
	}

	public Date getUpgradeAt() {
		return upgradeAt;
	}

	public void setUpgradeAt(Date upgradeAt) {
		this.upgradeAt = upgradeAt;
	}

	@Column(name ="IS_REGISTERED")
	private String isRegistered;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	
	@Column(name = "AppVersion")
	private Float appVersion;
	
	@Column(name = "AppBuild")
	private String appBuild;
	
	//bi-directional many-to-one association to TblDeviceOrderInfo
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ORDER_ID")
		private MightyDeviceOrderInfo deviceOrderInfo;
	

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
	
	public Float getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Float appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppBuild() {
		return appBuild;
	}

	public void setAppBuild(String appBuild) {
		this.appBuild = appBuild;
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
	
	
	public MightyDeviceOrderInfo getDeviceOrderInfo() {
		return deviceOrderInfo;
	}

	public void setDeviceOrderInfo(MightyDeviceOrderInfo deviceOrderInfo) {
		this.deviceOrderInfo = deviceOrderInfo;
	}
}
