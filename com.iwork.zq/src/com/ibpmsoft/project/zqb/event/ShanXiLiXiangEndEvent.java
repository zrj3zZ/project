package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class ShanXiLiXiangEndEvent extends ProcessTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	
	public ShanXiLiXiangEndEvent(UserContext uc, HashMap hash) {
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
			content.append(dataMap.get("PROJECTNAME").toString()).append("项目立项审核通过！");
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("SPZT", "审批通过");
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			 dataMap.put("TJSJ", startDate);
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			
			String xmlxUUID = getConfigUUID("xmlxuuid");
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			String customerno = dataMap.get("CUSTOMERNO").toString();
			conditionMap.put("CUSTOMERNO", customerno);
			List<HashMap> list = DemAPI.getInstance().getList(xmlxUUID,conditionMap, null);
			if(list!=null&&!list.isEmpty()){
				HashMap hashMap = list.get(0);
				hashMap.put("SPZT", "审批通过");
				DemAPI.getInstance().updateFormData(xmlxUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()), hashMap,Long.parseLong(hashMap.get("ID").toString()), false);
			}
			
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
					MessageAPI.getInstance().sendSysMail(senduser,email, "项目立项审核", content.toString());
				}
			}
		}
		return flag;
	}
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
}
