package com.iwork.plugs.calender.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.message.sysmsg.util.PageBean;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.constant.SysConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.calender.Util.CalendarUtil;
import com.iwork.plugs.calender.model.IworkSchCalendar;
import com.iwork.plugs.calender.service.SchCalendarService;
import com.iwork.plugs.meeting.util.UtilDate;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

public class SchCalendarAction extends ActionSupport{
	private static Logger logger = Logger.getLogger(SchCalendarAction.class);

	private static final long serialVersionUID = 1L;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private SchCalendarService schCalendarService;
	private IworkSchCalendar iworkSchCalendar;
	private String start;
	private String end;
	private String id;
	private String userid;
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
	private String wczt;
	private String wcqk;
	private String depname;
	private String ggnr;
	private String xmname;
	private String bmname;
	
	public String getXmname() {
		return xmname;
	}

	public void setXmname(String xmname) {
		this.xmname = xmname;
	}

	public String getBmname() {
		return bmname;
	}

	public void setBmname(String bmname) {
		this.bmname = bmname;
	}

	public String getGgnr() {
		return ggnr;
	}

	public void setGgnr(String ggnr) {
		this.ggnr = ggnr;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}

	private String username;
	private List<Map> list;

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getWczt() {
		return wczt;
	}

	public void setWczt(String wczt) {
		this.wczt = wczt;
	}

	public String getWcqk() {
		return wcqk;
	}

	public void setWcqk(String wcqk) {
		this.wcqk = wcqk;
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
			UserContext uc = UserContextUtil.getInstance().getUserContext(visitor);
			title = "<img width=\"30\" src=\"iwork_img/engine/calendar.png\">【" + uc._userModel.getUsername() + "】的工作日志";
			SysPersonConfig isShareConfig = UserContextUtil.getInstance().getUserConfig(visitor,SysPersonConfig.SYS_CALENDAR_IS_SHARE);
			SysPersonConfig isWriterConfig = UserContextUtil.getInstance().getUserConfig(visitor,SysPersonConfig.SYS_CALENDAR_IS_WRITER);
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
						SysPersonConfig purviewType = UserContextUtil.getInstance().getUserConfig(visitor,SysPersonConfig.SYS_CALENDAR_TYPE_PURVIEW);
						if (purviewType != null&& purviewType.getValue().equals("all")) {
							isWriter = SysConst.on;
						} else {
							SysPersonConfig userPurview = UserContextUtil.getInstance().getUserConfig(visitor,SysPersonConfig.SYS_CALENDAR_USER_PURVIEW);
							if (userPurview != null) {
								String userlist = userPurview.getValue();
								if (userlist != null) {
									String[] users = userlist.split(",");
									for (String item : users) {
										String userid = UserContextUtil.getInstance().getUserId(item);
										String currentUserid = UserContextUtil.getInstance().getCurrentUserId();
										if (userid != null&& userid.equals(currentUserid)) {
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
		SysPersonConfig defaultConfig = UserContextUtil.getInstance().getCurrentUserConfig(SysPersonConfig.SYS_CALENDAR_DEFAULT_VIEW);
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
	public void showPeriodJsonData() throws Exception{
		if (visitor == null) {
			UserContext uc = UserContextUtil.getInstance()
					.getCurrentUserContext();
			userid = uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CalendarUtil cu = new CalendarUtil();
		String startTemp = cu.getDateFromSeconds(this.getStart());//将得到的毫秒数转为日期
		String endTemp = cu.getDateFromSeconds(this.getEnd());
		Date startDate = sdf.parse(startTemp);
		Date endDate = sdf.parse(endTemp);
		String data = schCalendarService.getJsonData(startDate, endDate, userid);
		String periodJsonData = "["+data.substring(1,data.length()-1)+"]";
		periodJsonData = periodJsonData.replaceAll("\"false\"", "false");
		periodJsonData = periodJsonData.replaceAll("\"true\"", "true");
		ResponseUtil.write(periodJsonData);
	}

	public void showOtherJson() {
		String json = schCalendarService.getOtherUserJson();
		ResponseUtil.write(json);

	}

	/**
	 * !获得时间段内指定用户的重复事件
	 * @return
	 */
	public void showPeriodJsonData_Repeate() throws Exception{
		if (visitor == null) {
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			visitor = uc.get_userModel().getUserid();
		}
		userid = visitor;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CalendarUtil cu = new CalendarUtil();
		String startTemp = cu.getDateFromSeconds(this.getStart());
		String endTemp = cu.getDateFromSeconds(this.getEnd());
		Date startDate = sdf.parse(startTemp);
		Date endDate = sdf.parse(endTemp);
		String data = schCalendarService.getJsonData_Repeate(startDate, endDate, userid);
		String periodJsonData = "["+data.substring(1,data.length()-1)+"]";
		periodJsonData = periodJsonData.replaceAll("\"false\"", "false");
		periodJsonData = periodJsonData.replaceAll("\"true\"", "true");
		ResponseUtil.write(periodJsonData);
	}
	/**
	 * !增加一条记录
	 * @return
	 */
	public String addSchCalendar() throws Exception{
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (visitor == null) {
			userid = uc.get_userModel().getUserid()==null?"":uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		IworkSchCalendar model = new IworkSchCalendar();
		model.setUserid(userid);
		if(null!=this.getStartDate()&&!"".equals(this.getStartDate())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			model.setStartdate(startTemp);
			if(null!=this.getEndDate()&&!"".equals(this.getEndDate())){
				Date endTemp = sdf.parse(this.getEndDate());
				model.setEnddate(endTemp);
			}
			model.setStarttime(this.getStartTime());
			model.setEndtime(this.getEndTime());
		}
		if("true".equals(this.getAllDay())){
			model.setIsallday(new Long(1));
		}else{
			model.setIsallday(new Long(0));
		}
		this.setIworkSchCalendar(model);
		return SUCCESS;
	}
	/**
	 * !增加一条记录高级选项
	 * @return
	 */
	public String addSchCalendar_Advance() throws Exception{
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (visitor == null) {
			userid = uc.get_userModel().getUserid();
		} else {
			userid = visitor;
		}
		IworkSchCalendar model = new IworkSchCalendar();
		model.setUserid(userid);
		if(null!=this.getStartDate()&&!"".equals(this.getStartDate())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			model.setStartdate(startTemp);
			if(null!=this.getEndDate()&&!"".equals(this.getEndDate())){
				Date endTemp = sdf.parse(this.getEndDate());
				model.setEnddate(endTemp);
			}//reStartdate
			model.setStarttime(this.getStartTime());
			model.setEndtime(this.getEndTime());
			model.setReStartdate(new Date());
		}
		String allDayTemp = this.getAllDay();
		model.setIsallday(Long.parseLong(allDayTemp));
		if(null!=this.getTitle()){
			String titleTemp = this.getTitle();
			model.setTitle(java.net.URLDecoder.decode(titleTemp,"UTF-8"));
		}
		this.setIworkSchCalendar(model);
		return SUCCESS;
	}
	/**
	 * 首页日程查看
	 * @return
	 * @throws Exception
	 */
	public String open_Advance() throws Exception{
		String id = this.getId();
		if(id!=null&&!id.equals("")){
			IworkSchCalendar model = schCalendarService.getModel(id);
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	/**
	 * !保存
	 * @return
	 */
	public String saveSchCalendar() throws Exception{
		if(null==this.getIworkSchCalendar().getId()){
			IworkSchCalendar model = this.getIworkSchCalendar();
			// 判断如果当前用户与插入信息用户不一致，则标题中需要体现当前用户信息
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc.get_userModel().getUserid();
			if (!model.getUserid().equals(userid)) {
				String title = model.getTitle();
				title = uc.get_userModel().getUsername() + ":" + title;
				model.setTitle(title);
			}
			schCalendarService.save(model);
		}else{
			schCalendarService.update(this.getIworkSchCalendar());
		}
		/*//判断是否需要发送短信
		if (iworkSchCalendar.getIsalert() != null) {
			if (iworkSchCalendar.getIsalert() == 1) {
				SimpleDateFormat fTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String endtime=sdf.format(iworkSchCalendar.getEnddate()==null?iworkSchCalendar.getReEnddate():iworkSchCalendar.getEnddate());
				Date endDate=fTime.parse(endtime+" "+iworkSchCalendar.getEndtime());
				if(endDate.getTime()>new Date().getTime()){
					final UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
					OrgUser userModel = uc.get_userModel();
					final String userid = userModel.getUserid();
					String starttime=sdf.format(iworkSchCalendar.getStartdate()==null?iworkSchCalendar.getReStartdate():iworkSchCalendar.getStartdate());
					Date timeDate=fTime.parse(starttime+" "+iworkSchCalendar.getStarttime());
					Calendar calendar=null;
					calendar = Calendar.getInstance();
					calendar.setTime(timeDate);
				    String timeDateTxt=fTime.format(timeDate);
				    String tqTime=iworkSchCalendar.getAlerttime();
				    int mm=Integer.parseInt(tqTime);
				    calendar.add(calendar.MINUTE,-mm); 
				    String sendTime=fTime.format(calendar.getTime());
				    Date sendDate=fTime.parse(sendTime);
					final String smsContent = schCalendarService.tixingSMS(iworkSchCalendar.getTitle(),timeDateTxt);
					// 定时发送短信
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							UserContext target = UserContextUtil.getInstance().getUserContext(userid);
							OrgUser targetUserModel = target.get_userModel();
							String mobile = targetUserModel.getMobile();
							MessageAPI.getInstance().sendSMS(target, mobile, smsContent);
						}		
					},sendDate);
				}
			}
		}*/
		return SUCCESS;
	}
	public String saveSchCalendarzs() throws Exception{
		if(null==this.getIworkSchCalendar().getId()){
			IworkSchCalendar model = this.getIworkSchCalendar();
			if(isShare==0){
			
				model.setReStartdate(null);
				model.setReEnddate(null);
				model.setReMode(null);
			}
			// 判断如果当前用户与插入信息用户不一致，则标题中需要体现当前用户信息
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc.get_userModel().getUserid();
			if (!model.getUserid().equals(userid)) {
				String title = model.getTitle();
				title = uc.get_userModel().getUsername() + ":" + title;
				model.setTitle(title);
			}
			schCalendarService.save(model);
			schCalendarService.addGlb(instanceId,model);
			
		}else{
			schCalendarService.update(this.getIworkSchCalendar());
		}
		//判断是否需要发送短信
		/*if (iworkSchCalendar.getIsalert() != null) {
			if (iworkSchCalendar.getIsalert() == 1) {
				try {
					SimpleDateFormat fTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String endtime=sdf.format(iworkSchCalendar.getEnddate()==null?iworkSchCalendar.getReEnddate():iworkSchCalendar.getEnddate());
					Date endDate=fTime.parse(endtime+" "+iworkSchCalendar.getEndtime());
					if(endDate.getTime()>new Date().getTime()){
						final UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
						OrgUser userModel = uc.get_userModel();
						final String userid = userModel.getUserid();
						String starttime=sdf.format(iworkSchCalendar.getStartdate()==null?iworkSchCalendar.getReStartdate():iworkSchCalendar.getStartdate());
						Date timeDate=fTime.parse(starttime+" "+iworkSchCalendar.getStarttime());
						Calendar calendar=null;
						calendar = Calendar.getInstance();
						calendar.setTime(timeDate);
					    String timeDateTxt=fTime.format(timeDate);
					    String tqTime=iworkSchCalendar.getAlerttime();
					    int mm=Integer.parseInt(tqTime);
					    calendar.add(calendar.MINUTE,-mm); 
					    String sendTime=fTime.format(calendar.getTime());
					    Date sendDate=fTime.parse(sendTime);
						final String smsContent = schCalendarService.tixingSMS(iworkSchCalendar.getTitle(),timeDateTxt);
						// 定时发送短信
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							public void run() {
								UserContext target = UserContextUtil.getInstance().getUserContext(userid);
								OrgUser targetUserModel = target.get_userModel();
								String mobile = targetUserModel.getMobile();
								MessageAPI.getInstance().sendSMS(target, mobile, smsContent);
							}		
						},sendDate);
					}
				} catch (Exception e) {
					return null;
				}
			}
		}*/
		return null;
	}
	private String action;
	private Long instanceId;
	private String actStepDefId;
	
	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	/**
	 * 流程日志添加
	 */
	public void processAddlog(){
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String zqServer = config.get("zqServer");
		if(zqServer!=null&&!zqServer.equals("")&&zqServer.equals("hlzq")){
			return;
		}
		schCalendarService.processAddlog(action, title, instanceId, actStepDefId);
	}
	/**
	 * !编辑加载一个日程
	 * @return
	 * @throws Exception
	 */
	public String loadEvent() throws Exception{
		String id = this.getId();
		if(id!=null&&!id.equals("")){
			IworkSchCalendar model = schCalendarService.getModel(id);
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	
	public String loadEventweixin() throws Exception{
		
		if(id!=null&&!id.equals("")){
			IworkSchCalendar model = schCalendarService.getModel(id);
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	
	public String readloadEvent_Advance() throws Exception{
		String id = this.getId();
		if(id!=null&&!id.equals("")){
			IworkSchCalendar model = schCalendarService.getModel(id);
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	/**
	 * !编辑加载一个日程高级选项
	 * @return
	 * @throws Exception
	 */
	public String loadEvent_Advance() throws Exception{
		String id = this.getId();
		if(id!=null&&!id.equals("")){
			IworkSchCalendar model = schCalendarService.getModel(id);
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	public void loadEventzz() throws Exception{
		String  flag=schCalendarService.getCs(id);
		ResponseUtil.writeTextUTF8(flag);
	}
	/**
	 * !删除
	 * @return
	 */
	public String deleteSchCalendar() throws Exception{
		schCalendarService.delete(this.getIworkSchCalendar());
		return SUCCESS;
	}
	/**
	 * 编辑加载循环模式
	 * @return
	 * @throws Exception
	 */
	public String editRepeateMode() throws Exception{
		String id = this.getId();
		if(id!=null && !"".equals(id)){
			IworkSchCalendar model = new IworkSchCalendar();
			model.setId(Long.parseLong(id));
			this.setIworkSchCalendar(model);
		}
		return SUCCESS;
	}
	/**
	 * 添加循环模式
	 * @return
	 * @throws Exception
	 */
	public String addRepeateMode() throws Exception{
		IworkSchCalendar model = new IworkSchCalendar();
		if(null!=this.getStartDate()&&!"".equals(this.getStartDate())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTemp = sdf.parse(this.getStartDate());
			model.setReStartdate(startTemp);
			if(null!=this.getEndDate()&&!"".equals(this.getEndDate())){
				Date endTemp = sdf.parse(this.getEndDate());
				model.setReEnddate(endTemp);
			}
		}
		this.setIworkSchCalendar(model);
		return SUCCESS;
	}

	/**
	 * !初始化加载日历
	 * 
	 * @return
	 */
	public String searchCalendar() {
		Date startTemp = null;
		Date endTemp = null;
		try {
//			

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
		int totalRows = schCalendarService.getWorkLogRow();
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
		List<Map> list = schCalendarService.getWorkLogList(perLogPage,
				currLogPage, username, startDate, endDate,depname); // "一页"的记录

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
	
	private Long orgroleid;
	
	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}
	public String logbookXx(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
        orgroleid = user.getOrgroleid();
		Date startTemp = null;
		Date endTemp = null;
		try {
			if(username!=null){
				if(checkLoginInfo(username)){
		    		return ERROR;
		    	}
			}
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
		list = schCalendarService.getGzrzxx(pageSize, pageNumber,
				 startDate, endDate,ggnr,userid);
		totalNum = schCalendarService.getGzrzxxSize(startDate, endDate,ggnr,userid);
		return SUCCESS;
	}
	public String list() throws Exception {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
       
        orgroleid = user.getOrgroleid();
        
		Date startTemp = null;
		Date endTemp = null;
		try {
//			if (null != username && !"".equals(username)) {
//				username = new String(username.getBytes("iso8859-1"), "UTF-8");
//			}
			if(username!=null){
				if(checkLoginInfo(username)){
		    		return ERROR;
		    	}
			}
			

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
		xmname=username;
		bmname=depname;
		
		list = schCalendarService.getWorkLogList(pageSize, pageNumber,
				username, startDate, endDate,depname);
		totalNum = schCalendarService.getTotalListSize(username, startDate,
				endDate,depname);
		return SUCCESS;
	}

	private static boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return false;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
	/**
	 * 获得设计列表
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public String miniWinlist() throws Exception {
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
		} catch (ParseException e) {
			logger.error(e,e);
		}
		if (pageNumber == 0)
			pageNumber = 1;
		pageSize = 5;
		list = schCalendarService.getWorkLogList(pageSize, pageNumber,
				username, startDate, endDate,depname);
		totalNum = schCalendarService.getTotalListSize(username, startDate,
				endDate);
		return SUCCESS;
	}*/

	/**
	 * 导出excel
	 * 
	 * @return
	 */
	public String doExcelExp() {
		if (pageNumber == 0)
			pageNumber = 1;
		HttpServletResponse response = ServletActionContext.getResponse();
		schCalendarService.doExcelExp(response,username, depname);
		return null;
	}
//====================POJO===============================
	public void setCurrLogPage(int currLogPage) {
		this.currLogPage = currLogPage;
	}
	public SchCalendarService getSchCalendarService() {
		return schCalendarService;
	}
	public void setSchCalendarService(SchCalendarService schCalendarService) {
		this.schCalendarService = schCalendarService;
	}
	
	public IworkSchCalendar getIworkSchCalendar() {
		return iworkSchCalendar;
	}
	public void setIworkSchCalendar(IworkSchCalendar iworkSchCalendar) {
		this.iworkSchCalendar = iworkSchCalendar;
	}
	
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

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}

	public String getPurviewType() {
		return purviewType;
	}

	public String getPurviewUser() {
		return purviewUser;
	}

	public void setPurviewUser(String purviewUser) {
		this.purviewUser = purviewUser;
	}

	public void setPurviewType(String purviewType) {
		this.purviewType = purviewType;
	}

	public String getTipsInfo() {
		return tipsInfo;
	}
	private String text;
	
	public void edityj(){
		HashMap params=new HashMap();
		params.put(1,text);
		params.put(2,id);
		String sql="update  IWORK_SCH_CALENDAR set EXTENDS1=? where id=?";
		 DBUTilNew.update(sql, params);
		
		
		
	}
	

	public String edityijian(){
		
		
		return SUCCESS;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
