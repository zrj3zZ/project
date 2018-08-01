package com.iwork.plugs.appointment.model;

import java.util.Date;

public class AppointmentYYSX implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String DATABASE_ENTITY = "AppointmentYYSX";
	
	// Fields

	private Long id;
	private String sxms;
	private String cjr;
	private Date cjsj;
	private Date ksrq;
	private Date jzrq;
	private String yysj;
	private String yydd;
	public String getYydd() {
		return yydd;
	}
	public void setYydd(String yydd) {
		this.yydd = yydd;
	}
	public AppointmentYYSX() {
	}
	public AppointmentYYSX(Long id, String sxms, String cjr, Date cjsj,
			Date ksrq, Date jzrq,String yydd) {
		this.id = id;
		this.sxms = sxms;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.ksrq = ksrq;
		this.jzrq = jzrq;
		this.yydd = yydd;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSxms() {
		return sxms;
	}
	public void setSxms(String sxms) {
		this.sxms = sxms;
	}
	public String getCjr() {
		return cjr;
	}
	public void setCjr(String cjr) {
		this.cjr = cjr;
	}
	public Date getCjsj() {
		return cjsj;
	}
	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
	public Date getKsrq() {
		return ksrq;
	}
	public void setKsrq(Date ksrq) {
		this.ksrq = ksrq;
	}
	public Date getJzrq() {
		return jzrq;
	}
	public void setJzrq(Date jzrq) {
		this.jzrq = jzrq;
	}
	public String getYysj() {
		return yysj;
	}
	public void setYysj(String yysj) {
		this.yysj = yysj;
	}
	
}
