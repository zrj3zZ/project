package com.iwork.admin.framework.model;

import java.lang.reflect.Constructor;

public class DashboardModel {
	/**
	 * 键值
	 */
	private String key;
	/**
	 * portlet名称
	 */
	private String title;
	/**
	 * 分组
	 */
	private String groupName;
	/**
	 * 高度
	 */
	private String height;
	/**
	 * 类接口路径
	 */
	private String interfaceName;
	/**
	 * 类路径
	 */
	private String className;
	/**
	 * 描述
	 */
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
	
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
	
	
}
