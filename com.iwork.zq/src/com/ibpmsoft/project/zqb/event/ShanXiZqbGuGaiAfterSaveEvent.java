package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.activiti.engine.task.Task;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 股改流程保存后触发
 * 
 */
public class ShanXiZqbGuGaiAfterSaveEvent extends ProcessStepTriggerEvent {

	private static String KHUUID = "a243efd832bf406b9caeaec5df082e28";

	public ShanXiZqbGuGaiAfterSaveEvent(UserContext uc, HashMap hash) {
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
			String customerno = dataMap.get("CUSTOMERNO").toString();
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("CUSTOMERNO", customerno);
			List<HashMap> list = DemAPI.getInstance().getList(KHUUID, conditionMap, null);
			if (!list.isEmpty()) {
				HashMap map=list.get(0);//原来数据
				String ygsmc = dataMap.get("YGSMC").toString();
				String gsmc = dataMap.get("GSMC").toString();
				map.put("BGDZ", ygsmc);
				map.put("CUSTOMERNAME", gsmc);
				Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(map.get("ID").toString());
				flag=DemAPI.getInstance().updateFormData(KHUUID, instanceid, map, dataid, true);
				Long dataId=Long.parseLong(dataMap.get("ID").toString());
				LogUtil.getInstance().addLog(dataId, "股改流程", "公司名称由:"+ygsmc+"改为:"+gsmc);
				String processActDefId = this.getActDefId();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				dataMap.put("LCBH", processActDefId);
				dataMap.put("LCBS", instanceId);
				dataMap.put("TASKID", newTaskId.getId());
				dataMap.put("SPZT", newTaskId.getName());
				
				ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			}
		}
		return flag;
	}
}
