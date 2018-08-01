package com.iwork.app.mobile.process.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.definition.step.constant.ProcessStepTriggerConstant;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.constant.SendPageTypeConstant;
import com.iwork.process.runtime.pvm.impl.execute.PvmProcessSecurityEngine;
import com.iwork.process.runtime.pvm.impl.variable.RouteModel;
import com.iwork.process.runtime.pvm.impl.variable.SendPageModel;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.opensymphony.xwork2.ActionSupport;

public class SyncMobileProcessOperateAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(SyncMobileProcessOperateAction.class);

	private ProcessRuntimeOperateService processRuntimeOperateService;
	private ProcessStepTriggerService processStepTriggerService;//流程节点触发器模型
	
	//表单参数变量
	private String actDefId;  //流程模型ID
	private Long prcDefId ;	//流程定义ID
	private String actStepDefId ="";//流程节点ID
	private Long formId ;//表单ID
	private String taskId ="0";//任务ID
	private String currentUser;//当前用户
	private String action;//动作
	private Long instanceId;//实例ID
	private Long excutionId ;	//执行动作ID
	private Long dataid;//行数据ID
	private List<SendPageModel> sendList;
	private String mjId;  //审核跳转模型ID
	private String targetStepId;  //目标办理节点
	private String targetStepName;  //目标办理节点
	private String title;  //任务标题
	private String deviceType ;    //设备类型
	private String ccUsers;  //目标抄送用户
	private List<OrgUser> receiveUser;  //任务接收用户
	private HashMap backUser;  //任务接收用户
	private HashMap ccUser;  //抄送接收用户
	private String opinion1;  //办理意见
	private Long isOpinion;  //是否显示办理意见
	private int maxUser;   //最大用户  
	private Long ccinstal = 0L; //判断是否允许抄送
	private String tipsInfo; //提示信息
	private String inputField; //提示信息
	private Long backType; //返回类型   0：返回申请人  1：返回上一办理人
	private HashMap remindTypeList;  //流程通知列表
	
	/**
	 * 点击办理，获得发送页面
	 */
	public String executeHandle(){
		if(actDefId!=null&&actStepDefId!=null&&instanceId!=null&&excutionId!=null&&taskId!=null){
			//获得审核意见列表
			opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			//获得通知类型列表
			remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
			//安全校验，判断当前任务是否为当前用户办理
			boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
			if(flag){
				/**/    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				/**/   		HttpServletRequest request = ServletActionContext.getRequest();
				/**/   		HashMap params = (HashMap)request.getParameterMap(); 
				HashMap triggerParams = new HashMap();
				
				///#########################################表单办理前触发器##################################################################################
				/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_TRAN_BEFOR);
				/**/	boolean isrun = true;
				/**/	if(triggerModel!=null){
				/**/		//初始化触发器参数
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params); 
				/**/		isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
				/**/	}
				/**///#########################################END#######################################################################################
				if(isrun){
					RouteModel model = processRuntimeOperateService.executeHandle(actDefId, prcDefId, actStepDefId, taskId,params);
					if(model!=null){
						ccinstal = model.isCC();//判断是否允许抄送
						ccUsers = model.getCcUser();
						title = model.getTitle(); //设置任务标题
						isOpinion = model.getIsOpinion();  //判断是否允许审核意见填写
						if(!model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES)){   //判断是否为归档页面
							List<SendPageModel> sendList = new ArrayList();
							SendPageModel sendModel = new SendPageModel();
						    //装在节点名称及ID
						    sendModel.setTargetStepId(model.getNextStepId());
						    //获取下一节点名称
						    String t_activityName = model.getNextStepName();
						    if(t_activityName!=null){ 
						    	sendModel.setTargetStepName(t_activityName);
						    }
						    //获取接收人地址HTML
						    sendModel.setAddressHTML(model.getAddressHTML());
						    //判断返回页面类型 
						    sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
						    sendList.add(sendModel);
						    this.setSendList(sendList);
						    
						    if(model.getAction()!=null){
						    	action = model.getAction();
						    }else{
						    	 action = "顺序流转";
						    }
						    return SUCCESS;
						}else{
							action = "归档";
							 return "ARCHIVES"; 
						}
					}
				}
			}
		}
		return ERROR;
	}
	
	/**
	 * 执行人工操作
	 * @return
	 */
	public String executeManualJump(){
		//安全校验，判断命令发出人与流程任务节点任务办理人是否一致
		if(actDefId!=null&&actStepDefId!=null&&instanceId!=null&&excutionId!=null&&taskId!=null&&targetStepId!=null){
			//获得审核意见列表
			opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
			//获得通知类型列表
			remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
			if(flag){ 
				HashMap triggerParams = new HashMap(); 
				///#########################################表单办理前触发器##################################################################################
				/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_TRAN_BEFOR);
				/**/	boolean isrun = true;
				/**/	if(triggerModel!=null){
				/**/    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				/**/   		HttpServletRequest request = ServletActionContext.getRequest();
				/**/   		Map params = request.getParameterMap();
				/**/		//初始化触发器参数
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params); 
				/**/		isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
				/**/	}
				/**///#########################################END#######################################################################################
				if(isrun){
					/**/   		HttpServletRequest request = ServletActionContext.getRequest();
					/**/   		HashMap params = (HashMap)request.getParameterMap(); 
					RouteModel model = processRuntimeOperateService.executeManualJump(actDefId, prcDefId, actStepDefId, taskId, targetStepId,mjId,params);
					List<SendPageModel> sendList = new ArrayList();
					SendPageModel sendModel = new SendPageModel();
				    //装在节点名称及ID
				    sendModel.setTargetStepId(model.getNextStepId());
				    //获取下一节点名称
				    String t_activityName = model.getNextStepName();
				    if(t_activityName!=null){
				    	sendModel.setTargetStepName(t_activityName);
				    }
				  //装载节点标题
				    ccinstal = model.isCC();//判断是否允许抄送
					ccUsers = model.getCcUser();
					title = model.getTitle();
					//动作设置
				    if(model.getAction()!=null){
				    	action = model.getAction();
				    }else{
				    	 action = "任务跳转";
				    }
				    //获取接收人地址HTML
				    sendModel.setAddressHTML(model.getAddressHTML());
				    //判断返回页面类型
				    sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
				    sendList.add(sendModel);
				    this.setSendList(sendList);
				    //获取抄送用户
				    if(model.getSendPageCode()!=null&&model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES)){   //判断是否为归档页面
				    	action = "归档";
				    	 return "ARCHIVES"; 
				    }else{
				    	return SUCCESS; 
				    }
				}else{
					return ERROR;
				}
			}else{
				return ERROR;
			}
		}else{
				return ERROR;
		}
	}
	
	/**
	 * 执行驳回操作
	 * @return
	 */
	public String executeBack(){
		//获得驳回节点及人员信息
		SendPageModel backModel = null;
		if(actDefId!=null&&actStepDefId!=null&&instanceId!=null&&excutionId!=null&&taskId!=null){
			//设备类型
			//获得审核意见列表
			opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
			//获得通知类型列表
			remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			if(backType==null){
				backType = new Long(1);//默认返回上一个办理人
			}
			boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
			if(flag){
				HashMap triggerParams = new HashMap(); 
				///#########################################流程任务驳回前触发器##################################################################################
				/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_BACK);
				/**/	boolean isrun = true; 
				/**/	if(triggerModel!=null){
				/**/    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				/**/   		HttpServletRequest request = ServletActionContext.getRequest();
				/**/   		Map params = request.getParameterMap();
				/**/		//初始化触发器参数
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
				/**/		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params); 
				/**/		isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
				/**/	}
				/**///#########################################END#######################################################################################
				backModel = processRuntimeOperateService.executeBack(actDefId, prcDefId, actStepDefId, taskId,backType);
				title = backModel.getTitle();
				if(backModel.getMsgInfo()!=null){
					this.setTargetStepId(backModel.getTargetStepId());
					this.setTargetStepName(backModel.getTargetStepName());
					List<OrgUser> backUserList = backModel.getReceiveUser();
					this.setReceiveUser(backUserList);
					backUser = new HashMap();  //装载返回默认值
					if(backUserList!=null){
						for(OrgUser orgUser:backUserList){
							backUser.put(orgUser.getUserid(), orgUser.getUsername());
						}
					}
					if(backModel.getMsgInfo().equals(SUCCESS)){
						//获取抄送用户
//						ccUsers = processRuntimeOperateService.getCCReciveUser(actDefId, actStepDefId,taskId,instanceId,excutionId);
						action = "驳回";
						return SUCCESS;
					}else if(backModel.getMsgInfo().equals("ERROR-10011")){
						this.setTipsInfo("起草节点不允许执行“驳回”操作");
						return "nofind";
					}else{
						this.setTipsInfo("为找到上一节点办理人，不能执行“驳回”操作");
						return "nofind";
					}
				}else{
					return ERROR;
				}
			}else{
				return ERROR;
			}
		}else{
			return ERROR;
		}
	}

	
	
	/**
	 * 执行抄送操作
	 * @return
	 */
	public String executeCC(){
		//安全校验，判断命令发出人与流程任务节点任务办理人是否一致
		if(actDefId!=null&&prcDefId!=null&&actStepDefId!=null&&instanceId!=null&&taskId!=null&&excutionId!=null){
			//获取抄送用户
			try{
				if(ccUsers!=null){
					ccUsers = new String(ccUsers);
				}
			}catch(Exception e){logger.error(e,e);}
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * 执行转发操作
	 * @return
	 */
	public String executeForward(){
		//安全校验，判断命令发出人与流程任务节点任务办理人是否一致
		if(actDefId!=null&&prcDefId!=null&&actStepDefId!=null&&instanceId!=null&&taskId!=null&&excutionId!=null){
			//获得审核意见列表
			opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
			//获得通知类型列表
			remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			currentUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
			title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
			ProcessStepMap model = processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(prcDefId, actDefId, actStepDefId);
			if(model!=null){ 
				ccinstal = model.getIsCc();//判断是否允许抄送
			} 
			//获取抄送用户
			action = "转发 ";
		}
		return SUCCESS;
	}
	
	/**
	 * 执行加签操作
	 * @return
	 */
	public String executeAddSign(){
		//安全校验，判断命令发出人与流程任务节点任务办理人是否一致
		if(actDefId!=null&&prcDefId!=null&&actStepDefId!=null&&instanceId!=null&&taskId!=null&&excutionId!=null){
			//获得审核意见列表
			opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
			
			//获得通知类型列表
			remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
			
			title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			ProcessStepMap model = processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(prcDefId, actDefId, actStepDefId);
			if(model!=null){ 
				ccinstal = model.getIsCc();//判断是否允许抄送
			}  
			//获取抄送用户
			action = "加签";
			this.setTargetStepId("999999");
			this.setTargetStepName("加签操作");
//			ccUsers = processRuntimeOperateService.getCCReciveUser(actDefId, actStepDefId,taskId,instanceId,excutionId);
		}
		return SUCCESS;
	}
	/**
	 * 加载流程办理单选地址簿
	 * @return
	 */
	public String showProcessRadioAddress(){
		return SUCCESS;
	}
	
	/**
	 * 加签返回操作
	 * @return
	 */
	public String executeBackAddSign(){
		//获得驳回节点及人员信息
				SendPageModel backModel = null;
				if(actDefId!=null&&actStepDefId!=null&&excutionId!=null&&taskId!=null){
					//获得审核意见列表
					opinion1 = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
					title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
					//设备类型
					deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
					boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
					if(flag){
						backModel = processRuntimeOperateService.executeBack(actDefId, prcDefId, actStepDefId, taskId,null);
						if(backModel!=null){
							this.setTargetStepId(backModel.getTargetStepId());
							//获得通知类型列表,通知状态，取返回加签节点的通知设置
							remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, backModel.getTargetStepId());
							this.setTargetStepName(backModel.getTargetStepName()); 
							backUser = new HashMap();  //装载返回默认值
							if(backModel.getReceiveUser()!=null){
								receiveUser = backModel.getReceiveUser();
								for(OrgUser orgUser:backModel.getReceiveUser()){
									backUser.put(orgUser.getUserid(), orgUser.getUsername());
								}
							} 
//							isOpinion = backModel.getIsOpinion();  //判断是否允许审核意见填写
							action = "加签完毕";
							return SUCCESS;
						}else{
							return ERROR;
						}  
					}else{
						return ERROR;
					}
				}else{
					return ERROR;
				}
	}	
	//=========================POJO==================================================================

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public Long getPrcDefId() {
		return prcDefId;
	}

	public void setPrcDefId(Long prcDefId) {
		this.prcDefId = prcDefId;
	}

	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getExcutionId() {
		return excutionId;
	}

	public void setExcutionId(Long excutionId) {
		this.excutionId = excutionId;
	}

	public Long getDataid() {
		return dataid;
	}

	public void setDataid(Long dataid) {
		this.dataid = dataid;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public ProcessRuntimeOperateService getProcessRuntimeOperateService() {
		return processRuntimeOperateService;
	}
	public void setProcessRuntimeOperateService(
			ProcessRuntimeOperateService processRuntimeOperateService) {
		this.processRuntimeOperateService = processRuntimeOperateService;
	}
	public int getMaxUser() {
		return maxUser;
	}
	public void setMaxUser(int maxUser) {
		this.maxUser = maxUser;
	}
	public List<SendPageModel> getSendList() {
		return sendList;
	}
	public void setSendList(List<SendPageModel> sendList) {
		this.sendList = sendList;
	}
	public String getTargetStepId() {
		return targetStepId;
	}
	public void setTargetStepId(String targetStepId) {
		this.targetStepId = targetStepId;
	}
	public List<OrgUser> getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(List<OrgUser> receiveUser) {
		this.receiveUser = receiveUser;
	}
	public HashMap getCcUser() {
		return ccUser;
	}
	public void setCcUser(HashMap ccUser) {
		this.ccUser = ccUser;
	}
	public String getTargetStepName() {
		return targetStepName;
	}
	public void setTargetStepName(String targetStepName) {
		this.targetStepName = targetStepName;
	}
	public String getMjId() {
		return mjId;
	}
	public void setMjId(String mjId) {
		this.mjId = mjId;
	}
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public String getTipsInfo() {
		return tipsInfo;
	}
	public void setTipsInfo(String tipsInfo) {
		this.tipsInfo = tipsInfo;
	}
	public String getCcUsers() {
		return ccUsers;
	}
	public void setCcUsers(String ccUsers) {
		this.ccUsers = ccUsers;
	}
	public Long getCcinstal() {
		return ccinstal;
	}
	public void setCcinstal(Long ccinstal) {
		this.ccinstal = ccinstal;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public ProcessStepTriggerService getProcessStepTriggerService() {
		return processStepTriggerService;
	}


	public void setProcessStepTriggerService(
			ProcessStepTriggerService processStepTriggerService) {
		this.processStepTriggerService = processStepTriggerService;
	}

	public HashMap getBackUser() {
		return backUser;
	}
	public void setBackUser(HashMap backUser) {
		this.backUser = backUser;
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(String inputField) {
		this.inputField = inputField;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public HashMap getRemindTypeList() {
		return remindTypeList;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType; 
	} 

	public Long getBackType() {
		return backType;
	}

	public void setBackType(Long backType) {
		this.backType = backType;
	}


	public String getOpinion1() {
		return opinion1;
	}

	public void setOpinion1(String opinion1) {
		this.opinion1 = opinion1;
	}

	public Long getIsOpinion() {
		return isOpinion;
	}

	public void setIsOpinion(Long isOpinion) {
		this.isOpinion = isOpinion;
	}
	
}
