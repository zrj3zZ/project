package com.iwork.plugs.email.model;


/**
 * IworkMailGrouplist entity. @author MyEclipse Persistence Tools
 */

public class IworkMailGrouplist implements java.io.Serializable {

	// Fields

	private Long id;
	private String userid;
	private String groupTitle;
	private String groupDesc;

	// Constructors

	/** default constructor */
	public IworkMailGrouplist() {
	}

	/** minimal constructor */
	public IworkMailGrouplist(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkMailGrouplist(Long id, String userid, String groupTitle,
			String groupDesc) {
		this.id = id;
		this.userid = userid;
		this.groupTitle = groupTitle;
		this.groupDesc = groupDesc;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupTitle() {
		return this.groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public String getGroupDesc() {
		return this.groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

}