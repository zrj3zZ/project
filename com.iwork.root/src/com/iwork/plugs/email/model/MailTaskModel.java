package com.iwork.plugs.email.model;

import java.util.Date;

/** 
 * PortalDaoFactory
 *
 * @version 2.2.1
 */
public class MailTaskModel implements java.io.Serializable{
   /**
    */
  public static final String DATABASE_ENTITY = "IWORK_MAIL_TASK";

    /**
     * ID
     */ 
  public static final String FIELD_ID = "ID";
    /**
     * 邮件ID
     */
  public static final String FIELD_BIND_ID = "BIND_ID";
    /**
     * 邮件接收人
     */
  public static final String FIELD_OWNER = "OWNER";
    /**
     * 邮件发送人+
     */
  public static final String FIELD_MAIL_FROM = "MAIL_FROM";//
    /**
     * 是否已读
     */
  public static final String FIELD_IS_READ = "IS_READ";
    /**
     * 是否已删除
     */
  public static final String FIELD_IS_DEL = "IS_DEL";
    /**
     * 是否已存档
     */
  public static final String FIELD_IS_ARCHIVES = "IS_ARCHIVES";
    /**
     * 是否标星+
     */
  public static final String FIELD_IS_STAR = "IS_STAR";
    /**
     * 是否已回复
     */
  public static final String FIELD_IS_RE = "IS_RE";
    /**
     * 是否为重要邮件
     */
  public static final String FIELD_IS_IMPORTANT = "IS_IMPORTANT";
    /**
     * 邮件大小
     */
  public static final String FIELD_MAIL_SIZE = "MAIL_SIZE";
    /**
     * 邮件标题
     */
  public static final String FIELD_TITLE = "TITLE";
    /**
     * 接收时间
     */
  public static final String FIELD_CREATE_TIME = "CREATE_TIME";
    /**
     * 阅读时间
     */
  public static final String FIELD_READ_TIME = "READ_TIME";
    /**
     * 文件夹ID
     */
  public static final String FIELD_MAIL_BOX = "MAIL_BOX";

    /**
     */
  public Long id;
    /**
     */
  public Long bindId;
    /**
     */
  
  public String owner = "";

    /**
     */
  public Long isRead;
    /**
     */
  public Long isDel;
    /**
     */
  public Long isArchives;
    /**
     * 
     */
  public Long isStar;
    /**
     */
  public Long isRe;
    /**
     */
  public Long isImportant;
    /**
     */
  public Long mailSize;
    /**
     */
  public String title = "";
    /**
     */
  public Date createTime;
    /**
     */
  public Date readTime;
  
    /**
     */
  public Long mailBox;
  
  public String mailFrom;
  public String mailTo;
  public String attachment;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Long getBindId() {
	return bindId;
}
public void setBindId(Long bindId) {
	this.bindId = bindId;
}

public String getOwner() {
	return owner;
}
public void setOwner(String owner) {
	this.owner = owner;
}
public Long getIsRead() {
	return isRead;
}
public void setIsRead(Long isRead) {
	this.isRead = isRead;
}
public Long getIsDel() {
	return isDel;
}
public void setIsDel(Long isDel) {
	this.isDel = isDel;
}
public Long getIsArchives() {
	return isArchives;
}
public void setIsArchives(Long isArchives) {
	this.isArchives = isArchives;
}
public Long getIsRe() {
	return isRe;
}
public void setIsRe(Long isRe) {
	this.isRe = isRe;
}
public Long getIsImportant() {
	return isImportant;
}
public void setIsImportant(Long isImportant) {
	this.isImportant = isImportant;
}
public Long getMailSize() {
	return mailSize;
}
public void setMailSize(Long mailSize) {
	this.mailSize = mailSize;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public Date getReadTime() {
	return readTime;
}
public void setReadTime(Date readTime) {
	this.readTime = readTime;
}
public Long getMailBox() {
	return mailBox;
}
public void setMailBox(Long mailBox) {
	this.mailBox = mailBox;
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
public String getAttachment() {
	return attachment;
}
public void setAttachment(String attachment) {
	this.attachment = attachment;
}
public Long getIsStar() {
	return isStar;
}
public void setIsStar(Long isStar) {
	this.isStar = isStar;
}

 

}
