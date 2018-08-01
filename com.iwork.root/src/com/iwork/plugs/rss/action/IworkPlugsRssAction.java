package com.iwork.plugs.rss.action;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.rss.model.BdInfRssdyjlb;
import com.iwork.plugs.rss.service.IworkPlugsRssService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * RssAction RSS订阅
 * 
 * @author SuQi
 * 
 */
public class IworkPlugsRssAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private IworkPlugsRssService rssInformationService;
	private BdInfRssdyjlb rssModel;
	private String id;
	private String userProfile;// 用户配置信息
	private String pageList;// 页面信息
	private String responseText;

	/**
	 * 获得RSS订阅页面
	 * 
	 * @return
	 */
	public String getIndexPage() throws Exception {
		pageList = rssInformationService.getIndexList();
		return SUCCESS;
	}

	/**
	 * 获得填加RSS订阅页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAddRssPage() throws Exception {
		return SUCCESS;
	}

	/**
	 * 设置用户RSS订阅页面配置信息
	 * 
	 * @return
	 */
	public String setRssUserProfile() throws Exception {
		rssInformationService.setUserProfile(userProfile);
		return null;
	}

	/**
	 * 获取用户RSS订阅页面配置信息
	 * 
	 * @return
	 */
	public String getRssUserProfile() throws Exception {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		userProfile = rssInformationService.getUserProfile(uid);
		ResponseUtil.writeTextUTF8(userProfile);
		return null;
	}

	/**
	 * 增加RSS订阅
	 * 
	 * @return
	 */
	public String addRssSubscription() throws Exception {
		responseText = rssInformationService.addRssSubscription(rssModel);
		ResponseUtil.writeTextUTF8(responseText);
		return null;
	}

	/**
	 * 删除RSS订阅
	 * 
	 * @return
	 */
	public String deleteRssSubscription() throws Exception {
		rssInformationService.deleteRssSubscription(id);
		ResponseUtil.writeTextUTF8("success");
		return null;
	}

	/**
	 * 重置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String resetRssUserProfile() throws Exception {
		rssInformationService.resetRssUserProfile();
		return null;
	}

	/*--------------------get/set-----------------------*/

	public IworkPlugsRssService getRssInformationService() {
		return rssInformationService;
	}

	public void setRssInformationService(IworkPlugsRssService rssInformationService) {
		this.rssInformationService = rssInformationService;
	}

	public BdInfRssdyjlb getRssModel() {
		return rssModel;
	}

	public void setRssModel(BdInfRssdyjlb rssModel) {
		this.rssModel = rssModel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getPageList() {
		return pageList;
	}

	public void setPageList(String pageList) {
		this.pageList = pageList;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

}
