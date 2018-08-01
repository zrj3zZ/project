package com.iwork.asset.util;



import java.util.HashMap;

import com.iwork.asset.constant.StatusConstant;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 
 * 公共触发器
 * 流程节点触发器
 * 修改流程状态(起草-审批中)
 * 在流程第一个节点配置
 * @author yanglianfeng
 *
 */
public class StatusModifyStepTriggerEvent extends ProcessStepTriggerEvent {

	public StatusModifyStepTriggerEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;
		
		// 获取流程实例
		Long instanceid = this.getInstanceId();
		// 获取流程标识
		String actDefId = this.getActDefId();
		// 查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		// 获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		
		// 设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_SHENPIZHONG);
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
}
