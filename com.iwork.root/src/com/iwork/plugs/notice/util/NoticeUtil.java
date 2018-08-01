package com.iwork.plugs.notice.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.core.db.DBUtil;
import org.apache.log4j.Logger;

public class NoticeUtil {
	private static Logger logger = Logger.getLogger(NoticeUtil.class);
	private static NoticeUtil instance = null;
	
	private NoticeUtil() {
	}

	public static NoticeUtil getInstance() {

		if (instance == null) {
			instance = new NoticeUtil();
		}
		return instance;
	}
	
	//从临时表取数据
	public List<HashMap> GetNoticeInfo()
	{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT ID,NOTICENO,NOTICENAME,NOTICETYPE,NOTICEDATE,CREATEDATE,NOTICEFILE,KHMC,KHBH,CREATENAME,SPZT FROM BD_MEET_QTGGZLTEMP ORDER BY ID");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				int ID=rset.getInt("ID");				
				String NOTICENO = rset.getString("NOTICENO");
				String NOTICENAME = rset.getString("NOTICENAME");
				String NOTICETYPE = rset.getString("NOTICETYPE");
				String NOTICEDATE = rset.getString("NOTICEDATE");
				String NOTICEFILE = rset.getString("NOTICEFILE");		
				String KHMC = rset.getString("KHMC");		
				String KHBH = rset.getString("KHBH");		
				String CREATEDATE = rset.getString("CREATEDATE");
				String CREATENAME = rset.getString("CREATENAME");
				String SPZT = rset.getString("SPZT");
				HashMap hash = new HashMap();
				hash.put("ID", ID);
				hash.put("NOTICENO", NOTICENO);
				hash.put("NOTICENAME", NOTICENAME);
				hash.put("NOTICETYPE", NOTICETYPE);
				hash.put("NOTICEDATE", NOTICEDATE);
				hash.put("NOTICEFILE", NOTICEFILE);
				hash.put("KHMC", KHMC);
				hash.put("KHBH", KHBH);
				hash.put("CREATEDATE", CREATEDATE);
				hash.put("CREATENAME", CREATENAME);
				hash.put("SPZT", SPZT);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
	public List<HashMap> GetKHInfo()
	{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT * FROM BD_ZQB_KH_BASETEMP");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				int ID=rset.getInt("ID");				
				String CREATEUSER = rset.getString("CREATEUSER");
				String CREATEDATE = rset.getString("CREATEDATE");
				String CUSTOMERNAME = rset.getString("CUSTOMERNAME");
				String CUSTOMERNO = rset.getString("CUSTOMERNO");
				String STATUS = rset.getString("STATUS");		
				String USERNAME = rset.getString("USERNAME");		
				String TEL = rset.getString("TEL");		
				String EMAIL = rset.getString("EMAIL");
				String ZQJC = rset.getString("ZQJC");
				String ZQDM = rset.getString("ZQDM");
				String YGP = rset.getString("YGP");
				
				String PASSWORD = rset.getString("PASSWORD");
				Long ORGROLEID = rset.getLong("ORGROLEID");
				Long ISMANAGER = rset.getLong("ISMANAGER");
				Long USERTYPE = rset.getLong("USERTYPE");
				Date STARTDATE = rset.getDate("STARTDATE");
				Date ENDDATE = rset.getDate("ENDDATE");
				Long USERSTATE = rset.getLong("USERSTATE");
				Long COMPANYID = rset.getLong("COMPANYID");
				String COMPANYNAME = rset.getString("COMPANYNAME");
				
				HashMap hash = new HashMap();
				hash.put("ID", ID);
				hash.put("CREATEUSER", CREATEUSER);
				hash.put("CREATEDATE", CREATEDATE);
				hash.put("CUSTOMERNAME", CUSTOMERNAME);
				hash.put("CUSTOMERNO", CUSTOMERNO);
				hash.put("STATUS", STATUS);
				hash.put("USERNAME", USERNAME);
				hash.put("TEL", TEL);
				hash.put("EMAIL", EMAIL);
				hash.put("ZQJC", ZQJC);
				hash.put("ZQDM", ZQDM);
				hash.put("YGP", YGP);
				
				hash.put("PASSWORD", PASSWORD);
				hash.put("ORGROLEID", ORGROLEID);
				hash.put("ISMANAGER", ISMANAGER);
				hash.put("USERTYPE", USERTYPE);
				hash.put("STARTDATE", STARTDATE);
				hash.put("ENDDATE", ENDDATE);
				hash.put("USERSTATE", USERSTATE);
				hash.put("COMPANYID", COMPANYID);
				hash.put("COMPANYNAME", COMPANYNAME);
			
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}	
	//判断公告是否存在
	public int IsExistsNotice(String KHMC,String NoticeName)
	{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT COUNT(*) NUM FROM BD_MEET_QTGGZL WHERE KHMC=? AND NOTICENAME =?");
		int num=0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, KHMC);
			stmt.setString(2, NoticeName);
			rset = stmt.executeQuery();
			while (rset.next()) {
				num=rset.getInt("num");	
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return num;
	}
	//判断客户是否存在根据客户名称
	public List<HashMap> IsExistsKH(String CUSTOMERNAME)
	{
		StringBuffer sql = new StringBuffer();		
		List list = new ArrayList();
		sql.append("SELECT ID,YGP,CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNAME=?");		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, CUSTOMERNAME);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String CUSTOMERNO=rset.getString("CUSTOMERNO");
				long ID=rset.getLong("ID");
				String YGP=rset.getString("YGP");
				HashMap<String,Object> hash = new HashMap<String,Object>();
				hash.put("CUSTOMERNO", CUSTOMERNO);
				hash.put("ID", ID);
				hash.put("YGP", YGP);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
	//判断客户是否存在根据证券代码
	public List<HashMap> IsExistsKHByCode(String ZQDM)
	{
		StringBuffer sql = new StringBuffer();		
		List list = new ArrayList();
		sql.append("SELECT ID FROM BD_ZQB_KH_BASE WHERE ZQDM=?");		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, ZQDM);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String ID=rset.getString("ID");	
				HashMap hash = new HashMap();
				hash.put("ID", ID);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}	
	
	//判断客户是否存在根据客户编号
	public List<HashMap> IsExistsKHByNO(String CUSTOMERNO)
	{
		StringBuffer sql = new StringBuffer();		
		List list = new ArrayList();
		sql.append("SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?");		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, CUSTOMERNO);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String CUSTOMERNO_=rset.getString("CUSTOMERNO");	
				HashMap hash = new HashMap();
				hash.put("CUSTOMERNO", CUSTOMERNO_);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}	
	//更新公告数据中的客户编号
	//CUSTOMERNO 已经存在的客户编号
	//
	public int UpdateNotice(String KHBH,String CUSTOMERNO,String CUSTOMERNAME)
	{
		StringBuffer sql = new StringBuffer();		
		sql.append("UPDATE BD_MEET_QTGGZL SET KHBH=? WHERE KHBH=? AND KHMC=?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		int Result=0;
		try
		{
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, CUSTOMERNO);
			stmt.setString(2, KHBH);
			stmt.setString(3, CUSTOMERNAME);
			Result = stmt.executeUpdate();
		}
		catch (Exception e){
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Result;
	}
	//查询某序列的值
	public int GetKHSeqValue(String Key)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SEQUENCEVALUE FROM SYSSEQUENCE WHERE SEQUENCEKEY=?");
		int Value=0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, Key);
			rset = stmt.executeQuery();
			while (rset.next()) {
				Value=rset.getInt("SEQUENCEVALUE");	
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Value;		
	}
	public int UpdateSeqValue(String key, int Value)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE SYSSEQUENCE SET SEQUENCEVALUE=? WHERE SEQUENCEKEY=?");
		int Res=0;
		Connection conn = null;
		PreparedStatement stmt = null;
    	ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, Value);
			stmt.setString(2, key);
			Res = stmt.executeUpdate();				
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Res;		
	}
	public String GetCustomerNo() throws Exception
	{
		String CusNo="CNO";
		int no=GetKHSeqValue("BPM:0");
		no+=1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String STARTDATE = sdf.format(new Date());		
		CusNo+=STARTDATE.toString()+"-"+no;
		return CusNo+","+no;
	}
	//获取功能的UUID
	public String GetUUID(String Title)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE=?");
		String UUID="";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, Title);
			rset = stmt.executeQuery();
			while (rset.next()) {
				UUID=rset.getString("UUID");	
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return UUID;
	}
	
	
	
	
}
