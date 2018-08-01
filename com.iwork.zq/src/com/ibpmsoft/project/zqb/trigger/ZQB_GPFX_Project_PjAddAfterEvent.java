package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_GPFX_Project_PjAddAfterEvent  extends DemTriggerEvent {
	public ZQB_GPFX_Project_PjAddAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		String XMRWUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId =this.getInstanceId();
		String projectno =map.get("PROJECTNO").toString();
		String groupid =map.get("GROUPID").toString();
		HashMap conditionMap=new HashMap();
		conditionMap.put("PROJECTNO",projectno );
		conditionMap.put("GROUPID", groupid);
		List<HashMap> list=DemAPI.getInstance().getList(XMRWUUID, conditionMap, null);
		for(HashMap rwmap:list){
			Long instanceid=Long.parseLong(rwmap.get("INSTANCEID").toString());
			Long id=Long.parseLong(rwmap.get("ID").toString());
			rwmap.put("PJINSID", instanceId);
	    	DemAPI.getInstance().updateFormData(XMRWUUID, instanceid, rwmap, id, false);
		}
		return true;
	
	}
}
