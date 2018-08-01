package com.iwork.plugs.meeting.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iwork.core.db.DBUtil;
import com.iwork.plugs.meeting.bean.MeetingBlock;
import com.iwork.plugs.meeting.bean.MeetingTypebean;
import com.iwork.plugs.meeting.bean.MeetingZone;

public class MeetingRoomUtil {

	private static MeetingRoomUtil instance = null;

	private MeetingRoomUtil() {
	}

	public static MeetingRoomUtil getInstance() {
		if (instance == null) {
			instance = new MeetingRoomUtil();
		}
		return instance;
	}

	public String getSelectTime(String[] statictime, int j) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < statictime.length; i++) {
			if (i == j)
				str.append("<option value='" + statictime[i] + "' selected >"
						+ statictime[i] + "</option>");
			else {
				str.append("<option value='" + statictime[i] + "'>"
						+ statictime[i] + "</option>");
			}
		}
		return str.toString();
	}

	public String getSelectTime(String statictimeStr, String meetingDQBM) {
		StringBuffer str = new StringBuffer();
		String[] statictime = this.getMeetingRoomTimestamp(meetingDQBM);
		for (int i = 0; i < statictime.length; i++) {
			if (statictimeStr.equals(statictime[i]))
				str.append("<option value='" + statictime[i] + "' selected >"
						+ statictime[i] + "</option>");
			else if (statictime[i].trim().length() > 0) {
				str.append("<option value='" + statictime[i] + "'>"
						+ statictime[i] + "</option>");
			}
		}
		return str.toString();
	}

	public String getEndtime(String starttime) {
		String[] temptime = starttime.split(":");
		String endtime = "";

		temptime[0] = Integer.toString(Integer.parseInt(temptime[0]) + 1);
		endtime = temptime[0] + ":" + temptime[1];
		return endtime;
	}

	public String getSelectMeetingZone(ArrayList<MeetingZone> zonelist,
			String tempdqbh) {
		StringBuffer selectmeetingzone = new StringBuffer();
		selectmeetingzone
				.append("<select name=\"Select_MeetingZone\"  onchange=\"selectMeetingRoomZoneDestinetemp(frmMain,'Select_MeetingRoom')\" language=\"javascript\" id=\"Select_MeetingZone\" style=\"font-family:新宋体;width:60px;\">");

		if (zonelist != null) {
			Iterator<MeetingZone> iterator = zonelist.iterator();
			while (iterator.hasNext()) {
				MeetingZone bean = iterator.next();
				if (tempdqbh.equals(bean.getMeetingdqbh()))
					selectmeetingzone
							.append("<option value=" + bean.getMeetingdqbh()
									+ " selected>")
							.append(bean.getMeetingsqmc()).append("</option>");
				else
					selectmeetingzone
							.append("<option value=" + bean.getMeetingdqbh()
									+ ">").append(bean.getMeetingsqmc())
							.append("</option>");
			}
		} else {
			selectmeetingzone.append("<option value=\"\">地区暂时无法使用</option>");
		}
		selectmeetingzone.append("</select>");
		return selectmeetingzone.toString();
	}

	public static String getDayOfweek(int i) {
		String dayofweek = "";

		if (i == 7) {
			dayofweek = "星期六";
		}
		if (i == 1) {
			dayofweek = "星期日";
		}
		if (i == 2) {
			dayofweek = "星期一";
		}
		if (i == 3) {
			dayofweek = "星期二";
		}
		if (i == 4) {
			dayofweek = "星期三";
		}
		if (i == 5) {
			dayofweek = "星期四";
		}
		if (i == 6) {
			dayofweek = "星期五";
		}
		return dayofweek;
	}

	public String getMeetingRoomsListHtml(List<MeetingTypebean> beans, String date,
			String uid) {
		
		StringBuffer roomStatusHtml = new StringBuffer();

		int times = 0;
		if ((beans != null) && (beans.size() != 0)) {
			int roomIndex = 0;
			for (MeetingTypebean bean : beans) {
				int blockIndex = 0;
				String roomStyle = roomIndex % 2 == 0 ? "s3" : "s4";

				String roomColor = roomIndex % 2 == 0 ? "#FFFFFF" : "#F7F8FA";
				roomIndex++;

				roomStatusHtml.append("<tr>\n");
				roomStatusHtml
						.append("<td width='150' height=\"19\" bgcolor=\""
								+ roomColor
								+ "\">&nbsp;&nbsp;<a href=### title=' + "
								+ bean.getTitle()
								+ "'><font size=\"-1\" class=\"STYLE2\">");
				roomStatusHtml.append(bean.getMeetingName()).append(
						"</font></td>");

				// String funcName = bean.isApprovalNedded() ? "cMRT" : "cMRT2";
				String funcName = "getOneRoom";

				MeetingBlock[] blocks = bean.getBlocks();
				for (MeetingBlock block : blocks) {
					String blockStyle = block.getBlockIndex() % 2 == 0 ? "s2"
							: "s1";
					String ableAss = "";
					blockIndex++;

					if (block.isReserved()) {
						ableAss = "<input type=\"button\"  class=\""
								+ blockStyle + "\" title='"
								+ block.getBlockTitle() + "' "
								+ block.getUnsubscribeScript(uid) + ">";
					} else {
						ableAss = "<input type=\"button\" onClick=\""
								+ funcName + "(" + blockIndex + ",'" + date
								+ "','" + bean.getMeetingNo()
								+ "');\" class=\"" + roomStyle + "\" title='"
								+ block.getBlockTitle() + "'/>";
					}

					roomStatusHtml.append("<td>").append(ableAss)
							.append("</td>\n");
				}

				roomStatusHtml.append("</tr>");
			}
		}
		return roomStatusHtml.toString();
	}

	public static String getFinaltime(String time, String addtime,
			String meetingDQBM) {
		String finaltime = "";
		String[] temptime = time.split(":");
		String[] tempaddtime = addtime.split(":");
		int tempmin = Integer.parseInt(temptime[1])
				+ Integer.parseInt(tempaddtime[1]);

		int toadyFinaltime = MeetingRoomUtil.getInstance()
				.getMeetingRoomTime(meetingDQBM, null) - 1;
		String toadyFinaltimeStr = toadyFinaltime + ":45";

		if (tempmin > 60) {
			tempmin -= 60;
			int tempHour = Integer.parseInt(temptime[0])
					+ Integer.parseInt(tempaddtime[0]) + 1;
			if (tempHour >= toadyFinaltime + 1)
				finaltime = toadyFinaltimeStr;
			else if ((tempHour == toadyFinaltime) && (tempmin >= 45))
				finaltime = toadyFinaltimeStr;
			else
				finaltime = String.valueOf(tempHour) + ":"
						+ String.valueOf(tempmin);
		} else if (tempmin == 60) {
			int tempHour = Integer.parseInt(temptime[0])
					+ Integer.parseInt(tempaddtime[0]) + 1;
			if (tempHour >= toadyFinaltime + 1)
				finaltime = toadyFinaltimeStr;
			else if ((tempHour == toadyFinaltime) && (tempmin >= 45))
				finaltime = toadyFinaltimeStr;
			else
				finaltime = String.valueOf(tempHour) + ":00";
		} else if (tempmin == 0) {
			int tempHour = Integer.parseInt(temptime[0])
					+ Integer.parseInt(tempaddtime[0]);
			if (tempHour >= toadyFinaltime + 1)
				finaltime = toadyFinaltimeStr;
			else if ((tempHour == toadyFinaltime) && (tempmin >= 45))
				finaltime = toadyFinaltimeStr;
			else
				finaltime = String.valueOf(tempHour) + ":00";
		} else {
			int tempHour = Integer.parseInt(temptime[0])
					+ Integer.parseInt(tempaddtime[0]);
			if (tempHour >= toadyFinaltime + 1)
				finaltime = toadyFinaltimeStr;
			else if ((tempHour == toadyFinaltime) && (tempmin >= 45))
				finaltime = toadyFinaltimeStr;
			else {
				finaltime = String.valueOf(tempHour) + ":"
						+ String.valueOf(tempmin);
			}
		}
		return finaltime;
	}

	public String getLastTime(String lasttime) {
		StringBuffer str = new StringBuffer();
		String[] statictime = { "0.5小时", "1小时", "1.5小时", "2小时", "2.5小时", "3小时",
				"3.5小时", "4小时", "4.5小时", "5小时", "6小时", "7小时", "8小时", "9小时",
				"10小时", "11小时", "12小时" };
		String[] timevalue = { "00:30", "01:00", "01:30", "02:00", "02:30",
				"03:00", "03:30", "04:00", "04:30", "05:00", "06:00", "07:00",
				"08:00", "09:00", "10:00", "11:00", "12:00" };
		for (int i = 0; i < timevalue.length; i++) {
			if (lasttime.equals(timevalue[i]))
				str.append("<option value='" + timevalue[i] + "' selected >"
						+ statictime[i] + "</option>");
			else {
				str.append("<option value='" + timevalue[i] + "'>"
						+ statictime[i] + "</option>");
			}
		}
		return str.toString();
	}

	public String getPerpomNum(String personnum) {
		StringBuffer str = new StringBuffer();
		String[] statictime = { "5人以下", "5-10人", "11-15人", "16-20人", "21-25人",
				"26-30人", "31-40人", "41-100人", "100人以上" };
		String[] timevalue = { "5人以下", "10人以下", "10-15人", "15-20人", "20-25人",
				"25-30人", "30-40人", "40-100人", "100人以上" };
		for (int i = 0; i < timevalue.length; i++) {
			if (personnum.equals(timevalue[i]))
				str.append("<option value='" + timevalue[i] + "' selected >"
						+ statictime[i] + "</option>");
			else {
				str.append("<option value='" + timevalue[i] + "'>"
						+ statictime[i] + "</option>");
			}
		}
		return str.toString();
	}

	public String getMeetingTimeView(String meetingDQBM) {

		int earlyTime = this.getMeetingRoomTime(meetingDQBM, "early");
		int lateTime = this.getMeetingRoomTime(meetingDQBM, null);

		StringBuffer result = new StringBuffer();
		for (int i = earlyTime; i < lateTime; i++) {
			result.append(this.getTimeTitleHtml(i));
		}

		return result.toString();
	}

	public int haveNumStr(String str) {

		if (str != null && str.trim().length() > 0) {
			for (int i = 5; i <= 10; i++) {
				String testStr = i + "";
				if (str.indexOf(testStr) > 0) {
					return i;
				}
			}
		}

		return 7;
	}

	public String getTimeTitleHtml(int time) {

		StringBuffer sb = new StringBuffer();
		sb.append("<td align='left' colspan=\"4\" class=\"myTaskTitle\" style=\"width: 59px;\">");
		sb.append(time + ":00");
		sb.append("</td>");
		return sb.toString();
	}

	public int getMeetingTimeNum(String meetingDQBM) {

		int earlyTime = this.getMeetingRoomTime(meetingDQBM, "early");
		int lateTime = this.getMeetingRoomTime(meetingDQBM, null);
		return (lateTime - earlyTime) * 4;
	}

	public String[] getMeetingRoomTimestamp(String meetingDQBM) {

		StringBuffer result = new StringBuffer();

		int earlyTime = this.getMeetingRoomTime(meetingDQBM, "early");
		int lateTime = this.getMeetingRoomTime(meetingDQBM, null);
		result.append(" ###");
		for (int i = earlyTime; i <= lateTime; i++) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					result.append(i + ":00");
				if (j == 2)
					result.append(i + ":15");
				if (j == 3)
					result.append(i + ":30");
				if (j == 4)
					result.append(i + ":45");

				if (i == lateTime && j == 4)
					result.append("");
				else
					result.append("###");

			}
		}

		return result.toString().split("###");
	}

	public int getMeetingRoomTime(String meetingDQBM, String timeStr) {
		String sql = "SELECT * FROM BD_MEETING_ROOM_AREA where dqbh='"
				+ meetingDQBM + "'";
		String resultStr = "";
		if (timeStr != null) {
			resultStr = DBUtil.getString(sql, "ZZYDSJ");
			return this.haveNumStr(resultStr);
		} else {
			resultStr = DBUtil.getString(sql, "ZWYDSJ");
			return this.haveNumStr(resultStr) + 12;
		}
	}

	public int getTableWidth(String meetingDQBM){
		
		if(meetingDQBM == null){
			meetingDQBM = "010";
		}
		int earlyTime = this.getMeetingRoomTime(meetingDQBM, "early");
		int lateTime = this.getMeetingRoomTime(meetingDQBM, null);
		int result = 0;
		
		result = 180 + (lateTime - earlyTime) * 60;
		
		return result;
	}
	
}
