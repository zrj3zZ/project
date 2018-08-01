package com.iwork.plugs.contract.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ContractPerformProcessEvent extends ProcessTriggerEvent{
	
	private static final String contractPerformUUID = "c58e802fc13b499f91c7b774defe27a3";  //合同执行情况列表主数据模型UUID
	public ContractPerformProcessEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		
		Long instanceid = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 查询流程数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		Long demInstanceid = DemAPI.getInstance().newInstance(contractPerformUUID, uc.get_userModel().getUserid());
		boolean flag = DemAPI.getInstance().saveFormData(contractPerformUUID, demInstanceid, dataMap, false);

		//更新计划关联数据
		//saveFkjhTableData(instanceid,dataMap.get("HTXZ").toString(),dataMap.get("HTBH").toString());
		
						
		
		return flag;
	}
}
