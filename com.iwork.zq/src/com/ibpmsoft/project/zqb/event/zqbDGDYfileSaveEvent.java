package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbDGDYfileSaveEvent extends ProcessTriggerEvent {
	public zqbDGDYfileSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
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
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			//获取当前日期时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String  nowdatetime = df.format(new Date());
			//获得当前办理人id
		
				
					
					
					
			dataMap.put("SPZT", "已归档");
			//dataMap.put("GHSJ", nowdatetime);
			dataMap.put("LCBH", this.getActDefId());
			dataMap.put("LZWZ",this.getExcutionId());
			dataMap.put("RWID", this.getTaskId());
					
			List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
			if(pro!=null&pro.size()>0){
				Long prc=pro.get(0).getPrcDefId();
				dataMap.put("LCBS", prc);
			 }
			String processActDefId = this.getActDefId();
			Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
			String DYZLNR=dataMap.get("DYZLNR").toString();
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					
		
			LogUtil.getInstance().addLog(lcDataId, "工作底稿调阅", "流程："+dataMap.get("DYZLNR").toString()+"---已审核完成！");
			String smsContent = "";
			String mailContent = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("DYZLNR",dataMap.get("DYZLNR") == null ? "" : dataMap.get("DYZLNR"));
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ANN_ADD_KEY, map);
			//编辑短信内容
			mailContent =dataMap.get("DYZLNR").toString()+"的流程，已审核通过！";
			//发送短信
			String assignee = dataMap.get("DYRID").toString();
			String username = "";
			String noticename = (dataMap.get("DYZLNR").toString()==null||dataMap.get("DYZLNR").equals(""))?"项目名称：空。":"项目名称："+dataMap.get("DYZLNR").toString()+"。";
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			if (target != null) {
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("") ) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
					}
					String email = target.get_userModel().getEmail();
    				if(email!=null&&!email.equals("") && uc != null){
    					username = uc.get_userModel().getUsername();
    					MessageAPI.getInstance().sendSysMail(username, email, "工作底稿调阅审核通过", mailContent+"<br>"+noticename);
    				}
				}
			}
		}
		return flag;
	}

}
