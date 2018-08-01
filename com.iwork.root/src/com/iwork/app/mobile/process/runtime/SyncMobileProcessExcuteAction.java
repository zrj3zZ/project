package com.iwork.app.mobile.process.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.constant.IFormStatusConstants;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.iform.service.SysEngineSubformService;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.step.constant.ProcessStepTriggerConstant;
import com.iwork.process.definition.step.model.ProcessStepForm;
import com.iwork.process.definition.step.model.ProcessStepFormMap;
import com.iwork.process.definition.step.model.ProcessStepJstrigger;
import com.iwork.process.definition.step.service.ProcessStepFormMapService;
import com.iwork.process.definition.step.service.ProcessStepFormService;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.model.ProcessTaskStatus;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 移动端流程发起中心
 * @author chenM
 *
 */
public class SyncMobileProcessExcuteAction extends ActionSupport {
	private ProcessRuntimeExcuteService processRuntimeExcuteService;
	private ProcessDeploymentService processDeploymentService;//流程定义模型服务
	private ProcessOpinionService processOpinionService;   //审核意见
	private SysEngineIFormService sysEngineIFormService; //表单模型服务
	private ProcessStepFormService processStepFormService; //流程表单定义模型
	private ProcessStepFormMapService processStepFormMapService; //流程表单域权限定义模型
	private ProcessStepTriggerService processStepTriggerService; //流程节点触发器模型
	private SysEngineSubformService sysEngineSubformService; //流程子表处理
	private IFormService iformService;
	private List<ProcessStepForm> formList;
	
	//表单参数变量
	private String actDefId;  //流程模型ID
	private Long prcDefId = new Long(0);	//流程定义ID
	private String actStepDefId ="";//流程节点ID
	private String prcStepName ="";//流程节点名称
	private Long formId = new Long(0);//表单ID
	private String taskId ="0";//任务ID
	private Long instanceId = new Long(0);//实例ID
	private Long excutionId = new Long(0);	//执行动作ID
	private Long dataid = new Long(0);  //行数据ID
	private Long stepformId = new Long(0);  //行数据ID
	private Long subformid = new Long(0);  //子表ID
	private Long id = new Long(0);  //子表行ID
	
	private Long isLog ;    //是否记录表单修改日志
	private Long modelId ;    //模型ID
	private String modelType ;    //模型类型
	private String deviceType ;    //设备类型
	
	//办理参数
	private String targetActivityId;  //目标办理节点
	private String processButton;  //流程按钮
	private String processExtendButton;  //流程扩展按钮
	private String processTransButton;  //流程提交办理按钮
	private String processShortcutsButton;  //快捷键
	
	 
	//表单动态参数
	private String pageTab;
	private String title;   
	private String style;   
	private String script;   
	private String saveScriptEvent;   //保存脚本触发器事件   
	private String transScriptEvent;   //办理触发器事件   
	private String forminfo;   
	private String content;   
	private String info; 
	private String opinionList; 
	private List cyOpinionlist;
	private ProcessRuOpinion model;
	
	//发送页面参数
	
	/**
	 * 开始执行流程任务
	 * 创建流程实例，当用户打开表单，未进行保存或办理操作时,不进行实例创建，避免打开表单后，产生待办事宜的垃圾数据
	 * @return
	 */
	public String startProcessInstance(){
		boolean flag = true;  //判断表单打开状态，是否正常
		String page = "";//获得流程绑定表单模型
		//初始化表单
		String style_html = "";
		String script_html = ""; 
		if (actDefId != null && !actDefId.equals("")) {
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			
			ProcessDefDeploy model = processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
			if(model!=null){ 
				//设置模型定义属性
				modelId = prcDefId;
				modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_PROCESS;
				
				//装载流程id
				this.setPrcDefId(model.getId());
				//获得流程节点ID
				if(actStepDefId==null||"".equals(actStepDefId)){
					this.setActStepDefId(processRuntimeExcuteService.getProcessTopStepId(actDefId));
				}
				this.setPrcStepName(prcStepName); 
				//获得表单节点绑定列表
				this.setFormList(processRuntimeExcuteService.getStepFromList(actDefId, model.getId(), actStepDefId));
				if(this.getFormList()==null)flag = false; 
				//获取流程任务及表单状态
				if(flag){ 
					ProcessTaskStatus processTaskStatus = processRuntimeExcuteService.getTaskStatusInfo(formList,actDefId, prcDefId, actStepDefId, stepformId, taskId);
					processTaskStatus.setTasktype(ProcessTaskStatus.PRO_TASK_STATUS_CREATE);
					//判断当前表单类型为IFORM表单
					if(processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM)){
						//========补充参数=============================================
						if(formId==null||formId.equals(new Long(0))){
							this.setFormId(processTaskStatus.getFormid());
						}
						//========补充参数END=============================================
						 
						//==============获得表单多表单页签========================
						this.setPageTab(processRuntimeExcuteService.getProcessFormTab(formId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
						//=====================================================
							//获取当前任务节点表单模型对象
							if(processTaskStatus!=null){
								//获取流程绑定节点ID
								Long processStepFormId = new Long(0);
								if(processTaskStatus.getProcessStepForm()!=null)processStepFormId = processTaskStatus.getProcessStepForm().getId();
								
								//获取表单元素权限信息
								HashMap<String,ProcessStepFormMap> psfmList = processStepFormMapService.getFormMapList(actDefId, prcDefId, actStepDefId, processStepFormId);
								//判断表单权限，如果表单为允许编辑，并签办理人为当前用户
								if(processTaskStatus.isModify()){
//									page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_MODIFY,psfmList,taskId);//获得表单页面
								}else{ 
//									page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_READ,psfmList,taskId);//获得表单页面
								}
							}else{
								page = iformService.getFormPage(formId,instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);//获得表单页面
							}
							
							//获取表单信息
							SysEngineIform iformModel = sysEngineIFormService.getModel(formId);
							if(iformModel!=null){
								//获的CSS样式及js脚本
								if(iformModel.getIformCss()!=null)
									style_html = iformModel.getIformCss();
								if(iformModel.getIformJs()!=null) 
									script_html = iformModel.getIformJs();
							}
							
							//装载CSS样式				
							this.setStyle(style_html);
							//装载script脚本
							this.setScript(script_html); 
							this.setForminfo("");
							this.setContent(page);
							this.setInfo(info);
							flag = true;
					}else{ 
						//获得表单多表单页签
							this.setPageTab(processRuntimeExcuteService.getProcessFormTab(formId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
							String url = "";
							if(processTaskStatus.getProcessStepForm()!=null)
								url = processTaskStatus.getProcessStepForm().getUrl();
						this.setContent(" <iframe width=\"100%\" id=\"urlFrame\"  scrolling='auto' onLoad='iFrameHeight(\"urlFrame\")'  frameborder=\"0\" src=\""+url+"\"></iframe>");
						this.setInfo(info);
					}
				/*
					//===================================操作按钮显示==========================================================
					this.setProcessButton(processRuntimeExcuteService.getProcessPageButton(processTaskStatus,actDefId, model.getId(), actStepDefId,taskId));
					//获得扩展操作按钮
					this.setProcessExtendButton(processRuntimeExcuteService.getProcessExtendsButton(actDefId, model.getId(), actStepDefId,instanceId));
					//获取提交办理按钮列表
					this.setProcessTransButton(processRuntimeExcuteService.getProcessTransItemButton(actDefId,model.getId(), taskId));
					//===================================操作按钮显示END==========================================================
					 
					//===================================表单脚本触发器定义======================================================= 
					ProcessStepJstrigger jsTriggerModel = processRuntimeExcuteService.getProcessStepJsTriggerModel(actDefId,actStepDefId,prcDefId);
					if(jsTriggerModel!=null){
						this.setSaveScriptEvent(jsTriggerModel.getSaveJs());
						this.setTransScriptEvent(jsTriggerModel.getTranJs());
					}
					//------------------------------------表单脚本触发器定义END---------------------------------------------------------------------
					 * *
					 */
				}
			}else{
				//无可用流程，或当前流程已关闭
				flag = false;
			}
		}else{
			//流程参数异常
			flag = false;
		}
		//执行页面返回动作
		if(flag){
			return SUCCESS; 
		}else{
			return ERROR; 
		}
		
	}

	/**
	 * 加载流程表单页面
	 * @return
	 */ 
	public String loadFormPage(){
		boolean task_status = false;
		//获得流程绑定表单模型
		String page = "";
		//初始化表单 
		String style_html = "";
		String script_html = "";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(actDefId!=null){
			//获取流程模型
			ProcessDefDeploy model = processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
			if(model!=null){
				//设置模型定义属性
				modelId = prcDefId;
				modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_PROCESS;
				
					//获得默认节点
					this.setPrcDefId(model.getId());
					if(excutionId!=null&&excutionId!=0){
						ExecutionEntity ee = processRuntimeExcuteService.getProcessStepId(excutionId);//获得流程节点ID
						if(ee!=null){
							this.setActStepDefId(ee.getActivityId());
						}
					} 
					List<ProcessStepForm> list = processRuntimeExcuteService.getStepFromList(actDefId, prcDefId, actStepDefId);
					ProcessTaskStatus processTaskStatus = processRuntimeExcuteService.getTaskStatusInfo(list,actDefId, prcDefId, actStepDefId, stepformId, taskId);
					ProcessStepForm psf = processTaskStatus.getProcessStepForm();
					if(list!=null&&psf!=null&&psf.getId()!=null){ 
						this.setFormId(psf.getFormid());
						this.setFormList(list); 
						HashMap triggerParams = new HashMap();
						//判断当前表单类型为IFORM表单
						if(processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM)){
							//========补充参数=============================================
							if(formId==null||formId.equals(new Long(0))){
								this.setFormId(processTaskStatus.getFormid());
							}
							//--------------补充参数END----------------------------------
							//#########################################表单加载前触发器##################################################################################
							/**/   BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_LOAD);
							/**/   if(triggerModel!=null){
							/**/   	//初始化触发器参数
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,this.getFormId());
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
							/**/   		TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
							/**/   	}
							//#########################################END#############################################################################################
							 
							//==============获得表单多表单页签========================================
							this.setPageTab(processRuntimeExcuteService.getProcessFormTab(stepformId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
							//--------------------------------------------------------------------
								//获取当前任务节点表单模型对象
								if(processTaskStatus!=null){
									//获取流程绑定节点ID
									Long processStepFormId = new Long(0);
									if(processTaskStatus.getProcessStepForm()!=null)processStepFormId = processTaskStatus.getProcessStepForm().getId();
									
									//获取表单元素权限信息
									HashMap<String,ProcessStepFormMap> psfmList = processStepFormMapService.getFormMapList(actDefId, prcDefId, actStepDefId, processStepFormId);
									//判断表单权限，如果表单为允许编辑，并签办理人为当前用户
									if(processTaskStatus.isModify()){
										if(processTaskStatus.isTaskOwner()){
											page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_MODIFY,psfmList,taskId,null);//获得表单页面
										}else{
											page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_READ,psfmList,taskId,null);//获得表单页面
										}
									}else{
										page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_READ,psfmList,taskId,null);//获得表单页面
									}
								}else{
									//page = iformService.getMobileFormPage(formId,instanceId);//获得表单页面
								}
								//#########################################表单加载后触发器##################################################################################
								/**/	triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_LOAD);
								/**/	if(triggerModel!=null){
								/**/		TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId); 
								/**/	}
								//#########################################END#############################################################################################
								//获取表单信息
								SysEngineIform iformModel = sysEngineIFormService.getModel(formId);
								if(iformModel!=null){
									//获的CSS样式及js脚本
									if(iformModel.getIformCss()!=null)
										style_html = iformModel.getIformCss();
									if(iformModel.getIformJs()!=null) 
										script_html = iformModel.getIformJs();
								}
								//装载CSS样式				
								this.setStyle(style_html);
								//装载script脚本
								this.setScript(script_html);
								this.setForminfo("");
								this.setContent(page);
								
								this.setInfo(info);
						}else{
							//获得表单多表单页签
								this.setPageTab(processRuntimeExcuteService.getProcessFormTab(stepformId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
								String url = "";
								if(processTaskStatus.getProcessStepForm()!=null)
									url = processTaskStatus.getProcessStepForm().getUrl();
								this.setContent(processRuntimeExcuteService.getUrlTypeContext(url));
							this.setInfo(info);
						}
						//----------------------------------------END-----------------------------------------------------------------
						//===================================操作按钮显示==========================================================
//						this.setProcessButton(processRuntimeExcuteService.getProcessPageButton(processTaskStatus,actDefId,prcDefId, actStepDefId,taskId));
						//获得扩展操作按钮
//						this.setProcessExtendButton(processRuntimeExcuteService.getProcessExtendsButton(actDefId,prcDefId, actStepDefId,instanceId));
						//获取提交办理按钮列表
//						this.setProcessTransButton(processRuntimeExcuteService.getProcessTransItemButton(actDefId,prcDefId, taskId));
						//获取快捷键
//						this.setProcessShortcutsButton(processRuntimeExcuteService.getProcessShortcutsButton(processTaskStatus, actDefId, prcDefId, actStepDefId, taskId));
						//----------------------------------操作按钮显示END---------------------------------------------------------
						//===================================表单脚本触发器定义=======================================================
						ProcessStepJstrigger jsTriggerModel = processRuntimeExcuteService.getProcessStepJsTriggerModel(actDefId,actStepDefId,prcDefId);
						if(jsTriggerModel!=null){
							this.setSaveScriptEvent(jsTriggerModel.getSaveJs());
							this.setTransScriptEvent(jsTriggerModel.getTranJs());
						}
						//------------------------------------表单脚本触发器定义END---------------------------------------------------------------------
						//获取主表行记录参数
						if(formId!=null){
							Long dataid = iformService.getDataid(formId, instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
							this.setDataid(dataid);
						}
						//设备类型
						deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
						//获得审核意见列表
						opinionList = processOpinionService.getMobileProcessInstanceOpinionList(actDefId, prcDefId,actStepDefId, instanceId);
						//获得任务标题及状态
						if(taskId!=null){
							Task task = processRuntimeExcuteService.getProcessTask(taskId);
							if(task!=null){
								this.setTitle("["+task.getName()+"]"+task.getDescription());
								task_status =true;
							}
						}
					}else{
						this.addActionError("表单加载异常 ");
						this.setContent("表单加载异常 ");
					}
					return SUCCESS; 
			}else{
				return "task_over";
			}
		}else{
			return "task_over";
		}
	}
	
	/**
	 * 加载流程表单页面
	 * @return
	 */ 
	public String loadVisitPage(){
		boolean task_status = false;
		//获得流程绑定表单模型
		String page = "";
		//初始化表单 
		String style_html = "";
		String script_html = "";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(actDefId!=null){
			//获取流程模型
			ProcessDefDeploy model = processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
			if(model!=null){
				//设置模型定义属性
				modelId = prcDefId;
				modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_PROCESS;
				
				//获得默认节点
				this.setPrcDefId(model.getId());
				if(excutionId!=null&&excutionId!=0){
					ExecutionEntity ee = processRuntimeExcuteService.getProcessStepId(excutionId);//获得流程节点ID
					if(ee!=null){
						this.setActStepDefId(ee.getActivityId());
					}
				} 
				List<ProcessStepForm> list = processRuntimeExcuteService.getStepFromList(actDefId, prcDefId, actStepDefId);
				ProcessTaskStatus processTaskStatus = processRuntimeExcuteService.getTaskStatusInfo(list,actDefId, prcDefId, actStepDefId, stepformId, taskId);
				ProcessStepForm psf = processTaskStatus.getProcessStepForm();
				if(list!=null&&psf!=null&&psf.getId()!=null){ 
					this.setFormId(psf.getFormid());
					this.setFormList(list); 
					HashMap triggerParams = new HashMap();
					//判断当前表单类型为IFORM表单
					if(processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM)){
						//========补充参数=============================================
						if(formId==null||formId.equals(new Long(0))){
							this.setFormId(processTaskStatus.getFormid());
						}
						//--------------补充参数END----------------------------------
						//#########################################表单加载前触发器##################################################################################
						/**/   BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_LOAD);
						/**/   if(triggerModel!=null){
							/**/   	//初始化触发器参数
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,this.getFormId());
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
							/**/   		triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
							/**/   	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
							/**/   		TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
						/**/   	}
						//#########################################END#############################################################################################
						
						//==============获得表单多表单页签========================================
						this.setPageTab(processRuntimeExcuteService.getProcessFormTab(stepformId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
						//--------------------------------------------------------------------
						//获取当前任务节点表单模型对象
						if(processTaskStatus!=null){
							//获取流程绑定节点ID
							Long processStepFormId = new Long(0);
							if(processTaskStatus.getProcessStepForm()!=null)processStepFormId = processTaskStatus.getProcessStepForm().getId();
							
							//获取表单元素权限信息
							HashMap<String,ProcessStepFormMap> psfmList = processStepFormMapService.getFormMapList(actDefId, prcDefId, actStepDefId, processStepFormId);
							//判断表单权限，如果表单为允许编辑，并签办理人为当前用户
								page = iformService.getMobileFormPage(formId,instanceId,IFormStatusConstants.FORM_PAGE_READ,psfmList,taskId,null);//获得表单页面
						}else{
							//page = iformService.getMobileFormPage(formId,instanceId);//获得表单页面
						}
						//获取表单信息
						SysEngineIform iformModel = sysEngineIFormService.getModel(formId);
						if(iformModel!=null){
							//获的CSS样式及js脚本
							if(iformModel.getIformCss()!=null)
								style_html = iformModel.getIformCss();
							if(iformModel.getIformJs()!=null) 
								script_html = iformModel.getIformJs();
						}
						//装载CSS样式				
						this.setStyle(style_html);
						//装载script脚本
						this.setScript(script_html);
						this.setForminfo("");
						this.setContent(page);
						
						this.setInfo(info);
					}else{
						//获得表单多表单页签
						this.setPageTab(processRuntimeExcuteService.getProcessFormTab(stepformId, formList,processTaskStatus.getIsTalk()));   //获取页面传入参数
						String url = "";
						if(processTaskStatus.getProcessStepForm()!=null)
							url = processTaskStatus.getProcessStepForm().getUrl();
						this.setContent(processRuntimeExcuteService.getUrlTypeContext(url));
						this.setInfo(info);
					}
					//----------------------------------------END-----------------------------------------------------------------
					//===================================操作按钮显示==========================================================
//						this.setProcessButton(processRuntimeExcuteService.getProcessPageButton(processTaskStatus,actDefId,prcDefId, actStepDefId,taskId));
					//获得扩展操作按钮
//						this.setProcessExtendButton(processRuntimeExcuteService.getProcessExtendsButton(actDefId,prcDefId, actStepDefId,instanceId));
					//获取提交办理按钮列表
//						this.setProcessTransButton(processRuntimeExcuteService.getProcessTransItemButton(actDefId,prcDefId, taskId));
					//获取快捷键
//						this.setProcessShortcutsButton(processRuntimeExcuteService.getProcessShortcutsButton(processTaskStatus, actDefId, prcDefId, actStepDefId, taskId));
					//----------------------------------操作按钮显示END---------------------------------------------------------
					//===================================表单脚本触发器定义=======================================================
					ProcessStepJstrigger jsTriggerModel = processRuntimeExcuteService.getProcessStepJsTriggerModel(actDefId,actStepDefId,prcDefId);
					if(jsTriggerModel!=null){
						this.setSaveScriptEvent(jsTriggerModel.getSaveJs());
						this.setTransScriptEvent(jsTriggerModel.getTranJs());
					}
					//------------------------------------表单脚本触发器定义END---------------------------------------------------------------------
					//获取主表行记录参数
					if(formId!=null){
						Long dataid = iformService.getDataid(formId, instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						this.setDataid(dataid);
					}
					//设备类型
					deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
					//获得审核意见列表
					opinionList = processOpinionService.getMobileProcessInstanceOpinionList(actDefId, prcDefId,actStepDefId, instanceId);
					//获得任务标题及状态
					if(taskId!=null){
						Task task = processRuntimeExcuteService.getProcessTask(taskId);
						if(task!=null){
							this.setTitle("["+task.getName()+"]"+task.getDescription());
							task_status =true;
						}
					}
				}else{
					this.addActionError("表单加载异常 ");
					this.setContent("表单加载异常 ");
				}
				return SUCCESS; 
			}else{
				return "task_over";
			}
		}else{
			return "task_over";
		} 
	}
	/**
	 * 加载流程处理按钮
	 */
	public  void showTransBtn(){
		//===================================操作按钮显示==========================================================
		if(actDefId!=null&&taskId!=null){
			ProcessTaskStatus processTaskStatus = new ProcessTaskStatus();
			processTaskStatus.setModify(false);
			processTaskStatus.setTaskOwner(true); 
			String json = processRuntimeExcuteService.getMobileProcessPageButton(processTaskStatus,actDefId,taskId);
			ResponseUtil.write(json);
		} 
	} 
	/**
	 * 加载子表
	 * @return
	 */ 
	public String loadSubForm(){
		Page page = new Page();
		page.setPageSize(100);
		page.setCurPageNo(0); 
		if(actDefId!=null&&prcDefId!=null&&actStepDefId!=null&&taskId!=null&&instanceId!=null&&formId!=null){
			SysEngineSubform subformModel = sysEngineSubformService.getSysEngineSubformDAO().getModel(formId);
			if(subformModel!=null){
				title=subformModel.getTitle();
				subformid = formId;
			}
			
			//设备类型
			List<ProcessStepForm> list = processRuntimeExcuteService.getStepFromList(actDefId, prcDefId, actStepDefId);
			ProcessTaskStatus taskStatus = processRuntimeExcuteService.getTaskStatusInfo(list, actDefId, prcDefId, actStepDefId, stepformId, taskId);
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			if(taskStatus!=null){ 
				content = sysEngineSubformService.getMobileSubFormData(formId, instanceId,taskId,taskStatus.isModify(),page); 
			}
		}
		return SUCCESS; 
	}
	/**
	 * 编辑子表
	 * @return
	 */
	public String editSubForm(){
		if(taskId!=null&&instanceId!=null&&formId!=null&&subformid!=null&&id!=null){
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			content = sysEngineSubformService.getMobileSubFormItemPage(formId,subformid, taskId, instanceId,id);
		} 
		return SUCCESS;
	}
	/**
	 * 删除子表
	 */
	public void delSubFormItem(){
		boolean flag = true; 
		flag=iformService.removeSubformData(subformid, instanceId, id+"");
		ResponseUtil.write(flag+""); 
	}
	
	/**
	 * 加载传阅表单
	 * @return
	 */
	public String loadNoticeFormPage(){
		if(dataid!=null){
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			processRuntimeExcuteService.setCCisRead(dataid);
		}
		return loadFormPage();
		
	}
	/**
	 * 新增子表项目
	 * @return
	 */
	public String addsubFormItem(){
		if(taskId!=null&&instanceId!=null&&formId!=null&&subformid!=null){
			//设备类型
			deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
			content = sysEngineSubformService.getMobileSubFormItemPage(formId, subformid, taskId, instanceId, dataid);
		}   
		return SUCCESS;
	}
	/**
	 * 添加意见
	 * @return
	 */
	public String addOpinion(){
		model = processOpinionService.getProcessOpinionDAO().getUserOpinionModel(UserContextUtil.getInstance().getCurrentUserId(), actDefId, prcDefId, actStepDefId, instanceId, taskId, excutionId);
		//设备类型
		deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
		cyOpinionlist = processOpinionService.getProcessOpinionDAO().loadUserDefinedOpinions();
		return SUCCESS;
	}
	/**
	 * 执行流程表单保存操作
	 * @return
	 */
	public String processFormSave(){
			//创建流程实例，获得流程实例ID
			//执行保存动作，
			String  s_code= "success";
			boolean flag = false;
			boolean islog = false;
			HashMap triggerParams = new HashMap();
			Long type = new Long(1); 
			if(modelId!=null&&formId!=null&&actDefId!=null&&prcDefId!=null){
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				HttpServletRequest request = ServletActionContext.getRequest();
				Map params = request.getParameterMap();
				String str1 = params.toString();
				//判断是否记录表单变动日志
				if(isLog!=null&&isLog.equals(SysConst.on)){
					islog = true;
				}
				if(instanceId!=null&&instanceId>0&&dataid!=0){
					//获得行数据ID
					///#########################################表单更新前触发器##################################################################################
					/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_SAVE_BEFOR);
					/**/boolean isrun = true;
					/**/if(triggerModel!=null){
					/**/	//初始化触发器参数
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
					/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params);
					/**/	isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
					/**/}
					/**///#########################################END##################################################################################
					if(isrun){
						flag = iformService.updateForm(prcDefId,formId, instanceId, params,dataid,false,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						if(!flag){
							 s_code = "ERROR-10009";
						}else{
							//#########################################表单保存后触发器##################################################################################
							/**/	triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_SAVE_AFTER);
							/**/	if(triggerModel!=null){
							/**/		TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
							/**/	} 
							//#########################################END############################################################################################
							
							//更新表单索引
							processRuntimeExcuteService.addTaskFormIndex(actDefId, prcDefId, actStepDefId, stepformId, taskId, instanceId, excutionId, params);
							
						}
					}else{
						 s_code = "ERROR-10009"; 
					}
					 
				}else{
					//获取流程实例ID
					if(instanceId==null||instanceId==0){
						
						instanceId = processRuntimeExcuteService.initProcessInstanceId(actDefId,prcDefId, uc.get_userModel().getUserid());
						if(instanceId==null||instanceId==0) {
							return null;
						}
						//注册流程实例
						iformService.initInstanceId(formId, instanceId, type);
					}
					
					if(instanceId>0){
						//获得行数据ID
						///#########################################表单保存前触发器##################################################################################
						/**/BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_SAVE_BEFOR);
						/**/boolean isrun = true;
						/**/if(triggerModel!=null){
						/**/	//初始化触发器参数
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFID,actDefId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_ACTDEFSTEPID,actStepDefId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMID,formId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_INSTANCEID,instanceId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_TASKID,taskId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_EXECUTEID,excutionId);
						/**/	triggerParams.put(ProcessStepTriggerEvent.PROCESS_PARAMETER_FORMDATA,params);
						/**/	isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
						/**/}
						/**///#########################################END##################################################################################
						//执行保存动作,如果触发器保存动作返回成功，正常执行保存，如果返回false，则不进行保存操作
						if(isrun){
							dataid = iformService.saveForm(modelId,formId, instanceId, params,islog,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						}
						//#########################################表单保存后触发器##################################################################################
						/**/	triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, ProcessStepTriggerConstant.PROCESS_TRIGGER_EVENT_FORM_SAVE_AFTER);
						/**/	if(triggerModel!=null){
						/**/		TriggerAPI.getInstance().excuteEvent(triggerModel, uc, TriggerAPI.THREAD_TYPE_SYNC, triggerParams,instanceId);
						/**/	} 
						//#########################################END############################################################################################
						//获得任务对象
						Task task = processRuntimeExcuteService.getProcessTask(instanceId);
						if(task!=null){ 
							taskId = task.getId();
							excutionId =Long.parseLong(task.getExecutionId());
						} 
						if(dataid>0){
							s_code= "success";
							//添加索引
							processRuntimeExcuteService.addTaskFormIndex(actDefId, prcDefId, actStepDefId, stepformId, taskId, instanceId, excutionId, params);
						}else{
							s_code= "ERROR-10010";
						}
					}
				}
			//因为主表保存是无刷新保存，不做页面跳转，所以return null.用response返回执行结果
				String str = s_code+","+instanceId+","+dataid+","+taskId+","+excutionId;
				ResponseUtil.write(str);
		}
		return null; 
	}

	
	//=========================POJO==================================================================
	public ProcessRuntimeExcuteService getProcessRuntimeExcuteService() {
		return processRuntimeExcuteService;
	}

	public void setProcessRuntimeExcuteService(
			ProcessRuntimeExcuteService processRuntimeExcuteService) {
		this.processRuntimeExcuteService = processRuntimeExcuteService;
	}
	public SysEngineIFormService getSysEngineIFormService() {
		return sysEngineIFormService;
	}

	public void setSysEngineIFormService(SysEngineIFormService sysEngineIFormService) {
		this.sysEngineIFormService = sysEngineIFormService;
	}
	public IFormService getIformService() {
		return iformService;
	}

	public void setIformService(IFormService iformService) {
		this.iformService = iformService;
	}

	public String getProcessButton() {
		return processButton;
	}

	public void setProcessButton(String processButton) {
		this.processButton = processButton;
	}


	public ProcessDeploymentService getProcessDeploymentService() {
		return processDeploymentService;
	}

	public void setProcessDeploymentService(
			ProcessDeploymentService processDeploymentService) {
		this.processDeploymentService = processDeploymentService;
	}
	
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
	public List<ProcessStepForm> getFormList() {
		return formList;
	}

	public void setFormList(List<ProcessStepForm> formList) {
		this.formList = formList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getForminfo() {
		return forminfo;
	}

	public void setForminfo(String forminfo) {
		this.forminfo = forminfo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getProcessExtendButton() {
		return processExtendButton;
	}
	public void setProcessExtendButton(String processExtendButton) {
		this.processExtendButton = processExtendButton;
	}

	public String getPageTab() {
		return pageTab;
	}

	public void setPageTab(String pageTab) {
		this.pageTab = pageTab;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessTransButton() {
		return processTransButton;
	}

	public void setProcessTransButton(String processTransButton) {
		this.processTransButton = processTransButton;
	}

	public String getTargetActivityId() {
		return targetActivityId;
	}

	public void setTargetActivityId(String targetActivityId) {
		this.targetActivityId = targetActivityId;
	}

	public String getOpinionList() {
		return opinionList; 
	}
	public void setOpinionList(String opinionList) {
		this.opinionList = opinionList;
	}
	public ProcessOpinionService getProcessOpinionService() {
		return processOpinionService;
	}
	public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
		this.processOpinionService = processOpinionService;
	}

	public String getPrcStepName() {
		return prcStepName;
	}

	public void setPrcStepName(String prcStepName) {
		this.prcStepName = prcStepName;
	}

	public ProcessStepFormService getProcessStepFormService() {
		return processStepFormService;
	}

	public void setProcessStepFormService(
			ProcessStepFormService processStepFormService) {
		this.processStepFormService = processStepFormService;
	}

	public ProcessStepFormMapService getProcessStepFormMapService() {
		return processStepFormMapService;
	}

	public void setProcessStepFormMapService(
			ProcessStepFormMapService processStepFormMapService) {
		this.processStepFormMapService = processStepFormMapService;
	}

	public Long getStepformId() {
		return stepformId;
	}
	public void setStepformId(Long stepformId) {
		this.stepformId = stepformId;
	}

	public String getProcessShortcutsButton() {
		return processShortcutsButton;
	}
	public void setProcessShortcutsButton(String processShortcutsButton) {
		this.processShortcutsButton = processShortcutsButton;
	}

	public String getSaveScriptEvent() {
		return saveScriptEvent;
	}

	public void setSaveScriptEvent(String saveScriptEvent) {
		this.saveScriptEvent = saveScriptEvent;
	}

	public String getTransScriptEvent() {
		return transScriptEvent;
	}

	public void setTransScriptEvent(String transScriptEvent) {
		this.transScriptEvent = transScriptEvent;
	}

	public ProcessStepTriggerService getProcessStepTriggerService() {
		return processStepTriggerService;
	}

	public void setProcessStepTriggerService(
			ProcessStepTriggerService processStepTriggerService) {
		this.processStepTriggerService = processStepTriggerService;
	}
	public void setSysEngineSubformService(
			SysEngineSubformService sysEngineSubformService) {
		this.sysEngineSubformService = sysEngineSubformService;
	}

	public Long getSubformid() {
		return subformid;
	}

	public void setSubformid(Long subformid) {
		this.subformid = subformid;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getIsLog() {
		return isLog;
	}

	public void setIsLog(Long isLog) {
		this.isLog = isLog;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public List getCyOpinionlist() {
		return cyOpinionlist;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public ProcessRuOpinion getModel() {
		return model;
	}
	
}
