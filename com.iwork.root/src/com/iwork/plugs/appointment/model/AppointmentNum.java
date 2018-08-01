package com.iwork.plugs.appointment.model;

import java.util.Date;


public class AppointmentNum implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "AppointmentNum";
	
	// Fields

	private Long id;
	private String szr;
	private Long szs;
	private Date szsj;
	private Long yysx;

	// Constructors

	/** default constructor */

	/** full constructor */
	public AppointmentNum() {
	}
	
	// Property accessors
	public AppointmentNum(Long id, String szr, Long szs, Date szsj, Long yysx) {
		this.id = id;
		this.szr = szr;
		this.szs = szs;
		this.szsj = szsj;
		this.yysx = yysx;
	}

	public Long getSzs() {
		return szs;
	}

	public void setSzs(Long szs) {
		this.szs = szs;
	}

	public Long getYysx() {
		return yysx;
	}

	public void setYysx(Long yysx) {
		this.yysx = yysx;
	}

	public Long getId() {
		return this.id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getSzr() {
		return szr;
	}

	public void setSzr(String szr) {
		this.szr = szr;
	}

	public Date getSzsj() {
		return szsj;
	}

	public void setSzsj(Date szsj) {
		this.szsj = szsj;
	}

}
