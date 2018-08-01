package com.iwork.plugs.sms.action;
import org.apache.log4j.Logger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.service.LogService;
import com.opensymphony.xwork2.ActionContext;
/**
 * 日志加到数据库
 * @author Administrator
 *
 */
public  class LogAction {
	private static Logger logger = Logger.getLogger(LogAction.class);
	public LogService logService;
	private String checktype;
	private String keywords;
	private String sender;
	private String startDate;
	private String startHour;
	private String startMin;
	private String endDate;
	private String endHour;
	private String endMin;
	//日志查询条件（jquery）
	private String hoprator;
	private String hvalue;
	private String hstartd;
	private String hstarth;
	private String hstartm;
	private String hendd;
	private String hendh;
	private String hendm;
	private String hstatus;
	//分页
	private PagerService pagerService;
		private Pager pager;
		protected String currentPage;
		protected String pagerMethod;
		protected String totalRows;
	/**
	 * 链接到短信日志(jquery)
	 * @return
	 */

	public String loginlog(){
		String typebox=logService.getTypeCheckbox();
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("type-checkbox", typebox);
		int totalRow=logService.getRows();
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		return "msglog1";
	}
	/**
	 * 日志查询(jquery)
	 * @return
	 */
		public String qlogj(){
			String type=this.getChecktype();
			  String operator=this.getHoprator();
			  String keywords=this.getHvalue();
			  String startdate=this.getHstartd();
			  String starthour=this.getHstarth();
			  String startmin=this.getHstartm();
			  String enddate=this.getHendd();
			  String endhour=this.getHendh();
			  String endmin=this.getHendm();
			  String begintime="";
			  String endtime="";
			  String keywordss="";
			  if(startdate!=null&&!"".equals(startdate)){
				  String[]starta=startdate.split("/");
				  startdate=starta[2]+"-"+starta[0]+"-"+starta[1];
				  String[]enda=enddate.split("/");
				  enddate=enda[2]+"-"+enda[0]+"-"+enda[1];
			  begintime=startdate+" "+starthour+":"+startmin+":00";
			  endtime=enddate+" "+endhour+":"+endmin+":00";
			  } else{
				   begintime="";
				   endtime="";
				  }		
			  try{
				  keywordss=URLDecoder.decode(keywords,"UTF-8");
				}catch(Exception e){logger.error(e,e);}
				ArrayList type_list = new ArrayList();
				logService.splitType(type, type_list);
			int totalRow=logService.getRows2(sender, keywordss, begintime, endtime, type_list);
			pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
			this.setTotalRows(String.valueOf(totalRow));
			  String json = "";	
			  json = logService.getLogJson(pager.getPageSize(), pager.getStartRow(),operator, keywordss, begintime, endtime, type);
			  HttpServletRequest request = ServletActionContext.getRequest();	
			  request.setAttribute("log", json);
			  
			  return "msglogjson";
		}
		
		
		public String loginj(){
			 String operator=this.getHoprator();
			  String type=this.getChecktype();
			  String keywords=this.getHvalue();
			 // String status=this.getHstatus();
			  String startdate=this.getHstartd();
			  String starthour=this.getHstarth();
			  String startmin=this.getHstartm();
			  String enddate=this.getHendd();
			  String endhour=this.getHendh();
			  String endmin=this.getHendm();
			this.setHoprator(operator);
			this.setChecktype(type);
			this.setHvalue(keywords);
			//this.setHstatus(status);
			this.setHstartd(startdate);
			this.setHstarth(starthour);
			this.setHstartm(startmin);
			this.setHendd(enddate);
			this.setHendh(endhour);
			this.setHendm(endmin);
			
			ArrayList type_list = new ArrayList();
			logService.splitType(type, type_list);
			String typebox=logService.getTypeCheckbox(type_list);
			Map request = (Map) ActionContext.getContext().get("request");
			request.put("type-checkbox", typebox);
			
			  String begintime="";
			  String endtime="";
			  String keywordss="";
			  if(startdate!=null&&!"".equals(startdate)){
				  String[]starta=startdate.split("/");
				  startdate=starta[2]+"-"+starta[0]+"-"+starta[1];
				  String[]enda=enddate.split("/");
				  enddate=enda[2]+"-"+enda[0]+"-"+enda[1];
			  begintime=startdate+" "+starthour+":"+startmin+":00";
			  endtime=enddate+" "+endhour+":"+endmin+":00";
			  } else{
				   begintime="";
				   endtime="";
				  }		
			  try{
				  keywordss=URLDecoder.decode(keywords,"UTF-8");
				}catch(Exception e){logger.error(e,e);}
			  
			
			int totalRow=logService.getRows2(sender, keywordss, begintime, endtime, type_list);
			pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
			this.setTotalRows(String.valueOf(totalRow));
			return "msglog1";
		}
//	/**
//	 * 链接到短信日志
//	 * @return
//	 */
	public String qmsglog(){
		String begintime = "";
		String endtime = "";	
		String sender = this.getSender();
		String keywords = this.getKeywords();
		String startdate = this.getStartDate();
		String starthour = this.getStartHour();
		String startmin = this.getStartMin();
		String enddate = this.getEndDate();
		String endhour = this.getEndHour();
		String endmin = this.getEndMin();
		String checktype = this.getChecktype();
		if (startdate != null && !"".equals(startdate)) {
			String[] starta = startdate.split("/");
			startdate = starta[2] + "-" + starta[0] + "-" + starta[1];
			String[] enda = enddate.split("/");
			enddate = enda[2] + "-" + enda[0] + "-" + enda[1];
			 begintime = startdate + " " + starthour + ":" + startmin + ":00";
			endtime = enddate + " " + endhour + ":" + endmin + ":00";			
		} 
		String loglist=logService.getLog(sender, keywords, begintime, endtime, checktype);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("loglist",loglist);
		return "msglogxinxi";
	}
	
	
	
//错误日志
		public  void add(String uid, int type, String value) {
			LogMst lm=new LogMst();
			Calendar c = Calendar.getInstance();
			Date date = c.getTime(); // 返回Date型日期时间 
			lm.setLogtime(date);
			//lm.setLogtype(type);
			lm.setUserid(uid);
			lm.setValue(value);
			logService.adddb(lm);
			
		}

		public void setLogService(LogService logService) {
			this.logService = logService;
		}

		public String getChecktype() {
			return checktype;
		}
		public void setChecktype(String checktype) {
			this.checktype = checktype;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getStartHour() {
			return startHour;
		}
		public void setStartHour(String startHour) {
			this.startHour = startHour;
		}
		public String getStartMin() {
			return startMin;
		}
		public void setStartMin(String startMin) {
			this.startMin = startMin;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getEndHour() {
			return endHour;
		}
		public void setEndHour(String endHour) {
			this.endHour = endHour;
		}
		public String getEndMin() {
			return endMin;
		}
		public void setEndMin(String endMin) {
			this.endMin = endMin;
		}
		public LogService getLogService() {
			return logService;
		}
		public String getHoprator() {
			return hoprator;
		}
		public void setHoprator(String hoperator) {
			this.hoprator = hoperator;
		}
		public String getHvalue() {
			return hvalue;
		}
		public void setHvalue(String hvalue) {
			this.hvalue = hvalue;
		}
		public String getHstartd() {
			return hstartd;
		}
		public void setHstartd(String hstartd) {
			this.hstartd = hstartd;
		}
		public String getHstarth() {
			return hstarth;
		}
		public void setHstarth(String hstarth) {
			this.hstarth = hstarth;
		}
		public String getHstartm() {
			return hstartm;
		}
		public void setHstartm(String hstartm) {
			this.hstartm = hstartm;
		}
		public String getHendd() {
			return hendd;
		}
		public void setHendd(String hendd) {
			this.hendd = hendd;
		}
		public String getHendh() {
			return hendh;
		}
		public void setHendh(String hendh) {
			this.hendh = hendh;
		}
		public String getHendm() {
			return hendm;
		}
		public void setHendm(String hendm) {
			this.hendm = hendm;
		}
		public String getHstatus() {
			return hstatus;
		}
		public void setHstatus(String hstatus) {
			this.hstatus = hstatus;
		}
		public PagerService getPagerService() {
			return pagerService;
		}
		public void setPagerService(PagerService pagerService) {
			this.pagerService = pagerService;
		}
		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager pager) {
			this.pager = pager;
		}
		public String getCurrentPage() {
			return currentPage;
		}
		public void setCurrentPage(String currentPage) {
			this.currentPage = currentPage;
		}
		public String getPagerMethod() {
			return pagerMethod;
		}
		public void setPagerMethod(String pagerMethod) {
			this.pagerMethod = pagerMethod;
		}
		public String getTotalRows() {
			return totalRows;
		}
		public void setTotalRows(String totalRows) {
			this.totalRows = totalRows;
		}
			
 
}
