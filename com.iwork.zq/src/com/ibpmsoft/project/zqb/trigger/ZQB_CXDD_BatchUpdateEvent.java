package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;
import java.util.List;


import com.iwork.commons.util.ParameterMapUtil;

import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;

import com.iwork.sdk.DemAPI;


public class ZQB_CXDD_BatchUpdateEvent extends DemTriggerEvent {
	private static final String GGCB_UUID = "84ff70949eac4051806dc02cf4837bd9";

	public ZQB_CXDD_BatchUpdateEvent(UserContext me, HashMap hash) {
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
		String khbh="";
		if(map.get("KHBH")!=null){
			String[] split = map.get("KHBH").toString().split(",");
			String test="";
			for (String string : split) {
				test+=string+",";
			}
			khbh=test.substring(0, test.length()-1);
		}
		String[] Kkhbh = khbh.split(",");
		for (String string : Kkhbh) {
			HashMap cc = new HashMap();
			cc.put("KHBH", string);
			List<HashMap> list = DemAPI.getInstance().getList(GGCB_UUID, cc, null);
			HashMap cxddmap=list.get(0);
			//FHSPR        ZZSPR    ZZCXDD   KHFZR   CWSCBFZR3    CWSCBFZR2    GGFBR
			if(map.get("FHSPR")!=null&&!"".equals(map.get("FHSPR").toString())){
				cxddmap.put("FHSPR", map.get("FHSPR"));
			}
			if(map.get("ZZSPR")!=null&&!"".equals(map.get("ZZSPR").toString())){
				cxddmap.put("ZZSPR", map.get("ZZSPR"));
			}
			if(map.get("ZZCXDD")!=null&&!"".equals(map.get("ZZCXDD").toString())){
				cxddmap.put("ZZCXDD", map.get("ZZCXDD"));
			}
			if(map.get("KHFZR")!=null&&!"".equals(map.get("KHFZR").toString())){
				cxddmap.put("KHFZR", map.get("KHFZR"));
			}
			if(map.get("CWSCBFZR3")!=null&&!"".equals(map.get("CWSCBFZR3").toString())){
				cxddmap.put("CWSCBFZR3", map.get("CWSCBFZR3"));
			}
			if(map.get("CWSCBFZR2")!=null&&!"".equals(map.get("CWSCBFZR2").toString())){
				cxddmap.put("CWSCBFZR2", map.get("CWSCBFZR2"));
			}
			if(map.get("GGFBR")!=null&&!"".equals(map.get("GGFBR").toString())){
				cxddmap.put("GGFBR", map.get("GGFBR"));
			}
			DemAPI.getInstance().updateFormData(GGCB_UUID, Long.parseLong(list.get(0).get("INSTANCEID").toString()), cxddmap, Long.parseLong(list.get(0).get("ID").toString()), false);
		}
		return true;

	}
}
