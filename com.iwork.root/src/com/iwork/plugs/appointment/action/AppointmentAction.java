package com.iwork.plugs.appointment.action;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.iwork.app.message.sysmsg.util.PageBean;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.constant.SysConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.appointment.model.Appointment;
import com.iwork.plugs.appointment.service.AppointmentService;
import com.iwork.plugs.calender.Util.CalendarUtil;
import com.iwork.plugs.meeting.util.UtilDate;
import com.opensymphony.xwork2.ActionSupport;

public class AppointmentAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	// 保存提醒值
	private static Logger logger = Logger.getLogger(AppointmentAction.class);
	private AppointmentService appointmentService;
	private Appointment appointment;
	private String id;
	private String userid;
	private String szsj;
	private String jyr;
	private String sfjy;
	private String start;
	private String end;
	
	private String currentTime;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String allDay;
	private String title;
	private String defaultView;
	private Long sendSysmsg;
	private Long sendEmail;
	private Long sendSms;
	private Long isShare;
	private Long isWriter;
	private String visitor;
	private String purviewType;
	private String purviewUser;
	private String tipsInfo;

	private String username;
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAllDay() {
		return allDay;
	}

	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}

	public Long getSendSysmsg() {
		return sendSysmsg;
	}

	public void setSendSysmsg(Long sendSysmsg) {
		this.sendSysmsg = sendSysmsg;
	}

	public Long getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Long sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Long getSendSms() {
		return sendSms;
	}

	public void setSendSms(Long sendSms) {
		this.sendSms = sendSms;
	}

	public Long getIsShare() {
		return isShare;
	}

	public void setIsShare(Long isShare) {
		this.isShare = isShare;
	}

	public Long getIsWriter() {
		return isWriter;
	}

	public void setIsWriter(Long isWriter) {
		this.isWriter = isWriter;
	}

	public String getTipsInfo() {
		return tipsInfo;
	}

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}

	public String getPurviewType() {
		return purviewType;
	}

	public void setPurviewType(String purviewType) {
		this.purviewType = purviewType;
	}

	public String getPurviewUser() {
		return purviewUser;
	}

	public void setPurviewUser(String purviewUser) {
		this.purviewUser = purviewUser;
	}

	public AppointmentService getAppointmentService() {
		return appointmentService;
	}

	public void setAppointmentService(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public String getSzsj() {
		return szsj;
	}

	public void setSzsj(String szsj) {
		this.szsj = szsj;
	}

	public String getJyr() {
		return jyr;
	}

	public void setJyr(String jyr) {
		this.jyr = jyr;
	}

	public String getSfjy() {
		return sfjy;
	}

	public void setSfjy(String sfjy) {
		this.sfjy = sfjy;
	}

	private List<Map> list;

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private int totalNum; // 总页数

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getTotalLogPage() {
		return totalLogPage;
	}

	public void setTotalLogPage(int totalLogPage) {
		this.totalLogPage = totalLogPage;
	}

	public int getPerLogPage() {
		return perLogPage;
	}

	public void setPerLogPage(int perLogPage) {
		this.perLogPage = perLogPage;
	}

	public PageBean getLogListBean() {
		return logListBean;
	}

	public void setLogListBean(PageBean logListBean) {
		this.logListBean = logListBean;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrLogPage() {
		return currLogPage;
	}

	public void setTipsInfo(String tipsInfo) {
		this.tipsInfo = tipsInfo;
	}

	private int pageNumber; // 当前页数
	private int startRow; //
	private int currLogPage; //
	private int totalLogPage; //
	private int perLogPage = 10; //
	private PageBean logListBean; //

	private int pageSize = 10; // 每页条数

	/**
	 * !初始化加载日历
	 * 
	 * @return
	 */
	public String loadCalendar() {
		purviewType = "owner";
		if (currentTime == null) {
			currentTime = UtilDate.dateFormat(new Date());
		}
		SysPersonConfig defaultConfig = UserContextUtil
				.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_DEFAULT_VIEW);
		if (defaultConfig != null) {
			defaultView = defaultConfig.getValue();
		} else {
			defaultView = "month";
		}
		visitor = UserContextUtil.getInstance().getCurrentUserId();
		return SUCCESS;
	}

	/**
	 * 访问其他用户的日程
	 * 
	 * @return
	 */
	public String visitorOtherCalendar() {
		purviewType = "visitor";
		if (visitor != null && !visitor.equals("undefined")) {
			UserContext uc = UserContextUtil.getInstance().getUserContext(
					visitor);
			title = "<img width=\"30\" src=\"iwork_img/engine/calendar.png\">【"
					+ uc._userModel.getUsername() + "】的工作日志";
			SysPersonConfig isShareConfig = UserContextUtil.getInstance()
					.getUserConfig(visitor,
							SysPersonConfig.SYS_CALENDAR_IS_SHARE);
			SysPersonConfig isWriterConfig = UserContextUtil.getInstance()
					.getUserConfig(visitor,
							SysPersonConfig.SYS_CALENDAR_IS_WRITER);
			if (isShareConfig == null) {
				isShareConfig = new SysPersonConfig();
				isShareConfig.setValue("1");
				this.isShare = SysConst.on;
			}
			if (isWriterConfig == null) {
				isWriterConfig = new SysPersonConfig();
				isWriterConfig.setValue("1");
				this.isWriter = SysConst.on;
			}
			if (isShareConfig != null && isShareConfig.getValue() != null) {
				isShare = Long.parseLong(isShareConfig.getValue());
				if (isWriterConfig != null) {
					Long isWriter = Long.parseLong(isWriterConfig.getValue());
					if (isWriter != null && isWriter.equals(new Long(1))) {
						SysPersonConfig purviewType = UserContextUtil
								.getInstance()
								.getUserConfig(
										visitor,
										SysPersonConfig.SYS_CALENDAR_TYPE_PURVIEW);
						if (purviewType != null
								&& purviewType.getValue().equals("all")) {
							isWriter = SysConst.on;
						} else {
							SysPersonConfig userPurview = UserContextUtil
									.getInstance()
									.getUserConfig(
											visitor,
											SysPersonConfig.SYS_CALENDAR_USER_PURVIEW);
							if (userPurview != null) {
								String userlist = userPurview.getValue();
								if (userlist != null) {
									String[] users = userlist.split(",");
									for (String item : users) {
										String userid = UserContextUtil
												.getInstance().getUserId(item);
										String currentUserid = UserContextUtil
												.getInstance()
												.getCurrentUserId();
										if (userid != null
												&& userid.equals(currentUserid)) {
											this.isWriter = SysConst.on;
											break;
										}

									}
								}
							}

						}
					}
				}
				if (isShare != null && isShare.equals(SysConst.on)) {
					if (currentTime == null) {
						currentTime = UtilDate.dateFormat(new Date());
					}
					return SUCCESS;
				}
			}
		}
		tipsInfo = "指定用户未开放日志权限，无法查看";
		return ERROR;
	}

	public String showVisitor() {

		return SUCCESS;
	}

	public String settingIndex() {
		SysPersonConfig defaultConfig = UserContextUtil
				.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_DEFAULT_VIEW);
		if (defaultConfig != null) {
			defaultView = defaultConfig.getValue();
		}

		SysPersonConfig isShareConfig = UserContextUtil.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_IS_SHARE);
		if (isShareConfig != null) {
			isShare = Long.parseLong(isShareConfig.getValue());
		}

		SysPersonConfig isWriterConfig = UserContextUtil.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_IS_WRITER);
		if (isWriterConfig != null) {
			isWriter = Long.parseLong(isWriterConfig.getValue());
		}
		SysPersonConfig purviewConfig = UserContextUtil
				.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_TYPE_PURVIEW);
		if (purviewConfig != null) {
			purviewType = purviewConfig.getValue();
		}
		SysPersonConfig purviewUserConfig = UserContextUtil
				.getInstance()
				.getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_USER_PURVIEW);
		if (purviewUserConfig != null) {
			purviewUser = purviewUserConfig.getValue();
		}

		if (defaultView == null)
			defaultView = "month";
		// if(sendSysmsg==null)sendSysmsg=SysConst.on;
		// if(sendEmail==null)sendEmail=SysConst.off;
		if (isShare == null)
			isShare = SysConst.on;
		if (isWriter == null)
			isWriter = SysConst.off;
		if (purviewType == null)
			purviewType = "all";
		// if(sendSms==null)sendSms=SysConst.off;

		return SUCCESS;
	}

	public void doSetting() {
		if (defaultView == null)
			defaultView = "month";
		if (isShare == null)
			isShare = SysConst.on;
		if (isWriter == null)
			isWriter = SysConst.off;
		if (purviewType == null)
			purviewType = "all";

		SysPersonConfig defaultConfig = new SysPersonConfig();
		defaultConfig.setUserid(UserContextUtil.getInstance()
				.getCurrentUserId());
		defaultConfig.setType(SysPersonConfig.SYS_CALENDAR_DEFAULT_VIEW);
		defaultConfig.setValue(defaultView);
		UserContextUtil.getInstance().setCurrentUserConfig(
				SysPersonConfig.SYS_CALENDAR_DEFAULT_VIEW, defaultConfig);

		SysPersonConfig isShareConfig = new SysPersonConfig();
		isShareConfig.setUserid(UserContextUtil.getInstance()
				.getCurrentUserId());
		isShareConfig.setType(SysPersonConfig.SYS_CALENDAR_IS_SHARE);
		isShareConfig.setValue(isShare + "");
		UserContextUtil.getInstance().setCurrentUserConfig(
				SysPersonConfig.SYS_CALENDAR_IS_SHARE, isShareConfig);

		SysPersonConfig isPurviewConfig = new SysPersonConfig();
		isPurviewConfig.setUserid(UserContextUtil.getInstance()
				.getCurrentUserId());
		isPurviewConfig.setType(SysPersonConfig.SYS_CALENDAR_TYPE_PURVIEW);
		isPurviewConfig.setValue(purviewType);
		UserContextUtil.getInstance().setCurrentUserConfig(
				SysPersonConfig.SYS_CALENDAR_TYPE_PURVIEW, isPurviewConfig);

		SysPersonConfig userPurviewConfig = new SysPersonConfig();
		userPurviewConfig.setUserid(UserContextUtil.getInstance()
				.getCurrentUserId());
		userPurviewConfig.setType(SysPersonConfig.SYS_CALENDAR_USER_PURVIEW);
		userPurviewConfig.setValue(purviewUser + "");
		UserContextUtil.getInstance().setCurrentUserConfig(
				SysPersonConfig.SYS_CALENDAR_USER_PURVIEW, userPurviewConfig);

		SysPersonConfig isWriterConfig = new SysPersonConfig();
		isWriterConfig.setUserid(UserContextUtil.getInstance()
				.getCurrentUserId());
		isWriterConfig.setType(SysPersonConfig.SYS_CALENDAR_IS_WRITER);
		isWriterConfig.setValue(isWriter + "");
		UserContextUtil.getInstance().setCurrentUserConfig(
				SysPersonConfig.SYS_CALENDAR_IS_WRITER, isWriterConfig);
		ResponseUtil.write(SUCCESS);
		// if(sendSysmsg==null)sendSysmsg=SysConst.on;
		// if(sendEmail==null)sendEmail=SysConst.off;

		// if(sendSms==null)sendSms=SysConst.off;

	}

	/**
	 * !获得时间段内指定用户的一般日程
	 * 
	 * @return
	 */
	public void showPeriodJsonData() throws Exception {
		if (visitor == null) {
			UserContext uc = UserContextUtil.getInstance()
					.getCurrentUserContext();
			/*userid = uc.get_userModel().getUserid();*/
			userid = uc.get_userModel().getUsername();
		} else {
			userid = visitor;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CalendarUtil cu = new CalendarUtil();
		String startTemp = cu.getDateFromSeconds(this.getStart());// 将得到的毫秒数转为日期
		String endTemp = cu.getDateFromSeconds(this.getEnd());
		Date startDate = sdf.parse(startTemp);
		Date endDate = sdf.parse(endTemp);
		String data = appointmentService
				.getJsonData(startDate, endDate, userid);
		String periodJsonData = "[" + data.substring(1, data.length() - 1)
				+ "]";
		periodJsonData = periodJsonData.replaceAll("\"false\"", "false");
		periodJsonData = periodJsonData.replaceAll("\"true\"", "true");
		ResponseUtil.write(periodJsonData);
	}

	public void showOtherJson() {
		String json = appointmentService.getOtherUserJson();
		ResponseUtil.write(json);

	}

	/**
	 * !获得时间段内指定用户的重复事件
	 * 
	 * @return
	 */
	public void showPeriodJsonData_Repeate() throws Exception {
		if (visitor == null) {
			UserContext uc = UserContextUtil.getInstance()
					.getCurrentUserContext();
			visitor = uc.get_userModel().getUserid();
			visitor = uc.get_userModel().getUsername();
		}
		userid = visitor;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CalendarUtil cu = new CalendarUtil();
		String startTemp = cu.getDateFromSeconds(this.getStart());
		String endTemp = cu.getDateFromSeconds(this.getEnd());
		Date startDate = sdf.parse(startTemp);
		Date endDate = sdf.parse(endTemp);
		String data = appointmentService.getJsonData_Repeate(startDate,
				endDate, userid);
		String periodJsonData = "[" + data.substring(1, data.length() - 1)
				+ "]";
		periodJsonData = periodJsonData.replaceAll("\"false\"", "false");
		periodJsonData = periodJsonData.replaceAll("\"true\"", "true");
		HttpServletRequest request = ServletActionContext.getRequest();
		ResponseUtil.write(periodJsonData);
	}

	/**
	 * !增加一条记录
	 * 
	 * @return
	 */
	public String addSchCalendar() throws Exception {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (visitor == null) {
			userid = uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		Appointment model = new Appointment();
		model.setSzr(userid);
		if (null != this.getStartDate() && !"".equals(this.getStartDate())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			model.setSzsj(new Date());
			model.setJyr(sdf.parse(jyr));
			if (null != this.getEndDate() && !"".equals(this.getEndDate())) {
				Date endTemp = sdf.parse(this.getEndDate());
			}
		}
		this.setAppointment(model);
		return SUCCESS;
	}
	
	public String addappointment() throws Exception {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (visitor == null) {
			userid = uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		Appointment model = new Appointment();
		model.setSzr(uc._userModel.getUsername());
		if (null != this.getStartDate()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			model.setJyr(startTemp);
		}
		this.setAppointment(model);
		return SUCCESS;
	}

	/**
	 * !增加一条记录高级选项
	 * 
	 * @return
	 */
	public String addSchCalendar_Advance() throws Exception {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (visitor == null) {
			userid = uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		Appointment model = new Appointment();
		this.setAppointment(model);
		return SUCCESS;
	}

	/**
	 * !保存
	 * 
	 * @return
	 */
	public String saveAppointment() throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(this.getAppointment().getJyr());
		Map params = new HashMap();
		params.put(1, format);
		Long yId = DBUTilNew.getLong("ID","select ID from BD_XP_JYR where jyr= to_date ( ? ,' yyyy-MM-dd')", params);
		if (null == this.getAppointment().getId()&&yId==0) {
			Appointment model = this.getAppointment();
			// 判断如果当前用户与插入信息用户不一致，则标题中需要体现当前用户信息
			UserContext uc = UserContextUtil.getInstance()
					.getCurrentUserContext();
			String userid = uc.get_userModel().getUserid();
			model.setSzsj(new Date());
			appointmentService.save(model);
		}else if(this.getAppointment().getId()==null&&yId!=0){
			Appointment boData = appointmentService.getBoData(yId);
			this.getAppointment().setSzsj(new Date());
			this.getAppointment().setId(boData.getId());
			appointmentService.update(this.getAppointment());
		}else {
			this.getAppointment().setSzsj(new Date());
			appointmentService.update(this.getAppointment());
		}
		return SUCCESS;
	}

	/**
	 * !编辑加载一个日程
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadEvent() throws Exception {
		return SUCCESS;
	}

	/**
	 * !编辑加载一个日程高级选项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadEvent_Advance() throws Exception {
		String id = this.getId();
		Appointment model = appointmentService.getModel(id);
		this.setAppointment(model);
		return SUCCESS;
	}

	/**
	 * !删除
	 * 
	 * @return
	 */
	public String deleteAppointment() throws Exception {
		appointmentService.delete(this.getAppointment());
		return SUCCESS;
	}

	/**
	 * 编辑加载循环模式
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editRepeateMode() throws Exception {
		String id = this.getId();
		if (id != null && !id.equals("")) {
			Appointment model = new Appointment();
			model.setId(Long.parseLong(id));
			this.setAppointment(model);
		}
		return SUCCESS;
	}

	/**
	 * 添加循环模式
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addRepeateMode() throws Exception {
		Appointment model = new Appointment();
		if (null != this.getStartDate() && !"".equals(this.getStartDate())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			/*model.setReStartdate(startTemp);*/
			if (null != this.getEndDate() && !"".equals(this.getEndDate())) {
				Date endTemp = sdf.parse(this.getEndDate());
			/*	model.setReEnddate(endTemp);*/
			}
		}
		this.setAppointment(model);
		return SUCCESS;
	}

	/**
	 * !初始化加载日历
	 * 
	 * @return
	 */
	private String roleid;
	
	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String searchCalendar() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		 roleid = uc.get_orgRole().getId();  
		Date startTemp = null;
		Date endTemp = null;
		try {
			if (null != this.getStartDate() && !"".equals(this.getStartDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				startTemp = sdf.parse(this.getStartDate());
				if (null != this.getEndDate() && !"".equals(this.getEndDate())) {
					endTemp = sdf.parse(this.getEndDate());
				}

			}
		}
			catch (ParseException e) {
			logger.error(e,e);
		}
		int totalRows = appointmentService.getWorkLogRow();
		totalLogPage = PageBean.countTotalPage(perLogPage, totalRows);
		if (currLogPage == 0) {
			this.setCurrLogPage(1);
		}
		final int offset = PageBean.countOffset(perLogPage, currLogPage); // 当前页开始记录
		int length = perLogPage; // 每页记录数
		final int currentPage = PageBean.countCurrentPage(currLogPage);
		if (totalRows <= length) {
			length = totalRows;
		}
		List<Map> list = appointmentService.getWorkLogList(perLogPage,
				currLogPage, username, startDate, endDate); // "一页"的记录

		PageBean pageBean = new PageBean();
		pageBean.setPageSize(perLogPage);
		pageBean.setCurrentPage(currentPage);
		pageBean.setTotalRows(totalRows);
		pageBean.setTotalPages(totalLogPage);
		pageBean.setList(list);
		pageBean.init();
		this.setLogListBean(pageBean);
		return SUCCESS;
	}

	/**
	 * 获得设计列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		Date startTemp = null;
		Date endTemp = null;
		try {
			if (null != this.getStartDate() && !"".equals(this.getStartDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				startTemp = sdf.parse(this.getStartDate());
				sdf.format(startTemp);
				if (null != this.getEndDate() && !"".equals(this.getEndDate())) {
					endTemp = sdf.parse(this.getEndDate());
					sdf.format(endTemp);
				}

			}
		}
			catch (ParseException e) {
			logger.error(e,e);
		}
		if (pageNumber == 0)
			pageNumber = 1;
		list = appointmentService.getWorkLogList(pageSize, pageNumber,
				username, startDate, endDate);
		totalNum = appointmentService.getTotalListSize(username, startDate,
				endDate);
		return SUCCESS;
	}

	/**
	 * 获得设计列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String miniWinlist() throws Exception {
		Date startTemp = null;
		Date endTemp = null;
		try {
//			if (username != null && !"".equals(username)) {
//				username = new String(username.getBytes("iso8859-1"), "UTF-8");
//			}
			if (null != this.getStartDate() && !"".equals(this.getStartDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				startTemp = sdf.parse(this.getStartDate());
				if (null != this.getEndDate() && !"".equals(this.getEndDate())) {
					endTemp = sdf.parse(this.getEndDate());
				}

			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		if (pageNumber == 0)
			pageNumber = 1;
		pageSize = 5;
		list = appointmentService.getWorkLogList(pageSize, pageNumber,
				username, startDate, endDate);
		totalNum = appointmentService.getTotalListSize(username, startDate,
				endDate);
		return SUCCESS;
	}

	/**
	 * 导出excel
	 * 
	 * @return
	 */
	public String doExcelExp() {
		HttpServletResponse response = ServletActionContext.getResponse();
		appointmentService.doExcelExp(response);
		return null;
	}

	// ====================POJO===============================
	public void setCurrLogPage(int currLogPage) {
		this.currLogPage = currLogPage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
