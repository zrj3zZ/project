package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbZtNbcbBhEvent extends ProcessStepTriggerEvent{

	public zqbZtNbcbBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
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
//			String sq=dataMap.get("DYZLNR").toString();
//			if("".equals(sq)||sq==null){
//				return true;
//			}
		
			if (!dataMap.isEmpty()) {
				//回写审批状态 字段
				dataMap.put("NODE9SFKF", "驳回");
				dataMap.put("NODE5SFKF",this.getExcutionId()+"");
				dataMap.put("NODE4SFKF", this.getTaskId()+"");
				Long instanceid=Long.parseLong(dataMap.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(dataMap.get("ID").toString());
				String processActDefId = this.getActDefId();
				Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
				flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceid, dataMap, dataid, false,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(flag){
					//获得目标节点信息
					OrgUser orgUser = uc.get_userModel();
					String userid = orgUser.getUserid();
					String username = orgUser.getUsername();
					Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
					String assigneeUserid = newTaskId.getAssignee();
					UserContext target = UserContextUtil.getInstance().getUserContext(assigneeUserid);
					
					HashMap<String,String> contentMap = new HashMap<String,String>();
					
					String yhm=dataMap.get("NODE6SFKF")==null?"":dataMap.get("NODE6SFKF").toString();
					String damc=dataMap.get("NODE1")==null?"":dataMap.get("NODE1").toString();
					String dm=dataMap.get("NODE2")==null?"":dataMap.get("NODE2").toString();
					String content =username+"驳回了"+yhm+"的内部呈报申请:"+damc+"--"+dm+",请查看";
					if (target != null) {
						if (!content.equals("")) {
							//发送短信
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
							}
							//发送邮件
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								String senduser = target.get_userModel().getUsername();
								MessageAPI.getInstance().sendSysMail(senduser, email, "内部呈报", content.toString());
							}
						}
					}

				}
			}
			}
	
		return flag;
	}
}
