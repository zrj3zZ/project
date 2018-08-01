package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.km.document.model.IworkKmDoc;
import com.iwork.plugs.appointment.model.AppointmentNum;
import com.iwork.plugs.appointment.model.AppointmentYYSX;


public class ZqbMeetingManageDAO extends HibernateDaoSupport{
	
	
	
	public int getMeetingJC(String customerno,String meettype){
		StringBuffer sql = new StringBuffer();
		sql.append("select jc from bd_meet_plan where customerno = ?  and  MEETTYPE=? order by jc desc");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int num = 0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			ps.setString(2, meettype);
			rs = ps.executeQuery();
			if(rs.next()){
				num = rs.getInt("jc");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return num;
		
	}
	
	public int getMeetingHC(String customerno,String meettype,String hysx,int jc){
		StringBuffer sql = new StringBuffer();
		sql.append("select HC from bd_meet_plan where customerno = ?  and  MEETTYPE=? and jc=? and hysx = ? order by HC desc");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int num = 0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			ps.setString(2, meettype);
			ps.setInt(3, jc);
			ps.setString(4, hysx);
			rs = ps.executeQuery();
			if(rs.next()){ 
				num = rs.getInt("HC");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return num;
		 
	}
	
	/**
	 * 获得第n次召开会议
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype){
		StringBuffer sql = new StringBuffer();
		sql.append("select hc num from bd_meet_plan where customerno = ? and HYSX=? and ( MEETTYPE=? or ZYWYH=?) and jc=?");
		sql.append(" order by jc desc,hc desc");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int num = 0;
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
	}/**
	 * 获得第n次召开会议
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype,int hc,String year,String meetpro){
		StringBuffer sql = new StringBuffer();
		sql.append("select max(hc) num from bd_meet_plan where 1=1 ");
		List parameter=new ArrayList();//存放参数
		if(customerno!=null && !"".equals(customerno)){
			sql.append(" and customerno = ?");
			parameter.add(customerno);
		}
		if(year!=null && !"".equals(year)){
			sql.append(" and YEAR = ?");
			parameter.add(year);
		}
		if(jc!=0 ){
			sql.append(" and jc = ?");
			parameter.add(jc);
		}
		if(meettype!=null && !"".equals(meettype)){
			sql.append(" and MEETTYPE = ?");
			parameter.add(meettype);
		}
		if(meetpro!=null && !"".equals(meetpro)){
			sql.append(" and zywyh = ?");
			parameter.add(meetpro);
		}
		if(grouptype!=null && !"".equals(grouptype)){
			sql.append(" and hysx = ?");
			parameter.add(grouptype);
		}
		sql.append(" order by jc desc,hc desc");

		int num = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			while(rset.next()){
				String str = rset.getString(1);
				if(str!=null && !"".equals(str)){
					num = Integer.parseInt(str);
				}
			} 
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rset);
		}
		return num;
	}
	
	
	/**
	 * 获得第n次召开会议
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype,int hc,String year){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) num from bd_meet_plan where customerno = ? and HYSX=? and ( MEETTYPE=? or ZYWYH=?) and "+(meettype.equals("股东大会")==true?" (jc is null or jc=?)":"jc=?")
				+ " and hc=? and year=?");
		sql.append(" order by jc desc,hc desc");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int num = 0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			ps.setString(2, grouptype);
			ps.setString(3, meettype); 
			ps.setString(4, meettype);
			ps.setInt(5, jc);
			ps.setInt(6, hc);
			ps.setString(7, year);
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
	public List<HashMap> getMeetExcl(String customerno){
		
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT BOTABLE.Year, BOTABLE.Meettype,   BOTABLE.Jc,  BOTABLE.Hysx,   BOTABLE.Meetname,  to_char(BOTABLE.JHCJSJ,'yyyy-MM-dd') ctime, ");
		sb.append("   BOTABLE.Status,  b.zqjc,  b.zqdm, to_char(BOTABLE.Plantime,'yyyy-MM-dd') ptime ,BOTABLE.Hc, BOTABLE.Zywyh ");
		sb.append(" FROM BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid ");
		sb.append("  and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 96 and BINDTABLE.metadataid = 109 ");
		sb.append(" AND CUSTOMERNO = ?  ");
		params.add(customerno);
		sb.append("  left join bd_zqb_kh_base b on b.customerno=BOTABLE.CUSTOMERNO ");
		sb.append(" ORDER BY BOTABLE.PLANTIME DESC");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String meettype = (String) object[1];
					BigDecimal jc = (BigDecimal) object[2];
					String hysx = (String) object[3];
					String meetname = (String) object[4];
					String ctime = (String) object[5];
					String status = (String) object[6];
					String zqjc = (String) object[7];
					String zqdm = (String) object[8];
					String ptime = (String) object[9];
					BigDecimal hc = (BigDecimal) object[10];
					String zywyh = (String) object[11];
					map.put("meettype", meettype);
					map.put("jc", jc==null?"":jc);
					map.put("hysx", hysx==null?"":hysx);
					map.put("meetname", meetname);
					map.put("ctime", ctime);
					map.put("status", status);
					map.put("zqjc", zqjc==null?"":zqjc);
					map.put("zqdm", zqdm==null?"":zqdm);
					map.put("ptime", ptime);
					map.put("hc", hc==null?"":hc);
					map.put("zywyh", zywyh==null?"":zywyh);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getMeetRunList1(String customerno,int year,int month ,String meettype,String grouptype,String status,int pageSize, int pageNow){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.YEAR,BOTABLE.MEETTYPE,BOTABLE.MEETNAME,TO_CHAR(BOTABLE.PLANTIME,'YYYY-MM-DD HH24:MI:SS'),BOTABLE.STATUS,TO_CHAR(BOTABLE.JHCJSJ,'YYYY-MM-DD'),BOTABLE.CUSTOMERNO,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_MEET_PLAN  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109");
		if(!meettype.equals("全部")){
			sb.append(" AND MEETTYPE = ? ");
			params.add(meettype);
		}
		if(!status.equals("全部")){
			sb.append(" AND STATUS = ? ");
			params.add(status);
		}
		if(!grouptype.equals("全部")){
			sb.append(" AND HYSX = ? ");
			params.add(grouptype);
		}
		sb.append(" AND CUSTOMERNO = ?  ");
		params.add(customerno);
		sb.append(" ORDER BY BOTABLE.PLANTIME DESC");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal year = (BigDecimal) object[0];
					String meettype = (String) object[1];
					String meetname = (String) object[2];
					String plantime = (String) object[3];
					String status = (String) object[4];
					String createdate = (String) object[5];
					String customerno = (String) object[6];
					BigDecimal id=(BigDecimal)object[7];
					BigDecimal instanceid=(BigDecimal)object[8];
					map.put("MEETNAME", meetname);
					map.put("MEETTYPE", meettype);
					map.put("PLANTIME", plantime);
					map.put("STATUS", status);
					map.put("JHCJSJ", createdate);
					map.put("CUSTOMERNO", customerno);
					map.put("year", year.toString());
					map.put("year1", year.toString());
					map.put("ID", id.toString());
					map.put("INSTANCEID", instanceid.toString());					
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public int getMeetRunListSize(String customerno,int year,int month ,String meettype,String grouptype,String status){
		int count = 0;
		Map params = new HashMap();
		StringBuffer sb = new StringBuffer("select count(*) as NUM FROM BD_MEET_PLAN  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109");
		sb.append(" AND CUSTOMERNO = ? ");
		sb.append(" ORDER BY BOTABLE.ID DESC");
		params.put(1, customerno);
		final String sql1 = sb.toString();
		count = DBUTilNew.getInt( "NUM",sql1,params);
		return count;
	}
	
	
	public List<HashMap> getAppointList(String corpCode,String startDate,String endDate,int eventID,String cxdd,int pageSize, int pageNow){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT B.SXMS,(to_char(B.KSRQ,'yyyy-mm-dd')||'--'||to_char(B.JZRQ,'yyyy-mm-dd')) YYSJ,");
		sb.append("B.ID, B.CUSTOMERNO KHBH,A.YYR,A.YYRQ,B.ID YYSX,B.ZQDM,B.ZQJC,B.CXDD KHFZR,");
		sb.append("(CASE WHEN to_char(YYRQ,'yyyy-mm-dd') >= to_char(SYSDATE,'yyyy-mm-dd') THEN 1 WHEN YYRQ IS NULL THEN 2 ELSE 3 END) SortNum,E.INSTANCEID,E.FJ");
		sb.append(" FROM BD_XP_YYXX A");
		sb.append(" RIGHT JOIN (SELECT DISTINCT SX.*, KH.ZQJC, KH.ZQDM, KH.CUSTOMERNO, DD.KHFZR,CXDD FROM BD_XP_YYSX sx");
		sb.append(" INNER JOIN BD_XP_YYS S ON SX.ID = S.YYSX");			
		sb.append(" INNER JOIN (SELECT KHBH,substr(FHSPR, 1, instr(FHSPR, '[', -1) - 1) USERID,FHSPR KHFZR");
		sb.append(" FROM BD_MDM_KHQXGLB UNION ALL SELECT KHBH,substr(KHFZR, 1, instr(KHFZR, '[', -1) - 1),KHFZR FROM BD_MDM_KHQXGLB) DD");
		sb.append(" ON DD.USERID = S.SZR INNER JOIN BD_ZQB_KH_BASE KH ON KH.CUSTOMERNO = DD.KHBH");
		sb.append(" INNER JOIN (SELECT KHBH,substr(KHFZR, 1, instr(KHFZR, '[', -1) - 1) USERID,KHFZR CXDD FROM BD_MDM_KHQXGLB) CXDD ON KH.CUSTOMERNO = CXDD.KHBH WHERE 1=1");
		sb.append( " AND SX.ID = ?");
		params.add(eventID);
		if(!cxdd.equals("")){
			sb.append(" AND (DD.USERID=? OR CXDD.USERID=?)");
			params.add(cxdd);
			params.add(cxdd);
		}
		sb.append( ") B ON B.ID = A.YYSX AND B.CUSTOMERNO = A.KHBH");
		sb.append( "  LEFT JOIN (SELECT CD.INSTANCEID INSTANCEID, DG.YYID, DG.KHBH, dg.fj FROM BD_XP_DQYYGGFJ DG");
		sb.append( " LEFT JOIN (SELECT INSTANCEID, DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID =");
		sb.append( " (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_XP_DQYYGGFJ')) CD");
		sb.append( " ON DG.ID = CD.DATAID) E ON A.ID = E.YYID AND B.CUSTOMERNO = E.KHBH WHERE 1=1");
		if(!"".equals(startDate)&&startDate!=null)
		{
			sb.append(" AND to_char(YYRQ,'yyyy-MM-dd') >= ?");
			params.add(startDate);
		}
		if(!"".equals(endDate)&&endDate!=null)
		{
			sb.append(" AND to_char(YYRQ,'yyyy-MM-dd') <= ?");
			params.add(endDate);
		}
		if(!"".equals(corpCode)&&corpCode!=null)
		{
			sb.append(" AND B.ZQDM = ?");
			params.add(corpCode);
		}	
		sb.append(" ORDER BY SortNum,YYRQ,B.ZQDM");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();					
					String sxms = (String) object[0];//浜嬮」鎻忚堪
					String yysj = (String) object[1];//浜嬮」棰勭害鍛ㄦ湡
					BigDecimal ids = (BigDecimal) object[2];//棰勭害淇℃伅id
					Long id=null;
					if(!(ids==null)){
						id=ids.longValue();
					}
					String khbh = (String) object[3];//瀹㈡埛鍩烘湰淇℃伅琛ㄤ腑鐨刬d
					String yyr = (String) object[4];//棰勭害浜?
					Date yyrq1 = (Date) object[5];//棰勭害鏃?
					String yyrq = yyrq1==null?"":sdf.format(yyrq1);//棰勭害鏃?	
					String instanceid="";
					if(object[11]!=null)
					{
						instanceid =object[11].toString();
					}
					
					BigDecimal yysx = new BigDecimal(0);
					if(object[6] != null)
					{
						yysx = (BigDecimal) object[6];//棰勭害浜嬮」id
					}
					String zqdm="";
					if(object[7] != null)
					{
						zqdm=(String)object[7];//璇佸埜浠ｇ爜
					}
					String zqjc=(String)object[8];//璇佸埜绠€绉?
					String khfzr=(String)object[9];//鎸佺画鐫ｅ
					String fj = (String) object[12];
					map.put("ID", id);
					map.put("KHBH", khbh);
					map.put("SXMS", sxms);
					map.put("YYSJ", yysj);
					map.put("YYR", yyr);
					map.put("YYRQ", yyrq);
					map.put("YYSX", yysx);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("KHFZR", khfzr);
					map.put("INSTANCEID", instanceid);
					map.put("FJ", fj==null||fj.equals("")?0:fj.split(",").length);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getNewAppointList(String corpNO,String yyrq,int eventID){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		List params = new ArrayList();
		Long orgRoleId = user.getOrgroleid();
		String strWhere = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT B.SXMS,(to_char(B.KSRQ,'yyyy-mm-dd')||'--'||to_char(B.JZRQ,'yyyy-mm-dd')) YYSJFW,A.ID YYXXID,C.CUSTOMERNO,A.YYR,A.YYRQ,B.ID YYSXID,C.ZQDM,C.ZQJC,D.KHFZR,");
		sb.append("(CASE WHEN to_char(YYRQ,'yyyy-mm-dd') >= to_char(SYSDATE,'yyyy-mm-dd') THEN 1 WHEN YYRQ IS NULL THEN 2 ELSE 3 END) SortNum,");
		sb.append("E.INSTANCEID,E.FJ,");
		sb.append("TO_NUMBER(NVL(CASE WHEN (TO_DATE(B.YYSJ,'YYYY-MM-DD HH24:MI') - SYSDATE) * 24 * 60 * 60 > 0 THEN (TO_DATE(B.YYSJ,'YYYY-MM-DD HH24:MI') - SYSDATE) * 24 * 60 * 60 ELSE 0 END,0)) DJS,");
		sb.append("'YYCLNAME'||B.ID AS CLNAME,");
		sb.append("CASE WHEN TO_CHAR(B.JZRQ,'YYYY-MM-DD')<TO_CHAR(SYSDATE,'YYYY-MM-DD') THEN 1 ELSE 0 END SFGQ,");		
		sb.append("CASE WHEN A.KHBH IS NULL THEN 1 ELSE 0 END SFYY");
		sb.append(" FROM (SELECT * FROM BD_XP_YYSX ");
		if(eventID!=0){
				sb.append("WHERE ID=?");
				params.add(eventID);
		}
		sb.append(") B");
		sb.append(" INNER JOIN (");
		sb.append(" SELECT * FROM (");
		sb.append(" SELECT row_number()over(partition by KHBH,YYSX order by SZSJ desc) RN,KHBH,USERID,KHFZR,YYSX FROM BD_XP_YYS YYS");
		sb.append(" INNER JOIN (");
		sb.append(" SELECT KHBH,substr(FHSPR,1,instr(FHSPR,'[',-1)-1) USERID,FHSPR KHFZR FROM BD_MDM_KHQXGLB  ");

		if(orgRoleId!=5){
			sb.append(" WHERE KHBH=?");
			params.add(corpNO);
			}
		sb.append(" UNION ALL");
		sb.append(" SELECT KHBH,substr(KHFZR,1,instr(KHFZR,'[',-1)-1),KHFZR FROM BD_MDM_KHQXGLB  ");
		sb.append(" WHERE KHBH=?");
		params.add(corpNO);
		sb.append(") A ON YYS.SZR = A.USERID");
		sb.append(")  T WHERE T.RN = 1");
		sb.append(") D");
		sb.append(" ON B.ID = D.YYSX");
		sb.append(" LEFT JOIN (SELECT * FROM BD_XP_YYXX ");
		sb.append(" WHERE KHBH=?");
		params.add(corpNO);
		sb.append(") A ON A.KHBH = D.KHBH AND A.YYSX = D.YYSX");
		sb.append(" LEFT JOIN BD_ZQB_KH_BASE C ON C.CUSTOMERNO = D.KHBH");
		sb.append(" LEFT JOIN (SELECT CD.INSTANCEID INSTANCEID,DG.YYID,DG.KHBH,dg.fj FROM BD_XP_DQYYGGFJ DG");
		sb.append(" LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_DQYYGGFJ')) CD ON DG.ID=CD.DATAID ) E");
		sb.append(" ON A.ID=E.YYID AND C.CUSTOMERNO=E.KHBH");
		sb.append(" ORDER BY SortNum,YYRQ,C.ZQDM");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();					
					String sxms = (String) object[0];//浜嬮」鎻忚堪
					String yysj = (String) object[1];//浜嬮」棰勭害鍛ㄦ湡
					BigDecimal id = (BigDecimal) object[2];//棰勭害淇℃伅id
					String khbh = (String) object[3];//瀹㈡埛鍩烘湰淇℃伅琛ㄤ腑鐨刬d
					String yyr = (String) object[4];//棰勭害浜?
					Date yyrq1 = (Date) object[5];//棰勭害鏃?
					String yyrq = yyrq1==null?"":sdf.format(yyrq1);//棰勭害鏃?
					BigDecimal yysx = new BigDecimal(0);
					BigDecimal instanceid1 = (BigDecimal) object[11];//DBUtil.getString("SELECT CD.INSTANCEID INSTANCEID FROM BD_XP_DQYYGGFJ DG LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_DQYYGGFJ')) CD ON DG.ID=CD.DATAID AND DG.YYID="+id+" AND DG.KHBH='"+khbh+"'", "INSTANCEID");
					Long instanceid=null;
					if(instanceid1!=null){
						instanceid=instanceid1.longValue();
					}
					if(object[6] != null)
					{
						yysx = (BigDecimal) object[6];//棰勭害浜嬮」id
					}
					String zqdm="";
					if(object[7] != null)
					{
						zqdm=(String)object[7];//璇佸埜浠ｇ爜
					}
					String zqjc=(String)object[8];//璇佸埜绠€绉?
					String khfzr=(String)object[9];//鎸佺画鐫ｅ
					String fj = (String) object[12];
					BigDecimal djs = (BigDecimal) object[13];
					String clname = (String) object[14];
					BigDecimal sfgq = (BigDecimal) object[15];
					BigDecimal sfyy = (BigDecimal) object[16];
					map.put("YYXXID", id);
					map.put("CUSTOMERNO", khbh);
					map.put("SXMS", sxms);
					map.put("YYSJFW", yysj);
					map.put("YYR", yyr);
					map.put("YYRQ", yyrq);
					map.put("YYSXID", yysx);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("KHFZR", khfzr);
					map.put("FJINSTANCEID", instanceid==null?"":instanceid);
					map.put("FJ", fj);
					map.put("DJS", djs);
					map.put("SFGQ", sfgq);
					map.put("SFYY", sfyy);
					map.put("CLNAME", clname);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getAppointListSize(String corpCode,String startDate,String endDate,int eventID,String cxdd){
		String strWhere = "";
		Map params = new HashMap();
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		int n = 1;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) NUM FROM (");		
		sb.append("SELECT DISTINCT B.SXMS,(to_char(B.KSRQ,'yyyy-mm-dd')||'--'||to_char(B.JZRQ,'yyyy-mm-dd')) YYSJ,");
		sb.append("B.ID, B.CUSTOMERNO KHBH,A.YYR,A.YYRQ,B.ID YYSX,B.ZQDM,B.ZQJC,B.CXDD KHFZR,");
		sb.append("(CASE WHEN to_char(YYRQ,'yyyy-mm-dd') >= to_char(SYSDATE,'yyyy-mm-dd') THEN 1 WHEN YYRQ IS NULL THEN 2 ELSE 3 END) SortNum,E.INSTANCEID,E.FJ");
		sb.append(" FROM BD_XP_YYXX A");
		sb.append(" RIGHT JOIN (SELECT DISTINCT SX.*, KH.ZQJC, KH.ZQDM, KH.CUSTOMERNO, DD.KHFZR,CXDD FROM BD_XP_YYSX sx");
		sb.append(" INNER JOIN BD_XP_YYS S ON SX.ID = S.YYSX");			
		sb.append(" INNER JOIN (SELECT KHBH,substr(FHSPR, 1, instr(FHSPR, '[', -1) - 1) USERID,FHSPR KHFZR");
		sb.append(" FROM BD_MDM_KHQXGLB UNION ALL SELECT KHBH,substr(KHFZR, 1, instr(KHFZR, '[', -1) - 1),KHFZR FROM BD_MDM_KHQXGLB) DD");
		sb.append(" ON DD.USERID = S.SZR INNER JOIN BD_ZQB_KH_BASE KH ON KH.CUSTOMERNO = DD.KHBH");
		sb.append(" INNER JOIN (SELECT KHBH,substr(KHFZR, 1, instr(KHFZR, '[', -1) - 1) USERID,KHFZR CXDD FROM BD_MDM_KHQXGLB) CXDD ON KH.CUSTOMERNO = CXDD.KHBH WHERE 1=1");
		sb.append( " AND SX.ID = ?");		
		params.put(n,eventID);
		if(!cxdd.equals("")){
			sb.append(" AND (DD.USERID=? OR CXDD.USERID=?)");
			n++;
			params.put(n,cxdd);
			n++;
			params.put(n,cxdd);
		}
		sb.append( ") B ON B.ID = A.YYSX AND B.CUSTOMERNO = A.KHBH");
		sb.append( "  LEFT JOIN (SELECT CD.INSTANCEID INSTANCEID, DG.YYID, DG.KHBH, dg.fj FROM BD_XP_DQYYGGFJ DG");
		sb.append( " LEFT JOIN (SELECT INSTANCEID, DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID =");
		sb.append( " (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_XP_DQYYGGFJ')) CD");
		sb.append( " ON DG.ID = CD.DATAID) E ON A.ID = E.YYID AND B.CUSTOMERNO = E.KHBH WHERE 1=1");
		if(!"".equals(startDate)&&startDate!=null)
		{
			sb.append(" AND to_char(YYRQ,'yyyy-MM-dd') >= ?");
			n++;
			params.put(n,startDate);
		}
		if(!"".equals(endDate)&&endDate!=null)
		{
			sb.append(" AND to_char(YYRQ,'yyyy-MM-dd') <= ?");
			n++;
			params.put(n,endDate);
		}
		if(!"".equals(corpCode)&&corpCode!=null)
		{
			sb.append(" AND B.ZQDM = ?");
			n++;
			params.put(n,corpCode);
		}
		sb.append(")");
		String sql1 = sb.toString();
		int count = DBUTilNew.getInt("NUM",sql1,params);
		return count;
	}
	
	
	public List<HashMap> getSXMSList(String cxdd){		
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		if(cxdd != ""){
			sb.append("SELECT A.ID,SXMS FROM BD_XP_YYSX A INNER JOIN BD_XP_YYS B ON A.ID = B.YYSX WHERE B.SZR = ? AND SZS > 0 ORDER BY JZRQ desc");
			params.add(cxdd);
		}
		else{
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			Long orgRoleId = user.getOrgroleid();
			if(orgRoleId==5){
				sb.append("SELECT DISTINCT A.ID,SXMS FROM BD_XP_YYSX A INNER JOIN BD_XP_YYS B ON A.ID = B.YYSX ORDER BY a.id desc");
				
			}else{
				
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();		
				sb.append("SELECT DISTINCT A.ID,SXMS FROM BD_XP_YYSX A INNER JOIN BD_XP_YYS B ON A.ID = B.YYSX");
				sb.append(" INNER JOIN (");
				sb.append(" SELECT substr(FHSPR,1,instr(FHSPR,'[',-1)-1) USERID FROM BD_MDM_KHQXGLB WHERE KHBH=?");
				params.add(uc.get_userModel().getExtend1());
				sb.append(" UNION ALL ");
				sb.append("SELECT substr(KHFZR,1,instr(KHFZR,'[',-1)-1) FROM BD_MDM_KHQXGLB WHERE KHBH=?");
				params.add(uc.get_userModel().getExtend1());
				sb.append(") C ON B.SZR = C.USERID WHERE to_char(a.jzrq,'yyyy-mm-dd') >= to_char(SYSDATE,'yyyy-mm-dd')");
			}
			
		}
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();					
					String sxms = (String) object[1];//事项描述					
//					String id = (String) object[0];//事项id		
					BigDecimal id=(BigDecimal)object[0];
					map.put("ID", id);
					map.put("SXMS", sxms);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getcxddList(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String id = uc.get_orgRole().getId();
		StringBuffer sb=new StringBuffer();
		List params = new ArrayList();
		if("3".equals(id)){
			sb.append("SELECT A.USERID,(A.USERID || '['|| USERNAME || ']' ) UNAME");
			//查询专职持续督导
			sb.append(" FROM (SELECT DISTINCT substr(FHSPR,1,instr(FHSPR,'[',-1)-1) USERID FROM BD_MDM_KHQXGLB WHERE KHBH=?) A");
			params.add(uc.get_userModel().getExtend1());
			sb.append(" INNER JOIN ORGUSER B ON A.USERID = B.USERID");
		}else{			  
			sb.append("SELECT A.USERID,(A.USERID || '['|| USERNAME || ']' ) UNAME");
			sb.append(" FROM (SELECT DISTINCT substr(FHSPR,1,instr(FHSPR,'[',-1)-1) USERID FROM BD_MDM_KHQXGLB) A");
			sb.append(" INNER JOIN ORGUSER B ON A.USERID = B.USERID");
		}
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String id = uc.get_orgRole().getId();
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				int n = 0;
				HashMap test = new HashMap();				
				for (Object[] object : list) {
					map = new HashMap();
					String USERID = (String) object[0];//					
					String UNAME = (String) object[1];//
					map.put("USERID", USERID);
					map.put("UNAME", UNAME);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	//xlj 2015年6月24日16:59:49 获取公司代码和持续督导专员
	public HashMap GETCorpCodeAndCXDD(String customerno)
	{
		HashMap hm = new HashMap();
		try
		{			
			StringBuffer query = new StringBuffer("call GETCorpCodeAndCXDD(?,?)"); //调用存储过程的语法,输出参数用”?“表示			
			Connection conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(1,customerno);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			/*cstmt.registerOutParameter(3, OracleTypes.VARCHAR);*/
			cstmt.execute(); //执行存储过程
			String corpCode = cstmt.getString(2);
			/*String cxdd = cstmt.getString(3);*/
			hm.put("corpCode", corpCode);
			/*hm.put("cxdd", cxdd);*/
			cstmt.close();
			conn.close();
		}
		catch(Exception e)
        {
        	logger.error(e,e);	        	
        }
		return hm;
	}
	
	//添加预约信息
	public HashMap ADDYYXX(String CorpID,String CorpCode,int SXID,String Cxdd,String strYYDate)
	{		
		HashMap hm = new HashMap();
		try
		{		
			/* SUCCESS out NUMBER,--是否成功
			  CorpID IN OUT VARCHAR2,--客户编号
			  CorpCode VARCHAR2,--客户股票代码
			  SXID IN NUMBER, --预约事项ID
			  SZR IN VARCHAR2, --设置人
			  CXDDID IN VARCHAR2, --持续督导人ID
			  YYDATE IN VARCHAR2 --预约日期*/
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();			
			String userID = uc.get_userModel().getUserid();
			StringBuffer query = new StringBuffer("call KYYPACKAGE.ADDYYXX(?,?,?,?,?,?,?)"); //调用存储过程的语法,输出参数用”?“表示			
			Connection conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(2,CorpID);
			cstmt.setString(3,CorpCode);
			cstmt.setInt(4,SXID);
			cstmt.setString(5,userID);
			cstmt.setString(6,Cxdd);
			cstmt.setString(7,strYYDate);			
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
			cstmt.execute(); //执行存储过程
			CorpID = cstmt.getString(2);
			int SUCCESS = cstmt.getInt(1);
			hm.put("CorpID", CorpID);
			hm.put("SUCCESS", SUCCESS);
			cstmt.close();
			conn.close();
		}
		catch(Exception e)
        {
        	logger.error(e,e);	        	
        }
		return hm;
	}
	
	public String DeleteAppoint(String strID)
	{
		try
		{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();			
			String userID = uc.get_userModel().getUserid();
			Map params = new HashMap();
			params.put(1, strID);
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM BD_XP_YYXX where id in(  ");
			String[] strIDs = strID.split(",");
			int n=1;
			for (int i = 0; i < strIDs.length; i++) {
				if(i==(strIDs.length-1)){
					sql.append("?");
				}else{
					sql.append("?,");
				}
				params.put(n,strIDs[i].replaceAll("'", ""));
				n++;
			}
			sql.append(" ) ");
			DBUTilNew.update(sql.toString(),params);	
			
		}
		catch(Exception e)
        {
        	return e.getMessage();        	
        }
		return "";
	}

	//xlj 2015年6月26日15:22:30 更新预约日期
	public String UPDATEAppoint(String strID,String strDate)
	{
		try
		{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();			
			String userID = uc.get_userModel().getUserid();
			String sql = "";	
			Map params = new HashMap();
			params.put(1, userID);
			params.put(2, strDate);
			params.put(3, strID);
			sql = "UPDATE BD_XP_YYXX SET YYR= ? ,CJRQ=SYSDATE,YYRQ=to_date( ? ,'yyyy-mm-dd') where id in( ? )";
			DBUTilNew.update(sql.toString(),params);
		}
		catch(Exception e)
        {
        	return e.getMessage();        	
        }
		return "";
	}
	
	//xlj 2015年6月23日15:31:54 显示可以预约的列表
	//CorpCode 客户股票代码,DateStart 开始查询条件,SXID 预约事项ID,PageSize 一页多少条数据,显示第几页信息
	public List<HashMap> getCanAppoint(String CorpCode,String DateStart,int SXID,int PageSize,int PageIndex)
	{
		List<HashMap> list = new ArrayList<HashMap>();
		try
		{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String	customerno = "";
			String userID = "";
			if("3".equals(uc.get_orgRole().getId()))//角色=3是董秘
			{
				customerno = uc.get_userModel().getExtend1();
			}
			else {userID = uc.get_userModel().getUserid();}
			if(CorpCode == null){CorpCode = "";}
			if(DateStart == null){DateStart = "";}			
			 /*p_CURSOR out KYY_CURSOR,
			  SUCCESS out NUMBER,--是否成功
			  KYYCount out NUMBER,--可预约日期数量
			  ****CorpID IN OUT VARCHAR2,--客户编号
			  CorpCode VARCHAR2,--客户股票代码
			  DateStart DATE,--开始查询条件
			  ****SXID IN OUT NUMBER, --预约事项ID
			  PageSize NUMBER,--一页多少条数据
			  PageIndex NUMBER, --显示第几页信息
			  sxInfo out SX_CURSOR, --待预约的事项信息
			  ExistInfo out VARCHAR2, --存在的描述信息
			  ****CXDDID IN OUT VARCHAR2--持续督导账号*/
			StringBuffer query = new StringBuffer("call KYYPACKAGE.KYYPROC(?,?,?,?,?,?,?,?,?,?,?,?)"); //调用存储过程的语法,输出参数用”?“表示
			Connection conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(4,customerno);
			cstmt.setString(5,CorpCode);
			cstmt.setString(6,DateStart);
			cstmt.setInt(7, SXID);
			cstmt.setInt(8,PageSize);
			cstmt.setInt(9,PageIndex);
			cstmt.setString(12, userID);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR); //注册存储过程的输出参数为游标类型--即resultSet类型
			cstmt.registerOutParameter(2, OracleTypes.NUMBER);
			cstmt.registerOutParameter(3, OracleTypes.NUMBER);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(7, OracleTypes.NUMBER);
			cstmt.registerOutParameter(10, OracleTypes.CURSOR);
			cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
			cstmt.execute(); //执行存储过程
	        
	        int Success = cstmt.getInt(2);
	        int totalNum = cstmt.getInt(3);
	        String ExistInfo = cstmt.getString(11);
	        int sxid = cstmt.getInt(7);
	        HashMap map = new HashMap();
	        map.put("Success",Success);
	        map.put("totalNum",totalNum);
	        map.put("ExistInfo",ExistInfo);
	        map.put("SXID",sxid);
	        list.add(map);
	        List<HashMap> listInfo = new ArrayList<HashMap>();
	        SimpleDateFormat objDate = new SimpleDateFormat("yyyy-MM-dd");
	        
	        try
	        {
	        	if(Success > 0)
	        	{
			        ResultSet rDateset = (ResultSet) cstmt.getObject(1); //获取数据集合
			        while (rDateset.next())  
			        {
			        	map = new HashMap();
			        	int ROWNUM = (int)rDateset.getInt(1);
			        	Date yydate = (Date)rDateset.getDate(2);
			        	String XQ = (String)rDateset.getString(3);
			        	String SYSL = (String)rDateset.getString(6);
			        	map.put("ROWNUM",ROWNUM);
			        	map.put("YYDATE",objDate.format(yydate));
			        	map.put("XQ",XQ);
			        	map.put("SYSL",SYSL);
			        	listInfo.add(map);
			        }
	        	}
	        	else
	        	{
	        		map = new HashMap();
	        		map.put("ROWNUM","");
		        	map.put("YYDATE","");
		        	map.put("XQ","");
		        	map.put("SYSL","");
		        	listInfo.add(map);
	        	}
	        		
	        }
	        catch(Exception e)
	        {
	        	logger.error(e,e);	        	
	        }
	        map = new HashMap();
	        map.put("listInfo", listInfo);
	        list.add(map);
	        listInfo = new ArrayList<HashMap>();
	        try
	        {
	        	if(Success > 0)
	        	{
		        ResultSet rSXset = (ResultSet) cstmt.getObject(10); //获取数据集合
		        while (rSXset.next())  
		        {
		        	map = new HashMap();
		        	int ID = (int)rSXset.getInt(1);
		        	String SXMS = rSXset.getString(2);
		        	Date KSRQ = rSXset.getDate(5);
		        	Date JZRQ = rSXset.getDate(6);	        	        	
		        	map.put("ID",ID);
		        	map.put("SXMS","["+objDate.format(KSRQ)+"~~"+objDate.format(JZRQ)+"]"+SXMS);	        	
		        	listInfo.add(map);
		        }
	        	}
	        }
	        catch(Exception e)
	        {
	        	logger.error(e,e);	        	
	        }
	        map = new HashMap();
	        map.put("listSX", listInfo);
	        list.add(map);
	        cstmt.close();
	        conn.close();
		} catch (Exception e) {  
			logger.error(e,e);
        }  
		return list;
	}

	
	public List<HashMap> getSxmcList() {
		List params = new ArrayList();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(new Date());
		StringBuffer sb = new StringBuffer("SELECT ID,SXMS FROM BD_XP_YYSX where jzrq >=  to_date(?, 'yyyy-MM-dd')");
		params.add(format);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();					
					BigDecimal id = (BigDecimal) object[0];//					
					String sxmc = (String) object[1];//					
					map.put("ID", id);
					map.put("SXMC", sxmc);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public void addBoData(AppointmentNum model) {
		this.getHibernateTemplate().save(model);
	}
	
	public AppointmentYYSX addBoDataAppointmentYYSX(AppointmentYYSX model) {
		this.getHibernateTemplate().save(model);
		return model;
	}
	
	public void updateBoData(AppointmentNum model) {
		this.getHibernateTemplate().update(model);
	}
	
	public void updateBoDataAppointmentYYSX(AppointmentYYSX model) {
		this.getHibernateTemplate().update(model);
	}
	
	public AppointmentNum getBoData(Long id) {
		AppointmentNum model = (AppointmentNum) this.getHibernateTemplate()
				.get(AppointmentNum.class, id);
		return model;
	}
	
	public AppointmentYYSX getBoDataAppointmentYYSX(Long id) {
		AppointmentYYSX model = (AppointmentYYSX) this.getHibernateTemplate()
				.get(AppointmentYYSX.class, id);
		return model;
	}

	
		public List<HashMap> selectNum(String userid, int pageNumber, int pageSize) {
			final int pageSize1 = pageSize;
			final int startRow1 = (pageNumber - 1) * pageSize;	
			StringBuffer sb = new StringBuffer("   select DISTINCT a.ID,a.SXMS,a.CJR,a.CJSJ,a.KSRQ,a.JZRQ,a.YYSJ,b.szs,b.szsj from BD_XP_YYSX a left join BD_XP_YYS b on a.id=b.yysx  ");
			final String sql1 = sb.toString();
			return this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(org.hibernate.Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql1);
					query.setFirstResult(startRow1);
					query.setMaxResults(pageSize1);
					List<HashMap> l = new ArrayList<HashMap>();
					List<Object[]> list = query.list();
					HashMap map;
					HashMap m = new HashMap();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					int n = 0;
					for (Object[] object : list) {
						map = new HashMap();
						//SXID,SXMS,CJR,CJSJ,KSRQ,JZRQ,SZR,SZS,SZSJ,SFSZ,SZSID
						BigDecimal sxid = (BigDecimal) object[0];
						String sxms = (String) object[1];			
						String cjr = (String) object[2];
						Date cjsjTest = (Date) object[3];
						String cjsj = sdf.format(cjsjTest);
						Date ksrqTest = (Date) object[4];
						String ksrq = sdf.format(ksrqTest);
						Date jzrqTest = (Date) object[5];
						String jzrq = sdf.format(jzrqTest);
						String yysjTest = (String) object[6];
						BigDecimal szs = (BigDecimal) object[7];
						String szsj = "";
						if(object[8] != null)
						{
							Date szsjTest = (Date)object[8] ;
							szsj = sdf.format(szsjTest);
						}
						map.put("SXID", sxid);
						map.put("SXMS", sxms);
						map.put("CJR", cjr);
						map.put("CJSJ", cjsj);
						map.put("KSRQ", ksrq);
						map.put("JZRQ", jzrq);
						map.put("SZS", szs);
						map.put("SZSJ", szsj);
						l.add(map);
					}
					return l;
				}
			});
		}
		public int selectNumTotalNum(String userid) {
			int count = 0;
//			Map params = new HashMap();
//			params.put(1, userid);
			StringBuffer sb = new StringBuffer("SELECT COUNT(*) NUM  from (select DISTINCT a.ID,a.SXMS,a.CJR,a.CJSJ,a.KSRQ,a.JZRQ,a.YYSJ,b.szs,b.szsj from BD_XP_YYSX a left join BD_XP_YYS b on a.id=b.yysx)");
			final String sql1 = sb.toString();
			count = DBUtil.getInt(sql1, "NUM");
			return count;
		}
	
//	public int selectNumTotalNum(String userid) {
//		int count = 0;
//		Map params = new HashMap();
//		params.put(1, userid);
//		StringBuffer sb = new StringBuffer("select DISTINCT a.ID,a.SXMS,a.CJR,a.CJSJ,a.KSRQ,a.JZRQ,a.YYSJ,b.szs,b.szsj from BD_XP_YYSX a left join BD_XP_YYS b on a.id=b.yysx");
//		final String sql1 = sb.toString();
//		count = DBUTilNew.getInt("NUM",sql1,params);
//		return count;
//	}
	
	public List<HashMap> getList(String userid) {
		StringBuffer sb = new StringBuffer("select fp.khfzr khfzr,fp.fhspr fhspr,fp.zzspr zzspr,fp.ggfbr ggfbr,fp.khbh khbh,fp.zzcxdd zzcxdd,kh.zqjc zqjc,kh.zqdm zqdm,fp.khmc khmc,fp.id id from BD_MDM_KHQXGLB fp,bd_zqb_kh_base kh where fp.khbh=kh.customerno AND (SUBSTR(fp.KHFZR,0, instr(fp.KHFZR,'[',1)-1)=? or SUBSTR(fp.GGFBR,0, instr(fp.GGFBR,'[',1)-1)=? or SUBSTR(fp.ZZCXDD,0, instr(fp.ZZCXDD,'[',1)-1)=? or SUBSTR(fp.FHSPR,0, instr(fp.FHSPR,'[',1)-1)=? or SUBSTR(fp.ZZSPR,0, instr(fp.ZZSPR,'[',1)-1)=?) order by kh.zqdm");
		String gslx = "";
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, userid);
			ps.setString(2, userid);
			ps.setString(3, userid);
			ps.setString(4, userid);
			ps.setString(5, userid);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				String khfzr = rs.getString("khfzr");
				String fhspr = rs.getString("fhspr");
				String zzspr = rs.getString("zzspr");
				String ggfbr = rs.getString("ggfbr");
				String khbh = rs.getString("khbh");
				String zzcxdd= rs.getString("zzcxdd");
				String zqjc= rs.getString("zqjc");
				String zqdm = rs.getString("zqdm");
				String khmc = rs.getString("khmc");
				String id = rs.getString("id");
				map.put("KHFZR", khfzr);
				map.put("FHSPR", fhspr);
				map.put("ZZSPR", zzspr);
				map.put("GGFBR", ggfbr);
				map.put("KHBH", khbh);
				map.put("ZZCXDD", zzcxdd);
				map.put("ZQJC", zqjc);
				map.put("ZQDM", zqdm);
				map.put("KHMC", khmc);
				map.put("ID", id);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}
	public List<HashMap> getDmList(String corpCode) {
		StringBuffer sb = new StringBuffer("SELECT ORG.USERID,ORG.MOBILE,ORG.EMAIL FROM ORGUSER ORG LEFT JOIN BD_ZQB_KH_BASE KH ON ORG.EXTEND1=KH.CUSTOMERNO WHERE ORG.ORGROLEID=3 AND KH.ZQDM=? AND MOBILE IS NOT NULL");
		String gslx = "";
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, corpCode);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				String userid = rs.getString("USERID");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				map.put("USERID", userid);
				map.put("MOBILE", mobile);
				map.put("EMAIL", email);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}
 

public void deleteBoData(AppointmentNum model) {
		this.getHibernateTemplate().delete(model);
}
	public void deleteAppointmentYYSX(AppointmentYYSX boDataAppointmentYYSX) {
		this.getHibernateTemplate().delete(boDataAppointmentYYSX);
	}

	public List<HashMap> getAppointmentYYS(Long sxid) {
		StringBuffer sb = new StringBuffer("select ID from bd_xp_yys WHERE YYSX=?");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps =conn.prepareStatement(sb.toString());
			ps.setLong(1, sxid);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				BigDecimal yysid=rs.getBigDecimal("ID");
				long longValue = yysid.longValue();
				map.put("yysid", longValue);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}

	public String deleteAppointmentYYXX(Long sxid) {
		try
		{
			Map map=new HashMap();
			map.put(1, sxid);
			String sql = "delete from bd_xp_yyxx where id in (select id from bd_xp_yyxx where yysx=?)";
			DBUTilNew.update(sql, map);	
		}
		catch(Exception e)
        {
        	return e.getMessage();        	
        }
		return "";
	}

	public void saveKmDoc(String uuid, Long directoryId, Long filesize, String filename, Long id,Long order_index, String time) {
		IworkKmDoc ikd = new IworkKmDoc();
		ikd.setId(id);
		ikd.setDocName(filename);
		ikd.setDirectoryid(directoryId);
		ikd.setDocType(0l);
		ikd.setDocEnum(uuid);
		ikd.setStartDate(time);
		ikd.setEndDate("9999-12-31");
		ikd.setKeyword("");
		ikd.setPriority(0l);
		ikd.setFilesize(filesize);
		ikd.setCreateUser(UserContextUtil.getInstance().getCurrentUserId());
		ikd.setCreateDate(time);
		ikd.setVersion(1l);
		ikd.setStatus(1l);
		ikd.setOrderIndex(order_index);
		ikd.setMemo("");
		this.getHibernateTemplate().save(ikd);
	}
	
	public HashMap<String, List<HashMap<String, Object>>> getListMap(String sql,List parameter) {
		HashMap<String,List<HashMap<String, Object>>> listMap = new HashMap<String,List<HashMap<String, Object>>>();
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				if(parameter.get(i) instanceof String){
					ps.setString(index++, parameter.get(i).toString().toUpperCase());
				}else if(parameter.get(i) instanceof Integer){
					ps.setInt(index++, Integer.parseInt(parameter.get(i).toString()));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String clname = rs.getString("CLNAME");
				Long yysxid = rs.getLong("YYSXID");
				String sxms = rs.getString("SXMS");
				String cjr = rs.getString("CJR");
				Long djs = rs.getLong("DJS");
				String yysjfw = rs.getString("YYSJFW");
				String khfzr = rs.getString("KHFZR");
				String customerno = rs.getString("CUSTOMERNO");
				Long sfyy = rs.getLong("SFYY");
				Long sfgq = rs.getLong("SFGQ");
				Long dnum = rs.getLong("DNUM");
				Long yyxxid = rs.getLong("YYXXID");
				String yyrq = rs.getString("YYRQ");
				Long fjinstanceid = rs.getLong("INSTANCEID");
				String fj = rs.getString("FJ");
				result.put("CLNAME", clname);
				result.put("YYSXID", yysxid);
				result.put("SXMS", sxms);
				result.put("CJR", cjr);
				result.put("DJS", djs);
				result.put("YYSJFW", yysjfw);
				result.put("KHFZR", khfzr);
				result.put("CUSTOMERNO", customerno);
				result.put("SFYY", sfyy);
				result.put("SFGQ", sfgq);
				result.put("DNUM", dnum);
				result.put("YYXXID", yyxxid);
				result.put("YYRQ", yyrq);
				result.put("FJINSTANCEID", fjinstanceid);
				result.put("FJ", fj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		listMap.put("dataList", dataList);
		return listMap;
	}

}