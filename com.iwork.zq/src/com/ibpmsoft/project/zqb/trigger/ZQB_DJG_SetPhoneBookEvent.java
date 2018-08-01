package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;

import com.iwork.sdk.DemAPI;
public class ZQB_DJG_SetPhoneBookEvent  extends DemTriggerEvent { 
	public ZQB_DJG_SetPhoneBookEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap hash = this.getFormData();
		HashMap map = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=map.get("XM")==null?"":map.get("XM").toString();
		Long dataId=Long.parseLong(map.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "董监高信息", "添加/编辑董监高信息："+value);
		return false;
	}
}

