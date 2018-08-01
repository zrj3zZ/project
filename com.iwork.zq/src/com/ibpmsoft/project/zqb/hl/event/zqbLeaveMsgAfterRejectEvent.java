package com.ibpmsoft.project.zqb.hl.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 请假流程发起流程提交后触发
 * @author zouyalei
 *
 */
public class zqbLeaveMsgAfterRejectEvent extends ProcessStepTriggerEvent {

	public zqbLeaveMsgAfterRejectEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			dataMap.put("LCBH", this.getExcutionId());
			dataMap.put("LZJD", this.getActStepId());
			dataMap.put("SPZT", "驳回");
			dataMap.put("LCBS", this.getActDefId());
			dataMap.put("RWID", this.getTaskId());
			// 更新流程表单数据
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(flag){
				//获得目标节点信息
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				String assigneeUserid = newTaskId.getAssignee();
				UserContext target = UserContextUtil.getInstance().getUserContext(assigneeUserid);
				
				HashMap<String,String> contentMap = new HashMap<String,String>();
				
				String startdate = dataMap.get("STARTDATE").toString()+" "+(dataMap.get("SCALE")==null?"0":dataMap.get("SCALE").toString());
				String enddate = dataMap.get("ENDDATE").toString()+" "+(dataMap.get("GROUPID")==null?"0":dataMap.get("GROUPID").toString());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
				SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy年MM月dd日HH时");
				try {
					Date s = sdf.parse(startdate);
					Date e = sdf.parse(enddate);
					startdate = sdf_.format(s);
					enddate = sdf_.format(e);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				contentMap.put("STARTDATE", startdate);
				contentMap.put("ENDDATE", enddate);
				String content ="请假:您因"+dataMap.get("MEMO").toString()+"申请"+dataMap.get("STARTDATE").toString()+"日至"+dataMap.get("ENDDATE").toString()+"日请假，被驳回";
				UserContext user = UserContextUtil.getInstance().getUserContext("ZHAOWEI");
				String userm=user.get_userModel().getMobile();
				if (target != null) {
					if (!content.equals("")) {
						UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
						//发送短信
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
							MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
							
						}
						content="请假:"+ dataMap.get("PROJECTNAME").toString()+"因"+dataMap.get("MEMO").toString()+"申请"+dataMap.get("STARTDATE").toString()+"日至"+dataMap.get("ENDDATE").toString()+"日请假，被驳回";
						if (userm != null && !"".equals(userm)) {
						MessageAPI.getInstance().sendSMS(uc, userm, content.toString());
						}
						//发送邮件
						/*String email = target.get_userModel().getEmail();
						if (email != null && !email.equals("")) {
							String senduser = target.get_userModel().getUsername();
							MessageAPI.getInstance().sendSysMail(senduser, email, "请假流程审核", content.toString());
						}*/
					}
				}

			}
		}
		return flag;
	}

}
