package com.iwork.plugs.bgyp.action;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.bgyp.model.ShopCarModel;
import com.iwork.plugs.bgyp.service.IWorkbgypManageService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkbgypManageAction extends ActionSupport {
	private IWorkbgypManageService iworkbgypManageService;
	private String html;
	private String lbbh;
	private String typetab;
	private List<ShopCarModel> shoplist;
	private List<HashMap> list;
	private Long id;
	private Long instanceid;
	private String taskId;
	private String excutionId;
	private String djbh;
	private String content;
	/**
	 * 办公用品预定首页
	 * @return
	 */
	public String index(){
		html = iworkbgypManageService.showPage(lbbh,content);
		typetab = iworkbgypManageService.showTypeTabHTML(null, lbbh);
		return SUCCESS;
	} 
	/**
	 * 根据当前用户查找对应的办公用品领用记录
	 * @return
	 */
	public String selectTabs(){
		list = iworkbgypManageService.selectTabs();
		return SUCCESS;
	}
	/**
	 * 
	 */
	public String selectTabByDJBH(){
		html=iworkbgypManageService.selectTabByDJBH(djbh);
		return SUCCESS;
		
	}
	/**
	 * 搜索
	 * @return
	 */
	public String search(){
		html = iworkbgypManageService.showPage(lbbh,content);
		typetab = iworkbgypManageService.showTypeTabHTML(null, lbbh);
		return SUCCESS;
	}
	
	/**
	 * 购物车
	 * @return
	 */
	public String shopcar(){
		shoplist = iworkbgypManageService.getIworkbgypManageDao().getCurrentSelectList();
		return SUCCESS;
	}
	
	/**
	 * 添加购物项
	 * @return
	 */
	public String addshopItem(){
		if(id!=null){
			iworkbgypManageService.addShopCar(id);
		} 
		shoplist = iworkbgypManageService.getIworkbgypManageDao().getCurrentSelectList();
		return SUCCESS;
	}
	
	/**
	 * 打开流程页面
	 * @return
	 */
	public String showFlowPage(){ 	
		String actDefId = "BGYPSQLC:1:35306";
		Long formId = null;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		ProcessAPI processAPI = ProcessAPI.getInstance();
		formId = processAPI.getProcessDefaultFormId(actDefId);
		instanceid = processAPI.newInstance(actDefId,formId, userid);
		Task task = processAPI.newTaskId(instanceid);
		if(task!=null){
			taskId = task.getId();
			excutionId = task.getExecutionId();
		}  
		return SUCCESS;
	}
	/**
	 * 移除购物项
	 * @return
	 */
	public String remove(){
		if(id!=null){
			iworkbgypManageService.getIworkbgypManageDao().removeShopItem(id);
		} 
		shoplist = iworkbgypManageService.getIworkbgypManageDao().getCurrentSelectList();
		return SUCCESS;
	}
	
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getLbbh() {
		return lbbh;
	}
	public void setLbbh(String lbbh) {
		this.lbbh = lbbh;
	}
	public String getTypetab() {
		return typetab;
	}
	public void setTypetab(String typetab) {
		this.typetab = typetab;
	}
	
	public IWorkbgypManageService getIworkbgypManageService() {
		return iworkbgypManageService;
	}
	public void setIworkbgypManageService(
			IWorkbgypManageService iworkbgypManageService) {
		this.iworkbgypManageService = iworkbgypManageService;
	}


	public List<ShopCarModel> getShoplist() {
		return shoplist;
	}


	public void setShoplist(List<ShopCarModel> shoplist) {
		this.shoplist = shoplist;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getExcutionId() {
		return excutionId;
	}
	public void setExcutionId(String excutionId) {
		this.excutionId = excutionId;
	}
	public List<HashMap> getList() {
		return list;
	}
	public void setList(List<HashMap> list) {
		this.list = list;
	}
	public String getDjbh() {
		return djbh;
	}
	public void setDjbh(String djbh) {
		this.djbh = djbh;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
