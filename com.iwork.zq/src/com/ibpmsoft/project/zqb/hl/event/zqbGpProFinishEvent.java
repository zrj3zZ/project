package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;
import java.util.Map;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**归档时触发
 * @author admin
 *
 */
public class zqbGpProFinishEvent extends ProcessTriggerEvent {
	public zqbGpProFinishEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			dataMap.put("STATUS", "审批通过");
			// 更新流程表单数据
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(flag){
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				
				HashMap<String,String> contentMap = new HashMap<String,String>();
				contentMap.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
				contentMap.put("XMJD", dataMap.get("XMJD").toString());
				String content = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.GP_END_KEY, contentMap);
				
				//获得目标节点信息
				Map params = new HashMap();
				params.put(1, dataMap.get("PROJECTNO"));
				String targetUserid = com.iwork.commons.util.DBUtil.getDataStr("CREATEUSERID","SELECT * FROM BD_ZQB_PJ_BASE WHERE PROJECTNO = ?",params);
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
							MessageAPI.getInstance().sendSysMail(senduser, email, "挂牌流程审核", content.toString());
						}
					}
				}

			}
		}
		return flag;
	}
}

