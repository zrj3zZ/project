package com.ibpmsoft.project.zqb.event;


import java.util.HashMap;
import java.util.List;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectGpfxfpbmEndSaveEvent extends ProcessTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectGpfxfpbmEndSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		return newMethod();
	}
	
	public boolean newMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			String actDefId = this.getActDefId();
			Long taskId = this.getTaskId();
			String projectNo = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String bgczuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("PROJECTNO",projectNo);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(bgczuuid, conditionMap, null);
			HashMap<String, Object> hash = null;
			for (int i = 0; i < lcbsexists.size(); i++) {
				if(lcbsexists.get(i).get("PROJECTNO").toString().equals(projectNo)){
					hash = lcbsexists.get(i);
				}
			}
			if (hash != null) {
					hash.put("LCBH", actDefId);
					hash.put("LCBS", instanceId);
					hash.put("TASKID", taskId);
					hash.put("SPZT", "审批通过");
					flag = DemAPI.getInstance().updateFormData(bgczuuid,Long.parseLong(hash.get("INSTANCEID").toString()),hash, Long.parseLong(hash.get("ID").toString()),false);
					String smsContent = "";
					if(flag){
						smsContent = "股票发行项目:"+projectName+",分配部门信息审批通过,请查看!";
						String user=dataMap.get("CREATEUSERID").toString();
						UserContext target = UserContextUtil.getInstance().getUserContext(user);
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
		return flag;
	}

}
