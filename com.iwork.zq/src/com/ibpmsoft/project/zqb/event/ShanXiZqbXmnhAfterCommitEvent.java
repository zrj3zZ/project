package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.task.Task;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目内核保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbXmnhAfterCommitEvent extends ProcessStepTriggerEvent {
	
	public ShanXiZqbXmnhAfterCommitEvent(UserContext uc, HashMap hash) {
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
			String processActDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			String customername = dataMap.get("CUSTOMERNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			StringBuffer content=new StringBuffer();
			content.append(customername).append("已提交申报审核。");
			String assignee = newTaskId.getAssignee();
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = ut._userModel.getUserid();
			if(SystemConfig._xmnhLcConf.getJd4().equals(actStepId)){
				deleteData(processActDefId,SystemConfig._xmnhLcConf.getJd4(),userid,instanceId);
			}
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
						 MessageAPI.getInstance().sendSysMail(senduser, email, "申报审核", content.toString());
					}
				}
			}
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername());
			dataMap.put("TJRID", TJRID);
			dataMap.put("TJRXM", TJRXM);
			dataMap.put("TJSJ", sdf.format(date));
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}
	
	private void deleteData(String processActDefId, String actStepId, String userid,Long instanceId) {
		StringBuffer sql = new StringBuffer("DELETE FROM PROCESS_RU_OPINION WHERE");
		sql.append(" ACT_DEF_ID = '").append(processActDefId).append("'");
		sql.append(" AND ACTION = '规则跳转[*]'");
		sql.append(" AND ACT_STEP_ID = '").append(actStepId).append("'");
		sql.append(" AND CREATEUSER = '").append(userid).append("'");
		sql.append(" AND INSTANCEID = ").append(instanceId);
		DBUtil.executeUpdate(sql.toString());
	}
}
