package com.iwork.plugs.resoucebook.action;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.service.ResourceBookBaseInfoService;
import com.opensymphony.xwork2.ActionSupport;

public class ResourceBookBaseInfoAction extends ActionSupport {
	private ResourceBookBaseInfoService resourceBookBaseInfoService;
	private Long spaceId;
	private String spacename;
	private Long id;
	private String ids;
	private IworkRmBase model;
	public String index() {
		
		return SUCCESS;
	}
	
	/**
	 * 进入页面查询空间列表并显示
	 * 
	 * @return
	 */
	public void showJson() {
		if(spaceId!=null){
			String json = "";
			json = resourceBookBaseInfoService.showListJson(spaceId);
			ResponseUtil.write(json);
		}
	}
	/**
	 * 新增空间
	 * 
	 * @return
	 */
	public String add() {
		if(spaceId!=null&&spacename!=null){
			model = new IworkRmBase();
			model.setStatus(new Long(1));
			model.setSpaceid(spaceId);
			model.setSpacename(spacename);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存空间
	 * 
	 * @return
	 */
	public void save() {
		String msg = "error";
		if(model!=null){
			resourceBookBaseInfoService.save(model);
			msg = SUCCESS;
		}
		ResponseUtil.write(msg);
	}

	/**
	 * 删除空间
	 * 
	 * @return
	 */
	public void remove() {
		String msg = "error";
		if(ids!=null){
			resourceBookBaseInfoService.remove(ids);
			msg = SUCCESS;
		}
		ResponseUtil.write(msg);
	}

	/**
	 * 修改空间
	 * 
	 * @return
	 */
	public String edit() {
		if (id!=null) {
			model = resourceBookBaseInfoService.getSpaceManageDao().getBaseModel(id);
		}
		return SUCCESS;
	}
 

	public ResourceBookBaseInfoService getResourceBookBaseInfoService() {
		return resourceBookBaseInfoService;
	}
	public void setResourceBookBaseInfoService(
			ResourceBookBaseInfoService resourceBookBaseInfoService) {
		this.resourceBookBaseInfoService = resourceBookBaseInfoService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IworkRmBase getModel() {
		return model;
	}

	public void setModel(IworkRmBase model) {
		this.model = model;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
