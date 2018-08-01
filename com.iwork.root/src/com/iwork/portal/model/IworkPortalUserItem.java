package com.iwork.portal.model;

import java.util.Date;

/**
 * IworkPortalUserItem entity. @author MyEclipse Persistence Tools
 */

public class IworkPortalUserItem implements java.io.Serializable {

	// Fields

	private Long id;
	private String userid;
	private Long portletId;
	private String title;
	private String style;
	private Long height;
	private Long titleIsshow;
	private Date createdate;
	private Long groupindex;
	private Long orderIndex;

	// Constructors

	/** default constructor */
	public IworkPortalUserItem() {
	}

	/** full constructor */
	public IworkPortalUserItem(String userid, Long portletId,
			String title, String style, Long height,
			Long titleIsshow, Date createdate, Long groupindex,
			Long orderIndex) {
		this.userid = userid;
		this.portletId = portletId;
		this.title = title;
		this.style = style;
		this.height = height;
		this.titleIsshow = titleIsshow;
		this.createdate = createdate;
		this.groupindex = groupindex;
		this.orderIndex = orderIndex;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getPortletId() {
		return this.portletId;
	}

	public void setPortletId(Long portletId) {
		this.portletId = portletId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Long getHeight() {
		return this.height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getTitleIsshow() {
		return this.titleIsshow;
	}

	public void setTitleIsshow(Long titleIsshow) {
		this.titleIsshow = titleIsshow;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Long getGroupindex() {
		return this.groupindex;
	}

	public void setGroupindex(Long groupindex) {
		this.groupindex = groupindex;
	}

	public Long getOrderIndex() {
		return this.orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

}