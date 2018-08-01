package com.ibpmsoft.project.zqb.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.DdqyService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.ResponseUtil;

public class DdqyAction {
	private DdqyService ddqyService;
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
	private String formid;
	private String demid;
	private Long instanceid;
	private int id;
	private String khmc;
	private String kssj;
	private String jssj;
	private String rzformid;
	private String rzdemid;
	private String rzid;
	private String flags;
	private String jyyy;
	private File upFile;
	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}
	
	/**
	 * 督导签约导入
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void ddqyDrupfile() throws  Exception{
		String qydemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='重要督导事项'", "ID");
		String rzdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'", "ID");
		if (this.upFile != null) {
			ddqyService.doExcelImp(upFile,Long.parseLong(qydemid),Long.parseLong(rzdemid));
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write("error");
		}
			
	}
	public String ddqyDr(){
		return "success";
	}
	/**
	 * 下载督导签约模板
	 */
	public void ddqyMbupload(){
		try {
			ddqyService.ddqyMbupload();
		} catch (Exception e) {
		
		}
	}
	/**
	 * 导出 
	 */
	public void ddToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		ddqyService.thxmexportexcl(response,khmc, kssj, jssj, pageSize, pageNumber);
	}
	/**
	 *督导签约 主表列表
	 * @return
	 */
	public String ddListShow(){
		formid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='重要督导事项'","FORMID");
		demid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='重要督导事项'", "ID");
		rzformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'","FORMID");
		rzdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'", "ID");
		list=ddqyService.getddList(khmc, kssj, jssj, pageSize, pageNumber);
		totalNum=ddqyService.getddListSize(khmc, kssj, jssj);
		return "success";
	}
	/**
	 * 入账  子表列表
	 * @return
	 */
	public String ddZbRzList(){
		rzformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'","FORMID");
		rzdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='财务入账信息'", "ID");
		list=ddqyService.getrzList(rzid, pageSize, pageNumber);
		totalNum=ddqyService.getrzListSize(rzid);
		return "success";
	}
	/**
	 * 最后修改时间
	 */
	public void getDdqy(){
		String jsonstr = ddqyService.getDdqy(id);
		ResponseUtil.write(jsonstr); 
	}
	/**
	 * 客户名称，编号
	 */
	public void getZbkh(){
		String jsonstr = ddqyService.getZbkh(id);
		ResponseUtil.write(jsonstr); 
	}
	public String toJy(){
		return "success";
	}
	public void jySave(){
		String jsonstr = ddqyService.jySave(rzid,jyyy);
		ResponseUtil.write(jsonstr); 
	}
	public DdqyService getDdqyService() {
		return ddqyService;
	}

	public void setDdqyService(DdqyService ddqyService) {
		this.ddqyService = ddqyService;
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
	public String getKhmc() {
		return khmc;
	}
	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
	public String getRzformid() {
		return rzformid;
	}
	public void setRzformid(String rzformid) {
		this.rzformid = rzformid;
	}
	public String getRzdemid() {
		return rzdemid;
	}
	public void setRzdemid(String rzdemid) {
		this.rzdemid = rzdemid;
	}
	public String getRzid() {
		return rzid;
	}
	public void setRzid(String rzid) {
		this.rzid = rzid;
	}
	public String getFlags() {
		return flags;
	}
	public void setFlags(String flags) {
		this.flags = flags;
	}
	public String getJyyy() {
		return jyyy;
	}
	public void setJyyy(String jyyy) {
		this.jyyy = jyyy;
	}
	

}
