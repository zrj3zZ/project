package com.iwork.demo;

import java.util.HashMap;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
/**
 * 流程触发器
 * @author davidyang
 *
 */
public class ProcessTriggerTestEvent extends ProcessTriggerEvent {
	
	public ProcessTriggerTestEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	public boolean execute() {
		UserContext _me = this.getContext();
		String actDefId = this.getActDefId();
		//MessageQueueUtil.getInstance().putAlertMsg("ad司法所地方");
		return true;
	}
	
}
