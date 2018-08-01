package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.service.MailWithAttachmentService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MailWithAttachmentAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7459875921773900466L;
	private MailWithAttachmentService mailWithAttachmentService;
	protected String id;
	protected String nodeType;
    private String sjr;
    private String content;
    private String title;
    private List<HashMap<String,Object>> list;
    private String html;
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public List<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}

	public String getSjr() {
		return sjr;
	}

	public void setSjr(String sjr) {
		this.sjr = sjr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	private String file;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	private String companyId;

	public MailWithAttachmentService getMailWithAttachmentService() {
		return mailWithAttachmentService;
	}

	public void setMailWithAttachmentService(
			MailWithAttachmentService mailWithAttachmentService) {
		this.mailWithAttachmentService = mailWithAttachmentService;
	}

	public String sendMail() {
		list=mailWithAttachmentService.getList();
		return SUCCESS;

	}

	public void getJSON() {
		if ((this.id == null) || (this.id.equals("")))
			this.id = "0";
		if (this.nodeType == null)
			this.nodeType = "company";
		String json = mailWithAttachmentService.getTreeJson(this.id, this.nodeType);
		ResponseUtil.write(json);
	}
	
	public void getGroupData(){
		StringBuffer jsonHtml = mailWithAttachmentService.getGroupData(id);
		ResponseUtil.write(jsonHtml.toString());
	}
	
	public void mailsend(){
		String msg=mailWithAttachmentService.mailsend(sjr,content,file,title);
		ResponseUtil.write(msg);
	}
	public String sendMailContent(){
		return SUCCESS;
	}
	public String getXX(){
		html=mailWithAttachmentService.getHtml(id);
		return SUCCESS;
		
	}
}
