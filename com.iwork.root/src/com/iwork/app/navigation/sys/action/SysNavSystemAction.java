package com.iwork.app.navigation.sys.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.app.navigation.sys.service.SysNavSystemService;
import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.server.sequence.service.SysSequenceService;
import com.iwork.core.server.servicemanager.model.Sysservice;
import com.iwork.core.server.servicemanager.service.SysServiceManagerService;
import com.opensymphony.xwork2.ActionSupport;

public class SysNavSystemAction extends ActionSupport {
	private SysNavSystemService sysNavSystemService;
	private SysSequenceService sysSequenceService;
	private PagerService pagerService;
	private SysServiceManagerService sysServiceManagerService;
	
	private SysNavSystem model;
	private Pager pager;
	
	protected Collection availableItems;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String id ;
	protected String orderindex;
	protected String queryName;
	protected String queryValue;
	protected String searchName;
	protected String searchValue;
	protected String queryMap;
	protected List<String> serviceList;//可用服务列表
	
	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		if(queryMap ==null||queryMap.equals("")){
			
		}else{
			String[] str=queryMap.split("~");
			this.setQueryName(str[0]);
			this.setQueryValue(str[1]);
		}		
		int totalRow=sysNavSystemService.getRows(this.getQueryName(),this.getQueryValue());
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		//xlj 2015年7月6日15:32:20 修改，总是不能显示所有内容
		/*availableItems=sysNavSystemService.getBoDatas(this.getQueryName(),this.getQueryValue(),pager.getPageSize(), pager.getStartRow());*/
		availableItems=sysNavSystemService.getBoDatas(this.getQueryName(),this.getQueryValue(),totalRow, pager.getStartRow());
		this.setSearchName(this.getQueryName());
		this.setSearchValue(this.getQueryValue());
		this.setQueryName("");
		this.setQueryValue("");
		
		
		return SUCCESS;		 
	}	
	
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = sysNavSystemService.getBoData(id);
			//加载服务选项列表
			List<Sysservice> allService = sysServiceManagerService.getAll();
			List<String> serviceNameList = new ArrayList<String>();
			for(int i=0;i<allService.size();i++){
				String serviceName = allService.get(i).getServicename();
				serviceNameList.add(serviceName);
			}
			this.setServiceList(serviceNameList);
		}else{
			id=sysSequenceService.getSequence(AppContextConstant.SYS_NAV_SYSTEM);
			//加载服务选项列表
			List<Sysservice> allService = sysServiceManagerService.getAll();
			List<String> serviceNameList = new ArrayList<String>();
			for(int i=0;i<allService.size();i++){
				String serviceName = allService.get(i).getServicename();
				serviceNameList.add(serviceName);
			}
			this.setServiceList(serviceNameList);
		}
	    return SUCCESS;
	}
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id = sysSequenceService.getSequence(AppContextConstant.SYS_NAV_SYSTEM);
		model = null;
		//加载服务选项列表
		List<Sysservice> allService = sysServiceManagerService.getAll();
		List<String> serviceNameList = new ArrayList<String>();
		for(int i=0;i<allService.size();i++){
			String serviceName = allService.get(i).getServicename();
			serviceNameList.add(serviceName);
		}
		this.setServiceList(serviceNameList);
	    return SUCCESS;
	}
	
	public String save() throws Exception {
		SysNavSystem model=sysNavSystemService.getBoData(this.getModel().getId());
		if(model == null){
			sysNavSystemService.addBoData(this.getModel());
		}
		else
			sysNavSystemService.updateBoData(this.getModel());
		
		this.setQueryName(this.getQueryName());
		this.setQueryValue(this.getQueryValue());
	    return SUCCESS;
	}
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id=sysSequenceService.getSequence(AppContextConstant.SYS_NAV_SYSTEM);
			sysNavSystemService.moveUp(Integer.parseInt(id));
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id=sysSequenceService.getSequence(AppContextConstant.SYS_NAV_SYSTEM);
		sysNavSystemService.moveDown(Integer.parseInt(id));
		return SUCCESS;
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		sysNavSystemService.deleteBoData(this.getId());
		
		if(this.getQueryName()==null||this.getQueryValue()==null||this.getQueryName().equals("")||this.getQueryValue().equals("")){
			
		}else{
			queryMap=this.getQueryName()+"~"+this.getQueryValue();
		}
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryValue() {
		return queryValue;
	}
	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getQueryMap() {
		return queryMap;
	}
	public void setQueryMap(String queryMap) {
		this.queryMap = queryMap;
	}
	public SysNavSystem getModel() {
		return model;
	}
	public void setModel(SysNavSystem model) {
		this.model = model;
	}
	public SysNavSystemService getSysNavSystemService() {
		return sysNavSystemService;
	}
	public void setSysNavSystemService(SysNavSystemService sysNavSystemService) {
		this.sysNavSystemService = sysNavSystemService;
	}
	public String getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	public SysSequenceService getSysSequenceService() {
		return sysSequenceService;
	}
	public void setSysSequenceService(SysSequenceService sysSequenceService) {
		this.sysSequenceService = sysSequenceService;
	}

	public SysServiceManagerService getSysServiceManagerService() {
		return sysServiceManagerService;
	}

	public void setSysServiceManagerService(
			SysServiceManagerService sysServiceManagerService) {
		this.sysServiceManagerService = sysServiceManagerService;
	}

	public List<String> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

}
