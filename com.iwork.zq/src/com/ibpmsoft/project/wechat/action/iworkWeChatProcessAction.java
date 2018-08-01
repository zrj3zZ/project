package com.ibpmsoft.project.wechat.action;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.desk.handle.model.TodoTaskModel;
import com.iwork.process.desk.handle.service.ProcessDeskService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.util.List;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibpmsoft.project.wechat.service.iworkWeChatProcessService;

public class iworkWeChatProcessAction extends ActionSupport
{
  private iworkWeChatProcessService iworkWeChatProcessService;
  private ProcessDeskService processDeskService;
  private String initWeiXinScript;
  protected String listHtml;
  protected String searchKey;
  private int pageNo;
  private int itemNum;
  private int tabIndex;
  private int taskNum;
  private int noticeNum;
  private Date startdate;
  private List list;
  private String title;
  private Long type;
  private List<TodoTaskModel> tasklist;
  
  //加载今天日程页面
  public void todoRC()
  { 
	
    String userId = UserContextUtil.getInstance().getCurrentUserId();
    this.listHtml = this.iworkWeChatProcessService.getListrc(userId,pageNo,noticeNum,"0");
    ResponseUtil.write(this.listHtml);
  }
  //加载明天日程页面
  public void todotomRC()
  { 

    String userId = UserContextUtil.getInstance().getCurrentUserId();
    this.listHtml = this.iworkWeChatProcessService.getListrc(userId,pageNo,noticeNum,"1");
    ResponseUtil.write(this.listHtml);
  }
  //加载后天日程页面	
  public void tododaytomRC()
  { 
	 String userId = UserContextUtil.getInstance().getCurrentUserId();
	 this.listHtml = this.iworkWeChatProcessService.getListrc(userId,pageNo,noticeNum,"2");
	 ResponseUtil.write(this.listHtml);
  }
  //显示日程明细（待用）
  public void showRiChengHtml()
  {
	String userId = UserContextUtil.getInstance().getCurrentUserId();
    this.listHtml = this.iworkWeChatProcessService.getListrcMX(userId,title);
    ResponseUtil.write(this.listHtml);
  }
  //显示通知公告
  public void showTZGG(){
	  this.listHtml = this.iworkWeChatProcessService.getTZGG(pageNo);
	  ResponseUtil.write(this.listHtml);
  }
  //显示企业舆情
  public void showQYYQ(){
	  this.listHtml = this.iworkWeChatProcessService.getQYYQ(pageNo);
	  ResponseUtil.write(this.listHtml);
  }
  //显示市场新闻
  public void showSCXW(){
	  this.listHtml = this.iworkWeChatProcessService.getSCXW(pageNo);
	  ResponseUtil.write(this.listHtml);
  }
  /**
	 * 查询知道信息
	 */
	public void getKnowHtml(){
		String knowHtml = iworkWeChatProcessService.getLisWXWD(pageNo);
		ResponseUtil.write(knowHtml);
	}
	/**
	 * 根据名称查询知道信息
	 */
	public void getXXKnowHtml(){
		String knowHtml = iworkWeChatProcessService.getLisXXWXWD(title);
		ResponseUtil.write(knowHtml);
	}
  
	  public iworkWeChatProcessService getIworkWeChatProcessService() {
	return iworkWeChatProcessService;
	}
	public void setIworkWeChatProcessService(
			iworkWeChatProcessService iworkWeChatProcessService) {
	this.iworkWeChatProcessService = iworkWeChatProcessService;
}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public int getTabIndex() {
	    return this.tabIndex;
	  }

  public void setTabIndex(int tabIndex) {
    this.tabIndex = tabIndex;
  }

  public int getTaskNum() {
    return this.taskNum;
  }

  public void setTaskNum(int taskNum) {
    this.taskNum = taskNum;
  }

  public int getNoticeNum() {
    return this.noticeNum;
  }

  public void setNoticeNum(int noticeNum) {
    this.noticeNum = noticeNum;
  }

  public List getList() {
    return this.list;
  }

  public void setList(List list) {
    this.list = list;
  }

  public String getInitWeiXinScript() {
    return this.initWeiXinScript;
  }

  public void setInitWeiXinScript(String initWeiXinScript) {
    this.initWeiXinScript = initWeiXinScript;
  }

  public String getListHtml() {
    return this.listHtml;
  }

  public void setListHtml(String listHtml) {
    this.listHtml = listHtml;
  }

  public ProcessDeskService getProcessDeskService() {
    return this.processDeskService;
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

  public List<TodoTaskModel> getTasklist() {
    return this.tasklist;
  }

  public void setTasklist(List<TodoTaskModel> tasklist) {
    this.tasklist = tasklist;
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