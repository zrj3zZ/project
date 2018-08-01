package com.iwork.plugs.addressbook.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.RadioAddressBookService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class RadioAddressBookAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(RadioAddressBookAction.class);
	private RadioAddressBookService radioAddressBookService;
	
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
	private String actDefId;
	private String companyid;
	
	public String index() {
		return SUCCESS;
	}
	public String realIndex() {

		return SUCCESS;
	}
	
	/**
	 * 获得用户树
	 * @return
	 * @throws ParseException
	 */
	public String realjson() throws ParseException {
		String json = "";
		if(actDefId!=null && !"".equals(actDefId)){
			json = radioAddressBookService.getRealJson(actDefId);
		}
		ResponseUtil.write(json); 
		return null;
		
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
		if(nodeType != null){ 
			if(id!=null && nodeType.equals("dept")){
				json = radioAddressBookService.getDeptAndUserJson(new Long(id));
			}else{
				json = radioAddressBookService.getCompanyNodeJson(id,nodeType,false); 
			}
		}else{ 
			Long dept = null;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			startDept = startDept == null ? "0l" : startDept;
			currentDept = currentDept == null ? "" : currentDept;
			parentDept = parentDept == null ? "" : parentDept;
			if (parentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getParentdepartmentid();			
				json = radioAddressBookService.getDeptAndUserJson(new Long(dept));
			} else if (currentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getId(); 
				json = radioAddressBookService.getDeptAndUserJson(new Long(dept));
				addressName = uc.get_deptModel().getDepartmentname();
			} else if (!startDept.equals("")) {
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
				json = radioAddressBookService.getDeptsAndUserJson(idlist);
				
			} else {
				nodeType = "com";
				json = radioAddressBookService.getCompanyNodeJson(companyid,nodeType,true); 
			}  
		}
		ResponseUtil.write(json);
		return null;
		
	}
	/**
	 * 执行查询操作
	 */ 
	public void search(){
		if(checkLoginInfo(searchOrg)){
			ResponseUtil.write("含非法字符!"); 
    	}else{
			String json = "";
			Long dept = null;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			startDept = startDept == null ? "0l" : startDept;
			currentDept = currentDept == null ? "" : currentDept;
			parentDept = parentDept == null ? "" : parentDept;
			if(searchOrg!=null){
				if (parentDept.equalsIgnoreCase("true")) {
					dept = uc.get_deptModel().getParentdepartmentid();		
					List list = new ArrayList();
					list.add(dept);
					json = radioAddressBookService.searchOrgJson(searchOrg, list);
				} else if (currentDept.equalsIgnoreCase("true")) {
					dept = uc.get_deptModel().getId(); 
					List list = new ArrayList();
					list.add(dept);
					json = radioAddressBookService.searchOrgJson(searchOrg, list);
					addressName = uc.get_deptModel().getDepartmentname();
				} else if (!startDept.equals("")) {
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
					json = radioAddressBookService.searchOrgJson(searchOrg, idlist);
				} else {   
					json = radioAddressBookService.searchOrgJson(searchOrg, null);
				}  
			}
			ResponseUtil.write(json); 
    	}
	}
	private  boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
	public RadioAddressBookService getRadioAddressBookService() {
		return radioAddressBookService;
	}

	public void setRadioAddressBookService(
			RadioAddressBookService radioAddressBookService) {
		this.radioAddressBookService = radioAddressBookService;
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
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

}
