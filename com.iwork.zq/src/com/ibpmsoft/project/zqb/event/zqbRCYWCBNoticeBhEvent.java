package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbRCYWCBNoticeBhEvent extends ProcessStepTriggerEvent {

	private static final String TEMP_NOTICE_UUID = "a12564b4497a44cbaaae73e8fe2f7cac";    //日常业务呈报明细主数据
	public zqbRCYWCBNoticeBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			String sq=dataMap.get("KHBH").toString();
			if("".equals(sq)||sq==null){
				return true;
			}
			hashmap.put("NOTICESQ", sq);
			List<HashMap> list = DemAPI.getInstance().getList(TEMP_NOTICE_UUID,hashmap, null);
			if (!list.isEmpty()) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				map.put("SPZT", "驳回");
				map.put("YXID", this.getExcutionId());
				map.put("RWID", this.getTaskId());
				Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(map.get("ID").toString());
				flag=DemAPI.getInstance().updateFormData(TEMP_NOTICE_UUID, instanceid, map, dataid, true);
				String value=map.get("NOTICENAME")==null?"":map.get("NOTICENAME").toString();
    			Long dataId=Long.parseLong(map.get("ID").toString());
    			LogUtil.getInstance().addLog(dataId, "日常业务呈报管理", "驳回公告："+value);
				String smsContent = "";
				String mailContent = "";
				String senduser = "";
				String noticename = (dataMap.get("NOTICENAME").toString()==null||dataMap.get("NOTICENAME").equals(""))?"公告标题：空。":"公告标题："+dataMap.get("NOTICENAME").toString()+"。";
				String ggzy = (dataMap.get("GGZY").toString()==null||dataMap.get("GGZY").equals(""))?"日常业务呈报摘要：空。":"日常业务呈报摘要："+dataMap.get("GGZY").toString()+"。";
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.GGBH_ADD_KEY, map);
				/*String userid = DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+map.get("CREATENAME").toString()+"'", "USERID");*/
				String userid = dataMap.get("QCRID").toString();
				mailContent = dataMap.get("KHMC").toString() + "的日常业务呈报："+dataMap.get("NOTICENAME").toString()+"，被驳回！";
				UserContext target = UserContextUtil.getInstance().getUserContext(userid);
				UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
				if (target != null && tg != null) {
					senduser = tg.get_userModel().getUsername();
					if (!smsContent.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
							MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
						}
						String email = target.get_userModel().getEmail();
						if(email != null && !email.equals("")){
							MessageAPI.getInstance().sendSysMail(senduser, email, "公告驳回", mailContent+"<br>"+ggzy);
						}
					}
				}
			}
		}
		return true;
	}
	

}

