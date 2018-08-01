package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.service.ZqbCcywcService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbCcywcAction extends ActionSupport {
	private int runPageNumber; // 当前页数
	private int runTotalNum; // 总页数
	private int runPageSize = 10; // 每页条数
	private int closePageNumber; // 当前页数
	private int closeTotalNum; // 总页数
	private int closePageSize = 10; // 每页条数
	private List<HashMap<String, Object>> bgRunList;
	private List<HashMap<String, Object>> bgCloseList;
	private List<HashMap<String, Object>> taskList;
	private ZqbCcywcService zqbCcywcService;
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
	private String ccsy;
	private String zt;
	private String bslx;
	private String cclc;
	private String wclc;
	private String ccid;
	public String getCcid() {
		return ccid;
	}

	public void setCcid(String ccid) {
		this.ccid = ccid;
	}

	public String getBslx() {
		return bslx;
	}

	public void setBslx(String bslx) {
		this.bslx = bslx;
	}

	private String flag;
	public String getCcsy() {
		return ccsy;
	}

	public void setCcsy(String ccsy) {
		this.ccsy = ccsy;
	}

	public String getZt() {
		return zt;
	}

	public ZqbCcywcService getZqbCcywcService() {
		return zqbCcywcService;
	}

	public void setZqbCcywcService(ZqbCcywcService zqbCcywcService) {
		this.zqbCcywcService = zqbCcywcService;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}


	
	

	public String getCclc() {
		return cclc;
	}

	public void setCclc(String cclc) {
		this.cclc = cclc;
	}

	public String getWclc() {
		return wclc;
	}

	public void setWclc(String wclc) {
		this.wclc = wclc;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public String index(){
		cclc = ProcessAPI.getInstance().getProcessActDefId("CCLC");
		wclc = ProcessAPI.getInstance().getProcessActDefId("WCLC");
		//获取当前登录用户角色id
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String orgRoleId = uc._userModel.getUserid();
			if (pageNumber == 0)
				pageNumber = 1;
			zt=this.zt;
			ccsy=this.ccsy;
			bslx=this.bslx;
		list = zqbCcywcService.getCcwcList(ccsy,zt,bslx,pageSize, pageNumber);
		totalNum=zqbCcywcService.getCcwcListSize(ccsy,zt,bslx);
		return SUCCESS;
	}
	private String yhm;
	public String getccywcbyuserid(){
		cclc = ProcessAPI.getInstance().getProcessActDefId("CCLC");
		wclc = ProcessAPI.getInstance().getProcessActDefId("WCLC");
		//获取当前登录用户角色id
		
			if (pageNumber == 0)
				pageNumber = 1;
			zt=this.zt;
			ccsy=this.ccsy;
			bslx=this.bslx;
		list = zqbCcywcService.getCcwcbyuseridList(yhm,ccsy,zt,bslx,pageSize, pageNumber);
		totalNum=zqbCcywcService.getCcwcbyuseridListSize(yhm,ccsy,zt,bslx);
		return SUCCESS;
	}
	

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String delCc(){
		
		flag = zqbCcywcService.delCc(ccid);
		ResponseUtil.write(flag);
		return null;
	}

}
