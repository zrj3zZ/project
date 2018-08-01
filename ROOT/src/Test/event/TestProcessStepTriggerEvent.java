package Test.event;

import java.util.HashMap;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;

/**
 * 流程节点触发器
 * @author zouyalei
 *
 */
public class TestProcessStepTriggerEvent extends ProcessStepTriggerEvent {
	
	public TestProcessStepTriggerEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}

	public boolean execute(){
		
		Long taskId = this.getTaskId();
		TaskService taskService = (TaskService)SpringBeanUtil.getBean("taskService");
		Task task = taskService.createTaskQuery().taskId(taskId+"").singleResult();
		taskService.setVariable(taskId+"", ProcessTaskConstant.PROCESS_TASK_STEP_SIGN_USERS, "ADMIN,YANGLIANFENG,ZHUXIAOXUE");
		return true;
	}
}
