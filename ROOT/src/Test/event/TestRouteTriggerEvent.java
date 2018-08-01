package Test.event;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.definition.step.model.ProcessStepRoute;
import com.iwork.process.runtime.pvm.impl.route.SysRouteBaseTriggerEvent;

/**
 * 流程节点路由触发器
 * @author zouyalei
 * TestRouteTriggerEvent
 */
public class TestRouteTriggerEvent extends SysRouteBaseTriggerEvent {

	public TestRouteTriggerEvent(UserContext me, ProcessStepRoute psr, Task task) {
		super(me, psr, task);
	}

	public List<UserContext> getRouteUser(){
		
		List<UserContext> list = new ArrayList<UserContext>();
		this.getInstanceId(); // 流程实例ID
		this.getTaskId();     // 获取当前流程任务ID
		this.getExcutionId(); // 获取流程实例执行ID
		this.getActDefId();   // 获取流程模型ID
		this.getActStepId();  // 获取流程节点ID
		this.getContext();    // 获取当前用户上下文
		this.getProcessStepRouteModel();  // 获得路由模型设置
		this.getTask();       // 获得当前任务对象
		
		
		UserContext uc = UserContextUtil.getInstance().getUserContext("ADMIN");
		list.add(uc);
		
		return list;
	}
}
