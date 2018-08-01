package com.iwork.app.navigation.directory.action;

import java.util.Collection;
import java.util.List;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.app.navigation.directory.service.SysNavDirectoryService;
import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.server.sequence.service.SysSequenceService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SysNavDirectoryAction extends ActionSupport {
	private SysNavDirectoryService sysNavDirectoryService;
	private SysSequenceService sysSequenceService;
	private PagerService pagerService;
	
	private SysNavDirectory model;
	private Pager pager;
	protected Collection availableItems;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected Long id ;
	protected Long sysNavSystemid;
	protected String queryMap;
	protected List target ;
	
	public String list() throws Exception {
		int totalRow=sysNavDirectoryService.getRows();
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		availableItems=sysNavDirectoryService.getBoDatas(pager.getPageSize(), pager.getStartRow());
		return SUCCESS;		 
	}	
	
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null)
			model = sysNavDirectoryService.getBoData(id);
		else
			id= new Long(sysNavDirectoryService.getMaxID());
	    return SUCCESS;
	}
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id = new Long(sysSequenceService.getSequence(AppContextConstant.SYS_NAV_SYSTEM));
		model = sysNavDirectoryService.getBoData(id);
		if(model==null){
			model = new SysNavDirectory();
			model.setSystemId(sysNavSystemid);
			model.setId(id);
			model.setOrderindex(id);
		}
	    return SUCCESS;
	}
	
	public String save() throws Exception {
		if(model!=null){
			SysNavDirectory temp = sysNavDirectoryService.getBoData(model.getId());
			if(temp== null){
				sysNavDirectoryService.addBoData(this.getModel());
			}else{
				sysNavDirectoryService.updateBoData(this.getModel());
			}
			ResponseUtil.write(SUCCESS);
		}
	    return null;
	}
	
	public String delete() throws Exception {
		sysNavDirectoryService.deleteBoData(this.getId());
		return SUCCESS;
	}
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if(id==null)id=sysNavDirectoryService.getMaxID();
		sysNavDirectoryService.moveUp(id.intValue());
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if(id==null)id=sysNavDirectoryService.getMaxID();
		sysNavDirectoryService.moveDown(id.intValue());
		return SUCCESS;
	}
	
	public PagerService getPagerService() {
		return pagerService;
	}
	public void setPagerService(PagerService pagerService) {
		this.pagerService = pagerService;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
	public Collection getAvailableItems() {
		return availableItems;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPagerMethod() {
		return pagerMethod;
	}
	public void setPagerMethod(String pagerMethod) {
		this.pagerMethod = pagerMethod;
	}
	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQueryMap() {
		return queryMap;
	}
	public void setQueryMap(String queryMap) {
		this.queryMap = queryMap;
	}
	public SysNavDirectory getModel() {
		return model;
	}
	public void setModel(SysNavDirectory model) {
		this.model = model;
	}
	public SysNavDirectoryService getSysNavDirectoryService() {
		return sysNavDirectoryService;
	}
	public void setSysNavDirectoryService(SysNavDirectoryService sysNavDirectoryService) {
		this.sysNavDirectoryService = sysNavDirectoryService;
	}


	public SysSequenceService getSysSequenceService() {
		return sysSequenceService;
	}

	public void setSysSequenceService(SysSequenceService sysSequenceService) {
		this.sysSequenceService = sysSequenceService;
	}
}
