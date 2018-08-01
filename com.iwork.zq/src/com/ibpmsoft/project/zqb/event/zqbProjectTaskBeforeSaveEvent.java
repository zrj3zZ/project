package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbProjectTaskBeforeSaveEvent extends ProcessStepTriggerEvent {

	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";
	public zqbProjectTaskBeforeSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
//确认发送动作前，判断上一个任务是否已经审批结束，如果未审批，则不允许提交
	public boolean execute(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);// 获取流程表单数据
		//查询是否上一阶段未审批
		String projectno= dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString();
		String groupid=dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString();
		int lastId=DBUtil.getInt("select MAX(id) lastid from bd_pm_group t where id<"+groupid, "ID");//上一任务id
		if(lastId!=0){
			//判断
			String demUUID="b25ca8ed0a5a484296f2977b50db8396";
			HashMap conditionMap=new HashMap();
			conditionMap.put("PROJECTNO", projectno);
			conditionMap.put("GROUPID", groupid);
			List<HashMap> list=DemAPI.getInstance().getList(demUUID, conditionMap, null);//找到上一个
			if(list.size()>0){
				String spzt=list.get(0).get("SPZT")==null?"":list.get(0).get("SPZT").toString();
				if(!spzt.equals("")&&spzt.equals("审批通过")){
					 MessageQueueUtil.getInstance().putAlertMsg("上一任务阶段还未审批结束，不能进行提交审批！");
					flag=true;
				}else{
					flag=false;
				}
			}
		}else{
			flag=true;
		}
//		Long instanceId = this.getInstanceId();
//		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
//		// 获取流程表单数据
//		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
//		
//		if(dataMap!=null){
//			Long instanceid = DemAPI.getInstance().newInstance(PROJECT_TASK_UUID, uc.get_userModel().getUserid());
//			
//			HashMap<String,Object> hashdata = new HashMap<String,Object>();
//			hashdata.put("TASK_NAME", dataMap.get("TASK_NAME")==null?"":dataMap.get("TASK_NAME").toString());
//			hashdata.put("STARTDATE", dataMap.get("STARTDATE")==null?"":dataMap.get("STARTDATE").toString());
//			hashdata.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE").toString());
//			hashdata.put("SCALE", dataMap.get("SCALE")==null?"":dataMap.get("SCALE").toString());
//			hashdata.put("JDZL", dataMap.get("JDZL")==null?"":dataMap.get("JDZL").toString());
//			hashdata.put("PRIORITY", dataMap.get("PRIORITY")==null?"":dataMap.get("PRIORITY").toString());
//			hashdata.put("ATTACH", dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString());
//			hashdata.put("MEMO", dataMap.get("MEMO")==null?"":dataMap.get("MEMO").toString());
//			hashdata.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER").toString());
//			hashdata.put("GROUPID", dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString());
//			hashdata.put("PROJECTNO", dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString());
//			hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME")==null?"":dataMap.get("PROJECTNAME").toString());
//			hashdata.put("ORDERINDEX", dataMap.get("ORDERINDEX")==null?"":dataMap.get("ORDERINDEX").toString());
//			hashdata.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE").toString());
//			hashdata.put("SSJE", dataMap.get("SSJE")==null?"":dataMap.get("SSJE").toString());
//			hashdata.put("GXSJ", dataMap.get("GXSJ")==null?"":UtilDate.datetimeFormat((Timestamp)dataMap.get("GXSJ")))			;
//			hashdata.put("LCBS", instanceId);
//            hashdata.put("LCBH", this.getActDefId());
//			hashdata.put("YXID", this.getExcutionId());
//			hashdata.put("RWID", this.getTaskId());
//			hashdata.put("SPZT", "未提交");
//			hashdata.put("PRCID", this.getProDefId());
//			hashdata.put("STEPID", this.getActStepId());
//			//判断是否存在，已经存在，不存在保存，存在更新
//			HashMap conditionMap=new HashMap();
//			conditionMap.put("LCBS", instanceId);
//			List<HashMap> lcbsexists=DemAPI.getInstance().getList(PROJECT_TASK_UUID, conditionMap, null);
//			 if(lcbsexists!=null&&lcbsexists.size()==1){
//				for(HashMap<String,Object> hash:lcbsexists){
//	        		hash.put("TASK_NAME", dataMap.get("TASK_NAME")==null?"":dataMap.get("TASK_NAME").toString());
//	    			hash.put("GROUPID", dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString());
//	    			hash.put("STARTDATE", dataMap.get("STARTDATE")==null?"":dataMap.get("STARTDATE").toString());
//	    			hash.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE").toString());
//	    			hash.put("SCALE", dataMap.get("SCALE")==null?"":dataMap.get("SCALE").toString());
//	    			hash.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER").toString());
//	    			hash.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE").toString());
//	    			hash.put("SSJE", dataMap.get("SSJE")==null?"":dataMap.get("SSJE").toString());
//	    			hash.put("JDZL", dataMap.get("JDZL")==null?"":dataMap.get("JDZL").toString());
//	    			hash.put("ATTACH", dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString());
//	    			hash.put("MEMO", dataMap.get("MEMO")==null?"":dataMap.get("MEMO").toString());
//	    			hash.put("PROJECTNO", dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString());
//	    			hash.put("PROJECTNAME", dataMap.get("PROJECTNAME")==null?"":dataMap.get("PROJECTNAME").toString());
//	    			hash.put("ORDERINDEX", dataMap.get("ORDERINDEX")==null?"":dataMap.get("ORDERINDEX").toString());
//	    			hash.put("SPZT", "未提交");
//        			hash.put("STEPID", this.getActStepId());
//        			hash.put("YXID", this.getExcutionId());
//        			hash.put("RWID", this.getTaskId());
//	    			flag = DemAPI.getInstance().updateFormData(PROJECT_TASK_UUID,
//	    					Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
//	        	}
//			 
//            }else{
//			// 保存到项目任务数据表中
//			flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID, instanceid, hashdata, false);
//            }
//           }
		
		return flag;
	} 
	
}

