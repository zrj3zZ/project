package com.iwork.plugs.itsArticles.action;

import java.util.HashMap;
import java.util.List;

import com.iwork.plugs.itsArticles.service.ItsArticlesService;
import com.opensymphony.xwork2.ActionSupport;

public class ItsArticlesAction  extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ItsArticlesService itsArticlesService;
	private List<HashMap<String,Object>> itsList;
	private String khbh;
	private String zdmc;
	private String zdwh;
	private String fjmc;
	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	private int pageSize = 20; // 每页条数

	public String index(){
		if (pageNumber == 0)
			pageNumber = 1;
		if(khbh!=null&&!"".equals(khbh)){
			itsList=itsArticlesService.getItsList(khbh,zdmc,fjmc,pageNumber,pageSize);
			totalNum=itsArticlesService.getItsListSize(khbh,zdmc,fjmc);
		}
		return SUCCESS;
	}

	public String getFjmc() {
		return fjmc;
	}

	public void setFjmc(String fjmc) {
		this.fjmc = fjmc;
	}

	public List<HashMap<String, Object>> getItsList() {
		return itsList;
	}

	public void setItsList(List<HashMap<String, Object>> itsList) {
		this.itsList = itsList;
	}

	public String getZdmc() {
		return zdmc;
	}

	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}

	public String getZdwh() {
		return zdwh;
	}

	public void setZdwh(String zdwh) {
		this.zdwh = zdwh;
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

	public void setItsArticlesService(ItsArticlesService itsArticlesService) {
		this.itsArticlesService = itsArticlesService;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

}
