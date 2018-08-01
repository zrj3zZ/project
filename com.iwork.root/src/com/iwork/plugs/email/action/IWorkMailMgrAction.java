package com.iwork.plugs.email.action;

import com.iwork.plugs.email.service.IWorkMailMgrService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailMgrAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private IWorkMailMgrService iWorkMailMgrService;

	/**
	 * 系统管理
	 * @return
	 */
	public String index(){
		
		return SUCCESS;
	}
	
	
	public void setiWorkMailMgrService(IWorkMailMgrService iWorkMailMgrService) {
		this.iWorkMailMgrService = iWorkMailMgrService;
	}
	
}
