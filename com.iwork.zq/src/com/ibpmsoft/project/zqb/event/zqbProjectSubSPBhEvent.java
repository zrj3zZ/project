package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbProjectSubSPBhEvent extends ProcessStepTriggerEvent {
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectSubSPBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		newMethod();
		return true;
	}
	
	public void newMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			String smsContent = "";
			String projectNo = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 1.先查询 2.再更新s
			HashMap<String,String> hashmap = new HashMap<String,String>();
			hashmap.put("PROJECTNO",projectNo);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_UUID,hashmap, null);
			if (list != null && list.size() >0) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				map.put("ZBSPZT", "驳回");
				map.put("ZBSTEPID", this.getActStepId());
				map.put("ZBLCBS", this.getExcutionId());
				map.put("ZBTASKID", this.getTaskId());
				flag = DemAPI.getInstance().updateFormData(PROJECT_UUID, Long.parseLong(map.get("INSTANCEID").toString()), map, Long.parseLong(map.get("ID").toString()), false);
				if(flag){
					HashMap<String,String> contentMap=new HashMap<String,String>();
					contentMap.put("PROJECTNAME", projectName);
					contentMap.put("CONTENT", "分配部门与承揽个人信息呈报被部门负责人驳回");
					smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
					String userId=dataMap.get("CREATEUSERID").toString();
					UserContext target = UserContextUtil.getInstance().getUserContext(userId);
					String targetUserId="";
					if (target != null) {
						OrgUser targetUserModel = target.get_userModel();
						targetUserId=targetUserModel.getUserid();
						if (!smsContent.equals("")) {
							String mobile = targetUserModel.getMobile();
							if (mobile != null && !mobile.equals("")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
							}
						}
						if (!smsContent.equals("")) {
							String email = targetUserModel.getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,smsContent,"");
							}
						}
					}
				}
			}
		}
	}
}
