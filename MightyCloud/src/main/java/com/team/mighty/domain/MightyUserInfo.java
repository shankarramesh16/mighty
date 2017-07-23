package com.team.mighty.domain;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TBL_MIGHTY_USER_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MightyUserInfo extends BaseEntityInfo {

	private static final long serialVersionUID = 1L;
	
	public MightyUserInfo() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private long id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "USERFB_ID")
	private String userFBId;

	@Column(name = "USER_INDICATOR")
	private String userIndicator;

	public String getUserFBId() {
		return userFBId;
	}

	public void setUserFBId(String userFBId) {
		this.userFBId = userFBId;
	}

	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "AGE")
	private String age;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "PwdChangedDate")
	private Date pwdChangedDate;


	@Column(name = "USER_STATUS")
	private String userStatus;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "CREATED_DT")
	private Date createdDt;
	
	@Column(name = "UPDATED_DT")
	private Date updatedDt;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<MightyDeviceUserMapping> mightyDeviceUserMapping;
	
		
		//bi-directional many-to-many association to TblMightyUsrDevMap
		@ManyToMany
		@JoinTable(
			name="tbl_mighty_user_info_tbl_mighty_usr_dev_map"
			, joinColumns={
				@JoinColumn(name="TBL_MIGHTY_USER_INFO_USER_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="mightyDeviceUserMapping_id")
				}
			)
		private List<MightyDeviceUserMapping> mightyUsrDevMaps1;
		
		

		/*//bi-directional many-to-one association to TblMightyUsrDevMap
		@OneToMany(mappedBy="MightyUserInfo",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
		private List<MightyDeviceUserMapping> mightyUsrDevMaps2;*/
	
	public long getId() {
		return id;
	}

	public List<MightyDeviceUserMapping> getMightyUsrDevMaps1() {
		return mightyUsrDevMaps1;
	}

	public void setMightyUsrDevMaps1(List<MightyDeviceUserMapping> mightyUsrDevMaps1) {
		this.mightyUsrDevMaps1 = mightyUsrDevMaps1;
	}

	/*public List<MightyDeviceUserMapping> getMightyUsrDevMaps2() {
		return mightyUsrDevMaps2;
	}

	public void setMightyUsrDevMaps2(List<MightyDeviceUserMapping> mightyUsrDevMaps2) {
		this.mightyUsrDevMaps2 = mightyUsrDevMaps2;
	}*/

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getPwdChangedDate() {
		return pwdChangedDate;
	}

	public void setPwdChangedDate(Date pwdChangedDate) {
		this.pwdChangedDate = pwdChangedDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserIndicator() {
		return userIndicator;
	}

	public void setUserIndicator(String userIndicator) {
		this.userIndicator = userIndicator;
	}

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<MightyDeviceUserMapping> getMightyDeviceUserMapping() {
		return mightyDeviceUserMapping;
	}

	public void setMightyDeviceUserMapping(Set<MightyDeviceUserMapping> mightyDeviceUserMapping) {
		this.mightyDeviceUserMapping = mightyDeviceUserMapping;
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

}
