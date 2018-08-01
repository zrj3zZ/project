package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.service.ZqbCxddManagementService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbCxddManagementAction extends ActionSupport {
	private ZqbCxddManagementService zqbCxddManagementService; 
    private String cxddzy;
     private int pageNumber=1;
     private int pageSize = 10; 
 	private String WLINS;

    public String getWLINS() {
		return WLINS;
	}

	public void setWLINS(String wLINS) {
		WLINS = wLINS;
	}

	private boolean ISPURVIEW;
    

	public boolean isISPURVIEW() {
		return ISPURVIEW;
	}

	public void setISPURVIEW(boolean iSPURVIEW) {
		ISPURVIEW = iSPURVIEW;
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

	public String getCxddzy() {
		return cxddzy;
	}

	public void setCxddzy(String cxddzy) {
		this.cxddzy = cxddzy;
	}

	public String getZzcxdd() {
		return zzcxdd;
	}

	public void setZzcxdd(String zzcxdd) {
		this.zzcxdd = zzcxdd;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	private String col;
	private String rule;
	private String zzcxdd;
    private String khmc;
    private String khbh;
    private List<HashMap<String,Object>> list;
    private Long instanceid;
    private int totalNum;
    
	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public List<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}

	public ZqbCxddManagementService getZqbCxddManagementService() {
		return zqbCxddManagementService;
	}

	public void setZqbCxddManagementService(
			ZqbCxddManagementService zqbCxddManagementService) {
		this.zqbCxddManagementService = zqbCxddManagementService;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3737345492182112605L;
	private String returnContent;
	public String getReturnContent() {
		return returnContent;
	}

	public void setReturnContent(String returnContent) {
		this.returnContent = returnContent;
	}
	private String lc;
	private String zqdm;
	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getLc() {
		return lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String doSearch() {
		list=zqbCxddManagementService.doSearch(col,rule,cxddzy,zzcxdd,khmc,khbh,zqdm,pageNumber,pageSize);
		totalNum=zqbCxddManagementService.getTotalNum(cxddzy,zzcxdd,khmc,khbh,zqdm);
		ISPURVIEW = zqbCxddManagementService.getIsSuperMan();//是否场外，是场外则可新增、删除
		return SUCCESS;
	}
	public void doRemove(){
		String info= zqbCxddManagementService.delete(instanceid);
			ResponseUtil.write(info); 
	}
	  public String getform(){
	    	if(WLINS!=null&&!WLINS.equals("")){
	    	   returnContent=zqbCxddManagementService.getform(WLINS);
	    	}
	    	return SUCCESS;
	    }
	public void checkCXDD(){
		String flag=zqbCxddManagementService.checkCXDD(instanceid);
		if("".equals(flag)){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write(flag);
		}
	}
}
