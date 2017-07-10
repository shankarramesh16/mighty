package com.team.mighty.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the mightylog database table.
 * 
 */
@Entity
@Table(name = "mightylog")
@NamedQuery(name="Mightylog.findAll", query="SELECT m FROM Mightylog m")
public class Mightylog extends BaseEntityInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="descriptions")
	private String description;

	@Column(name="device_type")
	private String deviceType;
	
	@Column(name="phoneDeviceOSVersion")
	private String phoneDeviceOSVersion;
	
	public String getPhoneDeviceOSVersion() {
		return phoneDeviceOSVersion;
	}



	public void setPhoneDeviceOSVersion(String phoneDeviceOSVersion) {
		this.phoneDeviceOSVersion = phoneDeviceOSVersion;
	}

	

	public String getTicket() {
		return ticket;
	}



	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Column(name="ticket")
	private String ticket;
	
	private String deviceId;

	private String emailId;

	@Lob
	@Column(name="file_content")
	private Blob fileContent;

	public Blob getFileContent() {
		return fileContent;
	}

	@Column(name="devReg")
	private String devReg;

	public String getDevReg() {
		return devReg;
	}



	public void setDevReg(String devReg) {
		this.devReg = devReg;
	}



	public void setFileContent(Blob fileContent) {
		this.fileContent = fileContent;
	}

	@Column(name="file_name")
	private String fileName;

	@Column(name="log_type")
	private String logType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	private String username;

	public Mightylog() {
	}

	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}