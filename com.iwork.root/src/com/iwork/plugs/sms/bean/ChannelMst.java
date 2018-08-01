package com.iwork.plugs.sms.bean;

public class ChannelMst {
	private int cid;
	private int channelid;
	private String description;
	private int msgsp;

	public int getChannelid() {
		return channelid;
	}

	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMsgsp() {
		return msgsp;
	}

	public void setMsgsp(int msgsp) {
		this.msgsp = msgsp;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
