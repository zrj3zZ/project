package com.iwork.plugs.resoucebook.model;

import java.util.Date;

/**
 * IworkRmWeb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkRmWeb implements java.io.Serializable {

	// Fields
	public static String DATABASE_ENTITY = "IworkRmWeb";
	private Long id;
	private Long spaceid;
	private String spacename;
	private String resouceid;
	private String resouce;
	private String userid;
	private String username;
	private Date begintime;
	private Date endtime;
	private Long status;
	private String memo;

	// Constructors

	/** default constructor */
	public IworkRmWeb() {
	}

	/** minimal constructor */
	public IworkRmWeb(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkRmWeb(Long id, Long spaceid, String spacename, String resouceid,
			String resouce, String userid, String username, Date begintime,
			Date endtime, Long status, String memo) {
		this.id = id;
		this.spaceid = spaceid;
		this.spacename = spacename;
		this.resouceid = resouceid;
		this.resouce = resouce;
		this.userid = userid;
		this.username = username;
		this.begintime = begintime;
		this.endtime = endtime;
		this.status = status;
		this.memo = memo;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpaceid() {
		return this.spaceid;
	}

	public void setSpaceid(Long spaceid) {
		this.spaceid = spaceid;
	}

	public String getSpacename() {
		return this.spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public String getResouceid() {
		return this.resouceid;
	}

	public void setResouceid(String resouceid) {
		this.resouceid = resouceid;
	}

	public String getResouce() {
		return this.resouce;
	}

	public void setResouce(String resouce) {
		this.resouce = resouce;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getBegintime() {
		return this.begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
