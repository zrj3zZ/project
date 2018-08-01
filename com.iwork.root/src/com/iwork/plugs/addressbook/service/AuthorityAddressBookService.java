package com.iwork.plugs.addressbook.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgGroupDAO;
import com.iwork.core.organization.dao.OrgGroupSubDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgGroup;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;
import com.iwork.core.organization.model.OrgUser;


public class AuthorityAddressBookService {
	private OrgGroupDAO orgGroupDAO;
	private OrgGroupSubDAO orgGroupSubDAO;
	private OrgRoleDAO orgRoleDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	
	public String getRoleGroupTreeJson(List<String> selected) throws ParseException {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<OrgRoleGroup> list = orgRoleDAO.getRoleTypeList();
		for (OrgRoleGroup model : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", model.getId());
			
			item.put("text", model.getGroupName());
			item.put("iconCls", "icon-ok");
			item.put("children", this.getRoleTreeJsonById(model.getId().toString(), selected));
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye","roleGroup");
			item.put("attributes", attributes);	
			items.add(item);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public List<Map<String,Object>> getRoleTreeJsonById(String groupId, List<String> selected) throws ParseException {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<OrgRole> list = orgRoleDAO.getRoleList(Long.parseLong(groupId));
		for (OrgRole model : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", model.getId() + "");
			if (selected.contains(model.getId()+"")) {
				item.put("checked", "true");
			}
			item.put("text", model.getRolename());
			item.put("iconCls", "icon-ok");
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye","roleLeaf");
			item.put("attributes", attributes);	
			items.add(item);
		}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	public List<Map<String,Object>> getRoleTreeJson(List<String> selected) throws ParseException {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<OrgRole> list = orgRoleDAO.getAll();
		for (OrgRole model : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", model.getId() + "");
			if (selected.contains(model.getId()+"")) {
				item.put("checked", "true");
			}
			item.put("text", model.getRolename());
			item.put("iconCls", "icon-ok");
			items.add(item);
		}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	
	public String getGroupTreeJson(List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		//获取当前日期
		Date Time = new Date();
	  	String now=dateFormat(Time);
		Date nowTime=dateFormat(now);
		List<OrgGroup> list = orgGroupDAO.getOrgGroupList(nowTime);
			for(int i=0;i<list.size();i++){
				OrgGroup model = list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_group"); // changelog: type change, item_id, item_type
				item.put("id", model.getId()+"");
				if (selected.contains(model.getId()+"")) {
					item.put("checked", "true");
				}
				item.put("text", model.getGroupName());
				item.put("iconCls", "icon-ok");
				
				
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树一级节点
	 * @return
	 * @throws ParseException
	 */
	public String getPersonTreeJson(List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			//item.put("nodeTye","super_com");
			item.put("text", "金山软件");
			item.put("iconCls", "icon-ok");			
//			item.put("state", "open");
			item.put("children",this.getPerComTreeJson(selected));
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye","super_com");
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树二级节点
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getPerComTreeJson(List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List orgcompanyList = orgCompanyDAO.getAll();
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_com");
				//item.put("nodeTye", "com");
				item.put("text", model.getCompanyname());
				item.put("iconCls", "icon-ok");				
//					item.put("state", "open");
				item.put("children",this.getPerDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeTye", "com");
				attributes.put("companytype",model.getCompanytype());
				attributes.put("companyno", model.getCompanyno());
				attributes.put("companydesc",model.getCompanydesc());
				attributes.put("orderindex", model.getOrderindex());
				attributes.put("lookandfeel",model.getLookandfeel());
				attributes.put("orgadress",model.getOrgadress());
				attributes.put("post",model.getPost());
				attributes.put("email",model.getEmail());
				attributes.put("tel",model.getTel());
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	/**
	 * 增加人员获取人员树三级节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getPerDepartmentJson(String id, List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List departmentList = orgDepartmentDAO.getTopDepartmentList(id);
			for(int i=0;i<departmentList.size();i++){
				OrgDepartment model = (OrgDepartment)departmentList.get(i);
				if(this.getDeptPerson( model.getId())){
					continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				//item.put("nodeTye", "dept");
				item.put("text", model.getDepartmentname());
				
				
				item.put("iconCls", "icon-ok");				
//					item.put("state", "open");
				item.put("children",this.getPerSubDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeTye", "dept");
				attributes.put("companyid",model.getCompanyid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	/**
	 * 增加人员获取人员树剩余节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getPerSubDepartmentJson(Long id, List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List perlist = orgUserDAO.getActiveUserList(id);
		for(int i=0;i<perlist.size();i++){
			OrgUser model = (OrgUser)perlist.get(i);
			Map<String,Object> item = new HashMap<String,Object>();
			//item.put("id", model.getUserid()+"_per");
			item.put("id", model.getUserid()+"");
			if (selected.contains((model.getUserid()+"").toUpperCase())||selected.contains((model.getUserid()+""))) {
				item.put("checked", "true");
			}
			item.put("text", model.getUsername());			
			item.put("iconCls", "icon-ok");
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye", "user");
			attributes.put("departmentname", model.getDepartmentname());
			attributes.put("orgroleid",model.getOrgroleid());
			item.put("attributes", attributes);
			items.add(item);
		}
		List list = orgDepartmentDAO.getSubDepartmentList(id);
			for(int i=0;i<list.size();i++){
				OrgDepartment model = (OrgDepartment)list.get(i);
				if(this.getDeptPerson( model.getId())){
					continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				//item.put("nodeType", "dept");
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getPerSubDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeType", "dept");
				attributes.put("companyid",model.getCompanyid());
				attributes.put("parentdepartmentid",model.getParentdepartmentid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return items;
	}
	
	/**
	 * 判断部门节点下是否有人员
	 * @param id
	 * @return
	 */	
	public boolean getDeptPerson(Long id){
	boolean c=true;
	List perlist = orgUserDAO.getActiveUserList(id);
	if(perlist.size()>0){
		c=false;
	}else{
	List deptlist = orgDepartmentDAO.getSubDepartmentList(id);
	for(int i=0;i<deptlist.size();i++){
		OrgDepartment model = (OrgDepartment)deptlist.get(i);
		if(this.getDeptPerson(model.getId())==false){
			c=false;
		}
		
	}
	}
	return c;
	}
	
	/**
	 * 增加部门获取部门树一级节点
	 * @return
	 * @throws ParseException
	 */
	public String getDeptTreeJson(List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			item.put("text", "金山软件");
			item.put("iconCls", "icon-ok");
//			item.put("state", "open");
			item.put("children",this.getComTreeJson(selected));
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			attributes.put("isExpanded","true"); //是否展开
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
     * 增加部门获取部门树二级节点
     * @return
     * @throws ParseException
     */
	public List<Map<String,Object>> getComTreeJson(List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List orgcompanyList = orgCompanyDAO.getAll();
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", "com_" + model.getId()+"");
				item.put("text", model.getCompanyname());
				if (selected.contains(model.getId()+"")) {
					item.put("checked", "true");
				}
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("isExpanded","true"); //是否展开
				attributes.put("companytype",model.getCompanytype());
				attributes.put("companyno", model.getCompanyno());
				attributes.put("companydesc",model.getCompanydesc());
				attributes.put("orderindex", model.getOrderindex());
				attributes.put("lookandfeel",model.getLookandfeel());
				attributes.put("orgadress",model.getOrgadress());
				attributes.put("post",model.getPost());
				attributes.put("email",model.getEmail());
				attributes.put("tel",model.getTel());
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		
		return items;
	}
	
	/**
	 * 增加部门获取部门树三级节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getDepartmentJson(String id, List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List departmentList = orgDepartmentDAO.getTopDepartmentList(id);
			for(int i=0;i<departmentList.size();i++){
				OrgDepartment model = (OrgDepartment)departmentList.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				if (selected.contains(model.getId()+"")) {
					item.put("checked", "true");
				}
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getSubDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("isExpanded","true"); //是否展开
				attributes.put("companyid",model.getCompanyid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	/**
	 * 增加部门获取部门树剩余节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getSubDepartmentJson(Long id, List<String> selected) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List list = orgDepartmentDAO.getSubDepartmentList(id);
			for(int i=0;i<list.size();i++){
				OrgDepartment model = (OrgDepartment)list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				if (selected.contains(model.getId()+"")) {
					item.put("checked", "true");
				}
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getSubDepartmentJson(model.getId(), selected));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("isExpanded","true"); //是否展开
				attributes.put("companyid",model.getCompanyid());
				attributes.put("parentdepartmentid",model.getParentdepartmentid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
//		JSONArray json = JSONArray.fromObject(items);
//		jsonHtml.append(json);
		return items;
	}
	
	/**
	 * 时间格式转换：Date转String
	 * @param date
	 * @return
	 */
	public String dateFormat(Date date){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str=format.format(date);
		return str;
	}
	
	/**
	 * 时间格式转换：String转Date
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public Date dateFormat(String str) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date=format.parse(str);
		return date;
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
	
	
}
