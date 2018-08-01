package com.iwork.app.schedule.quartz;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.GetWorkDayTimeMillisecond;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.consts.ProcessSuperviseConsts;
import com.iwork.process.definition.deployment.dao.ProcessDeploymentDAO;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.flow.service.ProcessDefMapService;
import com.iwork.process.definition.step.constant.ProcessStepTriggerConstant;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.model.ProcessStepSupervise;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.definition.step.service.ProcessStepSuperviseService;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.pvm.impl.variable.RouteModel;
import com.iwork.process.runtime.pvm.trigger.ProcessStepSuperviseTriggerEvent;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.iwork.process.runtime.service.ProcessRuntimeSendService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.apache.log4j.Logger;
public class ProcessSuperviseTask{
	private static final String PROCESS_SEND_MAIL_OWNER_TEMPLATE = "流程督办提醒邮件文本_发送至发起人.flt"; 
	private static final String PROCESS_SEND_IM_OWNER_TEMPLATE = "流程督办提醒邮件文本_发送至发起人.flt"; 
	private static final String PROCESS_SEND_SYSMSG_OWNER_TEMPLATE = "流程督办提醒邮件文本_发送至发起人.flt"; 
	private static final String PROCESS_SEND_SMS_OWNER_TEMPLATE = "流程督办提醒邮件文本_发送至发起人.flt"; 
	private static Logger logger = Logger.getLogger(ProcessSuperviseTask.class);

	private static final String PROCESS_SEND_MAIL_TARGET_TEMPLATE = "流程督办提醒邮件文本_发送至当前办理人.flt"; 
	private static final String PROCESS_SEND_IM_TARGET_TEMPLATE = "流程督办提醒邮件文本_发送至当前办理人.flt"; 
	private static final String PROCESS_SEND_SYSMSG_TARGET_TEMPLATE = "流程督办提醒邮件文本_发送至当前办理人.flt"; 
	private static final String PROCESS_SEND_SMS_TARGET_TEMPLATE = "流程督办提醒邮件文本_发送至当前办理人.flt"; 
	
	public  ProcessRuntimeExcuteService processRuntimeExcuteService;
	public  ProcessRuntimeOperateService processRuntimeOperateService;
	public  ProcessRuntimeSendService processRuntimeSendService;
	public  ProcessOpinionService processOpinionService;
	private  ProcessDeploymentDAO processDeploymentDAO;   //流程发布管理操作
	private ProcessStepMapService processStepMapService;
	private ProcessDefMapService processDefMapService;
	private ProcessStepSuperviseService processStepSuperviseService;
	private ProcessStepTriggerService processStepTriggerService;
	private FreeMarkerConfigurer    mailfreemarderConfig;
	private List<Task> reasonList = new ArrayList();
	private List<Task> warningList = new ArrayList();
    public void execute() {
    	//获得全部督办节点模型设置
    	List<ProcessStepSupervise>  list = processStepSuperviseService.getProcessStepSuperviseAllList();
    	for(ProcessStepSupervise supervise:list){
    		String actDefId = supervise.getActDefId();if(actDefId==null)continue;
    		//获得部署模型
    		ProcessDefDeploy deploy = processDeploymentDAO.getDeployModelByActDefId(actDefId);if(deploy==null)continue;
    		//如果状态为关闭，则不进行操作
    		if(deploy.getStatus()==null||deploy.getStatus().equals(SysConst.off))continue;
    		//根据触发条件获取对照时间
    		if(supervise.getSrCondition()==null)continue;
    		//获得节点定义设置
    		ProcessStepMap stepMap = processStepMapService.getProcessDefMapModel(supervise.getPrcDefId(), supervise.getActDefId(), supervise.getActStepId());
    		if(stepMap==null)continue;
    		//办理跟踪触发时长
    		Long timeLong = null;
    		Long waitTime = supervise.getDaleyTime();
    		//依据合理办理时长
    		if(supervise.getSrCondition().equals(new Long(0))){
    			timeLong = stepMap.getStandardTime();
    		}else if(supervise.getSrCondition().equals(new Long(1))){  //依据预警办理时长
    			timeLong = stepMap.getWarningTime();
    		}
    		if(timeLong==null)continue;
    		//获取当前流程当前节点的待办任务
    		List<org.activiti.engine.task.Task> taskList = processRuntimeExcuteService.getTaskService().createTaskQuery().processDefinitionId(actDefId).taskDefinitionKey(supervise.getActStepId()).list();
    		
    		for(org.activiti.engine.task.Task task:taskList){
    			//==================如果当前任务设置为休眠状态，则不进行提醒========================
    			Long isWait = null;
				Object key = processRuntimeExcuteService.getTaskService().getVariable(task.getId(),ProcessTaskConstant.PROCESS_TASK_ACTION_STATUS_KEY);
				if(key!=null){
					if(key instanceof Long){ 
						isWait = (Long)key;
					}else if(key instanceof Integer){
						isWait = ((Integer) key).longValue();
					}else{
						isWait = ProcessTaskConstant.PROCESS_TASK_ACTION_ACTIVE;
					}
				}
				if(isWait!=null&&isWait.equals( new Long(0))){
					continue;
				}
    			//判断计时方式，获取任务起始参照时间
    			if(supervise.getSrType()==null)continue;
    			Date taskTime = null;
    			//按照任务接收时间计算
    			if(supervise.getSrType().equals(new Long(0))){
    				//获取任务接收时间
    				taskTime = task.getCreateTime();
    			}else if(supervise.getSrType().equals(new Long(1))){  //按照任务打开时间
    				Object obj = processRuntimeExcuteService.getTaskService().getVariable(task.getId(), ProcessTaskConstant.PROCESS_TASK_READ_TIME_KEY);
    				if(obj!=null){
    					String tmp = obj.toString();
    					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					try {
    						taskTime = sdf.parse(tmp);
						} catch (ParseException e) {logger.error(e,e);
						}
    				}else{
    					continue;
    				}
    				Object flag = processRuntimeExcuteService.getTaskService().getVariable(task.getId(), ProcessTaskConstant.PROCESS_TASK_SUPERVISE_FLAG);
    				if(flag!=null){
    					continue;
    				}
    			}
    			if(taskTime==null)continue;//未获取任务参照时间
    			//获取出去周六周日
    			GetWorkDayTimeMillisecond  tools = new GetWorkDayTimeMillisecond();
    			Long time = tools.getWorkdayTimeInMillis(taskTime.getTime(), new Date().getTime());
    			//获取时间差小时数
    			Long hour = time/3600000;
    			if(hour>=timeLong+waitTime){
    				//执行督办动作
    				callAction(deploy,supervise,task);
    				StringBuffer info = new StringBuffer();
    				info.append("【督办任务】").append(deploy.getTitle()).append("").append(task.getName()).append("执行类型为“").append(supervise.getSrAction()).append("”的督办动作");
    				LogUtil.getInstance().addLog(Long.parseLong(task.getId()),"督办", info.toString());
    				//设置提醒标识
    				 processRuntimeExcuteService.getTaskService().setVariable(task.getId(), ProcessTaskConstant.PROCESS_TASK_SUPERVISE_FLAG, true);
    			}
    		}
    		
    	}
    }
    
    /**
     * 
     * @param supervise
     * @param task
     * @return
     */
    private boolean callAction(ProcessDefDeploy deploy,ProcessStepSupervise supervise,org.activiti.engine.task.Task task){
    	boolean isrun = true;
    	String assignee = task.getAssignee();
    	if(supervise!=null&&task!=null){
    		if(supervise.getSrAction()==null)return false;
    		
    		String resion ="";
    		if(supervise.getSrCondition().equals(new Long(0))){
    			resion = "超出合理办理时间";
    		}else if(supervise.getSrCondition().equals(new Long(1))){  //依据预警办理时长
    			resion = "超出预警办理时间";
    		}
    		switch(supervise.getSrAction().intValue()){
    			//给发起人发送通知
    		case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_NOTICE_OWNER:
	    		 String userid = task.getOwner();
	    		 String targetUser = task.getAssignee();
	    		 String remindType = supervise.getRemindType(); 
	    		 String title = "【任务提醒】"+task.getDescription();
	    		 Map root = new HashMap();
    			 root.put("SYSTEMNAME", SystemConfig._iworkServerConf.getShortTitle());
    			 root.put("SOURCE", SystemConfig._iworkServerConf.getTitle()); //	 信息来源
	    		 root.put("OWNER", userid);
	    		 root.put("TARGETUSER", targetUser);
	    		 root.put("TASKTITLE", title);
	    		 if(remindType!=null&&!remindType.equals("")){
	    			 String[] types = remindType.split(",");
	    			 for(String type:types){ 
	    				 if(type.equals("_sysmsg")){
	    					 StringBuffer content = new StringBuffer();
	    					 content.append("您发起的流程标题为[").append(task.getDescription()).append("]的流程任务已在").append(UserContextUtil.getInstance().getUserName(targetUser)).append("账户中").append(resion).append(",请通知及时办理，或收回此任务重新选择办理人！");
	    					 StringBuffer url = new StringBuffer();
	    					 url.append("").append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
	    					 MessageAPI.getInstance().sendSysMsg(userid, title, content.toString(), url.toString());
	    				 }else if(type.equals("_sms")){ 
	    					 StringBuffer url = new StringBuffer();
	    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
	    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    					 if(uc!=null&&uc._userModel.getMobile()!=null){
	    						 String content = "标题为"+task.getDescription()+"的任务已"+resion+"";
	    						 MessageAPI.getInstance().sendSMS(uc, uc._userModel.getMobile(), content);
	    					 } 
	    				 }else if(type.equals("_im")){
	    					 StringBuffer content = new StringBuffer();
    						 content.append("标题为").append(task.getDescription()).append("的任务已在").append(UserContextUtil.getInstance().getUserName(targetUser)).append("账户中").append(resion).append("请提醒办理人及时办理，或收回此任务重新选择办理人！");
	    					 StringBuffer url = new StringBuffer();
	    					 MessageAPI.getInstance().sendIM(SecurityUtil.supermanager, userid, title, content.toString());
	    				 }else if(type.equals("_email")){
	    					 StringBuffer url = new StringBuffer();
	    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
	    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    					 if(uc!=null&&uc._userModel.getEmail()!=null){
	    						 StringBuffer content = new StringBuffer();
	    						 content.append("标题为").append(task.getDescription()).append("的任务已在").append(UserContextUtil.getInstance().getUserName(targetUser)).append("账户中").append(resion).append("请提醒办理人及时办理，或收回此任务重新选择办理人！");
	    						 MessageAPI.getInstance().sendSysMail("任务提醒", uc._userModel.getEmail(), title, content.toString());
	    					 }
	    				 }else if(type.trim().equals("_weixin")){
    						 StringBuffer content = new StringBuffer();
    						 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
    						 UserContext taskOwner = UserContextUtil.getInstance().getUserContext(assignee);
    						 content.append("标题为[").append(task.getDescription()).append("]的任务已").append(resion).append("\n");
    						 content.append("根据督办策略，通知指定用户[").append(uc.get_userModel().getUsername()).append("],请督促当前办理人[").append(taskOwner.get_userModel().getUsername()).append("],及时处理！");
	    					 if(uc!=null&&uc._userModel.getEmail()!=null){
	    						 MessageAPI.getInstance().sendWeiXin(userid, content.toString());
	    					 }
	    				 }
	    			 }
	    		 }
	    		 //给当前人发送提醒通知
    		case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_NOTICE_ASSIGNEE:
	    			  userid = task.getAssignee();
		    		  remindType = supervise.getRemindType();
		    		  title = "【任务提醒】"+task.getDescription();
		    		 if(remindType!=null&&!remindType.equals("")){
		    			 String[] types = remindType.split(",");
		    			 for(String type:types){
		    				 if(type.trim().equals("_sysmsg")){
		    					 String content = "您办理的流程标题为["+task.getDescription()+"]的任务已"+resion+",请及时办理！";
		    					 StringBuffer url = new StringBuffer();
		    					 url.append("").append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 MessageAPI.getInstance().sendSysMsg(userid, title, content, url.toString());
		    				 }else if(type.trim().equals("_sms")){
		    					 StringBuffer url = new StringBuffer();
		    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		    					 if(uc!=null&&uc._userModel.getMobile()!=null){
		    						 String content = "标题为"+task.getDescription()+"的任务已"+resion+",请及时办理！";
		    						 MessageAPI.getInstance().sendSMS(uc, uc._userModel.getMobile(), content);
		    					 }
		    				 }else if(type.trim().equals("_im")){
		    					 String content = "您办理的流程标题为["+task.getDescription()+"]的任务已"+resion+",请及时办理！";
		    					 StringBuffer url = new StringBuffer();
		    					 MessageAPI.getInstance().sendIM(SecurityUtil.supermanager, userid, title, content);
		    				 }else if(type.trim().equals("_email")){
		    					 StringBuffer url = new StringBuffer();
		    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		    					 if(uc!=null&&uc._userModel.getEmail()!=null){
		    						 String content = "标题为"+task.getDescription()+"的任务已"+resion+",请及时办理！";
		    						 MessageAPI.getInstance().sendSysMail("任务提醒", uc._userModel.getEmail(), title, content);
		    					 }
		    				 }else if(type.trim().equals("_weixin")){
	    						 StringBuffer content = new StringBuffer();
	    						 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    						 UserContext taskOwner = UserContextUtil.getInstance().getUserContext(assignee);
	    						 content.append("标题为[").append(task.getDescription()).append("]的任务已").append(resion).append("\n");
	    						 content.append("根据督办策略，通知指定用户[").append(uc.get_userModel().getUsername()).append("],请督促当前办理人[").append(taskOwner.get_userModel().getUsername()).append("],及时处理！");
		    					 if(uc!=null&&uc._userModel.getEmail()!=null){
		    						 MessageAPI.getInstance().sendWeiXin(userid, content.toString());
		    					 }
		    				 }
		    			 }
		    		 }
    		case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_NEXTACT:
    			String actDefId = task.getProcessDefinitionId();
    			String actStepDefId = task.getTaskDefinitionKey();
    			Long prcDefId = deploy.getId();
    			Long instanceId = Long.parseLong(task.getProcessInstanceId());
    			Long formId = ProcessAPI.getInstance().getFormIdByInstanceId(Long.parseLong(task.getProcessInstanceId()), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
    			HashMap tableMap =  ProcessAPI.getInstance().getFromData(instanceId, formId,  EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
    			String taskId = task.getId();
    			Long excutionId = Long.parseLong(task.getExecutionId());
    			HashMap triggerParams = new HashMap();
				///#########################################表单办理前触发器##################################################################################
				/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_TRAN_BEFOR);
				/**/	if(triggerModel!=null){
				/**/    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				/**/   		HttpServletRequest request = ServletActionContext.getRequest();
				/**/   		HashMap params = (HashMap)request.getParameterMap(); 
				/**/		//初始化触发器参数
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,task.getTaskDefinitionKey());
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params);  
				/**/		isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
				/**/	} 
				/**///#########################################END#######################################################################################
				if(!isrun){
					return false;
				}
    			 //获取下一个办理节点
				RouteModel rm = processRuntimeOperateService.executeHandle(actDefId, prcDefId, actStepDefId, taskId, tableMap);
				String nextStepId = rm.getNextStepId();
				String nextStepName = rm.getNextStepName();
    			//获取下一个办理人
				HashMap hm = rm.getFixedNextUser();
				String[] receiveUser = new String[hm.size()];
				int num = 0;
				Iterator iter = hm.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next(); Object key = entry.getKey();
					Object val = entry.getValue();
					receiveUser[num] = ObjectUtil.getString(val);
					num++;
				}
    			//执行发送
				processRuntimeSendService.executeSend(actDefId, prcDefId, actStepDefId, instanceId, excutionId, taskId, nextStepId, receiveUser, tableMap);
    			//添加自动办理意见
    			//提醒当前用户
    			//提醒发起人
    			
			case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_FORWARD_MANAGER:
				
			case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_BACK_SENDER:
    			  
			case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_KILL:
	    		//执行一个触发器事件
			case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_TRIGGER:
	    			String triggerClass = supervise.getTriggerEvent();
	    			if(triggerClass!=null&&!triggerClass.equals("")){
	    				BaseTriggerModel model = new BaseTriggerModel();
	    				model.setEventType(BaseTriggerModel.TRIGGER_TYPE_SUPERVISE_EVENT);
	    				model.setClassName(triggerClass);
	    				model.setTiggerType("superviseEvent"); //计划任务
	    				model.setTriggerUser(SecurityUtil.supermanager);
	    				triggerParams = new HashMap(); 
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.PROCESS_PARAMETER_ACTDEFID,task.getProcessDefinitionId());
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,task.getTaskDefinitionKey());
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.PROCESS_PARAMETER_INSTANCEID,task.getProcessInstanceId());
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.PROCESS_PARAMETER_TASKID,task.getId());
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.PROCESS_PARAMETER_EXECUTEID,task.getExecutionId());
	    				/**/		triggerParams.put(ProcessStepSuperviseTriggerEvent.SUPERVISE_EXECUTE_MODEL,supervise);
	    				isrun = TriggerAPI.getInstance().excuteEvent(model, UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager), TriggerAPI.THREAD_TYPE_SYNC, triggerParams,Long.parseLong(task.getProcessInstanceId()));
//	    				if(isrun){
//	    				}
	    			}
	    			//给指定用户发通知
				case ProcessSuperviseConsts.PROCESS_SUPERVISE_TYPE_NOTICE_USER:;
	    		userid = supervise.getEnumParams();
	    		if(userid!=null){
	    			  remindType = supervise.getRemindType();
		    		  title = "【督办提醒】"+task.getDescription();
		    		 if(remindType!=null&&!remindType.equals("")){
		    			 String[] types = remindType.split(",");
		    			 for(String type:types){
		    				 if(type.trim().equals("_sysmsg")){
		    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		    					 StringBuffer content = new StringBuffer();
	    						 UserContext taskOwner = UserContextUtil.getInstance().getUserContext(assignee);
	    						 content.append("标题为[").append(task.getDescription()).append("]的任务已").append(resion).append("\n");
	    						 content.append("根据督办策略，通知指 定用户[").append(uc.get_userModel().getUsername()).append("],请督促当前办理人[").append(taskOwner.get_userModel().getUsername()).append("],及时处理！");
		    					 StringBuffer url = new StringBuffer();
		    					 url.append("").append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 MessageAPI.getInstance().sendSysMsg(userid, title, content.toString(), url.toString());
		    				 }else if(type.trim().equals("_sms")){
		    					 StringBuffer url = new StringBuffer();
		    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		    					 if(uc!=null&&uc._userModel.getMobile()!=null){
		    						 String content = "标题为"+task.getDescription()+"的任务已"+resion+",请及时办理！";
		    						 MessageAPI.getInstance().sendSMS(uc, uc._userModel.getMobile(), content);
		    					 }
		    				 }else if(type.trim().equals("_im")){
		    					 String content = "流程标题为["+task.getDescription()+"]的任务已"+resion+"！";
		    					 StringBuffer url = new StringBuffer();
		    					 MessageAPI.getInstance().sendIM(SecurityUtil.supermanager, userid, title, content);
		    				 }else if(type.trim().equals("_email")){
		    					 StringBuffer url = new StringBuffer();
		    					 url.append(SystemConfig._iworkServerConf.getLoginURL()).append("/").append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
		    					 StringBuffer content = new StringBuffer();
	    						 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    						 UserContext taskOwner = UserContextUtil.getInstance().getUserContext(assignee);
	    						 content.append("标题为[").append(task.getDescription()).append("]的任务已").append(resion).append("\n");
	    						 content.append("根据督办策略，通知指定用户[").append(uc.get_userModel().getUsername()).append("],请督促当前办理人[").append(taskOwner.get_userModel().getUsername()).append("],及时处理！");
		    					   MessageAPI.getInstance().sendSysMail("督办提醒", uc._userModel.getEmail(), title, content.toString());
		    				 }else if(type.trim().equals("_weixin")){
		    						 StringBuffer content = new StringBuffer();
		    						 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		    						 UserContext taskOwner = UserContextUtil.getInstance().getUserContext(assignee);
		    						 content.append("标题为[").append(task.getDescription()).append("]的任务已").append(resion).append("\n");
		    						 content.append("根据督办策略，通知指定用户[").append(uc.get_userModel().getUsername()).append("],请督促当前办理人[").append(taskOwner.get_userModel().getUsername()).append("],及时处理！");
			    					 if(uc!=null&&uc._userModel.getEmail()!=null){
			    						 MessageAPI.getInstance().sendWeiXin(userid, content.toString());
			    					 }
		    					 
		    				 }
		    			 }
		    		 }
	    		}
    		}
    	}
    	return isrun;
    }
    /**
	 * 获得装载后的正文
	 * @param templateName
	 * @param root
	 * @return
	 */
	private String getBuildContent(String templateName,Map root){
		StringWriter stringWriter = new StringWriter();
		if(mailfreemarderConfig!=null){
			mailfreemarderConfig = (FreeMarkerConfigurer)SpringBeanUtil.getBean("mailfreemarderConfig");
		}
		try {
				Configuration freemarkerCfg = mailfreemarderConfig.createConfiguration();
				freemarkerCfg.setDefaultEncoding("GB2312"); 
				Template template = null;
				try{
					template = freemarkerCfg.getTemplate(templateName);
					 template.setEncoding("GB2312");
					//template.setEncoding("UTF-8");
				}catch(Exception e){
					logger.error(e,e);
				}
				if(template==null){
					MessageQueueUtil.getInstance().putTipWinMsg(SecurityUtil.supermanager, "系统异常","<h3>未发现定义的名称为【"+templateName+"】的表单模版</h3>");
					return "";
				}
				BufferedWriter writer = new BufferedWriter(stringWriter);
				TemplateElement te=template.getRootTreeNode();
			template.process(root, writer);
			//StringReader reader = new StringReader(stringWriter.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error(e,e);
			MessageQueueUtil.getInstance().putTipWinMsg(SecurityUtil.supermanager, "系统异常","数据异常，请联系管理员!<br>["+e.getMessage()+"]");
		}
		return stringWriter.toString();
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

	public void setProcessStepSuperviseService(
			ProcessStepSuperviseService processStepSuperviseService) {
		this.processStepSuperviseService = processStepSuperviseService;
	}

	public void setMailfreemarderConfig(FreeMarkerConfigurer mailfreemarderConfig) {
		this.mailfreemarderConfig = mailfreemarderConfig;
	}

	public void setProcessRuntimeOperateService(
			ProcessRuntimeOperateService processRuntimeOperateService) {
		this.processRuntimeOperateService = processRuntimeOperateService;
	}

	public void setProcessRuntimeSendService(
			ProcessRuntimeSendService processRuntimeSendService) {
		this.processRuntimeSendService = processRuntimeSendService;
	}

	public void setProcessStepTriggerService(
			ProcessStepTriggerService processStepTriggerService) {
		this.processStepTriggerService = processStepTriggerService;
	}

	public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
		this.processOpinionService = processOpinionService;
	}
}
