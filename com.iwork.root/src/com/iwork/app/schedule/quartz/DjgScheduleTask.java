package com.iwork.app.schedule.quartz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.MessageAPI;

/**
 * 当用户设置了定时提醒功能后，系统将每间隔半小时校验一次，将短信发至董秘人员
 * 
 * @author wuyao
 * 
 */
public class DjgScheduleTask {
	private static Logger logger = Logger.getLogger(DjgScheduleTask.class);

	/**
	 * 离职超过六个月的回对相应的董秘进行短信、邮件、系统消息提示
	 * 
	 * @throws ParseException
	 */
	public void execute() {
		try {
			sendLRmsg();
		} catch (Exception e) {
			logger.error(e,e);
		}
		try {
			sendZZmsg();
		} catch (Exception e) {
			logger.error(e,e);
		}
		try {
			sendOffmsg();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	
	private void sendOffmsg(){
		List<Integer> id = new ArrayList<Integer>();
		// 查询数据库
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.ID ID,   XM,  C.KHBH,   C.KHMC,   RZZT,  LRSJ,  DXYJFSZT, RZJSRQ,   RZLX,   ZW,  LRSJ,  ORG.USERNAME,   ORG.USERID,   ORG.MOBILE,   ORG.EMAIL,    KH.ZQJC,  KH.ZQDM");// AND RZZT='离任'
		sql.append("  , ( select z.mobile from orguser z where z.userid= (select substr(khfzr,1,instr(khfzr,'[',-1)-1) khfzr from bd_mdm_khqxglb y where to_char(y.khbh)=to_char(C.KHBH) ) ) dmmobile FROM (SELECT INSTANCEID, DATAID, METADATAID, B.* FROM (SELECT INSTANCEID, DATAID, METADATAID   FROM SYS_ENGINE_FORM_BIND   WHERE METADATAID =(SELECT ID FROM SYS_ENGINE_METADATA ");// AND RZZT='离任'
		sql.append(" WHERE ENTITYNAME = 'BD_ZQB_GGJBXXB')) A LEFT JOIN BD_ZQB_GGJBXXB B ON A.DATAID = B.ID) C LEFT JOIN (   SELECT A.INSTANCEID, DATAID, B.*  FROM (SELECT INSTANCEID, DATAID FROM SYS_ENGINE_FORM_BIND ");// AND RZZT='离任'
		sql.append("  WHERE METADATAID =  (SELECT ID  FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_BGSRZXXB')) A  LEFT JOIN BD_ZQB_BGSRZXXB B  ON A.DATAID = B.ID where lrsj is not null and to_char(lrsj,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') ");// AND RZZT='离任'
		sql.append(" union    SELECT A.INSTANCEID, DATAID, B.*   FROM (SELECT INSTANCEID, DATAID   FROM SYS_ENGINE_FORM_BIND  WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA  WHERE ENTITYNAME = 'BD_ZQB_BGSRZXXB')) A ");// AND RZZT='离任'
		sql.append("   LEFT JOIN BD_ZQB_BGSRZXXB B ON A.DATAID = B.ID where lrsj is  null and to_char(rzjsrq,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd')   ) D ON C.INSTANCEID = D.INSTANCEID INNER JOIN ORGUSER ORG   ON C.KHBH = ORG.EXTEND1 ");// AND RZZT='离任'
		sql.append("   AND C.KHMC = ORG.EXTEND2 LEFT JOIN BD_ZQB_KH_BASE KH  ON ORG.EXTEND1 = KH.CUSTOMERNO WHERE C.XM IS NOT NULL  AND D.DATAID IS NOT NULL AND RZJSRQ IS NOT NULL ");// AND RZZT='离任'
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("DXYJFSZT") != 3) {
					String xm = rs.getString("XM");
					java.sql.Date lrsj = rs.getDate("LRSJ");
					
						String mobile = rs.getString("mobile");
						String email = rs.getString("email");
						String userid = rs.getString("userid");
						String zqjc = rs.getString("zqjc");
						String zqdm = rs.getString("zqdm");
						String dmmobile = rs.getString("dmmobile");
						UserContext uc = UserContextUtil.getInstance().getUserContext(userid);						
						boolean flag=send3(email, xm, uc, mobile, userid, lrsj, zqjc, zqdm,dmmobile);
						if(flag){
							id.add(rs.getInt("ID"));
						}
					
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		StringBuffer sqlupdate = null;
		try {
			conn = DBUtil.open();
			for (Integer ids : id) {
				sqlupdate = new StringBuffer();
				sqlupdate.append("UPDATE BD_ZQB_BGSRZXXB SET DXYJFSZT=3 WHERE ID=?");
				stmt = conn.prepareStatement(sqlupdate.toString());
				stmt.setInt(1, ids);
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			DBUtil.close(conn, stmt, null);
		}
	}
	
	private void sendLRmsg() {
		List<Integer> id = new ArrayList<Integer>();
		// 查询数据库
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.ID ID,XM,KHBH,KHMC,RZZT,LRSJ,DXYJFSZT,RZJSRQ,RZLX,ZW,LRSJ,ORG.USERNAME,ORG.USERID,ORG.MOBILE,ORG.EMAIL FROM (SELECT INSTANCEID,DATAID,METADATAID,B.* FROM (SELECT INSTANCEID,DATAID,METADATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GGJBXXB')) A LEFT JOIN BD_ZQB_GGJBXXB B ON A.DATAID = B.ID ) C LEFT JOIN (SELECT A.INSTANCEID,DATAID, B.* FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_BGSRZXXB')) A LEFT JOIN BD_ZQB_BGSRZXXB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID INNER JOIN ORGUSER ORG ON C.KHBH=ORG.EXTEND1 AND C.KHMC=ORG.EXTEND2 WHERE C.XM IS NOT NULL AND D.DATAID IS NOT NULL AND RZJSRQ IS NOT NULL AND RZZT='离任'");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("RZZT").equals("离任")&& rs.getInt("DXYJFSZT") != 2) {
					String xm = rs.getString("XM");
					java.sql.Date lrsj = rs.getDate("LRSJ");
					if (lrsj != null) {
						String mobile = rs.getString("mobile");
						String email = rs.getString("email");
						String userid = rs.getString("userid");
						UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
						Date date = null;
						try {
							date = new SimpleDateFormat("yyyy-MM-dd").parse(lrsj.toString());
						} catch (ParseException e) {
							logger.error(e,e);
						}
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.MONTH, 6);
						date = cal.getTime();
						if (date.getTime() < (new Date()).getTime()) {
							id.add(rs.getInt("ID"));
							send(email, xm, uc, mobile, userid, lrsj);
						}
					}
				}
				// 张三，离任日期：2015-03-15，已离任满6个月，满足股份限售规定。
				// 李四，任职截止日期2015-03-15，请准备换届选举，及准备议案及会议。
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		StringBuffer sqlupdate = null;
		if(!id.isEmpty()){
			conn = DBUtil.open();
		}
		for (Integer ids : id) {
			try {
				sqlupdate = new StringBuffer();
				sqlupdate.append("UPDATE BD_ZQB_BGSRZXXB SET DXYJFSZT=2 WHERE ID=?");
				stmt = conn.prepareStatement(sqlupdate.toString());
				stmt.setInt(1, ids);
				stmt.executeUpdate();
			} catch (Exception e) {
				logger.error(e.toString());
			} finally{
				DBUtil.close(conn, stmt, null);
			}
		}
	}

	private void sendZZmsg() {
		List<Integer> id = new ArrayList<Integer>();
		// 查询数据库
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.ID ID,XM,KHBH,KHMC,RZZT,LRSJ,DXYJFSZT,RZJSRQ,RZLX,ZW,LRSJ,ORG.USERNAME,ORG.USERID,ORG.MOBILE,ORG.EMAIL FROM (SELECT INSTANCEID,DATAID,METADATAID,B.* FROM (SELECT INSTANCEID,DATAID,METADATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GGJBXXB')) A LEFT JOIN BD_ZQB_GGJBXXB B ON A.DATAID = B.ID ) C LEFT JOIN (SELECT A.INSTANCEID,DATAID, B.* FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_BGSRZXXB')) A LEFT JOIN BD_ZQB_BGSRZXXB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID INNER JOIN ORGUSER ORG ON C.KHBH=ORG.EXTEND1 AND C.KHMC=ORG.EXTEND2 WHERE C.XM IS NOT NULL AND D.DATAID IS NOT NULL AND RZJSRQ IS NOT NULL AND RZZT='在职'");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				int dxyjsfzt = rs.getInt("DXYJFSZT");
				if (rs.getString("RZZT").equals("在职") && dxyjsfzt == 0) {
					String xm = rs.getString("XM");
					java.sql.Date qzjsqr = rs.getDate("RZJSRQ");
					if (qzjsqr != null) {
						String mobile = rs.getString("mobile");
						String email = rs.getString("email");
						String userid = rs.getString("userid");
						UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
						Date date = null;
						try {
							date = new SimpleDateFormat("yyyy-MM-dd").parse(qzjsqr.toString());
						} catch (ParseException e) {
							logger.error(e,e);
						}
						if (date.getTime() < new Date().getTime()) {
							Calendar cal = Calendar.getInstance();
							Date now = new Date();
							cal.setTime(date);
							cal.add(Calendar.DAY_OF_YEAR, 1);
							cal.add(Calendar.MONTH, 6);
							date = cal.getTime();
							if ((date.getTime() < now.getTime())) {
								send(email, xm, uc, mobile, userid, qzjsqr);
								id.add(rs.getInt("ID"));
							}
						} else {
							Calendar cal = Calendar.getInstance();
							Date now = new Date();
							cal.setTime(now);
							cal.add(Calendar.MONTH, 3);
							now = cal.getTime();
							if (date.getTime() < now.getTime()) {
								send2(email, xm, uc, mobile, userid, qzjsqr);
								id.add(rs.getInt("ID"));
							}
						}
					}
				}
				// 张三，离任日期：2015-03-15，已离任满6个月，满足股份限售规定。
				// 李四，任职截止日期2015-03-15，请准备换届选举，及准备议案及会议。
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, stmt, rs);
		}
		StringBuffer sqlupdate = null;	
		for (Integer ids : id) {
			try {
				conn=DBUtil.open();
				sqlupdate = new StringBuffer("UPDATE BD_ZQB_BGSRZXXB SET DXYJFSZT=1 WHERE ID=?");
				stmt = conn.prepareStatement(sqlupdate.toString());
				stmt.setInt(1, ids);
				stmt.executeUpdate();
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, stmt, null);
			}
		}
	}

	public void send(String email, String xm, UserContext uc, String mobile, String userid, java.sql.Date lrsj) {
		if (email != null && !email.equals("")) {
			MessageAPI.getInstance().sendSysMail(xm + "满足股份解限售规定", email,xm + "满足股份解限售规定",xm + "，离任日期：" + lrsj + "，已离任满6个月，满足股份解限售规定。");
		}
		if (mobile != null && !mobile.equals("")&& mobile.matches("^1[358][0-9]{9}$")) {
			MessageAPI.getInstance().sendSMS(uc, mobile,xm + "满足股份解限售规定，离任日期：" + lrsj + "，已离任满6个月，满足股份解限售规定。");
		}
		MessageAPI.getInstance().sendSysMsg(userid, xm + "满足股份解限售规定",xm + "，离任日期：" + lrsj + "，已离任满6个月，满足股份解限售规定。");
	}

	public void send2(String email, String xm, UserContext uc, String mobile,String userid, java.sql.Date qzjsqr) {
		if (email != null && !email.equals("")) {
			MessageAPI.getInstance().sendSysMail(xm + "任期将到", email, xm + "任期将到", xm + "，任职截止日期：" + qzjsqr + "，请准备换届选举，及准备议案及会议。");
		}
		if (mobile != null && !mobile.equals("")&& mobile.matches("^1[358][0-9]{9}$")) {
			MessageAPI.getInstance().sendSMS(uc, mobile, xm + "任期将到，任职截止日期：" + qzjsqr + "，请准备换届选举，及准备议案及会议。");
		}
		MessageAPI.getInstance().sendSysMsg(userid, xm + "任期将到",xm + "，任职截止日期：" + qzjsqr + "，请准备换届选举，及准备议案及会议。");
	}
	
	public boolean  send3(String email, String xm, UserContext uc, String mobile, String userid, java.sql.Date lrsj, String zqjc, String zqdm,String dmmobile) {
		boolean flag=false;
		//公司代码公司简称，xx离职，请注意需及时办理限售。	
		//督导
		if (dmmobile != null && !dmmobile.equals("")&& dmmobile.matches("^1[358][0-9]{9}$")) {
			flag=MessageAPI.getInstance().sendSMS(uc,mobile,zqdm + zqjc + "，"+ xm +"离职，请注意需及时办理限售。");
		}
		//董秘
		if (mobile != null && !mobile.equals("")&& mobile.matches("^1[358][0-9]{9}$")) {
			MessageAPI.getInstance().sendSMS(uc, dmmobile, xm +"离职，请注意需及时办理限售。");
		}
		return flag;
	}
}