package com.ibpmsoft.project.zqb.event;


import java.util.HashMap;
import java.util.List;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbGGSPAfterDeleteEvent extends ProcessTriggerEvent {
	public zqbGGSPAfterDeleteEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	private static final String GG_UUID = "1dfe958166a347188339af1337e25fb7";

	public boolean execute() {
		boolean flag = true;
		Long instanceId = this.getInstanceId();
		HashMap hashmap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String sq=dataMap.get("NOTICESQ").toString();
		if("".equals(sq)||sq==null){
			return true;
		}
		hashmap.put("NOTICESQ", sq);
		List<HashMap> list = DemAPI.getInstance().getList(GG_UUID,hashmap, null);
		if(list.size()==1){
			for (HashMap map: list ) {
				if(!map.get("SPZT").equals("")){
					MessageQueueUtil.getInstance().putAlertMsg("当前公告已处于流转中，无法进行删除！");
					flag=false;
				}
			}
		}
		return flag;
	}
}