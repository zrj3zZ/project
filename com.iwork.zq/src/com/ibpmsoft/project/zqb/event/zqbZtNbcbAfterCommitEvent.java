package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbZtNbcbAfterCommitEvent extends ProcessStepTriggerEvent{
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件  
	public zqbZtNbcbAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser orgUser = uc.get_userModel();
		String userid = orgUser.getUserid();
	
		
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			//获取当前日期时间
		
			//获得当前办理人id
			
			dataMap.put("NODE9SFKF", "审批中");
			dataMap.put("NODE2SFKF", this.getActDefId());
			dataMap.put("NODE1SKP", this.getActStepId());
			dataMap.put("NODE5SFKF",this.getExcutionId()+"");
			dataMap.put("NODE4SFKF", this.getTaskId()+"");
					
			List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
			if(pro!=null&pro.size()>0){
				Long prc=pro.get(0).getPrcDefId();
				dataMap.put("NODE3SFKF", prc);
			 }
			String processActDefId = this.getActDefId();
			Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			
			String mailContent = "";
			String damc=dataMap.get("NODE1")==null?"":dataMap.get("NODE1").toString();
			String dm=dataMap.get("NODE2")==null?"":dataMap.get("NODE2").toString();
			mailContent =uc.get_userModel().getUsername() + "提交了内部呈报："+damc+"--"+dm+"，请求审核！";
			//发送短信
			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
			String username = "";
			String noticename = "内部呈报："+dataMap.get("NODE1").toString();
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			if (target != null) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("") ) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
					}
					String email = target.get_userModel().getEmail();
    				if(email!=null&&!email.equals("") && uc != null){
    					username = uc.get_userModel().getUsername();
    					MessageAPI.getInstance().sendSysMail(username, email, "内部呈报", mailContent+"<br>"+noticename);
    				}
			}
		}
		return flag;
	}
}
