package com.iwork.process.runtime.action;

import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.constant.IFormStatusConstants;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.step.model.ProcessStepForm;
import com.iwork.process.definition.step.model.ProcessStepJstrigger;
import com.iwork.process.definition.step.service.ProcessStepFormMapService;
import com.iwork.process.definition.step.service.ProcessStepFormService;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.model.ProcessRuSigns;
import com.iwork.process.runtime.model.ProcessTaskStatus;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.runtime.service.ProcessRuntimeSignsService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.struts2.ServletActionContext;

public class ProcessRuntimeExcuteAction extends ActionSupport
{
  private ProcessRuntimeExcuteService processRuntimeExcuteService;
  private ProcessDeploymentService processDeploymentService;
  private ProcessOpinionService processOpinionService;
  private SysEngineIFormService sysEngineIFormService;
  private ProcessStepFormService processStepFormService;
  private ProcessStepFormMapService processStepFormMapService;
  private ProcessStepTriggerService processStepTriggerService;
  private ProcessRuntimeSignsService processRuntimeSignsService;
  private IFormService iformService;
  private List<ProcessStepForm> formList;
  private String actDefId;
  private Long prcDefId = new Long(0L);
  private String actStepDefId = "";
  private String prcStepName = "";
  private Long formId = new Long(0L);
  private String taskId = "0";
  private Long instanceId = new Long(0L);
  private Long excutionId = new Long(0L);
  private Long dataid = new Long(0L);
  private Long stepformId = new Long(0L);
  private Long isLog;
  private Long isTalk;
  private Long id;
  private Long modelId;
  private Long subformid;
  private Long engineType;
  private String modelType;
  private Long formIsModify;
  private String oper;
  private Long taskType;
  private String formNo;
  private String targetActivityId;
  private String processButton;
  private String processExtendButton;
  private String processTransButton;
  private String processShortcutsButton;
  private String subformkey;
  private String pageTab;
  private String title;
  private String style;
  private String script;
  private String saveScriptEvent;
  private String transScriptEvent;
  private String initScriptEvent;
  private String forminfo;
  private String content;
  private String info;
  private String opinionList;
  private Map params;
  private String JDMC;
  private String XMJD;




public String getXMJD() {
	return XMJD;
}

public void setXMJD(String xMJD) {
	XMJD = xMJD;
}

public String getJDMC() {
	return JDMC;
}

public void setJDMC(String jDMC) {
	JDMC = jDMC;
}

public String startProcessInstance()
  {
	 
    boolean flag = true;
    this.taskType = new Long(0L);
    String page = "";

    String style_html = "";
    String script_html = "";
    if ((this.actDefId != null) && (!this.actDefId.equals("")) && (this.instanceId != null))
    {
      HttpServletRequest request = ServletActionContext.getRequest();
      Map pageParams = request.getParameterMap();
      ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(this.actDefId);
      if (model != null)
      {
        setPrcDefId(model.getId());

        if ((this.actStepDefId == null) || ("".equals(this.actStepDefId))) {
          setActStepDefId(this.processRuntimeExcuteService.getProcessTopStepId(this.actDefId));
        }
        setPrcStepName(this.prcStepName);

        setFormList(this.processRuntimeExcuteService.getStepFromList(this.actDefId, model.getId(), this.actStepDefId));
        if (getFormList() == null) flag = false;

        if (flag) {
          ProcessTaskStatus processTaskStatus = this.processRuntimeExcuteService.getTaskStatusInfo(this.formList, this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId);

          this.isLog = processTaskStatus.getIsLog();
          this.modelId = this.prcDefId;
          this.modelType = "PROCESS";

          processTaskStatus.setTasktype(7);

          if (processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM))
          {
            if ((this.formId == null) || (this.formId.equals(new Long(0L)))) {
              setFormId(processTaskStatus.getFormid());
            }

            setPageTab(this.processRuntimeExcuteService.getProcessFormTab(this.formId, this.formList, processTaskStatus.getIsTalk()));

            if (processTaskStatus != null)
            {
              Long processStepFormId = new Long(0L);
              if (processTaskStatus.getProcessStepForm() != null) {
                processStepFormId = processTaskStatus.getProcessStepForm().getId();
                this.formIsModify = processTaskStatus.getProcessStepForm().getIsModify();
              }

              HashMap psfmList = this.processStepFormMapService.getFormMapList(this.actDefId, this.prcDefId, this.actStepDefId, processStepFormId);

              if (processTaskStatus.isModify())
                page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_MODIFY, psfmList, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
              else
                page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_READ, psfmList, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
            }
            else {
              page = this.iformService.getFormPage(this.formId, this.instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
            }

            SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.formId);
            if (iformModel != null)
            {
              if (iformModel.getIformCss() != null)
                style_html = iformModel.getIformCss();
              if (iformModel.getIformJs() != null) {
                script_html = iformModel.getIformJs();
              }
            }
            setStyle(style_html);

            setScript(script_html);
            setForminfo("");
            setContent(page);
            setInfo(this.info);
            flag = true;
          }
          else {
            setPageTab(this.processRuntimeExcuteService.getProcessFormTab(this.formId, this.formList, processTaskStatus.getIsTalk()));
            String url = "";
            if (processTaskStatus.getProcessStepForm() != null)
              url = processTaskStatus.getProcessStepForm().getUrl();
            setContent(" <iframe width=\"100%\" id=\"urlFrame\"  scrolling='auto' onLoad='iFrameHeight(\"urlFrame\")'  frameborder=\"0\" src=\"" + url + "\"></iframe>");
            setInfo(this.info);
          }

          setProcessButton(this.processRuntimeExcuteService.getProcessPageButton(processTaskStatus, this.actDefId, model.getId(), this.actStepDefId, this.taskId));

          setProcessExtendButton(this.processRuntimeExcuteService.getProcessExtendsButton(this.actDefId, model.getId(), this.actStepDefId, this.instanceId, this.taskId));

          setProcessShortcutsButton(this.processRuntimeExcuteService.getProcessShortcutsButton(processTaskStatus, this.actDefId, this.prcDefId, this.actStepDefId, this.taskId));

          ProcessStepJstrigger jsTriggerModel = this.processRuntimeExcuteService.getProcessStepJsTriggerModel(this.actDefId, this.actStepDefId, this.prcDefId);
          if (jsTriggerModel != null) {
            setSaveScriptEvent(jsTriggerModel.getSaveJs());
            setTransScriptEvent(jsTriggerModel.getTranJs());
            setInitScriptEvent(jsTriggerModel.getInitJs());
          }
        }
      }
      else
      {
        flag = false;
      }
    }
    else {
      flag = false;
    }

    if (flag)
    {
      SysPersonConfig spconf = UserContextUtil.getInstance().getCurrentUserConfig("formLayoutSet");
      String layout = "";
      if (spconf != null)
      {
        layout = spconf.getValue();

        if ((layout != null) && (layout.equals("left")))
          return "layout_left";
        if ((layout != null) && (layout.equals("right")))
          return "layout_right";
        if ((layout != null) && (layout.equals("top")))
          return "layout_top";
        if ((layout != null) && (layout.equals("bottom"))) {
          return "layout_bottom";
        }
      }
      return "success";
    }
    return "error";
  }

  public String loadFormPage()
  {
    String page = "";

    String style_html = "";
    this.taskType = new Long(1L);
    boolean isSigns = false;
    String script_html = "";
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    if (this.actDefId != null)
    {
    	String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
  	  boolean matches=actDefId.matches(match);
  	  if(!matches){
  			return "error";
  	  }
      if ((this.instanceId != null) && (!this.instanceId.equals(new Long(0L))) && (this.taskId != null) && (!this.taskId.equals("0"))) {
        if (!this.processRuntimeExcuteService.isCheckTask(this.instanceId, this.taskId)) return "error"; 
      }
      else {
        return "error";
      }

      ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(this.actDefId);
      if (model != null)
      {
        this.modelId = this.prcDefId;
        this.modelType = "PROCESS";

        HttpServletRequest request = ServletActionContext.getRequest();
        Map pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());

        setPrcDefId(model.getId());
        if ((this.actStepDefId == null) || ("".equals(this.actStepDefId))) {
          Task task = (Task)this.processRuntimeExcuteService.getTaskService().createTaskQuery().taskId(this.taskId).singleResult();
          if (task == null) {
            if ((this.excutionId != null) && (this.excutionId.longValue() != 0L)) {
              ExecutionEntity ee = this.processRuntimeExcuteService.getProcessStepId(this.excutionId);
              if (ee != null) {
                setActStepDefId(ee.getActivityId());
                pageParams.put("actStepDefId", ee.getActivityId());
                
                
              }
            }
          }
          else this.actStepDefId = task.getTaskDefinitionKey();
          if ((this.actStepDefId == null) || ("".equals(this.actStepDefId))) {
                /*HashMap fromData = ProcessAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
                this.actStepDefId= fromData.get("LZJD")==null?"":fromData.get("LZJD").toString();*/
                if(this.actStepDefId==null || "".equals(this.actStepDefId)){
                    this.actStepDefId=DBUTilNew.getDataStr("task_def_key_","select s.task_def_key_ from process_hi_taskinst s where s.id_="+taskId,null);
                }
        	}
        }else{
        	
        }

        List list = this.processRuntimeExcuteService.getStepFromList(this.actDefId, this.prcDefId, this.actStepDefId);
        if(list==null || list.size()==0) return "task_over";
        ProcessTaskStatus processTaskStatus = this.processRuntimeExcuteService.getTaskStatusInfo(list, this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId);

        this.isLog = processTaskStatus.getIsLog();
        this.isTalk = processTaskStatus.getIsTalk();

        if (this.taskId != null) {
          isSigns = this.processRuntimeSignsService.checkSigns(this.actDefId, this.actStepDefId, this.instanceId, this.excutionId, Long.valueOf(Long.parseLong(this.taskId)));
        }
        ProcessStepForm psf = processTaskStatus.getProcessStepForm();
        if ((list != null) && (psf != null) && (psf.getId() != null)) {
          setFormId(psf.getFormid());
          setFormList(list);
          HashMap triggerParams = new HashMap();

          if (processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM))
          {
            if ((this.formId == null) || (this.formId.equals(new Long(0L)))) {
              setFormId(processTaskStatus.getFormid());
            }
            boolean isTrigger = false;

            if ((this.processRuntimeExcuteService.isCheckTaskTrigger(this.instanceId, this.actStepDefId, uc)) && (!isSigns))
            {
              BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_LOAD");
              if (triggerModel != null)
              {
                triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
                triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
                triggerParams.put("PROCESS_PARAMETER_FORMID", getFormId());
                triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
                triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
                triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
                isTrigger = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
              }

            }

            setPageTab(this.processRuntimeExcuteService.getProcessFormTab(this.stepformId, this.formList, processTaskStatus.getIsTalk()));

            if (processTaskStatus != null)
            {
              Long processStepFormId = new Long(0L);
              if (processTaskStatus.getProcessStepForm() != null) {
                processStepFormId = processTaskStatus.getProcessStepForm().getId();
                this.formIsModify = processTaskStatus.getProcessStepForm().getIsModify();
              }

              HashMap psfmList = this.processStepFormMapService.getFormMapList(this.actDefId, this.prcDefId, this.actStepDefId, processStepFormId);

              long s1 = System.currentTimeMillis();
              if ((processTaskStatus.isModify()) && (!isSigns)) {
                if (processTaskStatus.isTaskOwner())
                  page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_MODIFY, psfmList, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
                else
                  page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_READ, psfmList, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
              }
              else {
                page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_READ, psfmList, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
              }
              long e1 = System.currentTimeMillis();
              System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>" + (e1 - s1));
            } else {
              page = this.iformService.getFormPage(this.formId, this.instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
            }
            if ((this.processRuntimeExcuteService.isCheckTaskTrigger(this.instanceId, this.actStepDefId, uc)) && (!isSigns))
            {
              BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_LOAD_AFTER");
              if (triggerModel != null) {
                triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
                triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
                triggerParams.put("PROCESS_PARAMETER_FORMID", getFormId());
                triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
                triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
                triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
                TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
              }

            }

            SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.formId);
            if (iformModel != null)
            {
              if (iformModel.getIformCss() != null)
                style_html = iformModel.getIformCss();
              if (iformModel.getIformJs() != null) {
                script_html = iformModel.getIformJs();
              }
            }
            setStyle(style_html);

            setScript(script_html);
            setForminfo("");
            setContent(page);
            setInfo(this.info);
          }
          else {
            setPageTab(this.processRuntimeExcuteService.getProcessFormTab(this.stepformId, this.formList, processTaskStatus.getIsTalk()));
            String url = "";
            if (processTaskStatus.getProcessStepForm() != null) {
              url = processTaskStatus.getProcessStepForm().getUrl();
            }
            StringBuffer url_ext = new StringBuffer();
            url_ext.append("actDefId=").append(this.actDefId).append("&instanceId=").append(this.instanceId);

            if (!url.equals("")) {
              if (url.indexOf("?") > 0)
                url = url + "&" + url_ext.toString();
              else {
                url = url + "?" + url_ext.toString();
              }
              setContent(this.processRuntimeExcuteService.getUrlTypeContext(url));
            }
            setInfo(this.info);
          }

          if (!isSigns) {
            setProcessButton(this.processRuntimeExcuteService.getProcessPageButton(processTaskStatus, this.actDefId, this.prcDefId, this.actStepDefId, this.taskId));

            setProcessExtendButton(this.processRuntimeExcuteService.getProcessExtendsButton(this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId));

            setProcessShortcutsButton(this.processRuntimeExcuteService.getProcessShortcutsButton(processTaskStatus, this.actDefId, this.prcDefId, this.actStepDefId, this.taskId));
          } else {
            setProcessButton(this.processRuntimeExcuteService.getProcessSignsButton(this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId));
          }

          ProcessStepJstrigger jsTriggerModel = this.processRuntimeExcuteService.getProcessStepJsTriggerModel(this.actDefId, this.actStepDefId, this.prcDefId);
          if (jsTriggerModel != null) {
            setInitScriptEvent(jsTriggerModel.getInitJs());
            setSaveScriptEvent(jsTriggerModel.getSaveJs());
            setTransScriptEvent(jsTriggerModel.getTranJs());
          }

          if (this.formId != null) {
            Long dataid = this.iformService.getDataid(this.formId, this.instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
            setDataid(dataid);
          }

          this.opinionList = this.processOpinionService.getProcessInstanceOpinionList(true, this.actDefId, this.actStepDefId, this.instanceId.longValue());

          if (this.taskId != null) {
            Task task = this.processRuntimeExcuteService.getProcessTask(this.taskId);
            if (task != null)
              setTitle("[" + task.getName() + "]" + task.getDescription());
          }
        }
        else {
          addActionError("表单加载异常 ");
          setContent("表单加载异常 ");
        }

        SysPersonConfig spconf = UserContextUtil.getInstance().getCurrentUserConfig("formLayoutSet");
        String layout = "";
        if (spconf != null)
        {
          layout = spconf.getValue();

          if ((layout != null) && (layout.equals("left")))
            return "layout_left";
          if ((layout != null) && (layout.equals("right")))
            return "layout_right";
          if ((layout != null) && (layout.equals("top")))
            return "layout_top";
          if ((layout != null) && (layout.equals("bottom"))) {
            return "layout_bottom";
          }
        }
        return "success";
      }
      return "task_over";
    }

    return "task_over";
  }

  public String loadSignsPage()
  {
    String page = "";

    String style_html = "";
    this.taskType = new Long(1L);
    String script_html = "";
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    if (this.actDefId != null)
    {
      if ((this.instanceId != null) && (!this.instanceId.equals(new Long(0L))) && (this.taskId != null) && (!this.taskId.equals("0"))) {
        if (!this.processRuntimeExcuteService.isCheckTask(this.instanceId, this.taskId)) return "error"; 
      }
      else {
        return "error";
      }

      ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(this.actDefId);
      if (model != null)
      {
        this.modelId = this.prcDefId;
        this.modelType = "PROCESS";

        HttpServletRequest request = ServletActionContext.getRequest();
        Map pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());

        setPrcDefId(model.getId());
        if ((this.actStepDefId == null) || ("".equals(this.actStepDefId))) {
          Task task = (Task)this.processRuntimeExcuteService.getTaskService().createTaskQuery().taskId(this.taskId).singleResult();
          if (task == null) {
            if ((this.excutionId != null) && (this.excutionId.longValue() != 0L)) {
              ExecutionEntity ee = this.processRuntimeExcuteService.getProcessStepId(this.excutionId);
              if (ee != null) {
                setActStepDefId(ee.getActivityId());
                pageParams.put("actStepDefId", ee.getActivityId());
              }
            }
          }
          else this.actStepDefId = task.getTaskDefinitionKey();
        }

        List list = this.processRuntimeExcuteService.getStepFromList(this.actDefId, this.prcDefId, this.actStepDefId);
        ProcessTaskStatus processTaskStatus = this.processRuntimeExcuteService.getTaskStatusInfo(list, this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId);

        this.isLog = processTaskStatus.getIsLog();
        this.isTalk = processTaskStatus.getIsTalk();
        ProcessStepForm psf = processTaskStatus.getProcessStepForm();
        if ((list != null) && (psf != null) && (psf.getId() != null)) {
          setFormId(psf.getFormid());
          setFormList(list);
          HashMap triggerParams = new HashMap();

          setPageTab(this.processRuntimeExcuteService.getProcessFormTab(this.stepformId, this.formList, processTaskStatus.getIsTalk()));
          if (processTaskStatus.getFormtype().equals(ProcessStepForm.FORM_BIND_TYPE_IFORM))
          {
            if ((this.formId == null) || (this.formId.equals(new Long(0L)))) {
              setFormId(processTaskStatus.getFormid());
            }
            page = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_READ, null, this.taskId, pageParams, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

            SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.formId);
            if (iformModel != null)
            {
              if (iformModel.getIformCss() != null)
                style_html = iformModel.getIformCss();
              if (iformModel.getIformJs() != null) {
                script_html = iformModel.getIformJs();
              }
            }
            setStyle(style_html);

            setScript(script_html);
            setForminfo("");
            setContent(page);
            setInfo(this.info);
          }
          else {
            String url = "";
            if (processTaskStatus.getProcessStepForm() != null) {
              url = processTaskStatus.getProcessStepForm().getUrl();
            }
            StringBuffer url_ext = new StringBuffer();
            url_ext.append("actDefId=").append(this.actDefId).append("&instanceId=").append(this.instanceId);

            if (!url.equals("")) {
              if (url.indexOf("?") > 0)
                url = url + "&" + url_ext.toString();
              else {
                url = url + "?" + url_ext.toString();
              }
              setContent(this.processRuntimeExcuteService.getUrlTypeContext(url));
            }
            setInfo(this.info);
          }

          List<ProcessRuSigns> signlist = this.processRuntimeSignsService.getProcessRuntimeSignsDAO().getSignsList(this.actDefId, this.actStepDefId, this.instanceId, this.excutionId, Long.valueOf(Long.parseLong(this.taskId)), uc.get_userModel().getUserid());
          if ((signlist != null) && (signlist.size() > 0)) {
            for (ProcessRuSigns prs : signlist) {
              if (prs.getStatus().equals(ProcessRuntimeSignsService.PROCESS_TASK_STATUS_FINISH)) {
                setProcessButton(this.processRuntimeExcuteService.getProcessDoSignsButton(false, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, false));
              } else if (prs.getStatus().equals(ProcessRuntimeSignsService.PROCESS_TASK_STATUS_UNREAD)) {
                setProcessButton(this.processRuntimeExcuteService.getProcessDoSignsButton(true, this.actDefId, this.prcDefId, this.actStepDefId, this.instanceId, this.taskId, false));
                this.processRuntimeSignsService.setReadTime(this.actDefId, this.actStepDefId, this.instanceId, this.excutionId, this.taskId);
                break;
              }

            }

          }

          ProcessStepJstrigger jsTriggerModel = this.processRuntimeExcuteService.getProcessStepJsTriggerModel(this.actDefId, this.actStepDefId, this.prcDefId);
          if (jsTriggerModel != null) {
            setInitScriptEvent(jsTriggerModel.getInitJs());
            setSaveScriptEvent(jsTriggerModel.getSaveJs());
            setTransScriptEvent(jsTriggerModel.getTranJs());
          }

          if (this.formId != null) {
            Long dataid = this.iformService.getDataid(this.formId, this.instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
            setDataid(dataid);
          }

          this.opinionList = this.processOpinionService.getProcessInstanceOpinionList(true, this.actDefId, this.actStepDefId, this.instanceId.longValue());

          if (this.taskId != null) {
            Task task = this.processRuntimeExcuteService.getProcessTask(this.taskId);
            if (task != null)
              setTitle("[会签]" + task.getDescription());
          }
        }
        else {
          addActionError("表单加载异常 ");
          setContent("表单加载异常 ");
        }

        SysPersonConfig spconf = UserContextUtil.getInstance().getCurrentUserConfig("formLayoutSet");
        String layout = "";
        if (spconf != null)
        {
          layout = spconf.getValue();

          if ((layout != null) && (layout.equals("left")))
            return "layout_left";
          if ((layout != null) && (layout.equals("right")))
            return "layout_right";
          if ((layout != null) && (layout.equals("top")))
            return "layout_top";
          if ((layout != null) && (layout.equals("bottom"))) {
            return "layout_bottom";
          }
        }
        return "success";
      }
      return "task_over";
    }

    return "task_over";
  }

  public String loadNoticeFormPage()
  {
    if (this.dataid != null) {
      this.processRuntimeExcuteService.setCCisRead(this.dataid);
    }
    return loadFormPage();
  }

  public String processFormSave()
  {
	  if(this.actDefId != null){
		  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
			  return "ERROR-10009";
		  }
	  }
		if( this.actStepDefId!=null){
			 if( !DBUTilNew.validActStepId(this.actStepDefId) ){
				 return "ERROR-10009";
		     }
		}
	  try
	  {
    String s_code = "success";
    boolean flag = false;
    boolean islog = false;
    HashMap triggerParams = new HashMap();
    Long type = new Long(1L);
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    HttpServletRequest request = ServletActionContext.getRequest();
    if (this.params == null) {
      this.params = request.getParameterMap();
    }
    if(this.params!=null && this.formId!=null){
    	try {
			List list=new ArrayList();
			list.add("fieldname");
			list.add("fieldtitle");
			list.add("fieldtype");
			list.add("field_length");
			Map params=new HashMap();
			params.put(1,this.formId);
			String sql="select s.fieldname,s.fieldtitle,s.fieldtype,s.field_length from  sys_engine_metadata_map s where s.metadataid=(select t.metadataid from SYS_ENGINE_IFORM t where id = ?)";
			List<HashMap> dataList = DBUTilNew.getDataList(list, sql, params);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
			for(Iterator iter = this.params.entrySet().iterator();iter.hasNext();){ 
			    Map.Entry element = (Map.Entry)iter.next(); 
			    Object strKey = element.getKey();
			    String[] strObj =  (String[])element.getValue();
			   	for (int i = 0; i < dataList.size(); i++) {
			   		if(strKey.toString().toUpperCase().equals(dataList.get(i).get("fieldname"))){
			   			if(dataList.get(i).get("fieldtype").equals("日期")||dataList.get(i).get("fieldtype").equals("日期时间")){
							Long d1=0L;
							if(strObj[0]!=null && !"".equals(strObj[0].toString())){
								try {
									d1 = sdf.parse(strObj[0].toString()).getTime();
								} catch (Exception e) {
									 try {
										//Date date=sdf1.parse(strObj[0].toString());
										// d1=Long.parseLong(sdf.format(date));
										 Date date=sdf1.parse(strObj[0].toString().toString());
											// d1=Long.parseLong(sdf.format(date));
											 String a=sdf.format(date);
											 d1 = sdf.parse(a).getTime();
									} catch (Exception e1) {
										return "ERROR-10009";
									}
								}	
							}
							if(strObj[0]==null || strObj[0].toString().equals(""))
								break;
							else if(d1>sdf.parse("1900-01-01").getTime()&&d1<sdf.parse("3000-12-31").getTime()){
								break;							
							}else{
								return "ERROR-10009";
							}
					}else {
						if(dataList.get(i).get("field_length")==null||dataList.get(i).get("field_length").equals(""))
							break;
						else if(dataList.get(i).get("field_length").toString().contains(",")){
							if(strObj[0] == null || strObj[0].equals("")) 
								break;
							else{
								int zsLength = Integer.parseInt(dataList.get(i).get("field_length").toString().substring(0, dataList.get(i).get("field_length").toString().indexOf(",")));
								String strXS = dataList.get(i).get("field_length").toString().substring(dataList.get(i).get("field_length").toString().indexOf(",")+1);
								if(strXS.equals("")) strXS="0";
								int xsLength = Integer.parseInt(strXS);
								zsLength = zsLength - xsLength;
								String str = strObj[0].toString();
								 Pattern p = Pattern.compile("^-?\\d{1,"+zsLength+"}(\\.\\d{0,"+xsLength+"})?$");
								 if(!p.matcher(str).matches()){
									 return "ERROR-10009";
								 }
								 else
									 break;
							}
						}else if(!dataList.get(i).get("field_length").toString().contains(",") && Integer.parseInt(dataList.get(i).get("field_length").toString())<strObj[0].toString().length()){	
								return "ERROR-10009";
						}else{
							 break;
						}
						}
			   		}
			   		
				}
			}
		} catch (NumberFormatException e) {
			
		} catch (ParseException e) {
		} 
    }else{
    	return "ERROR-10009";
    }
    if ((this.formId != null) && (this.actDefId != null) && (this.prcDefId != null))
    {
      if ((this.isLog != null) && (this.isLog.equals(SysConst.on))) {
        islog = true;
      }
      String str1 = this.params.toString();
      if ((this.instanceId != null) && (this.instanceId.longValue() > 0L) && (this.dataid.longValue() != 0L))
      {
        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_BEFOR");
        boolean isrun = true;
        if (triggerModel != null)
        {
          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", this.params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          flag = this.iformService.updateForm(this.prcDefId, this.formId, this.instanceId, this.params, this.dataid, islog, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
          if (!flag) {
            s_code = "ERROR-10009";
          }
          else {
            triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_AFTER");
            if (triggerModel != null)
            {
              triggerParams = new HashMap();
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", this.params);
              isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }

          }

          this.processRuntimeExcuteService.addTaskFormIndex(this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId, this.instanceId, this.excutionId, this.params);
        } else {
          s_code = "ERROR-10009";
        }
      }
      else
      { 	String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
      		boolean matches=this.actDefId.matches(match);
      		if(!matches){
      			 s_code = "ERROR-10009";
      			ResponseUtil.write(s_code);
      			return null;
      		}
      	  if( !DBUTilNew.validActStepId(this.actStepDefId) ){
    		  return null;
    		 }
        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_BEFOR");
        boolean isrun = true;
        if (triggerModel != null)
        {
          triggerParams = new HashMap();
          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", this.params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          if ((this.instanceId == null) || (this.instanceId.longValue() == 0L)) {
            this.instanceId = this.processRuntimeExcuteService.initProcessInstanceId(this.actDefId, this.prcDefId, uc.get_userModel().getUserid(), this.formNo, this.params);
            if(instanceId==null||instanceId==0) {
            	 s_code = "ERROR-10009";
       			ResponseUtil.write(s_code);
       			return null;
			}
            this.iformService.initInstanceId(this.formId, this.instanceId, type);
          }
          if (this.instanceId.longValue() > 0L) {
            this.dataid = this.iformService.saveForm(this.prcDefId, this.formId, this.instanceId, this.params, islog, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

            triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_AFTER");
            if (triggerModel != null) {
              triggerParams = new HashMap();
              triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
              triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
              triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
              triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
              triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
              triggerParams.put("PROCESS_PARAMETER_FORMDATA", this.params);
              TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
            }

            Task task = this.processRuntimeExcuteService.getProcessTask(this.instanceId);
            if (task != null) {
              this.taskId = task.getId();
              this.excutionId = Long.valueOf(Long.parseLong(task.getExecutionId()));
            }
            if (this.dataid.longValue() > 0L) {
              s_code = "success";

              this.processRuntimeExcuteService.addTaskFormIndex(this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId, this.instanceId, this.excutionId, this.params);
            } else {
              s_code = "ERROR-10010";
            }
          } else {
            s_code = "ERROR-10009";
          }
        }
        else {
          s_code = "ERROR-10009";
        }
      }
    }
    else {
      s_code = "ERROR-10009";
    }
    String str = s_code + "," + this.instanceId + "," + this.dataid + "," + this.taskId + "," + this.excutionId;
    ResponseUtil.write(str);
	  }catch(Exception ex)
	  {
		  
	  }
    return null;
  }

  public String loadSubformPage()
  {
	  if(this.actDefId != null){
		  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
			  return "error";
		  }
	  }
		if( this.actStepDefId!=null){
			 if( !DBUTilNew.validActStepId(this.actStepDefId) ){
				 return "error";
		     }
		}
    Long instanceid = getInstanceId();
    boolean flag = false;
    String style_html = "";
    String script_html = "";
    if (this.subformid != null) {
      HttpServletRequest request = ServletActionContext.getRequest();
      Map params = request.getParameterMap();
      if (this.info == null) this.info = "";

      SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.subformid);

      if (iformModel != null)
      {
        List list = this.processRuntimeExcuteService.getStepFromList(this.actDefId, this.prcDefId, this.actStepDefId);
        ProcessTaskStatus processTaskStatus = this.processRuntimeExcuteService.getTaskStatusInfo(list, this.actDefId, this.prcDefId, this.actStepDefId, this.stepformId, this.taskId);

        HashMap psfmList = this.processStepFormMapService.getFormMapList(this.actDefId, this.prcDefId, this.actStepDefId, processTaskStatus.getProcessStepForm().getId());

        this.isLog = processTaskStatus.getIsLog();
        if (processTaskStatus != null) {
          flag = processTaskStatus.isModify();
        }
        this.engineType = new Long(1L);
        if (instanceid == null) instanceid = new Long(0L);
        BaseTriggerModel triggerModel = null;
        boolean isrun = true;

        String page = "";
        if (this.dataid == null) this.dataid = new Long(0L);
        Long formStatus = new Long(0L);
        if (flag)
          formStatus = IFormStatusConstants.FORM_PAGE_MODIFY;
        else {
          formStatus = IFormStatusConstants.FORM_PAGE_READ;
        }
        page = this.iformService.getSubFormPage(this.subformid, instanceid, this.dataid, formStatus, psfmList, this.taskId, params, this.engineType);
        if (iformModel.getIformCss() != null)
          style_html = iformModel.getIformCss();
        if (iformModel.getIformJs() != null) {
          script_html = iformModel.getIformJs();
        }
        if ((this.dataid == null) || (this.dataid.equals(new Long(0L)))) {
          Long localLong1 = this.iformService.getDataid(this.subformid, instanceid, this.engineType);
        }
        setId(this.dataid);

        setTitle(iformModel.getIformTitle());

        setStyle(style_html);

        setScript(script_html);
        setForminfo("");
        setContent(page);
        setInfo(this.info);
        setInstanceId(instanceid);
      } else {
        flag = false;
      }
    } else {
      flag = false;
    }
    return "success";
  }

  public void processFormDel()
  {
    String msg = "";

    if ((this.taskId != null) && (this.instanceId != null)) {
      msg = this.processRuntimeExcuteService.removeInstanceData(this.taskId, this.instanceId);
    }
    if ((msg != null) && (msg.equals("success")))
      ResponseUtil.write("success");
    else if ((msg != null) && (msg.equals("error")))
      ResponseUtil.write("error");
    else
      ResponseUtil.write(msg);
  }

  public String print()
  {
    String style_html = "";
    String script_html = "";
    if ((this.formId == null) || (this.formId.equals(new Long(0L)))) {
      List list = this.processRuntimeExcuteService.getStepFromList(this.actDefId, this.prcDefId, this.actStepDefId);
      ProcessStepForm psf = this.processRuntimeExcuteService.getDefProcessStepForm(this.formId, list);
      this.formId = psf.getFormid();
    }
    this.content = this.iformService.getFormPage(this.formId, this.instanceId, IFormStatusConstants.FORM_PAGE_READ, null, this.taskId, null, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

    SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.formId);
    if (iformModel != null)
    {
      if (iformModel.getIformCss() != null)
        style_html = iformModel.getIformCss();
      if (iformModel.getIformJs() != null) {
        script_html = iformModel.getIformJs();
      }
    }
    setStyle(style_html);

    setScript(script_html);
    setForminfo("");
    setInfo(this.info);

    setOpinionList(this.processOpinionService.getProcessInstanceOpinionList(false, this.actDefId, this.actStepDefId, this.instanceId.longValue()));

    ProcessStepJstrigger jsTriggerModel = this.processRuntimeExcuteService.getProcessStepJsTriggerModel(this.actDefId, this.actStepDefId, this.prcDefId);
    if (jsTriggerModel != null) {
      setInitScriptEvent(jsTriggerModel.getInitJs());
      setSaveScriptEvent(jsTriggerModel.getSaveJs());
      setTransScriptEvent(jsTriggerModel.getTranJs());
    }
    return "success";
  }

  public String showProcessInstanceJson()
  {
    if ((this.actDefId != null) && (this.instanceId != null)) {
      HttpServletRequest request = ServletActionContext.getRequest();
      HttpServletResponse response = ServletActionContext.getResponse();
      Map params = request.getParameterMap();
      String json = this.processRuntimeExcuteService.showProcessInstanceJson(this.actDefId, this.instanceId, params, response);
      ResponseUtil.write(json);
      return null;
    }
    return null;
  }

  public ProcessRuntimeExcuteService getProcessRuntimeExcuteService()
  {
    return this.processRuntimeExcuteService;
  }

  public void setProcessRuntimeExcuteService(ProcessRuntimeExcuteService processRuntimeExcuteService)
  {
    this.processRuntimeExcuteService = processRuntimeExcuteService;
  }
  public SysEngineIFormService getSysEngineIFormService() {
    return this.sysEngineIFormService;
  }

  public void setSysEngineIFormService(SysEngineIFormService sysEngineIFormService) {
    this.sysEngineIFormService = sysEngineIFormService;
  }
  public IFormService getIformService() {
    return this.iformService;
  }

  public void setIformService(IFormService iformService) {
    this.iformService = iformService;
  }

  public String getProcessButton() {
    return this.processButton;
  }

  public void setProcessButton(String processButton) {
    this.processButton = processButton;
  }

  public ProcessDeploymentService getProcessDeploymentService()
  {
    return this.processDeploymentService;
  }

  public void setProcessDeploymentService(ProcessDeploymentService processDeploymentService)
  {
    this.processDeploymentService = processDeploymentService;
  }

  public String getActDefId() {
    return this.actDefId;
  }

  public void setActDefId(String actDefId) {
    this.actDefId = actDefId;
  }

  public Long getPrcDefId() {
    return this.prcDefId;
  }

  public void setPrcDefId(Long prcDefId) {
    this.prcDefId = prcDefId;
  }

  public String getActStepDefId() {
    return this.actStepDefId;
  }

  public void setActStepDefId(String actStepDefId) {
    this.actStepDefId = actStepDefId;
  }

  public Long getFormId() {
    return this.formId;
  }

  public void setFormId(Long formId) {
    this.formId = formId;
  }

  public Long getInstanceId() {
    return this.instanceId;
  }

  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }

  public Long getExcutionId() {
    return this.excutionId;
  }

  public void setExcutionId(Long excutionId) {
    this.excutionId = excutionId;
  }

  public Long getDataid() {
    return this.dataid;
  }

  public void setDataid(Long dataid) {
    this.dataid = dataid;
  }
  public List<ProcessStepForm> getFormList() {
    return this.formList;
  }

  public void setFormList(List<ProcessStepForm> formList) {
    this.formList = formList;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStyle() {
    return this.style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getScript() {
    return this.script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public String getForminfo() {
    return this.forminfo;
  }

  public void setForminfo(String forminfo) {
    this.forminfo = forminfo;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getInfo() {
    return this.info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getProcessExtendButton() {
    return this.processExtendButton;
  }
  public void setProcessExtendButton(String processExtendButton) {
    this.processExtendButton = processExtendButton;
  }

  public String getPageTab() {
    return this.pageTab;
  }

  public void setPageTab(String pageTab) {
    this.pageTab = pageTab;
  }

  public String getTaskId() {
    return this.taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getProcessTransButton() {
    return this.processTransButton;
  }

  public void setProcessTransButton(String processTransButton) {
    this.processTransButton = processTransButton;
  }

  public String getTargetActivityId() {
    return this.targetActivityId;
  }

  public void setTargetActivityId(String targetActivityId) {
    this.targetActivityId = targetActivityId;
  }

  public String getOpinionList() {
    return this.opinionList;
  }
  public void setOpinionList(String opinionList) {
    this.opinionList = opinionList;
  }
  public ProcessOpinionService getProcessOpinionService() {
    return this.processOpinionService;
  }
  public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
    this.processOpinionService = processOpinionService;
  }

  public String getPrcStepName() {
    return this.prcStepName;
  }

  public void setPrcStepName(String prcStepName) {
    this.prcStepName = prcStepName;
  }

  public ProcessStepFormService getProcessStepFormService() {
    return this.processStepFormService;
  }

  public void setProcessStepFormService(ProcessStepFormService processStepFormService)
  {
    this.processStepFormService = processStepFormService;
  }

  public ProcessStepFormMapService getProcessStepFormMapService() {
    return this.processStepFormMapService;
  }

  public void setProcessStepFormMapService(ProcessStepFormMapService processStepFormMapService)
  {
    this.processStepFormMapService = processStepFormMapService;
  }

  public Long getStepformId() {
    return this.stepformId;
  }
  public void setStepformId(Long stepformId) {
    this.stepformId = stepformId;
  }

  public String getProcessShortcutsButton() {
    return this.processShortcutsButton;
  }
  public void setProcessShortcutsButton(String processShortcutsButton) {
    this.processShortcutsButton = processShortcutsButton;
  }

  public String getSaveScriptEvent() {
    return this.saveScriptEvent;
  }

  public void setSaveScriptEvent(String saveScriptEvent) {
    this.saveScriptEvent = saveScriptEvent;
  }

  public String getTransScriptEvent() {
    return this.transScriptEvent;
  }

  public void setTransScriptEvent(String transScriptEvent) {
    this.transScriptEvent = transScriptEvent;
  }

  public ProcessStepTriggerService getProcessStepTriggerService() {
    return this.processStepTriggerService;
  }

  public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService)
  {
    this.processStepTriggerService = processStepTriggerService;
  }

  public Long getModelId() {
    return this.modelId;
  }

  public void setModelId(Long modelId) {
    this.modelId = modelId;
  }

  public String getModelType() {
    return this.modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  public Long getIsLog() {
    return this.isLog;
  }

  public void setIsLog(Long isLog) {
    this.isLog = isLog;
  }

  public Long getIsTalk() {
    return this.isTalk;
  }

  public Long getTaskType() {
    return this.taskType;
  }

  public void setTaskType(Long taskType) {
    this.taskType = taskType;
  }

  public Long getSubformid() {
    return this.subformid;
  }

  public void setSubformid(Long subformid) {
    this.subformid = subformid;
  }

  public Long getEngineType() {
    return this.engineType;
  }

  public void setEngineType(Long engineType) {
    this.engineType = engineType;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOper() {
    return this.oper;
  }

  public void setOper(String oper) {
    this.oper = oper;
  }

  public String getSubformkey() {
    return this.subformkey;
  }

  public void setSubformkey(String subformkey) {
    this.subformkey = subformkey;
  }

  public String getInitScriptEvent() {
    return this.initScriptEvent;
  }

  public void setInitScriptEvent(String initScriptEvent) {
    this.initScriptEvent = initScriptEvent;
  }

  public Long getFormIsModify() {
    return this.formIsModify;
  }

  public void setFormIsModify(Long formIsModify) {
    this.formIsModify = formIsModify;
  }

  public String getFormNo() {
    return this.formNo;
  }

  public void setFormNo(String formNo) {
    this.formNo = formNo;
  }

  public void setProcessRuntimeSignsService(ProcessRuntimeSignsService processRuntimeSignsService)
  {
    this.processRuntimeSignsService = processRuntimeSignsService;
  }

  public void setParams(Map params) {
    this.params = params;
  }
}