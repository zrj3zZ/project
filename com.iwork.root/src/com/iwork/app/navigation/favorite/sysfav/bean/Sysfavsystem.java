package com.iwork.app.navigation.favorite.sysfav.bean;

/**
 * Sysfavsystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Sysfavsystem implements java.io.Serializable {
	public static String DATABASE_ENTITY = "Sysfavsystem";
	// Fields

	private Long id;
	private String sysName;
	private String sysIcon;
	private String sysUrl;
	private String sysTarget;
	private String sysMemo;
	private Long sysIndex;

	// Constructors

	/** default constructor */
	public Sysfavsystem() {
	}

	/** minimal constructor */
	public Sysfavsystem(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Sysfavsystem(Long id, String sysName, String sysIcon, String sysUrl,
			String sysTarget, String sysMemo, Long sysIndex) {
		this.id = id;
		this.sysName = sysName;
		this.sysIcon = sysIcon;
		this.sysUrl = sysUrl;
		this.sysTarget = sysTarget;
		this.sysMemo = sysMemo;
		this.sysIndex = sysIndex;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysIcon() {
		return this.sysIcon;
	}

	public void setSysIcon(String sysIcon) {
		this.sysIcon = sysIcon;
	}

	public String getSysUrl() {
		return this.sysUrl;
	}

	public void setSysUrl(String sysUrl) {
		this.sysUrl = sysUrl;
	}

	public String getSysTarget() {
		return this.sysTarget;
	}

	public void setSysTarget(String sysTarget) {
		this.sysTarget = sysTarget;
	}

	public String getSysMemo() {
		return this.sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}

	public Long getSysIndex() {
		return this.sysIndex;
	}

	public void setSysIndex(Long sysIndex) {
		this.sysIndex = sysIndex;
	}

}
