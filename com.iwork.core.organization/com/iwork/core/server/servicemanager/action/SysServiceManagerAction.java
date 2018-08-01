package com.iwork.core.server.servicemanager.action;   

import java.util.Collection;
import java.util.Date;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.server.servicemanager.model.Sysservice;
import com.iwork.core.server.servicemanager.service.SysServiceManagerService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SysServiceManagerAction extends ActionSupport {
	private SysServiceManagerService sysServiceManagerService;
	private PagerService pagerService;	
	private Sysservice model;	
	private Pager pager;
	protected Collection availableItems;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	private boolean superManager;
	protected Long id ;
	//=======================================
	private Long parentid;
	private String servicename;
	private String servicekey;
	private String servicedesc;
	private Date startdate;
	private Date enddate;
	private Long status;
	private Long orderIndex;

	/**
	 * 获得服务列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		superManager = SecurityUtil.isSuperManager();
		int totalRow=sysServiceManagerService.getRows();
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		availableItems=sysServiceManagerService.getBoDatas(pager.getPageSize(), pager.getStartRow());
		return SUCCESS;		 
	}
	
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = sysServiceManagerService.getBoData(id);
		}
		return SUCCESS;
	}
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		model = new Sysservice();
		model.setStatus(new Long(0));
	    return SUCCESS;
	}
	/**
	 * 异步保存数据
	 * 
	 */
	public void save() throws Exception {
		if(model!=null){
			sysServiceManagerService.saveModel(model);
		}
		ResponseUtil.writeTextUTF8("保存成功！");
	}
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if(id!=null){
			sysServiceManagerService.moveUp(id);
		}
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if(id!=null){
			sysServiceManagerService.moveDown(id);
		}
		
		return SUCCESS;
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		if(id!=null){
			sysServiceManagerService.deleteBoData(this.getId());
		}
		return SUCCESS;
	}
	/**
	 * 启动
	 * @return
	 */
	public String startService(){
		if(id!=null){
			sysServiceManagerService.changeStatus(this.getId());
		}
		return SUCCESS;
	}
	/**
	 * 停止
	 * @return
	 */
	public String stopService(){
		if(id!=null){
			sysServiceManagerService.changeStatus(this.getId());
		}
		return SUCCESS;
	}
	//====================================================//
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

	public SysServiceManagerService getSysServiceManagerService() {
		return sysServiceManagerService;
	}
	public void setSysServiceManagerService(SysServiceManagerService sysServiceManagerService) {
		this.sysServiceManagerService = sysServiceManagerService;
	}
	public boolean getSuperManager() {
		return superManager;
	}

	public void setSuperManager(boolean superManager) {
		this.superManager = superManager;
	}
	//======================================

	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getServicekey() {
		return servicekey;
	}
	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}
	public String getServicedesc() {
		return servicedesc;
	}
	public void setServicedesc(String servicedesc) {
		this.servicedesc = servicedesc;
	}
	
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	public void setAvailableItems(Collection availableItems) {
		this.availableItems = availableItems;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Long getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}
	public Sysservice getModel() {
		return model;
	}
	public void setModel(Sysservice model) {
		this.model = model;
	}

}
