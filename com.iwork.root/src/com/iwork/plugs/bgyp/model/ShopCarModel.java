package com.iwork.plugs.bgyp.model;

public class ShopCarModel {
	public static final String ID = "ID";
	public static final String USERID = "USERID";
	public static final String NO = "NO";
	public static final String NAME = "NAME";
	public static final String NUM = "NUM";
	private Long id;
	private String userid;
	private String no ;
	private String name;
	private Long num;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	
	
	
}
