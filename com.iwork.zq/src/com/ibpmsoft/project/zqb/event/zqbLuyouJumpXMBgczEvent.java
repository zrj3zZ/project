package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目任务流程规则跳转触发第一步
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpXMBgczEvent extends SysJumpTriggerEvent {
	public zqbLuyouJumpXMBgczEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(actStepId.equals(SystemConfig._bmfzrspConf.getJd1())){
			return SystemConfig._bmfzrspConf.getJd2();
		}else{
			return SystemConfig._bmfzrspConf.getEnd();
		}
	}
	
	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		// 获取项目负责人
		if (hash.get("OWNER") != null && !hash.get("OWNER").toString().equals("")) {
			String ownerId = hash.get("OWNER").toString().substring(0,hash.get("OWNER").toString().indexOf("["));
			UserContext uc = UserContextUtil.getInstance().getUserContext(ownerId);
			uclist.add(uc);
		}
		return uclist;
	}
}