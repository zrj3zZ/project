package com.iwork.plugs.email.model;

import java.util.Date;
import com.iwork.eaglesearch.model.IndexModel;

public class EmailIndexModel extends IndexModel {
	public static final String EMAIL_DATA_MAILID = "_MAILID";
	public static final String EMAIL_DATA_OWNER = "_OWNER";
	public static final String EMAIL_DATA_FROM = "_FROM";
	public static final String EMAIL_DATA_TO = "_TO";
	public static final String EMAIL_DATA_CC = "_CC";
	public static final String EMAIL_DATA_ATTACH = "_ATTACH";
	public static final String EMAIL_DATA_CREATEDATE = "_CREATEDATE";
	public static final String EMAIL_DATA_MAILBOX = "_MAILBOX";
	public static final String EMAIL_DATA_ID = "_MAILID"; 
	
	private Long mailid;
	private String owner;
	private String  mailFrom;
	private String  mailTo;
	private String  cclist;
	private String  title;
	private Date  createDate;
	private String  content;
	private String  attach;
	private Long mailBox;
	
	
	
	public Long getMailBox() {
		return mailBox;
	}
	public void setMailBox(Long mailBox) {
		this.mailBox = mailBox;
	}
	
	public String getCclist() {
		return cclist;
	}
	public void setCclist(String cclist) {
		this.cclist = cclist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Long getMailid() {
		return mailid;
	}
	public void setMailid(Long mailid) {
		this.mailid = mailid;
	}
	
	
}
