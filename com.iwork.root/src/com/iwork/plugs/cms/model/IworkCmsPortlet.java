package com.iwork.plugs.cms.model;

import java.util.Date;

/**
 * IworkCmsPortlet entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkCmsPortlet implements java.io.Serializable {
	public static String DATABASE_ENTITY = "IworkCmsPortlet";
	// Fields

	private Long id;
	private String portletkey;
	private String portletname;
	private String groupname;
	private String manager;
	private String template;
	private Long verifytype;
	private String browse;
	private Long portlettype;
	private String param;
	private Date begindate;
	private Date enddate;
	private Long status;
	private String memo;
	private Long prows;
	private Long pwidth;
	private Long pheight;
	private Long isborder;
	private Long istitle;
	private String morelink;
	private String linktarget;
	private Long orderindex;

	// Constructors

	/** default constructor */
	public IworkCmsPortlet() {
	}

	/** full constructor */
	public IworkCmsPortlet(String portletkey, String portletname,
			String groupname, String manager, String template, Long verifytype,
			String browse, Long portlettype, String param, Date begindate,
			Date enddate, Long status, String memo, Long prows, Long pwidth,
			Long pheight, Long isborder, Long istitle, String morelink, String linktarget) {
		this.portletkey = portletkey;
		this.portletname = portletname;
		this.groupname = groupname;
		this.manager = manager;
		this.template = template;
		this.verifytype = verifytype;
		this.browse = browse;
		this.portlettype = portlettype;
		this.param = param;
		this.begindate = begindate;
		this.enddate = enddate;
		this.status = status;
		this.memo = memo;
		this.prows = prows;
		this.pwidth = pwidth;
		this.pheight = pheight;
		this.isborder = isborder;
		this.istitle = istitle;
		this.morelink = morelink;
		this.linktarget = linktarget;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPortletkey() {
		return this.portletkey;
	}

	public void setPortletkey(String portletkey) {
		this.portletkey = portletkey;
	}

	public String getPortletname() {
		return this.portletname;
	}

	public void setPortletname(String portletname) {
		this.portletname = portletname;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Long getVerifytype() {
		return this.verifytype;
	}

	public void setVerifytype(Long verifytype) {
		this.verifytype = verifytype;
	}

	public String getBrowse() {
		return this.browse;
	}

	public void setBrowse(String browse) {
		this.browse = browse;
	}

	public Long getPortlettype() {
		return this.portlettype;
	}

	public void setPortlettype(Long portlettype) {
		this.portlettype = portlettype;
	}

	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Date getBegindate() {
		return this.begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getProws() {
		return this.prows;
	}

	public void setProws(Long prows) {
		this.prows = prows;
	}

	public Long getPwidth() {
		return this.pwidth;
	}

	public void setPwidth(Long pwidth) {
		this.pwidth = pwidth;
	}

	public Long getPheight() {
		return this.pheight;
	}

	public void setPheight(Long pheight) {
		this.pheight = pheight;
	}

	public Long getIsborder() {
		return this.isborder;
	}

	public void setIsborder(Long isborder) {
		this.isborder = isborder;
	}

	public Long getIstitle() {
		return this.istitle;
	}

	public void setIstitle(Long istitle) {
		this.istitle = istitle;
	}

	public String getMorelink() {
		return this.morelink;
	}

	public void setMorelink(String morelink) {
		this.morelink = morelink;
	}

	public String getLinktarget() {
		return linktarget;
	}

	public void setLinktarget(String linktarget) {
		this.linktarget = linktarget;
	}

	public Long getOrderindex() {
		return orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

	
}
