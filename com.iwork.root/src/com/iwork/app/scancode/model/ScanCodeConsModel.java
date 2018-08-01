package com.iwork.app.scancode.model;

import java.lang.reflect.Constructor;

public class ScanCodeConsModel {
	private String id;
	private String esType;
	private String title;
	private String className;
	private String memo;
	private Constructor cons;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEsType() {
		return esType;
	}
	public void setEsType(String esType) {
		this.esType = esType;
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
	
}
