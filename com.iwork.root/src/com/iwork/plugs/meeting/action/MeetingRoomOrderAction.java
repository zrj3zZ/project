package com.iwork.plugs.meeting.action;

import java.util.Hashtable;
import java.util.Map;

import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.meeting.model.IworkMeetingRoomOrder;
import com.iwork.plugs.meeting.service.MeetingRoomService;
import com.iwork.plugs.meeting.util.MeetingRoomUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MeetingRoomOrderAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private MeetingRoomService meetingRoomService;

	private String meetingSelectZone;
	private String meetingSelectDate;
	private String mettingView;
	private int j;
	private String dqbh;
	private String assemblyno;
	private String date;
	private String meetRoomDialogResult;
	private String meetingRoomName;
	private String meetingRoomNo;
	private String currdate;
	private String dateOption;
	private String jssjValue;
	private int orderId;
	private IworkMeetingRoomOrder meetingRoomOrder;
	private String meetingtitle;
	private String endtime;
	private String beizhu;
	private String meetingpersons;
	private String lasttime;
	private String beforORafter;
	private String meetingTimeView;
	private int tableWidth;

	public String meetingRoomOrderList() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		Hashtable<String, String> meetingRoomStatus = meetingRoomService
				.getMeetingRoomsStatusList(user, dqbh, date, beforORafter);

		meetingSelectZone = meetingRoomStatus.get("meeting_select_zone");
		meetingSelectDate = meetingRoomStatus.get("meeting_select_date");
		mettingView = meetingRoomStatus.get("metting_view");
		meetingTimeView = meetingRoomStatus.get("meetingTimeView");
		tableWidth = MeetingRoomUtil.getInstance().getTableWidth(dqbh);

		// meetingRoomStatus.get("Select_Display_Script");
		// meetingRoomStatus.get("assemblyno");
		// meetingRoomStatus.get("assemblyno");
		// meetingRoomStatus.get("selectMeetingRoomMethod");
		// meetingRoomStatus.get("selectMeetingRoom");
		// meetingRoomStatus.get("selectMeetingRoomDate");
		// meetingRoomStatus.get("MeetingRoomNO");
		// meetingRoomStatus.get("dqbh");
		// meetingRoomStatus.get("dqmc");
		// meetingRoomStatus.get("selectMeetingZone");

		return SUCCESS;
	}

	public String meetingRoomTimeOrder() {
		Map<String, String> meetingRoomParameter = meetingRoomService
				.getSelectMeetRoomDialog(j, dqbh, assemblyno, date);

		meetingRoomName = meetingRoomParameter.get("meetingRoomName");
		meetingRoomNo = meetingRoomParameter.get("meetingRoomNo");
		currdate = meetingRoomParameter.get("currdate");
		dateOption = meetingRoomParameter.get("dateOption");
		jssjValue = meetingRoomParameter.get("jssjValue");

		return SUCCESS;
	}

	public String meetingRoomOrderOpen() {

		Map<String, String> meetingRoomStatus = meetingRoomService
				.getRoomOrder(orderId, dqbh);

		meetingRoomName = meetingRoomStatus.get("meetingRoomName");
		dateOption = meetingRoomStatus.get("dateOption");
		meetingtitle = meetingRoomStatus.get("meetingtitle");
		endtime = meetingRoomStatus.get("endtime");
		lasttime = meetingRoomStatus.get("lasttime");
		meetingpersons = meetingRoomStatus.get("meetingpersons");
		beizhu = meetingRoomStatus.get("beizhu");
		orderId = Integer.parseInt(meetingRoomStatus.get("orderId"));

		return SUCCESS;
	}

	public MeetingRoomService getMeetingRoomService() {
		return meetingRoomService;
	}

	public void setMeetingRoomService(MeetingRoomService meetingRoomService) {
		this.meetingRoomService = meetingRoomService;
	}

	public String getMeetingRoomName() {
		return meetingRoomName;
	}

	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

	public String getMeetingRoomNo() {
		return meetingRoomNo;
	}

	public void setMeetingRoomNo(String meetingRoomNo) {
		this.meetingRoomNo = meetingRoomNo;
	}

	public String getCurrdate() {
		return currdate;
	}

	public void setCurrdate(String currdate) {
		this.currdate = currdate;
	}

	public String getDateOption() {
		return dateOption;
	}

	public void setDateOption(String dateOption) {
		this.dateOption = dateOption;
	}

	public String getJssjValue() {
		return jssjValue;
	}

	public void setJssjValue(String jssjValue) {
		this.jssjValue = jssjValue;
	}

	public String getMeetRoomDialogResult() {
		return meetRoomDialogResult;
	}

	public void setMeetRoomDialogResult(String meetRoomDialogResult) {
		this.meetRoomDialogResult = meetRoomDialogResult;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getAssemblyno() {
		return assemblyno;
	}

	public void setAssemblyno(String assemblyno) {
		this.assemblyno = assemblyno;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public MeetingRoomService getmeetingRoomService() {
		return meetingRoomService;
	}

	public void setmeetingRoomService(MeetingRoomService meetingRoomService) {
		this.meetingRoomService = meetingRoomService;
	}

	public String getMeetingSelectZone() {
		return meetingSelectZone;
	}

	public void setMeetingSelectZone(String meetingSelectZone) {
		this.meetingSelectZone = meetingSelectZone;
	}

	public String getMeetingSelectDate() {
		return meetingSelectDate;
	}

	public void setMeetingSelectDate(String meetingSelectDate) {
		this.meetingSelectDate = meetingSelectDate;
	}

	public String getMettingView() {
		return mettingView;
	}

	public void setMettingView(String mettingView) {
		this.mettingView = mettingView;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public IworkMeetingRoomOrder getMeetingRoomOrder() {
		return meetingRoomOrder;
	}

	public void setMeetingRoomOrder(IworkMeetingRoomOrder meetingRoomOrder) {
		this.meetingRoomOrder = meetingRoomOrder;
	}

	public String getMeetingtitle() {
		return meetingtitle;
	}

	public void setMeetingtitle(String meetingtitle) {
		this.meetingtitle = meetingtitle;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMeetingpersons() {
		return meetingpersons;
	}

	public void setMeetingpersons(String meetingpersons) {
		this.meetingpersons = meetingpersons;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getBeforORafter() {
		return beforORafter;
	}

	public void setBeforORafter(String beforORafter) {
		this.beforORafter = beforORafter;
	}

	public String getMeetingTimeView() {
		return meetingTimeView;
	}

	public void setMeetingTimeView(String meetingTimeView) {
		this.meetingTimeView = meetingTimeView;
	}

	public int getTableWidth() {
		return tableWidth;
	}

	public void setTableWidth(int tableWidth) {
		this.tableWidth = tableWidth;
	}

}
