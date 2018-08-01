package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 路由跳转触发器
 * @author zouyalei
 *
 */
public class zqbLuyouJumpEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		return "usertask3";
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> list = new ArrayList<UserContext>();
		Long lc_instanceId = this.getInstanceId();
		HashMap<String,Object> hash = ProcessAPI.getInstance().getFromData(lc_instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		 String userid = "DU1";
		 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		 list.add(uc);
		 return list;
	}
	
	
	/**
	 * 获得抄送用户列表
	 */
	public  List<UserContext> getCCUserList(){
		 List<UserContext> list = new ArrayList<UserContext>();
		 String userid = "DU1";
		 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		 list.add(uc);
		 return list;
	}

}
