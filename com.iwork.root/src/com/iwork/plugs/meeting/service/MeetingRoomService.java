package com.iwork.plugs.meeting.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.plugs.meeting.bean.MeetingTypebean;
import com.iwork.plugs.meeting.bean.MeetingZone;
import com.iwork.plugs.meeting.dao.MeetingRoomDao;
import com.iwork.plugs.meeting.model.IworkMeetingRoomOrder;
import com.iwork.plugs.meeting.util.MeetingDataUtil;
import com.iwork.plugs.meeting.util.MeetingRoomUtil;
import com.iwork.plugs.meeting.util.MeetingViewUtil;
import com.iwork.plugs.meeting.util.UtilDate;
import org.apache.log4j.Logger;
public class MeetingRoomService {

	MeetingRoomDao meetingRoomDao;
	private static Logger logger = Logger.getLogger(MeetingRoomService.class);
	public MeetingRoomService() {
	};

	public Hashtable<String, String> getMeetingRoomsStatusList(OrgUser user,
			String dqbh, String dateStr, String beforORafter) {

		MeetingDataUtil date = new MeetingDataUtil();

		String userid = user.getUserid();

		if ((dqbh == null) || ("".equals(dqbh))) {
			dqbh = "010";
		}
		String dqmc = MeetingDataUtil.getDqmc(dqbh);
		if ((dqmc == null) || ("".equals(dqmc))) {
			dqmc = "北京";
		}

		ArrayList<MeetingZone> zonelist = date.getKingsofZoneName();

		String meeting_select_zone = MeetingRoomUtil.getInstance()
				.getSelectMeetingZone(zonelist, dqbh);

		MeetingViewUtil biz = new MeetingViewUtil();

		Date selectDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (dateStr != null && dateStr.trim().length() > 10) {
				dateStr = dateStr.substring(0, 10);
				selectDate = sdf.parse(dateStr);
			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(selectDate);
		if (beforORafter != null && "befor".equals(beforORafter))
			cal.add(Calendar.DAY_OF_MONTH, -1);
		if (beforORafter != null && "after".equals(beforORafter))
			cal.add(Calendar.DAY_OF_MONTH, +1);
		int dayinweek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		String cueDate = UtilDate.dateFormat(cal.getTime());
		cueDate = cueDate + " " + weeks[dayinweek];

		List<MeetingTypebean> meetingRoomsInfo = biz.getAssemblyInfo(
				cueDate.substring(0, 10), dqbh);

		String metting_view = MeetingRoomUtil.getInstance()
				.getMeetingRoomsListHtml(meetingRoomsInfo,
						cueDate.substring(0, 10), userid);

		String meetingTimeView = MeetingRoomUtil.getInstance()
				.getMeetingTimeView(dqbh);

		String meeting_select_date = cueDate;

		String Select_Display_Script = "";
		String assemblyno = "all";
		String selectMeetingRoomMethod = "";
		String selectMeetingRoom = "all";
		String selectMeetingRoomDate = cueDate;
		String MeetingRoomNO = "all";
		String selectMeetingZone = "all";

		Hashtable<String, String> hashTags = new Hashtable<String, String>();

		hashTags.put("meetingTimeView", meetingTimeView);
		hashTags.put("meeting_select_zone", meeting_select_zone);
		hashTags.put("meeting_select_date", meeting_select_date);
		hashTags.put("Select_Display_Script", Select_Display_Script);
		hashTags.put("assemblyno", assemblyno);

		hashTags.put("metting_view", metting_view);
		hashTags.put("assemblyno", assemblyno);
		hashTags.put("selectMeetingRoomMethod", selectMeetingRoomMethod);
		hashTags.put("selectMeetingRoom", selectMeetingRoom);
		hashTags.put("selectMeetingRoomDate", selectMeetingRoomDate);
		hashTags.put("MeetingRoomNO", MeetingRoomNO);

		hashTags.put("dqbh", dqbh);
		hashTags.put("dqmc", dqmc);
		hashTags.put("selectMeetingZone", selectMeetingZone);
		return hashTags;
	}

	@SuppressWarnings("static-access")
	public Map<String, String> getSelectMeetRoomDialog(int j, String dqbh,
			String assemblyno, String date) {

		Map<String, String> result = new HashMap<String, String>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)) - 1,
				Integer.parseInt(date.substring(8, 10)));

		String[] timestamp = MeetingRoomUtil.getInstance()
				.getMeetingRoomTimestamp(dqbh);

		// 会议室参数

		String meetingRoomSql = "select MEETINGNO,MEETINGNAME from BD_MEETING_ROOM where MEETINGNO = '"
				+ assemblyno + "'";

		String meetingRoomName = DBUtil
				.getString(meetingRoomSql, "MEETINGNAME");
		String meetingRoomNo = DBUtil.getString(meetingRoomSql, "MEETINGNO");

		// 日期参数
		String currdate = date + " "
				+ MeetingRoomUtil.getInstance().getDayOfweek(calendar.get(7));

		String dateOption = MeetingRoomUtil.getInstance().getSelectTime(
				timestamp, j);

		String jssjValue = MeetingRoomUtil.getInstance().getEndtime(
				timestamp[j]);

		result.put("meetingRoomName", meetingRoomName);
		result.put("meetingRoomNo", meetingRoomNo);
		result.put("currdate", currdate);
		result.put("dateOption", dateOption);
		result.put("jssjValue", jssjValue);

		return result;
	}

	public Map<String, String> getRoomOrder(int orderId, String dqbh) {

		IworkMeetingRoomOrder roomOrder = new IworkMeetingRoomOrder();
		Map<String, String> result = new HashMap<String, String>();
		String timestamptemp = "";
		String endtime = "";
		String meetingtitle = "";
		String beizhu = "";
		String lasttime = "";
		String meetingpersons = "";
		String assemblyno = "";

		if (orderId > 0) {
			roomOrder = meetingRoomDao.getIworkMeetingRoomOrder((long) orderId);
			timestamptemp = roomOrder.getStarttime();
			endtime = roomOrder.getEndtime();
			lasttime = roomOrder.getLasttime() == null ? "01:00" : roomOrder
					.getLasttime();
			meetingtitle = roomOrder.getMeetingtitle();
			beizhu = roomOrder.getMemo() == null ? "" : roomOrder.getMemo();
			meetingpersons = roomOrder.getMeetingpersons();
			assemblyno = roomOrder.getMeetingroomno();
		}

		MeetingDataUtil meetingDataUtil = new MeetingDataUtil();
		String meetingRoomName = meetingDataUtil
				.getSelectAssemblyName(assemblyno);
		String dateOption = MeetingRoomUtil.getInstance().getSelectTime(
				timestamptemp, dqbh);
		String returnLasttime = MeetingRoomUtil.getInstance().getLastTime(
				lasttime);
		String retuenMeetingpersons = MeetingRoomUtil.getInstance()
				.getPerpomNum(meetingpersons);

		result.put("meetingRoomName", meetingRoomName);
		result.put("meetingtitle", meetingtitle);
		result.put("dateOption", dateOption);
		result.put("lasttime", returnLasttime);
		result.put("endtime", endtime);
		result.put("meetingpersons", retuenMeetingpersons);
		result.put("beizhu", beizhu);
		result.put("orderId", orderId + "");

		return result;
	}

	@SuppressWarnings("unchecked")
	public String creatMeetingRoomOrder(OrgUser user, String selectMeetingRoom,
			String meetingdate, String starttime, String endtime,
			String meetingpersons, String beizhu, String meetingtitle,
			String meetingDQBM) {

		String departmentName = user.getDepartmentname();
		String ordername = user.getUsername();
		String lasttime = endtime;
		String officetel = user.getOfficetel() == null ? "" : user
				.getOfficetel();

		endtime = MeetingRoomUtil.getInstance().getFinaltime(starttime,
				endtime, meetingDQBM);

		Calendar calendar = Calendar.getInstance();
		meetingdate = meetingdate.substring(0, 10);
		calendar.set(Integer.parseInt(meetingdate.substring(0, 4)),
				Integer.parseInt(meetingdate.substring(5, 7)) - 1,
				Integer.parseInt(meetingdate.substring(8, 10)));
		String currdate = meetingdate;
		currdate = currdate + "　"
				+ MeetingRoomUtil.getInstance().getDayOfweek(calendar.get(7));

		Date dangqiand = null;
		Date sdstd = null;
		Date edetd = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dangqiandate = sdf.format(new Date());
		try {
			dangqiand = sdf.parse(dangqiandate);
			String startStr = meetingdate.trim() + " " + starttime + ":00";
			String endStr = meetingdate.trim() + " " + endtime + ":00";
			sdstd = sdf.parse(startStr);
			edetd = sdf.parse(endStr);
		} catch (Exception e) {
			logger.error(e,e);

			return "预订会议室流程异常，请与管理员联系！ ";
		}

		if (sdstd.before(dangqiand)) {
			return "会议开始时间已过期！ ";
		}

		if (edetd.before(dangqiand)) {
			return "会议开始时间已过期！ ";
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from IWORK_MEETING_ROOM_ORDER where MEETINGROOMNO=? and (not (STARTDATE >= ? or ENDDATE <= ?)) and status='预订'");
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, selectMeetingRoom);
			ps.setString(2, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(endtime).toString()));
			ps.setString(3, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(starttime).toString()));
			rs = ps.executeQuery();

			if (rs.next()) {
				return "此会议室在该时间段内已经被预订，请您选择其他预订时间！ ";
			}
		} catch (Exception e) {
			logger.error(e,e);
			return "预订会议室流程异常，请与管理员联系！ ";
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		int i = 0;
		Hashtable hash = new Hashtable();
		hash.put("MEETINGROOMNO", selectMeetingRoom);
		hash.put("STARTDATEA", meetingdate);
		hash.put("STARTTIME", starttime);
		hash.put("ENDDATEA", meetingdate);
		hash.put("ENDTIME", endtime);
		hash.put("MEETINGPERSONS", meetingpersons);
		hash.put("MEMO", beizhu);
		hash.put("MEETINGTITLE", meetingtitle);
		hash.put("STATUS", "预定");
		hash.put("STARTDATE", meetingdate + " " + starttime);
		hash.put("ENDDATE", meetingdate + " " + endtime);
		hash.put("MEETINGDATE", meetingdate);
		hash.put("DEPARTMENTNAME", departmentName);
		hash.put("ORDERNAME", ordername);
		hash.put("ORDERTEL", officetel);
		hash.put("LASTTIME", lasttime);
		hash.put("CREATEUSER", user.getUserid());

		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			IworkMeetingRoomOrder model = new IworkMeetingRoomOrder(new Date(),
					user.getUserid(), new Date(), ordername, meetingpersons,
					selectMeetingRoom, user.getDepartmentname(),
					ymd.parse(meetingdate), "预订", sdf.parse(meetingdate + " "
							+ starttime + ":00"), sdf.parse(meetingdate + " "
							+ endtime + ":00"), starttime, endtime,
					meetingtitle, beizhu, ymd.parse(meetingdate),
					ymd.parse(meetingdate), lasttime, "", user.getOfficetel(),
					"", 0);

			meetingRoomDao.saveMeetingRoomOrder(model);
			return "会议室预订成功！";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}

		return "预订会议室流程异常，请与管理员联系！";
	}

	public IworkMeetingRoomOrder getRoomOrderById(int orderId) {

		IworkMeetingRoomOrder roomOrder = new IworkMeetingRoomOrder();
		if (orderId > 0) {
			roomOrder = meetingRoomDao.getIworkMeetingRoomOrder((long) orderId);
		}
		return roomOrder;
	}

	public boolean isModify(String starttime, int orderId) {
		String timestamp = this.getRoomOrderById(orderId).getStarttime();
		return timestamp.equals(starttime);
	}

	public String updateMeetingRoomOrder(OrgUser user,
			String selectMeetingRoom, String meetingdate, String starttime,
			String endtime, String meetingpersons, String beizhu,
			String meetingtitle, String selectmeetingzone, int orderId,
			String meetingDQBM) {

		boolean ismodify = this.isModify(starttime, orderId);
		int i;
		if (ismodify) {
			String lasttime = endtime;

			endtime = MeetingRoomUtil.getInstance().getFinaltime(starttime,
					endtime, meetingDQBM);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(meetingdate.substring(0, 4)),
					Integer.parseInt(meetingdate.substring(5, 7)) - 1,
					Integer.parseInt(meetingdate.substring(8, 10)));
			String currdate = meetingdate;
			currdate = currdate
					+ "　"
					+ MeetingRoomUtil.getInstance().getDayOfweek(
							calendar.get(7));

			Date dangqiand = null;
			Date sdstd = null;
			Date edetd = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String dangqiandate = sdf.format(new Date());
			try {
				dangqiand = sdf.parse(dangqiandate);
				sdstd = sdf.parse(meetingdate + " " + starttime + ":00");
				edetd = sdf.parse(meetingdate + " " + endtime + ":00");
			} catch (Exception e) {
				logger.error(e,e);
				return "预订会议室流程异常，请与管理员联系！";
			}

			if (edetd.before(dangqiand)) {
				return "已过修改时间，不可修改！";
			}
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				StringBuffer sql = new StringBuffer();
				sql.append("select * from IWORK_MEETING_ROOM_ORDER where MEETINGROOMNO=? and (not (STARTDATE >= ? or ENDDATE <= ?)) and STATUS='预定' and id !=?");
				conn = DBUtil.open();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, selectMeetingRoom);
				ps.setString(2, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(endtime).toString()));
				ps.setString(3, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(starttime).toString()));
				ps.setInt(4, orderId);
				rs = ps.executeQuery();

				if (rs.next()) {
					return "此会议室在该时间段内已经被预订，请您选择其他预订时间！";
				}
			} catch (Exception e) {
				logger.error(e,e);
				return "预订会议室流程异常，请与管理员联系！";
			} finally {
				DBUtil.close(conn, ps, rs);
			}
			String sql = "update IWORK_MEETING_ROOM_ORDER set MEETINGPERSONS = '"
					+ meetingpersons
					+ "',STARTDATE =  "
					+ DBUtil.convertLongDate(new StringBuilder()
							.append(meetingdate).append(" ").append(starttime)
							.toString())
					+ ",ENDDATE ="
					+ DBUtil.convertLongDate(new StringBuilder()
							.append(meetingdate).append(" ").append(endtime)
							.toString())
					+ ",STARTTIME ='"
					+ starttime
					+ "',ENDTIME= '"
					+ endtime
					+ "',"
					+ " MEETINGTITLE = '"
					+ meetingtitle
					+ "', MEMO = '"
					+ beizhu
					+ "', MEETINGDATE = "
					+ DBUtil.convertLongDate(meetingdate)
					+ ", STARTDATEA = "
					+ DBUtil.convertLongDate(meetingdate)
					+ ", ENDDATEA = "
					+ DBUtil.convertLongDate(meetingdate)
					+ ", LASTTIME = '" + lasttime + "' where id=" + orderId;

			i = DBUtil.executeUpdate(sql);
			if (i > 0) {
				return "会议室预订修改成功！";
			}
			return "预订会议室流程异常，请与管理员联系！";
		}

		String lasttime = endtime;

		endtime = MeetingRoomUtil.getInstance().getFinaltime(starttime,
				endtime, meetingDQBM);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(meetingdate.substring(0, 4)),
				Integer.parseInt(meetingdate.substring(5, 7)) - 1,
				Integer.parseInt(meetingdate.substring(8, 10)));
		String currdate = meetingdate;
		currdate = currdate + "　"
				+ MeetingRoomUtil.getInstance().getDayOfweek(calendar.get(7));

		Date dangqiand = null;
		Date sdstd = null;
		Date edetd = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dangqiandate = sdf.format(new Date());
		try {
			dangqiand = sdf.parse(dangqiandate);
			sdstd = sdf.parse(meetingdate + " " + starttime + ":00");
			edetd = sdf.parse(meetingdate + " " + endtime + ":00");
		} catch (Exception e) {
			logger.error(e,e);
			return "预订会议室流程异常，请与管理员联系！";
		}

		if (sdstd.before(dangqiand)) {
			return "会议开始时间已过期！";
		}

		if (edetd.before(dangqiand)) {
			return "已过修改时间，不可修改！";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from IWORK_MEETING_ROOM_ORDER where MEETINGROOMNO=? and (not (STARTDATE >= ? or ENDDATE <= ?)) and STATUS='预定' and id !=?");
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(2, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(endtime).toString()));
			ps.setString(3, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(starttime).toString()));
			ps.setInt(4, orderId);
			ps.setString(1, selectMeetingRoom);
			rs = ps.executeQuery();

			if (rs.next()) {
				return "此会议室在该时间段内已经被预订，请您选择其他预订时间！";
			}
		} catch (Exception e) {
			logger.error(e,e);
			return "预订会议室流程异常，请与管理员联系！";
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		StringBuffer sql = new StringBuffer("UPDATE IWORK_MEETING_ROOM_ORDER SET MEETINGPERSONS = ?,STARTDATE = ?,ENDDATE =?,STARTTIME =?,ENDTIME= ?,MEETINGTITLE = ?, MEMO = ?, MEETINGDATE = ?, STARTDATEA = ?, ENDDATEA = ?, LASTTIME = ? WHERE ID=?");
		Map params = new HashMap();
		params.put(1, meetingpersons);
		params.put(2, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(starttime).toString()));
		params.put(3, DBUtil.convertLongDate(new StringBuilder().append(meetingdate).append(" ").append(endtime).toString()));
		params.put(4, starttime);
		params.put(5, endtime);
		params.put(6, meetingtitle);
		params.put(7, beizhu);
		params.put(8, DBUtil.convertLongDate(meetingdate));
		params.put(9, DBUtil.convertLongDate(meetingdate));
		params.put(10, DBUtil.convertLongDate(meetingdate));
		params.put(11, lasttime);
		params.put(12, orderId);
		i = com.iwork.commons.util.DBUtil.update(sql.toString(), params);
		if (i > 0) {
			return "会议室预订修改成功！";
		}
		return "预订会议室流程异常，请与管理员联系！";
	}

	public String cancelMeetingRoomOrder(OrgUser user, int orderId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sqlt = new StringBuffer("select * from IWORK_MEETING_ROOM_ORDER where id=?");
			Date sd = null;
			conn = DBUtil.open();
			ps = conn.prepareStatement(sqlt.toString());
			ps.setInt(1, orderId);
			rs = ps.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = UtilDate.datetimeFormat(new Date());
			Date nowd = sdf.parse(nowtime);
			if (rs.next()) {
				String sdstr = rs.getString("STARTDATE");
				String sds = sdstr.substring(0, sdstr.lastIndexOf("."));
				sd = sdf.parse(sds);
			}

			if (sd.before(nowd)) {
				return "已过退订时间,此会议室不可退订!";
			} else {
				StringBuffer sql = new StringBuffer("delete from IWORK_MEETING_ROOM_ORDER where id=" + orderId);
				int i = DBUtil.executeUpdate(sql.toString());
				if (i >= 0)
					return "会议室退订成功!";
				else
					return "会议室退订失败!";
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		return "会议室退订失败!";
	}

	public MeetingRoomDao getMeetingRoomDao() {
		return meetingRoomDao;
	}

	public void setMeetingRoomDao(MeetingRoomDao meetingRoomDao) {
		this.meetingRoomDao = meetingRoomDao;
	}
}
