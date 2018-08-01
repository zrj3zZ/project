package com.iwork.app.mobile.organization.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.action.SysPersionUploadPhotoAction;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;

/**
 * 将组织结构同步到移动端Service
 * 
 * @author chenM
 * 
 */
public class SyncOrganizationService {
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private OrgUserService orgUserService;
	private static final String COMPANY_NODE = "company";
	private static final String DEPARTMENT_NODE = "department";
	private static final String USER_NODE = "user";
	/**
	 * 返回组织结构节点信息
	 * 
	 * @param id
	 * @param idType
	 * @return 如果idType为空则返回公司节点信息,如果idType为company则返回一级部门信息，
	 *         如果idType为department则返回部门子节点信息
	 */
	public String getNode(String id, String idType) {
		StringBuffer html = new StringBuffer();
		// 返回公司节点信息
		if (idType.equals("")) {
			List orgcompanyList = orgCompanyDAO.getAll();
			int orgcompanyNum = orgcompanyList.size();
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			if (orgcompanyList != null && orgcompanyNum != 0) {
				for (int i = 0; i < orgcompanyNum; i++) {
					// 加载公司节点
					if (orgcompanyList.get(i) == null)
						continue;
					OrgCompany oc = (OrgCompany) orgcompanyList.get(i);
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", oc.getId());
					item.put("companyname", oc.getCompanyname());
					item.put("companytype", oc.getCompanytype());
					item.put("companyno", oc.getCompanyno());
					item.put("companydesc", oc.getCompanydesc());
					item.put("lookandfeel", oc.getLookandfeel());
					item.put("orgadress", oc.getOrgadress());
					item.put("post", oc.getPost());
					item.put("email", oc.getEmail());
					item.put("tel", oc.getTel());
					item.put("zoneno", oc.getZoneno());
					item.put("zonename", oc.getZonename());
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
			// 返回一级部门的信息
		} else if (idType.equals(COMPANY_NODE)) {
			String companyId = id;
			List subDeptList = orgDepartmentDAO.getTopDepartmentList(companyId);
			List<Map<String, Object>> deptRows = new ArrayList<Map<String, Object>>();
			int orgDeptNum = subDeptList.size();
			if (subDeptList != null && orgDeptNum > 0) {
				for (int i = 0; i < orgDeptNum; i++) {
					if (subDeptList.get(i) == null)
						continue;
					OrgDepartment od = (OrgDepartment) subDeptList.get(i);
					Map<String, Object> subItem = new HashMap<String, Object>();
					Long deptId = od.getId();
					if (deptId != null) {
						subItem.put("id", od.getId());
						subItem.put("companyid", od.getCompanyid());
						subItem.put("departmentname", od.getDepartmentname());
						subItem.put("parentdepartmentid",
								od.getParentdepartmentid());
						subItem.put("departmentdesc", od.getDepartmentdesc());
						subItem.put("departmentno", od.getDepartmentno());
						subItem.put("layer", od.getLayer());
						subItem.put("orderindex", od.getOrderindex());
						subItem.put("zoneno", od.getZoneno());
						subItem.put("zonename", od.getZonename());
						subItem.put("departmentstate", od.getDepartmentstate());
						int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(deptId);
						subItem.put("subDepartmentSize", subDepartmentSize);
						int userSize = orgUserService.getOrgUserList(deptId).size();
						subItem.put("userSize", userSize);
						deptRows.add(subItem);
					}
				}
			}
			JSONArray json = JSONArray.fromObject(deptRows);
			html.append(json);
			// 返回部门子节点信息
		} else if (idType.equals(DEPARTMENT_NODE)) {
			Long departmentId = Long.parseLong(id);
			Map<String,Object> deptMap = new HashMap<String,Object>();
			List subDeptList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(id));
			List<Map<String, Object>> deptRows = new ArrayList<Map<String, Object>>();
			int orgDeptNum = subDeptList.size();
			if (subDeptList != null && orgDeptNum > 0) {
				for (int i = 0; i < orgDeptNum; i++) {
					if (subDeptList.get(i) == null)
						continue;
					OrgDepartment od = (OrgDepartment) subDeptList.get(i);
					Map<String, Object> subItem = new HashMap<String, Object>();
					Long deptId = od.getId();
					if (deptId != null) {
						subItem.put("id", od.getId());
						subItem.put("companyid", od.getCompanyid());
						subItem.put("departmentname", od.getDepartmentname());
						subItem.put("parentdepartmentid",
								od.getParentdepartmentid());
						subItem.put("departmentdesc", od.getDepartmentdesc());
						subItem.put("departmentno", od.getDepartmentno());
						subItem.put("layer", od.getLayer());
						subItem.put("orderindex", od.getOrderindex());
						subItem.put("zoneno", od.getZoneno());
						subItem.put("zonename", od.getZonename());
						subItem.put("departmentstate", od.getDepartmentstate());
						int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(deptId);
						subItem.put("subDepartmentSize", subDepartmentSize);
						int userSize = orgUserService.getOrgUserList(deptId).size();
						subItem.put("userSize", userSize);
						deptRows.add(subItem);
					}
				}
			}
			deptMap.put("dept", deptRows);
			// 指定部门下的普通用户列表
			List userList = orgUserService.getDeptAllUserList(departmentId);
			// 指定部门下的兼职用户列表
			List partTimeUserList = orgUserService.getOrgUserMapList(departmentId);
			if(partTimeUserList.size()>0){
				userList.addAll(partTimeUserList);
			}
			List<Map<String, Object>> userRows = new ArrayList<Map<String, Object>>();
			int orgUserNum = userList.size();
			if(userList!=null&&orgUserNum>0){
				for (int i = 0; i < orgUserNum; i++) {
					if (userList.get(i) == null)continue;
					OrgUser ou = (OrgUser)userList.get(i);
					Map<String, Object> subItem = new HashMap<String, Object>();
					subItem.put("userid", ou.getUserid());
					subItem.put("username", ou.getUsername());
					subItem.put("departmentid", ou.getDepartmentid());
					userRows.add(subItem);
				}
			}
			deptMap.put("user", userRows);
			JSONArray json = JSONArray.fromObject(deptMap);
			html.append(json);
		}else if(idType.equals(USER_NODE)){
			OrgUser user = orgUserService.getUserModel(id);
			Map<String, Object> userItem = new HashMap<String, Object>();
			userItem.put("id", user.getId());
			userItem.put("userid", user.getUserid());
			userItem.put("username", user.getUsername());
			userItem.put("departmentid", user.getDepartmentid());
			userItem.put("departmentname", user.getDepartmentname());
			userItem.put("orgroleid", user.getOrgroleid());
			userItem.put("logincounter", user.getLogincounter());
			userItem.put("bossid", user.getBossid());
			userItem.put("costcenterid", user.getCostcenterid());
			userItem.put("costcentername", user.getCostcentername());
			userItem.put("ismanager", user.getIsmanager());
			userItem.put("postsid", user.getPostsid());
			userItem.put("postsname", user.getPostsname());
			userItem.put("isroving", user.getIsroving());
			userItem.put("issinglelogin", user.getIssinglelogin());
			userItem.put("officetel", user.getOfficetel());
			userItem.put("officefax", user.getOfficefax());
			userItem.put("hometel", user.getHometel());
			userItem.put("mobile", user.getMobile());
			userItem.put("email", user.getEmail());
			userItem.put("jjlinkman", user.getJjlinkman());
			userItem.put("jjlinktel", user.getJjlinktel());
			userItem.put("userno", user.getUserno());
			userItem.put("orderindex", user.getOrderindex());
			userItem.put("workStatus", user.getWorkStatus());
			userItem.put("portalmodel", user.getPortalmodel());
			userItem.put("usertype", user.getUsertype());
			userItem.put("startdate", user.getStartdate());
			userItem.put("enddate", user.getEnddate());
			userItem.put("userstate", user.getUserstate());
			userItem.put("companyid", user.getCompanyid());
			userItem.put("companyname", user.getCompanyname());
			userItem.put("menulayouttype", user.getMenulayouttype());
			userItem.put("postsresponsibility", user.getPostsresponsibility());
			userItem.put("qqmsn", user.getQqmsn());
			userItem.put("selfdesc", user.getSelfdesc());
			userItem.put("priority", user.getPriority());
			JSONArray json = JSONArray.fromObject(userItem);
			html.append(json);
		}
		return html.toString();
	}
	
	/**
	 * 获得用户头像的路径
	 * @param userId
	 * @return
	 */
	public String getUserImg(String userId){
		StringBuffer html = new StringBuffer();
		Map<String, Object> userItem = new HashMap<String, Object>();
		if(null==userId){
			userItem.put("userImgPath", "noexits");
		}else{
			userId = userId.toUpperCase();
			String mobileImgPath = ServletActionContext.getServletContext()
			.getRealPath(
					SystemConfig._fileServerConf.getUserPhotoPath())
					+ SysPersionUploadPhotoAction.SMALL_PATH + userId + ".jpg";
			File file = new File(mobileImgPath);
			if(file.exists()){
				userItem.put("userImgPath", SystemConfig._fileServerConf.getUserPhotoPath()+SysPersionUploadPhotoAction.SMALL_PATH + userId + ".jpg");
			}else{
				userItem.put("userImgPath", "noexits");
			}
		}
		JSONArray json = JSONArray.fromObject(userItem);
		html.append(json);
		return html.toString();
	}

	//============================POJO========================================
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

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

}
