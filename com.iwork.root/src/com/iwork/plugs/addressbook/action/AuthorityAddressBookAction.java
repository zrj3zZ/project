package com.iwork.plugs.addressbook.action;

import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.AuthorityAddressBookService;
import com.opensymphony.xwork2.ActionSupport;

public class AuthorityAddressBookAction extends ActionSupport {
	//private OrgGroupService orgGroupService;
	private AuthorityAddressBookService authorityAddressBookService;
	
	private String target;
	private String code;
	private String selectHtml;
	
	public String demo() {
		return "demo";
	}
	
	public String index(){
		selectHtml = PurviewCommonUtil.codeToSelectHtml(code);
		return "index";
	}
	
	public String personTree() throws ParseException {
		String json = "";
		code = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		List<String> list = PurviewCommonUtil.getTypeList(code, PurviewCommonUtil.USER_PREFIX);
		json = authorityAddressBookService.getPersonTreeJson(list);
		ResponseUtil.write(json);
		return null;
		
	}
	
	public String deptTree() throws ParseException {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		List<String> list = PurviewCommonUtil.getTypeList(code, PurviewCommonUtil.DEPARTMENT_PREFIX);
		json = authorityAddressBookService.getDeptTreeJson(list);
		ResponseUtil.write(json);
		return null;
		
	}
	
	public String groupTree() throws ParseException {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		List<String> list = PurviewCommonUtil.getTypeList(code, PurviewCommonUtil.GROUP_PREFIX);
		json = authorityAddressBookService.getGroupTreeJson(list);
		ResponseUtil.write(json);
		return null;
		
	}
	
	public String roleTree() throws ParseException {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		List<String> list = PurviewCommonUtil.getTypeList(code, PurviewCommonUtil.ROLE_PREFIX);
		json = authorityAddressBookService.getRoleGroupTreeJson(list);
		ResponseUtil.write(json);
		return null;
	} 

//	public OrgGroupService getOrgGroupService() {
//		return orgGroupService;
//	}
//
//	public void setOrgGroupService(OrgGroupService orgGroupService) {
//		this.orgGroupService = orgGroupService;
//	}

	public AuthorityAddressBookService getAuthorityAddressBookService() {
		return authorityAddressBookService;
	}

	public void setAuthorityAddressBookService(
			AuthorityAddressBookService authorityAddressBookService) {
		this.authorityAddressBookService = authorityAddressBookService;
	}

	public String getCode() {
		return code;
	}


	public String getSelectHtml() {
		return selectHtml;
	}

	public void setSelectHtml(String selectHtml) {
		this.selectHtml = selectHtml;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	

}
