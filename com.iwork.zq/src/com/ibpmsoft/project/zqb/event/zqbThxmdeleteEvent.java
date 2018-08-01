package com.ibpmsoft.project.zqb.event;


import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbThxmdeleteEvent extends ProcessTriggerEvent {
	public zqbThxmdeleteEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	

	public boolean execute() {
		boolean flag = true;
		Long instanceId = this.getInstanceId();
		HashMap hashmap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String sq=dataMap.get("XMMC").toString().trim();
		if("".equals(sq)||sq==null){
			return true;
		}
		hashmap.put("XMMC", sq);
		List<HashMap> list = DemAPI.getInstance().getList(this.getConfigUUID("thxmuuid"),hashmap, null);
		if(list.size()>0){
			MessageQueueUtil.getInstance().putAlertMsg("当前项目已处于流转中，无法进行删除！");
			flag=false;
		}
		return flag;
	}
}