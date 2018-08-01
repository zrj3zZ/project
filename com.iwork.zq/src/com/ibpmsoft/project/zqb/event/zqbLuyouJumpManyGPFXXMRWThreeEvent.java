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
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbLuyouJumpManyGPFXXMRWThreeEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpManyGPFXXMRWThreeEvent(UserContext me, Task task) {
		super(me, task);
	} 

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		HashMap<String, Object> h = ProcessAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("PROJECTNO", h.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
		if (l.size() > 0) {
			HashMap hash = l.get(0);
			// 获取项目负责人
			if (hash.get("OWNER") != null && !hash.get("OWNER").toString().equals("")) {
				if (isExists("ZZSPR",hash.get("OWNER").toString())&&!isNextNode("ZZSPR",hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("[")))&&!isSkip("ZZSPR",hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("[")))) {
					return SystemConfig._gpfxXmlcConf.getJd4();
				} else {
					return SystemConfig._gpfxXmlcConf.getEnd();
				}
			} else {
				return SystemConfig._gpfxXmlcConf.getEnd();
			}
		} else {
			return SystemConfig._gpfxXmlcConf.getEnd();
		}
	}

	public boolean isExists(String filedName, String name) {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String spr = DBUtil.getString(
				"select * from BD_ZQB_GPFXXMSPR where csfzr='" + name + "'",
				filedName);
		if (spr != null && !spr.equals("")) {
			return true;
		}
		return false;
	}
	
	public boolean isNextNode(String filedName,String ownerId){
		HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		String sprId= DBUtil.getString("SELECT SUBSTR("+filedName+",0, INSTR("+filedName+",'[',1)-1) USERID FROM BD_ZQB_GPFXXMSPR WHERE SUBSTR(CSFZR,0, INSTR(CSFZR,'[',1)-1)='"+ownerId+"'", "USERID");
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			return false;
		}
		return true;
    }

	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
		if (l.size() > 0) {
			hash = l.get(0);
			// 获取项目负责人
			if (hash.get("OWNER") != null&&!hash.get("OWNER").toString().equals("")) {
				String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目审批人'", "UUID");
				conditionMap.put("CSFZR", hash.get("OWNER").toString());
				List<HashMap> list = DemAPI.getInstance().getList(demUUID,conditionMap, null);
				if (list.size() == 1) {
					if (list.get(0).get("ZZSPR") != null&& !list.get(0).get("ZZSPR").toString().equals("")&&!isNextNode("ZZSPR",hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("[")))&&!isSkip("ZZSPR",hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("[")))) {
						String user = list.get(0).get("ZZSPR").toString().substring(0,list.get(0).get("ZZSPR").toString().indexOf("["));
						UserContext uc = UserContextUtil.getInstance().getUserContext(user);
						uclist.add(uc);
					}
				}
			} else {
				String user = hash.get("OWNER").toString().substring(0, hash.get("OWNER").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance().getUserContext(user);
				uclist.add(uc);
			}
		}
		return uclist;
	}

	public boolean isOwner() {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
		if (l.size() > 0) {
			hash = l.get(0);
			String userId = UserContextUtil.getInstance().getCurrentUserId();
	    	return userId.equals(hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("[")));
		}
		return false;
	}

	/**
	 * 获得抄送用户列表
	 */
	public List<UserContext> getCCUserList() {
		List<UserContext> list = new ArrayList<UserContext>();
		String userid = "DU1";
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		list.add(uc);
		return list;
	}
	
	public boolean isSkip(String sprNode,String ownerId){
		String ownerUserId ="";
		boolean flag=false;
		String userId = "";
		Long taskId=0L;
		String sprId= DBUtil.getString("SELECT SUBSTR("+sprNode+",0, INSTR("+sprNode+",'[',1)-1) USERID FROM BD_ZQB_GPFXXMSPR WHERE SUBSTR(CSFZR,0, INSTR(CSFZR,'[',1)-1)='"+ownerId+"'", "USERID");
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
