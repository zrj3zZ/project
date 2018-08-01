package com.iwork.plugs.resoucebook.trigger;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;

public class CarManageToDriverTriggerEvent extends ProcessStepTriggerEvent {
	private UserContext _me;
	private SpaceManageDao spaceManageDao;
	private HashMap params;
	
	public CarManageToDriverTriggerEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	} 
	
	/**
	 * 我被初始化了 
	 */
	public boolean execute() {
		boolean flag = false;
		String[] mailToUser = {"yangdayong@kingsoft.com"};
		return flag;
	}
}
