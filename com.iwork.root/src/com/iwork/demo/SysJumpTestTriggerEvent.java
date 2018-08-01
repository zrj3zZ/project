package com.iwork.demo;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
/**
 * 流程节点规则触发器
 * @author davidyang
 *
 */
public class SysJumpTestTriggerEvent extends SysJumpTriggerEvent {
	public SysJumpTestTriggerEvent(UserContext uc,Task task){
		super(uc,task);
	} 
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		// TODO Auto-generated method stub\
		return "usertask5";
	}
	
	/**
	 * 获得抄送用户列表
	 */
	public  List<UserContext> getNextUserList(){
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获得抄送用户列表
	 */
	public  List<UserContext> getCCUserList(){
		 List<UserContext> list = new ArrayList();
		 list.add(this.getContext());
		return list;
	}
}
