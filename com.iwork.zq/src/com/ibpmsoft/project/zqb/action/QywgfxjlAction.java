package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.QywgfxjlService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.ResponseUtil;

public class QywgfxjlAction {
	private QywgfxjlService qywgfxjlService;
	private int runPageNumber; // 当前页数
	private int runTotalNum; // 总页数
	private int runPageSize = 10; // 每页条数
	private int closePageNumber; // 当前页数
	private int closeTotalNum; // 总页数
	private int closePageSize = 10; // 每页条数
	private int pageNumber = 1; // 当前页数
	private int pageSize = 10; // 每页条数
	private int totalNum; 
	private List<HashMap> list;
	private String gsdm;
	private String gsjc;
	private String wgfxnr;
	private String wgfxlx;
	private String formid;
	private String demid;
	private Long instanceid;
	private int id;
	private String khbh;
	private String khmc;
	
	public String qywgfxjlIndex (){
		return "success";
	}
	/**
	 * 列表查询
	 * @return
	 */
	public String showList(){
		try {
			formid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='信披基本信息'","FORMID");
			demid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='信披基本信息'", "ID");
			list=qywgfxjlService.getQyList(khbh, khmc, gsdm, gsjc, wgfxnr, wgfxlx, pageSize, pageNumber);
			totalNum=qywgfxjlService.getQyListSize(khbh, khmc, gsdm, gsjc, wgfxnr, wgfxlx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	/**
	 * 自动补全查询信息
	 */
	public void getDmJcData(){
		String jsonstr = qywgfxjlService.getDmJcData();
		ResponseUtil.write(jsonstr); 
	}
	public void getQysj(){
		String jsonstr = qywgfxjlService.getQysj(id);
		ResponseUtil.write(jsonstr); 
	}
	/**
	 * 导出 
	 */
	public void qyToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		qywgfxjlService.thxmexportexcl(response);
	}
	public QywgfxjlService getQywgfxjlService() {
		return qywgfxjlService;
	}
	public void setQywgfxjlService(QywgfxjlService qywgfxjlService) {
		this.qywgfxjlService = qywgfxjlService;
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
	public List<HashMap> getList() {
		return list;
	}
	public void setList(List<HashMap> list) {
		this.list = list;
	}
	public String getGsdm() {
		return gsdm;
	}
	public void setGsdm(String gsdm) {
		this.gsdm = gsdm;
	}
	public String getGsjc() {
		return gsjc;
	}
	public void setGsjc(String gsjc) {
		this.gsjc = gsjc;
	}
	public String getWgfxnr() {
		return wgfxnr;
	}
	public void setWgfxnr(String wgfxnr) {
		this.wgfxnr = wgfxnr;
	}
	public String getWgfxlx() {
		return wgfxlx;
	}
	public void setWgfxlx(String wgfxlx) {
		this.wgfxlx = wgfxlx;
	}
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getDemid() {
		return demid;
	}
	public void setDemid(String demid) {
		this.demid = demid;
	}
	public Long getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKhbh() {
		return khbh;
	}
	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}
	public String getKhmc() {
		return khmc;
	}
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

}
