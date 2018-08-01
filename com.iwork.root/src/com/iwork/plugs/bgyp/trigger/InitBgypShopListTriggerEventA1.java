package com.iwork.plugs.bgyp.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.fi.util.CalculateUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class InitBgypShopListTriggerEventA1 extends ProcessTriggerEvent {
	

	public InitBgypShopListTriggerEventA1(UserContext me,HashMap hash){
		super(me,hash);
		
	} 
	
	/**
	 * 归档成功添加单据编号
	 */
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		String actDefId = this.getActDefId();
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
        //更改流程表单状态为"complete"
		dataMap.put("ZT", "complete");
		ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap,CalculateUtil.changeTypeToLong(dataMap.get("ID")), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		Long dataid = new Long((Long)dataMap.get("ID"));
		String djbh = String.valueOf(dataMap.get("DJBH"));
		List<HashMap> datalist = new ArrayList();
		//ProcessAPI.getInstance().getSubFormDataList(foemId, instanceId, );
		List<HashMap> list = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_BGYPSQDZB");
		for(HashMap map:list){
			map.put("DJBH", djbh);
			
			map.get("SPBH");
			map.get("SQSL");
			
			HashMap<String,Object> queryMap = new HashMap<String, Object>();
			queryMap.put("NO", map.get("SPBH"));
			DemAPI.getInstance().getList("53d9813e-8370-4359-9ed9-482f6153f96c", queryMap, null);
			DBUtil.executeUpdate("update BD_BGYP_JCXXB set kcsl = (kcsl-"+map.get("SQSL")+") where no = '"+map.get("SPBH")+"'");
			datalist.add(map);
		}
		ProcessAPI.getInstance().updateFormDatas(actDefId, "SUBFORM_BGYPSQDZB", instanceId, datalist, null, null, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return true;
	}
}
