package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.UtilDate;
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
public class zqbProjectAfterCommitEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件

	public zqbProjectAfterCommitEvent(UserContext uc, HashMap hash) {
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
			String actStepId = this.getActStepId();
			String projectNo = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			String owner = dataMap.get("OWNER").toString();
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser userModel = uc._userModel;
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			String nhzkUUID = config.get("nhzkuuid");
			Long dataInstanceid=0L;
			boolean isZkrEqualsCurrentuserid = false;
			String zkr = DBUtil.getString("SELECT SUBSTR(ZKR,0, INSTR(ZKR,'[',1)-1) ZKR FROM BD_ZQB_ZKSPRQ WHERE PROJECTNO='"+projectNo+"'", "ZKR");
			String currentuserid = userModel.getUserid();
			if(zkr.equals(currentuserid)){isZkrEqualsCurrentuserid=true;}
			String nhspr = DBUtil.getString("SELECT SUBSTR(NHSPR,0, INSTR(NHSPR,'[',1)-1) NHSPR FROM BD_ZQB_NHSPRQ WHERE PROJECTNO='"+projectNo+"'", "NHSPR");
			
			List<HashMap> list = DemAPI.getInstance().getList(nhzkUUID,conditionMap, null);
			if (list.size() == 1) {
				if (list.get(0).get("ZKR") != null&& !list.get(0).get("ZKR").toString().equals("")&&SystemConfig._xmsplcConf.getJd1().equals(actStepId)) {
					String zkUUID = config.get("zkuuid");
					HashMap<String,Object> map = new HashMap<String,Object>();
					map.put("PROJECTNO", projectNo);
					List<HashMap> zkList = DemAPI.getInstance().getList(zkUUID, map, null);
					if(zkList.size()==1){
						HashMap zkMap = zkList.get(0);
						zkMap.put("CBSJ", UtilDate.getNowDatetime());
						DemAPI.getInstance().updateFormData(zkUUID, Long.parseLong(zkMap.get("INSTANCEID").toString()), zkMap,Long.parseLong(zkMap.get("ID").toString()), false);
					}else{
						map.clear();
						Long instanceid = DemAPI.getInstance().newInstance(zkUUID,userModel.getUserid());
						map.put("instanceId", instanceid);
						map.put("formid", config.get("zkformid"));
						map.put("PROJECTNO", projectNo);
						map.put("PROJECTNAME", projectName);
						map.put("OWNER", owner);
						map.put("CBSJ", UtilDate.getNowDatetime());
						map.put("ZKR", list.get(0).get("ZKR").toString());
						DemAPI.getInstance().saveFormData(zkUUID, instanceid, map,false);
					}
					map.clear();
					map.put("PROJECTNO",projectNo);
					List<HashMap> list2 = DemAPI.getInstance().getList(PROJECT_UUID, map, null);
					HashMap hashMap2 = list2.get(0);
					dataInstanceid=Long.parseLong(hashMap2.get("INSTANCEID").toString());
					hashMap2.put("SPZT", "质控审批");
					hashMap2.put("TASKID", newTaskId);
					DemAPI.getInstance().updateFormData(PROJECT_UUID, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd1().equals(actStepId)){
						String smsContent = "";
						HashMap<String,String> hash=new HashMap<String,String>();
						hash.put("PROJECTNAME", projectName);
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ZKTZ_ADD_KEY, hash);
						String user=list.get(0).get("ZKR").toString().substring(0,list.get(0).get("ZKR").toString().indexOf("["));
						UserContext target = UserContextUtil.getInstance().getUserContext(user);
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
				if (list.get(0).get("NHSPR") != null&& !list.get(0).get("NHSPR").toString().equals("")&&(SystemConfig._xmsplcConf.getJd2().equals(actStepId)||isZkrEqualsCurrentuserid)&&(nhspr==null||nhspr.equals(""))) {
					String nhUUID = config.get("nhuuid");
					HashMap map = new HashMap();
					map.put("PROJECTNO",projectNo);
					List<HashMap> nhList = DemAPI.getInstance().getList(nhUUID, map, null);
					if(nhList.size()==1){
						HashMap nhMap = nhList.get(0);
						nhMap.put("CBSJ", UtilDate.getNowDatetime());
						DemAPI.getInstance().updateFormData(nhUUID, Long.parseLong(nhMap.get("INSTANCEID").toString()), nhMap,Long.parseLong(nhMap.get("ID").toString()), false);
					}else{
						map.clear();
						Long instanceid = DemAPI.getInstance().newInstance(nhUUID,userModel.getUserid());
						map.put("instanceId", instanceid);
						map.put("formid", config.get("nhformid"));
						map.put("PROJECTNO", projectNo);
						map.put("PROJECTNAME", projectName);
						map.put("OWNER", owner);
						map.put("CBSJ", UtilDate.getNowDatetime());
						map.put("NHSPR", list.get(0).get("NHSPR").toString());
						DemAPI.getInstance().saveFormData(nhUUID, instanceid, map,false);
					}
					map.clear();
					map.put("PROJECTNO", projectNo);
					List<HashMap> list2 = DemAPI.getInstance().getList(PROJECT_UUID, map, null);
					HashMap hashMap2 = list2.get(0);
					dataInstanceid=Long.parseLong(hashMap2.get("INSTANCEID").toString());
					hashMap2.put("SPZT", "内核审批");
					hashMap2.put("TASKID", newTaskId);
					DemAPI.getInstance().updateFormData(PROJECT_UUID, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd2().equals(actStepId)){
						String smsContent = "";
						String sysMsgContent = "";
						HashMap hash=new HashMap();
						hash.put("PROJECTNAME", projectName);
						hash.put("CONTENT", "已提交内核审批");
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.NHTZ_ADD_KEY, hash);
						sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, hash);
						String user=list.get(0).get("NHSPR").toString().substring(0,list.get(0).get("NHSPR").toString().indexOf("["));
						UserContext target = UserContextUtil.getInstance().getUserContext(user);
						String targetUserId="";
						if (target != null) {
							OrgUser targetUserModel = target.get_userModel();
							targetUserId = targetUserModel.getUserid();
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
						String createuserUserId="";
						if (createuserTarget != null) {
							OrgUser createuserTargetUserModel = createuserTarget.get_userModel();
							createuserUserId=createuserTargetUserModel.getUserid();
							if(!createuserUserId.equals(targetUserId)){
								if (!sysMsgContent.equals("")) {
									String mobile = createuserTargetUserModel.getMobile();
									String email = createuserTargetUserModel.getEmail();
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
						String manageruser=map.get("MANAGER").toString().substring(0, map.get("MANAGER").toString().indexOf("["));
						UserContext managerTarget = UserContextUtil.getInstance().getUserContext(manageruser);
						String managerUserId="";
						if (managerTarget != null) {
							OrgUser managerTargetUserModel = managerTarget.get_userModel();
							managerUserId=managerTargetUserModel.getUserid();
							if(!managerUserId.equals(targetUserId)&&!managerUserId.equals(createuserUserId)){
								if (!sysMsgContent.equals("")) {
									String mobile = managerTargetUserModel.getMobile();
									String email = managerTargetUserModel.getEmail();
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
						String owneruser=map.get("OWNER").toString().substring(0,map.get("OWNER").toString().indexOf("["));
						UserContext ownerTarget = UserContextUtil.getInstance().getUserContext(owneruser);
						String ownerUserId="";
						if (ownerTarget != null) {
							OrgUser ownerTargetUserModel = ownerTarget.get_userModel();
							ownerUserId=ownerTargetUserModel.getUserid();
							if(!ownerUserId.equals(targetUserId)&&!ownerUserId.equals(createuserUserId)&&!ownerUserId.equals(managerUserId)){
								if (!sysMsgContent.equals("")) {
									String mobile = ownerTargetUserModel.getMobile();
									String email = ownerTargetUserModel.getEmail();
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
					}
				}
			}
			if(dataInstanceid!=0){
				Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
				/*String isLock = dataMap.get("A02").toString();
				if(SystemConfig._xmsplcConf.getJd1().equals(actStepId)&&(isLock.equals("0")||isLock.equals(""))){
					//获取流程数据子表并向物理数据子表中插入数据
					List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
					List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
					List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
					setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
					List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
					setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);
				}*/
				List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
				List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");
				List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMCYLB");
				setFromSubData(demId,fromSubXmcyData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMCYLB",lcFromSubXmcyData);
				List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMZJJG");
				setFromSubData(demId,fromSubZjjgData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMZJJG",lcFromSubZjjgData);
			}
		}
	}
	
	public void oldMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap hashmap = new HashMap();
		if (dataMap != null) {
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			String nhzkUUID = config.get("nhzkuuid");
			List<HashMap> list = DemAPI.getInstance().getList(nhzkUUID,
					conditionMap, null);
			if (list.size() == 1) {
				if (list.get(0).get("ZKR") != null
						&& !list.get(0).get("ZKR").toString().equals(
								"")&&SystemConfig._xmsplcConf.getJd1().equals(this.getActStepId())) {
					String zkUUID = config.get("zkuuid");
					HashMap map = new HashMap();
					map.put("PROJECTNO", dataMap.get("PROJECTNO"));
					List<HashMap> zkList = DemAPI.getInstance().getList(zkUUID, map, null);
					if(zkList.size()==1){
						HashMap zkMap = zkList.get(0);
						zkMap.put("CBSJ", UtilDate.getNowDatetime());
						DemAPI.getInstance().updateFormData(zkUUID, Long.parseLong(zkMap.get("INSTANCEID").toString()), zkMap,Long.parseLong(zkMap.get("ID").toString()), false);
					}else{
						map.clear();
						Long instanceid = DemAPI.getInstance()
								.newInstance(
										zkUUID,
										UserContextUtil.getInstance()
										.getCurrentUserContext()._userModel
										.getUserid());
						map.put("instanceId", instanceid);
						map.put("formid", config.get("zkformid"));
						map.put("PROJECTNO", dataMap.get("PROJECTNO"));
						map.put("PROJECTNAME", dataMap.get("PROJECTNAME"));
						map.put("CBSJ", UtilDate.getNowDatetime());
						map.put("ZKR", list.get(0).get("ZKR").toString());
						DemAPI.getInstance().saveFormData(zkUUID, instanceid, map,
								false);
					}
					map.clear();
					map.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
					List<HashMap> list2 = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", map, null);
					HashMap hashMap2 = list2.get(0);
					hashMap2.put("SPZT", "质控审批");
					DemAPI.getInstance().updateFormData("33833384d109463285a6a348813539f1", Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd1().equals(this.getActStepId())){
						String smsContent = "";
						String projectname=dataMap.get("PROJECTNAME").toString();
						HashMap hash=new HashMap();
						hash.put("PROJECTNAME", dataMap.get("PROJECTNAME"));
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
								ZQB_Notice_Constants.ZKTZ_ADD_KEY, hash);
						String user=list.get(0).get("ZKR").toString().substring(0, 
								list.get(0).get("ZKR").toString().indexOf("["));
						UserContext target = UserContextUtil.getInstance().getUserContext(
								user);
						if (target != null) {
							if (!smsContent.equals("")) {
								String mobile = target.get_userModel().getMobile();
								if (mobile != null && !mobile.equals("")) {
									mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
									MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
								}
								String email = target.get_userModel().getEmail();
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectname,
											smsContent,"");
								}
							}
						}
					}
				}
				if (list.get(0).get("NHSPR") != null
						&& !list.get(0).get("NHSPR").toString().equals(
								"")&&SystemConfig._xmsplcConf.getJd2().equals(this.getActStepId())) {
					String nhUUID = config.get("nhuuid");
					HashMap map = new HashMap();
					map.put("PROJECTNO", dataMap.get("PROJECTNO"));
					List<HashMap> nhList = DemAPI.getInstance().getList(nhUUID, map, null);
					if(nhList.size()==1){
						HashMap nhMap = nhList.get(0);
						nhMap.put("CBSJ", UtilDate.getNowDatetime());
						DemAPI.getInstance().updateFormData(nhUUID, Long.parseLong(nhMap.get("INSTANCEID").toString()), nhMap,Long.parseLong(nhMap.get("ID").toString()), false);
					}else{
						map.clear();
						Long instanceid = DemAPI.getInstance()
								.newInstance(
										nhUUID,
										UserContextUtil.getInstance()
												.getCurrentUserContext()._userModel
												.getUserid());
						map.put("instanceId", instanceid);
						map.put("formid", config.get("nhformid"));
						map.put("PROJECTNO", dataMap.get("PROJECTNO"));
						map.put("PROJECTNAME", dataMap.get("PROJECTNAME"));
						map.put("CBSJ", UtilDate.getNowDatetime());
						map.put("NHSPR", list.get(0).get("NHSPR").toString());
						DemAPI.getInstance().saveFormData(nhUUID, instanceid, map,
								false);
					}
					map.clear();
					map.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
					List<HashMap> list2 = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", map, null);
					HashMap hashMap2 = list2.get(0);
					hashMap2.put("SPZT", "内核审批");
					DemAPI.getInstance().updateFormData("33833384d109463285a6a348813539f1", Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd2().equals(this.getActStepId())){
						String smsContent = "";
						String sysMsgContent = "";
						String projectname=dataMap.get("PROJECTNAME").toString();
						HashMap hash=new HashMap();
						hash.put("PROJECTNAME", projectname);
						hash.put("CONTENT", "已提交内核审批");
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
								ZQB_Notice_Constants.NHTZ_ADD_KEY, hash);
						sysMsgContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(
								ZQB_Notice_Constants.XMSPZ_ADD_KEY, hash);
						String user=list.get(0).get("NHSPR").toString().substring(0, 
								list.get(0).get("NHSPR").toString().indexOf("["));
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
						String createuser=DBUtil.getString("SELECT USERID FROM ORGUSER WHERE USERNAME='"+dataMap.get("CREATEUSER")+"'", "USERID");
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
						String manageruser=map.get("MANAGER").toString().substring(0, 
								map.get("MANAGER").toString().indexOf("["));
						UserContext managerTarget = UserContextUtil.getInstance().getUserContext(
								manageruser);
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
						String owneruser=map.get("OWNER").toString().substring(0, 
								map.get("OWNER").toString().indexOf("["));
						UserContext ownerTarget = UserContextUtil.getInstance().getUserContext(
								owneruser);
						if(!ownerTarget.get_userModel().getUserid().equals(target.get_userModel().getUserid())&&!ownerTarget.get_userModel().getUserid().equals(createuserTarget.get_userModel().getUserid())&&!ownerTarget.get_userModel().getUserid().equals(managerTarget.get_userModel().getUserid())){
							if (ownerTarget != null) {
								if (!sysMsgContent.equals("")) {
									String mobile = ownerTarget.get_userModel().getMobile();
									String email = ownerTarget.get_userModel().getEmail();
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
