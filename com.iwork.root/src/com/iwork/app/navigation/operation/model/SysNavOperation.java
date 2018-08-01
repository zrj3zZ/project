package com.iwork.app.navigation.operation.model;

import com.iwork.core.db.ObjectModel;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysNavOperation implements java.io.Serializable,ObjectModel {
	public static String DATABASE_ENTITY = "SysNavOperation";
	// Fields
	private String id	     ;
	private String oname	     ;
	private String pid    ;
	private String ptype  ;
	private String odesc	    ;
	private String orderindex;
	private String actionurl;
	
	public String getActionurl() {
		return actionurl;
	}
	public void setActionurl(String actionurl) {
		this.actionurl = actionurl;
	}
	public static String getDATABASE_ENTITY() {
		return DATABASE_ENTITY;
	}
	public static void setDATABASE_ENTITY(String database_entity) {
		DATABASE_ENTITY = database_entity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id; 
	}
	public String getOrderindex() { 
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	public String getOname() {
		return oname;
	}
	public void setOname(String oname) {
		this.oname = oname;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getOdesc() {
		return odesc;
	}
	public void setOdesc(String odesc) {
		this.odesc = odesc;
	}

}
