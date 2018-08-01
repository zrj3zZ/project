package com.iwork.plugs.addressbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.commons.util.AddressBookUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgGroupDAO;
import com.iwork.core.organization.dao.OrgGroupSubDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;


public class MultiAddressBookService {
	private OrgGroupDAO orgGroupDAO;
	private OrgGroupSubDAO orgGroupSubDAO;
	private OrgRoleDAO orgRoleDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private OrgUserMapDAO orgUserMapDAO;

	/**
	 * 组织结构树初始化时，只显示公司
	 * @return
	 */
	public String getCompanyNodeJson(){
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		//未传入部门ID,则显示全部信息
			List orgcompanyList = orgCompanyDAO.getAll();  
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId());
				item.put("name", model.getCompanyname());
				item.put("open", "true"); 
				item.put("nodeType", "com");  
				item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
				item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
				item.put("companytype",model.getCompanytype());
				item.put("companyno", model.getCompanyno());
				item.put("companydesc",model.getCompanydesc());
				item.put("orderindex", model.getOrderindex());
				item.put("lookandfeel",model.getLookandfeel()); 
				item.put("orgadress",model.getOrgadress());
				item.put("post",model.getPost());
				item.put("email",model.getEmail());
				item.put("tel",model.getTel());
				item.put("zoneno",model.getZoneno());
				item.put("zonename",model.getZonename());
				item.put("extend1",model.getExtend1());
				item.put("extend2",model.getExtend2());
				item.put("extend3",model.getExtend3());
				item.put("extend4",model.getExtend4());
				item.put("extend5",model.getExtend5()); 
				List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
				List<Map<String,Object>> childrenItems = new ArrayList<Map<String,Object>>();
				for(int j=0;j<departmentList.size();j++){
					OrgDepartment dept = (OrgDepartment)departmentList.get(j);
					//装载部门列表 
					Map<String,Object> map = buildTreeDeptNode(dept);
					if(map!=null){
						childrenItems.add(map);
					}
				}
				if(childrenItems!=null&&childrenItems.size()>0){ 
					JSONArray childrenJson = JSONArray.fromObject(childrenItems);
					item.put("children", childrenJson.toString()); 
				}
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptsAndUserJson(List<Long> deptIds,String input){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		if(deptIds!=null){
			if(deptIds.size()==1){
				ArrayList selectList = this.getselectList(input); 
				String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
				//获得部门列表
				List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId),deptIds.get(0));
				for(OrgDepartment dept:deptlist){
					Map<String,Object> childrenItem = buildTreeDeptNode(dept); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
				}
				List<OrgUser> currentUserList = new ArrayList();//当前部门用户列表（用于去除兼职列表中的重复用户）
				//获得用户列表
				List<OrgUser> userList = orgUserDAO.getActiveUserList(Long.parseLong(companyId),deptIds.get(0));
				for(OrgUser model:userList){
					if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
						Map<String, Object> item = buildTreeUserNode(model,selectList);
						if(item!=null){
							list.add(item);
							currentUserList.add(model);
						}
					} 
				}
				//获得行政兼职用户列表
				List<OrgUserMap> userMapList = orgUserMapDAO.getOrgUserMap_DeptId(deptIds.get(0).intValue(), OrgUserMap.USER_TYPE_ORG);
				for(OrgUserMap oum:userMapList){
					String userid = oum.getUserid();  //获取用户帐号
					if(userid==null)continue;
					//获取用户信息
					OrgUser model = orgUserDAO.getUserModel(userid);
					if(model!=null){
						Long userstatus = model.getUserstate();
						if(userstatus.equals(new Long(0))){//判断用户状态是否正常1
							//判断是否已经添加过了
							if(currentUserList.contains(model)){ 
								continue;  //判断是否重复添加用户
							}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
								Map<String, Object> item = new HashMap<String, Object>();
								item.put("id", model.getUserid()); 
								item.put("name",model.getUsername());
								if(oum.getIsmanager()==null||oum.getIsmanager().equals("0")){
									item.put("icon", "iwork_img/user_business_boss.png");  
								}else{
									item.put("icon", "iwork_img/user.png"); 
								} 
								item.put("userName", model.getUsername());
								item.put("userId", model.getUserid()); 
								String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
								if(selectList!=null){
									if(selectList.contains(useraddress)){
										item.put("checked",true);
									}
								}
								item.put("useraddress", useraddress); 
								item.put("userno", model.getUserno());
								item.put("deptname", oum.getDepartmentname()); 
								item.put("deptId", oum.getDepartmentid());
								item.put("orgroleid",oum.getOrgroleid());
								list.add(item);  
								currentUserList.add(model);  //装载到当前用户列表
							}else{ 
								continue;
							}
						}
					}
				}
			}else{
				for(Long deptId:deptIds){
					OrgDepartment dept = orgDepartmentDAO.getBoData(deptId);
					if(dept!=null){
						Map<String,Object> childrenItem = buildTreeDeptNode(dept); 
						if(childrenItem!=null){
							list.add(childrenItem);
						}
					}
				}
			}
		}
		
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptAndUserJson(Long deptId,String input){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
				ArrayList selectList = this.getselectList(input); 
				OrgDepartment departMent = orgDepartmentDAO.getBoData(deptId);
				String companyId = departMent.getCompanyid();
				// String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
				//获得部门列表
				List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId),deptId);
				for(OrgDepartment dept:deptlist){
					Map<String,Object> childrenItem = buildTreeDeptNode(dept); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
				}
				List<OrgUser> currentUserList = new ArrayList();//当前部门用户列表（用于去除兼职列表中的重复用户）
				//获得用户列表
				List<OrgUser> userList = orgUserDAO.getActiveUserList(Long.parseLong(companyId),deptId);
				for(OrgUser model:userList){
					if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
						Map<String, Object> item = buildTreeUserNode(model,selectList);
						if(item!=null){
							list.add(item);
							currentUserList.add(model);
						}
					} 
				}
				//获得行政兼职用户列表
				List<OrgUserMap> userMapList = orgUserMapDAO.getOrgUserMap_DeptId(deptId.intValue(), OrgUserMap.USER_TYPE_ORG);
				for(OrgUserMap oum:userMapList){
					String userid = oum.getUserid();  //获取用户帐号
					if(userid==null)continue;
					//获取用户信息
					OrgUser model = orgUserDAO.getUserModel(userid);
					if(model!=null){
						Long userstatus = model.getUserstate();
						if(userstatus.equals(new Long(0))){//判断用户状态是否正常1
							//判断是否已经添加过了
							if(currentUserList.contains(model)){ 
								continue;  //判断是否重复添加用户
							}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
								Map<String, Object> item = new HashMap<String, Object>();
								item.put("id", model.getUserid()); 
								item.put("name",model.getUsername());
								if(oum.getIsmanager()==null||oum.getIsmanager().equals("0")){
									item.put("icon", "iwork_img/user_business_boss.png");  
								}else{
									item.put("icon", "iwork_img/user.png"); 
								} 
								item.put("userName", model.getUsername());
								item.put("userId", model.getUserid()); 
								String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
								if(selectList!=null){
									if(selectList.contains(useraddress)){
										item.put("checked",true);
									}
								}
								item.put("useraddress", useraddress); 
								item.put("userno", model.getUserno());
								item.put("deptname", oum.getDepartmentname()); 
								item.put("deptId", oum.getDepartmentid());
								item.put("orgroleid",oum.getOrgroleid());
								list.add(item);  
								currentUserList.add(model);  //装载到当前用户列表
							}else{ 
								continue;
							}
						}
					}
				}
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	/**
	 * 查询人员
	 * @param searchKey
	 * @return
	 */
	public String showMultiAddressList(String filedName,String searchKey){
		StringBuffer html = new StringBuffer();	
		html.append("<ul style=\"list-style:none\">");
		List<OrgUser> list = orgUserDAO.getSearchUserLists(null,searchKey);
		int count = 0;
		for(OrgUser model:list){
			count++;
			html.append("<li><input onclick=\"if(checked){$('#selecthtml').val(this.value);}else{$('#selectResult').val().replace(this.value,'');}\"  type=\"checkbox\" name=\"").append(filedName).append("_searchlist\" value=\"").append(model.getUserid()).append("[").append(model.getUsername()).append("]").append("\" id=\"").append(filedName).append("_searchlist_").append(count).append("\"><label for=\"").append(filedName).append("_searchlist_").append(count).append("\" >").append(model.getUsername()).append("</label></li> ");
		} 
		html.append("</ul>");
		return html.toString();
	}
	/**
	 * 构建部门节点
	 * @param list
	 * @param dept
	 * @return
	 */
	public Map<String,Object> buildTreeDeptNode(OrgDepartment dept){
		Map<String,Object> childrenItem = new HashMap<String,Object>();
		childrenItem.put("id", dept.getId());
		childrenItem.put("name", dept.getDepartmentname());
		childrenItem.put("open", "true"); 
		childrenItem.put("isParent", true); 
		childrenItem.put("nodeType", "dept");
		childrenItem.put("nodeId", dept.getId());
		childrenItem.put("companyid",dept.getCompanyid());
		childrenItem.put("departmentdesc", dept.getDepartmentdesc());
		childrenItem.put("departmentno",dept.getDepartmentno());
		childrenItem.put("layer",dept.getLayer());
		childrenItem.put("orderindex", dept.getOrderindex());			
		childrenItem.put("zoneno",dept.getZoneno()); 
		childrenItem.put("zonename",dept.getZonename());
		childrenItem.put("extend1",dept.getExtend1());
		childrenItem.put("extend2",dept.getExtend2());
		childrenItem.put("extend3",dept.getExtend3());
		childrenItem.put("extend4",dept.getExtend4());
		childrenItem.put("extend5",dept.getExtend5()); 
		return childrenItem; 
	}
	/**
	 * 构建人员节点
	 * @param list
	 * @param orguser
	 * @return
	 */
	private Map<String, Object> buildTreeUserNode(OrgUser model,ArrayList<String> selectList){
		return buildTreeUserNode(model,selectList,false);
	}
	
		/**
		 * 构建人员节点
		 * @param model
		 * @param selectList
		 * @param isShowDept
		 * @return
		 */
		private Map<String, Object> buildTreeUserNode(OrgUser model,ArrayList<String> selectList,boolean isShowDept){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", model.getUserid()); 	
		Long currentCompanyId = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getCompanyid();
		StringBuffer title = new StringBuffer();
		title.append(model.getUsername()); 
		if(!model.getCompanyid().equals(currentCompanyId)){
			UserContext showUser = UserContextUtil.getInstance().getUserContext(model.getUserid());
			if(showUser!=null){
				title.append("[").append(showUser.get_companyModel().getCompanyname());
				if(isShowDept){
					title.append(" ").append(model.getDepartmentname());
				}
				title.append("]"); 
			} 
		}else{
			if(isShowDept){
				title.append(" ").append(model.getDepartmentname());
			}
		}
		item.put("name",title.toString());  
		 
		if(model.getIsmanager()!=null&&model.getIsmanager().equals(new Long(1))){
			item.put("icon", "iwork_img/user_business_boss.png");  
		}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
			item.put("icon", "iwork_img/user.png"); 
			//item.put("icon", "iwork_img/user_silhouette.png"); 
		}
		String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
		if(selectList!=null){
			if(selectList.contains(useraddress)){
				item.put("checked",true);
			}
		}
		item.put("userName", model.getUsername());
		item.put("userId", model.getUserid()); 
		item.put("useraddress",useraddress); 
		item.put("userno", model.getUserno());
		item.put("deptname", model.getDepartmentname()); 
		item.put("deptId", model.getDepartmentid());
		item.put("orgroleid",model.getOrgroleid());
		return item;
	}
	
	/**
	 * 组织结构树初始化时，只显示公司
	 * @return
	 */
	public String getCompanyNodeJson(String id, String nodeType, boolean flag){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<OrgCompany> orgCompanyList = new ArrayList<OrgCompany>();
		
		// 构建当前登录所属公司的子公司
		if(nodeType!=null && nodeType.equals("com")){
			orgCompanyList = orgCompanyDAO.getCompanyList(id);
			if(orgCompanyList!=null && orgCompanyList.size()>0){
				for(OrgCompany model : orgCompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item = this.buildTreeCompanyNode(model);
					
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(model.getId());
					
					if((subCompanyList!=null&&subCompanyList.size()>0)){
						item.put("children", this.getCompanyNodeJson(model.getId(),nodeType, false)); 
					}
					List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
					if(departmentList!=null&&departmentList.size()>0){
						item.put("isParent", true); 
					}
					items.add(item);
				}
			}
		}
		
		// 构建部门
		List departmentList = null;
		if(nodeType != null && nodeType.equals("com")){ 
			departmentList = orgDepartmentDAO.getTopDepartmentList(id);
		}else{
			departmentList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(id)); 
		} 
		if(departmentList != null){
			for(int j=0;j<departmentList.size();j++){
				Map<String,Object> subItem = new HashMap<String,Object>();
				OrgDepartment dept = (OrgDepartment)departmentList.get(j);
				//装载部门列表 
				subItem = this.buildTreeDeptNode(dept);
				items.add(subItem);
			}
		}
		
		// 构建根节点
		JSONArray json = null;
		if(flag){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgCompany  orgCompany = orgCompanyDAO.getBoData(id);
			if(orgCompany!=null){
				item = this.buildTreeCompanyNode(orgCompany);
				item.put("children", items);
				root.add(item);
				json = JSONArray.fromObject(root);
			}
		}else{
			json = JSONArray.fromObject(items);
		}
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 
	 * Description:构建公司节点
	 * Params:参数描述
	 * Return:Map<String,Object>
	 *
	 * @author:zouyalei
	 * @date  :2015-7-24下午02:59:17
	 */
	public Map<String,Object> buildTreeCompanyNode(OrgCompany model){
		Map<String,Object> item = new HashMap<String,Object>();
		item.put("id", model.getId());
		item.put("name", model.getCompanyname());
		item.put("open", "true"); 
		item.put("nodeType", "com");  
		item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
		item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
		item.put("companyid",model.getId());
		item.put("companytype",model.getCompanytype());
		item.put("companyno", model.getCompanyno());
		item.put("companydesc",model.getCompanydesc());
		item.put("orderindex", model.getOrderindex());
		item.put("lookandfeel",model.getLookandfeel());
		item.put("orgadress",model.getOrgadress());
		item.put("post",model.getPost());
		item.put("email",model.getEmail());
		item.put("tel",model.getTel());
		item.put("zoneno",model.getZoneno());
		item.put("zonename",model.getZonename());
		item.put("extend1",model.getExtend1());
		item.put("extend2",model.getExtend2());
		item.put("extend3",model.getExtend3());
		item.put("extend4",model.getExtend4());
		item.put("extend5",model.getExtend5()); 
		
		return item;
	}
	
	/**
	 * 查询人员
	 * @param searchKey
	 * @return
	 */
	public String searchOrgJson(String searchKey,List<Long> deptIds,String input){
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String, Object>> list = new ArrayList();
		List<OrgUser> userList = null;
		if(deptIds!=null ){
			for(Long deptId:deptIds){
				if(deptId!=null&&searchKey!=null){
					userList = orgUserDAO.getSearchUserLists(deptId, searchKey.toUpperCase()); 
				}else if(deptId==null&&searchKey!=null){
					userList = orgUserDAO.getSearchUserLists(null, searchKey.toUpperCase());
				}
			}
		}else{
			userList = orgUserDAO.getSearchUserLists(null, searchKey.toUpperCase());
		}
		if(userList!=null && userList.size()>0){
			ArrayList selectList = this.getselectList(input); 
			for(OrgUser model:userList){
				if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
					Map<String, Object> item = buildTreeUserNode(model,selectList,true); 
					if(item!=null){
						list.add(item);
					}
				}
			}
		}
		
		JSONArray json = JSONArray.fromObject(list);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getSelectOrgJson(String input){
		StringBuffer jsonHtml = new StringBuffer();	
		ArrayList selectlist = new ArrayList();
		if(input!=null){
			String[] list = input.split(",");
			for(String str:list){ 
				if(str.equals(""))continue;
				String userid = UserContextUtil.getInstance().getUserId(str);
				if(!"".equals(userid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
					if(uc!=null){
						selectlist.add(this.buildTreeUserNode(uc.get_userModel(), null)); 
					}
				}
			}
			JSONArray json = JSONArray.fromObject(selectlist);
			jsonHtml.append(json);
		}
		if(jsonHtml.toString().equals("")){
			return "[]";
		}
		return jsonHtml.toString();
	}
	/**
	 * 动态获取选择列表
	 * @param input
	 * @return
	 */
	private ArrayList getselectList(String input){
		ArrayList selectlist = null;
		if(input!=null){
			String[] list = input.split(",");
			selectlist = new ArrayList();
			for(String str:list){ 
				if(str.equals(""))continue;
				selectlist.add(str); 
			}
		}
		return selectlist;
	}
	
	
	/**
	 * 获得指定部门列表
	 * @param type
	 * @param deptlist
	 * @return
	 */
	public String getDeptForUser(String deptid,String type,String deptlist){
		List<Map<String, Object>> list = new ArrayList();
		StringBuffer jsonHtml = new StringBuffer();
		if(deptid==null&&deptlist!=null){
			String[] idlist = deptlist.split(",");
			for(String id:idlist){
				String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
				//获得部门列表
				    OrgDepartment  dept = orgDepartmentDAO.getBoData(Long.parseLong(id));
					Map<String,Object> childrenItem = buildTreeDeptNode(dept); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
			}
			JSONArray json = JSONArray.fromObject(list); 
			jsonHtml.append(json); 
		}else if(deptid!=null){ 
			List deptids = new ArrayList();
			jsonHtml.append(this.getDeptAndUserJson(Long.parseLong(deptid), null));
		}
		return jsonHtml.toString();
	}
	/**
	 */
	public Long getParentDeptId(Long deptId) {
		OrgDepartment dept = orgDepartmentDAO.getBoData(deptId);
		return dept.getParentdepartmentid();
	}
	
	/**
	 */
	public Long getCurrentDeptId(Long deptId) {
		OrgDepartment dept = orgDepartmentDAO.getBoData(deptId);
		return dept.getId();
	}
	
	public OrgGroupDAO getOrgGroupDAO() {
		return orgGroupDAO;
	}
	public void setOrgGroupDAO(OrgGroupDAO orgGroupDAO) {
		this.orgGroupDAO = orgGroupDAO;
	}
	public OrgGroupSubDAO getOrgGroupSubDAO() {
		return orgGroupSubDAO;
	}
	public void setOrgGroupSubDAO(OrgGroupSubDAO orgGroupSubDAO) {
		this.orgGroupSubDAO = orgGroupSubDAO;
	}
	public OrgRoleDAO getOrgRoleDAO() {
		return orgRoleDAO;
	}
	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}
	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}
	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}
	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}
	public OrgUserMapDAO getOrgUserMapDAO() {
		return orgUserMapDAO;
	}
	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}
	
}
