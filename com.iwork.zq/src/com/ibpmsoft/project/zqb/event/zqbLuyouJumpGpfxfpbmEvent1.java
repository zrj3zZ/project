package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务流程规则跳转触发第一步
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpGpfxfpbmEvent1 extends SysJumpTriggerEvent {
	public zqbLuyouJumpGpfxfpbmEvent1(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(actStepId.equals(SystemConfig._gpfxfpbmConf.getJd1())){
			return SystemConfig._gpfxfpbmConf.getJd2();
		}else if(actStepId.equals(SystemConfig._gpfxfpbmConf.getJd2())){
			return SystemConfig._gpfxfpbmConf.getJd3();
		}else{
			return SystemConfig._gpfxfpbmConf.getEnd();
		}
	}
	
	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行分配部门审批人'", "UUID");
		List<HashMap> alldata = DemAPI.getInstance().getAllList(uuid, null, null);
		
		// 获取审批人
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String ownerId = hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("["));
		if(ownerId != null && !ownerId.equals("")){
			UserContext uc = UserContextUtil.getInstance().getUserContext(ownerId);
			uclist.add(uc);
		}else if (alldata.get(0) != null && !alldata.get(0).get("USERNAME").toString().equals("")) {
			String username = alldata.get(0).get("USERNAME").toString().substring(0,alldata.get(0).get("USERNAME").toString().indexOf("["));
			UserContext uc = UserContextUtil.getInstance().getUserContext(username);
			uclist.add(uc);
		}
		return uclist;
	}
}