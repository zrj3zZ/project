package com.ibpmsoft.project.zqb.event;

import java.sql.Timestamp;
import java.util.HashMap;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbSYDDDeleteEvent extends ProcessTriggerEvent {

	private static final String ZYDD_UUID = "a6a3202f5b294bb79acd578ddbe05e55";
	public zqbSYDDDeleteEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			HashMap map=DemAPI.getInstance().getFromData(Long.parseLong(dataMap.get("WLINS").toString()), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if (map != null && map.size() >0) {
				//回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
				map.put("LCBS", "");
				map.put("LCBH","");
				map.put("YXID", "");
				map.put("RWID", "");
				map.put("SPZT", "");
				map.put("FKZT", "0");
				map.put("FKDZL", "");
				map.put("PRCID", "");
				map.put("STEPID","");
				map.put("ZCFKSJ",UtilDate.datetimeFormat2((Timestamp)map.get("ZCFKSJ")));
				map.put("GXSJ",UtilDate.datetimeFormat2((Timestamp)map.get("GXSJ")));
				Long instanceid=Long.parseLong(dataMap.get("WLINS").toString());
				Long dataid=Long.parseLong(map.get("ID").toString());
				flag=DemAPI.getInstance().updateFormData(ZYDD_UUID, instanceid, map, dataid, true);
			}else{//之前的历史数据清理
				flag=true;
			}
		}
		flag=true;
		return flag;
	}
	

}

