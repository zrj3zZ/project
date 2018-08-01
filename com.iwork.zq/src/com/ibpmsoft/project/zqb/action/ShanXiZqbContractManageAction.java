package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ShanXiZqbContractManageService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ShanXiZqbContractManageAction extends ActionSupport {
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;

	public String index() {
		if (pageNumber == 0)
			pageNumber = 1;
		if(rqtype==null){
			rqtype="1";
		}
		xmqyFormId=shanXiZqbContractManageService.getConfig("xmqyformid");
		xmqyDemId=shanXiZqbContractManageService.getConfig("xmqydemid");
		contractList = shanXiZqbContractManageService.getList(gsmc, ssbm, xylx, xmlx, qyrqbegin, qyrqend,rqtype);
		//totalNum = shanXiZqbContractManageService.getListSize(gsmc, ssbm, xylx, xmlx, qyrqbegin, qyrqend);
		return SUCCESS;
	}
	
	public void remove(){
		boolean flag=shanXiZqbContractManageService.removQianYue(instanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public void expQianYue(){
		HttpServletResponse response = ServletActionContext.getResponse();
		shanXiZqbContractManageService.expProjectList(response,gsmc, ssbm, xylx, xmlx, qyrqbegin, qyrqend,rqtype);
	}
	
	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	private List<HashMap<String, Object>> contractList;
	private String gsmc;
	private String ssbm;
	private String xylx;
	private String xmlx;
	private String qyrqbegin;
	private String qyrqend;
	private Long instanceid;
	private Long xmqyDemId;
	private Long xmqyFormId;
	private String rqtype;
	private ShanXiZqbContractManageService shanXiZqbContractManageService;
	
	public String getRqtype() {
		return rqtype;
	}

	public void setRqtype(String rqtype) {
		this.rqtype = rqtype;
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

	public List<HashMap<String, Object>> getContractList() {
		return contractList;
	}

	public void setContractList(List<HashMap<String, Object>> contractList) {
		this.contractList = contractList;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getSsbm() {
		return ssbm;
	}

	public void setSsbm(String ssbm) {
		this.ssbm = ssbm;
	}

	public String getXylx() {
		return xylx;
	}

	public void setXylx(String xylx) {
		this.xylx = xylx;
	}

	public String getXmlx() {
		return xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	public String getQyrqbegin() {
		return qyrqbegin;
	}

	public void setQyrqbegin(String qyrqbegin) {
		this.qyrqbegin = qyrqbegin;
	}

	public String getQyrqend() {
		return qyrqend;
	}

	public void setQyrqend(String qyrqend) {
		this.qyrqend = qyrqend;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public Long getXmqyDemId() {
		return xmqyDemId;
	}

	public void setXmqyDemId(Long xmqyDemId) {
		this.xmqyDemId = xmqyDemId;
	}

	public Long getXmqyFormId() {
		return xmqyFormId;
	}

	public void setXmqyFormId(Long xmqyFormId) {
		this.xmqyFormId = xmqyFormId;
	}

	public void setShanXiZqbContractManageService(
			ShanXiZqbContractManageService shanXiZqbContractManageService) {
		this.shanXiZqbContractManageService = shanXiZqbContractManageService;
	}

}
