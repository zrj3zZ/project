package com.iwork.plugs.updateNotice.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.core.db.DBUtil;
import org.apache.log4j.Logger;
public class UpdateNoticeUserid implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(UpdateNoticeUserid.class);
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		UpdateGGUseridToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}

	// 修改通知情况表中的userid字段
	public void UpdateGGUseridToDB() {
		List<HashMap> list = getNullUserId1();
		for (HashMap dataMap : list) {
			String xm=dataMap.get("XM").toString();
			String ggsql="update bd_xp_hfqkb set userid=(select userid from orguser where username=xm),customerno=(select extend1 from orguser where username=xm) where xm='"+xm+"'";
			int ggcount = DBUtil.executeUpdate(ggsql);
		}
		List<HashMap> nList=getNullUserId2();
		for (HashMap dataMap : nList) {
			String xm=dataMap.get("XM").toString();
			String ggsql="update bd_xp_hfqkb set userid=(select userid from orguser where username=xm),customerno=(select extend1 from orguser where username=xm) where xm='"+xm+"'";
			int ggcount = DBUtil.executeUpdate(ggsql);
		}
	}
	
	public List<HashMap> getNullUserId1(){
		StringBuffer sb = new StringBuffer("select hf.xm as xm from (select distinct xm from bd_xp_hfqkb group by xm) hf inner join orguser org on hf.xm=org.username");
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
				HashMap map = new HashMap();
				String xm=rs.getString("xm");
				map.put("XM",xm);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<HashMap> getNullUserId2(){
		StringBuffer sb = new StringBuffer("select distinct xm as xm from bd_xp_hfqkb where userid is null group by xm");
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
				HashMap map = new HashMap();
				String xm=rs.getString("xm");
				map.put("XM",xm);
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


