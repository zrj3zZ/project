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

public class zqbCclcBhEvent extends ProcessStepTriggerEvent{

	public zqbCclcBhEvent(UserContext uc, HashMap hash) {
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
		OrgUser orgUser = uc.get_userModel();
		String userid = orgUser.getUserid();
		String username = orgUser.getUsername();
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
				dataMap.put("ZT", "驳回");
				dataMap.put("LZWZ",this.getExcutionId());
				dataMap.put("RWID", this.getTaskId());
				Long instanceid=Long.parseLong(dataMap.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(dataMap.get("ID").toString());
				String processActDefId = this.getActDefId();
				Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
				flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceid, dataMap, dataid, false,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			}
		}
		if(flag){
			
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			String assigneeUserid = newTaskId.getAssignee();
			UserContext target = UserContextUtil.getInstance().getUserContext(assigneeUserid);
			
			HashMap<String,String> contentMap = new HashMap<String,String>();
			String yhm=dataMap.get("YHM")==null?"您":dataMap.get("YHM").toString();
			String lx = dataMap.get("BSLX").toString();
			String content=null;
			if("出差".equals(lx)){
				
				 content = "出差:您因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日到"+dataMap.get("CCMDD").toString()+"出差，被驳回";
			}else{
				 content = "外出:您因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日外出，被驳回";
				 
			}
			if (target != null) {
				if (!content.equals("")) {
					//发送短信
					
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
						
					}
					
						
					UserContext user = UserContextUtil.getInstance().getUserContext("ZHAOWEI");
					if(!username.equals(user.get_userModel().getUsername())){
						String userm=user.get_userModel().getMobile();
						if(userm!=null&&!"".equals(userm)){
						if("出差".equals(lx)){
						content="出差:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日到"+dataMap.get("CCMDD").toString()+"出差，被驳回";
						}else{
						content="外出:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日外出，被驳回";
						}
						MessageAPI.getInstance().sendSMS(uc, userm, content.toString());
						}
					}
					//发送邮件
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						String senduser = target.get_userModel().getUsername();
						MessageAPI.getInstance().sendSysMail(senduser, email, lx, content.toString());
						
					}
				}
			}

		}
	

	
		return flag;
	}
}
