package com.iwork.process.desk.handle.service;

import com.ibm.icu.util.Calendar;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.syscalendar.sdk.SysCalendarAPI;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.flow.model.ProcessDefMap;
import com.iwork.process.definition.flow.service.ProcessDefTriggerService;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.model.ProcessStepSummary;
import com.iwork.process.definition.step.service.ProcessStepSummaryService;
import com.iwork.process.desk.handle.model.TodoTaskModel;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.dao.ProcessRuntimeCcDAO;
import com.iwork.process.runtime.model.ProcessRuCc;
import com.iwork.process.runtime.model.ProcessRuSigns;
import com.iwork.process.runtime.pvm.impl.task.PvmProcessTaskEngine;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.runtime.service.ProcessRuntimeSendService;
import com.iwork.process.runtime.service.ProcessRuntimeSignsService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.EngineAPI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
public class ProcessDeskService
{
	private static Logger logger = Logger.getLogger(ProcessDeskService.class);
  private final int pageSize = 15;
  private boolean isMore = false;
  private TaskService taskService;
  private HistoryService historyService;
  private RuntimeService runtimeService;
  private ProcessEngine processEngine;
  private ProcessRuntimeCcDAO processRuntimeCcDAO;
  private ProcessDeploymentService processDeploymentService;
  private ProcessRuntimeExcuteService processRuntimeExcuteService;
  private ProcessRuntimeSendService processRuntimeSendService;
  private ProcessOpinionService processOpinionService;
  private ProcessDefTriggerService processDefTriggerService;
  private ProcessStepSummaryService processStepSummaryService;
  private ProcessRuntimeSignsService processRuntimeSignsService;
  private final Long IWORK_WARNNING_ORANGE = new Long(1L);
  private final Long IWORK_WARNNING_RED = new Long(2L);
  private static String[] tabTitle = { "已办跟踪", "待办流程", "通知/抄送", "办理日志" };

  public void initProcessSystem()
  {
    List<Task> list = this.taskService.createTaskQuery().list();
    String taskId;
    for (Task task : list) {
      taskId = task.getId();

      Long instanceId = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
      this.processRuntimeExcuteService.removeInstanceData(taskId, instanceId);

      this.historyService.deleteHistoricTaskInstance(taskId);

      this.taskService.deleteTask(taskId, true);
    }

    List<HistoricTaskInstance> hislist = this.historyService.createHistoricTaskInstanceQuery().list();
    for (HistoricTaskInstance task : hislist) {
      String taskId1 = task.getId();
      Long instanceId = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
      this.processRuntimeExcuteService.removeInstanceData(taskId1, instanceId);

      this.historyService.deleteHistoricTaskInstance(taskId1);
    }

    this.processRuntimeExcuteService.iniitFormBindData(EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
  }

  public String getTabHtml(int deskType, String actDefKey)
  {
    StringBuffer html = new StringBuffer();
    html.append("<ul class=\"leftTab\">\n");
    int count = 0;
    for (String title : tabTitle) {
      count++;
      html.append("<li ");
      if (count == deskType)
        html.append("class=\"tab_active\"");
      else {
        html.append("class=\"tab_item\"");
      }
      html.append(">\n").append("<a href=\"process_desk_box.action?desktype=").append(count).append("&actDefkey=").append(actDefKey).append("\">").append(title).append("</a></li>\n");
    }
    html.append("</ul>\n");
    return html.toString();
  }

  public ProcessDefDeploy getProcessTitle(String actDefkey) {
    ProcessDefDeploy deploy = null;
    String actDefId = null;
    if (actDefkey != null) {
      List<ProcessDefinition> list = this.processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(actDefkey).list();
      for (ProcessDefinition pd : list) {
        ProcessDefDeploy temp = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(pd.getId());
        if ((temp != null) && (temp.getStatus() != null) && 
          (temp.getStatus().equals(SysConst.on))) {
          deploy = temp;
          break;
        }
      }

    }

    return deploy;
  }

  public List<TodoTaskModel> getTaskList(String searchKey)
  {
    return getTaskList(null, searchKey);
  }

  public List<TodoTaskModel> getTaskList(String actDefkey, String searchKey)
  {
    List tasklist = new ArrayList();
    StringBuffer html = new StringBuffer();
    String userId = UserContextUtil.getInstance().getCurrentUserId();
    List<Task> candidatelist = null;
    List<Task> todolist = null;

    List<ProcessRuSigns> signsList = this.processRuntimeSignsService.getSignsTodoList(actDefkey, userId, searchKey);
    StringBuffer msg;
    if (signsList != null) {
      for (ProcessRuSigns signsModel : signsList) {
        TodoTaskModel firstModel = buildHistoricTaskTaskModel(signsModel);
        if (firstModel != null) {
          msg = new StringBuffer();
          msg.append("【会签】").append(firstModel.getTitle()).append("");
          firstModel.setTitle(msg.toString());
          tasklist.add(firstModel);
        }
      }
    }

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

    List cclist = this.processRuntimeCcDAO.getUserCCUnReadList(actDefId, userId, searchKey);
    StringBuffer msg1;
    if ((cclist != null) && (cclist.size() > 0)) {
      TodoTaskModel firstModel = buildHistoricTaskTaskModel((ProcessRuCc)cclist.get(0));
      if (firstModel != null) {
        msg1 = new StringBuffer();
        msg1.append("<img src='iwork_img/warning.gif'>【抄送通知】您有<span style='color:red'>").append(cclist.size()).append("</span>个未读的流程抄送任务-<span style='color:#D2D2D2;weight:100'>").append(firstModel.getTitle()).append("</span>");
        TodoTaskModel model = new TodoTaskModel();
        model.setOwnerName("系统提示");
        model.setContent(firstModel.getTitle());
        model.setTitle(msg1.toString());
        model.setTaskType("Notice");
        model.setCreateDate(firstModel.getCreateDate());
        model.setIsRead(new Long(1L));
        model.setOperate(firstModel.getOperate());
        tasklist.add(model);
      }
    }

    if ((searchKey == null) || ("".equals(searchKey)))
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().processDefinitionKey(actDefkey).taskCandidateUser(userId).orderByTaskCreateTime().desc()).list();
    else {
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().processDefinitionKey(actDefkey).taskCandidateUser(userId).taskDescriptionLike("%" + searchKey + "%").orderByTaskCreateTime().desc()).list();
    }

    if (candidatelist != null) {
      for (Task task : candidatelist) {
        TodoTaskModel model = buildTaskModel(task, "CandidateUser", userId);
        if (model != null) {
          tasklist.add(model);
        }

      }

    }

    if ((searchKey == null) || ("".equals(searchKey)))
      todolist = ((TaskQuery)this.taskService.createTaskQuery().processDefinitionKey(actDefkey).taskAssignee(userId).orderByTaskCreateTime().desc()).list();
    else {
      todolist = ((TaskQuery)this.taskService.createTaskQuery().processDefinitionKey(actDefkey).taskDescriptionLike("%" + searchKey + "%").taskAssignee(userId).orderByTaskCreateTime().desc()).list();
    }

    if (todolist != null) {
      for (Task task : todolist) {
        TodoTaskModel model = buildTaskModel(task, "Assignee", userId);
        if (model != null) {
          tasklist.add(model);
        }
      }
    }
    return tasklist;
  }

  public String getListHtml(String searchKey, int pageNo, int itemNum)
  {
    this.isMore = false;
    StringBuffer html = new StringBuffer();
    List<TodoTaskModel> list = getTaskListPage(searchKey, pageNo, itemNum);
    int num = itemNum;
    num++;
    for (TodoTaskModel ttm : list) {
      String read_css = "";
      if (ttm.getIsRead().equals(new Long(1L))) {
        read_css = " read ";
      }

      if (ttm.getTaskType().equals("Notice")) {
        html.append("<tr class=\"table-tr-notice ").append(read_css).append("\" id=\"notice_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append(" <td class=\"task_title\" onclick=\"openNoticePage();\">").append(ttm.getTitle()).append("</td>").append("\n");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">").append(ttm.getLongTime()).append("</td>").append("\n");
        html.append("</tr> ");
      } else if (ttm.getTaskType().equals("Signs")) {
        html.append("<tr class=\"table-tr-notice ").append(read_css).append("\" id=\"signs_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append("<td class=\"task_title\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(ttm.getTitle()).append("</font></td>");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">");
        if (ttm.getWarningStr() != null) {
          html.append("<span style=\"background:red;color:#fff;padding:2px;\">");
          html.append(ttm.getLongTime());
          html.append("</span>");
        } else {
          html.append(ttm.getLongTime());
        }
        html.append("</td>").append("\n");
        html.append("</tr> ");
      } else {
        html.append("<tr class=\"wr-table-tr-row ").append(read_css).append("\" id=\"task_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append("<td class=\"task_title\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(ttm.getTitle()).append("</font></td>");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">");
        if (ttm.getWarningStr() != null) {
          html.append("<span style=\"background:red;color:#fff;padding:2px;\">");
          html.append(ttm.getLongTime());
          html.append("</span>");
        } else {
          html.append(ttm.getLongTime());
        }
        html.append("</td>").append("\n");
        html.append("</tr> ");
      }

      html.append("<tr class=\"wr-table-td-inner summary\" id=\"content").append(ttm.getTaskId()).append("\">").append("\n");
      html.append("<td>&nbsp;</td>").append("\n");
      html.append("<td class=\"row1\">&nbsp;</td>").append("\n");
      html.append("<td colspan=\"4\" ><div class=\"summary_main\">").append(ttm.getContent()).append("</div></td>").append("\n");
      html.append("</tr>").append("\n");
      num++;
    }
    if (this.isMore) {
      html.append("<tr class=\"moreBtnTr\">").append("\n");
      html.append("<td colspan=\"6\" ><div class=\"moreBtn\"><a href=\"javascript:getMoreItem()\">更多待办事宜...</a></div></td>").append("\n");

      html.append("</tr>").append("\n");
    }
    return html.toString();
  }

  public String getMiniListHtml(String searchKey, int pageNo, int itemNum)
  {
    this.isMore = false;
    StringBuffer html = new StringBuffer();
    List<TodoTaskModel> list = getTaskListPage(searchKey, pageNo, itemNum);
    int num = itemNum;
    num++;
    for (TodoTaskModel ttm : list) {
      String read_css = "";
      if (ttm.getIsRead().equals(new Long(1L))) {
        read_css = " read ";
      }

      if (ttm.getTaskType().equals("Notice")) {
        html.append("<tr class=\"table-tr-notice ").append(read_css).append("\" id=\"notice_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append(" <td class=\"task_title\" onclick=\"openNoticePage();\">").append(ttm.getTitle()).append("</td>").append("\n");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">").append(ttm.getLongTime()).append("</td>").append("\n");
        html.append("</tr> ");
      } else if (ttm.getTaskType().equals("Signs")) {
        html.append("<tr class=\"table-tr-notice ").append(read_css).append("\" id=\"signs_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append("<td class=\"task_title\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(ttm.getTitle()).append("</font></td>");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">");
        if (ttm.getWarningStr() != null) {
          html.append("<span style=\"background:red;color:#fff;padding:2px;\">");
          html.append(ttm.getLongTime());
          html.append("</span>");
        } else {
          html.append(ttm.getLongTime());
        }
        html.append("</td>").append("\n");
        html.append("</tr> ");
      } else {
        html.append("<tr class=\"wr-table-tr-row ").append(read_css).append("\" id=\"task_").append(ttm.getTaskId()).append("\">").append("\n");
        html.append("<td class=\"row3\">").append(num).append("</td>").append("\n");
        html.append("<td class=\"row1\">").append(ttm.getOwnerName()).append("</a></td>").append("\n");
        html.append("<td class=\"task_title\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(ttm.getTitle()).append("</font></td>");
        html.append(" <td><ul class=\"inline-operatebar\">").append("\n");
        html.append(ttm.getOperate()).append("\n");
        html.append("</ul></td>");
        html.append("<td class=\"row2\">").append(UtilDate.datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
        html.append(" <td class=\"row1\">");
        if (ttm.getWarningStr() != null) {
          html.append("<span style=\"background:red;color:#fff;padding:2px;\">");
          html.append(ttm.getLongTime());
          html.append("</span>");
        } else {
          html.append(ttm.getLongTime());
        }
        html.append("</td>").append("\n");
        html.append("</tr> ");
      }

      html.append("<tr class=\"wr-table-td-inner summary\" id=\"content").append(ttm.getTaskId()).append("\">").append("\n");
      html.append("<td>&nbsp;</td>").append("\n");
      html.append("<td class=\"row1\">&nbsp;</td>").append("\n");
      html.append("<td colspan=\"4\" ><div class=\"summary_main\">").append(ttm.getContent()).append("</div></td>").append("\n");
      html.append("</tr>").append("\n");
      num++;
    }
    if (this.isMore) {
      html.append("<tr class=\"moreBtnTr\">").append("\n");
      html.append("<td colspan=\"6\" ><div class=\"moreBtn\"><a href=\"javascript:getMoreItem()\">更多待办事宜...</a></div></td>").append("\n");

      html.append("</tr>").append("\n");
    }
    return html.toString();
  }

  public List<TodoTaskModel> getTaskListPage(String searchKey, int pageNo, int itemNum)
  {
    int noticeNum = 0;
    int signsNum = 0;
    int condidateNum = 0;
    int rowNum = 15;
    if ((searchKey != null) && (searchKey.trim().equals(""))) {
      searchKey = null;
    }
    List tasklist = new ArrayList();
    StringBuffer html = new StringBuffer();
    String userId = UserContextUtil.getInstance().getCurrentUserId();
    if(userId == null) return tasklist;//xlj 漏洞扫描 2018年5月15日11:00:06
    List<Task> candidatelist = null;
    List<Task> todolist = null;

    List cclist = this.processRuntimeCcDAO.getUserCCUnReadList(null, userId, searchKey);
    TodoTaskModel model;
    if ((cclist != null) && (cclist.size() > 0)) {
      if (pageNo == 1) {
        TodoTaskModel firstModel = buildHistoricTaskTaskModel((ProcessRuCc)cclist.get(0));
        if (firstModel != null) {
          StringBuffer msg = new StringBuffer();
          msg.append("<img src='iwork_img/warning.gif'>【抄送通知】您有<span style='color:red'>").append(cclist.size()).append("</span>个未读的流程抄送任务-<span style='color:#D2D2D2;weight:100'>").append(firstModel.getTitle()).append("</span>");
          model = new TodoTaskModel();
          model.setOwnerName("系统提示");
          model.setContent(firstModel.getTitle());
          model.setTitle(msg.toString());
          model.setTaskType("Notice");
          model.setLongTime("");
          model.setCreateDate(firstModel.getCreateDate());
          model.setIsRead(new Long(1L));
          model.setOperate(firstModel.getOperate());
          tasklist.add(model);
          rowNum--;
        }
      }
      noticeNum++;
    }

    List<ProcessRuSigns> signsList = this.processRuntimeSignsService.getSignsTodoList(null, userId, searchKey);
    if (signsList != null) {
      if (pageNo == 1) {
        for (ProcessRuSigns signsModel : signsList) {
          if (rowNum == 0) {
            break;
          }
          TodoTaskModel firstModel = buildHistoricTaskTaskModel(signsModel);
          if (firstModel != null) {
            StringBuffer msg = new StringBuffer();
            msg.append("【会签】").append(firstModel.getTitle()).append("");
            firstModel.setTitle(msg.toString());
            tasklist.add(firstModel);
            rowNum--;
          }
        }
      }

      signsNum = signsList.size();
    }

    if (rowNum < 1) {
      this.isMore = true;
      return tasklist;
    }
    int isCondidate = 0;
    int upSum = noticeNum + signsNum;

    int startNum = 0;
    int endNum = 0;
    Long condidateCount = new Long(0L);
    if (searchKey != null) {
      condidateCount = Long.valueOf(this.taskService.createTaskQuery().taskDescriptionLike("%" + searchKey + "%").taskCandidateUser(userId).count());
      if (itemNum - upSum <= 0) {
        startNum = 0;
        endNum = 15 - upSum;
      } else {
        startNum = condidateCount.intValue() - (condidateCount.intValue() - (itemNum - upSum));
        endNum = startNum + 15;
      }
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskDescriptionLike("%" + searchKey + "%").taskCandidateUser(userId).orderByTaskCreateTime().desc()).listPage(startNum, endNum);
    } else {
      condidateCount = Long.valueOf(this.taskService.createTaskQuery().taskCandidateUser(userId).count());
      if (itemNum - upSum <= 0) {
        startNum = 0;
        endNum = 15 - upSum;
      } else {
        startNum = condidateCount.intValue() - (condidateCount.intValue() - (itemNum - upSum));
        endNum = startNum + 15;
      }
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(userId).orderByTaskCreateTime().desc()).listPage(startNum, endNum);
    }
    condidateNum = condidateCount.intValue();

    if (candidatelist != null) {
      for (Task task : candidatelist) {
        if (rowNum == 0) {
          break;
        }
        TodoTaskModel model1 = buildTaskModel(task, "CandidateUser", userId);
        if (model1 != null) {
          tasklist.add(model1);
          rowNum--;
        }
      }
    }
    if (rowNum <= 1) {
      this.isMore = true;
      return tasklist;
    }
    int otherNum = noticeNum + signsNum + condidateNum;
    Long todoNum = new Long(0L);
    if ((searchKey == null) || ("".equals(searchKey))) {
      todoNum = Long.valueOf(this.taskService.createTaskQuery().taskAssignee(userId).count());
      if (itemNum - otherNum <= 0) {
        startNum = 0;
        endNum = rowNum;
      } else {
        startNum = todoNum.intValue() - (todoNum.intValue() - (itemNum - otherNum));
        endNum = startNum + rowNum;
      }
      todolist = ((TaskQuery)this.taskService.createTaskQuery().taskAssignee(userId).orderByTaskPriority().desc().orderByTaskCreateTime().desc()).listPage(startNum, endNum);
    } else {
      todoNum = Long.valueOf(this.taskService.createTaskQuery().taskDescriptionLike("%" + searchKey + "%").taskAssignee(userId).count());
      if (itemNum - otherNum <= 0) {
        startNum = 0;
        endNum = rowNum;
      } else {
        startNum = todoNum.intValue() - (todoNum.intValue() - (itemNum - otherNum));
        endNum = startNum + rowNum;
      }
      todolist = ((TaskQuery)this.taskService.createTaskQuery().taskDescriptionLike("%" + searchKey + "%").taskAssignee(userId).orderByTaskPriority().desc().orderByTaskCreateTime().desc()).listPage(startNum, endNum);
    }

    if (todolist != null) {
      for (Task task : todolist) {
        if (rowNum == 0) {
          break;
        }
        TodoTaskModel model1 = buildTaskModel(task, "Assignee", userId);
        if (model1 != null) {
          tasklist.add(model1);
          rowNum--;
        }
      }
    }

    if (otherNum + todoNum.longValue() > itemNum) {
      this.isMore = true;
    }
    return tasklist;
  }

  public List<TodoTaskModel> getTaskList(String actDefkey, String userId, String searchkey)
  {
	  
    List tasklist = new ArrayList();
    if(userId == null) return tasklist;//xlj 扫描漏洞 2018年5月15日10:42:00
    StringBuffer html = new StringBuffer();
    List<Task> candidatelist = null;
    if (actDefkey != null) "".equals(actDefkey);

    if ((searchkey == null) || ("".equals(searchkey)))
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(userId).orderByTaskCreateTime().desc()).list();
    else {
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(userId).taskDescriptionLike("%" + searchkey + "%").orderByTaskCreateTime().desc()).list();
    }
    List<Task> todolist = null;

    if (candidatelist != null) {
      for (Task task : candidatelist) {
        TodoTaskModel model = buildTaskModel(task, "CandidateUser", userId);
        if (model != null) {
          tasklist.add(model);
        }
      }
    }
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

    List<ProcessRuSigns> signsList = this.processRuntimeSignsService.getSignsTodoList(actDefId, userId, searchkey);
    if (signsList != null) {
      for (ProcessRuSigns signsModel : signsList) {
        TodoTaskModel firstModel = buildHistoricTaskTaskModel(signsModel);
        if (firstModel != null) {
          StringBuffer msg = new StringBuffer();
          msg.append("【会签】").append(firstModel.getTitle()).append("");
          firstModel.setTitle(msg.toString());
          tasklist.add(firstModel);
        }
      }

    }

    List cclist = this.processRuntimeCcDAO.getUserCCUnReadList(actDefId, userId, searchkey);
    StringBuffer msg;
    if ((cclist != null) && (cclist.size() > 0)) {
      TodoTaskModel firstModel = buildHistoricTaskTaskModel((ProcessRuCc)cclist.get(0));
      if (firstModel != null) {
        msg = new StringBuffer();
        msg.append("<img src='iwork_img/warning.gif'>【抄送通知】您有<span style='color:red'>").append(cclist.size()).append("</span>个未读的流程抄送任务-<span style='color:#D2D2D2;weight:100'>").append(firstModel.getTitle()).append("</span>");
        TodoTaskModel model = new TodoTaskModel();
        model.setOwnerName("系统提示");
        model.setContent(firstModel.getTitle());
        model.setTitle(msg.toString());
        model.setTaskType("Notice");
        model.setCreateDate(firstModel.getCreateDate());
        model.setIsRead(new Long(1L));
        model.setOperate(firstModel.getOperate());
        tasklist.add(model);
      }

    }

    if ((searchkey == null) || ("".equals(searchkey)))
      todolist = ((TaskQuery)this.taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc()).list();
    else {
      todolist = ((TaskQuery)this.taskService.createTaskQuery().taskDescriptionLike("%" + searchkey + "%").orderByTaskCreateTime().desc()).list();
    }

    if (todolist != null) {
      for (Task task : todolist) {
        TodoTaskModel model = buildTaskModel(task, "Assignee", userId);
        if (model != null) {
          tasklist.add(model);
        }
      }
    }
    return tasklist;
  }

  public List<TodoTaskModel> getTaskCandidateList(String searchKey)
  {
    List tasklist = new ArrayList();
    StringBuffer html = new StringBuffer();
    String userId = UserContextUtil.getInstance().getCurrentUserId();
    List<Task> candidatelist = null;
    List todolist = null;

    if ((searchKey == null) || ("".equals(searchKey)))
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(userId).orderByTaskCreateTime().desc()).list();
    else {
      candidatelist = ((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(userId).taskDescriptionLike("%" + searchKey + "%").orderByTaskCreateTime().desc()).list();
    }

    if (candidatelist != null) {
      for (Task task : candidatelist) {
        TodoTaskModel model = buildTaskModel(task, "CandidateUser", userId);
        if (model != null) {
          tasklist.add(model);
        }
      }
    }
    return tasklist;
  }

  public String getTaskSummary(String taskId)
  {
    StringBuffer content = new StringBuffer();
    content.append("摘要:<br>");
    if (taskId != null) {
      HistoricTaskInstance task = (HistoricTaskInstance)this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
      if (task != null) {
        ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(task.getProcessDefinitionId());
        List<ProcessStepSummary> list = this.processStepSummaryService.getProcessStepSummaryDAO().getList(model.getId(), task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        if ((list == null) || (list.size() == 0)) {
          list = this.processStepSummaryService.getProcessStepSummaryDAO().getList(model.getId(), task.getProcessDefinitionId());
        }
        if ((list != null) && (list.size() > 0)) {
          EngineAPI api = new EngineAPI();
          HashMap hashdata = api.getFromData(Long.valueOf(Long.parseLong(task.getProcessInstanceId())), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
          for (ProcessStepSummary summary : list) {
            String value = "";
            if (summary.getFieldName() != null)
            {
              Object fieldName = hashdata.get(summary.getFieldName());
              if (fieldName != null)
              {
                if ((fieldName instanceof Date))
                  value = UtilDate.dateFormat((Date)fieldName);
                else {
                  value = fieldName.toString();
                }

                content.append("[").append(summary.getFieldTitle()).append("]&nbsp;").append(value).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
              }
            }
          }
        }
      }
    }
    return content.toString();
  }

  private TodoTaskModel buildTaskModel(Task task, String taskType, String userId)
  {
    TodoTaskModel model = new TodoTaskModel();
    if (task != null) {
      model.setContent("加载正文请稍候...");
      model.setTaskType(taskType);

      if (task.getExecutionId() != null) {
        model.setExcutionId(Long.valueOf(Long.parseLong(task.getExecutionId())));
      }
      if (task.getProcessInstanceId() != null) {
        model.setInstanceId(Long.valueOf(Long.parseLong(task.getProcessInstanceId())));
      }
      if (task.getId() != null) {
        model.setTaskId(Long.valueOf(Long.parseLong(task.getId())));
      }
      if (task.getDescription() != null) {
        model.setTitle(task.getDescription());
      }
      if (task.getProcessDefinitionId() != null) {
        model.setActDefId(task.getProcessDefinitionId());
      }
      if (task.getTaskDefinitionKey() != null) {
        model.setActStepId(task.getTaskDefinitionKey());
      }
      if (task.getOwner() != null) {
        UserContext owner = UserContextUtil.getInstance().getUserContext(task.getOwner());
        if (owner != null) {
          model.setOwnerId(owner.get_userModel().getUserid());
          if(task.getPriority()==80)
        	  model.setOwnerName("<img src=\"iwork_img/star-on.png\"/>"+owner.get_userModel().getUsername());
          else
        	  model.setOwnerName(owner.get_userModel().getUsername());
        }
      }
      if (task.getCreateTime() != null) {
        model.setCreateDate(task.getCreateTime());

        String durtime = UtilDate.TimeDiff(Long.valueOf(task.getCreateTime().getTime()), Long.valueOf(new Date().getTime()));
        model.setLongTime(durtime);

        ProcessDefMap pdm = this.processRuntimeExcuteService.getProcessDefMapService().getProcessDefMapDAO().getProcessDefMapModel(task.getProcessDefinitionId());
        if ((pdm != null) && 
          (pdm.getIsMonitor() != null) && (pdm.getIsMonitor().equals(SysConst.on))) {
          boolean isWarning = false;
          if (!isWarning)
          {
            ProcessStepMap psm = this.processRuntimeExcuteService.getProcessStepMapService().getProcessDefMapModel(pdm.getPrcDefId(), task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            if ((psm != null) && (psm.getWarningTime() != null)) {
              if (psm.getStandardTime() == null) {
                psm.setStandardTime(new Long(0L));
              }
              int days = SysCalendarAPI.getInstance().betweenStartAndEndDays(task.getCreateTime(), new Date(), UserContextUtil.getInstance().getCurrentUserId());
              int standardHours = psm.getStandardTime().intValue();
              Double d = Double.valueOf(Math.ceil(standardHours / 24));
              if (d.intValue() == 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(task.getCreateTime());
                c.add(10, standardHours);
                long standardNumber = c.getTime().getTime() - new Date().getTime();
                if (standardNumber < 0L) {
                  isWarning = true;
                  model.setWarningStr("当前任务已超出预警时间，请及时办理!");
                  model.setWarningType(this.IWORK_WARNNING_ORANGE);
                  Calendar c2 = Calendar.getInstance();
                  c2.setTime(task.getCreateTime());
                  c2.add(10, psm.getWarningTime().intValue());
                  long warningNumber = c2.getTime().getTime() - new Date().getTime();
                  if (warningNumber < 0L) {
                    isWarning = true;
                    model.setWarningType(this.IWORK_WARNNING_RED);
                    model.setWarningStr("当前任务已超出预警时间，请及时办理!");
                  }
                }
              }
              else if (d.intValue() < days) {
                isWarning = true;
                model.setWarningType(this.IWORK_WARNNING_ORANGE);
                model.setWarningStr("当前任务已超出预警时间，请及时办理!");
                Double d2 = Double.valueOf(Math.ceil(psm.getWarningTime().intValue() / 24));
                if (d2.intValue() < days) {
                  model.setWarningType(this.IWORK_WARNNING_RED);
                  model.setWarningStr("当前任务已超出预警时间，请及时办理!");
                }

              }

            }

          }

        }

      }

      boolean flag = this.processRuntimeExcuteService.checkTaskReadStatus(task);
      if (flag)
        model.setIsRead(new Long(1L));
      else {
        model.setIsRead(new Long(0L));
      }

      model.setOperate(getTaskOperate(task, taskType, userId));
    }

    return model;
  }

  private TodoTaskModel buildHistoricTaskTaskModel(ProcessRuSigns signsModel)
  {
    TodoTaskModel model = new TodoTaskModel();
    if (signsModel != null) {
      model.setContent("加载正文请稍候...");
      model.setTaskType("Signs");

      if (signsModel.getExcutionid() != null) {
        model.setExcutionId(signsModel.getExcutionid());
      }
      if (signsModel.getInstanceid() != null) {
        model.setInstanceId(signsModel.getInstanceid());
      }
      if (signsModel.getTaskid() != null) {
        model.setTaskId(signsModel.getTaskid());
      }
      if (signsModel.getTitle() != null) {
        model.setTitle(signsModel.getTitle());
      }
      if (signsModel.getActDefId() != null) {
        model.setActDefId(signsModel.getActDefId());
      }
      if (signsModel.getActStepId() != null) {
        model.setActStepId(signsModel.getActStepId());
      }
      if (signsModel.getOwner() != null) {
        UserContext owner = UserContextUtil.getInstance().getUserContext(signsModel.getOwner());
        if (owner != null) {
          model.setOwnerId(owner.get_userModel().getUserid());
          model.setOwnerName(owner.get_userModel().getUsername());
        }
      }
      if (signsModel.getCreateTime() != null) {
        model.setCreateDate(signsModel.getCreateTime());

        String durtime = UtilDate.TimeDiff(Long.valueOf(signsModel.getCreateTime().getTime()), Long.valueOf(new Date().getTime()));
        model.setLongTime(durtime);
      }
      if (signsModel.getReadtime() == null)
        model.setIsRead(new Long(1L));
      else {
        model.setIsRead(new Long(0L));
      }

      model.setDataid(signsModel.getId());

      StringBuffer btnHTML = new StringBuffer();
      btnHTML.append("<li class=\"operate\"><a class=\"operate msg\" href=\"javascript:setTaskSigns(").append(signsModel.getTaskid()).append(",").append(signsModel.getId()).append(");\" title=\"标记已读\"></a></li>").append("\n");
      btnHTML.append("<li class=\"operate\"><a class=\"operate more\" title=\"查看摘要\" id=\"more").append(signsModel.getTaskid()).append("\" href=\"javascript:showInfo(").append(signsModel.getTaskid()).append(")\"></a></li>").append("\n");

      model.setOperate(btnHTML.toString());
    }

    return model;
  }

  private TodoTaskModel buildHistoricTaskTaskModel(ProcessRuCc ccModel)
  {
    TodoTaskModel model = new TodoTaskModel();
    if (ccModel != null) {
      model.setContent("加载正文请稍候...");
      model.setTaskType("Notice");

      if (ccModel.getExcutionid() != null) {
        model.setExcutionId(ccModel.getExcutionid());
      }
      if (ccModel.getInstanceid() != null) {
        model.setInstanceId(ccModel.getInstanceid());
      }
      if (ccModel.getTaskid() != null) {
        model.setTaskId(ccModel.getTaskid());
      }
      if (ccModel.getTitle() != null) {
        model.setTitle(ccModel.getTitle());
      }
      if (ccModel.getActDefId() != null) {
        model.setActDefId(ccModel.getActDefId());
      }
      if (ccModel.getActStepId() != null) {
        model.setActStepId(ccModel.getActStepId());
      }
      if (ccModel.getCcUser() != null) {
        UserContext owner = UserContextUtil.getInstance().getUserContext(ccModel.getCcUser());
        if (owner != null) {
          model.setOwnerId(owner.get_userModel().getUserid());
          model.setOwnerName(owner.get_userModel().getUsername());
        }
      }
      if (ccModel.getCcTime() != null) {
        model.setCreateDate(ccModel.getCcTime());

        String durtime = UtilDate.TimeDiff(Long.valueOf(ccModel.getCcTime().getTime()), Long.valueOf(new Date().getTime()));
        model.setLongTime(durtime);
      }
      model.setIsRead(new Long(1L));
      model.setDataid(ccModel.getId());

      StringBuffer btnHTML = new StringBuffer();
      btnHTML.append("<li class=\"operate\"><a class=\"operate msg\" href=\"javascript:setTaskNotice(").append(ccModel.getTaskid()).append(",").append(ccModel.getId()).append(");\" title=\"标记已读\"></a></li>").append("\n");
      btnHTML.append("<li class=\"operate\"><a class=\"operate more\" title=\"查看摘要\" id=\"more").append(ccModel.getTaskid()).append("\" href=\"javascript:showInfo(").append(ccModel.getTaskid()).append(")\"></a></li>").append("\n");

      model.setOperate(btnHTML.toString());
    }

    return model;
  }

  private String getTaskOperate(Task task, String taskType, String userId)
  {
    StringBuffer html = new StringBuffer();
    if (taskType.equals("CandidateUser")) {
      html.append("<li class=\"operate\"><a class=\"operate ly\" href=\"javascript:claim(" + task.getId() + ",'" + UserContextUtil.getInstance().getCurrentUserId() + "');\" title=\"领取任务\"></a></li>").append("\n");
    }
    else {
      boolean isDel = PvmProcessTaskEngine.getInstance().checkTaskIsDel(task.getId(), userId);
      if (isDel) {
        ProcessDefMap processDefMap = this.processRuntimeExcuteService.getProcessDefMapService().getProcessDefMapModel(task.getProcessDefinitionId());

        if (processDefMap != null) {
          if ((processDefMap.getTaskCancelType() != null) && (processDefMap.getTaskCancelType().equals("DEL"))){
        	  if(task.getAssignee().equals(UserContextUtil.getInstance().getCurrentUserId())){
            html.append("<li class=\"operate\"><a class=\"operate del\" title=\"删除当前待办任务\"  href=\"javascript:deleteTask(" + task.getId() + ");\"></a></li>").append("\n");
        	  }
          }else{
            html.append("<li class=\"operate\"><a class=\"operate close\" title=\"关闭当前待办任务\" href=\"javascript:closeTask(" + task.getId() + ");\"></a></li>").append("\n");
          }
        }
        else
        	if(task.getAssignee().equals(UserContextUtil.getInstance().getCurrentUserId())){
          html.append("<li class=\"operate\"><a class=\"operate del\" title=\"删除当前待办任务\"   href=\"javascript:deleteTask(" + task.getId() + ");\"></a></li>").append("\n");
        	}
      }
      else {
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
            if (isWait.equals(new Long(0L))) {
              html.append("<li class=\"operate\"><a class=\"operate gq\" title=\"[任务已休眠]，点击激活当前任务\"  href=\"javascript:activeTask(" + task.getId() + ");\"></a></li>").append("\n");
            }
            else if (isWait.equals(new Long(1L)))
            {
              html.append("<li class=\"operate\"><a class=\"operate gq\" href=\"javascript:sleepTask(" + task.getId() + ");\" title=\"点击执行修休眠(执行睡眠操作后，将不再对任务进行提醒及催办)\"></a></li>").append("\n");
            }
          }
        }
      }
    }
    html.append("<li class=\"operate\"><a class=\"operate more\" title=\"查看摘要\" id=\"more").append(task.getId()).append("\" href=\"javascript:showInfo(").append(task.getId()).append(")\"></a></li>").append("\n");
    return html.toString();
  }

  public boolean setNoticeReadStatus(Long dataid)
  {
    boolean flag = false;
    ProcessRuCc model = this.processRuntimeCcDAO.getCCModel(dataid);
    if ((model != null) && 
      (model.getTargetUser() != null) && (model.getTargetUser().equals(UserContextUtil.getInstance().getCurrentUserId()))) {
      model.setReadtime(new Date());
      this.processRuntimeCcDAO.update(model);
      flag = true;
    }

    return flag;
  }

  public String getHistoryTaskJson(String from, String actDefkey, String title, Long isCreate, Long processStatus, Page page)
  {
    StringBuffer jsonHtml = new StringBuffer();
    Map total = new HashMap();
    List items = new ArrayList();
    int totalRecord = 0;
    int totalNum = 0;

    String userid = UserContextUtil.getInstance().getCurrentUserId();
    HistoricTaskInstanceQuery taskQuery = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userid).finished();
    if ((actDefkey != null) && (!"".equals(actDefkey))) {
      taskQuery.processDefinitionKey(actDefkey);
    }

    if (processStatus != null) {
      if (processStatus.equals(new Long(1L))) {
        taskQuery = taskQuery.processUnfinished();
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByHistoricTaskInstanceJoinEndTime().desc();
      } else if (processStatus.equals(new Long(2L))) {
        taskQuery = taskQuery.processFinished();
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByHistoricTaskInstanceJoinEndTime().desc();
      } else {
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByHistoricTaskInstanceEndTime().desc();
      }
    }
    else taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByHistoricTaskInstanceEndTime().desc();

    if ((from != null) && (!from.equals(""))) {
      from = from.trim();
      taskQuery = taskQuery.taskOwner(from);
    }

    if ((title != null) && (!title.equals(""))) {
      taskQuery = taskQuery.taskDescriptionLike("%" + title + "%");
    }
    if (isCreate != null) {
      taskQuery = taskQuery.taskOwner(userid);
    }
    if (page == null) {
      page = new Page();
      page.setOrderBy("");
      page.setCurPageNo(0);
      page.setPageSize(15);
      page.setCurPageNo(1);
    }

    if (page.getOrderBy().equals("2")) {
      if (page.getOrder().equals("asc"))
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByTaskOwner().asc();
      else
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByTaskOwner().desc();
    }
    else if (page.getOrderBy().equals("3")) {
      if (page.getOrder().equals("asc"))
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByTaskName().asc();
      else {
        taskQuery = (HistoricTaskInstanceQuery)taskQuery.orderByTaskName().desc();
      }

    }

    List histTaskList = taskQuery.list();

    List<HistoricTaskInstance> instanceList = distincHistoryTask(histTaskList);
    if (instanceList != null) {
      totalRecord = instanceList.size();
    }
    BigDecimal b1 = new BigDecimal(totalRecord);
    BigDecimal b2 = new BigDecimal(page.getPageSize());
    totalNum = b1.divide(b2, 0, 0).intValue();

    int num = page.getPageSize() * (page.getCurPageNo() - 1);
    int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
    int endRow = startRow + page.getPageSize();
    if (totalNum == page.getCurPageNo()) {
      endRow = startRow + instanceList.size() % page.getPageSize();
      if (instanceList.size() % page.getPageSize() == 0) {
        endRow = startRow + page.getPageSize();
      }
    }

    int count = 0;
    if ((instanceList != null) && (instanceList.size() > 0)) {
      if (instanceList.size() > startRow)
        instanceList = instanceList.subList(startRow, endRow);
      else {
        instanceList = new ArrayList();
      }
    }
    for (HistoricTaskInstance task : instanceList) {
      StringBuffer operate = new StringBuffer();
      Map item = new HashMap();

      Task currentTask = null;
      String actDefId = task.getProcessDefinitionId();
      if ((actDefId != null) && (!actDefId.equals(""))) {
        ProcessDefDeploy model = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
        if (model != null) {
          Long prcDefId = model.getId();
          String actDefStepId = task.getTaskDefinitionKey();
          try
          {
            currentTask = (Task)this.taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).executionId(task.getExecutionId()).singleResult();
            if (currentTask != null)
              actDefStepId = currentTask.getTaskDefinitionKey();
          }
          catch (Exception e) {
            logger.error(e,e);
          }

          operate.append("<a href='javascript:openMonitorPage(\"").append(actDefId).append("\",\"").append(actDefStepId).append("\",\"").append(prcDefId).append("\",\"").append(task.getId()).append("\",\"").append(task.getProcessInstanceId()).append("\")' class=\"link_btn\">跟踪</a>");
        }
      }
      if ((isCreate == null) || (checkUserProcessOwner(actDefId, task.getProcessInstanceId(), userid)))
      {
        if (isCreate != null) {
          item.put("owner", UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername());
        }
        else if (task.getOwner() != null) {
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
          boolean isOwner = false;
          for (Task item_task : current_tasklist) {
            String assignee = item_task.getAssignee();
            if (assignee != null) {
              if (assignee.equals(userid)) {
                isOwner = true;
                String username = UserContextUtil.getInstance().getUserName(assignee);
                user.append(username).append("<br>");
              } else {
                String username = UserContextUtil.getInstance().getUserName(assignee);
                user.append(username).append("<br>");
              }
            }

            step.append(item_task.getName()).append("<br>");
          }
          item.put("CURR_USER", user.toString());
          item.put("CURR_STEP", step.toString());

          if (!isOwner) {
            ProcessDefMap map = this.processRuntimeExcuteService.getProcessDefMapService().getProcessDefMapModel(task.getProcessDefinitionId());
            boolean isCreateReback = false;

            if ((map != null) && (map.getIsCreaterCancel() != null) && (map.getIsCreaterCancel().equals(SysConst.on)) && 
              (checkUserProcessOwner(actDefId, task.getProcessInstanceId(), userid))) {
              operate.append("<a href=\"###\" onclick=\"reback(").append(task.getProcessInstanceId()).append(")\" class=\"link_btn\">撤销</a>");
              isCreateReback = true;
            }

            ProcessStepMap psm = this.processRuntimeExcuteService.getProcessStepMapService().getProcessDefMapModel(null, task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            if (psm != null)
            {
              if (((psm.getIsReback() == null) || (psm.getIsReback().equals(SysConst.on))) && 
                (currentTask != null) && (currentTask.getOwner().equals(UserContextUtil.getInstance().getCurrentUserId()))) {
                boolean flag = this.processRuntimeExcuteService.checkTaskReadStatus(currentTask);
                if (flag)
                  operate.append("<a href=\"###\" onclick=\"rebackStep(").append(task.getProcessInstanceId()).append(")\" class=\"link_btn\">收回</a>");
                else {
                  operate.append("<span title='该任务当前办理用户已读，无法执行收回操作'>收回</span>");
                }
              }
            }

          }

        }
        else if ((task.getDeleteReason() != null) && (task.getDeleteReason().equals("deleted"))) {
          item.put("CURR_USER", "-");
          item.put("CURR_STEP", "已终止");
        } else {
          item.put("CURR_USER", "-");
          item.put("CURR_STEP", "已归档");
        }

        num++;
        item.put("id", Integer.valueOf(num));

        item.put("OPERATE", operate.toString());
        item.put("actDefId", task.getProcessDefinitionId());
        item.put("actStepDefId", task.getTaskDefinitionKey());
        item.put("instanceId", task.getProcessInstanceId());
        item.put("excutionId", task.getExecutionId());
        item.put("taskId", task.getId());
        count++;
        items.add(item);
      }
    }
    totalNum = b1.divide(b2, 0, 0).intValue();
    if (totalNum == page.getCurPageNo()) {
      endRow = startRow + instanceList.size() % page.getPageSize();
      if (instanceList.size() % page.getPageSize() == 0) {
        endRow = startRow + page.getPageSize();
      }
    }
    total.put("total", Integer.valueOf(count));
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", items);

    JSONArray json = JSONArray.fromObject(total);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  private boolean checkUserProcessOwner(String actDefId, String processInstanceId, String userid)
  {
    boolean isOwner = false;
    String topStepId = this.processRuntimeExcuteService.getProcessTopStepId(actDefId);
    long count = this.historyService.createHistoricTaskInstanceQuery().taskDefinitionKey(topStepId).processInstanceId(processInstanceId).taskOwner(userid).taskAssignee(userid).count();
    if (count > 0L) {
      isOwner = true;
    }
    return isOwner;
  }

  private List<HistoricTaskInstance> distincHistoryTask(List<HistoricTaskInstance> list)
  {
    List instanceList = new ArrayList();
    List newList = new ArrayList();
    int count = 0;
    for (HistoricTaskInstance task : list) {
      if (!instanceList.contains(task.getProcessInstanceId()))
      {
        instanceList.add(task.getProcessInstanceId());
        newList.add(task);
      }
    }
    return newList;
  }

  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
  public void setHistoryService(HistoryService historyService) {
    this.historyService = historyService;
  }
  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }
  public void setProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }
  public void setProcessRuntimeCcDAO(ProcessRuntimeCcDAO processRuntimeCcDAO) {
    this.processRuntimeCcDAO = processRuntimeCcDAO;
  }

  public void setProcessDeploymentService(ProcessDeploymentService processDeploymentService) {
    this.processDeploymentService = processDeploymentService;
  }

  public void setProcessRuntimeExcuteService(ProcessRuntimeExcuteService processRuntimeExcuteService) {
    this.processRuntimeExcuteService = processRuntimeExcuteService;
  }

  public void setProcessRuntimeSendService(ProcessRuntimeSendService processRuntimeSendService) {
    this.processRuntimeSendService = processRuntimeSendService;
  }
  public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
    this.processOpinionService = processOpinionService;
  }

  public void setProcessDefTriggerService(ProcessDefTriggerService processDefTriggerService) {
    this.processDefTriggerService = processDefTriggerService;
  }

  public void setProcessStepSummaryService(ProcessStepSummaryService processStepSummaryService)
  {
    this.processStepSummaryService = processStepSummaryService;
  }

  public ProcessRuntimeSignsService getProcessRuntimeSignsService() {
    return this.processRuntimeSignsService;
  }

  public void setProcessRuntimeSignsService(ProcessRuntimeSignsService processRuntimeSignsService)
  {
    this.processRuntimeSignsService = processRuntimeSignsService;
  }
}