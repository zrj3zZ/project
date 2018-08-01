package com.iwork.plugs.hr.staff.impl;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.hr.staff.IWorkHrStaffInterface;
import com.iwork.plugs.hr.staff.model.ConfigModel;

public class IWorkHrStaffAbst implements IWorkHrStaffInterface {

	private UserContext me;
	private ConfigModel configModel;
	private String url;
	public IWorkHrStaffAbst(UserContext uc,ConfigModel configModel){
		me = uc;
		this.configModel = configModel;
		this.url = url;
	}

	public String getScript(){
		
		return null;
	}
	public String getBtnHtml(){
		 
		return null;
	}
	public String getContent() {
		// TODO Auto-generated method stub
		StringBuffer html = new StringBuffer();
		html.append("<iframe width='100%' height='99%' src =\"").append(url).append("\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\" frameborder=\"0\" scrolling=\"auto\" id=\"ifm\" name=\"ifm\" />").append("\n");
		return html.toString();
	}
	public UserContext getUserContext() {
		return me;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public ConfigModel getConfigModel() {
		return configModel;
	}

}
