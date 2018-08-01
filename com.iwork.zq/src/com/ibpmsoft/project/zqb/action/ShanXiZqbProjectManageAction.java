package com.ibpmsoft.project.zqb.action;

import com.ibpmsoft.project.zqb.service.ShanXiZqbProjectManageService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ShanXiZqbProjectManageAction extends ActionSupport{
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectNo;
	private Long instanceId;
	private String customerNo;
	private String xmlx;
	private String jdmc;
	private ShanXiZqbProjectManageService shanXiZqbProjectManageService;
	
	public void xmlxCommit(){
		String jsonString=shanXiZqbProjectManageService.xmlxCommit(instanceId,projectNo);
		ResponseUtil.write(jsonString);
	}
	
	public void setCustomerContent(){
		boolean flag = shanXiZqbProjectManageService.setCustomerContent(instanceId);
	}
	
	public void getCustomerContent(){
		String jsonString=shanXiZqbProjectManageService.getCustomerContent(customerNo);
		ResponseUtil.write(jsonString);
	}
	
	public void getXmlxContent(){
		String jsonString=shanXiZqbProjectManageService.getXmlxContent(instanceId,projectNo,customerNo);
		ResponseUtil.write(jsonString);
	}
	
	public void commitAuthority(){
		String jsonString=shanXiZqbProjectManageService.commitAuthority(projectNo,xmlx,jdmc);
		ResponseUtil.write(jsonString);
	}
	
	public void xmlxContent(){
		String jsonString=shanXiZqbProjectManageService.xmlxContent(customerNo);
		ResponseUtil.write(jsonString);
	}
	
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getXmlx() {
		return xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public void setShanXiZqbProjectManageService(
			ShanXiZqbProjectManageService shanXiZqbProjectManageService) {
		this.shanXiZqbProjectManageService = shanXiZqbProjectManageService;
	}
	
}
