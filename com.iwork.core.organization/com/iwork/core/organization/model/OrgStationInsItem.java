package com.iwork.core.organization.model;


/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgStationInsItem implements java.io.Serializable {

	private Long id ;
	private Long stationId ;
	private Long stationInsId ;
	private String orgtype ;
	private String val ;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getStationInsId() {
		return stationInsId;
	}
	public void setStationInsId(Long stationInsId) {
		this.stationInsId = stationInsId;
	}
	public String getOrgtype() {
		return orgtype;
	}
	public void setOrgtype(String orgtype) {
		this.orgtype = orgtype;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

}
