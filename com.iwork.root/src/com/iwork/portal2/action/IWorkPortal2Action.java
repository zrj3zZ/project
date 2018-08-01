package com.iwork.portal2.action;


import java.util.List;
import com.iwork.commons.util.UtilCode;
import com.iwork.core.util.ResponseUtil;
import com.iwork.portal.constans.PortalConstant;
import com.iwork.portal.model.IworkPortalOverall;
import com.iwork.portal2.service.IWorkPortal2Service;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkPortal2Action extends ActionSupport {
	 private IWorkPortal2Service iworkPortal2Service;
	 private String tablist;
	 private List<IworkPortalOverall> list;
	 private IworkPortalOverall overallModel;
	 private String id;
	 private Long portletid;
	 private String ids;
	 private String title;
	 private String style;
	 private String columnid;
	 private String orderlist;
	 private String groupname;
	 private String columnHTML;
	 private String[] groupList;
	 
	 /** 
	  * 获得个人门户首页
	  * @return
	  */
	 public String personIndex(){
		 columnHTML = iworkPortal2Service.getColumnHTML();
		 return SUCCESS;  
	 }
	 
	 
	 
	 /**
	  * 加载个人设置树视图
	  * @return
	  */
	 public String showset(){
		 return SUCCESS;
	 }
	 /**
	  * 获得树JSON
	  */
	 public void treesetJSON(){
		 String json = iworkPortal2Service.showOverAllTreeJSON();
		 ResponseUtil.write(json);
	 }
	 
	 /**
	  * 移动portlet
	  */
	 public void movePortlet(){
		 if(columnid!=null&&portletid!=null&&orderlist!=null){
			 iworkPortal2Service.movePortlet(Long.parseLong(columnid), portletid, orderlist);
		 }   
		 ResponseUtil.write(SUCCESS);
	 }
	  
	 /**
	  * 更新portlet
	  */
	 public void updatePortlet(){
		 if(portletid!=null&&title!=null&&style!=null){
			 iworkPortal2Service.updatePortlet(portletid, title, style);
		 }     
		 ResponseUtil.write(SUCCESS);
	 }
	 
	 
	 /**
	  * 移除portlet
	  */
	 public void removePortlet(){
		boolean flag =  iworkPortal2Service.removePortlet(portletid);
		if(flag)
			ResponseUtil.write(SUCCESS);
		else
			 ResponseUtil.write(ERROR);
	 }
	 /**
	  * 获得通用门户组件
	  * @return 
	  */
	 public String overallIndex(){
		 if(groupname!=null){
			 groupname = UtilCode.convert2UTF8(groupname);
		 }
		 tablist = iworkPortal2Service.getTabList(groupname);
		 list = iworkPortal2Service.getIworkPortal2DAO().getOverAllList(groupname);
		 return SUCCESS;  
	 }
	 /**
	  * 设置门户组件列表
	  */
	 public void setPortlet(){
		 String msg = "";
		 if(ids!=null){
			 iworkPortal2Service.setPortletList(ids);
			 msg=SUCCESS; 
		 }else{
			 msg = ERROR;
		 }
		 ResponseUtil.write(msg);
	 }
	 /**
	  * 添加通用门户组件
	  * @return
	  */
	 public String overallAdd(){
		 groupList = PortalConstant.overallGroupList;
		 return SUCCESS;
	 }
	 /**
	  * 添加通用门户组件
	  * @return 
	  */
	 public String overallEdit(){
		 if(id!=null){
			 groupList = PortalConstant.overallGroupList;
			 overallModel = iworkPortal2Service.getIworkPortal2DAO().getOverAllModel(Long.valueOf(id));
		 }
		 return SUCCESS;
	 }
	 /**
	  * 保存通用门户组件
	  * @return
	  */
	 public void overallSave(){
		 String msg = SUCCESS;
		 if(overallModel!=null){
			 iworkPortal2Service.saveOverAllModel(overallModel);
		 } 
		 ResponseUtil.write(msg); 
	 }
	 /**
	  * 保存通用门户组件
	  * @return
	  */
	 public void overallDel(){
		 String msg = "";
		 if(id!=null){ 
			 iworkPortal2Service.removeOverAllModel(Long.valueOf(id));
			 msg = SUCCESS; 
		 }else{ 
			 msg = ERROR;
		 }
		 ResponseUtil.write(msg); 
	 }
	 

	

	public IWorkPortal2Service getIworkPortal2Service() {
		return iworkPortal2Service;
	}



	public void setIworkPortal2Service(IWorkPortal2Service iworkPortal2Service) {
		this.iworkPortal2Service = iworkPortal2Service;
	}



	public String getTablist() {
		return tablist;
	}

	public void setTablist(String tablist) {
		this.tablist = tablist;
	}

	public List<IworkPortalOverall> getList() {
		return list;
	}

	public void setList(List<IworkPortalOverall> list) {
		this.list = list;
	}
	public IworkPortalOverall getOverallModel() {
		return overallModel;
	}
	public void setOverallModel(IworkPortalOverall overallModel) {
		this.overallModel = overallModel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String[] getGroupList() {
		return groupList;
	}
	public void setGroupList(String[] groupList) {
		this.groupList = groupList;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getColumnHTML() {
		return columnHTML;
	}
	public void setColumnHTML(String columnHTML) {
		this.columnHTML = columnHTML;
	}

	public Long getPortletid() {
		return portletid;
	}

	public void setPortletid(Long portletid) {
		this.portletid = portletid;
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

	
	
}
