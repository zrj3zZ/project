package com.iwork.core.organization.model;

import com.iwork.core.db.ObjectModel;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgCompany   implements ObjectModel ,java.io.Serializable {
	public static String DATABASE_ENTITY = "OrgCompany";
	private String id ;
	private String companyname;
	private String companytype;
	private String companyno;
	private String companydesc;
	private String orderindex;
	private String lookandfeel;
	private String orgadress;
	private String post	;
	private String email	;
	private String tel	;
	private String zoneno	;
	private String zonename;
	private String extend1	;
	private String extend2	;
	private String extend3	;
	private String extend4 ;
	private String extend5	;
	private String parentid	;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCompanytype() {
		return companytype;
	}
	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}
	public String getCompanyno() {
		return companyno;
	}
	public void setCompanyno(String companyno) {
		this.companyno = companyno;
	}
	public String getCompanydesc() {
		return companydesc;
	}
	public void setCompanydesc(String companydesc) {
		this.companydesc = companydesc;
	}
	public String getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	public String getLookandfeel() {
		return lookandfeel;
	}
	public void setLookandfeel(String lookandfeel) {
		this.lookandfeel = lookandfeel;
	}
	public String getOrgadress() {
		return orgadress;
	}
	public void setOrgadress(String orgadress) {
		this.orgadress = orgadress;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
}
