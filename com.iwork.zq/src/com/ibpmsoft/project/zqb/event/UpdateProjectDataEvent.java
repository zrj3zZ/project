package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.service.ZqbProjectManageService;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class UpdateProjectDataEvent implements IWorkScheduleInterface {
	private ZqbProjectManageService zqbProjectManageService;
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始项目信息同步...... ");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始项目信息同步...... ");
		updateProjectData();
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-项目信息同步完毕...... ");
		return true;
	}

	private void updateProjectData() {
		if(zqbProjectManageService==null)
			zqbProjectManageService = (ZqbProjectManageService)SpringBeanUtil.getBean("zqbProjectManageService");
		List<HashMap<String,Long>> projectData = zqbProjectManageService.getProjectData();
		for (HashMap<String, Long> hashMap : projectData) {
			Long instanceId = hashMap.get("LCBS");
			Long dataInstanceId = hashMap.get("INSTANCEID");
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			HashMap pjDataMap = DemAPI.getInstance().getFromData(dataInstanceId);
			if (dataMap != null) {
				Long lcTaskId = Long.parseLong(pjDataMap.get("TASKID").toString());
				String actDefId = pjDataMap.get("LCBH").toString();
				String actStepId = pjDataMap.get("STEPID").toString();
				Long dataId=Long.parseLong(pjDataMap.get("ID").toString());
				Set<String> keySet = dataMap.keySet();
				for (String key : keySet) {
					if(key.equals("WBCLRY")){
						pjDataMap.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}else if(!key.equals("ID")){
						pjDataMap.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}
				}
				pjDataMap.put("LCBH", "");
				pjDataMap.put("LCBS", "");
				pjDataMap.put("STEPID", "");
				pjDataMap.put("TASKID", "");
				DemAPI.getInstance().updateFormData(PROJECT_UUID,dataInstanceId,pjDataMap, dataId,false);
					
				//获取流程数据子表并向物理数据子表中插入数据
				List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
				List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
				List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
				List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");
				
				Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
				
				List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_XMCYLB");
				setFromSubData(demId,fromSubXmcyData,PROJECT_UUID,dataInstanceId,"SUBFORM_XMCYLB",lcFromSubXmcyData);
				List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLR");
				setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceId,"SUBFORM_CLR",lcFromSubClrData);
				List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLJG");
				setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceId,"SUBFORM_CLJG",lcFromSubCljgData);
				List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_XMZJJG");
				setFromSubData(demId,fromSubZjjgData,PROJECT_UUID,dataInstanceId,"SUBFORM_XMZJJG",lcFromSubZjjgData);
				
				removeFromSubData(actDefId,instanceId,"SUBFORM_XMCYLB",lcFromSubXmcyData);
				removeFromSubData(actDefId,instanceId,"SUBFORM_CLR",lcFromSubClrData);
				removeFromSubData(actDefId,instanceId,"SUBFORM_CLJG",lcFromSubCljgData);
				removeFromSubData(actDefId,instanceId,"SUBFORM_XMZJJG",lcFromSubZjjgData);
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				if(newTaskId!=null){
					String taskId = newTaskId.getId();
					String userId = newTaskId.getAssignee();
					ProcessAPI.getInstance().removeProcessInstance(taskId, userId);
				}
				ProcessAPI.getInstance().removeFormData(instanceId);
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
	
	private void removeFromSubData(String actDefId, Long instanceId, String subformkey, List<HashMap> lcFromSubXmcyData){
		for (HashMap hashMap : lcFromSubXmcyData) {
			ProcessAPI.getInstance().removeSubFormData(actDefId, instanceId, Long.parseLong(hashMap.get("ID").toString()), subformkey);
		}
	}

}
