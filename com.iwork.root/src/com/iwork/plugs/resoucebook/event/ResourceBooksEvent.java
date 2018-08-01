package com.iwork.plugs.resoucebook.event;

import java.util.Date;
import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class ResourceBooksEvent extends ProcessTriggerEvent {
	private static SpaceManageDao spaceManageDao;
	public ResourceBooksEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		boolean flag = true;
		Long instanceid = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			 if(spaceManageDao==null){
				 spaceManageDao = (SpaceManageDao)SpringBeanUtil.getBean("spaceManageDao");
			 }
		IworkRmWeb rmb = new IworkRmWeb();
		rmb.setSpaceid(new Long(6));
		rmb.setSpacename("证照管理");
		rmb.setResouceid(String.valueOf(dataMap.get("RESOURCEID")));
		rmb.setResouce(String.valueOf(dataMap.get("RESOURCENAME")));
		rmb.setUserid(String.valueOf(dataMap.get("SQRZH")));
		rmb.setUsername(String.valueOf(dataMap.get("SQR")));
		rmb.setBegintime((Date)dataMap.get("SQKSRQ"));
		rmb.setEndtime((Date)dataMap.get("SQJSRQ"));
		rmb.setStatus(new Long(1));
		rmb.setMemo(String.valueOf(dataMap.get("SQYY")));
		spaceManageDao.addWeb(rmb);
		
		return flag;
	}
}
