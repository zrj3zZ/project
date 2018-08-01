package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务流程规则跳转触发第一步
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpXMSubEvent extends SysJumpTriggerEvent {
	public zqbLuyouJumpXMSubEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String pjno = hash.get("PROJECTNO").toString();
		if(actStepId.equals(SystemConfig._xmzbConf.getJd1())&&!isSkip("OWNER", pjno)){
			return SystemConfig._xmzbConf.getJd2();
		}else{
			return SystemConfig._xmzbConf.getEnd();
		}
	}
	
	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String pjno = hash.get("PROJECTNO").toString();
		// 获取项目负责人
		if (hash.get("OWNER") != null && !hash.get("OWNER").toString().equals("")&&!isSkip("OWNER", pjno)) {
			String ownerId = hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("["));
			UserContext uc = UserContextUtil.getInstance().getUserContext(ownerId);
			uclist.add(uc);
		}
		return uclist;
	}
	
	public boolean isSkip(String sprNode,String pjno){
		boolean flag=false;
		String userId = "";
		Long taskId=0L;
		String sprId= DBUtil.getString("SELECT SUBSTR("+sprNode+",0, INSTR("+sprNode+",'[',1)-1) USERID FROM BD_ZQB_PJ_BASE WHERE PROJECTNO='"+pjno+"'", "USERID");
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			userId=sprId;
		}
		List<ProcessRuOpinion> processOpinionList = ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getInstanceId());
		if(processOpinionList.isEmpty()){
			return flag;
		}else{
			Collections.reverse(processOpinionList);
			for (ProcessRuOpinion processRuOpinion : processOpinionList) {
				String action = processRuOpinion.getAction();
				if(action.equals("驳回")){
					taskId=processRuOpinion.getTaskid();
					break;
				}
			}
			for (ProcessRuOpinion processRuOpinion : processOpinionList) {
				if(taskId!=0){
					String createuser = processRuOpinion.getCreateuser();
					if(taskId<processRuOpinion.getTaskid()&&!processRuOpinion.getAction().equals("转发")){
						if(createuser.equals(userId)){
							flag=true;
							break;
						}else{
							flag=false;
						}
					}else{
						continue;
					}
				}else if(taskId==0){
					String createuser = processRuOpinion.getCreateuser();
					if(createuser.equals(userId)){
						flag=true;
						break;
					}else{
						flag=false;
					}
				}
			}
		}
		return flag;
	}

}