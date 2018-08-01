package com.iwork.demo;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.definition.step.model.ProcessStepRoute;
import com.iwork.process.runtime.pvm.impl.route.SysRouteBaseTriggerEvent;
/**
 * 获得路由自定义触发器
 * @author davidyang
 *
 */
public class ProcessJavaTriggerRouteDemoEvent extends SysRouteBaseTriggerEvent {
	
	public ProcessJavaTriggerRouteDemoEvent(UserContext me,ProcessStepRoute psr,Task task){
		super(me,psr,task);
	}
	
	@Override
	/**
	 * 获取当前节点办理人列表
	 */
	public List<UserContext> getRouteUser() {
		List<UserContext> list = new ArrayList<UserContext>();
		String actDefId = this.getActDefId();
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getUserContext("SUQI");
		list.add(uc);
		return list;
	}

}
