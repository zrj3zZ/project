package com.iwork.plugs.email.action;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.email.model.MailDelModel;
import com.iwork.plugs.email.service.IWorkMailDelService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailDelAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private IWorkMailDelService iWorkMailDelService;
	private List<MailDelModel> mailDelList;
	private List<HashMap> mailDelLists;
	private int pageNumber;	//当前页数
	private int pageSize;//每页显示条数
	private long total;//总记录条数
	
	public IWorkMailDelService getiWorkMailDelService() {
		return iWorkMailDelService;
	}

	public void setiWorkMailDelService(IWorkMailDelService iWorkMailDelService) {
		this.iWorkMailDelService = iWorkMailDelService;
	}



	/**
	 * 获取已删除分页信息
	 * @return
	 */
	public String queryMailDelListByPage(){
		String userId=UserContextUtil.getInstance().getCurrentUserId();
		// 获取总记录条数
		total = iWorkMailDelService.getMailDelListSize(userId);
		int startRow = 0;
		if(pageNumber == 0){
			pageNumber = 1;
		}
		if(pageSize == 0){
			pageSize = 10;
		}
		// 分页记录
		mailDelLists=iWorkMailDelService.getMailDelList(userId,pageSize,pageNumber);

		return SUCCESS;
		
	}

	public List<MailDelModel> getMailDelList() {
		return mailDelList;
	}

	public void setMailDelList(List<MailDelModel> mailDelList) {
		this.mailDelList = mailDelList;
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

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<HashMap> getMailDelLists() {
		return mailDelLists;
	}

	public void setMailDelLists(List<HashMap> mailDelLists) {
		this.mailDelLists = mailDelLists;
	}
	
}
