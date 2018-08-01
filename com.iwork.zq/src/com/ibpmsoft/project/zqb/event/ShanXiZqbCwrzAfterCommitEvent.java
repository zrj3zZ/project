package com.ibpmsoft.project.zqb.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbCwrzAfterCommitEvent extends ProcessStepTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	
	public ShanXiZqbCwrzAfterCommitEvent(UserContext uc, HashMap hash) {
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
				
		if(dataMap!=null){
			//获取提交时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String processActDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			String customername = dataMap.get("CUSTOMERNAME").toString();
			StringBuffer content=new StringBuffer();
			content.append(customername).append("已提交财务入账信息，请审核!");
			
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String taskName = newTaskId.getName()!=null?newTaskId.getName().equals("起草")?"呈报中":"审批中":"";
			Long lcDataId = Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", newTaskId.getId());
			dataMap.put("CJSJ",dataMap.get("CJSJ")==null?sdf.format(date):sdf.format(dataMap.get("CJSJ")));
			dataMap.put("DZRQ",dataMap.get("DZRQ")==null?sdf.format(date):sdf.format(dataMap.get("DZRQ")));
			dataMap.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername()+taskName);
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			
			UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
			
			String assignee = newTaskId.getAssignee();
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
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
						MessageAPI.getInstance().sendSysMail(senduser, email, "财务入账信息审核", content.toString());
					}
				}
			}
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
}
