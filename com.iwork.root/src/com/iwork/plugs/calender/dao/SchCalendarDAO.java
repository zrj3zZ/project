package com.iwork.plugs.calender.dao;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import bios.report.hazelnut.sb;

import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.calender.model.IworkSchCalendar;

public class SchCalendarDAO extends HibernateDaoSupport {

	public List<String> getConfigTypeUsers() {
		// String sql =
		// "SELECT DISTINCT USERID FROM SYS_PERSON_CONFIG WHERE TYPE='CalendarIsShare' AND VALUE=1";
		StringBuffer sql = new StringBuffer();
		sql.append("select userid from orguser t1 where t1.usertype<>2 and not exists (SELECT DISTINCT USERID FROM SYS_PERSON_CONFIG t2 WHERE TYPE='CalendarIsShare' AND VALUE='0' and t1.userid = t2.userid )");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		List<String> list = new ArrayList();
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String userid = rset.getString("USERID");
				list.add(userid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 获取指定用户的全部日程
	 * 
	 * @param userid
	 * @return
	 */
	public List<IworkSchCalendar> getUserCalendarList(String userid) {
		String sql = "FROM " + IworkSchCalendar.DATABASE_ENTITY + " WHERE USERID=? ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		Object[] value = {userid};
		List<IworkSchCalendar> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}

	/**
	 * 获得时间段内指定用户的一般日程
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public List<IworkSchCalendar> getPeriodList(Date startdate, Date enddate,
			String userid) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(IworkSchCalendar.class);
//		criteria.add(Restrictions.isNull("reStartdate"));
		criteria.add(Restrictions.eq("userid", userid));
		criteria.add(Restrictions.or(Restrictions.and(
				Restrictions.le("startdate", startdate),
				Restrictions.ge("enddate", startdate)), Restrictions.or(
				Restrictions.and(Restrictions.ge("startdate", startdate),
						Restrictions.le("enddate", enddate)), Restrictions.and(
						Restrictions.le("startdate", enddate),
						Restrictions.ge("enddate", enddate)))));
		List<IworkSchCalendar> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 获得时间段内指定用户的重复事件
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public List<IworkSchCalendar> getPeriodList_Repeate(String userid) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(IworkSchCalendar.class);
		criteria.add(Restrictions.isNotNull("reStartdate"));
		criteria.add(Restrictions.eq("userid", userid));
		List<IworkSchCalendar> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public IworkSchCalendar getBoData(Long id) {
		IworkSchCalendar model = (IworkSchCalendar) this.getHibernateTemplate()
				.get(IworkSchCalendar.class, id);
		return model;
	}
	public String getFlag(Long id) {
		String a= DBUtil.getString("   select count(*) A from bd_xp_xpsxqtb s where s.xpsxid='"+id+"' and  s.customerno is not null", "A");
		return a;
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateBoData(IworkSchCalendar model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 插入
	 * 
	 * @param model
	 */
	public void addBoData(IworkSchCalendar model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 删除
	 * 
	 * @param model
	 */
	public void deleteBoData(IworkSchCalendar model) {
		this.getHibernateTemplate().delete(model);
	}
	public List<Map> getGzrzxx(int pageSize, int pageNow,
			String startDate, String endDate,String gznr,String userid) {	
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(gznr!=null&&!"".equals(gznr)){
			if(d.HasInjectionData(gznr)){
				return l;
			}
		}
		if(startDate!=null&&!"".equals(startDate)){
			if(d.HasInjectionData(startDate)){
				return l;
			}
		}
		if(endDate!=null&&!"".equals(endDate)){
			if(d.HasInjectionData(endDate)){
				return l;
			}
		}
		
		StringBuffer sql = new StringBuffer();
		 
		
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		sql.append(" select * from (SELECT t.*,rownum rn from ( ");
		sql.append("   SELECT J.USERID,  J.TITLE,   TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME STARTDATE, TO_CHAR(J.ENDDATE, 'YYYY-MM-DD') || ' ' || J.ENDTIME ENDDATE,  ");
		sql.append("   J.STARTDATE AS JS,  J.ENDDATE AS JE,J.ID,  J.REMARK,  J.WCZT,  J.WCQK,   J.EXTENDS1,   J.UIDS  ");
		sql.append("  FROM (   SELECT * FROM (SELECT C.*, ROWNUM RN FROM (  SELECT O.USERID UIDS,A.*, USERNAME,O.DEPARTMENTNAME from ORGUSER O left join     IWORK_SCH_CALENDAR A    ");
		sql.append("   ON A.USERID = O.USERID  AND O.ORGROLEID<>3   AND A.RE_MODE IS NULL  ORDER BY A.USERID, A.STARTDATE desc ) C) B ");
		sql.append("      ) J where J.USERID=?  ");
		
		
		  if (gznr != null && !"".equals(gznr)) {
              sql.append(" AND J.TITLE LIKE ?");
         }
		 if (startDate != null && !"".equals(startDate)) {
	          sql.append(" AND TO_CHAR(J.STARTDATE,'YYYY-MM-DD')>= ?");
	     }
	     if (endDate != null && !"".equals(endDate)) {
	         sql.append(" AND TO_CHAR(J.ENDDATE,'YYYY-MM-DD')<= ?");
	     }
		sql.append(" order by j.STARTDATE desc  ");
		sql.append(" ) t) where rn> ? and rn< ?");
		final String sql1 = sql.toString();
		
		final String final_gznr = gznr;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		final String final_userid=userid;
		final int final_startRow1 = startRow1;
		final int final_pageSize1 = pageSize1;
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
			
				if (final_userid != null && !"".equals(final_userid)) {
					query.setString(j, final_userid);j++;
				}
				if (final_gznr != null && !"".equals(final_gznr)) {
					query.setString(j, "%"+final_gznr+"%");j++;
				}
				if (final_startDate != null && !"".equals(final_startDate)) {
					query.setString(j, final_startDate);j++;
				}
				if (final_endDate != null && !"".equals(final_endDate)) {
					query.setString(j, final_endDate);j++;
				}
				query.setInteger(j, final_startRow1);j++;
				query.setInteger(j, final_pageSize1);j++;
				//query.setFirstResult(startRow1);
				//query.setMaxResults(pageSize1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				
				for (Object[] object : list) {
					map = new HashMap();
					String userid = (String) object[0];
					String title = (String) object[1];
					String startDate = (String) object[2];
					String endDate = (String) object[3];
					BigDecimal id = (BigDecimal) object[6];
					String remark = (String) object[7];
					String wczt = (String) object[8];
					String wcqk = (String) object[9];
					String extends1 = (String) object[10];
					
					map.put("userid", userid);
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					map.put("id", id==null?"":id.toString());
					map.put("remark", remark);
					map.put("wczt", wczt);
					map.put("wcqk", wcqk);
					map.put("extends1", extends1);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<Map> getGzrzxxSize(String startDate, String endDate,String gznr,String userid) {	
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(gznr!=null&&!"".equals(gznr)){
			if(d.HasInjectionData(gznr)){
				return l;
			}
		}
		if(startDate!=null&&!"".equals(startDate)){
			if(d.HasInjectionData(startDate)){
				return l;
			}
		}
		if(endDate!=null&&!"".equals(endDate)){
			if(d.HasInjectionData(endDate)){
				return l;
			}
		}
		
		StringBuffer sql = new StringBuffer();
		 
	
	
		sql.append("   SELECT J.USERID,  J.TITLE,   TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME STARTDATE, TO_CHAR(J.ENDDATE, 'YYYY-MM-DD') || ' ' || J.ENDTIME ENDDATE,  ");
		sql.append("   J.STARTDATE AS JS,  J.ENDDATE AS JE,J.ID,  J.REMARK,  J.WCZT,  J.WCQK,   J.EXTENDS1,   J.UIDS  ");
		sql.append("  FROM (   SELECT * FROM (SELECT C.*, ROWNUM RN FROM (  SELECT O.USERID UIDS,A.*, USERNAME,O.DEPARTMENTNAME from ORGUSER O left join     IWORK_SCH_CALENDAR A    ");
		sql.append("   ON A.USERID = O.USERID  AND O.ORGROLEID<>3   AND A.RE_MODE IS NULL  ORDER BY A.USERID, A.STARTDATE desc ) C) B ");
		sql.append("      ) J where J.USERID=?  ");
		
		
		  if (gznr != null && !"".equals(gznr)) {
              sql.append(" AND J.TITLE LIKE ?");
         }
		 if (startDate != null && !"".equals(startDate)) {
	          sql.append(" AND TO_CHAR(J.STARTDATE,'YYYY-MM-DD')>= ?");
	     }
	     if (endDate != null && !"".equals(endDate)) {
	         sql.append(" AND TO_CHAR(J.ENDDATE,'YYYY-MM-DD')<= ?");
	     }
		sql.append(" order by j.STARTDATE desc  ");
		final String sql1 = sql.toString();
		
		final String final_gznr = gznr;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		final String final_userid=userid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
			
				if (final_userid != null && !"".equals(final_userid)) {
					query.setString(j, final_userid);j++;
				}
				if (final_gznr != null && !"".equals(final_gznr)) {
					query.setString(j, "%"+final_gznr+"%");j++;
				}
				if (final_startDate != null && !"".equals(final_startDate)) {
					query.setString(j, final_startDate);j++;
				}
				if (final_endDate != null && !"".equals(final_endDate)) {
					query.setString(j, final_endDate);j++;
				}
				//query.setInteger(j, final_startRow1);j++;
				//query.setInteger(j, final_pageSize1);j++;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				
				for (Object[] object : list) {
					map = new HashMap();
					String userid = (String) object[0];
					
					map.put("userid", userid);
					
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<Map> getWorkLogList(int pageSize, int pageNow, String username,
			 int type, String cuid,String depname) {	
		UserContext uc = UserContextUtil.getInstance().getUserContext(cuid);
		Long orgroleid = uc.get_userModel().getOrgroleid();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(username!=null&&!"".equals(username)){
			if(d.HasInjectionData(username)){
				return l;
			}
		}
	
		
		StringBuffer sql = new StringBuffer();
		 
		
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
	
		sql.append(" select * from (  select tt.*,rownum rnw from (   select t.*,rownum rn from (select * from (SELECT J.USERID,  J.USERNAME,  J.DEPARTMENTNAME, D.NUM,  J.TITLE, TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME STARTDATE,  ");
		sql.append(" TO_CHAR(J.ENDDATE, 'YYYY-MM-DD') || ' ' || J.ENDTIME ENDDATE,  J.ID,  J.REMARK, J.WCZT,  J.WCQK,  J.EXTENDS1,   J.UIDS,  J.DEPARTMENTID,  ");
		sql.append("  row_number() OVER(PARTITION BY  J.UIDS ORDER BY   TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME  desc) as row_flg   FROM (  SELECT USERID,UIDS, COUNT(USERID) NUM  ");
		sql.append("  FROM (SELECT USERID,UIDS  FROM(SELECT  USERID, USERNAME,UIDS, ROWNUM RN  ");
		sql.append("    FROM (SELECT A.*, O.USERNAME,O.USERID UIDS FROM ORGUSER O LEFT JOIN IWORK_SCH_CALENDAR A ON A.USERID = O.USERID WHERE O.ORGROLEID<>3 AND SYSDATE<O.ENDDATE AND A.RE_MODE IS NULL ");
		sql.append("    ORDER BY A.USERID, A.STARTDATE ) C) B )  GROUP BY USERID,UIDS order by userid  ) D  ");
		sql.append("  LEFT JOIN ( SELECT * FROM (SELECT C.*, ROWNUM RN FROM (  SELECT O.USERID UIDS,A.*, USERNAME,O.DEPARTMENTNAME,O.DEPARTMENTID from ORGUSER O left join     ");
		sql.append("   (SELECT * FROM IWORK_SCH_CALENDAR   ) A  ");
		sql.append("    ON A.USERID = O.USERID   WHERE SYSDATE<O.ENDDATE    ORDER BY A.USERID, A.STARTDATE desc ) C) B )J  ON J.UIDS = D.UIDS) temp  ");
		sql.append(" where temp.row_flg  = '1'  order by userid) t where 1=1    ");
		  if (username != null && !"".equals(username)) {
              sql.append(" AND t.USERNAME LIKE ?");
         }
		  if (depname != null && !"".equals(depname)) {
              sql.append(" AND t.departmentname LIKE ?");
         }
		 if(orgroleid!=5){
			 if(uc.get_userModel().getIsmanager()!=null && uc.get_userModel().getIsmanager()==1){
				  sql.append(" AND t.DEPARTMENTID = '"+uc.get_userModel().getDepartmentid()+"'");
			 }else{
				 sql.append(" AND t.UIDS = '"+uc.get_userModel().getUserid()+"'");
			 }
		 }
		sql.append(" order by t.STARTDATE desc, num desc ) tt    ) te   ");
		if(pageSize!=0 || pageNow!=0)
			sql.append("where te.rnw> ? and te.rnw<=?");
		final String sql1 = sql.toString();
		final Long final_orgroleid = orgroleid;
		final String final_departmentname = depname;
		final String final_username = username;
		final int final_startRow1 = startRow1;
		final int final_pageSize1 = pageSize1;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
			
				if (final_username != null && !"".equals(final_username)) {
					query.setString(j, "%"+final_username+"%");j++;
				}
				if (final_departmentname != null && !"".equals(final_departmentname)) {
					query.setString(j, "%"+final_departmentname+"%");j++;
				}
				if(final_startRow1!=0 || final_pageSize1!=0){
					query.setInteger(j, final_startRow1);j++;
					query.setInteger(j, final_pageSize1);j++;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				
				for (Object[] object : list) {
					map = new HashMap();
					String username = (String) object[1];
					String departmentname = (String) object[2];
					BigDecimal num = (BigDecimal) object[3];
					String title = (String) object[4];
					String startDate = (String) object[5];
					String endDate = (String) object[6];
					BigDecimal id = (BigDecimal) object[7];
					String extends1 = (String) object[11];
					String userid = (String) object[12];
					String wczt = (String) object[9];
					String wcqk = (String) object[10];
					String remark = (String) object[8];
					map.put("userid", userid);
					map.put("username", username);
					map.put("num", num==null?"1":num.toString());
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					map.put("id", id==null?"":id.toString());
					map.put("remark", remark);
					map.put("wczt", wczt);
					map.put("wcqk", wcqk);
					map.put("depname", departmentname);
					map.put("extends1", extends1);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<Map> getWorkLogLists( String username,
			String startDate, String endDate, int type, String departmentname, String cuid) {		
		UserContext uc = UserContextUtil.getInstance().getUserContext(cuid);
		Long orgroleid = uc.get_userModel().getOrgroleid();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(username!=null&&!"".equals(username)){
			if(d.HasInjectionData(username)){
				return l;
			}
		}
		if(startDate!=null&&!"".equals(startDate)){
			if(d.HasInjectionData(startDate)){
				return l;
			}
		}
		if(endDate!=null&&!"".equals(endDate)){
			if(d.HasInjectionData(endDate)){
				return l;
			}
		}
		if(departmentname!=null&&!"".equals(departmentname)){
			if(d.HasInjectionData(departmentname)){
				return l;
			}
		}
		if(cuid!=null&&!"".equals(cuid)){
			if(d.HasInjectionData(cuid)){
				return l;
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT J.USERID,J.USERNAME,D.NUM,J.TITLE,TO_CHAR(J.STARTDATE, 'YYYY-MM-DD')||' '||J.STARTTIME STARTDATE,TO_CHAR(J.ENDDATE, 'YYYY-MM-DD')||' '||J.ENDTIME ENDDATE"
				+ ",J.ID,J.STARTDATE AS JS,J.ENDDATE AS JE,J.STARTTIME,J.ENDTIME,J.ISALLDAY,J.ISALERT,J.ALERTTIME,J.ISSHARING,J.REMARK,J.RE_STARTDATE,J.RE_ENDDATE,J.RE_STARTTIME"
				+ ",J.RE_ENDTIME,J.RE_MODE,J.RE_DAY_INTERVAL,J.RE_WEEK_DATE,J.RE_MONTH_DAYS,J.RE_YEAR_MONTH,J.RE_YEAR_DAYS ,J.WCZT,J.WCQK,J.EXTENDS1  "
				+ " FROM (");
		       sql.append(" SELECT * FROM (");
		              sql.append(" SELECT C.*,ROWNUM RN FROM(SELECT A.*,USERNAME FROM IWORK_SCH_CALENDAR A LEFT JOIN ORGUSER O ON A.USERID=O.USERID WHERE 1=1 AND (O.ORGROLEID <> 3 OR O.ORGROLEID IS NULL)");
		              
		              if(type==0){
		            	  sql.append(" AND A.USERID = ?");
		              }else if(type==1){
		            	  if(!(orgroleid==5)){
		            	  sql.append(" AND A.USERID IN (SELECT USERID FROM ORGUSER WHERE DEPARTMENTNAME IN("); 
		            	  String[] departmentnames = departmentname.split(",");
		            	  for (int i = 0; i < departmentnames.length; i++) {
		            		  if(i==(departmentnames.length-1)){
		            			  sql.append("?");
		            		  }else{
		            			  sql.append("?,");
		            		  }
		            	  }
		            	  sql.append("))");
		            	  }
		              }else if(type==2){
		            	  sql.append(" AND A.USERID = ?");
		              }
		              
		              if (username != null && !"".equals(username)) {
		              sql.append(" AND O.USERNAME LIKE ?");
		              }
		              if (startDate != null && !"".equals(startDate)) {
		              sql.append(" AND TO_CHAR(A.STARTDATE,'YYYY-MM-DD')>= ?");
		              }
		              if (endDate != null && !"".equals(endDate)) {
		              sql.append(" AND TO_CHAR(A.ENDDATE,'YYYY-MM-DD')<= ?");
		              }
		       sql.append(" ORDER BY A.USERID, A.STARTDATE) C) B ");
		sql.append(" ) J");
		sql.append(" LEFT JOIN (");
		       sql.append(" SELECT USERID,COUNT(USERID) NUM FROM (");
		              sql.append(" SELECT USERID FROM (");
		                     sql.append(" SELECT USERID,USERNAME,ROWNUM RN FROM(SELECT A.*,USERNAME FROM IWORK_SCH_CALENDAR A LEFT JOIN ORGUSER O ON A.USERID=O.USERID WHERE 1=1 AND (O.ORGROLEID <> 3 OR O.ORGROLEID IS NULL)");
		                     
		                     if(type==0){
				            	  sql.append(" AND A.USERID = ?");
				             }else if(type==1){
				            	 if(!(orgroleid==5)){
				            	  sql.append(" AND A.USERID IN (SELECT USERID FROM ORGUSER WHERE DEPARTMENTNAME IN(");
				            	  String[] departmentnames = departmentname.split(",");
				            	  for (int i = 0; i < departmentnames.length; i++) {
				            		  if(i==(departmentnames.length-1)){
				            			  sql.append("?");
				            		  }else{
				            			  sql.append("?,");
				            		  }
				            	  }
				            	  sql.append("))");
				            	 }
				             }else if(type==2){
				            	  sql.append(" AND A.USERID = ?");
				             }
		                     
		                     if (username != null && !"".equals(username)) {
		   		              sql.append(" AND O.USERNAME LIKE ?");
		   		              }
		   		              if (startDate != null && !"".equals(startDate)) {
		   		              sql.append(" AND TO_CHAR(A.STARTDATE,'YYYY-MM-DD')>= ?");
		   		              }
		   		              if (endDate != null && !"".equals(endDate)) {
		   		              sql.append(" AND TO_CHAR(A.ENDDATE,'YYYY-MM-DD')<= ?");
		   		              }
		              sql.append(" ORDER BY A.USERID , A.STARTDATE) C ) B ");
		       sql.append(" ) GROUP BY USERID");
		sql.append(" ) D ON J.USERID=D.USERID");
		
		final String sql1 = sql.toString();
		final int final_type = type;
		final String final_cuid = cuid;
		final Long final_orgroleid = orgroleid;
		final String final_departmentname = departmentname;
		final String final_username = username;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
				if(final_type==0){
					query.setString(j, final_cuid);j++;
				}else if(final_type==1){
					 if(!(final_orgroleid==5)){
					String[] departmentnames = final_departmentname.split(",");
					for (int i = 0; i < departmentnames.length; i++) {
						query.setString(j, (departmentnames[i].substring(1, departmentnames[i].length()-1)));j++;
					}
					 }
				}else if(final_type==2){
					query.setString(j, final_cuid);j++;
				}
				if (final_username != null && !"".equals(final_username)) {
					query.setString(j, "%"+final_username+"%");j++;
				}
				if (final_startDate != null && !"".equals(final_startDate)) {
					query.setString(j, final_startDate);j++;
				}
				if (final_endDate != null && !"".equals(final_endDate)) {
					query.setString(j, final_endDate);j++;
				}
				
				
				if(final_type==0){
					query.setString(j, final_cuid);j++;
				}else if(final_type==1){
					if(!(final_orgroleid==5)){
					String[] departmentnames = final_departmentname.split(",");
					for (int i = 0; i < departmentnames.length; i++) {
						query.setString(j, (departmentnames[i].substring(1, departmentnames[i].length()-1)));j++;
					}
					}
				}else if(final_type==2){
					query.setString(j, final_cuid);j++;
				}
				if (final_username != null && !"".equals(final_username)) {
					query.setString(j, "%"+final_username+"%");j++;
				}
				if (final_startDate != null && !"".equals(final_startDate)) {
					query.setString(j, final_startDate);j++;
				}
				if (final_endDate != null && !"".equals(final_endDate)) {
					query.setString(j, final_endDate);j++;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				/*for (Object[] object : list) {

					String username = (String) object[1];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
					} else {
						m.put(username, new BigDecimal(1));
					}

				}*/
				for (Object[] object : list) {
					map = new HashMap();
					String userid = (String) object[0];
					String username = (String) object[1];
					BigDecimal num = (BigDecimal) object[2];
					String title = (String) object[3];
					String startDate = (String) object[4];
					String endDate = (String) object[5];
					BigDecimal id = (BigDecimal) object[6];
					
					Date js = (Date) object[7];
					Date je = (Date) object[8];
					String starttime = (String) object[9];
					String endtime = (String) object[10];
					BigDecimal isallday = (BigDecimal) object[11];
					BigDecimal isalert = (BigDecimal) object[12];
					String alerttime = (String) object[13];
					BigDecimal issharing = (BigDecimal) object[14];
					String remark = (String) object[15];
					Date re_startdate = (Date) object[16];
					Date re_enddate = (Date) object[17];
					String re_starttime = (String) object[18];
					String re_endtime = (String) object[19];
					String re_mode = (String) object[20];
					String re_day_interval = (String) object[21];
					String re_week_date = (String) object[22];
					String re_month_days = (String) object[23];
					String re_year_month = (String) object[24];
					String re_year_days = (String) object[25];
					
					String wczt = (String) object[26];
					String wcqk = (String) object[27];
					String extends1 = (String) object[28];
					map.put("userid", userid);
					map.put("username", username);
					map.put("num", num==null?"1":num.toString());
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					map.put("id", id.toString());
					if(startDate!=null&&!startDate.equals("")&&startDate.length()>10){
						map.put("isclick", startDate.substring(0, 10).equals(UtilDate.getNowdate())?"1":"0");
						if(!"".equals(js) && js!=null)
							map.put("js", sdf.format(js));
						else
							map.put("js", "");
						if(!"".equals(je) && je!=null)
							map.put("je", sdf.format(je));
						else
							map.put("je", "");
					}else {
						map.put("isclick", "0");
						map.put("js", "");
						map.put("je", "");
					}
					
					
					map.put("starttime", starttime);
					map.put("endtime", endtime);
					map.put("isallday", isallday);
					map.put("isalert", isalert);
					map.put("alerttime", alerttime);
					map.put("issharing", issharing);
					map.put("remark", remark);
					map.put("re_startdate", re_startdate!=null?sdf.format(re_startdate):"");
					map.put("re_enddate", re_enddate!=null?sdf.format(re_enddate):"");
					map.put("re_starttime", re_starttime);
					map.put("re_endtime", re_endtime);
					map.put("re_mode", re_mode);
					map.put("re_day_interval", re_day_interval);
					map.put("re_week_date", re_week_date);
					map.put("re_month_days", re_month_days);
					map.put("re_year_month", re_year_month);
					map.put("re_year_days", re_year_days);
					map.put("wczt", wczt);
					map.put("wcqk", wcqk);
					map.put("extends1", extends1);
					l.add(map);
				}
				return l;
			}
		});
	}
	public int getWorkLogRow() {
		int count = 0;
		String sql = "select count(*) as count FROM IWORK_SCH_CALENDAR";
		count = DBUtil.getInt(sql, "count");
		return count;
	}

	public List<Map> getWorkListSize(String username, String startDate, String endDate, int type, String departmentname, String cuid,String depname) {	
		UserContext uc = UserContextUtil.getInstance().getUserContext(cuid);
		Long orgroleid = uc.get_userModel().getOrgroleid();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(username!=null&&!"".equals(username)){
			if(d.HasInjectionData(username)){
				return l;
			}
		}
		if(startDate!=null&&!"".equals(startDate)){
			if(d.HasInjectionData(startDate)){
				return l;
			}
		}
		if(endDate!=null&&!"".equals(endDate)){
			if(d.HasInjectionData(endDate)){
				return l;
			}
		}
		if(departmentname!=null&&!"".equals(departmentname)){
			if(d.HasInjectionData(departmentname)){
				return l;
			}
		}
		if(cuid!=null&&!"".equals(cuid)){
			if(d.HasInjectionData(cuid)){
				return l;
			}
		}
		StringBuffer sql = new StringBuffer();
		 
		
	
	
		sql.append("  select * from (   select t.*,rownum rn from (select * from (SELECT J.USERID,  J.USERNAME,  J.DEPARTMENTNAME, D.NUM,  J.TITLE, TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME STARTDATE,  ");
		sql.append(" TO_CHAR(J.ENDDATE, 'YYYY-MM-DD') || ' ' || J.ENDTIME ENDDATE,  J.ID,  J.REMARK, J.WCZT,  J.WCQK,  J.EXTENDS1,   J.UIDS,  J.DEPARTMENTID,  ");
		sql.append("  row_number() OVER(PARTITION BY  J.UIDS ORDER BY   TO_CHAR(J.STARTDATE, 'YYYY-MM-DD') || ' ' || J.STARTTIME  desc) as row_flg   FROM (  SELECT USERID,UIDS, COUNT(USERID) NUM  ");
		sql.append("  FROM (SELECT USERID,UIDS  FROM(SELECT  USERID, USERNAME,UIDS, ROWNUM RN  ");
		sql.append("    FROM (SELECT A.*, O.USERNAME,O.USERID UIDS FROM ORGUSER O LEFT JOIN IWORK_SCH_CALENDAR A ON A.USERID = O.USERID WHERE O.ORGROLEID<>3 AND SYSDATE<O.ENDDATE AND A.RE_MODE IS NULL ");
		sql.append("    ORDER BY A.USERID, A.STARTDATE ) C) B )  GROUP BY USERID,UIDS order by userid  ) D  ");
		sql.append("  LEFT JOIN ( SELECT * FROM (SELECT C.*, ROWNUM RN FROM (  SELECT O.USERID UIDS,A.*, USERNAME,O.DEPARTMENTNAME,O.DEPARTMENTID from ORGUSER O left join     ");
		sql.append("   (SELECT * FROM IWORK_SCH_CALENDAR   ) A  ");
		sql.append("    ON A.USERID = O.USERID    WHERE SYSDATE<O.ENDDATE   ORDER BY A.USERID, A.STARTDATE desc ) C) B )J  ON J.UIDS = D.UIDS) temp  ");
		sql.append(" where temp.row_flg  = '1'  order by userid) t where 1=1    ");
		  if (username != null && !"".equals(username)) {
              sql.append(" AND t.USERNAME LIKE ?");
         }
		  if (depname != null && !"".equals(depname)) {
              sql.append(" AND t.departmentname LIKE ?");
         }
		 if(orgroleid!=5){
			 if(uc.get_userModel().getIsmanager()!=null && uc.get_userModel().getIsmanager()==1){
				  sql.append(" AND t.DEPARTMENTID = '"+uc.get_userModel().getDepartmentid()+"'");
			 }else{
				 sql.append(" AND t.UIDS = '"+uc.get_userModel().getUserid()+"'");
			 }
		 }
		sql.append(" ) tt   order by tt.STARTDATE desc, num desc");
		final String sql1 = sql.toString();
		final String final_cuid = cuid;
		final Long final_orgroleid = orgroleid;
		final String final_departmentname = depname;
		final String final_username = username;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
			
				if (final_username != null && !"".equals(final_username)) {
					query.setString(j, "%"+final_username+"%");j++;
				}
				if (final_departmentname != null && !"".equals(final_departmentname)) {
					query.setString(j, "%"+final_departmentname+"%");j++;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				
				for (Object[] object : list) {
					map = new HashMap();
					String username = (String) object[1];
					String departmentname = (String) object[2];
				
					map.put("username", username);
				
					map.put("depname", departmentname);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<Map> getWorkLogList(String username, String startDate,
			String endDate) {
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		// String sql = "FROM " + IworkSchCalendar.DATABASE_ENTITY +
		// " where 1 = 1 " +
		// " ORDER BY  userid desc";
		StringBuffer sql = new StringBuffer("select * from (select a.userid,a.username,a.num,b.title,to_char(b.startdate, 'yyyy-mm-dd')||' '||b.starttime startdate,to_char(b.enddate, 'yyyy-mm-dd')||' '||b.endtime  enddate from (select a.userid, nvl(b.username, '') username, count(*) num from IWORK_SCH_CALENDAR a left join orguser b on a.userid = b.userid where 1=1  and (b.orgroleid <> 3 or b.orgroleid is null) group by a.userid, b.username) a,IWORK_SCH_CALENDAR b where a.userid = b.userid ) a where 1=1");
		if(username!=null&&!username.equals("")){
			if(d.HasInjectionData(username)){
				return l;
			}
			sql.append(" and a.username='"+username+"'");
		}
		if(startDate!=null&&!startDate.equals("")){
			if(d.HasInjectionData(startDate)){
				return l;
			}
			sql.append(" and a.startdate>=?");
		}
		if(endDate!=null&&!endDate.equals("")){
			if(d.HasInjectionData(endDate)){
				return l;
			}
			sql.append(" and a.enddate<=?");
		}
		sql.append(" order by a.userid");
		final String sql1 = sql.toString();
		final String final_username = username;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i = 0;
				if(final_username!=null&&!final_username.equals("")){
					query.setString(i, final_username);i++;
				}
				if(final_startDate!=null&&!final_startDate.equals("")){
					query.setString(i, final_startDate+" 00:00");i++;
				}
				if(final_endDate!=null&&!final_endDate.equals("")){
					query.setString(i, final_endDate+" 23:59");i++;
				}
				// Query query = session.createQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[1];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
						
					} else {
						m.put(username, new BigDecimal(1));
						
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String id = (String) object[0];
					String username = (String) object[1];
					BigDecimal num = (BigDecimal) object[2];
					String title = (String) object[3];
					String startDate = (String) object[4];
					String endDate = (String) object[5];
					map.put("username", username);
					map.put("num", m.get(username));
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					l.add(map);
				}
				return l;
			}
		});
	}

	
	//获得提醒短信文本
	public String  tixingSMS(String title,String starttime){
		
		return title+";"+"开始时间"+starttime;
		
		
	}
	
	public void doExcelExp(HttpServletResponse response,String username, String depname,int type,String departmentname,String cuid) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("工作日志");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setWrapText(true);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("部门");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("工作内容");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("完成状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("完成情况");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，int pageSize, int pageNow
		List list = getWorkLogList(0,0,username, type, cuid, depname );
		
		List<Map> l;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		Map person = new HashMap();

		for (int j = 0; j < list.size(); j++) {
			Map map = (HashMap) list.get(j);
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(map.get("username").toString()+"("+map.get("num").toString()+")");
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(map.get("depname").toString());
			cell2.setCellStyle(style1);
			HSSFCell cell3 = row.createCell((short) 2);
			cell3.setCellValue(map.get("startdate")==null?"":map.get("startdate").toString());
			cell3.setCellStyle(style1);
			HSSFCell cell4 = row.createCell((short) 3);
			cell4.setCellValue(map.get("title")==null?"":map.get("title").toString());
			cell4.setCellStyle(style1);
			HSSFCell cell5 = row.createCell((short) 4);
			cell5.setCellValue(map.get("wczt")==null?"":map.get("wczt").toString());
			cell5.setCellStyle(style1);
			HSSFCell cell6 = row.createCell((short) 5);
			cell6.setCellValue(map.get("wcqk")==null?"":map.get("wcqk").toString());
			cell6.setCellStyle(style1);
			HSSFCell cell7 = row.createCell((short) 6);
			cell7.setCellValue(map.get("remark")==null?"":map.get("remark").toString());
			cell7.setCellStyle(style1);
			m++;
		}
	//	sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));

		
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 4500);
		sheet.setColumnWidth(3, 17000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 17000);
		sheet.setColumnWidth(6, 17000);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("工作总结.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
			// out1.flush();
			// out1.close();

		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	/*public void doExcelExp(HttpServletResponse response,String username, String depname,int type,String departmentname,String cuid) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("工作日志");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setWrapText(true);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("工作内容");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("完成状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("完成情况");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，int pageSize, int pageNow
		List list = getWorkLogList(0,0,username, type, cuid, depname );
		
		List<Map> l;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		Map person = new HashMap();

		for (int j = 0; j < list.size(); j++) {
			Map map = (HashMap) list.get(j);
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue((map.get("username") != null ? map.get(
					"username").toString() : "")
					+ "(" + map.get("num").toString() + ")");
			cell1.setCellStyle(style2);
			// 如何记录已显示人员的map里没有记录，或者不等于当前的用户
			if (person.get("username") == null
					|| !person
							.get("username")
							.toString()
							.equals((map.get("username") != null ? map.get(
									"username").toString() : "")
									+ "(" + map.get("num").toString() + ")")) {
				// 单元格合并
				// 四个参数分别是：起始行，起始列，结束行，结束列
				if (person.get("username") != null) {
					sheet.addMergedRegion(new Region(Integer.parseInt(person
							.get("begin").toString()), (short) 0, n - 2,
							(short) 0));
				}
				person.put("username",
						(map.get("username") != null ? map.get("username")
								.toString() : "")
								+ "("
								+ map.get("num").toString() + ")");
				person.put("begin", n - 1);
				// 再把式样设置到cell中：
			}
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(map.get("startdate").toString());
			cell2.setCellStyle(style1);
			HSSFCell cell3 = row.createCell((short) 2);
			cell3.setCellValue(map.get("title").toString());
			cell3.setCellStyle(style1);
			HSSFCell cell4 = row.createCell((short) 3);
			cell4.setCellValue(map.get("wczt")==null?"":map.get("wczt").toString());
			cell4.setCellStyle(style1);
			HSSFCell cell5 = row.createCell((short) 4);
			cell5.setCellValue(map.get("wcqk")==null?"":map.get("wcqk").toString());
			cell5.setCellStyle(style1);
			HSSFCell cell6 = row.createCell((short) 5);
			cell6.setCellValue(map.get("remark")==null?"":map.get("remark").toString());
			cell6.setCellStyle(style1);
			short colLength = (short) (map.get("title").toString().length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
			m++;
		}
		sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));

		
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 15000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 15000);
		sheet.setColumnWidth(5, 15000);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("工作日志.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
			// out1.flush();
			// out1.close();

		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}*/
}
