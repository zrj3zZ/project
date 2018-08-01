package com.iwork.core.organization.model;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgDepartment implements java.io.Serializable {
	public static String DATABASE_ENTITY = "OrgDepartment";
	private Long id	            ; 
	private String departmentname      ; 
	private String companyid	    ; 
	private Long parentdepartmentid  ; 
	private String departmentdesc	    ; 
	private String departmentno	    ; 
	private String layer	            ; 
	private String orderindex          ; 
	private String zoneno	            ; 
	private String zonename            ; 
	private String extend1             ; 
	private String extend2	            ; 
	private String extend3             ; 
	private String extend4             ; 
	private String extend5             ;
	private String departmentstate		;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public Long getParentdepartmentid() {
		return parentdepartmentid;
	}
	public void setParentdepartmentid(Long parentdepartmentid) {
		this.parentdepartmentid = parentdepartmentid;
	}
	public String getDepartmentdesc() {
		return departmentdesc;
	}
	public void setDepartmentdesc(String departmentdesc) {
		this.departmentdesc = departmentdesc;
	}
	public String getDepartmentno() {
		return departmentno;
	}
	public void setDepartmentno(String departmentno) {
		this.departmentno = departmentno;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public String getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	public String getZoneno() {
		return zoneno;
	}
	public void setZoneno(String zoneno) {
		this.zoneno = zoneno;
	}
	public String getZonename() {
		return zonename;
	}
	public void setZonename(String zonename) {
		this.zonename = zonename;
	}
	public String getExtend1() {
		return extend1;
	}
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	public String getExtend2() {
		return extend2;
	}
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	public String getExtend4() {
		return extend4;
	}
	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}
	public String getExtend5() {
		return extend5;
	}
	public void setExtend5(String extend5) {
		this.extend5 = extend5;
	}
	public String getDepartmentstate() {
		return departmentstate;
	}
	public void setDepartmentstate(String departmentstate) {
		this.departmentstate = departmentstate;
	} 
	
}
