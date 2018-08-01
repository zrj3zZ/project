package com.iwork.app.message.sysmsg.model;


public class SysMessageLog {
	public static String DATABASE_ENTITY = "SysMessageLog";
	
	public static final int SYS_MESSAGE_LOG_STATUS_SUCCESS = 1;
	public static final int SYS_MESSAGE_LOG_STATUS_FAILURE = 0;
	
	// Fields BigDecimal.valueOf
	private Long id;
	private int priority;  //
	private int type;  //消息类型
	private String rcvRange; //
	private String title ; //消息标题
	private String content ; //消息正文

	private String url ;

	private String sendDate ;

	private String sender; 
    private int urlTarget;
    private int status;
    
    public SysMessageLog(){
    	
    }
    
    public SysMessageLog(SysMessage model) {
    	this.setContent(model.getContent());
    	this.setPriority(model.getPriority());
    	this.setRcvRange(model.getRcvRange());
    	this.setTitle(model.getTitle());
    	this.setType(model.getType());
    	this.setUrl(model.getUrl());
    	this.setUrlTarget(model.getUrlTarget());
    	this.setSender(model.getSender());
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRcvRange() {
		return rcvRange;
	}
	public void setRcvRange(String rcvRange) {
		this.rcvRange = rcvRange;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getUrlTarget() {
		return urlTarget;
	}
	public void setUrlTarget(int urlTarget) {
		this.urlTarget = urlTarget;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
