package com.ibpmsoft.project.zqb.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.common.ZQBConstants;
import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.ibpmsoft.project.zqb.service.ZqbProjectManageService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.conf.XmlcConf;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
public class ZqbProjectManageAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(ZqbProjectManageAction.class);
    private ZqbProjectManageService zqbProjectManageService;
    private List<HashMap> runList;
    private List<HashMap> finishList;
    private List<HashMap> closeList;
    private List<HashMap> taskList;
    private List<HashMap> xmcyggList;
    private HashMap hash;
    private List<HashMap> list;
    private  String projectNo;
    private String projectName;
    private String groupid;
    private String uuid;
    private String userid;
    private String question;
    private String zcPro;
    private Double rzje;
    private boolean add;
    private int ymid;
    private String startdate;
    private String enddate;
    //下面这个属性是用来存储页面AjaxPOST传入的项目名的
    private String projectno;
    private String pjr;
    private List<HashMap> listxmzl;
    private Long xmjsformid;
    private Long xmjsid;
    //数据选择器数据集合
    private List<HashMap> listxjxzq;
    private String prodate;
    private String beginprodate;
    private String endprodate;
    private String name;
    private String no;
    private String json;
    private String html;
    private int id;
    private int index;
    private Long instanceid;
    private Long taskid;
    private Long questionId;
    private Long score;
    private String instanceId;
    private String startDate;
    private String endDate;
    private String typePieData;
    private String reTalk;
    private String typePieData2;
    private String typePieData3;
    private String typePieData4;
    private String typeBarData;
    private String typeBarLabel;
    private boolean readonly;
    private boolean readwrite;
    private boolean superman;
    private boolean cwAndZkNh;
    private String username;
    private String createdate;
    private String type;
    private String searchkey;
    private List<UploadDocModel> doclist;
    private String typeBarLabel3;
    private String typeBarData3;
    private List<HashMap> xmcyxxList;
    private int pageNumber; // 当前页数
    private int pageNumber1; // 当前页数
    private int pageNumber3; // 当前页数
    private int pageNumber2; // 当前页数
    private int pageNumberXM;
    private int pageSizeXM = 10;
    private int xmNum;
    private String customername;
    private String content;
    private int htje;
    private int wsje;
    private String temp;
    private String dgzt;
    private String xmsplcServer;
    private Long formid;
    private Long demid;
    private Long xmrbid;
    private boolean ISVIEWDG;
    private String xmlcServer;
    private String attach;
    private String xmjd;
    private String gxsj;
    private int totalNum; // 总页数
    private int finishNum; // 总页数
    private int closeNum; // 总页数
    private int pageSize = 10; // 每页条数
    private int pageSize1 = 10; // 每页条数
    private int pageSize2 = 10; // 每页条数
    private int pageSize3 = 10; // 每页条数
    private String sssyb;
    private String cyrName;
    private boolean IsViewSearch;
    private String rzrq;
    private String xmlx;
    private String olddepartmentname;
    private String newdepartmentname;
    private String customerno;
    private String ggjzr;
    private List<HashMap> zklist;
    private int zklistsize;
    private Long zkformid;
    private Long zkid;
    private List<HashMap> nhlist;
    private int nhlistsize;
    private Long nhformid;
    private Long nhid;
    private String pjname;
    private String jdmc;
    private List jdmcList;
    private String ids;
    private String projectname;
    private List logList;
    private String isDwPj;//0：否，1：是。
    private boolean IsZkOrNh;
    private List<HashMap> dzList;
    private String dzid;
    private String status;
    private String zkspr;
    private String nhspr;
    private String issx;
    private String ordertype;
    private String gppjdemid;
    private String gppjformid;
    private String gpfxdemid;
    private String gpfxformid;
    private Long qtDemid;
    private Long qtFormid;
    private String commonstr;
    private String qtJdFormid;
    private String qtJdDemid;
    private String tzggSjxzqId;
    private String sszjj;
    private String szbm;
    private String gpdq;
    private String gprqks;
    private String gprqjs;
    private String ssbm;
    private String createuser;
    private String tracking;
    private String progress;
    private String tel;
    private String fj;
    private String projectdate;
    private String extend1;

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getProjectdate() {
        return projectdate;
    }

    public void setProjectdate(String projectdate) {
        this.projectdate = projectdate;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getSsbm() {
        return ssbm;
    }

    public void setSsbm(String ssbm) {
        this.ssbm = ssbm;
    }

    public String getGpdq() {
        return gpdq;
    }

    public void setGpdq(String gpdq) {
        this.gpdq = gpdq;
    }

    public String getGprqks() {
        return gprqks;
    }

    public void setGprqks(String gprqks) {
        this.gprqks = gprqks;
    }

    public String getGprqjs() {
        return gprqjs;
    }

    public void setGprqjs(String gprqjs) {
        this.gprqjs = gprqjs;
    }

    public String getSszjj() {
        return sszjj;
    }

    public void setSszjj(String sszjj) {
        this.sszjj = sszjj;
    }

    public String getSzbm() {
        return szbm;
    }

    public void setSzbm(String szbm) {
        this.szbm = szbm;
    }

    public String getTzggSjxzqId() {
        return tzggSjxzqId;
    }

    public void setTzggSjxzqId(String tzggSjxzqId) {
        this.tzggSjxzqId = tzggSjxzqId;
    }

    public String getGppjdemid() {
        return gppjdemid;
    }

    public void setGppjdemid(String gppjdemid) {
        this.gppjdemid = gppjdemid;
    }

    public String getGppjformid() {
        return gppjformid;
    }

    public void setGppjformid(String gppjformid) {
        this.gppjformid = gppjformid;
    }

    public String getGpfxdemid() {
        return gpfxdemid;
    }

    public void setGpfxdemid(String gpfxdemid) {
        this.gpfxdemid = gpfxdemid;
    }

    public String getGpfxformid() {
        return gpfxformid;
    }

    public void setGpfxformid(String gpfxformid) {
        this.gpfxformid = gpfxformid;
    }

    public Long getQtDemid() {
        return qtDemid;
    }

    public void setQtDemid(Long qtDemid) {
        this.qtDemid = qtDemid;
    }

    public Long getQtFormid() {
        return qtFormid;
    }

    public void setQtFormid(Long qtFormid) {
        this.qtFormid = qtFormid;
    }

    public String getCommonstr() {
        return commonstr;
    }

    public void setCommonstr(String commonstr) {
        this.commonstr = commonstr;
    }

    public String getQtJdFormid() {
        return qtJdFormid;
    }

    public void setQtJdFormid(String qtJdFormid) {
        this.qtJdFormid = qtJdFormid;
    }

    public String getQtJdDemid() {
        return qtJdDemid;
    }

    public void setQtJdDemid(String qtJdDemid) {
        this.qtJdDemid = qtJdDemid;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getIssx() {
        return issx;
    }

    public void setIssx(String issx) {
        this.issx = issx;
    }

    public Long getDemid() {
        return demid;
    }

    public void setDemid(Long demid) {
        this.demid = demid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDzid() {
        return dzid;
    }

    public void setDzid(String dzid) {
        this.dzid = dzid;
    }

    public List<HashMap> getDzList() {
        return dzList;
    }

    public void setDzList(List<HashMap> dzList) {
        this.dzList = dzList;
    }

    public boolean isIsZkOrNh() {
        return IsZkOrNh;
    }

    public void setIsZkOrNh(boolean isZkOrNh) {
        IsZkOrNh = isZkOrNh;
    }

    public List getLogList() {
        return logList;
    }

    public void setLogList(List logList) {
        this.logList = logList;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List getJdmcList() {
        return jdmcList;
    }

    public void setJdmcList(List jdmcList) {
        this.jdmcList = jdmcList;
    }

    public List<HashMap> getNhlist() {
        return nhlist;
    }

    public void setNhlist(List<HashMap> nhlist) {
        this.nhlist = nhlist;
    }

    public int getNhlistsize() {
        return nhlistsize;
    }

    public void setNhlistsize(int nhlistsize) {
        this.nhlistsize = nhlistsize;
    }

    public Long getNhformid() {
        return nhformid;
    }

    public void setNhformid(Long nhformid) {
        this.nhformid = nhformid;
    }

    public Long getNhid() {
        return nhid;
    }

    public void setNhid(Long nhid) {
        this.nhid = nhid;
    }

    public String getPjname() {
        return pjname;
    }

    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    public String getJdmc() {
        return jdmc;
    }

    public void setJdmc(String jdmc) {
        this.jdmc = jdmc;
    }

    public List<HashMap> getZklist() {
        return zklist;
    }

    public void setZklist(List<HashMap> zklist) {
        this.zklist = zklist;
    }

    public int getZklistsize() {
        return zklistsize;
    }

    public void setZklistsize(int zklistsize) {
        this.zklistsize = zklistsize;
    }

    public Long getZkformid() {
        return zkformid;
    }

    public void setZkformid(Long zkformid) {
        this.zkformid = zkformid;
    }

    public Long getZkid() {
        return zkid;
    }

    public void setZkid(Long zkid) {
        this.zkid = zkid;
    }

    public String getGgjzr() {
        return ggjzr;
    }

    public void setGgjzr(String ggjzr) {
        this.ggjzr = ggjzr;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getOlddepartmentname() {
        return olddepartmentname;
    }

    public void setOlddepartmentname(String olddepartmentname) {
        this.olddepartmentname = olddepartmentname;
    }

    public String getNewdepartmentname() {
        return newdepartmentname;
    }

    public void setNewdepartmentname(String newdepartmentname) {
        this.newdepartmentname = newdepartmentname;
    }

    public String getXmlx() {
        return xmlx;
    }

    public void setXmlx(String xmlx) {
        this.xmlx = xmlx;
    }

    public String getBeginprodate() {
        return beginprodate;
    }

    public void setBeginprodate(String beginprodate) {
        this.beginprodate = beginprodate;
    }

    public String getEndprodate() {
        return endprodate;
    }

    public void setEndprodate(String endprodate) {
        this.endprodate = endprodate;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public boolean isIsViewSearch() {
        return IsViewSearch;
    }

    public void setIsViewSearch(boolean isViewSearch) {
        IsViewSearch = isViewSearch;
    }

    public String getSssyb() {
        return sssyb;
    }

    public void setSssyb(String sssyb) {
        this.sssyb = sssyb;
    }

    public String getCyrName() {
        return cyrName;
    }

    public void setCyrName(String cyrName) {
        this.cyrName = cyrName;
    }

    public String getProdate() {
        return prodate;
    }

    public void setProdate(String prodate) {
        this.prodate = prodate;
    }

    public Long getXmjsformid() {
        return xmjsformid;
    }

    public void setXmjsformid(Long xmjsformid) {
        this.xmjsformid = xmjsformid;
    }

    public Long getXmjsid() {
        return xmjsid;
    }

    public void setXmjsid(Long xmjsid) {
        this.xmjsid = xmjsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<HashMap> getListxjxzq() {
        return listxjxzq;
    }

    public void setListxjxzq(List<HashMap> listxjxzq) {
        this.listxjxzq = listxjxzq;
    }

    public List<HashMap> getListxmzl() {
        return listxmzl;
    }

    public void setListxmzl(List<HashMap> listxmzl) {
        this.listxmzl = listxmzl;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getYmid() {
        return ymid;
    }

    public void setYmid(int ymid) {
        this.ymid = ymid;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public Double getRzje() {
        return rzje;
    }

    public void setRzje(Double rzje) {
        this.rzje = rzje;
    }

    public String getZcPro() {
        return zcPro;
    }

    public void setZcPro(String zcPro) {
        this.zcPro = zcPro;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectno() {
        return projectno;
    }

    public void setProjectno(String projectno) {
        this.projectno = projectno;
    }

    public String getPjr() {
        return pjr;
    }

    public void setPjr(String pjr) {
        this.pjr = pjr;
    }

    public boolean isISVIEWDG() {
        return ISVIEWDG;
    }

    public void setISVIEWDG(boolean iSVIEWDG) {
        ISVIEWDG = iSVIEWDG;
    }

    public void setTypePieData(String typePieData) {
        this.typePieData = typePieData;
    }

    public Long getFormid() {
        return formid;
    }

    public void setFormid(Long formid) {
        this.formid = formid;
    }

    public Long getXmrbid() {
        return xmrbid;
    }

    public void setXmrbid(Long xmrbid) {
        this.xmrbid = xmrbid;
    }

    public String getXmsplcServer() {
        return xmsplcServer;
    }

    public void setXmsplcServer(String xmsplcServer) {
        this.xmsplcServer = xmsplcServer;
    }

    public String getDgzt() {
        return dgzt;
    }

    public void setDgzt(String dgzt) {
        this.dgzt = dgzt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getHtje() {
        return htje;
    }

    public void setHtje(int htje) {
        this.htje = htje;
    }

    public int getWsje() {
        return wsje;
    }

    public void setWsje(int wsje) {
        this.wsje = wsje;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getXmlcServer() {
        return xmlcServer;
    }

    public void setXmlcServer(String xmlcServer) {
        this.xmlcServer = xmlcServer;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getGxsj() {
        return gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getXmjd() {
        return xmjd;
    }

    public void setXmjd(String xmjd) {
        this.xmjd = xmjd;
    }

    public int getPageNumber2() {
        return pageNumber2;
    }

    public void setPageNumber2(int pageNumber2) {
        this.pageNumber2 = pageNumber2;
    }

    public int getPageSize2() {
        return pageSize2;
    }

    public void setPageSize2(int pageSize2) {
        this.pageSize2 = pageSize2;
    }

    public int getPageNumber1() {
        return pageNumber1;
    }

    public void setPageNumber1(int pageNumber1) {
        this.pageNumber1 = pageNumber1;
    }

    public int getPageSize1() {
        return pageSize1;
    }

    public void setPageSize1(int pageSize1) {
        this.pageSize1 = pageSize1;
    }

    public int getCloseNum() {
        return closeNum;
    }

    public void setCloseNum(int closeNum) {
        this.closeNum = closeNum;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public int getPageNumber3() {
        return pageNumber3;
    }

    public void setPageNumber3(int pageNumber3) {
        this.pageNumber3 = pageNumber3;
    }

    public int getPageSize3() {
        return pageSize3;
    }

    public void setPageSize3(int pageSize3) {
        this.pageSize3 = pageSize3;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<HashMap> getXmcyxxList() {
        return xmcyxxList;
    }

    public void setXmcyxxList(List<HashMap> xmcyxxList) {
        this.xmcyxxList = xmcyxxList;
    }

    public String getTypeBarLabel3() {
        return typeBarLabel3;
    }

    public void setTypeBarLabel3(String typeBarLabel3) {
        this.typeBarLabel3 = typeBarLabel3;
    }

    public String getTypeBarData3() {
        return typeBarData3;
    }

    public void setTypeBarData3(String typeBarData3) {
        this.typeBarData3 = typeBarData3;
    }
    public List<HashMap> getList() {
        return list;
    }

    public void setList(List<HashMap> list) {
        this.list = list;
    }

    public void setZqbProjectManageService(
            ZqbProjectManageService zqbProjectManageService) {
        this.zqbProjectManageService = zqbProjectManageService;
    }


    public List<HashMap> getRunList() {
        return runList;
    }

    public List<HashMap> getFinishList() {
        return finishList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap getHash() {
        return hash;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getJson() {
        return json;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Long getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(Long instanceid) {
        this.instanceid = instanceid;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getTypePieData() {
        return typePieData;
    }

    public String getTypeBarData() {
        return typeBarData;
    }

    public void setTypeBarData(String typeBarData) {
        this.typeBarData = typeBarData;
    }

    public String getTypeBarLabel() {
        return typeBarLabel;
    }

    public void setTypeBarLabel(String typeBarLabel) {
        this.typeBarLabel = typeBarLabel;
    }

    public String getTypePieData2() {
        return typePieData2;
    }

    public String getTypePieData3() {
        return typePieData3;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public boolean isSuperman() {
        return superman;
    }

    public void setSuperman(boolean superman) {
        this.superman = superman;
    }

    public String getHtml() {
        return html;
    }

    public void setReTalk(String reTalk) {
        this.reTalk = reTalk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypePieData4() {
        return typePieData4;
    }

    public String getSearchkey() {
        return searchkey;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public List<HashMap> getCloseList() {
        return closeList;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public List<UploadDocModel> getDoclist() {
        return doclist;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setDoclist(List<UploadDocModel> doclist) {
        this.doclist = doclist;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<HashMap> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<HashMap> taskList) {
        this.taskList = taskList;
    }

    public List<HashMap> getXmcyggList() {
        return xmcyggList;
    }

    public void setXmcyggList(List<HashMap> xmcyggList) {
        this.xmcyggList = xmcyggList;
    }

    public void setXmNum(int xmNum) {
        this.xmNum = xmNum;
    }

    public int getXmNum() {
        return xmNum;
    }

    public void setPageNumberXM(int pageNumberXM) {
        this.pageNumberXM = pageNumberXM;
    }

    public int getPageNumberXM() {
        return pageNumberXM;
    }

    public void setPageSizeXM(int pageSizeXM) {
        this.pageSizeXM = pageSizeXM;
    }

    public int getPageSizeXM() {
        return pageSizeXM;
    }

    public String getZkspr() {
        return zkspr;
    }

    public void setZkspr(String zkspr) {
        this.zkspr = zkspr;
    }

    public String getNhspr() {
        return nhspr;
    }

    public void setNhspr(String nhspr) {
        this.nhspr = nhspr;
    }

    public boolean isCwAndZkNh() {
        return cwAndZkNh;
    }

    public void setCwAndZkNh(boolean cwAndZkNh) {
        this.cwAndZkNh = cwAndZkNh;
    }

    public String index() {
        // 判断是否是超级用户
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumber == 0)
            pageNumber = 1;
        if (pageNumber1 == 0)
            pageNumber1 = 1;
        if (pageNumber2 == 0)
            pageNumber2 = 1;
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        HashMap<String,List<String>> parameterMap=zqbProjectManageService.getDepartmentUserList();
        runList = zqbProjectManageService.getRunProjectList1(superman,
                pageNumber, pageSize, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        totalNum = Integer.parseInt(zqbProjectManageService.getRunProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        finishList = zqbProjectManageService.getFinishProjectList(superman,
                pageNumber1, pageSize1, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        finishNum = Integer.parseInt(zqbProjectManageService.getFinishProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        closeList = zqbProjectManageService.getCloseProjectList(superman,
                pageNumber2, pageSize2, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        closeNum = Integer.parseInt(zqbProjectManageService.getCloseProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        IsViewSearch=zqbProjectManageService.getIsViewSearch();
        return SUCCESS;
    }

    public String indexNew() {
        // 判断是否是超级用户
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumber == 0)
            pageNumber = 1;
        if (pageNumber1 == 0)
            pageNumber1 = 1;
        if (pageNumber2 == 0)
            pageNumber2 = 1;
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        HashMap<String,List<String>> parameterMap=zqbProjectManageService.getDepartmentUserList();
        runList = zqbProjectManageService.getRunProjectList1(superman,//2015-11-27修改SQL，将阶段负责人换成所属事业部
                pageNumber, pageSize, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        totalNum = Integer.parseInt(zqbProjectManageService.getRunProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        finishList = zqbProjectManageService.getFinishProjectList(superman,//2015-11-27修改SQL，将阶段负责人换成所属事业部
                pageNumber1, pageSize1, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        finishNum = Integer.parseInt(zqbProjectManageService.getFinishProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        closeList = zqbProjectManageService.getCloseProjectList(superman,//2015-11-27修改SQL，将阶段负责人换成所属事业部
                pageNumber2, pageSize2, projectName, xmjd, startDate,
                customername,dgzt,sssyb,cyrName,parameterMap);
        closeNum = Integer.parseInt(zqbProjectManageService.getCloseProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName,parameterMap).get(0).toString());
        ISVIEWDG=zqbProjectManageService.getIsViewDg();
        IsViewSearch=zqbProjectManageService.getIsViewSearch();
        IsZkOrNh=ZQBNoticeUtil.getInstance().getZkAndNhCount()>0?true:false;
        return SUCCESS;
    }

    public String indexDw(){
        //其他项目demid、formid
        qtDemid = Long.parseLong(zqbProjectManageService.getConfigUUID("qtDemid")==null||zqbProjectManageService.getConfigUUID("qtDemid").equals("")?"0":zqbProjectManageService.getConfigUUID("qtDemid"));
        qtFormid = Long.parseLong(zqbProjectManageService.getConfigUUID("qtFormid")==null||zqbProjectManageService.getConfigUUID("qtFormid").equals("")?"0":zqbProjectManageService.getConfigUUID("qtFormid"));
        //并购重组demid、formid
        demid=Long.parseLong(zqbProjectManageService.getConfigUUID("bgczlxxxid")==null||zqbProjectManageService.getConfigUUID("bgczlxxxid").equals("")?"0":zqbProjectManageService.getConfigUUID("bgczlxxxid"));
        formid=Long.parseLong(zqbProjectManageService.getConfigUUID("bgczlxxxformid")==null||zqbProjectManageService.getConfigUUID("bgczlxxxformid").equals("")?"0":zqbProjectManageService.getConfigUUID("bgczlxxxformid"));
        //股票发行项目评价demid、formid
        gppjdemid = zqbProjectManageService.getConfigUUID("gppjdemid");
        gppjformid = zqbProjectManageService.getConfigUUID("gppjformid");
        //股票发行项目demid、formid
        gpfxdemid = zqbProjectManageService.getConfigUUID("gpfxdemid");
        gpfxformid = zqbProjectManageService.getConfigUUID("gpfxformid");

        if (pageNumber == 0)
            pageNumber = 1;
        if (pageNumber1 == 0)
            pageNumber1 = 1;
        if (pageNumber2 == 0)
            pageNumber2 = 1;
        HashMap<String,Object> datarun = zqbProjectManageService.getReqireData(pageNumber, pageSize,customername,sssyb,cyrName,xmlx);
        runList = (List<HashMap>)datarun.get("RUNLIST");
        BigDecimal totalNum_ = (BigDecimal)datarun.get("TOTALNUM");
        totalNum = totalNum_.intValue();

		/*HashMap<String,Object> datafinish = zqbProjectManageService.getReqireData(pageNumber1, pageSize1,customername,sssyb,cyrName,xmlx);
		finishList = (List<HashMap>)datafinish.get("FINISHLIST");
		BigDecimal finishNum_ = (BigDecimal)datafinish.get("FINISHNUM");
		finishNum = finishNum_.intValue();*/

        HashMap<String,Object> dataclose = zqbProjectManageService.getReqireData(pageNumber2, pageSize2,customername,sssyb,cyrName,xmlx);
        closeList = (List<HashMap>)dataclose.get("CLOSELIST");
        BigDecimal closeNum_ = (BigDecimal)dataclose.get("CLOSENUM");
        closeNum = closeNum_.intValue();

        ISVIEWDG=zqbProjectManageService.getIsViewDg();
        IsViewSearch=zqbProjectManageService.getIsViewSearch();
        IsZkOrNh=ZQBNoticeUtil.getInstance().getZkAndNhCount()>0?true:false;
        return SUCCESS;
    }

    public String pjIndex() {
        // 判断是否是超级用户
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumber == 0)
            pageNumber = 1;
        if (pageNumber1 == 0)
            pageNumber1 = 1;
        if (pageNumber2 == 0)
            pageNumber2 = 1;
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        HashMap<String,List<String>> parameterMap=zqbProjectManageService.getDepartmentUserList();
        runList = zqbProjectManageService.getRunProjectList3(superman,
                pageNumber, pageSize, projectName, xmjd, startDate,
                customername,dgzt);
        totalNum = zqbProjectManageService.getRunProjectListSize3(superman,
                projectName, xmjd, startDate, customername,dgzt).size();
        finishList = zqbProjectManageService.getFinishProjectList1(superman,
                pageNumber1, pageSize1, projectName, xmjd, startDate,
                customername,dgzt);
        finishNum = zqbProjectManageService.getFinishProjectListSize2(superman,
                projectName, xmjd, startDate, customername,dgzt).size();
        closeList = zqbProjectManageService.getCloseProjectList(superman,
                pageNumber2, pageSize2, projectName, xmjd, startDate,
                customername,dgzt,null,null,null);
        closeNum = zqbProjectManageService.getCloseProjectListSize1(superman,
                projectName, xmjd, startDate, customername,dgzt,null,null,null).size();
        return SUCCESS;
    }
    /**
     * 项目提交前验证分配部门与承揽人信息
     */
    public void commitCheckSubData(){
        if(zqbProjectManageService.getConfigUUID("isDwPj").equals("1")){
            boolean clrbool = false;
            boolean cljgnumbool = false;
            Map params=new HashMap();
            params.put(1, projectno);
            int clr = DBUTilNew.getInt("COUNTNUM","SELECT COUNT(C.PROJECTNO) COUNTNUM FROM (SELECT INSTANCEID,DATAID PROJECTID,B.LCBS,B.PROJECTNO FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目管理表单')) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,B.CLR FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='承揽人表单')) A LEFT JOIN BD_ZQB_CLR B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID WHERE D.CLR IS NOT NULL AND C.PROJECTNO= ? ", params);
            double cljgnum=Double.parseDouble(zqbProjectManageService.getRestNum(instanceid,ids));
            if(clr!=0){
                clrbool=true;
            }
            if(cljgnum<100.00){
                cljgnumbool=true;
            }
            String cljgcount=zqbProjectManageService.getRestNum(instanceid,ids);
            if(clrbool&&cljgnumbool){
                ResponseUtil.write("1");
            }else{
                ResponseUtil.write("0");
            }
            if(Double.parseDouble(cljgcount)==0.00){
                ResponseUtil.write("1");
            }else{
                ResponseUtil.write("0");
            }
            ResponseUtil.write("1");
        }else{
            ResponseUtil.write("");
        }
    }

    public void commitProject(){
        String jsonString=zqbProjectManageService.commitProject(instanceid,projectNo);
        ResponseUtil.write(jsonString);
    }

    public String getXM() {
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        typePieData = zqbProjectManageService.getYxmwjz(superman, pageNumberXM,
                pageSizeXM);
        xmNum = zqbProjectManageService.getXMListSize(superman).size();
        return SUCCESS;
    }

    public String getXMList() {
        xmcyggList = zqbProjectManageService.getXMCYList(userid);
        return SUCCESS;
    }
    /**
     * 修改部门名称更新项目部门
     */
    public void zqbUpdatePdepartment() {
        if(olddepartmentname!=null&&!olddepartmentname.equals("")&&newdepartmentname!=null&&!newdepartmentname.equals("")){
            Map params = new HashMap();
            params.put(1, newdepartmentname);
            params.put(2, olddepartmentname);
            String lcsssyb = "UPDATE BD_ZQB_XMLCXXB SET SSSYB= ?  WHERE SSSYB = ? ";
            DBUTilNew.update(lcsssyb,params);
            String lcfzjgmc = "UPDATE BD_ZQB_XMLCXXB SET FZJGMC= ?  WHERE FZJGMC = ? ";
            DBUTilNew.update(lcfzjgmc,params);
            String wlsssyb = "UPDATE BD_ZQB_PJ_BASE SET SSSYB= ?  WHERE SSSYB = ? ";
            DBUTilNew.update(wlsssyb,params);
            String wlfzjgmc = "UPDATE BD_ZQB_PJ_BASE SET FZJGMC= ? WHERE FZJGMC = ? ";
            DBUTilNew.update(wlfzjgmc,params);
            String czbm = "UPDATE BD_ZQB_GPFXXMB SET CZBM= ?  WHERE CZBM = ? ";
            DBUTilNew.update(czbm,params);
            String clbm = "UPDATE BD_ZQB_GPFXXMB SET CLBM= ?  WHERE CLBM = ? ";
            DBUTilNew.update(clbm,params);
        }
    }
    /**
     * 按项目参与人统计
     *
     * @return
     */
    public String getCYR() {
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        list = zqbProjectManageService.getYxmcyrz(superman, pageNumberXM,pageSizeXM);
        xmNum = zqbProjectManageService.getTotalCYNum(superman);
        return SUCCESS;
    }

    public String expproIndex(){
        superman=ZQBNoticeUtil.getInstance().getIsCw();
        cwAndZkNh=ZQBNoticeUtil.getInstance().getIsCwAndZkNh();
        return SUCCESS;
    }
    public String expprocxddIndex(){
        superman=ZQBNoticeUtil.getInstance().getIsCw();
        cwAndZkNh=ZQBNoticeUtil.getInstance().getIsCwAndZkNh();
        return SUCCESS;
    }

    public void exppro(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expProjectList(response,zcPro,"run");
    }
    public void expprocxdd(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expProjectList(response,zcPro,"cxdd");
    }

    /**
     * 文档加载
     *
     * @return
     */
    public String docLoad() {
        doclist = zqbProjectManageService.showProjectDocList(null);
        return SUCCESS;
    }

    public String doSearch() {
        doclist = zqbProjectManageService.showProjectDocList(searchkey);
        return SUCCESS;
    }

    public String search() {
        ActionContext actionContext = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) actionContext
                .get(ServletActionContext.HTTP_REQUEST);
        String strPtname = request.getParameter("searchkey");
        String value = strPtname;
        if (type != null && value != null) {
            superman = zqbProjectManageService.getIsSuperMan();
            runList = zqbProjectManageService.getSearchProjectList(superman,type, value);
            isDwPj = zqbProjectManageService.getConfigUUID("isDwPj");
        }
        return SUCCESS;
    }

    public boolean isReadwrite() {
        return readwrite;
    }

    public void setReadwrite(boolean readwrite) {
        this.readwrite = readwrite;
    }

    public String charIndex() {
        // 获取当前用户的角色
        superman = zqbProjectManageService.getIsSuperMan();
        String[] data = zqbProjectManageService
                .getProjectTypeBarChart(superman);
        if (superman) {
            typePieData2 = zqbProjectManageService.getProjectTypePieChart2(
                    superman, "chart");
			/*typePieData3 = zqbProjectManageService.getProjectTypePieChart3(
					superman, "chart");*/
        }
        typePieData4 = zqbProjectManageService.getProjectTypePieChart4(
                superman, "chart");
        typeBarLabel = data[0];
        typeBarData = data[1];
        return SUCCESS;
    }

    public String charUser() {
        typeBarData = zqbProjectManageService.getProjectUserTableList(superman);
        return SUCCESS;
    }

    public String gridIndex() {
        // 获取当前用户的角色
        superman = zqbProjectManageService.getIsSuperMan();
        typePieData = zqbProjectManageService.getProjectTypePieChart4(superman,
                "grid");
        typeBarData = zqbProjectManageService.getProjectTypeBarGrid(superman);
        if (superman) {
            typePieData2 = zqbProjectManageService.getProjectTypePieChart2(
                    superman, "grid");
			/*typePieData3 = zqbProjectManageService.getProjectTypePieChart3(
					superman, "grid");*/
        }
        return SUCCESS;
    }

    public void updateScore() {
        if (score != null && instanceid != null) {
            HashMap data = DemAPI.getInstance().getFromData(instanceid,
                    EngineConstants.SYS_INSTANCE_TYPE_DEM);
            if (data != null) {
                data.put("FXPGFS", score);
                DemAPI.getInstance().updateFormData("", instanceid, data,
                        Long.parseLong(data.get("ID").toString()), false);
            }

        }

    }

    /**
     * 关闭项目
     */
    public void projectClose() {
        boolean flag = zqbProjectManageService.projectClose(projectNo,
                instanceid);
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    public String itemIndex() {
        if (projectNo != null) {
            XmlcConf conf = SystemConfig._xmlcConf;
            String config = conf.getConfig();
            // 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
            // 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
            String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
            xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
            readonly = true;
            readwrite = true;
            hash = zqbProjectManageService.getFinishProjectModel(projectNo);
            if (hash != null) {
                instanceid = Long.parseLong(hash.get("INSTANCEID").toString());
                String type = zqbProjectManageService.isPurview(hash,
                        ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY);
                readonly = false;
                if (type.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER)) {
                    readwrite = false;
                }
                // 判断状态
                if (hash.get("STATUS") != null) {
                    if (hash.get("STATUS").toString().equals("已完成")) {
                        readonly = true;
                    }
                }
            }
            taskList = zqbProjectManageService.loadGanttJson1(projectNo);
        }
        return SUCCESS;
    }

    /**
     * 设置项目转入持续督导阶段
     */
    public void setProjectFinish() {
        boolean flag = zqbProjectManageService.projectFinish(projectNo,
                instanceid);
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }

    }

    public void setProjectFinishDg() {
        boolean flag = zqbProjectManageService.setProjectFinishDg(temp);
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }

    }

    /**
     * 更新任务时间
     */
    public void updateTaskTime() {
        boolean flag = false;
        if (instanceid != null && startDate != null && endDate != null) {
            flag = zqbProjectManageService.drawUpdateItem(projectName,
                    projectNo, taskid, instanceid, startDate, endDate);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    /**
     * 移除任务
     */
    public void removeItem() {
        boolean flag = false;
        if (instanceid != null && projectNo != null && taskid != null) {
            flag = zqbProjectManageService.removeItem(projectNo, taskid,
                    instanceid);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }

    }

    /**
     * 加载问题页面
     *
     * @return
     */
    public String showQuestionPage() {
        if (taskid != null && projectNo != null && questionId != null) {
            html = zqbProjectManageService.getQuestionTalkList(projectNo,
                    taskid, questionId);
            username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
                    .getUsername();
            createdate = UtilDate.getNowdate();
        }
        return SUCCESS;
    }

    public String showQuestion() {
        if (taskid != null && projectNo != null) {
            html = zqbProjectManageService.showQuestion(projectNo, taskid,
                    questionId);
            username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
                    .getUsername();
            createdate = UtilDate.getNowdate();
        }
        return SUCCESS;
    }

    //
    public void commitQuestion() {
        if (taskid != null && projectNo != null && reTalk != null) {
            boolean flag = zqbProjectManageService.commitQuestion(projectNo,
                    taskid, new Long(id), reTalk, attach);
            if (flag) {
                ResponseUtil.write("success");
            } else {
                ResponseUtil.write("error");
            }
        }
    }

    public void removeQuestion() {
        if (type != null && instanceid != null) {
            boolean flag = zqbProjectManageService.removeQuestion(type,
                    instanceid);
            if (flag) {
                ResponseUtil.write("success");
            } else {
                ResponseUtil.write("error");
            }
        }
    }

    public void removeAllQuestion() {
        if (projectNo != null && taskid != null && questionId != null) {
            boolean flag = zqbProjectManageService.getQuestionTalkAllList(
                    projectNo, taskid, questionId, instanceId);
            if (flag) {
                ResponseUtil.write("success");
            } else {
                ResponseUtil.write("error");
            }
        }
    }

    public String showManagerBarChart() {
        String[] str = zqbProjectManageService.getManagerBarData();
        typeBarLabel = str[0];
        typeBarData = str[1];
        return SUCCESS;
    }

    public String showManagerBarChartInfo() {
        username=zqbProjectManageService.getChartProjectName(index);
        runList = zqbProjectManageService.getChartProjectList(index);
        return SUCCESS;
    }

    /**
     * 实收账款统计图
     *
     * @return
     */
    public String yszkChart() {
        if (pageNumber == 0)
            pageNumber = 1;
        totalNum = zqbProjectManageService.getYSZKChartDataSize();
        String[] str = zqbProjectManageService.getYSZKChartData(pageNumber, pageSize);
        typeBarLabel = str[0];
        typeBarData = str[1];
        return SUCCESS;
    }

    /**
     * 实收账款钻取页面
     *
     * @return
     */
    public String showYszkChartInfo() {
        taskList = zqbProjectManageService.getJieDuanList(index, htje, wsje);
        htje = Integer.parseInt(taskList.get(0).get("HTJE").toString());
        wsje = Integer.parseInt(taskList.get(0).get("WSJE").toString());
        return SUCCESS;
    }

    /**
     * 项目及成员信息
     *
     * @return
     */
    public String xmcy() {
        // 判断是否是超级用户
        superman = zqbProjectManageService.getIsSuperMan();
        xmcyxxList = zqbProjectManageService.getXmcy(superman);
        return SUCCESS;
    }

    /**
     * 导出项目及成员信息
     *
     * @return
     */
    public String doExp() {
        superman = zqbProjectManageService.getIsSuperMan();
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.doXmcyExp(response, superman);
        return SUCCESS;
    }

    public void saveRz(){
        boolean falg=zqbProjectManageService.saveRz(projectNo,groupid,rzje);
        if(falg){
            ResponseUtil.write("success");
        }else{
            ResponseUtil.write("error");
        }
    }

    public void editRz(){
        boolean falg=zqbProjectManageService.editRz(projectNo,groupid,rzje);
        if(falg){
            ResponseUtil.write("success");
        }else{
            ResponseUtil.write("error");
        }
    }

    public String rzList(){
        if(!add){
            rzje=zqbProjectManageService.getRzList(projectNo,groupid);
        }
        return SUCCESS;
    }
    private Long orgroleid;

    public Long getOrgroleid() {
        return orgroleid;
    }

    public void setOrgroleid(Long orgroleid) {
        this.orgroleid = orgroleid;
    }

    public String showDaily(){
        if (pageNumber == 0)
            pageNumber = 1;
        orgroleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
        formid=zqbProjectManageService.getConfig("xmrbformid");
        xmrbid=zqbProjectManageService.getConfig("xmrbid");
        list=zqbProjectManageService.showDailyList(projectNo,pageNumber,pageSize,formid,projectname);

        totalNum=zqbProjectManageService.showDailyListSize(projectNo,projectname);
        return SUCCESS;
    }
    public String checkDaily(){
        HashMap dailyData = zqbProjectManageService.getSingleDaily(instanceid);
        if(dailyData==null) return ERROR;
        createuser = dailyData.get("CREATEUSER")==null?"":dailyData.get("CREATEUSER").toString();
        projectno = dailyData.get("PROJECTNO")==null?"":dailyData.get("PROJECTNO").toString();
        projectdate = dailyData.get("PROJECTDATE")==null?"":dailyData.get("PROJECTDATE").toString();
        tracking = dailyData.get("TRACKING")==null?"":dailyData.get("TRACKING").toString();
        progress = dailyData.get("PROGRESS")==null?"":dailyData.get("PROGRESS").toString();
        username = dailyData.get("USERNAME")==null?"":dailyData.get("USERNAME").toString();
        tel = dailyData.get("TEL")==null?"":dailyData.get("TEL").toString();
        fj = dailyData.get("FJ")==null?"":dailyData.get("FJ").toString();
        extend1 = dailyData.get("EXTEND1")==null?"":dailyData.get("EXTEND1").toString();
        return SUCCESS;
    }

    public void removeDaily(){
        boolean flag = false;
        if(instanceid!=null){
            flag = zqbProjectManageService.removeDaily(instanceid);
        }
        if(flag){
            ResponseUtil.write("success");
        }else{
            ResponseUtil.write("error");
        }

    }

    public void expDaily(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expDaily(response,projectNo);
    }

    public void expProWordDZ(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expProjecWordDZ(response,instanceid);
    }

    public void expProWord(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expProjecWord(response,instanceid);
    }

    public String getPjSettlementSheet(){
        if (pageNumber == 0)
            pageNumber = 1;
        xmjsformid=zqbProjectManageService.getConfig("xmjsformid");
        xmjsid=zqbProjectManageService.getConfig("xmjsid");
        HashMap data = zqbProjectManageService.getPjSettlementSheet(customername,rzrq,xmlx,ordertype,pageNumber,pageSize);
        list = (List<HashMap>)data.get("LIST");
        totalNum=(Integer)data.get("SIZE");
        return SUCCESS;
    }

    public void zqbProjectDeleteformdate(){
        String info= zqbProjectManageService.deletePjSettlementSheet(instanceid);
        ResponseUtil.write(info);
    }

    public String setMonth(){
        return SUCCESS;
    }

    public String ywhzSetMonth(){
        return SUCCESS;
    }

    public String ydqySetMonth(){
        return SUCCESS;
    }

    public String jsfcSetMonth(){
        return SUCCESS;
    }

    /**
     * 导出项目结算信息管理
     */
    public void expxmjs(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expxmjs(response,prodate,xmlx,status);
    }
    /**
     * 导出推荐业务收入情况汇总表
     */
    public void expywhz(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expywhz(response,prodate);
    }

    /**
     * 导出月度签约项目统计表
     */
    public void expydqy(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expydqy(response,beginprodate,endprodate);
    }

    /**
     * 导出定增项目的结算分成
     */
    public void expjsfc(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expjsfc(response,prodate,xmlx);
    }

    /**
     * 项目参与人统计信息
     *
     * @return
     */
    public String showParticipantBarChart() {
        String[] str = zqbProjectManageService.getParticipantBarData();
        typeBarLabel = str[0];
        typeBarData = str[1];
        return SUCCESS;
    }

    public String showParticipantBarChartInfo() {
        runList = zqbProjectManageService.getParticipantChartProjectList(username);
        return SUCCESS;
    }

    public String showParticipantBarIndex() {
        typePieData = zqbProjectManageService.getParticipantGridBarData();
        return SUCCESS;
    }

    public String doExcelExp() {
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.doExcelExp(response);
        return null;
    }

    public String doExcelExp1() {
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.doExcelExp1(response);
        return null;
    }

    public String doExcelExp2() {
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.doExcelExp2(response);
        return null;
    }

    public String doExcelExp3() {
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.doExcelExp3(response);
        return null;
    }

    /**
     * 以项目为基准统计导出
     *
     * @return
     */
    public String doExcelXMExp() {
        HttpServletResponse response = ServletActionContext.getResponse();
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        pageSizeXM = zqbProjectManageService.getTotalNum(superman);
        zqbProjectManageService.doExcelExpXM(response, superman, pageNumberXM,
                pageSizeXM);
        return null;
    }

    /**
     * 以成员为基准统计导出
     *
     * @return
     */
    public String doExcelCYExp() {
        HttpServletResponse response = ServletActionContext.getResponse();
        superman = zqbProjectManageService.getIsSuperMan();
        if (pageNumberXM == 0)
            pageNumberXM = 1;
        pageSizeXM = zqbProjectManageService.getTotalCYNum(superman);
        zqbProjectManageService.doExcelExpCY(response, superman, pageNumberXM,
                pageSizeXM);
        return null;
    }

    /**
     * 删除项目
     *
     * @return
     */
    public void removeProject() {
        boolean flag = false;
        if (instanceid != null && projectNo != null) {
            flag = zqbProjectManageService.removeProject(projectNo, instanceid);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    /**
     * 删除持续督导项目
     *
     */
    public void removeCxddProject() {
        String info = "";
        if (instanceid != null && projectNo != null) {
            info = zqbProjectManageService.removeCxddProject(projectNo,
                    instanceid);
        }
        ResponseUtil.write(info);

    }

    /**
     * 项目基本信息
     *
     * @return
     */
    public String loadProject() {
        XmlcConf conf = SystemConfig._xmlcConf;
        String config = conf.getConfig();
        // 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
        // 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
        String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
        xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
        /*String to=projectName;*/
        try {
            projectName=URLDecoder.decode(projectName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.error(e,e);
        }
        content = zqbProjectManageService.loadProject(projectNo);
        return SUCCESS;
    }

    public String loadProjectNew() {
        XmlcConf conf = SystemConfig._xmlcConf;
        String config = conf.getConfig();
        // 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
        // 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
        String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
        xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
        try {
            projectName=URLDecoder.decode(projectName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.error(e,e);
        }
        content = zqbProjectManageService.loadProjectNew(projectNo);
        return SUCCESS;
    }

    /**
     * 项目评价信息
     *
     * @return
     */
    public String loadPj() {
        content = zqbProjectManageService.loadPj(projectNo);
        return SUCCESS;
    }

    /**
     * 相关问题
     *
     * @return
     */
    public String loadXgwt() {
        content = zqbProjectManageService.loadXgwt(projectNo);
        return SUCCESS;
    }

    public void checkRwid() {
        String zlrw = zqbProjectManageService.checkRwid(projectNo, groupid);
        ResponseUtil.write(zlrw);
    }

    /**
     * 删除评价 by dqq
     */
    public void removePj() {
        boolean flag = false;
        if (instanceid != null) {
            flag = zqbProjectManageService.removePJ(instanceid);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    /**
     * 删除相关问题 by dqq
     */
    public void deleteXgwt() {
        boolean flag = false;
        if (instanceid != null) {
            flag = zqbProjectManageService.deleteXgwt(instanceid);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    /**
     * 删除任务阶段 by dqq
     *
     */
    public void deleteTask() {
        boolean flag = false;
        if (instanceid != null) {
            flag = zqbProjectManageService.deleteTask(instanceid);
        }
        if (flag) {
            ResponseUtil.write("success");
        } else {
            ResponseUtil.write("error");
        }
    }

    public String loadXmrw() {

        return SUCCESS;
    }

    /**
     * 加载资料
     *
     */
    public void loadjdzl() {
        String jdzl = zqbProjectManageService.getJdzl(projectNo, groupid,
                instanceid);
        ResponseUtil.write(jdzl);
    }

    /**
     * 删除文件
     *
     */
    public void deleteFile() {
        boolean flag = zqbProjectManageService.deleteFile(uuid);
        String info = "";
        if (flag) {
            info = "true";
        } else {
            info = "false";
        }
        ResponseUtil.write(info);
    }

    public void deleteXMZL() {
        boolean flag = zqbProjectManageService.deleteXMZL(uuid);
        String info = "";
        if (flag) {
            info = "true";
        } else {
            info = "false";
        }
        ResponseUtil.write(info);
    }

    public void checkPJ() {
        boolean pj = zqbProjectManageService.checkPJ(projectNo, groupid);
        ResponseUtil.write(String.valueOf(pj));
    }

    public void checkPJRecord() {
        String pj = zqbProjectManageService.checkPJRecord(projectno, groupid,pjr,instanceid);
        ResponseUtil.write(pj);
    }

    public void checkXmjdExists() {
        boolean id = zqbProjectManageService
                .checkXmjdExists(projectNo, groupid);
        ResponseUtil.write(String.valueOf(id));
    }

    /**
     * 相关问题回复
     *
     * @return
     */
    public String xgwthf() {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        Map<String, String> map = ConfigUtil.readAllProperties("/common.properties");
        issx = map.get("isSX")==null?"":map.get("isSX");
        String demid$formid = DBUtil.getString("SELECT ID||'@'||FORMID DEMID$FORMID FROM SYS_DEM_ENGINE WHERE TITLE='项目立项表单'", "DEMID$FORMID");
        if(demid$formid!=null&&!demid$formid.equals("")){
            demid = Long.parseLong(demid$formid.split("@")[0]);
            formid = Long.parseLong(demid$formid.split("@")[1]);
        }
        list = zqbProjectManageService.getXgwthfx(pageNumber, pageSize,projectName, xmjd, question);
        totalNum = zqbProjectManageService.getXgwthfSize(projectName, xmjd,question);
        isDwPj = zqbProjectManageService.getConfigUUID("isDwPj");
        return SUCCESS;
    }

    /**
     * 获取最大输入值
     */
    public void getrestnum(){
        String num=zqbProjectManageService.getRestNum(instanceid,ids);
        ResponseUtil.write(num);
    }

    public void getClrRest(){
        String num=zqbProjectManageService.getFpblRest(instanceid);
        ResponseUtil.write(num);
    }

    public void checkXMCY_CLR_CLJG(){
        String currentuserid = UserContextUtil.getInstance().getInstance().getCurrentUserId();
        String zkr$nhsp = DBUtil.getString("SELECT SUBSTR(ZKR,0, INSTR(ZKR,'[',1)-1)||'-'||SUBSTR(NHSPR,0, INSTR(NHSPR,'[',1)-1) ZKR$NHSP FROM BD_ZQB_ZKNHSPR", "ZKR$NHSP");
        if(currentuserid.equals(zkr$nhsp.split("-")[0])||currentuserid.equals(zkr$nhsp.split("-")[1])){
            ResponseUtil.write("");
        }else{
            Map params = new HashMap();
            params.put(1,customerno);
            params.put(2,projectno);
            params.put(3,customerno);
            params.put(4,projectno);
            String projectname=DBUTilNew.getDataStr("PROJECTNAME", "SELECT PROJECTNAME FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO= ?  AND PROJECTNO= ?  UNION SELECT PROJECTNAME FROM BD_ZQB_XMLCXXB WHERE CUSTOMERNO= ? AND PROJECTNO= ? ", params);
            if(projectname!=null&&!projectname.equals("")){
                boolean clrbool = false;
                boolean cljgnumbool = false;
                params = new HashMap();
                params.put(1, customerno);
                int clr = DBUTilNew.getInt("COUNTNUM","SELECT COUNT(C.PROJECTNO) COUNTNUM FROM (SELECT INSTANCEID,DATAID PROJECTID,B.CUSTOMERNO,B.LCBS,B.PROJECTNO FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目管理表单')) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,B.CLR FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='承揽人表单')) A LEFT JOIN BD_ZQB_CLR B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID WHERE D.CLR IS NOT NULL AND C.CUSTOMERNO= ? ", params);
                double cljgnum=Double.parseDouble(zqbProjectManageService.getRestNum(instanceid,ids));
                if(clr!=0){
                    clrbool=true;
                }
                if(cljgnum<100.00){
                    cljgnumbool=true;
                }

                String cljgcount=zqbProjectManageService.getRestNum(instanceid,ids);
                //String clrcount=zqbProjectManageService.getFpblRest(instanceid);

                if(/*xmcybool&&*/clrbool&&cljgnumbool){
                    ResponseUtil.write("1");
                }else{
                    ResponseUtil.write("0");
                }
                if(Double.parseDouble(cljgcount)==0.00){
                    ResponseUtil.write("1");
                }else{
                    ResponseUtil.write("0");
                }
                //if(Double.parseDouble(clrcount)==0.00){
                ResponseUtil.write("1");
                //}else{
                //ResponseUtil.write("0");
                //}
            }
        }
    }
    /**
     * 该方法检查东吴当前项目是否审批通过，用于设置项目管理表单只读
     */
    public void zqbProjectThisCheck(){
        //获取配置参数
        String isDWProjectSPTGReadOnly = ConfigUtil.readAllProperties("/isshowproject.properties").get("isDWProjectSPTGReadOnly");
        String spzt=zqbProjectManageService.getZqbProjectThisCheck(projectno);
        if(spzt == null){
            ResponseUtil.write("");
        }else if(isDWProjectSPTGReadOnly != null && isDWProjectSPTGReadOnly.equals("1") && spzt.equals("审批通过")){
            ResponseUtil.write(spzt);
        }
    }

    /**
     * 当前项目的客户数据选择器
     */
    public String zqbProjectCostormerSet(){
        listxjxzq=zqbProjectManageService.getZqbProjectCostormerSet(name,no);
        return SUCCESS;
    }

    public String zqbGpfxProjectCostormerSet(){
        listxjxzq=zqbProjectManageService.getZqbGpfxProjectCostormerSet(name,no);
        return SUCCESS;
    }

    public String zqbAddxmcyDepartmentSet(){
        return SUCCESS;
    }
    public String zqbCLBMSet(){
        return SUCCESS;
    }
    public void zqbAddxmcyDepartmentZtree(){
        List<HashMap> gnmklist = zqbProjectManageService.zqbAddxmcyDepartmentZtree();
        List<HashMap> root = new ArrayList();
        for (HashMap departmentlist : gnmklist) {
            HashMap gnmkmap = new HashMap();
            gnmkmap.put("id",departmentlist.get("id").toString());
            gnmkmap.put("pId",departmentlist.get("pId").toString());
            gnmkmap.put("name",departmentlist.get("name").toString());
            gnmkmap.put("open", false);
            gnmkmap.put("icon","iwork_img/model.gif");
            root.add(gnmkmap);
        }
        StringBuffer jsonHtml = new StringBuffer();
        JSONArray json = JSONArray.fromObject(root);
        jsonHtml.append(json);
        ResponseUtil.write(jsonHtml.toString());
    }

    public String getZkList(){
        if (pageNumber == 0){
            pageNumber = 1;
        }
        zkformid=zqbProjectManageService.getConfig("zkformid");
        zkid=zqbProjectManageService.getConfig("zkid");
        zklist=zqbProjectManageService.getZkList(pjname,jdmc,pageNumber,pageSize);
        zklistsize=zqbProjectManageService.getZkListSize(pjname,jdmc);
        jdmcList=zqbProjectManageService.getJdmcList();
        return SUCCESS;
    }

    public String getNhList(){
        if (pageNumber == 0){
            pageNumber = 1;
        }
        nhformid=zqbProjectManageService.getConfig("nhformid");
        nhlist=zqbProjectManageService.getNhList(pjname,jdmc,pageNumber,pageSize);
        nhlistsize=zqbProjectManageService.getNhListSize(pjname,jdmc);
        jdmcList=zqbProjectManageService.getJdmcList();
        return SUCCESS;
    }


    public void expZK(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expZK(response,pjname,jdmc);
    }
    public void expNH(){
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbProjectManageService.expNH(response,pjname,jdmc);
    }

    public String logList(){
        if(projectname!=null&&!projectname.equals("")){
            logList=zqbProjectManageService.getLogList(projectname,startDate,endDate,type);
        }
        return SUCCESS;
    }

    public void getZkAndNhDate(){
        String info = zqbProjectManageService.getZkAndNhDate(projectno);
        ResponseUtil.write(info);
    }

    public void pjSetLocking(){
        boolean setPjLocking = zqbProjectManageService.setPjLocking(instanceid);
        String info="";
        if(setPjLocking){
            info="success";
        }else{
            info="锁定失败。";
        }
        ResponseUtil.write(info);
    }

    public void pjCleanLocking(){
        boolean cleanPjLocking = zqbProjectManageService.cleanPjLocking(instanceid);
        String info="";
        if(cleanPjLocking){
            info="success";
        }else{
            info="解锁失败。";
        }
        ResponseUtil.write(info);
    }

    public void getSubSpContent(){
        String jsonString=zqbProjectManageService.getSubSpContent(instanceid);
        ResponseUtil.write(jsonString);
    }

    public void getSubLcSpContent(){
        String jsonString=zqbProjectManageService.getSubLcSpContent(projectno);
        ResponseUtil.write(jsonString);
    }

    public void projectSyncData(){
        zqbProjectManageService.projectSyncData(projectno);
    }

    public String loadDz(){
        dzList=zqbProjectManageService.loadDz(instanceid,projectno);
        return SUCCESS;
    }

    public void setPjDz(){
        String info = zqbProjectManageService.setPjDz(instanceid,dzid);
        ResponseUtil.write(info);
    }

    public void cleanPjDz(){
        String info = zqbProjectManageService.cleanPjDz(instanceid);
        ResponseUtil.write(info);
    }

    public String loadLcDz(){
        dzList=zqbProjectManageService.loadLcDz(instanceid,projectno);
        return SUCCESS;
    }

    public void setLcPjDz(){
        String info = zqbProjectManageService.setLcPjDz(instanceid,dzid);
        ResponseUtil.write(info);
    }

    public void cleanLcPjDz(){
        String info = zqbProjectManageService.cleanLcPjDz(instanceid);
        ResponseUtil.write(info);
    }

    public void createZkData(){
        String info = zqbProjectManageService.createZkData(zkformid,zkid,projectno,projectname,zkspr);
        ResponseUtil.write(info);
    }

    public void createNhData(){
        String info = zqbProjectManageService.createNhData(nhformid,nhid,projectno,projectname,nhspr);
        ResponseUtil.write(info);
    }
}
