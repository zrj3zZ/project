package com.iwork.plugs.meeting.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.iwork.core.db.DBUtil;
import com.iwork.plugs.meeting.bean.MeetingTypebean;
import com.iwork.plugs.meeting.bean.MeetingZone;
import org.apache.log4j.Logger;
public class MeetingDataUtil {
	private static Logger logger = Logger.getLogger(MeetingDataUtil.class);
	private static Hashtable<String, String> TIME_INFO;
	private static Hashtable<String, Integer> TIME_INDEX;

	public HashMap getDateSelect() {
		Calendar cal = Calendar.getInstance();
		HashMap map = new HashMap();
		for (int i = 0; i < 15; i++) {
			map.put(new Integer(i),
					UtilDate.dateFormat(cal.getTime())
							+ " "
							+ UtilDate.getDayOfWeekSymbols(cal.get(1),
									cal.get(2) + 1, cal.get(5)));
			cal.add(5, 1);
		}
		return map;
	}

	public static int getAssemblyState(String meetingno, String date,
			String datetime) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String a = DBUtil.convertLongDate(date);
		int flag = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select MEETINGROOMNO from BO_MEETING_ROOM_ORDER where MEETINGROOMNO=? and STATUS='预定' and ?>=STARTDATE and ?<=ENDDATE ");
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, meetingno);
			ps.setString(2, DBUtil.convertLongDate(datetime));
			ps.setString(3, DBUtil.convertLongDate(datetime));
			rs = ps.executeQuery();

			if (rs.next()) {
				sql = new StringBuffer();
				String nowtime = UtilDate.datetimeFormat(new Date());
				sql.append("select MEETINGROOMNO from BO_MEETING_ROOM_ORDER where MEETINGROOMNO=? and STATUS='预定' and ?>=STARTDATE and ?<=ENDDATE");

				ps.close();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, meetingno);
				ps.setString(2, DBUtil.convertLongDate(datetime));
				ps.setString(3, DBUtil.convertLongDate(datetime));
				rs = ps.executeQuery();
				if (rs.next()) {
					flag = 2;
				} else {
					flag = 3;
				}
			} else {
				flag = 1;
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e,e);
			return 0;
		} finally {			
		DBUtil.close(conn, ps, rs);
		}
		return flag;
	}

	public static String getDqmc(String dqbh) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dqmc = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from BD_MEETING_ROOM_AREA where DQBH=?");
			conn = DBUtil.open();			
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, dqbh);
			rs = ps.executeQuery();
			if (rs.next()) {
				dqmc = rs.getString("DQMC");
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		return dqmc;
	}

	public static String[] getMeetingInfo(String meetingno) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] meetingInfo = new String[4];
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from BO_MEETINGROOM where MEETINGNO=?");
			conn = DBUtil.open();
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, meetingno);
			rs = ps.executeQuery();
			if (rs.next()) {
				meetingInfo[0] = rs.getString("MEETINGNAME");
				meetingInfo[1] = rs.getString("MEETINGDQBM");
				meetingInfo[2] = rs.getString("MEETINGDQ");
				meetingInfo[3] = rs.getString("MEETINGTYPE");
			} else {
				meetingInfo = null;
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		return meetingInfo;
	}

	public static Hashtable<String, String> getMeetingRoomsHashTable(
			String meetingDQBM) {

		TIME_INFO = new Hashtable<String, String>();

		int earlyTime = MeetingRoomUtil.getInstance().getMeetingRoomTime(
				meetingDQBM, "ZZYDSJ");
		int lateTime = MeetingRoomUtil.getInstance().getMeetingRoomTime(
				meetingDQBM, null);

		int temp = 0;
		for (int i = earlyTime; i <= lateTime; i++) {
			for (int j = 1; j <= 4; j++) {
				temp++;
				String str = "";
				if (j == 1)
					str = i + ":00";
				if (j == 2)
					str = i + ":15";
				if (j == 3)
					str = i + ":30";
				if (j == 4)
					str = i + ":45";
				TIME_INFO.put(temp + "", str);
			}
		}
		return TIME_INFO;
	}

	public static synchronized Hashtable<String, Integer> getTimeIndexHashtable(
			String meetingDQBM) {
		Iterator i;
		TIME_INDEX = new Hashtable();
		Hashtable roomHash = getMeetingRoomsHashTable(meetingDQBM);
		Set set = roomHash.entrySet();
		for (i = set.iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			TIME_INDEX.put((String) entry.getValue(),
					Integer.valueOf(Integer.parseInt((String) entry.getKey())));
		}
		return TIME_INDEX;
	}

	public ArrayList<MeetingZone> getKingsofZoneName() {
		ArrayList<MeetingZone> array = new ArrayList<MeetingZone>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM BD_MEETING_ROOM_AREA");
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				MeetingZone bean = new MeetingZone();
				bean.setMeetingdqbh(rs.getString("DQBH"));
				bean.setMeetingsqmc(rs.getString("DQMC"));
				array.add(bean);
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return array;
	}

	public String getSelectAssemblyName(String assemblyno) {
		ArrayList<MeetingTypebean> array = new ArrayList<MeetingTypebean>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String temp = "";
		try {
			StringBuffer sql = new StringBuffer("select MEETINGNO,MEETINGNAME,MEETINGDQBM from BD_MEETING_ROOM where MEETINGNO = ?");
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, assemblyno);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if ((rs.getString(1) != null) && (rs.getString(2) != null)) {
					String dqbh = rs.getString(3);
					int maxNum = MeetingRoomUtil.getInstance()
							.getMeetingTimeNum(dqbh);
					MeetingTypebean bean = new MeetingTypebean(maxNum);
					bean.setMeetingNo(rs.getString(1));
					bean.setMeetingName(rs.getString(2));
					temp = "<b>"
							+ rs.getString(2)
							+ "  "
							+ rs.getString(1)
							+ "</b><input type='hidden'  value='"
							+ rs.getString(2)
							+ "'> <input type='hidden' name='selectMeetingRoom' value='"
							+ rs.getString(1) + "'>";
					array.add(bean);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return temp;
	}

	public String getDepZone(String userid) {
		String depZone = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer("select id,alwaysarea from bo_hr_archives  where awsuid =?");
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				depZone = rs.getString(2);
			}
			DBUtil.close(conn, ps, rs);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, ps, rs);
		}
		return depZone;
	}
}