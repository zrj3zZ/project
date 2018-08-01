package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.service.ZqbSearchCenterService;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbSearchCenterAction extends ActionSupport {
	private ZqbSearchCenterService zqbSearchCenterService;

	public List<HashMap> customerList;
	public List<HashMap> companyList;
	public List<HashMap> inUserList;
	public List<HashMap> outUserList;
	public List<HashMap> stocklist1;
	public List<HashMap> stocklist2;
	public List<HashMap> stocklist3;
	public List<HashMap> stocklist4;
	public List<HashMap> eventList;
	public List<HashMap> getEventList() {
		return eventList;
	}
	public void setEventList(List<HashMap> eventList) {
		this.eventList = eventList;
	}
	private String customerno;
	
	public String customerIndex(){
		customerList = zqbSearchCenterService.getCustomerListData();
		return SUCCESS;
	}
	public String companyIndex(){
		if(customerno!=null){
			companyList = zqbSearchCenterService.getCompanyListData(customerno);	
		}
		return SUCCESS;
	}
	public String inUserIndex(){
		if(customerno!=null){
			inUserList = zqbSearchCenterService.getInUserListData(customerno);
		}
		return SUCCESS;
	}
	public String stockIndex(){
		if(customerno!=null){
			stocklist1 = zqbSearchCenterService.getInStockData1(customerno);
			stocklist2 = zqbSearchCenterService.getInStockData2(customerno);
			stocklist3 = zqbSearchCenterService.getInStockData3(customerno);
			stocklist4 = zqbSearchCenterService.getInStockData4(customerno);
		}
		return SUCCESS;
	}
	
	
	public String eventIndex(){
		if(customerno!=null){
			eventList = zqbSearchCenterService.getInEventListData(customerno);
		}
		return SUCCESS;
	}
	public String outUserIndex(){
		if(customerno!=null){
			outUserList = zqbSearchCenterService.getOutListData(customerno);
		}
		return SUCCESS;
	}
	
	
	public void setZqbSearchCenterService(
			ZqbSearchCenterService zqbSearchCenterService) {
		this.zqbSearchCenterService = zqbSearchCenterService;
	}
	public List<HashMap> getCustomerList() {
		return customerList;
	}
	public List<HashMap> getCompanyList() {
		return companyList;
	}
	public List<HashMap> getInUserList() {
		return inUserList;
	}
	public List<HashMap> getOutUserList() {
		return outUserList;
	}
	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	public List<HashMap> getStocklist1() {
		return stocklist1;
	}
	public List<HashMap> getStocklist2() {
		return stocklist2;
	}
	public List<HashMap> getStocklist3() {
		return stocklist3;
	}
	public List<HashMap> getStocklist4() {
		return stocklist4;
	}
	
	
	
	
	
}
