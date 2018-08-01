package com.iwork.app.navigation.function.action;

import java.util.ArrayList;
import java.util.List;


import com.iwork.app.navigation.directory.cache.SysNavDirectoryCache;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.app.navigation.directory.service.SysNavDirectoryService;
import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.app.navigation.function.service.SysNavFunctionService;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SysNavFunctionAction extends ActionSupport {
	private SysNavDirectoryService sysNavDirectoryService;
	private SysNavFunctionService sysNavFunctionService;
	private PagerService pagerService;
	private Sysnavfunction model;
	private Pager pager;
	protected List<Sysnavfunction> list;
	protected List directoryList;
	protected String currentPage;
	protected String pagerMethod;
	protected String directoryName;
	protected String totalRows;
	protected String id ;
	protected Long directoryId ;
	
	public String list() {
		if(directoryId!=null){
			SysNavDirectory dirModel = sysNavDirectoryService.getBoData(directoryId);
			if(dirModel!=null){
				directoryName = dirModel.getDirectoryName();
			}  
			list=sysNavFunctionService.getFunctionList(directoryId);
		} 
		return SUCCESS;		 
	}
	/**
	 * 加载导航树
	 * @return
	 * @throws Exception
	 */
	public String showTree() {
		return SUCCESS;
	}
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load()  {
		if(id!=null){
			model = sysNavFunctionService.getBoData(Long.parseLong(id));
			SysNavDirectory sysNavDirectory = sysNavDirectoryService.getBoData(model.getDirectoryId()); 
			directoryList = sysNavDirectoryService.getDirectoryList();
		}else{
			id=sysNavFunctionService.getMaxID();
		} 
	    return SUCCESS;
	}
	
	/**
	 * 添加模块菜单
	 * @return
	 * @throws Exception
	 */
	public String addItem() {
		id=sysNavFunctionService.getMaxID();
		SysNavDirectory sysNavDirectory = (SysNavDirectory)SysNavDirectoryCache.getInstance().getModel(directoryId);
		if(sysNavDirectory==null){
			sysNavDirectory = sysNavDirectoryService.getBoData(directoryId);
		}
		if(sysNavDirectory!=null){
			directoryId = sysNavDirectory.getId();
		}
		List list = new ArrayList();
		list.add(sysNavDirectory);
		this.setDirectoryList(list);
		model = null;
	    return SUCCESS;
	}
	/**
	 * 保存子系统信息
	 * @return
	 * @throws Exception
	 */
	public String save() {
		if(model!=null){
			Sysnavfunction temp=sysNavFunctionService.getBoData(model.getId());
			if(temp == null){
				//this.getModel().set(this.getSysNavSystemid());
				sysNavFunctionService.addBoData(this.getModel());
			}else{
				sysNavFunctionService.updateBoData(this.getModel());
			}
		}
		ResponseUtil.write(SUCCESS);
	    return null;
	}
	/**
	 * 删除子系统信息
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		sysNavFunctionService.deleteBoData(Long.parseLong(id));
		ResponseUtil.write(SUCCESS);
	    return null;
	}
	
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id=sysNavFunctionService.getMaxID();
		sysNavFunctionService.moveUp(Integer.parseInt(id));
		ResponseUtil.write(SUCCESS);
	    return null;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id=sysNavFunctionService.getMaxID();
		sysNavFunctionService.moveDown(Integer.parseInt(id));
		ResponseUtil.write(SUCCESS); 
	    return null;
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
	
	public List<Sysnavfunction> getList() {
		return list;
	}

	public void setList(List<Sysnavfunction> list) {
		this.list = list;
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
	public Sysnavfunction getModel() {
		return model;
	}
	public void setModel(Sysnavfunction model) {
		this.model = model;
	}
	public SysNavFunctionService getSysNavFunctionService() {
		return sysNavFunctionService;
	}
	public void setSysNavFunctionService(SysNavFunctionService sysNavFunctionService) {
		this.sysNavFunctionService = sysNavFunctionService; 
	}
	public SysNavDirectoryService getSysNavDirectoryService() {
		return sysNavDirectoryService;
	}

	public void setSysNavDirectoryService(
			SysNavDirectoryService sysNavDirectoryService) {
		this.sysNavDirectoryService = sysNavDirectoryService;
	}

	public List getDirectoryList() {
		return directoryList;
	}

	public void setDirectoryList(List directoryList) {
		this.directoryList = directoryList;
	}
	public Long getDirectoryId() {
		return directoryId;
	}

	public void setDirectoryId(Long directoryId) {
		this.directoryId = directoryId;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	
}
