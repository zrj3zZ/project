package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 关联方管理信息添加
 * @author wuyao
 *
 */
public class ZQB_CF_SaveAfterEvent  extends DemTriggerEvent {
	public ZQB_CF_SaveAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=hash.get("ZQDM")==null?"":hash.get("ZQDM").toString()+"["+hash.get("ZQJC")==null?"":hash.get("ZQJC").toString()+"]";
		Long dataId=Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "挂牌企业处罚记录管理", value+"添加/修改挂牌企业处罚记录信息");
		return true;
	}
}