package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
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
public class zqbGPFXProjectAfterCommitEvent extends ProcessStepTriggerEvent {

	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件

	public zqbGPFXProjectAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap hashmap = new HashMap();
		String XMUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
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
					HashMap<String, Object> map = new HashMap<String, Object>();
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
					map.put("ZKR", list.get(0).get("ZKR").toString());
					DemAPI.getInstance().saveFormData(zkUUID, instanceid, map,
							false);
					map.clear();
					map.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
					List<HashMap> list2 = DemAPI.getInstance().getList(XMUUID, map, null);
					HashMap<String, String> hashMap2 = list2.get(0);
					hashMap2.put("SPZT", "质控审批");
					DemAPI.getInstance().updateFormData(XMUUID, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd1().equals(this.getActStepId())){
						String smsContent = "";
						HashMap<String, Object> hash=new HashMap<String, Object>();
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
							}
						}
					}
				}
				if (list.get(0).get("NHSPR") != null
						&& !list.get(0).get("NHSPR").toString().equals(
								"")&&SystemConfig._xmsplcConf.getJd2().equals(this.getActStepId())) {
					String nhUUID = config.get("nhuuid");
					HashMap<String, Object> map = new HashMap<String, Object>();
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
					map.put("NHSPR", list.get(0).get("NHSPR").toString());
					DemAPI.getInstance().saveFormData(nhUUID, instanceid, map,
							false);
					map.clear();
					map.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
					List<HashMap> list2 = DemAPI.getInstance().getList(XMUUID, map, null);
					HashMap<String, String> hashMap2 = list2.get(0);
					hashMap2.put("SPZT", "内核审批");
					DemAPI.getInstance().updateFormData(XMUUID, Long.parseLong(hashMap2.get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
					if(SystemConfig._xmsplcConf.getJd2().equals(this.getActStepId())){
						String smsContent = "";
						HashMap<String, Object> hash=new HashMap<String, Object>();
						hash.put("PROJECTNAME", dataMap.get("PROJECTNAME"));
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
								ZQB_Notice_Constants.NHTZ_ADD_KEY, hash);
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
						}
					}
				}
			}
		}

		return true;
	}

}
