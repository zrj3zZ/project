package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.service.CjywlczyService;

public class CjywlczyAction {
	private CjywlczyService cjywlczyService;
	private String sxmc;
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
	
	public String showList(){
		try {
			list=cjywlczyService.getCjList(sxmc, pageSize, pageNumber);
			totalNum=cjywlczyService.getCjListSize(sxmc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	public CjywlczyService getCjywlczyService() {
		return cjywlczyService;
	}
	public void setCjywlczyService(CjywlczyService cjywlczyService) {
		this.cjywlczyService = cjywlczyService;
	}
	public String getSxmc() {
		return sxmc;
	}
	public void setSxmc(String sxmc) {
		this.sxmc = sxmc;
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

}
