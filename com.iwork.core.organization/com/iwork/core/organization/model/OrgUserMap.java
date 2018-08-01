package com.iwork.core.organization.model;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgUserMap implements java.io.Serializable {
	/**
	 * 组织兼任
	 */
	public static final String USER_TYPE_ORG = "0";
	
	/**
	 * 系统兼任
	 */
	public static final String USER_TYPE_SYS = "1";
	
	private String id;
	private String userid;
	private String departmentid;
	private String departmentname;
	private String orgroleid;
	private String orgrolename;
	private String ismanager;
	private String usermaptype;
	public String getId() {
		return id;
	} 
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getOrgroleid() {
		return orgroleid;
	}
	public void setOrgroleid(String orgroleid) {
		this.orgroleid = orgroleid;
	}
	public String getOrgrolename() {
		return orgrolename;
	}
	public void setOrgrolename(String orgrolename) {
		this.orgrolename = orgrolename;
	}
	public String getIsmanager() {
		return ismanager;
	}
	public void setIsmanager(String ismanager) {
		this.ismanager = ismanager;
	}
	public String getUsermaptype() {
		return usermaptype;
	}
	public void setUsermaptype(String usermaptype) {
		this.usermaptype = usermaptype;
	}
}
