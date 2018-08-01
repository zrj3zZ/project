package com.ibpmsoft.project.zqb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.db.DBUtil;
import org.apache.log4j.Logger;

public class ZqbEventManageDAO{
	private static Logger logger = Logger.getLogger(ZqbEventManageDAO.class);
	/**
	 * 获得第n次召开会议
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) num from bd_meet_plan where customerno = ? and HYSX=? and ( MEETTYPE=? or ZYWYH=?) and jc=?");
		sql.append(" order by jc desc,hc desc");
		int num = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			ps.setString(2, grouptype);
			ps.setString(3, meettype);
			ps.setString(4, meettype);
			ps.setInt(5, jc);
			rs = ps.executeQuery();
			if(rs.next()){
				num = rs.getInt("num");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return num;
	}

	public String getGslx(String jydxbh,String jydxmc) {
		StringBuffer sql = new StringBuffer();
		sql.append("select gslx from BD__FZGSGLZB where zch=? and gsmc=? ");
		String gslx = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, jydxbh);
			ps.setString(2, jydxmc);
			rs = ps.executeQuery();
			if(rs.next()){
				gslx = rs.getString("gslx");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return gslx;
		
	}

	public List<HashMap> getList() {
		StringBuffer sb = new StringBuffer("SELECT FP.KHFZR,FP.FHSPR,FP.ZZSPR,FP.GGFBR,FP.ZZCXDD,FP.CWSCBFZR2,FP.CWSCBFZR3,FP.FBBWJSHR"
				+ ",FP.KHBH KHBH,KH.ZQJC ZQJC,KH.ZQDM ZQDM,FP.KHMC KHMC,FP.ID ID,FP.QYNBRYSH QYNBRYSH,KH.CLASSIFICATION"
				+ " FROM BD_MDM_KHQXGLB FP,BD_ZQB_KH_BASE KH WHERE FP.KHBH=KH.CUSTOMERNO ORDER BY KH.ZQDM");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				String khfzr = rs.getString("khfzr");
				String fhspr = rs.getString("fhspr");
				String zzspr = rs.getString("zzspr");
				String ggfbr = rs.getString("ggfbr");
				String cwscbfzr2 = rs.getString("cwscbfzr2");
				String cwscbfzr3 = rs.getString("cwscbfzr3");
				String fbbwjshr = rs.getString("fbbwjshr");
				String khbh = rs.getString("khbh");
				String zzcxdd= rs.getString("zzcxdd");
				String zqjc= rs.getString("zqjc");
				String zqdm = rs.getString("zqdm");
				String khmc = rs.getString("khmc");
				String id = rs.getString("id");
				String qynbrysh = rs.getString("qynbrysh");
				String classification = rs.getString("classification");
				map.put("KHFZR", khfzr);
				map.put("FHSPR", fhspr);
				map.put("ZZSPR", zzspr);
				map.put("GGFBR", ggfbr);
				map.put("CWSCBFZR2", cwscbfzr2);
				map.put("CWSCBFZR3", cwscbfzr3);
				map.put("FBBWJSHR", fbbwjshr);
				map.put("KHBH", khbh);
				map.put("ZZCXDD", zzcxdd);
				map.put("ZQJC", zqjc);
				map.put("ZQDM", zqdm);
				map.put("KHMC", khmc);
				map.put("ID", id);
				map.put("QYNBRYSH", qynbrysh);
				map.put("CLASSIFICATION", classification);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}
}
 