package com.iwork.plugs.sms.bean;

public class PhonebookgroupMst implements java.io.Serializable{
	public static String DATABASE_ENTITY = "PhonebookgroupMst";
	private int cid;
	private int groupid;
	private String groupname;
	private String userid;
	public PhonebookgroupMst(){
		
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
