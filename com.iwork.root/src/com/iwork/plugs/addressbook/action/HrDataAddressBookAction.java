package com.iwork.plugs.addressbook.action;

import java.text.ParseException;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.HrDataAddressBookService;
import com.opensymphony.xwork2.ActionSupport;

public class HrDataAddressBookAction extends ActionSupport {
	private HrDataAddressBookService hrDataAddressBookService;
	private String id;
	private String userId;
	private String userName; 
	private String selectHtml;
	
	private String typeHtml; //可选类型，通过参考录入生成，分 组织结构、角色、团队三种
	private Long startDept;
	private String currentDept;
	private String parentDept;
	private String nodeId;
	private String showType;
	private String nodeType;
	private String targetUserNo;
	private String targetUserId;
	private String targetUserName;
	private String targetDeptId;
	private String targetDeptName; 
	private String addressName; 
	private String searchOrg;
	private String defaultField; 
	private String input; 
	private String selectJSON; 
	private String params; 
	public String index() {
		return SUCCESS;
	}
	
	public String diyIndex(){
		if(showType!=null&&params!=null){
			
		}
		return SUCCESS;
	}
	
	public void diyOrgJson() {
		String json = "";
		ResponseUtil.write(json); 
	}
	 
	/**
	 * 获得用户树
	 * @return
	 * @throws ParseException
	 */
	public String showjson() throws ParseException {
		String json = ""; 
//				json = hrDataAddressBookService.getOrgJson(id);
		ResponseUtil.write(json); 
		return null;
		
	}
	/**
	 * 执行查询操作
	 */ 
	public void search(){
		String json = "";
//				json = hrDataAddressBookService.searchOrgJson(searchOrg);
		ResponseUtil.write(json); 
	}

	public String getUserId() {
		return userId;
	}

	public String getSelectHtml() {
		return selectHtml;
	}

	public void setSelectHtml(String selectHtml) {
		this.selectHtml = selectHtml;
	}
	
	public Long getStartDept() {
		return startDept;
	}

	public void setStartDept(Long startDept) {
		this.startDept = startDept;
	}

	public String getCurrentDept() {
		return currentDept;
	}

	public void setCurrentDept(String currentDept) {
		this.currentDept = currentDept;
	}

	public String getParentDept() {
		return parentDept;
	}

	public void setParentDept(String parentDept) {
		this.parentDept = parentDept;
	}

	public String getTypeHtml() {
		return typeHtml;
	}

	public void setTypeHtml(String typeHtml) {
		this.typeHtml = typeHtml;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTargetUserNo() {
		return targetUserNo;
	}

	public void setTargetUserNo(String targetUserNo) {
		this.targetUserNo = targetUserNo;
	}
	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getTargetDeptId() {
		return targetDeptId;
	}

	public void setTargetDeptId(String targetDeptId) {
		this.targetDeptId = targetDeptId;
	}

	public String getTargetDeptName() {
		return targetDeptName;
	}

	public void setTargetDeptName(String targetDeptName) {
		this.targetDeptName = targetDeptName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getSearchOrg() {
		return searchOrg;
	}

	public void setSearchOrg(String searchOrg) {
		this.searchOrg = searchOrg;
	}
	public String getDefaultField() {
		return defaultField;
	}
	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	public String getSelectJSON() {
		return selectJSON;
	}

	public void setSelectJSON(String selectJSON) {
		this.selectJSON = selectJSON;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public HrDataAddressBookService getHrDataAddressBookService() {
		return hrDataAddressBookService;
	}

	public void setHrDataAddressBookService(
			HrDataAddressBookService hrDataAddressBookService) {
		this.hrDataAddressBookService = hrDataAddressBookService;
	}
	
}
