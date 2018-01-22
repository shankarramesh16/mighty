package com.team.mighty.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the mightyotadevices database table.
 * 
 */
@Entity
@Table(name="mightyotadevices")
@NamedQuery(name="Mightyotadevice.findAll", query="SELECT m FROM Mightyotadevice m")
public class Mightyotadevice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	public long getId() {
		return id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;


	public Date getCreatedDt() {
		return createdDt;
	}



	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}



	public void setId(long id) {
		this.id = id;
	}

	private String devices;
	
	@Column(name="version")
	private String version;
	
	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}

	private String username;

	public Mightyotadevice() {
	}

	

	public String getDevices() {
		return this.devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

}