package com.iwork.webservice.model;


/**
 * SysWsRuParams entity. @author MyEclipse Persistence Tools
 */

public class SysWsRuParams implements java.io.Serializable {

	// Fields

	private int id;
	private int logId;
	private String inOrOut;
	private String title;
	private String name;
	private String type;
	private String value;
	private String uuid;

	// Constructors

	/** default constructor */
	public SysWsRuParams() {
	}

	/** full constructor */
	public SysWsRuParams(int logId, String inOrOut, String title,
			String name, String type, String value, String uuid) {
		this.logId = logId;
		this.inOrOut = inOrOut;
		this.title = title;
		this.name = name;
		this.type = type;
		this.value = value;
		this.uuid = uuid;
	}

	// Property accessors

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLogId() {
		return this.logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getInOrOut() {
		return this.inOrOut;
	}

	public void setInOrOut(String inOrOut) {
		this.inOrOut = inOrOut;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}