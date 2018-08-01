package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;
import java.util.List;


import com.iwork.commons.util.ParameterMapUtil;

import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;

import com.iwork.sdk.DemAPI;

public class ZQB_GGSP_BaseAddEvent extends DemTriggerEvent {
	private static final String GGCB_UUID = "1dfe958166a347188339af1337e25fb7";

	public ZQB_GGSP_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();  
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap aa = new HashMap();
		HashMap cc = new HashMap();
		cc.put("KHBH", map.get("KHBH"));
		cc.put("NOTICENO", map.get("NOTICENO"));
		List<HashMap> list = DemAPI.getInstance().getList(GGCB_UUID, cc, null);
		aa.put("MEETNAME", list.get(0).get("MEETNAME"));
		DemAPI.getInstance().updateFormData(GGCB_UUID, Long.parseLong(list.get(0).get("instanceId").toString()), aa, Long.parseLong(list.get(0).get("ID").toString()), false);
		return true;

	}
}
