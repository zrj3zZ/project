package com.ibpmsoft.project.zqb.event;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbGPFXProjectBhEvent extends ProcessStepTriggerEvent {

	public zqbGPFXProjectBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String PROJECT_TASK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		if (dataMap != null) {
			//归档时回写任务阶段表中的审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			hashmap.put("LCBS", this.getInstanceId());
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_TASK_UUID,
					hashmap, null);
			if (list != null && list.size() >0) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				map.put("SPZT", "驳回");
//				map.put("STEPID", this.getActStepId());
				map.put("YXID", this.getExcutionId());
				map.put("RWID", this.getTaskId());
				Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(map.get("ID").toString());
				flag=DemAPI.getInstance().updateFormData(PROJECT_TASK_UUID, instanceid, map, dataid, true);
			}
			BigDecimal xmjd = dataMap.get("GROUPID") == null ? new BigDecimal(0):new BigDecimal(dataMap.get("GROUPID").toString());
			String jdmc=DBUtil.getString("SELECT JDMC FROM BD_ZQB_TYXM_INFO WHERE ID="+xmjd, "JDMC");
			HashMap<String,String> contentMap = new HashMap<String,String>();
			contentMap.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
			contentMap.put("JDMC",jdmc);
			String smsContent="";
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMJDBH_ADD_KEY, contentMap);
			String userid = UserContextUtil.getInstance().getUserId(dataMap.get("MANAGER").toString());
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			if (target != null) {
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				String email = target.get_userModel().getEmail();
				if (email != null && !email.equals("")) {
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "项目任务",
							smsContent,"");
				}
			}
		}

		return flag;
	}

}
