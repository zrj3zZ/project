package com.iwork.plugs.cms.action;

import sun.util.logging.resources.logging;

import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.service.CmsPortletService;
import com.iwork.plugs.cms.util.CmsMyDesktopPortletUtil;
import com.iwork.plugs.cms.util.CmsPageModelUtil;
import com.opensymphony.xwork2.ActionSupport;

public class CmsMyDesktopPortletAction extends ActionSupport {

	private CmsPortletService cmsPortletService;
	String commonType;
	String cityName;
	String key;
	String htmlShowStr;
	Long showId;
	int pageSize;
	int pageNo;
	String pagePagingHtml;
	int portletId;
	String moreTitle;
	int dataCount ;

	/**
	 * 获得系统栏目列表
	 * @return
	 */
	public String getCommonTypeShowStr() {

		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getCommonShowStr(
				showId);

		return SUCCESS;
	}

	/**
	 * 天气预报栏目HTML
	 * @return
	 */
	public String getWeatherShowStr() {

		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getWeatherShowStr(
				cityName);

		return SUCCESS;
	}
	/**
	 * 视频栏目HTML
	 * @return
	 */
	public String getVideoShowStr() {

		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getVideoShowStr();

		return SUCCESS;
	}
	/**
	 * 常用资料 HTML
	 * @return
	 */
	public String getAppkmShowStr() {

		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getAppkmShowStr();

		return SUCCESS;
	}
	/**
	 * RSS HTML
	 * @return
	 */
	public String getKsBlogShowStr() {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getKsBlogShowStr(
				user.getUserid());

		return SUCCESS;
	}
	/**
	 * 轮播图 HTML 首页
	 * @return
	 */
	public String getPicShowStr() {
		Long portletId = null;
		if(key!=null){
			IworkCmsPortlet portlet = cmsPortletService.getCmsPortletDAO().getPortletModel(key);
			if(portlet!=null){
				portletId = portlet.getId();
			}
		}
		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getPicShowStr("myDesktop",portletId);
		return SUCCESS;
	}
	/**
	 * 轮播图 HTML 我的桌面
	 * @return
	 */
	public String getPicHtmlDivStr() {
		Long portletId = null;
		if(key!=null){
			IworkCmsPortlet portlet = cmsPortletService.getCmsPortletDAO().getPortletModel(key);
			if(portlet!=null){
				portletId = portlet.getId();
			}
		} 
		
		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getPicShowStr("index",portletId);

		if (htmlShowStr != null && htmlShowStr.length() > 0) {
			ResponseUtil.writeTextUTF8(htmlShowStr);
		}

		return null;
	}
	/**
	 * 更多页面，改版使用下面新的
	 * @return
	 */
//	public String getMoreCommonTypeHtmlStr() {
//
//		if (pageSize == 0) {
//			pageSize = 10;
//		}
//
//		if (pageNo == 0) {
//			pageNo = 1;
//		}
//		if(showId==0){
//			pageSize = 18;
//		}
//		if(showId==14){
//			pageSize = 18;
//		}
//		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getCommonShowStr(showId, pageSize, pageNo);
//		int dataCount=0;
//		try {
//			dataCount = CmsMyDesktopPortletUtil.getInstance().getCommonTypeCount(showId);
//		} catch (Exception e) {
//		}
//		String url = "getMoreCommonTypeHtmlStr.action?showId=" + showId + "&";
//		pagePagingHtml = CmsPageModelUtil.getInstance().getPageModelHtmlStr(url, dataCount, pageSize, pageNo);
//		moreTitle = CmsMyDesktopPortletUtil.getInstance().getMoreTitleStr(showId);
//
//		return SUCCESS;
//	}
	public String getMoreCommonTypeHtmlStr() {

		if (pageSize == 0) {
			pageSize = 15;
		}

		if (pageNo == 0) {
			pageNo = 1;
		}
		htmlShowStr = CmsMyDesktopPortletUtil.getInstance().getCommonShowStr(showId, pageSize, pageNo);
		
		try {
			dataCount = CmsMyDesktopPortletUtil.getInstance().getCommonTypeCount(showId);
		} catch (Exception e) {
			dataCount=0;
		}
		moreTitle = CmsMyDesktopPortletUtil.getInstance().getMoreTitleStr(showId);
		

		return SUCCESS;
	}
	/**
	 * 打开更多时的标题选取
	 * @return
	 */
	public String getMoreTitleStr() {

		if (portletId >= 0) {
			String title = CmsMyDesktopPortletUtil.getInstance()
					.getMoreTitleStr(portletId);
			ResponseUtil.writeTextUTF8(title);
		} else {
			ResponseUtil.writeTextUTF8("更多内容");
		}

		return null;
	}

	/**
	 * 搜索类型取出
	 * @return
	 */
	public String getSearchTypeHtml() {

		String result = CmsMyDesktopPortletUtil.getInstance()
				.getSearchTypeHtml();
		ResponseUtil.writeTextUTF8(result);

		return null;
	}

	public String getMoreTitle() {
		return moreTitle;
	}

	public void setMoreTitle(String moreTitle) {
		this.moreTitle = moreTitle;
	}

	public int getPortletId() {
		return portletId;
	}

	public void setPortletId(int portletId) {
		this.portletId = portletId;
	}

	public String getCommonType() {
		return commonType;
	}

	public void setCommonType(String commonType) {
		this.commonType = commonType;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getHtmlShowStr() {
		return htmlShowStr;
	}

	public void setHtmlShowStr(String htmlShowStr) {
		this.htmlShowStr = htmlShowStr;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getPagePagingHtml() {
		return pagePagingHtml;
	}

	public void setPagePagingHtml(String pagePagingHtml) {
		this.pagePagingHtml = pagePagingHtml;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setCmsPortletService(CmsPortletService cmsPortletService) {
		this.cmsPortletService = cmsPortletService;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

	
	
}
