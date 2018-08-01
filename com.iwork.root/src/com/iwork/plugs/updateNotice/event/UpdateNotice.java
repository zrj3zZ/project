package com.iwork.plugs.updateNotice.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import org.apache.log4j.Logger;
public class UpdateNotice implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(UpdateNotice.class);
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		UpdateGGToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}

	// 将公告数据添加至数据库
	public void UpdateGGToDB() {
		List<HashMap> list = getGG();
		Date date = UtilDate.StringToDate("2015-08-08","yyyy-MM-dd");
		Integer year = UtilDate.getYear(date);
		Integer month = UtilDate.getMonth(date);
		for (HashMap dataMap : list) {
			String ggid=dataMap.get("GGID").toString();
			String lcbs=dataMap.get("LCBS").toString();
			String sq="GG"+year+"-0"+month+"-"+ggid;
			String ggsql="UPDATE BD_MEET_QTGGZL SET NOTICESQ = '"+sq+"' WHERE NOTICESQ is null and ID = " + ggid;
			String gglcsql="UPDATE BD_XP_QTGGZLLC SET NOTICESQ = '"+sq+"' WHERE NOTICESQ is null and INSTANCEID = " + lcbs;
			int ggcount = DBUtil.executeUpdate(ggsql);
			int gglccount = DBUtil.executeUpdate(gglcsql);
		}
		System.out.println("-----------公告序列已修改完毕-----------");
	}
	
	public List<HashMap> getGG(){
		StringBuffer sb = new StringBuffer("select zl.id id,zl.lcbs lcbs from BD_MEET_QTGGZL zl inner join BD_XP_QTGGZLLC lc on zl.noticesq is null and zl.lcbs=lc.instanceid");
		String sql = sb.toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				Long ggid = rs.getLong("id");
				Long lcbs = rs.getLong("lcbs");
				map.put("GGID", ggid);
				map.put("LCBS", lcbs);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return list;
	}

}


