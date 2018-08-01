package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class zqbDzwothSbzlProDemAfterSaveEvent extends DemTriggerEvent{
	public zqbDzwothSbzlProDemAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		//定增(200人以内)项目
		String XMUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='申报资料二百以上'", "UUID");
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		hash.put("LCBH", this.getInstanceId());
		hash.put("SPZT", "已申报");
		boolean flag = DemAPI.getInstance().updateFormData(XMUUID, this.getInstanceId(), hash, Long.parseLong(hash.get("ID").toString()), false);
		return true;
	}

}
