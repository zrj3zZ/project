package com.iwork.app.tools.addressbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;


/**
 * 地址簿
 * @author YangDayong
 *
 */
public class SysAddressBookService {
	
	private OrgUserDAO orgUserDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private SysAddressBookService iworkToolsAddressService;
	



	/**
	 * 获得单选树视图
	 * @param departmentid
	 * @param targetField
	 * @return
	 */
	public String getRadioAddressTree(String departmentid,String targetField){
		StringBuffer htmltree = new StringBuffer();
		if(departmentid==null){
			UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
			OrgCompany companyModel = uc._companyModel;
			
		}
		
		return "";
	}
	
	
	/**
	 * 获得多选树视图
	 * @param departmentid
	 * @param targetField
	 * @return
	 */
	public String getMoreAddressTree(String departmentid,String targetField){
		
		
		return "";
	}
	
	/**
	 * 获得树节点
	 * @param pid
	 * @return
	 */
	public String getAddressTreeJson(int pid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		
		
		if(pid==0){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgCompany orgCompany = uc.get_companyModel();
			Map<String,Object> company = new HashMap<String,Object>();
			company.put("id", orgCompany.getId());
			company.put("text", orgCompany.getCompanyname());
			company.put("iconCls", "icon-ok");
			company.put("children",this.getTopDeptJson(orgCompany.getId()));
			items.add(company);
		}else{
			items = this.getChildrenJson(new Long(pid));
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	
	/**
	 * 获得首级部门列表
	 * @param companyId
	 * @return
	 */
	private List<Map<String,Object>> getTopDeptJson(String companyId){
		List<Map<String,Object>> sub_items = new ArrayList<Map<String,Object>>();
		List list = orgDepartmentDAO.getTopDepartmentList(companyId);
		for(int i=0;i<list.size();i++){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgDepartment orgDepartment = (OrgDepartment)list.get(i);
			item.put("id", orgDepartment.getId());
			item.put("text", orgDepartment.getDepartmentname());
			item.put("state", "closed");
			if(this.isChildrenJson(orgDepartment.getId())){
				item.put("iconCls","icon-nav-sys");
			}
//			item.put("children",this.getChildrenJson(orgDepartment.getId()));
			sub_items.add(item);
		}
		return sub_items;
	}
	
	/**
	 * 判断是否存在子部门
	 * @param departmentid
	 * @return
	 */
	private boolean isChildrenJson(Long departmentid){
		boolean flag = false;
		List list = orgDepartmentDAO.getSubDepartmentList(departmentid);
		if(list!=null){
			if(list.size()>0){
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 获得子节点
	 * @param departmentid
	 * @return
	 */
	private List<Map<String,Object>> getChildrenJson(Long departmentid){
		List<Map<String,Object>> sub_items = new ArrayList<Map<String,Object>>();
			UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
			//获取部门信息
			List deptlist = orgDepartmentDAO.getSubDepartmentList(departmentid);
			for(int i=0;i<deptlist.size();i++){
				Map<String,Object> item = new HashMap<String,Object>();
				OrgDepartment orgDepartment = (OrgDepartment)deptlist.get(i);
				item.put("id", orgDepartment.getId());
				item.put("text", orgDepartment.getDepartmentname());
				item.put("iconCls", "icon-ok");
				item.put("state", "closed");
//				item.put("children",this.getChildrenJson(orgDepartment.getId()));
				sub_items.add(item);
			}
			List userList = orgUserDAO.getActiveUserList(departmentid);
			for(int i=0;i<userList.size();i++){
				Map<String,Object> item = new HashMap<String,Object>();
				OrgUser orgUser = (OrgUser)userList.get(i);
				item.put("id", orgUser.getId());
				item.put("text", orgUser.getUsername()+"["+orgUser.getUserid()+"]");
				item.put("iconCls", "icon-ok");
				item.put("state", "open");
//				item.put("children",this.getChildrenJson(orgDepartment.getId()));
				sub_items.add(item);
			}
		return sub_items;
	}
	
	public SysAddressBookService getIworkToolsAddressService() {
		return iworkToolsAddressService;
	}


	public void setIworkToolsAddressService(
			SysAddressBookService iworkToolsAddressService) {
		this.iworkToolsAddressService = iworkToolsAddressService;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}
	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}
	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}
	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}
	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	
	

}
