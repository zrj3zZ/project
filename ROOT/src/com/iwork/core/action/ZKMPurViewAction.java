package com.iwork.core.action;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iwork.core.organization.action.OrgCompanyAction;
import com.iwork.core.server.ZKMPurViewService;
import com.iwork.core.util.ResponseUtil;

import com.opensymphony.xwork2.ActionSupport;

public class ZKMPurViewAction extends ActionSupport { 
	private ZKMPurViewService zKMPurViewService;
	private static final Logger logger = LoggerFactory.getLogger(OrgCompanyAction.class);
	private String id; 
	private Long pid;           //授权主体 ID
	private Long purviewType;   //0 按部门授权 ,1 按人员授权
	private String purviewGroup;     //‘folder’则表示在jqGrid中显示文件夹下内容，‘doc’则表示在jqGrid中只显示该文件       
	private String purviewIdList;     //授权ID列表
	private String purviewHTML;     //授权ID列表
	private String purviewBtn;     
	private Long type;    //1:管理授权/  0:浏览授权
	private String searchOrg;
	/**
	 * 显示权限列表
	 * @return 
	 */
	public String purviewList(){
		 if(purviewGroup!=null&&id!=null){  
			 purviewBtn =  zKMPurViewService.loadPurviewBtn(Long.parseLong(id), purviewGroup);
			 purviewHTML = zKMPurViewService.getPurviewList(purviewGroup,Long.parseLong(id));
		 }
		return SUCCESS;
	}
	
	/**
	 * 显示用户树
	 * @return
	 */
	public String purviewTree(){
		if(id!=null){
			
		}
		return SUCCESS;
	}
	/**
	 * 加载组织授权JSON
	 */
	public void showOrgPurviewJson(){
		String json ="";
		if(pid!=null&&purviewType!=null&&id==null){  
			json = zKMPurViewService.getCompanyNodeJson(purviewGroup,purviewType,type, pid,searchOrg); 
		}else if(pid!=null&&purviewType!=null&&id!=null){   
			json = zKMPurViewService.getDeptAndUserJson(Long.parseLong(id),purviewGroup,purviewType,type,pid,searchOrg);
		}
		ResponseUtil.write(json);
	}
	
	/**
	 * 设置知识权限
	 */
	public void setPurview(){
		if(purviewType!=null&&purviewGroup!=null&&purviewIdList!=null&&type!=null&&pid!=null){
			zKMPurViewService.addPurview(purviewGroup, purviewType, purviewIdList,type,pid); 
		} 
		ResponseUtil.write(SUCCESS);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getPurviewType() {
		return purviewType;
	}
	public void setPurviewType(Long purviewType) {
		this.purviewType = purviewType;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}

	
	public ZKMPurViewService getzKMPurViewService() {
		return zKMPurViewService;
	}

	public void setzKMPurViewService(ZKMPurViewService zKMPurViewService) {
		this.zKMPurViewService = zKMPurViewService;
	}

	public String getPurviewGroup() {
		return purviewGroup;
	}
	public void setPurviewGroup(String purviewGroup) {
		this.purviewGroup = purviewGroup;
	}

	public String getPurviewIdList() {
		return purviewIdList;
	}

	public void setPurviewIdList(String purviewIdList) {
		this.purviewIdList = purviewIdList;
	}

	public String getPurviewHTML() {
		return purviewHTML;
	}

	public void setPurviewHTML(String purviewHTML) {
		this.purviewHTML = purviewHTML;
	}

	public String getPurviewBtn() {
		return purviewBtn;
	}
	public void setPurviewBtn(String purviewBtn) {
		this.purviewBtn = purviewBtn;
	}

	public String getSearchOrg() {
		return searchOrg;
	}

	public void setSearchOrg(String searchOrg) {
		this.searchOrg = searchOrg;
	}
	
	
}
