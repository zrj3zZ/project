
package com.iwork.core.organization.action;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgRoleService;
import com.iwork.core.organization.service.OrgUserMapService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * @author David.Yang
 */
public class OrgUserMapAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgUserMapAction.class);
	private OrgUserMapService orgUserMapService;
	private OrgRoleService orgRoleService;
	private OrgDepartmentService orgDepartmentService;
	private PagerService pagerService;
	
	private OrgUserMap model;
	private Pager pager;
	protected String userid;
	protected Collection availableItems;
	protected List companyList;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String id ;
	protected String queryMap;
	
	protected String navTree;

	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		if(!"".equals(userid)&&userid!=null){
				int totalRow=orgUserMapService.getRows(userid);
				pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
				this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
				this.setTotalRows(String.valueOf(totalRow));
				userid = this.getUserid();
				availableItems=orgUserMapService.getOrgUserMapList(userid,100, pager.getStartRow());
		} 
		return SUCCESS;	
	}	
	
	/**
	 * 编辑加载用户信息
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = orgUserMapService.getUserModel(id);
			this.setUserid(model.getUserid());
		}else{
			id=orgUserMapService.getMaxID();
		}
		availableItems = orgRoleService.getAll();
	    return SUCCESS;
	} 
	
	
	/**
	 * 添加兼职信息信息
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id=orgUserMapService.getMaxID();
		userid = this.getUserid();
		availableItems = orgRoleService.getAll();
		this.setModel(null);
	    return SUCCESS;
	} 
	
	/**
	 * 保存兼职信息
	 * @return
	 * @throws Exception
	 */
	public void save() throws Exception {
		String id = this.getModel().getId();
		OrgUserMap model=orgUserMapService.getUserModel(id);
		if(model == null){
			orgUserMapService.addBoData(this.getModel());
		}else{
			orgUserMapService.updateBoData(this.getModel());
		}
		availableItems = orgRoleService.getAll();
		this.setUserid(this.getModel().getUserid());
		ResponseUtil.write("success");
	}
	
	/**
	 * 删除数据
	 * @return
	 * @throws Exception
	 */
	public void delete() throws Exception {
		OrgUserMap model=orgUserMapService.getUserModel(id);
		if(model!=null){
			userid = model.getUserid();
		}
		orgUserMapService.deleteBoData(this.getId());
		ResponseUtil.write(SUCCESS);
	}
	
	//###############################################################################
	//##             SET/GET方法
	//###############################################################################

	public OrgUserMapService getOrgUserService() {
		return orgUserMapService;
	}

	public void setOrgUserService(OrgUserMapService orgUserMapService) {
		this.orgUserMapService = orgUserMapService;
	}

	public PagerService getPagerService() {
		return pagerService;
	}

	public void setPagerService(PagerService pagerService) {
		this.pagerService = pagerService;
	}

	public OrgUserMap getModel() {
		return model;
	}

	public void setModel(OrgUserMap model) {
		this.model = model;
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

	public void setAvailableItems(Collection availableItems) {
		this.availableItems = availableItems;
	}

	public List getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List companyList) {
		this.companyList = companyList;
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

	public String getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(String queryMap) {
		this.queryMap = queryMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNavTree() {
		return navTree;
	}

	public void setNavTree(String navTree) {
		this.navTree = navTree;
	}

	public OrgDepartmentService getOrgDepartmentService() {
		return orgDepartmentService;
	}

	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}

	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public OrgUserMapService getOrgUserMapService() {
		return orgUserMapService;
	}

	public void setOrgUserMapService(OrgUserMapService orgUserMapService) {
		this.orgUserMapService = orgUserMapService;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
