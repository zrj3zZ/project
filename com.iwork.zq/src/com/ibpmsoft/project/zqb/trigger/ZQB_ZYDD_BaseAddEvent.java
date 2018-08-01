package com.ibpmsoft.project.zqb.trigger;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.plugs.meeting.util.UtilDate;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ZQB_ZYDD_BaseAddEvent extends DemTriggerEvent {
	private static final String actDefId = "ZYDDSXLC:1:81304";
	private static final String ZYDD_UUID = "a6a3202f5b294bb79acd578ddbe05e55";

	public ZQB_ZYDD_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String tablename = this.getTableName();
		if (tablename != null && !tablename.equals("") && tablename.equals("BD_XP_ZYDDSXFKRB")) {
			// 事项反馈人数据
			List<HashMap> sublist = DemAPI.getInstance().getFromSubData(
					this.getInstanceId(), "SUBFORM_SXFKRBD");
			//sublist升序排列
			if(sublist!=null&&sublist.size()>0){
				Collections.sort(sublist, new Comparator<Map>() {
					 public int compare(Map o1, Map o2) {
			                return (Integer) o1.get("ID") < (Integer) o2
			                        .get("ID") ? ((Integer) o1.get("ID") == (Integer) o2
			                        .get("ID") ? 0 : -1) : 1;
			            }
		        });
			}
			// 获取第一个人，往流程表里插入数据
			for (HashMap sub : sublist) {
				if (sub.get("USERID") != null
						&& !sub.get("USERID").toString().equals("")) {
					// 如果流程表里已经存在数据，则是编辑，编辑时需要判断流程表里的事项反馈人是不是第一个人，不是则删除掉流程并进行插入新流程
					HashMap hashdata = new HashMap();
					hashdata.put("WLINS", this.getInstanceId());
					Long wlns = DBUtil.getLong(
							"select WLINS from BD_XP_ZYDDSXLCB where WLINS="
									+ this.getInstanceId(), "WLINS");
					//如果存在流程，则判断反馈人是不是第一个人
					boolean isDel=false;
					if(wlns!=null&&wlns != 0){
						Long lcid=hash.get("LCBS")==null||hash.get("LCBS").toString().equals("")?0:Long.parseLong(hash.get("LCBS").toString());//流程id
						List<OrgUser> userlist=ProcessAPI.getInstance().getProcessTransUserList(lcid);
						if(userlist!=null&&userlist.size()>0){
							String userid=userlist.get(0).getUserid();
							if(!sub.get("USERID").toString().equals(userid)){
								List<Task> tasklist = ProcessAPI.getInstance()
								.getUserProcessTaskList(actDefId, "usertask6",
										userid);
								for (Task task : tasklist) {
									if (Long.parseLong(task.getProcessInstanceId()) == lcid) {
										isDel=ProcessAPI.getInstance().removeProcessInstance(task.getId(), userid);
										break;
									}

								}
							}
						}
						
					}
					if (wlns == null || wlns == 0||isDel) {
						Long formId = ProcessAPI.getInstance()
								.getProcessDefaultFormId(actDefId);
						Long instanceId = ProcessAPI.getInstance().newInstance(
								actDefId, formId, sub.get("USERID").toString());
						boolean flag = ProcessAPI.getInstance().saveFormData(
								actDefId, instanceId, hashdata, false);// 保存流程
						// HashMap
						// proMap=ProcessAPI.getInstance().getFromData(instanceId,
						// EngineConstants.SYS_INSTANCE_TYPE_PROCESS);//流程表中的数据
						List<Task> tasklist = ProcessAPI.getInstance()
								.getUserProcessTaskList(actDefId, "usertask6",
										sub.get("USERID").toString());
						String taskid = "";
						String yxid = "";
						for (Task task : tasklist) {
							if (Long.parseLong(task.getProcessInstanceId()) == instanceId) {
								taskid = task.getId();
								yxid = task.getExecutionId();
							}

						}
						// 回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
						hash.put("LCBS", instanceId);
						hash.put("LCBH", actDefId);
						hash.put("YXID", yxid);
						hash.put("RWID", taskid);
						// map.put("SPZT", "审批中");
						// map.put("FKZT", "1");
						// map.put("FKDZL",
						// dataMap.get("FKDZL")!=null?dataMap.get("FKDZL").toString():"");
						// map.put("PRCID", proMap.get("PRCDEFID"));
						hash.put("STEPID", "usertask6");
						Long id = Long.parseLong(hash.get("ID").toString());
						hash.put("ZCFKSJ",UtilDate.datetimeFormat2((Timestamp)hash.get("ZCFKSJ")) );
						hash.put("GXSJ",UtilDate.datetimeFormat2((Timestamp)hash.get("GXSJ")) );
						boolean updateflag = DemAPI.getInstance().updateFormData(ZYDD_UUID,
								this.getInstanceId(), hash, id, true);

					}
					
					// List<Task> tasks =
					// ProcessAPI.getInstance().getUserProcessTaskList(actDefId,
					// "", "");
					// for(Task task:tasks){
					// task.getProcessInstanceId();
					// }

				}
				break;
			}
		}
		return true;

	}
}
