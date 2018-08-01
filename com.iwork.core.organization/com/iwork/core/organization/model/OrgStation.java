package com.iwork.core.organization.model;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgStation implements java.io.Serializable {

	private Long id ;
	private String stationName ;
	private String uuid;
	private String memo;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStationName() {
		return stationName;
	}
	
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
