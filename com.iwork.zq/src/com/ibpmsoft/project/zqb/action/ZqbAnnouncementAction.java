package com.ibpmsoft.project.zqb.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import news.common.HelloWebService;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.addressbook.service.MultiAddressBookService;
import com.iwork.plugs.calender.model.IworkSchCalendar;
import com.iwork.plugs.calender.service.SchCalendarService;
import com.iwork.plugs.cms.util.CmsMyDesktopPortletUtil;
import com.iwork.plugs.weboffice.tools.WebOfficeTools;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.OrganizationAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbAnnouncementAction extends ActionSupport {

	/**
	 * 
	 */
	// xlj add 2016-01-22
	private String glfUUID;

	public String getGlfUUID() {
		return glfUUID;
	}

	public void setGlfUUID(String glfUUID) {
		this.glfUUID = glfUUID;
	}
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static final long serialVersionUID = 1L;
	private ZqbAnnouncementService zqbAnnouncementService;
	private MultiAddressBookService multiAddressBookService;
	private String khbh;
	private String khmc;
	private File uploadify;
	private Long demId;
	private Long formid;
	private String fileName;
	private String uuid;
	private String fileUrl;
	private String selectJSON;
	private String input;
	private String id;
	private String userId;
	private String userName;
	private String selectHtml;
	private String typeHtml; // 可选类型，通过参考录入生成，分 组织结构、角色、团队三种
	private Long startDept;
	private String currentDept;
	private String parentDept;
	private String nodeId;
	private String showType;
	private String nodeType;
	private String targetUserNo;
	private String targetUserId;
	private String targetUserName;
	private String targetDeptId;
	private String targetDeptName;
	private String addressName;
	private String searchOrg;
	private String defaultField;
	private Long instanceid;
	private String startdate;
	private String noticename;
	private String noticeno;
	private String noticetext;
	private String recordid;
	private String html;
	private String username;
	private Long instanceId;
	private String content;
	private String dmggsplc;
	private Long isLoadBookMarks;
	private String enddate;
	private int pageNumber = 1; // 当前页数
	private int pageSize = 10; // 每页条数
	private int totalNum;
	private String ggsplc;
	private String spzt;
	private String ggzy;
	private String zqdm;
	private List<HashMap> list;
	private List<HashMap> feedBackList;
	private List<HashMap> zxlist;
	private List<HashMap> zxfeedBackList;
	private List<HashMap> noFeedBackList;
	private List<HashMap> hflist;
	private String tzbt;
	private boolean tzggGrantCUD;
	private String zchfsj;
	private String tznr;
	private String XGZL;
	private String hfr;
	private String sftz;
	private String fsr;
	private String ggid;
	private String status;
	private String flag;
	private String hfcontent;
	private String xgzl;
	private String gonggaoID;
	private String count;
	private int tzcount;
	private int whfcount;
	private String filehtml;
	private int wdsize = 0;
	private String zqjc;
	private String roleid;
	private String zqdmtest;
	private String szbm;
	private String sfqd;
	private String cyrid;
	private long cusins;
	
	private boolean superman;
	private String nmxxStart;
	private String nmxxEnd;
	private String nmxx;
	private String fszt;
	private int noFeedBackTotal;
	private String customerno;
	private String gtid;
	private String zqdmxs;
	private String zqjcxs;
	private String cwbbcontent;
	private String cwbbformid;
	private String cwbbdemid;
	private String cwbbformidsc;
	private String cwbbdemidsc;
	private String fzgsmc;
	private String gsmc;
	private String noticetype;
	private String lockStatusForToolsNav;
	private String lockstatus;
	private String bid;
	private Long tzggid;
	private String tzggids;
	private String ggdf;
	private String filecontent;
	private String customername;
	private List<HashMap> glflist;
	private String xmjdzlformid;
	private String xmjdzldemid;
	private List<HashMap> sysdemMdmXmjdzlList;
	private String checkid;
	private String downids;
	private String upids;
	private String uptopid;
	private String downbottomid;
	private String xmlxformid;
	private String xmlxdemid;
	private List<HashMap> sysdemMdmXmlxList;
	private String Tyxmjdzlformid;
	private String Tyxmjdzldemid;
	private List<HashMap> sysdemMdmTyxmjdzlList;
	private String xpzcwtformid;
	private String xpzcwtdemid;
	private List<HashMap> sysdemMdmXpzcwtList;
	private String xmlx;
	private String instanceids;
	private String gslx;
	private String zch;
	private String zcdz;
	private String descript;
	private String gzscformid;
	private String gzscdemid;
	public String getTzggids() {
		return tzggids;
	}

	public void setTzggids(String tzggids) {
		this.tzggids = tzggids;
	}
	private String gzscinstanceid;
	private String gzschfformid;
	private String gzschfdemid;
	private int gzscnothfnum;
	private String xpgtFile;
	private Long hfqkid;
	private String nmsx;
	private String gzxtyjdemid;
	private String gzxtyjformid;
	private String gzyjid;
	private String gzyjlrr;
	private String gzyjlrsj;
	private String gzyjjsm;
	private String gzyjsmwd;
	private String dxyjtzr;
	private String dxyjtzrsb;
	private String gzyjlrrid;
	private String gzxtyjhfdemid;
	private String gzxtyjhfformid;
	private List<HashMap> gzxtyjlist;
	private List<HashMap<String, Object>> itemlist;
	private List<HashMap<String, Object>> itemContentList;
	private List<HashMap<String, Object>> itemBcList;
	private List<HashMap<String, Object>> itemSteplist;
	private Long itemFormId;
	private Long itemDemId;
	private Long itemContentFormId;
	private Long itemContentDemId;
	private Long itemBcFormId;
	private Long itemBcDemId;
	private Long itemStepFormId;
	private Long itemStepDemId;
	private String sxlx;
	private Long sxinstanceid;
	private Long cinstanceid;
	private Long bcinstanceid;
	private Long bzinstanceid;
	private List<String> itemTitleList;
	private String fileUUID;
	private List<HashMap> xpsxlist;
	private String xpsxname;
	private String noticedatestart;
	private String noticedateend;
	private String xpsxformid;
	private String xpsxdemid;
	private String sxmc;
	private String txrq;
	private String sygz;
	private String plyq;
	private String bznr;
	private String type;
	private String nr;
	private String bnr;
	private String bbc;
	private String bzlx;
	private List<HashMap> zqdmlist;
	private List<HashMap> ydry;
	private List<HashMap<String, Object>> qaList;
	private List<HashMap<String, Object>> detailList;
	private Long lcbs;
	private String lcbh;
	private String stepid;
	private String opinionList;
	private List<HashMap<String, Object>> opinionUserList;
	private List<HashMap<String, Object>> opinionUserContentList;
	private String opinionContent;
	private String createuser;
	private String createusername;
	private String zhuhy;
	private String zihy;
	private String filename;
	private String actdefid;
	private String fxsj;
	private String hksj;
	private String zqinsid;
	private String zjlsqkhz;
	private String jyszqdffx;
	private String dffxggytz;
	private String rcid;
	private String zqmc;
	private String jc;
	private String zqfzbm;
	private String fxfs;
	private String chfs;
	private Long pInstanceId;
	private String fullname;
	private String model;
	private String orderby;
	private String starttime;
	private String endtime;
	private String firstload;
	private String departmentname;
	private String dq;
	private String companyno;
	private String tzlx;
	private String wjfile;
	private String dctm;
	private String extend5;
	private String sfyhf;
	private int YDRS;
	private int SDRS;
	private String demid;
	private String sszjj;
	private String ssbm;
	private String dqrq;
	private String yjzdms;
	private String zqServer;
	private String sfcx;
	private String memo;
	
	private String HYMC;
	private String HYLX;
	private String ZT;
	private String HYSJ;
	private String DRLX;
	private String YEAR;
	private String JC;
	private String HC;
	private String zywyh;
	private int pageSizes=30;
	private Long CID;

    public Long getCID() {
        return CID;
    }

    public void setCID(Long CID) {
        this.CID = CID;
    }

    public int getPageSizes() {
		return pageSizes;
	}

	public void setPageSizes(int pageSizes) {
		this.pageSizes = pageSizes;
	}

	public String getZywyh() {
		return zywyh;
	}

	public void setZywyh(String zywyh) {
		this.zywyh = zywyh;
	}

	public String getHYMC() {
		return HYMC;
	}

	public void setHYMC(String hYMC) {
		HYMC = hYMC;
	}

	public String getHYLX() {
		return HYLX;
	}

	public void setHYLX(String hYLX) {
		HYLX = hYLX;
	}

	public String getZT() {
		return ZT;
	}

	public void setZT(String zT) {
		ZT = zT;
	}

	public String getHYSJ() {
		return HYSJ;
	}

	public void setHYSJ(String hYSJ) {
		HYSJ = hYSJ;
	}

	public String getDRLX() {
		return DRLX;
	}

	public void setDRLX(String dRLX) {
		DRLX = dRLX;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	public String getJC() {
		return JC;
	}

	public void setJC(String jC) {
		JC = jC;
	}

	public String getHC() {
		return HC;
	}

	public void setHC(String hC) {
		HC = hC;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSfcx() {
		return sfcx;
	}

	public void setSfcx(String sfcx) {
		this.sfcx = sfcx;
	}

	public String getZqServer() {
		return zqServer;
	}

	public void setZqServer(String zqServer) {
		this.zqServer = zqServer;
	}

	public String getYjzdms() {
		return yjzdms;
	}

	public void setYjzdms(String yjzdms) {
		this.yjzdms = yjzdms;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	public String getSszjj() {
		return sszjj;
	}

	public void setSszjj(String sszjj) {
		this.sszjj = sszjj;
	}

	public String getSsbm() {
		return ssbm;
	}

	public void setSsbm(String ssbm) {
		this.ssbm = ssbm;
	}

	public String getCyrid() {
		return cyrid;
	}

	public void setCyrid(String cyrid) {
		this.cyrid = cyrid;
	}

	public String getSzbm() {
		return szbm;
	}

	public void setSzbm(String szbm) {
		this.szbm = szbm;
	}

	public String getSfqd() {
		return sfqd;
	}

	public void setSfqd(String sfqd) {
		this.sfqd = sfqd;
	}

	public List<HashMap> getYdry() {
		return ydry;
	}

	public void setYdry(List<HashMap> ydry) {
		this.ydry = ydry;
	}

	public String getWjfile() {
		return wjfile;
	}

	public void setWjfile(String wjfile) {
		this.wjfile = wjfile;
	}

	public String getDctm() {
		return dctm;
	}

	public void setDctm(String dctm) {
		this.dctm = dctm;
	}

	public String getExtend5() {
		return extend5;
	}

	public void setExtend5(String extend5) {
		this.extend5 = extend5;
	}

	public String getSfyhf() {
		return sfyhf;
	}

	public void setSfyhf(String sfyhf) {
		this.sfyhf = sfyhf;
	}

	public String getDemid() {
		return demid;
	}

	public void setDemid(String demid) {
		this.demid = demid;
	}

	public int getYDRS() {
		return YDRS;
	}

	public void setYDRS(int yDRS) {
		YDRS = yDRS;
	}

	public int getSDRS() {
		return SDRS;
	}

	public void setSDRS(int sDRS) {
		SDRS = sDRS;
	}

	public String getTzlx() {
		return tzlx;
	}

	public void setTzlx(String tzlx) {
		this.tzlx = tzlx;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getFirstload() {
		return firstload;
	}

	public void setFirstload(String firstload) {
		this.firstload = firstload;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Long getpInstanceId() {
		return pInstanceId;
	}

	public void setpInstanceId(Long pInstanceId) {
		this.pInstanceId = pInstanceId;
	}

	public String getZqmc() {
		return zqmc;
	}

	public void setZqmc(String zqmc) {
		this.zqmc = zqmc;
	}

	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public String getZqfzbm() {
		return zqfzbm;
	}

	public void setZqfzbm(String zqfzbm) {
		this.zqfzbm = zqfzbm;
	}

	public String getFxfs() {
		return fxfs;
	}

	public void setFxfs(String fxfs) {
		this.fxfs = fxfs;
	}

	public String getChfs() {
		return chfs;
	}

	public void setChfs(String chfs) {
		this.chfs = chfs;
	}

	public String getRcid() {
		return rcid;
	}

	public void setRcid(String rcid) {
		this.rcid = rcid;
	}

	public String getDffxggytz() {
		return dffxggytz;
	}

	public void setDffxggytz(String dffxggytz) {
		this.dffxggytz = dffxggytz;
	}

	public String getJyszqdffx() {
		return jyszqdffx;
	}

	public void setJyszqdffx(String jyszqdffx) {
		this.jyszqdffx = jyszqdffx;
	}

	public String getZjlsqkhz() {
		return zjlsqkhz;
	}

	public void setZjlsqkhz(String zjlsqkhz) {
		this.zjlsqkhz = zjlsqkhz;
	}

	public String getFxsj() {
		return fxsj;
	}

	public void setFxsj(String fxsj) {
		this.fxsj = fxsj;
	}

	public String getHksj() {
		return hksj;
	}

	public void setHksj(String hksj) {
		this.hksj = hksj;
	}

	public String getZqinsid() {
		return zqinsid;
	}

	public void setZqinsid(String zqinsid) {
		this.zqinsid = zqinsid;
	}

	public String getActdefid() {
		return actdefid;
	}

	public void setActdefid(String actdefid) {
		this.actdefid = actdefid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getZhuhy() {
		return zhuhy;
	}
	
	public void setZhuhy(String zhuhy) {
		this.zhuhy = zhuhy;
	}

	public String getZihy() {
		return zihy;
	}

	public void setZihy(String zihy) {
		this.zihy = zihy;
	}

	public String getCreateusername() {
		return createusername;
	}

	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public List<HashMap<String, Object>> getOpinionUserContentList() {
		return opinionUserContentList;
	}

	public void setOpinionUserContentList(
			List<HashMap<String, Object>> opinionUserContentList) {
		this.opinionUserContentList = opinionUserContentList;
	}

	public String getOpinionContent() {
		return opinionContent;
	}

	public void setOpinionContent(String opinionContent) {
		this.opinionContent = opinionContent;
	}

	public List<HashMap<String, Object>> getOpinionUserList() {
		return opinionUserList;
	}

	public void setOpinionUserList(List<HashMap<String, Object>> opinionUserList) {
		this.opinionUserList = opinionUserList;
	}

	public String getOpinionList() {
		return opinionList;
	}

	public void setOpinionList(String opinionList) {
		this.opinionList = opinionList;
	}

	public Long getLcbs() {
		return lcbs;
	}

	public void setLcbs(Long lcbs) {
		this.lcbs = lcbs;
	}

	public String getLcbh() {
		return lcbh;
	}

	public void setLcbh(String lcbh) {
		this.lcbh = lcbh;
	}

	public String getStepid() {
		return stepid;
	}

	public void setStepid(String stepid) {
		this.stepid = stepid;
	}

	public List<HashMap<String, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<HashMap<String, Object>> detailList) {
		this.detailList = detailList;
	}

	public List<HashMap<String, Object>> getQaList() {
		return qaList;
	}

	public void setQaList(List<HashMap<String, Object>> qaList) {
		this.qaList = qaList;
	}

	public List<HashMap> getZqdmlist() {
		return zqdmlist;
	}

	public void setZqdmlist(List<HashMap> zqdmlist) {
		this.zqdmlist = zqdmlist;
	}

	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}

	public String getBzlx() {
		return bzlx;
	}

	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getBnr() {
		return bnr;
	}

	public void setBnr(String bnr) {
		this.bnr = bnr;
	}

	public String getBbc() {
		return bbc;
	}

	public void setBbc(String bbc) {
		this.bbc = bbc;
	}

	public String getSxmc() {
		return sxmc;
	}

	public void setSxmc(String sxmc) {
		this.sxmc = sxmc;
	}

	public String getTxrq() {
		return txrq;
	}

	public void setTxrq(String txrq) {
		this.txrq = txrq;
	}

	public String getSygz() {
		return sygz;
	}

	public void setSygz(String sygz) {
		this.sygz = sygz;
	}

	public String getPlyq() {
		return plyq;
	}

	public void setPlyq(String plyq) {
		this.plyq = plyq;
	}

	public String getBznr() {
		return bznr;
	}

	public void setBznr(String bznr) {
		this.bznr = bznr;
	}

	public String getXpsxformid() {
		return xpsxformid;
	}

	public void setXpsxformid(String xpsxformid) {
		this.xpsxformid = xpsxformid;
	}

	public String getXpsxdemid() {
		return xpsxdemid;
	}

	public void setXpsxdemid(String xpsxdemid) {
		this.xpsxdemid = xpsxdemid;
	}

	public List<HashMap> getXpsxlist() {
		return xpsxlist;
	}

	public void setXpsxlist(List<HashMap> xpsxlist) {
		this.xpsxlist = xpsxlist;
	}

	public String getXpsxname() {
		return xpsxname;
	}

	public void setXpsxname(String xpsxname) {
		this.xpsxname = xpsxname;
	}

	public String getNoticedatestart() {
		return noticedatestart;
	}

	public void setNoticedatestart(String noticedatestart) {
		this.noticedatestart = noticedatestart;
	}

	public String getNoticedateend() {
		return noticedateend;
	}

	public void setNoticedateend(String noticedateend) {
		this.noticedateend = noticedateend;
	}

	public List<String> getItemTitleList() {
		return itemTitleList;
	}

	public void setItemTitleList(List<String> itemTitleList) {
		this.itemTitleList = itemTitleList;
	}

	public Long getCinstanceid() {
		return cinstanceid;
	}

	public void setCinstanceid(Long cinstanceid) {
		this.cinstanceid = cinstanceid;
	}

	public Long getBcinstanceid() {
		return bcinstanceid;
	}

	public void setBcinstanceid(Long bcinstanceid) {
		this.bcinstanceid = bcinstanceid;
	}

	public Long getBzinstanceid() {
		return bzinstanceid;
	}

	public void setBzinstanceid(Long bzinstanceid) {
		this.bzinstanceid = bzinstanceid;
	}

	public Long getSxinstanceid() {
		return sxinstanceid;
	}

	public void setSxinstanceid(Long sxinstanceid) {
		this.sxinstanceid = sxinstanceid;
	}

	public Long getItemFormId() {
		return itemFormId;
	}

	public void setItemFormId(Long itemFormId) {
		this.itemFormId = itemFormId;
	}

	public Long getItemDemId() {
		return itemDemId;
	}

	public void setItemDemId(Long itemDemId) {
		this.itemDemId = itemDemId;
	}

	public Long getItemContentFormId() {
		return itemContentFormId;
	}

	public void setItemContentFormId(Long itemContentFormId) {
		this.itemContentFormId = itemContentFormId;
	}

	public Long getItemContentDemId() {
		return itemContentDemId;
	}

	public void setItemContentDemId(Long itemContentDemId) {
		this.itemContentDemId = itemContentDemId;
	}

	public Long getItemBcFormId() {
		return itemBcFormId;
	}

	public void setItemBcFormId(Long itemBcFormId) {
		this.itemBcFormId = itemBcFormId;
	}

	public Long getItemBcDemId() {
		return itemBcDemId;
	}

	public void setItemBcDemId(Long itemBcDemId) {
		this.itemBcDemId = itemBcDemId;
	}

	public Long getItemStepFormId() {
		return itemStepFormId;
	}

	public void setItemStepFormId(Long itemStepFormId) {
		this.itemStepFormId = itemStepFormId;
	}

	public Long getItemStepDemId() {
		return itemStepDemId;
	}

	public void setItemStepDemId(Long itemStepDemId) {
		this.itemStepDemId = itemStepDemId;
	}

	public List<HashMap<String, Object>> getItemContentList() {
		return itemContentList;
	}

	public void setItemContentList(List<HashMap<String, Object>> itemContentList) {
		this.itemContentList = itemContentList;
	}

	public List<HashMap<String, Object>> getItemBcList() {
		return itemBcList;
	}

	public void setItemBcList(List<HashMap<String, Object>> itemBcList) {
		this.itemBcList = itemBcList;
	}

	public List<HashMap<String, Object>> getItemSteplist() {
		return itemSteplist;
	}

	public void setItemSteplist(List<HashMap<String, Object>> itemSteplist) {
		this.itemSteplist = itemSteplist;
	}

	public String getSxlx() {
		return sxlx;
	}

	public void setSxlx(String sxlx) {
		this.sxlx = sxlx;
	}

	public List<HashMap<String, Object>> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<HashMap<String, Object>> itemlist) {
		this.itemlist = itemlist;
	}

	public String getXpzcwtformid() {
		return xpzcwtformid;
	}

	public void setXpzcwtformid(String xpzcwtformid) {
		this.xpzcwtformid = xpzcwtformid;
	}

	public String getXpzcwtdemid() {
		return xpzcwtdemid;
	}

	public void setXpzcwtdemid(String xpzcwtdemid) {
		this.xpzcwtdemid = xpzcwtdemid;
	}

	public List<HashMap> getSysdemMdmXpzcwtList() {
		return sysdemMdmXpzcwtList;
	}

	public void setSysdemMdmXpzcwtList(List<HashMap> sysdemMdmXpzcwtList) {
		this.sysdemMdmXpzcwtList = sysdemMdmXpzcwtList;
	}

	public List<HashMap> getGzxtyjlist() {
		return gzxtyjlist;
	}

	public void setGzxtyjlist(List<HashMap> gzxtyjlist) {
		this.gzxtyjlist = gzxtyjlist;
	}

	public String getGzxtyjhfdemid() {
		return gzxtyjhfdemid;
	}

	public void setGzxtyjhfdemid(String gzxtyjhfdemid) {
		this.gzxtyjhfdemid = gzxtyjhfdemid;
	}

	public String getGzxtyjhfformid() {
		return gzxtyjhfformid;
	}

	public void setGzxtyjhfformid(String gzxtyjhfformid) {
		this.gzxtyjhfformid = gzxtyjhfformid;
	}

	public String getGzyjlrr() {
		return gzyjlrr;
	}

	public void setGzyjlrr(String gzyjlrr) {
		this.gzyjlrr = gzyjlrr;
	}

	public String getGzyjlrsj() {
		return gzyjlrsj;
	}

	public void setGzyjlrsj(String gzyjlrsj) {
		this.gzyjlrsj = gzyjlrsj;
	}

	public String getGzyjjsm() {
		return gzyjjsm;
	}

	public void setGzyjjsm(String gzyjjsm) {
		this.gzyjjsm = gzyjjsm;
	}

	public String getGzyjsmwd() {
		return gzyjsmwd;
	}

	public void setGzyjsmwd(String gzyjsmwd) {
		this.gzyjsmwd = gzyjsmwd;
	}

	public String getDxyjtzr() {
		return dxyjtzr;
	}

	public void setDxyjtzr(String dxyjtzr) {
		this.dxyjtzr = dxyjtzr;
	}

	public String getDxyjtzrsb() {
		return dxyjtzrsb;
	}

	public void setDxyjtzrsb(String dxyjtzrsb) {
		this.dxyjtzrsb = dxyjtzrsb;
	}

	public String getGzyjlrrid() {
		return gzyjlrrid;
	}

	public void setGzyjlrrid(String gzyjlrrid) {
		this.gzyjlrrid = gzyjlrrid;
	}

	public String getGzyjid() {
		return gzyjid;
	}

	public void setGzyjid(String gzyjid) {
		this.gzyjid = gzyjid;
	}

	public String getGzxtyjdemid() {
		return gzxtyjdemid;
	}

	public void setGzxtyjdemid(String gzxtyjdemid) {
		this.gzxtyjdemid = gzxtyjdemid;
	}

	public String getGzxtyjformid() {
		return gzxtyjformid;
	}

	public void setGzxtyjformid(String gzxtyjformid) {
		this.gzxtyjformid = gzxtyjformid;
	}

	public String getNmsx() {
		return nmsx;
	}

	public void setNmsx(String nmsx) {
		this.nmsx = nmsx;
	}

	public Long getHfqkid() {
		return hfqkid;
	}

	public void setHfqkid(Long hfqkid) {
		this.hfqkid = hfqkid;
	}

	public String getXpgtFile() {
		return xpgtFile;
	}

	public void setXpgtFile(String xpgtFile) {
		this.xpgtFile = xpgtFile;
	}

	public int getGzscnothfnum() {
		return gzscnothfnum;
	}

	public void setGzscnothfnum(int gzscnothfnum) {
		this.gzscnothfnum = gzscnothfnum;
	}

	public String getGzschfformid() {
		return gzschfformid;
	}

	public void setGzschfformid(String gzschfformid) {
		this.gzschfformid = gzschfformid;
	}

	public String getGzschfdemid() {
		return gzschfdemid;
	}

	public void setGzschfdemid(String gzschfdemid) {
		this.gzschfdemid = gzschfdemid;
	}

	public String getGzscinstanceid() {
		return gzscinstanceid;
	}

	public void setGzscinstanceid(String gzscinstanceid) {
		this.gzscinstanceid = gzscinstanceid;
	}

	public String getGzscformid() {
		return gzscformid;
	}

	public void setGzscformid(String gzscformid) {
		this.gzscformid = gzscformid;
	}

	public String getGzscdemid() {
		return gzscdemid;
	}

	public void setGzscdemid(String gzscdemid) {
		this.gzscdemid = gzscdemid;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getGslx() {
		return gslx;
	}

	public void setGslx(String gslx) {
		this.gslx = gslx;
	}

	public String getZch() {
		return zch;
	}

	public void setZch(String zch) {
		this.zch = zch;
	}

	public String getZcdz() {
		return zcdz;
	}

	public void setZcdz(String zcdz) {
		this.zcdz = zcdz;
	}

	public String getXmlx() {
		return xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	public String getDownbottomid() {
		return downbottomid;
	}

	public void setDownbottomid(String downbottomid) {
		this.downbottomid = downbottomid;
	}

	public String getUptopid() {
		return uptopid;
	}

	public void setUptopid(String uptopid) {
		this.uptopid = uptopid;
	}

	public String getCheckid() {
		return checkid;
	}

	public void setCheckid(String checkid) {
		this.checkid = checkid;
	}

	public String getDownids() {
		return downids;
	}

	public void setDownids(String downids) {
		this.downids = downids;
	}

	public String getUpids() {
		return upids;
	}

	public void setUpids(String upids) {
		this.upids = upids;
	}

	public String getInstanceids() {
		return instanceids;
	}

	public void setInstanceids(String instanceids) {
		this.instanceids = instanceids;
	}

	public String getTyxmjdzlformid() {
		return Tyxmjdzlformid;
	}

	public void setTyxmjdzlformid(String tyxmjdzlformid) {
		Tyxmjdzlformid = tyxmjdzlformid;
	}

	public String getTyxmjdzldemid() {
		return Tyxmjdzldemid;
	}

	public void setTyxmjdzldemid(String tyxmjdzldemid) {
		Tyxmjdzldemid = tyxmjdzldemid;
	}

	public List<HashMap> getSysdemMdmTyxmjdzlList() {
		return sysdemMdmTyxmjdzlList;
	}

	public void setSysdemMdmTyxmjdzlList(List<HashMap> sysdemMdmTyxmjdzlList) {
		this.sysdemMdmTyxmjdzlList = sysdemMdmTyxmjdzlList;
	}

	public String getXmlxformid() {
		return xmlxformid;
	}

	public void setXmlxformid(String xmlxformid) {
		this.xmlxformid = xmlxformid;
	}

	public String getXmlxdemid() {
		return xmlxdemid;
	}

	public void setXmlxdemid(String xmlxdemid) {
		this.xmlxdemid = xmlxdemid;
	}

	public List<HashMap> getSysdemMdmXmlxList() {
		return sysdemMdmXmlxList;
	}

	public void setSysdemMdmXmlxList(List<HashMap> sysdemMdmXmlxList) {
		this.sysdemMdmXmlxList = sysdemMdmXmlxList;
	}

	public List<HashMap> getSysdemMdmXmjdzlList() {
		return sysdemMdmXmjdzlList;
	}

	public void setSysdemMdmXmjdzlList(List<HashMap> sysdemMdmXmjdzlList) {
		this.sysdemMdmXmjdzlList = sysdemMdmXmjdzlList;
	}

	public String getXmjdzlformid() {
		return xmjdzlformid;
	}

	public void setXmjdzlformid(String xmjdzlformid) {
		this.xmjdzlformid = xmjdzlformid;
	}

	public String getXmjdzldemid() {
		return xmjdzldemid;
	}

	public void setXmjdzldemid(String xmjdzldemid) {
		this.xmjdzldemid = xmjdzldemid;
	}

	public List<HashMap> getGlflist() {
		return glflist;
	}

	public void setGlflist(List<HashMap> glflist) {
		this.glflist = glflist;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}

	public String getGgdf() {
		return ggdf;
	}

	public void setGgdf(String ggdf) {
		this.ggdf = ggdf;
	}

	public Long getTzggid() {
		return tzggid;
	}

	public void setTzggid(Long tzggid) {
		this.tzggid = tzggid;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getLockstatus() {
		return lockstatus;
	}

	public void setLockstatus(String lockstatus) {
		this.lockstatus = lockstatus;
	}

	public String getLockStatusForToolsNav() {
		return lockStatusForToolsNav;
	}

	public void setLockStatusForToolsNav(String lockStatusForToolsNav) {
		this.lockStatusForToolsNav = lockStatusForToolsNav;
	}

	public String getNoticetype() {
		return noticetype;
	}

	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}

	public String getCwbbformidsc() {
		return cwbbformidsc;
	}

	public void setCwbbformidsc(String cwbbformidsc) {
		this.cwbbformidsc = cwbbformidsc;
	}

	public String getCwbbdemidsc() {
		return cwbbdemidsc;
	}

	public void setCwbbdemidsc(String cwbbdemidsc) {
		this.cwbbdemidsc = cwbbdemidsc;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getFzgsmc() {
		return fzgsmc;
	}

	public void setFzgsmc(String fzgsmc) {
		this.fzgsmc = fzgsmc;
	}

	public String getCwbbformid() {
		return cwbbformid;
	}

	public void setCwbbformid(String cwbbformid) {
		this.cwbbformid = cwbbformid;
	}

	public String getCwbbdemid() {
		return cwbbdemid;
	}

	public void setCwbbdemid(String cwbbdemid) {
		this.cwbbdemid = cwbbdemid;
	}

	private static Logger logger = Logger
			.getLogger(ZqbAnnouncementAction.class);

	public String getZqdmxs() {
		return zqdmxs;
	}

	public void setZqdmxs(String zqdmxs) {
		this.zqdmxs = zqdmxs;
	}

	public String getZqjcxs() {
		return zqjcxs;
	}

	public void setZqjcxs(String zqjcxs) {
		this.zqjcxs = zqjcxs;
	}

	public String getCwbbcontent() {
		return cwbbcontent;
	}

	public void setCwbbcontent(String cwbbcontent) {
		this.cwbbcontent = cwbbcontent;
	}

	public String getGtid() {
		return gtid;
	}

	public void setGtid(String gtid) {
		this.gtid = gtid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public int getNoFeedBackTotal() {
		return noFeedBackTotal;
	}

	public void setNoFeedBackTotal(int noFeedBackTotal) {
		this.noFeedBackTotal = noFeedBackTotal;
	}

	public List<HashMap> getNoFeedBackList() {
		return noFeedBackList;
	}

	public void setNoFeedBackList(List<HashMap> noFeedBackList) {
		this.noFeedBackList = noFeedBackList;
	}

	public List<HashMap> getZxlist() {
		return zxlist;
	}

	public void setZxlist(List<HashMap> zxlist) {
		this.zxlist = zxlist;
	}

	public List<HashMap> getZxfeedBackList() {
		return zxfeedBackList;
	}

	public void setZxfeedBackList(List<HashMap> zxfeedBackList) {
		this.zxfeedBackList = zxfeedBackList;
	}

	public List<HashMap> getFeedBackList() {
		return feedBackList;
	}

	public void setFeedBackList(List<HashMap> feedBackList) {
		this.feedBackList = feedBackList;
	}

	public String getFszt() {
		return fszt;
	}

	public void setFszt(String fszt) {
		this.fszt = fszt;
	}

	public String getNmxxStart() {
		return nmxxStart;
	}

	public void setNmxxStart(String nmxxStart) {
		this.nmxxStart = nmxxStart;
	}

	public String getNmxxEnd() {
		return nmxxEnd;
	}

	public void setNmxxEnd(String nmxxEnd) {
		this.nmxxEnd = nmxxEnd;
	}

	public String getNmxx() {
		return nmxx;
	}

	public void setNmxx(String nmxx) {
		this.nmxx = nmxx;
	}

	public long getCusins() {
		return cusins;
	}

	public void setCusins(long cusins) {
		this.cusins = cusins;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getZqjc() {
		return zqjc;
	}

	public void setZqjc(String zqjc) {
		this.zqjc = zqjc;
	}

	public String getCompanyno() {
		return companyno;
	}

	public void setCompanyno(String companyno) {
		this.companyno = companyno;
	}

	public List<HashMap> getHflist() {
		return hflist;
	}

	public void setHflist(List<HashMap> hflist) {
		this.hflist = hflist;
	}

	public int getWdsize() {
		return wdsize;
	}

	public void setWdsize(int wdsize) {
		this.wdsize = wdsize;
	}

	public String getFilehtml() {
		return filehtml;
	}

	public void setFilehtml(String filehtml) {
		this.filehtml = filehtml;
	}

	public int getTzcount() {
		return tzcount;
	}

	public void setTzcount(int tzcount) {
		this.tzcount = tzcount;
	}

	public int getWhfcount() {
		return whfcount;
	}

	public void setWhfcount(int whfcount) {
		this.whfcount = whfcount;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getGonggaoID() {
		return gonggaoID;
	}

	public void setGonggaoID(String gonggaoID) {
		this.gonggaoID = gonggaoID;
	}

	public String getHfcontent() {
		return hfcontent;
	}

	public void setHfcontent(String hfcontent) {
		this.hfcontent = hfcontent;
	}

	public String getXgzl() {
		return xgzl;
	}

	public void setXgzl(String xgzl) {
		this.xgzl = xgzl;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTzbt() {
		return tzbt;
	}

	public void setTzbt(String tzbt) {
		this.tzbt = tzbt;
	}

	public boolean getTzggGrantCUD() {
		return tzggGrantCUD;
	}

	public void setTzggGrantCUD(boolean tzggGrantCUD) {
		this.tzggGrantCUD = tzggGrantCUD;
	}

	public String getZchfsj() {
		return zchfsj;
	}

	public void setZchfsj(String zchfsj) {
		this.zchfsj = zchfsj;
	}

	public String getTznr() {
		return tznr;
	}

	public void setTznr(String tznr) {
		this.tznr = tznr;
	}

	public String getXGZL() {
		return XGZL;
	}

	public void setXGZL(String xGZL) {
		XGZL = xGZL;
	}

	public String getHfr() {
		return hfr;
	}

	public void setHfr(String hfr) {
		this.hfr = hfr;
	}

	public String getSftz() {
		return sftz;
	}

	public void setSftz(String sftz) {
		this.sftz = sftz;
	}

	public String getFsr() {
		return fsr;
	}

	public void setFsr(String fsr) {
		this.fsr = fsr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSelectHtml() {
		return selectHtml;
	}

	public void setSelectHtml(String selectHtml) {
		this.selectHtml = selectHtml;
	}

	public String getTypeHtml() {
		return typeHtml;
	}

	public void setTypeHtml(String typeHtml) {
		this.typeHtml = typeHtml;
	}

	public Long getStartDept() {
		return startDept;
	}

	public void setStartDept(Long startDept) {
		this.startDept = startDept;
	}

	public String getCurrentDept() {
		return currentDept;
	}

	public void setCurrentDept(String currentDept) {
		this.currentDept = currentDept;
	}

	public String getParentDept() {
		return parentDept;
	}

	public void setParentDept(String parentDept) {
		this.parentDept = parentDept;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getTargetUserNo() {
		return targetUserNo;
	}

	public void setTargetUserNo(String targetUserNo) {
		this.targetUserNo = targetUserNo;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getTargetDeptId() {
		return targetDeptId;
	}

	public void setTargetDeptId(String targetDeptId) {
		this.targetDeptId = targetDeptId;
	}

	public String getTargetDeptName() {
		return targetDeptName;
	}

	public void setTargetDeptName(String targetDeptName) {
		this.targetDeptName = targetDeptName;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getSearchOrg() {
		return searchOrg;
	}

	public void setSearchOrg(String searchOrg) {
		this.searchOrg = searchOrg;
	}

	public String getDefaultField() {
		return defaultField;
	}

	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}

	public String getSelectJSON() {
		return selectJSON;
	}

	public void setSelectJSON(String selectJSON) {
		this.selectJSON = selectJSON;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getNoticeno() {
		return noticeno;
	}

	public void setNoticeno(String noticeno) {
		this.noticeno = noticeno;
	}

	public String getDmggsplc() {
		return dmggsplc;
	}

	public void setDmggsplc(String dmggsplc) {
		this.dmggsplc = dmggsplc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getIsLoadBookMarks() {
		return isLoadBookMarks;
	}

	public void setIsLoadBookMarks(Long isLoadBookMarks) {
		this.isLoadBookMarks = isLoadBookMarks;
	}

	public String getHtml() {
		return html;
	}

	public MultiAddressBookService getMultiAddressBookService() {
		return multiAddressBookService;
	}

	public void setMultiAddressBookService(
			MultiAddressBookService multiAddressBookService) {
		this.multiAddressBookService = multiAddressBookService;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public String getNoticetext() {
		return noticetext;
	}

	public void setNoticetext(String noticetext) {
		this.noticetext = noticetext;
	}

	public String getNoticename() {
		return noticename;
	}

	public void setNoticename(String noticename) {
		this.noticename = noticename;
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

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getSpzt() {
		return spzt;
	}

	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}

	public String getGgzy() {
		return ggzy;
	}

	public void setGgzy(String ggzy) {
		this.ggzy = ggzy;
	}

	public String getGgsplc() {
		return ggsplc;
	}

	public void setGgsplc(String ggsplc) {
		this.ggsplc = ggsplc;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public void setZqbAnnouncementService(
			ZqbAnnouncementService zqbAnnouncementService) {
		this.zqbAnnouncementService = zqbAnnouncementService;
	}

	public ZqbAnnouncementService getZqbAnnouncementService() {
		return zqbAnnouncementService;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getKhbh() {
		return khbh;
	}

	/**
	 * 公告垃圾箱树结构
	 * 
	 * @return
	 */
	public String dustbinIndexTree() {
		return SUCCESS;
	}

	/**
	 * 公告垃圾箱主页
	 * 
	 * @return
	 */
	public String dustbinIndex() {
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		roleid = uc.getOrgroleid().toString();
		Map params = new HashMap();
		if ("".equals(zqdm) || zqdm == null) {
			zqdmtest = "";
		} else {
			zqdmtest = " and zqdm like ?";
			params.put(2,"%"+zqdm+"%");
		}
		if (!"".equals(zqjc) && zqjc != null) {
			params.put(1,"%"+zqjc+"%");
			khbh = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNO", "select CUSTOMERNO from bd_zqb_kh_base where zqjc like ?" + zqdmtest + "", params);
		} else if (!"".equals(zqdmtest)) {
			params.put(1,"1");
			khbh = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNO", "select CUSTOMERNO from bd_zqb_kh_base where 1=? " + zqdmtest + "", params);
		}
		list = zqbAnnouncementService.getDustbinList(khbh, pageSize, pageNumber, noticename, noticetype, startdate, enddate, bzlx);
		totalNum = zqbAnnouncementService.getDustbinListSize(khbh, noticename, noticetype, startdate, enddate, bzlx);
		return SUCCESS;
	}

	/**
	 * 公告垃圾箱删除
	 */
	public void thoroughDelete() {
		boolean flag = false;
		if (instanceid != null) {
			flag = zqbAnnouncementService.thoroughDelete(instanceid);
		}
		if (flag) {
			ResponseUtil.write(SUCCESS);
		} else {
			ResponseUtil.write(ERROR);
		}
	}
	public void khxxglRsx(){
		String flag = zqbAnnouncementService.khxxglRsx(startdate,enddate);
		ResponseUtil.write(flag);
	}
	/**
	 * 公告还原
	 */
	public void restore() {
		boolean flag = false;
		if (instanceid != null) {
			flag = zqbAnnouncementService.restore(instanceid);
		}
		if (flag) {
			ResponseUtil.write(SUCCESS);
		} else {
			ResponseUtil.write(ERROR);
		}
	}
	/**
	 * 判断角色,返回客户编号
	 */
	public void checkRole(){
		OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgroleid = orguser.getOrgroleid();
		String khbh = "";
		if(orgroleid==3L){
			khbh = orguser.getExtend1();
		}
		ResponseUtil.write("{\"KHBH\":\""+khbh+"\"}");
	}

	/**
	 * 进入公告呈报管理
	 * 
	 * @return
	 */
	public String index() {
		return SUCCESS;
	}

	public String index2() {
		glfUUID = zqbAnnouncementService.getConfigUUID("glfuuid");
		return SUCCESS;
	}
	
	public void getNoticeFileUuid(){
		HashMap data = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String noticefile;
		String companyname;
		if(data!=null){
			noticefile = data.get("NOTICEFILE")==null?"":data.get("NOTICEFILE").toString();
			companyname = data.get("COMPANYNAME")==null?"":data.get("COMPANYNAME").toString();
		}else{
			noticefile = "";
			companyname= "";
		}
		String flag="";
		if(noticefile!="" && companyname!=""){
			flag=noticefile+","+companyname;
		}else if(noticefile!=""){
			flag=noticefile;
		}else{
			flag=companyname;
		}
		ResponseUtil.write(flag);
	}
	
	public void getNoticeFileSXUuid(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		HashMap data = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String noticefile;
		String ids="";
		if(data!=null){
			noticefile = data.get("SXFJ")==null?"":data.get("SXFJ").toString();
			if(!noticefile.equals("")){
				List<FileUpload> l =FileUploadAPI.getInstance().getFileUploads(noticefile);
				if(l.size()==0){
					noticefile = "";
				}else{
					for (FileUpload fileUpload : l) {
						File file = new File(filepath+fileUpload.getFileUrl());//判断实体文件是否存在
						if(file.exists()){
							ids+=fileUpload.getFileId()+",";
						}
					}
					if(ids.equals(""))
						noticefile=ids;
				}
			}
		}else{
			noticefile = "";
		}
		ResponseUtil.write(noticefile);
	}

	public void downloadThisNoticeFile() {
		try {
			zqbAnnouncementService.downloadThisNoticeFile(instanceId);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	public void xxzcMbupload(){
		try {
			
			zqbAnnouncementService.xxzcMbupload();
		} catch (Exception e) {
		
		}
	}
	public void downloadThisNoticesxFile() {
		try {
			zqbAnnouncementService.downloadThisNoticesxFile(instanceId);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	//公告模板批量下载
	public void batchdownload() {
		String filenames="";
		try {
			filenames=zqbAnnouncementService.batchdownload(fileUUID,instanceids);
		} catch (Exception e) {
			logger.error(e,e);
		}
		ResponseUtil.write(filenames);
	}
	public void zqb_download_ggmb() {
		zqbAnnouncementService.downGgmb(filename);
	}
	public String count() {
		list = zqbAnnouncementService.count(noticename, noticetype, startdate,
				enddate, khmc, spzt, ggzy, zqdm, pageNumber, pageSize);
		totalNum = zqbAnnouncementService.countTotal(noticename, noticetype,
				startdate, enddate, khmc, spzt, ggzy, zqdm);
		return SUCCESS;
	}

	public String announcementExp() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.doExcelExp(response);
		return null;
	}

	public void deleteAnnouncement() {
		boolean flag = false;
		if (instanceid != null) {
			flag = zqbAnnouncementService.deleteAnnouncement(instanceid);
		}
		if (flag) {
			ResponseUtil.write(SUCCESS);
		} else {
			ResponseUtil.write(ERROR);
		}
	}

	public String announcementNotice() {
		if(tzbt!=null && !tzbt.equals("")){
			if(checkLoginInfo(tzbt)){
	    		return ERROR;
	    	}
		}
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		zqServer = config.get("zqServer");
		tzggGrantCUD = config.get("tzggGrantCUD")==null||config.get("tzggGrantCUD").equals("")?true:Arrays.asList(config.get("tzggGrantCUD").split(",")).contains(UserContextUtil.getInstance().getCurrentUserId());//通知公告CUD权限
		list = zqbAnnouncementService.getTZGGList(tzlx,tzbt, spzt, pageNumber,
				pageSize);
		totalNum = zqbAnnouncementService.getTZGGListSize(tzlx,tzbt, spzt);
		wdsize = zqbAnnouncementService.getTZGGWDSize();
		return SUCCESS;
	}
	/**
	 * 下载督导签约模板
	 */
	public void cwbbmb(){
		try {
			zqbAnnouncementService.cwbbmb();
		} catch (Exception e) {
		
		}
	}
	
	
	
	
	public String zqbAnnouncementGzsc() {
		
		gzscformid = DBUtil.getString(
				"SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'",
				"FORMID");
		gzscdemid = DBUtil.getString(
				"SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "ID");
		list = zqbAnnouncementService.getGZSCList(tzbt, spzt, pageNumber,
				pageSize);
		totalNum = zqbAnnouncementService.getGZSCListSize(tzbt, spzt);
		
		Long roleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		
		return SUCCESS;
	}

	public String zqbAnnouncementCheckHF() {
		gzschfformid = DBUtil.getString(
				"SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'",
				"FORMID");
		gzschfdemid = DBUtil.getString(
				"SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "ID");
		if (id != null && !id.equals("")) {
			list = zqbAnnouncementService.getGZSCHFList(id, spzt);
			totalNum = list.size();
		}
		gzscnothfnum = DBUtil
				.getInt("SELECT COUNT(*) NUM FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,E.DEPARTMENTNAME,E.HFSJ,F.ZQDM,ROWNUM RNUM FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,B.HFSJ,ORG.USERNAME,ORG.EXTEND1,ORG.DEPARTMENTNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.HFR=ORG.USERID) E ON D.ID=E.DID LEFT JOIN BD_ZQB_KH_BASE F ON E.EXTEND1=F.CUSTOMERNO) WHERE CID="
						+ id + " AND FKZT=0", "NUM");
		totalNum = DBUtil
				.getInt("SELECT COUNT(*) NUM FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,E.DEPARTMENTNAME,E.HFSJ,F.ZQDM,ROWNUM RNUM FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,B.HFSJ,ORG.USERNAME,ORG.EXTEND1,ORG.DEPARTMENTNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.HFR=ORG.USERID) E ON D.ID=E.DID LEFT JOIN BD_ZQB_KH_BASE F ON E.EXTEND1=F.CUSTOMERNO) WHERE USERID IS NOT NULL AND CID="
						+ id + "", "NUM");
		return SUCCESS;
	}
	//部门通知删除
	public void delGZSC(){
		
		try {
			String sql="delete  BD_XP_GZSC where id=?";
			HashMap params=new HashMap();
			params.put(1, id);
			DBUTilNew.update(sql, params);
			
			//删除时连同回复信息一同删除
			String sql1="delete  BD_XP_GZBCHF where cid=?";
			
			DBUTilNew.update(sql1, params);
			ResponseUtil.write("success");
			
		} catch (Exception e) {
			ResponseUtil.write("erro");
		}
	}
	public void fspGzscSendddMail() {
		String info = zqbAnnouncementService.fspGzscSendddMail(gzscinstanceid);
		ResponseUtil.write(info.equals("") ? "success" : info);
	}

	public String announcementNoticeReception() {
		list = zqbAnnouncementService.getTZGGReceptionList(tzbt, spzt,
				pageNumber, pageSize);
		totalNum = zqbAnnouncementService.getTZGGReceptionListSize(tzbt, spzt);
		return SUCCESS;
	}

	public void validationAnnouncement() {
		boolean flag = false;
		if (!"".equals(noticeno)) {
			flag = zqbAnnouncementService.validationAnnouncement(noticeno,
					khbh, instanceid);
		}
		if (flag) {
			ResponseUtil.write(SUCCESS);
		} else {
			ResponseUtil.write(ERROR);
		}
	}

	/**
	 * 根据客户查询公告
	 * 
	 * @return
	 */
	public String queryAnnouncementList() {
		if (khbh != null) {
			list = zqbAnnouncementService.queryAnnouncement(khbh);
		}
		return SUCCESS;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public void getRoleId() {
		String roleid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		ResponseUtil.write(roleid);
	}
    public String doSearchNews(){
    	if (pageNumber == 0)
			pageNumber = 1;
    	list=new ArrayList<HashMap>();
    	HashMap map =null;
    	int ks=(pageNumber-1)*pageSizes;
    	tzcount=ks;
    	List<news.service.Ssxx> Ssxxlist =HelloWebService.getSsxx(pageNumber,pageSizes);
    	for (news.service.Ssxx ssxx : Ssxxlist) {
    		tzcount++;
    		map = new HashMap();
    		map.put("JC", ssxx.getHref().getValue());
    		map.put("HC", ssxx.getTitle().getValue());
    		map.put("zywyh", ssxx.getTimes().getValue().substring(0, 16));
    		map.put("tzcount", tzcount);
    		
    		list.add(map);
    	}
    	totalNum=CmsMyDesktopPortletUtil.getInstance().getCommonTypeCount(new Long(0));
    	return SUCCESS;
    }
    public String doSearchQyyqNews(){
    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
    	
    	
    	
    	SimpleDateFormat sd = new SimpleDateFormat("yy-MM-dd HH:mm");
		Long roleId = us.getOrgroleid();
		StringBuffer sql=new StringBuffer();
		if(roleId==3){
			String gsmc = us.getDepartmentname();
			String customerno = us.getExtend1();
			SimpleDateFormat sdb = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			String times = sdb.format(calendar.getTime());
			sql.append(" TIMES>='").append(times).append("'");
			String zqdm = getDMZqdm(customerno, gsmc);
			if(zqdm!=null&&!zqdm.equals("")){
				sql.append(" AND TYPE=").append(zqdm).append("");
			}
		}else{
			SimpleDateFormat sdb = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			String times = sdb.format(calendar.getTime());
			sql.append(" TIMES>='").append(times).append("'");
			
			String cuid = us.getUserid();
			Long roleid = us.getOrgroleid();
			String zqdm =getCustomerZqdm(cuid, null, roleid);
			if(zqdm!=null&&!zqdm.equals("")){
				sql.append(" AND TYPE IN(").append(zqdm).append(")");
			}
		}
		try {
			if (pageNumber == 0)
				pageNumber = 1;
	    	list=new ArrayList<HashMap>();
	    	HashMap map =null;
	    	int ks=(pageNumber-1)*pageSizes;
	    	tzcount=ks;
			List<news.service.Cxdd> data = HelloWebService.getCxdd(pageNumber, pageSizes, sql.toString());
			for (news.service.Cxdd cxdd : data) {
				tzcount++;
	    		map = new HashMap();
	    		map.put("JC", cxdd.getHref().getValue());
	    		map.put("HC", cxdd.getTitle().getValue());
	    		map.put("zqdm", cxdd.getGsmc().getValue());
	    		map.put("zqjc", cxdd.getGfdm().getValue());
	    		map.put("zywyh", cxdd.getTimes().getValue().substring(0, 16));
	    		map.put("tzcount", tzcount);
	    	
	    		list.add(map);
			}
		} catch (Exception e) {
		}
		totalNum=CmsMyDesktopPortletUtil.getInstance().getCommonTypeCount(new Long(14));
    	return SUCCESS;
    }
    public String getDMZqdm(String khbh,String gsmc){
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" (CUSTOMERNAME LIKE ? OR ZQJC LIKE ?) AND ");
			parameter.add("%"+gsmc+"%");
			parameter.add("%"+gsmc+"%");
		}
		sql.append(" CUSTOMERNO=?");
		parameter.add(khbh);
		int index=1;
		StringBuffer result = new StringBuffer();
		String zqdm = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				zqdm = rs.getString("ZQDM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		if(zqdm!=null&&!zqdm.trim().equals("")){
			String[] split = zqdm.split(",");
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {  
					result.append("'").append(split[i]).append("'"); 
				}else if((i%999)==0 && i>0){  
					result.append("'").append(split[i]).append("') OR TYPE IN ("); 
				}else{
					result.append("'").append(split[i]).append("',");  
				}  
			}
		}
		return result.toString();
	}
    public String getCustomerZqdm(String userid,String gsmc,Long roleid){
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT TO_CHAR(WM_CONCAT(ZQDM)) ZQDM FROM (SELECT BASE.ZQDM ZQDM FROM (SELECT KHBH FROM BD_MDM_KHQXGLB WHERE 1=1 ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" AND KHMC LIKE ? ");
			parameter.add("%"+gsmc+"%");
		}
		if(roleid!=5){
			sql.append(" AND (SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1)=? OR SUBSTR(ZZCXDD,0, INSTR(ZZCXDD,'[',1)-1)=? OR SUBSTR(FHSPR,0, INSTR(FHSPR,'[',1)-1)=? OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1)=? OR SUBSTR(GGFBR,0, INSTR(GGFBR,'[',1)-1)=?)");
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
		}
		sql.append(" ) CXDD INNER JOIN (SELECT CUSTOMERNO,ZQDM FROM BD_ZQB_KH_BASE ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" WHERE CUSTOMERNAME LIKE ? OR ZQJC LIKE ?");
			parameter.add("%"+gsmc+"%");
			parameter.add("%"+gsmc+"%");
		}
		sql.append(" ) BASE ON CXDD.KHBH=BASE.CUSTOMERNO)");
		int index=1;
		StringBuffer result = new StringBuffer();
		String zqdm = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				zqdm = rs.getString("ZQDM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		if(zqdm!=null&&!zqdm.trim().equals("")){
			String[] split = zqdm.split(",");
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {  
					result.append("'").append(split[i]).append("'"); 
				}else if((i%999)==0 && i>0){  
					result.append("'").append(split[i]).append("') OR TYPE IN ("); 
				}else{
					result.append("'").append(split[i]).append("',");  
				}  
			}
		}
		return result.toString();
	}
	public String doSearch() {
		ggsplc = ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		dmggsplc = ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		if (pageNumber == 0)
			pageNumber = 1;
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		
		if ("3".equals(roleid)) {
			khbh = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
			khmc = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getDepartmentname();
		} else {
			Map params = new HashMap();
			if ("".equals(zqdm) || zqdm == null) {
				zqdmtest = "";
			} else {
				zqdmtest = " and zqdm = ?";
				params.put(2, zqdm);
			}
			if (!"".equals(zqjc) && zqjc != null) {
				params.put(1, zqjc);
				khmc = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNAME", "select CUSTOMERNAME from bd_zqb_kh_base where zqjc = ?" + zqdmtest + "", params);
				khbh = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNO", "select CUSTOMERNO from bd_zqb_kh_base where zqjc = ?" + zqdmtest + "", params);
			} else if (!"".equals(zqdmtest)) {
				params.put(1, "1");
				khmc = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNAME", "select CUSTOMERNAME from bd_zqb_kh_base where 1=? " + zqdmtest + "", params);
				khbh = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNO", "select CUSTOMERNO from bd_zqb_kh_base where 1=? " + zqdmtest + "", params);
			}
		}
		list = zqbAnnouncementService.getList(khbh, pageSize, pageNumber,noticename, noticetype, startdate, enddate, bzlx,spzt,zqdm,zqjc,dq,companyno);
		totalNum = zqbAnnouncementService.getTotalListSize(khbh, noticename,noticetype, startdate, enddate, bzlx,spzt,zqdm,zqjc,dq,companyno);
		gzxtyjdemid = DBUtil.getString("SELECT ID DEMID FROM SYS_DEM_ENGINE WHERE TITLE='股转系统意见'","DEMID");
		gzxtyjformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股转系统意见'","FORMID");
		/*if(!"3".equals(roleid)  || ("3".equals(roleid) && !configParameter.equals("dgzq"))){
			roleid="0";
		}*/
		return SUCCESS;
	}
	
	public String companyEnterprises(){
		if (pageNumber == 0)
			pageNumber = 1;
		if(firstload!=null&&firstload.equals("1")&&(starttime==null||starttime.equals(""))&&(endtime==null||endtime.equals(""))){
			String[] year_month_day = UtilDate.getNowdate().split("-");
			int year = Integer.parseInt(year_month_day[0]);
			int month = Integer.parseInt(year_month_day[1])-1;
			starttime = UtilDate.getFirstDayTimeOfMonth(year, month);
			endtime = UtilDate.getLastDayTimeOfMonth(year, month);
		}
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		if(configParameter.equals("sxzq") /*|| configParameter.equals("hlzq")*/){
			flag="show";
		}else{
			flag="hide";
		}
		list = zqbAnnouncementService.getCompanyEnterprisesList(pageSize,pageNumber,fullname,model,orderby,starttime,endtime);
		totalNum = zqbAnnouncementService.getCompanyEnterprisesSize(fullname,model,orderby,starttime,endtime);
		return SUCCESS;
	}
	/**
	 * 员工绩效导出
	 * @return
	 */
	public void ygjxExcel(){
		if(firstload!=null&&firstload.equals("1")&&(starttime==null||starttime.equals(""))&&(endtime==null||endtime.equals(""))){
			String[] year_month_day = UtilDate.getNowdate().split("-");
			int year = Integer.parseInt(year_month_day[0]);
			int month = Integer.parseInt(year_month_day[1])-1;
			starttime = UtilDate.getFirstDayTimeOfMonth(year, month);
			endtime = UtilDate.getLastDayTimeOfMonth(year, month);
		}
		//list = zqbAnnouncementService.getCompanyEnterprisesList();
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.ygjxExcel(response,fullname,model,orderby,starttime,endtime);
	}
	
	public String submsgEnterprises(){
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		if(configParameter.equals("sxzq") || configParameter.equals("hlzq")){
			flag="show";
		}else{
			flag="hide";
		}
		list = zqbAnnouncementService.submsgEnterprisesList(pageSize,pageNumber,fullname,model,orderby,starttime,endtime,departmentname);
		totalNum = zqbAnnouncementService.submsgEnterprisesSize(fullname,model,orderby,starttime,endtime,departmentname);
		return SUCCESS;
	}
	
	public void expCompanyList(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.expCompanyList(response,fullname,model,orderby,starttime,endtime);
	}
	
	public void expSubmsgList(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.expSubmsgList(response,fullname,model,orderby,starttime,endtime,departmentname);
	}

	// -----------股转系统意见开始---------------------
	public String AnnouncementUsermultiindex() {
		return SUCCESS;
	}
	
	public void getGzid(){
		String gzid = zqbAnnouncementService.getGzid(ggid);
		ResponseUtil.write(gzid);
	}
	
	public void getSMJinsid(){
		String gzid = zqbAnnouncementService.getSMJinsid(ggid);
		ResponseUtil.write(gzid);
	}

	public void AnnouncementUsermulti() {
		List<HashMap> gnmklist = zqbAnnouncementService
				.AnnouncementUsermulti(khbh);
		List<HashMap> root = new ArrayList();
		for (HashMap departmentlist : gnmklist) {
			HashMap gnmkmap = new HashMap();
			gnmkmap.put("id", departmentlist.get("id").toString());
			gnmkmap.put("pId", departmentlist.get("pId").toString());
			gnmkmap.put("name", departmentlist.get("name").toString());
			gnmkmap.put("userid", departmentlist.get("userid") == null ? ""
					: departmentlist.get("userid").toString());
			gnmkmap.put("open", false);
			gnmkmap.put("icon",
					departmentlist.get("userid") != null ? "iwork_img/user.png"
							: "iwork_img/km/treeimg/folderopen.gif");
			root.add(gnmkmap);
		}
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		ResponseUtil.write(jsonHtml.toString());
	}

	public void NsetDXYJTZR() {
		// 持续督导专员
		String khfzr = DBUtil
				.getString(
						"SELECT SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1) KHFZR FROM BD_MDM_KHQXGLB WHERE KHBH='"
								+ khbh + "'", "KHFZR");

		String currentuserid = UserContextUtil.getInstance().getCurrentUserId();
		Long currentOrgroleid = UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getOrgroleid();
		if ((khfzr == null || khfzr.equals(""))
				|| (khfzr != null && !khfzr.equals("") && khfzr
						.equals(currentuserid))) {
			ResponseUtil.write("");
		} else {
			if (currentOrgroleid != 3) {
				String userid = DBUtil
						.getString(
								"SELECT USERID FROM ORGUSER WHERE EXTEND1 = '"
										+ khbh
										+ "' AND ID=(SELECT MIN(ID) FROM ORGUSER WHERE EXTEND1='"
										+ khbh + "')", "USERID");
				String dxyjtzr = userid + ",";
				String name = UserContextUtil.getInstance()
						.getUserContext(userid).get_userModel().getUsername()
						+ "[" + userid + "]";
				String json = "<div id='"
						+ userid
						+ "'><span class='selItem'>"
						+ name
						+ "<img id='"
						+ userid
						+ "O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
				ResponseUtil.write(dxyjtzr + json);
			} else {
				String dxyjtzr = khfzr + ",";
				String name = UserContextUtil.getInstance()
						.getUserContext(khfzr).get_userModel().getUsername()
						+ "[" + khfzr + "]";
				String json = "<div id='"
						+ khfzr
						+ "'><span class='selItem'>"
						+ name
						+ "<img id='"
						+ khfzr
						+ "O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
				ResponseUtil.write(dxyjtzr + json);
			}
		}
	}

	public void YsetDXYJTZR() {
		Map params = new HashMap();
		params.put(1,ggid);
		String dxyjtzr = DBUTilNew.getDataStr("DXYJTZR","SELECT DXYJTZR FROM BD_XP_GZXTYJB WHERE GGID= ? ",params);
		String[] dxyjtzrarray = dxyjtzr.split(",");
		String json = "";
		String name = "";
		for (int i = 0; i < dxyjtzrarray.length; i++) {
			if (dxyjtzrarray[i] != null && !dxyjtzrarray[i].equals("")) {
				name = UserContextUtil.getInstance()
						.getUserContext(dxyjtzrarray[i]).get_userModel()
						.getUsername()
						+ "[" + dxyjtzrarray[i] + "]";
				json += "<div id='"
						+ dxyjtzrarray[i]
						+ "'><span class='selItem'>"
						+ name
						+ "<img id='"
						+ dxyjtzrarray[i]
						+ "O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
			}
		}
		ResponseUtil.write(json);
	}

	public String AnnouncementDxyjtzr() {
		gzxtyjhfdemid = DBUtil.getString("SELECT ID DEMID FROM SYS_DEM_ENGINE WHERE TITLE='股转系统意见回复'","DEMID");
		gzxtyjhfformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股转系统意见回复'","FORMID");
		if (gzyjid != null && !"".equals(gzyjid)) {
			list = zqbAnnouncementService.announcementDxyjtzr(gzyjid);
			gzyjid = list.get(0).get("ID") == null ? "" : list.get(0).get("ID")
					.toString();
			gzyjlrr = list.get(0).get("GZYJLRR") == null ? "" : list.get(0)
					.get("GZYJLRR").toString();
			gzyjlrsj = list.get(0).get("GZYJLRSJ") == null ? "" : list.get(0)
					.get("GZYJLRSJ").toString();
			gzyjjsm = list.get(0).get("GZYJJSM") == null ? "" : list.get(0)
					.get("GZYJJSM").toString();
			gzyjsmwd = list.get(0).get("GZYJSMWD") == null ? "" : list.get(0)
					.get("GZYJSMWD").toString();
			dxyjtzr = list.get(0).get("DXYJTZR") == null ? "" : list.get(0)
					.get("DXYJTZR").toString();
			dxyjtzrsb = list.get(0).get("DXYJTZRSB") == null ? "" : list.get(0)
					.get("DXYJTZRSB").toString();
			ggid = list.get(0).get("GGID") == null ? "" : list.get(0)
					.get("GGID").toString();
			khbh = list.get(0).get("KHBH") == null ? "" : list.get(0)
					.get("KHBH").toString();
			gzyjlrrid = list.get(0).get("GZYJLRRID") == null ? "" : list.get(0)
					.get("GZYJLRRID").toString();
		}
		gzxtyjlist = zqbAnnouncementService.getGzxtyjhfList(gzyjid);
		return SUCCESS;
	}
	
	public void expNotice(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.expNotice(khbh, noticename, noticetype, startdate, enddate, bzlx,spzt,zqdm,zqjc,dq,companyno,response);
	}

	public void NsetDXYJTZRHF() {
		String gzyjlrrid = DBUtil
				.getString("SELECT GZYJLRRID FROM BD_XP_GZXTYJB WHERE ID='"
						+ gzyjid + "'", "GZYJLRRID");
		String dxyjtzr = gzyjlrrid + ",";
		ResponseUtil.write(dxyjtzr);
		String name = UserContextUtil.getInstance().getUserContext(gzyjlrrid)
				.get_userModel().getUsername()
				+ "[" + gzyjlrrid + "]";
		String json = "<div id='"
				+ gzyjlrrid
				+ "'><span class='selItem'>"
				+ name
				+ "<img id='"
				+ gzyjlrrid
				+ "O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
		ResponseUtil.write(json);
	}

	public void YsetDXYJTZRHF() {
		String dxyjtzr = DBUtil
				.getString(
						"SELECT DXYJTZR FROM BD_XP_GZXTYJHFB WHERE ID=" + id,
						"DXYJTZR");
		
		String json = "";
		String name = "";
		if(dxyjtzr!=null && !"".equals(dxyjtzr)){
			String[] dxyjtzrarray = dxyjtzr.split(",");
			for (int i = 0; i < dxyjtzrarray.length; i++) {
				if (dxyjtzrarray[i] != null && !dxyjtzrarray[i].equals("")) {
					name = UserContextUtil.getInstance()
							.getUserContext(dxyjtzrarray[i]).get_userModel()
							.getUsername()
							+ "[" + dxyjtzrarray[i] + "]";
					json += "<div id='"
							+ dxyjtzrarray[i]
							+ "'><span class='selItem'>"
							+ name
							+ "<img id='"
							+ dxyjtzrarray[i]
							+ "O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
				}
			}
		}
		ResponseUtil.write(json);
	}

	// -----------股转系统意见结束---------------------

	public String zqIndex(){
		String demformid = DBUtil.getString("SELECT ID||'&'||FORMID DEMFORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息'", "DEMFORMID");
		String[] demform = demformid.split("&");
		demId = Long.parseLong(demform[0]);
		formid = Long.parseLong(demform[1]);
		if(pageSize==0){
			pageSize=1;
		}
		list = zqbAnnouncementService.getBondList(pageNumber, pageSize,zqmc,jc,zqfzbm,fxfs,chfs);
		totalNum = zqbAnnouncementService.getBondListSize(zqmc,jc,zqfzbm,fxfs,chfs);
		return SUCCESS;
	}
	public void zqDel(){
		zqbAnnouncementService.zqDel(instanceId);
	}
	public String zqExpDialog(){
		return SUCCESS;
	}
	public void zqExp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.zqExp(response,startdate,enddate);
	}
	
	public String zqSubIndex(){
		String demformid = DBUtil.getString("SELECT ID||'&'||FORMID DEMFORMID FROM SYS_DEM_ENGINE WHERE TITLE='付息时间表单'", "DEMFORMID");
		String[] demform = demformid.split("&");
		demId = Long.parseLong(demform[0]);
		formid = Long.parseLong(demform[1]);
		list = zqbAnnouncementService.getBondSubData(id);
		return SUCCESS;
	}
	
	public void getMaxRcid(){
		zqbAnnouncementService.getMaxRcids(fxsj,hksj,zqinsid,zjlsqkhz,jyszqdffx,dffxggytz,rcid,instanceId,pInstanceId);
	}

	public void getExtraFields(){
		zqbAnnouncementService.getExtraFields(content);
	}
	public void delSubBond(){
		HashMap data = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		SchCalendarService schCalendarService = (SchCalendarService)SpringBeanUtil.getBean("schCalendarService");
		IworkSchCalendar model = schCalendarService.getModel(data.get("RCID")==null?"0":data.get("RCID").toString());
		if(model!=null){
			schCalendarService.delete(model);
		}
		boolean flag = DemAPI.getInstance().removeFormData(instanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public void isDudaoCustomer() {
		String flag = zqbAnnouncementService.isDudaoCustomer(zqdm, zqjc);
		ResponseUtil.write(flag);
	}

	public void loadAnnouncement() {
		list = zqbAnnouncementService.loadAnnouncementList(ggid);
		if(list.size()==1){
			HashMap data = list.get(0);
			JSONArray json = JSONArray.fromObject(data);
			StringBuffer jsonHtml = new StringBuffer();
			jsonHtml.append(json);
			ResponseUtil.write(jsonHtml.toString());
			//[{JSZT=未回复, FSR=郭天顺, WJFILE=null, TZBT=bfgbfg, TZNR=知类知, FSSJ=2017-09-28 18:15, SFTZ=否, SFYHF=null, XGZL=null, FSZT=未完成, COUNT=, DCTM=null, TZLX=培训通知, ID=228, HFR=null, ZCHFSJ=2017-09-13 18:02}]
		}
//		return SUCCESS;
	}

	/**
	 * 获取历史版本
	 * 
	 * @return
	 */
	public String getLsbb() {
		list = zqbAnnouncementService.getLsbb(noticetext);
		return SUCCESS;
	}

	public String docShow() {
		//recordid=this.recordid;
		isLoadBookMarks=1L;
		html = WebOfficeTools.getInstance().getWebBrowserObjHtml();
		return SUCCESS;
	}
	private  String fj;
	
	public String getFj() {
		return fj;
	}

	public void setFj(String fj) {
		this.fj = fj;
	}

	public void docDownload() {
		if (recordid != null && !recordid.equals("")) {
			fj=this.fj;
			HttpServletResponse response = ServletActionContext.getResponse();
			zqbAnnouncementService.docDownload(response, recordid,fj);
		}
	}

	/**
	 * 公告查看
	 * 
	 * @return
	 */
	public String getGG() {
		
		try{
		// 编号、名称、类型、日期、附件、正文
			content = zqbAnnouncementService.getGG(instanceId, zqdmxs, zqjcxs);
		}
		catch(Exception e){
			logger.error(e,e);
		}			
		return SUCCESS;
	}
	
	public void getUserTime(){
		String jsonHtml = zqbAnnouncementService.getUserTime();
		ResponseUtil.write(jsonHtml.toString());
	}
	
	public void getTentUserDate(){
		String jsonHtml = zqbAnnouncementService.getTentUserDate(instanceId);
		ResponseUtil.write(jsonHtml.toString());
	}
	
	public void getTentUserDateForPro(){
		String jsonHtml = zqbAnnouncementService.getTentUserDateForPro(instanceId);
		ResponseUtil.write(jsonHtml.toString());
	}

	public void getTentUserDateForRc(){
		String jsonHtml = zqbAnnouncementService.getTentUserDateForRc(instanceId);
		ResponseUtil.write(jsonHtml.toString());
	}
	public String addIndex() {
		selectJSON = multiAddressBookService.getSelectOrgJson(input);
		return SUCCESS;
	}

	public String orgjson() throws ParseException {
		String json = "";
		if (nodeType != null) {
			if (id != null && nodeType.equals("dept")) {
				json = multiAddressBookService.getDeptAndUserJson(new Long(id),
						input);
			}
		} else {
			Long dept = null;
			UserContext uc = UserContextUtil.getInstance()
					.getCurrentUserContext();
			startDept = startDept == null ? new Long(0) : startDept;
			currentDept = currentDept == null ? "" : currentDept;
			parentDept = parentDept == null ? "" : parentDept;
			if (parentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getParentdepartmentid();
				json = multiAddressBookService.getDeptAndUserJson(
						new Long(dept), input);
			} else if (currentDept.equalsIgnoreCase("true")) {
				dept = uc.get_deptModel().getId();
				json = multiAddressBookService.getDeptAndUserJson(
						new Long(dept), input);
				addressName = uc.get_deptModel().getDepartmentname();
			} else if (!startDept.equals("")) {
				dept = startDept;
				json = multiAddressBookService.getDeptAndUserJson(
						new Long(dept), input);
			} else {
				json = multiAddressBookService.getCompanyNodeJson();
			}
		}
		ResponseUtil.write(json);
		return null;

	}

	public String noticeList() {
		list = zqbAnnouncementService.getNoticeList(roleid, status, ggid, pageNumber,
				pageSize);
		totalNum = zqbAnnouncementService.getNoticeListSize(roleid, status, ggid);
		tzcount = zqbAnnouncementService.getNoticeListSize(roleid, ggid);
		whfcount = zqbAnnouncementService.getNoticeWHFListSize(roleid, ggid);
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		tzggGrantCUD = config.get("tzggGrantCUD")==null||config.get("tzggGrantCUD").equals("")?true:Arrays.asList(config.get("tzggGrantCUD").split(",")).contains(UserContextUtil.getInstance().getCurrentUserId());//通知公告CUD权限
		return SUCCESS;
	}
	
	public void getproblems(){
		String json = zqbAnnouncementService.getproblems(ggid);
		ResponseUtil.write(json);
	}

	public void announcementSave() {
		
		flag = zqbAnnouncementService.save(tzbt.trim(), zchfsj, tznr, XGZL, hfr,
				sftz, ggid,tzlx,wjfile,dctm,extend5);
		
		ResponseUtil.write(flag);
	}

	public void announcementSaveOne() {
		flag = zqbAnnouncementService.saveOne(tzbt.trim(), zchfsj, tznr, XGZL, hfr,
				sftz, ggid,tzlx,wjfile,dctm,extend5);
		ResponseUtil.write(flag);
	}

	public void announcementSaveAll() {
		flag = zqbAnnouncementService.saveAll(tzbt.trim(), zchfsj, tznr, XGZL, hfr,
				sftz, ggid,tzlx,wjfile,dctm,extend5);
		ResponseUtil.write(flag);
	}
	public void announcementSaveWGP() {
		flag = zqbAnnouncementService.saveWGP(tzbt.trim(), zchfsj, tznr, XGZL, hfr, sftz, ggid,tzlx,wjfile,dctm,extend5);
		ResponseUtil.write(flag);
	}
	public void announcementSaveYXZ() {
		flag = zqbAnnouncementService.saveYXZ(tzbt.trim(), zchfsj, tznr, XGZL, hfr, sftz, ggid,USERID,tzlx,wjfile,dctm,extend5);
		ResponseUtil.write(flag);
	}

	public void announcementNoticeOne() {
		flag = zqbAnnouncementService.noticeOne();
		ResponseUtil.write(flag);
	}

	public void announcementNoticeAll() {
		flag = zqbAnnouncementService.noticeAll();
		ResponseUtil.write(flag);
	}

	public void announcementReplySave() {
		boolean flag = zqbAnnouncementService.replySave(hfcontent, xgzl,
				gonggaoID, hfqkid, sfcx);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}
	/**
	 * 通知公告查看情况
	 */
	public void announcementTzggCkqk() {
		try {
			if(ggid ==null) return;//xlj 漏洞扫描2018年5月15日11:01:14
			zqbAnnouncementService.tzggCkqk(Long.parseLong(ggid));
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	
	/**
	 * 设置对方已阅
	 */
	public void announcementTzggDfyy() {
		try {
			zqbAnnouncementService.tzggDfyy(ggid,userId);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	public String listCommitFile() {
		filehtml = zqbAnnouncementService.getfilehtml(ggid);
		return SUCCESS;
	}

	public void deleteReply() {
		boolean flag = zqbAnnouncementService.deleteReply(ggid, tzggid, hfr);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}
	
	public void deleteReplyAll() {
		boolean flag=true;
		
		if(ggid==null || tzggids==null || hfr==null || ggid.equals("")  || tzggids.equals("")  ){
			ResponseUtil.write("error");
			return;
		}
		/*if(ggid.indexOf(",")==-1  && tzggids.indexOf(",")==-1){
				flag = zqbAnnouncementService.deleteReply(ggid,Long.valueOf(tzggids), "");
		}else{
			String[] ggida=ggid.split(",");
			String[] tzggida=tzggids.split(",");
			//String[] hfra=hfr.split(",");
			//if(ggida.length != tzggida.length)ResponseUtil.write("error");
			for(int i=0;i<ggida.length;i++){
				flag = zqbAnnouncementService.deleteReply(ggida[i],Long.valueOf(tzggida[i]), "");
				if(!flag)break;
			}
		}*/
		flag =zqbAnnouncementService.deleteReplyAll(ggid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}
	
	public void updateMemo() {
		boolean flag = zqbAnnouncementService.updateMemo(id, memo);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void deleteSendNotice() {
		boolean flag = zqbAnnouncementService.deleteSendNotice(ggid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void updateSendNotice() {
		boolean flag = zqbAnnouncementService.updateSendNotice(ggid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public String announcementNoticeAdd() {
		roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		if (ggid != null && !"".equals(ggid)) {
			list = zqbAnnouncementService.getannouncementNoticeList(ggid);
			ggid = list.get(0).get("ID") == null ? "" : list.get(0).get("ID").toString();
			tzbt = list.get(0).get("TZBT") == null ? "" : list.get(0).get("TZBT").toString();
			zchfsj = list.get(0).get("ZCHFSJ") == null ? "" : list.get(0).get("ZCHFSJ").toString();
			tznr = list.get(0).get("TZNR") == null ? "" : list.get(0).get("TZNR").toString();
			hfr = list.get(0).get("HFR") == null ? "" : list.get(0).get("HFR").toString();
			sftz = list.get(0).get("SFTZ") == null ? "" : list.get(0).get("SFTZ").toString();
			count = list.get(0).get("COUNT") == null ? "" : list.get(0).get("COUNT").toString();
			XGZL = list.get(0).get("XGZL") == null ? "" : list.get(0).get("XGZL").toString();
			fszt = list.get(0).get("FSZT") == null ? "" : list.get(0).get("FSZT").toString();
			tzlx = list.get(0).get("TZLX") == null ? "" : list.get(0).get("TZLX").toString();
			wjfile = list.get(0).get("WJFILE") == null ? "" : list.get(0).get("WJFILE").toString();
			dctm = list.get(0).get("DCTM") == null ? "" : list.get(0).get("DCTM").toString();
			sfyhf = list.get(0).get("SFYHF") == null ? "" : list.get(0).get("SFYHF").toString();
			extend5 = list.get(0).get("EXTEND5") == null ? "" : list.get(0).get("EXTEND5").toString();
		}
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		demId=Long.parseLong(config.get("wjdemid"));
		formid=Long.parseLong(config.get("wjformid"));
		zqServer = config.get("zqServer");
		lcbh = config.get("fjsclx")==null?"":config.get("fjsclx").toString();
		tzggGrantCUD = config.get("tzggGrantCUD")==null||config.get("tzggGrantCUD").equals("")?true:Arrays.asList(config.get("tzggGrantCUD").split(",")).contains(UserContextUtil.getInstance().getCurrentUserId());//通知公告CUD权限
		return SUCCESS;
	}

	public String announcementNoticeReplyAdd() {
		if(ggid!=null && !"".equals(ggid) && isInteger(ggid)){
			list = zqbAnnouncementService.getNoticeList1(Long.valueOf(ggid));
			hflist = zqbAnnouncementService.getNoticeHfContent(hfqkid);
			if (list.size() > 0) {
				count = list.get(0).get("XGZL").toString();
			}
			Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
			tzlx = list.get(0).get("TZLX")==null?"":list.get(0).get("TZLX").toString();
		}
		return SUCCESS;
	}
	public void announcementNoticeReplySelect() {
		list = zqbAnnouncementService.getNoticeList(ggid);
		ResponseUtil.write(list.toString());
	}
	//首页查询公告类型
	public void announcementNoticeHade(){
		if(ggid ==null){
			ResponseUtil.write("");//xlj 漏洞扫描2018年5月15日11:01:14
			return;
		}
		if(!isInteger(ggid)){
			ResponseUtil.write("");//xlj 漏洞扫描2018年5月15日11:01:14
			return;
		}
		String tzlx = zqbAnnouncementService.gettzlx(Long.parseLong(ggid));
		ResponseUtil.write(tzlx);
	}
	/**
	 * 判断是否数字
	 * @param value
	 * @return
	 */
	 public static boolean isInteger(String value) {
		  try {
		   Integer.parseInt(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
	 }
	public void announcementNoticeRead() {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String hfxxbformid = config.get("hfxxbformid");
		String hfxxbdemid = config.get("hfxxbdemid");
		String hfxx=hfxxbformid+","+hfxxbdemid;
		ResponseUtil.write(hfxx);
	}

	public String showReply() {
		list = zqbAnnouncementService.getNoticeList(ggid);
		hflist = zqbAnnouncementService.getNoticeList(instanceId);
		return SUCCESS;
	}

	public String announcementNoticeList() {
		list = zqbAnnouncementService.getannouncementNoticeList(ggid);
		ggid = list.get(0).get("ID").toString();
		tzbt = list.get(0).get("TZBT").toString();
		zchfsj = list.get(0).get("ZCHFSJ").toString();
		tznr = list.get(0).get("TZNR").toString();
		hfr = list.get(0).get("HFR").toString();
		sftz = list.get(0).get("SFTZ").toString();
		return SUCCESS;
	}

	public void getEventTreeJson() {
		superman = zqbAnnouncementService.getIsSuperMan();
		String json = zqbAnnouncementService.getEventTreeJson(superman);
		ResponseUtil.write(json);
	}

	public String nmsxList() {
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		formid = zqbAnnouncementService.getConfig("nmxxformid");
		id = zqbAnnouncementService.getConfigUUID("nmxxid");
		list = zqbAnnouncementService.getNmsxList(khmc, khbh, nmxxStart,
				nmxxEnd, nmxx, pageNumber, pageSize);
		totalNum = zqbAnnouncementService.getNmsxListSize(khmc, khbh,
				nmxxStart, nmxxEnd, nmxx);
		return SUCCESS;
	}

	/**
	 * 问卷调查统计
	 * 
	 * @return
	 */

	public String voteList() {
		list = zqbAnnouncementService.getVoteList(pageNumber, pageSize);
		totalNum = zqbAnnouncementService.getVoteListSize();
		return SUCCESS;
	}

	public String voteGoList() {
		roleid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		list = zqbAnnouncementService.getNoFeedBackList2(ggid, zqdm, status,sszjj,ssbm);
		/*
		 * noFeedBackList=zqbAnnouncementService.getNoFeedBackList(ggid);
		 * feedBackList=zqbAnnouncementService.getSuccessFeedBackList(ggid);
		 */
		noFeedBackTotal = zqbAnnouncementService.getNoFeedBackListSize(ggid,
				zqdm,sszjj,ssbm);
		return SUCCESS;
	}
	
	public String previewPaper(){
		String now = UtilDate.getNowdate().substring(8);
		yjzdms = now.equals("01")?"预计本月是否发生：":"预计下周是否发生：";
		
		//list = null;
		feedBackList = zqbAnnouncementService.xpList();
		
		//zxlist = null;
		zxfeedBackList = zqbAnnouncementService.zxList(feedBackList.size());
		
		lockStatusForToolsNav = "1";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			roleid = uc.get_userModel().getOrgroleid().toString();
		}
		zqdm = "证券代码";
		zqjc = "证券简称";
		customername = "客户名称";
		username = "";
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		zqServer = config.get("zqServer");
		return SUCCESS;
	}
	
	public void downCheck() {
		Map params =new HashMap();
		params.put(1, ggid);
		String qydemid = DBUTilNew.getDataStr("ID"," select id from bd_vote_wjdc where tzggid= ?  ", params);
		//0未回复   1有人回复
		ResponseUtil.write(qydemid==""?"0":"1"); 
	}
	//导出
	public void downExcel(){
		try {
			HttpServletResponse responses = ServletActionContext.getResponse();
			zqbAnnouncementService.downExcel(responses,ggid);
		} catch (Exception e) {
		}
	}
	public String voteWjdcList(){
		if(ggid==null || !isInteger(ggid)) return ERROR;
		String userid = "";
		OrgUser uc = null;
		if(instanceid!=null && !"".equals(instanceid)){
			userid = DemAPI.getInstance().getFromData(instanceid).get("HFRID")==null?"":DemAPI.getInstance().getFromData(instanceid).get("HFRID").toString();
			uc = UserContextUtil.getInstance().getUserContext(userid)._userModel;
		}else{
			uc = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			userid = uc.getUserid();
		}
		
		roleid = zqbAnnouncementService.getOrgroleid(uc);
		Map params = new HashMap();
		params.put(1,userid);
		params.put(2,Long.parseLong(ggid));
		lockStatusForToolsNav = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_CXDDRCGZFK WHERE USERID= ? AND TZGGID= ? ",params);
		if (lockStatusForToolsNav == null || lockStatusForToolsNav.equals("")) {
			lockStatusForToolsNav = "0";
		}
		userId=userid;
		list = zqbAnnouncementService.getCxddFeedBackList(Long.parseLong(ggid),userid,roleid,lockStatusForToolsNav);
		feedBackList = zqbAnnouncementService.getCxddSuccessFeedBackList(Long.parseLong(ggid),userid, list.size(),roleid,lockStatusForToolsNav);
		return SUCCESS;
	}
	//查询信披自查沟通附件数量
	public void voteGoxpfjsl(){
		int count  = zqbAnnouncementService.voteGoxpfjsl(ggid);
		if(count==0){
			ResponseUtil.write("0");	
		}
	}
	
	//验证信披问题是否重复
	public void xpwtcfyz(){
		String fls="false";
		try {
			fls= zqbAnnouncementService.xpwtcfyz(flag,instanceid);
		} catch (Exception e) {
			
		}
		ResponseUtil.write(fls);
	}
	//信披问题最多添加50个
		public void xpwttjyz(){
			String fls="false";
			try {
				fls= zqbAnnouncementService.xpwtcfyz();
			} catch (Exception e) {
				
			}
			ResponseUtil.write(fls);
		}	
	public String xxzcDr(){
		return "success";
	}
	//打包信披自查沟通附件
	public void votedownloadxpfjsl(){
		//获取当前公告回复数据
		list = zqbAnnouncementService.getgghfvotesj(ggid);
		String filenames="";
		try {
			 filenames=zqbAnnouncementService.votedownload(list);
		} catch (Exception e) {
		}
		ResponseUtil.write(filenames);
	}
	private File upFile;
	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	//导入信披自查问题
	public void xxzcDrupfile(){
		demid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='信披自查问题表单'", "ID");
		if (this.upFile != null) {
			flag = zqbAnnouncementService.doExcelImp(upFile,Long.parseLong(demid));
			ResponseUtil.write(flag);
		} else {
			ResponseUtil.write("error");
		}
		
	}
	public void gddhyztx(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List lables=new ArrayList();
		lables.add("id");
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, username);
		params.put(3, username);
		List<HashMap> list=DBUTilNew.getDataList(lables, "select * from BD_MDM_KHQXGLB where khbh=? and (khfzr = ? or fhspr=?)", params);
		if((list!=null && list.size()>0)||uc.get_userModel().getOrgroleid()==3){
			ResponseUtil.write("0");
		}else{
			ResponseUtil.write("1");
		}
	}
	public void updGlhy(){
		Map params = new HashMap();
		params.put(1, JC);
		params.put(2, HC);
		params.put(3, instanceid);
		String sql = "update BD_XP_QTGGZLLC z  set z.meetid= ? ,  z.meetname= ? where z.noticesq=(select t.noticesq from BD_MEET_QTGGZL t where t.instanceid= ? ) ";
		try {
			DBUTilNew.update(sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void votedownload(){
		zqbAnnouncementService.down(filename);
		
	}
	public void zqb_download_tzgg(){
		zqbAnnouncementService.downTzgg(filename);
		
	}
	public void impWjdc(){
		zqbAnnouncementService.impWjdc(uuid);
	}
	
	public String cxddCustomerList(){
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = uc.getUserid();
		
		roleid = zqbAnnouncementService.getOrgroleid(uc);
		Map params = new HashMap();
		params.put(1,userid);
		params.put(2,Long.parseLong(ggid));
		lockStatusForToolsNav = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_CXDDRCGZFK WHERE USERID= ?  AND TZGGID= ? ",params);
		if (lockStatusForToolsNav == null || lockStatusForToolsNav.equals("")) {
			lockStatusForToolsNav = "0";
		}
		
		list = zqbAnnouncementService.getCxddFeedBackList(Long.parseLong(ggid),userid,roleid,lockStatusForToolsNav);
		feedBackList = zqbAnnouncementService.getCxddSuccessFeedBackList(Long.parseLong(ggid),userid, list.size(),roleid,lockStatusForToolsNav);
		return SUCCESS;
	}
	
	public String cxddCustomerListSub(){
		OrgUser uc = UserContextUtil.getInstance().getUserContext(hfr)._userModel;
		String userid = uc.getUserid();
		
		roleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid()+"";
		Map params = new HashMap();
		params.put(1,userid);
		params.put(2,ggid);
		lockStatusForToolsNav = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_CXDDRCGZFK WHERE USERID= ?  AND TZGGID= ? ",params);
		if (lockStatusForToolsNav == null || lockStatusForToolsNav.equals("")) {
			lockStatusForToolsNav = "0";
		}
		
		list = zqbAnnouncementService.getCxddFeedBackList(Long.parseLong(ggid),userid,roleid,lockStatusForToolsNav);
		feedBackList = zqbAnnouncementService.getCxddSuccessFeedBackList(Long.parseLong(ggid),userid, list.size(),roleid,lockStatusForToolsNav);
		return SUCCESS;
	}
	
	public String cxddList(){
		list = zqbAnnouncementService.getCxddList(pageNumber, pageSize);
		totalNum = zqbAnnouncementService.getCxddListSize();
		return SUCCESS;
	}
	
	public String cxddGoList() {
		roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		list = zqbAnnouncementService.getNoFeedBackListCxdd(ggid,status);
		noFeedBackTotal = zqbAnnouncementService.getNoFeedBackListCxddSize(ggid);
		return SUCCESS;
	}
	
	public void cxddUnlockthis() {
		zqbAnnouncementService.cxddUnlockthis(lockstatus, hfr,ggid);
	}
	public void cxddLockthis() {
		zqbAnnouncementService.cxddLockthis(lockstatus, hfr, ggid);
	}
	
	public void zqbCxddUpdateCxddfk() {
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		String username = uc._userModel.getUsername();
		Long orgroleid = uc.get_userModel().getOrgroleid();
		StringBuffer sql = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		List<String> str = new ArrayList<String>();
		if (uc != null && orgroleid == 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
		} else if (uc != null && orgroleid != 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
			str.add("PZ");
		}
		String[] string2 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
				"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
				"41", "42", "43", "44", "45", "46", "47", "48", "49", "50" };
		for (String s1 : str) {
			for (String s2 : string2) {
				if (Long.parseLong(s2) > Long.parseLong(forSize)) {
					break;
				}
				String value = request.getParameter(s1 + s2) == null ? ""
						: request.getParameter(s1 + s2).trim().toString();
				if (value != null && !value.equals("")) {
					list.add(s1 + s2);
					map.put(s1 + s2, request.getParameter(s1 + s2));
				} else {
					list.add(s1 + s2);
					map.put(s1 + s2, "");
				}
			}
		}
		for (String string : list) {
			sql.append("" + string + "=?,");
		}

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE BD_VOTE_CXDDRCGZFK SET ");
		sb.append(sql.toString().substring(0, sql.toString().length() - 1));
		if (uc != null && orgroleid == 3l) {
			sb.append(",USERNAME=?,USERID=?");
			list.add("USERNAME");
			list.add("USERID");
			map.put("USERNAME", username);
			map.put("USERID", userid);
		}
		sb.append(" WHERE USERID=? AND TZGGID=?");
		zqbAnnouncementService.zqbVoteUpdateXpzcfk(sb.toString(), CUSTOMERNO,TZGGID, list, map);
	}
	public String voteCustomerList() {
		yjzdms = dqrq.equals("01")?"预计本月是否发生：":"预计下周是否发生：";
		list = zqbAnnouncementService.getFeedBackList(Long.parseLong(ggid), khbh);
		feedBackList = zqbAnnouncementService.getSuccessFeedBackList(Long.parseLong(ggid), khbh, list.size());
		
		zxlist = zqbAnnouncementService.getZxFeedBackList(ggid, khbh);
		zxfeedBackList = zqbAnnouncementService.getZxSuccessFeedBackList(Long.parseLong(ggid), khbh, zxlist.size());
		
		Map params = new HashMap();
		params.put(1,khbh);
		params.put(2,Long.parseLong(ggid));
		lockStatusForToolsNav = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ?  AND TZGGID=? ", params);
		
		if (lockStatusForToolsNav == null || lockStatusForToolsNav.equals("")) {
			lockStatusForToolsNav = "0";
		}
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			roleid = uc.get_userModel().getOrgroleid() + "";
		}
		
		Map param = new HashMap();
		param.put(1,khbh);
		zqdm = DBUTilNew.getDataStr("ZQDM","SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO= ? ",param);
		zqjc = DBUTilNew.getDataStr("ZQJC","SELECT ZQJC FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO= ? ", param);
		customername = DBUTilNew.getDataStr("CUSTOMERNAME","SELECT CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO= ? ", param);
		username = DBUTilNew.getDataStr("USERNAME","SELECT USERNAME FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ?  AND TZGGID= ? ", params);
		
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		zqServer = config.get("zqServer");
		return SUCCESS;
	}
	
	public void getWjdcContent(){
		String jsonString=zqbAnnouncementService.getWjdcContent(ggid,khbh);
		ResponseUtil.write(jsonString);
	}
	
	public void getWjdcWt(){
		String jsonString=zqbAnnouncementService.getWjdcWt(ggid);
		ResponseUtil.write(jsonString);
	}
	
	public void getTzggFssj(){
		String jsonString=zqbAnnouncementService.getTzggFssj(ggid);
		ResponseUtil.write(jsonString);
	}

	public String showCommunicate() {
		return SUCCESS;
	}

	public void communicateSave() throws Exception {
		boolean flag;
		if (gtid == null || "".equals(gtid)) {
			flag = zqbAnnouncementService.communicateSave(content, customerno,
					ggid, xpgtFile);
		} else {
			flag = zqbAnnouncementService.communicateUpdate(content,
					customerno, gtid, ggid, xpgtFile);
		}
		if (!flag) {
			ResponseUtil.write("error");
		} else {
			ResponseUtil.write("success");
		}
	}

	public String updateCommunicate() {
		list = zqbAnnouncementService.getCommunicateList(customerno, Long.parseLong(ggid));
		content = list.get(0).get("CONTENT") == null ? "" : list.get(0)
				.get("CONTENT").toString();
		gtid = list.get(0).get("GTID").toString();
		customerno = list.get(0).get("CUSTOMERNO").toString();
		ggid = list.get(0).get("GGID").toString();
		xpgtFile = list.get(0).get("XPGTFILE") == null ? "" : list.get(0)
				.get("XPGTFILE").toString();
		String str = xpgtFile;
		if (!str.equals("")) {
			String[] noticefilearray = str.split(",");
			StringBuffer file = new StringBuffer();
			if (noticefilearray.length > 0) {
				for (int i = 0; i < noticefilearray.length; i++) {
					String filename = DBUtil
							.getString(
									"SELECT FILE_SRC_NAME FROM SYS_UPLOAD_FILE WHERE FILE_ID='"
											+ noticefilearray[i] + "'",
									"FILE_SRC_NAME");
					String fileDivId = noticefilearray[i];
					file.append("<div  id=\"")
							.append(fileDivId)
							.append("\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">\n");
					file.append("<div style=\"align:right;float:right;\">&nbsp;&nbsp;\n");
					file.append("<a href=\"javascript:uploadifyReomve('")
							.append("XPGTFILE")
							.append("','")
							.append(fileDivId)
							.append("','")
							.append(fileDivId)
							.append("');\"><img src=\"/iwork_img/del3.gif\"/></a>\n");
					file.append("</div>\n");
					String icon = "iwork_img/attach.png";
					if (filename != null) {
						icon = FileType.getFileIcon(filename);
					}
					file.append(
							"<span><a href=\"uploadifyDownload.action?fileUUID=")
							.append(fileDivId)
							.append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"")
							.append(icon).append("\"/>").append(filename)
							.append("</a></span>\n");
					file.append("</div>\n");
				}
			}
			filecontent = file.toString();
		}
		return SUCCESS;
	}

	public String nmindex() {
		return SUCCESS;
	}

	public void delete() {
		String info = zqbAnnouncementService.deleteNmxx(instanceid);
		ResponseUtil.write(info);
	}

	public void fileUpload() {
		String name = zqbAnnouncementService.fileUpload(instanceid);
		ResponseUtil.write(name);
	}

	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public Long getDemId() {
		return demId;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public Long getFormid() {
		return formid;
	}

	public void setFormid(Long formid) {
		this.formid = formid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	/* 企业内部审核人 */
	private String nbshr;
	private Long nbshrFormid;
	private Long nbshrDemId;

	public Long getNbshrFormid() {
		return nbshrFormid;
	}

	public void setNbshrFormid(Long nbshrFormid) {
		this.nbshrFormid = nbshrFormid;
	}

	public Long getNbshrDemId() {
		return nbshrDemId;
	}

	public void setNbshrDemId(Long nbshrDemId) {
		this.nbshrDemId = nbshrDemId;
	}

	public String getNbshr() {
		return nbshr;
	}

	public void setNbshr(String nbshr) {
		this.nbshr = nbshr;
	}

	/* 该集合用于存储该公司的内部审核人员 */
	List<HashMap> nbshrlist;

	public List<HashMap> getNbshrlist() {
		return nbshrlist;
	}

	public void setNbshrlist(List<HashMap> nbshrlist) {
		this.nbshrlist = nbshrlist;
	}

	/**
	 * 此方法用于显示当前董秘的企业内部人员可分配信息
	 * 
	 * @return
	 */
	public List<HashMap> getNbshrList() {
		nbshrlist = zqbAnnouncementService.getNbshrList();
		return nbshrlist;
	}

	/**
	 * 此方法用于获取企业内部审核人员表单的formid和demid
	 * 
	 * @return
	 */
	public String getConpanyDuDao() {
		getNbshrList();
		nbshrFormid = DBUtil.getLong(
				"SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='企业内部审核人员'",
				"FORMID");
		nbshrDemId = DBUtil.getLong(
				"SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='企业内部审核人员'", "ID");
		if (nbshrFormid != null && !nbshrFormid.equals("")
				&& nbshrDemId != null && !nbshrDemId.equals("")) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public void addUserCheckUserid() {

		Map params = new HashMap();
		params.put(1,USERID);
		String checkuserid = DBUTilNew.getDataStr("USERID","SELECT USERID FROM ORGUSER WHERE USERID= ? ",params);
		if (checkuserid != null && !checkuserid.equals("")) {
			ResponseUtil.write("1");
		}
	}

	/**
	 * 获取可设置名单
	 */
	List cansetList;

	public List getCansetList() {
		return cansetList;
	}

	public void setCansetList(List cansetList) {
		this.cansetList = cansetList;
	}

	public String getNbshrCansetList() {
		cansetList = zqbAnnouncementService.getNbshrCansetList();
		if (cansetList != null) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public String zqbFzgsCwbb() {
		cwbbformid = DBUtil.getString(
				"SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '财务报表表单'",
				"FORMID");
		cwbbdemid = DBUtil.getString(
				"SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '财务报表表单'", "ID");

		cwbbformidsc = DBUtil.getString(
				"SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '财务报表上传'",
				"FORMID");
		cwbbdemidsc = DBUtil.getString(
				"SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '财务报表上传'", "ID");
		cwbbcontent = zqbAnnouncementService.zqbFzgsCwbb(khbh, id);
		return SUCCESS;
	}

	public void zqbFzgsNamecheck() {
		boolean flag = instanceId == 0l;
		if (flag) {
			int num = 0;
			Map params = new HashMap();
			params.put(1, khbh);
			params.put(2, khmc);
			params.put(3, gsmc);
			num = DBUTilNew.getInt("GSMC","SELECT COUNT(GSMC) GSMC FROM BD__FZGSGLZB WHERE KHBH= ?  AND KHMC= ?  AND GSMC= ? ",params);
			if (num != 0) {
				ResponseUtil.write("1");
			}
		} else {
			ResponseUtil.write("0");
		}
	}

	/**
	 * 该方法用于删除财务报表
	 */
	public void zqbFzgsUpdateDeleteStatus() {
		zqbAnnouncementService.setZqbFzgsUpdateDeleteStatus(bid);
	}

	/**
	 * 该方法用于解锁客户使客户可编辑 “信披自查反馈”
	 */
	public void zqbVoteUnlockthis() {
		zqbAnnouncementService.setZqbVoteUnlockthis(lockstatus, customerno,
				ggid);
	}

	/**
	 * 该方法用于锁定客户使客户不可编辑 “信披自查反馈”
	 */
	public void zqbVoteLockthis() {
		zqbAnnouncementService.setZqbVoteLockthis(lockstatus, customerno, ggid);
	}
	public void zqbVoteLockmore() {
		zqbAnnouncementService.setZqbVoteLockmore(lockstatus, customerno, ggid);
	}

	public void zqbGlfGetThisData() {
		ResponseUtil
				.write(zqbAnnouncementService.zqbGlfGetThisData(gsmc, khbh));
	}

	public void zqbFzgsGetThisData() {
		ResponseUtil.write(zqbAnnouncementService
				.zqbFzgsGetThisData(gsmc, khbh));
	}

	private String CUSTOMERNAME;
	private String CUSTOMERNO;
	private String USERID;
	private String TZGGID;
	private String USERNAME;
	private String WT1;
	private String WT2;
	private String WT3;
	private String WT4;
	private String WT5;
	private String WT6;
	private String WT7;
	private String WT8;
	private String WT9;
	private String WT10;
	private String WT11;
	private String WT12;
	private String WT13;
	private String WT14;
	private String WT15;
	private String WT16;
	private String WT17;
	private String WT18;
	private String WT19;
	private String WT20;
	private String WT21;
	private String WT22;
	private String WT23;
	private String WT24;
	private String WT25;
	private String WT26;
	private String WT27;
	private String WT28;
	private String WT29;
	private String WT30;
	private String WT31;
	private String WT32;
	private String WT33;
	private String WT34;
	private String WT35;
	private String WT36;
	private String WT37;
	private String WT38;
	private String WT39;
	private String WT40;
	private String WT41;
	private String WT42;
	private String WT43;
	private String WT44;
	private String WT45;
	private String WT46;
	private String WT47;
	private String WT48;
	private String WT49;
	private String WT50;
	private String AS1;
	private String AS2;
	private String AS3;
	private String AS4;
	private String AS5;
	private String AS6;
	private String AS7;
	private String AS8;
	private String AS9;
	private String AS10;
	private String AS11;
	private String AS12;
	private String AS13;
	private String AS14;
	private String AS15;
	private String AS16;
	private String AS17;
	private String AS18;
	private String AS19;
	private String AS20;
	private String AS21;
	private String AS22;
	private String AS23;
	private String AS24;
	private String AS25;
	private String AS26;
	private String AS27;
	private String AS28;
	private String AS29;
	private String AS30;
	private String AS31;
	private String AS32;
	private String AS33;
	private String AS34;
	private String AS35;
	private String AS36;
	private String AS37;
	private String AS38;
	private String AS39;
	private String AS40;
	private String AS41;
	private String AS42;
	private String AS43;
	private String AS44;
	private String AS45;
	private String AS46;
	private String AS47;
	private String AS48;
	private String AS49;
	private String AS50;
	private String BZ1;
	private String BZ2;
	private String BZ3;
	private String BZ4;
	private String BZ5;
	private String BZ6;
	private String BZ7;
	private String BZ8;
	private String BZ9;
	private String BZ10;
	private String BZ11;
	private String BZ12;
	private String BZ13;
	private String BZ14;
	private String BZ15;
	private String BZ16;
	private String BZ17;
	private String BZ18;
	private String BZ19;
	private String BZ20;
	private String BZ21;
	private String BZ22;
	private String BZ23;
	private String BZ24;
	private String BZ25;
	private String BZ26;
	private String BZ27;
	private String BZ28;
	private String BZ29;
	private String BZ30;
	private String BZ31;
	private String BZ32;
	private String BZ33;
	private String BZ34;
	private String BZ35;
	private String BZ36;
	private String BZ37;
	private String BZ38;
	private String BZ39;
	private String BZ40;
	private String BZ41;
	private String BZ42;
	private String BZ43;
	private String BZ44;
	private String BZ45;
	private String BZ46;
	private String BZ47;
	private String BZ48;
	private String BZ49;
	private String BZ50;
	private String PL1;
	private String PL2;
	private String PL3;
	private String PL4;
	private String PL5;
	private String PL6;
	private String PL7;
	private String PL8;
	private String PL9;
	private String PL10;
	private String PL11;
	private String PL12;
	private String PL13;
	private String PL14;
	private String PL15;
	private String PL16;
	private String PL17;
	private String PL18;
	private String PL19;
	private String PL20;
	private String PL21;
	private String PL22;
	private String PL23;
	private String PL24;
	private String PL25;
	private String PL26;
	private String PL27;
	private String PL28;
	private String PL29;
	private String PL30;
	private String PL31;
	private String PL32;
	private String PL33;
	private String PL34;
	private String PL35;
	private String PL36;
	private String PL37;
	private String PL38;
	private String PL39;
	private String PL40;
	private String PL41;
	private String PL42;
	private String PL43;
	private String PL44;
	private String PL45;
	private String PL46;
	private String PL47;
	private String PL48;
	private String PL49;
	private String PL50;
	private String PZ1;
	private String PZ2;
	private String PZ3;
	private String PZ4;
	private String PZ5;
	private String PZ6;
	private String PZ7;
	private String PZ8;
	private String PZ9;
	private String PZ10;
	private String PZ11;
	private String PZ12;
	private String PZ13;
	private String PZ14;
	private String PZ15;
	private String PZ16;
	private String PZ17;
	private String PZ18;
	private String PZ19;
	private String PZ20;
	private String PZ21;
	private String PZ22;
	private String PZ23;
	private String PZ24;
	private String PZ25;
	private String PZ26;
	private String PZ27;
	private String PZ28;
	private String PZ29;
	private String PZ30;
	private String PZ31;
	private String PZ32;
	private String PZ33;
	private String PZ34;
	private String PZ35;
	private String PZ36;
	private String PZ37;
	private String PZ38;
	private String PZ39;
	private String PZ40;
	private String PZ41;
	private String PZ42;
	private String PZ43;
	private String PZ44;
	private String PZ45;
	private String PZ46;
	private String PZ47;
	private String PZ48;
	private String PZ49;
	private String PZ50;
	private String forSize;

	public String getForSize() {
		return forSize;
	}

	public void setForSize(String forSize) {
		this.forSize = forSize;
	}

	public String getCUSTOMERNAME() {
		return CUSTOMERNAME;
	}

	public void setCUSTOMERNAME(String cUSTOMERNAME) {
		CUSTOMERNAME = cUSTOMERNAME;
	}

	public String getCUSTOMERNO() {
		return CUSTOMERNO;
	}

	public void setCUSTOMERNO(String cUSTOMERNO) {
		CUSTOMERNO = cUSTOMERNO;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public String getTZGGID() {
		return TZGGID;
	}

	public void setTZGGID(String tZGGID) {
		TZGGID = tZGGID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getWT1() {
		return WT1;
	}

	public void setWT1(String wT1) {
		WT1 = wT1;
	}

	public String getWT2() {
		return WT2;
	}

	public void setWT2(String wT2) {
		WT2 = wT2;
	}

	public String getWT3() {
		return WT3;
	}

	public void setWT3(String wT3) {
		WT3 = wT3;
	}

	public String getWT4() {
		return WT4;
	}

	public void setWT4(String wT4) {
		WT4 = wT4;
	}

	public String getWT5() {
		return WT5;
	}

	public void setWT5(String wT5) {
		WT5 = wT5;
	}

	public String getWT6() {
		return WT6;
	}

	public void setWT6(String wT6) {
		WT6 = wT6;
	}

	public String getWT7() {
		return WT7;
	}

	public void setWT7(String wT7) {
		WT7 = wT7;
	}

	public String getWT8() {
		return WT8;
	}

	public void setWT8(String wT8) {
		WT8 = wT8;
	}

	public String getWT9() {
		return WT9;
	}

	public void setWT9(String wT9) {
		WT9 = wT9;
	}

	public String getWT10() {
		return WT10;
	}

	public void setWT10(String wT10) {
		WT10 = wT10;
	}

	public String getWT11() {
		return WT11;
	}

	public void setWT11(String wT11) {
		WT11 = wT11;
	}

	public String getWT12() {
		return WT12;
	}

	public void setWT12(String wT12) {
		WT12 = wT12;
	}

	public String getWT13() {
		return WT13;
	}

	public void setWT13(String wT13) {
		WT13 = wT13;
	}

	public String getWT14() {
		return WT14;
	}

	public void setWT14(String wT14) {
		WT14 = wT14;
	}

	public String getWT15() {
		return WT15;
	}

	public void setWT15(String wT15) {
		WT15 = wT15;
	}

	public String getWT16() {
		return WT16;
	}

	public void setWT16(String wT16) {
		WT16 = wT16;
	}

	public String getWT17() {
		return WT17;
	}

	public void setWT17(String wT17) {
		WT17 = wT17;
	}

	public String getWT18() {
		return WT18;
	}

	public void setWT18(String wT18) {
		WT18 = wT18;
	}

	public String getWT19() {
		return WT19;
	}

	public void setWT19(String wT19) {
		WT19 = wT19;
	}

	public String getWT20() {
		return WT20;
	}

	public void setWT20(String wT20) {
		WT20 = wT20;
	}

	public String getWT21() {
		return WT21;
	}

	public void setWT21(String wT21) {
		WT21 = wT21;
	}

	public String getWT22() {
		return WT22;
	}

	public void setWT22(String wT22) {
		WT22 = wT22;
	}

	public String getWT23() {
		return WT23;
	}

	public void setWT23(String wT23) {
		WT23 = wT23;
	}

	public String getWT24() {
		return WT24;
	}

	public void setWT24(String wT24) {
		WT24 = wT24;
	}

	public String getWT25() {
		return WT25;
	}

	public void setWT25(String wT25) {
		WT25 = wT25;
	}

	public String getWT26() {
		return WT26;
	}

	public void setWT26(String wT26) {
		WT26 = wT26;
	}

	public String getWT27() {
		return WT27;
	}

	public void setWT27(String wT27) {
		WT27 = wT27;
	}

	public String getWT28() {
		return WT28;
	}

	public void setWT28(String wT28) {
		WT28 = wT28;
	}

	public String getWT29() {
		return WT29;
	}

	public void setWT29(String wT29) {
		WT29 = wT29;
	}

	public String getWT30() {
		return WT30;
	}

	public void setWT30(String wT30) {
		WT30 = wT30;
	}

	public String getWT31() {
		return WT31;
	}

	public void setWT31(String wT31) {
		WT31 = wT31;
	}

	public String getWT32() {
		return WT32;
	}

	public void setWT32(String wT32) {
		WT32 = wT32;
	}

	public String getWT33() {
		return WT33;
	}

	public void setWT33(String wT33) {
		WT33 = wT33;
	}

	public String getWT34() {
		return WT34;
	}

	public void setWT34(String wT34) {
		WT34 = wT34;
	}

	public String getWT35() {
		return WT35;
	}

	public void setWT35(String wT35) {
		WT35 = wT35;
	}

	public String getWT36() {
		return WT36;
	}

	public void setWT36(String wT36) {
		WT36 = wT36;
	}

	public String getWT37() {
		return WT37;
	}

	public void setWT37(String wT37) {
		WT37 = wT37;
	}

	public String getWT38() {
		return WT38;
	}

	public void setWT38(String wT38) {
		WT38 = wT38;
	}

	public String getWT39() {
		return WT39;
	}

	public void setWT39(String wT39) {
		WT39 = wT39;
	}

	public String getWT40() {
		return WT40;
	}

	public void setWT40(String wT40) {
		WT40 = wT40;
	}

	public String getWT41() {
		return WT41;
	}

	public void setWT41(String wT41) {
		WT41 = wT41;
	}

	public String getWT42() {
		return WT42;
	}

	public void setWT42(String wT42) {
		WT42 = wT42;
	}

	public String getWT43() {
		return WT43;
	}

	public void setWT43(String wT43) {
		WT43 = wT43;
	}

	public String getWT44() {
		return WT44;
	}

	public void setWT44(String wT44) {
		WT44 = wT44;
	}

	public String getWT45() {
		return WT45;
	}

	public void setWT45(String wT45) {
		WT45 = wT45;
	}

	public String getWT46() {
		return WT46;
	}

	public void setWT46(String wT46) {
		WT46 = wT46;
	}

	public String getWT47() {
		return WT47;
	}

	public void setWT47(String wT47) {
		WT47 = wT47;
	}

	public String getWT48() {
		return WT48;
	}

	public void setWT48(String wT48) {
		WT48 = wT48;
	}

	public String getWT49() {
		return WT49;
	}

	public void setWT49(String wT49) {
		WT49 = wT49;
	}

	public String getWT50() {
		return WT50;
	}

	public void setWT50(String wT50) {
		WT50 = wT50;
	}

	public String getAS1() {
		return AS1;
	}

	public void setAS1(String aS1) {
		AS1 = aS1;
	}

	public String getAS2() {
		return AS2;
	}

	public void setAS2(String aS2) {
		AS2 = aS2;
	}

	public String getAS3() {
		return AS3;
	}

	public void setAS3(String aS3) {
		AS3 = aS3;
	}

	public String getAS4() {
		return AS4;
	}

	public void setAS4(String aS4) {
		AS4 = aS4;
	}

	public String getAS5() {
		return AS5;
	}

	public void setAS5(String aS5) {
		AS5 = aS5;
	}

	public String getAS6() {
		return AS6;
	}

	public void setAS6(String aS6) {
		AS6 = aS6;
	}

	public String getAS7() {
		return AS7;
	}

	public void setAS7(String aS7) {
		AS7 = aS7;
	}

	public String getAS8() {
		return AS8;
	}

	public void setAS8(String aS8) {
		AS8 = aS8;
	}

	public String getAS9() {
		return AS9;
	}

	public void setAS9(String aS9) {
		AS9 = aS9;
	}

	public String getAS10() {
		return AS10;
	}

	public void setAS10(String aS10) {
		AS10 = aS10;
	}

	public String getAS11() {
		return AS11;
	}

	public void setAS11(String aS11) {
		AS11 = aS11;
	}

	public String getAS12() {
		return AS12;
	}

	public void setAS12(String aS12) {
		AS12 = aS12;
	}

	public String getAS13() {
		return AS13;
	}

	public void setAS13(String aS13) {
		AS13 = aS13;
	}

	public String getAS14() {
		return AS14;
	}

	public void setAS14(String aS14) {
		AS14 = aS14;
	}

	public String getAS15() {
		return AS15;
	}

	public void setAS15(String aS15) {
		AS15 = aS15;
	}

	public String getAS16() {
		return AS16;
	}

	public void setAS16(String aS16) {
		AS16 = aS16;
	}

	public String getAS17() {
		return AS17;
	}

	public void setAS17(String aS17) {
		AS17 = aS17;
	}

	public String getAS18() {
		return AS18;
	}

	public void setAS18(String aS18) {
		AS18 = aS18;
	}

	public String getAS19() {
		return AS19;
	}

	public void setAS19(String aS19) {
		AS19 = aS19;
	}

	public String getAS20() {
		return AS20;
	}

	public void setAS20(String aS20) {
		AS20 = aS20;
	}

	public String getAS21() {
		return AS21;
	}

	public void setAS21(String aS21) {
		AS21 = aS21;
	}

	public String getAS22() {
		return AS22;
	}

	public void setAS22(String aS22) {
		AS22 = aS22;
	}

	public String getAS23() {
		return AS23;
	}

	public void setAS23(String aS23) {
		AS23 = aS23;
	}

	public String getAS24() {
		return AS24;
	}

	public void setAS24(String aS24) {
		AS24 = aS24;
	}

	public String getAS25() {
		return AS25;
	}

	public void setAS25(String aS25) {
		AS25 = aS25;
	}

	public String getAS26() {
		return AS26;
	}

	public void setAS26(String aS26) {
		AS26 = aS26;
	}

	public String getAS27() {
		return AS27;
	}

	public void setAS27(String aS27) {
		AS27 = aS27;
	}

	public String getAS28() {
		return AS28;
	}

	public void setAS28(String aS28) {
		AS28 = aS28;
	}

	public String getAS29() {
		return AS29;
	}

	public void setAS29(String aS29) {
		AS29 = aS29;
	}

	public String getAS30() {
		return AS30;
	}

	public void setAS30(String aS30) {
		AS30 = aS30;
	}

	public String getAS31() {
		return AS31;
	}

	public void setAS31(String aS31) {
		AS31 = aS31;
	}

	public String getAS32() {
		return AS32;
	}

	public void setAS32(String aS32) {
		AS32 = aS32;
	}

	public String getAS33() {
		return AS33;
	}

	public void setAS33(String aS33) {
		AS33 = aS33;
	}

	public String getAS34() {
		return AS34;
	}

	public void setAS34(String aS34) {
		AS34 = aS34;
	}

	public String getAS35() {
		return AS35;
	}

	public void setAS35(String aS35) {
		AS35 = aS35;
	}

	public String getAS36() {
		return AS36;
	}

	public void setAS36(String aS36) {
		AS36 = aS36;
	}

	public String getAS37() {
		return AS37;
	}

	public void setAS37(String aS37) {
		AS37 = aS37;
	}

	public String getAS38() {
		return AS38;
	}

	public void setAS38(String aS38) {
		AS38 = aS38;
	}

	public String getAS39() {
		return AS39;
	}

	public void setAS39(String aS39) {
		AS39 = aS39;
	}

	public String getAS40() {
		return AS40;
	}

	public void setAS40(String aS40) {
		AS40 = aS40;
	}

	public String getAS41() {
		return AS41;
	}

	public void setAS41(String aS41) {
		AS41 = aS41;
	}

	public String getAS42() {
		return AS42;
	}

	public void setAS42(String aS42) {
		AS42 = aS42;
	}

	public String getAS43() {
		return AS43;
	}

	public void setAS43(String aS43) {
		AS43 = aS43;
	}

	public String getAS44() {
		return AS44;
	}

	public void setAS44(String aS44) {
		AS44 = aS44;
	}

	public String getAS45() {
		return AS45;
	}

	public void setAS45(String aS45) {
		AS45 = aS45;
	}

	public String getAS46() {
		return AS46;
	}

	public void setAS46(String aS46) {
		AS46 = aS46;
	}

	public String getAS47() {
		return AS47;
	}

	public void setAS47(String aS47) {
		AS47 = aS47;
	}

	public String getAS48() {
		return AS48;
	}

	public void setAS48(String aS48) {
		AS48 = aS48;
	}

	public String getAS49() {
		return AS49;
	}

	public void setAS49(String aS49) {
		AS49 = aS49;
	}

	public String getAS50() {
		return AS50;
	}

	public void setAS50(String aS50) {
		AS50 = aS50;
	}

	public String getBZ1() {
		return BZ1;
	}

	public void setBZ1(String bZ1) {
		BZ1 = bZ1;
	}

	public String getBZ2() {
		return BZ2;
	}

	public void setBZ2(String bZ2) {
		BZ2 = bZ2;
	}

	public String getBZ3() {
		return BZ3;
	}

	public void setBZ3(String bZ3) {
		BZ3 = bZ3;
	}

	public String getBZ4() {
		return BZ4;
	}

	public void setBZ4(String bZ4) {
		BZ4 = bZ4;
	}

	public String getBZ5() {
		return BZ5;
	}

	public void setBZ5(String bZ5) {
		BZ5 = bZ5;
	}

	public String getBZ6() {
		return BZ6;
	}

	public void setBZ6(String bZ6) {
		BZ6 = bZ6;
	}

	public String getBZ7() {
		return BZ7;
	}

	public void setBZ7(String bZ7) {
		BZ7 = bZ7;
	}

	public String getBZ8() {
		return BZ8;
	}

	public void setBZ8(String bZ8) {
		BZ8 = bZ8;
	}

	public String getBZ9() {
		return BZ9;
	}

	public void setBZ9(String bZ9) {
		BZ9 = bZ9;
	}

	public String getBZ10() {
		return BZ10;
	}

	public void setBZ10(String bZ10) {
		BZ10 = bZ10;
	}

	public String getBZ11() {
		return BZ11;
	}

	public void setBZ11(String bZ11) {
		BZ11 = bZ11;
	}

	public String getBZ12() {
		return BZ12;
	}

	public void setBZ12(String bZ12) {
		BZ12 = bZ12;
	}

	public String getBZ13() {
		return BZ13;
	}

	public void setBZ13(String bZ13) {
		BZ13 = bZ13;
	}

	public String getBZ14() {
		return BZ14;
	}

	public void setBZ14(String bZ14) {
		BZ14 = bZ14;
	}

	public String getBZ15() {
		return BZ15;
	}

	public void setBZ15(String bZ15) {
		BZ15 = bZ15;
	}

	public String getBZ16() {
		return BZ16;
	}

	public void setBZ16(String bZ16) {
		BZ16 = bZ16;
	}

	public String getBZ17() {
		return BZ17;
	}

	public void setBZ17(String bZ17) {
		BZ17 = bZ17;
	}

	public String getBZ18() {
		return BZ18;
	}

	public void setBZ18(String bZ18) {
		BZ18 = bZ18;
	}

	public String getBZ19() {
		return BZ19;
	}

	public void setBZ19(String bZ19) {
		BZ19 = bZ19;
	}

	public String getBZ20() {
		return BZ20;
	}

	public void setBZ20(String bZ20) {
		BZ20 = bZ20;
	}

	public String getBZ21() {
		return BZ21;
	}

	public void setBZ21(String bZ21) {
		BZ21 = bZ21;
	}

	public String getBZ22() {
		return BZ22;
	}

	public void setBZ22(String bZ22) {
		BZ22 = bZ22;
	}

	public String getBZ23() {
		return BZ23;
	}

	public void setBZ23(String bZ23) {
		BZ23 = bZ23;
	}

	public String getBZ24() {
		return BZ24;
	}

	public void setBZ24(String bZ24) {
		BZ24 = bZ24;
	}

	public String getBZ25() {
		return BZ25;
	}

	public void setBZ25(String bZ25) {
		BZ25 = bZ25;
	}

	public String getBZ26() {
		return BZ26;
	}

	public void setBZ26(String bZ26) {
		BZ26 = bZ26;
	}

	public String getBZ27() {
		return BZ27;
	}

	public void setBZ27(String bZ27) {
		BZ27 = bZ27;
	}

	public String getBZ28() {
		return BZ28;
	}

	public void setBZ28(String bZ28) {
		BZ28 = bZ28;
	}

	public String getBZ29() {
		return BZ29;
	}

	public void setBZ29(String bZ29) {
		BZ29 = bZ29;
	}

	public String getBZ30() {
		return BZ30;
	}

	public void setBZ30(String bZ30) {
		BZ30 = bZ30;
	}

	public String getBZ31() {
		return BZ31;
	}

	public void setBZ31(String bZ31) {
		BZ31 = bZ31;
	}

	public String getBZ32() {
		return BZ32;
	}

	public void setBZ32(String bZ32) {
		BZ32 = bZ32;
	}

	public String getBZ33() {
		return BZ33;
	}

	public void setBZ33(String bZ33) {
		BZ33 = bZ33;
	}

	public String getBZ34() {
		return BZ34;
	}

	public void setBZ34(String bZ34) {
		BZ34 = bZ34;
	}

	public String getBZ35() {
		return BZ35;
	}

	public void setBZ35(String bZ35) {
		BZ35 = bZ35;
	}

	public String getBZ36() {
		return BZ36;
	}

	public void setBZ36(String bZ36) {
		BZ36 = bZ36;
	}

	public String getBZ37() {
		return BZ37;
	}

	public void setBZ37(String bZ37) {
		BZ37 = bZ37;
	}

	public String getBZ38() {
		return BZ38;
	}

	public void setBZ38(String bZ38) {
		BZ38 = bZ38;
	}

	public String getBZ39() {
		return BZ39;
	}

	public void setBZ39(String bZ39) {
		BZ39 = bZ39;
	}

	public String getBZ40() {
		return BZ40;
	}

	public void setBZ40(String bZ40) {
		BZ40 = bZ40;
	}

	public String getBZ41() {
		return BZ41;
	}

	public void setBZ41(String bZ41) {
		BZ41 = bZ41;
	}

	public String getBZ42() {
		return BZ42;
	}

	public void setBZ42(String bZ42) {
		BZ42 = bZ42;
	}

	public String getBZ43() {
		return BZ43;
	}

	public void setBZ43(String bZ43) {
		BZ43 = bZ43;
	}

	public String getBZ44() {
		return BZ44;
	}

	public void setBZ44(String bZ44) {
		BZ44 = bZ44;
	}

	public String getBZ45() {
		return BZ45;
	}

	public void setBZ45(String bZ45) {
		BZ45 = bZ45;
	}

	public String getBZ46() {
		return BZ46;
	}

	public void setBZ46(String bZ46) {
		BZ46 = bZ46;
	}

	public String getBZ47() {
		return BZ47;
	}

	public void setBZ47(String bZ47) {
		BZ47 = bZ47;
	}

	public String getBZ48() {
		return BZ48;
	}

	public void setBZ48(String bZ48) {
		BZ48 = bZ48;
	}

	public String getBZ49() {
		return BZ49;
	}

	public void setBZ49(String bZ49) {
		BZ49 = bZ49;
	}

	public String getBZ50() {
		return BZ50;
	}

	public void setBZ50(String bZ50) {
		BZ50 = bZ50;
	}

	public String getPL1() {
		return PL1;
	}

	public void setPL1(String pL1) {
		PL1 = pL1;
	}

	public String getPL2() {
		return PL2;
	}

	public void setPL2(String pL2) {
		PL2 = pL2;
	}

	public String getPL3() {
		return PL3;
	}

	public void setPL3(String pL3) {
		PL3 = pL3;
	}

	public String getPL4() {
		return PL4;
	}

	public void setPL4(String pL4) {
		PL4 = pL4;
	}

	public String getPL5() {
		return PL5;
	}

	public void setPL5(String pL5) {
		PL5 = pL5;
	}

	public String getPL6() {
		return PL6;
	}

	public void setPL6(String pL6) {
		PL6 = pL6;
	}

	public String getPL7() {
		return PL7;
	}

	public void setPL7(String pL7) {
		PL7 = pL7;
	}

	public String getPL8() {
		return PL8;
	}

	public void setPL8(String pL8) {
		PL8 = pL8;
	}

	public String getPL9() {
		return PL9;
	}

	public void setPL9(String pL9) {
		PL9 = pL9;
	}

	public String getPL10() {
		return PL10;
	}

	public void setPL10(String pL10) {
		PL10 = pL10;
	}

	public String getPL11() {
		return PL11;
	}

	public void setPL11(String pL11) {
		PL11 = pL11;
	}

	public String getPL12() {
		return PL12;
	}

	public void setPL12(String pL12) {
		PL12 = pL12;
	}

	public String getPL13() {
		return PL13;
	}

	public void setPL13(String pL13) {
		PL13 = pL13;
	}

	public String getPL14() {
		return PL14;
	}

	public void setPL14(String pL14) {
		PL14 = pL14;
	}

	public String getPL15() {
		return PL15;
	}

	public void setPL15(String pL15) {
		PL15 = pL15;
	}

	public String getPL16() {
		return PL16;
	}

	public void setPL16(String pL16) {
		PL16 = pL16;
	}

	public String getPL17() {
		return PL17;
	}

	public void setPL17(String pL17) {
		PL17 = pL17;
	}

	public String getPL18() {
		return PL18;
	}

	public void setPL18(String pL18) {
		PL18 = pL18;
	}

	public String getPL19() {
		return PL19;
	}

	public void setPL19(String pL19) {
		PL19 = pL19;
	}

	public String getPL20() {
		return PL20;
	}

	public void setPL20(String pL20) {
		PL20 = pL20;
	}

	public String getPL21() {
		return PL21;
	}

	public void setPL21(String pL21) {
		PL21 = pL21;
	}

	public String getPL22() {
		return PL22;
	}

	public void setPL22(String pL22) {
		PL22 = pL22;
	}

	public String getPL23() {
		return PL23;
	}

	public void setPL23(String pL23) {
		PL23 = pL23;
	}

	public String getPL24() {
		return PL24;
	}

	public void setPL24(String pL24) {
		PL24 = pL24;
	}

	public String getPL25() {
		return PL25;
	}

	public void setPL25(String pL25) {
		PL25 = pL25;
	}

	public String getPL26() {
		return PL26;
	}

	public void setPL26(String pL26) {
		PL26 = pL26;
	}

	public String getPL27() {
		return PL27;
	}

	public void setPL27(String pL27) {
		PL27 = pL27;
	}

	public String getPL28() {
		return PL28;
	}

	public void setPL28(String pL28) {
		PL28 = pL28;
	}

	public String getPL29() {
		return PL29;
	}

	public void setPL29(String pL29) {
		PL29 = pL29;
	}

	public String getPL30() {
		return PL30;
	}

	public void setPL30(String pL30) {
		PL30 = pL30;
	}

	public String getPL31() {
		return PL31;
	}

	public void setPL31(String pL31) {
		PL31 = pL31;
	}

	public String getPL32() {
		return PL32;
	}

	public void setPL32(String pL32) {
		PL32 = pL32;
	}

	public String getPL33() {
		return PL33;
	}

	public void setPL33(String pL33) {
		PL33 = pL33;
	}

	public String getPL34() {
		return PL34;
	}

	public void setPL34(String pL34) {
		PL34 = pL34;
	}

	public String getPL35() {
		return PL35;
	}

	public void setPL35(String pL35) {
		PL35 = pL35;
	}

	public String getPL36() {
		return PL36;
	}

	public void setPL36(String pL36) {
		PL36 = pL36;
	}

	public String getPL37() {
		return PL37;
	}

	public void setPL37(String pL37) {
		PL37 = pL37;
	}

	public String getPL38() {
		return PL38;
	}

	public void setPL38(String pL38) {
		PL38 = pL38;
	}

	public String getPL39() {
		return PL39;
	}

	public void setPL39(String pL39) {
		PL39 = pL39;
	}

	public String getPL40() {
		return PL40;
	}

	public void setPL40(String pL40) {
		PL40 = pL40;
	}

	public String getPL41() {
		return PL41;
	}

	public void setPL41(String pL41) {
		PL41 = pL41;
	}

	public String getPL42() {
		return PL42;
	}

	public void setPL42(String pL42) {
		PL42 = pL42;
	}

	public String getPL43() {
		return PL43;
	}

	public void setPL43(String pL43) {
		PL43 = pL43;
	}

	public String getPL44() {
		return PL44;
	}

	public void setPL44(String pL44) {
		PL44 = pL44;
	}

	public String getPL45() {
		return PL45;
	}

	public void setPL45(String pL45) {
		PL45 = pL45;
	}

	public String getPL46() {
		return PL46;
	}

	public void setPL46(String pL46) {
		PL46 = pL46;
	}

	public String getPL47() {
		return PL47;
	}

	public void setPL47(String pL47) {
		PL47 = pL47;
	}

	public String getPL48() {
		return PL48;
	}

	public void setPL48(String pL48) {
		PL48 = pL48;
	}

	public String getPL49() {
		return PL49;
	}

	public void setPL49(String pL49) {
		PL49 = pL49;
	}

	public String getPL50() {
		return PL50;
	}

	public void setPL50(String pL50) {
		PL50 = pL50;
	}

	public String getPZ1() {
		return PZ1;
	}

	public void setPZ1(String pZ1) {
		PZ1 = pZ1;
	}

	public String getPZ2() {
		return PZ2;
	}

	public void setPZ2(String pZ2) {
		PZ2 = pZ2;
	}

	public String getPZ3() {
		return PZ3;
	}

	public void setPZ3(String pZ3) {
		PZ3 = pZ3;
	}

	public String getPZ4() {
		return PZ4;
	}

	public void setPZ4(String pZ4) {
		PZ4 = pZ4;
	}

	public String getPZ5() {
		return PZ5;
	}

	public void setPZ5(String pZ5) {
		PZ5 = pZ5;
	}

	public String getPZ6() {
		return PZ6;
	}

	public void setPZ6(String pZ6) {
		PZ6 = pZ6;
	}

	public String getPZ7() {
		return PZ7;
	}

	public void setPZ7(String pZ7) {
		PZ7 = pZ7;
	}

	public String getPZ8() {
		return PZ8;
	}

	public void setPZ8(String pZ8) {
		PZ8 = pZ8;
	}

	public String getPZ9() {
		return PZ9;
	}

	public void setPZ9(String pZ9) {
		PZ9 = pZ9;
	}

	public String getPZ10() {
		return PZ10;
	}

	public void setPZ10(String pZ10) {
		PZ10 = pZ10;
	}

	public String getPZ11() {
		return PZ11;
	}

	public void setPZ11(String pZ11) {
		PZ11 = pZ11;
	}

	public String getPZ12() {
		return PZ12;
	}

	public void setPZ12(String pZ12) {
		PZ12 = pZ12;
	}

	public String getPZ13() {
		return PZ13;
	}

	public void setPZ13(String pZ13) {
		PZ13 = pZ13;
	}

	public String getPZ14() {
		return PZ14;
	}

	public void setPZ14(String pZ14) {
		PZ14 = pZ14;
	}

	public String getPZ15() {
		return PZ15;
	}

	public void setPZ15(String pZ15) {
		PZ15 = pZ15;
	}

	public String getPZ16() {
		return PZ16;
	}

	public void setPZ16(String pZ16) {
		PZ16 = pZ16;
	}

	public String getPZ17() {
		return PZ17;
	}

	public void setPZ17(String pZ17) {
		PZ17 = pZ17;
	}

	public String getPZ18() {
		return PZ18;
	}

	public void setPZ18(String pZ18) {
		PZ18 = pZ18;
	}

	public String getPZ19() {
		return PZ19;
	}

	public void setPZ19(String pZ19) {
		PZ19 = pZ19;
	}

	public String getPZ20() {
		return PZ20;
	}

	public void setPZ20(String pZ20) {
		PZ20 = pZ20;
	}

	public String getPZ21() {
		return PZ21;
	}

	public void setPZ21(String pZ21) {
		PZ21 = pZ21;
	}

	public String getPZ22() {
		return PZ22;
	}

	public void setPZ22(String pZ22) {
		PZ22 = pZ22;
	}

	public String getPZ23() {
		return PZ23;
	}

	public void setPZ23(String pZ23) {
		PZ23 = pZ23;
	}

	public String getPZ24() {
		return PZ24;
	}

	public void setPZ24(String pZ24) {
		PZ24 = pZ24;
	}

	public String getPZ25() {
		return PZ25;
	}

	public void setPZ25(String pZ25) {
		PZ25 = pZ25;
	}

	public String getPZ26() {
		return PZ26;
	}

	public void setPZ26(String pZ26) {
		PZ26 = pZ26;
	}

	public String getPZ27() {
		return PZ27;
	}

	public void setPZ27(String pZ27) {
		PZ27 = pZ27;
	}

	public String getPZ28() {
		return PZ28;
	}

	public void setPZ28(String pZ28) {
		PZ28 = pZ28;
	}

	public String getPZ29() {
		return PZ29;
	}

	public void setPZ29(String pZ29) {
		PZ29 = pZ29;
	}

	public String getPZ30() {
		return PZ30;
	}

	public void setPZ30(String pZ30) {
		PZ30 = pZ30;
	}

	public String getPZ31() {
		return PZ31;
	}

	public void setPZ31(String pZ31) {
		PZ31 = pZ31;
	}

	public String getPZ32() {
		return PZ32;
	}

	public void setPZ32(String pZ32) {
		PZ32 = pZ32;
	}

	public String getPZ33() {
		return PZ33;
	}

	public void setPZ33(String pZ33) {
		PZ33 = pZ33;
	}

	public String getPZ34() {
		return PZ34;
	}

	public void setPZ34(String pZ34) {
		PZ34 = pZ34;
	}

	public String getPZ35() {
		return PZ35;
	}

	public void setPZ35(String pZ35) {
		PZ35 = pZ35;
	}

	public String getPZ36() {
		return PZ36;
	}

	public void setPZ36(String pZ36) {
		PZ36 = pZ36;
	}

	public String getPZ37() {
		return PZ37;
	}

	public void setPZ37(String pZ37) {
		PZ37 = pZ37;
	}

	public String getPZ38() {
		return PZ38;
	}

	public void setPZ38(String pZ38) {
		PZ38 = pZ38;
	}

	public String getPZ39() {
		return PZ39;
	}

	public void setPZ39(String pZ39) {
		PZ39 = pZ39;
	}

	public String getPZ40() {
		return PZ40;
	}

	public void setPZ40(String pZ40) {
		PZ40 = pZ40;
	}

	public String getPZ41() {
		return PZ41;
	}

	public void setPZ41(String pZ41) {
		PZ41 = pZ41;
	}

	public String getPZ42() {
		return PZ42;
	}

	public void setPZ42(String pZ42) {
		PZ42 = pZ42;
	}

	public String getPZ43() {
		return PZ43;
	}

	public void setPZ43(String pZ43) {
		PZ43 = pZ43;
	}

	public String getPZ44() {
		return PZ44;
	}

	public void setPZ44(String pZ44) {
		PZ44 = pZ44;
	}

	public String getPZ45() {
		return PZ45;
	}

	public void setPZ45(String pZ45) {
		PZ45 = pZ45;
	}

	public String getPZ46() {
		return PZ46;
	}

	public void setPZ46(String pZ46) {
		PZ46 = pZ46;
	}

	public String getPZ47() {
		return PZ47;
	}

	public void setPZ47(String pZ47) {
		PZ47 = pZ47;
	}

	public String getPZ48() {
		return PZ48;
	}

	public void setPZ48(String pZ48) {
		PZ48 = pZ48;
	}

	public String getPZ49() {
		return PZ49;
	}

	public void setPZ49(String pZ49) {
		PZ49 = pZ49;
	}

	public String getPZ50() {
		return PZ50;
	}

	public void setPZ50(String pZ50) {
		PZ50 = pZ50;
	}
	
	public void zqbVoteUpdateWjdc() {
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(ServletActionContext.HTTP_REQUEST);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		String username = uc._userModel.getUsername();
		Long orgroleid = uc.get_userModel().getOrgroleid();
		StringBuffer sql = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		List<String> str = new ArrayList<String>();
		if (uc != null && orgroleid == 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
		} else if (uc != null && orgroleid != 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
			str.add("PZ");
		}
		String[] string2 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
				"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
				"41", "42", "43", "44", "45", "46", "47", "48", "49", "50" };
		for (String s1 : str) {
			for (String s2 : string2) {
				if (Long.parseLong(s2) > Long.parseLong(forSize)) {
					break;
				}
				String value = request.getParameter(s1 + s2) == null ? ""
						: request.getParameter(s1 + s2).trim().toString();
				if (value != null && !value.equals("")) {
					list.add(s1 + s2);
					map.put(s1 + s2, request.getParameter(s1 + s2));
				} /*else {
					list.add(s1 + s2);
					map.put(s1 + s2, "");
				}*/
			}
		}
		for (String string : list) {
			sql.append("" + string + "=?,");
		}

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE BD_VOTE_CXDDRCGZFK SET ");
		sb.append(sql.toString().substring(0, sql.toString().length() - 1));
		if (uc != null && orgroleid == 3l) {
			sb.append(",USERNAME=?,USERID=?");
			list.add("USERNAME");
			list.add("USERID");
			map.put("USERNAME", username);
			map.put("USERID", userid);
		}
		sb.append(" WHERE USERID=? AND TZGGID=?");
		zqbAnnouncementService.zqbVoteUpdateXpzcfk(sb.toString(), userId,
				TZGGID, list, map);
	}

	public void zqbVoteUpdateXpzcfk() {
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(ServletActionContext.HTTP_REQUEST);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		String username = uc._userModel.getUsername();
		Long orgroleid = uc.get_userModel().getOrgroleid();
		StringBuffer sql = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		List<String> str = new ArrayList<String>();
		if (uc != null && orgroleid == 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
			str.add("YJXYSFFS");
		} else if (uc != null && orgroleid != 3l) {
			str.add("AS");
			str.add("PL");
			str.add("BZ");
			str.add("PZ");
			str.add("YJXYSFFS");
		}
		String[] string2 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
				"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
				"41", "42", "43", "44", "45", "46", "47", "48", "49", "50" };
		for (String s1 : str) {
			for (String s2 : string2) {
				if (Long.parseLong(s2) > Long.parseLong(forSize)) {
					break;
				}
				String value = "";//request.getParameter(s1 + s2) == null ? "" : request.getParameter(s1 + s2).trim().toString();
				try {
					Object o = request.getParameter(s1 + s2)==null?"":request.getParameter(s1 + s2);
					value = new String(o.toString().getBytes("UTF-8"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.error(e,e);
				}
				/*try {
					value= java.net.URLDecoder.decode(value, "utf-8");
				} catch (Exception e) {
					logger.error(e,e);
				}*/
				if (value != null && !value.equals("")) {
					list.add(s1 + s2);
					map.put(s1 + s2, request.getParameter(s1 + s2));
				} else {
					list.add(s1 + s2);
					map.put(s1 + s2, "");
				}
			}
		}
		for (String string : list) {
			sql.append("" + string + "=?,");
		}

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE BD_VOTE_WJDC SET ");
		sb.append(sql.toString().substring(0, sql.toString().length() - 1));
		if (uc != null && orgroleid == 3l) {
			sb.append(",USERNAME=?,USERID=?");
			list.add("USERNAME");
			list.add("USERID");
			map.put("USERNAME", username);
			map.put("USERID", userid);
		}
		sb.append(" WHERE CUSTOMERNO=? AND TZGGID=?");
		zqbAnnouncementService.zqbVoteUpdateXpzcfk(sb.toString(), CUSTOMERNO,
				TZGGID, list, map);
	}

	public void zqbGlfCheckname() {
		boolean flag = instanceId == 0l;
		if (flag) {
			Map params = new HashMap();
			params.put(1,khbh);
			params.put(2,khmc);
			params.put(3,gsmc);
			String gsmcstr = DBUTilNew.getDataStr("GSMC","SELECT GSMC FROM BD_ZQB_GLF WHERE KHBH= ?  AND KHMC= ?  AND GSMC= ? ", params);
			if (gsmcstr != null && !gsmcstr.equals("")) {
				ResponseUtil.write("1");
			}
		} else {
			ResponseUtil.write("0");
		}
	}

	public void zqbAnnouncementCheckIsQYNBSHR() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String isNBSHR = "";
		String user = "";
		if (uc != null) {
			user = uc.get_userModel().getUserid() + "["
					+ uc.get_userModel().getUsername() + "]";
			isNBSHR = DBUtil.getString("SELECT DISTINCT QYNBRYSH FROM BD_MDM_KHQXGLB WHERE QYNBRYSH='"+ user + "'", "QYNBRYSH");
		}
		if (isNBSHR == null || isNBSHR.equals("")) {
			ResponseUtil.write("0");
		} else {
			ResponseUtil.write("1");
		}
	}

	public void zqbAnnouncementSetGGDF() {
		Map params = new HashMap();
		params.put(1, id);
		String oldggdf = DBUTilNew.getDataStr("GGDF","SELECT GGDF FROM BD_XP_QTGGZLLC WHERE ID= ? ",params);
		if (oldggdf != null && !oldggdf.equals("")) {
			if ((Double.parseDouble(oldggdf) - Double.parseDouble(ggdf)) > 0.0) {
				params = new HashMap();
				params.put(1, ggdf);
				params.put(2, id);
				DBUTilNew.update("UPDATE BD_XP_QTGGZLLC SET GGDF=GGDF-? WHERE ID=? ",params);
				params = new HashMap();
				params.put(1, id);
				params.put(2, id);
				params.put(3, id);
				DBUTilNew.update("UPDATE BD_MEET_QTGGZL SET GGDF=(SELECT GGDF FROM BD_XP_QTGGZLLC WHERE ID= ? ) WHERE NOTICENO=(SELECT NOTICENO FROM BD_XP_QTGGZLLC WHERE ID=? ) AND KHBH=(SELECT KHBH FROM BD_XP_QTGGZLLC WHERE ID=? )",params);
			} else {
				params = new HashMap();
				params.put(1, id);
				DBUTilNew.update("UPDATE BD_XP_QTGGZLLC SET GGDF=0 WHERE ID= ? ",params);
				params = new HashMap();
				params.put(1, id);
				params.put(2, id);
				DBUTilNew.update("UPDATE BD_MEET_QTGGZL SET GGDF=0 WHERE NOTICENO=(SELECT NOTICENO FROM BD_XP_QTGGZLLC WHERE ID=? ) AND KHBH=(SELECT KHBH FROM BD_XP_QTGGZLLC WHERE ID= ? )",params);
			}
		}
	}

	public void forwardMsgsend(){
		zqbAnnouncementService.forwardMsgsend(actdefid,userName,instanceid);
	}
	public String zqbGlfGgGlf(){
		glflist = zqbAnnouncementService.zqbGlfGgGlf(khbh,gsmc,gslx,zch,zcdz,pageNumber,pageSize);
		totalNum = glflist.size();
		return SUCCESS;
	}

	public void expWord() {
		HttpServletResponse response = ServletActionContext.getResponse();
		if (khbh == null || khbh.equals("") || khbh.equals("undefined")) {
			zqbAnnouncementService.expWordDefault(response,dqrq);
		} else {
			zqbAnnouncementService.expWord(response, customername, Long.parseLong(ggid), khbh,dqrq);
		}
	}
	
	public void expWjdc(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.expWjdc(response,ggid);
	}
	
	public void senSMSToNotReply(){
		zqbAnnouncementService.senSMSToNotReply(ggid);
	}
	/**
	 * 选择项目经办人
	 * @return
	 */
	public String xmjbrSet(){
		list=zqbAnnouncementService.getZqbTzggProjectCostormerSet(userId,username,roleid,gsmc);
		return SUCCESS;
	}

	// -------------信批自查问题开始----------
	public String sysdemMdmXpzcwt() {
		xpzcwtformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"FORMID");
		xpzcwtdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"ID");
		sysdemMdmXpzcwtList = zqbAnnouncementService.sysdemMdmXpzcwt();
		return SUCCESS;
	}

	public void DeleteXpzcwt() {
		String[] instanceid = instanceids.split(",");
		if(instanceid[0].equals("on"))
			instanceid[0]="";
		List<Boolean> bool = new ArrayList<Boolean>();
		boolean flag;
		for (String instcdid : instanceid) {
			if (!instcdid.equals("")) {
				flag = DemAPI.getInstance().removeFormData(
						Long.parseLong(instcdid));
				if (flag == true) {
					bool.add(flag);
				}
			}
		}
		
		ResponseUtil.write("success");
	}

	public String DownXpzcwt() {
		String[] ids = downids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String downid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params);
			String maxsortid = DBUtil.getString("SELECT MAX(SORTID) SORTID FROM BD_VOTE_WJDCWTB", "SORTID");
			if (Integer.parseInt(maxsortid) > Integer.parseInt(checksortid)) {
				Map params1 = new HashMap();
				params1.put(1,downid);
				String downsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params1);
				params1 = new HashMap();
				params1.put(1,downsortid);
				params1.put(2,checkid);
				int numa = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params1);
				if (numa == 1) {
					params1 = new HashMap();
					params1.put(1,checksortid);
					params1.put(2,downid);
					int numb = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params1);
					if (numb != 1) {
						params1 = new HashMap();
						params1.put(1,checksortid);
						params1.put(2,checkid);
						while (true) {
							int numc = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params1);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		xpzcwtformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'","FORMID");
		xpzcwtdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'","ID");
		sysdemMdmXpzcwtList = zqbAnnouncementService.sysdemMdmXpzcwt();
		return SUCCESS;
	}

	public String UpXpzcwt() {
		String[] ids = upids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String upid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params);
			String minsortid = DBUtil.getString("SELECT MIN(SORTID) SORTID FROM BD_VOTE_WJDCWTB", "SORTID");
			if (Integer.parseInt(minsortid) < Integer.parseInt(checksortid)) {
				params = new HashMap();
				params.put(1,upid);
				String upsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params);
				int numa = DBUtil
						.executeUpdate("UPDATE BD_VOTE_WJDCWTB SET SORTID="
								+ upsortid + " WHERE ID=" + checkid);
				if (numa == 1) {
					int numb = DBUtil
							.executeUpdate("UPDATE BD_VOTE_WJDCWTB SET SORTID="
									+ checksortid + " WHERE ID=" + upid);
					if (numb != 1) {
						while (true) {
							int numc = DBUtil
									.executeUpdate("UPDATE BD_VOTE_WJDCWTB SET SORTID="
											+ checksortid
											+ " WHERE ID="
											+ checkid);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		xpzcwtformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"FORMID");
		xpzcwtdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"ID");
		sysdemMdmXpzcwtList = zqbAnnouncementService.sysdemMdmXpzcwt();
		return SUCCESS;
	}

	public String UpTopXpzcwt() {
		String ids = uptopid.replaceAll(",", "");
		checkid = ids;
		String minsortid_id = DBUtil.getString("SELECT ID FROM BD_VOTE_WJDCWTB WHERE SORTID=(SELECT MIN(SORTID) SORTID FROM BD_VOTE_WJDCWTB)","ID");
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params);
		String minsortid = DBUtil.getString("SELECT MIN(SORTID) SORTID FROM BD_VOTE_WJDCWTB", "SORTID");
		params = new HashMap();
		params.put(1,minsortid);
		params.put(2,checkid);
		int numa = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params);
		if (numa == 1) {
			params = new HashMap();
			params.put(1,checksortid);
			params.put(2,minsortid_id);
			int numb = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params);
			if (numb != 1) {
				params = new HashMap();
				params.put(1,checksortid);
				params.put(2,checkid);
				while (true) {
					int numc = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",params);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		xpzcwtformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"FORMID");
		xpzcwtdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"ID");
		sysdemMdmXpzcwtList = zqbAnnouncementService.sysdemMdmXpzcwt();
		return SUCCESS;
	}

	public String DownBottomXpzcwt() {
		String ids = downbottomid.replaceAll(",", "");
		checkid = ids;
		String maxsortid_id = DBUtil.getString("SELECT ID FROM BD_VOTE_WJDCWTB WHERE SORTID=(SELECT MAX(SORTID) SORTID FROM BD_VOTE_WJDCWTB)","ID");
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_VOTE_WJDCWTB WHERE ID= ? ",params);

		String maxsortid = DBUtil.getString("SELECT MAX(SORTID) SORTID FROM BD_VOTE_WJDCWTB", "SORTID");

		Map param = new HashMap();
		param.put(1, maxsortid);
		param.put(2, checkid);
		int numa = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",param);
		if (numa == 1) {
			 param = new HashMap();
				param.put(1, checksortid);
				param.put(2, maxsortid_id);
			int numb =DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",param);
			if (numb != 1) {
				 param = new HashMap();
					param.put(1, checksortid);
					param.put(2, checkid);
				while (true) {
					int numc = DBUTilNew.update("UPDATE BD_VOTE_WJDCWTB SET SORTID= ?  WHERE ID= ? ",param);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		xpzcwtformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"FORMID");
		xpzcwtdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单'",
						"ID");
		sysdemMdmXpzcwtList = zqbAnnouncementService.sysdemMdmXpzcwt();
		return SUCCESS;
	}

	// -------------信批自查问题结束----------

	// -------------项目阶段提交资料开始------
	public String sysdemMdmXmjdzl() {
		xmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"FORMID");
		xmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"ID");
		sysdemMdmXmjdzlList = zqbAnnouncementService.sysdemMdmXmjdzl();
		return SUCCESS;
	}

	public void DeleteXmjdzl() {
		String[] instanceid = instanceids.split(",");
		String ids = instanceids.substring(0, instanceids.length() - 1);
		Map params = new HashMap();
		params.put(1, ids);
		int num = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET STATE=0 WHERE ID IN( ? )",params);
		if ((instanceid.length - num) == 0) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write((instanceid.length - num) + "");
		}
	}

	public String DownXmjdzl() {
		String[] ids = downids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String downid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
			String maxsortid = DBUtil.getString(
					"SELECT MAX(SORTID) SORTID FROM BD_ZQB_KM_INFO", "SORTID");
			if (Integer.parseInt(maxsortid) > Integer.parseInt(checksortid)) {
				params = new HashMap();
				params.put(1,downid);
				String downsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
				int numa = DBUtil
						.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
								+ downsortid + " WHERE ID=" + checkid);
				if (numa == 1) {
					int numb = DBUtil
							.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
									+ checksortid + " WHERE ID=" + downid);
					if (numb != 1) {
						while (true) {
							int numc = DBUtil
									.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
											+ checksortid
											+ " WHERE ID="
											+ checkid);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		xmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"FORMID");
		xmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"ID");
		sysdemMdmXmjdzlList = zqbAnnouncementService.sysdemMdmXmjdzl();
		return SUCCESS;
	}

	public String UpXmjdzl() {
		String[] ids = upids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String upid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
			String minsortid = DBUtil.getString(
					"SELECT MIN(SORTID) SORTID FROM BD_ZQB_KM_INFO", "SORTID");
			if (Integer.parseInt(minsortid) < Integer.parseInt(checksortid)) {
			    params = new HashMap();
				params.put(1,upid);
				String upsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
				int numa = DBUtil
						.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
								+ upsortid + " WHERE ID=" + checkid);
				if (numa == 1) {
					int numb = DBUtil
							.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
									+ checksortid + " WHERE ID=" + upid);
					if (numb != 1) {
						while (true) {
							int numc = DBUtil
									.executeUpdate("UPDATE BD_ZQB_KM_INFO SET SORTID="
											+ checksortid
											+ " WHERE ID="
											+ checkid);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		xmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"FORMID");
		xmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"ID");
		sysdemMdmXmjdzlList = zqbAnnouncementService.sysdemMdmXmjdzl();
		return SUCCESS;
	}

	public String UpTopXmjdzl() {
		String ids = uptopid.replaceAll(",", "");
		checkid = ids;
		String minsortid_id = DBUtil.getString("SELECT ID FROM BD_ZQB_KM_INFO WHERE SORTID=(SELECT MIN(SORTID) SORTID FROM BD_ZQB_KM_INFO)","ID");
		
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
		String minsortid = DBUtil.getString("SELECT MIN(SORTID) SORTID FROM BD_ZQB_KM_INFO", "SORTID");
		params = new HashMap();
		params.put(1,minsortid);
		params.put(2,checkid);
		int numa = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
		if (numa == 1) {
			params = new HashMap();
			params.put(1,checksortid);
			params.put(2,minsortid_id);
			int numb = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
			if (numb != 1) {
				while (true) {
					int numc = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		xmjdzlformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'","FORMID");
		xmjdzldemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'","ID");
		sysdemMdmXmjdzlList = zqbAnnouncementService.sysdemMdmXmjdzl();
		return SUCCESS;
	}

	public String DownBottomPXmjdzl() {
		String ids = downbottomid.replaceAll(",", "");
		checkid = ids;
		String maxsortid_id = DBUtil
				.getString(
						"SELECT ID FROM BD_ZQB_KM_INFO WHERE SORTID=(SELECT MAX(SORTID) SORTID FROM BD_ZQB_KM_INFO)",
						"ID");
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_KM_INFO WHERE ID= ? ",params);
		
		String maxsortid = DBUtil.getString("SELECT MAX(SORTID) SORTID FROM BD_ZQB_KM_INFO", "SORTID");
		params = new HashMap();
		params.put(1,maxsortid);
		params.put(2,checkid);
		int numa = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
		if (numa == 1) {
			params = new HashMap();
			params.put(1,checksortid);
			params.put(2,maxsortid_id);
			int numb = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
			if (numb != 1) {
				params = new HashMap();
				params.put(1,checksortid);
				params.put(2,checkid);
				while (true) {
					int numc = DBUTilNew.update("UPDATE BD_ZQB_KM_INFO SET SORTID= ?  WHERE ID= ? ",params);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		xmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"FORMID");
		xmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单'",
						"ID");
		sysdemMdmXmjdzlList = zqbAnnouncementService.sysdemMdmXmjdzl();
		return SUCCESS;
	}

	// -------------项目阶段提交资料结束------
	public String sysdemMdmXmlx() {
		xmlxformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目类型'",
						"FORMID");
		xmlxdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目类型'",
						"ID");
		sysdemMdmXmlxList = zqbAnnouncementService.sysdemMdmXmlx();
		return SUCCESS;
	}

	public void DeleteXmlx() {
		String[] instanceid = instanceids.split("-");
		List<Boolean> bool = new ArrayList<Boolean>();
		boolean flag;
		for (String instcdid : instanceid) {
			if (!instcdid.equals("")) {
				flag = DemAPI.getInstance().removeFormData(
						Long.parseLong(instcdid));
				if (flag == true) {
					bool.add(flag);
				}
			}
		}
		if ((instanceid.length - bool.size()) == 0) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write((instanceid.length - bool.size()) + "");
		}
	}

	// -------------通用项目阶段资料开始------
	public String sysdemMdmTyxmjdzl() {
		Tyxmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"FORMID");
		Tyxmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"ID");
		sysdemMdmTyxmjdzlList = zqbAnnouncementService.sysdemMdmTyxmjdzl(xmlx);
		return SUCCESS;
	}

	public void DeleteTyxmjdzl() {
		String[] instanceid = instanceids.split(",");
		String ids = instanceids.substring(0, instanceids.length() - 1);
		int num = DBUtil
				.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET STATE=0 WHERE ID IN("
						+ ids + ")");
		/*
		 * List<Boolean> bool = new ArrayList<Boolean>(); boolean flag; for
		 * (String instcdid : instanceid) { if(!instcdid.equals("")){ flag =
		 * DemAPI.getInstance().removeFormData(Long.parseLong(instcdid));
		 * if(flag==true){ bool.add(flag); } } }
		 */
		if ((instanceid.length - num) == 0) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write((instanceid.length - num) + "");
		}
	}

	public String DownTyxmjdzl() {
		String[] ids = downids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String downid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
	
			String maxsortid = DBUtil
					.getString(
							"SELECT MAX(SORTID) SORTID FROM BD_ZQB_TYXM_INFO",
							"SORTID");
			if (Integer.parseInt(maxsortid) > Integer.parseInt(checksortid)) {
				params = new HashMap();
				params.put(1,downid);
				String downsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
				int numa = DBUtil
						.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
								+ downsortid + " WHERE ID=" + checkid);
				if (numa == 1) {
					int numb = DBUtil
							.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
									+ checksortid + " WHERE ID=" + downid);
					if (numb != 1) {
						while (true) {
							int numc = DBUtil
									.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
											+ checksortid
											+ " WHERE ID="
											+ checkid);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		Tyxmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"FORMID");
		Tyxmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"ID");
		sysdemMdmTyxmjdzlList = zqbAnnouncementService.sysdemMdmTyxmjdzl(xmlx);
		return SUCCESS;
	}

	public String UpTyxmjdzl() {
		String[] ids = upids.split(",");
		if (ids.length == 2) {
			checkid = ids[0];
			String upid = ids[1];
			Map params = new HashMap();
			params.put(1,checkid);
			String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
			String minsortid = DBUtil.getString("SELECT MIN(SORTID) SORTID FROM BD_ZQB_TYXM_INFO","SORTID");
			if (Integer.parseInt(minsortid) < Integer.parseInt(checksortid)) {
				params = new HashMap();
				params.put(1,upid);
				String upsortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
				int numa = DBUtil
						.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
								+ upsortid + " WHERE ID=" + checkid);
				if (numa == 1) {
					int numb = DBUtil
							.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
									+ checksortid + " WHERE ID=" + upid);
					if (numb != 1) {
						while (true) {
							int numc = DBUtil
									.executeUpdate("UPDATE BD_ZQB_TYXM_INFO SET SORTID="
											+ checksortid
											+ " WHERE ID="
											+ checkid);
							if (numc == 1) {
								break;
							}
						}
					}
				}
			}
		}
		Tyxmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"FORMID");
		Tyxmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"ID");
		sysdemMdmTyxmjdzlList = zqbAnnouncementService.sysdemMdmTyxmjdzl(xmlx);
		return SUCCESS;
	}

	public String UpTopTyxmjdzl() {
		String ids = uptopid.replaceAll(",", "");
		checkid = ids;
		String minsortid_id = DBUtil
				.getString(
						"SELECT ID FROM BD_ZQB_TYXM_INFO WHERE SORTID=(SELECT MIN(SORTID) SORTID FROM BD_ZQB_TYXM_INFO)",
						"ID");
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
		
		String minsortid = DBUtil.getString(
				"SELECT MIN(SORTID) SORTID FROM BD_ZQB_TYXM_INFO", "SORTID");
		params = new HashMap();
		params.put(1, minsortid);
		params.put(2,checkid);
		int numa = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ?  WHERE ID= ? ",params);
		if (numa == 1) {
			params = new HashMap();
			params.put(1, checksortid);
			params.put(2,minsortid_id);
			int numb = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ?  WHERE ID= ? ",params);
			if (numb != 1) {
				while (true) {
					params = new HashMap();
					params.put(1, checksortid);
					params.put(2,checkid);
					int numc = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ?  WHERE ID= ? ",params);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		Tyxmjdzlformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'","FORMID");
		Tyxmjdzldemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"ID");
		sysdemMdmTyxmjdzlList = zqbAnnouncementService.sysdemMdmTyxmjdzl(xmlx);
		return SUCCESS;
	}

	public String DownBottomPTyxmjdzl() {
		String ids = downbottomid.replaceAll(",", "");
		checkid = ids;
		String maxsortid_id = DBUtil
				.getString(
						"SELECT ID FROM BD_ZQB_TYXM_INFO WHERE SORTID=(SELECT MAX(SORTID) SORTID FROM BD_ZQB_TYXM_INFO)",
						"ID");
		Map params = new HashMap();
		params.put(1,checkid);
		String checksortid = DBUTilNew.getDataStr("SORTID","SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID= ? ",params);
	
		String maxsortid = DBUtil.getString(
				"SELECT MAX(SORTID) SORTID FROM BD_ZQB_TYXM_INFO", "SORTID");
		params =new HashMap();
		params.put(1, maxsortid);
		params.put(2, checkid);
		int numa = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ?  WHERE ID= ? ",params );
		if (numa == 1) {
			params =new HashMap();
			params.put(1, checksortid);
			params.put(2, maxsortid_id);
			int numb = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ? WHERE ID= ? ",params);
			if (numb != 1) {
				while (true) {
					params =new HashMap();
					params.put(1, checksortid);
					params.put(2, checkid);
					int numc = DBUTilNew.update("UPDATE BD_ZQB_TYXM_INFO SET SORTID= ?  WHERE ID= ? ",params);
					if (numc == 1) {
						break;
					}
				}
			}
		}

		Tyxmjdzlformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"FORMID");
		Tyxmjdzldemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单'",
						"ID");
		sysdemMdmTyxmjdzlList = zqbAnnouncementService.sysdemMdmTyxmjdzl(xmlx);
		return SUCCESS;
	}

	// -------------通用项目阶段资料结束------
	public String industrymsgManegement() {
		if(zhuhy!=null && !zhuhy.equals("") && zihy!=null && !zihy.equals("")){
			if(checkLoginInfo(zhuhy) || checkLoginInfo(zihy)){
	    		return ERROR;
	    	}
		}
		String demid$formid = DBUtil
				.getString(
						"SELECT ID||'@'||FORMID DEMID$FORMID FROM SYS_DEM_ENGINE WHERE TITLE='行业信息维护表单'",
						"DEMID$FORMID");
		demId = Long.parseLong(demid$formid.split("@")[0]);
		formid = Long.parseLong(demid$formid.split("@")[1]);
		list = zqbAnnouncementService.industrymsgManegement(pageSize,
				pageNumber, zhuhy, zihy);
		totalNum = zqbAnnouncementService
				.industrymsgManegementSize(zhuhy, zihy);
		return SUCCESS;
	}
	private static boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
	public String impIndustrymsgIndex() {
		return SUCCESS;
	}

	public void impIndustrymsg() throws Exception {
		zqbAnnouncementService.impIndustrymsg(filename);
	}

	public void industrymsgRemove() {
		boolean flag = DemAPI.getInstance().removeFormData(instanceid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void zqbXpCheckLSBB() {
		Map params = new HashMap();
		params.put(1,noticetext);
		params.put(1,descript);
		String dct = DBUTilNew.getDataStr("DESCRIPT","SELECT DESCRIPT FROM IWORK_WEBOFFCIE_VERSION WHERE RECORDID= ?  AND DESCRIPT= ? ",params);
		if (dct != null && !dct.equals("")) {
			ResponseUtil.write("1");
		}
	}

	public void nmsxNamecheck() {
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, nmsx);
		int num = DBUTilNew.getInt("NUM","SELECT COUNT(NMSX) NUM FROM BD_XP_NMXX WHERE KHBH= ? AND NMSX= ? ", params);
		if (num >= 1) {
			ResponseUtil.write("1");
		} else {
			ResponseUtil.write("0");
		}
	}

	public String itemIndex() {
		itemlist = zqbAnnouncementService.getItemList(sxlx);
		/* itemTotal=zqbAnnouncementService.getItemListSize(sxlx); */
		itemFormId = zqbAnnouncementService.getConfig("itemformid");
		itemDemId = zqbAnnouncementService.getConfig("itemdemid");
		itemTitleList = zqbAnnouncementService.getItemTitleList();
		return SUCCESS;
	}

	public String itemContentIndex() {
		itemContentList = zqbAnnouncementService.getItemContentList(sxlx);
		/* itemContentTotal=zqbAnnouncementService.getItemContentListSize(sxlx); */
		itemContentFormId = zqbAnnouncementService
				.getConfig("itemcontentformid");
		itemContentDemId = zqbAnnouncementService.getConfig("itemcontentdemid");
		itemTitleList = zqbAnnouncementService.getItemTitleList();
		return SUCCESS;
	}

	public String itemBcIndex() {
		itemBcList = zqbAnnouncementService.getItemBcList(sxlx);
		/* itemBcTotal=zqbAnnouncementService.getItemBcListSize(sxlx); */
		itemBcFormId = zqbAnnouncementService.getConfig("itembcformid");
		itemBcDemId = zqbAnnouncementService.getConfig("itembcdemid");
		itemTitleList = zqbAnnouncementService.getItemTitleList();
		return SUCCESS;
	}

	public String itemStepIndex() {
		itemSteplist = zqbAnnouncementService.getItemStepList(sxlx);
		/* itemStepTotal=zqbAnnouncementService.getItemStepListSize(sxlx); */
		itemStepFormId = zqbAnnouncementService.getConfig("itemstepformid");
		itemStepDemId = zqbAnnouncementService.getConfig("itemstepdemid");
		itemTitleList = zqbAnnouncementService.getItemTitleList();
		return SUCCESS;
	}

	public void itemRemove() {
		String info = zqbAnnouncementService.itemRemove(sxinstanceid);
		ResponseUtil.write(info);
	}

	public void itemContentRemove() {
		boolean flag = zqbAnnouncementService.itemContentRemove(cinstanceid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void itemBcRemove() {
		boolean flag = zqbAnnouncementService.itemBcRemove(bcinstanceid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void itemStepRemove() {
		boolean flag = zqbAnnouncementService.itemStepRemove(bzinstanceid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}

	public void itemMoveUp() {
		zqbAnnouncementService.itemMoveUp(itemDemId, itemFormId, sxinstanceid);
		ResponseUtil.write("ok");
	}

	public void itemMoveDown() {
		zqbAnnouncementService
				.itemMoveDown(itemDemId, itemFormId, sxinstanceid);
		ResponseUtil.write("ok");
	}

	public void itemMoveTop() {
		zqbAnnouncementService.itemMoveTop(itemDemId, itemFormId, sxinstanceid);
		ResponseUtil.write("ok");
	}

	public void itemMoveBottom() {
		zqbAnnouncementService.itemMoveBottom(itemDemId, itemFormId,
				sxinstanceid);
		ResponseUtil.write("ok");
	}

	public String itemImpIndex() {
		return SUCCESS;
	}

	public void itemImp() {
		zqbAnnouncementService.itemImp(fileUUID);
		ResponseUtil.write("ok");
	}
	public String reportIndex(){
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='督导信息报告'", "DFID");
		if(dfid_.contains("#")){
			demId = Long.parseLong(dfid_.split("#")[0]);
			formid = Long.parseLong(dfid_.split("#")[1]);
		}
		list = zqbAnnouncementService.getReportList(pageNumber,pageSize,departmentname,username);
		totalNum = zqbAnnouncementService.getReportListSize(departmentname,username);
		return SUCCESS;
	}
	
	public String reportOfOne(){
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='督导信息报告'", "DFID");
		if(dfid_.contains("#")){
			demId = Long.parseLong(dfid_.split("#")[0]);
			formid = Long.parseLong(dfid_.split("#")[1]);
		}
		list = zqbAnnouncementService.getReportList(pageNumber,pageSize,userId);
		totalNum = zqbAnnouncementService.getReportListSize(userId);
		return SUCCESS;
	}
	
	public void reportDel(){
		zqbAnnouncementService.delReport(instanceid);
	}

	// 信披事项开始
	public String Xpsxindex() {
		ggsplc = ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		dmggsplc = ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		roleid = uc.getOrgroleid().toString();
		if (roleid.equals("3")) {
			zqdm = DBUtil
					.getString(
							"SELECT A.ZQDM FROM BD_ZQB_KH_BASE A LEFT JOIN ORGUSER B ON A.CUSTOMERNO=B.EXTEND1 WHERE B.USERID='"
									+ uc.getUserid() + "'", "ZQDM");
			zqjc = DBUtil
					.getString(
							"SELECT A.ZQJC FROM BD_ZQB_KH_BASE A LEFT JOIN ORGUSER B ON A.CUSTOMERNO=B.EXTEND1 WHERE B.USERID='"
									+ uc.getUserid() + "'", "ZQJC");
		}
		gzxtyjhfformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披事项备查文件表'",
						"FORMID");
		gzxtyjhfdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披事项备查文件表'",
						"ID");
		xpsxlist = zqbAnnouncementService.getXpsxlist(roleid, zqjc, zqdm,
				xpsxname, noticename, noticetype, noticedatestart,
				noticedateend, pageNumber, pageSize);
		if (!roleid.equals("3")) {
			zqdmlist = zqbAnnouncementService.getZqdmList(roleid);
		}
		totalNum = zqbAnnouncementService.getXpsxlistSize(roleid, zqjc, zqdm,
				xpsxname, noticename, noticetype, noticedatestart,
				noticedateend).size();
		return SUCCESS;
	}

	public void Xpsxdeletefile() {
		Map params =new HashMap();
		params.put(1, uuid);
		DBUTilNew.update("DELETE FROM SYS_UPLOAD_FILE WHERE FILE_ID= ? ",params);
	}

	public void Xpsxdeletexpsx() {
		// 判断是否存在公告
		int num = 0;
		Map params =null;
		params= new HashMap();
		params.put(1, id);
		int ggnum = DBUTilNew.getInt( "NUM","SELECT COUNT(*) NUM FROM BD_MEET_QTGGZL WHERE BZLX= ? ",params);
		if (ggnum > 0) {
			num = num + 1;
		}
		// 判断是否存在信披事项文件
		String fileuuid = DBUTilNew.getDataStr("FILRUUID","SELECT TO_CHAR(WMSYS.WM_CONCAT(XPFILE)) FILRUUID FROM BD_XP_XPSXBCWJB WHERE XPSXQTID= ? ", params);
		String[] uuidarray = null;
		if (fileuuid != null) {
			uuidarray = fileuuid.split(",");
		}
		int filenum = 0;
		if (uuidarray != null && uuidarray.length > 0) {
			params= new HashMap();
			for (int i = 0; i < uuidarray.length; i++) {
				params.put(1, uuidarray[i]);
				filenum = DBUTilNew.getInt("NUM","SELECT COUNT(*) NUM FROM SYS_UPLOAD_FILE WHERE FILE_ID= ? ", params);
				if (filenum > 0) {
					break;
				}
			}
		}
		if (filenum > 0) {
			num = num + 2;
		}
		// 设置返回值
		if (num == 0) {
			params =new HashMap();
			params.put(1, id);
			DBUTilNew.update("DELETE FROM BD_XP_XPSXBCWJB WHERE XPSXQTID= ? ",params);
			DBUTilNew.update("DELETE FROM BD_XP_XPSXQTB WHERE ID= ? ",params);
			ResponseUtil.write("" + num);
		} else {
			ResponseUtil.write("" + num);
		}
	}

	public String XpsxList() {
		xpsxformid = DBUtil
				.getString(
						"SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披事项前台表'",
						"FORMID");
		xpsxdemid = DBUtil
				.getString(
						"SELECT ID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披事项前台表'",
						"ID");
		xpsxlist = zqbAnnouncementService.getXpsxList();
		return SUCCESS;
	}

	public void XpsxCompanyList() {
		String khbhzqjc = zqbAnnouncementService.getXpsxCompanyList();
		ResponseUtil.write(khbhzqjc);
	}

	public void XpsxCompany() {
		String khbhzqjc = DBUtil
				.getString(
						"SELECT ZQJC || '(' || ZQDM || ')' COMPANY FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"
								+ khbh + "'", "COMPANY");
		ResponseUtil.write(khbhzqjc);
	}

	public void XpsxSetCompany() {
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		if (uc.getOrgroleid() == 3l) {
			String khbh = uc.getExtend1();
			String khbhzqjc = DBUtil
					.getString(
							"SELECT ZQJC || '(' || ZQDM || ')' COMPANY FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"
									+ khbh + "'", "COMPANY");
			ResponseUtil.write(khbhzqjc + "$" + khbh);
		}/*
		 * else{ ResponseUtil.write(""); }
		 */
	}

	public String XpsxCheck() {
		xpsxlist = zqbAnnouncementService.getXpsxById(id);
		if (xpsxlist.size() > 0) {
			HashMap map = xpsxlist.get(0);
			nr = "步骤";
			bznr = zqbAnnouncementService.getTypeList(
					map.get("ID") == null ? "" : map.get("ID").toString(), nr);
			sxmc = map.get("SXMC") == null ? "" : map.get("SXMC").toString();
			xpsxname = map.get("XPSXNAME") == null ? "" : map.get("XPSXNAME")
					.toString();
			username = map.get("USERNAME") == null ? "" : map.get("USERNAME")
					.toString();
			txrq = map.get("TXRQ") == null ? "" : map.get("TXRQ").toString();
			sygz = map.get("SYGZ") == null ? "" : map.get("SYGZ").toString();
			plyq = map.get("PLYQ") == null ? "" : map.get("PLYQ").toString();
		}
		List lables = new ArrayList();
		lables.add("RN");
		lables.add("STR");
		bnr = zqbAnnouncementService.getStr(lables,"BD_XP_XPSXNR",id,"PLNR").toString();
		bbc = zqbAnnouncementService.getStr(lables,"BD_XP_XPSXBC",id,"PLBC").toString();
		return SUCCESS;
	}
	public String glhyCheck() {
		xpsxlist = zqbAnnouncementService.getGlhyById(id);
		if (xpsxlist.size() > 0) {
			HashMap map = xpsxlist.get(0);
			HYMC=map.get("HYMC") == null ? "" : map.get("HYMC").toString();
			HYLX=map.get("HYLX") == null ? "" : map.get("HYLX").toString();
			ZT=map.get("ZT") == null ? "" : map.get("ZT").toString();
			HYSJ=map.get("HYSJ") == null ? "" : map.get("HYSJ").toString();
			DRLX=map.get("DRLX") == null ? "" : map.get("DRLX").toString();
			YEAR=map.get("YEAR") == null ? "" : map.get("YEAR").toString();
			JC=map.get("JC") == null ? "" : map.get("JC").toString();
			HC=map.get("HC") == null ? "" : map.get("HC").toString();
			zywyh=map.get("ZYWYH") == null ? "" : map.get("ZYWYH").toString();
			html=zqbAnnouncementService.getQtFileList(id);
			selectHtml=html=zqbAnnouncementService.getFileList(id);
		}
		return SUCCESS;
	}
	public void XpsxtSave() {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		int num = 0;
		if (id != null & !id.equals("") && xpsxname != null
				& !xpsxname.equals("") && txrq != null & !txrq.equals("")) {
			Map params = new HashMap();
			params.put(1, xpsxname);
			params.put(2, txrq);
			params.put(3, user.getUsername());
			params.put(4, user.getUserid());
			params.put(5, id);
			num = DBUTilNew.update("UPDATE BD_XP_XPSXQTB SET XPSXNAME= ? ,TXRQ=TO_DATE( ? ,'yyyy-mm-dd hh24:mi:ss'),CREATEUSER= ? ,CREATEUSERID= ?  WHERE ID= ? ",params);
		}
		ResponseUtil.write((num + ""));
	}

	public String XpsxType() {
		nr = zqbAnnouncementService.getTypeList(id, nr);
		return SUCCESS;
	}

	public String XpsxTypeGg() {
		if (id != null && !id.equals("")) {
			// id=DBUtil.getString("SELECT XPSXID FROM BD_XP_XPSXQTB WHERE ID="+id,
			// "XPSXID");
			nr = zqbAnnouncementService.getTypeList(id, nr);
		}
		return SUCCESS;
	}

	public void XpsxGgGetbc() {
		if (id != null && !id.equals("")) {
			// id=DBUtil.getString("SELECT XPSXID FROM BD_XP_XPSXQTB WHERE ID="+id,
			// "XPSXID");
			ResponseUtil.write(zqbAnnouncementService.getTypeStr(id));
		}
	}

	public void XpsxGgGetbcfile() {
		ResponseUtil.write(zqbAnnouncementService.XpsxGgGetbcfile(id));
	}

	public void Xpsxtgetthisname() {
		String xpsxname;
		xpsxname = zqbAnnouncementService.Xpsxtgetthisname(id);
		// xpsxname =
		// DBUtil.getString("SELECT XPSXNAME FROM BD_XP_XPSXQTB WHERE ID="+id,
		// "XPSXNAME");
		ResponseUtil.write(xpsxname.toString());
	}
	public void glhytgetthisname() {
		String xpsxname;
		xpsxname = zqbAnnouncementService.Glhygetthisname(id);
		// xpsxname =
		// DBUtil.getString("SELECT XPSXNAME FROM BD_XP_XPSXQTB WHERE ID="+id,
		// "XPSXNAME");
		ResponseUtil.write(xpsxname.toString());
	}
	public void XpsxtNoticetypecheck() {
		Long orgroleid = UserContextUtil.getInstance().getUserContext(
				createuser)._userModel.getOrgroleid();
		if (orgroleid == 3l) {
			ResponseUtil.write("1");
		}
	}
	/**
	 * 信披质量评估导出
	 */
	public void xpqaExcel(){
		if (startdate == null)
			startdate = UtilDate.getMonthDate("yyyy-MM-dd", -6);
		if (enddate == null)
			enddate = UtilDate.getNowdate();
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.pxjlexcl(response,zqdm,zqjc,startdate,enddate);
	}
	// 信披事项结束

	public String xpqaList() {
		if (startdate == null)
			startdate = UtilDate.getMonthDate("yyyy-MM-dd", -6);
		if (enddate == null)
			enddate = UtilDate.getNowdate();
		qaList = zqbAnnouncementService.getXpqaList(zqjc, zqdm, startdate,
				enddate);
		return SUCCESS;
	}

	public String noticeDetails() {
		if (startdate == null)
			startdate = UtilDate.getMonthDate("yyyy-MM-dd", -6);
		if (enddate == null)
			enddate = UtilDate.getNowdate();
		detailList = zqbAnnouncementService.getNoticeDetails(customerno, zqjc,
				zqdm, noticename, noticetype, startdate, enddate);
		return SUCCESS;
	}

	public String opinionList() {
		opinionList = zqbAnnouncementService.getOpinionList(lcbs, lcbh, stepid);
		return SUCCESS;
	}

	public String opinionUserList() {
		if (startdate == null)
			startdate = UtilDate.getMonthDate("yyyy-MM-dd", -6);
		if (enddate == null)
			enddate = UtilDate.getNowdate();
		opinionUserList = zqbAnnouncementService.getOpinionUserList(startdate,
				enddate);
		return SUCCESS;
	}

	public String opinionUserContent() {
		if (startdate == null)
			startdate = UtilDate.getMonthDate("yyyy-MM-dd", -6);
		if (enddate == null)
			enddate = UtilDate.getNowdate();
		createusername = OrganizationAPI.getInstance()
				.getOrguserModel(createuser).getUsername();
		opinionUserContentList = zqbAnnouncementService
				.getopinionUserContentList(createuser, opinionContent,
						pageNumber, pageSize, startdate, enddate);
		totalNum = zqbAnnouncementService.getopinionUserContentListSize(
				createuser, opinionContent, startdate, enddate);
		return SUCCESS;
	}

	public String getDq() {
		return dq;
	}

	public void setDq(String dq) {
		this.dq = dq;
	}
    
	//查询培训通知公告instanceid
	public String openTZGG(){
		Map params = new HashMap();
		params.put(1, Long.parseLong(ggid));		
		instanceId = DBUTilNew.getLong("instanceid","select instanceid from bd_xp_hfxxb where ggid= ?  and hfqkid ="+hfqkid,params);
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String hfxxbformid = config.get("hfxxbformid");
		String hfxxbdemid = config.get("hfxxbdemid");
		demid = hfxxbdemid;
		formid = Long.parseLong(hfxxbformid);
		return SUCCESS;
	}
	//培训通知公告签到页面
	public String Signinup(){
		ydry= zqbAnnouncementService.getSigninup(username,szbm,startdate,enddate,sfqd,ggid);
		return SUCCESS;
		
	}
	public void updateSigninup(){
		boolean flag=false;
		if(cyrid!=null&&!cyrid.equals("")){
			flag = zqbAnnouncementService.updateSigninup(cyrid);
		}
		if(flag)
			ResponseUtil.write("success");
			else
			ResponseUtil.write("error");	
	}
	//参与培训人员信息导出
	public String SigninupExp() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbAnnouncementService.SigninupExcelExp(response,username,szbm,startdate,enddate,sfqd,ggid,tzbt);
		return null;
	}
	//通知公告回复附件数量
	public void tzggbatchsl(){
		int size = zqbAnnouncementService.gettzggbatchsl(ggid,roleid);
		if(size==0){
			ResponseUtil.write("0");	
		}
	}
	//下载通知公告回复附件
	public void tzggbatchdownload(){
		//获取当前公告回复表单数据
		list = zqbAnnouncementService.getgghfxxsj(ggid,roleid);
		String filenames="";
		try {
			filenames=zqbAnnouncementService.tzggbatchdownload(list);
		} catch (Exception e) {
			logger.error(e,e);
		}
		ResponseUtil.write(filenames);
	}
	public String zxksDr(){
		return "success";
	}
	/**
	 * 底稿归档
	 * @return
	 */
	public String dggdDr(){
		return "success";
	}
	/**
	 * 在线考试导入
	 * @throws Exception
	 */
	public void zxksDrupfile() throws  Exception{
		String tkstdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='题库试题内容维护表单'", "ID");
		String sjdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='试卷管理维护表单'", "ID");
		if (this.upFile != null) {
			zqbAnnouncementService.doExcelImp(upFile,Long.parseLong(tkstdemid),Long.parseLong(sjdemid));
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
			
	}
	/**
	 * 底稿归档导入
	 * @throws Exception
	 */
	public void dggdDrupfile() throws  Exception{
		if (this.upFile != null) {
			String flag=zqbAnnouncementService.doDggdImp(upFile);
			ResponseUtil.write(flag);
		} else {
			ResponseUtil.write("error");
		}
			
	}
	/**
	 * 在线考试模板下载
	 */
	public void zxksMbupload(){
		try {
			zqbAnnouncementService.zxksMbupload();
		} catch (Exception e) {
			
		}
	}
	/**
	 * 底稿归档
	 */
	public void dggdMbupload(){
		try {
			zqbAnnouncementService.dggdMbupload();
		} catch (Exception e) {
			
		}
	}
	/**
	 * 工作日志姓名选择器
	 * @return
	 */
	public void gzrzxzq(){
		list=zqbAnnouncementService.getGzrzxmxzq(username);
		JSONArray msg=new JSONArray();
		if(list!=null&&list.size()>0){
		msg = JSONArray.fromObject(list);
		ResponseUtil.writeTextUTF8(msg.toString());
		}else{
			ResponseUtil.writeTextUTF8("");
		}
		
	}
	/**
	 *  工作日志部门选择器
	 * @return
	 */
	public void gzrzxzqBM(){
		list=zqbAnnouncementService.getGzrzxmxzqBM(departmentname);
		JSONArray msg=new JSONArray();
		if(list!=null&&list.size()>0){
		msg = JSONArray.fromObject(list);
		ResponseUtil.writeTextUTF8(msg.toString());
		}else{
			ResponseUtil.writeTextUTF8("");
		}
	}
    /**
     *  工作备查手动查询
     * @return
     */
    public void showGzbcXx(){
        String json=zqbAnnouncementService.showGzbcXx(CID);
        JSONObject msg=JSONObject.fromObject(json);
        ResponseUtil.writeTextUTF8(msg.toString());

    }
}
