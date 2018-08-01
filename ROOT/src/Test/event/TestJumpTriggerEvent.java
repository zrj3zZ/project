package Test.event;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;

/**
 * 流程系统规则跳转触发器
 * @author zouyalei
 *
 */
public class TestJumpTriggerEvent extends SysJumpTriggerEvent {

	public TestJumpTriggerEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一办理节点
	 */
	public String getNextStepId(){
		
		this.getInstanceId();  // 流程实例ID
		this.getTaskId();      // 流程任务ID
		this.getExcutionId();  // 流程实例执行ID
		this.getActDefId();    // 流程ID
		this.getActStepId();   // 流程节点ID
		this.getOwner();       // 当前实例发起人
		
		return "usertask6";
	}
	
	/**
	 * 获取办理人列表
	 */
	public List<UserContext> getNextUserList(){
		List<UserContext> list = new ArrayList<UserContext>();
		list.add(this.getContext());
		return list;
	}
	
	/**
	 * 获得抄送用户列表
	 */
	public List<UserContext> getCCUserList(){
		List<UserContext> list = new ArrayList<UserContext>();
//		list.add(this.getContext());
		return list;
	}
}
