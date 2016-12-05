package com.team.mighty.dto;


import java.util.Date;
import java.util.List;

public class DeviceFirmWareDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String latestVersion;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	private String latestVersionLink;
	
	private Date effectiveDt;
	
	private Date createdDt;
	
	private String version;
	private String fileName;
	private String status;
	private Date updatedDt;
	
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	private String fileDownloadUrl;
	private String fileSize;
	
	private List<DeviceFirmWareDTO> lstPreviousVersion;
	
	private String hashValue;
	private int hashType;

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

	public String getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getLatestVersionLink() {
		return latestVersionLink;
	}

	public void setLatestVersionLink(String latestVersionLink) {
		this.latestVersionLink = latestVersionLink;
	}

	public Date getEffectiveDt() {
		return effectiveDt;
	}

	public void setEffectiveDt(Date effectiveDt) {
		this.effectiveDt = effectiveDt;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(java.util.Date date) {
		this.createdDt = date;
	}

	public List<DeviceFirmWareDTO> getLstPreviousVersion() {
		return lstPreviousVersion;
	}

	public void setLstPreviousVersion(List<DeviceFirmWareDTO> lstPreviousVersion) {
		this.lstPreviousVersion = lstPreviousVersion;
	}

	public String getFileDownloadUrl() {
		return fileDownloadUrl;
	}

	public void setFileDownloadUrl(String fileDownloadUrl) {
		this.fileDownloadUrl = fileDownloadUrl;
	}

}
