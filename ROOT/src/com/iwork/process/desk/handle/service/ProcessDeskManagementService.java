package com.iwork.process.desk.handle.service;

import com.iwork.app.login.control.LoginContext;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.constant.SysConst;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.constant.UserTypeConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.interceptor.EaglesSearchInterface;
import com.iwork.process.definition.deployment.dao.ProcessDeploymentDAO;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.flow.model.ProcessDefMap;
import com.iwork.process.definition.flow.model.ProcessDefTrigger;
import com.iwork.process.definition.flow.service.ProcessDefMapService;
import com.iwork.process.definition.flow.service.ProcessDefTriggerService;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.desk.constant.ProcessDeskManagementConstant;
import com.iwork.process.managementcenter.util.PageBean;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.dao.ProcessRuntimeCcDAO;
import com.iwork.process.runtime.model.ProcessRuCc;
import com.iwork.process.runtime.pvm.impl.task.PvmProcessTaskEngine;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.iwork.process.runtime.service.ProcessRuntimeSendService;
import com.iwork.process.runtime.util.ProcessRemindUtil;
import com.iwork.process.runtime.util.ProcessTaskUtil;
import com.iwork.process.tools.processopinion.dao.ProcessOpinionDAO;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessDeskManagementService
{
  private static final Log log = LogFactory.getLog(ProcessDeskManagementService.class);
  private TaskService taskService;
  private HistoryService historyService;
  private RuntimeService runtimeService;
  private ProcessEngine processEngine;
  private ProcessRuntimeCcDAO processRuntimeCcDAO;
  private ProcessDeploymentService processDeploymentService;
  private ProcessRuntimeExcuteService processRuntimeExcuteService;
  private ProcessRuntimeOperateService processRuntimeOperateService;
  private ProcessRuntimeSendService processRuntimeSendService;
  private ProcessOpinionService processOpinionService;
  private ProcessDefTriggerService processDefTriggerService;
  private ProcessOpinionDAO processOpinionDAO;
  private ProcessRemindUtil processRemindUtil;
  private final int perTodoPage = 10;
  private final int perTaskPage = 10;
  private final int perHistPage = 20;

  public int getProcessTaskCount(String userId)
  {
    int count = 0;
    List CandidateUserlist = this.taskService.createTaskQuery().taskCandidateUser(userId).list();
    List list = this.taskService.createTaskQuery().taskAssignee(userId).list();
    if (CandidateUserlist != null) {
      count += CandidateUserlist.size();
    }
    if (list != null) {
      count += list.size();
    }
    return count;
  }

  public String getProcessingTaskJson(Page page, String taskOwner_query, String taskName_query)
  {
    if (taskName_query != null) {
      try {
        taskName_query = URLDecoder.decode(taskName_query, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    String userid = UserContextUtil.getInstance().getCurrentUserId();
    int totalRecord = 0;
    int totalNum = 0;

    HistoricTaskInstanceQuery taskQuery_Processing = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userid).finished();

    if ((taskOwner_query != null) && (!taskOwner_query.equals(""))) {
      taskOwner_query = taskOwner_query.trim();
      taskQuery_Processing = taskQuery_Processing.taskOwner(taskOwner_query);
    }

    if ((taskName_query != null) && (!taskName_query.equals(""))) {
      taskQuery_Processing = taskQuery_Processing.taskDescriptionLike("%" + taskName_query + "%");
    }

    List histTaskList = new ArrayList();
    if (page.getOrderBy().equals("2")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByTaskOwner().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByTaskOwner().desc()).list();
    }
    else if (page.getOrderBy().equals("3")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByTaskName().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByTaskName().desc()).list();
    }
    else if (page.getOrderBy().equals("4")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByHistoricActivityInstanceStartTime().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByHistoricActivityInstanceStartTime().desc()).list();
    }
    else {
      histTaskList = ((HistoricTaskInstanceQuery)taskQuery_Processing.orderByHistoricActivityInstanceStartTime().desc()).list();
    }

    Map historicTaskInstanceMap = new HashMap();
    List newList = new ArrayList();
    for (int i = 0; i < histTaskList.size(); i++) {
      HistoricTaskInstance task = (HistoricTaskInstance)histTaskList.get(i);
      if (!historicTaskInstanceMap.containsKey(task.getProcessInstanceId()))
      {
        historicTaskInstanceMap.put(task.getProcessInstanceId(), task);
        newList.add(task);
      }
    }
    histTaskList = newList;

    totalRecord = histTaskList.size();
    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());
    totalNum = b1.divide(b2, 0, 0).intValue();
    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
    int endRow = startRow + page.getPageSize();
    if (totalNum == page.getCurPageNo()) {
      endRow = startRow + histTaskList.size() % page.getPageSize();
      if (histTaskList.size() % page.getPageSize() == 0) {
        endRow = startRow + page.getPageSize();
      }
    }
    int num = startRow;
    if (endRow <= histTaskList.size())
    {
      List<HistoricTaskInstance> histTaskList_includePage = histTaskList.subList(startRow, endRow);
      for (HistoricTaskInstance task : histTaskList_includePage) {
        num++;
        Map item = new HashMap();
        item.put("id", Integer.valueOf(num));

        String actDefId = task.getProcessDefinitionId();
        if ((actDefId != null) && (!actDefId.equals("")))
        {
          ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
          if (model == null) continue;
          Long prcDefId = model.getId();
          String excutionId = task.getExecutionId();
          if ((excutionId != null) && (Long.parseLong(excutionId) != 0L)) {
            ExecutionEntity ee = this.processRuntimeExcuteService.getProcessStepId(Long.valueOf(Long.parseLong(excutionId)));
            if (ee == null) continue;
            String actStepDefId = ee.getActivityId();
            item.put("OPERATE", "<a href='javascript:openMonitorPage(\"" + actDefId + "\",\"" + actStepDefId + "\",\"" + prcDefId + "\",\"" + task.getId() + "\",\"" + task.getProcessInstanceId() + "\")'>跟踪</a>");
          }

        }

        if (task.getOwner() != null) {
          String owner = task.getAssignee();
          UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
          item.put("OWNER", ownerContext._userModel.getUsername());
        }
        else {
          item.put("OWNER", "");
        }
        if (task.getDescription() != null)
          item.put("TITLE", task.getDescription());
        else {
          item.put("TITLE", "未知标题");
        }

        item.put("NAME", task.getName());

        if (task.getStartTime() != null)
          item.put("STARTTIME", UtilDate.datetimeFormat(task.getStartTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("STARTTIME", "");
        }

        if (task.getEndTime() != null)
          item.put("ENDTIME", UtilDate.datetimeFormat(task.getEndTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("ENDTIME", "");
        }

        if (task.getDurationInMillis() != null)
          item.put("DURTIME", task.getDurationInMillis());
        else {
          item.put("DURTIME", "");
        }

        String instaceId = task.getProcessInstanceId();
        List<Task> current_tasklist = this.taskService.createTaskQuery().processDefinitionId(task.getProcessDefinitionId()).processInstanceId(instaceId).list();
        if ((current_tasklist != null) && (current_tasklist.size() > 0)) {
          StringBuffer step = new StringBuffer();
          StringBuffer user = new StringBuffer();
          for (Task item_task : current_tasklist) {
            String username = UserContextUtil.getInstance().getUserName(item_task.getAssignee());
            user.append(username).append("<br>");
            step.append(item_task.getName()).append("<br>");
          }
          item.put("CURR_USER", user.toString());
          item.put("CURR_STEP", step.toString());
        } else {
          item.put("CURR_USER", "");
          item.put("CURR_STEP", "");
        }

        item.put("actDefId", task.getProcessDefinitionId());
        item.put("instanceId", task.getProcessInstanceId());
        item.put("excutionId", task.getExecutionId());
        item.put("taskId", task.getId());
        items.add(item);
      }
    }
    total.put("total", Integer.valueOf(histTaskList.size()));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public String getTodoListJson(Page page, String taskOwner_query, String taskName_query)
  {
    if ((taskName_query != null) && (!taskName_query.equals(""))) {
      try {
        taskName_query = URLDecoder.decode(taskName_query, "UTF-8");
        page.setCurPageNo(1);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int totalRecord = 0;
    int totalNum = 0;
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

    TaskQuery taskQuery_candidate = (TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(uc.get_userModel().getUserid()).orderByTaskPriority().desc();

    TaskQuery taskQuery_todo = (TaskQuery)this.taskService.createTaskQuery().taskAssignee(uc.get_userModel().getUserid()).orderByTaskPriority().desc();

    if ((taskOwner_query != null) && (!taskOwner_query.equals(""))) {
      page.setCurPageNo(1);
      taskOwner_query = taskOwner_query.trim();
      taskQuery_candidate = taskQuery_candidate.taskOwner(taskOwner_query);
      taskQuery_todo = taskQuery_todo.taskOwner(taskOwner_query);
    }

    if ((taskName_query != null) && (!taskName_query.equals(""))) {
      taskQuery_candidate = taskQuery_candidate.taskDescriptionLike("%" + taskName_query + "%");
      taskQuery_todo = taskQuery_todo.taskDescriptionLike("%" + taskName_query + "%");
    }

    List CandidateUserlist = new ArrayList();
    List toDoList = new ArrayList();
    if ("3".equals(page.getOrderBy())) {
      if (page.getOrder().equals("asc")) {
        CandidateUserlist = ((TaskQuery)taskQuery_candidate.orderByTaskName().asc()).list();
        toDoList = ((TaskQuery)taskQuery_todo.orderByTaskName().asc()).list();
      } else {
        CandidateUserlist = ((TaskQuery)taskQuery_candidate.orderByTaskName().desc()).list();
        toDoList = ((TaskQuery)taskQuery_todo.orderByTaskName().desc()).list();
      }
    } else if ("4".equals(page.getOrderBy())) {
      if (page.getOrder().equals("asc")) {
        CandidateUserlist = ((TaskQuery)taskQuery_candidate.orderByTaskCreateTime().asc()).list();
        toDoList = ((TaskQuery)taskQuery_todo.orderByTaskCreateTime().asc()).list();
      } else {
        CandidateUserlist = ((TaskQuery)taskQuery_candidate.orderByTaskCreateTime().desc()).list();
        toDoList = ((TaskQuery)taskQuery_todo.orderByTaskCreateTime().desc()).list();
      }
    } else {
      CandidateUserlist = ((TaskQuery)taskQuery_candidate.orderByTaskCreateTime().desc()).list();
      toDoList = ((TaskQuery)taskQuery_todo.orderByTaskCreateTime().desc()).list();
    }

    totalRecord = CandidateUserlist.size() + toDoList.size();
    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());

    totalNum = b1.divide(b2, 0, 0).intValue();

    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);

    int num = startRow;

    int endRow = startRow + page.getPageSize();

    int candidateUserTotalRecord = CandidateUserlist.size();
    BigDecimal cb1 = new BigDecimal(candidateUserTotalRecord);

    int candidateTotalNum = cb1.divide(b2, 0, 0).intValue();

    if (candidateTotalNum == page.getCurPageNo()) {
      if (CandidateUserlist.size() % page.getPageSize() == 0)
        endRow = startRow + page.getPageSize();
      else {
        endRow = startRow + CandidateUserlist.size() % page.getPageSize();
      }
    }

    if (endRow <= CandidateUserlist.size()) {
      List<Task> CandidateUserlist_IncludePage = CandidateUserlist.subList(startRow, endRow);
      for (Task task : CandidateUserlist_IncludePage) {
        num++;
        Map item = new HashMap();
        item.put("id", Integer.valueOf(num));

        boolean flag = this.processRuntimeExcuteService.checkTaskReadStatus(task);
        String b = "";
        String b_close = "";
        if (flag) {
          b = "<b>";
          b_close = "</b>";
        }

        if (task.getOwner() != null) {
          String owner = task.getOwner();
          UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
          item.put("OWNER", b + ownerContext._userModel.getUsername() + b_close);
        }
        else {
          item.put("OWNER", "");
        }
        if (task.getDescription() != null) {
          if (task.getName() == null) {
            task.setName("");
          }
          item.put("TITLE", b + "(" + task.getName() + ")" + task.getDescription() + b_close);
        }
        else {
          item.put("TITLE", b + "(" + task.getName() + ")" + b_close);
        }

        item.put("NAME", task.getName());

        if (task.getCreateTime() != null) {
          item.put("DATETIME", b + UtilDate.datetimeFormat(task.getCreateTime()) + b_close);
          String durtime = UtilDate.TimeDiff(Long.valueOf(task.getCreateTime().getTime()), Long.valueOf(new Date().getTime()));
          item.put("DURETIME", b + durtime + b_close);
        }
        else {
          item.put("DATETIME", "");
          item.put("DURETIME", "");
        }
        item.put("TYPE", "领取任务列表");

        item.put("OPERATE", "<a href=\"javascript:onclick=claim(" + task.getId() + ",'" + uc.get_userModel().getUserid() + "');\"><img src='iwork_img/down16.png' border='0'>领取</a>");

        item.put("actDefId", task.getProcessDefinitionId());
        item.put("instanceId", task.getProcessInstanceId());
        item.put("excutionId", task.getExecutionId());
        item.put("taskId", task.getId());
        items.add(item);
      }

    }

    if (page.getCurPageNo() < candidateTotalNum) { startRow = 0; endRow = 0;
    }
    else if (page.getCurPageNo() == candidateTotalNum) {
      startRow = 0;
      if (CandidateUserlist.size() % page.getPageSize() == 0)
        endRow = 0;
      else
        endRow = page.getPageSize() - CandidateUserlist.size() % page.getPageSize();
    }
    else {
      if (CandidateUserlist.size() % page.getPageSize() == 0)
        startRow = (page.getCurPageNo() - candidateTotalNum - 1) * page.getPageSize();
      else {
        startRow = (page.getCurPageNo() - candidateTotalNum - 1) * page.getPageSize() + (page.getPageSize() - CandidateUserlist.size() % page.getPageSize());
      }
      endRow = startRow + page.getPageSize();
    }

    if (endRow > toDoList.size()) endRow = toDoList.size();
    List<Task> toDoList_IncludePage = toDoList.subList(startRow, endRow);
    for (Task task : toDoList_IncludePage) {
      num++;
      Map item = new HashMap();

      boolean flag = this.processRuntimeExcuteService.checkTaskReadStatus(task);
      String b = "";
      String b_close = "";
      if (flag) {
        b = "<b>";
        b_close = "</b>";
      }
      item.put("id", Integer.valueOf(num));

      if (task.getOwner() != null) {
        String owner = task.getOwner();
        UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
        item.put("OWNER", b + ownerContext._userModel.getUsername() + b_close);
      }
      else {
        item.put("OWNER", "");
      }
      if (task.getDescription() != null) {
        if (task.getName() == null) {
          task.setName("");
        }
        String pr_icon = "";

        if (task.getPriority() == 80) {
          pr_icon = "<img src=\"iwork_img/logotodo.gif\" alt=\"紧急\">";
        }
        item.put("TITLE", b + pr_icon + "(" + task.getName() + ")" + task.getDescription() + b_close);
      } else {
        item.put("TITLE", b + "(" + task.getName() + ")" + b_close);
      }

      item.put("NAME", task.getName());

      if (task.getCreateTime() != null) {
        item.put("DATETIME", b + UtilDate.datetimeFormat(task.getCreateTime()) + b_close);
        String durtime = UtilDate.TimeDiff(Long.valueOf(task.getCreateTime().getTime()), Long.valueOf(new Date().getTime()));
        item.put("DURETIME", b + durtime + b_close);
      } else {
        item.put("DATETIME", "");
        item.put("DURETIME", "");
      }

      boolean isDel = PvmProcessTaskEngine.getInstance().checkTaskIsDel(task.getId(), UserContextUtil.getInstance().getCurrentUserId());
      if (isDel) {
        ProcessDefMap processDefMap = this.processRuntimeExcuteService.getProcessDefMapService().getProcessDefMapModel(task.getProcessDefinitionId());

        if (processDefMap != null) {
          if ((processDefMap.getTaskCancelType() != null) && (processDefMap.getTaskCancelType().equals("DEL")))
            item.put("OPERATE", "<a href=\"javascript:onclick=deleteTask(" + task.getId() + ");\"><img title='删除当前待办任务' src='iwork_img/delete.png' width='12' border='0'></a>");
          else
            item.put("OPERATE", "<a href=\"javascript:onclick=closeTask(" + task.getId() + ");\"><img  title='关闭当前待办任务' src='iwork_img/delete2.gif' border='0'></a>");
        }
        else
          item.put("OPERATE", "<a href=\"javascript:onclick=deleteTask(" + task.getId() + ");\"><img title='删除当前待办任务' src='iwork_img/delete.png' width='12' border='0'></a>");
      }
      else
      {
        ProcessStepMap processStepMap = this.processRuntimeExcuteService.getProcessStepMapService().getProcessDefMapModel(null, task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        if ((processStepMap != null) && (processStepMap.getIsWait() != null) && (processStepMap.getIsWait().equals(SysConst.on)))
        {
          Long isWait = null;
          Object key = this.taskService.getVariable(task.getId(), "PROCESS_TASK_ACTION_STATUS_KEY");
          if (key != null) {
            if ((key instanceof Long))
              isWait = (Long)key;
            else if ((key instanceof Integer))
              isWait = Long.valueOf(((Integer)key).longValue());
            else {
              isWait = ProcessTaskConstant.PROCESS_TASK_ACTION_ACTIVE;
            }
          }
          if (isWait != null) {
            if (isWait.equals(new Long(0L)))
              item.put("OPERATE", "<a href=\"javascript:onclick=activeTask(" + task.getId() + ");\"><img title='[任务已休眠]，点击激活当前任务' src='iwork_img/control_pause.png' border='0'></a>");
            else if (isWait.equals(new Long(1L)))
              item.put("OPERATE", "<a href=\"javascript:onclick=sleepTask(" + task.getId() + ");\"><img title='点击执行修休眠(执行睡眠操作后，将不再对任务进行提醒及催办)' src='iwork_img/pin.png' border='0'></a>");
          }
          else {
            item.put("OPERATE", "");
          }
        }
      }
      item.put("TYPE", "待办任务列表");

      item.put("actDefId", task.getProcessDefinitionId());
      item.put("instanceId", task.getProcessInstanceId());
      item.put("excutionId", task.getExecutionId());
      item.put("taskId", task.getId());
      items.add(item);
    }
    total.put("total", Integer.valueOf(CandidateUserlist.size() + toDoList.size()));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public String getNoticeListJson(Page page, String actDefkey, String taskOwner_query, String taskName_query)
  {
    if (taskName_query != null) {
      try {
        taskName_query = URLDecoder.decode(taskName_query, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    int totalRecord = 0;
    int totalNum = 0;
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

    String actDefId = null;
    if (actDefkey != null) {
      List<ProcessDefinition> list = this.processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(actDefkey).list();
      for (ProcessDefinition pd : list) {
        ProcessDefDeploy deploy = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(pd.getId());
        if ((deploy != null) && (deploy.getStatus() != null) && 
          (deploy.getStatus().equals(SysConst.on))) {
          actDefId = pd.getId();
          break;
        }

      }

    }

    totalRecord = this.processRuntimeCcDAO.getUserCCAllListSize(actDefId, uc.get_userModel().getUserid(), taskName_query);
    String orderByStr = page.getOrderBy();
    if (orderByStr.equals("2")) orderByStr = "ccUser";
    else if (orderByStr.equals("3")) orderByStr = "title";
    else if (orderByStr.equals("4")) orderByStr = "ccTime";
    else if (orderByStr.equals("5")) orderByStr = "readtime"; else {
      orderByStr = "ccTime";
    }

    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());
    totalNum = b1.divide(b2, 0, 0).intValue();
    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
    int num = startRow;

    List<ProcessRuCc> list_includePage = this.processRuntimeCcDAO.getUserCCList(actDefId, uc.get_userModel().getUserid(), taskOwner_query, taskName_query, page.getPageSize(), startRow, orderByStr, page.getOrder());
    for (ProcessRuCc task : list_includePage) {
      boolean isread = false;
      num++;
      Map item = new HashMap();
      item.put("id", Integer.valueOf(num));

      if (task.getReadtime() == null) {
        item.put("OPERATE", "未读");
        isread = false;
      } else {
        item.put("OPERATE", UtilDate.datetimeFormat(task.getReadtime(), "yyyy-MM-dd HH:mm"));
        isread = true;
      }

      if (task.getCcUser() != null) {
        String owner = task.getCcUser();
        UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
        if (ownerContext != null)
          item.put("OWNER", ProcessTaskUtil.getIsReadStr(isread, ownerContext._userModel.getUsername()));
        else
          item.put("OWNER", owner);
      }
      else
      {
        item.put("OWNER", "");
      }
      if (task.getTitle() != null)
        item.put("TITLE", ProcessTaskUtil.getIsReadStr(isread, task.getTitle()));
      else {
        item.put("TITLE", "");
      }

      item.put("NAME", task.getActStepId());

      if (task.getCcTime() != null)
        item.put("DATETIME", ProcessTaskUtil.getIsReadStr(isread, UtilDate.datetimeFormat(task.getCcTime(), "yyyy-MM-dd HH:mm")));
      else {
        item.put("DATETIME", "");
      }

      item.put("actDefId", task.getActDefId());
      item.put("instanceId", task.getInstanceid());
      item.put("excutionId", task.getExcutionid());
      item.put("taskId", task.getTaskid());
      item.put("dataid", task.getId());
      items.add(item);
    }

    total.put("total", Integer.valueOf(totalRecord));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public int getNoticeListJsonSize()
  {
    int size = this.processRuntimeCcDAO.getUserCCListCount(UserContextUtil.getInstance().getCurrentUserId());
    return size;
  }

  public boolean claimTask(Long taskId, String userId)
  {
    boolean flag = true;
    try {
      this.taskService.claim(taskId.toString(), userId);
    } catch (Exception e) {
      flag = false;
    }
    return flag;
  }

  public boolean waitTask(String taskId, String userId, boolean iswait, String resion)
  {
    boolean flag = true;
    try {
      Task task = (Task)this.taskService.createTaskQuery().taskAssignee(userId).taskId(taskId).singleResult();
      if (task != null) {
        if (iswait) {
          this.taskService.setVariable(taskId, "PROCESS_TASK_ACTION_STATUS_KEY", ProcessTaskConstant.PROCESS_TASK_ACTION_SLEEP);
          if (resion != null) {
            this.taskService.setVariable(taskId, "PROCESS_TASK_ACTION_STATUS_RESION", resion);
          }

          String owner = task.getOwner();
          if (owner != null) {
            UserContext currentContext = UserContextUtil.getInstance().getCurrentUserContext();
            UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);

            if (ownerContext != null)
            {
              if (userId.equals(owner)) {
                String formno = "";
                Object tmp = this.taskService.getVariable(taskId, "PROCESS_TASK_FORMNO");
                if (tmp != null) {
                  formno = tmp.toString();
                }
                StringBuffer title = new StringBuffer();
                StringBuffer content = new StringBuffer();
                title.append("[").append(currentContext.get_userModel().getUsername()).append("]将您发起的单据号为【").append(formno).append("】流程单据置为休眠状态");
                content.append("<b>[操 作 人]</b>").append(currentContext.get_userModel().getUsername()).append("<br/>");
                content.append("<b>[休眠节点]</b>").append(task.getName()).append("<br/>");
                content.append("<b>[休眠原因]</b><br/>").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(resion);
                StringBuffer url = new StringBuffer();
                url.append("loadProcessFormPage.action?actDefId=").append(task.getProcessDefinitionId()).append("&instanceId=").append(task.getProcessInstanceId()).append("&excutionId=").append(task.getExecutionId()).append("&taskId=").append(task.getId());
                MessageAPI.getInstance().sendSysMsg(ownerContext.get_userModel().getUserid(), title.toString(), content.toString(), url.toString());
              }
            }
          }
        }
        else
        {
          this.taskService.setVariable(taskId, "PROCESS_TASK_ACTION_STATUS_KEY", ProcessTaskConstant.PROCESS_TASK_ACTION_ACTIVE);
        }
        this.taskService.saveTask(task);
      }
    } catch (Exception e) {
      flag = false;
    }
    return flag;
  }

  public boolean closeTask(String taskId)
  {
    boolean flag = false;
    if (taskId != null) {
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      if (task != null)
      {
        UserContext me = UserContextUtil.getInstance().getCurrentUserContext();
        if (me == null) {
          me = UserContextUtil.getInstance().getUserContext("ADMIN");
        }

        boolean isPurview = false;

        if ((task.getOwner() != null) && (task.getOwner().equals(me.get_userModel().getUserid()))) {
          isPurview = true;
        }
        if ((task.getAssignee() != null) && (task.getAssignee().equals(me.get_userModel().getUserid()))) {
          isPurview = true;
        }
        if (me.get_userModel().getUsertype().equals(UserTypeConst.USER_TYPE_SYSTEMUSER)) {
          isPurview = true;
        }
        if ((!isPurview) || (!SecurityUtil.isSuperManager(me.get_userModel().getUserid()))) {
          return false;
        }

        boolean isTrigger = true;

        ProcessDefTrigger triggermoel = this.processDefTriggerService.getProcessTriggerModel(task.getProcessDefinitionId(), null, "beforeProgressDelete");
        if (triggermoel != null) {
          HashMap hash = new HashMap();
          hash.put("PROCESS_PARAMETER_ACTDEFID", task.getProcessDefinitionId());
          hash.put("PROCESS_PARAMETER_INSTANCEID", task.getProcessInstanceId());
          hash.put("PROCESS_PARAMETER_TASKID", taskId);
          hash.put("PROCESS_PARAMETER_EXECUTEID", task.getExecutionId());
          hash.put("REBACK_TYPE", "CLOSE");

          Object obj = this.runtimeService.getVariable(task.getProcessInstanceId(), "PROCESS_TASK_FORMNO");
          if (obj != null) {
            hash.put("PROCESS_TASK_FORMNO", obj.toString());
          }

          isTrigger = TriggerAPI.getInstance().excuteEvent(triggermoel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", hash, Long.valueOf(Long.parseLong(task.getProcessInstanceId())));
        }

        if (isTrigger) {
          this.taskService.deleteTask(taskId);
          flag = true;
          this.processOpinionService.sendAction("任务终止", task.getProcessDefinitionId(), null, task.getTaskDefinitionKey(), Long.valueOf(Long.parseLong(task.getProcessInstanceId())), taskId, Long.valueOf(Long.parseLong(task.getExecutionId())), "当前任务已被发起人作废");
        }
      }

    }

    return flag;
  }

  public boolean deleteTask(String taskId, String userId)
  {
    boolean flag = false;
    try {
     // Task task = (Task)this.taskService.createTaskQuery().taskAssignee(userId).taskId(taskId).singleResult();
      Task task= ProcessAPI.getInstance().getTask(taskId);
      if (task != null) {
        boolean isTrigger = true;
        boolean isPurview = false;
        UserContext me = UserContextUtil.getInstance().getCurrentUserContext();
        if ((task.getOwner() != null) && (task.getOwner().equals(userId))) {
          isPurview = true;
        }
        if ((task.getAssignee() != null) && (task.getAssignee().equals(me.get_userModel().getUserid()))) {
          isPurview = true;
        }
        if (me.get_userModel().getUserstate().equals(UserTypeConst.USER_TYPE_SYSTEMUSER)) {
          isPurview = true;
        }
        if (isPurview)
        {
          ProcessDefTrigger triggermoel = this.processRuntimeExcuteService.getProcessDefTriggerService().getProcessTriggerModel(task.getProcessDefinitionId(), null, "beforeProgressDelete");
          if (triggermoel != null) {
            HashMap hash = new HashMap();
            hash.put("PROCESS_PARAMETER_ACTDEFID", task.getProcessDefinitionId());
            hash.put("PROCESS_PARAMETER_INSTANCEID", task.getProcessInstanceId());
            hash.put("REBACK_TYPE", "DEL");
            isTrigger = TriggerAPI.getInstance().excuteEvent(triggermoel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", hash, Long.valueOf(Long.parseLong(task.getProcessInstanceId())));
          }

          if (isTrigger)
          {
            Long instanceId = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
            String result = this.processRuntimeExcuteService.removeInstanceData(taskId, instanceId);
            if (result.equals("success"))
            {
              this.historyService.deleteHistoricTaskInstance(taskId);

              this.taskService.deleteTask(taskId, true);

              DBUtil.executeUpdate("delete from process_hi_actinst t where t.proc_inst_id_='" + instanceId + "'");
              DBUtil.executeUpdate("delete from process_hi_procinst t where t.proc_inst_id_='" + instanceId + "'");
              DBUtil.executeUpdate("delete from process_hi_taskinst t where t.proc_inst_id_='" + instanceId + "'");

              EaglesSearchFactory.getEaglesSearcherImpl("FORMINDEX").delDocuemnt(taskId);
              flag = true;
            }
          }
        }
        else if (task.getOwner() != null) {
          String owner = task.getOwner();
          if (!owner.equals("")) {
            String ownerInfo = UserContextUtil.getInstance().getFullUserAddress(owner);
            StringBuffer msg = new StringBuffer();
            msg.append("当前任务的所有人为【").append(ownerInfo).append("】当前用户不允许删除。请转发至【").append(ownerInfo).append("】执行删除操作或联系管理员删除");
            MessageQueueUtil.getInstance().putAlertMsg(msg.toString());
          }
        }
      }
    }
    catch (Exception e) {
      flag = false;
    }
    return flag;
  }

  private List<Task> todoList(int totalTodoPage, int currTodoPage)
  {
    List<String> userGroupList = new ArrayList();
    List userGroupTaskList = new ArrayList();
    for (String group : userGroupList) {
      userGroupTaskList.addAll(((TaskQuery)this.taskService.createTaskQuery().taskCandidateGroup(group).orderByTaskCreateTime().desc()).list());
    }
    List userSelfTaskList = new ArrayList();
    LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get("LOGINCONTEXT");
    userSelfTaskList.addAll(((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(lc.getUid()).orderByTaskCreateTime().desc()).list());

    List userTaskList = new ArrayList();
    userTaskList.addAll(userGroupTaskList);
    userTaskList.addAll(userSelfTaskList);

    int totalRows = userTaskList.size();

    totalTodoPage = PageBean.countTotalPage(10, totalRows);
    if (currTodoPage == 0) {
      currTodoPage = 1;
    }
    int offset = PageBean.countOffset(10, currTodoPage);
    int length = 10;
    int currentPage = PageBean.countCurrentPage(currTodoPage);
    if (totalRows <= length) {
      length = totalRows;
    }

    List list = userTaskList.subList(offset, offset + length);
    return list;
  }

  private List<Task> taskList()
  {
    List userTaskList = new ArrayList();
    LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get("LOGINCONTEXT");

    userTaskList = ((TaskQuery)this.taskService.createTaskQuery().taskAssignee(lc.getUid()).orderByTaskCreateTime().desc()).list();
    return userTaskList;
  }

  public PageBean histList(int totalHistPage, int currHistPage)
  {
    List histTaskList = new ArrayList();
    LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get("LOGINCONTEXT");

    histTaskList = ((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskAssignee(lc.getUid()).orderByProcessDefinitionId().orderByHistoricTaskInstanceEndTime().desc()).list();

    int totalRows = histTaskList.size();

    totalHistPage = PageBean.countTotalPage(20, totalRows);
    if (currHistPage == 0) {
      currHistPage = 1;
    }
    int offset = PageBean.countOffset(20, currHistPage);
    int length = 20;
    int currentPage = PageBean.countCurrentPage(currHistPage);
    if (totalRows <= length) {
      length = totalRows;
    }

    int nextRows = offset + length;
    if (nextRows >= totalRows) {
      nextRows = totalRows;
    }
    List list = histTaskList.subList(offset, nextRows);

    PageBean pageBean = new PageBean();
    pageBean.setPageSize(20);
    pageBean.setCurrentPage(currentPage);
    pageBean.setTotalRows(totalRows);
    pageBean.setTotalPages(totalHistPage);
    pageBean.setList(list);
    pageBean.init();
    return pageBean;
  }

  public String getHistoryListJson(Page page, String actDefkey, String taskOwner_query, String taskName_query)
  {
    if (taskName_query != null) {
      try {
        taskName_query = URLDecoder.decode(taskName_query, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    int totalRecord = 0;
    int totalNum = 0;

    HistoricTaskInstanceQuery taskQuery_history = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(uc.get_userModel().getUserid()).orderByHistoricTaskInstanceEndTime();
    if ((actDefkey != null) && (!"".equals(actDefkey))) {
      taskQuery_history.processDefinitionKey(actDefkey);
    }

    if ((taskOwner_query != null) && (!taskOwner_query.equals(""))) {
      taskOwner_query = taskOwner_query.trim();
      taskQuery_history = taskQuery_history.taskOwner(taskOwner_query);
    }

    if ((taskName_query != null) && (!taskName_query.equals(""))) {
      taskQuery_history = taskQuery_history.taskDescriptionLike("%" + taskName_query + "%");
    }

    List histTaskList = new ArrayList();
    if (page.getOrderBy().equals("2")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskOwner().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskOwner().desc()).list();
    }
    else if (page.getOrderBy().equals("3")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskName().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskName().desc()).list();
    }
    else if (page.getOrderBy().equals("4")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricActivityInstanceStartTime().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricActivityInstanceStartTime().desc()).list();
    }
    else if (page.getOrderBy().equals("5")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceEndTime().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceEndTime().desc()).list();
    }
    else if (page.getOrderBy().equals("6")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceDuration().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceDuration().desc()).list();
    }
    else {
      histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceEndTime().desc()).list();
    }

    totalRecord = histTaskList.size();
    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());
    totalNum = b1.divide(b2, 0, 0).intValue();
    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
    int endRow = startRow + page.getPageSize();
    if (totalNum == page.getCurPageNo()) {
      endRow = startRow + histTaskList.size() % page.getPageSize();
      if (histTaskList.size() % page.getPageSize() == 0) {
        endRow = startRow + page.getPageSize();
      }
    }
    int num = startRow;

    if (endRow <= histTaskList.size()) {
      List<HistoricTaskInstance> histTaskList_includePage = histTaskList.subList(startRow, endRow);
      for (HistoricTaskInstance task : histTaskList_includePage) {
        Map item = new HashMap();

        if (task.getEndTime() != null)
          item.put("ENDTIME", UtilDate.datetimeFormat(task.getEndTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("ENDTIME", "");
        }

        if (task.getOwner() != null) {
          String owner = task.getOwner();
          UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
          if (ownerContext != null)
            item.put("OWNER", ownerContext._userModel.getUsername());
          else {
            item.put("OWNER", "");
          }
        }
        else
        {
          item.put("OWNER", "");
        }
        if (task.getDescription() != null)
          item.put("TITLE", "(" + task.getName() + ")" + task.getDescription());
        else {
          item.put("TITLE", "(" + task.getName() + ")");
        }

        item.put("NAME", task.getName());

        if (task.getStartTime() != null)
          item.put("STARTTIME", UtilDate.datetimeFormat(task.getStartTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("STARTTIME", "");
        }

        if (task.getDurationInMillis() != null)
          item.put("DURTIME", UtilDate.TimeDiff(task.getDurationInMillis()));
        else {
          item.put("DURTIME", "");
        }

        ProcessRuOpinion opinion = this.processOpinionDAO.getProData(uc.get_userModel().getUserid(), task.getProcessDefinitionId(), null, task.getTaskDefinitionKey(), Long.valueOf(Long.parseLong(task.getProcessInstanceId())), task.getId(), Long.valueOf(Long.parseLong(task.getExecutionId())));
        if (opinion != null) {
          item.put("OPINION", opinion.getContent());
          item.put("ACTION", opinion.getAction());
        }
        num++;
        item.put("id", Integer.valueOf(num));

        item.put("actDefId", task.getProcessDefinitionId());
        item.put("actStepDefId", task.getTaskDefinitionKey());
        item.put("instanceId", task.getProcessInstanceId());
        item.put("excutionId", task.getExecutionId());
        item.put("taskId", task.getId());
        items.add(item);
      }
    }
    total.put("total", Integer.valueOf(histTaskList.size()));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public String getCreatProcessJson(Page page, String taskName_query)
  {
    if (taskName_query != null) {
      try {
        taskName_query = URLDecoder.decode(taskName_query, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    int totalRecord = 0;
    int totalNum = 0;

    HistoricTaskInstanceQuery taskQuery_history = this.historyService.createHistoricTaskInstanceQuery().taskOwner(uc.get_userModel().getUserid()).taskAssignee(uc.get_userModel().getUserid());

    if ((taskName_query != null) && (!taskName_query.equals(""))) {
      taskQuery_history = taskQuery_history.taskDescriptionLike("%" + taskName_query + "%");
    }

    List histTaskList = new ArrayList();
    if (page.getOrderBy().equals("2")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskName().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByTaskName().desc()).list();
    }
    else if (page.getOrderBy().equals("3")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricActivityInstanceStartTime().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricActivityInstanceStartTime().desc()).list();
    }
    else if (page.getOrderBy().equals("4")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceEndTime().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceEndTime().desc()).list();
    }
    else if (page.getOrderBy().equals("5")) {
      if (page.getOrder().equals("asc"))
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceDuration().asc()).list();
      else
        histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricTaskInstanceDuration().desc()).list();
    }
    else {
      histTaskList = ((HistoricTaskInstanceQuery)taskQuery_history.orderByHistoricActivityInstanceStartTime().desc()).list();
    }

    totalRecord = histTaskList.size();
    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());
    totalNum = b1.divide(b2, 0, 0).intValue();
    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
    int endRow = startRow + page.getPageSize();
    if (totalNum == page.getCurPageNo()) {
      endRow = startRow + histTaskList.size() % page.getPageSize();
      if (histTaskList.size() % page.getPageSize() == 0) {
        endRow = startRow + page.getPageSize();
      }
    }
    int num = startRow;

    if (endRow <= histTaskList.size()) {
      List<HistoricTaskInstance> histTaskList_includePage = histTaskList.subList(startRow, endRow);
      List showlist = new ArrayList();
      for (HistoricTaskInstance task : histTaskList_includePage)
      {
        num++;
        Map item = new HashMap();
        item.put("id", Integer.valueOf(num));

        if (task.getDescription() != null)
          item.put("TITLE", task.getDescription());
        else {
          item.put("TITLE", "未知标题");
        }

        item.put("NAME", task.getName());

        if (task.getStartTime() != null)
          item.put("STARTTIME", UtilDate.datetimeFormat(task.getStartTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("STARTTIME", "");
        }

        if (task.getEndTime() != null)
          item.put("ENDTIME", UtilDate.datetimeFormat(task.getEndTime(), "yyyy-MM-dd HH:mm"));
        else {
          item.put("ENDTIME", "");
        }

        if (task.getDurationInMillis() != null)
          item.put("DURTIME", UtilDate.TimeDiff(task.getDurationInMillis()));
        else {
          item.put("DURTIME", "");
        }

        item.put("MONITOR", "");

        String instanceId = task.getProcessInstanceId();
        List list = this.taskService.createTaskQuery().processInstanceId(instanceId).list();
        if ((list != null) && (list.size() > 0)) {
          ProcessDefMap defMap = this.processRuntimeExcuteService.getProcessDefMapService().getProcessDefMapModel(task.getProcessDefinitionId());
          if ((defMap != null) && (defMap.getIsCreaterCancel() != null) && (defMap.getIsCreaterCancel().equals(SysConst.on)))
            item.put("OPERA", "<a href='javascript:reback(" + instanceId + ");'>撤销</a>");
          else {
            item.put("OPERA", "");
          }
          item.put("STATUS", "流转中");
        } else {
          Long isFinishList = Long.valueOf(this.historyService.createHistoricProcessInstanceQuery().finished().processDefinitionId(task.getProcessDefinitionId()).processInstanceId(task.getProcessInstanceId()).count());
          if ((isFinishList != null) && (isFinishList.longValue() > 0L))
            item.put("STATUS", "已归档");
          else {
            item.put("STATUS", "流转中");
          }

        }

        item.put("actDefId", task.getProcessDefinitionId());
        item.put("instanceId", task.getProcessInstanceId());
        item.put("excutionId", task.getExecutionId());
        item.put("taskId", task.getId());
        items.add(item);
      }

    }

    total.put("total", Integer.valueOf(histTaskList.size()));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public String getDeskTypeTab(int index)
  {
    StringBuffer html = new StringBuffer();
    String[] tablist = ProcessDeskManagementConstant.PROCESS_TASK_TYPE_LIST;
    String[] urllist = ProcessDeskManagementConstant.PROCESS_TASK_TYPE_URL;
    for (int i = 0; i < tablist.length; i++) {
      String tab = tablist[i];
      if (i == index) {
        html.append("<li ><a href=\"###\"  onClick=\"getTagsPage('").append(i).append("','").append(urllist[i]).append("');\"><span style=\"color:#f77215;background-color:#fff;font-weight:bold;\">").append(tab).append("</span></a></li>").append("\n");
      }
      else
      {
        html.append("<li><a href=\"###\"  onClick=\"getTagsPage('").append(i).append("','").append(urllist[i]).append("');\"><span>").append(tab).append("</span></a></li>").append("\n");
      }

    }

    return html.toString();
  }

  public String getTaskNoticeNum()
  {
    int taskNum = 0;
    int noticeNum = 0;
    String userId = UserContextUtil.getInstance().getCurrentUserId();
    taskNum = getProcessTaskCount(userId);
    noticeNum = this.processRuntimeCcDAO.getUserCCListCount(userId);
    StringBuffer msgJson = new StringBuffer("");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("taskNum", Integer.valueOf(taskNum));
    jsonObject.put("noticeNum", Integer.valueOf(noticeNum));
    msgJson.append(jsonObject);
    return msgJson.toString();
  }

  public Task getCurrentTask(Long instanceId)
  {
    String taskId = "";
    Task task = null;
    List list = this.taskService.createTaskQuery().processInstanceId(instanceId.toString()).list();
    if ((list != null) && (list.size() == 1)) {
      task = (Task)list.get(0);
      taskId = task.getId();
    }

    return task;
  }

  public boolean doReback(String taskId, String resion, String[] remindType)
  {
    boolean flag = false;
    if (taskId != null) {
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      if (task == null) {
        return flag;
      }
      String actDefId = task.getProcessDefinitionId();
      String instanceId = task.getProcessInstanceId();
      RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl)this.processEngine.getRepositoryService();
      ProcessDefinitionEntity pde = (ProcessDefinitionEntity)repositoryServiceImpl.getDeployedProcessDefinition(actDefId);
      List<ActivityImpl> list = pde.getActivities();
      String targetStepId = "";
      String receiveUser = UserContextUtil.getInstance().getCurrentUserId();
      List outTransitionlist;
      for (ActivityImpl activity : list)
      {
        if ((activity.getActivityBehavior() instanceof NoneStartEventActivityBehavior))
        {
          outTransitionlist = activity.getOutgoingTransitions();
          if ((outTransitionlist == null) || (outTransitionlist.size() != 1)) break;
          PvmTransition trans = (PvmTransition)outTransitionlist.get(0);
          PvmActivity targetActivity = trans.getDestination();
          targetStepId = targetActivity.getId();

          break;
        }
      }

      if ((targetStepId != null) && (!targetStepId.equals("")))
      {
        List<HistoricTaskInstance> historyTasklist = this.historyService.createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(instanceId).taskDefinitionKey(targetStepId).list();
        for (HistoricTaskInstance historyTask : historyTasklist) {
          if ((historyTask == null) || (historyTask.getOwner() == null) || (!historyTask.getOwner().equals(receiveUser))) {
            return false;
          }

        }

        boolean isTrigger = true;

        ProcessDefTrigger triggermoel = this.processDefTriggerService.getProcessTriggerModel(task.getProcessDefinitionId(), null, "processRebacBefore");
        if (triggermoel != null) {
          HashMap hash = new HashMap();
          hash.put("PROCESS_PARAMETER_ACTDEFID", task.getProcessDefinitionId());
          hash.put("PROCESS_PARAMETER_INSTANCEID", task.getProcessInstanceId());
          hash.put("PROCESS_PARAMETER_TASKID", taskId);
          hash.put("PROCESS_PARAMETER_EXECUTEID", task.getExecutionId());
          isTrigger = TriggerAPI.getInstance().excuteEvent(triggermoel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", hash, Long.valueOf(Long.parseLong(task.getProcessInstanceId())));
        }

        if (!isTrigger) {
          return false;
        }

        String[] receiveUsers = { receiveUser };

        String msg = this.processRuntimeSendService.executeBack(taskId, targetStepId, receiveUsers, null, "任务撤销", null);

        if ((msg != null) && (msg.equals("success")))
        {
          String action = "发起人撤销";
          this.processOpinionService.sendAction(action, actDefId, null, targetStepId, Long.valueOf(Long.parseLong(task.getProcessInstanceId())), taskId, Long.valueOf(Long.parseLong(task.getExecutionId())), resion);
          HashMap params = new HashMap();
          params.put("receiveUser", task.getAssignee());
          params.put("remindType", remindType);
          params.put("actDefId", task.getProcessDefinitionId());
          params.put("title", task.getDescription());
          params.put("PROCESS_TASK_PRIORITY", Integer.valueOf(0));
          params.put("instanceId", task.getProcessInstanceId());
          params.put("excutionId", task.getExecutionId());
          params.put("taskId", task.getId());
          params.put("opinion", resion);

          long count = this.taskService.createTaskQuery().taskAssignee(task.getAssignee()).count() + this.taskService.createTaskQuery().taskCandidateUser(task.getAssignee()).count();
          params.put("task_count", Long.valueOf(count));
          this.processRemindUtil.sendRemindRedoMsg(UserContextUtil.getInstance().getCurrentUserContext(), params, action);
          flag = true;
        }
      }

    }

    return flag;
  }

  public boolean doStepReback(String taskId, String resion, String[] remindType)
  {
    boolean flag = false;
    if (taskId != null) {
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      boolean isRead = this.processRuntimeExcuteService.checkTaskReadStatus(task);
      if (!isRead) {
        MessageQueueUtil.getInstance().putAlertMsg("当前用户已读取此任务，无法进行‘收回’操作，如需收回，请联系当前办理人");
        return flag;
      }

      String actDefId = task.getProcessDefinitionId();
      String currentUserId = UserContextUtil.getInstance().getCurrentUserId();
      String instanceId = task.getProcessInstanceId();
      String targetStepId = "";
      List<HistoricTaskInstance> list = ((HistoricTaskInstanceQuery)this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(instanceId).orderByTaskId().desc()).list();
      if ((list == null) || (list.size() > 0)) {
        for (HistoricTaskInstance history : list) {
          String tdk = history.getId();
          if (!tdk.equals(taskId))
          {
            String assignee = history.getAssignee();
            if ((assignee != null) && (assignee.equals(currentUserId))) {
              targetStepId = history.getTaskDefinitionKey();
              break;
            }
          }

        }

      }

      if ((targetStepId != null) && (!targetStepId.equals("")))
      {
        boolean isTrigger = true;

        ProcessDefTrigger triggermoel = this.processDefTriggerService.getProcessTriggerModel(task.getProcessDefinitionId(), null, "processRebacBefore");
        if (triggermoel != null) {
          HashMap hash = new HashMap();
          hash.put("PROCESS_PARAMETER_ACTDEFID", task.getProcessDefinitionId());
          hash.put("PROCESS_PARAMETER_INSTANCEID", task.getProcessInstanceId());
          hash.put("PROCESS_PARAMETER_TASKID", taskId);
          hash.put("PROCESS_PARAMETER_EXECUTEID", task.getExecutionId());
          isTrigger = TriggerAPI.getInstance().excuteEvent(triggermoel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", hash, Long.valueOf(Long.parseLong(task.getProcessInstanceId())));
        }

        if (!isTrigger) {
          return false;
        }

        String[] receiveUsers = { currentUserId };

        String msg = this.processRuntimeSendService.executeBack(taskId, targetStepId, receiveUsers, null, "任务撤销", null);
      // String msg =null;
        if ((msg != null) && (msg.equals("success")))
        {
          String action = "发起人撤销";
          this.processOpinionService.sendAction(action, actDefId, null, targetStepId, Long.valueOf(Long.parseLong(task.getProcessInstanceId())), taskId, Long.valueOf(Long.parseLong(task.getExecutionId())), resion);
          HashMap params = new HashMap();
          params.put("receiveUser", task.getAssignee());
          params.put("remindType", remindType);
          params.put("actDefId", task.getProcessDefinitionId());
          params.put("title", task.getDescription());
          params.put("PROCESS_TASK_PRIORITY", Integer.valueOf(0));
          params.put("instanceId", task.getProcessInstanceId());
          params.put("excutionId", task.getExecutionId());
          params.put("taskId", task.getId());
          params.put("opinion", resion);

          long count = this.taskService.createTaskQuery().taskAssignee(task.getAssignee()).count() + this.taskService.createTaskQuery().taskCandidateUser(task.getAssignee()).count();
          params.put("task_count", Long.valueOf(count));
          this.processRemindUtil.sendRemindRedoMsg(UserContextUtil.getInstance().getCurrentUserContext(), params, action);
          flag = true;
        }
      }
 //     DBUtil.executeUpdate("delete from process_ru_opinion where id=(select id from (select * from process_ru_opinion s where s.instanceid="+instanceId+"  order by id desc) where rownum=1) ");
   //   DBUtil.executeUpdate("delete from process_hi_taskinst where id_=(select id_ from (select * from process_hi_taskinst s where s.proc_inst_id_="+instanceId+"  order by id_ desc) where rownum=1)");
    //  DBUtil.executeUpdate("delete from PROCESS_HI_ACTINST where id_=(select id_ from (SELECT * FROM PROCESS_HI_ACTINST A WHERE A.PROC_INST_ID_="+instanceId+" ORDER BY A.ID_ DESC) where rownum=1)");
   //   DBUTilNew.update("  update PROCESS_HI_ACTINST set end_time_='',duration_='' where id_ = (select id_ from (SELECT * FROM PROCESS_HI_ACTINST A WHERE A.PROC_INST_ID_="+instanceId+" ORDER BY A.ID_ DESC) where rownum=1) ",null);
  //    DBUTilNew.update("  update process_hi_taskinst set end_time_='',duration_='',delete_reason_='' where id_ = (select id_ from (select * from process_hi_taskinst s where s.proc_inst_id_="+instanceId+"  order by id_ desc) where rownum=1) ",null);
  //    flag = true;
    }
    return flag;
  }
  public TaskService getTaskService() {
    return this.taskService;
  }

  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }

  public HistoryService getHistoryService() {
    return this.historyService;
  }

  public void setHistoryService(HistoryService historyService) {
    this.historyService = historyService;
  }

  public RuntimeService getRuntimeService() {
    return this.runtimeService;
  }

  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }
  public ProcessRuntimeCcDAO getProcessRuntimeCcDAO() {
    return this.processRuntimeCcDAO;
  }
  public void setProcessRuntimeCcDAO(ProcessRuntimeCcDAO processRuntimeCcDAO) {
    this.processRuntimeCcDAO = processRuntimeCcDAO;
  }

  public ProcessDeploymentService getProcessDeploymentService() {
    return this.processDeploymentService;
  }

  public void setProcessDeploymentService(ProcessDeploymentService processDeploymentService) {
    this.processDeploymentService = processDeploymentService;
  }
  public ProcessRuntimeExcuteService getProcessRuntimeExcuteService() {
    return this.processRuntimeExcuteService;
  }

  public void setProcessRuntimeExcuteService(ProcessRuntimeExcuteService processRuntimeExcuteService) {
    this.processRuntimeExcuteService = processRuntimeExcuteService;
  }

  public void setProcessRuntimeSendService(ProcessRuntimeSendService processRuntimeSendService) {
    this.processRuntimeSendService = processRuntimeSendService;
  }
  public ProcessEngine getProcessEngine() {
    return this.processEngine;
  }
  public void setProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }
  public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
    this.processOpinionService = processOpinionService;
  }

  public void setProcessDefTriggerService(ProcessDefTriggerService processDefTriggerService) {
    this.processDefTriggerService = processDefTriggerService;
  }
  public void setProcessOpinionDAO(ProcessOpinionDAO processOpinionDAO) {
    this.processOpinionDAO = processOpinionDAO;
  }

  public void setProcessRuntimeOperateService(ProcessRuntimeOperateService processRuntimeOperateService) {
    this.processRuntimeOperateService = processRuntimeOperateService;
  }
  public ProcessRuntimeOperateService getProcessRuntimeOperateService() {
    return this.processRuntimeOperateService;
  }
  public void setProcessRemindUtil(ProcessRemindUtil processRemindUtil) {
    this.processRemindUtil = processRemindUtil;
  }
}