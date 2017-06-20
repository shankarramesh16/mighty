package com.team.mighty.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the settings database table.
 * 
 */
@Entity
@Table(name="settings")
@NamedQuery(name="Setting.findAll", query="SELECT s FROM Setting s")
public class Setting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date utilityLastUpdatedDate;

	private String utilityrunningtime;

	public Setting() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getUtilityLastUpdatedDate() {
		return this.utilityLastUpdatedDate;
	}

	public void setUtilityLastUpdatedDate(Date utilityLastUpdatedDate) {
		this.utilityLastUpdatedDate = utilityLastUpdatedDate;
	}

	public String getUtilityrunningtime() {
		return this.utilityrunningtime;
	}

	public void setUtilityrunningtime(String utilityrunningtime) {
		this.utilityrunningtime = utilityrunningtime;
	}

}