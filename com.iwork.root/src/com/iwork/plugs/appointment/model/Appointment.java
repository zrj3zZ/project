package com.iwork.plugs.appointment.model;

import java.util.Date;


public class Appointment implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "Appointment";
	
	// Fields

	private Long id;
	private String szr;
	private Date szsj;
	private Date jyr;
	private Long sfjy;

	// Constructors

	/** default constructor */

	/** full constructor */
	public Appointment() {
	}
	
	// Property accessors
	public Appointment(String szr, Date szsj, Date jyr, Long sfjy) {
		this.szr = szr;
		this.szsj = szsj;
		this.jyr = jyr;
		this.sfjy = sfjy;
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

	public Date getJyr() {
		return jyr;
	}

	public void setJyr(Date jyr) {
		this.jyr = jyr;
	}

	public Long getSfjy() {
		return sfjy;
	}

	public void setSfjy(Long sfjy) {
		this.sfjy = sfjy;
	}

	public Date getSzsj() {
		return szsj;
	}

	public void setSzsj(Date szsj) {
		this.szsj = szsj;
	}

}
