package com.iwork.plugs.meeting.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.iwork.core.db.DBUtil;
import com.iwork.plugs.meeting.bean.MeetingBlock;
import com.iwork.plugs.meeting.bean.MeetingTypebean;
import org.apache.log4j.Logger;
public class MeetingViewUtil {
	private static Logger logger = Logger.getLogger(MeetingViewUtil.class);

	public List<MeetingTypebean> getAssemblyInfo(String date, String dqbh) {
		ArrayList<MeetingTypebean> meetRoomList = new ArrayList<MeetingTypebean>();
		LinkedHashMap beanHash = new LinkedHashMap();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select MEETINGNO, MEETINGNAME, MEETINGTYPE, DIMENSIONS,  MEETINGDQ, MEETINGWZ, MEETINGSIZE, MEETINGCONF, IFSP, ASSETNO  from BD_MEETING_ROOM  where MEETINGDQBM=? order by MEETINGNO asc");
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select r.MEETINGNO, r.assetno, o.*  from BD_MEETING_ROOM r, IWORK_MEETING_ROOM_ORDER o  where r.meetingno = o.meetingroomno  and ? between o.startdatea and o.enddatea order by r.MEETINGNO asc");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Hashtable timeHash = MeetingDataUtil.getTimeIndexHashtable(dqbh);
		Hashtable blockHash = MeetingDataUtil.getMeetingRoomsHashTable(dqbh);
		int maxNum = MeetingRoomUtil.getInstance().getMeetingTimeNum(dqbh);
		
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql1.toString());
			stmt.setString(1, dqbh);
			rs = stmt.executeQuery(); 

			while (rs.next()) {
				String meetingno = rs.getString("MEETINGNO");
				String meetingname = rs.getString("MEETINGNAME");
				String mt = rs.getString("MEETINGTYPE");
				String gm = rs.getString("DIMENSIONS");
				String mdq = rs.getString("MEETINGDQ");
				String mwz = rs.getString("MEETINGWZ");
				String ms = rs.getString("MEETINGSIZE");
				String conf = rs.getString("MEETINGCONF");
				if (conf == null)
					conf = "";
				String ifsp = rs.getString("IFSP");
				String[] confs = conf.split("__eol__");
				StringBuffer confb = new StringBuffer();
				for (String c : confs) {
					confb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + c.trim());
					confb.append("\n");
				}
				StringBuffer tt1 = new StringBuffer();
				tt1.append("会议室编号：").append(meetingno == null ? "" : meetingno)
						.append("\n");
				tt1.append("会议室名称：")
						.append(meetingname == null ? "" : meetingname)
						.append("\n");
				tt1.append("会议室类型：").append(mt == null ? "" : mt).append("\n");
				tt1.append("会议室规模：").append(gm == null ? "" : gm).append("\n");
				tt1.append("会议室所在地区：").append(mdq == null ? "" : mdq)
						.append("\n");
				tt1.append("会议室所在位置：").append(mwz == null ? "" : mwz)
						.append("\n");
				tt1.append("容纳人数：").append(ms == null ? "" : ms).append("\n");
				tt1.append("是否需要审批：").append(ifsp == null ? "" : ifsp)
						.append("\n");
				tt1.append("会议室主要配置：")
						.append("\n")
						.append(confb.toString() == null ? "" : confb
								.toString());
				
				MeetingTypebean bean = new MeetingTypebean(maxNum);
				bean.setDate(format.parse(date));
				bean.setMeetingName(meetingname);
				bean.setMeetingNo(meetingno);
				bean.setTitle(tt1.toString());
				bean.setApprovalNedded("是".equals(ifsp));
				bean.setAssetNo(rs.getString("ASSETNO"));
				beanHash.put(meetingno, bean);
				meetRoomList.add(bean);
			}
			rs.close();
			stmt.close();
			stmt = conn.prepareStatement(sql2.toString());
			stmt.setString(1, DBUtil.convertLongDate(date));
			rs = stmt.executeQuery();

			while (rs.next()) {
				String meetingNo = rs.getString("MEETINGNO");

				String beginTime = rs.getString("STARTTIME");
				String endTime = rs.getString("ENDTIME");
				String sqr = rs.getString("ORDERNAME");
				String tel = rs.getString("ORDERTEL");
				String department = rs.getString("DEPARTMENTNAME");
				String hyzt = rs.getString("MEETINGTITLE");
				String meetingtype = rs.getString("MEETINGTYPE");
				String spsbzt = rs.getString("SPSBZT");
				String uid = rs.getString("CREATEUSER");
				int bindid = rs.getInt("BINDID");
				int orderId = rs.getInt("ID");

				StringBuffer titleSb = new StringBuffer();

				titleSb.append("预订时间：")
						.append((StringUtils.isEmpty(beginTime) ? "无"
								: beginTime)
								+ "-"
								+ (StringUtils.isEmpty(endTime) ? "无" : endTime))
						.append("\n");
				titleSb.append("预订人：")
						.append(StringUtils.isEmpty(sqr) ? "无" : sqr)
						.append("\n");
				titleSb.append("预订人的联系方式：")
						.append(StringUtils.isEmpty(tel) ? "无" : tel)
						.append("\n");
				titleSb.append("预订部门：")
						.append(StringUtils.isEmpty(department) ? "无"
								: department).append("\n");
				titleSb.append("会议主题：")
						.append(StringUtils.isEmpty(hyzt) ? "无" : hyzt)
						.append("\n");
				if ("视频会议室".equals(meetingtype)) {
					titleSb.append("视频系统状态：")
							.append(StringUtils.isEmpty(spsbzt) ? "不使用视频系统"
									: spsbzt).append("\n");
				}
				String title = titleSb.toString();

				int beginIndex = 1;
				int endIndex = MeetingRoomUtil.getInstance().getMeetingTimeNum(
						dqbh);

				if (timeHash.get(beginTime) != null)
					beginIndex = ((Integer) timeHash.get(beginTime)).intValue() - 1;
				else
					beginIndex = endIndex;

				if (timeHash.get(endTime) != null)
					endIndex = ((Integer) timeHash.get(endTime)).intValue() - 1;

				if (endIndex > maxNum)
					endIndex = maxNum;

				MeetingTypebean bean = (MeetingTypebean) beanHash
						.get(meetingNo);
				if (bean != null) {
					MeetingBlock[] blocks = bean.getBlocks();
					for (int i = beginIndex; i < endIndex; i++) {
						blocks[i].setOrderId(orderId);
						blocks[i].setBlockTitle(title);
						blocks[i].setReserved(true);
						blocks[i].setRoomOrderBindid(bindid);
						blocks[i].setUid(uid);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return meetRoomList;
	}
}