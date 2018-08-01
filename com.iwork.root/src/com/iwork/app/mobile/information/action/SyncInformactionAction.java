package com.iwork.app.mobile.information.action;

import com.iwork.app.mobile.information.service.SyncInformactionService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 移动端新闻资讯Action
 * 
 * @author SuQi
 * 
 */
public class SyncInformactionAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private SyncInformactionService syncInformactionService;
	private String JsonData;
	private IworkCmsContent model;
	private String type;
	private int pageNum;
	private int pageSize;
	private String id;

	public String getList() throws Exception {
		JsonData = syncInformactionService.getList(type, pageNum, pageSize);
		ResponseUtil.writeTextUTF8(JsonData);
		return null;
	}

	public String getMoreList() throws Exception {
		JsonData = syncInformactionService.getList();
	//	ResponseUtil.writeTextUTF8(JsonData);
		return SUCCESS;
	}

	public String getContentList() throws Exception {
		model = syncInformactionService.getContent(id);
		//ResponseUtil.writeTextUTF8(JsonData);
		return SUCCESS;
	}

	public SyncInformactionService getSyncInformactionService() {
		return syncInformactionService;
	}

	public void setSyncInformactionService(SyncInformactionService syncInformactionService) {
		this.syncInformactionService = syncInformactionService;
	}

	public String getJsonData() {
		return JsonData;
	}

	public void setJsonData(String jsonData) {
		JsonData = jsonData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IworkCmsContent getModel() {
		return model;
	}

	public void setModel(IworkCmsContent model) {
		this.model = model;
	}

}
