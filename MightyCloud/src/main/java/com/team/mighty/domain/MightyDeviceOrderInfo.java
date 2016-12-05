package com.team.mighty.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name ="TBL_DEVICE_ORDER_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MightyDeviceOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	private long id;
	
	@Column(name ="ORDER_ID")
	private String orderId;
	
	@Column(name ="ORDER_DESC")
	private String orderDesc;
	
	@Column(name ="FIELD_1")
	private String field1;
	
	@Column(name ="FIELD_2")
	private String field2;
	
	@Column(name ="FIELD_3")
	private String field3;
	
	@Column(name ="FIELD_4")
	private String field4;
	
	@Column(name ="FIELD_5")
	private String field5;
	
	@Column(name ="FIELD_6")
	private String field6;
	
	@Column(name ="NO_OF_DEVICE")
	private int noOfDevice;
	
	@Column(name ="IS_GIFT")
	private String isGift;
	
	@Column(name ="DEVICE_SERIALNO")
	private String deviceSerialNo;
	
	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}

	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}

	@Column(name ="PLAYLIST_URL")
	private String playListURL;
	
	@Column(name ="WELCOME_MESS_URL")
	private String welcomeMessURL;
	
	@Column(name ="WELCOME_MESS_TEXT")
	private String welcomeMessText;
	
	@Column(name ="PARTNER_CODE")
	private String partnerCode;
	
	@Column(name ="ADMIN_USER_ID")
	private String adminUserId;
	
	@Column(name ="CREATED_DT")
	private Date createdDt;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DT")
	private Date updatedDt;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	
	//bi-directional many-to-one association to TblMightyDeviceInfo
	@OneToMany(mappedBy="deviceOrderInfo")
	private List<MightyDeviceInfo> mightyDeviceInfos;
	
	@Transient
	private String errorCode;
	
	@Transient
	private String errorDesc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	public int getNoOfDevice() {
		return noOfDevice;
	}

	public void setNoOfDevice(int noOfDevice) {
		this.noOfDevice = noOfDevice;
	}

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getPlayListURL() {
		return playListURL;
	}

	public void setPlayListURL(String playListURL) {
		this.playListURL = playListURL;
	}

	public String getWelcomeMessURL() {
		return welcomeMessURL;
	}

	public void setWelcomeMessURL(String welcomeMessURL) {
		this.welcomeMessURL = welcomeMessURL;
	}

	public String getWelcomeMessText() {
		return welcomeMessText;
	}

	public void setWelcomeMessText(String welcomeMessText) {
		this.welcomeMessText = welcomeMessText;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	/*public List<MightyDeviceInfo> getMightyDeviceInfos() {
		return this.MightyDeviceInfos;
	}

	public void setMightyDeviceInfos(List<MightyDeviceInfo> MightyDeviceInfos) {
		this.MightyDeviceInfos = MightyDeviceInfos;
	}*/

	public List<MightyDeviceInfo> getMightyDeviceInfos() {
		return mightyDeviceInfos;
	}

	public void setMightyDeviceInfos(List<MightyDeviceInfo> mightyDeviceInfos) {
		this.mightyDeviceInfos = mightyDeviceInfos;
	}

	
}
