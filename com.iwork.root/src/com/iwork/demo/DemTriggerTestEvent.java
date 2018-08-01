package com.iwork.demo;

import java.util.HashMap;

import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
/**
 * 数据维护触发器
 * @author davidyang
 *
 */
public class DemTriggerTestEvent extends DemTriggerEvent {
	public DemTriggerTestEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute() {
		return true;
	}
}
