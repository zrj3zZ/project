package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbGuaPaiAfterCommitEvent extends ProcessStepTriggerEvent {
	
	public ShanXiZqbGuaPaiAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取登录人信息
		String TJRID = UserContextUtil.getInstance().getCurrentUserId();
		UserContext DLUser = UserContextUtil.getInstance().getCurrentUserContext();
		String TJRXM = DLUser._userModel.getUsername(); 
		//获取提交时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dataMap!=null){
			String customername = dataMap.get("GSMC").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			StringBuffer content=new StringBuffer();
			content.append(customername).append("已提交挂牌登记及归档，请审核!");
			String assignee = newTaskId.getAssignee();
			
			//设置BPM专员
			String actstepid = this.getActStepId();
			if(SystemConfig._guapaiLcConf.getJd1().equals(actstepid)){
				dataMap.put("EXTEND1", assignee);
				dataMap.put("TJSJ", sdf.format(date));
				ProcessAPI.getInstance().updateFormData(this.getActDefId(), this.getInstanceId(), dataMap, Long.getLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				
				//添加董秘人员 xlj 2017年3月16日12:23:26 注掉，因目前逻辑有bug，避免更多错误，只用界面录入的董秘手机号进行发送短信，不进行董秘自动添加。
				//addCustomerUser(dataMap);
			}
			//发送挂牌成功通知给董秘人员
			if(SystemConfig._guapaiLcConf.getJd4().equals(actstepid)){
				String msg = "恭喜贵司成功在全国中小企业股份转让系统挂牌!";
				UserContext sendContext = UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager);
				if(sendContext!=null&&dataMap.get("CUSTOMERNO")!=null){
					String mobile= "";
					//mobile= DBUtil.getString("SELECT MOBILE FROM ORGUSER WHERE EXTEND1="+dataMap.get("CUSTOMERNO"), "MOBILE");
					mobile = dataMap.get("MOBILE").toString();
					if(mobile!=null&&!mobile.equals(""))
						MessageAPI.getInstance().sendSMS(sendContext, mobile, msg);
				}
			}
			//同步股改设置的内核专员
			String gGnhzy = DBUtil.getString("SELECT NHZY FROM BD_ZQB_LCGG GG WHERE GG.CUSTOMERNO='"+dataMap.get("CUSTOMERNO")+"'", "NHZY");
			
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null) {
				if (!content.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("") && ut != null) {
						String senduser = ut.get_userModel().getUsername();
						 MessageAPI.getInstance().sendSysMail(senduser, email, "挂牌登记及归档审核", content.toString());
					}
				}
			}
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername());
			dataMap.put("TJRID", TJRID);
			dataMap.put("TJRXM", TJRXM);
			dataMap.put("TJSJ", sdf.format(date));
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}

	private void addCustomerUser(HashMap<String, Object> dataMap) {
		int num = DBUtil.getInt("SELECT COUNT(USERID) NUM FROM ORGUSER WHERE USERID = '"+dataMap.get("ZQDM")+"'", "NUM");
		if(num==0){
			OrgUserService orgUserService=(OrgUserService) SpringBeanUtil.getBean("orgUserService");
			OrgDepartmentDAO orgDepartmentDAO=(OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
			OrgCompanyDAO orgCompanyDAO=(OrgCompanyDAO) SpringBeanUtil.getBean("orgCompanyDAO");
			OrgUser org = new OrgUser();
			String msg = DBUtil.getString("SELECT ID||'#'||DEPARTMENTNAME MSG FROM ORGDEPARTMENT WHERE DEPARTMENTNO='"+dataMap.get("CUSTOMERNO").toString()+"'", "MSG");
			org.setDepartmentid(Long.parseLong(msg.split("#")[0]));
			org.setDepartmentname(msg.split("#")[1]);
			//执行MD5加密
			/*MD5 md5 = new MD5();
			String md5pwd = md5.getEncryptedPwd(pwd);*/
			//执行SHA-256+SALT加密
			String salt = ShaSaltUtil.getStringSalt();
			String shapwd=ShaSaltUtil.getEncryptedPwd(SystemConfig._iworkServerConf.getUserDefaultPassword(),salt,true);
			org.setExtend3(salt);
			org.setPassword(shapwd);
			org.setExtend1(dataMap.get("CUSTOMERNO").toString());
			org.setExtend2(msg.split("#")[1]);
			org.setUserstate(new Long(0L));
			org.setUsername(dataMap.get("KHLXR").toString());
			org.setUserid(dataMap.get("ZQDM").toString().toUpperCase().trim());
			OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(Long.parseLong(msg.split("#")[0]));
			org.setCompanyid(Long.valueOf(Long.parseLong(orgDepartment.getCompanyid())));
			OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
			org.setCompanyname(orgCompany.getCompanyname());
			org.setOrgroleid(3l);
			org.setIsmanager(0L);
			orgUserService.addBoData(org);
		}
	}
}
