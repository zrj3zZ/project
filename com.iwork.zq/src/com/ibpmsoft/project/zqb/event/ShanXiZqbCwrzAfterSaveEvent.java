package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 股改流程保存后触发
 * 
 */
public class ShanXiZqbCwrzAfterSaveEvent extends ProcessStepTriggerEvent {

	private final static String CN_FILENAME = "/common.properties";

	public ShanXiZqbCwrzAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

		if(dataMap!=null){
			//重置提交时间格式
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String processActDefId = this.getActDefId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String taskName = newTaskId.getName()!=null?newTaskId.getName().equals("起草")?"呈报中":newTaskId.getName()+"审批中":"";
			Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", newTaskId.getId());
			dataMap.put("SPZT", taskName);
			dataMap.put("CJSJ",dataMap.get("CJSJ")==null?sdf.format(date):sdf.format(dataMap.get("CJSJ")));
			dataMap.put("DZRQ",dataMap.get("DZRQ")==null?sdf.format(date):sdf.format(dataMap.get("DZRQ")));
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
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
