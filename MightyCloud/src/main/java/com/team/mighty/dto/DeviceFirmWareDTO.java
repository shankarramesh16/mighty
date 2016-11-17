package com.team.mighty.dto;

import java.sql.Date;
import java.util.List;

public class DeviceFirmWareDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String latestVersion;
	
	private String latestVersionLink;
	
	private Date effectiveDt;
	
	private Date createdDt;
	
	private String fileDownloadUrl;
	
	private List<DeviceFirmWareDTO> lstPreviousVersion;
	
	private String hashValue;
	private int hastType;

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public int getHastType() {
		return hastType;
	}

	public void setHastType(int hastType) {
		this.hastType = hastType;
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

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
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
