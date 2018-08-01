package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbCclcAfterSaveEvent extends ProcessStepTriggerEvent{
	public zqbCclcAfterSaveEvent(UserContext uc, HashMap hash) {
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
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		OrgUser orgUser = uc.get_userModel();
		String userid = orgUser.getUserid();
		
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			//获取当前日期时间
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		
//			String nowdatetime = df.format(new Date());
			//获得当前办理人id
			
			
			dataMap.put("ZT", "审批中");
			dataMap.put("LCBH", this.getActDefId());
			dataMap.put("LZJD", this.getActStepId());
			dataMap.put("LZWZ",this.getExcutionId());
			dataMap.put("RWID", this.getTaskId());
			String qssj=dataMap.get("QSSJ")==null?"":dataMap.get("QSSJ").toString();
			String jssj=dataMap.get("JSSJ")==null?"":dataMap.get("JSSJ").toString();
			if(!"".equals(qssj)){
				qssj=StringUtils.replace(qssj," ","日");
//				int i=qssj.indexOf(":");
//				qssj=qssj.substring(0,i)+"点";
			}else{
				qssj="未知时间";
			}		
			if(!"".equals(jssj)){
				jssj=StringUtils.replace(jssj," ","日");
//				int y=jssj.indexOf(":");
//				jssj=jssj.substring(0,y)+"点";
				
			}else{
				jssj="未知时间";
			}
			
			List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
			
			if(pro!=null&pro.size()>0){
				Long prc=pro.get(0).getPrcDefId();
				dataMap.put("LCBS", prc);
			 }
			String processActDefId = this.getActDefId();
			Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String lx = dataMap.get("BSLX").toString();
			String title = dataMap.get("YHM").toString() +dataMap.get("QSSJ").toString()+lx+ "，特此通知！";
			String departmentname = uc.get_userModel().getDepartmentname();
			if(("step_7b00fa54-661a-a2e7-a9d5-cb4121b3dd73".equals(this.getActStepId())||"step_7df7c25c-f25d-4d0c-2308-27dbdf9fcbdd".equals(this.getActStepId()))&&(!"综合管理部".equals(departmentname))){
			
				ProcessAPI.getInstance().sendProcessCC(this.getActDefId(), this.getActStepId(), this.getTaskId().toString(), instanceId, this.getExcutionId(), title, "ZHAOWEI[赵炜]");
				}
			String mailContent = "";
			if("出差".equals(lx)){
			
			mailContent = "出差:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日到"+dataMap.get("CCMDD").toString()+"出差，请查看";
			}else{
			mailContent="外出:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+dataMap.get("QSSJ").toString()+"日至"+dataMap.get("JSSJ").toString()+"日外出,请查看";
			}
			//发送短信
			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
			String username = "";
			
			
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			
			
			if (target != null) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("") ) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
						
					}
//					String email = target.get_userModel().getEmail();
//    				if(email!=null&&!email.equals("") && uc != null){
//    					username = uc.get_userModel().getUsername();
//    					MessageAPI.getInstance().sendSysMail(username, email, lx, mailContent+"<br>");
//    				}
			}
		}
		
		return flag;
	}
}
