package com.ibpmsoft.project.zqb.event;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbZYDDAfterSaveEvent extends ProcessStepTriggerEvent {

	private static final String ZYDD_UUID = "a6a3202f5b294bb79acd578ddbe05e55";
	public zqbZYDDAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			// 1.先查询 2.再更新
				//回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			dataMap.put("LCBS", instanceId);
			dataMap.put("LCBH", this.getActDefId());
			dataMap.put("YXID", this.getExcutionId());
			dataMap.put("RWID", this.getTaskId());
			dataMap.put("SPZT", "反馈中");
			dataMap.put("ZCFKSJ",UtilDate.datetimeFormat2((Timestamp)dataMap.get("ZCFKSJ")));
			dataMap.put("GXSJ",UtilDate.datetimeFormat2((Timestamp)dataMap.get("GXSJ")));
			dataMap.put("FKDZL", dataMap.get("FKDZL")!=null?dataMap.get("FKDZL").toString():"");
			dataMap.put("PRCID", this.getProDefId());
			dataMap.put("STEPID", this.getActStepId());
				Long instanceid=Long.parseLong(dataMap.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(dataMap.get("ID").toString());
				flag=ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			
		}

		return flag;
	}
	

}

