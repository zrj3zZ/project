package com.iwork.km.know.action;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.km.know.constant.KnowScoreConstant;
import com.iwork.km.know.model.IworkKnowAnswer;
import com.iwork.km.know.model.IworkKnowClasses;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.km.know.model.IworkKnowTalk;
import com.iwork.km.know.model.IworkKnowUser;
import com.iwork.km.know.service.KnowService;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

public class KnowAction extends ActionSupport
{
  private KnowService knowService;
  private String keyword;
  private Long bcid;
  private String yg;
  private List<IworkKnowClasses> classList;
  private IworkKnowQuestion question;
  private String term;
  private Long score;
  private List<Map<String, Object>> questionList;
  private String isAdmin;
  private List<Map<String, Object>> questionClasses;
  private List<IworkKnowUser> top10ScoreUserList;
  private Long myscore;
  private int pageNow;
  private int pageSize;
  private int total;
  private Long type;
  private Long qid;
  private Long aid;
  private List<Map<String, Object>> questionDetail;
  private String loginuid;
  private IworkKnowAnswer answer;
  private IworkKnowTalk talk;
  private Long tid;
  private String searchResult;
  private String selectHtml;
  private String currentPage;
  private String pagerMethod;
  private String totalRows;
  private PagerService pagerService;
  private Pager pager;
  private IworkKnowClasses model;
  private File uploadify;
  private String uploadifyFileName;
  private String fileUUID;
  private FileUploadService uploadifyService;
  private String parentHidId;
  private String oldUUIDs;
  private String parentFileDivId;
  private String sizeLimit = "";
  private String multi = "true";
  private String fileExt = "";
  private String fileDesc = "";
  private List<FileUpload> files;

  public String getKnowIndexPage()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    this.questionClasses = this.knowService.getBigClasses();
    this.myscore = this.knowService.getKnowDAO().getMyScoreByUid(user.getUserid());
    this.top10ScoreUserList = this.knowService.getKnowDAO().getTop10ScoreUserList();
    this.pageNow = 1;
    this.pageSize = 10;
    return "success";
  }

  public String getTabContent()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    this.isAdmin = this.knowService.isAdmin(user);
    this.searchResult = "";
    if (this.type != null) {
      if ((this.keyword == null) || ("".equals(this.keyword.trim()))) {
        this.keyword = "";
      }
      if (this.bcid == null) {
        this.bcid = new Long(0L);
      }
      if ((this.bcid.longValue() != 0L) && 
        (!"NEEQMANAGER".equals(user.getUserid()))) {
        this.model = this.knowService.getKnowDAO().getClassModel(this.bcid);
        if ((this.model.getCexpert() != null) && (!"".equals(this.model.getCexpert()))) {
          boolean flag = false;
          String[] experts = this.model.getCexpert().split(",");
          for (int i = 0; i < experts.length; i++) {
            if (!flag) {
              String expert = experts[i];
              if (expert.indexOf("[") > -1) {
                expert = expert.substring(0, expert.indexOf("["));
              }
              flag = expert.equals(user.getUserid());
            }
          }
          if (flag)
            this.isAdmin = "true";
          else {
            this.isAdmin = "false";
          }
        }
      }

      this.searchResult = this.knowService.getSearchResultStr(this.keyword, this.bcid);
      this.knowService.updateKeywordLog(this.keyword);
      if(type!=null){
	      if ((this.type.longValue() == 0L) || (this.type.longValue() == 1L)) {
	        this.questionList = this.knowService.searchUnsolvedOrSolvedQuestionList(this.keyword, this.bcid, this.pageNow, this.pageSize, this.type);
	        this.total = this.knowService.getUnsolvedOrSolvedQuestionsNum(this.keyword, this.bcid, this.type);
	      }
	      else if ((this.type.longValue() == 2L) || (this.type.longValue() == 3L)) {
	        this.questionList = this.knowService.searchMyAskOrAnswerQuestionList(this.keyword, this.bcid, this.pageNow, this.pageSize, user.getUserid(), this.type);
	        this.total = this.knowService.getMyAskOrAnswerQuestionsNum(this.keyword, this.bcid, user.getUserid(), this.type);
	      }
	      else if (this.type.longValue() == 4L) {
	        this.questionList = this.knowService.searchAskMeQuestionList(this.keyword, this.bcid, this.pageNow, this.pageSize, user);
	        this.total = this.knowService.getKnowDAO().searchAskMeQuestionList(this.keyword, this.bcid, -1, -1, user).size();
	      }else if(this.type.longValue() == 5L){
	    	  questionList = knowService.searchUnsolvedOrSolvedQuestionList(keyword,bcid,pageNow,pageSize,type);   //0待解决问题,1已解决问题
	    	  total = knowService.getUnsolvedOrSolvedQuestionsNum(keyword, bcid,type);  
	      }
      }
    }
    return "success";
  }

  public String askQuestion()
  {
    this.classList = this.knowService.getKnowDAO().getClassList(0);
    this.question = this.knowService.initQuestion();
    if ((this.keyword != null) && (!"".equals(this.keyword))) {
      try {
        this.keyword = URLDecoder.decode(this.keyword, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      this.question.setQcontent(this.keyword);
    }
    if (this.bcid != null) {
      IworkKnowClasses classes = this.knowService.getKnowDAO().getClassModel(this.bcid);
      this.question.setIworkKnowClasses(classes);
    }
    if ((this.yg != null) && (!"".equals(this.yg))) {
      this.yg = UserContextUtil.getInstance().getFullUserAddress(this.yg);
      this.question.setAnswerbody(this.yg);
    }
    return "success";
  }

  public String editAnswer()
  {
    if ((this.aid != null) && (this.knowService.getKnowDAO().getAnswerModel(this.aid) != null)) {
      this.answer = this.knowService.getKnowDAO().getAnswerModel(this.aid);
      if ((this.answer.getInvitedman() == null) || ("".equals(this.answer.getInvitedman().trim()))) {
        this.answer.setInvitedman("多个被邀请回答人请用逗号分隔");
      }
      String uuids = this.answer.getFileuuids();
      this.files = this.uploadifyService.getFileUploads(FileUpload.class, uuids);
    }
    else {
      this.answer = this.knowService.initAnswer();
    }
    return "success";
  }

  public void saveAnswer()
  {
    if (this.answer != null) {
      boolean isSave = false;
      IworkKnowQuestion questionModel = null;
      if (this.qid != null) {
        questionModel = this.knowService.getKnowDAO().getQuestionModel(this.qid);
        this.answer.setIworkKnowQuestion(questionModel);
      }
      if (this.knowService.isAnswerExist(this.answer.getId())) {
        this.knowService.updateAnswerModel(this.answer);
      } else {
        this.answer.setAtime(UtilDate.getNowDatetime());
        this.knowService.saveAnswerModel(this.answer);
        isSave = true;
      }
      if ((isSave) && (questionModel != null)) {
        if (questionModel.getIworkKnowAnswers().size() == 0) {
          this.knowService.updateScore(KnowScoreConstant.FIRST_ANSWER_SCORE, this.answer.getAuid());
        }
        else {
          this.knowService.updateScore(KnowScoreConstant.ANSWER_SCORE, this.answer.getAuid());
        }
      }
      String qcontent = questionModel.getQcontent().replaceAll("__eol__", "<br/>");
      String title = "【OA知道】中有人回答了你的提问";
      String content = "OA系统中【OA知道】频道，有位同事回答了你的提问：" + qcontent + "<br/>";
      String askUser = questionModel.getQuid() + "[" + questionModel.getQuname() + "]";
      this.knowService.sendMessage(askUser, questionModel.getId(), title, content);

      if ((this.answer.getInvitedman() != null) && (!"".equals(this.answer.getInvitedman().trim()))) {
        String title2 = "【OA知道】中有人问你呢";
        StringBuffer content2 = new StringBuffer();
        content2.append(" 有位同事在【OA知道】向你请教问题，赶快点击右上角的“OA知道”看看吧！<br/>");
        if (questionModel.getShownametype().longValue() == 0L)
          content2.append("【提问人】匿名");
        else {
          content2.append("【提问人】" + questionModel.getQuname() + "(" + questionModel.getQuid() + ")");
        }
        this.knowService.sendMessage(this.answer.getInvitedman().trim(), questionModel.getId(), title2, content2.toString());
      }
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void autoGetUserInfo()
  {
    String json = "";
    if (this.term != null) {
      json = this.knowService.getKnowDAO().autoGetUserInfo(this.term);
    }
    ResponseUtil.write(json);
  }

  public void checkScoreIsEnough()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    String msg = "";
    if ((this.score != null) && 
      (this.score.longValue() != 0L)) {
      Long myScore = this.knowService.getKnowDAO().getMyScoreByUid(user.getUserid());
      if (this.score.longValue() > myScore.longValue()) {
        msg = "积分不足！您现在有" + myScore + "积分！";
      }
    }

    ResponseUtil.writeTextUTF8(msg);
  }

  public void checkUserIsExist()
  {
    StringBuffer msg = new StringBuffer();
    if (this.yg != null) {
      String[] ygs = this.yg.split(",");
      for (String user : ygs) {
        if ((user != null) && (!"".equals(user))) {
          boolean bool = UserContextUtil.getInstance().checkAddress(user.trim());
          if (!bool)
            msg.append("【" + user + "】不存在，");
        }
      }
    }
    ResponseUtil.writeTextUTF8(msg.toString());
  }

  public String openQuestion()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    this.questionDetail = new ArrayList();
    this.answer = new IworkKnowAnswer();
    this.talk = new IworkKnowTalk();
    if ((this.qid != null) && (this.qid.longValue() != 0L)) {
      this.questionDetail = this.knowService.getQuestionDetail(this.qid);
      this.answer = this.knowService.initAnswer();
      this.talk = this.knowService.initTalk();
    }
    this.loginuid = user.getUserid();
    this.isAdmin = this.knowService.isAdmin(user);
    return "success";
  }

  public String editQuestion()
  {
    this.classList = this.knowService.getKnowDAO().getClassList(0);
    if ((this.qid != null) && (this.knowService.getKnowDAO().getQuestionModel(this.qid) != null)) {
      this.question = this.knowService.getKnowDAO().getQuestionModel(this.qid);
      if ((this.question.getAnswerbody() == null) || ("".equals(this.question.getAnswerbody().trim()))) {
        this.question.setAnswerbody("多个被邀请回答人请用逗号分隔");
      }
      String uuids = this.question.getFileuuids();
      this.files = this.uploadifyService.getFileUploads(FileUpload.class, uuids);
    }
    else {
      this.question = this.knowService.initQuestion();
    }
    return "success";
  }

  public void saveQuestion()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    if (this.question != null) {
      if (this.question.getIworkKnowClasses().getId() != null) {
        IworkKnowClasses classes = this.knowService.getKnowDAO().getClassModel(this.question.getIworkKnowClasses().getId());
        this.question.setIworkKnowClasses(classes);
      }
      if ((this.question.getId() != null) && (this.knowService.isQuestionExist(this.question.getId()))) {
        this.knowService.update(this.question);
      }
      else {
        this.knowService.save(this.question);
        this.knowService.updateScore(KnowScoreConstant.ASK_SCORE, user.getUserid());
      }
      if ((this.question.getAnswerbody() != null) && (!"".equals(this.question.getAnswerbody().trim()))) {
        String title = "【OA知道】中有人问你呢";
        StringBuffer content = new StringBuffer();
        content.append(" 有位同事在【OA知道】向你请教问题，赶快点击右上角的“OA知道”看看吧！<br/>");
        if (this.question.getShownametype().longValue() == 0L)
          content.append("【提问人】匿名");
        else {
          content.append("【提问人】" + user.getUsername() + "(" + user.getUserid() + ")");
        }
        this.knowService.sendMessage(this.question.getAnswerbody().trim(), this.question.getId(), title, content.toString());
      }
      if ((this.question.getQtags() != null) && (!"".equals(this.question.getQtags().trim()))) {
        String qtags = this.question.getQtags().trim();
        String[] tags = qtags.split(",");
        String className = this.question.getIworkKnowClasses().getCname() == null ? "" : this.question.getIworkKnowClasses() == null ? "" : this.question.getIworkKnowClasses().getCname();
        for (String tag : tags) {
          if ((tag != null) && (!"".equals(tag.trim())))
            this.knowService.updateTagLog(this.question.getIworkKnowClasses(), tag.trim());
        }
      }
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void delQuestion()
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    String flag = "false";
    if (this.knowService.isAdmin(user) == "true") {
      flag = "true";
    }
    if (this.qid != null) {
      IworkKnowQuestion question = this.knowService.getKnowDAO().getQuestionModel(this.qid);
      if ((question.getIworkKnowClasses().getCexpert() != null) && (!"".equals(question.getIworkKnowClasses().getCexpert()))) {
        String[] experts = question.getIworkKnowClasses().getCexpert().split(",");
        for (int i = 0; i < experts.length; i++) {
          if (flag != "true") {
            String expert = experts[i];
            if (expert.indexOf("[") > -1) {
              expert = expert.substring(0, expert.indexOf("["));
            }
            if (expert.equals(user.getUserid())) {
              flag = "true";
            }
          }
        }
      }
      if (flag == "true") {
        question.setQtype(new Long(2L));
        this.knowService.update(question);
      }
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void holdOutAnswer()
  {
    if ((this.aid != null) && (this.aid.longValue() != 0L)) {
      this.knowService.holdOutAnswer(this.aid);
    }
  }

  public void delAnswer()
  {
    if (this.aid != null) {
      this.knowService.updateAnswerType(this.aid, 0);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void setAnswerBest()
  {
    if (this.aid != null) {
      this.knowService.setAnswerBest(this.aid);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void saveTalk()
  {
    if ((this.talk != null) && (this.aid != null)) {
      this.knowService.saveTalk(this.aid, this.talk);
      IworkKnowAnswer answer = this.knowService.getKnowDAO().getAnswerModel(this.aid);
      this.knowService.updateScore(KnowScoreConstant.TALK_SCORE, this.talk.getTalkman());
      String title = "【OA知道】中有人评论了你的回答";
      String content = "OA系统中【OA知道】频道，有位同事评论了你的回答：" + answer.getAcontent().replaceAll("__eol__", "</br>") + "</br>";
      String toPeople = answer.getAuid() + "[" + answer.getAuname() + "]";
      this.knowService.sendMessage(toPeople, answer.getIworkKnowQuestion().getId(), title, content);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void delTalk()
  {
    if (this.tid != null) {
      this.knowService.updateTalkType(this.tid);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public String BigClassManager()
  {
    int totalRow = this.knowService.getKnowDAO().getClassList(1).size();
    this.pager = this.pagerService.getPager(getCurrentPage(), getPagerMethod(), totalRow);
    setCurrentPage(String.valueOf(this.pager.getCurrentPage()));
    setTotalRows(String.valueOf(totalRow));
    this.classList = this.knowService.getKnowDAO().getClassList(this.pager.getPageSize(), this.pager.getStartRow());
    return "success";
  }

  public String addBigClass()
  {
    this.model = this.knowService.initBigClass();
    return "success";
  }

  public String editBigClass()
  {
    if ((this.bcid != null) && (this.knowService.getKnowDAO().getClassModel(this.bcid) != null))
      this.model = this.knowService.getKnowDAO().getClassModel(this.bcid);
    else {
      this.model = this.knowService.initBigClass();
    }
    return "success";
  }

  public void saveBigClass()
  {
    if (this.model != null) {
      if ((this.model.getId() != null) && (this.knowService.getKnowDAO().getClassModel(this.model.getId()) != null))
        this.knowService.getKnowDAO().updateBigClassModel(this.model);
      else {
        this.knowService.getKnowDAO().saveBigClassModel(this.model);
      }
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void isQuestionExist()
  {
    String returnStr = "true";
    if (this.bcid != null) {
      returnStr = this.knowService.isQueUnBclassExist(this.bcid);
    }
    ResponseUtil.writeTextUTF8(returnStr);
  }

  public String delBigClass()
  {
    if ((this.bcid != null) && 
      (this.knowService.isQueUnBclassExist(this.bcid) == "false")) {
      String relateFileUUIDs = this.knowService.getKnowDAO().delBigClassAllInfo(this.bcid);
      if ((relateFileUUIDs != null) && (!"".equals(relateFileUUIDs))) {
        this.uploadifyService.deleteAll(FileUpload.class, relateFileUUIDs);
      }
    }

    return "success";
  }

  public String startBigClass()
  {
    if (this.bcid != null) {
      IworkKnowClasses bigClass = this.knowService.getKnowDAO().getClassModel(this.bcid);
      bigClass.setCtype("可用");
      this.knowService.getKnowDAO().updateBigClassModel(bigClass);
    }
    return "success";
  }

  public String stopBigClass()
  {
    if (this.bcid != null) {
      IworkKnowClasses bigClass = this.knowService.getKnowDAO().getClassModel(this.bcid);
      bigClass.setCtype("停用");
      this.knowService.getKnowDAO().updateBigClassModel(bigClass);
    }
    return "success";
  }

  public String showExperts()
  {
    if (this.bcid != null) {
      IworkKnowClasses bigClass = this.knowService.getKnowDAO().getClassModel(this.bcid);
      this.selectHtml = (bigClass.getCexpert() == null ? "" : bigClass.getCexpert().replaceAll(",", "<br/>"));
    }
    return "success";
  }

  public String setCorderDown()
  {
    if (this.bcid != null) {
      this.knowService.getKnowDAO().updateCorder("down", this.bcid);
    }
    return "success";
  }

  public String setCorderUp()
  {
    if (this.bcid != null) {
      this.knowService.getKnowDAO().updateCorder("up", this.bcid);
    }
    return "success";
  }

  public String showUploadifyPage()
  {
    return "success";
  }

  public String upload()
    throws Exception
  {
    FileUpload model = this.uploadifyService.save(this.uploadify, this.uploadifyFileName);
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setCharacterEncoding("utf-8");
    if (model != null) {
      String uuid = model.getFileId();
      String url = model.getFileUrl();
      response.getWriter().print("{flag:true,uuid:'" + uuid + "',url:'" + url + "'}");
    } else {
      response.getWriter().print("{flag:false}");
      throw new Exception(this.uploadifyFileName + "上传失败");
    }
    return null;
  }

  public String remove()
    throws Exception
  {
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setCharacterEncoding("utf-8");
    if ((this.fileUUID != null) && (!this.fileUUID.equals(""))) {
      this.uploadifyService.deleteFileUpload(FileUpload.class, this.fileUUID);
      response.getWriter().print("{flag:true}");
    } else {
      response.getWriter().print("{flag:false}");
    }
    return null;
  }

  public String download()
  {
    this.uploadifyService.downLoadFile(FileUpload.class, this.fileUUID, ServletActionContext.getResponse());
    return null;
  }

  public void fileUUIDSynchron()
  {
    if ((this.qid != null) && (this.oldUUIDs != null)) {
      IworkKnowQuestion model = this.knowService.getKnowDAO().getQuestionModel(this.qid);
      if (model.getFileuuids() != this.oldUUIDs) {
        model.setFileuuids(this.oldUUIDs);
      }
      this.knowService.getKnowDAO().updateQuestionModel(model);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public void fileUUIDSynchronAnswer()
  {
    if ((this.aid != null) && (this.oldUUIDs != null)) {
      IworkKnowAnswer model = this.knowService.getKnowDAO().getAnswerModel(this.aid);
      if (model.getFileuuids() != this.oldUUIDs) {
        model.setFileuuids(this.oldUUIDs);
      }
      this.knowService.getKnowDAO().updateAnswerModel(model);
    }
    ResponseUtil.writeTextUTF8("ok");
  }

  public KnowService getKnowService() {
    return this.knowService;
  }

  public void setKnowService(KnowService knowService) {
    this.knowService = knowService;
  }
  public String getKeyword() {
    return this.keyword;
  }
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
  public Long getBcid() {
    return this.bcid;
  }
  public void setBcid(Long bcid) {
    this.bcid = bcid;
  }
  public String getYg() {
    return this.yg;
  }
  public void setYg(String yg) {
    this.yg = yg;
  }
  public IworkKnowQuestion getQuestion() {
    return this.question;
  }
  public void setQuestion(IworkKnowQuestion question) {
    this.question = question;
  }
  public List<IworkKnowClasses> getClassList() {
    return this.classList;
  }
  public void setClassList(List<IworkKnowClasses> classList) {
    this.classList = classList;
  }
  public String getTerm() {
    return this.term;
  }
  public void setTerm(String term) {
    this.term = term;
  }
  public Long getScore() {
    return this.score;
  }
  public void setScore(Long score) {
    this.score = score;
  }
  public String getIsAdmin() {
    return this.isAdmin;
  }
  public void setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
  }
  public List<Map<String, Object>> getQuestionClasses() {
    return this.questionClasses;
  }
  public void setQuestionClasses(List<Map<String, Object>> questionClasses) {
    this.questionClasses = questionClasses;
  }
  public Long getMyscore() {
    return this.myscore;
  }
  public void setMyscore(Long myscore) {
    this.myscore = myscore;
  }
  public List<IworkKnowUser> getTop10ScoreUserList() {
    return this.top10ScoreUserList;
  }
  public void setTop10ScoreUserList(List<IworkKnowUser> top10ScoreUserList) {
    this.top10ScoreUserList = top10ScoreUserList;
  }
  public List<Map<String, Object>> getQuestionList() {
    return this.questionList;
  }
  public void setQuestionList(List<Map<String, Object>> questionList) {
    this.questionList = questionList;
  }
  public int getPageNow() {
    return this.pageNow;
  }
  public void setPageNow(int pageNow) {
    this.pageNow = pageNow;
  }
  public Long getType() {
    return this.type;
  }
  public void setType(Long type) {
    this.type = type;
  }
  public Long getQid() {
    return this.qid;
  }
  public void setQid(Long qid) {
    this.qid = qid;
  }
  public List<Map<String, Object>> getQuestionDetail() {
    return this.questionDetail;
  }
  public void setQuestionDetail(List<Map<String, Object>> questionDetail) {
    this.questionDetail = questionDetail;
  }
  public String getLoginuid() {
    return this.loginuid;
  }
  public void setLoginuid(String loginuid) {
    this.loginuid = loginuid;
  }
  public Long getAid() {
    return this.aid;
  }
  public void setAid(Long aid) {
    this.aid = aid;
  }
  public IworkKnowAnswer getAnswer() {
    return this.answer;
  }
  public void setAnswer(IworkKnowAnswer answer) {
    this.answer = answer;
  }
  public IworkKnowTalk getTalk() {
    return this.talk;
  }
  public void setTalk(IworkKnowTalk talk) {
    this.talk = talk;
  }
  public Long getTid() {
    return this.tid;
  }
  public void setTid(Long tid) {
    this.tid = tid;
  }
  public int getPageSize() {
    return this.pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  public int getTotal() {
    return this.total;
  }
  public void setTotal(int total) {
    this.total = total;
  }
  public String getSearchResult() {
    return this.searchResult;
  }
  public void setSearchResult(String searchResult) {
    this.searchResult = searchResult;
  }
  public String getSelectHtml() {
    return this.selectHtml;
  }
  public void setSelectHtml(String selectHtml) {
    this.selectHtml = selectHtml;
  }
  public String getCurrentPage() {
    return this.currentPage;
  }
  public void setCurrentPage(String currentPage) {
    this.currentPage = currentPage;
  }
  public String getPagerMethod() {
    return this.pagerMethod;
  }
  public void setPagerMethod(String pagerMethod) {
    this.pagerMethod = pagerMethod;
  }
  public String getTotalRows() {
    return this.totalRows;
  }
  public void setTotalRows(String totalRows) {
    this.totalRows = totalRows;
  }
  public PagerService getPagerService() {
    return this.pagerService;
  }
  public void setPagerService(PagerService pagerService) {
    this.pagerService = pagerService;
  }
  public Pager getPager() {
    return this.pager;
  }
  public void setPager(Pager pager) {
    this.pager = pager;
  }
  public IworkKnowClasses getModel() {
    return this.model;
  }
  public void setModel(IworkKnowClasses model) {
    this.model = model;
  }
  public File getUploadify() {
    return this.uploadify;
  }
  public void setUploadify(File uploadify) {
    this.uploadify = uploadify;
  }
  public String getUploadifyFileName() {
    return this.uploadifyFileName;
  }
  public void setUploadifyFileName(String uploadifyFileName) {
    this.uploadifyFileName = uploadifyFileName;
  }
  public String getFileUUID() {
    return this.fileUUID;
  }
  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }
  public FileUploadService getUploadifyService() {
    return this.uploadifyService;
  }
  public void setUploadifyService(FileUploadService uploadifyService) {
    this.uploadifyService = uploadifyService;
  }
  public String getParentHidId() {
    return this.parentHidId;
  }
  public void setParentHidId(String parentHidId) {
    this.parentHidId = parentHidId;
  }
  public String getParentFileDivId() {
    return this.parentFileDivId;
  }
  public void setParentFileDivId(String parentFileDivId) {
    this.parentFileDivId = parentFileDivId;
  }
  public String getSizeLimit() {
    return this.sizeLimit;
  }
  public void setSizeLimit(String sizeLimit) {
    this.sizeLimit = sizeLimit;
  }
  public String getMulti() {
    return this.multi;
  }
  public void setMulti(String multi) {
    this.multi = multi;
  }
  public String getFileExt() {
    return this.fileExt;
  }
  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }
  public String getFileDesc() {
    return this.fileDesc;
  }
  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }
  public List<FileUpload> getFiles() {
    return this.files;
  }
  public void setFiles(List<FileUpload> files) {
    this.files = files;
  }
  public String getOldUUIDs() {
    return this.oldUUIDs;
  }
  public void setOldUUIDs(String oldUUIDs) {
    this.oldUUIDs = oldUUIDs;
  }
}