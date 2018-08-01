package com.iwork.plugs.contract.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ContractPerformIvoiceEvent extends ProcessTriggerEvent{
	private static final String ivoicePerformUUID = "dde36307fba544c582476c92e11e280f";  //发票执行情况列表主数据模型UUID
	
	public ContractPerformIvoiceEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		
		Long instanceid = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 查询流程数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		Long demInstanceid = DemAPI.getInstance().newInstance(ivoicePerformUUID, uc.get_userModel().getUserid());
		boolean flag = DemAPI.getInstance().saveFormData(ivoicePerformUUID, demInstanceid, dataMap, false);

		//更新计划关联数据
		//saveFkjhTableData(instanceid,dataMap.get("HTXZ").toString(),dataMap.get("HTBH").toString());
		
						
		
		return flag;
	}
}
