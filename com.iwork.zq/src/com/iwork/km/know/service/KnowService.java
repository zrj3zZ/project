package com.iwork.km.know.service;

import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.app.message.sysmsg.service.SysMessageService;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.impl.EaglesSearchknowImpl;
import com.iwork.eaglesearch.model.KnowIndexModel;
import com.iwork.km.know.constant.KnowScoreConstant;
import com.iwork.km.know.dao.KnowDAO;
import com.iwork.km.know.model.IworkKnowAnswer;
import com.iwork.km.know.model.IworkKnowClasses;
import com.iwork.km.know.model.IworkKnowHoldoutLog;
import com.iwork.km.know.model.IworkKnowKeywordLog;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.km.know.model.IworkKnowTagLog;
import com.iwork.km.know.model.IworkKnowTalk;
import com.iwork.km.know.model.IworkKnowUser;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class KnowService
{
  private SysMessageService sysMessageService;
  private FileUploadService uploadifyService;
  private KnowDAO knowDAO;

  public IworkKnowQuestion initQuestion()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    IworkKnowQuestion model = new IworkKnowQuestion();
    model.setAnswerbody("多个被邀请回答人请用逗号分隔");

    model.setQtags("多个标签请用逗号分隔");
    model.setScore(KnowScoreConstant.DEFAUL_SCORE);
    model.setShownametype(Long.valueOf(1L));
    model.setQuid(user.getUserid());
    model.setQuname(user.getUsername());
    model.setQbegintime(UtilDate.getNowDatetime());
    model.setQtype(new Long(0L));
    model.setClickcount(new Long(0L));
    return model;
  }

  public boolean isQuestionExist(Long id)
  {
    IworkKnowQuestion model = this.knowDAO.getQuestionModel(id);
    if (model == null) {
      return false;
    }
    return true;
  }

  public String isQueUnBclassExist(Long bcid)
  {
    String returnStr = "true";
    Long count = this.knowDAO.getQuestionCountByCid(bcid, "b");
    if (count.longValue() == 0L) {
      returnStr = "false";
    }
    return returnStr;
  }

  public void save(IworkKnowQuestion model)
  {
    if (model != null) {
      this.knowDAO.saveQuestionModel(model);

      addIndex(model);
    }
  }

  public void update(IworkKnowQuestion model)
  {
    if (model != null) {
      this.knowDAO.updateQuestionModel(model);

      updateIndex(model);
    }
  }

  private void addIndex(IworkKnowQuestion model)
  {
    KnowIndexModel knowModel = new KnowIndexModel();
    EaglesSearchknowImpl docIndex = (EaglesSearchknowImpl)EaglesSearchFactory.getEaglesSearcherImpl("KNOWINDEX");
    knowModel.setId(model.getId().toString());
    knowModel.setTitle(model.getQcontent());

    knowModel.setContent(model.getQcontent());
    knowModel.setKeyword(model.getQtags());
    docIndex.addDocument(knowModel);
    docIndex = null;
  }

  private void updateIndex(IworkKnowQuestion model)
  {
    KnowIndexModel knowModel = new KnowIndexModel();
    EaglesSearchknowImpl docIndex = (EaglesSearchknowImpl)EaglesSearchFactory.getEaglesSearcherImpl("KNOWINDEX");
    if (model.getQtype().equals(new Long(2L))) {
      docIndex.delDocuemnt(model.getId().toString());
    } else {
      knowModel.setId(model.getId().toString());
      knowModel.setTitle(model.getQcontent());

      StringBuffer content = new StringBuffer();
      content.append(model.getQcontent());
      List<IworkKnowAnswer> answerlist = this.knowDAO.getAnswerList(model.getId());
      for (IworkKnowAnswer answer : answerlist) {
        content.append(answer.getAcontent());
      }
      knowModel.setContent(content.toString());
      knowModel.setKeyword(model.getQtags());
      docIndex.updateDocument(knowModel);
      docIndex = null;
    }
  }

  private void delIndex(String indexId)
  {
    KnowIndexModel knowModel = new KnowIndexModel();
    EaglesSearchknowImpl docIndex = (EaglesSearchknowImpl)EaglesSearchFactory.getEaglesSearcherImpl("KNOWINDEX");
    docIndex.delDocuemnt(indexId);
    docIndex = null;
  }

  public void updateTagLog(IworkKnowClasses classes, String tag)
  {
    if (isTagLogExist(classes.getId(), tag)) {
      IworkKnowTagLog model = this.knowDAO.getTagLog(classes.getId(), tag);
      model.setUsecount(Long.valueOf(model.getUsecount().longValue() + 1L));
      this.knowDAO.updateTagLogModel(model);
    } else {
      IworkKnowTagLog model = new IworkKnowTagLog(classes, tag, new Long(1L), classes.getCname());
      this.knowDAO.saveTagLogModel(model);
    }
  }

  public boolean isTagLogExist(Long cid, String tag)
  {
    IworkKnowTagLog model = this.knowDAO.getTagLog(cid, tag);
    if (model == null) {
      return false;
    }
    return true;
  }

  public void updateScore(Long score, String uid)
  {
    if (isUserExist(uid)) {
      IworkKnowUser model = this.knowDAO.getUserModel(uid);
      model.setScore(Long.valueOf(model.getScore().longValue() + score.longValue()));
      this.knowDAO.updateUserModel(model);
    } else {
      String uname = UserContextUtil.getInstance().getUserName(uid);
      IworkKnowUser model = new IworkKnowUser(uid, uname, null, null, score);
      this.knowDAO.saveUserModel(model);
    }
  }

  public boolean isUserExist(String uid)
  {
    IworkKnowUser model = this.knowDAO.getUserModel(uid);
    if (model == null) {
      return false;
    }
    return true;
  }

  public List<String> getReceiverList(String receivers)
  {
    String[] receiverArray = receivers.split(",");
    List list = new ArrayList();
    for (String receiver : receiverArray) {
      if ((receiver != null) && 
        (receiver.indexOf("[") > 0)) {
        receiver = receiver.substring(0, receiver.indexOf("["));
        list.add(receiver);
      }
    }
    return list;
  }

  public List<Map<String, Object>> searchUnsolvedOrSolvedQuestionList(String keyword, Long bcid, int pageNow, int pageSize, Long type)
  {
    List list = new ArrayList();
    List<IworkKnowQuestion> questionList = null;
    if(type.longValue() == 5L){
		questionList = knowDAO.searchSolvedQuestionListAll(keyword,bcid,pageNow,pageSize);
	}else if (type.longValue() == 0L) {
      questionList = this.knowDAO.searchUnsolvedQuestionList(keyword, bcid, pageNow, pageSize);
    }
    else if (type.longValue() == 1L) {
      questionList = this.knowDAO.searchSolvedQuestionList(keyword, bcid, pageNow, pageSize);
    }
    if (questionList != null) {
      for (IworkKnowQuestion model : questionList) {
        Map map = new HashMap();
        if (model != null) {
          map.put("qbigc", model.getIworkKnowClasses().getId());
          map.put("className", model.getIworkKnowClasses().getCname() == null ? "" : model.getIworkKnowClasses().getCname());
          map.put("score", model.getScore());
          map.put("id", model.getId());
          map.put("quid", model.getQuid());
          map.put("qcontent", model.getQcontent());
          map.put("qtype", model.getQtype());
          map.put("answerCount", Integer.valueOf(model.getIworkKnowAnswers().size()));
          String beginTime = getShowTime(model.getQbegintime());
          map.put("beginTime", beginTime);
          list.add(map);
        }
      }
    }
    return list;
  }

  public int getUnsolvedOrSolvedQuestionsNum(String keyword, Long bcid, Long type)
  {
    int num = 0;
    if(type.longValue() == 5L){
		num = knowDAO.searchUnsolvedQuestionListAll(keyword, bcid, -1, -1).size();
	}else if (type.longValue() == 0L) {
      num = this.knowDAO.searchUnsolvedQuestionList(keyword, bcid, -1, -1).size();
    }
    else if (type.longValue() == 1L) {
      num = this.knowDAO.searchSolvedQuestionList(keyword, bcid, -1, -1).size();
    }
    return num;
  }

  public List<Map<String, Object>> searchMyAskOrAnswerQuestionList(String keyword, Long bcid, int pageNow, int pageSize, String uid, Long type)
  {
    List list = new ArrayList();
    List<IworkKnowQuestion> questionList = null;
    if (type.longValue() == 2L) {
      questionList = this.knowDAO.searchMyAskQuestionList(keyword, bcid, pageNow, pageSize, uid);
    }
    else if (type.longValue() == 3L) {
      questionList = this.knowDAO.searchMyAnswerQuestionList(keyword, bcid, pageNow, pageSize, uid);
    }
    if (questionList != null)
      for (IworkKnowQuestion model : questionList) {
        Map map = new HashMap();
        if (model != null) {
          map.put("qbigc", model.getIworkKnowClasses().getId());
          map.put("className", model.getIworkKnowClasses().getCname() == null ? "" : model.getIworkKnowClasses().getCname());
          map.put("score", model.getScore());
          map.put("id", model.getId());
          map.put("quid", model.getQuid());
          map.put("qcontent", model.getQcontent());
          map.put("qtype", model.getQtype());
          map.put("answerCount", Integer.valueOf(model.getIworkKnowAnswers().size()));
          String beginTime = getShowTime(model.getQbegintime().toString());
          map.put("beginTime", beginTime);
          list.add(map);
        }
      }
    return list;
  }

  public int getMyAskOrAnswerQuestionsNum(String keyword, Long bcid, String uid, Long type)
  {
    int num = 0;
    if (type.longValue() == 2L) {
      num = this.knowDAO.searchMyAskQuestionList(keyword, bcid, -1, -1, uid).size();
    }
    else if (type.longValue() == 3L) {
      num = this.knowDAO.searchMyAnswerQuestionList(keyword, bcid, -1, -1, uid).size();
    }
    return num;
  }

  public List<Map<String, Object>> searchAskMeQuestionList(String keyword, Long bcid, int pageNow, int pageSize, OrgUser user)
  {
    List list = new ArrayList();
    List<IworkKnowQuestion> unsolvedList = this.knowDAO.searchAskMeQuestionList(keyword, bcid, pageNow, pageSize, user);
    if (unsolvedList != null) {
      for (IworkKnowQuestion model : unsolvedList) {
        Map map = new HashMap();
        if (model != null) {
          map.put("qbigc", model.getIworkKnowClasses().getId());
          map.put("className", model.getIworkKnowClasses().getCname() == null ? "" : model.getIworkKnowClasses().getCname());
          map.put("score", model.getScore());
          map.put("id", model.getId());
          map.put("quid", model.getQuid());
          map.put("qcontent", model.getQcontent());
          map.put("qtype", model.getQtype());
          map.put("answerCount", Integer.valueOf(model.getIworkKnowAnswers().size()));
          String beginTime = getShowTime(model.getQbegintime());
          map.put("beginTime", beginTime);
          list.add(map);
        }
      }
    }
    return list;
  }

  public String getSearchResultStr(String keyword, Long bcid)
  {
    StringBuffer searchResult = new StringBuffer();
    boolean flag = true;
    if ("".equals(keyword)) {
      if (bcid.longValue() != 0L) {
        searchResult.append("您查看的分类是：<span style='color:#C00102'>[" + this.knowDAO.getClassModel(bcid).getCname() + "]");
      }
      else
        flag = false;
    }
    else
    {
      searchResult.append("您搜索的关键字是：<span class='oasearch_fontcolor'>\"" + keyword + "\"");
      if (bcid.longValue() != 0L) {
        searchResult.append("+[" + this.knowDAO.getClassModel(bcid).getCname() + "]");
      }

    }

    if (flag) {
      searchResult.append("</span> (待解决问题：<span class='oasearch_fontcolor'>" + 
        getUnsolvedOrSolvedQuestionsNum(keyword, bcid, Long.valueOf(0L)) + "</span>；已解决问题：<span class='oasearch_fontcolor'>" + 
        getUnsolvedOrSolvedQuestionsNum(keyword, bcid, Long.valueOf(1L)) + "</span>)");
    }
    return searchResult.toString();
  }

  public String getShowTime(String createTime)
  {
    String time = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = new Date();
      String today = sdf.format(date);
      today = today + " 00:00:00";
      Date jintian = sdf1.parse(today);
      if (sdf1.parse(createTime).before(jintian))
        time = createTime.substring(0, 10);
      else
        time = "今日 " + createTime.substring(11, 16);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return time;
  }

  public List<Map<String, Object>> getBigClasses()
  {
    List list = new ArrayList();
    List<IworkKnowClasses> classes = this.knowDAO.getClassList(0);
    if (classes != null)
      for (IworkKnowClasses model : classes) {
        Map map = new HashMap();
        if (model != null) {
          map.put("id", model.getId());
          map.put("cname", model.getCname());
          Long qcount = this.knowDAO.getQuestionCountByCid(model.getId(), "b");
          map.put("qcount", qcount);
          List innerlist = new ArrayList();
          String experts = model.getCexpert();
          if (experts != null) {
            String[] expertArray = experts.split(",");
            for (String expert : expertArray) {
              Map innermap = new HashMap();
              if ((expert != null) && (!"".equals(expert.trim())) && 
                (UserContextUtil.getInstance().checkAddress(expert.trim()))) {
                String uidname = UserContextUtil.getInstance().getFullUserAddress(expert.trim());
                String uid = UserContextUtil.getInstance().getUserId(uidname);
                String uname = UserContextUtil.getInstance().getUserName(uidname);
                innermap.put("uidname", uidname);
                innermap.put("uid", uid);
                innermap.put("uname", uname);
                innerlist.add(innermap);
              }
            }
          }

          map.put("experts", innerlist);
          list.add(map);
        }
      }
    return list;
  }

  public List<Map<String, Object>> getQuestionDetail(Long qid)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    List list = new ArrayList();
    IworkKnowQuestion question = this.knowDAO.getQuestionModel(qid);
    if (question != null) {
      question.setClickcount(Long.valueOf(question.getClickcount().longValue() + 1L));
      this.knowDAO.updateQuestionModel(question);
      if (question != null) {
        Map map = new HashMap();
        map.put("qid", question.getId());
        map.put("qbigc", question.getIworkKnowClasses().getId());
        map.put("className", question.getIworkKnowClasses().getCname() == null ? "" : question.getIworkKnowClasses().getCname());
        map.put("score", question.getScore());
        map.put("qtags", question.getQtags() == null ? "" : question.getQtags().trim());
        map.put("quid", question.getQuid() == null ? null : question.getQuid().trim());
        map.put("quname", question.getQuname() == null ? null : question.getQuname().trim());
        map.put("qcontent", question.getQcontent());
        map.put("qtype", question.getQtype());
        map.put("clickcount", question.getClickcount() == null ? new Long(0L) : question.getClickcount());
        map.put("beginTime", question.getQbegintime());
        map.put("shownametype", question.getShownametype());
        String answerbody = question.getAnswerbody();
        map.put("answerbody", answerbody == null ? null : getUserDividedByFenHao(answerbody));
        map.put("answerCount", Integer.valueOf(getAnswerList(question.getId(), 1).size()));
        map.put("answerlist", getAnswerList(question.getId(), 1));
        if (question.getQtype().longValue() == 1L) {
          map.put("bestanswer", getAnswerList(question.getId(), 0));
        }
        map.put("qfileList", this.uploadifyService.getFileUploads(FileUpload.class, question.getFileuuids()));
        list.add(map);
      }
    }

    return list;
  }

  public String getUserDividedByFenHao(String answerbody)
  {
    String anbodys = null;
    if ((answerbody != null) && (!"".equals(answerbody.trim()))) {
      anbodys = answerbody.trim().replace(",", "；");
      int len = anbodys.length();
      if (anbodys.lastIndexOf("；") == len - 1) {
        anbodys = anbodys.substring(0, len - 1);
      }
    }
    return anbodys.trim();
  }

  public List<Map<String, Object>> getAnswerList(Long qid, int type)
  {
    List answerlist = new ArrayList();
    List iworkKnowAnswers = this.knowDAO.getAnswerList(qid);
    if (iworkKnowAnswers != null) {
      Iterator iterator = iworkKnowAnswers.iterator();
      while (iterator.hasNext()) {
        Map map2 = new HashMap();
        IworkKnowAnswer answer = (IworkKnowAnswer)iterator.next();
        if ((answer != null) && 
          ((type != 1) || (answer.getAtype().longValue() != 1L)) && (
          (type != 0) || (answer.getAtype().longValue() == 1L))) {
          map2.put("aid", answer.getId());
          map2.put("auid", answer.getAuid());
          map2.put("auname", answer.getAuname());
          map2.put("acontent", answer.getAcontent());
          map2.put("atime", answer.getAtime());
          map2.put("atype", answer.getAtype());
          String invitedman = answer.getInvitedman();
          map2.put("invitedman", invitedman == null ? null : getUserDividedByFenHao(invitedman));
          Long delNum = new Long(0L);
          if ((invitedman == null) || ("".equals(invitedman.trim()))) {
            delNum = new Long(1L);
          }
          map2.put("delNum", delNum);
          map2.put("holdout", Integer.valueOf(getHoldSize(answer.getId())));
          map2.put("talkcount", Integer.valueOf(getTalkList(answer.getId()).size()));
          map2.put("talklist", getTalkList(answer.getId()));
          map2.put("ishold", isHoldOut(answer.getId()));
          map2.put("afileList", this.uploadifyService.getFileUploads(FileUpload.class, answer.getFileuuids()));
          answerlist.add(map2);
        }
      }
    }
    return answerlist;
  }

  public List<Map<String, Object>> getTalkList(Long aid)
  {
    List talklist = new ArrayList();
    List iworkKnowTalks = this.knowDAO.getTalkList(aid);
    if (iworkKnowTalks != null) {
      Iterator iterator2 = iworkKnowTalks.iterator();
      while (iterator2.hasNext()) {
        Map map3 = new HashMap();
        IworkKnowTalk iworkKnowTalk = (IworkKnowTalk)iterator2.next();
        if (iworkKnowTalk != null) {
          map3.put("tid", iworkKnowTalk.getId());
          map3.put("talkman", iworkKnowTalk.getTalkman());
          map3.put("talkname", iworkKnowTalk.getTalkname());
          map3.put("talkcontent", iworkKnowTalk.getTalkcontent());
          map3.put("talktime", iworkKnowTalk.getTalktime());
          map3.put("status", iworkKnowTalk.getStatus());
          talklist.add(map3);
        }
      }
    }
    return talklist;
  }

  public String isHoldOut(Long aid)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    IworkKnowAnswer answer = this.knowDAO.getAnswerModel(aid);
    Set iworkKnowHoldoutLogs = answer.getIworkKnowHoldoutLogs();
    String bool = "false";
    if (iworkKnowHoldoutLogs != null) {
      Iterator iterator3 = iworkKnowHoldoutLogs.iterator();
      while (iterator3.hasNext()) {
        IworkKnowHoldoutLog iworkKnowHoldoutLog = (IworkKnowHoldoutLog)iterator3.next();
        if ((iworkKnowHoldoutLog != null) && 
          (iworkKnowHoldoutLog.getHoldoutman().trim().equals(user.getUserid().trim()))) {
          bool = "true";
          break;
        }
      }
    }
    return bool;
  }

  public int getHoldSize(Long aid)
  {
    IworkKnowAnswer answer = this.knowDAO.getAnswerModel(aid);
    return answer.getIworkKnowHoldoutLogs().size();
  }

  public void updateAnswerType(Long aid, int type)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    IworkKnowAnswer answer = this.knowDAO.getAnswerModel(aid);
    if (type == 0) {
      if (isAdmin(user) == "true")
        answer.setAtype(new Long(3L));
      else {
        answer.setAtype(new Long(2L));
      }
      String aFileUUIDs = answer.getFileuuids();
      if (aFileUUIDs != null) {
        this.uploadifyService.deleteAll(FileUpload.class, aFileUUIDs);
      }
      answer.setFileuuids("");

      updateIndex(answer.getIworkKnowQuestion());
    }
    else if (type == 1) {
      answer.setAtype(new Long(1L));
    }
    this.knowDAO.updateAnswerModel(answer);
  }

  public void setAnswerBest(Long aid)
  {
    updateAnswerType(aid, 1);
    IworkKnowAnswer answer = this.knowDAO.getAnswerModel(aid);
    IworkKnowQuestion question = answer.getIworkKnowQuestion();
    if (question != null) {
      question.setQtype(new Long(1L));
      this.knowDAO.updateQuestionModel(question);
      if (!question.getQuid().trim().equals(answer.getAuid().trim())) {
        Long questionScore = question.getScore();
        Long sysScore = KnowScoreConstant.BEST_ANSWER_SCORE;
        updateScore(Long.valueOf(questionScore.longValue() + sysScore.longValue()), answer.getAuid());
        updateScore(Long.valueOf(0L - questionScore.longValue()), question.getQuid());
      }
    }
    String toPeople = answer.getAuid() + "[" + answer.getAuname() + "]";
    String title = "【OA知道】中你的回答被选为最佳回答，恭喜！";
    String content = "恭喜你！<br/>你的回答被选为最佳回答！";
    sendMessage(toPeople, question.getId(), title, content);
  }

  public String isAdmin(OrgUser user)
  {
    String isAdmin = "false";
    if (("ADMIN".equals(user.getUserid())) || ("KOADMIN".equals(user.getUserid()))) {
      isAdmin = "true";
    }
    return isAdmin;
  }

  public void holdOutAnswer(Long aid)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    Map params = new HashMap();
    params.put(1, aid);
    params.put(2, user.getUserid());
    params.put(3, user.getUsername());
    String num = DBUtil.getDataStr("NUM", "SELECT COUNT(*) AS NUM FROM Iwork_Know_Holdout_Log WHERE AID=? AND HOLDOUTMAN=? AND HOLDOUTNAME=?" , params);
    if(num!=null&&num.equals("0")){
    	IworkKnowAnswer iworkKnowAnswer = this.knowDAO.getAnswerModel(aid);
    	IworkKnowHoldoutLog holdoutlog = new IworkKnowHoldoutLog(iworkKnowAnswer, user.getUserid(), user.getUsername());
    	this.knowDAO.saveHoldoutLogModel(holdoutlog);
    	updateScore(KnowScoreConstant.HOLDOUT_SCORE, iworkKnowAnswer.getAuid());
    	ResponseUtil.writeTextUTF8("ok");
    }
  }

  public IworkKnowAnswer initAnswer()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    IworkKnowAnswer answer = new IworkKnowAnswer();

    answer.setAuid(user.getUserid());
    answer.setAuname(user.getUsername());

    answer.setAtype(new Long(0L));
    return answer;
  }

  public IworkKnowTalk initTalk()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    IworkKnowTalk talk = new IworkKnowTalk();
    talk.setStatus(new Long(0L));
    talk.setTalkman(user.getUserid());
    talk.setTalkname(user.getUsername());

    return talk;
  }

  public void saveTalk(Long aid, IworkKnowTalk talk)
  {
    IworkKnowAnswer answer = this.knowDAO.getAnswerModel(aid);
    talk.setIworkKnowAnswer(answer);
    talk.setTalktime(UtilDate.getNowDatetime());
    this.knowDAO.saveTalkModel(talk);
  }

  public void updateTalkType(Long tid)
  {
    IworkKnowTalk talk = this.knowDAO.getTalkModel(tid);
    talk.setStatus(new Long(1L));
    this.knowDAO.updateTalkModel(talk);
  }

  public boolean isAnswerExist(Long id)
  {
    IworkKnowAnswer model = this.knowDAO.getAnswerModel(id);
    if (model == null) {
      return false;
    }
    return true;
  }

  public void sendMessage(String receiver, Long qid, String title, String content)
  {
    SysMessage sms = new SysMessage();
    sms.setPriority(0);
    sms.setType(0);
    sms.setStatus(0);
    sms.setUrlTarget(0);
    sms.setTitle(title);
    sms.setContent(content);
    HttpServletRequest request = ServletActionContext.getRequest();
    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/know_open_question.action?qid=" + qid;
    sms.setUrl(url);

    List receiverList = getReceiverList(receiver);
    try {
      this.sysMessageService.sendMessageToUserList(sms, receiverList);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public IworkKnowClasses initBigClass()
  {
    IworkKnowClasses bigClass = new IworkKnowClasses();
    bigClass.setCtype("可用");
    bigClass.setCorder(Long.valueOf(this.knowDAO.getNextCorder()));
    return bigClass;
  }

  public void updateKeywordLog(String keywords)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    String[] keywordArray = keywords.split(" ");
    for (String keyword : keywordArray)
      if ((keyword != null) && (!"".equals(keyword.trim()))) {
        IworkKnowKeywordLog model = this.knowDAO.getKeywordLog(keyword);
        if (model == null) {
          IworkKnowKeywordLog newlog = new IworkKnowKeywordLog(keyword, new Long(0L), UtilDate.getNowDatetime(), user.getUsername(), user.getUserid());
          this.knowDAO.saveKeywordLog(newlog);
        } else {
          model.setLastpp(user.getUsername());
          model.setLastppid(user.getUserid());
          model.setLasttime(UtilDate.getNowDatetime());
          model.setSearchcount(Long.valueOf(model.getSearchcount().longValue() + 1L));
          this.knowDAO.updateKeywordLog(model);
        }
      }
  }

  public void saveAnswerModel(IworkKnowAnswer answer)
  {
    this.knowDAO.saveAnswerModel(answer);

    updateIndex(answer.getIworkKnowQuestion());
  }

  public void updateAnswerModel(IworkKnowAnswer answer)
  {
    this.knowDAO.updateAnswerModel(answer);

    updateIndex(answer.getIworkKnowQuestion());
  }

  public void setKnowDAO(KnowDAO knowDAO)
  {
    this.knowDAO = knowDAO;
  }
  public KnowDAO getKnowDAO() {
    return this.knowDAO;
  }
  public SysMessageService getSysMessageService() {
    return this.sysMessageService;
  }
  public void setSysMessageService(SysMessageService sysMessageService) {
    this.sysMessageService = sysMessageService;
  }
  public FileUploadService getUploadifyService() {
    return this.uploadifyService;
  }
  public void setUploadifyService(FileUploadService uploadifyService) {
    this.uploadifyService = uploadifyService;
  }
}