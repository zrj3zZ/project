package com.iwork.plugs.meeting.model;

import java.util.Date;

/**
 * IworkMeetingRoomOrderNew entity. @author MyEclipse Persistence Tools
 */

public class IworkMeetingRoomOrder implements java.io.Serializable{

	// Fields

	private long id;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String ordername;
	private String meetingpersons;
	private String meetingroomno;
	private String departmentname;
	private Date meetingdate;
	private String status;
	private Date startdate;
	private Date enddate;
	private String starttime;
	private String endtime;
	private String meetingtitle;
	private String memo;
	private Date startdatea;
	private Date enddatea;
	private String lasttime;
	private String spsbzt;
	private String ordertel;
	private String meetingtype;
	private long bindid;

	// Constructors

	/** default constructor */
	public IworkMeetingRoomOrder() {
	}

	/** full constructor */
	public IworkMeetingRoomOrder(Date createdate, String createuser,
			Date updatedate, String ordername, String meetingpersons,
			String meetingroomno, String departmentname, Date meetingdate,
			String status, Date startdate, Date enddate, String starttime,
			String endtime, String meetingtitle, String memo, Date startdatea,
			Date enddatea, String lasttime, String spsbzt, String ordertel,
			String meetingtype, long bindid) {
		this.id = id;
		this.createdate = createdate;
		this.createuser = createuser;
		this.updatedate = updatedate;
		this.ordername = ordername;
		this.meetingpersons = meetingpersons;
		this.meetingroomno = meetingroomno;
		this.departmentname = departmentname;
		this.meetingdate = meetingdate;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.meetingtitle = meetingtitle;
		this.memo = memo;
		this.startdatea = startdatea;
		this.enddatea = enddatea;
		this.lasttime = lasttime;
		this.spsbzt = spsbzt;
		this.ordertel = ordertel;
		this.meetingtype = meetingtype;
		this.bindid = bindid;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getOrdername() {
		return this.ordername;
	}

	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}

	public String getMeetingpersons() {
		return this.meetingpersons;
	}

	public void setMeetingpersons(String meetingpersons) {
		this.meetingpersons = meetingpersons;
	}

	public String getMeetingroomno() {
		return this.meetingroomno;
	}

	public void setMeetingroomno(String meetingroomno) {
		this.meetingroomno = meetingroomno;
	}

	public String getDepartmentname() {
		return this.departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public Date getMeetingdate() {
		return this.meetingdate;
	}

	public void setMeetingdate(Date meetingdate) {
		this.meetingdate = meetingdate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getStarttime() {
		return this.starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMeetingtitle() {
		return this.meetingtitle;
	}

	public void setMeetingtitle(String meetingtitle) {
		this.meetingtitle = meetingtitle;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getStartdatea() {
		return this.startdatea;
	}

	public void setStartdatea(Date startdatea) {
		this.startdatea = startdatea;
	}

	public Date getEnddatea() {
		return this.enddatea;
	}

	public void setEnddatea(Date enddatea) {
		this.enddatea = enddatea;
	}

	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getSpsbzt() {
		return this.spsbzt;
	}

	public void setSpsbzt(String spsbzt) {
		this.spsbzt = spsbzt;
	}

	public String getOrdertel() {
		return this.ordertel;
	}

	public void setOrdertel(String ordertel) {
		this.ordertel = ordertel;
	}

	public String getMeetingtype() {
		return this.meetingtype;
	}

	public void setMeetingtype(String meetingtype) {
		this.meetingtype = meetingtype;
	}

	public long getBindid() {
		return this.bindid;
	}

	public void setBindid(long bindid) {
		this.bindid = bindid;
	}

}