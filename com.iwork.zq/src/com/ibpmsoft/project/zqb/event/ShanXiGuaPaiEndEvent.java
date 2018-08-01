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

public class ShanXiGuaPaiEndEvent extends ProcessTriggerEvent {
	public ShanXiGuaPaiEndEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			StringBuffer content=new StringBuffer();
			content.append(dataMap.get("GSMC").toString()).append("挂牌登记及归档审核通过！");
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("SPZT", "审批通过");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			 dataMap.put("TJSJ", startDate);
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			UserContext target = UserContextUtil.getInstance().getUserContext(dataMap.get("TBRID").toString());
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
					MessageAPI.getInstance().sendSysMail(senduser,email, "挂牌登记及归档", content.toString());
				}
			}
		}
		return flag;
	}
}
