package com.iwork.core.organization.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.runtime.CheckInfo;
import com.iwork.core.organization.model.CompanyModel;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.service.OrgCompanyService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
public class OrgCompanyAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgCompanyAction.class);
	private OrgCompanyService orgCompanyService;
	private PagerService pagerService;
	
	private OrgCompany model;
	private List<CompanyModel> list;
	private Pager pager;
	 
	protected Collection availableItems;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String parentid;
	protected String parentname;
	protected String id ;
	protected String queryName;
	protected String queryValue;
	protected String searchName;
	protected String q;
	protected String searchValue;
	protected String queryMap;
	protected String companyName;
	protected String companyCode;
	protected String departmentid;
	protected String departmentname;
	protected String companyType;
	
	protected boolean isAdd;
	protected boolean isDel;
	protected boolean isEdit;
	private String delInfo;
	

	/**
	 * 获得组织列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		int totalRow=0;
		totalRow = orgCompanyService.getRows(this.getQueryName(),this.getQueryValue());
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		availableItems=orgCompanyService.getBoDatas(parentid,pager.getPageSize(), pager.getStartRow());
		this.setSearchName(this.getQueryName());
		this.setSearchValue(this.getQueryValue());
		this.setQueryName("");
		this.setQueryValue("");
		return SUCCESS; 		 
	}
	public String queryList() throws Exception{
		int totalRow=0;
		totalRow = orgCompanyService.getRows(this.getQueryName(),this.getQueryValue());
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		availableItems=orgCompanyService.getBoDatas(parentid,pager.getPageSize(), pager.getStartRow());
		this.setSearchName(this.getQueryName());
		this.setSearchValue(this.getQueryValue());
		this.setQueryName("");
		this.setQueryValue("");
		return SUCCESS;
	}
	
	public void showTreeJson(){
		String json = orgCompanyService.getTreeJson(id);
		ResponseUtil.write(json);
	}
	
	/**
	 * 上市公司树
	 * zouyalei  2015-01-23
	 */
	public void ssShowTreeJson(){
		String type = "SS";
		String json = orgCompanyService.getSSorFSSTreeJson(id,null,type);
		ResponseUtil.write(json);
	}
	
	/**
	 * 非上市公司树
	 * zouyalei  2015-01-23
	 */
	public void fssShowTreeJson(){
		String type = "FSS";
		String json = orgCompanyService.getSSorFSSTreeJson(id,null,type);
		ResponseUtil.write(json);
	}
	
	/**
	 * 加载指定组织机构对象
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = orgCompanyService.getBoData(id);
			if(model!=null){
				parentid = model.getParentid();
			}
		}else
			id=orgCompanyService.getMaxID();
		
		list = CheckInfo.getInstance().getCompanyList();
	    return SUCCESS;
	}
	 
	/**
	 * 添加
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		model = null;
		if(parentid!=null){
			model = new OrgCompany();
			model.setParentid(parentid);
			model.setId(id=orgCompanyService.getMaxID());
			OrgCompany parentModel = orgCompanyService.getBoData(parentid);
			if(parentModel!=null){
				parentname = parentModel.getCompanyname();
			}
		}
		list = CheckInfo.getInstance().getCompanyList();
	    return SUCCESS;
	}
	
	/**
	 * 保存组织
	 * @throws Exception
	 */
	public void save() throws Exception {
		String msg = "";
		if(model!=null){
				msg = orgCompanyService.saveCompany(model);
		}
	    ResponseUtil.write(msg) ; 
	}
	
	/**
	 * 确认添加公司
	 * @return
	 */
	public String confirmAddCompany(){
		if(companyName!=null){
			if(companyName.indexOf("-")>0){
				companyName = companyName.substring(0,companyName.indexOf("-"));
				Map params = new HashMap();
				params.put(1, companyName);
				companyCode=DBUtil.getDataStr("organid", "select organid from bd_ge_orgcompanyinfo where name = ? ", params);
			}
			//获取部门编号
			
		}
		return SUCCESS;
	}
	/**
	 * 快速添加组织机构
	 * @throws Exception
	 */
	public void quickAddCompany() throws Exception {
		String log = "";
		if(parentid!=null&&companyName!=null&&companyCode!=null){
			log = orgCompanyService.quickAddCompany(parentid,companyName,companyCode,companyType);
		}
		if(log.equals("")){
			ResponseUtil.write(SUCCESS) ; 
		}else{
			ResponseUtil.write(log) ;
		}
		
	}
	
	
	
	public String delete() throws Exception {
		delInfo = "";
		if(this.getQueryName()==null||this.getQueryValue()==null||this.getQueryName().equals("")||this.getQueryValue().equals("")){
			
		}else{
			queryMap=this.getQueryName()+"~"+this.getQueryValue();
		}
		boolean flag = orgCompanyService.hasDepartment(this.getId());
		model = orgCompanyService.getBoData(this.getId());
		this.setModel(model);
		if(flag){
			delInfo = "2";
			return ERROR;
		}else{
			delInfo = "1";
			orgCompanyService.deleteBoData(this.getId());
			return SUCCESS;			
		}
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id=orgCompanyService.getMaxID();
		orgCompanyService.moveUp(Integer.parseInt(id));
		return SUCCESS;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id=orgCompanyService.getMaxID();
		orgCompanyService.moveDown(Integer.parseInt(id));
		return SUCCESS;
	}
	
	/**
	 * 公司信息检索
	 */
	public void searchCompany(){ 
		/**/   		HttpServletRequest request = ServletActionContext.getRequest();
		/**/   		HashMap params = (HashMap)request.getParameterMap(); 
		if(q!=null&&!q.equals("")){
			String json = orgCompanyService.search(q);
			ResponseUtil.write(json);
		}
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
	public OrgCompany getModel() {
		return model;
	}
	public void setModel(OrgCompany model) {
		this.model = model;
	}
	public OrgCompanyService getOrgCompanyService() {
		return orgCompanyService;
	}
	public void setOrgCompanyService(OrgCompanyService orgCompanyService) {
		this.orgCompanyService = orgCompanyService;
	}
	public String getDelInfo() {
		return delInfo;
	}
	public void setDelInfo(String delInfo) {
		this.delInfo = delInfo;
	}

	public List<CompanyModel> getList() {
		return list;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
	
		this.companyType = companyType;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	

}
