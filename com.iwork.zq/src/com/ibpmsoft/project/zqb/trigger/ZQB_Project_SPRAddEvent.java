package com.ibpmsoft.project.zqb.trigger;

import java.util.Date;
import java.util.HashMap;


import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_Project_SPRAddEvent extends DemTriggerEvent {
	public ZQB_Project_SPRAddEvent(UserContext me, HashMap hash) {
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
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "项目审批人设置信息维护", "更新项目审批人员:"+content.toString());
		}else{
			LogUtil.getInstance().addLog(dataId, "项目审批人设置信息维护", "新增项目审批人员:"+content.toString());
		}
		return true;
	}
}
