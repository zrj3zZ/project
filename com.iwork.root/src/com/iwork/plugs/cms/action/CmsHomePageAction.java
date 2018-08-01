package com.iwork.plugs.cms.action;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.service.CmsHomePageService;
import com.iwork.plugs.cms.util.CmsPageModelUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 首页Action
 * 处理首页邮件及待办异步加载
 * @author zouyalei
 *
 */
public class CmsHomePageAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Long type;//知道类型
	private Long channelId;//频道类型
	String htmlShowStr;
	String pagePagingHtml;
	int pageSize;
	int pageNo;
	String moreTitle;
	private String sj;
	
	
	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	/** 首页Service */
	private CmsHomePageService cmsHomePageService;
	
	public CmsHomePageService getCmsHomePageService() {
		return cmsHomePageService;
	}

	public void setCmsHomePageService(CmsHomePageService cmsHomePageService) {
		this.cmsHomePageService = cmsHomePageService;
	}
	
	/**
	 * 加载待办流程
	 */
	public void getToDoHtml(){
		
		String toDoHtml = cmsHomePageService.getListHtml(null, 0, 0);
		
		ResponseUtil.writeTextUTF8(toDoHtml);
	}

	/**
	 * 动态加载首页信息咨询tab
	 */
	public void getHomeTab(){
		String homeTab = cmsHomePageService.getHomeTab();
		
		ResponseUtil.writeTextUTF8(homeTab);
	}
	/**
	 * 初始化  董秘行业动态tab
	 */
	public void getHydtTab(){
		String homeTab = cmsHomePageService.getHydtTab();
		
		ResponseUtil.writeTextUTF8(homeTab);
	}
	/**
	 * 查询公告信息
	 */
	public void getHomeHtml(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc == null) 
			{
			ResponseUtil.writeTextUTF8("");
			return;
			}
		OrgUser user = uc.get_userModel();
		if(channelId==null){
			Long firstId = cmsHomePageService.getFirstChannelid();
			channelId = firstId;
		}
	
		String homeHtml = cmsHomePageService.getHomeInfoHtml(channelId);
		
		ResponseUtil.writeTextUTF8(homeHtml);
	}
	/**
	 * 初始化滚动信息
	 */
	public void showGdtDiv(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc == null) 
			{
			ResponseUtil.writeTextUTF8("");
			return;
			}
		OrgUser user = uc.get_userModel();
		
	
		String homeHtml = "";
		if(user.getOrgroleid()==3){
			homeHtml = cmsHomePageService.showGdtDiv();
		}else{
			homeHtml = cmsHomePageService.showFDMGdtDiv();
		}
		ResponseUtil.writeTextUTF8(homeHtml);
	}
	/**
	 * 查询知道信息
	 */
	public void getKnowHtml(){
		if(type==null){
			type = 0L;
		}
		
		String knowHtml = cmsHomePageService.getKnowInfoHtml(type);
		
		ResponseUtil.writeTextUTF8(knowHtml);
	}
	/**
	 * 查询待办事宜
	 */
	public void getDBSY(){
		if(type==null){
			type = 0L;
		}
		
		String knowHtml = cmsHomePageService.getDBSYHtml(type);
		
		ResponseUtil.writeTextUTF8(knowHtml);
	}
	/**
	 * 查询日程提醒  or  问答
	 */
	public void getRctx(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String knowHtml="";
		if( user.getOrgroleid()==3)  {
			knowHtml = cmsHomePageService.getWd();
		}else{
			if(user.getOrgroleid()==4){
				knowHtml = cmsHomePageService.getRctx();
			}else{
				knowHtml = cmsHomePageService.getfddRctx();
			}
			
		}
		ResponseUtil.writeTextUTF8(knowHtml);
	}
	
	/**
	 * 日程提醒完成事件
	 */
	public void addRcwc(){
		try {
			cmsHomePageService.addRcwc(type);
		} catch (Exception e) {}
	}
	/**
	 * 周期日程提醒完成事件
	 */
	public void updRcwc(){
		try {
			cmsHomePageService.updRcwc(type,sj);
		} catch (Exception e) {}
	}
	/**
	 * 查询Email
	 */
	public void getEmailHtml(){
		
		String emailHtml = cmsHomePageService.getEmailHtml();
		
		ResponseUtil.writeTextUTF8(emailHtml);
	}
	
	/**
	 * 查询首页待审批
	 */
	public void getTodoHtml(){
		
		String todoHtml = cmsHomePageService.getTodoHtml();
		
		ResponseUtil.writeTextUTF8(todoHtml);
	}
	/**
	 * 查询首页我的公文
	 */
	public void getGovHtml(){
		
		String govHtml = cmsHomePageService.getGovHtml();
		
		ResponseUtil.writeTextUTF8(govHtml);
	}

	/**
	 * 查询首页常用资料
	 */
	public void getAppkmHtml(){
		
		String appkmHtml = cmsHomePageService.getAppkmHtml();
		
		ResponseUtil.writeTextUTF8(appkmHtml);
	}
	/**
	 * 更多常用资料
	 * @return
	 */
	public String getMoreAppkmHtmlStr() {

		if (pageSize == 0) {
			pageSize = 10;
		}

		if (pageNo == 0) {
			pageNo = 1;
		}

		htmlShowStr = cmsHomePageService.getAppkmShowStr(pageSize, pageNo);
		int dataCount = cmsHomePageService.getAppkmCount();
		String url = "getMoreAppkmHtmlStr.action?";
		pagePagingHtml = CmsPageModelUtil.getInstance().getPageModelHtmlStr(url, dataCount, pageSize, pageNo);
		moreTitle = "常用资料";

		return SUCCESS;
	}
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getHtmlShowStr() {
		return htmlShowStr;
	}

	public void setHtmlShowStr(String htmlShowStr) {
		this.htmlShowStr = htmlShowStr;
	}

	public String getPagePagingHtml() {
		return pagePagingHtml;
	}

	public void setPagePagingHtml(String pagePagingHtml) {
		this.pagePagingHtml = pagePagingHtml;
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

	public String getMoreTitle() {
		return moreTitle;
	}

	public void setMoreTitle(String moreTitle) {
		this.moreTitle = moreTitle;
	}
	
	
}
