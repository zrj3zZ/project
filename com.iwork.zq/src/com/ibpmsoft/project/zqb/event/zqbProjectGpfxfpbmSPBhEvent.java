package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbProjectGpfxfpbmSPBhEvent extends ProcessStepTriggerEvent {
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectGpfxfpbmSPBhEvent(UserContext uc, HashMap hash) {
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
			String projectName = dataMap.get("JYF").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String bgczuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '并购重组立项信息'", "UUID");
			// 1.先查询 2.再更新s
			HashMap<String,String> hashmap = new HashMap<String,String>();
			hashmap.put("PROJECTNO",projectNo);
			List<HashMap> list = DemAPI.getInstance().getList(bgczuuid,hashmap, null);
			HashMap map = null;
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).get("PROJECTNO").toString().equals(projectNo)){
					map = list.get(i);
				}
			}
			if (map != null) {
				//回写审批状态 字段
				map.put("ZBSPZT", "驳回");
				map.put("ZBSTEPID", this.getActStepId());
				map.put("ZBLCBS", this.getExcutionId());
				map.put("ZBTASKID", this.getTaskId());
				flag = DemAPI.getInstance().updateFormData(bgczuuid, Long.parseLong(map.get("INSTANCEID").toString()), map, Long.parseLong(map.get("ID").toString()), false);
				if(flag){
					smsContent = "股票发行项目:"+projectName+",承揽个人信息呈报被部门负责人驳回,请查看!";
					String userId=dataMap.get("TBRID").toString().substring(0,dataMap.get("TBRID").toString().indexOf("["));
					UserContext target = UserContextUtil.getInstance().getUserContext(userId);
					String targetUserId=null;
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
