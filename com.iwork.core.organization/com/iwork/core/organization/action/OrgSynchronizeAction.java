package com.iwork.core.organization.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.iwork.core.organization.service.OrgSynchronizeService;
import com.opensymphony.xwork2.ActionSupport;

public class OrgSynchronizeAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgSynchronizeAction.class);
	
	private OrgSynchronizeService orgSynchronizeService;
	
	
	/**
	 * 组织机构同步管理
	 * @return
	 */
	public String index(){
		
		
		return SUCCESS;
	}
	
	public void doExcute(){
		orgSynchronizeService.doExcute();
		
	}
	
	public void setOrgSynchronizeService(OrgSynchronizeService orgSynchronizeService) {
		this.orgSynchronizeService = orgSynchronizeService;
	}
	
	
}
