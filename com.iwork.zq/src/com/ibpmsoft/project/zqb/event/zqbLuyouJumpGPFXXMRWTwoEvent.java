package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务流程规则跳转触发第二步
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpGPFXXMRWTwoEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpGPFXXMRWTwoEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		if(isExists("FSFZR")&&!isNextNode("FSFZR")&&!isSkip("FSFZR")){
			return SystemConfig._gpfxXmlcConf.getJd4();
		}else if(isExists("ZZSPR")&&!isNextNode("ZZSPR")&&!isSkip("ZZSPR")){
			return SystemConfig._gpfxXmlcConf.getJd5();
		}else{
			return SystemConfig._gpfxXmlcConf.getEnd();
		}
	}
	public boolean isExists(String filedName){
		HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		String spr= DBUtil.getString("select * from BD_ZQB_GPFXXMSPR ", filedName);
		if(spr!=null&&!spr.equals("")){
			return true;
		}
		return false;
	}
	
	public boolean isNextNode(String filedName){
		HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		String sprId= DBUtil.getString("SELECT SUBSTR("+filedName+",0, INSTR("+filedName+",'[',1)-1) USERID FROM BD_ZQB_GPFXXMSPR", "USERID");
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			return false;
		}
		return true;
	}
	
	public List<UserContext> getNextUserList() {
		String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目审批人'", "UUID");
		HashMap conditionMap=new HashMap();
		List<UserContext> uclist=new ArrayList<UserContext>();
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		if (list.size() == 1) {
			if (list.get(0).get("FSFZR") != null&&!list.get(0).get("FSFZR").toString().equals("")&&!isNextNode("FSFZR")&&!isSkip("FSFZR")) {
				String user=list.get(0).get("FSFZR").toString().substring(0, 
						list.get(0).get("FSFZR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);
			}else if (list.get(0).get("ZZSPR") != null&&!list.get(0).get("ZZSPR").toString().equals("")&&!isNextNode("ZZSPR")&&!isSkip("ZZSPR")) {
				String user=list.get(0).get("ZZSPR").toString().substring(0, 
						list.get(0).get("ZZSPR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);
			}
		}
		return uclist;
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
	
	public boolean isSkip(String sprNode){
		String ownerUserId ="";
		boolean flag=false;
		String userId = "";
		Long taskId=0L;
		String sprId= DBUtil.getString("SELECT SUBSTR("+sprNode+",0, INSTR("+sprNode+",'[',1)-1) USERID FROM BD_ZQB_GPFXXMSPR", "USERID");
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
