package com.iwork.plugs.email.model;

/**
 * IworkMailGroupItem entity. @author MyEclipse Persistence Tools
 */

public class IworkMailGroupItem implements java.io.Serializable {

	// Fields

	private Long id;
	private Long pid;
	private String userid;

	// Constructors

	/** default constructor */
	public IworkMailGroupItem() {
	}

	/** minimal constructor */
	public IworkMailGroupItem(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkMailGroupItem(Long id, Long pid, String userid) {
		this.id = id;
		this.pid = pid;
		this.userid = userid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}