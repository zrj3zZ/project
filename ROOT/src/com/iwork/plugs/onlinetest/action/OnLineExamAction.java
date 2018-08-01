package com.iwork.plugs.onlinetest.action;

import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.onlinetest.bean.ExamTopic;
import com.iwork.plugs.onlinetest.service.ExamService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class OnLineExamAction extends ActionSupport
{
  private static final long serialVersionUID = 1L;
  ExamService examService;
  String examNo;
  String examId;
  Long countNo;
  String userInfo;
  String paperSubmitBtn;
  String examTitle;
  String answerStr;
  Long instanceId;
  String paperListHtml;
  String oldPaperListHtml;
  String topicId;
  ExamTopic topic;
  protected String searchArea;
  protected String conditionStr;
  protected Long rowNum;
  protected Page page;
  protected String colNames;
  protected String colModel;
  String userId;
  public String paperno;

  public String index()
  {
    return "success";
  }

  public String topicList()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
      .get_userModel();
    this.paperListHtml = this.examService.getPaperListHtml(user);
    this.oldPaperListHtml = this.examService.getOldPaperListHtml(user);
    return "success";
  }

  public String topicContent()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
      .get_userModel();
    this.topic = this.examService.getTopicContent(user, this.topicId);
    if ((this.topicId != null) && (this.topicId.trim().length() > 0)) {
      return "success";
    }

    return null;
  }

  public String topicTop()
  {
    return "success";
  }

  public String startTest()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    HashMap result = this.examService.indexPage(user, this.examNo);
    if (result.get("instanceId") != null) {
      this.instanceId = Long.valueOf(result.get("instanceId").toString());
    }
    this.userInfo = ((String)result.get("userInfo"));
    this.paperSubmitBtn = ((String)result.get("paperSubmitBtn"));
    this.examTitle = ((String)result.get("examTitle"));

    return "success";
  }

  public String getTreeJson()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
      .get_userModel();
    String treeJson = this.examService.getExecuteTree(this.examNo, user);
    ResponseUtil.writeTextUTF8(treeJson);
    return null;
  }

  public String openOneExam()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
      .get_userModel();
    String examText = this.examService.openOneExam(this.examNo, this.countNo.longValue(), user, 
      this.instanceId);
    if (examText != null) {
      ResponseUtil.writeTextUTF8(examText);
    }
    return null;
  }

  public String answerQuestion()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    this.examService.answerQuestion(user, this.countNo.longValue(), this.examId, this.answerStr, this.instanceId);

    return null;
  }

  public String examExecuteSubmit()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
      .get_userModel();

    String result = this.examService.examExecuteSubmit(this.examNo, this.instanceId, user);

    if (result != null) {
      ResponseUtil.writeTextUTF8(result);
    }
    return null;
  }

  public String onLineTestReport()
  {
    this.searchArea = this.examService.getConditionHTML();
    this.colModel = this.examService.getColModel();
    this.colNames = this.examService.getColName();
    this.rowNum = this.examService.getPageRowNum();
    return "success";
  }

  public String onLineTestUserReport()
  {
    this.colModel = this.examService.getColModel(this.userId, this.paperno);
    this.colNames = this.examService.getColName(this.userId, this.paperno);
    this.rowNum = this.examService.getPageRowNum();

    return "success";
  }

  public String reportDoSearch()
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    Map params = request.getParameterMap();
    String json = this.examService.reportDoSearch(params, this.page, this.userInfo, 
      this.paperno);
    if ((json != null) && (json.length() > 0)) {
      ResponseUtil.write(json.substring(1, json.length() - 1));
    }

    return null;
  }

  public String userReportDoSearch()
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    Map params = request.getParameterMap();
    String json = this.examService.userReportDoSearch(params, this.page, this.userId, 
      this.paperno);
    if ((json != null) && (json.length() > 0)) {
      ResponseUtil.write(json.substring(1, json.length() - 1));
    }

    return null;
  }

  public String getExamId() {
    return this.examId;
  }

  public void setExamId(String examId) {
    this.examId = examId;
  }

  public ExamService getExamService() {
    return this.examService;
  }

  public void setExamService(ExamService examService) {
    this.examService = examService;
  }

  public String getExamNo() {
    return this.examNo;
  }

  public void setExamNo(String examNo) {
    this.examNo = examNo;
  }

  public long getCountNo() {
    return this.countNo.longValue();
  }

  public void setCountNo(long countNo) {
    this.countNo = Long.valueOf(countNo);
  }

  public String getUserInfo() {
    return this.userInfo;
  }

  public void setUserInfo(String userInfo) {
    this.userInfo = userInfo;
  }

  public String getPaperSubmitBtn() {
    return this.paperSubmitBtn;
  }

  public void setPaperSubmitBtn(String paperSubmitBtn) {
    this.paperSubmitBtn = paperSubmitBtn;
  }

  public String getExamTitle() {
    return this.examTitle;
  }

  public void setExamTitle(String examTitle) {
    this.examTitle = examTitle;
  }

  public String getAnswerStr() {
    return this.answerStr;
  }

  public void setAnswerStr(String answerStr) {
    this.answerStr = answerStr;
  }

  public long getInstanceId() {
    return this.instanceId.longValue();
  }

  public void setInstanceId(long instanceId) {
    this.instanceId = Long.valueOf(instanceId);
  }

  public String getPaperListHtml() {
    return this.paperListHtml;
  }

  public void setPaperListHtml(String paperListHtml) {
    this.paperListHtml = paperListHtml;
  }

  public String getTopicId() {
    return this.topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public ExamTopic getTopic() {
    return this.topic;
  }

  public void setTopic(ExamTopic topic) {
    this.topic = topic;
  }

  public String getOldPaperListHtml() {
    return this.oldPaperListHtml;
  }

  public void setOldPaperListHtml(String oldPaperListHtml) {
    this.oldPaperListHtml = oldPaperListHtml;
  }

  public String getSearchArea() {
    return this.searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public String getConditionStr() {
    return this.conditionStr;
  }

  public void setConditionStr(String conditionStr) {
    this.conditionStr = conditionStr;
  }

  public Long getRowNum() {
    return this.rowNum;
  }

  public void setRowNum(Long rowNum) {
    this.rowNum = rowNum;
  }

  public Page getPage() {
    return this.page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getColNames() {
    return this.colNames;
  }

  public void setColNames(String colNames) {
    this.colNames = colNames;
  }

  public String getColModel() {
    return this.colModel;
  }

  public void setColModel(String colModel) {
    this.colModel = colModel;
  }

  public void setCountNo(Long countNo) {
    this.countNo = countNo;
  }

  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getpaperno() {
    return this.paperno;
  }

  public void setpaperno(String paperno) {
    this.paperno = paperno;
  }
}