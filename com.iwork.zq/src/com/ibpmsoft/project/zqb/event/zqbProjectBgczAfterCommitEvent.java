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
public class zqbProjectBgczAfterCommitEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectBgczAfterCommitEvent(UserContext uc, HashMap hash) {
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
			String bgczuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '并购重组立项信息'", "UUID");
			String smsContent = "";
			String projectno = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("JYF").toString();
			String ownerId = dataMap.get("OWNER").toString().substring(0,dataMap.get("OWNER").toString().indexOf("["));
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
			hashMap2.put("ZBSPZT", "审批中");
			DemAPI.getInstance().updateFormData(bgczuuid, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
			smsContent = "并购重组项目"+projectName+",承揽个人信息已呈报，请您审核！";
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
			
			//获取流程数据子表并向物理数据子表中插入数据
			/*List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
			
			Long demId = DemAPI.getInstance().getDemModel(bgczuuid).getId();
			
			List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
			setFromSubData(bgczuuid,demId,fromSubClrData,bgczuuid,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
			
			String lcbh = hashMap2.get("LCBH").toString();
			Long pjlcbs = hashMap2.get("LCBS").toString().equals("")?0L:Long.parseLong(hashMap2.get("LCBS").toString());
			if(!pjlcbs.equals("")&&pjlcbs!=0){
				List<HashMap> lcFromClrData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_CLR");
				setFromSubData(lcFromClrData,lcbh,pjlcbs,"SUBFORM_CLR",lcFromSubClrData,false);
			}*/
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
	private void setFromSubData(String bgczuuid,Long demId, List<HashMap> getFromSubData,
			String demUUID, Long dataInstanceid, String subFromKey,
			List<HashMap> saveFromSubData) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					DemAPI.getInstance().removeSubFormData(bgczuuid, dataInstanceid, Long.parseLong(hashMap.get("ID").toString()), subFromKey);
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
