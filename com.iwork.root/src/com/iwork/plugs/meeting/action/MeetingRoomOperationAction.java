package com.iwork.plugs.meeting.action;

import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.meeting.model.IworkMeetingRoomOrder;
import com.iwork.plugs.meeting.service.MeetingRoomService;
import com.opensymphony.xwork2.ActionSupport;

public class MeetingRoomOperationAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MeetingRoomService meetingRoomService;

	private String selectMeetingRoom;
	private String beizhu;
	private String endtime;
	private String meetingdate;
	private String meetingpersons;
	private String meetingtitle;
	private String starttime;
	private String selectMeetingZone;
	private String jssj;
	private int orderId;
	private String dqbh;

	public String orderSubmit() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();

		String result = meetingRoomService.creatMeetingRoomOrder(user,
				selectMeetingRoom, meetingdate, starttime, endtime,
				meetingpersons, beizhu, meetingtitle,dqbh);


		ResponseUtil.writeTextUTF8(result);

		return null;
	}

	public String orderCancel() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();

		String result = meetingRoomService
				.cancelMeetingRoomOrder(user, orderId);


		ResponseUtil.writeTextUTF8(result);

		return null;
	}

	public String orderUpdate() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		selectMeetingZone = "0101";

		String result = meetingRoomService.updateMeetingRoomOrder(user,
				selectMeetingRoom, meetingdate, starttime, endtime,
				meetingpersons, beizhu, meetingtitle, selectMeetingZone,
				orderId, dqbh);


		ResponseUtil.writeTextUTF8(result);

		return null;
	}

	public String checkUser() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		IworkMeetingRoomOrder meetingRoomOrder = meetingRoomService
				.getRoomOrderById(orderId);

		if (user.getUserid().equals(meetingRoomOrder.getCreateuser())) {
			ResponseUtil.writeTextUTF8("ok");
		} else {
			ResponseUtil.writeTextUTF8("对不起，您无权取消或修改该会议室预订！");
		}

		return null;
	}

	public MeetingRoomService getmeetingRoomService() {
		return meetingRoomService;
	}

	public void setmeetingRoomService(MeetingRoomService meetingRoomService) {
		this.meetingRoomService = meetingRoomService;
	}

	public MeetingRoomService getMeetingRoomService() {
		return meetingRoomService;
	}

	public void setMeetingRoomService(MeetingRoomService meetingRoomService) {
		this.meetingRoomService = meetingRoomService;
	}

	public String getSelectMeetingRoom() {
		return selectMeetingRoom;
	}

	public void setSelectMeetingRoom(String selectMeetingRoom) {
		this.selectMeetingRoom = selectMeetingRoom;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMeetingdate() {
		return meetingdate;
	}

	public void setMeetingdate(String meetingdate) {
		this.meetingdate = meetingdate;
	}

	public String getMeetingpersons() {
		return meetingpersons;
	}

	public void setMeetingpersons(String meetingpersons) {
		this.meetingpersons = meetingpersons;
	}

	public String getMeetingtitle() {
		return meetingtitle;
	}

	public void setMeetingtitle(String meetingtitle) {
		this.meetingtitle = meetingtitle;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getSelectMeetingZone() {
		return selectMeetingZone;
	}

	public void setSelectMeetingZone(String selectMeetingZone) {
		this.selectMeetingZone = selectMeetingZone;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

}
