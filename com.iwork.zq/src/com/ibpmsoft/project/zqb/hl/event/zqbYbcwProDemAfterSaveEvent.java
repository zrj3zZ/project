package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class zqbYbcwProDemAfterSaveEvent extends DemTriggerEvent{

	public zqbYbcwProDemAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		//定增(200人以内)项目
		String XMUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='一般性财务顾问项目'", "UUID");
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("初步尽调")){
			hash.put("SPZT", "已尽调");
		}else if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("资料归档（需确认是否发行成功的状态）")){
			hash.put("SPZT", "已归档");
		}
		hash.put("LCBH", this.getInstanceId());
		boolean flag = DemAPI.getInstance().updateFormData(XMUUID, this.getInstanceId(), hash, Long.parseLong(hash.get("ID").toString()), false);
		return true;
	}

}
