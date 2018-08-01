package com.iwork.plugs.cms.model;

/**
 * IworkCmsRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkCmsRelation implements java.io.Serializable {
	public static String DATABASE_ENTITY = "IworkCmsRelation";
	// Fields

	private Long id;
	private Long channelid;
	private Long portletid;

	// Constructors

	/** default constructor */
	public IworkCmsRelation() {
	}

	/** full constructor */
	public IworkCmsRelation(Long channelid, Long portletid) {
		this.channelid = channelid;
		this.portletid = portletid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Long getPortletid() {
		return this.portletid;
	}

	public void setPortletid(Long portletid) {
		this.portletid = portletid;
	}

}
