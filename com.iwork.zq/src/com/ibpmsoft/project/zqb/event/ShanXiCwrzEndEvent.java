package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class ShanXiCwrzEndEvent extends ProcessTriggerEvent {
	public ShanXiCwrzEndEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			StringBuffer content=new StringBuffer();
			content.append(dataMap.get("CUSTOMERNAME").toString()).append("财务入账信息审核通过！");
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			Long taskId2 = this.getTaskId();
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", taskId2);
			dataMap.put("SPZT", "审批通过");
			dataMap.put("CJSJ",dataMap.get("CJSJ")==null?sdf.format(date):sdf.format(dataMap.get("CJSJ")));
			dataMap.put("DZRQ",dataMap.get("DZRQ")==null?sdf.format(date):sdf.format(dataMap.get("DZRQ")));
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			UserContext target = UserContextUtil.getInstance().getUserContext(dataMap.get("LRR").toString());
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
					MessageAPI.getInstance().sendSMS(uc, mobile,content.toString());
				}
				String email = target.get_userModel().getEmail();
				if (email != null && !email.equals("")) {
					MessageAPI.getInstance().sendSysMail(senduser,email, "财务入账信息", content.toString());
				}
			}
		}
		return flag;
	}
}
