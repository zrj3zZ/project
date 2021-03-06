package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class zqbDzwothProDemAfterSaveEvent extends DemTriggerEvent{
	public zqbDzwothProDemAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		//定增(200人以内)项目
		String XMUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='定增(200人以上)资料归档'", "UUID");
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash.get("JDMC")!=null&&hash.get("JDMC").equals("初步尽调")){
			hash.put("SPZT", "已尽调");
		}else if(hash.get("JDMC")!=null&&hash.get("JDMC").equals("资料归档（需确认是否发行成功的状态）")){
			hash.put("SPZT", "已归档");
		}
		hash.put("LCBH", this.getInstanceId());
		boolean flag = DemAPI.getInstance().updateFormData(XMUUID, this.getInstanceId(), hash, Long.parseLong(hash.get("ID").toString()), false);
		return true;
	}
}
