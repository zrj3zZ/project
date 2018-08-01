package com.iwork.plugs.cms.model;

import java.util.Date;

/**
 * IworkCmsComment entity. @author MyEclipse Persistence Tools
 */

public class IworkCmsComment implements java.io.Serializable {

	public static String DATABASE_ENTITY = "IworkCmsComment";
	// Fields

	private Long id;
	private Integer contentid;
	private Integer readcount;
	private String talkname;
	private String talkcontent;
	private Date talktime;
	private String readuser;
	private String ipaddress;

	// Constructors

	/** default constructor */
	public IworkCmsComment() {
	}

	/** minimal constructor */
	public IworkCmsComment(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkCmsComment(Long id, Integer contentid, Integer readcount,
			String talkname, String talkcontent, Date talktime,
			String readuser, String ipaddress) {
		this.id = id;
		this.contentid = contentid;
		this.readcount = readcount;
		this.talkname = talkname;
		this.talkcontent = talkcontent;
		this.talktime = talktime;
		this.readuser = readuser;
		this.ipaddress = ipaddress;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getContentid() {
		return this.contentid;
	}

	public void setContentid(Integer contentid) {
		this.contentid = contentid;
	}

	public Integer getReadcount() {
		return this.readcount;
	}

	public void setReadcount(Integer readcount) {
		this.readcount = readcount;
	}

	public String getTalkname() {
		return this.talkname;
	}

	public void setTalkname(String talkname) {
		this.talkname = talkname;
	}

	public String getTalkcontent() {
		return this.talkcontent;
	}

	public void setTalkcontent(String talkcontent) {
		this.talkcontent = talkcontent;
	}

	public Date getTalktime() {
		return this.talktime;
	}

	public void setTalktime(Date talktime) {
		this.talktime = talktime;
	}

	public String getReaduser() {
		return this.readuser;
	}

	public void setReaduser(String readuser) {
		this.readuser = readuser;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

}