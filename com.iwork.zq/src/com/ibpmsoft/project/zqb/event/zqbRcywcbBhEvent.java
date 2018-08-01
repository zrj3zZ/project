package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbRcywcbBhEvent extends ProcessStepTriggerEvent {

	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件    
	public zqbRcywcbBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		HashMap hashmap = new HashMap();
		String sq=dataMap.get("NOTICESQ").toString();
		if("".equals(sq)||sq==null){
			return true;
		}
		hashmap.put("NOTICESQ", sq);
		List<HashMap> list = DemAPI.getInstance().getList(getConfigUUID("rcywcbuuid"),hashmap, null);
		
		if (dataMap != null) {
			//回写审批状态 字段
			HashMap map=list.get(0);//原来数据
			map.put("SPZT", "驳回");
			map.put("YXID", this.getExcutionId());
			map.put("TASKID", this.getTaskId());
			map.put("LCBH", this.getActDefId());
			map.put("LCBS", instanceId);
			Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
			Long dataid=Long.parseLong(map.get("ID").toString());
			flag=DemAPI.getInstance().updateFormData(getConfigUUID("rcywcbuuid"), instanceid, map, dataid, true);
		   
			/*LogUtil.getInstance().addLog(dataId, "公告呈报管理", "驳回公告："+value);
			String mailContent = "";
			String userid = dataMap.get("CREATEUSER").toString();
			mailContent = dataMap.get("KHMC").toString() + "的事项："+dataMap.get("SXMC").toString()+"，被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				flag = true;
				if (!mailContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						//MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
					}
					String email = target.get_userModel().getEmail();
					if(email != null && !email.equals("")){
						//MessageAPI.getInstance().sendSysMail(senduser, email, "事项驳回", mailContent);
					}
				}
			}*/
		}
		return flag;
	}
}

