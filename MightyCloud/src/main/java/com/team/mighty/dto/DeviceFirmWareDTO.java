package com.team.mighty.dto;


import java.util.Date;
import java.util.List;

public class DeviceFirmWareDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	
	private String latestVersion;
	private String latestVersionLink;
	private Float latestRequired;
	private Date effectiveDt;
	private Date createdDt;
	private String version;
	private String fileName;
	private String status;
	private Date updatedDt;
	private Float requires;
	private Float compatibleIOS;
	private String compatibleAND;
	private String compatibleHW;
	private String fileDownloadUrl;
	private String fileSize;
	private String hashValue;
	private int hashType;
	private String ht;
	public String getHt() {
		return ht;
	}

	public void setHt(String ht) {
		this.ht = ht;
	}

	public String getReqHT() {
		return reqHT;
	}

	public void setReqHT(String reqHT) {
		this.reqHT = reqHT;
	}

	private List<DeviceFirmWareDTO> lstPreviousVersion;
	
	
	
	private String reqLatestVersion;
	private Float reqCompatibleIOS;
	private String reqCompatibleLatestAND;
	private String reqCompatibleLatestHW;
	private String reqHashValue;
	private String reqHT;
	
	
	public String getReqLatestVersion() {
		return reqLatestVersion;
	}

	public void setReqLatestVersion(String reqLatestVersion) {
		this.reqLatestVersion = reqLatestVersion;
	}
	public Float getLatestRequired() {
		return latestRequired;
	}

	public void setLatestRequired(Float latestRequired) {
		this.latestRequired = latestRequired;
	}


	public Float getReqCompatibleIOS() {
		return reqCompatibleIOS;
	}

	public void setReqCompatibleIOS(Float reqCompatibleIOS) {
		this.reqCompatibleIOS = reqCompatibleIOS;
	}

	public String getReqCompatibleLatestAND() {
		return reqCompatibleLatestAND;
	}

	public void setReqCompatibleLatestAND(String reqCompatibleLatestAND) {
		this.reqCompatibleLatestAND = reqCompatibleLatestAND;
	}

	public String getReqCompatibleLatestHW() {
		return reqCompatibleLatestHW;
	}

	public void setReqCompatibleLatestHW(String reqCompatibleLatestHW) {
		this.reqCompatibleLatestHW = reqCompatibleLatestHW;
	}

	public String getReqHashValue() {
		return reqHashValue;
	}

	public void setReqHashValue(String reqHashValue) {
		this.reqHashValue = reqHashValue;
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
	
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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
