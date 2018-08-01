package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;


import com.iwork.app.log.util.LogUtil;

import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;

import com.iwork.sdk.DemAPI;

public class ZQB_Cnqk_BaseDeleteBeforeEvent extends DemTriggerEvent {

	public ZQB_Cnqk_BaseDeleteBeforeEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		Long instanceId = this.getInstanceId();
		HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
		String value=fromData.get("CNZT")==null?"":fromData.get("CNZT").toString();
		Long dataId=Long.parseLong(fromData.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "对外承诺情况", "删除对外承诺情况："+value);
		return true;

	}
}
