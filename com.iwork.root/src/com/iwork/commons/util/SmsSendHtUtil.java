package com.iwork.commons.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;

public class SmsSendHtUtil {
	
	private static Logger logger=Logger.getLogger(SmsSendHtUtil.class);
	
	public static int sendSMSforHttp(String mobile,String content){
		Connection dbConn = openSqlConnection();
		PreparedStatement sql = null;
		int num = 0;
		try {
			sql = dbConn.prepareStatement("INSERT INTO PS_MSEND(PS_MSEND_USER,PS_DEPART_CODE,PS_MSEND_STATUS,PS_MSEND_WEIGHT,PS_MSEND_TYPE,PS_MSEND_CONT,PS_MSEND_TIME,PS_APP_ID) VALUES(?,'zz323233',0,7,0,?,getdate(),3015)");
			sql.setString(1, mobile);
			sql.setString(2, content);
			num = sql.executeUpdate();
		} catch (Exception e) {
			logger.error("插入数据异常"+e.toString()+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"方法：getmobileyzm()");
		} finally{
			closeSqlConnection(dbConn, sql, null);
		}
		return num;
	}
	
	private static Connection openSqlConnection() {
		Connection dbConn = null;
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = ConfigUtil.readAllProperties("/huataijdbc.properties").get("dbURL");
		String userName = ConfigUtil.readAllProperties("/huataijdbc.properties").get("userName");
		String userPwd = ConfigUtil.readAllProperties("/huataijdbc.properties").get("userPwd");
		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
		} catch (Exception e) {
			logger.error("连接数据库异常"+e.toString()+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"方法：openSqlConnection()");
		}
		return dbConn;
	}
	
	private static void closeSqlConnection(Connection dbConn, PreparedStatement sql,ResultSet rs) {
		try {
			if(rs != null){rs.close();}
			if(sql != null){sql.close();}
			if(dbConn != null){dbConn.close();}
		} catch (Exception e) {
			logger.error("关闭数据库数据异常"+e.toString()+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"方法：closeSqlConnection()");
		}
	}
	
	public static int sendSMSDg(String toNumber, String content) {
		int num = 0;
		String dgurl = SystemConfig._smsConf.getDgurl();
		String cmd = SystemConfig._smsConf.getCmd();
		String uid = SystemConfig._smsConf.getUid();
		String msgid = SystemConfig._smsConf.getMsgid();
		String psw = SystemConfig._smsConf.getPsw();
		StringBuffer action = new StringBuffer();
		
		try {
			action
			.append(dgurl).append("?")
			.append("cmd=").append(cmd).append("&")
			.append("uid=").append(uid).append("&")
			.append("msgid=").append(msgid).append("&")
			.append("psw=").append(psw).append("&")
			.append("mobiles=").append(toNumber).append("&")
			.append("msg=").append(URLEncoder.encode(content,"UTF-8"));
			URL url = new URL(action.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();	
			conn.setConnectTimeout(3000);
		    conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");  
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line; 
            int i=1;
            while ((line = in.readLine()) != null) {  
                num = Integer.parseInt(line.equals("")?"1":line); 
                i++;
            }  
            in.close();  
		} catch (Exception e){logger.error(e,e);
			num=1;
		}
		return num;
	}
	public static int sendSMSXn(String toNumber, String content) {
		int num = 0;
		String xnurl = SystemConfig._smsConf.getXnurl();
		String lx = SystemConfig._smsConf.getLx();
		String dlzh = SystemConfig._smsConf.getDlzh();
		String dlmm = SystemConfig._smsConf.getDlmm();
		
		String dxlx = SystemConfig._smsConf.getDxlx();
		String dssj = SystemConfig._smsConf.getDssj();
		String shyh = SystemConfig._smsConf.getShyh();
		
		String fhls = SystemConfig._smsConf.getFhls();
		String yybdm = SystemConfig._smsConf.getYybdm();
		
		StringBuffer action = new StringBuffer();
		
		try {
			//SJHM
			//DXNR
			if(lx!=null&lx.equals("1")){
				action.append(xnurl).append("?");
				action.append("lx=").append(lx).append("&");
				if(!dlzh.equals(""))action.append("DLZH=").append(dlzh).append("&");
				if(!dlmm.equals(""))action.append("DLMM=").append(dlmm).append("&");
				if(!toNumber.equals(""))action.append("SJHM=").append(toNumber).append("&");
				if(!content.equals(""))action.append("DXNR=").append(URLEncoder.encode(content,"GB2312")).append("&");
				if(!dxlx.equals(""))action.append("DXLX=").append(dxlx).append("&");
				if(!dssj.equals(""))action.append("DSSJ=").append(dssj).append("&");
				if(!shyh.equals(""))action.append("SHYH=").append(shyh);
			}else if(lx!=null&lx.equals("0")){
				action.append(xnurl).append("?");
				action.append("lx=").append(lx).append("&");
				if(!dlzh.equals(""))action.append("DLZH=").append(dlzh).append("&");
				if(!dlmm.equals(""))action.append("DLMM=").append(dlmm).append("&");
				if(!toNumber.equals(""))action.append("SJHM=").append(toNumber).append("&");
				if(!content.equals(""))action.append("DXNR=").append(URLEncoder.encode(content,"GB2312")).append("&");
				if(!fhls.equals(""))action.append("FHLS=").append(fhls).append("&");
				if(!yybdm.equals(""))action.append("YYBDM=").append(yybdm);
			}
			
			URL url = new URL(action.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
		    conn.setReadTimeout(3000);
		    conn.setDoInput(true);
	        conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length","0");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.write( "".getBytes("UTF-8"), 0, 0);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line; 
            int i=1;
            while ((line = in.readLine()) != null) {  
                num = Integer.parseInt(line.equals("")?"1":"0");//获取短信流水号(有流水号:发送成功;无流水号:发送失败)
                i++;
            }  
            in.close();  
		} catch (Exception e){logger.error(e,e);
			num=1;
		}
		return num;
	}
}
