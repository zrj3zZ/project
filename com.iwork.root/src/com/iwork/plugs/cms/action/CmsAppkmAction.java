package com.iwork.plugs.cms.action;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsAppkm;
import com.iwork.plugs.cms.service.CmsAppkmService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 常用资料管理
 * 
 * @author SuQi
 * 
 */
public class CmsAppkmAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private IworkCmsAppkm iworkCmsAppkm;
	private CmsAppkmService cmsAppkmService;
	private String id;
	private String title;
	private String url;
	private String sequence;
	private String type;

	public String index() {
		return SUCCESS;
	}

	public void getList() {
		String json = cmsAppkmService.getList();
		ResponseUtil.writeTextUTF8(json);
	}

	public String edit() {
		cmsAppkmService.edit(id, title, url, sequence, type);
		return SUCCESS;
	}

	/*------------------GET/SET--------------------*/

	public IworkCmsAppkm getIworkCmsAppkm() {
		return iworkCmsAppkm;
	}

	public void setIworkCmsAppkm(IworkCmsAppkm iworkCmsAppkm) {
		this.iworkCmsAppkm = iworkCmsAppkm;
	}

	public CmsAppkmService getCmsAppkmService() {
		return cmsAppkmService;
	}

	public void setCmsAppkmService(CmsAppkmService cmsAppkmService) {
		this.cmsAppkmService = cmsAppkmService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
