package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class zqbDGDYBhEvent extends ProcessStepTriggerEvent {

	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	public zqbDGDYBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			String sq=dataMap.get("DYZLNR").toString();
			if("".equals(sq)||sq==null){
				return true;
			}
		
			if (!dataMap.isEmpty()) {
				//回写审批状态 字段
				dataMap.put("SPZT", "驳回");
				dataMap.put("LZWZ",this.getExcutionId());
				dataMap.put("RWID", this.getTaskId());
				Long instanceid=Long.parseLong(dataMap.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(dataMap.get("ID").toString());
				String processActDefId = this.getActDefId();
				Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
				flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceid, dataMap, dataid, false,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			}
		}
		return flag;
	}
	

}

