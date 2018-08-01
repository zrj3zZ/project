package com.ibpmsoft.project.zqb.sx.gpfx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.sx.gpfx.service.ZqbGpfxProjectSxService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbGpfxProjectSxAction extends ActionSupport {
	private ZqbGpfxProjectSxService zqbGpfxProjectSxService;
	private List<HashMap> runlist;
	private List<HashMap> closelist;
	private int pageSize=10;
	private int pageNumber;
	private int runNum;
	private int pageSize1=10;
	private int pageNumber1;
	private int closeNum;
	private String ymid;
	private String customername;
	private String sshy;
	private String czbm;
	private String cyrname;
	private String demid;
	private String formid;
	private Long instanceid;
	private List<HashMap> accountedforlist;
	private int totalNum;
	private String departmentname;
	private String dzrqbegin;
	private String dzrqend;
	private String xmlx;
	private String xylx;
	private String instanceids;
	private String commonstr;
	private String jsyy;
	private String memo;
	private String month;
	private String dzrq;
	private List<HashMap> commonlist;
	private Long id;
	private String xmlxnfxServer;
	private String zlgdsjfxServer;
	private String fazlbsServer;
	private String sbzlServer;
	private String projectno;
	private String projectname;
	private String countdownscore;
	private String actDefId;
	private String cwrzServer;
	
	public String getCwrzServer() {
		return cwrzServer;
	}
	public void setCwrzServer(String cwrzServer) {
		this.cwrzServer = cwrzServer;
	}
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	public String getCountdownscore() {
		return countdownscore;
	}
	public void setCountdownscore(String countdownscore) {
		this.countdownscore = countdownscore;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getProjectno() {
		return projectno;
	}
	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}
	public String getXmlxnfxServer() {
		return xmlxnfxServer;
	}
	public void setXmlxnfxServer(String xmlxnfxServer) {
		this.xmlxnfxServer = xmlxnfxServer;
	}
	public String getZlgdsjfxServer() {
		return zlgdsjfxServer;
	}
	public void setZlgdsjfxServer(String zlgdsjfxServer) {
		this.zlgdsjfxServer = zlgdsjfxServer;
	}
	public String getFazlbsServer() {
		return fazlbsServer;
	}
	public void setFazlbsServer(String fazlbsServer) {
		this.fazlbsServer = fazlbsServer;
	}
	public String getSbzlServer() {
		return sbzlServer;
	}
	public void setSbzlServer(String sbzlServer) {
		this.sbzlServer = sbzlServer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<HashMap> getCommonlist() {
		return commonlist;
	}
	public void setCommonlist(List<HashMap> commonlist) {
		this.commonlist = commonlist;
	}
	public String getDzrq() {
		return dzrq;
	}
	public void setDzrq(String dzrq) {
		this.dzrq = dzrq;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getJsyy() {
		return jsyy;
	}
	public void setJsyy(String jsyy) {
		this.jsyy = jsyy;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCommonstr() {
		return commonstr;
	}
	public void setCommonstr(String commonstr) {
		this.commonstr = commonstr;
	}
	public String getInstanceids() {
		return instanceids;
	}
	public void setInstanceids(String instanceids) {
		this.instanceids = instanceids;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getDzrqbegin() {
		return dzrqbegin;
	}
	public void setDzrqbegin(String dzrqbegin) {
		this.dzrqbegin = dzrqbegin;
	}
	public String getDzrqend() {
		return dzrqend;
	}
	public void setDzrqend(String dzrqend) {
		this.dzrqend = dzrqend;
	}
	public String getXmlx() {
		return xmlx;
	}
	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}
	public String getXylx() {
		return xylx;
	}
	public void setXylx(String xylx) {
		this.xylx = xylx;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<HashMap> getAccountedforlist() {
		return accountedforlist;
	}
	public void setAccountedforlist(List<HashMap> accountedforlist) {
		this.accountedforlist = accountedforlist;
	}
	public Long getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public String getYmid() {
		return ymid;
	}
	public void setYmid(String ymid) {
		this.ymid = ymid;
	}
	public String getDemid() {
		return demid;
	}
	public void setDemid(String demid) {
		this.demid = demid;
	}
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getSshy() {
		return sshy;
	}
	public void setSshy(String sshy) {
		this.sshy = sshy;
	}
	public String getCzbm() {
		return czbm;
	}
	public void setCzbm(String czbm) {
		this.czbm = czbm;
	}
	public String getCyrname() {
		return cyrname;
	}
	public void setCyrname(String cyrname) {
		this.cyrname = cyrname;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getRunNum() {
		return runNum;
	}
	public void setRunNum(int runNum) {
		this.runNum = runNum;
	}
	public int getPageSize1() {
		return pageSize1;
	}
	public void setPageSize1(int pageSize1) {
		this.pageSize1 = pageSize1;
	}
	public int getPageNumber1() {
		return pageNumber1;
	}
	public void setPageNumber1(int pageNumber1) {
		this.pageNumber1 = pageNumber1;
	}
	public int getCloseNum() {
		return closeNum;
	}
	public void setCloseNum(int closeNum) {
		this.closeNum = closeNum;
	}
	public List<HashMap> getCloselist() {
		return closelist;
	}
	public void setCloselist(List<HashMap> closelist) {
		this.closelist = closelist;
	}
	public List<HashMap> getRunlist() {
		return runlist;
	}
	public void setRunlist(List<HashMap> runlist) {
		this.runlist = runlist;
	}
	public void setZqbGpfxProjectSxService(ZqbGpfxProjectSxService zqbGpfxProjectSxService) {
		this.zqbGpfxProjectSxService = zqbGpfxProjectSxService;
	}
	public String indexGpfxSx(){
		String demid$formid = DBUtil.getString("SELECT ID||'@'||FORMID DEMID$FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目信息'", "DEMID$FORMID");
		demid = demid$formid.split("@")[0];
		formid = demid$formid.split("@")[1];
		if(pageNumber==0){
			pageNumber=1;
		}
		runlist = zqbGpfxProjectSxService.getRunlist(pageSize,pageNumber,customername,sshy,czbm,cyrname);
		runNum = zqbGpfxProjectSxService.getRunlistSize(customername,sshy,czbm,cyrname).size();
		if(pageNumber1==0){
			pageNumber1=1;
		}
		closelist = zqbGpfxProjectSxService.getCloselist(pageSize1,pageNumber1,customername,sshy,czbm,cyrname);
		closeNum = zqbGpfxProjectSxService.getCloselistSize(customername,sshy,czbm,cyrname).size();
		return SUCCESS;
	}
	public String projectCloseDialog(){
		commonstr = zqbGpfxProjectSxService.projectCloseDialog(instanceids);
		return SUCCESS;
	}
	public String projectXmjd(){
		if(instanceid != null && instanceid > 0){
			HashMap dataMap = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			projectname = dataMap.get("PROJECTNAME").toString();
		}
		xmlxnfxServer = ProcessAPI.getInstance().getProcessActDefId("XMLXNFX");
		zlgdsjfxServer = ProcessAPI.getInstance().getProcessActDefId("ZLGDSJFX");
		fazlbsServer = ProcessAPI.getInstance().getProcessActDefId("FAZLBS");
		sbzlServer = ProcessAPI.getInstance().getProcessActDefId("SBZL");
		commonstr = zqbGpfxProjectSxService.projectXmjdContent(projectno);
		return SUCCESS;
	}
	public String projectMainMsg(){
		commonstr = zqbGpfxProjectSxService.projectMainMsgContent(projectno);
		return SUCCESS;
	}
	public String wxprojectMainMsg(){
		commonstr = zqbGpfxProjectSxService.wxprojectMainMsgContent(projectno);
		return SUCCESS;
	}
	public String setCloseReason(){
		List<HashMap> list = new ArrayList<HashMap>();
		list.add(DemAPI.getInstance().getFromData(instanceid));
		closelist = list;
		commonlist = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_DZDX");
		commonstr = zqbGpfxProjectSxService.getFileListHtml(closelist);
		return SUCCESS;
	}
	public void setCloseReasonUpdate(){
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目信息'", "UUID");
		HashMap hashdata = DemAPI.getInstance().getFromData(instanceid);
		hashdata.put("JSYY",jsyy);
		hashdata.put("MEMO",memo);
		Boolean flag = DemAPI.getInstance().updateFormData(demUUID, instanceid, hashdata, id, false);
		ResponseUtil.write(flag.toString());
	} 
	public void projectClose() {
		boolean flag = zqbGpfxProjectSxService.projectClose(instanceids,jsyy,memo);
		if (flag) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}
	public String expFixedReserveIndex(){
		return SUCCESS;
	}
	public void expFixedReserve(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectSxService.expFixedReserve(response,month);
	}
	
	public void expIssueObject(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectSxService.expIssueObject(response);
	}
	public String accountedForIndex(){
		/*String demid$formid = DBUtil.getString("SELECT ID||'@'||FORMID DEMID$FORMID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'", "DEMID$FORMID");
		demid = demid$formid.split("@")[0];
		formid = demid$formid.split("@")[1];*/
		cwrzServer = ProcessAPI.getInstance().getProcessActDefId("CWRZSP");
		if(pageNumber==0){
			pageNumber=1;
		}
		
		accountedforlist = zqbGpfxProjectSxService.accountedForList(pageSize,pageNumber,customername,departmentname,dzrqbegin,dzrqend,xmlx,xylx);
		totalNum = zqbGpfxProjectSxService.accountedForListSize(customername,departmentname,dzrqbegin,dzrqend,xmlx,xylx).size();
		return SUCCESS;
	}
	public void accountedForDel(){
		boolean result = DemAPI.getInstance().removeFormData(instanceid);
		if(result){
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
	}
	
	public void accountedForExp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectSxService.accountedForExp(response);
	}
	
	public String setMonth(){
		return SUCCESS;
	}
	
	public void expCwrz(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectSxService.expCwrzList(response,dzrq);
	}
	
	public void expXmsrhz(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbGpfxProjectSxService.expXmsrhzList(response);
	}
}
