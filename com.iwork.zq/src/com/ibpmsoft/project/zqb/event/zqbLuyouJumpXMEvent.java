package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务流程规则跳转触发第一步
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpXMEvent extends SysJumpTriggerEvent {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public zqbLuyouJumpXMEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._xmsplcConf.getJd1())){
			if(isExists("ZKR")&&!isNextNode("ZKR")&&!isSkip("ZKR")){
				return SystemConfig._xmsplcConf.getJd2();
			}
			else if(isExists("NHSPR")&&!isNextNode("NHSPR")&&!isSkip("NHSPR")){
				return SystemConfig._xmsplcConf.getJd3();
			}else{
				return SystemConfig._xmsplcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmsplcConf.getJd2())){
			if(isExists("NHSPR")&&!isNextNode("NHSPR")&&!isSkip("NHSPR")){
				return SystemConfig._xmsplcConf.getJd3();
			}else{
				return SystemConfig._xmsplcConf.getEnd();
			}
		}
		return null;
	}
	
	public boolean isExists(String filedName){
		HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		String spr="";
		if("ZKR".equals(filedName)){
			spr= DBUtil.getString("select * from BD_ZQB_ZKNHSPR ", filedName);
		}else if("NHSPR".equals(filedName)){
			spr= DBUtil.getString("select * from BD_ZQB_ZKNHSPR ", filedName);
		}
		if(spr!=null&&!spr.equals("")){
			return true;
		}
		 return false;
	}
	
	public boolean isNextNode(String filedName){
		HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		String sprId= DBUtil.getString("SELECT SUBSTR("+filedName+",0, INSTR("+filedName+",'[',1)-1) USERID FROM BD_ZQB_ZKNHSPR", "USERID");
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			return false;
		}
		return true;
    }
	
	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(
				this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String dem="33833384d109463285a6a348813539f1";
		HashMap<String, Object> condition= new HashMap<String, Object>();
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l=DemAPI.getInstance().getList(dem, condition, null);
		if(l.size()>0){
			hash=l.get(0);
		}
		String actStepId = this.getActStepId();
		// 获取项目负责人
		if (hash.get("OWNER") != null && !hash.get("OWNER").toString().equals("")) {
			// 获取当前客户
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			String demUUID = config.get("nhzkuuid");
			if (isExists("ZKR")&&!isNextNode("ZKR")&&!isSkip("ZKR")) {// 跳转到所设置的审批人1
				List<HashMap> list = DemAPI.getInstance().getList(demUUID,conditionMap, null);
				if (list.size() == 1) {
					String user=list.get(0).get("ZKR").toString().substring(0, 
							list.get(0).get("ZKR").toString().indexOf("["));
					UserContext uc = UserContextUtil.getInstance()
							.getUserContext(user);
					uclist.add(uc);
				}
			} else if(isExists("NHSPR")&&!isNextNode("NHSPR")&&!isSkip("NHSPR")) {
				List<HashMap> list = DemAPI.getInstance().getList(demUUID,conditionMap, null);
				if (list.size() == 1) {
					String user=list.get(0).get("NHSPR").toString().substring(0, 
							list.get(0).get("NHSPR").toString().indexOf("["));
					UserContext uc = UserContextUtil.getInstance()
							.getUserContext(user);
					uclist.add(uc);
				}
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
		String sprId= DBUtil.getString("SELECT SUBSTR("+sprNode+",0, INSTR("+sprNode+",'[',1)-1) USERID FROM BD_ZQB_ZKNHSPR", "USERID");
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