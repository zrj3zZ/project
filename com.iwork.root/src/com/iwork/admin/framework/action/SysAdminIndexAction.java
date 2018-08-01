package com.iwork.admin.framework.action;

import com.iwork.admin.framework.service.SysAdminIndexService;
import com.iwork.admin.util.AdminUtil;
import com.opensymphony.xwork2.ActionSupport;


public class SysAdminIndexAction extends ActionSupport {
	public String toolbar ;
	public String menu ;
	public String html ;
	public SysAdminIndexService sysAdminIndexService;
	/**
	 * 加载控制台首页
	 * @return
	 */
	public String index(){
		if(AdminUtil.getInstance().isManagerLogin()){
			toolbar = sysAdminIndexService.getToolbarHtml();
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	/**
	 * 首页看版
	 * @return
	 */
	public String dashboard(){
		if(AdminUtil.getInstance().isManagerLogin()){
			html = sysAdminIndexService.getPortletsHtml();
			return SUCCESS;
		}else{
			return ERROR;
		}
	}


	public String getToolbar() {
		return toolbar;
	}


	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}


	public SysAdminIndexService getSysAdminIndexService() {
		return sysAdminIndexService;
	}


	public void setSysAdminIndexService(SysAdminIndexService sysAdminIndexService) {
		this.sysAdminIndexService = sysAdminIndexService;
	}


	public String getMenu() {
		return menu;
	}


	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
}
