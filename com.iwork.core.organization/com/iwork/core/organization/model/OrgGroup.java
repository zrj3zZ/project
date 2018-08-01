package com.iwork.core.organization.model;

import java.util.Date;

import com.iwork.core.db.ObjectModel;

/**
 * OrgGroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgGroup implements ObjectModel ,java.io.Serializable {
	public static String DATABASE_ENTITY = "OrgGroup";
	// Fields

	private String id;
	private String groupName;
	private String groupCharge;
	private String state;
	private Date begindate;
	private Date enddate;
	private String memo;

	// Constructors

	/** default constructor */
	public OrgGroup() {
	}

	/** minimal constructor */
	public OrgGroup(String id) {
		this.id = id;
	}

	/** full constructor */
	public OrgGroup(String id, String groupName, String groupCharge,
			String state, Date begindate, Date enddate, String memo) {
		this.id = id;
		this.groupName = groupName;
		this.groupCharge = groupCharge;
		this.state = state;
		this.begindate = begindate;
		this.enddate = enddate;
		this.memo = memo;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCharge() {
		return this.groupCharge;
	}

	public void setGroupCharge(String groupCharge) {
		this.groupCharge = groupCharge;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getBegindate() {
		return this.begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
