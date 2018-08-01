package com.ibpmsoft.project.zqb.hl.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.activiti.engine.task.Task;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 请假流程发起流程保存后触发
 * @author zouyalei
 *
 */
public class zqbLeaveMsgAfterSaveEvent extends ProcessStepTriggerEvent {

	public zqbLeaveMsgAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			dataMap.put("LCBH", newTaskId.getExecutionId());
			dataMap.put("LZJD", this.getActStepId());
			dataMap.put("SPZT", "未提交");
			dataMap.put("LCBS", this.getActDefId());
			dataMap.put("RWID", newTaskId.getId());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Long qjsj=null;
			try {
				
				long time = sdf.parse(dataMap.get("STARTDATE").toString()).getTime();
				long time2 = sdf.parse(dataMap.get("ENDDATE").toString()).getTime();
				qjsj=(time2-time)/(1000 * 60 * 60 * 24);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataMap.put("EXTEND1", qjsj);
			// 更新流程表单数据
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}

}
