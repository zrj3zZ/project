package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
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
public class zqbProjectSubEndSaveEvent extends ProcessTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectSubEndSaveEvent(UserContext uc, HashMap hash) {
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
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("PROJECTNO",projectNo);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(PROJECT_UUID, conditionMap, null);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					Set<String> keySet = dataMap.keySet();
					hash.put("ZBLCBH", actDefId);
					hash.put("ZBLCBS", instanceId);
					hash.put("ZBTASKID", taskId);
					hash.put("ZBSPZT", "审批通过");
					flag = DemAPI.getInstance().updateFormData(PROJECT_UUID,Long.parseLong(hash.get("INSTANCEID").toString()),hash, Long.parseLong(hash.get("ID").toString()),false);
					String smsContent = "";
					if(flag){
						HashMap<String,String> contentMap=new HashMap<String,String>();
						contentMap.put("PROJECTNAME", projectName);
						contentMap.put("CONTENT", "分配部门与承揽个人信息审批通过");
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMSPZ_ADD_KEY, contentMap);
						String user=dataMap.get("CREATEUSERID").toString();
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
							if (!smsContent.equals("")) {
								String email = targetUserModel.getEmail();
								if (email != null && !email.equals("")) {
									MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, projectName,smsContent,"");
								}
							}
						}
					}
					Long dataInstanceid=Long.parseLong(hash.get("INSTANCEID").toString());
					//获取流程数据子表并向物理数据子表中插入数据
					List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
					List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
					
					Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
					
					List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
					setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
					List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
					setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);
					
					String lcbh = hash.get("LCBH").toString();
					Long pjlcbs = hash.get("LCBS").toString().equals("")?0L:Long.parseLong(hash.get("LCBS").toString());
					if(!pjlcbs.equals("")&&pjlcbs!=0){
						List<HashMap> lcFromClrData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_CLR");
						setFromSubData(lcFromClrData,lcbh,pjlcbs,"SUBFORM_CLR",lcFromSubClrData,false);
						List<HashMap> lcFromCljgData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_CLJG");
						setFromSubData(lcFromCljgData,lcbh,pjlcbs,"SUBFORM_CLJG",lcFromSubCljgData,false);
					}
					
				}
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
	
	/**
	 * 
	 * @param getFromSubData 流程表中子表的信息
	 * @param actDefId 流程Id
	 * @param instanceId
	 * @param fromSubKey 子表名
	 * @param savefromSubData 需保存到流程数据中子表的信息
	 * @param isLog 是否记录日志
	 */
	private void setFromSubData(List<HashMap> getFromSubData, String actDefId, Long instanceId,
			String fromSubKey, List<HashMap> savefromSubData, boolean isLog) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					ProcessAPI.getInstance().removeSubFormData(actDefId,instanceId,Long.parseLong(hashMap.get("ID").toString()),fromSubKey);
				}
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}else{
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}
		}else{
			if(savefromSubData!=null){
				ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
			}
		}
	}

}
