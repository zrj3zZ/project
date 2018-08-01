package com.iwork.plugs.onchat.model;

import java.util.Date;


public class OnlineChat implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "Appointment";
	
	// Fields

	private Long id;
	private String username;
	private String sendname;
	private String chatrecordname;
	private String content;
	private Date datatime;

	public OnlineChat() {
		
	}

	public OnlineChat(Long id, String username, String sendname,
			String chatrecordname, String content, Date datatime) {
		super();
		this.id = id;
		this.username = username;
		this.sendname = sendname;
		this.chatrecordname = chatrecordname;
		this.content = content;
		this.datatime = datatime;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getChatrecordname() {
		return chatrecordname;
	}

	public void setChatrecordname(String chatrecordname) {
		this.chatrecordname = chatrecordname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}
}
