package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 挂牌发起流程提交后触发
 * @author zouyalei
 *
 */
public class zqbGpProAfterRejectEvent extends ProcessStepTriggerEvent {

	public zqbGpProAfterRejectEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser orgUser = uc.get_userModel();
		String userid = orgUser.getUserid();
		String username = orgUser.getUsername();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			dataMap.put("TYPENO", this.getExcutionId());
			dataMap.put("STATUS", "驳回");
			dataMap.put("KHLXR", this.getActDefId());
			dataMap.put("CUSTOMERNO", this.getTaskId());
			// 更新流程表单数据
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(flag){
				//获得目标节点信息
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				String assigneeUserid = newTaskId.getAssignee();
				UserContext target = UserContextUtil.getInstance().getUserContext(assigneeUserid);
				
				HashMap<String,String> contentMap = new HashMap<String,String>();
				contentMap.put("NAME", username);
				contentMap.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
				contentMap.put("XMJD", dataMap.get("XMJD").toString());
				String content = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.GP_REJECT_KEY, contentMap);
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
							MessageAPI.getInstance().sendSysMail(senduser, email, "挂牌流程审核", content.toString());
						}
					}
				}

			}
		}
		return flag;
	}

}
