package com.iwork.process.tools.processopinion.service;

import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.MobileUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.constant.SysConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.flow.model.ProcessDefActionset;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.tools.processopinion.dao.ProcessOpinionDAO;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.process.tools.processopinion.model.ProcessToolsOpinion;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

public class ProcessOpinionService
{
  private ProcessOpinionDAO processOpinionDAO;
  private ProcessStepMapService processStepMapService;
  private ProcessEngine processEngine;
  private ProcessDeploymentService processDeploymentService;
  
  public List<ProcessRuOpinion> getProcessRuOpinionList(String actDefId, Long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
    Long prcDefId = null;
    ProcessDefDeploy deploy = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
    if (deploy != null) {
      prcDefId = deploy.getId();
    }
    return this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, prcDefId, instanceid.longValue());
  }
  
  public String getProcessInstanceOpinionList(boolean isForm, String actDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    StringBuffer optionList = new StringBuffer();
    Long prcDefId = null;
    ProcessDefDeploy deploy = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
    if (deploy != null) {
      prcDefId = deploy.getId();
    } else {
      return "";
    }
    ProcessStepMap model = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
    if (model != null)
    {
      if (model.getIsDisplayOpinion() == null) {
        model.setIsDisplayOpinion(SysConst.off);
      }
    }
    else if ((actStepId != null) && (actStepId.equals("999999")))
    {
      ProcessDefActionset pda = this.processDeploymentService.getProcessDefActionService().getProcessDefActionDao().getModel(prcDefId.longValue(), actDefId, "addSign");
      if (pda != null)
      {
        if ((pda.getIsOpinion() != null) && (pda.getIsOpinion().equals(SysConst.on))) {
          isForm = false;
        }
      }
      else {
        isForm = false;
      }
    }
    else
    {
      ProcessDefActionset pda = this.processDeploymentService.getProcessDefActionService().getProcessDefActionDao().getModel(prcDefId.longValue(), actDefId, "formView");
      if (pda != null)
      {
        if ((pda.getIsOpinion() != null) && (pda.getIsOpinion().equals(SysConst.on))) {
          isForm = false;
        }
      }
      else {
        isForm = false;
      }
    }
    if ((!isForm) || ((model != null) && (model.getIsDisplayOpinion().equals(new Long(1L)))))
    {
      optionList.append("<div  class=\"opinion_toolbar\">").append("\n");
      optionList.append("<div  style='height:20px;border-left:5px solid #FFCC66;padding:5px;padding-left:10px;' id=\"infotitle\">跟踪记录<font color=\"red\"><s:property value='info' escape='false'/></font></div>").append("\n");
      optionList.append("<table class=\"opinion_tb\"width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">").append("\n");
      optionList.append("<tr>").append("\n");
      optionList.append("<td  class='opinion_title' align='left'>任务名称</td>").append("\n");
      optionList.append("<td  class='opinion_title' style='width:100px'>操作</td>").append("\n");
      optionList.append("<td class='opinion_title' >意见描述</td>").append("\n");
      optionList.append("<td class='opinion_title' style='width:100px'>办理人</td>").append("\n");
      optionList.append("<td class='opinion_title' style='width:100px'>办理时间</td>").append("\n");
      optionList.append("</tr>").append("\n");
      List<ProcessRuOpinion> list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, prcDefId, instanceid);
      String title;
      for (ProcessRuOpinion opinion : list)
      {
        title = "";
        ProcessStepMap ruMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, opinion.getActStepId());
        if (ruMap != null) {
          title = ruMap.getStepTitle();
        } else if ("999999".equals(opinion.getActStepId())) {
          title = "加签";
        } else {
          title = "节点标题未定义";
        }
        optionList.append("<tr>").append("\n");
        optionList.append("<td class='opinion_step'  align='left'>").append(title).append("</td>").append("\n");
        optionList.append("<td  class='opinion_item' ><img src='iwork_img/010538151.gif' style='margin-left:10px;margin-right:2px' border='0'>");
        String resion = "";
        Long status;
        if (opinion.getAction() != null)
        {
          optionList.append(opinion.getAction()).append("&nbsp;");
        }
        else
        {
          String action = "[处理中...]";
          if (opinion.getTaskid() != null)
          {
            Long taskId = opinion.getTaskid();
            status = new Long(0L);
            Object obj = null;
            try
            {
              obj = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_KEY");
            }
            catch (Exception localException) {}
            if (obj != null)
            {
              if ((obj instanceof Long)) {
                status = Long.valueOf(((Long)obj).longValue());
              } else if ((obj instanceof Integer)) {
                status = Long.valueOf(((Integer)obj).longValue());
              }
              if (status.equals(ProcessTaskConstant.PROCESS_TASK_ACTION_SLEEP))
              {
                action = "【休眠中】";
                Object tmp = null;
                try
                {
                  tmp = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_RESION");
                }
                catch (Exception localException1) {}
                if (tmp != null) {
                  resion = tmp.toString();
                }
              }
            }
          }
          optionList.append(action).append("&nbsp;");
        }
        optionList.append("</td>").append("\n");
        optionList.append("<td class='opinion_content' >");
        if (opinion.getContent() != null) {
          if (!resion.equals("")) {
            optionList.append(resion);
          } else {
            optionList.append(opinion.getContent());
          }
        }
        if (opinion.getAttach() != null)
        {
          List<FileUpload> attachList = FileUploadAPI.getInstance().getFileUploads(opinion.getAttach());
          if ((attachList != null) && (attachList.size() > 0)) {
            for (FileUpload fileUpload : attachList) {
              if (fileUpload != null)
              {
                String fileDivId = fileUpload.getFileId();
                optionList.append("<div  id=\"").append(fileDivId).append("\" style=\"vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;\">\n");
                String icon = "iwork_img/attach.png";
                if (fileUpload.getFileSrcName() != null) {
                  icon = FileType.getFileIcon(fileUpload.getFileSrcName());
                }
                optionList.append("<span><a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a></span>\n");
                optionList.append("</div>\n");
              }
            }
          }
        }
        optionList.append("&nbsp;</td>").append("\n");
        
        String userId = opinion.getCreateuser();
        UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
        String username = "";
        if (uc != null) {
          username = uc.get_userModel().getUsername();
        }
        optionList.append("<td class='opinion_item' >").append(username).append("</td>").append("\n");
        optionList.append("<td class='opinion_item' ><span title=\"").append(UtilDate.getDaysBeforeNow(opinion.getCreatetime())).append("\">");
        optionList.append(UtilDate.datetimeFormat(opinion.getCreatetime(), "yyyy-MM-dd HH:mm")).append("</td>").append("\n");
        optionList.append("</span></tr>").append("\n");
      }
      List<Task> tasklist = this.processEngine.getTaskService().createTaskQuery().processDefinitionId(actDefId).processInstanceId(String.valueOf(instanceid)).list();
      if ((tasklist != null) && (tasklist.size() > 0)&&UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid()!=3)
      {
        for (Task task : tasklist)
        {
          optionList.append("<tr >").append("\n");
          optionList.append("<td class='opinion_step'  align='left'>").append(task.getName()).append("</td>").append("\n");
          optionList.append("<td  class='opinion_item' ><img src='iwork_img/aol.png' style='margin-left:10px;margin-right:2px' border='0'>处理中...");
          optionList.append("</td>").append("\n");
          optionList.append("<td class='opinion_content' >");
          
          optionList.append("&nbsp;</td>").append("\n");
          String userId = task.getAssignee();
          String username = "";
          if ((userId != null) && (!"".equals(userId)))
          {
            UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
            if (uc != null) {
              username = uc.get_userModel().getUsername();
            }
          }
          optionList.append("<td class='opinion_item' >").append(username).append("</td>").append("\n");
          optionList.append("<td class='opinion_item' >-</td>").append("\n");
          optionList.append("</span></tr>").append("\n");
        }
      }
      else
      {
    	 String instanceId=instanceid+"";
        Object haiList = ((HistoricActivityInstanceQuery)this.processEngine.getHistoryService().createHistoricActivityInstanceQuery().activityType("receiveTask").processDefinitionId(actDefId).processInstanceId(instanceId).orderByHistoricActivityInstanceStartTime().desc()).list();
        if ((haiList != null) && (((List)haiList).size() > 0))
        {
          HistoricActivityInstance hai = (HistoricActivityInstance)((List)haiList).get(0);
          Execution e = (Execution)this.processEngine.getRuntimeService().createExecutionQuery().executionId(hai.getExecutionId()).singleResult();
          if (e != null)
          {
            optionList.append("<tr >").append("\n");
            optionList.append("<td class='opinion_step'  align='left'>").append(hai.getActivityName()).append("</td>").append("\n");
            optionList.append("<td  class='opinion_item' ><img src='iwork_img/aol.png' style='margin-left:10px;margin-right:2px' border='0'>处理中...");
            optionList.append("</td>").append("\n");
            optionList.append("<td class='opinion_content' >");
            
            optionList.append("&nbsp;</td>").append("\n");
            optionList.append("<td class='opinion_item' >").append("超级管理员").append("</td>").append("\n");
            optionList.append("<td class='opinion_item' >-</td>").append("\n");
            optionList.append("</span></tr>").append("\n");
          }
        }
      }
      optionList.append("</table>").append("\n");
      optionList.append("</div>").append("\n");
    }
    return optionList.toString();
  }
  
  public String getProcessInstanceOpinionStepList(String actDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    List<String> strlist = new ArrayList();
    Long prcDefId = null;
    ProcessDefDeploy deploy = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
    if (deploy != null) {
      prcDefId = deploy.getId();
    } else {
      return "";
    }
    ProcessStepMap model = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
    if ((model != null) && (model.getIsDisplayOpinion().equals(new Long(1L))))
    {
      List<ProcessRuOpinion> list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, prcDefId, instanceid);
      for (ProcessRuOpinion opinion : list)
      {
        String title = "";
        ProcessStepMap ruMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, opinion.getActStepId());
        if (ruMap != null) {
          title = ruMap.getStepTitle();
        } else if ("999999".equals(opinion.getActStepId())) {
          title = "加签";
        } else {
          title = "节点标题未定义";
        }
        strlist.add(opinion.getActStepId());
      }
    }
    JSONArray json = JSONArray.fromObject(strlist);
    return json.toString();
  }
  
  public String getProcessInstanceCurrentStepId(String actDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    List<String> strlist = new ArrayList();
    Long prcDefId = null;
    
    List<Task> tasklist = this.processEngine.getTaskService().createTaskQuery().processDefinitionId(actDefId).processInstanceId(String.valueOf(instanceid)).list();
    if ((tasklist != null) && (tasklist.size() > 0)) {
      for (Task task : tasklist) {
        strlist.add(task.getTaskDefinitionKey());
      }
    }
    JSONArray json = JSONArray.fromObject(strlist);
    return json.toString();
  }
  
  public String getMobileOpinionList(boolean isForm, String actDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    StringBuffer optionList = new StringBuffer();
    Long prcDefId = null;
    ProcessDefDeploy deploy = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
    if (deploy != null) {
      prcDefId = deploy.getId();
    } else {
      return "";
    }
    ProcessStepMap model = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
    if (model != null)
    {
      if (model.getIsDisplayOpinion() == null) {
        model.setIsDisplayOpinion(SysConst.off);
      }
    }
    else if ((actStepId != null) && (actStepId.equals("999999")))
    {
      model = new ProcessStepMap();
      model.setIsDisplayOpinion(SysConst.on);
    }
    if ((!isForm) || ((model != null) && (model.getIsDisplayOpinion().equals(new Long(1L)))))
    {
      optionList.append("<div  class=\"opinion_toolbar\">").append("\n");
      
      optionList.append("<table class=\"opinion_tb\"width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">").append("\n");
      








      List<ProcessRuOpinion> list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, prcDefId, instanceid);
      String title;
      for (ProcessRuOpinion opinion : list)
      {
        title = "";
        ProcessStepMap ruMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, opinion.getActStepId());
        if (ruMap != null) {
          title = ruMap.getStepTitle();
        } else if ("999999".equals(opinion.getActStepId())) {
          title = "加签";
        } else {
          title = "节点标题未定义";
        }
        optionList.append("<tr>").append("\n");
        
        String userId = opinion.getCreateuser();
        UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
        String username = "";
        if (uc != null) {
          username = uc.get_userModel().getUsername();
        }
        String resion = "";
        optionList.append("<tr>").append("\n");
        optionList.append("<td class='opinion_step'  align='left'>");
        optionList.append(title);
        optionList.append("<span style='float:right'>").append("");
        if (opinion.getAction() != null)
        {
          optionList.append(opinion.getAction()).append("&nbsp;");
        }
        else
        {
          String action = "[处理中...]";
          if (opinion.getTaskid() != null)
          {
            Long taskId = opinion.getTaskid();
            Long status = new Long(0L);
            Object obj = null;
            try
            {
              obj = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_KEY");
            }
            catch (Exception localException) {}
            if (obj != null)
            {
              if ((obj instanceof Long)) {
                status = Long.valueOf(((Long)obj).longValue());
              } else if ((obj instanceof Integer)) {
                status = Long.valueOf(((Integer)obj).longValue());
              }
              if (status.equals(ProcessTaskConstant.PROCESS_TASK_ACTION_SLEEP))
              {
                action = "【休眠中】";
                Object tmp = null;
                try
                {
                  tmp = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_RESION");
                }
                catch (Exception localException1) {}
                if (tmp != null) {
                  resion = tmp.toString();
                }
              }
            }
          }
          optionList.append(action).append("&nbsp;");
        }
        optionList.append("</span>");
        optionList.append("</td>").append("\n");
        optionList.append("</tr>").append("\n");
        optionList.append("<tr>").append("\n");
        optionList.append("<td style='padding-left:10px;'align='left'>");
        if (opinion.getContent() != null)
        {
          optionList.append("<div class='opinionContent'>");
          if (!resion.equals("")) {
            optionList.append(resion);
          } else {
            optionList.append(opinion.getContent());
          }
          optionList.append("</div>");
        }
        optionList.append("</td>").append("\n");
        optionList.append("</tr>").append("\n");
        optionList.append("<tr>").append("\n");
        optionList.append("<td class=\"hr\"><img src='iwork_img/010538151.gif' style='margin-left:10px;margin-right:2px' border='0'>");
        optionList.append(username).append("&nbsp;&nbsp;&nbsp;");
        optionList.append("<span title=\"").append(UtilDate.getDaysBeforeNow(opinion.getCreatetime())).append("\">");
        optionList.append(UtilDate.datetimeFormat(opinion.getCreatetime(), "yyyy-MM-dd HH:mm")).append("</span></td>").append("\n");
        optionList.append("</tr>").append("\n");
      }
      List<Task> tasklist = this.processEngine.getTaskService().createTaskQuery().processDefinitionId(actDefId).processInstanceId(String.valueOf(instanceid)).list();
      if ((tasklist != null) && (tasklist.size() > 0)) {
        for (Task task : tasklist)
        {
          optionList.append("<tr >").append("\n");
          String userId = task.getAssignee();
          String username = "";
          if ((userId != null) && (!"".equals(userId)))
          {
            UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
            if (uc != null) {
              username = uc.get_userModel().getUsername();
            }
          }
          optionList.append("<td class='opinion_step'  align='left'>").append(task.getName()).append("\n");
          optionList.append("<span style='float:right'><img src='iwork_img/010538151.gif' style='margin-left:10px;margin-right:2px' border='0'>处理中...");
          optionList.append("</span>").append("\n");
          optionList.append("</span></td></tr>").append("\n");
          optionList.append("<tr >").append("\n");
          optionList.append("<td class=\"hr\">");
          optionList.append(username).append("");
          optionList.append("</span></td>").append("\n");
          optionList.append("</tr>").append("\n");
        }
      }
      optionList.append("</table>").append("\n");
      optionList.append("</div>").append("\n");
    }
    return optionList.toString();
  }
  
  public String getProcessInstanceOpinionTimerLineList(String actDefId, long prcDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    StringBuffer optionList = new StringBuffer();
    

    List<ProcessRuOpinion> list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, Long.valueOf(prcDefId), instanceid);
    if ((list == null) || (list.size() == 0))
    {
      optionList.append("<tr>").append("\n");
      optionList.append("<td  class='opinion_title' style='text-align:left' colspan=\"5\">[空]</td>").append("\n");
      optionList.append("</tr>").append("\n");
    }
    for (ProcessRuOpinion opinion : list)
    {
      String title = "";
      ProcessStepMap ruMap = this.processStepMapService.getProcessDefMapModel(Long.valueOf(prcDefId), actDefId, opinion.getActStepId());
      if (ruMap != null) {
        title = ruMap.getStepTitle();
      } else if ("999999".equals(opinion.getActStepId())) {
        title = "加签";
      } else {
        title = "节点标题未定义";
      }
      String userId = opinion.getCreateuser();
      UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
      String username = "";
      if (uc != null) {
        username = uc.get_userModel().getUsername();
      }
      optionList.append("<div class=\"item\">").append("\n");
      optionList.append("<h3> <span class=\"fl\">").append(title).append("</span><span class=\"fr\">");
      optionList.append(username).append("</span></h3>").append("\n");
      String action = "[处理中...]";
      String content = "";
      String resion = "";
      if (opinion.getAction() != null)
      {
        action = "[" + opinion.getAction() + "]";
      }
      else if (opinion.getTaskid() != null)
      {
        Long taskId = opinion.getTaskid();
        Long status = new Long(0L);
        Object obj = null;
        try
        {
          obj = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_KEY");
        }
        catch (Exception localException) {}
        if (obj != null)
        {
          if ((obj instanceof Long)) {
            status = Long.valueOf(((Long)obj).longValue());
          } else if ((obj instanceof Integer)) {
            status = Long.valueOf(((Integer)obj).longValue());
          }
          if (status.equals(ProcessTaskConstant.PROCESS_TASK_ACTION_SLEEP))
          {
            action = "【休眠中】";
            Object tmp = null;
            try
            {
              tmp = this.processEngine.getTaskService().getVariable(taskId.toString(), "PROCESS_TASK_ACTION_STATUS_RESION");
            }
            catch (Exception localException1) {}
            if (tmp != null) {
              resion = tmp.toString();
            }
          }
        }
      }
      if (opinion.getContent() != null) {
        if (resion.equals("")) {
          content = opinion.getContent();
        } else {
          content = resion;
        }
      }
      optionList.append("    <p class=\"con\"><span >").append(action).append("</span>").append(content).append("</p>");
      optionList.append("     <div align=\"right\" class=\"memoinfo\">").append(UtilDate.datetimeFormat(opinion.getCreatetime())).append("</div>");
      optionList.append("    </div>");
    }
    return optionList.toString();
  }
  
  public String getMobileProcessInstanceOpinionList(String actDefId, long prcDefId, String actStepId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    StringBuffer optionList = new StringBuffer();
    ProcessStepMap model = this.processStepMapService.getProcessDefMapModel(Long.valueOf(prcDefId), actDefId, actStepId);
    if ((model == null) || (model.getIsDisplayOpinion().equals(new Long(1L))))
    {
      optionList.append("<div  class=\"opinion_toolbar\">").append("\n");
      
      optionList.append("<ul data-role=\"listview\" data-inset=\"true\">").append("\n");
      optionList.append("<li data-role=\"list-divider\">跟踪记录</li>").append("\n");
      






      List<ProcessRuOpinion> list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, Long.valueOf(prcDefId), instanceid);
      if ((list == null) || (list.size() == 0))
      {
        optionList.append("<tr>").append("\n");
        optionList.append("<td  class='opinion_title' style='text-align:left' >[空]</td>").append("\n");
        optionList.append("</tr>").append("\n");
      }
      for (ProcessRuOpinion opinion : list)
      {
        String title = "";
        ProcessStepMap ruMap = this.processStepMapService.getProcessDefMapModel(Long.valueOf(prcDefId), actDefId, opinion.getActStepId());
        if (ruMap != null) {
          title = ruMap.getStepTitle();
        } else if ("999999".equals(opinion.getActStepId())) {
          title = "加签";
        } else {
          title = "节点标题未定义";
        }
        optionList.append("<li>").append("\n");
        optionList.append("<p><strong>").append(title).append("</strong>").append("\n");
        String action = "";
        if (opinion.getAction() != null) {
          action = opinion.getAction();
        } else {
          action = "[处理中...]";
        }
        optionList.append(" &nbsp;&nbsp;&nbsp;[").append(action).append("]</p>");
        if (opinion.getContent() != null) {
          optionList.append("<p>").append(opinion.getContent()).append("</p>").append("\n");
        }
        String userId = opinion.getCreateuser();
        UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
        String username = "";
        if (uc != null) {
          username = uc.get_userModel().getUsername();
        }
        optionList.append(" <p class=\"ui-li-aside\">").append(username).append("&nbsp;<strong><span title=\"").append(UtilDate.datetimeFormat(opinion.getCreatetime())).append("\">");
        optionList.append(UtilDate.getDaysBeforeNow(opinion.getCreatetime())).append("</span></strong></p>").append("\n");
        optionList.append("</li>").append("\n");
      }
      optionList.append("</ul>").append("\n");
      optionList.append("</div>").append("\n");
    }
    return optionList.toString();
  }
  
  public String getProcessInstanceOpinionJson(String actDefId, long prcDefId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
    List list = this.processOpinionDAO.getProcessInstanceOpinionList(actDefId, Long.valueOf(prcDefId), instanceid);
    return "";
  }
  
  public List<ProcessToolsOpinion> loadUserDefinedOpinions()
  {
    List<ProcessToolsOpinion> opinions = new ArrayList();
    opinions = this.processOpinionDAO.loadUserDefinedOpinions();
    if (opinions == null)
    {
      loadDefaultOpinions();
      opinions = this.processOpinionDAO.loadUserDefinedOpinions();
    }
    return opinions;
  }
  
  public void loadDefaultOpinions()
  {
    this.processOpinionDAO.loadDefaultOpinions();
  }
  
  public String IsOpinExist(String opin)
  {
    return this.processOpinionDAO.IsOpinExist(opin);
  }
  
  public void exchangeOrderIndex(long index1, long index2)
  {
    this.processOpinionDAO.exchangeOrderIndex(index1, index2);
  }
  
  public void MoveTop(long index1, long index2)
  {
    this.processOpinionDAO.MoveTop(index1, index2);
  }
  
  public void MoveLow(long index1, long index2)
  {
    this.processOpinionDAO.MoveLow(index1, index2);
  }
  
  public String getUserOpinion(String userid, String actDefId, long proDefId, String actStepId, long instanceid, String taskid, long excutionid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return null;
		 }
    String userOpin = this.processOpinionDAO.getUserOpinion(userid, actDefId, proDefId, actStepId, instanceid, taskid, excutionid);
    return userOpin;
  }
  
  public void addOpinion(String addopin)
  {
    this.processOpinionDAO.addOpinion(addopin);
  }
  
  public void sendOpinion(ProcessRuOpinion model)
  {
    if (model != null)
    {
      ActionContext actionContext = ActionContext.getContext();
      HttpServletRequest request = (HttpServletRequest)actionContext.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String ipaddress = request.getRemoteAddr();
      model.setIpaddress(ipaddress);
    }
    ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(model.getPrcDefId(), model.getActDefId(), model.getActStepId());
    String StepName = "";
    if ((processStepMap == null) || ("".equals(processStepMap.getStepTitle())))
    {
      RepositoryService repositoryService = this.processEngine.getRepositoryService();
      ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(model.getActDefId());
      ActivityImpl activity = pde.findActivity(model.getActStepId());
      if (activity != null) {
        if (activity.getId().equals("999999")) {
          StepName = "加签";
        } else if (activity.getProperty("name") != null) {
          StepName = activity.getProperty("name").toString();
        } else {
          StepName = "未知";
        }
      }
      model.setPrcDefId(model.getPrcDefId());
    }
    else if (model.getAction() == null)
    {
      StepName = processStepMap.getStepTitle();
      model.setAction(StepName);
    }
    if (model.getId() == null)
    {
      this.processOpinionDAO.addRowData(model);
    }
    else
    {
      model.setCreatetime(new Date());
      this.processOpinionDAO.updateRowData(model);
    }
  }
  
  public void sendAction(String action, String actDefId, Long prcDefId, String actStepId, Long instanceId, String taskId, Long executionid, String content)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return ;
	  }
	  if( !DBUTilNew.validActStepId(actStepId) ){
		  return ;
		 }
    String userid = UserContextUtil.getInstance().getCurrentUserId();
    if ((userid == null) || (userid == UserContextUtil.FREE_ACCOUNT)) {
      userid = "ADMIN";
    }
    if ((prcDefId == null) && (actDefId != null))
    {
      ProcessDefDeploy pdd = this.processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
      if (pdd != null) {
        prcDefId = pdd.getId();
      }
    }
    ProcessRuOpinion obj = this.processOpinionDAO.getProData(userid, actDefId, prcDefId, actStepId, instanceId, taskId, executionid);
    ActionContext actionContext = ActionContext.getContext();
    String ipaddress = null;
    if (actionContext != null)
    {
      HttpServletRequest request = (HttpServletRequest)actionContext.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      ipaddress = request.getRemoteAddr();
    }
    if (obj == null)
    {
      obj = new ProcessRuOpinion();
      obj.setActDefId(actDefId);
      obj.setAction(action);
      obj.setCreateuser(userid);
      obj.setCreatetime(new Date());
      obj.setActStepId(actStepId);
      obj.setPrcDefId(prcDefId);
      if (taskId != null) {
        obj.setTaskid(Long.valueOf(Long.parseLong(taskId)));
      }
      obj.setExecutionid(executionid);
      obj.setInstanceid(instanceId);
      obj.setIpaddress(ipaddress);
      if (content != null)
      {
        String opinion = content + MobileUtil.getInstance().getFromTxt();
        obj.setContent(opinion);
      }
      else
      {
        obj.setContent(MobileUtil.getInstance().getFromTxt());
      }
      this.processOpinionDAO.addRowData(obj);
    }
    else
    {
      obj.setCreatetime(new Date());
      obj.setAction(action);
      obj.setIpaddress(ipaddress);
      if (content != null)
      {
        String opinion = content + MobileUtil.getInstance().getFromTxt();
        obj.setContent(opinion);
      }
      else
      {
        obj.setContent(MobileUtil.getInstance().getFromTxt());
      }
      this.processOpinionDAO.updateRowData(obj);
    }
  }
  
  public void modOpinion(int modindex, String newopin)
  {
    this.processOpinionDAO.modOpinion(modindex, newopin);
  }
  
  public void delOpinion(Long delindex)
  {
    this.processOpinionDAO.delOpinion(delindex);
  }
  
  public ProcessOpinionDAO getProcessOpinionDAO()
  {
    return this.processOpinionDAO;
  }
  
  public void setProcessOpinionDAO(ProcessOpinionDAO processOpinionDAO)
  {
    this.processOpinionDAO = processOpinionDAO;
  }
  
  public ProcessStepMapService getProcessStepMapService()
  {
    return this.processStepMapService;
  }
  
  public void setProcessStepMapService(ProcessStepMapService processStepMapService)
  {
    this.processStepMapService = processStepMapService;
  }
  
  public ProcessEngine getProcessEngine()
  {
    return this.processEngine;
  }
  
  public void setProcessEngine(ProcessEngine processEngine)
  {
    this.processEngine = processEngine;
  }
  
  public void setProcessDeploymentService(ProcessDeploymentService processDeploymentService)
  {
    this.processDeploymentService = processDeploymentService;
  }
}
