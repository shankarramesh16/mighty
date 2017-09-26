package com.team.mighty.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the mighty_spotify database table.
 * 
 */
@Entity
@Table(name="mighty_spotify")
@NamedQuery(name="MightySpotify.findAll", query="SELECT m FROM MightySpotify m")
public class MightySpotify implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createddt;

	private String deviceId;

	@Column(name="m_username")
	private String mUsername;

	@Column(name="sp_username")
	private String spUsername;
	
	@Column(name="sw_version")
	private String swVersion;

	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateddt;

	public MightySpotify() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMUsername() {
		return this.mUsername;
	}

	public void setMUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	public String getSpUsername() {
		return this.spUsername;
	}

	public void setSpUsername(String spUsername) {
		this.spUsername = spUsername;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateddt() {
		return this.updateddt;
	}

	public void setUpdateddt(Date updateddt) {
		this.updateddt = updateddt;
	}

}