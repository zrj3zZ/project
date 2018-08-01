package com.iwork.plugs.sms.bean;

import java.util.Date;



public class LimitMst {
	public static String DATABASE_ENTITY = "LimitMst";

	private int cid;
	private String userid;
	private int smsLimit;
	private int cmobilechannel;
	private int cunicomchannel;
	private int ctelecomchannel;
	private Date submittime;
	public Date getSubmittime() {
		return submittime;
	}
	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getSmsLimit() {
		return smsLimit;
	}
	public void setSmsLimit(int smsLimit) {
		this.smsLimit = smsLimit;
	}
	public int getCmobilechannel() {
		return cmobilechannel;
	}
	public void setCmobilechannel(int cmobilechannel) {
		this.cmobilechannel = cmobilechannel;
	}
	public int getCunicomchannel() {
		return cunicomchannel;
	}
	public void setCunicomchannel(int cunicomchannel) {
		this.cunicomchannel = cunicomchannel;
	}
	public int getCtelecomchannel() {
		return ctelecomchannel;
	}
	public void setCtelecomchannel(int ctelecomchannel) {
		this.ctelecomchannel = ctelecomchannel;
	}

}
