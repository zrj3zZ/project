package com.iwork.app.tools.addressbook.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.app.tools.addressbook.constant.SysAddressBookConstant;
import com.iwork.app.tools.addressbook.service.SysAddressBookService;
import com.opensymphony.xwork2.ActionSupport;

public class SysAdreesBookAction extends ActionSupport {
	private SysAddressBookService sysAddressBookService;
	
	private String type; //地址簿类型
	private String targetField;   //插入目标字段
	private String departmentid;
	private String pid;
	





	/**
	 * 获得地址簿树
	 * @return
	 */
	public String getAddressTree() {
		if("".equals(type)||SysAddressBookConstant.ADDRESS_TYPE1.equals(type)){
			sysAddressBookService.getRadioAddressTree(departmentid, targetField);
			return "radioAddress";
				
		}else if(SysAddressBookConstant.ADDRESS_TYPE2.equals(type)){
			sysAddressBookService.getRadioAddressTree(departmentid, targetField);
			return "moreAddress";
		}else{
			sysAddressBookService.getRadioAddressTree(departmentid, targetField);
			return "radioAddress";
		}
	}
	
	/**
	 * 打开树节点
	 * @return
	 * @throws Exception
	 */
	public String openAddressJsonTree(){
		String json = "";
		if(pid==null)pid = "0";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = sysAddressBookService.getAddressTreeJson(Integer.parseInt(pid));
		
		request.setAttribute("addressTreeJson", json);
		return SUCCESS;	
	}
	

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getTargetField() {
		return targetField;
	}


	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}


	public String getDepartmentid() {
		return departmentid;
	}


	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}


	public void setSysAddressBookService(SysAddressBookService sysAddressBookService) {
		this.sysAddressBookService = sysAddressBookService;
	}
}
