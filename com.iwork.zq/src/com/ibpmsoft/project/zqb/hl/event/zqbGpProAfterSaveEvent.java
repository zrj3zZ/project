package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;
/**
 * 挂牌流程保存后触发
 * 
 */
public class zqbGpProAfterSaveEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(zqbGpProAfterSaveEvent.class);
	private final static String CN_FILENAME = "/common.properties";

	public zqbGpProAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = true;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser orgUser = uc.get_userModel();
		String userid = orgUser.getUserid();
		String username = orgUser.getUsername();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			dataMap.put("TYPENO", newTaskId.getExecutionId());
			dataMap.put("STATUS", "未提交");
			dataMap.put("KHLXR", this.getActDefId());
			dataMap.put("CUSTOMERNO", newTaskId.getId());
			// 更新流程表单数据
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}
}
