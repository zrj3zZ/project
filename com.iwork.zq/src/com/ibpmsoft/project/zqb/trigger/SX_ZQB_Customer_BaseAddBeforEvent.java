package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import com.ibpmsoft.project.zqb.service.ZqbUpdateDataService;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;

public class SX_ZQB_Customer_BaseAddBeforEvent extends DemTriggerEvent{
	private ZqbUpdateDataService zqbUpdateDataService;
	
	public SX_ZQB_Customer_BaseAddBeforEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		HashMap map = null;
		if(formData!=null){
			 map = ParameterMapUtil.getParameterMap(formData);
		}
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(zqbUpdateDataService==null){
			zqbUpdateDataService = (ZqbUpdateDataService)SpringBeanUtil.getBean("zqbUpdateDataService");
		}
		zqbUpdateDataService.updateDataCustomer(hash, map);
		return true;
	}
	

}
