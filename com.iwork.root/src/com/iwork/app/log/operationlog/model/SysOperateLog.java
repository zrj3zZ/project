package com.iwork.app.log.operationlog.model;

import java.util.Date;

/**
 * SysOperateLog entity. @author MyEclipse Persistence Tools
 */

public class SysOperateLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "SysOperateLog";
	// Fields
	private String id;
	private Date createdate;
	private String logtype;
	private String userid;
	private Long indexid;
	private String loginfo;
	private String memo;

	// Constructors

	/** default constructor */
	public SysOperateLog() {
	}

	/** full constructor */
	public SysOperateLog(Date createdate, String logtype, String userid,
			Long indexid, String loginfo, String memo) {
		this.createdate = createdate;
		this.logtype = logtype;
		this.userid = userid;
		this.indexid = indexid;
		this.loginfo = loginfo;
		this.memo = memo;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getLogtype() {
		return this.logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getIndexid() {
		return this.indexid;
	}

	public void setIndexid(Long indexid) {
		this.indexid = indexid;
	}

	public String getLoginfo() {
		return this.loginfo;
	}

	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
