package com.iwork.process.runtime.service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.constant.SysConst;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.impl.EaglesSearchFormDataImpl;
import com.iwork.eaglesearch.model.IndexFormDataModel;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.flow.model.ProcessDefActionset;
import com.iwork.process.definition.flow.service.ProcessDefActionService;
import com.iwork.process.definition.flow.service.ProcessDefMapService;
import com.iwork.process.definition.flow.service.ProcessDefParamService;
import com.iwork.process.definition.flow.service.ProcessDefTriggerService;
import com.iwork.process.definition.step.dao.ProcessStepFormDAO;
import com.iwork.process.definition.step.model.ProcessStepDefbutton;
import com.iwork.process.definition.step.model.ProcessStepForm;
import com.iwork.process.definition.step.model.ProcessStepJstrigger;
import com.iwork.process.definition.step.model.ProcessStepManualJump;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepDIYBtnService;
import com.iwork.process.definition.step.service.ProcessStepFormMapService;
import com.iwork.process.definition.step.service.ProcessStepFormService;
import com.iwork.process.definition.step.service.ProcessStepManualJumpService;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.definition.step.service.ProcessStepScriptTriggerService;
import com.iwork.process.definition.step.service.ProcessStepSysjumpService;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.dao.ProcessRuntimeCcDAO;
import com.iwork.process.runtime.model.ProcessRuCc;
import com.iwork.process.runtime.model.ProcessTaskStatus;
import com.iwork.process.runtime.param.FormParamEntity;
import com.iwork.process.runtime.pvm.impl.execute.PvmDefExecuteEngine;
import com.iwork.process.runtime.pvm.impl.task.PvmProcessTaskEngine;
import com.iwork.process.runtime.util.BuildDataUtil;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.ProcessAPI;

public class ProcessRuntimeExcuteService
{
  public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
  private static final Log log = LogFactory.getLog(ProcessRuntimeExcuteService.class);
  private ProcessEngine processEngine;
  private TaskService taskService;
  private IFormService iformService;
  private RepositoryService repositoryService;
  private RuntimeService runtimeService;
  private ProcessStepFormDAO processStepFormDAO;
  private SysEngineIFormDAO sysEngineIFormDAO;
  private ProcessRuntimeCcDAO processRuntimeCcDAO;
  private ProcessOpinionService processOpinionService;
  private ProcessDeploymentService processDeploymentService;
  private ProcessDefMapService processDefMapService;
  private ProcessDefParamService processDefParamService;
  private ProcessDefActionService processDefActionService;
  private ProcessDefTriggerService processDefTriggerService;
  private ProcessStepFormService processStepFormService;
  private ProcessStepFormMapService processStepFormMapService;
  private ProcessStepManualJumpService processStepManualJumpService;
  private ProcessStepMapService processStepMapService;
  private ProcessStepSysjumpService processStepSysjumpService;
  private ProcessStepTriggerService processStepTriggerService;
  private ProcessStepDIYBtnService processStepDIYBtnService;
  private ProcessStepScriptTriggerService processStepScriptTriggerService;
  private ProcessRuntimeSignsService processRuntimeSignsService;

  public Long initProcessInstanceId(String actDefId, Long proDefId, String createUser)
  {
    Long instanceId = initProcessInstanceId(actDefId, proDefId, createUser, null);
    return instanceId;
  }

  public Long initProcessInstanceId(String actDefId, Long proDefId, String createUser, String formNo)
  {
    Long instanceId = initProcessInstanceId(actDefId, proDefId, createUser, formNo, null);
    return instanceId;
  }

  public Long initProcessInstanceId(String actDefId, Long proDefId, String createUser, String formNo, Map hash)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return 0L;
	  }
		
    Long instanceId = new Long(0L);
    this.runtimeService = this.processEngine.getRuntimeService();
    Map assigneeMappingTable = new HashMap();

    if ((formNo == null) || ("".equals(formNo))) {
      formNo = PvmProcessTaskEngine.getInstance().buildProcessIntanceFormNo(actDefId, createUser);
    }

    String title = PvmProcessTaskEngine.getInstance().buildProcessTaskTitle(actDefId, createUser, formNo, hash);
    assigneeMappingTable.put("PROCESS_TASK_FORMNO", formNo);
    assigneeMappingTable.put("PROCESS_TASK_TITLE", title);
    assigneeMappingTable.put("PROCESS_TASK_OWNER", createUser);
    assigneeMappingTable.put("PROCESS_TASK_PRIORITY", "0");
    assigneeMappingTable.put("PROCESS_INSTANCE_CREATEDATE", UtilDate.getNowdate());
    assigneeMappingTable.put("assignee", createUser);
    if (hash != null) {
      Iterator iter = hash.entrySet().iterator();
      if (iter != null) {
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          Object key = entry.getKey();
          Object val = entry.getValue();
          assigneeMappingTable.put(key.toString(), val);
        }
      }
    }
    ProcessInstance procInstance = this.runtimeService.startProcessInstanceById(actDefId, assigneeMappingTable);
    if (procInstance != null)
    {
      instanceId = Long.valueOf(procInstance.getProcessInstanceId());
      this.runtimeService.setVariable(instanceId.toString(), "PROCESS_INSTANCE_FORMNO", formNo);
      this.runtimeService.setVariable(instanceId.toString(), "PROCESS_TASK_FORMNO", formNo);
      this.runtimeService.setVariable(instanceId.toString(), "PROCESS_TASK_TITLE", title);
      this.runtimeService.setVariable(instanceId.toString(), "PROCESS_INSTANCE_OWNER", createUser);
      this.runtimeService.setVariable(instanceId.toString(), "PROCESS_INSTANCE_CREATEDATE", UtilDate.getNowdate());
    }
    return instanceId;
  }

  public boolean isCheckTask(Long instanceId, String taskId)
  {
    boolean flag = false;
    if (instanceId != null) {
      ProcessInstance obj = (ProcessInstance)this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(instanceId.toString()).singleResult();
      if (obj == null) {
        if (!flag) {
          List list = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(instanceId.toString()).taskId(taskId).list();
          if ((list != null) && (list.size() > 0)) {
            flag = true;
          }

        }

      }
      else
      {
        flag = true;
      }
    }
    return flag;
  }

  public boolean isCheckTaskTrigger(Long instanceId, String actDefStepId, UserContext uc)
  {
    boolean flag = false;
    try {
		if (instanceId != null) {
		  Task task = ProcessAPI.getInstance().newTaskId(instanceId);
		  if ((task != null) && (actDefStepId != null) && 
		    (task.getTaskDefinitionKey().equals(actDefStepId)) && 
		    (uc.get_userModel().getUserid().equals(task.getAssignee()))) {
		    flag = true;
		  }

		}
	} catch (Exception e) {
		
	}

    return flag;
  }

  public Task getProcessTask(Long instanceId)
  {
    if (instanceId.longValue() > 0L) {
      Task task = null;
      List list = this.taskService.createTaskQuery().processInstanceId(String.valueOf(instanceId)).list();
      Iterator localIterator = list.iterator(); if (localIterator.hasNext()) { Task t = (Task)localIterator.next();
        task = t;
      }

      return task;
    }
    return null;
  }

  public Task getProcessTask(String taskId)
  {
    if (!taskId.equals("")) {
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      return task;
    }
    return null;
  }

  public ExecutionEntity getProcessStepId(Long excutionId)
  {
    if (excutionId == null) excutionId = new Long(0L);
    if (this.runtimeService == null)
      this.runtimeService = this.processEngine.getRuntimeService();
    ExecutionEntity exe = (ExecutionEntity)this.runtimeService.createExecutionQuery().executionId(String.valueOf(excutionId)).singleResult();
    return exe;
  }

  public List<ProcessStepForm> getStepFromList(String actDefId, Long prcDefId, String actStepId)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    List list = this.processStepFormDAO.getProcessStepFormList(prcDefId, actDefId, actStepId);
    if ((list == null) || (list.size() == 0))
    {
      String first_stepId = getProcessTopStepId(actDefId);
      if (!first_stepId.equals(actStepId)) {
        list = getStepFromList(actDefId, prcDefId, first_stepId);
      }
    }

    return list;
  }

  public ProcessStepForm getDefProcessStepForm(Long stepformId, List<ProcessStepForm> list)
  {
    ProcessStepForm model = null;

    if ((stepformId == null) || (stepformId.longValue() == 0L)) {
      for (ProcessStepForm temp : list) {
        if ((temp != null) && 
          (temp.getIsDef().equals(new Long(1L)))) {
          model = temp;
          break;
        }
      }
    }
    else {
      for (ProcessStepForm temp : list) {
        if ((temp != null) && 
          (temp.getId().equals(stepformId))) {
          model = temp;
          break;
        }

      }

    }

    if ((model == null) && (list.size() > 0)) {
      model = (ProcessStepForm)list.get(0);
    }
    return model;
  }

  public String iniitFormBindData(Long engineType)
  {
    Connection conn = DBUtil.open();
    Statement stmt = null;
    while (true)
    {
      StringBuffer sql = new StringBuffer();
      List<Long> list = new ArrayList();
      int num = DBUtil.getInt("select count(ID) AS NUM from sys_engine_form_bind where (engine_type = " + engineType + " or engine_type is null) and rownum<10000", "NUM");
      if (num <= 0) break; sql.append("select * from sys_engine_form_bind where (engine_type = ").append(engineType).append("  or engine_type is null) and rownum<10000");
      Long metadataid;
      try { stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql.toString());
        while (rs.next()) {
          Long id = Long.valueOf(rs.getLong("ID"));
          metadataid = Long.valueOf(rs.getLong("METADATAID"));
          Long dataid = Long.valueOf(rs.getLong("DATAID"));

          SysEngineMetadata sem = this.iformService.getSysEngineMetadataDAO().getModel(metadataid);
          if (sem != null) {
            StringBuffer sql1 = new StringBuffer();

            sql1.append("delete from ").append(sem.getEntityname()).append(" where id = ").append(dataid);

            int delNum = DBUtil.executeUpdate(sql1.toString());
            list.add(id);
          } else {
            list.add(id);
          }
        }
      } catch (Exception e)
      {
        e.printStackTrace();
      }

      if (list.size() > 0) {
        try {
          Connection conn1 = DBUtil.open();
          for (Long id : list) {
            String delsql = "delete from sys_engine_form_bind where id = " + id;
            DBUtil.executeUpdate(conn1, delsql);
          }
          conn1.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return "success";
  }

  public String removeInstanceData(String taskId, Long instanceId)
  {
    String msg = "";

    String userid = UserContextUtil.getInstance().getCurrentUserId();
    Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
    if (task != null) {
      boolean isFlag = false;
      if ((task.getOwner() != null) && (task.getOwner().equals(userid))) {
        isFlag = true;
      } else {
        String topStepId = getProcessTopStepId(task.getProcessDefinitionId());
        if ((topStepId != null) && (topStepId.equals(task.getTaskDefinitionKey())) && 
          (task.getAssignee() != null) && (task.getAssignee().equals(userid))) {
          isFlag = true;
        }
      }

      if (isFlag) {
        this.iformService.removeFormData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
        msg = "success";
      } else {
        msg = "error";
      }
    } else {
      msg = "error";
    }
    return msg;
  }

  public String removeSuperSecurityInstanceData(Long instanceId)
  {
    String msg = "";
    boolean isFlag = SecurityUtil.isSuperManager();
    if (isFlag) {
      this.iformService.removeFormData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
      msg = "success";
    } else {
      msg = "error";
    }
    return msg;
  }

  public String getProcessFormTab(Long selectStepFormId, List<ProcessStepForm> formList, Long isTalk)
  {
    StringBuffer html = new StringBuffer();
    String selectcss = "class=PY_tago";
    String css = "class=PY_tagn";
    html.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >").append("\n");
    html.append("    <tr>").append("\n");
    html.append("      <td align=\"right\"  class=PY_tag  valign=\"bottom\" nowrap=\"nowrap\" width='100%'>").append("\n");
    html.append("<UL style=\"text-align:right\" >").append("\n");
    if ((selectStepFormId != null) && (!selectStepFormId.equals(new Long(0L)))) {
      for (ProcessStepForm model : formList) {
        if (model.getFormname() == null) model.setFormname("流程表单");
        if (model.getId().equals(selectStepFormId)) {
          html.append("<LI>").append("\n");
          html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        } else {
          html.append("<LI  onclick='openSubForm(").append(model.getId()).append(")'>");
          html.append("<DIV ").append(css).append(" ><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        }
      }
    }
    else {
      boolean isHaveDef = false;
      for (ProcessStepForm model : formList) {
        if (model.getFormname() == null) model.setFormname("流程表单");
        if (model.getIsDef().longValue() == 1L) {
          isHaveDef = true;
          html.append("<LI>").append("\n");
          html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        } else {
          html.append("<LI  onclick='openSubForm(").append(model.getId()).append(")'>");
          html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          isHaveDef = true;
        }
      }

      if ((!isHaveDef) && (formList.size() > 0)) {
        for (ProcessStepForm model : formList) {
          if (model.getId() == ((ProcessStepForm)formList.get(0)).getId()) {
            html.append("<LI>").append("\n");
            html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          } else {
            html.append("<LI  href='javascript:openSubForm(").append(model.getId()).append(")'>");
            html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          }
        }
      }
    }
    html.append("<LI  onclick='openMonitorPage();'>");
    html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_monitor.gif' style='margin-right:5px;' border='0'>流程跟踪</SPAN></DIV></LI>").append("\n");
    if ((isTalk != null) && (isTalk.equals(new Long(1L)))) {
      html.append("<LI  onclick='openTalkListPage();'>");
      html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_talk.gif' style='margin-right:5px;' border='0'>讨论区</SPAN></DIV></LI>").append("\n");
    }
    html.append("        </UL>");
    html.append("      </td>").append("\n");
    html.append("    </tr>").append("\n");
    html.append("  </table>").append("\n");
    return html.toString();
  }

  public String getProcessFormTab(Long selectStepFormId, List<ProcessStepForm> formList, ProcessTaskStatus processTaskStatus, List<ProcessDefActionset> deflist)
  {
    StringBuffer html = new StringBuffer();
    String selectcss = "class=PY_tago";
    String css = "class=PY_tagn";
    html.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >").append("\n");
    html.append("    <tr>").append("\n");
    html.append("      <td align=\"right\"  class=PY_tag  valign=\"bottom\" nowrap=\"nowrap\" width='100%'>").append("\n");
    html.append("<UL style=\"text-align:right\" >").append("\n");
    if ((selectStepFormId != null) && (!selectStepFormId.equals(new Long(0L)))) {
      for (ProcessStepForm model : formList) {
        if (model.getFormname() == null) model.setFormname("流程表单");
        if (model.getId().equals(selectStepFormId)) {
          html.append("<LI>").append("\n");
          html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        } else {
          html.append("<LI  onclick='openSubForm(").append(model.getId()).append(")'>");
          html.append("<DIV ").append(css).append(" ><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        }
      }
    }
    else {
      boolean isHaveDef = false;
      for (ProcessStepForm model : formList) {
        if (model.getFormname() == null) model.setFormname("流程表单");
        if (model.getIsDef().longValue() == 1L) {
          isHaveDef = true;
          html.append("<LI>").append("\n");
          html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
        } else {
          html.append("<LI  onclick='openSubForm(").append(model.getId()).append(")'>");
          html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          isHaveDef = true;
        }
      }

      if ((!isHaveDef) && (formList.size() > 0)) {
        for (ProcessStepForm model : formList) {
          if (model.getId() == ((ProcessStepForm)formList.get(0)).getId()) {
            html.append("<LI>").append("\n");
            html.append("<DIV ").append(selectcss).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          } else {
            html.append("<LI  href='javascript:openSubForm(").append(model.getId()).append(")'>");
            html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>").append(model.getFormname()).append("</SPAN></DIV></LI>").append("\n");
          }
        }
      }
    }
    html.append("<LI  onclick='openMonitorPage();'>");
    html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_monitor.gif' style='margin-right:5px;' border='0'>流程跟踪</SPAN></DIV></LI>").append("\n");

    if ((processTaskStatus.getTasktype() == 6) || (processTaskStatus.getTasktype() == 5)) {
      if (deflist.size() > 0) {
        ProcessDefActionset processDef = (ProcessDefActionset)deflist.get(1);
        Long isTalk = processDef.getIsTalk();
        if (isTalk.equals(new Long(1L))) {
          html.append("<LI  onclick='openTalkListPage();'>");
          html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_talk.gif' style='margin-right:5px;' border='0'>讨论区</SPAN></DIV></LI>").append("\n");
        }
      }
    } else if (processTaskStatus.getTasktype() == 3) {
      if (deflist.size() > 0) {
        ProcessDefActionset processDef = (ProcessDefActionset)deflist.get(0);
        Long isTalk = processDef.getIsTalk();
        if (isTalk.equals(new Long(1L))) {
          html.append("<LI  onclick='openTalkListPage();'>");
          html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_talk.gif' style='margin-right:5px;' border='0'>讨论区</SPAN></DIV></LI>").append("\n");
        }
      }
    }
    else if ((processTaskStatus.getIsTalk() != null) && (processTaskStatus.getIsTalk().equals(new Long(1L)))) {
      html.append("<LI  onclick='openTalkListPage();'>");
      html.append("<DIV ").append(css).append("><SPAN class=bold><img src='iwork_img/min_talk.gif' style='margin-right:5px;' border='0'>讨论区</SPAN></DIV></LI>").append("\n");
    }

    html.append("        </UL>");
    html.append("      </td>").append("\n");
    html.append("    </tr>").append("\n");
    html.append("  </table>").append("\n");
    return html.toString();
  }

  public String getUrlTypeContext(String url)
  {
    StringBuffer content = new StringBuffer();
    content.append(" <iframe width=\"100%\" height=\"100%\" id=\"urlFrame\"  scrolling='auto' onLoad='iFrameHeight(\"urlFrame\")'  frameborder=\"0\" src=\"").append(url).append("\"></iframe>");
    return content.toString();
  }
  
  public String getProcessPageButton(ProcessTaskStatus processTaskStatus, String actDefId, Long prcDefId, String actStepDefid, String taskId)
  {
	Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
	String bhryfh = config.get("bhryfh")==null?"":config.get("bhryfh").toString();
    StringBuffer pageButton = new StringBuffer();

    if ((processTaskStatus.isModify()) && ((0 == processTaskStatus.getTasktype()) || (7 == processTaskStatus.getTasktype()))) {
      pageButton.append("<a  href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='saveForm()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-save\">保存</a>");
    }

    ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepDefid);

    if ((processTaskStatus.getTasktype() == 0) && (processTaskStatus.isTaskOwner()))
    {
      pageButton.append(getProcessTransBtnLineLayout(processStepMap, actDefId, prcDefId, taskId));
      if (processStepMap != null)
      {
        boolean isBack = false;
        StringBuffer btn = new StringBuffer();

        if ((processStepMap.getIsBack() != null) && (processStepMap.getIsBack().equals(SysConst.on))) {
          isBack = true;

          ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.processEngine.getRepositoryService()).getDeployedProcessDefinition(actDefId);
          ActivityImpl activity = pde.findActivity(actStepDefid);
          if (activity != null) {
            boolean flag = PvmDefExecuteEngine.getInstance().isBackPrevious(activity);
            if (flag) {
              btn.append("<div iconCls=\"icon-previous\" onClick=\"executeBack(1);\">驳回至上一节点</div>");
            }
          }
        }

        if ((processStepMap.getIsBackSrc() != null) && (processStepMap.getIsBackSrc().equals(SysConst.on))) {
          isBack = true;
          btn.append("<div iconCls=\"icon-backhome\"  onClick=\"executeBack(0);\">驳回至起草人</div><div class=\"menu-sep\"></div>");
        }

        if ((processStepMap.getIsBackToBack() != null) && (processStepMap.getIsBackToBack().equals(SysConst.on))) {
          isBack = true;
          btn.append("<div iconCls=\"icon-backhome\"  onClick=\"executeBack(2);\">驳回申请人修改后返回</div><div class=\"menu-sep\"></div>");
        }
       
        if ((processStepMap.getIsBackDiy() != null) && (processStepMap.getIsBackDiy().equals(SysConst.on))) {
          isBack = true;
          btn.append("<div iconCls=\"icon-previous\" onClick=\"executeBackOther();\">驳回其他节点</div>");
        }
        if(bhryfh.equals("1")){
       	 btn.append("<div class=\"menu-sep\"></div><div iconCls=\"icon-backhome\"  onClick=\"executeBackfh();\">点对点驳回</div>");
       }
        if (isBack) {
          pageButton.append("<a href=\"javascript:void(0)\" style=\"margin-left:1px;margin-right:1px\" id=\"mb1\" class=\"easyui-menubutton\" menu=\"#backlist\" iconCls=\"icon-undo\">驳回</a><div id=\"backlist\"  style=\"width:150px;\">\n");
          pageButton.append(btn);
          pageButton.append("</div>\n");
        }

        if ((processStepMap.getIsAddsign() != null) && (processStepMap.getIsAddsign().equals(SysConst.on))) {
          pageButton.append("<a href=\"#\"  style=\"margin-left:1px;margin-right:1px\" onclick='addSign()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-addsign\">加签</a>");
        }

        if ((processStepMap.getIsForward() != null) && (processStepMap.getIsForward().equals(SysConst.on))) {
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='execForward()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-redo\">转发</a>");
        }

        if ((processStepMap.getIsOffline() != null) && (processStepMap.getIsOffline().equals(SysConst.on))) {
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='addOLWin()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\">添加跟踪记录</a>");
        }

        if (((processStepMap.getIsSigns() != null) && (processStepMap.getIsSigns().equals(SysConst.on))) || (processStepMap.getStepType().equals(new Long(2L)))) {
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='sendSigns()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-signs-add\">发送会签</a>");
        }

        if ((processStepMap == null) || (processStepMap.getIsPrint().longValue() == 1L)) {
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='print()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-print\">打印</a>");
        }

      }

    }
    else if ((processTaskStatus.getTasktype() == 2) && (processTaskStatus.isTaskOwner())) {
      pageButton.append("<a  href=\"#\"  style=\"margin-left:1px;margin-right:1px\" onclick='claimTask()' alt=\"由于当前任务由多人处理，执行【领用】操作后，其他办理人则对此任务不可见\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-claim\">领取任务</a>");
    } else if ((processTaskStatus.getTasktype() == 3) && (processTaskStatus.isTaskOwner())) {
      pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='backAddSign()'  class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-undo\">加签完毕</a>");
      ProcessDefActionset model = this.processDefActionService.getProcessDefActionDao().getModel(prcDefId.longValue(), actDefId, "addSign");

      if ((model == null) || (model.getIsPrint().longValue() == 1L)) {
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='print()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-print\">打印</a>");
      }

      if ((model == null) || (model.getIsForward().longValue() == 1L))
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='execForward()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-redo\">转发</a>");
    }
    else if (processTaskStatus.getTasktype() == 6) {
      ProcessDefActionset model = this.processDefActionService.getProcessDefActionDao().getModel(prcDefId.longValue(), actDefId, "formView");

      if ((model == null) || (model.getIsPrint().longValue() == 1L)) {
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='print()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-print\">打印</a>");
      }

      if ((model == null) || (model.getIsForward().longValue() == 1L))
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='executeCC()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-cc\">抄送</a>");
    }
    else if (processTaskStatus.getTasktype() == 5) {
      pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='backAddSign()'  class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-undo\">加签完毕</a>");
      ProcessDefActionset model = this.processDefActionService.getProcessDefActionDao().getModel(prcDefId.longValue(), actDefId, "notice");

      if ((model == null) || (model.getIsOpinion().longValue() == 1L)) {
        pageButton.append("<a href=\"#\" onclick='addAuditOpinion();return false;' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-addopinion\">添加意见</a>");
      }

      if ((model == null) || (model.getIsPrint().longValue() == 1L)) {
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='print()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-print\">打印</a>");
      }

      if ((model == null) || (model.getIsForward().longValue() == 1L)) {
        pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='execForward()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-redo\">转发</a>");
      }
    }
    return pageButton.toString();
  }

  public String getMobileProcessPageButton(ProcessTaskStatus processTaskStatus, String actDefId, String taskId)
  {
    ArrayList btnlist = new ArrayList();
    HashMap hash = null;
    int num = 0;
    ProcessDefDeploy spd = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
    Long prcDefId = spd.getId();
    this.taskService = this.processEngine.getTaskService();
    String userid = UserContextUtil.getInstance().getCurrentUserId();

    Task task = (Task)this.taskService.createTaskQuery().taskAssignee(userid).taskId(taskId).singleResult();

    if (task == null) {
      task = (Task)this.taskService.createTaskQuery().taskCandidateUser(userid).taskId(taskId).singleResult();
      if (task != null)
        processTaskStatus.setTasktype(2);
    }
    else {
      processTaskStatus.setTasktype(0);
    }
    if (task != null) {
      List out = null;
      ExecutionEntity exe = (ExecutionEntity)this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
      RepositoryService rps = this.processEngine.getRepositoryService();
      RepositoryServiceImpl rpsImpl = (RepositoryServiceImpl)rps;
      ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(task.getProcessDefinitionId());
      ActivityImpl activityImpl = pde.findActivity(exe.getActivityId());
      ProcessStepMap processStepMap = null;
      if (activityImpl != null)
      {
        if ((!activityImpl.getId().equals("999999")) && (processTaskStatus.getTasktype() != 2)) {
          List<PvmTransition> out_trans = activityImpl.getOutgoingTransitions();
          if (out_trans != null)
          {
            ProcessStepManualJump psmj;
            if (out_trans.size() == 1) {
              processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityImpl.getId());

              if (processStepMap == null) {
                processStepMap = new ProcessStepMap();
                processStepMap.setActDefId(actDefId);
                processStepMap.setActStepId(activityImpl.getId());
                processStepMap.setPrcDefId(prcDefId);
                processStepMap = this.processStepMapService.initializeMapModel(processStepMap);
              }
              hash = new HashMap();
              hash.put("ID", Integer.valueOf(num));
              if (processStepMap.getStepAbstract() != null)
                hash.put("TITLE", processStepMap.getStepAbstract());
              else {
                hash.put("TITLE", "提交审批");
              }

              hash.put("TYPE", "TRANS");
              hash.put("IS_HIDDEN", Boolean.valueOf(false));
              hash.put("URL_TYPE", "JS");
              hash.put("URL", "javascript:executeHandle()");
              btnlist.add(hash);

              List list = this.processStepManualJumpService.getProcessStepManualJumpList(actDefId, prcDefId, activityImpl.getId());
              for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { psmj = (ProcessStepManualJump)localIterator.next();
                hash = new HashMap();
                hash.put("TITLE", psmj.getMjName());
                hash.put("TYPE", "MJ");
                hash.put("IS_HIDDEN", Boolean.valueOf(false));
                hash.put("URL_TYPE", "JS");
                hash.put("URL", "javascript:executeManualJump(" + psmj.getId() + ",'" + psmj.getMjType() + "','" + psmj.getMjName() + "');");
                btnlist.add(hash);
              }
            }
            else if (out_trans.size() > 1) {
              for (PvmTransition pt : out_trans) {
                ActivityImpl targetActivity = (ActivityImpl)pt.getDestination();
                if (targetActivity != null) {
                  String name = (String)targetActivity.getProperty("name");
                  hash.put("TYPE", "SELECT");
                  hash.put("URL_TYPE", "JS");
                  hash.put("IS_HIDDEN", Boolean.valueOf(false));
                  hash.put("TITLE", new StringBuffer("发送至\"").append(name).append("\""));
                  hash.put("TYPE", "JS");
                  hash.put("URL", "javascript:selectStep(\"" + targetActivity.getId() + "\");");
                  btnlist.add(hash);
                }
              }
            }
          }

        }
        else if (activityImpl.getId().equals("999999")) {
          hash = new HashMap();
          hash.put("TITLE", "完成加签");
          hash.put("TYPE", "ADDSIGN");
          hash.put("IS_HIDDEN", Boolean.valueOf(false));
          hash.put("URL_TYPE", "JS");
          hash.put("URL", "javascript:backAddSign();");
          btnlist.add(hash);
        }

      }

      if ((processTaskStatus.isModify()) && ((0 == processTaskStatus.getTasktype()) || (7 == processTaskStatus.getTasktype()))) {
        hash = new HashMap();

        hash.put("TITLE", "保存");
        hash.put("TYPE", "SAVE");
        hash.put("URL_TYPE", "JS");
        hash.put("IS_HIDDEN", Boolean.valueOf(true));
        hash.put("URL", "javascript:saveForm()");
        btnlist.add(hash);
      }
      if (processStepMap == null) {
        processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityImpl.getId());
      }

      if ((processTaskStatus.getTasktype() == 0) && (processTaskStatus.isTaskOwner())) {
        if (processStepMap != null) {
          if (processStepMap.getIsAddOpinion().longValue() == 1L) {
            hash = new HashMap();
            hash.put("TITLE", "意见审批");
            hash.put("TYPE", "OPTINION");
            hash.put("URL_TYPE", "JS");
            hash.put("IS_HIDDEN", Boolean.valueOf(true));
            hash.put("URL", "javascript:addOpinion()");
            btnlist.add(hash);
          }

          if (processStepMap.getIsAddsign().equals(SysConst.on)) {
            hash = new HashMap();
            hash.put("TITLE", "加签");
            hash.put("TYPE", "ADDSIGN");
            hash.put("IS_HIDDEN", Boolean.valueOf(false));
            hash.put("URL_TYPE", "JS");
            hash.put("URL", "javascript:addSign()");
            btnlist.add(hash);
          }

          if ((processStepMap.getIsBack() != null) && (processStepMap.getIsBack().equals(SysConst.on))) {
            hash = new HashMap();
            hash.put("TITLE", "驳回至申请人");
            hash.put("TYPE", "BACK");
            hash.put("IS_HIDDEN", Boolean.valueOf(false));
            hash.put("URL_TYPE", "JS");
            hash.put("URL", "javascript:executeBack(0)");
            btnlist.add(hash);
            hash = new HashMap();
            hash.put("TITLE", "驳回至发送人");
            hash.put("TYPE", "BACK");
            hash.put("IS_HIDDEN", Boolean.valueOf(false));
            hash.put("URL_TYPE", "JS");
            hash.put("URL", "javascript:executeBack(1)");
            btnlist.add(hash);
          }

          if ((processStepMap.getIsForward() != null) && (processStepMap.getIsForward().equals(SysConst.on))) {
            hash = new HashMap();
            hash.put("TITLE", "转发");
            hash.put("TYPE", "FORWARD");
            hash.put("IS_HIDDEN", Boolean.valueOf(false));
            hash.put("URL_TYPE", "JS");
            hash.put("URL", "javascript:execForward()");
            btnlist.add(hash);
          }
          if ((processStepMap.getIsOffline() != null) && (processStepMap.getIsOffline().equals(SysConst.on))) {
            hash = new HashMap();
            hash.put("TITLE", "离线反馈");
            hash.put("TYPE", "OFFLINE");
            hash.put("IS_HIDDEN", Boolean.valueOf(false));
            hash.put("URL_TYPE", "JS");
            hash.put("URL", "javascript:addOLWin()");
            btnlist.add(hash);
          }

        }

      }
      else if ((processTaskStatus.getTasktype() == 2) && (processTaskStatus.isTaskOwner())) {
        hash = new HashMap();
        hash.put("TITLE", "任务领取");
        hash.put("TYPE", "CLAIM");
        hash.put("URL_TYPE", "JS");
        hash.put("IS_HIDDEN", Boolean.valueOf(false));
        hash.put("URL", "javascript:claimTask()");
        btnlist.add(hash);
      } else if ((processTaskStatus.getTasktype() == 3) && (processTaskStatus.isTaskOwner())) {
        hash = new HashMap();
        hash.put("TITLE", "加签完毕");
        hash.put("TYPE", "ADDSIGN_FINISH");
        hash.put("URL_TYPE", "JS");
        hash.put("IS_HIDDEN", Boolean.valueOf(false));
        hash.put("URL", "javascript:saveForm()");
        btnlist.add(hash);
      }
    }
    StringBuffer jsonHtml = new StringBuffer();
    JSONArray json = JSONArray.fromObject(btnlist);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public String getProcessTransBtnLineLayout(ProcessStepMap stepMap, String actDefId, Long prcDefId, String taskId)
  {
    StringBuffer pageButton = new StringBuffer();
    if ((taskId != null) && (!"0".equals(taskId)))
    {
      this.taskService = this.processEngine.getTaskService();
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      if (task != null) {
        List out = null;
        ExecutionEntity exe = (ExecutionEntity)this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        RepositoryService rps = this.processEngine.getRepositoryService();
        RepositoryServiceImpl rpsImpl = (RepositoryServiceImpl)rps;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(task.getProcessDefinitionId());
        ActivityImpl activityImpl = pde.findActivity(exe.getActivityId());
        if (activityImpl != null) {
          List out_trans = activityImpl.getOutgoingTransitions();
          if (out_trans != null)
          {
            if ((stepMap != null) && (stepMap.getIsTrans().equals(SysConst.on))) {
              if ((stepMap.getStepAbstract() != null) && (!"".equals(stepMap.getStepAbstract())))
                pageButton.append("<a  href=\"#\"  style=\"margin-left:1px;margin-right:1px\" id=\"transBtn\" onclick='executeHandle()' text='Ctrl+Enter' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-trancation\">").append(stepMap.getStepAbstract()).append("</a>");
              else {
                pageButton.append("<a  href=\"#\"  style=\"margin-left:1px;margin-right:1px\" onclick='executeHandle()' text='Ctrl+Enter' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-trancation\">提交</a>");
              }
            }

            List<ProcessStepManualJump> list = this.processStepManualJumpService.getProcessStepManualJumpList(actDefId, prcDefId, activityImpl.getId());
            if ((list != null) && (list.size() > 0))
            {
              for (ProcessStepManualJump psmj : list) {
                pageButton.append("<a  href=\"#\"   style=\"margin-left:1px;margin-right:1px\" id=\"mjBtn").append(psmj.getId()).append("\" plain=\"true\"  onclick=\"executeManualJump(").append(psmj.getId()).append(",'").append(psmj.getMjType()).append("','").append(psmj.getMjName()).append("');\"  class=\"easyui-linkbutton\"  iconCls=\"icon-process-cc\">").append(psmj.getMjName()).append("</a>");
              }
            }
            ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityImpl.getId());

            if (processStepMap == null) {
              processStepMap = new ProcessStepMap();
              processStepMap.setActDefId(actDefId);
              processStepMap.setActStepId(activityImpl.getId());
              processStepMap.setPrcDefId(prcDefId);
              processStepMap = this.processStepMapService.initializeMapModel(processStepMap);
            }

          }

        }

      }

    }

    return pageButton.toString();
  }

  public String getProcessTransItemButton(String actDefId, Long prcDefId, String taskId)
  {
    StringBuffer pageButton = new StringBuffer();
    pageButton.append("<div id=\"trancation\" style=\"width:150px;\">");
    if ((taskId != null) && (!"0".equals(taskId)))
    {
      this.taskService = this.processEngine.getTaskService();
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      if (task != null) {
        List out = null;
        ExecutionEntity exe = (ExecutionEntity)this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        RepositoryService rps = this.processEngine.getRepositoryService();
        RepositoryServiceImpl rpsImpl = (RepositoryServiceImpl)rps;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(task.getProcessDefinitionId());
        ActivityImpl activityImpl = pde.findActivity(exe.getActivityId());

        if (activityImpl != null) {
          List<PvmTransition> out_trans = activityImpl.getOutgoingTransitions();
          if (out_trans != null)
          {
            ProcessStepMap processStepMap;
            if (out_trans.size() == 1) {
              pageButton.append("<div iconCls=\"icon-process-trans\" onClick=\"executeHandle();\">").append("顺序流转").append("</div>");

              List<ProcessStepManualJump> list = this.processStepManualJumpService.getProcessStepManualJumpList(actDefId, prcDefId, activityImpl.getId());
              if ((list != null) && (list.size() > 0)) {
                pageButton.append("<div class=\"menu-sep\"></div>");
              }
              for (ProcessStepManualJump psmj : list) {
                pageButton.append("<div onClick=\"executeManualJump(").append(psmj.getId()).append(",'").append(psmj.getMjType()).append("','").append(psmj.getMjName()).append("');\">").append(psmj.getMjName()).append("</div>");
              }
              processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityImpl.getId());

              if (processStepMap == null) {
                processStepMap = new ProcessStepMap();
                processStepMap.setActDefId(actDefId);
                processStepMap.setActStepId(activityImpl.getId());
                processStepMap.setPrcDefId(prcDefId);
                processStepMap = this.processStepMapService.initializeMapModel(processStepMap);
              }
              pageButton.append("<div class=\"menu-sep\"></div>");
            } else if (out_trans.size() > 1) {
              for (PvmTransition pt : out_trans) {
                ActivityImpl targetActivity = (ActivityImpl)pt.getDestination();
                if ((targetActivity != null) && 
                  (targetActivity.getProperty("name") != null)) {
                  String name = ObjectUtil.getString(targetActivity.getProperty("name"));
                  pageButton.append("<div iconCls=\"icon-process-trans\"  onClick=\"selectStep('").append(targetActivity.getId()).append("');\">发送至\"").append(name).append("\"</div>");
                  pageButton.append("<div class=\"menu-sep\"></div>");
                }
              }

            }

          }

        }

      }

    }

    pageButton.append("</div>");
    return pageButton.toString();
  }

  public String getProcessShortcutsButton(ProcessTaskStatus processTaskStatus, String actDefId, Long prcDefId, String actStepDefId, String taskId)
  {
    StringBuffer pageButton = new StringBuffer();
    pageButton.append("jQuery(document).bind('keydown',function (evt){");
    pageButton.append("\t\tif(evt.ctrlKey&&evt.ShiftKey){").append("\n");
    pageButton.append("\t\t\treturn false;").append("\n");
    pageButton.append("\t\t}").append("\n");

    if ((processTaskStatus.isModify()) && ((0 == processTaskStatus.getTasktype()) || (7 == processTaskStatus.getTasktype()))) {
      pageButton.append("\t\telse if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作").append("\n");
      pageButton.append("\t\t\tsaveForm(); return false;").append("\n");
      pageButton.append("\t\t} ").append("\n");
    }
    ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepDefId);

    if ((processTaskStatus.getTasktype() == 0) && (processTaskStatus.isTaskOwner())) {
      pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==13){  //Ctrl+回车 顺序办理操作").append("\n");
      pageButton.append("\t\t\t\texecuteHandle(); return false;").append("\n");
      pageButton.append("\t\t\t}").append("\n");
      if (processStepMap != null) {
        if (processStepMap.getIsAddOpinion().longValue() == 1L) {
          pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==45){ //Ctrl+insert 添加意见操作").append("\n");
          pageButton.append("\t\t\t\taddAuditOpinion(); return false;").append("\n");
          pageButton.append("\t\t\t}").append("\n");
        }

        if (processStepMap.getIsAddsign().longValue() == 1L) {
          pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==77){ //Ctrl+m 加签操作").append("\n");
          pageButton.append("\t\t\t\taddSign(); return false;").append("\n");
          pageButton.append("\t\t\t}").append("\n");
        }

        if (processStepMap.getIsForward().longValue() == 1L) {
          pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==8){ //Ctrl+backspace 驳回操作").append("\n");
          pageButton.append("\t\t\t\texecuteBack(); return false;").append("\n");
          pageButton.append("\t\t\t}").append("\n");
        }

        if (processStepMap.getIsForward().longValue() == 1L) {
          pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==70){ //Ctrl+f 转发操作").append("\n");
          pageButton.append("\t\t\t\texecForward(); return false;").append("\n");
          pageButton.append("\t\t\t}").append("\n");
        }

        if (processStepMap.getIsTalk().longValue() == 1L) {
          pageButton.append("\t\t\telse if(evt.shiftKey && event.keyCode==84){ //Ctrl+t /发起讨论操作").append("\n");
          pageButton.append("\t\t\t\texecTalk(); return false;").append("\n");
          pageButton.append("\t\t\t}").append("\n");
        }
      }
    } else if ((processTaskStatus.getTasktype() == 2) && (processTaskStatus.isTaskOwner())) {
      pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==13){  //Ctrl+回车 任务领取操作").append("\n");
      pageButton.append("\t\t\t\tclaimTask(); return false;").append("\n");
      pageButton.append("\t\t\t}").append("\n");
    }
    else if ((processTaskStatus.getTasktype() == 3) && (processTaskStatus.isTaskOwner())) {
      pageButton.append("\t\t\t\telse if(evt.ctrlKey && event.keyCode==13){  //Ctrl+回车 加签完毕操作").append("\n");
      pageButton.append("\t\t\t\tbackAddSign(); return false;").append("\n");
      pageButton.append("\t\t\t}").append("\n");
      pageButton.append("\t\t\telse if(evt.ctrlKey && event.keyCode==45){ //Ctrl+insert 添加意见操作").append("\n");
      pageButton.append("\t\t\t\t\taddAuditOpinion(); return false;").append("\n");
      pageButton.append("\t\t\t\t}").append("\n");
    }

    if ((processStepMap != null) && 
      (processStepMap.getIsPrint().longValue() == 1L)) {
      pageButton.append("\t\t\t\telse if(evt.ctrlKey && event.keyCode==80){ //Ctrl+p 打印操作").append("\n");
      pageButton.append("\t\t\t\t\tprint(); return false;").append("\n");
      pageButton.append("\t\t\t\t}").append("\n");
    }

    pageButton.append("}); //快捷键");
    return pageButton.toString();
  }

  public String getProcessExtendsButton(String actDefId, Long prcDefId, String actStepDefid, Long instanceId, String taskId)
  {
    StringBuffer pageButton = new StringBuffer();
    Long taskStatus = ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_FOREVER;
    if (taskId != null) {
      Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
      if (task != null)
        taskStatus = ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_TODO;
      else
        taskStatus = ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_HISTORY;
    }
    else
    {
      taskStatus = ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_FOREVER;
    }
    if (((actStepDefid == null) || (actStepDefid.equals(""))) && 
      (taskId != null)) {
      HistoricTaskInstance hti = (HistoricTaskInstance)this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
      if (hti != null) {
        actStepDefid = hti.getTaskDefinitionKey();
        taskStatus = ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_FOREVER;
      }

    }

    List<ProcessStepDefbutton> diyList = this.processStepDIYBtnService.getList(actDefId, actStepDefid, prcDefId.longValue());

    if (diyList != null) {
      for (ProcessStepDefbutton model : diyList)
      {
        if (model.getPurviewScope() == null) {
          model.setPurviewScope(ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_FOREVER);
        }
        if ((model.getPurviewScope().equals(ProcessStepDefbutton.PURVIEW_SCOPE_TYPE_FOREVER)) || 
          (model.getPurviewScope().equals(taskStatus)))
        {
          if (model.getPermission() != null) {
            boolean flag = PurviewCommonUtil.getInstance().checkUserInPurview(UserContextUtil.getInstance().getCurrentUserId(), model.getPermission());
            if (flag) {
              pageButton.append("<a href=\"").append(model.getUrl()).append("\" ").append(model.getButtonScript()).append(" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"").append(model.getIcon()).append("\">").append(model.getButtonName()).append("</a>");
            }
          }
          else
          {
            pageButton.append("<a href=\"").append(model.getUrl()).append("\" ").append(model.getButtonScript()).append(" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"").append(model.getIcon()).append("\">").append(model.getButtonName()).append("</a>");
          }

        }

      }

    }

    pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='location.reload();' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-reload\">刷新</a>");

    pageButton.append("");
    return pageButton.toString();
  }

  public String getProcessSignsButton(String actDefId, Long prcDefId, String actStepDefid, Long instanceId, String taskId)
  {
    StringBuffer pageButton = new StringBuffer();
    Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
    if ((task != null) && 
      (task.getAssignee() != null) && (task.getAssignee().equals(UserContextUtil.getInstance().getCurrentUserId())))
    {
      pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='finishSigns()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-process-trancation\">完成会签</a>");

      pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='sendSigns()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-signs-look\">查看会签状态</a>");
    }

    pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='location.reload();' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-reload\">刷新</a>");

    pageButton.append("");
    return pageButton.toString();
  }

  public String getProcessDoSignsButton(boolean signsing, String actDefId, Long prcDefId, String actStepDefid, Long instanceId, String taskId, boolean isMobile)
  {
    StringBuffer pageButton = new StringBuffer();
    if (isMobile)
    {
      if (signsing)
        pageButton.append("<li  id=\"signsBtn\"><a href=\"###\"  onclick='sendSigns()'><img src=\"../iwork_img/icon/arrow_first.gif\" class=\"ui-li-icon ui-corner-none\">填写会签意见</a></li>\n");
    }
    else
    {
      if (signsing) {
        int size = this.processRuntimeSignsService.getProcessRuntimeSignsDAO().getUnSignsSize(actDefId, actStepDefid, instanceId, instanceId, Long.valueOf(Long.parseLong(taskId)));
        if (size == 1)
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='doSignsNext()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-edit\">会签完毕</a>");
        else {
          pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='doSigns()' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\">填写会签意见</a>");
        }
      }

      pageButton.append("<a href=\"#\" style=\"margin-left:1px;margin-right:1px\"  onclick='location.reload();' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-reload\">刷新</a>");

      pageButton.append("");
    }
    return pageButton.toString();
  }

  public String getProcessTopStepId(String actDefId)
  {
    String stepId = "";
    RepositoryService rps = this.processEngine.getRepositoryService();
    ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(actDefId);
    List<ActivityImpl> list = pde.getActivities();
    for (ActivityImpl activity : list) {
      boolean flag = false;

      if ((activity.getActivityBehavior() instanceof NoneStartEventActivityBehavior)) {
        List<PvmTransition> startoutlist = activity.getOutgoingTransitions();
        for (PvmTransition pt : startoutlist) {
          ActivityImpl ai = (ActivityImpl)pt.getDestination();
          if (ai != null) {
            stepId = ai.getId();
            flag = true;
            break;
          }
        }
      }
      if (flag)
      {
        break;
      }

    }

    return stepId;
  }

  public ProcessStepJstrigger getProcessStepJsTriggerModel(String actDefId, String actStepId, Long prcDefId)
  {
    ProcessStepJstrigger model = this.processStepScriptTriggerService.getModel(actDefId, actStepId, prcDefId.longValue());
    if (model == null) {
      model = new ProcessStepJstrigger();
      model.setInitJs("return true;");
      model.setSaveJs("return true;");
      model.setTranJs("return true;");
    }
    if ((model.getInitJs() == null) || ("".equals(model.getInitJs()))) {
      model.setInitJs("return true;");
    }
    if ((model.getSaveJs() == null) || ("".equals(model.getSaveJs()))) {
      model.setSaveJs("return true;");
    }

    if ((model.getSaveJs() == null) || ("".equals(model.getSaveJs()))) {
      model.setSaveJs("return true;");
    }
    return model;
  }

  public FormParamEntity initFormParamModel(String actDefId, String actStepId, Long formId, Long taskId, Long instanceId, Long dataid)
  {
    FormParamEntity model = new FormParamEntity();
    if (actDefId != null)
    {
      ProcessDefDeploy spd = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
      model.setActDefId(actDefId);
      model.setPrcDefId(spd.getId());

      if (actStepId == null) {
        String temp = getProcessTopStepId(actDefId);
        if (temp != null) model.setActStepDefId(temp); 
      }
      else { model.setActStepDefId(actStepId); }


      if (formId != null)
        model.setFormId(formId);
      else {
        model.setFormId(new Long(0L));
      }

      if (taskId != null)
        model.setTaskId(taskId);
      else {
        model.setTaskId(new Long(0L));
      }
      if (instanceId != null)
        model.setInstanceId(instanceId);
      else {
        model.setInstanceId(new Long(0L));
      }
      if (dataid != null)
        model.setDataid(dataid);
      else
        model.setDataid(new Long(0L));
    }
    return model;
  }

  public ProcessTaskStatus getTaskStatusInfo(List<ProcessStepForm> formlist, String actDefId, Long prcDefId, String actStepId, Long stepformId, String taskId)
  {
    ProcessTaskStatus task_status = new ProcessTaskStatus();
    try {
		task_status.setTasktype(6);
		task_status.setFormtype(ProcessTaskStatus.PROCESS_FORM_TYPE_IFORM);
		task_status.setModify(false);
		task_status.setFormid(new Long(0L));

		if (formlist != null)
		{
		  ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
		  if (processStepMap != null) {
		    task_status.setIsLog(processStepMap.getIsLog());
		    task_status.setIsTalk(processStepMap.getIsTalk());
		  }

		  ProcessStepForm processStepForm = getDefProcessStepForm(stepformId, formlist);

		  if (processStepForm != null) {
		    task_status.setFormtype(processStepForm.getBindType());
		    task_status.setProcessStepForm(processStepForm);
		    task_status.setFormid(processStepForm.getFormid());

		    if (processStepForm.getIsModify().equals(ProcessStepForm.PRO_STEPFORM_TYPE_MODIFY))
		      task_status.setModify(true);
		    else
		      task_status.setModify(false);
		  }
		  else {
		    task_status.setModify(false);
		  }

		  String userid = UserContextUtil.getInstance().getCurrentUserId();

		  if ((taskId != null) && (!taskId.equals("0")))
		  {
		    Task task=null;
			try {
				task = (Task)this.taskService.createTaskQuery().taskAssignee(userid).taskId(taskId).singleResult();
			} catch (Exception e) {
				log.error("-----------"+userid+"----------"+taskId+"---------------------", e);
			}

		    if ((task != null) && (!actStepId.equals("999999")))
		    {
		      task_status.setTaskOwner(true);
		      task_status.setTasktype(0);
		      boolean flag = checkTaskReadStatus(task);
		      if (flag)
		      {
		        this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_STATUS", ProcessTaskConstant.PROCESS_EXCUTION_READ);

		        this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_TIME", UtilDate.getNowDatetime());
		        task.setDueDate(new Date());
		        this.taskService.saveTask(task);
		      }
		    } else if ((task != null) && (actStepId.equals("999999"))) {
		      task_status.setTaskOwner(true);
		      task_status.setTasktype(3);
		      task_status.setModify(false);
		      boolean flag = checkTaskReadStatus(task);
		      if (flag)
		      {
		        this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_STATUS", ProcessTaskConstant.PROCESS_EXCUTION_READ);

		        this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_TIME", UtilDate.getNowDatetime());
		      }
		    }
		    else {
		      task = (Task)this.taskService.createTaskQuery().taskCandidateUser(userid).taskId(taskId).singleResult();
		      if (task != null)
		      {
		        boolean flag = checkTaskReadStatus(task);
		        if (flag)
		        {
		          this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_STATUS", ProcessTaskConstant.PROCESS_EXCUTION_READ);

		          this.taskService.setVariable(task.getId(), "PROCESS_EXCUTION_READ_TIME", UtilDate.getNowDatetime());
		        }
		        task_status.setTasktype(2);
		        task_status.setTaskOwner(true);
		        task_status.setModify(false);
		      }
		      else if ("999999".equals(actStepId)) {
		        task_status.setTasktype(3);
		        task_status.setModify(false);
		      }
		      else {
		        task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
		        if (task != null) {
		          task_status.setModify(false);
		          task_status.setTasktype(6);
		        }
		      }
		    }

		  }
		  else
		  {
		    task_status.setTaskOwner(true);
		    task_status.setTasktype(7);
		  }
		}
	} catch (Exception e) {
		log.error(e,e);
	}
    return task_status;
  }

  public boolean setCCisRead(Long id)
  {
    boolean flag = false;
    ProcessRuCc model = this.processRuntimeCcDAO.getCCModel(id);
    if ((model != null) && (model.getReadtime() == null)) {
      model.setReadtime(new Date());
      this.processRuntimeCcDAO.update(model);
      flag = true;
    }
    return flag;
  }

  public boolean addTaskFormIndex(String actDefId, Long prcDefId, String actStepId, Long stepformId, String taskId, Long instanceId, Long excutionId, Map params)
  {
    EaglesSearchFormDataImpl eagles_search = (EaglesSearchFormDataImpl)EaglesSearchFactory.getEaglesSearcherImpl("FORMINDEX");
    if (eagles_search != null) {
      IndexFormDataModel ifdm = new IndexFormDataModel();

      ifdm.setId(instanceId.toString());
      ifdm.setInstanceId(instanceId.toString());
      ifdm.setTaskId(taskId);
      ifdm.setExcuteId(excutionId.toString());
      ifdm.setActDefId(actDefId);
      ifdm.setActDefStepId(actStepId);
      ifdm.setStepFormId(stepformId.toString());
      Task task = getProcessTask(taskId);
      if (task != null) {
        String title = task.getDescription();
        if (title == null) title = "";
        ifdm.setTitle(title);
        ifdm.setType("FORMINDEX");
        if (params != null) {
          HashMap tmp = ParameterMapUtil.getParameterMap(params);
          ifdm.setParams(tmp);
        }
        ifdm.setCreateDate(new Date());
        ifdm.setOwner(UserContextUtil.getInstance().getCurrentUserId());
        try {
          eagles_search.addDocument(ifdm);
        }
        catch (Exception localException) {
        }
      }
    }
    return false;
  }

  private boolean isTaskOwner(String taskId, String userid)
  {
    boolean flag = false;

    Task task = (Task)this.taskService.createTaskQuery().taskAssignee(userid).taskId(taskId).singleResult();
    if (task == null)
    {
      task = (Task)this.taskService.createTaskQuery().taskCandidateUser(userid).taskId(taskId).singleResult();
    }
    if (task != null) {
      flag = true;
    }
    return flag;
  }

  public boolean checkTaskReadStatus(Task task)
  {
    boolean flag = false;
    if (task != null) {
      Long status = new Long(0L);
      Object obj = this.taskService.getVariable(task.getId(), "PROCESS_EXCUTION_READ_STATUS");
      if (obj != null) {
        if ((obj instanceof Long))
          status = (Long)obj;
        else if ((obj instanceof Integer))
          status = Long.valueOf(((Integer)obj).longValue());
        else {
          status = ProcessTaskConstant.PROCESS_EXCUTION_UNREAD;
        }
        if (status.equals(ProcessTaskConstant.PROCESS_EXCUTION_UNREAD))
          flag = true;
      }
      else {
        flag = true;
      }
    }
    return flag;
  }

  public String showProcessInstanceJson(String actDefId, Long instanceId, Map params, HttpServletResponse response)
  {
    StringBuffer json = new StringBuffer();
    String msg = "";
    String taskTitle = "";
    List<Long> formList = ProcessAPI.getInstance().getFormIdsByInstanceId(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
    Map root = new HashMap();
    HistoricProcessInstance hpi = (HistoricProcessInstance)this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instanceId.toString()).singleResult();
    if (hpi != null) {
      String actdefId = hpi.getProcessDefinitionId();
      root.put("DE_ACTDEFID", actdefId);
      root.put("BUSINESSKEY", hpi.getBusinessKey());
    } else {
      log.error("未获取到当前任务实例，数据输出错误");
      return null;
    }
    root = BuildDataUtil.encodeData(root);

    if ((formList != null) && (formList.size() > 0)) {
      List formDataList = new ArrayList();
      for (Long formId : formList) {
        Map formData = new HashMap();

        HashMap hm = ProcessAPI.getInstance().getFromData(instanceId, formId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
        formData.put("DE_FORMDATA_MAIN", BuildDataUtil.encodeData(hm));

        List sublist = getSubFormList(formId, instanceId);
        formData.put("DE_FORMDATA_SUB", BuildDataUtil.encodeDataList(sublist));

        formDataList.add(formData);
      }
      root.put("DE_FORMDATA", formDataList);
    }

    List opinionlist = this.processOpinionService.getProcessRuOpinionList(actDefId, instanceId);
    root.put("DE_FORMOPINION", opinionlist);

    JSONArray jsonArray = JSONArray.fromObject(root);
    json.append(jsonArray);
    return json.toString();
  }

  private List<HashMap> getSubFormList(Long formId, Long instanceId)
  {
    List list = new ArrayList();

    List<SysEngineSubform> subformlist = this.iformService.getSysEngineSubformDAO().getDataList(formId);
    for (SysEngineSubform ses : subformlist) {
      HashMap data = new HashMap();
      List subformdata = ProcessAPI.getInstance().getSubFormDataList(formId, instanceId, ses.getSubtablekey());
      data.put("DE_FORMDATA_SUBFORM_TITLE", ses.getTitle());
      data.put("DE_FORMDATA_SUBFORM_KEY", ses.getSubtablekey());
      data.put("DE_FORMDATA_SUBFORM_DATA", subformdata);
      list.add(data);
    }
    return list;
  }
  public ProcessEngine getProcessEngine() {
    return this.processEngine;
  }
  public void setProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }
  public TaskService getTaskService() {
    return this.taskService;
  }
  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
  public RepositoryService getRepositoryService() {
    return this.repositoryService;
  }
  public void setRepositoryService(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }
  public RuntimeService getRuntimeService() {
    return this.runtimeService;
  }
  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  public ProcessStepFormDAO getProcessStepFormDAO()
  {
    return this.processStepFormDAO;
  }
  public void setProcessStepFormDAO(ProcessStepFormDAO processStepFormDAO) {
    this.processStepFormDAO = processStepFormDAO;
  }

  public ProcessDefMapService getProcessDefMapService()
  {
    return this.processDefMapService;
  }

  public void setProcessDefMapService(ProcessDefMapService processDefMapService)
  {
    this.processDefMapService = processDefMapService;
  }

  public ProcessDefParamService getProcessDefParamService()
  {
    return this.processDefParamService;
  }

  public void setProcessDefParamService(ProcessDefParamService processDefParamService)
  {
    this.processDefParamService = processDefParamService;
  }

  public ProcessDefTriggerService getProcessDefTriggerService()
  {
    return this.processDefTriggerService;
  }

  public void setProcessDefTriggerService(ProcessDefTriggerService processDefTriggerService)
  {
    this.processDefTriggerService = processDefTriggerService;
  }

  public ProcessStepFormService getProcessStepFormService()
  {
    return this.processStepFormService;
  }

  public void setProcessStepFormService(ProcessStepFormService processStepFormService)
  {
    this.processStepFormService = processStepFormService;
  }

  public ProcessStepFormMapService getProcessStepFormMapService()
  {
    return this.processStepFormMapService;
  }

  public void setProcessStepFormMapService(ProcessStepFormMapService processStepFormMapService)
  {
    this.processStepFormMapService = processStepFormMapService;
  }

  public ProcessStepManualJumpService getProcessStepManualJumpService()
  {
    return this.processStepManualJumpService;
  }

  public void setProcessStepManualJumpService(ProcessStepManualJumpService processStepManualJumpService)
  {
    this.processStepManualJumpService = processStepManualJumpService;
  }

  public ProcessStepMapService getProcessStepMapService()
  {
    return this.processStepMapService;
  }

  public void setProcessStepMapService(ProcessStepMapService processStepMapService)
  {
    this.processStepMapService = processStepMapService;
  }

  public ProcessStepSysjumpService getProcessStepSysjumpService()
  {
    return this.processStepSysjumpService;
  }

  public void setProcessStepSysjumpService(ProcessStepSysjumpService processStepSysjumpService)
  {
    this.processStepSysjumpService = processStepSysjumpService;
  }

  public ProcessStepTriggerService getProcessStepTriggerService()
  {
    return this.processStepTriggerService;
  }

  public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService)
  {
    this.processStepTriggerService = processStepTriggerService;
  }

  public SysEngineIFormDAO getSysEngineIFormDAO()
  {
    return this.sysEngineIFormDAO;
  }

  public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO)
  {
    this.sysEngineIFormDAO = sysEngineIFormDAO;
  }

  public ProcessRuntimeCcDAO getProcessRuntimeCcDAO() {
    return this.processRuntimeCcDAO;
  }

  public void setProcessRuntimeCcDAO(ProcessRuntimeCcDAO processRuntimeCcDAO) {
    this.processRuntimeCcDAO = processRuntimeCcDAO;
  }

  public ProcessStepDIYBtnService getProcessStepDIYBtnService() {
    return this.processStepDIYBtnService;
  }

  public void setProcessStepDIYBtnService(ProcessStepDIYBtnService processStepDIYBtnService) {
    this.processStepDIYBtnService = processStepDIYBtnService;
  }

  public ProcessStepScriptTriggerService getProcessStepScriptTriggerService() {
    return this.processStepScriptTriggerService;
  }

  public void setProcessStepScriptTriggerService(ProcessStepScriptTriggerService processStepScriptTriggerService) {
    this.processStepScriptTriggerService = processStepScriptTriggerService;
  }

  public ProcessDeploymentService getProcessDeploymentService() {
    return this.processDeploymentService;
  }

  public void setProcessDeploymentService(ProcessDeploymentService processDeploymentService) {
    this.processDeploymentService = processDeploymentService;
  }

  public ProcessDefActionService getProcessDefActionService() {
    return this.processDefActionService;
  }

  public void setProcessDefActionService(ProcessDefActionService processDefActionService)
  {
    this.processDefActionService = processDefActionService;
  }

  public void setIformService(IFormService iformService) {
    this.iformService = iformService;
  }
  public ProcessRuntimeSignsService getProcessRuntimeSignsService() {
    return this.processRuntimeSignsService;
  }

  public void setProcessRuntimeSignsService(ProcessRuntimeSignsService processRuntimeSignsService) {
    this.processRuntimeSignsService = processRuntimeSignsService;
  }
  public ProcessOpinionService getProcessOpinionService() {
    return this.processOpinionService;
  }
  public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
    this.processOpinionService = processOpinionService;
  }
}