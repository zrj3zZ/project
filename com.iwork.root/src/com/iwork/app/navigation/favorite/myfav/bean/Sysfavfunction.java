package com.iwork.app.navigation.favorite.myfav.bean;

/**
 * Sysfavfunction entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Sysfavfunction implements java.io.Serializable {
	public static String DATABASE_ENTITY = "Sysfavfunction";
	// Fields

	private Long id;
	private String userId;
	private String funId;
	private String funName;
	private String funRname;
	private String funIcon;
	private String funTarget;
	private String funMemo;
	private Long funIndex;
	private String funUrl;

	// Constructors

	/** default constructor */
	public Sysfavfunction() {
	}

	/** minimal constructor */
	public Sysfavfunction(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Sysfavfunction(Long id, String userId, String funId, String funName,
			String funRname, String funIcon, String funTarget, String funMemo,
			Long funIndex, String funUrl) {
		this.id = id;
		this.userId = userId;
		this.funId = funId;
		this.funName = funName;
		this.funRname = funRname;
		this.funIcon = funIcon;
		this.funTarget = funTarget;
		this.funMemo = funMemo;
		this.funIndex = funIndex;
		this.funUrl = funUrl;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFunId() {
		return this.funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String getFunName() {
		return this.funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getFunRname() {
		return this.funRname;
	}

	public void setFunRname(String funRname) {
		this.funRname = funRname;
	}

	public String getFunIcon() {
		return this.funIcon;
	}

	public void setFunIcon(String funIcon) {
		this.funIcon = funIcon;
	}

	public String getFunTarget() {
		return this.funTarget;
	}

	public void setFunTarget(String funTarget) {
		this.funTarget = funTarget;
	}

	public String getFunMemo() {
		return this.funMemo;
	}

	public void setFunMemo(String funMemo) {
		this.funMemo = funMemo;
	}

	public Long getFunIndex() {
		return this.funIndex;
	}

	public void setFunIndex(Long funIndex) {
		this.funIndex = funIndex;
	}

	public String getFunUrl() {
		return this.funUrl;
	}

	public void setFunUrl(String funUrl) {
		this.funUrl = funUrl;
	}

}
