package com.iwork.plugs.contract.event;

import java.util.HashMap;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ContractPerformDeliveryProcessEvent extends ProcessTriggerEvent{
	private static final String deliveryPerformUUID= "3b8e530169a04e9b8b0f609315cd8e52";  //交货执行情况列表主数据模型UUID
	public ContractPerformDeliveryProcessEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag=false;
		//try{
		
		Long instanceid = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 查询流程数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		Long demInstanceid = DemAPI.getInstance().newInstance(deliveryPerformUUID, uc.get_userModel().getUserid());
	    flag = DemAPI.getInstance().saveFormData(deliveryPerformUUID, demInstanceid, dataMap, false);

		return flag;
	}
	
	
	
}
