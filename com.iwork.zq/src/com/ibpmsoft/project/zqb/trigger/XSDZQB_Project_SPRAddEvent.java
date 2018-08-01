package com.ibpmsoft.project.zqb.trigger;

import java.util.Date;
import java.util.HashMap;


import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class XSDZQB_Project_SPRAddEvent extends DemTriggerEvent {
	public XSDZQB_Project_SPRAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap m = DemAPI.getInstance().getFromData( this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		m.put("ZHGXSJ", UtilDate.datetimeFormat(new Date()));
		 DemAPI.getInstance().updateFormData(
				"24f7944184ca402d986325ce72fa20c9", this.getInstanceId(), m,
				Long.parseLong(m.get("ID").toString()), false);
		return true;
	}
}
