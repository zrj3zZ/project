package com.iwork.plugs.sms.action;

import java.net.URLDecoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.plugs.sms.service.PrivateMsgService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
public class PrivateMsgAction {
	private static Logger logger = Logger.getLogger(PrivateMsgAction.class);
	private PrivateMsgService privateMsgService;
	//个人短信查询条件
	private String batchnum;
	private String mobilenum;
	private String keywords;
	private String startDate;
	private String startHour;
	private String startMin;
	private String endDate;
	private String endHour;
	private String endMin;
	private String status;
	//个人短信查询条件（jquery）
	private String hbatch;
	private String hmobile;
	private String hcontent;
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
	 * 进入页面（jquery）
	 * @return
	 */
	public String loginprimsg(){
		Map request = (Map) ActionContext.getContext().get("request");
		//String statuses=privateMsgService.querystatuses();//数据库里取状态
		String statuses=privateMsgService.querystatuses2();//数据库里取状态
	    String html2=privateMsgService.queryuserdata1("test"); //查询用户余额等
	    request.put("html2", html2);
		request.put("statuses", statuses);
		int totalRow=privateMsgService.getRows();
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		return "privatemsg1";
	}
	
	
	/**
	 * 个人短信查询(jquery)
	 * @return
	 */
		public String privatemsgj(){
		  String batchnum=this.getHbatch();
		  String mobilenum=this.getHmobile();
		  String keywords=this.getHcontent();
		  String status=this.getHstatus();
		  String startdate=this.getHstartd();
		  String starthour=this.getHstarth();
		  String startmin=this.getHstartm();
		  String enddate=this.getHendd();
		  String endhour=this.getHendh();
		  String endmin=this.getHendm();
		  String begintime="";
		  String endtime="";
		  String keywordss="";
		  String statuss="";
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
				  statuss=URLDecoder.decode(status,"UTF-8");
				}catch(Exception e){logger.error(e,e);}
				int totalRow=privateMsgService.getRows();
				pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
				this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
				this.setTotalRows(String.valueOf(totalRow));
			  String json = "";	
			  json = privateMsgService.getMsgTreeJson(pager.getPageSize(), pager.getStartRow(),mobilenum,keywordss,statuss,begintime,endtime,batchnum); 
			  HttpServletRequest request = ServletActionContext.getRequest();	
			  request.setAttribute("privatemsg", json);
			 
			  return "privatemsgjson";
		}
	public String loadq(){
		  String batchnum=this.getHbatch();
		  String mobilenum=this.getHmobile();
		  String keywords=this.getHcontent();
		  String status=this.getHstatus();
		  String startdate=this.getHstartd();
		  String starthour=this.getHstarth();
		  String startmin=this.getHstartm();
		  String enddate=this.getHendd();
		  String endhour=this.getHendh();
		  String endmin=this.getHendm();
		this.setHbatch(batchnum);
		this.setHmobile(mobilenum);
		this.setHcontent(keywords);
		this.setHstatus(status);
		this.setHstartd(startdate);
		this.setHstarth(starthour);
		this.setHstartm(startmin);
		this.setHendd(enddate);
		this.setHendh(endhour);
		this.setHendm(endmin);
		Map request = (Map) ActionContext.getContext().get("request");
		//String statuses=privateMsgService.querystatuses();//数据库里取状态
		String statuses=privateMsgService.querystatuses3(status);//数据库里取状态

	    String html2=privateMsgService.queryuserdata1("test"); //查询用户余额等
	    request.put("html2", html2);
		request.put("statuses", statuses);
		String begintime="";
		String endtime="";
	    String keywordss="";
		String statuss="";
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
			  statuss=URLDecoder.decode(status,"UTF-8");
			}catch(Exception e){logger.error(e,e);}
		int totalRow=privateMsgService.getRows2(mobilenum,keywordss,statuss,begintime,endtime,batchnum);
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
	
		return "privatemsg1";
	}
/**
 * 个人短信查询
 * @return
 */
	public String privatemsg(){
		String batchnum=this.getBatchnum();
		  String mobilenum=this.getMobilenum();
		  String keywords=this.getKeywords();
		  String status=this.getStatus();
		  String startdate=this.getStartDate();
		  String starthour=this.getStartHour();
		  String startmin=this.getStartMin();
		  String enddate=this.getEndDate();
		  String endhour=this.getEndHour();
		  String endmin=this.getEndMin();
		  String begintime="";
		  String endtime="";
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
		  String html=privateMsgService.queryPMsg(pager.getPageSize(), pager.getStartRow(),mobilenum,keywords,status,begintime,endtime,batchnum);
		  Map request = (Map) ActionContext.getContext().get("request");
		  request.put("html", html);
	   
		  return "privatemsgxinxi";
	}
	public PrivateMsgService getPrivateMsgService() {
		return privateMsgService;
	}

	public void setPrivateMsgService(PrivateMsgService privateMsgService) {
		this.privateMsgService = privateMsgService;
	}

	public String getBatchnum() {
		return batchnum;
	}

	public void setBatchnum(String batchnum) {
		this.batchnum = batchnum;
	}

	public String getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getHbatch() {
		return hbatch;
	}
	public void setHbatch(String hbatch) {
		this.hbatch = hbatch;
	}
	public String getHmobile() {
		return hmobile;
	}
	public void setHmobile(String hmobile) {
		this.hmobile = hmobile;
	}
	public String getHcontent() {
		return hcontent;
	}
	public void setHcontent(String hcontent) {
		this.hcontent = hcontent;
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
