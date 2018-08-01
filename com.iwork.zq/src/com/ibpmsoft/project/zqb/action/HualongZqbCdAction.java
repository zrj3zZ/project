package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.HualongZqbCdService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class HualongZqbCdAction extends ActionSupport{
	private int runPageNumber; // 当前页数
	private int runTotalNum; // 总页数
	private int runPageSize = 10; // 每页条数
	private int closePageNumber; // 当前页数
	private int closeTotalNum; // 总页数
	private int closePageSize = 10; // 每页条数
	private List<HashMap<String, Object>> bgRunList;
	private List<HashMap<String, Object>> bgCloseList;
	private List<HashMap<String, Object>> taskList;
	private String jyf;
	private String jydsf;
	private String czbm;
	private String clbm;
	private String cyrxm;
	private int ymid;
	private String xmbh;
	private String xmlcServer;
	private String fazlbsServer;
	private String sbzlServer;
	private String zlgdServer;
	private String ybcwXmlxServer;
	private String ybcwGzjdhbServer;
	private String ybcwZlgdServer;
	private String instanceIdStr;
	private String customername;
	private Long formid;
	private Long demId;
	private String  xmmc;
	private String  hldagdlc;
	private String  startdate;
	private String  enddate;
	private String  gjjd;
	private String  scbk;
	private int pageNumber = 1; // 当前页数
	private int pageSize = 10; // 每页条数
	private int totalNum; 
	private List<HashMap<String, Object>> list;
	private Long instanceid;
	private String actStepDefId;
	private String damc;
	private String sqsj;
	private String gdsj;
	private String dalx;
	private String gdwz;
	private String daid;
	private String zt;
	private String flag;
	private Long orgroleid;
	
	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	private HualongZqbCdService hualongZqbCdService;
	
	
	

	public String getDaid() {
		return daid;
	}

	public void setDaid(String daid) {
		this.daid = daid;
	}

	public String getDamc() {
		return damc;
	}

	public void setDamc(String damc) {
		this.damc = damc;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getGdsj() {
		return gdsj;
	}

	public void setGdsj(String gdsj) {
		this.gdsj = gdsj;
	}

	public String getDalx() {
		return dalx;
	}

	public void setDalx(String dalx) {
		this.dalx = dalx;
	}

	public String getGdwz() {
		return gdwz;
	}

	public void setGdwz(String gdwz) {
		this.gdwz = gdwz;
	}

	public HualongZqbCdService getHualongZqbCdService() {
		return hualongZqbCdService;
	}

	public void setHualongZqbCdService(HualongZqbCdService hualongZqbCdService) {
		this.hualongZqbCdService = hualongZqbCdService;
	}

	public int getRunPageNumber() {
		return runPageNumber;
	}

	public void setRunPageNumber(int runPageNumber) {
		this.runPageNumber = runPageNumber;
	}

	public int getRunTotalNum() {
		return runTotalNum;
	}

	public void setRunTotalNum(int runTotalNum) {
		this.runTotalNum = runTotalNum;
	}

	public int getRunPageSize() {
		return runPageSize;
	}

	public void setRunPageSize(int runPageSize) {
		this.runPageSize = runPageSize;
	}

	public int getClosePageNumber() {
		return closePageNumber;
	}

	public void setClosePageNumber(int closePageNumber) {
		this.closePageNumber = closePageNumber;
	}

	public int getCloseTotalNum() {
		return closeTotalNum;
	}

	public void setCloseTotalNum(int closeTotalNum) {
		this.closeTotalNum = closeTotalNum;
	}

	public int getClosePageSize() {
		return closePageSize;
	}

	public void setClosePageSize(int closePageSize) {
		this.closePageSize = closePageSize;
	}
	
	public List<HashMap<String, Object>> getBgRunList() {
		return bgRunList;
	}

	public void setBgRunList(List<HashMap<String, Object>> bgRunList) {
		this.bgRunList = bgRunList;
	}

	public List<HashMap<String, Object>> getBgCloseList() {
		return bgCloseList;
	}

	public void setBgCloseList(List<HashMap<String, Object>> bgCloseList) {
		this.bgCloseList = bgCloseList;
	}

	public List<HashMap<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<HashMap<String, Object>> taskList) {
		this.taskList = taskList;
	}

	public String getJyf() {
		return jyf;
	}

	public void setJyf(String jyf) {
		this.jyf = jyf;
	}

	public String getJydsf() {
		return jydsf;
	}

	public void setJydsf(String jydsf) {
		this.jydsf = jydsf;
	}

	public String getCzbm() {
		return czbm;
	}

	public void setCzbm(String czbm) {
		this.czbm = czbm;
	}

	public String getClbm() {
		return clbm;
	}

	public void setClbm(String clbm) {
		this.clbm = clbm;
	}

	public String getCyrxm() {
		return cyrxm;
	}

	public void setCyrxm(String cyrxm) {
		this.cyrxm = cyrxm;
	}

	public int getYmid() {
		return ymid;
	}

	public void setYmid(int ymid) {
		this.ymid = ymid;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmlcServer() {
		return xmlcServer;
	}

	public void setXmlcServer(String xmlcServer) {
		this.xmlcServer = xmlcServer;
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

	public String getZlgdServer() {
		return zlgdServer;
	}

	public void setZlgdServer(String zlgdServer) {
		this.zlgdServer = zlgdServer;
	}

	public String getYbcwXmlxServer() {
		return ybcwXmlxServer;
	}

	public void setYbcwXmlxServer(String ybcwXmlxServer) {
		this.ybcwXmlxServer = ybcwXmlxServer;
	}

	public String getYbcwGzjdhbServer() {
		return ybcwGzjdhbServer;
	}

	public void setYbcwGzjdhbServer(String ybcwGzjdhbServer) {
		this.ybcwGzjdhbServer = ybcwGzjdhbServer;
	}

	public String getYbcwZlgdServer() {
		return ybcwZlgdServer;
	}

	public void setYbcwZlgdServer(String ybcwZlgdServer) {
		this.ybcwZlgdServer = ybcwZlgdServer;
	}

	public String getInstanceIdStr() {
		return instanceIdStr;
	}

	public void setInstanceIdStr(String instanceIdStr) {
		this.instanceIdStr = instanceIdStr;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public Long getFormid() {
		return formid;
	}

	public void setFormid(Long formid) {
		this.formid = formid;
	}

	public Long getDemId() {
		return demId;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	

	public String getHldagdlc() {
		return hldagdlc;
	}

	public void setHldagdlc(String hldagdlc) {
		this.hldagdlc = hldagdlc;
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

	public String getGjjd() {
		return gjjd;
	}

	public void setGjjd(String gjjd) {
		this.gjjd = gjjd;
	}

	public String getScbk() {
		return scbk;
	}

	public void setScbk(String scbk) {
		this.scbk = scbk;
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

	

	public List<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}
	private String  thxmlc;
	private String	status;
	private String	dyrId;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDyrId() {
		return dyrId;
	}

	public void setDyrId(String dyrId) {
		this.dyrId = dyrId;
	}

	
	
	public String getThxmlc() {
		return thxmlc;
	}

	public void setThxmlc(String thxmlc) {
		this.thxmlc = thxmlc;
	}
	public String index(){
		hldagdlc = ProcessAPI.getInstance().getProcessActDefId("DAGDLC");
		//获取当前登录用户角色id
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String orgRoleId = uc._userModel.getUserid();
			if (pageNumber == 0)
				pageNumber = 1;
		damc=this.damc;
		zt=this.zt;
		dalx=this.dalx;
		gdwz=this.gdwz;
		list = hualongZqbCdService.getHldaList(damc,gdwz,dalx,zt,pageSize, pageNumber);
		totalNum=hualongZqbCdService.getHldaListSize(damc,gdwz,dalx,zt);
		orgroleid = uc._userModel.getOrgroleid();
		return SUCCESS;
	}
	
	public String gdOut(){
		hldagdlc = ProcessAPI.getInstance().getProcessActDefId("DAGDLC");
		//获取当前登录用户角色id
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String orgRoleId = uc._userModel.getUserid();
			if (pageNumber == 0)
				pageNumber = 1;
		damc=this.damc;
		zt=this.zt;
		dalx=this.dalx;
		gdwz=this.gdwz;
		HttpServletResponse response = ServletActionContext.getResponse();
		hualongZqbCdService.gdOut(response,damc,gdwz,dalx,zt);
		
		
		
		return null;
	}
	public String damx(){
		
		try {
			list=hualongZqbCdService.getUpd(daid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return SUCCESS;
	}


	public String delDa(){
		
		flag = hualongZqbCdService.delDg(daid);
		ResponseUtil.write(flag);
		return null;
	}
}
