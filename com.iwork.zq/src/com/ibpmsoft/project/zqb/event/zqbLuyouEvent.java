package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.definition.step.model.ProcessStepRoute;
import com.iwork.process.runtime.pvm.impl.route.SysRouteBaseTriggerEvent;

/**
 * 路由选择触发器
 * @author zouyalei
 *
 */
public class zqbLuyouEvent extends SysRouteBaseTriggerEvent {

	public zqbLuyouEvent(UserContext me, ProcessStepRoute psr, Task task) {
		super(me, psr, task);
	}
	
	/**
	*获取当前节点办理人列表
	*/
	public List<UserContext> getRouteUser() {
		List<UserContext> list = new ArrayList<UserContext>();
		//获取流程ID
		String actDefId = this.getActDefId();
		//获得当前流程实例ID
		Long instanceId = this.getInstanceId();
		String userid = "A01";
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		list.add(uc);
		return list;
	}
}
