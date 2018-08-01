package com.iwork.commons.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.pool.ConnectionPool;
/**
 * 安全DBUtil
 * @author wuyao
 *
 */

public class DBUTilNew {
	private static Logger logger = Logger.getLogger(DBUTilNew.class);
	public static Connection open(){
	    Connection conn = ConnectionPool.getInstance().getConnection();
	    return conn;
	}
	
	/**
	 * 验证actStepId参数只能是usertask开头或者step_开头
	 * @param str
	 * @return
	 */
	public static boolean validActStepId(String str){
		 String match="^usertask{1,3}\\d+$|(?=^.{9,50}$)step_[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+$|^9{6}$";
		 if(str!=null){
			  boolean matches=str.matches(match);
			  if(!matches){
					return false;
			  }
		 }
		  return true;
	}
	
	/**
	 * 关闭数据库链接新
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
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
	public static List<HashMap> getDataList(List lables,String sql,Map params) {
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<HashMap> back = new ArrayList<HashMap>();
		DBUtilInjection d=new DBUtilInjection();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
					if(d.HasInjectionData(params.get(i).toString())){
						return back;
					}
					}
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
			String readValue = ConfigUtil.readValue(CN_FILENAME, "dayin");
			 if("0".equals(readValue)){
				 System.out.println(sql);
			 }
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
		DBUtilInjection d=new DBUtilInjection();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
					if(d.HasInjectionData(params.get(i).toString())){
						return str;
					}
					}
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
			String readValue = ConfigUtil.readValue(CN_FILENAME, "dayin");
			 if("0".equals(readValue)){
				 System.out.println(sql);
			 }
		} finally{
			close(conn, ps, rs);
		}
		return str;
	}

	public static int update(String sql,Map params){
		Connection conn = open();
		PreparedStatement ps = null;
		int num = 0;
		DBUtilInjection d=new DBUtilInjection();
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
					if(d.HasInjectionData(params.get(i).toString())){
						return num;
					}
					}
					ps.setString(i,params.get(i).toString());
				}
			}
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
			String readValue = ConfigUtil.readValue(CN_FILENAME, "dayin");
			 if("0".equals(readValue)){
				 System.out.println(sql);
			 }
		} finally{
			close(conn, ps, null);
		}
		return num;
	}
	public static Long getLong(String lable,String sql,Map params){
		DBUtilInjection d=new DBUtilInjection();
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long str=0L;
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
					if(d.HasInjectionData(params.get(i).toString())){
						return str;
					}
					}
					ps.setString(i,params.get(i)==null?"":params.get(i).toString());
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				str = Long.valueOf(rs.getString(lable));
				break;
			}
		} catch (Exception e) {
			logger.error(e,e);
			String readValue = ConfigUtil.readValue(CN_FILENAME, "dayin");
			 if("0".equals(readValue)){
				 System.out.println(sql);
			 }
		} finally{
			close(conn, ps, rs);
		}
		return str;
	}
	public static int getInt(String lable,String sql,Map params){
		DBUtilInjection d=new DBUtilInjection();
		Connection conn = open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int str=0;
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 1; i <= params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
						if(d.HasInjectionData(params.get(i).toString())){
							return str;
						}
					}
					ps.setString(i,params.get(i)==null?"":params.get(i).toString());
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				str = rs.getInt(lable);
				break;
			}
		} catch (Exception e) {
			logger.error(e,e);
			String readValue = ConfigUtil.readValue(CN_FILENAME, "dayin");
			 if("0".equals(readValue)){
				 System.out.println(sql);
			 }
		} finally{
			close(conn, ps, rs);
		}
		return str;
	}
}
