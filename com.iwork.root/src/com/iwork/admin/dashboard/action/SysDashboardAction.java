package com.iwork.admin.dashboard.action;

import com.iwork.admin.dashboard.service.SysDashboardService;
import com.iwork.admin.util.AdminUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;


public class SysDashboardAction extends ActionSupport {
	public String input ;
	public String html ;
	 private String id;
	 private String portletid;
	 private String ids;
	 private String title;
	 private String style;
	 private String columnid;
	 private String orderlist;
	 private String groupname;
	public SysDashboardService sysDashboardService;
	
	/**
	 * 加载控制台首页
	 * @return
	 */
	public String index(){
		if(AdminUtil.getInstance().isManagerLogin()){
			html = sysDashboardService.getPortletsHtml();
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	/**
	 * 首页看版
	 * @return
	 */
	public String showDesign(){
		
		return SUCCESS;
	}
	
	/**
	 * 获得树视图
	 */
	public void showDesignJSON(){
		String json = sysDashboardService.showOverAllTreeJSON();
		ResponseUtil.write(json);
	}
	

	 /**
	  * 移动portlet
	  */
	 public void movePortlet(){
		 if(orderlist!=null){
			 sysDashboardService.movePortlet(orderlist);
		 }   
		 ResponseUtil.write(SUCCESS);
	 }
	 
	 /**
	  * 更新portlet
	  */
	 public void updatePortlet(){
		 if(portletid!=null&&title!=null&&style!=null){
			 sysDashboardService.updatePortlet(portletid, title, style);
		 }     
		 ResponseUtil.write(SUCCESS);
	 }
	 
	 
	 /**
	  * 移除portlet
	  */
	 public void removePortlet(){
		 if(orderlist!=null&&portletid!=null){
			 boolean flag =  sysDashboardService.removePortlet(portletid,orderlist);
				if(flag)
					ResponseUtil.write(SUCCESS);
				else
					 ResponseUtil.write(ERROR);
		 }else{
			 ResponseUtil.write(ERROR);
		 }
		
	 }
	
	 /**
		 * 保存设置
		 */
	 public void setPortlet(){
		 String msg = "";
		 if(ids!=null){
			 sysDashboardService.setPortletList(ids);
			 msg=SUCCESS; 
		 }else{
			 msg = ERROR;
		 }
		 ResponseUtil.write(msg);
	 }
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public void setSysDashboardService(SysDashboardService sysDashboardService) {
		this.sysDashboardService = sysDashboardService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortletid() {
		return portletid;
	}
	public void setPortletid(String portletid) {
		this.portletid = portletid;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getColumnid() {
		return columnid;
	}
	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}
	public String getOrderlist() {
		return orderlist;
	}
	public void setOrderlist(String orderlist) {
		this.orderlist = orderlist;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
}
