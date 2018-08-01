package com.iwork.core.organization.model;

import java.util.Date;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgStationIns implements java.io.Serializable {

	private Long id ;
	private String title ;
	private Long stationId ;
	private String owners ;
	private Date startDate;
	private Date endDate;
	private Long status;
	private Long orderindex;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getOwners() {
		return owners;
	}
	public void setOwners(String owners) {
		this.owners = owners;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}
	
}
