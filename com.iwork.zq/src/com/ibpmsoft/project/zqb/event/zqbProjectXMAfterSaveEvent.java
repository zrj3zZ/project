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
public class zqbProjectXMAfterSaveEvent extends ProcessTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectXMAfterSaveEvent(UserContext uc, HashMap hash) {
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
			OrgUser userModel = uc.get_userModel();
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("PROJECTNO",projectNo);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(PROJECT_UUID, conditionMap, null);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					Long dataInstanceid=Long.parseLong(hash.get("INSTANCEID").toString());
					Set<String> keySet = dataMap.keySet();
					for (String key : keySet) {
						if(key.equals("CREATEUSER")||key.equals("CREATEDATE")||key.equals("CREATEUSERID")||key.equals("A02")||key.equals("A03")){
							continue;
						}
						if(key.equals("WBCLRY")){
							hash.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
						}else if(!key.equals("ID")){
							hash.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
						}
					}
					hash.put("LCBH", actDefId);
					hash.put("LCBS", instanceId);
					hash.put("TASKID", taskId==0?instanceId+7:taskId);
					hash.put("SPZT", "审批通过");
					flag = DemAPI.getInstance().updateFormData(PROJECT_UUID,Long.parseLong(hash.get("INSTANCEID").toString()),hash, Long.parseLong(hash.get("ID").toString()),false);
					String smsContent = "";
					String sysMsgContent = "";
					if(flag){
						HashMap<String,String> contentMap=new HashMap<String,String>();
						contentMap.put("PROJECTNAME", projectName);
						contentMap.put("CONTENT", "审批通过");
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
						sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
						String user=hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("["));
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
							OrgUser createUserModel = createuserTarget.get_userModel();
							createUserId = createUserModel.getUserid();
							if(!createUserId.equals(targetUserId)){
								if (!sysMsgContent.equals("")) {
									String mobile = createUserModel.getMobile();
									String email = createUserModel.getEmail();
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
						String managerUser=hash.get("MANAGER").toString().substring(0,hash.get("MANAGER").toString().indexOf("["));
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
						//获取流程数据子表并向物理数据子表中插入数据
						List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
						/*List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
						List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");*/
						List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");
	
						Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
						
						List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMCYLB");
						setFromSubData(demId,fromSubXmcyData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMCYLB",lcFromSubXmcyData);
						/*List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
						setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
						List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
						setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);*/
						List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMZJJG");
						setFromSubData(demId,fromSubZjjgData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMZJJG",lcFromSubZjjgData);
					}
				}
			} else {
				// 保存到项目任务数据表中
				Long instanceid = DemAPI.getInstance().newInstance(PROJECT_UUID, userModel.getUserid());
				HashMap<String, Object> hashdata = new HashMap<String, Object>();
				Set<String> keySet = dataMap.keySet();
				for (String key : keySet) {
					if(key.equals("CREATEUSER")||key.equals("CREATEDATE")||key.equals("CREATEUSERID")||key.equals("A02")||key.equals("A03")){
						continue;
					}
					if(key.equals("WBCLRY")){
						hashdata.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}else if(!key.equals("ID")){
						hashdata.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}
				}
				hashdata.put("LCBH", actDefId);
				hashdata.put("LCBS", instanceId);
				hashdata.put("TASKID", taskId==0?instanceId+7:taskId);
				flag = DemAPI.getInstance().saveFormData(PROJECT_UUID,instanceid, hashdata, false);
			}
		}
		return flag;
	}
	
	public boolean oldMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//instanceId=99031&excutionId=99031&taskId=99038
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap = new HashMap();
			conditionMap.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(
					PROJECT_UUID, conditionMap, null);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					hash.put("KHLXDH", dataMap.get("KHLXDH") == null ? ""
							: dataMap.get("KHLXDH").toString());
					hash.put("OWNER", dataMap.get("OWNER") == null ? ""
							: dataMap.get("OWNER").toString());
					hash.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
							.get("MEMO").toString());
					hash.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
							: dataMap.get("STARTDATE").toString());
					hash.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? "" : dataMap
							.get("PROJECTNO").toString());
					hash.put("XMJD", dataMap.get("XMJD") == null ? ""
							: dataMap.get("XMJD").toString());
					hash.put("PROJECTNAME", dataMap.get("PROJECTNAME") == null ? ""
							: dataMap.get("PROJECTNAME").toString());
					hash.put("SBJZR", dataMap.get("SBJZR") == null ? ""
							: dataMap.get("SBJZR").toString());
					hash.put("KHLXR", dataMap.get("KHLXR") == null ? "" : dataMap
							.get("KHLXR").toString());
					hash.put(
							"XMBZ",
							dataMap.get("XMBZ") == null ? "" : dataMap.get("XMBZ"));
					hash.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER"));
					hash.put("STATUS", dataMap.get("STATUS")==null?"":dataMap.get("STATUS"));
					hash.put("WBCLRYJG", dataMap.get("WBCLRYJG")==null?"":dataMap.get("WBCLRYJG"));
					hash.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER"));
					hash.put("CUSTOMERINFO", dataMap.get("CUSTOMERINFO")==null?"":dataMap.get("CUSTOMERINFO"));
					hash.put("FZJGLXR", dataMap.get("FZJGLXR")==null?"":dataMap.get("FZJGLXR"));
					hash.put("CUSTOMERNO", dataMap.get("CUSTOMERNO")==null?"":dataMap.get("CUSTOMERNO"));
					hash.put("YJZXYNJLR", dataMap.get("YJZXYNJLR")==null?"":dataMap.get("YJZXYNJLR"));
					hash.put("QRDG", dataMap.get("QRDG")==null?"":dataMap.get("QRDG"));
					hash.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE"));
					hash.put("ZCLR", dataMap.get("ZCLR")==null?"":dataMap.get("ZCLR"));
					hash.put("SFXZCL", dataMap.get("SFXZCL")==null?"":dataMap.get("SFXZCL"));
					hash.put("GGJZR", dataMap.get("GGJZR")==null?"":dataMap.get("GGJZR"));
					hash.put("CUSTOMERNAME", dataMap.get("CUSTOMERNAME")==null?"":dataMap.get("CUSTOMERNAME"));
					hash.put("FZJGMC", dataMap.get("FZJGMC")==null?"":dataMap.get("FZJGMC"));
					hash.put("ZCLRDH", dataMap.get("ZCLRDH")==null?"":dataMap.get("ZCLRDH"));
					hash.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE"));
					hash.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE"));
					hash.put("LCBH", this.getActDefId());
					hash.put("LCBS", instanceId);
					hash.put("SFTXCL", dataMap.get("SFTXCL"));
					hash.put("SSSYB", dataMap.get("SSSYB"));
					hash.put("XMZRY", dataMap.get("XMZRY"));
					hash.put("TASKID", this.getTaskId()==0?instanceId+7:this.getTaskId());
					hash.put("SPZT", "审批通过");
					hash.put("CLSLR", dataMap.get("CLSLR") == null ? ""
							: dataMap.get("CLSLR").toString());
					hash.put("SHTGR", dataMap.get("SHTGR") == null ? ""
							: dataMap.get("SHTGR").toString());
					flag = DemAPI.getInstance().updateFormData(
							PROJECT_UUID,
							Long.parseLong(hash.get("INSTANCEID").toString()),
							hash, Long.parseLong(hash.get("ID").toString()),
							false);
					String smsContent = "";
					String sysMsgContent = "";
					if(flag){
						HashMap contentMap=new HashMap();
						String projectname=hash.get("PROJECTNAME").toString();
						contentMap.put("PROJECTNAME", projectname);
						contentMap.put("CONTENT", "审批通过");
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
						sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(
								ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
						String user=hash.get("OWNER").toString().substring(0, 
								hash.get("OWNER").toString().indexOf("["));
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
						String createuser=DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+hash.get("CREATEUSER")+"'", "USERID");
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
						String managerUser=hash.get("MANAGER").toString().substring(0, 
								hash.get("MANAGER").toString().indexOf("["));
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
					List<HashMap> fromSubData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
					DemAPI.getInstance().saveFormDatas(PROJECT_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), "SUBFORM_XMCYLB", fromSubData, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				}
			} else {
				// 保存到项目任务数据表中
				Long instanceid = DemAPI.getInstance().newInstance(
						PROJECT_UUID, uc.get_userModel().getUserid());
				HashMap<String, Object> hashdata = new HashMap<String, Object>();
				hashdata.put("KHLXDH", dataMap.get("KHLXDH") == null ? ""
						: dataMap.get("KHLXDH").toString());
				hashdata.put("OWNER", dataMap.get("OWNER") == null ? ""
						: dataMap.get("OWNER").toString());
				hashdata.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
						.get("MEMO").toString());
				hashdata.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
						: dataMap.get("STARTDATE").toString());
				hashdata.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? "" : dataMap
						.get("PROJECTNO").toString());
				hashdata.put("XMJD", dataMap.get("XMJD") == null ? ""
						: dataMap.get("XMJD").toString());
				hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME") == null ? ""
						: dataMap.get("PROJECTNAME").toString());
				hashdata.put("SBJZR", dataMap.get("SBJZR") == null ? ""
						: dataMap.get("SBJZR").toString());
				hashdata.put("KHLXR", dataMap.get("KHLXR") == null ? "" : dataMap
						.get("KHLXR").toString());
				hashdata.put(
						"XMBZ",
						dataMap.get("XMBZ") == null ? "" : dataMap.get("XMBZ"));
				hashdata.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER"));
				hashdata.put("STATUS", dataMap.get("STATUS")==null?"":dataMap.get("STATUS"));
				hashdata.put("WBCLRYJG", dataMap.get("WBCLRYJG")==null?"":dataMap.get("WBCLRYJG"));
				hashdata.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER"));
				hashdata.put("CUSTOMERINFO", dataMap.get("CUSTOMERINFO")==null?"":dataMap.get("CUSTOMERINFO"));
				hashdata.put("FZJGLXR", dataMap.get("FZJGLXR")==null?"":dataMap.get("FZJGLXR"));
				hashdata.put("CUSTOMERNO", dataMap.get("CUSTOMERNO")==null?"":dataMap.get("CUSTOMERNO"));
				hashdata.put("YJZXYNJLR", dataMap.get("YJZXYNJLR")==null?"":dataMap.get("YJZXYNJLR"));
				hashdata.put("QRDG", dataMap.get("QRDG")==null?"":dataMap.get("QRDG"));
				hashdata.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE"));
				hashdata.put("ZCLR", dataMap.get("ZCLR")==null?"":dataMap.get("ZCLR"));
				hashdata.put("SFXZCL", dataMap.get("SFXZCL")==null?"":dataMap.get("SFXZCL"));
				hashdata.put("GGJZR", dataMap.get("GGJZR")==null?"":dataMap.get("GGJZR"));
				hashdata.put("CUSTOMERNAME", dataMap.get("CUSTOMERNAME")==null?"":dataMap.get("CUSTOMERNAME"));
				hashdata.put("FZJGMC", dataMap.get("FZJGMC")==null?"":dataMap.get("FZJGMC"));
				hashdata.put("ZCLRDH", dataMap.get("ZCLRDH")==null?"":dataMap.get("ZCLRDH"));
				hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE"));
				hashdata.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE"));
				hashdata.put("LCBH", this.getActDefId());
				hashdata.put("LCBS", instanceId);
				hashdata.put("CLSLR", dataMap.get("CLSLR") == null ? ""
						: dataMap.get("CLSLR").toString());
				hashdata.put("SHTGR", dataMap.get("SHTGR") == null ? ""
						: dataMap.get("SHTGR").toString());
				hashdata.put("SFTXCL", dataMap.get("SFTXCL"));
				hashdata.put("SSSYB", dataMap.get("SSSYB") == null ? "" : dataMap.get("SSSYB").toString());
				hashdata.put("XMZRY", dataMap.get("XMZRY") == null ? "" : dataMap.get("XMZRY").toString());
				hashdata.put("TASKID", this.getTaskId()==0?instanceId+7:this.getTaskId());
				flag = DemAPI.getInstance().saveFormData(PROJECT_UUID,
						instanceid, hashdata, false);
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param demId 
	 * @param getFromSubData 物理表中子表信息的集合
	 * @param demUUID
	 * @param dataInstanceid
	 * @param subFromKey 子表名
	 * @param saveFromSubData 需要更新到物理子表中的集合
	 */
	private void setFromSubData(Long demId, List<HashMap> getFromSubData,
			String demUUID, Long dataInstanceid, String subFromKey,
			List<HashMap> saveFromSubData) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					DemAPI.getInstance().removeSubFormData(PROJECT_UUID, dataInstanceid, Long.parseLong(hashMap.get("ID").toString()), subFromKey);
				}
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}else{
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}
		}else{
			if(saveFromSubData!=null){
				DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
			}
		}
	}

}
