package com.iwork.app.theme.action;

import com.iwork.app.theme.service.IWorkThemeService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkThemeAction extends ActionSupport {
	private IWorkThemeService iworkThemeService;

	
	public void showUserPhoto(){
		
		
	}
	
	
	public void setIworkThemeService(IWorkThemeService iworkThemeService) {
		this.iworkThemeService = iworkThemeService;
	}
}
