package com.iwork.plugs.email.model;

import java.util.Date;

/**
 * PortalDaoFactory
 * 
 * @version 2.2.1
 */
public class MailOwnerModel implements java.io.Serializable {
	/**
	 * 邮件发件任务表名
	 */
	public static final String DATABASE_ENTITY = "IWORK_MAIL_OWNER";

	/**
	 * 发送任务ID
	 */
	public static final String FIELD_ID = "ID";
	/**
	 * 邮件ID
	 */
	public static final String FIELD_BIND_ID = "BIND_ID";
	/**
	 * 发送人
	 */
	public static final String FIELD_OWNER = "OWNER";
	/**
	 * 接收人
	 */
	public static final String FIELD_MAIL_TO = "MAIL_TO";

	/**
	 * 是否删除
	 */
	public static final String FIELD_IS_DEL = "IS_DEL";
	/**
	 * 是否标星
	 */
	public static final String FIELD_IS_STAR = "IS_STAR";

	/**
	 * 是否归档
	 */
	public static final String FIELD_IS_ARCHIVES = "IS_ARCHIVES";
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
	 * 发送时间
	 */
	public static final String FIELD_CREATE_TIME = "CREATE_TIME";

	/**
	 * 文件夹ID
	 */
	public static final String FIELD_MAIL_BOX = "MAIL_BOX";

	/**
	 * 发送任务ID
     */
	public Long id;
	/**
	 * 邮件ID
     */
	public Long bindId;
	
	/**
	 * 发送人
     */
	public String owner = "";
	/**
	 * 接收人
     */
	public String mailTo;
	/**
	 * 是否删除
     */
	public Long isDel;
	/**
	 * 是否标星
     */
	public Long isStar;
	
	/**
	 * 是否归档
     */
	public Long isArchives;
	/**
	 * 是否已回复
     */
	public Long isRe;
	
	
	/**
	 * 是否重要邮件
     */
	public Long isImportant;
	/**
	 * 邮箱大小
     */
	public Long mailSize;
	/**
	 * 邮箱标题
     */
	public String title = "";
	/**
	 * 发送时间
     */
	public Date createTime;
	/**
	 * 文件夹ID
     */
	public Long mailBox;
	
	/**
	 * 截取后字符串
	 */
	public String subTitle;
	
	/**
	 * 截取后字符串
	 */
	public String sjr;
	
	public String getSjr() {
		return sjr;
	}

	public void setSjr(String sjr) {
		this.sjr = sjr;
	}

	public Long getId() {
		return id;
	}

	public Long getIsStar() {
		return isStar;
	}

	public void setIsStar(Long isStar) {
		this.isStar = isStar;
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


	public Long getMailBox() {
		return mailBox;
	}

	public void setMailBox(Long mailBox) {
		this.mailBox = mailBox;
	}


	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	


}
