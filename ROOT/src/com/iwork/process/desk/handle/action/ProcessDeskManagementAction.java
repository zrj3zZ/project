package com.iwork.process.desk.handle.action;

import com.iwork.app.login.control.LoginContext;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.desk.constant.ProcessDeskManagementConstant;
import com.iwork.process.desk.handle.service.ProcessDeskManagementService;
import com.iwork.process.managementcenter.util.PageBean;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

public class ProcessDeskManagementAction extends ActionSupport
{
  private TaskService taskService;
  private HistoryService historyService;
  private ProcessDeskManagementService processDeskManagementService;
  private int currTodoPage;
  private int totalTodoPage;
  private int currTaskPage;
  private int totalTaskPage;
  private int currHistPage;
  private int totalHistPage;
  private int perTodoPage = 10;
  private int perTaskPage = 10;
  private int perHistPage = 20;
  private PageBean todoListBean;
  private PageBean taskListBean;
  private PageBean histListBean;
  private Long taskId;
  private Long instanceId;
  private String userId;
  private String title;
  private String currentUser;
  private String[] remindType;
  private String readStatus;
  private String tablist;
  private int pagetype;
  private String resion;
  private String taskOwner_query;
  private String taskName_query;
  private String creatTimeStart_query;
  private String creatTimeEnd_query;
  private String actDefkey;
  private HashMap remindTypeList;
  protected Page page = new Page();

  public String pageIndex() { return "success"; }


  public String index()
  {
    this.tablist = this.processDeskManagementService.getDeskTypeTab(ProcessDeskManagementConstant.PROCESS_TASK_TYPE_TODOLIST);
    return "todo";
  }

  public String loadTodoJson()
  {
    String taskOwner_query = getTaskOwner_query();
    String json = "";
    if (this.pagetype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_TODOLIST)
      json = this.processDeskManagementService.getTodoListJson(this.page, taskOwner_query, this.taskName_query);
    else if (this.pagetype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_NOTICE)
      json = this.processDeskManagementService.getNoticeListJson(this.page, this.actDefkey, taskOwner_query, this.taskName_query);
    else if (this.pagetype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_PROCESSING)
      json = this.processDeskManagementService.getProcessingTaskJson(this.page, taskOwner_query, this.taskName_query);
    else if (this.pagetype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_HISTORY)
      json = this.processDeskManagementService.getHistoryListJson(this.page, this.actDefkey, taskOwner_query, this.taskName_query);
    else if (this.pagetype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_CREATE) {
      json = this.processDeskManagementService.getCreatProcessJson(this.page, this.taskName_query);
    }

    String temp = json.substring(1, json.length() - 1);
    ResponseUtil.write(temp);
    return null;
  }

  public String taskProcessing()
  {
    this.tablist = this.processDeskManagementService.getDeskTypeTab(ProcessDeskManagementConstant.PROCESS_TASK_TYPE_PROCESSING);
    return "prcessing";
  }

  public String createProcess()
  {
    this.tablist = this.processDeskManagementService.getDeskTypeTab(ProcessDeskManagementConstant.PROCESS_TASK_TYPE_CREATE);
    return "create";
  }

  public String notice()
  {
    this.tablist = this.processDeskManagementService.getDeskTypeTab(ProcessDeskManagementConstant.PROCESS_TASK_TYPE_NOTICE);
    return "notice";
  }

  public String taskHistory()
  {
    this.tablist = this.processDeskManagementService.getDeskTypeTab(ProcessDeskManagementConstant.PROCESS_TASK_TYPE_HISTORY);
    return "success";
  }

  public String loadHistoryJson()
  {
    String json = this.processDeskManagementService.getHistoryListJson(this.page, this.actDefkey, this.taskOwner_query, this.taskName_query);
    String temp = json.substring(1, json.length() - 1);
    ResponseUtil.write(temp);

    return null;
  }

  public PageBean todoList()
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

    this.totalTodoPage = PageBean.countTotalPage(this.perTodoPage, totalRows);
    if (this.currTodoPage == 0) {
      setCurrTodoPage(1);
    }
    int offset = PageBean.countOffset(this.perTodoPage, this.currTodoPage);
    int length = this.perTodoPage;
    int currentPage = PageBean.countCurrentPage(this.currTodoPage);
    if (totalRows <= length) {
      length = totalRows;
    }

    List list = userTaskList.subList(offset, offset + length);

    PageBean pageBean = new PageBean();
    pageBean.setPageSize(this.perTodoPage);
    pageBean.setCurrentPage(currentPage);
    pageBean.setTotalRows(totalRows);
    pageBean.setTotalPages(this.totalTodoPage);
    pageBean.setList(list);
    pageBean.init();

    return pageBean;
  }

  public PageBean taskList()
  {
    List userTaskList = new ArrayList();
    LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get("LOGINCONTEXT");
    userTaskList = ((TaskQuery)this.taskService.createTaskQuery().taskAssignee(lc.getUid()).orderByTaskCreateTime().desc()).list();

    int totalRows = userTaskList.size();
    this.totalTaskPage = PageBean.countTotalPage(this.perTaskPage, totalRows);
    if (this.currTaskPage == 0) {
      setCurrTaskPage(1);
    }
    int offset = PageBean.countOffset(this.perTaskPage, this.currTaskPage);
    int length = this.perTaskPage;
    int currentPage = PageBean.countCurrentPage(this.currTaskPage);
    if (totalRows <= length) {
      length = totalRows;
    }

    int nextRows = offset + length;
    if (nextRows >= totalRows) {
      nextRows = totalRows;
    }
    List list = userTaskList.subList(offset, nextRows);

    PageBean pageBean = new PageBean();
    pageBean.setPageSize(this.perTaskPage);
    pageBean.setCurrentPage(currentPage);
    pageBean.setTotalRows(totalRows);
    pageBean.setTotalPages(this.totalTaskPage);
    pageBean.setList(list);
    pageBean.init();

    return pageBean;
  }

  public PageBean histList()
  {
    List histTaskList = new ArrayList();
    LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get("LOGINCONTEXT");

    histTaskList = ((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskAssignee(lc.getUid()).orderByProcessDefinitionId().desc()).list();

    int totalRows = histTaskList.size();

    this.totalHistPage = PageBean.countTotalPage(this.perHistPage, totalRows);
    if (this.currHistPage == 0) {
      setCurrHistPage(1);
    }
    int offset = PageBean.countOffset(this.perHistPage, this.currHistPage);
    int length = this.perHistPage;
    int currentPage = PageBean.countCurrentPage(this.currHistPage);
    if (totalRows <= length) {
      length = totalRows;
    }

    int nextRows = offset + length;
    if (nextRows >= totalRows) {
      nextRows = totalRows;
    }
    List list = histTaskList.subList(offset, nextRows);

    PageBean pageBean = new PageBean();
    pageBean.setPageSize(this.perHistPage);
    pageBean.setCurrentPage(currentPage);
    pageBean.setTotalRows(totalRows);
    pageBean.setTotalPages(this.totalHistPage);
    pageBean.setList(list);
    pageBean.init();

    return pageBean;
  }

  public void claimTask()
  {
    boolean flag = false;
    String msg = "";
    if (this.taskId != null) {
      if (this.userId == null) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        this.userId = uc.get_userModel().getUserid();
      }
      flag = this.processDeskManagementService.claimTask(this.taskId, this.userId);
    }
    if (flag)
      msg = "success";
    else {
      msg = "error";
    }

    ResponseUtil.write(msg);
  }

  public void closeTask()
  {
    boolean flag = false;
    String msg = "";
    if (this.taskId != null) {
      flag = this.processDeskManagementService.closeTask(String.valueOf(this.taskId));
    }
    if (flag)
      msg = "success";
    else {
      msg = "error";
    }

    ResponseUtil.write(msg);
  }

  public void sleepTask()
  {
    boolean flag = false;
    String msg = "";
    if (this.taskId != null) {
      if (this.userId == null) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        this.userId = uc.get_userModel().getUserid();
      }
      flag = this.processDeskManagementService.waitTask(String.valueOf(this.taskId), this.userId, true, this.resion);
    }
    if (flag)
      msg = "success";
    else {
      msg = "error";
    }

    ResponseUtil.write(msg);
  }

  public void activeTask()
  {
    boolean flag = false;
    String msg = "";
    if (this.taskId != null) {
      if (this.userId == null) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        this.userId = uc.get_userModel().getUserid();
      }
      flag = this.processDeskManagementService.waitTask(String.valueOf(this.taskId), this.userId, false, null);
    }
    if (flag)
      msg = "success";
    else {
      msg = "error";
    }

    ResponseUtil.write(msg);
  }

  public void deleteTask()
  {
    boolean flag = false;
    String msg = "";
    if (this.taskId != null) {
    	if(yz(this.taskId)){
    		 this.userId = UserContextUtil.getInstance().getCurrentUserId();
    	      flag = this.processDeskManagementService.deleteTask(String.valueOf(this.taskId), this.userId);
    	}
     
    }
    if (flag)
      msg = "success";
    else {
      msg = "error";
    }

    ResponseUtil.write(msg);
  }
  private static final String GG_UUID = "1dfe958166a347188339af1337e25fb7";
  public boolean yz(Long taskid) {
		boolean flag = true;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long roleid=uc.get_userModel().getOrgroleid();
		if(roleid==3){
			Map map =new HashMap();
			map.put(1, taskid);
			String sql="SELECT  PROC_INST_ID_ FROM PROCESS_HI_TASKINST WHERE ID_=? ";
			String insid=DBUTilNew.getDataStr("PROC_INST_ID_", sql, map);
			if(insid!=null &&!"".equals(insid)){
				int count=DBUTilNew.getInt("cn", "select count(*) cn from bd_meet_qtggzl s where s.lcbs="+insid, null);
                int rc=DBUTilNew.getInt("cn", "select count(*) cn from rcywcb s where s.instanceid="+insid, null);
				if(count!=0 || rc!=0){
					flag = false;
				}
			}
		}
		
		return flag;
	}
  
  

  public void showTaskCount()
  {
    String json = this.processDeskManagementService.getTaskNoticeNum();
    ResponseUtil.write(json);
  }

  public String showReback()
  {
    if (this.instanceId != null) {
      Task task = this.processDeskManagementService.getCurrentTask(this.instanceId);
      if (task == null) {
        return "error";
      }
      this.taskId = Long.valueOf(Long.parseLong(task.getId()));
      this.title = task.getDescription();
      String userid = task.getAssignee();
      if (userid != null) {
        this.currentUser = UserContextUtil.getInstance().getFullUserAddress(userid);
      }

      this.remindTypeList = this.processDeskManagementService.getProcessRuntimeOperateService().getTaskRemindCheckBox(task.getProcessDefinitionId(), null, task.getTaskDefinitionKey());
      boolean flag = this.processDeskManagementService.getProcessRuntimeExcuteService().checkTaskReadStatus(task);
      if (flag)
        this.readStatus = "未读";
      else {
        this.readStatus = "已读";
      }
      return "success";
    }

    return "error";
  }

  public void doReback()
  {
    if ((this.taskId != null) && (this.resion != null)) {
      boolean flag = this.processDeskManagementService.doReback(this.taskId.toString(), this.resion, this.remindType);
      if (flag)
        ResponseUtil.write("success");
      else
        ResponseUtil.write("error");
    }
  }

  public String showStepReback()
  {
    if (this.instanceId != null) {
      Task task = this.processDeskManagementService.getCurrentTask(this.instanceId);
      if (task == null) {
        return "error";
      }
      this.taskId = Long.valueOf(Long.parseLong(task.getId()));
      this.title = task.getDescription();
      String userid = task.getAssignee();
      if (userid != null) {
        this.currentUser = UserContextUtil.getInstance().getFullUserAddress(userid);
      }

      this.remindTypeList = this.processDeskManagementService.getProcessRuntimeOperateService().getTaskRemindCheckBox(task.getProcessDefinitionId(), null, task.getTaskDefinitionKey());
      boolean flag = this.processDeskManagementService.getProcessRuntimeExcuteService().checkTaskReadStatus(task);
      if (flag)
        this.readStatus = "未读";
      else {
        this.readStatus = "已读";
      }
      return "success";
    }

    return "error";
  }

  public void doStepReback()
  {
    if ((this.taskId != null) && (this.resion != null)) {
      boolean flag = this.processDeskManagementService.doStepReback(this.taskId.toString(), this.resion, this.remindType);
      if (flag)
        ResponseUtil.write("success");
      else
        ResponseUtil.write("error");
    }
  }

  public TaskService getTaskService()
  {
    return this.taskService;
  }

  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }

  public int getCurrTodoPage() {
    return this.currTodoPage;
  }

  public void setCurrTodoPage(int currTodoPage) {
    this.currTodoPage = currTodoPage;
  }

  public int getTotalTodoPage() {
    return this.totalTodoPage;
  }

  public void setTotalTodoPage(int totalTodoPage) {
    this.totalTodoPage = totalTodoPage;
  }

  public int getCurrTaskPage() {
    return this.currTaskPage;
  }

  public void setCurrTaskPage(int currTaskPage) {
    this.currTaskPage = currTaskPage;
  }

  public int getTotalTaskPage() {
    return this.totalTaskPage;
  }

  public void setTotalTaskPage(int totalTaskPage) {
    this.totalTaskPage = totalTaskPage;
  }

  public int getPerTodoPage() {
    return this.perTodoPage;
  }

  public void setPerTodoPage(int perTodoPage) {
    this.perTodoPage = perTodoPage;
  }

  public int getPerTaskPage() {
    return this.perTaskPage;
  }

  public void setPerTaskPage(int perTaskPage) {
    this.perTaskPage = perTaskPage;
  }

  public PageBean getTodoListBean() {
    return this.todoListBean;
  }

  public void setTodoListBean(PageBean todoListBean) {
    this.todoListBean = todoListBean;
  }

  public PageBean getTaskListBean() {
    return this.taskListBean;
  }

  public void setTaskListBean(PageBean taskListBean) {
    this.taskListBean = taskListBean;
  }

  public HistoryService getHistoryService() {
    return this.historyService;
  }

  public void setHistoryService(HistoryService historyService) {
    this.historyService = historyService;
  }

  public int getCurrHistPage() {
    return this.currHistPage;
  }

  public void setCurrHistPage(int currHistPage) {
    this.currHistPage = currHistPage;
  }

  public int getTotalHistPage() {
    return this.totalHistPage;
  }

  public void setTotalHistPage(int totalHistPage) {
    this.totalHistPage = totalHistPage;
  }

  public int getPerHistPage() {
    return this.perHistPage;
  }

  public void setPerHistPage(int perHistPage) {
    this.perHistPage = perHistPage;
  }

  public PageBean getHistListBean() {
    return this.histListBean;
  }

  public void setHistListBean(PageBean histListBean) {
    this.histListBean = histListBean;
  }
  public ProcessDeskManagementService getProcessDeskManagementService() {
    return this.processDeskManagementService;
  }

  public void setProcessDeskManagementService(ProcessDeskManagementService processDeskManagementService) {
    this.processDeskManagementService = processDeskManagementService;
  }
  public Page getPage() {
    return this.page;
  }
  public void setPage(Page page) {
    this.page = page;
  }
  public Long getTaskId() {
    return this.taskId;
  }
  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }
  public String getUserId() {
    return this.userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getTablist() {
    return this.tablist;
  }
  public void setTablist(String tablist) {
    this.tablist = tablist;
  }
  public int getPagetype() {
    return this.pagetype;
  }
  public void setPagetype(int pagetype) {
    this.pagetype = pagetype;
  }
  public String getTaskOwner_query() {
    return this.taskOwner_query;
  }
  public void setTaskOwner_query(String taskOwner_query) {
    this.taskOwner_query = taskOwner_query;
  }
  public String getTaskName_query() {
    return this.taskName_query;
  }
  public void setTaskName_query(String taskName_query) {
    this.taskName_query = taskName_query;
  }
  public String getCreatTimeStart_query() {
    return this.creatTimeStart_query;
  }
  public void setCreatTimeStart_query(String creatTimeStart_query) {
    this.creatTimeStart_query = creatTimeStart_query;
  }
  public String getCreatTimeEnd_query() {
    return this.creatTimeEnd_query;
  }
  public void setCreatTimeEnd_query(String creatTimeEnd_query) {
    this.creatTimeEnd_query = creatTimeEnd_query;
  }
  public Long getInstanceId() {
    return this.instanceId;
  }
  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }
  public void setResion(String resion) {
    this.resion = resion;
  }

  public String getTitle() {
    return this.title;
  }

  public String getCurrentUser() {
    return this.currentUser;
  }

  public String getReadStatus() {
    return this.readStatus;
  }

  public HashMap getRemindTypeList() {
    return this.remindTypeList;
  }

  public void setRemindTypeList(HashMap remindTypeList) {
    this.remindTypeList = remindTypeList;
  }

  public String[] getRemindType() {
    return this.remindType;
  }

  public void setRemindType(String[] remindType) {
    this.remindType = remindType;
  }

  public String getActDefkey() {
    return this.actDefkey;
  }
  public void setActDefkey(String actDefkey) {
    this.actDefkey = actDefkey;
  }
}