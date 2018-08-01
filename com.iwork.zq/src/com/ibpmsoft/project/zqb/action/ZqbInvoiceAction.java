package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ZqbInvoiceService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbInvoiceAction extends ActionSupport {
	private ZqbInvoiceService zqbInvoiceService;
	private List<HashMap<String,Object>> invoiceKhList;
	private List<HashMap<String,Object>> invoiceKpList;
	private Integer totalNum;
	private String customername;
	private int pageNumber; // 当前页数
	private int pageSize = 10; // 每页条数
	private Long khformid;
	private Long khid;
	private Long kpformid;
	private Long kpid;
	private Long khinstanceid;
	private Long kpinstanceid;
	private Long kpdataId;
	private String customerno;
	private String state;
	private String nsrlx;
	private String sqrq;
	private String memo;
	private String flag;
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getNsrlx() {
		return nsrlx;
	}

	public void setNsrlx(String nsrlx) {
		this.nsrlx = nsrlx;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public Long getKpdataId() {
		return kpdataId;
	}

	public void setKpdataId(Long kpdataId) {
		this.kpdataId = kpdataId;
	}

	public Long getKhinstanceid() {
		return khinstanceid;
	}

	public void setKhinstanceid(Long khinstanceid) {
		this.khinstanceid = khinstanceid;
	}

	public Long getKpinstanceid() {
		return kpinstanceid;
	}

	public void setKpinstanceid(Long kpinstanceid) {
		this.kpinstanceid = kpinstanceid;
	}

	public List<HashMap<String, Object>> getInvoiceKpList() {
		return invoiceKpList;
	}

	public void setInvoiceKpList(List<HashMap<String, Object>> invoiceKpList) {
		this.invoiceKpList = invoiceKpList;
	}

	public List<HashMap<String, Object>> getInvoiceKhList() {
		return invoiceKhList;
	}

	public void setInvoiceKhList(List<HashMap<String, Object>> invoiceKhList) {
		this.invoiceKhList = invoiceKhList;
	}

	public Long getKhformid() {
		return khformid;
	}

	public void setKhformid(Long khformid) {
		this.khformid = khformid;
	}

	public Long getKhid() {
		return khid;
	}

	public void setKhid(Long khid) {
		this.khid = khid;
	}

	public Long getKpformid() {
		return kpformid;
	}

	public void setKpformid(Long kpformid) {
		this.kpformid = kpformid;
	}

	public Long getKpid() {
		return kpid;
	}

	public void setKpid(Long kpid) {
		this.kpid = kpid;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
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

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public void setZqbInvoiceService(ZqbInvoiceService zqbInvoiceService) {
		this.zqbInvoiceService = zqbInvoiceService;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 导出底稿归档
	 */
	public void dggdToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbInvoiceService.thxmexportexcl(response,customername,customerno,state);
	}
	public String khIndex(){
		if (pageNumber == 0)
			pageNumber = 1;
		khformid=zqbInvoiceService.getConfig("khformid");
		khid=zqbInvoiceService.getConfig("khid");
		kpformid=zqbInvoiceService.getConfig("kpformid");
		kpid=zqbInvoiceService.getConfig("kpid");
		invoiceKhList=zqbInvoiceService.getKhList(customername,pageNumber,pageSize);
		totalNum=zqbInvoiceService.getKhListSize(customername);
		return SUCCESS;
	}
	;
	public String kpIndex(){
		if (pageNumber == 0)
			pageNumber = 1;
		khformid=zqbInvoiceService.getConfig("khformid");
		khid=zqbInvoiceService.getConfig("khid");
		kpformid=zqbInvoiceService.getConfig("kpformid");
		kpid=zqbInvoiceService.getConfig("kpid");
		invoiceKpList=zqbInvoiceService.getKpList(customername,customerno,state,pageNumber,pageSize);
		totalNum=zqbInvoiceService.getKpListSize(customername,customerno,state);
		return SUCCESS;
	}
	public String updIndex(){
		
		try {
			invoiceKpList=zqbInvoiceService.getUpd(khid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return SUCCESS;
	}
	public String updSave(){
		flag = zqbInvoiceService.updsave(khid, customername, customerno, state, nsrlx, sqrq,memo);
		ResponseUtil.write(flag);
		return null;
	}
	public String delDg(){
		flag = zqbInvoiceService.delDg(khid);
		ResponseUtil.write(flag);
		return null;
	}
	public void removekh(){
		boolean flag=zqbInvoiceService.removekh(khinstanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public void removekp(){
		boolean flag=zqbInvoiceService.removekp(kpinstanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public void setKhState(){
		boolean flag=zqbInvoiceService.setKhState(kpinstanceid,kpid,kpdataId);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
}
