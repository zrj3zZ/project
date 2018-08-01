package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 重大事项归档触发器
 * @author zouyalei
 *
 */
public class zqbMajorMatterEvent  extends ProcessTriggerEvent{

	private static final String MAJOR_MATTER_UUID = "c807510e83a0415cb37810bc2994d71a";
	public zqbMajorMatterEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		if(dataMap!=null){
			Long instanceid = DemAPI.getInstance().newInstance(MAJOR_MATTER_UUID, uc.get_userModel().getUserid());
			
			HashMap<String,Object> hashdata = new HashMap<String,Object>();
			hashdata.put("SXBH", dataMap.get("SXBH")==null?"":dataMap.get("SXBH").toString());
			hashdata.put("FSR", dataMap.get("FSR")==null?"":dataMap.get("FSR").toString());
			hashdata.put("FSRZH", dataMap.get("FSRZH")==null?"":dataMap.get("FSRZH").toString());
			hashdata.put("CONTENT", dataMap.get("CONTENT")==null?"":dataMap.get("CONTENT").toString());
			hashdata.put("FJ", dataMap.get("FJ")==null?"":dataMap.get("JDZL").toString());
			hashdata.put("REANSER", dataMap.get("REANSER")==null?"":dataMap.get("REANSER").toString());
			
			// 保存到重大事项数据表中
			flag = DemAPI.getInstance().saveFormData(MAJOR_MATTER_UUID, instanceid, hashdata, false);
		}
		
		return flag;
	}

}
