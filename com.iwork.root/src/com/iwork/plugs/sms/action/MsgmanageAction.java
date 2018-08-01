package com.iwork.plugs.sms.action;

import java.util.Map;

import com.iwork.commons.Pager;
import com.iwork.commons.PagerService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.sms.service.MsgmanageService;
import com.iwork.plugs.sms.service.PrivateMsgService;
import com.opensymphony.xwork2.ActionContext;

public class MsgmanageAction {
	public PrivateMsgService privateMsgService;
	private MsgmanageService msgmanageService;
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
	private String chanel;
	private String supplier;
	private String sender;
	// 修改短信通道或状态
	private String tvalue;// 选得哪几条短信
	private String updatestatus;
	private String updatechanel;
	// 修改后再查询条件
	private String senderd;
	private String supplierd;
	private String channeld;
	private String mobilenumd;
	private String keywordsd;
	private String statusd;
	private String begintimed;
	private String endtimed;
	private String batchd;
//分页
private PagerService pagerService;
	private Pager pager;
	protected String currentPage;
	protected String pagerMethod;
	protected String totalRows;
	protected String id ;
	protected String orderindex;
	protected OrgUser userModel;
	protected OrgDepartment departmentModel;
	private String listmsg;
	
	public String loginmanage() {
		Map request = (Map) ActionContext.getContext().get("request");
		String channelall = msgmanageService.getchannelall();// 数据库里取通道
		String spb = msgmanageService.getspall();// 数据库里取供应商
		String statuses = privateMsgService.querystatuses();// 数据库里取状态
		request.put("channelb4", channelall);
		request.put("spb4", spb);
		request.put("statusb4", statuses);

		return "msgmanage";
	}
	public String qbegin(){
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("listmsg", "请填写以上查询条件，查看短信发送记录信息");
		return "msgmanage1";
	}
	public String msgmanage() {
		String sender = this.getSender();
		String supplier = this.getSupplier();
		String chanel = this.getChanel();
		String mobilenum = this.getMobilenum();
		String keywords = this.getKeywords();
		String status = this.getStatus();
		String startdate = this.getStartDate();
		String starthour = this.getStartHour();
		String startmin = this.getStartMin();
		String enddate = this.getEndDate();
		String endhour = this.getEndHour();
		String endmin = this.getEndMin();
		String batch = this.getBatchnum();
		String begintime = "";
		String endtime = "";
		if (startdate != null && !"".equals(startdate)) {
			String[] starta = startdate.split("/");
			startdate = starta[2] + "-" + starta[0] + "-" + starta[1];
			String[] enda = enddate.split("/");
			enddate = enda[2] + "-" + enda[0] + "-" + enda[1];
			begintime = startdate + " " + starthour + ":" + startmin + ":00";
			endtime = enddate + " " + endhour + ":" + endmin + ":00";
		}
		//分页
		UserContext userContext =  UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserModel(userContext.get_userModel());
		this.setDepartmentModel(userContext._deptModel);
	//	int totalRow=msgmanageService.getRows(userContext.get_userModel().getUserid());
		
		int totalRow=msgmanageService.getRows2(sender, supplier, chanel,
				mobilenum, keywords, status, begintime, endtime, batch);
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		//msgList=sysMessageService.getMessageList(userContext.get_userModel().getUserid(),pager.getPageSize(), pager.getStartRow());				
		
		 listmsg = msgmanageService.getQuery(pager.getPageSize(), pager.getStartRow(),sender, supplier, chanel,
				mobilenum, keywords, status, begintime, endtime, batch);

		String channelall = msgmanageService.getchannelall2();// 数据库里取通道
		Map request = (Map) ActionContext.getContext().get("request");
		//request.put("listmsg", listmsg);
		request.put("updatesp", channelall);
		request.put("senderd", sender);
		request.put("supplierd", supplier);
		request.put("channeld", chanel);
		request.put("mobilenumd", mobilenum);
		request.put("keywordsd", keywords);
		request.put("statusd", status);
		request.put("begintimed", begintime);
		request.put("endtimed", endtime);
		request.put("batchd", batch);
		return "msgmanagexinxi";
	}
	/**
	 * 分页
	 * @return
	 */
	public String msgmanage1() {
		String sender = this.getSenderd();
		String supplier = this.getSupplierd();
		String chanel = this.getChanneld();
		String mobilenum = this.getMobilenumd();
		String keywords = this.getKeywordsd();
		String status = this.getStatusd();
		String begintime=this.getBegintimed();
		String endtime=this.getEndtimed();
		String batch=this.getBatchd();
		//分页
		UserContext userContext =  UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserModel(userContext.get_userModel());
		this.setDepartmentModel(userContext._deptModel);

		int totalRow=msgmanageService.getRows2(sender, supplier, chanel,
				mobilenum, keywords, status, begintime, endtime, batch);
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		//msgList=sysMessageService.getMessageList(userContext.get_userModel().getUserid(),pager.getPageSize(), pager.getStartRow());				
		
		 listmsg = msgmanageService.getQuery(pager.getPageSize(), pager.getStartRow(),sender, supplier, chanel,
				mobilenum, keywords, status, begintime, endtime, batch);

		String channelall = msgmanageService.getchannelall2();// 数据库里取通道
		Map request = (Map) ActionContext.getContext().get("request");
		//request.put("listmsg", listmsg);
		request.put("updatesp", channelall);
		request.put("senderd", sender);
		request.put("supplierd", supplier);
		request.put("channeld", chanel);
		request.put("mobilenumd", mobilenum);
		request.put("keywordsd", keywords);
		request.put("statusd", status);
		request.put("begintimed", begintime);
		request.put("endtimed", endtime);
		request.put("batchd", batch);
		return "msgmanagexinxi";
	}

	/**
	 * 修改短信通道或状态
	 * 
	 * @return
	 */
	public String updatemsg() {

		String tvalue = this.getTvalue();
		String updatestatus = this.getUpdatestatus();
		String updatechannel = this.getUpdatechanel();
		String rvalue = msgmanageService.getupdate(tvalue, updatestatus,
				updatechannel);
		int totalRow=msgmanageService.getRows2(sender, supplier, chanel,
				mobilenum, keywords, status, begintimed, endtimed, batchd);
		pager=pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		this.setTotalRows(String.valueOf(totalRow));
		 listmsg = msgmanageService.getQuery(pager.getPageSize(), pager.getStartRow(),senderd, supplierd,
				channeld, mobilenumd, keywordsd, statusd, begintimed, endtimed,
				batchd);

		String channelall = msgmanageService.getchannelall2();// 数据库里取通道
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("listmsg", listmsg);
		request.put("updatesp", channelall);

		return "msgmanagexinxi";
	}
	public String getTvalue() {
		return tvalue;
	}

	public void setTvalue(String tvalue) {
		this.tvalue = tvalue;
	}

	public String getUpdatestatus() {
		return updatestatus;
	}

	public void setUpdatestatus(String updatestatus) {
		this.updatestatus = updatestatus;
	}

	public String getUpdatechanel() {
		return updatechanel;
	}

	public void setUpdatechanel(String updatechanel) {
		this.updatechanel = updatechanel;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
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

	public String getChanel() {
		return chanel;
	}

	public void setChanel(String chanel) {
		this.chanel = chanel;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public MsgmanageService getMsgmanageService() {
		return msgmanageService;
	}

	public void setMsgmanageService(MsgmanageService msgmanageService) {
		this.msgmanageService = msgmanageService;
	}

	public String getSenderd() {
		return senderd;
	}

	public void setSenderd(String senderd) {
		this.senderd = senderd;
	}

	public String getSupplierd() {
		return supplierd;
	}

	public void setSupplierd(String supplierd) {
		this.supplierd = supplierd;
	}

	public String getChanneld() {
		return channeld;
	}

	public void setChanneld(String channeld) {
		this.channeld = channeld;
	}

	public String getMobilenumd() {
		return mobilenumd;
	}

	public void setMobilenumd(String mobilenumd) {
		this.mobilenumd = mobilenumd;
	}

	public String getKeywordsd() {
		return keywordsd;
	}

	public void setKeywordsd(String keywordsd) {
		this.keywordsd = keywordsd;
	}

	public String getStatusd() {
		return statusd;
	}

	public void setStatusd(String statusd) {
		this.statusd = statusd;
	}

	public String getBegintimed() {
		return begintimed;
	}

	public void setBegintimed(String begintimed) {
		this.begintimed = begintimed;
	}

	public String getEndtimed() {
		return endtimed;
	}

	public void setEndtimed(String endtimed) {
		this.endtimed = endtimed;
	}

	public String getBatchd() {
		return batchd;
	}

	public void setBatchd(String batchd) {
		this.batchd = batchd;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}
	public OrgUser getUserModel() {
		return userModel;
	}
	public void setUserModel(OrgUser userModel) {
		this.userModel = userModel;
	}
	public OrgDepartment getDepartmentModel() {
		return departmentModel;
	}
	public void setDepartmentModel(OrgDepartment departmentModel) {
		this.departmentModel = departmentModel;
	}
	public String getListmsg() {
		return listmsg;
	}
	public void setListmsg(String listmsg) {
		this.listmsg = listmsg;
	}
}
