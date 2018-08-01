package com.iwork.plugs.onlinetest.service;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgRoleGroup;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.onlinetest.bean.ExamPaperModel;
import com.iwork.plugs.onlinetest.bean.ExamStoreModel;
import com.iwork.plugs.onlinetest.bean.ExamTopic;
import com.iwork.plugs.onlinetest.bean.ExamTreeJsonModel;
import com.iwork.plugs.onlinetest.bean.ExamUserExamModel;
import com.iwork.plugs.onlinetest.bean.ExamUserItemLogModel;
import com.iwork.plugs.onlinetest.dao.ExamDAO;
import com.iwork.plugs.onlinetest.dao.ExamUserDAO;
import com.iwork.plugs.onlinetest.util.ExamUserUtil;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionContext;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

public class ExamService{
  ExamUserDAO examUserDAO;
  ExamDAO examDAO;
  SysEngineMetadataDAO sysEngineMetadataDAO;
  SysEngineIFormDAO sysEngineIFormDAO;
  String testStatus = "user_online_test_status";

  public String getPaperListHtml(OrgUser user){
    StringBuffer sb = new StringBuffer();
    List<ExamPaperModel> list = this.examDAO.getPaperList();
    List<ExamPaperModel> lists =new ArrayList<ExamPaperModel>();
    for (ExamPaperModel model : list){
      boolean flag = SecurityUtil.getInstance().checkUserSecurity( user.getUserid(), model.getExamuser());
      if (!flag) {
       // list.remove(model);
    	  lists.add(model);
      }
    }
   for (int i = 0; i < list.size(); i++) {
		for (int j = 0; j < lists.size(); j++) {
			if(list.get(i)==lists.get(j)){
				list.remove(lists.get(j));
			}
		}
   }
    for (ExamPaperModel model : list) {
      sb.append("<dd style=\"word-break: break-all;\">") .append("<a style=\"overflow:hidden;\"  title=\""+model.getName()+"\" id=\"syspersion_generalSet\" href=\"topicContent.action?topicId=" +  model.getNo() + "\" target=\"online_test_right\">").append(model.getName()).append("</a></dd>");
    }
    return sb.toString();
  }
  public String getOldPaperListHtml(OrgUser user) {
    StringBuffer sb = new StringBuffer();
    List list = this.examDAO.getPaperList();
    List<Map> result = new ArrayList();
    String sql = "select * from BD_EXAM_USER_EXAM where userid= ?  and status='已交卷'";
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ExamPaperModel model;
    try {
      Map map = null;
      conn = DBUtil.open();
      conn.setAutoCommit(true);
      stmt =conn.prepareStatement(sql);
      stmt.setString(1, user.getUserid());
      rs = stmt.executeQuery();
      if (rs != null) {
      	while (rs.next()) {
      		 map = new HashMap();
      	  String paperno = String.valueOf(rs.getInt("paperno"));
      	  String papername = rs.getString("papername");
      	  map.put("paperno", paperno);
      	 map.put("papername", papername);
      	 result.add(map);
      	}
      }
     
    }
    catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, stmt, rs);
    }
   
    if(result!=null && result.size()>0){
	    for (int i = 0; i < result.size(); i++) {
	    	 sb.append("<dd style=\"word-break: break-all;\">") .append("<a style=\"overflow:hidden;\" title=\""+result.get(i).get("papername")+"\" id=\"syspersion_generalSet\" href=\"topicContent.action?topicId=" +  result.get(i).get("paperno") + "\" target=\"online_test_right\">").append(result.get(i).get("papername")).append("</a></dd>");
		}
    }
    return sb.toString();
  }

  public HashMap<String, String> indexPage(OrgUser user, String examNo) {
    ActionContext actionContext = ActionContext.getContext();
    HttpServletRequest request = (HttpServletRequest)actionContext.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    HttpSession session = request.getSession();

    String userInfo = user.getUsername() + "【" + user.getUserid() + "】【" + 
      user.getDepartmentname() + "】";
    String paperSubmitBtn = "<input style=\"width: 80px; height: 24px;\" type = button class='papersubmit' name='papersubmit' value = ' 交 卷 ' onClick=\"examSubmit();\">";
    HashMap hashTags = new HashMap();

    ExamUserExamModel euem = this.examUserDAO.getUserExamModel(examNo, 
      user.getUserid());

    ExamPaperModel model = this.examDAO.getPaperModel(examNo);
    model.setNo(examNo);
    long instanceId = 0L;
    if ((euem == null) || ("".equals(euem.getPaperno()))) {
      instanceId = this.examUserDAO.createUserExamInfo(model, user, "考试中");
      euem = this.examUserDAO.getUserExamModel(examNo, user.getUserid());
    }
    else if (euem.getStatus().equals("已交卷")) {
      paperSubmitBtn = "<input style=\"width: 80px; height: 24px;\"  type = button class='papersubmit' name='papersubmit' disabled value = ' 已交卷 '>";
    }

    session.setAttribute(this.testStatus, euem.getStatus());
    Map params = new HashMap();
    String examTitle = model.getName();
    if (instanceId == 0L) {
      SysEngineMetadata metadata = this.sysEngineMetadataDAO
        .getModel("BD_EXAM_USER_EXAM");
      if ((metadata != null) && (metadata.getId().longValue() > 0L)) {
        String sql = "select * from sys_engine_form_bind where metadataid = ?  and dataid= ? ";
        params.put(1, metadata.getId());
        params.put(2, euem.getId());
      //  instanceId = DBUtil.getInt(sql, "instanceid");
        instanceId=DBUTilNew.getInt( "instanceid",sql,params);
      }
    }

    hashTags.put("instanceId", instanceId);
    hashTags.put("userInfo", userInfo);
    hashTags.put("paperSubmitBtn", paperSubmitBtn);
    hashTags.put("examTitle", examTitle);

    return hashTags;
  }

  public String getExecuteTree(String examNo, OrgUser user){
    Hashtable hashTags = new Hashtable();

    ExamUserExamModel euem = this.examUserDAO.getUserExamModel(examNo, 
      user.getUserid());
    String tree = getTreeJson(examNo);
    return tree;
  }

  private String getTreeJson(String examNo) {
    List list = new ArrayList();
    int count = 0;
    Map params = new HashMap();
    SysEngineMetadata metadata = this.sysEngineMetadataDAO.getModel("BD_EXAM_PAPER_P");
    String sql = "";
    long instanceId = 0L;

    if ((metadata != null) && (metadata.getId().longValue() > 0L)){
      sql = "select * from sys_engine_form_bind where metadataid = ? and dataid= ? ";
      params.put(1, metadata.getId());
      params.put(2, examNo);
     // instanceId = DBUtil.getInt(sql, "instanceid");
      instanceId=DBUTilNew.getInt( "instanceid",sql,params);
    } else {
      return "";
    }

    List radiolist = new ArrayList();
    List checklist = new ArrayList();
    List judgelist = new ArrayList();

    List<HashMap> map = DemAPI.getInstance().getFromSubData(Long.valueOf(instanceId),  "SUBFORM_DXT");
    for (HashMap hashMap : map) {
      radiolist.add(hashMap.get("NO"));
    }
    map = DemAPI.getInstance().getFromSubData(Long.valueOf(instanceId), "SUBFORM_FXT");
    for (HashMap hashMap : map) {
      checklist.add(hashMap.get("NO"));
    }
    map = DemAPI.getInstance().getFromSubData(Long.valueOf(instanceId), "SUBFORM_PDT");
    for (HashMap hashMap : map) {
      judgelist.add(hashMap.get("NO"));
    }

    String treeIconUrl = "/iwork_img/blueprint--pencil.png";
    String checkIconUrl = "/iwork_img/ui-check-box.png";
    String judgeIconUrl = "/iwork_img/ok.gif";
    String radioIconUrl = "/iwork_img/ui-radio-button.png";

    ExamTreeJsonModel model = new ExamTreeJsonModel();
    model.setId(1);
    model.setpId(0);
    model.setName("试题列表");
    model.setOpen(true);
    model.setClick(Boolean.valueOf(false));
    model.setIcon(treeIconUrl);
    list.add(model);

    model = new ExamTreeJsonModel();
    model.setId(11);
    model.setpId(1);
    model.setName("单选题");
    model.setOpen(true);
    model.setClick(Boolean.valueOf(false));
    model.setIcon(treeIconUrl);
    list.add(model);

    if (radiolist != null) {
      int topid = model.getId();
      for (int i = 0; i < radiolist.size(); i++)
      {
        count++;
        model = new ExamTreeJsonModel();
        model.setId(topid * 10 + i);
        model.setpId(topid);
        model.setName("试题" + count);
        model.setClick("openOneExam('" + radiolist.get(i) + "','" + count + "');");
        model.setExamId(radiolist.get(i).toString());
        model.setCountNo(String.valueOf(count));
        model.setIcon(radioIconUrl);
        list.add(model);
      }

    }

    model = new ExamTreeJsonModel();
    model.setId(12);
    model.setpId(1);
    model.setName("复选题");
    model.setOpen(true);
    model.setClick(Boolean.valueOf(false));
    model.setIcon(treeIconUrl);
    list.add(model);

    if (checklist != null) {
      int topid = model.getId();
      for (int i = 0; i < checklist.size(); i++)
      {
        count++;
        model = new ExamTreeJsonModel();
        model.setId(topid * 10 + i);
        model.setpId(topid);
        model.setName("试题" + count);
        model.setClick("openOneExam('" + checklist.get(i) + "','" + count + "');");
        model.setExamId(checklist.get(i).toString());
        model.setCountNo(String.valueOf(count));
        model.setIcon(checkIconUrl);
        list.add(model);
      }

    }

    model = new ExamTreeJsonModel();
    model.setId(13);
    model.setpId(1);
    model.setName("判断题");
    model.setOpen(true);
    model.setClick(Boolean.valueOf(false));
    model.setIcon(treeIconUrl);
    list.add(model);

    if ((judgelist.size() > 0) && 
      (judgelist != null)) {
      int topid = model.getId();
      for (int i = 0; i < judgelist.size(); i++)
      {
        count++;
        model = new ExamTreeJsonModel();
        model.setId(topid * 10 + i);
        model.setpId(topid);
        model.setName("试题" + count);
        model.setClick("openOneExam('" + judgelist.get(i) + "','" + count + "');");
        model.setExamId(judgelist.get(i).toString());
        model.setCountNo(String.valueOf(count));
        model.setIcon(judgeIconUrl);
        list.add(model);
      }

    }

    JSONArray json = JSONArray.fromObject(list);
    return json.toString();
  }

  public String openOneExam(String examId, long countNo, OrgUser user, Long userExamInstanceId) {
    String demUUID = "4277f94a-4b63-4805-9389-b76d1ed7d317";
    HashMap conditionMap = new HashMap();
    conditionMap.put("NO", examId);
    List list = DemAPI.getInstance().getAllList(demUUID, conditionMap, null);
    HashMap map = null;
    if ((list != null) && (list.size() > 0)) {
      map = (HashMap)list.get(0);
    }
    Long instanceId = Long.valueOf(map.get("INSTANCEID").toString());

    String examText = "";
    String answerStr = "";
    StringBuffer examTestSb = new StringBuffer();

    StringBuffer nextTestSB = new StringBuffer();
    if (map != null) {
      examText = (String)map.get("TITLE");

      examTestSb.append("<tr>").append("<td width=\"4%\">").append("&nbsp;</td>").append("<td width=\"96%\" style=\"font-size: 14px; font-weight: bold\">").append(countNo + "、" + examText + "（ ）" + "(" + 
        map.get("SCORENUM") + "分)").append("</td></tr>");

      List listSubData = DemAPI.getInstance().getFromSubData( instanceId, "SUBFORM_TKSTXXB");

      ExamUserItemLogModel model = this.examUserDAO.getUserExamItemLog(examId, String.valueOf(countNo), user.getUserid(), userExamInstanceId.longValue());

      answerStr = examOption((String)map.get("TOPICTYPE"), listSubData, model, user.getUserid());

      nextTestSB.append("<tr><td>&nbsp</td><td>");
      if (countNo > 1L) {
        nextTestSB .append("<input onclick=\"nextExam(" + (countNo - 1L) +  ")\" style=\"width: 80px; height: 24px;\" type = button class='papersubmit' value=\"上一题\" >");
        nextTestSB.append("<input onclick=\"nextExam(" + ( countNo + 1L) +  ")\" style=\"width: 80px; height: 24px;margin-left: 10px;\" type = button class='papersubmit' value=\"下一题\" >");
      } else {
        nextTestSB.append("<input onclick=\"nextExam(1)\" style=\"width: 80px; height: 24px;\" type = button class='papersubmit' value=\"上一题\" >");
        nextTestSB.append("<input onclick=\"nextExam(2)\" style=\"width: 80px; height: 24px;margin-left: 10px;\" type = button class='papersubmit' value=\"下一题\" >");
      }
      nextTestSB.append("</td></tr>");
    }
    return examTestSb.toString() + answerStr + nextTestSB.toString();
  }

  private String examOption(String type, List<HashMap> listSubData, ExamUserItemLogModel model, String userId){
    ActionContext actionContext = ActionContext.getContext();
    HttpServletRequest request = (HttpServletRequest)actionContext.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    HttpSession session = request.getSession();

    StringBuffer sb = new StringBuffer();
    String inputType = "";
    boolean flag = false;
    if (type.equals("单选题")) {
      inputType = "radio";
    } else if (type.equals("复选题")) {
      inputType = "checkbox";
      flag = true;
    } else if (type.equals("判断题")) {
      inputType = "radio";
    }

    String disabled = "";
    String status = (String)session.getAttribute(this.testStatus);
    if (status.equals("已交卷")) {
      disabled = "disabled=\"disabled\"";
    }

    for (int i = 0; i < listSubData.size(); i++){
      String moreStr = "";
      String name = "answer";
      if (flag) {
        name = name + i;
      }
      HashMap map = (HashMap)listSubData.get(i);
      String itemId = map.get("ITEMCODE").toString();
      if ((model != null) && (model.getReply().indexOf(itemId) != -1)) {
        moreStr = "checked=\"checked\"";
      }

      sb.append("<tr><td>").append("<input type=\"" + inputType + "\" name=\"" + name +  "\" id=\"userSelect" + i + "\" " + moreStr + disabled + " value=\"" + map.get("ITEMCODE") + 
        "\" />").append("</td><td><label for=\"userSelect" + i +  "\"><span>").append("（" + map.get("ITEMCODE") + "）" +  map.get("ITEMTITLE")) .append("</span></label></td></tr>");
    }

    return sb.toString();
  }

  public String answerQuestion(OrgUser user, long countNo, String examId, String answerStr, Long instanceId) {
    if (countNo > 0L) {
      ExamStoreModel storemodel = this.examDAO.getStoreExamModel(examId);

      int score = storemodel.getScorenum();
      ExamUserItemLogModel itemlogmodel = new ExamUserItemLogModel();

      itemlogmodel.setExamno(instanceId.intValue());
      itemlogmodel.setItemno(String.valueOf(countNo));
      itemlogmodel.setPaperno(examId);
      itemlogmodel.setReply(answerStr);

      if (answerStr.trim().length() > 1) {
        boolean flag = ExamUserUtil.isCheckAnswerSuccess(
          storemodel.getAnswer(), answerStr.trim());
        if (flag)
          itemlogmodel.setScore(score);
        else {
          itemlogmodel.setScore(0);
        }
      }
      else if (storemodel.getAnswer().trim().equals(answerStr.trim())) {
        itemlogmodel.setScore(score);
      } else {
        itemlogmodel.setScore(0);
      }

      boolean flag = this.examUserDAO.createUserSubmit(user, itemlogmodel, 
        instanceId.longValue());
      return "";
    }

    return "";
  }

  public String examExecuteSubmit(String examNo, Long instanceId, OrgUser user) {
    int count = ExamUserUtil.isFinishNumber(examNo, instanceId.longValue(), user);
    String alertStr = "";
    if (count > 0) {
      alertStr = "您还有" + count + "道题目没有完成，请完成后点击\"交卷\"按钮";
    } else if (count == 0) {
      int sumSorce = this.examUserDAO.getTotalSorce(instanceId.longValue(), 
        user.getUserid());
      this.examUserDAO.executePaperSubmit(examNo, user.getUserid(), sumSorce);
      alertStr = "恭喜您答题完毕 ,您本次考试的分数为 " + sumSorce + "分";
    } else {
      alertStr = "提交失败,请联系管理员";
    }

    return alertStr;
  }

  public ExamTopic getTopicContent(OrgUser user, String topicId) {
    ExamTopic topic = new ExamTopic();
    ExamPaperModel model = this.examDAO.getPaperModel(topicId);
    topic.setTopicTitle(model.getName());

    String topicTime = "";
    if ((model.getBegindate().trim().length() > 10) && 
      (model.getEnddate().trim().length() > 10)) {
      topicTime = model.getBegindate().substring(0, 10) + " 到 " + model.getEnddate().substring(0, 10);
    }
    topic.setTopicTime(topicTime);
    topic.setTopicArea(model.getExamzone());
    topic.setTopicContent(model.getMemo());

    StringBuffer scoreExplain = new StringBuffer();

    scoreExplain.append(" 单选题" + model.getDxt() + "分，");
    scoreExplain.append(" 复选题" + model.getFxt() + "分，");
    scoreExplain.append(" 判断题" + model.getPdt() + "分，");
    scoreExplain.append(" 总分" + model.getZf() + "分。");

    topic.setScoreExplain(scoreExplain.toString());
    Map params=new HashMap();
	params.put(1, topicId);
	params.put(2, user.getUserid());
	String topicStatus="";
    String sql = "select * from BD_EXAM_USER_EXAM where paperno= ?  and userid= ? ";
  //  String topicStatus = DBUTilNew.getDataStr("status",sql,params );
    if(topicId!=null &&!"".equals(topicId)) topicStatus = DBUTilNew.getDataStr("status",sql,params );
    StringBuffer sb = new StringBuffer();
    if ("已交卷".equals(topicStatus)) {
      sb.append("恭喜您，题目已完成，您的分数是：");
      int score = DBUTilNew.getInt("sumscore",sql,params);
      sb.append(score + "分");
      sb.append("<br>");
      sb.append("<input type=button style=\"width: 90px; height: 25px; margin-top: 6px;\" name='startup' value='查看历史' onclick=\"startTest(" + topicId + ")\">");
    } else {
      sb.append("<input type=button style=\"width: 90px; height: 25px; margin-top: 6px;\" name='startup' value='开始答题' onclick=\"startTest(" + topicId + ")\">");
    }
    topic.setTopicScore(sb.toString());

    return topic;
  }

  public String getOnLineTestReportJson(long examId) {
    return "";
  }

  public String getOnLineTestUserReportJson(long examId, OrgUser user) {
    return "";
  }

  public String getConditionHTML() {
    StringBuffer sb = new StringBuffer();
    List<ExamPaperModel> list = this.examDAO.getPaperList();
    sb.append("<option selected=\"selected\" value =\"0\">全部</option>");
    for (ExamPaperModel model : list) {
      sb.append("<option value =\"" + model.getNo() + "\">" + model.getName() + "</option>");
    }
    return sb.toString();
  }

  public String getColModel(){
    String[] titles = { "PAPERNO", "PAPERNAME", "USERID", "USERNAME",  "DEPTFULLNAME", "DATETIME", "EXAMTIME", "SUMSCORE", "STATUS" };

    return getColModelJson(titles);
  }

  public String getColModel(String userId, String papgerName) {
    String[] titles = { "QUESTION", "ANSWER", "REPLY", "USERID",  "USERNAME", "DEPTNAME", "SCORE" };

    return getColModelJson(titles);
  }

  private String getColModelJson(String[] titles) {
    StringBuffer html = new StringBuffer();
    List maplist = new ArrayList();
    int count = 0;
    for (int i = 0; i < titles.length; i++) {
      count++;
      HashMap hash = new HashMap();
      hash.put("index", Integer.valueOf(count));
      hash.put("align", "center");
      hash.put("name", titles[i]);
      hash.put("width", new Long(130L));
      maplist.add(hash);
    }
    JSONArray json = JSONArray.fromObject(maplist);
    html.append(json);
    return html.toString();
  }

  public String getColName() {
    String[] titles = { "试卷编号", "试卷名称", "答题人账号", "答题人姓名", "答题人部门", "答题日期",  "答题用时", "得分", "考试状态" };

    return getColNameJson(titles);
  }

  public String getColName(String userId, String paperName){
    String[] titles = { "问题", "答案", "选项", "用户账号", "用户姓名", "部门名称", "得分" };

    return getColNameJson(titles);
  }

  private String getColNameJson(String[] titles) {
    List maplist = new ArrayList();
    for (int i = 0; i < titles.length; i++) {
      maplist.add(titles[i]);
    }
    JSONArray json = JSONArray.fromObject(maplist);
    StringBuffer html = new StringBuffer();
    html.append(json);
    return html.toString();
  }

  public Long getPageRowNum() {
    return new Long(20L);
  }

  public String reportDoSearch(Map params, Page page, String userId, String paperno) {
    if (page == null) {
      page = new Page();
      page.setCurPageNo(0);
      page.setPageSize(20);
    }

    List<ExamUserExamModel> userexamList = this.examUserDAO.getAllUserSelfExamList(userId, paperno);

    int totalRecord = 0;
    int totalNum = 0;
    HashMap total = new HashMap();
    List datalist = new ArrayList();
    String[] titles = { "PAPERNO", "PAPERNAME", "USERID", "USERNAME",  "DEPTFULLNAME", "DATETIME", "EXAMTIME", "SUMSCORE", "STATUS" };

    if ((userexamList != null) && (userexamList.size() > 0)) {
      for (ExamUserExamModel model : userexamList) {
        Map map = new HashMap();
        map.put(titles[0],  model.getPaperno() == null ? "" : model.getPaperno());
        map.put(titles[1],  model.getPapername() == null ? "" : model .getPapername());
        map.put(titles[2],  model.getUserid() == null ? "" : model.getUserid());
        map.put(titles[3],  model.getUsername() == null ? "" : model.getUsername());
        map.put(titles[4],  model.getDeptfullname() == null ? "" : model.getDeptfullname());
        map.put(titles[5], model.getDatetime() == null ? "" : model.getDatetime().subSequence(0, 10));
        map.put(titles[8], model.getStatus() == null ? "" : model.getStatus());
        map.put(titles[6], Integer.valueOf(model.getExamtime()));
        map.put(titles[7], Integer.valueOf(model.getSumscore()));

        datalist.add(map);
      }

      totalRecord = userexamList.size();
      BigDecimal b1 = new BigDecimal(totalRecord);
      BigDecimal b2 = new BigDecimal(page.getPageSize());

      totalNum = b1.divide(b2, 0, 0).intValue();
    }

    total.put("total", Integer.valueOf(totalRecord));
    total.put("curPageNo", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", datalist);
    JSONArray json = JSONArray.fromObject(total);
    return json.toString();
  }

  public String userReportDoSearch(Map params, Page page, String userId, String paperno){
    if (page == null) {
      page = new Page();
      page.setCurPageNo(0);
      page.setPageSize(20);
    }
    Map params1 = new HashMap();
    params1.put(1, paperno);
    params1.put(2, userId);
   // instanceId = DBUtil.getInt(sql, "instanceid");
   // instanceId=DBUTilNew.getInt( "instanceid",sql,params1);
    String getDataIdSql = "select * from BD_EXAM_USER_EXAM where paperno= ?  and userid= ? ";
   // int dataId = DBUtil.getInt(getDataIdSql, "id");
    int dataId = DBUTilNew.getInt("id",getDataIdSql, params1);
    SysEngineMetadata metadata = this.sysEngineMetadataDAO.getModel("BD_EXAM_USER_EXAM");
    long instanceId = new Long(0L).longValue();
    if ((metadata != null) && (metadata.getId().longValue() > 0L)) {
      params1 = new HashMap();
      params1.put(1, metadata.getId());
      params1.put(2, dataId);
      String sql = "select * from sys_engine_form_bind where metadataid = ?  and dataid= ? ";
      instanceId =DBUTilNew.getInt("instanceid",sql,params1);
    }

    List<ExamUserItemLogModel> userexamList = this.examUserDAO.getAllUserLogExam(instanceId, userId);

    HashMap total = new HashMap();
    List datalist = new ArrayList();

    String[] titles = { "QUESTION", "ANSWER", "REPLY", "USERID", "USERNAME", "DEPTNAME", "SCORE" };

    if ((userexamList != null) && (userexamList.size() > 0)) {
      for (ExamUserItemLogModel model : userexamList) {
        Map map = new HashMap();

        long questionId =  Integer.parseInt(model.getPaperno() == null ? "0" : model.getPaperno());

        if (questionId > 0L) {
          params1 = new HashMap();
          params1.put(1, questionId);
          String getQuestionSql = "SELECT * FROM BD_EXAM_STOREITEM_P where NO= ? " ;
          String questionTitle = DBUTilNew.getDataStr("title",getQuestionSql, params1);

          SysEngineMetadata examMetadata = this.sysEngineMetadataDAO .getModel("BD_EXAM_STOREITEM_P");
          long examInstanceId = 0L;
          if ((examMetadata != null) && (examMetadata.getId().longValue() > 0L)) {
        	params1 = new HashMap();
            params1.put(1, examMetadata.getId());
            String examSql = "select * from sys_engine_form_bind where metadataid = ? ";
            examInstanceId = DBUTilNew.getInt( "instanceid",examSql,params1);
          }
          ExamStoreModel storemodel = this.examDAO.getStoreExamModel(model.getPaperno());
          List listSubData = DemAPI.getInstance().getFromSubData(Long.valueOf(examInstanceId), "SUBFORM_TKSTXXB");
          String answer = "";
          for (int i = 0; i < listSubData.size(); i++) {
            HashMap dataMap = (HashMap)listSubData.get(i);
            String itemId = dataMap.get("ITEMCODE").toString();
            if ((model != null) && 
              (model.getReply().indexOf(itemId) != -1)) {
              if (answer.length() > 0)
                answer = answer + "||" + 
                  dataMap.get("ITEMTITLE");
              else {
                answer = dataMap.get("ITEMTITLE").toString();
              }
            }
          }

          map.put(titles[0], questionTitle);
          map.put(titles[1], storemodel.getAnswer().trim());
          map.put(titles[2],model.getReply() == null ? "" : model.getReply());
          map.put(titles[3], userId);
          UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
          map.put(titles[4], uc._userModel.getUsername());
          map.put(titles[5], uc._userModel.getDepartmentname());
          map.put(titles[6], Integer.valueOf(model.getScore()));

          datalist.add(map);
        }

      }

      int totalRecord = userexamList.size();
      BigDecimal b1 = new BigDecimal(totalRecord);
      BigDecimal b2 = new BigDecimal(page.getPageSize());

      int totalNum = b1.divide(b2, 0, 0).intValue();

      total.put("total", Integer.valueOf(totalRecord));
      total.put("curPageNo", Integer.valueOf(page.getCurPageNo()));
      total.put("pageSize", Integer.valueOf(page.getPageSize()));
      total.put("totalPages", Integer.valueOf(totalNum));
      total.put("totalRecords", Integer.valueOf(totalRecord));
      total.put("dataRows", datalist);
      JSONArray json = JSONArray.fromObject(total);
      return json.toString();
    }

    return null;
  }

  public ExamUserDAO getExamUserDAO() {
    return this.examUserDAO;
  }

  public void setExamUserDAO(ExamUserDAO examUserDAO) {
    this.examUserDAO = examUserDAO;
  }

  public ExamDAO getExamDAO() {
    return this.examDAO;
  }

  public void setExamDAO(ExamDAO examDAO) {
    this.examDAO = examDAO;
  }

  public SysEngineMetadataDAO getSysEngineMetadataDAO() {
    return this.sysEngineMetadataDAO;
  }

  public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO)
  {
    this.sysEngineMetadataDAO = sysEngineMetadataDAO;
  }

  public SysEngineIFormDAO getSysEngineIFormDAO() {
    return this.sysEngineIFormDAO;
  }

  public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
    this.sysEngineIFormDAO = sysEngineIFormDAO;
  }
}