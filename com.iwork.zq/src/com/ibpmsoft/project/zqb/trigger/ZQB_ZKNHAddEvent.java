package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 质控内核保存后
 * 
 * @author David
 * 
 */
public class ZQB_ZKNHAddEvent extends DemTriggerEvent {
	public ZQB_ZKNHAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
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
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "质控内核人员设置信息维护", "更新质控内核审批人员:"+content.toString());
		}else{
			LogUtil.getInstance().addLog(dataId, "质控内核人员设置信息维护", "新增质控内核审批人员:"+content.toString());
		}
		return true;
	}
	
}
