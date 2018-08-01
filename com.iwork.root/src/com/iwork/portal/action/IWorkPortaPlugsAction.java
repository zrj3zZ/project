package com.iwork.portal.action;

import com.iwork.portal.service.IWorkPortalPlugsService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkPortaPlugsAction extends ActionSupport {
	private IWorkPortalPlugsService iworkPortalPlugsService;
	private String topNews;
	private String calendarList;
	private Long portletid;
	/**
	 * 获得首页新闻
	 * @return
	 */
	public String showTopNews(){
		if(portletid==null)portletid = new Long(0); 
		topNews = iworkPortalPlugsService.getCmsTopNews(portletid);
		return SUCCESS; 
	}
	/**
	 * 我的日程
	 * @return
	 */
	public String mycalendar(){
		//calendarList = iworkPortalPlugsService
		return SUCCESS; 
	}
	
	
	public IWorkPortalPlugsService getIworkPortalPlugsService() {
		return iworkPortalPlugsService;
	}
	public void setIworkPortalPlugsService(
			IWorkPortalPlugsService iworkPortalPlugsService) {
		this.iworkPortalPlugsService = iworkPortalPlugsService;
	}
	public String getTopNews() {
		return topNews;
	}
	public void setTopNews(String topNews) {
		this.topNews = topNews;
	}
	public Long getPortletid() {
		return portletid;
	}
	public void setPortletid(Long portletid) {
		this.portletid = portletid;
	}
	public String getCalendarList() {
		return calendarList;
	}
	public void setCalendarList(String calendarList) {
		this.calendarList = calendarList;
	}
	
	

}
