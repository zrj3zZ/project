package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.task.Task;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class ShanXiZqbGzfkBhEvent extends ProcessStepTriggerEvent {
	
	public ShanXiZqbGzfkBhEvent(UserContext uc, HashMap hash) {
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
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			String customername = dataMap.get("CUSTOMERNAME").toString();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			Long taskId2 = this.getTaskId();
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", taskId2);
			dataMap.put("SPZT", newTaskId.getName());
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			LogUtil.getInstance().addLog(dataId, "股转反馈审核流程", customername+"股转反馈审核流程被驳回！");
			/*String smsContent = customername+"股转反馈审核流程被驳回！";
			String userid = zqbLcBhHQBHREvent.getUsername();
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
						MessageAPI.getInstance().sendSysMail(senduser,email, "股转反馈审核", smsContent);
					}
				}
			}*/
		}
		return flag;
	}
}
