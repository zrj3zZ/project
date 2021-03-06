package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 申请开票保存后
 * 
 * @author David
 * 
 */
public class ZQB_InvoiceSQKPAddEvent extends DemTriggerEvent {
	public ZQB_InvoiceSQKPAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String customername=hash.get("CUSTOMERNAME").toString();
		Long dataId = Long.parseLong(hash.get("ID").toString());
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "申请开票信息维护", customername+",更新申请开票信息。");
		}else{
			LogUtil.getInstance().addLog(dataId, "申请开票信息维护", customername+",添加申请开票信息。");
		}
		return true;
	}
	
}
