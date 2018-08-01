package com.iwork.plugs.syscalendar.model;

import java.util.Date;

/**
 *  系统日历管理
 *  @author Wangjh
 */

public class SysCalendarBaseInfoModel implements java.io.Serializable,Comparable<SysCalendarBaseInfoModel> {

	// Fields

	private Long id;
	private String calendarName;
	private String describ;
	private String ts;
	private String sun;
	private String mon;
	private String tues;
	private String wed;
	private String turs;
	private String fri;
	private String sat;
	private Date expDateFrom;
	private Date expDateTo;
	private String status;
	private Long workTimeFrom;
	private Long workTimeTo;
	private String grantUsers;
	private String calendarType;
	private String createTime;

	// Constructors
	//重写排序方法
	public int compareTo(SysCalendarBaseInfoModel model){
		return this.getCreateTime().compareTo(model.getCreateTime());
	}

	/** default constructor */
	public SysCalendarBaseInfoModel() {
	}

	/** full constructor */
	public SysCalendarBaseInfoModel(String calendarName, String describ, String ts,
			String sun, String mon, String tues, String wed, String turs,
			String fri, String sat, Date expDateFrom, Date expDateTo,
			String status, Long workTimeFrom, Long workTimeTo,
			String grantUsers, String calendarType) {
		this.calendarName = calendarName;
		this.describ = describ;
		this.ts = ts;
		this.sun = sun;
		this.mon = mon;
		this.tues = tues;
		this.wed = wed;
		this.turs = turs;
		this.fri = fri;
		this.sat = sat;
		this.expDateFrom = expDateFrom;
		this.expDateTo = expDateTo;
		this.status = status;
		this.workTimeFrom = workTimeFrom;
		this.workTimeTo = workTimeTo;
		this.grantUsers = grantUsers;
		this.calendarType = calendarType;
	}
	public String getCalendarName() {
		return this.calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public String getDescrib() {
		return this.describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getSun() {
		return this.sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	public String getMon() {
		return this.mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getTues() {
		return this.tues;
	}

	public void setTues(String tues) {
		this.tues = tues;
	}

	public String getWed() {
		return this.wed;
	}

	public void setWed(String wed) {
		this.wed = wed;
	}

	public String getTurs() {
		return this.turs;
	}

	public void setTurs(String turs) {
		this.turs = turs;
	}

	public String getFri() {
		return this.fri;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	public String getSat() {
		return this.sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}

	public Date getExpDateFrom() {
		return this.expDateFrom;
	}

	public void setExpDateFrom(Date expDateFrom) {
		this.expDateFrom = expDateFrom;
	}

	public Date getExpDateTo() {
		return this.expDateTo;
	}

	public void setExpDateTo(Date expDateTo) {
		this.expDateTo = expDateTo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkTimeFrom() {
		return workTimeFrom;
	}

	public void setWorkTimeFrom(Long workTimeFrom) {
		this.workTimeFrom = workTimeFrom;
	}

	public Long getWorkTimeTo() {
		return workTimeTo;
	}

	public void setWorkTimeTo(Long workTimeTo) {
		this.workTimeTo = workTimeTo;
	}

	public String getGrantUsers() {
		return this.grantUsers;
	}

	public void setGrantUsers(String grantUsers) {
		this.grantUsers = grantUsers;
	}

	public String getCalendarType() {
		return this.calendarType;
	}

	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}