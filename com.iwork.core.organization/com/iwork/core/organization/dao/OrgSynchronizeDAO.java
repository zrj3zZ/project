package com.iwork.core.organization.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.util.ExtDBSourceUtil;

/**
 * 团队管理主表操作类
 * @author WeiGuangjian
 *
 */
public class OrgSynchronizeDAO extends HibernateDaoSupport{

	
	
	public List<OrgDepartment> getTargetDeptList(SysExdbsrcCenter model){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
		List<OrgDepartment> list = new ArrayList();
		try { 
			conn = ExtDBSourceUtil.getInstance().open(model);
			stmt = conn.createStatement();
			String sql = "select * from	base_dic_Organization order by OrgCode";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int deptid = rs.getInt("OrgCode");
				String name = rs.getString("Name");
				int type = rs.getInt("Type");
				int isDisabled = rs.getInt("IsDisabled");
				//获取当前系统部门id
				int d = this.getCurentDeptId(deptid);
				OrgDepartment dept = new OrgDepartment();
				dept.setId(new Long(d));
				dept.setDepartmentname(name);
				dept.setDepartmentno(deptid+"");
				dept.setDepartmentstate(isDisabled+""); 
				dept.setExtend1(type+"");
				dept.setCompanyid(companyId); 
				list.add(dept);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return list;
	}
	 
	/** 
	 * 根据身份证号码信息，获取对象
	 * @return
	 */
	public OrgUser getOrgUserForExtend4(String idcard){
		String sql = "From OrgUser where extend4=?";
		OrgUser model = null;
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(idcard!=null&&!"".equals(idcard)){
			if(d.HasInjectionData(idcard)){
				return model;
			}
		}
		Object[] value = {idcard};
		List<OrgUser> list = this.getHibernateTemplate().find(sql,value);
		
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}
		return model;
	}
	
	
	public List<OrgRole> getTargetRoleOneList(SysExdbsrcCenter model){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		Statement stmt = null;
		ResultSet rs = null;
		List<OrgRole> list = new ArrayList();
		try { 
			stmt = conn.createStatement();
			String sql = "select * from base_dic_Role order by id";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				String description = rs.getString("Description");
				
				OrgRole role = new OrgRole();
				role.setId(id+""); 
				role.setRolename(name); 
				role.setLookandfeel("def"); 
				role.setRoledesc(description);  
				role.setGroupid("3");
				role.setGroupname("一级角色组");
				role.setOrderindex(id+""); 
				role.setRoletype("一级角色");
				list.add(role);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return list;
	}
	
	public List<OrgRole> getTargetRoleTwoList(SysExdbsrcCenter model){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		Statement stmt = null;
		ResultSet rs = null;
		List<OrgRole> list = new ArrayList();
		try { 
			stmt = conn.createStatement();
			String sql = "select * from base_Role order by id";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("ID");
				int roleDicId = rs.getInt("RoleDicID");
				int orgCode = rs.getInt("OrgCode");
				String name = rs.getString("Name");
				
				OrgRole role = new OrgRole();
				role.setId(id+"");
				role.setRolename(name); 
				role.setLookandfeel("def"); 
				role.setOrderindex(id+"");
				role.setGroupid("4");
				role.setGroupname("二级角色组");
				role.setRoletype("二级角色");
				role.setMemo(roleDicId+"");
				list.add(role);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return list;
	}
	
	/**
	 * 获得指定部门模型
	 * @param model
	 * @param orgCode
	 * @return
	 */
	public OrgDepartment getTargetDeptModel(SysExdbsrcCenter model,int orgCode){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OrgDepartment dept = new OrgDepartment();
		try { 
			stmt = conn.prepareStatement("select * from	base_dic_Organization Where  OrgCode=?");
			stmt.setInt(1, orgCode);
			rs = stmt.executeQuery();
			if(rs.next()){
				int deptid = rs.getInt("OrgCode");
				String name = rs.getString("Name");
				int type = rs.getInt("Type");
				int isDisabled = rs.getInt("IsDisabled");
				
				dept.setId(new Long(deptid));
				dept.setDepartmentname(name);
				dept.setDepartmentno(deptid+"");
				dept.setDepartmentstate(isDisabled+""); 
				dept.setExtend1(type+"");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return dept;
	}
	
	/**
	 * 获得指定部门模型
	 * @param model
	 * @param orgCode
	 * @return
	 */
	public String getTargetWorkGroupName(SysExdbsrcCenter model,int workGroupCode){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String workGroupName = "";
		try { 
			stmt = conn.prepareStatement("select * from	base_dic_WorkGroup Where WorkGroupCode=?");
			stmt.setInt(1, workGroupCode);
			rs = stmt.executeQuery();
			if(rs.next()){
				int deptid = rs.getInt("WorkGroupCode");
				workGroupName = rs.getString("Name");
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return workGroupName;
	}
	
	
	/**
	 * 获得指定用户的一级角色
	 * @param usercode
	 * @return
	 */
	public int getRoleOneUser(SysExdbsrcCenter model,int usercode){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		int roleid = 0;
		String sql="select * from	base_UserAccount a,base_rel_Role_UserAccount b where a.usercode = b.usercode and b.usercode = "+usercode;
		int tmpRoleid = DBUtil.getInt(conn,sql, "ROLEID");
		if(tmpRoleid!=0){
			String sql2 = "select * from Base_Role a ,base_dic_role b where a.roledicid = b.id and a.id="+tmpRoleid;
			roleid = DBUtil.getInt(conn,sql2, "ROLEDicid");
		}
		ExtDBSourceUtil.getInstance().close(conn);
		return roleid;
	}
	
	public List<OrgUserMap> getUserMapTwoRoleForUser(SysExdbsrcCenter model,String userid){
		
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrgUserMap> list = new ArrayList();
		try { 
			stmt = conn.prepareStatement("select  LOGONNAME,CHINESENAME,ROLEID from  base_UserAccount a,base_rel_Role_UserAccount b where a.usercode = b.usercode and isDisabled =0 and  logonname = ? ");
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				String logonName = rs.getString("LOGONNAME");
				String chineseName = rs.getString("ChineseName");
				int orgroleid = rs.getInt("ROLEID"); 
				OrgUserMap map = new OrgUserMap();
				map.setOrgroleid(orgroleid+"");
				map.setUserid(logonName);
				map.setUsermaptype("1"); 
				map.setIsmanager("0");
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return list;
	}
	
	/**
	 * 获得目标用户列表
	 * @param model
	 * @return
	 */
	public List<OrgUser> getTargetUserList(SysExdbsrcCenter model){
		Connection conn = ExtDBSourceUtil.getInstance().open(model);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
		String companyname = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getCompanyname();
		List<OrgUser> list = new ArrayList();
		try { 
			stmt = conn.prepareStatement(" select * from  base_UserAccount a,base_dic_Organization b,base_dic_WorkGroup c where a.orgcode = b.orgcode and a.WorkGroupCode = c.WorkGroupCode order by UserCode");
			rs = stmt.executeQuery();
			while(rs.next()){
				int userCode = rs.getInt("UserCode");
				String logonName = rs.getString("LogonName");
				String chineseName = rs.getString("ChineseName");
				String name = rs.getString("NAME");
				int deptno = rs.getInt("OrgCode");
				//获取部门id
//				int orgCode = this.getCurentDeptId(deptno);
				int workGroupCode = rs.getInt("WorkGroupCode");
				String workGroupName = this.getTargetWorkGroupName(model,workGroupCode);
				int isDisabled = rs.getInt("IsDisabled");
				OrgUser orgUser = new OrgUser();
				orgUser.setCompanyid(new Long(2));
				orgUser.setId(new Long(userCode));
				orgUser.setUserid(logonName);
				orgUser.setUsername(chineseName);
				orgUser.setUserno(userCode+"");
				orgUser.setDepartmentid(new Long(deptno));
				orgUser.setDepartmentname(name);
				orgUser.setUserstate(new Long(isDisabled));
				orgUser.setUsertype(new Long(0));
				orgUser.setExtend1(workGroupCode+"");
				orgUser.setExtend2(workGroupName);
				Date startdate = UtilDate.StringToDate("2014-01-01", "yyyy-MM-dd");
				Date enddate = UtilDate.StringToDate("2024-01-01", "yyyy-MM-dd");
				orgUser.setStartdate(startdate);
				orgUser.setEnddate(enddate);
				orgUser.setCompanyid(Long.parseLong(companyId));
				orgUser.setCompanyname(companyname);
				//获得指定用户的一级角色
				int oneRoleId = this.getRoleOneUser(model,userCode);
				orgUser.setOrgroleid(new Long(oneRoleId));
				list.add(orgUser);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {logger.error(e,e);
			}
			ExtDBSourceUtil.getInstance().close(conn);
		}
		return list;
	}
	
	
	/**
	 * 
	 * @param deptno
	 * @return
	 */
	public int getCurentDeptId(int deptno){
		String sql = "select * from orgDepartment where departmentno="+deptno;
		int id = DBUtil.getInt(sql, "ID");
		return id;
	}
	
	
	
}
