package com.team.mighty.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MIGHTY_KEY_CONFIG")
public class MightyKeyConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private long id;
	
	@Column(name = "MIGHTY_KEY_NAME")
	private String mightyKeyName;
	
	@Column(name = "MIGHTY_KEY_VALUE")
	private String mightyKeyValue;

	@Column(name = "IS_ENABLED")
	private String isEnabled;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CREATED_DT")
	private Date createdDt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMightyKeyName() {
		return mightyKeyName;
	}

	public void setMightyKeyName(String mightyKeyName) {
		this.mightyKeyName = mightyKeyName;
	}

	public String getMightyKeyValue() {
		return mightyKeyValue;
	}

	public void setMightyKeyValue(String mightyKeyValue) {
		this.mightyKeyValue = mightyKeyValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	} 

}
