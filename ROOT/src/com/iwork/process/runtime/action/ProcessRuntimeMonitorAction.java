package com.iwork.process.runtime.action;

import com.iwork.core.constant.SysConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.eaglesearch.model.IndexFormDataModel;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.runtime.model.ActiveProcessModel;
import com.iwork.process.runtime.model.MonitorModel;
import com.iwork.process.runtime.model.ReceiveModel;
import com.iwork.process.runtime.service.ProcessRuntimeMonitorService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.opensymphony.xwork2.ActionSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;

public class ProcessRuntimeMonitorAction extends ActionSupport
{
  private ProcessRuntimeMonitorService processRuntimeMonitorService;
  private ProcessOpinionService processOpinionService;
  private ProcessStepMapService processStepMapService;
  private String opinionList;
  private String ccList;
  private String offlineList;
  private String actDefId;
  private String actStepDefId;
  private String taskId;
  private String action;
  private String currentUser;
  private Long prcDefId;
  private Long instanceId;
  private List<LinkedHashMap> targetList;
  private Long excutionId;
  private static final long serialVersionUID = 1L;
  private String historyList;
  private String html;
  private String searchkey;
  private String content;
  private String hotArea;
  private List<IndexFormDataModel> searchlist;
  private Long isShowLog;
  private Long isOffLine;
  private Long isCC;
  private Long isSigns;
  private String type;
  private String owner;
  private String targetUser;
  private String taskTitle;
  private MonitorModel model;
  private String tasklist;
  private String currentStepId;
  private List<ReceiveModel> receiveList;
  private ActiveProcessModel apm;
  private Long orgroleid;

  public Long getOrgroleid() {
	return orgroleid;
}

public void setOrgroleid(Long orgroleid) {
	this.orgroleid = orgroleid;
}

public String index()
  {
    this.model = this.processRuntimeMonitorService.getRuntimeProcessParam();
    return "success";
  }

  public String showReceiveList()
  {
    this.receiveList = this.processRuntimeMonitorService.showReceiveList();
    return "success";
  }

  public String reActiveIndex()
  {
    if ((this.actDefId != null) && (this.instanceId != null) && (this.excutionId != null)) {
      this.apm = this.processRuntimeMonitorService.getReActiveModel(this.actDefId, this.instanceId.toString(), this.excutionId.toString());
      this.taskTitle = this.apm.getTaskTitle();
    }
    return "success";
  }

  public void doReActive()
  {
    String msg = "";
    if ((this.actDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.actStepDefId != null) && (this.targetUser != null)) {
      msg = this.processRuntimeMonitorService.reActiveProcess(this.actDefId, this.instanceId.toString(), this.taskTitle, this.excutionId.toString(), this.actStepDefId, this.targetUser);
    }
    ResponseUtil.write(msg);
  }

  public void doReceive()
  {
    if (this.excutionId != null) {
      this.processRuntimeMonitorService.doReceive(this.excutionId);
    }
    ResponseUtil.write("success");
  }

  public String showPrcRuntime()
  {
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null)) {
      this.tasklist = this.processOpinionService.getProcessInstanceOpinionStepList(this.actDefId, this.actStepDefId, this.instanceId.longValue());
      this.currentStepId = this.processOpinionService.getProcessInstanceCurrentStepId(this.actDefId, this.actStepDefId, this.instanceId.longValue());
    }
    return "success";
  }

  public String showPrcInstanceMornitorPage()
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return "error";
	  }
	  orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.prcDefId != null) && (this.instanceId != null))
    {
      this.ccList = this.processRuntimeMonitorService.getProcessInstanceCCList(this.actDefId, this.prcDefId.longValue(), this.actStepDefId, this.instanceId.longValue());

      ProcessStepMap model = this.processStepMapService.getProcessDefMapModel(this.prcDefId, this.actDefId, this.actStepDefId);
      if (model != null) {
        this.isShowLog = model.getIsShowLog();
        this.isOffLine = model.getIsOffline();
        this.isCC = model.getIsCc();
        if ((model.getIsSigns() != null) && (model.getIsSigns().equals(SysConst.on)))
          this.isSigns = SysConst.on;
        else if (model.getStepType().equals(new Long(2L)))
          this.isSigns = SysConst.on;
        else {
          this.isSigns = SysConst.off;
        }
      }
    }
    return "success";
  }

  public String showOpinionList()
  {
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.instanceId != null)) {
      if (this.type == null) this.type = "grid";
      if ((this.actStepDefId != null) && ("".equals(this.actStepDefId))) {
        this.actStepDefId = null;
      }
      if (this.type.equals("timerLine")) {
        this.opinionList = this.processOpinionService.getProcessInstanceOpinionTimerLineList(this.actDefId, this.prcDefId.longValue(), this.actStepDefId, this.instanceId.longValue());
      } else {
        this.opinionList = this.processOpinionService.getProcessInstanceOpinionList(false, this.actDefId, this.actStepDefId, this.instanceId.longValue());
        return "grid";
      }
    }

    return "success";
  }

  public String showProcessingLog()
  {
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null)) {
      String history = this.processRuntimeMonitorService.showProcessingLog(this.actDefId, this.actStepDefId, this.instanceId.toString());
      ResponseUtil.write(history);
    }
    return null;
  }

  public String showPrcRuntimeImg()
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
    if (this.actDefId != null) {
      HttpServletResponse response = null;
      ServletOutputStream out = null;
      InputStream in = null;
      byte[] bytes = null;
      try {
        response = ServletActionContext.getResponse();

        response.setContentType("image/jpeg");

        out = response.getOutputStream();

        in = this.processRuntimeMonitorService.getProcessMonitorPage(this.actDefId, this.instanceId.toString());
        if (in != null) {
          bytes = new byte[1024];
          while (-1 != in.read(bytes)) {
            out.write(bytes);
          }
        }

        out.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }  finally {
        if (in != null) {
          try {
            in.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (out != null) try {
            out.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        bytes = null;
      }
    }
    return null;
  }

  public String eaglesSearchRmMonitor()
  {
    return "success";
  }

  public String doSearchMornitorList()
  {
    this.searchlist = this.processRuntimeMonitorService.dosearchList(this.owner, this.targetUser, this.searchkey);

    return "success";
  }

  public String forwordPage()
  {
    if (this.taskId != null) {
      Task task = (Task)this.processRuntimeMonitorService.getTaskService().createTaskQuery().taskId(this.taskId).singleResult();
      if (task != null) {
        this.actDefId = task.getProcessDefinitionId();
        this.instanceId = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
        this.excutionId = Long.valueOf(Long.parseLong(task.getExecutionId()));
        this.actStepDefId = task.getTaskDefinitionKey();
        this.action = "管理员移交";
        this.currentUser = task.getAssignee();
      }
    }
    return "success";
  }

  public String jumpPage()
  {
    if (this.taskId != null) {
      Task task = (Task)this.processRuntimeMonitorService.getTaskService().createTaskQuery().taskId(this.taskId).singleResult();
      if (task != null)
      {
        this.actDefId = task.getProcessDefinitionId();
        this.instanceId = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
        this.excutionId = Long.valueOf(Long.parseLong(task.getExecutionId()));
        this.actStepDefId = task.getTaskDefinitionKey();
        this.targetList = this.processRuntimeMonitorService.getProcessJumpList(this.actDefId, this.actStepDefId);
        this.action = "管理员跳转";
        this.currentUser = task.getAssignee();
      }
    }
    return "success";
  }

  public void instanceRemove()
  {
    boolean flag = false;
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    if ((uc != null) && (uc._userModel.getUsertype() != null) && (uc._userModel.getUsertype().equals(new Long(1L))))
    {
      if (this.taskId != null) {
        flag = this.processRuntimeMonitorService.delProcessInstance(this.taskId);
      }
      if (flag)
        ResponseUtil.write("success");
      else
        ResponseUtil.write("error");
    }
    else {
      ResponseUtil.write("nopurview");
    }
  }

  public ProcessRuntimeMonitorService getProcessRuntimeMonitorService() { return this.processRuntimeMonitorService; }


  public void setProcessRuntimeMonitorService(ProcessRuntimeMonitorService processRuntimeMonitorService)
  {
    this.processRuntimeMonitorService = processRuntimeMonitorService;
  }
  public String getActDefId() {
    return this.actDefId;
  }
  public void setActDefId(String actDefId) {
    this.actDefId = actDefId;
  }
  public String getActStepDefId() {
    return this.actStepDefId;
  }
  public void setActStepDefId(String actStepDefId) {
    this.actStepDefId = actStepDefId;
  }
  public String getTaskId() {
    return this.taskId;
  }
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
  public Long getPrcDefId() {
    return this.prcDefId;
  }
  public void setPrcDefId(Long prcDefId) {
    this.prcDefId = prcDefId;
  }
  public Long getInstanceId() {
    return this.instanceId;
  }
  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }
  public String getHtml() {
    return this.html;
  }
  public void setHtml(String html) {
    this.html = html;
  }
  public ProcessOpinionService getProcessOpinionService() {
    return this.processOpinionService;
  }
  public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
    this.processOpinionService = processOpinionService;
  }
  public String getOpinionList() {
    return this.opinionList;
  }
  public void setOpinionList(String opinionList) {
    this.opinionList = opinionList;
  }
  public String getHotArea() {
    return this.hotArea;
  }
  public void setHotArea(String hotArea) {
    this.hotArea = hotArea;
  }

  public String getHistoryList() {
    return this.historyList;
  }

  public void setHistoryList(String historyList) {
    this.historyList = historyList;
  }

  public String getOfflineList() {
    return this.offlineList;
  }

  public void setOfflineList(String offlineList) {
    this.offlineList = offlineList;
  }

  public Long getExcutionId() {
    return this.excutionId;
  }

  public void setExcutionId(Long excutionId) {
    this.excutionId = excutionId;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSearchkey() {
    return this.searchkey;
  }

  public void setSearchkey(String searchkey) {
    this.searchkey = searchkey;
  }

  public Long getIsShowLog() {
    return this.isShowLog;
  }

  public Long getIsOffLine() {
    return this.isOffLine;
  }

  public void setProcessStepMapService(ProcessStepMapService processStepMapService) {
    this.processStepMapService = processStepMapService;
  }

  public List<IndexFormDataModel> getSearchlist() {
    return this.searchlist;
  }

  public String getAction() {
    return this.action;
  }

  public String getCurrentUser() {
    return this.currentUser;
  }

  public void setCurrentUser(String currentUser) {
    this.currentUser = currentUser;
  }

  public List getTargetList() {
    return this.targetList;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setTargetUser(String targetUser) {
    this.targetUser = targetUser;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getCcList() {
    return this.ccList;
  }

  public Long getIsCC() {
    return this.isCC;
  }

  public Long getIsSigns() {
    return this.isSigns;
  }

  public void setIsSigns(Long isSigns) {
    this.isSigns = isSigns;
  }
  public MonitorModel getModel() {
    return this.model;
  }
  public void setModel(MonitorModel model) {
    this.model = model;
  }

  public String getTasklist() {
    return this.tasklist;
  }

  public void setTasklist(String tasklist) {
    this.tasklist = tasklist;
  }

  public String getCurrentStepId() {
    return this.currentStepId;
  }

  public void setCurrentStepId(String currentStepId) {
    this.currentStepId = currentStepId;
  }
  public List<ReceiveModel> getReceiveList() {
    return this.receiveList;
  }
  public ActiveProcessModel getApm() {
    return this.apm;
  }

  public String getTaskTitle() {
    return this.taskTitle;
  }

  public void setTaskTitle(String taskTitle) {
    this.taskTitle = taskTitle;
  }
}