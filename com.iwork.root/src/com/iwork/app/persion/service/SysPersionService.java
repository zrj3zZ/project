package com.iwork.app.persion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.persion.dao.SysPersonDAO;
import com.iwork.app.persion.model.MutilRoleModel;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 构建个人信息
 * @author YangDayong
 *
 */
public class SysPersionService {
	private SysPersionService sysPersionService;
	private OrgUserDAO orgUserDAO;
	private SysPersonDAO sysPersonDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgRoleDAO orgRoleDAO;
	
	/**
	 * 获取角色列表
	 * @return
	 */
	public List<MutilRoleModel> getRoleList(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		List<OrgUserMap> maplist = uc.get_userMapList();
		List<MutilRoleModel> list = new ArrayList();
		MutilRoleModel model = null;
		if(uc!=null){
			 model = this.buildModel(uc.get_companyModel(), uc.get_deptModel(), uc.get_userModel(), uc.get_orgRole(), true);
			list.add(model);
		}
		if(list!=null&&list.size()>0){
			for(OrgUserMap oum:maplist){
				OrgCompany orgCompany = null;
				Long departmentId = Long.parseLong(oum.getDepartmentid());
				OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(departmentId);
				if(orgDepartment!=null){
					orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
				}
				String roleid = oum.getOrgroleid();
				OrgRole orgRole = orgRoleDAO.getBoData(roleid);
				if(orgCompany!=null&&orgDepartment!=null&&orgRole!=null){
					if(!model.getCompanyid().equals(orgCompany.getId())||!model.getDepartmentid().equals(orgDepartment.getId())||!model.getRoleId().equals(Long.parseLong(orgRole.getId()))){
						MutilRoleModel mrm =  this.buildModel(orgCompany, orgDepartment, uc.get_userModel(),orgRole, false);
						 list.add(mrm); 
					}
					
				}
			}
		}
		return list;
		
	}
	
	/**
	 * @param itemid
	 * @return
	 */
	public boolean  doMutiRole(String itemid){
		boolean flag = false;
		String[] items = itemid.split(","); 
		ActionContext actionContext = ActionContext.getContext();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc!=null){
			//构建兼任角色列表
			List<OrgUserMap> newMaplist = new ArrayList();
			List<OrgUserMap> list = uc.get_userMapList();
			OrgUserMap map = new OrgUserMap();
			map.setDepartmentid(uc.get_deptModel().getId().toString());
			map.setDepartmentname(uc.get_deptModel().getDepartmentname());
			map.setIsmanager(uc.get_userModel().getIsmanager().toString());
			map.setOrgroleid(uc.get_orgRole().getId());
			map.setOrgrolename(uc.get_orgRole().getRolename());
			map.setUserid(uc.get_userModel().getUserid());
			map.setUsermaptype("0");
			newMaplist.add(map);
			String companyid = items[0];
			String departmentid = items[1];
			String roleid = items[2];
			Long isManager = null; 
			for(OrgUserMap usermap:list){
				 map = new OrgUserMap();
				if(usermap.getDepartmentid().equals(departmentid)&&usermap.getOrgroleid().equals(roleid)){
					isManager = Long.parseLong(usermap.getIsmanager());
					continue;
				}
				map.setDepartmentid(usermap.getDepartmentid());
				map.setDepartmentname(usermap.getDepartmentname());
				map.setIsmanager(usermap.getIsmanager());
				map.setOrgroleid(usermap.getOrgroleid());
				map.setOrgrolename(usermap.getOrgrolename());
				map.setUserid(usermap.getUserid());
				map.setUsermaptype(usermap.getUsermaptype());
				newMaplist.add(map);
			}
			OrgCompany company = orgCompanyDAO.getBoData(companyid);
			OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(Long.parseLong(departmentid));
			OrgRole orgRole = orgRoleDAO.getBoData(roleid);
			if(company!=null&&orgDepartment!=null&&orgRole!=null){
				uc.set_companyModel(company);
				uc.set_deptModel(orgDepartment);
				uc.set_orgRole(orgRole);
				uc.set_userMapList(newMaplist);
				OrgUser orgUser = uc.get_userModel();
				orgUser.setCompanyid(Long.parseLong(companyid));
				orgUser.setCompanyname(orgUser.getCompanyname());
				orgUser.setDepartmentid(orgDepartment.getId());
				orgUser.setDepartmentname(orgDepartment.getDepartmentname());
				orgUser.setOrgroleid(Long.parseLong(orgRole.getId()));
				orgUser.setIsmanager(isManager);
				uc.set_userModel(orgUser);

				//构建userMap
				HttpServletRequest request = (HttpServletRequest) actionContext
				.get(ServletActionContext.HTTP_REQUEST);
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute(LoginConst.USER_LOGIN_TYPE, LoginConst.USER_LOGIN_TYPE_WEB);
				httpSession.setAttribute(AppContextConstant.USER_CONTEXT, uc);
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 构建角色模型
	 * @param company
	 * @param dept
	 * @param user
	 * @return
	 */
	public MutilRoleModel buildModel(OrgCompany company,OrgDepartment dept,OrgUser user,OrgRole role,boolean isMain){
		MutilRoleModel model = new MutilRoleModel();
		model.setCompanyid(Long.parseLong(company.getId()));
		model.setCompanyname(company.getCompanyname());
		model.setCompanyno(company.getCompanyno());
		model.setDepartmentid(dept.getId());
		model.setDepartmentname(dept.getDepartmentname());
		model.setMain(isMain);
		model.setUserid(user.getUserid());
		model.setUsername(user.getUsername());
		model.setRoleId(Long.parseLong(role.getId()));
		model.setRoleName(role.getRolename());
		return model;
	}
	
	/**
	 * 删除用户配置信息
	 * @param model
	 */
	public void delUserInfo(SysPersonConfig model){
		sysPersonDAO.delBoData(model);
	}
	
	/**
	 * 更新用户信息
	 * @param orgUser
	 */
	public void updateUserInfo(OrgUser orgUser){
		orgUserDAO.updateBoData(orgUser);
	}
	
	public Map getPassword$Extend3(String userid) {
		return sysPersonDAO.getPassword$Extend3(userid);
	}
	
	/**
	 * 获得当前用户对象
	 * @param uc
	 * @return
	 */
	public OrgUser getMyPersionInfo(UserContext uc){
		return orgUserDAO.getUserModel(uc._userModel.getUserid());
	}
	
	/**
	 * 获得用户的配置信息
	 * @param userid
	 * @param condition	//传入的数组为当前需要查询的用户配置的名称
	 * @return
	 */
	public List<SysPersonConfig> getUserAllConfig(String userid,String[] condition){
		List<SysPersonConfig> list = sysPersonDAO.getUserConfigList(userid,condition);
		return list;
	}
	/**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	public SysPersonConfig getModel(String id){
		long temp = Long.parseLong(id);
		SysPersonConfig sysPersonConfig =  sysPersonDAO.getBoData(temp);
		return sysPersonConfig;
	}
	/**
	 * 执行单条数据更新、保存
	 * @param id
	 * @param userid
	 * @param type
	 * @param value
	 */
	public void saveConfig(String id,String userid,String type,String value){
		String[] typelist = {type};
		SysPersonConfig model = sysPersonDAO.getUserConfig(userid, type);
		if(model==null){
			 model = new SysPersonConfig(userid,type,value);
			sysPersonDAO.addBoData(model);
		}else{
			if(model.getValue()==null){
				model.setValue("");
			}
			if(!model.getValue().equals(value)){
				model.setValue(value);
				sysPersonDAO.updateBoData(model);
			}
			
		}
	}
	
	/**
	 * 执行单条数据更新、保存
	 * @param id
	 * @param userid
	 * @param type
	 * @param value
	 */
	public SysPersonConfig getConfig(String userid,String type){
		SysPersonConfig model = sysPersonDAO.getUserConfig(userid, type);
		return model;
	} 
//===================POJO===============================
	public SysPersionService getSysPersionService() {
		return sysPersionService;
	}
	public void setSysPersionService(SysPersionService sysPersionService) {
		this.sysPersionService = sysPersionService;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}
	public SysPersonDAO getSysPersonDAO() {
		return sysPersonDAO;
	}
	public void setSysPersonDAO(SysPersonDAO sysPersonDAO) {
		this.sysPersonDAO = sysPersonDAO;
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
	public OrgRoleDAO getOrgRoleDAO() {
		return orgRoleDAO;
	}
	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}
}
