package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.task.Task;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbNhfkAfterCommitEvent extends ProcessStepTriggerEvent {
	
	public ShanXiZqbNhfkAfterCommitEvent(UserContext uc, HashMap hash) {
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
			String customername = dataMap.get("CUSTOMERNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			StringBuffer content=new StringBuffer();
			content.append(customername).append("已提交内核反馈及回复，请审核!");
			String assignee = newTaskId.getAssignee();
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
						 MessageAPI.getInstance().sendSysMail(senduser, email, "内核反馈及回复审核", content.toString());
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
}
