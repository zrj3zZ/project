package com.iwork.plugs.hr.staff.model;

import java.lang.reflect.Constructor;

public class ConfigModel {
	private String key;
	private String title;
	private String interfaceClass;
	private String implementsClass;
	private String url;
	private String isDefault;
	private String desc;
	private Constructor cons;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	public String getImplementsClass() {
		return implementsClass;
	}
	public void setImplementsClass(String implementsClass) {
		this.implementsClass = implementsClass;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Constructor getCons() {
		return cons;
	}
	public void setCons(Constructor cons) {
		this.cons = cons;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

}
