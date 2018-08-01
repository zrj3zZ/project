

package com.iwork.plugs.email.model;

import java.util.Date;



public class MailModel implements java.io.Serializable {

    public static final String DATABASE_ENTITY = "IWORK_MAIL_DATA";
    public static final String FIELD_ID = "ID";//邮件ID
    public static final String FIELD_IS_SEND = "IS_SEND";//是否已发送
    public static final String FIELD_MAIL_TYPE = "MAIL_TYPE";//邮件类型
    public static final String FIELD_IS_IMPORTANT = "IS_IMPORTANT";//是否为重要邮件
    public static final String FIELD_MAIL_SIZE = "MAIL_SIZE";//邮件大小
    public static final String FIELD_CREATE_USER = "CREATE_USER";//创建人
    public static final String FIELD_CREATE_DATE = "CREATE_DATE";//创建日期
    public static final String FIELD_TO = "TOO";//收件人
    public static final String FIELD_CC = "CC";//抄送人
    public static final String FIELD_BCC = "BCC";//密送人
    public static final String FIELD_MAIL_FROM = "MAIL_FROM";//发件人
    public static final String FIELD_TITLE = "TITLE";//标题
    public static final String FIELD_CONTENT = "CONTENT";//邮件内容
    public static final String FIELD_ATTACHMENTS = "ATTACHMENTS";//附件
    public static final String FIELD_MESSAGEID="MESSAGEID";  
    public static final String FIELD_RELATEDID="RELATEDID";//关联邮件ID

    public Long _id;
    public Long _mailType;
    public Long _isSend;
    public Long _isImportant;
    public Long _mailSize;
    public String _createUser = "";
    public Date _createDate;
    public String _to = "";
    public String _cc = "";
    public String _bcc = "";
    public String _mailFrom = "";
    public String _title = "";
    public String _content = "";
    public String _attachments = "";
   
    public Long _relatedId;
    
	public Long get_id() {
		return _id;
	}
	public void set_id(Long id) {
		_id = id;
	}
	public Long get_mailType() {
		return _mailType;
	}
	public void set_mailType(Long mailType) {
		_mailType = mailType;
	}
	public Long get_isSend() {
		return _isSend;
	}
	public void set_isSend(Long isSend) {
		_isSend = isSend;
	}
	public Long get_isImportant() {
		return _isImportant;
	}
	public void set_isImportant(Long isImportant) {
		_isImportant = isImportant;
	}
	public Long get_mailSize() {
		return _mailSize;
	}
	public void set_mailSize(Long mailSize) {
		_mailSize = mailSize;
	}
	public String get_createUser() {
		return _createUser;
	}
	public void set_createUser(String createUser) {
		_createUser = createUser;
	}
	public Date get_createDate() {
		return _createDate;
	}
	public void set_createDate(Date createDate) {
		_createDate = createDate;
	}
	public String get_to() {
		return _to;
	}
	public void set_to(String to) {
		_to = to;
	}
	public String get_cc() {
		return _cc;
	}
	public void set_cc(String cc) {
		_cc = cc;
	}
	public String get_bcc() {
		return _bcc;
	}
	public void set_bcc(String bcc) {
		_bcc = bcc;
	}
	public String get_mailFrom() {
		return _mailFrom;
	}
	public void set_mailFrom(String mailFrom) {
		_mailFrom = mailFrom;
	}
	public String get_title() {
		return _title;
	}
	public void set_title(String title) {
		_title = title;
	}
	public String get_content() {
		return _content;
	}
	public void set_content(String content) {
		_content = content;
	}
	public String get_attachments() {
		return _attachments;
	}
	public void set_attachments(String attachments) {
		_attachments = attachments;
	}

	public Long get_relatedId() {
		return _relatedId;
	}
	public void set_relatedId(Long relatedId) {
		_relatedId = relatedId;
	}


   
}
