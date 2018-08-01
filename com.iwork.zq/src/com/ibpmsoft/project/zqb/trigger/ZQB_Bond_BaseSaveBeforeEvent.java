package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;


public class ZQB_Bond_BaseSaveBeforeEvent extends DemTriggerEvent {

	public ZQB_Bond_BaseSaveBeforeEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		return true;

	}
}
