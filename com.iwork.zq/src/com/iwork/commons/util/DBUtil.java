package com.iwork.commons.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iwork.core.pool.ConnectionPool;
import org.apache.log4j.Logger;
/**
 * 安全DBUtil
 * @author wuyao
 *
 */
public class DBUtil {
	private static Logger logger = Logger.getLogger(DBUtil.class);
	public static Connection open(){
	    Connection conn = ConnectionPool.getInstance().getConnection();
	    return conn;
	}
	/**
	 * 关闭数据库链接新
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static synchronized final void close(Connection conn,PreparedStatement ps,ResultSet rs) {
		try {
			if(rs != null){rs.close();}
			if(ps != null){ps.close();}
			if(conn != null){conn.close();}
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	/**
     * 
     * @param lables 字段名
     * @param sql:prepareStatement
     * @param params 参数
     * @return
     */
	public static List<String> getResultList(List lables,String sql,Map params) {
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> back = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setObject(i, params.get(i));
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				for (int i = 0; i < lables.size(); i++) {
					back.add(rs.getString(lables.get(i).toString()));
				}
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, rs);
		}
		return back;
	}
	/**
     * 
     * @param lables 字段名
     * @param sql:prepareStatement
     * @param params 参数
     * @return
     */
	public static List<HashMap> getDataList(List lables,String sql,Map params) {
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<HashMap> back = new ArrayList<HashMap>();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setObject(i, params.get(i));
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map = new HashMap();
				for (int i = 0; i < lables.size(); i++) {
					map.put(lables.get(i).toString(),rs.getObject(lables.get(i).toString()));
				}
				back.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, rs);
		}
		return back;
	}
	/**
     * 
     * @param lables 字段名
     * @param sql:prepareStatement
     * @param params 参数
     * @return
     */
	public static List<String> getStringList(String lable,String sql,Map params) {
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> back = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setObject(i, params.get(i));
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				back.add(rs.getObject(lable.toString()).toString());
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, rs);
		}
		return back;
	}
	/**
	 * 
	 * @param lable 字段名
	 * @param sql psql
	 * @param params 参数
	 * @return
	 */
	public static String getDataStr(String lable,String sql,Map params){
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str = "";
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setString(i,params.get(i).toString());
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				str = rs.getString(lable);
				break;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, rs);
		}
		return str;
	}
	
	public static int update(String sql,Map params){
		Connection conn = open();
		PreparedStatement ps = null;
		int num = 0;
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setString(i,params.get(i).toString());
				}
			}
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, null);
		}
		return num;
	}
	public static Timestamp getTimestamp(String lable, String sql, Map params) {
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Timestamp str = null;
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					ps.setString(i,params.get(i).toString());
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				str = rs.getTimestamp(lable);
				break;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			close(conn, ps, rs);
		}
		return str;
	}
}
