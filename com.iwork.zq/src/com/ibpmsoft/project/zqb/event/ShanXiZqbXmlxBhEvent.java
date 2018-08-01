package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ShanXiZqbXmlxBhEvent extends ProcessStepTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	
	public ShanXiZqbXmlxBhEvent(UserContext uc, HashMap hash) {
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
			
			String projectname = dataMap.get("PROJECTNAME").toString();
			String customerno = dataMap.get("CUSTOMERNO").toString();
			String processActDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String xmlxUUID = getConfigUUID("xmlxuuid");
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("CUSTOMERNO", customerno);
			List<HashMap> list = DemAPI.getInstance().getList(xmlxUUID,conditionMap, null);
			if(list!=null&&!list.isEmpty()){
				HashMap hashMap = list.get(0);
				Long dataId=Long.parseLong(hashMap.get("ID").toString());
				hashMap.put("LCBH", processActDefId);
				hashMap.put("LCBS", instanceId);
				hashMap.put("TASKID", newTaskId.getId());
				hashMap.put("SPZT", newTaskId.getName());
				flag=DemAPI.getInstance().updateFormData(xmlxUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()), hashMap,dataId, false);
				LogUtil.getInstance().addLog(dataId, "项目立项审核流程", projectname+"项目立项审核流程被驳回！");
			}
			/*String smsContent = projectname+"项目立项审核流程被驳回！";
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
						MessageAPI.getInstance().sendSysMail(senduser,email, "项目立项审核", smsContent);
					}
				}
			}*/
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
