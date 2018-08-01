package com.iwork.app.navigation.function.model;


/**
 * Sysnavfunction entity. @author MyEclipse Persistence Tools
 */

public class Sysnavfunction implements java.io.Serializable {

	// Fields

	private Long id;
	private String functionName;
	private Long directoryId;
	private String functionType;
	private String functionUrl;
	private String functionIcon;
	private String functionTarget;
	private String functionDesc;
	private Long orderindex;

	// Constructors

	/** default constructor */
	public Sysnavfunction() {
	}

	/** full constructor */
	public Sysnavfunction(String functionName, Long directoryId,
			String functionType, String functionUrl, String functionIcon,
			String functionTarget, String functionDesc, Long orderindex) {
		this.functionName = functionName;
		this.directoryId = directoryId;
		this.functionType = functionType;
		this.functionUrl = functionUrl;
		this.functionIcon = functionIcon;
		this.functionTarget = functionTarget;
		this.functionDesc = functionDesc;
		this.orderindex = orderindex;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Long getDirectoryId() {
		return this.directoryId;
	}

	public void setDirectoryId(Long directoryId) {
		this.directoryId = directoryId;
	}

	public String getFunctionType() {
		return this.functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFunctionUrl() {
		return this.functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public String getFunctionIcon() {
		return this.functionIcon;
	}

	public void setFunctionIcon(String functionIcon) {
		this.functionIcon = functionIcon;
	}

	public String getFunctionTarget() {
		return this.functionTarget;
	}

	public void setFunctionTarget(String functionTarget) {
		this.functionTarget = functionTarget;
	}

	public String getFunctionDesc() {
		return this.functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public Long getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

}