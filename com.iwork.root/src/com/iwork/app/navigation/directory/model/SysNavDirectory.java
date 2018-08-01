package com.iwork.app.navigation.directory.model;


/**
 * SysnavdirectoryId entity. @author MyEclipse Persistence Tools
 */

public class SysNavDirectory implements java.io.Serializable{
	public static String DATABASE_ENTITY = "SysNavDirectory";
	// Fields 
	private Long id;
	private String directoryName;
	private Long systemId;
	private String directoryIcon;
	private String directoryUrl;
	private String directoryTarget;
	private String directoryDesc;
	private Long orderindex;

	// Constructors
	// Property accessors

	public Long getId() { 
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDirectoryName() {
		return this.directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public Long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public String getDirectoryIcon() {
		return this.directoryIcon;
	}

	public void setDirectoryIcon(String directoryIcon) {
		this.directoryIcon = directoryIcon;
	}

	public String getDirectoryUrl() {
		return this.directoryUrl;
	}

	public void setDirectoryUrl(String directoryUrl) {
		this.directoryUrl = directoryUrl;
	}

	public String getDirectoryTarget() {
		return this.directoryTarget;
	}

	public void setDirectoryTarget(String directoryTarget) {
		this.directoryTarget = directoryTarget;
	}

	public String getDirectoryDesc() {
		return this.directoryDesc;
	}

	public void setDirectoryDesc(String directoryDesc) {
		this.directoryDesc = directoryDesc;
	}

	public Long getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

}
