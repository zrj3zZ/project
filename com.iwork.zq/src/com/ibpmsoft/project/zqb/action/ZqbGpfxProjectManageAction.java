package com.ibpmsoft.project.zqb.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.common.ZQBConstants;
import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.ibpmsoft.project.zqb.service.ZqbGpfxProjectManageService;
import com.ibpmsoft.project.zqb.service.ZqbProjectManageService;
import com.iwork.app.conf.GpfxXmlcConf;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.conf.XmlcConf;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

public class ZqbGpfxProjectManageAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(ZqbGpfxProjectManageAction.class);
	private ZqbGpfxProjectManageService zqbGpfxProjectManageService;
	private List<HashMap> runList;
	private List<HashMap> finishList;
	private List<HashMap> closeList;
	private List<HashMap> taskList;
	private List<HashMap> xmcyggList;
	private HashMap hash;
	private List<HashMap> list;
	private String projectNo;
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
	private Long xmrbid;
	private Long demId;
	private String pjsm;
	private String projectno;
	private String pjr;
	private String attach;
	private String xmlcServer;
	private String xmjd;
	private String gxsj;
	private int totalNum; // 总页数
	private int finishNum; // 总页数
	private int closeNum; // 总页数
	private int pageSize = 10; // 每页条数
	private int pageSize1 = 10; // 每页条数
	private int pageSize2 = 10; // 每页条数
	private int pageSize3 = 10; // 每页条数
	private String czbm;
	private String cyrName;
	private boolean IsViewSearch;
	private String customerno;
	private String targetid;
	private String targetname;
	
	public String getTargetid() {
		return targetid;
	}

	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}

	public String getTargetname() {
		return targetname;
	}

	public void setTargetname(String targetname) {
		this.targetname = targetname;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	public boolean isIsViewSearch() {
		return IsViewSearch;
	}

	public void setIsViewSearch(boolean isViewSearch) {
		IsViewSearch = isViewSearch;
	}

	public String getCzbm() {
		return czbm;
	}

	public void setCzbm(String czbm) {
		this.czbm = czbm;
	}

	public String getCyrName() {
		return cyrName;
	}

	public void setCyrName(String cyrName) {
		this.cyrName = cyrName;
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
	
	public void setTypePieData(String typePieData) {
		this.typePieData = typePieData;
	}

	public String getPjsm() {
		return pjsm;
	}

	public void setPjsm(String pjsm) {
		this.pjsm = pjsm;
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
	
	public void setZqbGpfxProjectManageService(ZqbGpfxProjectManageService zqbGpfxProjectManageService) {
		this.zqbGpfxProjectManageService = zqbGpfxProjectManageService;
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

	public boolean isReadwrite() {
		return readwrite;
	}

	public void setReadwrite(boolean readwrite) {
		this.readwrite = readwrite;
	}

	public String index() {
		// 判断是否是超级用户
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumber == 0)
			pageNumber = 1;
		if (pageNumber1 == 0)
			pageNumber1 = 1;
		if (pageNumber2 == 0)
			pageNumber2 = 1;
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		HashMap<String,List<String>> parameterMap = zqbGpfxProjectManageService.getDepartmentUserList();
		runList = zqbGpfxProjectManageService.getRunProjectList1(superman,
				pageNumber, pageSize, projectName, xmjd, startDate,
				customername,dgzt,czbm,cyrName,parameterMap);
		totalNum = Integer.parseInt(zqbGpfxProjectManageService.getRunProjectListSize1(superman,
				projectName, xmjd, startDate, customername,dgzt,czbm,cyrName,parameterMap).get(0).toString());
		closeList = zqbGpfxProjectManageService.getCloseProjectList(superman,
				pageNumber2, pageSize2, projectName, xmjd, startDate,
				customername,dgzt,czbm,cyrName,parameterMap);
		closeNum = Integer.parseInt(zqbGpfxProjectManageService.getCloseProjectListSize1(superman,
				projectName, xmjd, startDate, customername,dgzt,czbm,cyrName,parameterMap).get(0).toString());
		formid=zqbGpfxProjectManageService.getFormid();
		demId=zqbGpfxProjectManageService.getDemId();
		IsViewSearch=zqbGpfxProjectManageService.getIsViewSearch();
		return SUCCESS;
	}
	
	public void gpfxproAssociate(){
		String jsonstr=zqbGpfxProjectManageService.gpfxproAssociate(customerno);
		ResponseUtil.write(jsonstr); 
	}
	
	public String indexNew() {
		// 判断是否是超级用户
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumber == 0)
			pageNumber = 1;
		if (pageNumber1 == 0)
			pageNumber1 = 1;
		if (pageNumber2 == 0)
			pageNumber2 = 1;
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		xmsplcServer = ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
		HashMap<String,List<String>> parameterMap = zqbGpfxProjectManageService.getDepartmentUserList();
		runList = zqbGpfxProjectManageService.getRunProjectList1(superman,
				pageNumber, pageSize, projectName, xmjd, startDate,
				customername,dgzt,czbm,cyrName,parameterMap);
		totalNum = Integer.parseInt(zqbGpfxProjectManageService.getRunProjectListSize1(superman,
				projectName, xmjd, startDate, customername,dgzt,czbm,cyrName,parameterMap).get(0).toString());
		closeList = zqbGpfxProjectManageService.getCloseProjectList(superman,
				pageNumber2, pageSize2, projectName, xmjd, startDate,
				customername,dgzt,czbm,cyrName,parameterMap);
		closeNum = Integer.parseInt(zqbGpfxProjectManageService.getCloseProjectListSize1(superman,
				projectName, xmjd, startDate, customername,dgzt,czbm,cyrName,parameterMap).get(0).toString());
		IsViewSearch=zqbGpfxProjectManageService.getIsViewSearch();
		return SUCCESS;
	}
	
	public String pjIndex() {
		// 判断是否是超级用户
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumber == 0)
			pageNumber = 1;
		if (pageNumber1 == 0)
			pageNumber1 = 1;
		if (pageNumber2 == 0)
			pageNumber2 = 1;
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		runList = zqbGpfxProjectManageService.getRunProjectList3(superman,
				pageNumber, pageSize, projectName, xmjd, startDate,
				customername,dgzt);
		totalNum = zqbGpfxProjectManageService.getRunProjectListSize3(superman,
				projectName, xmjd, startDate, customername,dgzt).size();
		finishList = zqbGpfxProjectManageService.getFinishProjectList1(superman,
				pageNumber1, pageSize1, projectName, xmjd, startDate,
				customername,dgzt);
		finishNum = zqbGpfxProjectManageService.getFinishProjectListSize2(superman,
				projectName, xmjd, startDate, customername,dgzt).size();
		closeList = zqbGpfxProjectManageService.getCloseProjectList(superman,
				pageNumber2, pageSize2, projectName, xmjd, startDate,
				customername,dgzt,null,null,null);
		closeNum = zqbGpfxProjectManageService.getCloseProjectListSize1(superman,
				projectName, xmjd, startDate, customername,dgzt,null,null,null).size();
		return SUCCESS;
	}
	
	public void getGpfxfpbmSpContent(){
		String jsonString=zqbGpfxProjectManageService.getGpfxfpbmSpContent(instanceid);
		ResponseUtil.write(jsonString);
	}

	public String getXM() {
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		typePieData = zqbGpfxProjectManageService.getYxmwjz(superman, pageNumberXM,
				pageSizeXM);
		xmNum = zqbGpfxProjectManageService.getXMListSize(superman).size();
		return SUCCESS;
	}

	public String getXMList() {
		xmcyggList = zqbGpfxProjectManageService.getXMCYList(userid);
		return SUCCESS;
	}

	/**
	 * 按项目参与人统计
	 * 
	 * @return
	 */
	public String getCYR() {
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		list = zqbGpfxProjectManageService.getYxmcyrz(superman, pageNumberXM,
				pageSizeXM);
		xmNum = zqbGpfxProjectManageService.getTotalCYNum(superman);
		return SUCCESS;
	}

	public String expproIndex(){
		return SUCCESS;
	}
	
	public void exppro(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.expProjectList(response,zcPro);
	}
	/**
	 * 文档加载
	 * 
	 * @return
	 */
	public String docLoad() {
		doclist = zqbGpfxProjectManageService.showProjectDocList(null);
		return SUCCESS;
	}

	public String doSearch() {
		doclist = zqbGpfxProjectManageService.showProjectDocList(searchkey);
		return SUCCESS;
	}

	public String search() {
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(ServletActionContext.HTTP_REQUEST);
		String strPtname = request.getParameter("searchkey");
		String value = strPtname;
		if (type != null && value != null) {
			superman = zqbGpfxProjectManageService.getIsSuperMan();
			runList = zqbGpfxProjectManageService.getSearchProjectList(superman,
					type, value);
		}
		demId = zqbGpfxProjectManageService.getTYDemId("股票发行项目");
		formid = zqbGpfxProjectManageService.getTYFormid("股票发行项目");
		return SUCCESS;
	}

	public String charIndex() {
		// 获取当前用户的角色
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		String[] data = zqbGpfxProjectManageService
				.getProjectTypeBarChart(superman);
		if (superman) {
			typePieData2 = zqbGpfxProjectManageService.getProjectTypePieChart2(
					superman, "chart");
		}
		typePieData4 = zqbGpfxProjectManageService.getProjectTypePieChart4(
				superman, "chart");
		typeBarLabel = data[0];
		typeBarData = data[1];
		return SUCCESS;
	}

	public String charUser() {
		typeBarData = zqbGpfxProjectManageService.getProjectUserTableList(superman);
		return SUCCESS;
	}

	public String gridIndex() {
		// 获取当前用户的角色
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		typePieData = zqbGpfxProjectManageService.getProjectTypePieChart4(superman,
				"grid");
		typeBarData = zqbGpfxProjectManageService.getProjectTypeBarGrid(superman);
		if (superman) {
			typePieData2 = zqbGpfxProjectManageService.getProjectTypePieChart2(
					superman, "grid");
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
		boolean flag = zqbGpfxProjectManageService.projectClose(projectNo,
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
			hash = zqbGpfxProjectManageService.getFinishProjectModel(projectNo);
			if (hash != null) {
				instanceid = Long.parseLong(hash.get("INSTANCEID").toString());
				String type = zqbGpfxProjectManageService.isPurview(hash,
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
			taskList = zqbGpfxProjectManageService.loadGanttJson1(projectNo);
		}
		return SUCCESS;
	}

	/**
	 * 设置项目转入持续督导阶段
	 */
	public void setProjectFinish() {
		boolean flag = zqbGpfxProjectManageService.projectFinish(projectNo,
				instanceid);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}

	}
	
	public void setProjectFinishDg() {
		boolean flag = zqbGpfxProjectManageService.setProjectFinishDg(temp);
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
			flag = zqbGpfxProjectManageService.drawUpdateItem(projectName,
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
			flag = zqbGpfxProjectManageService.removeItem(projectNo, taskid,
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
			html = zqbGpfxProjectManageService.getQuestionTalkList(projectNo,
					taskid, questionId);
			username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
					.getUsername();
			createdate = UtilDate.getNowdate();
		}
		return SUCCESS;
	}

	public String showQuestion() {
		if (taskid != null && projectNo != null) {
			html = zqbGpfxProjectManageService.showQuestion(projectNo, taskid,
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
			boolean flag = zqbGpfxProjectManageService.commitQuestion(projectNo,
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
			boolean flag = zqbGpfxProjectManageService.removeQuestion(type,
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
			boolean flag = zqbGpfxProjectManageService.getQuestionTalkAllList(
					projectNo, taskid, questionId, instanceId);
			if (flag) {
				ResponseUtil.write("success");
			} else {
				ResponseUtil.write("error");
			}
		}
	}

	public String showManagerBarChart() {
		String[] str = zqbGpfxProjectManageService.getManagerBarData();
		typeBarLabel = str[0];
		typeBarData = str[1];
		return SUCCESS;
	}

	public String showManagerBarChartInfo() {
		username=zqbGpfxProjectManageService.getChartProjectName(index);
		runList = zqbGpfxProjectManageService.getChartProjectList(index);
		return SUCCESS;
	}

	/**
	 * 实收账款统计图
	 * 
	 * @return
	 */
	public String yszkChart() {
		String[] str = zqbGpfxProjectManageService.getYSZKChartData();
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
		taskList = zqbGpfxProjectManageService.getJieDuanList(index, htje, wsje);
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
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		xmcyxxList = zqbGpfxProjectManageService.getXmcy(superman);
		return SUCCESS;
	}

	/**
	 * 导出项目及成员信息
	 * 
	 * @return
	 */
	public String doExp() {
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.doXmcyExp(response, superman);
		return SUCCESS;
	}
	
	public void saveRz(){
		boolean falg=zqbGpfxProjectManageService.saveRz(projectNo,groupid,rzje);
		if(falg){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public void editRz(){
		boolean falg=zqbGpfxProjectManageService.editRz(projectNo,groupid,rzje);
		if(falg){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public String rzList(){
		if(!add){
			rzje=zqbGpfxProjectManageService.getRzList(projectNo,groupid);
		}
		return SUCCESS;
	}
	
	public String showDaily(){
		if (pageNumber == 0)
			pageNumber = 1;
		formid=zqbGpfxProjectManageService.getConfig("xmrbformid");
		xmrbid=zqbGpfxProjectManageService.getConfig("xmrbid");
		list=zqbGpfxProjectManageService.showDailyList(projectNo,pageNumber,pageSize,startdate,enddate,formid);
		totalNum=zqbGpfxProjectManageService.showDailyListSize(projectNo,startdate,enddate);
		return SUCCESS;
	}
	
	public void removeDaily(){
		boolean flag = false;
		if(instanceid!=null){
			flag = zqbGpfxProjectManageService.removeDaily(instanceid);
		}
		if(flag){
			ResponseUtil.write("success"); 
		}else{
			ResponseUtil.write("error"); 
		}
		
	}
	
	public void expDaily(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.expDaily(response,projectNo);
	}
	
	public String loadGpfxProject() {
		GpfxXmlcConf conf = SystemConfig._gpfxXmlcConf;
		String config = conf.getConfig();
		// 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
		// 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
		String pro = config.equals("1") ? "GPFXXMRWLC" : "DGPFXXMRWLC";
		xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
		try {
			projectName=URLDecoder.decode(projectName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		content = zqbGpfxProjectManageService.loadGpfxProject(projectNo);
		formid = zqbGpfxProjectManageService.getTYFormid("股票发行项目任务");
		demId = zqbGpfxProjectManageService.getTYDemId("股票发行项目任务");
		return SUCCESS;
	}

	/**
	 * 项目参与人统计信息
	 * 
	 * @return
	 */
	public String showParticipantBarChart() {
		String[] str = zqbGpfxProjectManageService.getParticipantBarData();
		typeBarLabel = str[0];
		typeBarData = str[1];
		return SUCCESS;
	}

	public String showParticipantBarChartInfo() {
		runList = zqbGpfxProjectManageService.getParticipantChartProjectList(username);
		return SUCCESS;
	}

	public String showParticipantBarIndex() {
		typePieData = zqbGpfxProjectManageService.getParticipantGridBarData();
		return SUCCESS;
	}

	public String doExcelExp() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.doExcelExp(response);
		return null;
	}

	public String doExcelExp1() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.doExcelExp1(response);
		return null;
	}

	public String doExcelExp2() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.doExcelExp2(response);
		return null;
	}

	public String doExcelExp3() {
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectManageService.doExcelExp3(response);
		return null;
	}

	/**
	 * 以项目为基准统计导出
	 * 
	 * @return
	 */
	public String doExcelXMExp() {
		HttpServletResponse response = ServletActionContext.getResponse();
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		pageSizeXM = zqbGpfxProjectManageService.getTotalNum(superman);
		zqbGpfxProjectManageService.doExcelExpXM(response, superman, pageNumberXM,
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
		superman = zqbGpfxProjectManageService.getIsSuperMan();
		if (pageNumberXM == 0)
			pageNumberXM = 1;
		pageSizeXM = zqbGpfxProjectManageService.getTotalCYNum(superman);
		zqbGpfxProjectManageService.doExcelExpCY(response, superman, pageNumberXM,
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
			flag = zqbGpfxProjectManageService.removeProject(projectNo, instanceid);
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
			info = zqbGpfxProjectManageService.removeCxddProject(projectNo,
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
		try {
			projectName=URLDecoder.decode(projectName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		content = zqbGpfxProjectManageService.loadProject(projectNo);
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
		content = zqbGpfxProjectManageService.loadProjectNew(projectNo);
		return SUCCESS;
	}

	/**
	 * 项目评价信息
	 * 
	 * @return
	 */
	public String loadPj() {
		content = zqbGpfxProjectManageService.loadPj(projectNo);
		demId = zqbGpfxProjectManageService.getTYDemId("股票发行评价表单");
		formid = zqbGpfxProjectManageService.getTYFormid("股票发行评价表单");
		return SUCCESS;
	}

	/**
	 * 相关问题
	 * 
	 * @return
	 */
	public String loadXgwt() {
		content = zqbGpfxProjectManageService.loadXgwt(projectNo);
		demId = zqbGpfxProjectManageService.getTYDemId("股票发行问题反馈表单");
		formid = zqbGpfxProjectManageService.getTYFormid("股票发行问题反馈表单");
		return SUCCESS;
	}

	public void checkRwid() {
		String zlrw = zqbGpfxProjectManageService.checkRwid(projectNo, groupid);
		ResponseUtil.write(zlrw);
	}

	/**
	 * 删除评价 by dqq
	 */
	public void removePj() {
		boolean flag = false;
		if (instanceid != null) {
			flag = zqbGpfxProjectManageService.removePJ(instanceid);
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
			flag = zqbGpfxProjectManageService.deleteXgwt(instanceid);
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
			flag = zqbGpfxProjectManageService.deleteTask(instanceid);
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
		String jdzl = zqbGpfxProjectManageService.getJdzl(projectNo, groupid,
				instanceid);
		ResponseUtil.write(jdzl);
	}
	
	public void loadjdzlTwo() {
		String jdzl = zqbGpfxProjectManageService.getJdzlTwo(projectNo, groupid,
				instanceid);
		ResponseUtil.write(jdzl);
	}
	
	//checkXMPJ
	public void checkXMPJ(){
		String info=zqbGpfxProjectManageService.checkXMPJ(groupid,pjsm,projectno,pjr,instanceid);
		ResponseUtil.write(info);
	}

	/**
	 * 删除文件
	 * 
	 */
	public void deleteFile() {
		boolean flag = zqbGpfxProjectManageService.deleteFile(uuid);
		String info = "";
		if (flag) {
			info = "true";
		} else {
			info = "false";
		}
		ResponseUtil.write(info);
	}

	public void deleteXMZL() {
		boolean flag = zqbGpfxProjectManageService.deleteXMZL(uuid);
		String info = "";
		if (flag) {
			info = "true";
		} else {
			info = "false";
		}
		ResponseUtil.write(info);
	}

	public void checkPJ() {
		boolean pj = zqbGpfxProjectManageService.checkPJ(projectNo, groupid);
		ResponseUtil.write(String.valueOf(pj));
	}

	public void checkXmjdExists() {
		boolean id = zqbGpfxProjectManageService
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
		list = zqbGpfxProjectManageService.getXgwthfx(pageNumber, pageSize,
				projectName, xmjd, question);
		totalNum = zqbGpfxProjectManageService.getXgwthfSize(projectName, xmjd,
				question);
		demId = zqbGpfxProjectManageService.getTYDemId("股票发行项目");
		formid = zqbGpfxProjectManageService.getTYFormid("股票发行项目");
		return SUCCESS;
	}
	public void zqbGpfxProjectThisCheck(){
		String spztcheck = ZqbProjectManageService.getZqbGpfxProjectThisCheck(projectno);
		if(spztcheck == null){
			ResponseUtil.write("");
		}else if(spztcheck.equals("审批通过")){
			ResponseUtil.write(spztcheck);
		}
	}
	
	public String zqbAddxmcyDepartmentSet(){
		return SUCCESS;
	}
	public String zqbCLBMSet(){
		return SUCCESS;
	}
}
