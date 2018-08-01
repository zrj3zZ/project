package com.ibpmsoft.project.zqb.event;

import java.math.BigDecimal;
import java.util.ArrayList;
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

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectTaskAfterCommitEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";

	public zqbProjectTaskAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		String actStepId = this.getActStepId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String projectno=dataMap.get("PROJECTNO").toString();
		String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目管理维护'", "UUID");
		List<HashMap> list=new ArrayList<HashMap>();
		if (dataMap != null) {
			Long instanceid = DemAPI.getInstance().newInstance(
					PROJECT_TASK_UUID, uc.get_userModel().getUserid());
				// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap = new HashMap();
			conditionMap.put("LCBS", instanceId);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(
					PROJECT_TASK_UUID, conditionMap, null);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					hash.put("SPZT", "已提交");
					flag = DemAPI.getInstance().updateFormData(
							PROJECT_TASK_UUID,
							Long.parseLong(hash.get("INSTANCEID").toString()),
							hash, Long.parseLong(hash.get("ID").toString()),
							false);
				}

			} 
			BigDecimal xmjd = dataMap.get("GROUPID") == null ? new BigDecimal(0):new BigDecimal(dataMap.get("GROUPID").toString());
			String jdmc=DBUtil.getString("SELECT JDMC FROM BD_ZQB_KM_INFO WHERE ID="+xmjd, "JDMC");
			HashMap<String,String> contentMap = new HashMap<String,String>();
			contentMap.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
			contentMap.put("JDMC",jdmc);
			String smsContent="";
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMJDTJ_ADD_KEY, contentMap);
			String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目审批人维护'", "UUID");
			if(flag){
				String config = SystemConfig._xmlcConf.getConfig();
				if("1".equals(config)){
					if(!isOwner()&&SystemConfig._xmlcConf.getJd1().equals(actStepId)){
						HashMap map = new HashMap();
						map.put("PROJECTNO",projectno);
						List<HashMap> list2 = DemAPI.getInstance().getList(PROJECT_UUID, map, null);
						SendContent(list2,smsContent,"OWNER");
					}else if(isOwner()&&SystemConfig._xmlcConf.getJd1().equals(actStepId)){
						conditionMap.clear();
						List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID, conditionMap, null);
						if(rwlist.get(0).get("CSFZR")!=null&&!"".equals(rwlist.get(0).get("CSFZR").toString())){
							SendContent(rwlist,smsContent,"CSFZR");
						}else if(rwlist.get(0).get("FSFZR")!=null&&!"".equals(rwlist.get(0).get("FSFZR").toString())){
							SendContent(rwlist,smsContent,"FSFZR");
						}else if(rwlist.get(0).get("ZZSPR")!=null&&!"".equals(rwlist.get(0).get("ZZSPR").toString())){
							SendContent(rwlist,smsContent,"ZZSPR");
						}
					}else if (SystemConfig._xmlcConf.getJd2().equals(actStepId)){
						conditionMap.clear();
						List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID, conditionMap, null);
						if(rwlist.get(0).get("CSFZR")!=null&&!"".equals(rwlist.get(0).get("CSFZR").toString())){
							SendContent(rwlist,smsContent,"CSFZR");
						}else if(rwlist.get(0).get("FSFZR")!=null&&!"".equals(rwlist.get(0).get("FSFZR").toString())){
							SendContent(rwlist,smsContent,"FSFZR");
						}else if(rwlist.get(0).get("ZZSPR")!=null&&!"".equals(rwlist.get(0).get("ZZSPR").toString())){
							SendContent(rwlist,smsContent,"ZZSPR");
						}
					}else if (SystemConfig._xmlcConf.getJd3().equals(actStepId)){
						conditionMap.clear();
						List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID, conditionMap, null);
						if(rwlist.get(0).get("FSFZR")!=null&&!"".equals(rwlist.get(0).get("FSFZR").toString())){
							SendContent(rwlist,smsContent,"FSFZR");
						}else if(rwlist.get(0).get("ZZSPR")!=null&&!"".equals(rwlist.get(0).get("ZZSPR").toString())){
							SendContent(rwlist,smsContent,"ZZSPR");
						}
					}else if (SystemConfig._xmlcConf.getJd4().equals(actStepId)){
						conditionMap.clear();
						List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID, conditionMap, null);
						if(rwlist.get(0).get("ZZSPR")!=null&&!"".equals(rwlist.get(0).get("ZZSPR").toString())){
							SendContent(rwlist,smsContent,"ZZSPR");
						}
					}
				}else if("2".equals(config)){
					conditionMap.clear();
					HashMap map = new HashMap();
					map.put("PROJECTNO",projectno);
					list = DemAPI.getInstance().getList(PROJECT_UUID, map, null);
					String owner=list.get(0).get("OWNER").toString();
					conditionMap.put("CSFZR", owner);
					List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID, conditionMap, null);
					if(!isOwner()&&SystemConfig._xmlcConf.getJd1().equals(actStepId)){
						SendContent(rwlist,smsContent,"CSFZR");
					}else if(isOwner()){
						SendContent(rwlist,smsContent,"FSFZR");
					}else if(SystemConfig._xmlcConf.getJd3().equals(actStepId)){
						SendContent(rwlist,smsContent,"ZZSPR");
					}
				}
			}
		}
		return true;
	}

	public boolean isOwner() {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目管理维护'", "UUID");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
		if (l.size() > 0) {
			hash = l.get(0);
			String username = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			return username.equals(hash.get("OWNER").toString());
		}
		return false;
	}
	
	public void SendContent(List<HashMap> rwlist,String smsContent,String name){
		String userid = UserContextUtil.getInstance().getUserId(rwlist.get(0).get(name).toString());
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
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
}
