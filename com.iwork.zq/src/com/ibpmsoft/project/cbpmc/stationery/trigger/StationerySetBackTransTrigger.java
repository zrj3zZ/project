package com.ibpmsoft.project.cbpmc.stationery.trigger;

import java.util.HashMap;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class StationerySetBackTransTrigger  extends ProcessStepTriggerEvent{
	private UserContext _me;
	private HashMap params;
	private String log = "";
	public StationerySetBackTransTrigger(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	public boolean execute()
	  { 
		boolean flag = false;
		HashMap hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(hash!=null){
			hash.put("BILLSTATUS", 10);
			hash.put("PROCESSTYPE","未处理");
			Long id = (Long)hash.get("ID");
			 flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), this.getInstanceId(),hash, id, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		} 
	    return flag;
	  }
} 
