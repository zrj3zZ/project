package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.dao.ProcessOpinionDAO;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbXmlxAfterCommitEvent extends ProcessStepTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	 private static ProcessOpinionDAO processOpinionDAO;
	
	public ShanXiZqbXmlxAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取登录人信息
		String TJRID = UserContextUtil.getInstance().getCurrentUserId();
		UserContext DLUser = UserContextUtil.getInstance().getCurrentUserContext();
		String TJRXM = DLUser._userModel.getUsername(); 
		//获取提交时间
		 Date date = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
		 dataMap.put("TJSJ", startDate);
			
		if(dataMap!=null){
			Long taskId = this.getTaskId();
			String processActDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			Long proDefId = this.getProDefId();
			String projectname = dataMap.get("PROJECTNAME").toString();
			String customerno = dataMap.get("CUSTOMERNO").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String taskDefinitionKey = newTaskId.getTaskDefinitionKey();
			StringBuffer content=new StringBuffer();
			content.append(projectname).append("已提交项目立项，请审核!");
			String xmlxUUID = getConfigUUID("xmlxuuid");
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("CUSTOMERNO", customerno);
			List<HashMap> list = DemAPI.getInstance().getList(xmlxUUID,conditionMap, null);
			if(list!=null&&!list.isEmpty()){
				HashMap hashMap = list.get(0);
				hashMap.put("LCBH", processActDefId);
				hashMap.put("LCBS", instanceId);
				hashMap.put("STEPID", taskDefinitionKey);
				hashMap.put("TASKID", newTaskId.getId());
				hashMap.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername());
				DemAPI.getInstance().updateFormData(xmlxUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()), hashMap,Long.parseLong(hashMap.get("ID").toString()), false);
			}
			String assignee = newTaskId.getAssignee();
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = ut._userModel.getUserid();
			if(SystemConfig._xmlxSpLcConf.getJd3().equals(actStepId)){
				deleteData(processActDefId,SystemConfig._xmlxSpLcConf.getJd3(),userid,instanceId);
			}
			if (target != null) {
				if (!content.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("") && ut != null) {
						String senduser = ut.get_userModel().getUsername();
						 MessageAPI.getInstance().sendSysMail(senduser, email, "项目立项审核", content.toString());
					}
				}
			}
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("TJRID", TJRID);
			dataMap.put("TJRXM", TJRXM);
			dataMap.put("TJSJ", sdf.format(date));
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}
	

	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	
	private void deleteData(String processActDefId, String actStepId, String userid,Long instanceId) {
		StringBuffer sql = new StringBuffer("DELETE FROM PROCESS_RU_OPINION WHERE");
		sql.append(" ACT_DEF_ID = '").append(processActDefId).append("'");
		sql.append(" AND ACTION = '规则跳转[*]'");
		sql.append(" AND ACT_STEP_ID = '").append(actStepId).append("'");
		sql.append(" AND CREATEUSER = '").append(userid).append("'");
		sql.append(" AND INSTANCEID = ").append(instanceId);
		DBUtil.executeUpdate(sql.toString());
	}
}
