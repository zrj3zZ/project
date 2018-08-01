package com.iwork.app.schedule.quartz;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.TaskService;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.constant.SysPersionRemindConstant;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.server.ge.constant.GeVariableConstant;
import com.iwork.core.server.ge.model.SysGeProperty;
import com.iwork.core.util.SystemVariableUtil;
import com.iwork.core.util.TemplateUtil;
import com.iwork.process.definition.deployment.dao.ProcessDeploymentDAO;
import com.iwork.process.definition.flow.service.ProcessDefMapService;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.sdk.MessageAPI;
/**
 * 当用户设置了定时提醒功能后，系统将每间隔2小时，将新增的待办事宜发送给待办用户
 * @author davidyang
 *
 */
public class ProcessTodoTask{
	private static final String PROCESS_SEND_MAIL_OWNER_TEMPLATE_TITLE = "新增待办任务定时提醒邮件标题.flt"; 
	private static final String PROCESS_SEND_MAIL_OWNER_TEMPLATE = "新增待办任务定时提醒邮件文本.flt"; 
	private static final String PROCESS_TASK_SEND_NOTICE_VARIABLE_NAME = "ProcesTaskNotice_key"; 
	public static  Date EXECUTE_TIME; 
	private static Logger logger = Logger.getLogger(ProcessTodoTask.class);

	public  TaskService taskService;
	public  ProcessRuntimeExcuteService processRuntimeExcuteService;
	private  ProcessDeploymentDAO processDeploymentDAO;   //流程发布管理操作
	private ProcessStepMapService    processStepMapService               ;
	private ProcessDefMapService           processDefMapService          ;
	private FreeMarkerConfigurer    mailfreemarderConfig          ;
	
    public void execute() {
    	List<UserContext> userlist = this.getTimerUsers();
    	if(userlist==null)return;
    	//设置调用时间
    	SysGeProperty model = SystemVariableUtil.getInstance().getVaroable(GeVariableConstant.GE_VARIABLE_TODO_TIME);
    	for(UserContext uc:userlist){
    		List<org.activiti.engine.task.Task> senderList = new ArrayList();
    		List<org.activiti.engine.task.Task> list = taskService.createTaskQuery().taskAssignee(uc.get_userModel().getUserid()).list();
    		if(list!=null&&list.size()>0){
    			int sumTaskNum = list.size();
    			for(org.activiti.engine.task.Task task:list){
    				Object obj = taskService.getVariable(task.getId(),PROCESS_TASK_SEND_NOTICE_VARIABLE_NAME);
    				if(obj==null){
    					senderList.add(task);
    					taskService.setVariableLocal(task.getId(),PROCESS_TASK_SEND_NOTICE_VARIABLE_NAME,"已发送 ");
    				}
    			}
    			if(senderList.size()>0){
    				this.callAction(senderList, sumTaskNum,uc,model);
    			}
    		}
    	}
    	if(model==null){
    		model = new SysGeProperty();
    		model.setName(GeVariableConstant.GE_VARIABLE_TODO_TIME);
    		model.setValue(UtilDate.getNowDatetime());
    		model.setMemo("记录最后一次定时获取待办任务列表，发送邮件推送时间");
    		SystemVariableUtil.getInstance().saveVaroable(model);
    	}else{
    		model.setValue(UtilDate.getNowDatetime());
    		SystemVariableUtil.getInstance().saveVaroable(model);
    	}
    } 
    
    /**
     * 获取设置定时提醒的用户列表
     * @return
     */
    public List<UserContext> getTimerUsers(){
    	StringBuffer sql = new StringBuffer("select distinct Userid from sys_person_config WHERE TYPE = ? and value='1'");
    	List<UserContext> userlist = new ArrayList();
    	Connection conn = DBUtil.open();
    	PreparedStatement stmt = null;
    	ResultSet rset = null;
    	try {
    		stmt = conn.prepareStatement(sql.toString());
    		stmt.setString(1, SysPersionRemindConstant.PERSION_REMIND_TYPE);
    		rset = stmt.executeQuery();
    		while(rset.next()){
    			String userid = rset.getString("USERID");
    			if(userid!=null){
    				UserContext context = UserContextUtil.getInstance().getUserContext(userid);
    				if(context!=null){
    					userlist.add(context);
    				}
    			}
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rset);
    	}
    	
    	return userlist;
    	
    }
    
    /**
     * 
     * @param supervise
     * @param task
     * @return
     */
    private boolean callAction(List<org.activiti.engine.task.Task> senderlist,int count,UserContext receveContext,SysGeProperty model){
    	StringBuffer content = new StringBuffer();
    	for(org.activiti.engine.task.Task task:senderlist){
    		String from = UserContextUtil.getInstance().getUserName(task.getOwner()); //来自
    		String sendtime = "";
    		//发送时间
    		Date date = task.getCreateTime();
    		if(date!=null){
    			sendtime = UtilDate.datetimeFormat(date,"HH:mm");
    		} 
    		//任务标题
    		StringBuffer linkUrl = new StringBuffer();
    		linkUrl.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
    		String title = task.getDescription();
    		content.append("<tr >").append("\n"); 
    		content.append("<td style=\"padding:2px;border-bottom:1px solid #666;border-right:1px solid #666\">").append(from).append("</td>").append("\n");
    		content.append("<td style=\"padding:2px;border-bottom:1px solid #666;border-right:1px solid #666\"\">").append(sendtime).append("</td>").append("\n");
    		content.append("<td style=\"padding:2px;border-bottom:1px solid #666;border-right:1px solid #666\"\"><a target='_blank' href='").append(linkUrl.toString()).append("'>").append(title).append("</td>").append("\n"); 
    		content.append("</tr>").append("\n"); 
    	}
    	HashMap root = new HashMap();
    	root.put("USERNAME", receveContext.get_userModel().getUsername());
    	if(model==null){ 
    		root.put("TODO_DATE",UtilDate.getNowDatetime()); 
    	}else{ 
    		//获得当前小时
        	int hour = Calendar.getInstance().get(Calendar.HOUR); 
        	int start_hour = hour - 2;
        	root.put("START_TIME", start_hour+":00");
        	root.put("END_TIME",  hour+":00");  
        	String todo_dateStr = "";
        	Date upDate = UtilDate.StringToDate(model.getValue(),"yyyy-MM-dd hh:mm:ss");
        	if(upDate!=null){
        		Date currentDate = Calendar.getInstance().getTime();
        		//判断日期是否一致
        		if(UtilDate.dateFormat(upDate).equals(UtilDate.dateFormat(currentDate))){
        			todo_dateStr = UtilDate.dateFormat(currentDate)+" ["+UtilDate.datetimeFormat(upDate,"HH:mm")+"] 至 ["+UtilDate.datetimeFormat(currentDate,"HH:mm")+"]";
        		}else{
        			todo_dateStr = " ["+UtilDate.datetimeFormat(upDate,"yyyy-MM-dd hh:mm")+"] 至 ["+UtilDate.datetimeFormat(currentDate,"yyyy-MM-dd hh:mm")+"]";
        		}
        	}
    		root.put("TODO_DATE", todo_dateStr); 
        	
    	}
    	
    	root.put("NEW_COUNT",  senderlist.size());
    	root.put("TASK_SUM",  count);
    	root.put("TASK_LIST",  content.toString());
    	root.put("LOGINURL",  SystemConfig._iworkServerConf.getLoginURL());
    	root.put("SYSTEMNAME",  SystemConfig._iworkServerConf.getShortTitle());
    	root.put("SOURCE",  SystemConfig._iworkServerConf.getTitle()); 
    	String sendContent = TemplateUtil.getInstance().getBuildContent(PROCESS_SEND_MAIL_OWNER_TEMPLATE, root);
    	String sendTitle = TemplateUtil.getInstance().getBuildContent(PROCESS_SEND_MAIL_OWNER_TEMPLATE_TITLE, root);
    	MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), receveContext.get_userModel().getEmail(), sendTitle, sendContent);
    	return false;
    } 
	
	public void setProcessRuntimeExcuteService(
			ProcessRuntimeExcuteService processRuntimeExcuteService) {
		this.processRuntimeExcuteService = processRuntimeExcuteService;
	}

	public void setProcessDeploymentDAO(ProcessDeploymentDAO processDeploymentDAO) {
		this.processDeploymentDAO = processDeploymentDAO;
	}

	public void setProcessStepMapService(ProcessStepMapService processStepMapService) {
		this.processStepMapService = processStepMapService;
	}

	public void setProcessDefMapService(ProcessDefMapService processDefMapService) {
		this.processDefMapService = processDefMapService;
	}

	public void setMailfreemarderConfig(FreeMarkerConfigurer mailfreemarderConfig) {
		this.mailfreemarderConfig = mailfreemarderConfig;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
    
}
