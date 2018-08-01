package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
/**
 * 档案归档流程发送后触发
 * @author RENSONGBING
 *
 */
public class zqbDAGDAfterSaveEvent extends ProcessStepTriggerEvent  {
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件    
	public zqbDAGDAfterSaveEvent(UserContext uc, HashMap hash) {
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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		
			String nowdatetime = df.format(new Date());
			//获得当前办理人id
			
			dataMap.put("ZT", "审批中");
			dataMap.put("LCBH", this.getActDefId());
			dataMap.put("LZJD", this.getActStepId());
			dataMap.put("LZWZ",this.getExcutionId());
			dataMap.put("RWID", this.getTaskId());
					
			List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
			if(pro!=null&pro.size()>0){
				Long prc=pro.get(0).getPrcDefId();
				dataMap.put("LCBS", prc);
			 }
			String processActDefId = this.getActDefId();
			Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			
			String mailContent = "";
			String damc=dataMap.get("DAMC")==null?"":dataMap.get("DAMC").toString();
			mailContent =uc.get_userModel().getUsername() + "提交了档案："+damc+"，请求审核！";
			//发送短信
			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
			String username = "";
			String noticename = "档案名称："+dataMap.get("DAMC").toString();
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
    					MessageAPI.getInstance().sendSysMail(username, email, "档案归档", mailContent+"<br>"+noticename);
    				}
			}
		}
		return flag;
	}
}
