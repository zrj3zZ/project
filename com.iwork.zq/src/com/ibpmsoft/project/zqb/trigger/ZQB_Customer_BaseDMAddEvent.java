package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;

public class ZQB_Customer_BaseDMAddEvent  extends DemTriggerEvent{
	private OrgUserService orgUserService;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgCompanyDAO orgCompanyDAO;
	public ZQB_Customer_BaseDMAddEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		orgUserService=(OrgUserService) SpringBeanUtil.getBean("orgUserService");
		orgDepartmentDAO=(OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
		orgCompanyDAO=(OrgCompanyDAO) SpringBeanUtil.getBean("orgCompanyDAO");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc.get_userModel();
		HashMap formData = this.getFormData();
		HashMap map = null;
		if(formData!=null){
			 map = ParameterMapUtil.getParameterMap(formData);
		}
		
		String sql = "SELECT MAX(id)+1 ID FROM OrgUser ";
    	String noStr = null;
    	String ll = DBUtil.getString(sql, "ID");
    	if (ll == null)
    		noStr = "1";
    	else {
    		noStr = ll.toString();
    	}
    	if (noStr.length() == 1)
    		noStr = "000" + noStr;
    	else if (noStr.length() == 2)
    		noStr = "00" + noStr;
    	else if (noStr.length() == 3)
    		noStr = "0" + noStr;
    	else {
    		noStr = noStr;
    	}
    	OrgUser org = new OrgUser();
    	org.setUserno(noStr);
    	String dno = userModel.getDepartmentid().toString();
    	org.setDepartmentid(Long.parseLong(dno));
    	org.setDepartmentname(userModel.getDepartmentname());
    	//执行MD5加密
		/*MD5 md5 = new MD5();
		String md5pwd = md5.getEncryptedPwd(pwd);*/
		//执行SHA-256+SALT加密
		String salt = ShaSaltUtil.getStringSalt();
		String shapwd=ShaSaltUtil.getEncryptedPwd(SystemConfig._iworkServerConf.getUserDefaultPassword(),salt,true);
		org.setExtend3(salt);
    	org.setPassword(shapwd);
    	org.setExtend1(userModel.getExtend1());
    	org.setExtend2(userModel.getExtend2());
    	org.setUserstate(new Long(0L));
    	org.setUsername(map.get("USERNAME").toString());
    	org.setUserid(map.get("USERID").toString().toUpperCase().trim());
    	OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(Long.parseLong(dno));
	    org.setCompanyid(Long.valueOf(Long.parseLong(orgDepartment.getCompanyid())));
	    OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
	    org.setCompanyname(orgCompany.getCompanyname());
	    org.setOrgroleid(userModel.getOrgroleid());
	    org.setMobile(map.get("MOBILE")==null?"":map.get("MOBILE").toString());
	    org.setEmail(map.get("EMAIL")==null?"":map.get("EMAIL").toString());
	    org.setIsmanager(0L);
    	orgUserService.addBoData(org);
		return true;
	}

}
