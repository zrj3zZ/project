package com.iwork.process.runtime.action;

import com.iwork.app.weixin.process.action.qy.util.TestSendMes;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.step.model.ProcessStepForm;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.deployment.service.SysProcessDefinitionService;
import com.iwork.process.runtime.service.ProcessRuntimeSendService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class ProcessRuntimeSendAction
  extends ActionSupport
{
  private ProcessRuntimeSendService processRuntimeSendService;
  private ProcessStepTriggerService processStepTriggerService;
  private SysProcessDefinitionService sysProcDefService;
  private SysEngineIFormService sysEngineIFormService;
  private ProcessOpinionService processOpinionService;
  private IFormService iformService;
  private List<ProcessStepForm> formList;
  private String actDefId;
  private Long prcDefId;
  private Long backType;
  private String actStepDefId;
  private String currentUser;
  private String action;
  private Long formId;
  private String taskId;
  private String opinion;
  private String attach;
  private Long instanceId;
  private Long excutionId;
  private Long dataid;
  private Long isRemindHistoricUser;
  private HashMap remindTypeList;
  private String title;
  private String targetStepId;
  private String targetStepName;
  private String[] receiveUser;
  private String receiveUserName;
  private String ccUsers;
  private String parallelGateway;
  
  public void executeSend()
  {
	  this.remindTypeList = new HashMap();
    boolean isError = false;
    String msg = "";
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.excutionId != null) && (this.taskId != null))
    {
      if ((this.targetStepId != null) && (this.receiveUser != null))
      {
        Map param = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        param = request.getParameterMap();
        if ((this.ccUsers != null) && (!"".equals(this.ccUsers)) && 
          (!UserContextUtil.getInstance().checkAddress(this.ccUsers)))
        {
          msg = "ERROR-10014";
          isError = true;
        }
        HashMap triggerParams = new HashMap();
        if (!isError)
        {
          if (this.actStepDefId != this.targetStepId)
          {
            boolean isRun = true;
            
            BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_BEFOR");
            if (triggerModel != null)
            {
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
              
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
              isRun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }
            if (isRun) {
              msg = this.processRuntimeSendService.executeSend(this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.excutionId, this.taskId, this.targetStepId, this.receiveUser, param);
              UserContext ucs = UserContextUtil.getInstance().getCurrentUserContext();
              TestSendMes tms=new TestSendMes();
              tms.sendMsgList(this.receiveUser,ucs._userModel.getUsername(),this.instanceId,"提交",this.title);
            } else {
              msg = "ERROR-100016";
            }
          }
          if (msg.equals("success"))
          {
            this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
            if (this.ccUsers != null) {
              this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
            }
            BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_AFTER");
            if (triggerModel != null)
            {
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
              TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }
            triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.targetStepId, "TRAN_RECEVER_AFTER");
            if (triggerModel != null)
            {
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
              TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }
          }
        }
        setReceiveUserName(UserContextUtil.getInstance().getUserName(this.receiveUser));
      }
      else
      {
        msg = "ERROR-10015";
      }
    }
    else {
      msg = "ERROR-10005";
    }
  //  DBUTilNew.update("update process_ru_task s set s.extend1='"+targetStepId+"' ,s.extend2='"+targetStepName+"' ,s.extend3='"+receiveUser[0]+"' where s.proc_inst_id_="+instanceId,null);
    DBUTilNew.update("update process_hi_taskinst  set extend1='"+targetStepId+"' ,extend2='"+targetStepName+"' ,extend3='"+receiveUser[0]+"' where id_=(select t.id_ from ( select s.id_ from process_hi_taskinst s where s.proc_inst_id_="+instanceId+"  order by s.id_ ) t where rownum=1)",null);
    ResponseUtil.write(msg);
  }
  
  public void executeService()
  {
    this.remindTypeList = new HashMap();
    boolean isError = false;
    String msg = "";
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.excutionId != null) && (this.taskId != null))
    {
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = ParameterMapUtil.getParameterMap(request.getParameterMap());
      if ((this.ccUsers != null) && (!"".equals(this.ccUsers)) && 
        (!UserContextUtil.getInstance().checkAddress(this.ccUsers)))
      {
        msg = "ERROR-10014";
        isError = true;
      }
      HashMap triggerParams = new HashMap();
      if (!isError)
      {
        if (this.actStepDefId != this.targetStepId)
        {
          boolean isRun = true;
          
          BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_BEFOR");
          if (triggerModel != null)
          {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
            
            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
            isRun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
          if (isRun) {
            msg = this.processRuntimeSendService.executeService(this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.excutionId, this.taskId, this.targetStepId, this.receiveUser, param);
          } else {
            msg = "ERROR-100016";
          }
        }
        if (msg.equals("success"))
        {
          this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
          if (this.ccUsers != null) {
            this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
          }
          BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_AFTER");
          if (triggerModel != null)
          {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
            TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
        }
      }
      if (this.receiveUser != null) {
        setReceiveUserName(UserContextUtil.getInstance().getUserName(this.receiveUser));
      }
    }
    else
    {
      msg = "ERROR-10005";
    }
    ResponseUtil.write(msg);
  }
  
  public void executeAddSignSend()
  {
    this.remindTypeList = new HashMap();
    boolean isError = false;
    String msg = "";
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.excutionId != null) && (this.taskId != null))
    {
      if ((this.targetStepId != null) && (this.receiveUser != null))
      {
        Map param = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        param = request.getParameterMap();
        if ((this.ccUsers != null) && (!"".equals(this.ccUsers)) && 
          (!UserContextUtil.getInstance().checkAddress(this.ccUsers)))
        {
          msg = "ERROR-10014";
          isError = true;
        }
        HashMap triggerParams = new HashMap();
        if (!isError)
        {
          if (this.actStepDefId != this.targetStepId)
          {
            boolean isRun = true;
            
            BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_ADDSIGN_BEFOR");
            if (triggerModel != null)
            {
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
              
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
              isRun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }
            if (isRun) {
              msg = this.processRuntimeSendService.executeSend(this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.excutionId, this.taskId, this.targetStepId, this.receiveUser, param);
            } else {
              msg = "ERROR-100016";
            }
          }
          if (msg.equals("success"))
          {
            this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
            if (this.ccUsers != null) {
              this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
            }
            BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_SEND_ADDSIGN_AFTER");
            if (triggerModel != null)
            {
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
              TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }
          }
        }
        setReceiveUserName(UserContextUtil.getInstance().getUserName(this.receiveUser));
      }
      else
      {
        msg = "ERROR-10015";
      }
    }	
    else {
      msg = "ERROR-10005";
    }
    ResponseUtil.write(msg);
  }
  
  public void executeArchive()
  {
    boolean flag = false;
    String msg = "";
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.taskId != null))
    {
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = request.getParameterMap();
      flag = this.processRuntimeSendService.executeArchives(this.taskId, param);
      if (flag)
      {
//        if (this.ccUsers != null) {
//          this.processRuntimeSendService.excuteArchiveNoticeMsg(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
//        }
        this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
        if (this.ccUsers != null) {
          this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
        }
        msg = "success";
       
        
        UserContext ucs = UserContextUtil.getInstance().getCurrentUserContext();
        TestSendMes tms=new TestSendMes();
        tms.sendMsgList(this.receiveUser,ucs._userModel.getUsername(),this.instanceId,"审批通过",this.title);
      }
      else
      {
        msg = "ERROR-10005";
      }
    }
    else
    {
      msg = "ERROR-10005";
    }
    ResponseUtil.write(msg);
  }
  
  public void executeBack()
  {
    String msg = "";
    if ((this.taskId != null) && (this.targetStepId != null) && (this.receiveUser != null))
    {
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = request.getParameterMap();
      boolean isRun = true;
      HashMap triggerParams = new HashMap();
      if (isRun)
      {
        msg = this.processRuntimeSendService.executeBack(this.taskId, this.targetStepId, this.receiveUser, param, this.action, this.isRemindHistoricUser);
        UserContext ucs = UserContextUtil.getInstance().getCurrentUserContext();
        TestSendMes tms=new TestSendMes();
        tms.sendMsgList(this.receiveUser,ucs._userModel.getUsername(),this.instanceId,"驳回",this.title);
        if (this.ccUsers != null) {
          this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
        }
        if (this.action.equals("驳回"))
        {
          BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_BACK_AFTER");
          if (triggerModel != null)
          {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
            
            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
            isRun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
        }
      }
      else
      {
        msg = "ERROR-100016";
      }
      if (msg.equals("success")) {
        this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
      }
      BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.targetStepId, "TRAN_RECEVER_AFTER");
      if (triggerModel != null)
      {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
        triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
        triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
        triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
        triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
        triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
        triggerParams.put("PROCESS_PARAMETER_FORMDATA", param);
        TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
      }
    }
    else
    {
      msg = "ERROR-0005";
    }
    ResponseUtil.write(msg);
  }
  
  public void executeForward()
  {
    String msg = "";
    if ((this.receiveUser != null) && (this.receiveUser.length > 0))
    {
      StringBuffer content = new StringBuffer();
      String userId = UserContextUtil.getInstance().getUserId(this.receiveUser[0]);
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = request.getParameterMap();
      msg = this.processRuntimeSendService.executeForward(this.taskId, userId, param);
      if (this.opinion != null) {
        content.append(this.opinion).append("</br>");
      }
      if ((this.currentUser != null) && (!"".equals(this.currentUser)) && (userId != null)) {
        content.append(UserContextUtil.getInstance().getFullUserAddress(this.currentUser)).append("-->>").append(UserContextUtil.getInstance().getFullUserAddress(userId));
      }
      if (msg.equals("success")) {
        this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, System.currentTimeMillis()+"", this.excutionId, content.toString());
      }
    }
    ResponseUtil.write(msg);
  }
  
  public void executeAddSign()
  {
    String msg = "";
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.excutionId != null) && (this.taskId != null) && 
      (this.targetStepId != null) && (this.receiveUser != null))
    {
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = request.getParameterMap();
      msg = this.processRuntimeSendService.executeAddSign(this.actDefId, this.actStepDefId, this.taskId, this.targetStepId, this.receiveUser[0], param);
      if (this.action == null) {
        this.action = "加签";
      }
      if (this.ccUsers != null) {
        this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
      }
      this.processOpinionService.sendAction(this.action, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, this.excutionId, this.opinion);
    }
    ResponseUtil.write(msg);
  }
  
  public void doExcuteCC()
  {
    String msg = "error";
    if (this.ccUsers != null)
    {
      Map param = null;
      HttpServletRequest request = ServletActionContext.getRequest();
      param = request.getParameterMap();
      msg = this.processRuntimeSendService.excuteCC(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId, this.title, this.ccUsers, param);
    }
    ResponseUtil.write(msg);
  }
  
  public void reActivation()
  {
    this.processRuntimeSendService.reActivation(this.actDefId, this.actStepDefId, this.taskId, this.instanceId, this.excutionId);
  }
  
  public SysEngineIFormService getSysEngineIFormService()
  {
    return this.sysEngineIFormService;
  }
  
  public void setSysEngineIFormService(SysEngineIFormService sysEngineIFormService)
  {
    this.sysEngineIFormService = sysEngineIFormService;
  }
  
  public IFormService getIformService()
  {
    return this.iformService;
  }
  
  public void setIformService(IFormService iformService)
  {
    this.iformService = iformService;
  }
  
  public SysProcessDefinitionService getSysProcDefService()
  {
    return this.sysProcDefService;
  }
  
  public void setSysProcDefService(SysProcessDefinitionService sysProcDefService)
  {
    this.sysProcDefService = sysProcDefService;
  }
  
  public String getActDefId()
  {
    return this.actDefId;
  }
  
  public void setActDefId(String actDefId)
  {
    this.actDefId = actDefId;
  }
  
  public Long getPrcDefId()
  {
    return this.prcDefId;
  }
  
  public void setPrcDefId(Long prcDefId)
  {
    this.prcDefId = prcDefId;
  }
  
  public String getActStepDefId()
  {
    return this.actStepDefId;
  }
  
  public void setActStepDefId(String actStepDefId)
  {
    this.actStepDefId = actStepDefId;
  }
  
  public Long getFormId()
  {
    return this.formId;
  }
  
  public void setFormId(Long formId)
  {
    this.formId = formId;
  }
  
  public Long getInstanceId()
  {
    return this.instanceId;
  }
  
  public void setInstanceId(Long instanceId)
  {
    this.instanceId = instanceId;
  }
  
  public Long getExcutionId()
  {
    return this.excutionId;
  }
  
  public void setExcutionId(Long excutionId)
  {
    this.excutionId = excutionId;
  }
  
  public Long getDataid()
  {
    return this.dataid;
  }
  
  public void setDataid(Long dataid)
  {
    this.dataid = dataid;
  }
  
  public List<ProcessStepForm> getFormList()
  {
    return this.formList;
  }
  
  public void setFormList(List<ProcessStepForm> formList)
  {
    this.formList = formList;
  }
  
  public String getTaskId()
  {
    return this.taskId;
  }
  
  public void setTaskId(String taskId)
  {
    this.taskId = taskId;
  }
  
  public String getTargetStepId()
  {
    return this.targetStepId;
  }
  
  public void setTargetStepId(String targetStepId)
  {
    this.targetStepId = targetStepId;
  }
  
  public ProcessRuntimeSendService getProcessRuntimeSendService()
  {
    return this.processRuntimeSendService;
  }
  
  public void setProcessRuntimeSendService(ProcessRuntimeSendService processRuntimeSendService)
  {
    this.processRuntimeSendService = processRuntimeSendService;
  }
  
  public String[] getReceiveUser()
  {
    return this.receiveUser;
  }
  
  public void setReceiveUser(String[] receiveUser)
  {
    this.receiveUser = receiveUser;
  }
  
  public String getCcUsers()
  {
    return this.ccUsers;
  }
  
  public void setCcUsers(String ccUsers)
  {
    this.ccUsers = ccUsers;
  }
  
  public String getReceiveUserName()
  {
    return this.receiveUserName;
  }
  
  public void setReceiveUserName(String receiveUserName)
  {
    this.receiveUserName = receiveUserName;
  }
  
  public String getTargetStepName()
  {
    return this.targetStepName;
  }
  
  public void setTargetStepName(String targetStepName)
  {
    this.targetStepName = targetStepName;
  }
  
  public String getParallelGateway()
  {
    return this.parallelGateway;
  }
  
  public void setParallelGateway(String parallelGateway)
  {
    this.parallelGateway = parallelGateway;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  public ProcessStepTriggerService getProcessStepTriggerService()
  {
    return this.processStepTriggerService;
  }
  
  public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService)
  {
    this.processStepTriggerService = processStepTriggerService;
  }
  
  public HashMap getRemindTypeList()
  {
    return this.remindTypeList;
  }
  
  public void setRemindTypeList(HashMap remindTypeList)
  {
    this.remindTypeList = remindTypeList;
  }
  
  public String getAction()
  {
    return this.action;
  }
  
  public void setAction(String action)
  {
    this.action = action;
  }
  
  public ProcessOpinionService getProcessOpinionService()
  {
    return this.processOpinionService;
  }
  
  public void setProcessOpinionService(ProcessOpinionService processOpinionService)
  {
    this.processOpinionService = processOpinionService;
  }
  
  public String getCurrentUser()
  {
    return this.currentUser;
  }
  
  public void setCurrentUser(String currentUser)
  {
    this.currentUser = currentUser;
  }
  
  public String getOpinion()
  {
    return this.opinion;
  }
  
  public void setOpinion(String opinion)
  {
    this.opinion = opinion;
  }
  
  public void setIsRemindHistoricUser(Long isRemindHistoricUser)
  {
    this.isRemindHistoricUser = isRemindHistoricUser;
  }
  
  public Long getBackType()
  {
    return this.backType;
  }
  
  public void setBackType(Long backType)
  {
    this.backType = backType;
  }
  
  public String getAttach()
  {
    return this.attach;
  }
  
  public void setAttach(String attach)
  {
    this.attach = attach;
  }
}
