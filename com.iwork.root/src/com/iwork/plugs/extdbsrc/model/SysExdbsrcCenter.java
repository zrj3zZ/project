package com.iwork.plugs.extdbsrc.model;

/**
 * SysExdbsrcCenter entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysExdbsrcCenter implements java.io.Serializable {
	public static String DATABASE_ENTITY = "SysExdbsrcCenter";
	// Fields

	private Long id;
	private String dsrcTitle;
	private String dsrcType;
	private String driverName;
	private String dsrcUrl;
	private String username;
	private String password;
	private String uuid;

	// Constructors

	/** default constructor */
	public SysExdbsrcCenter() {
	}

	/** full constructor */
	public SysExdbsrcCenter(String dsrcTitle, String dsrcType,
			String driverName, String dsrcUrl, String username, String password,String uuid) {
		this.dsrcTitle = dsrcTitle;
		this.dsrcType = dsrcType;
		this.driverName = driverName;
		this.dsrcUrl = dsrcUrl;
		this.username = username;
		this.password = password;
		this.uuid = uuid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDsrcTitle() {
		return this.dsrcTitle;
	}

	public void setDsrcTitle(String dsrcTitle) {
		this.dsrcTitle = dsrcTitle;
	}

	public String getDsrcType() {
		return this.dsrcType;
	}

	public void setDsrcType(String dsrcType) {
		this.dsrcType = dsrcType;
	}

	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDsrcUrl() {
		return this.dsrcUrl;
	}

	public void setDsrcUrl(String dsrcUrl) {
		this.dsrcUrl = dsrcUrl;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
