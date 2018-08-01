package com.iwork.app.navigation.operation.action;

import java.util.Collection;
import java.util.List;


import com.iwork.app.navigation.directory.service.SysNavDirectoryService;
import com.iwork.app.navigation.function.service.SysNavFunctionService;
import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.app.navigation.operation.service.SysNavOperationService;
import com.iwork.app.navigation.sys.service.SysNavSystemService;
import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.opensymphony.xwork2.ActionSupport;

public class SysNavOperationAction extends ActionSupport {
	private SysNavOperationService sysNavOperationService;
	private SysNavDirectoryService sysNavDirectoryService;
	private SysNavSystemService sysNavSystemService;
	private SysNavFunctionService sysNavFunctionService;
	private PagerService pagerService;
	
	private SysNavOperation model;
	private Pager pager;
	
	protected Collection availableItems;
	protected List systemList;
	protected List directoryList;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String id ;
	protected  String pid;
	protected String ptype;
	protected String navTree;
	protected String function;
	protected String nodeId;//树的节点ID
	protected String nodeType;//树的节点类型
	
	public String list() throws Exception {
		int totalRow=sysNavOperationService.getRows(this.getPtype(),this.getPid());
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		availableItems=sysNavOperationService.getBoDatas(this.getPtype(),this.getPid(),pager.getPageSize(), pager.getStartRow());
		directoryList =  sysNavDirectoryService.getAll(); //获得list 
		//#############################################################################################################################################
		return SUCCESS;		 
		//##############################################################################################################################################
	}
	
	/**
	 * 加载导航树
	 * @return
	 * @throws Exception
	 */
	public String showTree() throws Exception{
		return SUCCESS;
	}
	
	
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = sysNavOperationService.getBoData(id);
			directoryList = sysNavDirectoryService.getAll();
		}else{
			id=sysNavOperationService.getMaxID();
		}
	    return SUCCESS;
	}
	
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id=sysNavOperationService.getMaxID();
		
		model = null;
		model = new SysNavOperation();
		pid = this.getPid();
		ptype=this.getPtype();
		model.setPid(pid);
		model.setPtype(ptype);
		model.setId(id);
	    return SUCCESS;
	}
	/**
	 * 保存子系统信息
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		SysNavOperation model=sysNavOperationService.getBoData(this.getModel().getId());
		this.setPid(this.getModel().getPid());
		if(model == null){
			//this.getModel().set(this.getSysNavSystemid());
			sysNavOperationService.addBoData(this.getModel());
		}else{
			sysNavOperationService.updateBoData(this.getModel());
		}
		/*
		if(this.getQueryName()==null||this.getQueryValue()==null||this.getQueryName().equals("")||this.getQueryValue().equals("")){
			
		}else{
			queryMap=this.getQueryName()+"~"+this.getQueryValue();
		}*/
	    return SUCCESS;
	}
	/**
	 * 删除子系统信息
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		sysNavOperationService.deleteBoData(this.getId());
		return SUCCESS;
	}
	
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id=sysNavOperationService.getMaxID();
		sysNavOperationService.moveUp(Integer.parseInt(id));
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id=sysNavOperationService.getMaxID();
		sysNavOperationService.moveDown(Integer.parseInt(id));
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
	
	public SysNavOperation getModel() {
		return model;
	}
	public void setModel(SysNavOperation model) {
		this.model = model;
	}
	public SysNavFunctionService getSysNavFunctionService() {
		return sysNavFunctionService;
	}
	public void setSysNavFunctionService(SysNavFunctionService sysNavFunctionService) {
		this.sysNavFunctionService = sysNavFunctionService;
	}

	public SysNavSystemService getSysNavSystemService() {
		return sysNavSystemService;
	}

	public List getSystemList() {
		return systemList;
	}

	public void setSysNavSystemService(SysNavSystemService sysNavSystemService) {
		this.sysNavSystemService = sysNavSystemService;
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

	public String getNavTree() {
		return navTree;
	}

	public void setNavTree(String navTree) {
		this.navTree = navTree;
	}
	public String getFunction() {
		return function;
	}

	public SysNavOperationService getSysNavOperationService() {
		return sysNavOperationService;
	}

	public void setSysNavOperationService(
			SysNavOperationService sysNavOperationService) {
		this.sysNavOperationService = sysNavOperationService;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

}
