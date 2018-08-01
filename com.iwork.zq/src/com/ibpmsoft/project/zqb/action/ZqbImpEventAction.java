package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.service.ZqbImpEventService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**重要督导事项
 * @author admin
 *
 */
public class ZqbImpEventAction  extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZqbImpEventService zqbImpEventService;
	private List<HashMap> list;
	private String sxmc;
	private String startdate;
	private String instanceid;
	private boolean ISPURVIEW;
	private String WLINS;
	
	public String getWLINS() {
		return WLINS;
	}
	public void setWLINS(String wLINS) {
		WLINS = wLINS;
	}
	private String returnContent;
	public String getReturnContent() {
		return returnContent;
	}
	public void setReturnContent(String returnContent) {
		this.returnContent = returnContent;
	}
	
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	private String enddate;
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getSxmc() {
		return sxmc;
	}
	public void setSxmc(String sxmc) {
		this.sxmc = sxmc;
	}
	public List<HashMap> getList() {
		return list;
	}
	public void setList(List<HashMap> list) {
		this.list = list;
	}
	private int pageNumber; // 当前页数
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
	private int pageSize = 10; // 每页条数
	private int totalNum;
	public ZqbImpEventService getZqbImpEventService() {
		return zqbImpEventService;
	}
	public void setZqbImpEventService(ZqbImpEventService zqbImpEventService) {
		this.zqbImpEventService = zqbImpEventService;
	}
	private boolean superman;
	public boolean isSuperman() {
		return superman;
	}
	public void setSuperman(boolean superman) {
		this.superman = superman;
	}
	public String list(){
		if (pageNumber == 0)
			pageNumber = 1;
		superman = zqbImpEventService.getIsSuperMan();
		list = zqbImpEventService.getList(pageSize, pageNumber,
				sxmc, startdate, enddate);
		totalNum = zqbImpEventService.getTotalListSize(pageSize, pageNumber,
				sxmc, startdate, enddate);
		return SUCCESS;
	}
	public String cxddlist(){
		list = zqbImpEventService.getcxddList(pageSize, pageNumber,
				sxmc, startdate, enddate);
		return SUCCESS;
	}
	public void getFkzl(){
		boolean flag=zqbImpEventService.getFkzl(instanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	public void sendMail(){
		String info=zqbImpEventService.sendMail(instanceid);
		ResponseUtil.write(info.equals("")?"success":info);
	}
    public String getform(){
    	if(WLINS!=null&&!WLINS.equals("")){
    	   returnContent=zqbImpEventService.getZydd(WLINS);
    	}
    	return SUCCESS;
    }
    public void deleteDdsj(){
    	boolean flag=zqbImpEventService.deleteDdsj(instanceid);
    	ResponseUtil.write(flag?"success":"error");
    }
}
