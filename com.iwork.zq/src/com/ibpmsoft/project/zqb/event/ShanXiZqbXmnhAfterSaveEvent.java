package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.task.Task;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目内核流程保存后触发
 * 
 */
public class ShanXiZqbXmnhAfterSaveEvent extends ProcessStepTriggerEvent {

	public ShanXiZqbXmnhAfterSaveEvent(UserContext uc, HashMap hash) {
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
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			String customername = dataMap.get("CUSTOMERNAME").toString();
			String processActDefId = this.getActDefId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", newTaskId.getId());
			dataMap.put("SPZT", newTaskId.getName());
			LogUtil.getInstance().addLog(dataId, "申报审核", customername+"变更申报审核信息");
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}
}
