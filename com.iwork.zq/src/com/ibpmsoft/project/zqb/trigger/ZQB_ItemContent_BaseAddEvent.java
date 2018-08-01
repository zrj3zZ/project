package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 项目基本信息添加
 * 
 * @author David
 * 
 */
public class ZQB_ItemContent_BaseAddEvent extends DemTriggerEvent {
	public ZQB_ItemContent_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId = this.getInstanceId();
		Long demId = Long.parseLong(map.get("modelId").toString());
		HashMap hash = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash.get("ORDERINDEX")==null){
			Long id=Long.parseLong(hash.get("ID").toString());
			hash.put("ORDERINDEX", id);
			DemAPI.getInstance().updateFormData(demId, instanceId, hash, id, false);
		}
		String sxid=hash.get("SXID").toString();
		String plnr=hash.get("PLNR").toString();
		Long dataId = Long.parseLong(hash.get("ID").toString());
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "披露内容信息维护", "更新信披事项ID为"+sxid+"的披露内容:"+plnr);
		}else{
			LogUtil.getInstance().addLog(dataId, "披露内容信息维护", "新增信披事项ID为"+sxid+"的披露内容:"+plnr);
		}
		return true;
	}
}
