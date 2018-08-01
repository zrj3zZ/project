package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;


import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_Fzgs_BaseAddEvent extends DemTriggerEvent {

	public ZQB_Fzgs_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=hash.get("GSMC")==null?"":hash.get("GSMC").toString();
		Long dataId=Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "分子公司信息", "添加/修改分子公司信息："+value);
		return true;

	}
}
