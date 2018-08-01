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

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectGpfxfpbmAfterCommitEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectGpfxfpbmAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		newMethod();
		return true;
	}
	
	public void newMethod(){
		boolean flag = false; 
		Long instanceId = this.getInstanceId();
		String newTaskId = ProcessAPI.getInstance().newTaskId(instanceId).getId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			String bgczuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
			String smsContent = "";
			String projectno = dataMap.get("PROJECTNO").toString();
			String attach = dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString();
			String fpbm = dataMap.get("FPBM")==null?"":dataMap.get("FPBM").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			
			String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行分配部门审批人'", "UUID");
			List<HashMap> alldata = DemAPI.getInstance().getAllList(uuid, null, null);
			String ownerId = "";
			if(alldata.size()==1){
				ownerId = alldata.get(0).get("USERNAME").toString().substring(0,alldata.get(0).get("USERNAME").toString().indexOf("["));
			}
			
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("PROJECTNO", projectno);
			List<HashMap> list2 = DemAPI.getInstance().getList(bgczuuid, map, "ID");
			HashMap hashMap2 = null;
			for (int i = 0; i < list2.size(); i++) {
				if(list2.get(i).get("PROJECTNO").toString().equals(projectno)){
					hashMap2 = list2.get(i);
				}
			}
			Long dataInstanceid=Long.parseLong(hashMap2.get("INSTANCEID").toString());
			hashMap2.put("SPZT", "审批中");
			hashMap2.put("ATTACH", attach);
			hashMap2.put("FPBM", fpbm);
			DemAPI.getInstance().updateFormData(bgczuuid, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
			smsContent = "股票发行项目:"+projectName+",分配部门信息已呈报，请您审核！";
			UserContext target = UserContextUtil.getInstance().getUserContext(ownerId);
			if (target != null) {
				OrgUser targetUserModel = target.get_userModel();
				if (!smsContent.equals("")) {
					String mobile = targetUserModel.getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
					String email = targetUserModel.getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,smsContent,"");
					}
				}
			}
			
		}
	}
}
