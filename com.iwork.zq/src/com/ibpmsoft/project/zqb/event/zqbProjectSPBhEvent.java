package com.ibpmsoft.project.zqb.event;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
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

public class zqbProjectSPBhEvent extends ProcessStepTriggerEvent {
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectSPBhEvent(UserContext uc, HashMap hash) {
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
			String sysMsgContent = "";
			String projectNo = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser userModel = uc.get_userModel();
			//归档时回写任务阶段表中的审批状态 字段
			// 1.先查询 2.再更新s
			HashMap<String,String> hashmap = new HashMap<String,String>();
			hashmap.put("PROJECTNO",projectNo);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_UUID,hashmap, null);
			if (list != null && list.size() >0) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				Set<String> keySet = dataMap.keySet();
				for (String key : keySet) {
					if(key.equals("CREATEUSER")||key.equals("CREATEDATE")||key.equals("CREATEUSERID")||key.equals("A02")||key.equals("A03")){
						continue;
					}
					if(key.equals("WBCLRY")){
						map.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}else if(!key.equals("ID")){
						map.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}
				}
				map.put("SPZT", "驳回");
				map.put("STEPID", this.getActStepId());
				map.put("LCBS", this.getExcutionId());
				map.put("TASKID", this.getTaskId());
				flag = DemAPI.getInstance().updateFormData(PROJECT_UUID, Long.parseLong(map.get("INSTANCEID").toString()), map, Long.parseLong(map.get("ID").toString()), false);
				if(flag){
					HashMap<String,String> contentMap=new HashMap<String,String>();
					contentMap.put("PROJECTNAME", projectName);
					contentMap.put("CONTENT", "被驳回");
					smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
					sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
					String user=map.get("OWNER").toString().substring(0,map.get("OWNER").toString().indexOf("["));
					UserContext target = UserContextUtil.getInstance().getUserContext(user);
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
						if (!sysMsgContent.equals("")) {
							String email = targetUserModel.getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,sysMsgContent,"");
							}
						}
					}
					String createuser=dataMap.get("USERID")==null?DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+dataMap.get("CREATEUSER")+"'", "USERID"):dataMap.get("USERID").toString().equals("")?DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+dataMap.get("CREATEUSER")+"'", "USERID"):dataMap.get("USERID").toString();
					UserContext createuserTarget = UserContextUtil.getInstance().getUserContext(createuser);
					String createUserId="";
					if (createuserTarget != null) {
						OrgUser createuserModel = createuserTarget.get_userModel();
						createUserId=createuserModel.getUserid();
						if(!createUserId.equals(targetUserId)){
							if (!sysMsgContent.equals("")) {
								String mobile = createuserModel.getMobile();
								String email = createuserModel.getEmail();
								if (mobile != null && !mobile.equals("")) {
									mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
									MessageAPI.getInstance().sendSMS(uc, mobile, sysMsgContent);
								}
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,sysMsgContent,"");
								}
							}
						}
					}
					String managerUser=map.get("MANAGER").toString().substring(0,map.get("MANAGER").toString().indexOf("["));
					UserContext managerTarget = UserContextUtil.getInstance().getUserContext(managerUser);
					String managerUserId="";
					if (managerTarget != null) {
						OrgUser managerUserModel = managerTarget.get_userModel();
						managerUserId=managerUserModel.getUserid();
						if(!managerUserId.equals(targetUserId)&&!managerUserId.equals(createUserId)){
							if (!sysMsgContent.equals("")) {
								String mobile = managerUserModel.getMobile();
								String email = managerUserModel.getEmail();
								if (mobile != null && !mobile.equals("")) {
									mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
									MessageAPI.getInstance().sendSMS(uc, mobile, sysMsgContent);
								}
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,sysMsgContent,"");
								}
							}
						}
					}
				/*	
					List<OrgUser> processTransUserList = ProcessAPI.getInstance().getProcessTransUserList(instanceId);
					for (OrgUser orgUser : processTransUserList) {
						String userid = orgUser.getUserid();
						if(!userid.equals(managerUserId)&&!userid.equals(targetUserId)&&!userid.equals(createUserId)){
							if (!sysMsgContent.equals("")) {
								String mobile = orgUser.getMobile();
								String email = orgUser.getEmail();
								if (mobile != null && !mobile.equals("")) {
									MessageAPI.getInstance().sendSMS(uc, mobile, sysMsgContent);
								}
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,sysMsgContent,"");
								}
							}
						}
					}*/
				}
			}
		/*	Long dataid = Long.parseLong(dataMap.get("ID").toString());
			dataMap.put("A02",1);
			ProcessAPI.getInstance().updateFormData(this.getActDefId(), instanceId, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);*/
		}
	}

	public void oldMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String smsContent = "";
		String sysMsgContent = "";
		if (dataMap != null) {
			//归档时回写任务阶段表中的审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			hashmap.put("PROJECTNO", dataMap.get("PROJECTNO"));
			List<HashMap> list = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1",
					hashmap, null);
			if (list != null && list.size() >0) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				HashMap hash=new HashMap();
				hash.put("PROJECTNO", map.get("PROJECTNO"));
				List<HashMap> list2 = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", hash, null);
				HashMap hashMap2 = list2.get(0);
				hashMap2.put("SPZT", "驳回");
				hashMap2.put("LCBS", this.getExcutionId());
				hashMap2.put("TASKID", this.getTaskId());
				flag = DemAPI.getInstance().updateFormData("33833384d109463285a6a348813539f1", Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
				if(flag){
					HashMap contentMap=new HashMap();
					String projectname=map.get("PROJECTNAME").toString();
					contentMap.put("PROJECTNAME", projectname);
					contentMap.put("CONTENT", "被驳回");
					smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, map);
					sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(
							ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
					String user=map.get("OWNER").toString().substring(0, 
							map.get("OWNER").toString().indexOf("["));
					UserContext target = UserContextUtil.getInstance().getUserContext(
							user);
					if (target != null) {
						if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
							}
						}
						if (!sysMsgContent.equals("")) {
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectname,
										sysMsgContent,"");
							}
						}
					}
					String createuser=DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+map.get("CREATEUSER")+"'", "USERID");
					UserContext createuserTarget = UserContextUtil.getInstance().getUserContext(
							createuser);
					if(!createuserTarget.get_userModel().getUserid().equals(target.get_userModel().getUserid())){
						if (createuserTarget != null) {
							if (!sysMsgContent.equals("")) {
								String mobile = createuserTarget.get_userModel().getMobile();
								String email = createuserTarget.get_userModel().getEmail();
								if (mobile != null && !mobile.equals("")) {
									mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
									MessageAPI.getInstance().sendSMS(uc, mobile, sysMsgContent);
								}
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectname,
											sysMsgContent,"");
								}
							}
						}
					}
					String managerUser=map.get("MANAGER").toString().substring(0, 
							map.get("MANAGER").toString().indexOf("["));
					UserContext managerTarget = UserContextUtil.getInstance().getUserContext(
							managerUser);
					if(!managerTarget.get_userModel().getUserid().equals(target.get_userModel().getUserid())&&!managerTarget.get_userModel().getUserid().equals(createuserTarget.get_userModel().getUserid())){
						if (managerTarget != null) {
							if (!sysMsgContent.equals("")) {
								String mobile = managerTarget.get_userModel().getMobile();
								String email = managerTarget.get_userModel().getEmail();
								if (mobile != null && !mobile.equals("")) {
									mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
									MessageAPI.getInstance().sendSMS(uc, mobile, sysMsgContent);
								}
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectname,
											sysMsgContent,"");
								}
							}
						}
					}
				}
			}
		}
	}
}
