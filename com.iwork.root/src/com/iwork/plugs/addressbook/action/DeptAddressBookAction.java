package com.iwork.plugs.addressbook.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.DeptAddressBookService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

public class DeptAddressBookAction extends ActionSupport {
	private DeptAddressBookService deptAddressBookService;
	private static Logger logger = Logger.getLogger(DeptAddressBookAction.class);

	private String id;
	private String userId;
	private String userName;
	private String selectHtml;
	
	private String typeHtml; //可选类型，通过参考录入生成，分 组织结构、角色、团队三种
	private String startDept;
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
	private String deptlist;
	private String companyid;
	
	public String index() {
		return SUCCESS;
	}

	
	public String multiIndex() {
		return SUCCESS;
	}
	/**
	 * 获得用户树
	 * @return
	 * @throws ParseException
	 */
	public String orgjson() throws ParseException {
		String json = "";
		if(companyid==null || "".equals(companyid)){
			companyid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
		}
		if(nodeType!=null){ 
			if(id!=null&&nodeType.equals("dept")){
				json = deptAddressBookService.getDeptAndUserJson(new Long(id));
			}else{
				json = deptAddressBookService.getCompanyNodeJson(companyid,nodeType,false); 
			}
		}else{ 
			Long dept = null;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			startDept = startDept == null ? "0l" : startDept;
			currentDept = currentDept == null ? "" : currentDept;
			parentDept = parentDept == null ? "" : parentDept;
			if (parentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getParentdepartmentid();			
				json = deptAddressBookService.getDeptAndUserJson(new Long(dept));
			} else if (currentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getId(); 
				json = deptAddressBookService.getDeptAndUserJson(new Long(dept));
				addressName = uc.get_deptModel().getDepartmentname();
			}else if (!startDept.equals("")) {
				List idlist = new ArrayList();
				String[] sd = startDept.split(";");
				for(String it:sd){
					if(it.trim().equals("")){
						continue;
					}
					try{
						Long id = Long.parseLong(it);
						idlist.add(new Long(it));
					}catch(Exception e){logger.error(e,e);
					}
				}
				json = deptAddressBookService.getDeptsAndUserJson(idlist);
				
			} else {
				nodeType = "com";
				json = deptAddressBookService.getCompanyNodeJson(companyid,nodeType,true); 
			}  
		}
		ResponseUtil.write(json); 
		return null;
		
	}

	public DeptAddressBookService getDeptAddressBookService() {
		return deptAddressBookService;
	}

	public void setDeptAddressBookService(
			DeptAddressBookService deptAddressBookService) {
		this.deptAddressBookService = deptAddressBookService;
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
	
	public String getStartDept() {
		return startDept;
	}

	public void setStartDept(String startDept) {
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

	public String getDeptlist() {
		return deptlist;
	}

	public void setDeptlist(String deptlist) {
		this.deptlist = deptlist;
	}
	
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
}
