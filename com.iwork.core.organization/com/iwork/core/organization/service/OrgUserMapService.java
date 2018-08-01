package com.iwork.core.organization.service;

import java.util.List;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgUserMap;

public class OrgUserMapService {
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserMapDAO orgUserMapDAO;
	
	public void addBoData(OrgUserMap obj) {
		if(obj!=null&&obj.getId()==null){
			obj.setId(this.getMaxID());
		}
		orgUserMapDAO.addBoData(obj);
	}

	public void deleteBoData(String id) {
		OrgUserMap model=orgUserMapDAO.getUserModel(id);
		if(model!=null){
			orgUserMapDAO.deleteBoData(model);
		}
	}

	/**
	 * 获得全部用户列表
	 * @return 
	 */
	public List getAll() {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getAll();
	}

	/**
	 * 获得用户模型
	 * @param id
	 * @return
	 */
	public OrgUserMap getUserModel(String id) {
		if(null==id||"".equals(id)){
			return null;
		}else{
			return orgUserMapDAO.getUserModel(id);
		}
	} 

	/**
	 * 获得用户列表
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getOrgUserMapList(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getOrgUserMapList(pageSize, startRow);
	}
	/**
	 * 获得指定部门下用户列表
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getOrgUserMapList(String userid, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getOrgUserMapList(userid, pageSize, startRow);
	}
	
	/**
	 * 获得指定部门下用户及角色信息列表
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getOrgUserMapAndRoleList(String userid, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getOrgUserMapAndOrgUserRoleList(userid, pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getMaxID();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getRows();
	}

	public int getRows(String userid) {
		// TODO Auto-generated method stub
		return orgUserMapDAO.getRows(userid);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgUserMapDAO.queryBoDatas(fieldname, value);
	}  

	public void updateBoData(OrgUserMap obj) {
		orgUserMapDAO.updateBoData(obj);

	}
	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgUserMapDAO getOrgUserMapDAO() {
		return orgUserMapDAO;
	}
	
}
