package com.iwork.plugs.email.action;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.email.model.IworkMailGrouplist;
import com.iwork.plugs.email.service.IWorkMailGroupService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailGroupAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private IWorkMailGroupService iWorkMailGroupService;
	private String html; 
    private String title; 
    private String userlist; 
    private String desc;
    private Long id;

    
    /**
     * 通讯录
     * @return
     */
    public String booklist(){
    	html = iWorkMailGroupService.getGroupList();
    	return SUCCESS;
    }
    /**
     * 通讯录新增
     * @return
     */
    public String booklistAdd(){
    	if(id!=null){
    		IworkMailGrouplist model = iWorkMailGroupService.getiWorkMailGroupDAO().getGroupListModel(id);
    		if(model!=null){
    			title  = model.getGroupTitle();
    			id = model.getId();
    			desc = model.getGroupDesc();
    		}
    	}
    	return SUCCESS;
    }
    
   
    /**
     * 保存通讯录分组
     */
    public void booklistSave(){
    	if(title!=null&&desc!=null&&userlist!=null){
    		iWorkMailGroupService.saveGroupList(id,title, desc, userlist);
    		ResponseUtil.write(SUCCESS);
    	}
    }
    /**
     * 加载分组明细
     */
    public void showBooksublist(){
    	if(id!=null){
    		String html = iWorkMailGroupService.getGroupSubList(id);
    		ResponseUtil.write(html);
    	}
    }
    /**
     * 移除分组明细
     */
    public void removeItem(){
    	if(id!=null){
    		boolean flag = iWorkMailGroupService.groupRemove(id);
    		if(flag){
    			ResponseUtil.write(SUCCESS);
    		}else{
    			ResponseUtil.write(ERROR);
    		}
    	}
    }
    /**
     * 移除分组明细
     */
    public void removeSubItem(){
    	if(id!=null){
    		boolean flag = iWorkMailGroupService.groupSubRemove(id);
    		if(flag){
    			ResponseUtil.write(SUCCESS);
    		}else{
    			ResponseUtil.write(ERROR);
    		}
    	}
    }
	
	
	public void setiWorkMailGroupService(IWorkMailGroupService iWorkMailGroupService) {
		this.iWorkMailGroupService = iWorkMailGroupService;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserlist() {
		return userlist;
	}
	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public IWorkMailGroupService getiWorkMailGroupService() {
		return iWorkMailGroupService;
	}
	
}
