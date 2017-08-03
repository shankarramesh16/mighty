package com.team.mighty.domain;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the mightydlauditlog database table.
 * 
 */
@Entity
@Table(name = "mightydlauditlog")
@NamedQuery(name="Mightydlauditlog.findAll", query="SELECT m FROM Mightydlauditlog m")
public class Mightydlauditlog extends BaseEntityInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String app_OS;

	@Column(name="app_ver")
	private String appVer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createddt;

	private String deviceId;

	@Column(name="download_curr_ver")
	private String downloadCurrVer;

	@Column(name="download_perc")
	private String downloadPerc;

	@Column(name="error_code")
	private String errorCode;

	@Column(name="internet_conn")
	private String internetConn;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateddt;

	

	@Column(name="wifi_status")
	private String wifiStatus;
	
	@Column(name="ble_status")
	private String bleStatus;

	public Mightydlauditlog() {
	}

	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getApp_OS() {
		return this.app_OS;
	}

	public void setApp_OS(String app_OS) {
		this.app_OS = app_OS;
	}
	public String getBleStatus() {
		return bleStatus;
	}

	public void setBleStatus(String bleStatus) {
		this.bleStatus = bleStatus;
	}

	public String getAppVer() {
		return this.appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public Date getCreateddt() {
		return this.createddt;
	}

	public void setCreateddt(Date createddt) {
		this.createddt = createddt;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDownloadCurrVer() {
		return this.downloadCurrVer;
	}

	public void setDownloadCurrVer(String downloadCurrVer) {
		this.downloadCurrVer = downloadCurrVer;
	}

	public String getDownloadPerc() {
		return this.downloadPerc;
	}

	public void setDownloadPerc(String downloadPerc) {
		this.downloadPerc = downloadPerc;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getInternetConn() {
		return this.internetConn;
	}

	public void setInternetConn(String internetConn) {
		this.internetConn = internetConn;
	}

	public Date getUpdateddt() {
		return this.updateddt;
	}

	public void setUpdateddt(Date updateddt) {
		this.updateddt = updateddt;
	}

	public String getWifiStatus() {
		return this.wifiStatus;
	}

	public void setWifiStatus(String wifiStatus) {
		this.wifiStatus = wifiStatus;
	}

}