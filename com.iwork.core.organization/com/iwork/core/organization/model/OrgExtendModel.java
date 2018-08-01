package com.iwork.core.organization.model;

import java.lang.reflect.Constructor;

public class OrgExtendModel {
	private String id;
	private String title;
	private String className;
	private String type;
	private String memo;
	private Constructor cons;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Constructor getCons() {
		return cons;
	}
	public void setCons(Constructor cons) {
		this.cons = cons;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
