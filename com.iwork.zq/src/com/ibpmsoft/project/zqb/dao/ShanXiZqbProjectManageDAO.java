package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.core.db.DBUtil;

public class ShanXiZqbProjectManageDAO extends HibernateDaoSupport {
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiZqbProjectManageDAO.class);

	public int getCurrentCustomerListSize(String sql, List<String> parameter) {
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getXmlxProjectList(String sql, int pageSize, int pageNumber) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, startRow);
			ps.setInt(2, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long rnum = rs.getLong("RNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String zhxgsj = rs.getString("ZHXGSJ");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String ddap = rs.getString("DDAP");
				String xmzk = rs.getString("XMZK");
				String xmhy = rs.getString("XMHY");
				String xmls = rs.getString("XMLS");
				result.put("RNUM", rnum);
				result.put("CUSTOMERNAME", customername);
				result.put("ZHXGSJ", zhxgsj);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("DDAP", ddap);
				result.put("XMZK", xmzk);
				result.put("XMHY", xmhy);
				result.put("XMLS", xmls);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getXmlxProjectListSize(String sql) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getXmlxCyrProjectList(String sql, int pageSize, int pageNumber) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, startRow);
			ps.setInt(2, endRow);
			ps.setInt(3, startRow);
			ps.setInt(4, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long num = rs.getLong("NUM");
				Long cnum = rs.getLong("CNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String name = rs.getString("NAME");
				String type = rs.getString("TYPE");
				String zhxgsj = rs.getString("ZHXGSJ");
				result.put("NUM", num);
				result.put("CNUM", cnum);
				result.put("CUSTOMERNAME", customername);
				result.put("NAME", name);
				result.put("TYPE", type);
				result.put("ZHXGSJ", zhxgsj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getXmlxCyrProjectListSize(String sql) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getExpXmlxProjectList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String customername = rs.getString("PROJECTNAME");
				String zhxgsj = rs.getString("ZHXGSJ");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String ddap = rs.getString("DDAP");
				String xmzk = rs.getString("XMZK");
				String xmhy = rs.getString("XMHY");
				String xmls = rs.getString("XMLS");
				result.put("CUSTOMERNAME", customername);
				result.put("ZHXGSJ", zhxgsj);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("DDAP", ddap);
				result.put("XMZK", xmzk);
				result.put("XMHY", xmhy);
				result.put("XMLS", xmls);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getExpXmlxCyrProjectList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long num = rs.getLong("NUM");
				Long xmnum = rs.getLong("XMNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String name = rs.getString("NAME");
				String type = rs.getString("TYPE");
				String zhxgsj = rs.getString("ZHXGSJ");
				result.put("NUM", num);
				result.put("XMNUM", xmnum);
				result.put("CUSTOMERNAME", customername);
				result.put("NAME", name);
				result.put("TYPE", type);
				result.put("ZHXGSJ", zhxgsj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, String>> getCustomerAutoDataList(String sql) {
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String customername = rs.getString("CUSTOMERNAME");
				String customerno = rs.getString("CUSTOMERNO");
				result.put("CUSTOMERNAME", customername);
				result.put("CUSTOMERNO", customerno);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public static List<HashMap<String, Object>> getExpCustomerList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String customername = rs.getString("CUSTOMERNAME");
				String customerno = rs.getString("CUSTOMERNO");
				String type = rs.getString("TYPE");
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				String sshy = rs.getString("SSHY");
				String zwmc = rs.getString("ZWMC");
				BigDecimal zczb = rs.getBigDecimal("ZCZB");
				String gfgsrq = rs.getString("GFGSRQ");
				String xmlxspzt = rs.getString("XMLXSPZT");
				Integer cnum = rs.getInt("CNUM");
				String gglcspzt = rs.getString("GGLCSPZT");
				String xmnhlcspzt = rs.getString("XMNHLCSPZT");
				String nhfklcspzt = rs.getString("NHFKLCSPZT");
				String jdmc = rs.getString("JDMC");
				
				result.put("CUSTOMERNAME", customername);
				result.put("CUSTOMERNO", customerno);
				result.put("TYPE", type);
				result.put("ZQJC", zqjc);
				result.put("ZQDM", zqdm);
				result.put("SSHY", sshy);
				result.put("ZWMC", zwmc);
				result.put("ZCZB", zczb);
				result.put("GFGSRQ", gfgsrq);
				result.put("XMLXSPZT", xmlxspzt);
				result.put("CNUM", cnum);
				result.put("GGLCSPZT", gglcspzt);
				result.put("XMNHLCSPZT", xmnhlcspzt);
				result.put("NHFKLCSPZT", nhfklcspzt);
				result.put("JDMC", jdmc);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
