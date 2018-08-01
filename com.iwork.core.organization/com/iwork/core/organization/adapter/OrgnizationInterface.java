package com.iwork.core.organization.adapter;

import java.util.List;

import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;

public interface OrgnizationInterface {
	
	/**
	 * 获取用户对象模型
	 * @param userId
	 * @return
	 */
	public OrgUser getUserModel(String userId);
	/**
	 * 删除指定用户
	 * @param userId
	 * @return
	 */
	public void deleteUserModel(String userId);
	/**
	 * 设置用户注销
	 * @param userId
	 */
	public void setUserDisabled(String userId);
	/**
	 * 设置用户激活
	 * @param userId
	 */
	public void setUserActive(String userId);
	/**
	 * 获得全部用户
	 * @param departmentId
	 * @return
	 */
	public List<OrgUser> getUserAllList(String departmentId);
	/**
	 * 获得活跃用户
	 * @param departmentId
	 * @return
	 */
	public List<OrgUser> getUserActiveList(String departmentId);
	
	
	/**
	 * 获取用户兼任职位模型
	 * @param userId
	 * @return
	 */
	public List<OrgUserMap> getOrgUserMapList(String userId);

	/**
	 * 获得角色对象模型
	 * @param roleId
	 * @return
	 */
	public OrgRole getUserRole(String roleId);
	

	/**
	 * 获得部门对象模型
	 * @param deptId
	 * @return
	 */
	public OrgDepartment getUserDepartment(String deptId);
	
	
	/**
	 * 获得组织模型
	 * @param companyId
	 * @return
	 */
	public OrgCompany getCompayModle(String companyId);
	
	
}
