package com.iwork.plugs.bgyp.model;


public class IWorkbgypTypeModel {
	public static final String ID =   "ID";
	public static final String KJMC = "KJMC";
	public static final String KJBH = "KJBH";
	public static final String LBBH = "LBBH";
	public static final String LBMC = "LBMC";
	public static final String BZ = "BZ";
	
	private Long id ;
	private String kjbh = "";
	private String kjmc = "";
	private String bz = "";
	private String lbbh = "";
	private String lbmc = "";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKjbh() {
		return kjbh;
	}
	public void setKjbh(String kjbh) {
		this.kjbh = kjbh;
	}
	public String getKjmc() {
		return kjmc;
	}
	public void setKjmc(String kjmc) {
		this.kjmc = kjmc;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getLbbh() {
		return lbbh;
	}
	public void setLbbh(String lbbh) {
		this.lbbh = lbbh;
	}
	public String getLbmc() {
		return lbmc;
	}
	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}
	
}
