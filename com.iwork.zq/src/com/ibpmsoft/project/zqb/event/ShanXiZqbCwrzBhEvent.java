package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.task.Task;
import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class ShanXiZqbCwrzBhEvent extends ProcessStepTriggerEvent {
	
	public ShanXiZqbCwrzBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			
			//重置提交时间格式
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String ygsmc = dataMap.get("CUSTOMERNAME").toString();
			String processActDefId = this.getActDefId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			Long taskId2 = this.getTaskId();
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", taskId2);
			dataMap.put("CJSJ",dataMap.get("CJSJ")==null?sdf.format(date):sdf.format(dataMap.get("CJSJ")));
			dataMap.put("DZRQ",dataMap.get("DZRQ")==null?sdf.format(date):sdf.format(dataMap.get("DZRQ")));
			dataMap.put("SPZT", newTaskId.getName());
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			LogUtil.getInstance().addLog(dataId, "财务入账流程", ygsmc+"财务入账流程被驳回！");
			String smsContent = ygsmc+"财务入账流程被驳回！";
			String userid = dataMap.get("LRR").toString();
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "财务入账信息审核", smsContent);
					}
				}
			}
		}
		return flag;
	}
}
