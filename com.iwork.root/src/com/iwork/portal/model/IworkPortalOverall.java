package com.iwork.portal.model;


/**
 * IworkPortalOverall entity. @author MyEclipse Persistence Tools
 */

public class IworkPortalOverall implements java.io.Serializable {

	// Fields

	private Long id;
	private String groupName;
	private String title;
	private Long type;
	private Long height; 
	private String interface_;
	private String urlLink;
	private String moreLink;
	private Long status;

	// Constructors

	/** default constructor */
	public IworkPortalOverall() {
	}

	/** full constructor */
	public IworkPortalOverall(String groupName, String title, Long type,
			Long height, String interface_, String urlLink,
			String moreLink, Long status) {
		this.groupName = groupName;
		this.title = title;
		this.type = type;
		this.height = height;
		this.interface_ = interface_;
		this.urlLink = urlLink;
		this.moreLink = moreLink;
		this.status = status;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getHeight() {
		return this.height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public String getInterface_() {
		return this.interface_;
	}

	public void setInterface_(String interface_) {
		this.interface_ = interface_;
	}

	public String getUrlLink() {
		return this.urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getMoreLink() {
		return this.moreLink;
	}

	public void setMoreLink(String moreLink) {
		this.moreLink = moreLink;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}