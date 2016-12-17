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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TBL_MIGHTY_DEVICE_FIRMWARE")
public class MightyDeviceFirmware implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private String id;
	
	@Column(name="FILE")
	@Lob
	private Blob file;
	
	/*@Column(name="FILE")
	@Lob
	private byte[] file;
	
	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}*/

	@Column(name = "VERSION")
	private String version;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "REQUIRES")
	private Float requires;
	
	@Column(name = "COMPATIBLE_IOS")
	private Float compatibleIOS;
	
	
	@Column(name = "COMPATIBLE_AND")
	private String compatibleAND;
	
	@Column(name = "COMPATIBLE_HW")
	private String compatibleHW;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "HASH_VALUE")
	private String hashValue;
	
	
	@Column(name = "HASH_TYPE")
	private int hashType;
	
	@Column(name = "CREATED_DT")
	private Date createdDt;
	
	@Column(name = "UPDATED_DT")
	private Date updatedDt;
	
	@Column(name = "EFFECTIVE_DT")
	private Date effectiveDt;
	
	@Transient
	private String errorCode;
	
	@Transient
	private String errorDesc;

	public String getId() {
		return id;
	}
	
	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public int getHashType() {
		return hashType;
	}

	public void setHashType(int hashType) {
		this.hashType = hashType;
	}


	public void setId(String id) {
		this.id = id;
	}
	public Blob getFile() {
		return this.file;
	}

	public void setFile(Blob file) {
		this.file = file;
	}

	public String getVersion() {
		return version;
	}
	public Float getRequires() {
		return requires;
	}

	public void setRequires(Float requires) {
		this.requires = requires;
	}

	public Float getCompatibleIOS() {
		return compatibleIOS;
	}

	public void setCompatibleIOS(Float compatibleIOS) {
		this.compatibleIOS = compatibleIOS;
	}

	public String getCompatibleAND() {
		return compatibleAND;
	}

	public void setCompatibleAND(String compatibleAND) {
		this.compatibleAND = compatibleAND;
	}

	public String getCompatibleHW() {
		return compatibleHW;
	}

	public void setCompatibleHW(String compatibleHW) {
		this.compatibleHW = compatibleHW;
	}


	public void setVersion(String version) {
		this.version = version;
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

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public Date getEffectiveDt() {
		return effectiveDt;
	}

	public void setEffectiveDt(Date effectiveDt) {
		this.effectiveDt = effectiveDt;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
