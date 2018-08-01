package com.iwork.process.desk.handle.action;

import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.desk.constant.ProcessDeskManagementConstant;
import com.iwork.process.desk.handle.model.TodoTaskModel;
import com.iwork.process.desk.handle.service.ProcessDeskService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import org.activiti.engine.task.Task;

public class ProcessDeskAction extends ActionSupport
{
  private ProcessDeskService processDeskService;
  private int desktype;
  private int pageNo;
  private int itemNum;
  private List<TodoTaskModel> taskCandidateList;
  private List<TodoTaskModel> tasklist;
  private String searchKey;
  private String from;
  private String title;
  private Long isCreate;
  private Long processStatus;
  private String content;
  private String actDefkey;
  private String actDefId;
  private String processTitle;
  private String processMemo;
  private String tabHtml;
  private Long taskId;
  private Long excutionId;
  private Long dataid;
  private Long instanceid;
  protected Page page;
  private Long orgroleid;

  public Long getOrgroleid() {
	return orgroleid;
}

public void setOrgroleid(Long orgroleid) {
	this.orgroleid = orgroleid;
}

public String todolist()
  {
    if (this.desktype == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_TODOLIST)
    {
      this.tasklist = this.processDeskService.getTaskList(this.searchKey);
    }

    return "success";
  }

  public String index()
  {
    return "success";
  }

  public void showlist() {
    if (this.pageNo == 0) {
      this.pageNo = 1;
    }
    String content = this.processDeskService.getListHtml(this.searchKey, this.pageNo, this.itemNum);
    ResponseUtil.write(content);
  }

  public String transferInit()
  {
    String actDefId = "GZJJLC:1:114404";
    Long formId = null;
    String userid = UserContextUtil.getInstance().getCurrentUserId();
    ProcessAPI processAPI = ProcessAPI.getInstance();
    formId = processAPI.getProcessDefaultFormId(actDefId);
    this.instanceid = processAPI.newInstance(actDefId, formId, userid);
    Task task = processAPI.newTaskId(this.instanceid);
    if (task != null) {
      this.taskId = Long.valueOf(Long.parseLong(task.getId()));
      this.excutionId = Long.valueOf(Long.parseLong(task.getExecutionId()));
    }
    return "success";
  }

  public String historyMonitor()
  {
	  orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
    return "success";
  }

  public String loadTodoJson()
  {
    String json = "";
    json = this.processDeskService.getHistoryTaskJson(this.from, this.actDefkey, this.title, this.isCreate, this.processStatus, this.page);

    String temp = json.substring(1, json.length() - 1);
    ResponseUtil.write(temp);
    return null;
  }

  public void showSummary() {
    if (this.taskId != null) {
      this.content = this.processDeskService.getTaskSummary(this.taskId.toString());
      ResponseUtil.write(this.content);
    }
  }

  public void setNoticeStatsu() { if (this.dataid != null) {
      boolean flag = this.processDeskService.setNoticeReadStatus(this.dataid);
      if (flag)
        ResponseUtil.write("success");
      else
        ResponseUtil.write("error");
    }
  }

  public String notice()
  {
    return "success";
  }

  public String processing()
  {
    return "success";
  }

  public String history() {
    return "success";
  }

  public String createlog() {
    return "success";
  }

  public String processBox()
  {
    if (this.desktype == 0) {
      this.desktype = 1;
    }
    if (this.actDefkey != null) {
      ProcessDefDeploy deploy = this.processDeskService.getProcessTitle(this.actDefkey);
      if (deploy != null) {
        this.processTitle = deploy.getTitle();
        this.actDefId = deploy.getActDefId();
        if (deploy.getMemo() != null) {
          this.processMemo = deploy.getMemo();
          this.processMemo = this.processMemo.replace("\n", "<br/>");
        }
      }

      this.tabHtml = this.processDeskService.getTabHtml(this.desktype, this.actDefkey);
      if (this.desktype == 1)
        return "history";
      if (this.desktype == 2) {
        if (this.actDefkey != null) {
          this.tasklist = this.processDeskService.getTaskList(this.actDefkey, this.searchKey);
        }
        return "todo";
      }if (this.desktype == 3)
        return "notice";
      if (this.desktype == 4) {
        return "loglist";
      }
    }

    return "success";
  }
  public void setProcessDeskService(ProcessDeskService processDeskService) {
    this.processDeskService = processDeskService;
  }

  public String getSearchKey() {
    return this.searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public int getDesktype()
  {
    return this.desktype;
  }
  public void setDesktype(int desktype) {
    this.desktype = desktype;
  }
  public List<TodoTaskModel> getTasklist() {
    return this.tasklist;
  }

  public List<TodoTaskModel> getTaskCandidateList() {
    return this.taskCandidateList;
  }
  public Long getTaskId() {
    return this.taskId;
  }
  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }
  public String getContent() {
    return this.content;
  }

  public void setDataid(Long dataid) {
    this.dataid = dataid;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getIsCreate() {
    return this.isCreate;
  }

  public void setIsCreate(Long isCreate) {
    this.isCreate = isCreate;
  }

  public Long getProcessStatus() {
    return this.processStatus;
  }

  public void setProcessStatus(Long processStatus) {
    this.processStatus = processStatus;
  }

  public Page getPage() {
    return this.page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Long getExcutionId() {
    return this.excutionId;
  }

  public void setExcutionId(Long excutionId) {
    this.excutionId = excutionId;
  }

  public String getActDefkey()
  {
    return this.actDefkey;
  }
  public void setActDefkey(String actDefkey) {
    this.actDefkey = actDefkey;
  }
  public String getTabHtml() {
    return this.tabHtml;
  }
  public void setTabHtml(String tabHtml) {
    this.tabHtml = tabHtml;
  }
  public String getActDefId() {
    return this.actDefId;
  }
  public void setActDefId(String actDefId) {
    this.actDefId = actDefId;
  }
  public String getProcessTitle() {
    return this.processTitle;
  }
  public void setProcessTitle(String processTitle) {
    this.processTitle = processTitle;
  }

  public String getProcessMemo() {
    return this.processMemo;
  }

  public void setProcessMemo(String processMemo) {
    this.processMemo = processMemo;
  }

  public int getPageNo() {
    return this.pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(int itemNum) {
    this.itemNum = itemNum;
  }
}