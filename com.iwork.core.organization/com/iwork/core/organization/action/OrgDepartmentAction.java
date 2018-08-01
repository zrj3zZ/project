package com.iwork.core.organization.action;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibpmsoft.project.zqb.service.ZqbUpdateDataService;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.commons.util.UtilCode;
import com.iwork.core.constant.DepartmentConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.service.OrgCompanyService;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OrgDepartmentAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgDepartmentAction.class);

	private OrgDepartmentService orgDepartmentService;
	private OrgCompanyService orgCompanyService;
	private PagerService pagerService;
	private ZqbUpdateDataService zqbUpdateDataService;
	
	private OrgCompany company;
	private OrgDepartment model;
	private Pager pager;
	protected String companyId;
	protected Collection availableItems;
	protected List<OrgCompany> companyList;
	protected String currentPage;
	protected String nodeType;
	protected String pagerMethod;
	protected String totalRows;
	protected Long id ;
	protected String queryMap;
	protected Long parentdeptid;
	protected String parentdeptname;
	protected String departmentid;
	protected String departmentname;
	
	protected String layer;
	protected String navTree;
	protected String searchkey;
	private String delInfo = "";
	protected String orderindex;
	private String searchOrg;
	public String getSearchOrg() {
		return searchOrg;
	}

	public void setSearchOrg(String searchOrg) {
		this.searchOrg = searchOrg;
	}

	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {	
		if(companyId!=null&&parentdeptid!=null){
			if(parentdeptid.equals(new Long(0))){
				companyList = orgCompanyService.getOrgCompanyDAO().getCompanyList(companyId);
			}
			int totalRow=orgDepartmentService.getRows(Integer.parseInt(this.getCompanyId()),this.getParentdeptid());
			pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
			this.setTotalRows(String.valueOf(totalRow));
			int companyid= Integer.parseInt(this.getCompanyId());
			int parentid = this.getParentdeptid().intValue();
			availableItems=orgDepartmentService.getBoDatas(companyid,parentid,pager.getPageSize(), pager.getStartRow());
		}
		return SUCCESS;	
	}
	
	/**
	 * 部门组织结构窗口
	 * @return
	 */
	public String miniIndex(){
		
		return SUCCESS;
	}
	/**
	 * 编辑加载子系统
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception {
		if(id!=null){
			model = orgDepartmentService.getBoData(id);
			companyId = model.getCompanyid();
			company = orgCompanyService.getBoData(companyId); 
			parentdeptid = model.getParentdepartmentid();
			OrgDepartment parentmodel = orgDepartmentService.getBoData(parentdeptid);
			if(parentmodel!=null){
				parentdeptname = parentmodel.getDepartmentname();
			}
			companyList = orgCompanyService.getAll();
		}
	    return SUCCESS;
	}
	
	/**
	 * 执行部门激活/注销
	 * @return
	 */
	public void disable(){
		String msg = "";
		if(id!=null){
			msg= orgDepartmentService.disable(id);
		}
		ResponseUtil.write(msg);
	}
	/**m
	 * 执行部门激活/注销
	 * @return
	 */
	public void doActive(){
		String msg = "";
		if(id!=null){
			msg = orgDepartmentService.doActive(id);
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 添加子系统
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		if(companyId==null)companyId = UserContextUtil.getInstance().getCurrentUserContext()._companyModel.getId();
		company = orgCompanyService.getBoData(companyId); 
		if(parentdeptname!=null)parentdeptname = UtilCode.convert2UTF8(parentdeptname);
		model = null;
		if(model==null)
		model = new OrgDepartment();
		orderindex = orgDepartmentService.getMaxOrderIndex();
		model.setOrderindex(orderindex);
	    return SUCCESS; 
	}
	
	public void save() throws Exception {
		String flag="";
		if(model!=null){
			if(model.getDepartmentname()!=null && !"".equals(model.getDepartmentname())){
				if(model.getDepartmentname().length()>64 || model.getDepartmentname().length()<2){
					flag="zLength";
				}else{
					Pattern p = Pattern.compile("[0-9A-Za-z\u4e00-\u9fa5\\（\\）]*");
					 if(!p.matcher(model.getDepartmentname()).matches()){
						 flag="zh";
					 }
				}
			}
			if("".equals(flag)){
				if(model.getId()==null){
					orgDepartmentService.addBoData(model);
					LogUtil.getInstance().addLog(model.getId(), "部门管理信息维护", "新增部门:部门名称为"+model.getDepartmentname());
				}else {
					OrgDepartment oldModel = orgDepartmentService.getBoData(model.getId());
					LogUtil.getInstance().addLog(model.getId(), "部门管理信息维护", "更新部门:部门名称由"+oldModel.getDepartmentname()+"变更为"+model.getDepartmentname());
					orgDepartmentService.updateBoData(model);
					if(zqbUpdateDataService==null){
						zqbUpdateDataService = (ZqbUpdateDataService)SpringBeanUtil.getBean("zqbUpdateDataService");
					}
					zqbUpdateDataService.updateDataDepartment(oldModel, model);
				}
			}
			if(!"".equals(flag)){
				 ResponseUtil.write(flag);
			}else{
				 ResponseUtil.write(SUCCESS);
			}
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	public void quickAdd(){
		if(companyId!=null&&parentdeptid!=null&&departmentid!=null&&departmentname!=null){
			String log = orgDepartmentService.quickAdd(companyId,parentdeptid, departmentid, departmentname);
			if(log.equals("")||log.equals(SUCCESS)){
				 ResponseUtil.write(SUCCESS);
			}else{
				 ResponseUtil.write(ERROR); 
			}
		}
	}
	/**
	 * 删除数据
	 * @return 
	 * @throws Exception
	 */
	public String delete() throws Exception {
		boolean flag = false;
		delInfo = "";//清空信息
		if(this.getId()!=null){
			OrgDepartment model  = orgDepartmentService.getBoData(this.getId());
			this.setModel(model);
			if(model!=null){
				String temp = orgDepartmentService.deleteBoData(model);
				if(temp.equals("deleteSuccess")){
					flag = true;	
					delInfo = "1"; 
				}else if(temp.equals("hasDept")){
					delInfo = "2";
					flag = false;
				}else if(temp.equals("hasUser")){
					delInfo = "5";
					flag = false;
				}
			}else{ 
				flag = false;
				delInfo = "3";
			}
		}else{
			flag = false;
			delInfo = "4";
		}
		if(flag){
			LogUtil.getInstance().addLog(model.getId(), "部门管理信息维护", "删除部门:部门名称为"+model.getDepartmentname());
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
	
	
	/**
	 * 加载导航树页面
	 * @return
	 * @throws Exception
	 */
	public String showTree() throws Exception{
		return SUCCESS;
	}
	
	
	/**
	 * 加载导航树的Json
	 * @return
	 * @throws Exception
	 */
	public void showTree_Json() throws Exception{
		boolean isRoot = false;
		if(id==null||id.equals("")){id = new Long(0);}
		if(nodeType==null)nodeType = "company"; 
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(companyId==null){
			companyId = uc.get_companyModel().getId(); 
			isRoot = true;
		} 
		String JsonData =orgDepartmentService.getTreeJson(companyId,id+"",nodeType,isRoot);
		ResponseUtil.write(JsonData);   
	}
	
	public void showTree_G() throws Exception{
		boolean isRoot = false;
		if(id==null||id.equals("")){id = new Long(0);}
		if(nodeType==null)nodeType = "company"; 
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(companyId==null){
			companyId = uc.get_companyModel().getId(); 
			isRoot = true;
		} 
		String JsonData =orgDepartmentService.getTreeJson(companyId,id+"","dept",isRoot,searchOrg);
		ResponseUtil.write(JsonData);   
	}
	/**
	 * 打开部门字典（单选）
	 * @param FieldName
	 * @return
	 */
	public String showDeptDictionaryRadio(){
		navTree = orgDepartmentService.getShowDeptDictionaryScript("radio",DepartmentConst.DEPARTMENT_TREE_TYPE1);
		return SUCCESS; 
	}
	/**
	 * 打开部门字典（多选 ）
	 * @param FieldName
	 * @return
	 */
	public String showDeptDictionaryCheckList(){
		navTree = orgDepartmentService.getNavTreeScript();
		return SUCCESS;
	}
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		delInfo = "";//清空信息
		if(id!=null){
		model=orgDepartmentService.getBoData(id);
		if(model!=null){
			int companyid= Integer.parseInt(model.getCompanyid());		
			int parentid = model.getParentdepartmentid().intValue();
			orgDepartmentService.moveUp(companyid,parentid,id.intValue());
		}
		}
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		delInfo = "";//清空信息
		if(id!=null){
			model=orgDepartmentService.getBoData(id);
			int companyid= Integer.parseInt(model.getCompanyid());		
			int parentid = model.getParentdepartmentid().intValue();
			orgDepartmentService.moveDown(companyid,parentid,id.intValue());
		}
		return SUCCESS;
	}
	/**
	 * 执行部门查询
	 * @return
	 * @throws Exception
	 */
	public String dosearch() throws Exception {
		if(searchkey!=null){
			if(checkLoginInfo(searchkey)){
	    		return ERROR;
	    	}
			availableItems=orgDepartmentService.getOrgDepartmentDAO().getSearchList(searchkey);
		} 
		int totalRow=0;
		if(availableItems!=null){
			totalRow=availableItems.size();
		}
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		int companyid= Integer.parseInt((this.getCompanyId()==null||"".equals(this.getCompanyId()))?"0":this.getCompanyId());
		int parentid = this.getParentdeptid()==null?0:this.getParentdeptid().intValue();
		return SUCCESS;
	}
	private static boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
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
	public OrgDepartment getModel() {
		return model;
	}
	public void setModel(OrgDepartment model) {
		this.model = model;
	}
	public OrgDepartmentService getOrgDepartmentService() {
		return orgDepartmentService;
	}
	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}

	public String getNavTree() {
		return navTree;
	}

	public void setNavTree(String navTree) {
		this.navTree = navTree;
	}

	public OrgCompanyService getOrgCompanyService() {
		return orgCompanyService;
	}

	public void setOrgCompanyService(OrgCompanyService orgCompanyService) {
		this.orgCompanyService = orgCompanyService;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List getCompanyList() {
		return companyList;
	}

	public Long getParentdeptid() {
		return parentdeptid;
	}

	public void setParentdeptid(Long parentdeptid) {
		this.parentdeptid = parentdeptid;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getParentdeptname() {
		return parentdeptname;
	}

	public void setParentdeptname(String parentdeptname) {
		this.parentdeptname = parentdeptname;
	}

	public String getDelInfo() {
		return delInfo;
	}

	public void setDelInfo(String delInfo) {
		this.delInfo = delInfo;
	}

	public String getSearchkey() {
		return searchkey;
	}
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public OrgCompany getCompany() {
		return company;
	}

	public void setCompany(OrgCompany company) {
		this.company = company;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getOrderindex() {
		return orderindex;
	}

	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	
}
