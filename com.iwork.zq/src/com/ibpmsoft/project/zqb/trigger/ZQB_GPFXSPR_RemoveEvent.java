package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 股票发行项目审批人删除前
 * 
 * @author David
 * 
 */
public class ZQB_GPFXSPR_RemoveEvent extends DemTriggerEvent {
	public ZQB_GPFXSPR_RemoveEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String csfzr=hash.get("CSFZR").toString();
		String fsfzr=hash.get("FSFZR").toString();
		String zzspr=hash.get("ZZSPR").toString();
		StringBuffer content=new StringBuffer();
		if(!csfzr.equals("")){
			content.append("初审负责人："+csfzr+"  ");
		}
		if(!fsfzr.equals("")){
			content.append("复审负责人："+fsfzr+"  ");
		}
		if(!zzspr.equals("")){
			content.append("最终负责人："+zzspr);
		}
		Long dataId = Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "股票发行项目审批人设置信息维护", "删除股票发行项目审批人员:"+content.toString());
		return true;
	}
	
}
