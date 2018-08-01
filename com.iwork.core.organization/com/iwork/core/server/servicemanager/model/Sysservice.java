package com.iwork.core.server.servicemanager.model;

import java.util.Date;

/**
 * Sysservice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Sysservice implements java.io.Serializable {

	// Fields

	private Long id;
	private Long parentid;
	private String servicename;
	private String servicekey;
	private String servicedesc;
	private Long status;
	private Date startdate;
	private Date enddate;
	private Long orderindex;

	// Constructors

	/** default constructor */
	public Sysservice() {
	}

	/** full constructor */
	public Sysservice(Long parentid, String servicename, String servicekey,
			String servicedesc, Long status, Date startdate, Date enddate,
			Long orderindex) {
		this.parentid = parentid;
		this.servicename = servicename;
		this.servicekey = servicekey;
		this.servicedesc = servicedesc;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.orderindex = orderindex;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentid() {
		return this.parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public String getServicename() {
		return this.servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getServicekey() {
		return this.servicekey;
	}

	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}

	public String getServicedesc() {
		return this.servicedesc;
	}

	public void setServicedesc(String servicedesc) {
		this.servicedesc = servicedesc;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
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

	public Long getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

}
