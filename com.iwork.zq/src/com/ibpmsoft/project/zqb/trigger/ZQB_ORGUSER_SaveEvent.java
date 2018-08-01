package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;


import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;

import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;

/**
 * 项目基本信息添加
 * @author David
 *
 */
public class ZQB_ORGUSER_SaveEvent  extends DemTriggerEvent {
	private OrgUserService orgUserService;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgCompanyDAO orgCompanyDAO;
	public ZQB_ORGUSER_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		//添加会议计划就给对应的持续督导人员发短信通知，告知内容格式如下：【公司简称】于2014-12-24日计划召开第一届第十一次董事会，请您关注！
		//（注：已召开过的会议，后填到系统中的不进行短信提醒，但系统消息提醒谁填了什么会议就可以）
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String useridTemp = hash.get("USERID").toString();
		orgUserService=(OrgUserService) SpringBeanUtil.getBean("orgUserService");
		orgDepartmentDAO=(OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
		orgCompanyDAO=(OrgCompanyDAO) SpringBeanUtil.getBean("orgCompanyDAO");
		OrgUser model = orgUserService.getUserModel(useridTemp.toUpperCase().trim());
	    if (model == null){
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
	    	String dno = hash.get("DEPARTMENTID").toString();
	    	org.setDepartmentid(Long.parseLong(dno));
	    	org.setDepartmentname(hash.get("DEPARTMENTNAME").toString());
	    	//执行MD5加密
			/*MD5 md5 = new MD5();
			String md5pwd = md5.getEncryptedPwd(pwd);*/
			//执行SHA-256+SALT加密
			String salt = ShaSaltUtil.getStringSalt();
			String shapwd=ShaSaltUtil.getEncryptedPwd(SystemConfig._iworkServerConf.getUserDefaultPassword(),salt,true);
			org.setExtend3(salt);
	    	org.setPassword(shapwd);
	    	org.setUserstate(new Long(0L));
	    	org.setUsername(hash.get("USERNAME").toString());
	    	org.setUserid(useridTemp.toUpperCase().trim());
	    	OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(Long.parseLong(dno));
		    org.setCompanyid(Long.valueOf(Long.parseLong(orgDepartment.getCompanyid())));
		    OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
		    org.setCompanyname(orgCompany.getCompanyname());
		    org.setOrgroleid(Long.parseLong(hash.get("ROLEID").toString()));
		    org.setMobile(hash.get("MOBILE")==null?"":hash.get("MOBILE").toString());
		    org.setEmail(hash.get("EMAIL")==null?"":hash.get("EMAIL").toString());
		    org.setIsmanager(0L);
	    	orgUserService.addBoData(org);
	    }
	    return true;
	}
}

