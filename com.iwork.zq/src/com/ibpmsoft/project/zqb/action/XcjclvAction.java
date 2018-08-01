package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.XcjclvService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.ResponseUtil;

public class XcjclvAction {
	private XcjclvService xcjclvService;
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
	private String khbh;
	private String khmc;
	private String zqdmxs;
	private String zqjcxs;
	private String  startdate;
	private String  enddate;
	private String xcjcry;
	private String xcjclx;
	private String formid;
	private String demid;
	private List<HashMap<String,Object>> xcList;
	private String tid; 
	private Long instanceid;
	private String pxry; 
	/**
	 * 进入培训记录
	 * @return
	 */
	public String pxjuindex(){
		return "success";
	}
	/**
	 * 现场检查记录导出 
	 */
	public void xcjcjlToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		xcjclvService.thxmexportexcl(response,zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pageSize, pageNumber);
	}/**
	 * 培训记录导出 
	 */
	public void pxjllToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		xcjclvService.pxjlexcl(response,zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pxry,pageSize, pageNumber);
	}
	/**
	 * 进入现场检查记录
	 * @return
	 */
	public String index(){
		return "success";
	}
	/**
	 * 查询现场检查记录
	 * @return
	 */
	public String doSearch(){
		if("undefined".equals(zqdmxs)){
			zqdmxs="";
		}
		if("undefined".equals(zqjcxs)){
			zqjcxs="";
		}
		
		formid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='开户信息管理'","FORMID");
		demid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='开户信息管理'", "ID");
		list=xcjclvService.getXcjcList(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pageSize, pageNumber);
		totalNum=xcjclvService.getXcjcListSize(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx);
		return "success";
	}
	/**
	 * 查询培训记录
	 * @return
	 */
	public String doSearchPxjl(){
		if("undefined".equals(zqdmxs)){
			zqdmxs="";
		}
		if("undefined".equals(zqjcxs)){
			zqjcxs="";
		}
		
		formid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='培训记录'","FORMID");
		demid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='培训记录'", "ID");
		list=xcjclvService.getPxjlList(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pxry,pageSize, pageNumber);
		totalNum=xcjclvService.getPxjlListSize(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pxry);
		return "success";
	}
	public void delXcjclv(){
		
		String info= xcjclvService.delete(instanceid);
		ResponseUtil.write(info); 
	}
	public XcjclvService getXcjclvService() {
		return xcjclvService;
	}
	public void setXcjclvService(XcjclvService xcjclvService) {
		this.xcjclvService = xcjclvService;
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
	public String getXcjcry() {
		return xcjcry;
	}
	public void setXcjcry(String xcjcry) {
		this.xcjcry = xcjcry;
	}
	public String getXcjclx() {
		return xcjclx;
	}
	public void setXcjclx(String xcjclx) {
		this.xcjclx = xcjclx;
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
	public List<HashMap<String, Object>> getXcList() {
		return xcList;
	}
	public void setXcList(List<HashMap<String, Object>> xcList) {
		this.xcList = xcList;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Long getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public String getPxry() {
		return pxry;
	}
	public void setPxry(String pxry) {
		this.pxry = pxry;
	}

}
