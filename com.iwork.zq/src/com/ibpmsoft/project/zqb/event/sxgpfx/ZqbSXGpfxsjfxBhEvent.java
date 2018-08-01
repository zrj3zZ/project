package com.ibpmsoft.project.zqb.event.sxgpfx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.task.Task;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.EventUtil;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ZqbSXGpfxsjfxBhEvent extends ProcessStepTriggerEvent {
	
	public ZqbSXGpfxsjfxBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			
			//重置提交时间格式
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			
			String processActDefId = this.getActDefId();
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			String projectname = dataMap.get("PROJECTNAME").toString();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			Long taskId2 = this.getTaskId();
			
			String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '实际发行信息'", null);
			List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
			Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
			List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMSJFXB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='实际发行信息') AND B.PROJECTNO=?", params);
			HashMap h =  new HashMap();
			h = dataMap;
			h.put("SPZT", "驳回");
			h.put("LCBS", this.getActDefId());
			h.put("LCBH", this.getInstanceId());
			h.put("STEPID",this.getActStepId());
			h.put("TASKID",newTaskId.getId());
			h.put("EXCUTIONID",newTaskId.getExecutionId());
			flag = DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
			
			
		}
		EventUtil.updateMainData4(dataMap);
		return flag;
	}
}
