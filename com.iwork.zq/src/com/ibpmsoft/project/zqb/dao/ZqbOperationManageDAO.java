package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.ProcessAPI;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ZqbOperationManageDAO extends HibernateDaoSupport{
	private static Logger logger = Logger.getLogger(ZqbOperationManageDAO.class);
	/**
	 * 获得第n次召开会议
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) num from bd_meet_plan where customerno = ? and HYSX=? and ( MEETTYPE=? or ZYWYH=?) and jc=? order by jc desc,hc desc");
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
		StringBuffer sb = new StringBuffer("select fp.khfzr khfzr,fp.fhspr fhspr,fp.zzspr zzspr,fp.ggfbr ggfbr,fp.khbh khbh,fp.zzcxdd zzcxdd,kh.zqjc zqjc,kh.zqdm zqdm,fp.khmc khmc,fp.id id from BD_MDM_KHQXGLB fp,bd_zqb_kh_base kh where fp.khbh=kh.customerno order by kh.zqdm");
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
	
	public List<HashMap> getMeetRunList(int pageSize,int pageNumber,String userid,String customerno,Long orgRoleId,String zqdm,String zqjc,String startdate,String enddate,String noticename,String spzt){
	
			
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				java.util.Date Date1 = null;
				java.util.Date Date2 = null;
				int ct1 = 0;
				if(startdate!=null&&!startdate.equals(""))
					Date1 = format.parse(startdate);
				if(startdate!=null&&!startdate.equals(""))
					Date2 = format.parse(enddate);
				if(Date1!=null&&Date2!=null)
					ct1 = Date1.compareTo(Date2);
			
			if(ct1>0){
				return null;
			}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.ID,B.INSTANCEID,B.COMPANYNO,B.COMPANYNAME,B.SXMC,B.SXGY,B.SXFJ,B.EXTEND1,"
				+ "B.EXTEND2,B.EXTEND3,B.EXTEND4,B.EXTEND5,B.LCBS,B.LCBH,B.TASKID,B.SPZT,B.STEPID,"
				+ "B.CREATEUSER,B.CREATEDATE,B.KHBH,B.KHMC,B.SXLX,B.NOTICETYPE,B.QCRID,B.YXID,"
				+ "B.CREATENAME,B.NOTICESQ,K.ZQJC,K.ZQDM,T.TALKNUM,D.YJINS FROM BD_XP_RCYWCBZSJ B "
				+ "LEFT JOIN BD_ZQB_KH_BASE K ON B.KHBH=K.CUSTOMERNO "
				+ "LEFT JOIN (SELECT SXBH,COUNT(SXBH) AS TALKNUM FROM BD_XP_XXPLJLB GROUP BY SXBH) T ON B.INSTANCEID=T.SXBH "
				+ "LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.INSTANCEID=D.GGINS WHERE 1=1");
		if(orgRoleId==3l){
			sb.append(" AND B.KHBH=?");
			params.add(customerno);
		}
		if(orgRoleId!=3l&&orgRoleId!=5l){
			sb.append(" AND B.KHBH IN(");
			sb.append("SELECT KHBH FROM BD_MDM_KHQXGLB WHERE"); 
			sb.append(" SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(ZZCXDD,0, INSTR(ZZCXDD,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(FHSPR,0, INSTR(FHSPR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(CWSCBFZR2,0, INSTR(CWSCBFZR2,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(CWSCBFZR3,0, INSTR(CWSCBFZR3,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(FBBWJSHR,0, INSTR(FBBWJSHR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(GGFBR,0, INSTR(GGFBR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(QYNBRYSH,0, INSTR(QYNBRYSH,'[',1)-1) = ?");
			sb.append(")");
			params.add(userid);params.add(userid);params.add(userid);params.add(userid);
			params.add(userid);params.add(userid);params.add(userid);params.add(userid);params.add(userid);
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND B.KHBH=?");
			params.add(customerno);
		}
		if(zqdm!=null&&!"".equals(zqdm)){
			sb.append(" AND K.ZQDM LIKE ?");
			params.add("%"+zqdm+"%");
		}
		if(zqjc!=null&&!"".equals(zqjc)){
			sb.append(" AND K.ZQJC LIKE ?");
			params.add("%"+zqjc+"%");
		}
		if(startdate!=null&&!"".equals(startdate)){
			sb.append(" AND B.CREATEDATE>=TO_DATE(?,'yyyy-MM-dd')");
			params.add(startdate);
		}
		if(enddate!=null&&!"".equals(enddate)){
			sb.append(" AND B.CREATEDATE<=TO_DATE(?,'yyyy-MM-dd')");
			params.add(enddate);
		}
		if(noticename!=null&&!"".equals(noticename)){
			sb.append(" AND B.SXMC LIKE ?");
			params.add("%"+noticename+"%");
		}
		if(spzt!=null&&!"".equals(spzt)){
			sb.append("  AND B.SPZT =?");
			params.add(spzt);
		}
		sb.append(" ORDER BY B.ID DESC");
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param=params;
		final String sql1 = sb.toString();
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					BigDecimal INSTANCEID = (BigDecimal)object[1];
					String COMPANYNO = (String)object[2];
					String COMPANYNAME = (String)object[3];
					String SXMC = (String)object[4];
					String SXGY = (String)object[5];
					String SXFJ = (String)object[6];
					String EXTEND1 = (String)object[7];
					String EXTEND2 = (String)object[8];
					String EXTEND3 = (String)object[9];
					String EXTEND4 = (String)object[10];
					String EXTEND5 = (String)object[11];
					BigDecimal LCBS = (BigDecimal)object[12];
					String LCBH = (String)object[13];
					BigDecimal TASKID = (BigDecimal)object[14];
					String SPZT = (String)object[15];
					String STEPID = (String)object[16];
					String CREATEUSER = (String)object[17];
					Date CREATEDATE = (Date)object[18];
					String KHBH = (String)object[19];
					String KHMC = (String)object[20];
					String SXLX = (String)object[21];
					String NOTICETYPE = (String)object[22];
					String QCRID = (String)object[23];
					String YXID = (String)object[24];
					String CREATENAME = (String)object[25];
					String NOTICESQ = (String)object[26];
					String ZQJC = (String)object[27];
					String ZQDM = (String)object[28];
					BigDecimal TALKNUM = (BigDecimal)object[29];
					BigDecimal YJINS = (BigDecimal)object[30];

					map.put("ID", ID.toString());
					map.put("INSTANCEID", INSTANCEID.toString());
					map.put("COMPANYNO", COMPANYNO);
					map.put("COMPANYNAME", COMPANYNAME);
					map.put("SXMC", SXMC);
					map.put("SXGY", SXGY);
					map.put("SXFJ", SXFJ);
					map.put("EXTEND1", EXTEND1);
					map.put("EXTEND2", EXTEND2);
					map.put("EXTEND3", EXTEND3);
					map.put("EXTEND4", EXTEND4);
					map.put("EXTEND5", EXTEND5);
					map.put("LCBS", LCBS.toString());
					map.put("LCBH", LCBH);
					map.put("TASKID", TASKID.toString());
					map.put("SPZT", SPZT);
					map.put("STEPID", STEPID);
					map.put("CREATEUSER", CREATEUSER);
					map.put("CREATEDATE", CREATEDATE);
					map.put("KHBH", KHBH);
					map.put("KHMC", KHMC);
					map.put("SXLX", SXLX);
					map.put("NOTICETYPE", NOTICETYPE);
					map.put("QCRID", QCRID);
					map.put("YXID", YXID);
					map.put("CREATENAME", CREATENAME);
					map.put("NOTICESQ", NOTICESQ);
					map.put("ZQJC", ZQJC);
					map.put("ZQDM", ZQDM);
					map.put("TALKNUM", TALKNUM==null?0:TALKNUM.intValue());
					map.put("YJINS", YJINS==null?"":YJINS.intValue());
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(LCBH, LCBS.longValue());
				    if(pro!=null&pro.size()>0){
				    	Long prc=pro.get(0).getPrcDefId();
				    	map.put("PRCID", prc.toString());
				    }
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getEventRunListSize(String userid,String customerno,Long orgRoleId,String zqdm,String zqjc,String startdate,String enddate,String noticename,String spzt){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date Date1 = null;
			java.util.Date Date2 = null;
			int ct1 = 0;
			if(startdate!=null&&!startdate.equals(""))
				Date1 = format.parse(startdate);
			if(startdate!=null&&!startdate.equals(""))
				Date2 = format.parse(enddate);
			if(Date1!=null&&Date2!=null)
				ct1 = Date1.compareTo(Date2);
		
		if(ct1>0){
			return null;
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.ID,B.INSTANCEID,B.COMPANYNO,B.COMPANYNAME,B.SXMC,B.SXGY,B.SXFJ,B.EXTEND1,"
				+ "B.EXTEND2,B.EXTEND3,B.EXTEND4,B.EXTEND5,B.LCBS,B.LCBH,B.TASKID,B.SPZT,B.STEPID,"
				+ "B.CREATEUSER,B.CREATEDATE,B.KHBH,B.KHMC,B.SXLX,B.NOTICETYPE,B.QCRID,B.YXID,"
				+ "B.CREATENAME,B.NOTICESQ,K.ZQJC,K.ZQDM,T.TALKNUM FROM BD_XP_RCYWCBZSJ B "
				+ "LEFT JOIN BD_ZQB_KH_BASE K ON B.KHBH=K.CUSTOMERNO "
				+ "LEFT JOIN (SELECT SXBH,COUNT(SXBH) AS TALKNUM FROM BD_XP_XXPLJLB GROUP BY SXBH) T ON B.INSTANCEID=T.SXBH WHERE 1=1");
		if(orgRoleId==3l){
			sb.append(" AND B.KHBH=?");
			params.add(customerno);
		}
		if(orgRoleId!=3l&&orgRoleId!=5l){
			sb.append(" AND B.KHBH IN(");
			sb.append("SELECT KHBH FROM BD_MDM_KHQXGLB WHERE"); 
			sb.append(" SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(ZZCXDD,0, INSTR(ZZCXDD,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(FHSPR,0, INSTR(FHSPR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(CWSCBFZR2,0, INSTR(CWSCBFZR2,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(CWSCBFZR3,0, INSTR(CWSCBFZR3,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(FBBWJSHR,0, INSTR(FBBWJSHR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(GGFBR,0, INSTR(GGFBR,'[',1)-1) = ?");
			sb.append(" OR SUBSTR(QYNBRYSH,0, INSTR(QYNBRYSH,'[',1)-1) = ?");
			sb.append(")");
			params.add(userid);params.add(userid);params.add(userid);params.add(userid);
			params.add(userid);params.add(userid);params.add(userid);params.add(userid);params.add(userid);
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND B.KHBH=?");
			params.add(customerno);
		}
		if(zqdm!=null&&!"".equals(zqdm)){
			sb.append(" AND K.ZQDM LIKE ?");
			params.add("%"+zqdm+"%");
		}
		if(zqjc!=null&&!"".equals(zqjc)){
			sb.append(" AND K.ZQJC LIKE ?");
			params.add("%"+zqjc+"%");
		}
		if(startdate!=null&&!"".equals(startdate)){
			sb.append(" AND B.CREATEDATE>=TO_DATE(?,'yyyy-MM-dd')");
			params.add(startdate);
		}
		if(enddate!=null&&!"".equals(enddate)){
			sb.append(" AND B.CREATEDATE<=TO_DATE(?,'yyyy-MM-dd')");
			params.add(enddate);
		}
		if(noticename!=null&&!"".equals(noticename)){
			sb.append(" AND B.SXMC LIKE ?");
			params.add("%"+noticename+"%");
		}
		if(spzt!=null&&!"".equals(spzt)){
			sb.append("  AND B.SPZT =?");
			params.add(spzt);
		}
		sb.append(" ORDER BY B.ID DESC");
		final List param=params;
		final String sql1 = sb.toString();
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
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					BigDecimal INSTANCEID = (BigDecimal)object[1];
					String COMPANYNO = (String)object[2];
					String COMPANYNAME = (String)object[3];
					String SXMC = (String)object[4];
					String SXGY = (String)object[5];
					String SXFJ = (String)object[6];
					String EXTEND1 = (String)object[7];
					String EXTEND2 = (String)object[8];
					String EXTEND3 = (String)object[9];
					String EXTEND4 = (String)object[10];
					String EXTEND5 = (String)object[11];
					BigDecimal LCBS = (BigDecimal)object[12];
					String LCBH = (String)object[13];
					BigDecimal TASKID = (BigDecimal)object[14];
					String SPZT = (String)object[15];
					String STEPID = (String)object[16];
					String CREATEUSER = (String)object[17];
					Date CREATEDATE = (Date)object[18];
					String KHBH = (String)object[19];
					String KHMC = (String)object[20];
					String SXLX = (String)object[21];
					String NOTICETYPE = (String)object[22];
					String QCRID = (String)object[23];
					String YXID = (String)object[24];
					String CREATENAME = (String)object[25];
					String NOTICESQ = (String)object[26];
					String ZQJC = (String)object[27];
					String ZQDM = (String)object[28];
					BigDecimal TALKNUM = (BigDecimal)object[29];

					map.put("ID", ID.toString());
					map.put("INSTANCEID", INSTANCEID.toString());
					map.put("COMPANYNO", COMPANYNO);
					map.put("COMPANYNAME", COMPANYNAME);
					map.put("SXMC", SXMC);
					map.put("SXGY", SXGY);
					map.put("SXFJ", SXFJ);
					map.put("EXTEND1", EXTEND1);
					map.put("EXTEND2", EXTEND2);
					map.put("EXTEND3", EXTEND3);
					map.put("EXTEND4", EXTEND4);
					map.put("EXTEND5", EXTEND5);
					map.put("LCBS", LCBS.toString());
					map.put("LCBH", LCBH);
					map.put("TASKID", TASKID.toString());
					map.put("SPZT", SPZT);
					map.put("STEPID", STEPID);
					map.put("CREATEUSER", CREATEUSER);
					map.put("CREATEDATE", CREATEDATE);
					map.put("KHBH", KHBH);
					map.put("KHMC", KHMC);
					map.put("SXLX", SXLX);
					map.put("NOTICETYPE", NOTICETYPE);
					map.put("QCRID", QCRID);
					map.put("YXID", YXID);
					map.put("CREATENAME", CREATENAME);
					map.put("NOTICESQ", NOTICESQ);
					map.put("ZQJC", ZQJC);
					map.put("ZQDM", ZQDM);
					map.put("TALKNUM", TALKNUM==null?0:TALKNUM.intValue());
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(LCBH, LCBS.longValue());
				    if(pro!=null&pro.size()>0){
				    	Long prc=pro.get(0).getPrcDefId();
				    	map.put("PRCID", prc.toString());
				    }
					l.add(map);
				}
				return l;
			}
		});
	}
	
}
 