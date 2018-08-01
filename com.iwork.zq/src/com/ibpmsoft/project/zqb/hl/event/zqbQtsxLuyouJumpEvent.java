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

public class zqbQtsxLuyouJumpEvent extends SysJumpTriggerEvent{

	public zqbQtsxLuyouJumpEvent(UserContext me, Task task) {
		super(me, task);
	}
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._hlQtsxLcConf.getJd4())){
			return SystemConfig._hlQtsxLcConf.getJd5();
		}else{
			return SystemConfig._hlQtsxLcConf.getEnd();
		}
	}
	
	public List<UserContext> getNextUserList() {
		HashMap<String, Object> processData = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String createuserid = processData.get("CREATEUSERID").toString();
		Map params = new HashMap();
		params.put(1, createuserid);
		String manager = DBUtil.getDataStr("MANAGER", "SELECT USERID AS MANAGER FROM ORGUSER O WHERE O.ISMANAGER=1 AND O.DEPARTMENTID=(SELECT DEPARTMENTID FROM ORGUSER WHERE USERID=?)", params);
		HashMap<String, Object> hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// 获取项目负责人
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		if (manager != null && !manager.toString().equals("")) {
			UserContext uc = UserContextUtil.getInstance().getUserContext(manager);
			uclist.add(uc);
		}
		return uclist;
	}
}
