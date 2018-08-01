package com.ibpmsoft.project.zqb.hl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 挂牌项目立项流程规则跳转
 * 
 * @author wuyao
 * 
 */
public class zqbGpProLuyouJumpSbgzEvent extends SysJumpTriggerEvent {
	public zqbGpProLuyouJumpSbgzEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		HashMap<String, Object> processData = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String actStepId = this.getActStepId();

		List lables = new ArrayList();
		lables.add("MANAGER");
		lables.add("OWNER");
		
		Map params = new HashMap();
		String projectno = processData.get("PROJECTNO")==null?"":processData.get("PROJECTNO").toString();
		params.put(1, projectno);
		
		List<HashMap> userList = DBUtil.getDataList(lables, "SELECT SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) AS MANAGER,SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) AS OWNER FROM BD_ZQB_PJ_BASE B WHERE B.PROJECTNO=?", params);
		String manager = "";
		String owner = "";
		if(userList.size()==1){
			HashMap users = userList.get(0);
			manager = users.get("MANAGER")==null?"":users.get("MANAGER").toString();
			owner = users.get("OWNER")==null?"":users.get("OWNER").toString();
		}
		
		if(actStepId.equals(SystemConfig._hlGpsbgzLcConf.getJd1())&&manager != null && !manager.toString().equals("")){
			return SystemConfig._hlGpsbgzLcConf.getJd2();
		}else if(actStepId.equals(SystemConfig._hlGpsbgzLcConf.getJd2())&&owner != null && !owner.toString().equals("")){
			return SystemConfig._hlGpsbgzLcConf.getJd3();
		}else{
			return SystemConfig._hlGpsbgzLcConf.getJd4();
		}
	}
	
	public List<UserContext> getNextUserList() {
		HashMap<String, Object> processData = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String actStepId = this.getActStepId();

		List lables = new ArrayList();
		lables.add("MANAGER");
		lables.add("OWNER");
		
		Map params = new HashMap();
		String projectno = processData.get("PROJECTNO")==null?"":processData.get("PROJECTNO").toString();
		params.put(1, projectno);
		
		List<HashMap> userList = DBUtil.getDataList(lables, "SELECT SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) AS MANAGER,SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) AS OWNER FROM BD_ZQB_PJ_BASE B WHERE B.PROJECTNO=?", params);
		String manager = "";
		String owner = "";
		if(userList.size()==1){
			HashMap users = userList.get(0);
			manager = users.get("MANAGER")==null?"":users.get("MANAGER").toString();
			owner = users.get("OWNER")==null?"":users.get("OWNER").toString();
		}
		HashMap<String, Object> hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// 获取项目负责人
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		if(actStepId.equals(SystemConfig._hlGpsbzkLcConf.getJd1())&&manager != null && !manager.toString().equals("")){
			UserContext uc = UserContextUtil.getInstance().getUserContext(manager);
			uclist.add(uc);
		}else if(actStepId.equals(SystemConfig._hlGpsbzkLcConf.getJd2())&&owner != null && !owner.toString().equals("")){
			UserContext uc = UserContextUtil.getInstance().getUserContext(owner);
			uclist.add(uc);
		}
		return uclist;
	}
}