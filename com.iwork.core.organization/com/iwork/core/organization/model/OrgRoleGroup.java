package com.iwork.core.organization.model;


/**
 * OrgRoleGroup entity. @author MyEclipse Persistence Tools
 */

public class OrgRoleGroup implements java.io.Serializable {

	// Fields

	private Long id;
	private String groupName;
	private String master;
	private String memo;

	// Constructors

	/** default constructor */
	public OrgRoleGroup() {
	}

	/** full constructor */
	public OrgRoleGroup(String groupName, String master, String memo) {
		this.groupName = groupName;
		this.master = master;
		this.memo = memo;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMaster() {
		return this.master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}