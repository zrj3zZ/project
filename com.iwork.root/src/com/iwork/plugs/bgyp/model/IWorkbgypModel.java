package com.iwork.plugs.bgyp.model;


public class IWorkbgypModel {
	public static final String ID =   "ID";
	public static final String LBBH = "LBBH";
	public static final String LBMC = "LBMC";
	public static final String NO =   "NO";
	public static final String JLDW = "JLDW";
	public static final String GG   = "GG";
	public static final String XH   = "XH";
	public static final String TP   = "TP";
	public static final String KCSL = "KCSL";
	public static final String ZDKC = "ZDKC";
	public static final String   DJ = "DJ";
	public static final String SFCY = "SFCY";
	public static final String NAME = "NAME";
	
	private Long id ;
	private String lbbh = "";
	private String lbmc = "";
	private String no = "";
	private String jldw = "";
	private String gg = "";
	private String xh = "";
	private String tp = "";
	private Long kcsl ;
	private Long zdkc;
	private Long dj ;
	private String sfcy = "";
	private String name = "";
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public Long getKcsl() {
		return kcsl;
	}
	public void setKcsl(Long kcsl) {
		this.kcsl = kcsl;
	}
	public Long getZdkc() {
		return zdkc;
	}
	public void setZdkc(Long zdkc) {
		this.zdkc = zdkc;
	}
	public Long getDj() {
		return dj;
	}
	public void setDj(Long dj) {
		this.dj = dj;
	}
	public String getSfcy() {
		return sfcy;
	}
	public void setSfcy(String sfcy) {
		this.sfcy = sfcy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
}
