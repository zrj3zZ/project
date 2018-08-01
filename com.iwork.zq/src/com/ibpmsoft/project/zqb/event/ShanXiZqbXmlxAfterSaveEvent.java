package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 股改流程保存后触发
 * 
 */
public class ShanXiZqbXmlxAfterSaveEvent extends ProcessStepTriggerEvent {

	private final static String CN_FILENAME = "/common.properties";

	public ShanXiZqbXmlxAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

		if(dataMap!=null){
			//重置提交时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			String processActDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String customerNo = dataMap.get("CUSTOMERNO").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 判断是否存在，已经存在，不存在保存，存在更新
			String xmlxUUID = getConfigUUID("xmlxuuid");
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("CUSTOMERNO", customerNo);
			List<HashMap> xmlxList = DemAPI.getInstance().getList(xmlxUUID, conditionMap, null);
			if (xmlxList != null && xmlxList.size() == 1) {
				HashMap hash = xmlxList.get(0);
				Long dataInstanceid = Long.parseLong(hash.get("INSTANCEID").toString());
				Long dataId = Long.parseLong(hash.get("ID").toString());
				hash.put("LCBH", processActDefId);
				hash.put("LCBS", instanceId);
				hash.put("STEPID", actStepId);
				hash.put("TASKID", newTaskId.getId());
				hash.put("SPZT", newTaskId.getName());
				flag = DemAPI.getInstance().updateFormData(xmlxUUID,dataInstanceid,	hash, dataId,false);
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
