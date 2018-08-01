package com.ibpmsoft.project.zqb.event.sxgpfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.EventUtil;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class ZqbSXGpfxnfxEndEvent extends ProcessTriggerEvent {
	public ZqbSXGpfxnfxEndEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
			Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
			List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMNFXB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='拟发行信息') AND B.PROJECTNO=?", params);
			
			String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '拟发行信息'", null);
			HashMap h = DemAPI.getInstance().getFromData(Long.parseLong(l.get(0).get("INSTANCEID").toString()));
			
			h.put("SPZT", "审批通过");
			h.put("SCORE",dataMap.get("SCORE"));
			h.put("NFXRQ",dataMap.get("NFXRQ"));
			flag = DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
			
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			StringBuffer content=new StringBuffer();
			String projectname = dataMap.get("PROJECTNAME").toString();
			content.append("股票发行项目:").append(projectname).append(",拟发行信息已审批通过!");
			
			UserContext target = UserContextUtil.getInstance().getUserContext(dataMap.get("CREATEUSERID").toString());
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance().sendSMS(uc, mobile,content.toString());
				}
				String email = target.get_userModel().getEmail();
				if (email != null && !email.equals("")) {
					MessageAPI.getInstance().sendSysMail(senduser,email, "拟发行信息审批", content.toString());
				}
			}
		}
		EventUtil.updateMainData(dataMap);
		return flag;
	}
}
