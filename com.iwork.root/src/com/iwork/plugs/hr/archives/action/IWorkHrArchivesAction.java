package com.iwork.plugs.hr.archives.action;

import java.io.File;
import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.hr.archives.model.StaffPageModel;
import com.iwork.plugs.hr.archives.service.IWorkHrArchivesService;
import com.iwork.plugs.hr.constants.IWorkHrConstants;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkHrArchivesAction  extends ActionSupport{

	private String toolbar;
	private String content;
	private String script;
	private String btnHtml;
	private String ids;
	private String userImgPath;
	private String userid;
	private String html;
	
	private String tabkey;
	
	private IWorkHrArchivesService iWorkHrArchivesService;
	/**
	 * 员工花名册首页
	 * @return
	 */
	public String index(){
		StaffPageModel  model = iWorkHrArchivesService.getStaffModel(tabkey);
		if(model!=null){
			toolbar = model.getToolbar();
				btnHtml = model.getBtnHtml();
				content = model.getContent();
		}
		userid = UserContextUtil.getInstance().getCurrentUserId();
		String imageFileName = userid+".jpg";
		String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) +File.separator+ imageFileName;
	    File userImage = new File(path);
	    if(!userImage.exists()){
	    	this.setUserImgPath("iwork_img/default_userImg.jpg");
	    }else{
	    	String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
	    	this.setUserImgPath(imgPath);
	    }
		return SUCCESS;
	}
	
	
	
	/**
	 * 加载员工自助设置页面
	 * @return
	 */
	public String showSet(){
		
		return SUCCESS;
	}
	/**
	 * 执行员工自助设置页面
	 * @return
	 */
	public void showJson(){
		String json = iWorkHrArchivesService.showOverAllTreeJSON();
		ResponseUtil.write(json);
		
	}
	/** 
	 * 执行员工自助设置页面
	 * @return
	 */
	public void doSet(){
		if(ids!=null){
			SysPersonConfig spg = UserContextUtil.getInstance().getCurrentUserConfig(IWorkHrConstants.HR_STAFF_SELF_USERCONFIG);
			if(spg==null){
				 spg = new SysPersonConfig();
				 spg.setType(IWorkHrConstants.HR_STAFF_SELF_USERCONFIG);
				 spg.setUserid(UserContextUtil.getInstance().getCurrentUserId());
			}
			spg.setValue(ids);
			UserContextUtil.getInstance().setCurrentUserConfig(IWorkHrConstants.HR_STAFF_SELF_USERCONFIG, spg);
			ResponseUtil.write(SUCCESS);
		}
		
	}
	
	
	public String infoTip(){
		
		return SUCCESS;
	}
	
	/**
	 * 员工花名册查询
	 * @return
	 */
	public String searchIndex(){
		
		return SUCCESS;
	}
	
	/**
	 * 员工卡片信息
	 * @return
	 */
	public String cardIndex(){
		return SUCCESS;
	}

	public String getToolbar() {
		return toolbar;
	}

	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}

	public IWorkHrArchivesService getiWorkHrArchivesService() {
		return iWorkHrArchivesService;
	}

	public void setiWorkHrArchivesService(
			IWorkHrArchivesService iWorkHrArchivesService) {
		this.iWorkHrArchivesService = iWorkHrArchivesService;
	}

	public String getTabkey() {
		return tabkey;
	}

	public void setTabkey(String tabkey) {
		this.tabkey = tabkey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBtnHtml() {
		return btnHtml;
	}

	public void setBtnHtml(String btnHtml) {
		this.btnHtml = btnHtml;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}


	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getUserImgPath() {
		return userImgPath;
	}


	public void setUserImgPath(String userImgPath) {
		this.userImgPath = userImgPath;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
}
