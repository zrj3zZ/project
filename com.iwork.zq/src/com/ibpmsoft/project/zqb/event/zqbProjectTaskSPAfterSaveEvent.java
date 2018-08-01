package com.ibpmsoft.project.zqb.event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbProjectTaskSPAfterSaveEvent extends ProcessTriggerEvent {
	public zqbProjectTaskSPAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}

	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";
	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap=new HashMap();
			conditionMap.put("LCBS", instanceId);
			List<HashMap> lcbsexists=DemAPI.getInstance().getList(PROJECT_TASK_UUID, conditionMap, null);
			 if(lcbsexists!=null&&lcbsexists.size()==1){
				for(HashMap<String,Object> hash:lcbsexists){
	        		hash.put("TASK_NAME", dataMap.get("TASK_NAME")==null?"":dataMap.get("TASK_NAME").toString());
	    			hash.put("GROUPID", dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString());
	    			hash.put("STARTDATE", dataMap.get("STARTDATE")==null?"":dataMap.get("STARTDATE").toString());
	    			hash.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE").toString());
	    			hash.put("SCALE", dataMap.get("SCALE")==null?"":dataMap.get("SCALE").toString());
	    			hash.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER").toString());
	    			hash.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE").toString());
	    			hash.put("SSJE", dataMap.get("SSJE")==null?"":dataMap.get("SSJE").toString());
	    			hash.put("JDZL", dataMap.get("JDZL")==null?"":dataMap.get("JDZL").toString());
	    			hash.put("ATTACH", dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString());
	    			hash.put("MEMO", dataMap.get("MEMO")==null?"":dataMap.get("MEMO").toString());
	    			hash.put("PROJECTNO", dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString());
	    			hash.put("PROJECTNAME", dataMap.get("PROJECTNAME")==null?"":dataMap.get("PROJECTNAME").toString());
	    			hash.put("ORDERINDEX", dataMap.get("ORDERINDEX")==null?"":dataMap.get("ORDERINDEX").toString());
	    			hash.put("SPZT", "审批通过");
	    			flag = DemAPI.getInstance().updateFormData(PROJECT_TASK_UUID,
	    					Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
	        	}
            }else{
            	//判断是否存在任务表中，如果存在则不进行处理，如果存在则直接插入
    			Long instanceid = DemAPI.getInstance().newInstance(PROJECT_TASK_UUID, uc.get_userModel().getUserid());
    			HashMap<String,Object> hashdata = new HashMap<String,Object>();
    			hashdata.put("TASK_NAME", dataMap.get("TASK_NAME")==null?"":dataMap.get("TASK_NAME").toString());
    			hashdata.put("STARTDATE", dataMap.get("STARTDATE")==null?"":dataMap.get("STARTDATE").toString());
    			hashdata.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE").toString());
    			hashdata.put("SCALE", dataMap.get("SCALE")==null?"":dataMap.get("SCALE").toString());
    			hashdata.put("JDZL", dataMap.get("JDZL")==null?"":dataMap.get("JDZL").toString());
    			hashdata.put("PRIORITY", dataMap.get("PRIORITY")==null?"":dataMap.get("PRIORITY").toString());
    			hashdata.put("ATTACH", dataMap.get("ATTACH")==null?"":dataMap.get("ATTACH").toString());
    			hashdata.put("MEMO", dataMap.get("MEMO")==null?"":dataMap.get("MEMO").toString());
    			hashdata.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER").toString());
    			hashdata.put("GROUPID", dataMap.get("GROUPID")==null?"":dataMap.get("GROUPID").toString());
    			hashdata.put("PROJECTNO", dataMap.get("PROJECTNO")==null?"":dataMap.get("PROJECTNO").toString());
    			hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME")==null?"":dataMap.get("PROJECTNAME").toString());
    			hashdata.put("ORDERINDEX", dataMap.get("ORDERINDEX")==null?"":dataMap.get("ORDERINDEX").toString());
    			hashdata.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE").toString());
    			hashdata.put("SSJE", dataMap.get("SSJE")==null?"":dataMap.get("SSJE").toString());
    			hashdata.put("GXSJ", dataMap.get("GXSJ")==null?"":UtilDate.datetimeFormat((Timestamp)dataMap.get("GXSJ")))			;
    			hashdata.put("LCBS", instanceId);
    			hashdata.put("SPZT", "审批通过");
    			String actStepId=SystemConfig._xmlcConf.getEnd();
    			hashdata.put("STEPID",actStepId);
    			hashdata.put("YXID", this.getExcutionId());
    			hashdata.put("RWID", this.getTaskId());
    			hashdata.put("LCBH", instanceId);
    			// 保存到项目任务数据表中
    			flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID, instanceid, hashdata, false);
            }
			// 更新项目基本信息中的任务阶段为最新的阶段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			hashmap.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? "" : dataMap.get("PROJECTNO").toString());
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_UUID,hashmap, null);
			BigDecimal xmjd = dataMap.get("GROUPID") == null ? new BigDecimal(0):new BigDecimal(dataMap.get("GROUPID").toString());
			String jdmc=DBUtil.getString("SELECT JDMC FROM BD_ZQB_KM_INFO WHERE ID="+xmjd, "JDMC");
			if (list != null && list.size() == 1) {
				if (jdmc != null && !jdmc.equals("")) {
					HashMap m = list.get(0);
					if (!m.get("XMJD").toString().equals(jdmc)) {
						Long instance = Long.parseLong(m.get("INSTANCEID").toString());
						Long dataid = Long.parseLong(m.get("ID").toString());
						m.put("XMJD", jdmc);
						flag = DemAPI.getInstance().updateFormData(PROJECT_UUID, instance, m, dataid, false);
					}else{
						flag =true;
					}
				}
			}
			HashMap<String,String> contentMap = new HashMap<String,String>();
			contentMap.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
			contentMap.put("JDMC",jdmc);
			String smsContent=ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.XMJDSPTG_ADD_KEY, contentMap);
			String userid = UserContextUtil.getInstance().getUserId(dataMap.get("MANAGER").toString());
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
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
      
		return flag;
	}
}
