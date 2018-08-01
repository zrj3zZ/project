package com.ibpmsoft.project.zqb.event.sxgpfx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 拟发行流程提交后触发
 * 
 * @author zouyalei
 * 
 */
public class ZqbSXGpfxsbzlAfterCommitEvent extends ProcessStepTriggerEvent {
	
	public ZqbSXGpfxsbzlAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		String processActDefId = this.getActDefId();
		String actStepId = this.getActStepId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取登录人信息
		String TJRID = UserContextUtil.getInstance().getCurrentUserId();
		UserContext DLUser = UserContextUtil.getInstance().getCurrentUserContext();
		String TJRXM = DLUser._userModel.getUsername(); 
		//获取提交时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dataMap!=null){
			String projectname = dataMap.get("PROJECTNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			StringBuffer content=new StringBuffer();
			content.append("股票发行项目:").append(projectname).append(",申报资料信息已提交,请审核!");
			String assignee = newTaskId.getAssignee();
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = ut._userModel.getUserid();
			if(SystemConfig._sbzlLcConf.getJd3().equals(actStepId)){
				deleteData(processActDefId,SystemConfig._sbzlLcConf.getJd3(),userid,instanceId);
			}
			if (target != null) {
				if (!content.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("") && ut != null) {
						String senduser = ut.get_userModel().getUsername();
						 MessageAPI.getInstance().sendSysMail(senduser, email, "申报资料信息审批", content.toString());
					}
				}
			}
			
			//param2、4
			List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
			Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
			List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMSBZLSB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='申报资料信息') AND B.PROJECTNO=?", params);
			//param3
			HashMap h =  new HashMap();
			h = dataMap;
			h.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername()+"审批中");
			h.put("LCBS", this.getActDefId());
			h.put("LCBH", this.getInstanceId());
			h.put("STEPID",this.getActStepId());
			h.put("TASKID",newTaskId.getId());
			h.put("EXCUTIONID",newTaskId.getExecutionId());
			//param1
			String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '申报资料信息'", null);
			
			flag = DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
		}
		//EventUtil.updateMainData4(dataMap);
		
		Long dataId=Long.parseLong(dataMap.get("ID").toString());
		dataMap.put("TJRID", TJRID);
		dataMap.put("TJRXM", TJRXM);
		dataMap.put("TJSJ", sdf.format(date));
		flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
	
	private void deleteData(String processActDefId, String actStepId, String userid,Long instanceId) {
		StringBuffer sql = new StringBuffer("DELETE FROM PROCESS_RU_OPINION WHERE");
		sql.append(" ACT_DEF_ID = '").append(processActDefId).append("'");
		sql.append(" AND ACTION = '规则跳转[*]'");
		sql.append(" AND ACT_STEP_ID = '").append(actStepId).append("'");
		sql.append(" AND CREATEUSER = '").append(userid).append("'");
		sql.append(" AND INSTANCEID = ").append(instanceId);
		com.iwork.core.db.DBUtil.executeUpdate(sql.toString());
	}
}
