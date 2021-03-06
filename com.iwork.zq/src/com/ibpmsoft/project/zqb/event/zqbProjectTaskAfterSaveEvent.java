package com.ibpmsoft.project.zqb.event;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectTaskAfterSaveEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";

	public zqbProjectTaskAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

		if (dataMap != null) {
			Long instanceid = DemAPI.getInstance().newInstance(
					PROJECT_TASK_UUID, uc.get_userModel().getUserid());

			HashMap<String, Object> hashdata = new HashMap<String, Object>();
			hashdata.put("TASK_NAME", dataMap.get("TASK_NAME") == null ? ""
					: dataMap.get("TASK_NAME").toString());
			hashdata.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
					: dataMap.get("STARTDATE").toString());
			hashdata.put("ENDDATE", dataMap.get("ENDDATE") == null ? ""
					: dataMap.get("ENDDATE").toString());
			hashdata.put("SCALE", dataMap.get("SCALE") == null ? "" : dataMap
					.get("SCALE").toString());
			hashdata.put("JDZL", dataMap.get("JDZL") == null ? "" : dataMap
					.get("JDZL").toString());
			hashdata.put("PRIORITY", dataMap.get("PRIORITY") == null ? ""
					: dataMap.get("PRIORITY").toString());
			hashdata.put("ATTACH", dataMap.get("ATTACH") == null ? "" : dataMap
					.get("ATTACH").toString());
			hashdata.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
					.get("MEMO").toString());
			hashdata.put("MANAGER", dataMap.get("MANAGER") == null ? ""
					: dataMap.get("MANAGER").toString());
			hashdata.put("GROUPID", dataMap.get("GROUPID") == null ? ""
					: dataMap.get("GROUPID").toString());
			hashdata.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? ""
					: dataMap.get("PROJECTNO").toString());
			hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME") == null ? ""
					: dataMap.get("PROJECTNAME").toString());
			hashdata.put("ORDERINDEX", dataMap.get("ORDERINDEX") == null ? ""
					: dataMap.get("ORDERINDEX").toString());
			hashdata.put("HTJE", dataMap.get("HTJE") == null ? "" : dataMap
					.get("HTJE").toString());
			hashdata.put("SSJE", dataMap.get("SSJE") == null ? "" : dataMap
					.get("SSJE").toString());
			hashdata.put("SXZLMB", dataMap.get("SXZLMB") == null ? "" : dataMap
					.get("SXZLMB").toString());
			hashdata.put(
					"GXSJ",
					dataMap.get("GXSJ") == null ? "" : UtilDate
							.datetimeFormat((Timestamp) dataMap.get("GXSJ")));
			hashdata.put("LCBS", instanceId);
			hashdata.put("LCBH", this.getActDefId());
			hashdata.put("YXID", this.getExcutionId());
			hashdata.put("RWID", this.getTaskId());
			hashdata.put("SPZT", "未提交");
			hashdata.put("PRCID", this.getProDefId());
			hashdata.put("STEPID", this.getActStepId());
			// 保存到项目资料表中
			if (!hashdata.get("ATTACH").equals("")) {
				String[] sxzlmb = hashdata.get("ATTACH").toString()
						.split(";");
				String userid = this.getUserContext().get_userModel()
						.getUserid();
				for (int i = 0; i < sxzlmb.length; i++) {
					String[] zllist = sxzlmb[i].split(":");
					if(zllist.length>1){
					String[] zll = zllist[1].split(",");
					String mb = zllist[0];
					for(int j=0;j<zll.length;j++){
						String zl=zll[j];
						HashMap map = new HashMap();
						map.put("JDZL", zl);
						map.put("PROJECTNO", hashdata.get("PROJECTNO")
								.toString());
						map.put("SXZL", mb);
						map.put("JDBH", hashdata.get("GROUPID").toString());
						Long ins = DemAPI.getInstance().newInstance(
								"db9b82e9d24a491aba9239cc67284b75", userid);
						List<HashMap> rwzl = DemAPI.getInstance().getList(
								"db9b82e9d24a491aba9239cc67284b75", map, null);
						if (rwzl.size() <= 0) {
							DemAPI.getInstance().saveFormData(
									"db9b82e9d24a491aba9239cc67284b75", ins,
									map, true);// 物理表单
						}
						List<HashMap> rwzllc = DemAPI.getInstance().getList(
								"80a37097080d496a85ceddb63e916f2c", map, null);
						if (rwzllc.size() <= 0) {
							ins = DemAPI.getInstance().newInstance(
									"80a37097080d496a85ceddb63e916f2c", userid);
							DemAPI.getInstance().saveFormData(
									"80a37097080d496a85ceddb63e916f2c", ins,
									map, true);// 流程表单

						}
					}
					}
				}
			}
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap = new HashMap();
			conditionMap.put("LCBS", instanceId);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(
					PROJECT_TASK_UUID, conditionMap, null);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					hash.put("TASK_NAME", dataMap.get("TASK_NAME") == null ? ""
							: dataMap.get("TASK_NAME").toString());
					hash.put("GROUPID", dataMap.get("GROUPID") == null ? ""
							: dataMap.get("GROUPID").toString());
					hash.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
							: dataMap.get("STARTDATE").toString());
					hash.put("ENDDATE", dataMap.get("ENDDATE") == null ? ""
							: dataMap.get("ENDDATE").toString());
					hash.put("SCALE", dataMap.get("SCALE") == null ? ""
							: dataMap.get("SCALE").toString());
					hash.put("MANAGER", dataMap.get("MANAGER") == null ? ""
							: dataMap.get("MANAGER").toString());
					hash.put("HTJE", dataMap.get("HTJE") == null ? "" : dataMap
							.get("HTJE").toString());
					hash.put("SSJE", dataMap.get("SSJE") == null ? "" : dataMap
							.get("SSJE").toString());
					hash.put("JDZL", dataMap.get("JDZL") == null ? "" : dataMap
							.get("JDZL").toString());
					hash.put("ATTACH", dataMap.get("ATTACH") == null ? ""
							: dataMap.get("ATTACH").toString());
					hash.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
							.get("MEMO").toString());
					hash.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? ""
							: dataMap.get("PROJECTNO").toString());
					hash.put("PROJECTNAME",
							dataMap.get("PROJECTNAME") == null ? "" : dataMap
									.get("PROJECTNAME").toString());
					hash.put("ORDERINDEX",
							dataMap.get("ORDERINDEX") == null ? "" : dataMap
									.get("ORDERINDEX").toString());
					hash.put("SPZT", hash.get("SPZT").toString());
					hash.put("STEPID", this.getActStepId());
					hash.put("YXID", this.getExcutionId());
					hash.put("RWID", this.getTaskId());
					flag = DemAPI.getInstance().updateFormData(
							PROJECT_TASK_UUID,
							Long.parseLong(hash.get("INSTANCEID").toString()),
							hash, Long.parseLong(hash.get("ID").toString()),
							false);
				}

			} else {
				// 保存到项目任务数据表中
				flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID,
						instanceid, hashdata, false);
			}
		}
		if(SystemConfig._xmlcConf.getConfig().equals("2")){
			String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目管理维护'", "UUID");
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("PROJECTNO", dataMap.get("PROJECTNO"));
			List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目审批人维护'", "UUID");
			String owner = l.get(0).get("OWNER").toString();
			conditionMap.put("CSFZR", owner);
			List<HashMap> list = DemAPI.getInstance().getList(demUUID,conditionMap, null);
			if(list.isEmpty()){
				Long instanceid = DemAPI.getInstance().newInstance(demUUID,owner.substring(0,owner.indexOf("[")));
				HashMap map= new HashMap();
				map.put("CSFZR", owner);
				map.put("ZHGXSJ", UtilDate.getNowDatetime());
				DemAPI.getInstance().saveFormData(demUUID,instanceid, map, false);
			}
		}
		return flag;
	}

}
