package com.iwork.plugs.syscalendar.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCalendarDetailInfo entity. @author MyEclipse Persistence Tools
 */

public class SysCalendarDetailInfoModel implements java.io.Serializable {
	private Long id;
	private Date CDate;
	private String actualDayType;
	private String commonDayType;
	private Long calendarId;
	private String dayOfWeek;
	private String ts;

	/** default constructor */
	public SysCalendarDetailInfoModel() {
	}

	/** full constructor */
	public SysCalendarDetailInfoModel(Date CDate, String actualDayType,
			String commonDayType, Long calendarId, String ts,String dayOfWeek) {
		this.CDate = CDate;
		this.actualDayType = actualDayType;
		this.commonDayType = commonDayType;
		this.calendarId = calendarId;
		this.ts = ts;
		this.dayOfWeek = dayOfWeek;
	}
	// Property accessors
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCDate() {
		return this.CDate;
	}

	public void setCDate(Date CDate) {
		this.CDate = CDate;
	}

	public String getActualDayType() {
		return this.actualDayType;
	}

	public void setActualDayType(String actualDayType) {
		this.actualDayType = actualDayType;
	}

	public String getCommonDayType() {
		return this.commonDayType;
	}

	public void setCommonDayType(String commonDayType) {
		this.commonDayType = commonDayType;
	}


	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

}