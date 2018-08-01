package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 质控内核删除前
 * 
 * @author David
 * 
 */
public class ZQB_ZKNHRemoveEvent extends DemTriggerEvent {
	public ZQB_ZKNHRemoveEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String zkr=hash.get("ZKR").toString();
		String nhspr=hash.get("NHSPR").toString();
		StringBuffer content=new StringBuffer();
		if(!zkr.equals("")){
			content.append("质控审批人："+zkr+"  ");
		}
		if(!nhspr.equals("")){
			content.append("内核审批人："+nhspr);
		}
		Long dataId = Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "质控内核人员设置信息维护", "删除质控内核审批人员:"+content.toString());
		return true;
	}
	
}
