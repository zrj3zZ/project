package com.iwork.plugs.email.model;

import java.util.Date;

/**
 * 邮件删除记录Model
 * 
 * @author zouyalei
 * 
 */
public class MailDelModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String DATABASE_ENTITY = "MailDelModel";

	// 主键ID
	public Long id;
	// 邮件ID
	public Long bindId;
	// 邮件任务ID
	public Long taskId;
	// 邮件所有人
	public String owner;
	// 邮件类型
	public String type;
	// 删除时间
	public Date createTime;

	public MailDelModel(){
		
	}

	// 构造方法
	public MailDelModel(Long id, Long bindId, Long taskId, String owner,
			String type, Date createTime) {
		super();
		this.id = id;
		this.bindId = bindId;
		this.taskId = taskId;
		this.owner = owner;
		this.type = type;
		this.createTime = createTime;
	}

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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
