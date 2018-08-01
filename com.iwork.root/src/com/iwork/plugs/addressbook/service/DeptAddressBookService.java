package com.iwork.plugs.addressbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.commons.util.AddressBookUtil;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgGroupDAO;
import com.iwork.core.organization.dao.OrgGroupSubDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;


public class DeptAddressBookService {
	private OrgGroupDAO orgGroupDAO;
	private OrgGroupSubDAO orgGroupSubDAO;
	private OrgRoleDAO orgRoleDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptsAndUserJson(List<Long> deptIds){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		if(deptIds!=null&&deptIds.size()>0){
			if(deptIds.size()==1){
				//获得部门列表
				List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(deptIds.get(0));
				for(OrgDepartment dept:deptlist){
					List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
					boolean isLeaf = true;
					if(templist!=null&&templist.size()>0){
						isLeaf = false;
					}
					Map<String,Object> childrenItem = buildTreeDeptNode(dept,isLeaf); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
				}
			}else{
				for(Long deptId:deptIds){
					OrgDepartment dept = orgDepartmentDAO.getBoData(deptId);
					boolean isLeaf = true;
					Map<String,Object> childrenItem = buildTreeDeptNode(dept,isLeaf); 
					if(childrenItem!=null){
						list.add(childrenItem);
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
	public String getDeptAndUserJson(Long deptId){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		//获得部门列表
		List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(deptId);
		for(OrgDepartment dept:deptlist){
			List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
			boolean isLeaf = true;
			if(templist!=null&&templist.size()>0){
				isLeaf = false;
			}
			Map<String,Object> childrenItem = buildTreeDeptNode(dept,isLeaf); 
			if(childrenItem!=null){
				list.add(childrenItem);
			}
		}
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	
	/**
	 * 构建部门节点
	 * @param list
	 * @param dept
	 * @return
	 */
	private Map<String,Object> buildTreeDeptNode(OrgDepartment dept,boolean isLeaf){
		Map<String,Object> childrenItem = new HashMap<String,Object>();
		childrenItem.put("id", dept.getId());
		childrenItem.put("name", dept.getDepartmentname());
		childrenItem.put("open", "true"); 
		childrenItem.put("nodeType", "dept");
		if(isLeaf){
			childrenItem.put("icon", "iwork_img/model.gif");
			childrenItem.put("isParent", false);
		}else{
			childrenItem.put("isParent", true);
		}
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
	private Map<String, Object> buildTreeUserNode(OrgUser model){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", model.getUserid()); 
		item.put("name",model.getUsername());
		if(model.getIsmanager().equals(new Long(1))){
			item.put("icon", "iwork_img/user_business_boss.png");  
		}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
			item.put("icon", "iwork_img/user.png"); 
			//item.put("icon", "iwork_img/user_silhouette.png"); 
		}
		item.put("userName", model.getUsername());
		item.put("userId", model.getUserid()); 
		item.put("useraddress", AddressBookUtil.generateUid(model.getUserid(),model.getUsername()));
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
				
				List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
				boolean isLeaf = true;
				if(templist!=null&&templist.size()>0){
					isLeaf = false;
				}
				//装载部门列表 
				subItem = buildTreeDeptNode(dept,isLeaf);
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
	
}
