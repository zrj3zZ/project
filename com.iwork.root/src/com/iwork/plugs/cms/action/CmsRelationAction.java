package com.iwork.plugs.cms.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.service.CmsRelationService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * CMS频道与栏目关系跳转类
 * @author WeiGuangjian
 *
 */
public class CmsRelationAction extends ActionSupport{

	private CmsRelationService cmsRelationService;
	private String contentid;
	private String channelid;
	private String addstr;
	private String html;
	
	/**
	 * 主页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
        this.setChannelid(channelid);
        html=cmsRelationService.getPortletList(channelid);
        this.setHtml(html);
		return "index";
	}
	
	/**
	 * 获取当前频道栏目表
	 * @return
	 * @throws Exception
	 */
	public void contentgrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();	
		json = cmsRelationService.getGridJson(channelid);
		ResponseUtil.write(json);
	}
	/**
	 * 获取当前频道栏目表
	 * @return
	 * @throws Exception
	 */
	public void showlist() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();	
		json = cmsRelationService.getGridJson(channelid);
		ResponseUtil.write(json);
	}
	
	/**
	 * 获取栏目树
	 * @return
	 * @throws Exception
	 */
	public void portlettree() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsRelationService.getTreeJson();
		ResponseUtil.write(json);
	}
	
	/**
	 * 添加
	 * @return
	 * @throws Exception
	 */
	public String cmsAddPortlet()throws Exception{	
		cmsRelationService.cmsAddPortlet(channelid,addstr); 
		this.setChannelid(channelid);
		html=cmsRelationService.getPortletList(channelid);
	    this.setHtml(html);
		return "index";	
	}
	
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String cmsRemovePortlet()throws Exception{	
		cmsRelationService.cmsRemovePortlet(contentid); 
		this.setChannelid(channelid);
		html=cmsRelationService.getPortletList(channelid);
	    this.setHtml(html);
		return "index";	
	}
	
	public CmsRelationService getCmsRelationService() {
		return cmsRelationService;
	}

	public void setCmsRelationService(CmsRelationService cmsRelationService) {
		this.cmsRelationService = cmsRelationService;
	}
	
	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getAddstr() {
		return addstr;
	}

	public void setAddstr(String addstr) {
		this.addstr = addstr;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	
	
}
