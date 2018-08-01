package com.iwork.app.log.model;

import java.util.Date;

/**
 * SysUserLoginLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
 
public class SysUserLoginLog implements java.io.Serializable {

	// Fields

	private Long id;
	private Date loginTime;
	private String loginUser;
	private String ipadress;
	private Long loginType;
	private String loginLabel;
	private String sessionid;

	// Constructors

	/** default constructor */
	public SysUserLoginLog() {
	}

	/** full constructor */
	public SysUserLoginLog(Date loginTime, String loginUser, String ipadress,
			Long loginType, String loginLabel, String sessionid) {
		this.loginTime = loginTime;
		this.loginUser = loginUser;
		this.ipadress = ipadress;
		this.loginType = loginType;
		this.loginLabel = loginLabel;
		this.sessionid = sessionid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginUser() {
		return this.loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getIpadress() {
		return this.ipadress;
	}

	public void setIpadress(String ipadress) {
		this.ipadress = ipadress;
	}

	public Long getLoginType() {
		return this.loginType;
	}

	public void setLoginType(Long loginType) {
		this.loginType = loginType;
	}

	public String getLoginLabel() {
		return this.loginLabel;
	}

	public void setLoginLabel(String loginLabel) {
		this.loginLabel = loginLabel;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

}
