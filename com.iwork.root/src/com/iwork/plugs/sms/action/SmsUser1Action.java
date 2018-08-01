package com.iwork.plugs.sms.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.plugs.sms.service.SmsUser1Service;
import com.opensymphony.xwork2.ActionSupport;

public class SmsUser1Action extends ActionSupport {
	private SmsUser1Service smsUser1Service;
	private String hoprator;
	private String hstartd;
	private String hendd;
	// 用户设置
	private String hcid;
	private String hmchannel;
	private String htchannel;
	private String huchannel;
	private String htype;
	private String hcount;
	

	public String loginuserj() {
		String operator = this.getHoprator();
		String startd = this.getHstartd();
		String endd = this.getHendd();
		this.setHoprator(operator);
		this.setHstartd(startd);
		this.setHendd(endd);
		return "userOperator";
	}

	public String quserj() {
		String operator = this.getHoprator();
		String startd = this.getHstartd();
		String endd = this.getHendd();
		if (startd != null && !"".equals(startd)) {
			String[] starta = startd.split("/");
			startd = starta[2] + "-" + starta[0] + "-" + starta[1];
		}
		if (endd != null && !"".equals(endd)) {
			String[] enda = endd.split("/");
			endd = enda[2] + "-" + enda[0] + "-" + enda[1];
		}
		String json = "";
		json = smsUser1Service.getSmsUserJson(operator, startd, endd);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("users", json);

		return "userjson";
	}

	public String saveuser() {
		String cid=this.getHcid();
		String mochannel=this.getHmchannel();
		String unchannel=this.getHuchannel();
		String techannel=this.getHtchannel();
		String type=this.getHtype();
		String count=this.getHcount();
		smsUser1Service.savechange(cid,type,count);
		
		return "userOperator";
	}

	public String getHoprator() {
		return hoprator;
	}

	public void setHoprator(String hoprator) {
		this.hoprator = hoprator;
	}

	public String getHstartd() {
		return hstartd;
	}

	public void setHstartd(String hstartd) {
		this.hstartd = hstartd;
	}

	public String getHendd() {
		return hendd;
	}

	public void setHendd(String hendd) {
		this.hendd = hendd;
	}

	public SmsUser1Service getSmsUser1Service() {
		return smsUser1Service;
	}

	public void setSmsUser1Service(SmsUser1Service smsUser1Service) {
		this.smsUser1Service = smsUser1Service;
	}

	public String getHmchannel() {
		return hmchannel;
	}

	public void setHmchannel(String hmchannel) {
		this.hmchannel = hmchannel;
	}

	public String getHtchannel() {
		return htchannel;
	}

	public void setHtchannel(String htchannel) {
		this.htchannel = htchannel;
	}

	public String getHuchannel() {
		return huchannel;
	}

	public void setHuchannel(String huchannel) {
		this.huchannel = huchannel;
	}

	public String getHtype() {
		return htype;
	}

	public void setHtype(String htype) {
		this.htype = htype;
	}

	public String getHcid() {
		return hcid;
	}

	public void setHcid(String hcid) {
		this.hcid = hcid;
	}
	public String getHcount() {
		return hcount;
	}

	public void setHcount(String hcount) {
		this.hcount = hcount;
	}
}
