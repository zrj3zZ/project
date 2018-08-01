package com.iwork.core.organization.context;

import java.io.Serializable;
import java.util.List;


import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;


/**
 * 用户会话上下文包含了运行时刻信息和与用户
 * 相关的静态信息(ORG，DEPT，USER，ROLE，TEAM)
 * 
 * @author David.Yang
 * @preserve 声明此方法不被JOC混淆
 */
public class UserContext implements Serializable{	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6949929648726973348L;
	
	
	
	public OrgUser _userModel;
    public List<OrgUserMap> _userMapList;
    public List<OrgDepartment> _parentDeptList;
    public OrgDepartment _deptModel;
    public OrgCompany _companyModel;
    
    public OrgRole _orgRole ; 
    public String _userIp;
    public String _WorkStatus;
    public String _logintime;
//    /**
//     * 获得指定用户上下文
//     * @param userid
//     */
//    public UserContext(String userid){
//    	LoginContext loginContext = new LoginContext();
//    	loginContext.setUid(userid);
//    	this.initContextUserContext(loginContext.getUid());
//    }
    
	public OrgUser get_userModel() {
		return _userModel;
	}
	public void set_userModel(OrgUser model) {
		_userModel = model;
	}
	public OrgDepartment get_deptModel() {
		return _deptModel;
	}
	public void set_deptModel(OrgDepartment model) {
		_deptModel = model;
	}
	public OrgCompany get_companyModel() {
		return _companyModel;
	}
	public void set_companyModel(OrgCompany model) {
		_companyModel = model;
	}
	public OrgRole get_orgRole() {
		return _orgRole;
	}
	public void set_orgRole(OrgRole role) {
		_orgRole = role;
	}
	public String get_userIp() {
		return _userIp;
	}
	public void set_userIp(String ip) {
		_userIp = ip;
	}
	public String get_WorkStatus() {
		return _WorkStatus;
	}
	public void set_WorkStatus(String workStatus) {
		_WorkStatus = workStatus;
	}
	public String get_logintime() {
		return _logintime;
	}
	public void set_logintime(String _logintime) {
		this._logintime = _logintime;
	}

	public List<OrgUserMap> get_userMapList() {
		return _userMapList;
	}

	public void set_userMapList(List<OrgUserMap> mapList) {
		_userMapList = mapList;
	}
	public List<OrgDepartment> get_parentDeptList() {
		return _parentDeptList;
	}
	public void set_parentDeptList(List<OrgDepartment> _parentDeptList) {
		this._parentDeptList = _parentDeptList;
	} 
	
}
