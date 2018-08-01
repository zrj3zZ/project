package com.iwork.plugs.rss.model;

/**
 * BdInfRssdyjlb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BdInfRssdyjlb implements java.io.Serializable {

	// Fields

	private Long id;
	private Long linesize;
	private String groupname;
	private String title;
	private String keyword;
	private String rssurl;
	private String type;
	private String createuser;

	// Constructors

	/** default constructor */
	public BdInfRssdyjlb() {
	}

	/** minimal constructor */
	public BdInfRssdyjlb(Long id) {
		this.id = id;
	}

	/** full constructor */
	public BdInfRssdyjlb(Long id, Long linesize, String groupname,
			String title, String keyword, String rssurl, String type,
			String createuser) {
		this.id = id;
		this.linesize = linesize;
		this.groupname = groupname;
		this.title = title;
		this.keyword = keyword;
		this.rssurl = rssurl;
		this.type = type;
		this.createuser = createuser;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLinesize() {
		return this.linesize;
	}

	public void setLinesize(Long linesize) {
		this.linesize = linesize;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getRssurl() {
		return this.rssurl;
	}

	public void setRssurl(String rssurl) {
		this.rssurl = rssurl;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

}