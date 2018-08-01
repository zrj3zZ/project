package com.ibpmsoft.project.zqb.event.sxgpfx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.task.Task;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 拟发行流程保存后触发
 * 
 */
public class ZqbSXGpfxfazlbsAfterSaveEvent extends ProcessStepTriggerEvent {

	public ZqbSXGpfxfazlbsAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

		if(dataMap!=null){
			//重置提交时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			Map<Integer,String> map = new HashMap<Integer,String>();
			map.put(1,dataMap.get("PROJECTNO").toString());
			String num = DBUtil.getDataStr("NUM", "SELECT COUNT(ID) NUM FROM BD_ZQB_GPFXXMFAZLBSB WHERE PROJECTNO=?",map);
			
			String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '方案资料报审信息'", null);
			
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			if(num.equals("0")){
				Long ins = DemAPI.getInstance().newInstance(demUUID,user.getUserid());
				HashMap h =  new HashMap();
				h = dataMap;
				h.put("SPZT", "未提交");
				h.put("LCBS", this.getActDefId());
				h.put("LCBH", this.getInstanceId());
				h.put("STEPID",this.getActStepId());
				h.put("TASKID",newTaskId.getId());
				h.put("PRODEFID","");
				h.put("EXCUTIONID",newTaskId.getExecutionId());
				
				flag = DemAPI.getInstance().saveFormData(demUUID, ins,h, false);
			}else{
				List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
				Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
				List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMFAZLBSB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='方案资料报审信息') AND B.PROJECTNO=?", params);
				HashMap h = new HashMap();
				h = dataMap;
				h.put("SPZT", "未提交");
				h.put("LCBS", this.getActDefId());
				h.put("LCBH", this.getInstanceId());
				h.put("STEPID",this.getActStepId());
				h.put("TASKID",newTaskId.getId());
				h.put("EXCUTIONID",newTaskId.getExecutionId());
				flag = DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
			}
		}
		//EventUtil.updateMainData4(dataMap);
		return flag;
	}
}
