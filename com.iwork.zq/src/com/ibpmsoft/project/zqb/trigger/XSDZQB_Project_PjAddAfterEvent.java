package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class XSDZQB_Project_PjAddAfterEvent  extends DemTriggerEvent {
	private final static String RW_UUID="b25ca8ed0a5a484296f2977b50db8396";
	public XSDZQB_Project_PjAddAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId =this.getInstanceId();
		//Long.valueOf(String.valueOf(map.get("instanceId"))).longValue();
		String projectno =map.get("PROJECTNO").toString();
		String groupid =map.get("GROUPID").toString();
	    //更新任务管理表中的pjinsid
		HashMap conditionMap=new HashMap();
		conditionMap.put("PROJECTNO",projectno );
		conditionMap.put("GROUPID", groupid);
		List<HashMap> list=DemAPI.getInstance().getList(RW_UUID, conditionMap, null);
		for(HashMap rwmap:list){
			Long instanceid=Long.parseLong(rwmap.get("INSTANCEID").toString());
			Long id=Long.parseLong(rwmap.get("ID").toString());
			rwmap.put("PJINSID", instanceId);
	    	DemAPI.getInstance().updateFormData(RW_UUID, instanceid, rwmap, id, false);
		}
		return true;
	
	}
}
