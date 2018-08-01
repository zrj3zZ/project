package com.iwork.webservice.model;

import java.util.Date;

/**
 * SysWsRuLog entity. @author MyEclipse Persistence Tools
 */

public class SysWsRuLog implements java.io.Serializable {

	// Fields

	private int id;
	private int pid;
	private Date createdate;
	private long showtime;
	private int status;
	private String loginfo;
	private String uuid;

	// Constructors

	/** default constructor */
	public SysWsRuLog() {
	}

	/** full constructor */
	public SysWsRuLog(int pid, Date createdate, int showtime,
			int status, String loginfo, String uuid) {
		this.pid = pid;
		this.createdate = createdate;
		this.showtime = showtime;
		this.status = status;
		this.loginfo = loginfo;
		this.uuid = uuid;
	}

	// Property accessors

	public Date getCreatedate() {
		return this.createdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public long getShowtime() {
		return showtime;
	}

	public void setShowtime(long showtime) {
		this.showtime = showtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	public String getLoginfo() {
		return this.loginfo;
	}

	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}