package com.iwork.core.organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;

public class OrgRoleService {
	private OrgRoleDAO orgRoleDAO;
	
	/**
	 * 获得角色树视图ＪＳＯＮ
	 * @return
	 */
	public String getRoleTreeJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<OrgRoleGroup> list = orgRoleDAO.getRoleTypeList();
		Iterator itr = list.iterator();
		for (OrgRoleGroup org:list) {
			
			Map<String, Object> childrenItem = new HashMap<String, Object>();
			childrenItem.put("id", org.getId());
			childrenItem.put("name", org.getGroupName());
			childrenItem.put("open", "true"); 
			childrenItem.put("nodeType", "group");
			childrenItem.put("nocheck", true);
			childrenItem.put("master",  org.getMaster());
			childrenItem.put("icon", "iwork_img/images/folder.gif");
			childrenItem.put("memo",  org.getMemo());
			childrenItem.put("nodeId",  org.getId());
			childrenItem.put("children", this.getRoleTreeJsonById(org.getId()));
			items.add(childrenItem);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	private String getRoleTreeJsonById(Long groupId) {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<OrgRole> list = orgRoleDAO.getRoleList(groupId);
		for (OrgRole model : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", model.getId());
			item.put("name", model.getRolename());
			item.put("open", "true"); 
			item.put("nodeType", "role");
			item.put("nodeId", model.getId());
			item.put("groupid", model.getGroupid());
			item.put("groupname", model.getGroupname());
			item.put("roletype", model.getRoletype());
			items.add(item);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 获得分组列表页签
	 * @param groupId
	 * @return
	 */
	public String getTabList(Long groupId){
		List<OrgRoleGroup> list = orgRoleDAO.getRoleGroupList();
		StringBuffer html = new StringBuffer();
		html.append("<dl class=\"demos-nav\"> ").append("\n");
		if(groupId==null){
			html.append("	<dd><a class=\"selected\" href=\"role_list.action\" target=\"_self\">全部</a></dd> ").append("\n");
		}else{
			html.append("	<dd><a  href=\"role_list.action\" target=\"_self\">全部</a></dd> ").append("\n");
		}
		
		for(OrgRoleGroup model:list){
			if(model.getId().equals(groupId)){
				html.append("	<dd><a class=\"selected\" href=\"role_list.action?groupId=").append(model.getId()).append("\"  target=\"_self\">").append(model.getGroupName()).append("</a></dd> ").append("\n");
			}else{
				html.append("	<dd><a href=\"role_list.action?groupId=").append(model.getId()).append("\"  target=\"_self\">").append(model.getGroupName()).append("</a></dd> ").append("\n");	
			}
		}
		html.append("</dl>").append("\n");
		return html.toString();
	}
	
	public void addBoData(OrgRole obj) {
		orgRoleDAO.addBoData(obj);
	}

	public void deleteBoData(String id) {
		OrgRole model=orgRoleDAO.getBoData(id);
		orgRoleDAO.deleteBoData(model);
	}
	
	/**
	 * 删除分组信息
	 * @param model
	 */
	public String delGroup(Long groupId){
		String msg = "";
		if(groupId!=null){
			List<OrgRole> list = orgRoleDAO.getRoleList(groupId);
			//如果分组目录下包含角色，则不允许删除
			if(list!=null&&list.size()>0){
				msg = "ishavingsub";
			}else{
				OrgRoleGroup group = orgRoleDAO.getRoleGroupModel(groupId);
				if(group!=null){
					orgRoleDAO.delGroup(group);
					msg ="success";
				}else{
					msg = "error";
				}
			}
		}
		return msg;
	}
	/**
	 * 获得全部列表
	 * @return
	 */
	public List getAll() {
		// TODO Auto-generated method stub
		return orgRoleDAO.getAll();
	}
/**
 * 获得角色对象模型
 * @param id
 * @return
 */
	public OrgRole getBoData(String id) {
		// TODO Auto-generated method stub
		return orgRoleDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return orgRoleDAO.getBoDatas(pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return orgRoleDAO.getMaxID();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return orgRoleDAO.getRows();
	}

	public int getRows(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgRoleDAO.getRows(fieldname, value);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgRoleDAO.queryBoDatas(fieldname, value);
	}  

	public void updateBoData(OrgRole obj) {
		orgRoleDAO.updateBoData(obj);

	}
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		orgRoleDAO.updateIndex(id,type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		orgRoleDAO.updateIndex(id, type);
	}

	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}

	public OrgRoleDAO getOrgRoleDAO() {
		return orgRoleDAO;
	}
	
}
