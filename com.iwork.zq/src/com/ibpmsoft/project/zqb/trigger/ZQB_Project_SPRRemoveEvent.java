package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_Project_SPRRemoveEvent extends DemTriggerEvent {
	public ZQB_Project_SPRRemoveEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap m = DemAPI.getInstance().getFromData( this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String csfzr=m.get("CSFZR").toString();
		String fsfzr=m.get("FSFZR").toString();
		String zzspr=m.get("ZZSPR").toString();
		String xmbh=m.get("XMBH").toString();
		StringBuffer content=new StringBuffer();
		if(!xmbh.equals("")){
			content.append("项目编号为："+xmbh+"的");
		}
		if(!csfzr.equals("")){
			content.append("初审负责人："+csfzr+"  ");
		}
		if(!fsfzr.equals("")){
			content.append("复审负责人："+fsfzr+"  ");
		}
		if(!zzspr.equals("")){
			content.append("最终负责人："+zzspr);
		}
		Long dataId = Long.parseLong(m.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "项目审批人设置信息维护", "删除项目审批人员:"+content.toString());
		return true;
	}
}
