package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbZtNbcbfileSaveEvent extends ProcessTriggerEvent{
	private static final String CN_FILENAME = "/common.properties"; // 抓取网站配置文件
	public zqbZtNbcbfileSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	public String getConfigUUID(String parameter) {
		String config = ConfigUtil.readValue(CN_FILENAME, parameter);
		return config;
	}

	@SuppressWarnings("unchecked")
	public boolean execute() {

		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId,
				EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			// 获取当前日期时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String nowdatetime = df.format(new Date());
			// 获得当前办理人id

			dataMap.put("NODE9SFKF", "审批通过");
			
			dataMap.put("NODE2SFKF", this.getActDefId());
			dataMap.put("NODE5SFKF", this.getExcutionId()+"");
			dataMap.put("NODE4SFKF", this.getTaskId()+"");

			List<ProcessRuOpinion> pro = ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(),
					this.getExcutionId());
			if (pro != null & pro.size() > 0) {
				Long prc = pro.get(0).getPrcDefId();
				dataMap.put("NODE3SFKF", prc);
			}
			String processActDefId = this.getActDefId();
			Long lcDataId = Long.parseLong(dataMap.get("ID").toString());
//			String DYZLNR = dataMap.get("DYZLNR").toString();
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

			
			if(flag){
				
				String damc=dataMap.get("NODE1")==null?"":dataMap.get("NODE1").toString();
				String dm=dataMap.get("NODE2")==null?"":dataMap.get("NODE2").toString();
				String content="您的内部呈报："+damc+"--"+dm+"已通过审核";
				
				
				//获得目标节点信息
//				Map params = new HashMap();
//				params.put(1, dataMap.get("YHID"));
				String targetUserid = dataMap.get("NODE7SFKF").toString();
				UserContext target = UserContextUtil.getInstance().getUserContext(targetUserid);
				if (target != null) {
					if (!content.equals("")) {
						//发送短信
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile();
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
		return flag;
	}

}
