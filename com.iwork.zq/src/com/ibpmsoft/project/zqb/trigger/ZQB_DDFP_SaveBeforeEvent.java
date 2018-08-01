package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;


import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_DDFP_SaveBeforeEvent extends DemTriggerEvent{
	private final static String DDFP_UUID="84ff70949eac4051806dc02cf4837bd9";
	public ZQB_DDFP_SaveBeforeEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if("0".equals(map.get("instanceId").toString())){//新增时，不允许有重复记录
			HashMap khmap=new HashMap();
			khmap.put("KHBH", map.get("KHBH"));
			List<HashMap> khlist= DemAPI.getInstance().getList(DDFP_UUID, khmap, null);
			if(khlist.size()>0){
			 	return false;
			}
		}else{//不是新增，修改了客户，则判断修改的客户是否已经进行过分派
			HashMap khmap=new HashMap();
			khmap.put("KHBH", map.get("KHBH"));
			List<HashMap> khlist= DemAPI.getInstance().getList(DDFP_UUID, khmap, null);
			if(khlist.size()>0){
				for(HashMap m:khlist){
					if(!m.get("ID").toString().equals(map.get("dataid").toString())){
					 	return false;
					}
				}
			}
		}
		return true;
	}
}
