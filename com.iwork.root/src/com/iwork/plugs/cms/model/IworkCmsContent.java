package com.iwork.plugs.cms.model;

import java.util.Date;

/**
 * IworkCmsContent entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkCmsContent implements java.io.Serializable {
	public static String DATABASE_ENTITY = "IworkCmsContent";
	// Fields

	private Long id;
	private Long channelid;
	private String title;
	private String brieftitle;
	private Date releasedate;
	private String releaseman;
	private String releasedept; 
	private String source;
	private String prepicture;
	private String precontent;
	private String content;
	private Long istalk;
	private Long iscopy;
	private Long markred;
	private Long markbold;
	private Long marktop;
	private String keyword;
	private String archives;
	private Long status;
	private Date updatedate;
	private String updateman;
	private String updatedept; 
	private String browse;
	private String sort;//分类
	// Constructors

	/** default constructor */
	public IworkCmsContent() {
	}

	/** full constructor */
	public IworkCmsContent(Long channelid, String title, String brieftitle,
			Date releasedate, String releaseman, String releasedept,
			String source, String prepicture, String precontent,
			String content, Long istalk, Long iscopy, Long markred,
			Long markbold, Long marktop, String keyword, String archives,
			Long status, Date updatedate, String updateman, String updatedept,
			String browse,String sort) {
		this.channelid = channelid;
		this.title = title;
		this.brieftitle = brieftitle;
		this.releasedate = releasedate;
		this.releaseman = releaseman;
		this.releasedept = releasedept;
		this.source = source;
		this.prepicture = prepicture;
		this.precontent = precontent;
		this.content = content;
		this.istalk = istalk;
		this.iscopy = iscopy;
		this.markred = markred;
		this.markbold = markbold;
		this.marktop = marktop;
		this.keyword = keyword;
		this.archives = archives;
		this.status = status;
		this.updatedate = updatedate;
		this.updateman = updateman;
		this.updatedept = updatedept;
		this.browse = browse;
		this.sort=sort;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrieftitle() {
		return this.brieftitle;
	}

	public void setBrieftitle(String brieftitle) {
		this.brieftitle = brieftitle;
	}

	public Date getReleasedate() {
		return this.releasedate;
	}

	public void setReleasedate(Date releasedate) {
		this.releasedate = releasedate;
	}

	public String getReleaseman() {
		return this.releaseman;
	}

	public void setReleaseman(String releaseman) {
		this.releaseman = releaseman;
	}

	public String getReleasedept() {
		return this.releasedept;
	}

	public void setReleasedept(String releasedept) {
		this.releasedept = releasedept;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPrepicture() {
		return this.prepicture;
	}

	public void setPrepicture(String prepicture) {
		this.prepicture = prepicture;
	}

	public String getPrecontent() {
		return this.precontent;
	}

	public void setPrecontent(String precontent) {
		this.precontent = precontent;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getIstalk() {
		return this.istalk;
	}

	public void setIstalk(Long istalk) {
		this.istalk = istalk;
	}

	public Long getIscopy() {
		return this.iscopy;
	}

	public void setIscopy(Long iscopy) {
		this.iscopy = iscopy;
	}

	public Long getMarkred() {
		return this.markred;
	}

	public void setMarkred(Long markred) {
		this.markred = markred;
	}

	public Long getMarkbold() {
		return this.markbold;
	}

	public void setMarkbold(Long markbold) {
		this.markbold = markbold;
	}

	public Long getMarktop() {
		return this.marktop;
	}

	public void setMarktop(Long marktop) {
		this.marktop = marktop;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getArchives() {
		return this.archives;
	}

	public void setArchives(String archives) {
		this.archives = archives;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdateman() {
		return this.updateman;
	}

	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}

	public String getUpdatedept() {
		return this.updatedept;
	}

	public void setUpdatedept(String updatedept) {
		this.updatedept = updatedept;
	}

	public String getBrowse() {
		return this.browse;
	}

	public void setBrowse(String browse) {
		this.browse = browse;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
