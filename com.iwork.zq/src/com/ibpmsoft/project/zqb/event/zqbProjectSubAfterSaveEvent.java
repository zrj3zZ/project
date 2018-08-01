package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectSubAfterSaveEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public zqbProjectSubAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		return newMethod();
	}
	
	public boolean newMethod(){
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			String projectNo = dataMap.get("PROJECTNO").toString();
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("PROJECTNO", projectNo);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(PROJECT_UUID, conditionMap, null);
			Long dataInstanceid=0L;
			if (lcbsexists != null && !lcbsexists.isEmpty()) {
				dataInstanceid=Long.parseLong(lcbsexists.get(0).get("INSTANCEID").toString());
				//获取流程数据子表并向物理数据子表中插入数据
				List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
				List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
				
				Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
				
				List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
				setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
				List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
				setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);
			}
		}
		return true;
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
