package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务归档触发器
 * @author zouyalei
 *
 */
public class zqbProjectTaskEvent  extends ProcessTriggerEvent{

	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";
	public zqbProjectTaskEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		if(dataMap!=null){
			Long instanceid = DemAPI.getInstance().newInstance(PROJECT_TASK_UUID, uc.get_userModel().getUserid());
			
			HashMap<String,Object> hashdata = new HashMap<String,Object>();
			hashdata.put("TASK_NAME", dataMap.get("TASK_NAME")==null?"":dataMap.get("TASK_NAME").toString());
			hashdata.put("STARTDATE", dataMap.get("STARTDATE")==null?"":dataMap.get("STARTDATE").toString());
			hashdata.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE").toString());
			hashdata.put("SCALE", dataMap.get("SCALE")==null?"":dataMap.get("SCALE").toString());
			hashdata.put("JDZL", dataMap.get("JDZL")==null?"":dataMap.get("JDZL").toString());
			hashdata.put("PRIORITY", dataMap.get("PRIORITY")==null?"":dataMap.get("PRIORITY").toString());
			hashdata.put("ATTACH", dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString());
			hashdata.put("MEMO", dataMap.get("MEMO")==null?"":dataMap.get("MEMO").toString());
			hashdata.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER").toString());
			hashdata.put("GROUPID", dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString());
			hashdata.put("PROJECTNO", dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString());
			hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME")==null?"":dataMap.get("PROJECTNAME").toString());
			hashdata.put("ORDERINDEX", dataMap.get("ORDERINDEX")==null?"":dataMap.get("ORDERINDEX").toString());
			hashdata.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE").toString());
			hashdata.put("SSJE", dataMap.get("SSJE")==null?"":dataMap.get("SSJE").toString());
			hashdata.put("GXSJ", dataMap.get("GXSJ")==null?"":dataMap.get("GXSJ").toString());
			
			// 保存到项目任务数据表中
			flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID, instanceid, hashdata, false);
		}
		
		return flag;
	}
}
