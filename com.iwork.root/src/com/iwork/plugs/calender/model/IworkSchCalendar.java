package com.iwork.plugs.calender.model;

import java.util.Date;

/**
 * IworkSchCalendar entity. @author MyEclipse Persistence Tools
 */

public class IworkSchCalendar implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "IworkSchCalendar";
	
	// Fields

	private Long id;
	private String userid;
	private String title;
	private Date startdate;
	private Date enddate;
	private String starttime;
	private String endtime;
	private Long isallday;
	private Long isalert;
	private String alerttime;
	private Long issharing;
	private String remark;
	private Date reStartdate;
	private Date reEnddate;
	private String reStarttime;
	private String reEndtime;
	private String reMode;
	private String reDayInterval;
	private String reWeekDate;
	private String reMonthDays;
	private String reYearMonth;
	private String reYearDays;
	private String wczt;
	private String wcqk;
	private String extends1;
	private String extends2;
	private String extends3;
	// Constructors

	/** default constructor */
	public IworkSchCalendar() {
	}

	/** full constructor */
	public IworkSchCalendar(String userid, String title, Date startdate,
			Date enddate, String starttime, String endtime,
			Long isallday, Long isalert, String alerttime,
			Long issharing, String remark, Date reStartdate,
			Date reEnddate, String reStarttime, String reEndtime,
			String reMode, String reDayInterval, String reWeekDate,
			String reMonthDays, String reYearMonth, String reYearDays,String wczt,String wcqk,String extends1,String extends2,String extends3) {
		this.userid = userid;
		this.title = title;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.isallday = isallday;
		this.isalert = isalert;
		this.alerttime = alerttime;
		this.issharing = issharing;
		this.remark = remark;
		this.reStartdate = reStartdate;
		this.reEnddate = reEnddate;
		this.reStarttime = reStarttime;
		this.reEndtime = reEndtime;
		this.reMode = reMode;
		this.reDayInterval = reDayInterval;
		this.reWeekDate = reWeekDate;
		this.reMonthDays = reMonthDays;
		this.reYearMonth = reYearMonth;
		this.reYearDays = reYearDays;
		this.wczt = wczt;
		this.wcqk = wcqk;
		this.extends1 = extends1;
		this.extends2 = extends2;
		this.extends3 = extends3;
	}

	

	// Property accessors

	public String getWczt() {
		return wczt;
	}

	public void setWczt(String wczt) {
		this.wczt = wczt;
	}

	public String getWcqk() {
		return wcqk;
	}

	public void setWcqk(String wcqk) {
		this.wcqk = wcqk;
	}

	public String getExtends1() {
		return extends1;
	}

	public void setExtends1(String extends1) {
		this.extends1 = extends1;
	}

	public String getExtends2() {
		return extends2;
	}

	public void setExtends2(String extends2) {
		this.extends2 = extends2;
	}

	public String getExtends3() {
		return extends3;
	}

	public void setExtends3(String extends3) {
		this.extends3 = extends3;
	}

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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Long getIsallday() {
		return this.isallday;
	}

	public void setIsallday(Long isallday) {
		this.isallday = isallday;
	}

	public Long getIsalert() {
		return this.isalert;
	}

	public void setIsalert(Long isalert) {
		this.isalert = isalert;
	}

	public String getAlerttime() {
		return this.alerttime;
	}

	public void setAlerttime(String alerttime) {
		this.alerttime = alerttime;
	}

	public Long getIssharing() {
		return this.issharing;
	}

	public void setIssharing(Long issharing) {
		this.issharing = issharing;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getReStartdate() {
		return this.reStartdate;
	}

	public void setReStartdate(Date reStartdate) {
		this.reStartdate = reStartdate;
	}

	public Date getReEnddate() {
		return this.reEnddate;
	}

	public void setReEnddate(Date reEnddate) {
		this.reEnddate = reEnddate;
	}

	public String getReStarttime() {
		return this.reStarttime;
	}

	public void setReStarttime(String reStarttime) {
		this.reStarttime = reStarttime;
	}

	public String getReEndtime() {
		return this.reEndtime;
	}

	public void setReEndtime(String reEndtime) {
		this.reEndtime = reEndtime;
	}

	public String getReMode() {
		return this.reMode;
	}

	public void setReMode(String reMode) {
		this.reMode = reMode;
	}

	public String getReDayInterval() {
		return this.reDayInterval;
	}

	public void setReDayInterval(String reDayInterval) {
		this.reDayInterval = reDayInterval;
	}

	public String getReWeekDate() {
		return this.reWeekDate;
	}

	public void setReWeekDate(String reWeekDate) {
		this.reWeekDate = reWeekDate;
	}

	public String getReMonthDays() {
		return this.reMonthDays;
	}

	public void setReMonthDays(String reMonthDays) {
		this.reMonthDays = reMonthDays;
	}

	public String getReYearMonth() {
		return this.reYearMonth;
	}

	public void setReYearMonth(String reYearMonth) {
		this.reYearMonth = reYearMonth;
	}

	public String getReYearDays() {
		return this.reYearDays;
	}

	public void setReYearDays(String reYearDays) {
		this.reYearDays = reYearDays;
	}

}
