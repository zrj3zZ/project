package com.iwork.core.organization.action;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;
import com.iwork.core.organization.service.OrgRoleService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OrgRoleAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgRoleAction.class);

	private OrgRoleService orgRoleService;
	private PagerService pagerService;
	
	private OrgRole model;
	private List<OrgRoleGroup> list;
	private OrgRoleGroup groupmodel;
	private Pager pager;
	
	protected Collection availableItems;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String id ;
	protected Long groupId ;
	protected String queryName;
	protected String queryValue;
	protected String searchName;
	protected String searchValue;
	protected String queryMap;
	protected String tab_html;
	protected String inputName;
	protected String inputTitle;
	
	/**
	 * 加载系统导航列表
	 * @return
	 */
	public String showNavSystemItem(){
		
		 
		return "";
	}
	
	/**
	 * 加载角色树ｊｓｏｎ
	 */
	public void showRoleTreeJson(){
		String json = orgRoleService.getRoleTreeJson();
		ResponseUtil.write(json);
	}
	
	/** 
	 * 添加分组
	 * @return
	 */
	public String addGroup(){  
		return SUCCESS;			
	}
	/**
	 * 编辑分组
	 * @return
	 */
	public String editGroup(){  
		if(groupId!=null){
			groupmodel = orgRoleService.getOrgRoleDAO().getRoleGroupModel(groupId);
		}
		return SUCCESS;			
	}
	/**
	 * 编辑分组
	 * @return
	 */
	public void delGroup(){  
		String msg = "";
		if(groupId!=null){
			msg = orgRoleService.delGroup(groupId);
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 保存分组
	 * @return
	 */
	public void saveGroup(){
		String msg = "";
		if(groupmodel!=null){
			orgRoleService.getOrgRoleDAO().saveGroup(groupmodel);
			msg = SUCCESS;
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {	
		//获得分组列表
		tab_html = orgRoleService.getTabList(groupId);
		availableItems=orgRoleService.getOrgRoleDAO().getRoleList(groupId);
		return SUCCESS;		 
	}	
	
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		list = orgRoleService.getOrgRoleDAO().getRoleGroupList();
		if(id!=null)
			model = orgRoleService.getBoData(id);
		else
			id=orgRoleService.getMaxID();
	    return SUCCESS;
	}
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id=orgRoleService.getMaxID();
		list = orgRoleService.getOrgRoleDAO().getRoleGroupList();
		model = null; 
	    return SUCCESS;
	}
	public String save() throws Exception {
		OrgRole orgModel=orgRoleService.getBoData(this.getModel().getId());
		if(orgModel == null)
			orgRoleService.addBoData(this.getModel());
		else
			orgRoleService.updateBoData(this.getModel());
		
		this.setQueryName(this.getQueryName());
		this.setQueryValue(this.getQueryValue());
		ResponseUtil.write(SUCCESS);
	    return null; 
	}
	
	public String delete() throws Exception {
		orgRoleService.deleteBoData(this.getId());
		
		if(this.getQueryName()==null||this.getQueryValue()==null||this.getQueryName().equals("")||this.getQueryValue().equals("")){
			
		}else{
			queryMap=this.getQueryName()+"~"+this.getQueryValue();
		}
		return SUCCESS;
	}
	
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id = orgRoleService.getMaxID();
		OrgRole model = orgRoleService.getBoData(id);
		this.setModel(model);
		orgRoleService.moveUp(Integer.parseInt(id));
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id = orgRoleService.getMaxID();
		OrgRole model = orgRoleService.getBoData(id);
		this.setModel(model);
		orgRoleService.moveDown(Integer.parseInt(id));
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
	public OrgRole getModel() {
		return model;
	}
	public void setModel(OrgRole model) {
		this.model = model;
	}
	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}
	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}
	public OrgRoleGroup getGroupmodel() {
		return groupmodel;
	}
	public void setGroupmodel(OrgRoleGroup groupmodel) {
		this.groupmodel = groupmodel;
	}
	public String getTab_html() {
		return tab_html;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public List<OrgRoleGroup> getList() {
		return list;
	}
	public void setList(List<OrgRoleGroup> list) {
		this.list = list;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String inputTitle) {
		this.inputTitle = inputTitle;
	} 

}
