package com.ibpmsoft.project.zqb.zt.dao;

import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;


public class ZqbZtProjectDao extends HibernateDaoSupport{
	
	public List<HashMap> getMeetRunList(int pageSize, int pageNumber, String userid, String customerno, Long orgRoleId,
			String zqdm, String zqjc, String startdate, String enddate, String noticename, String spzt) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.ID,B.INSTANCEID,B.NODE1,B.NODE2,B.NODE3,B.NODE4,B.NODE5,NODE6,B.NODE7,"
				+ "B.NODE8,B.NODE9,B.NODE1SFKF,B.NODE2SFKF,B.NODE3SFKF,B.NODE4SFKF,B.NODE5SFKF,B.NODE6SFKF,"
				+ "B.NODE7SFKF,B.NODE8SFKF,B.NODE9SFKF,B.NODE1SKP FROM BD_ZQB_GPJDNODE B WHERE 1=1 ");
				
	
		if(orgRoleId!=5l){
			sb.append(" AND B.NODE7SFKF =?");
			
		
			params.add(userid);
		}
//		if(customerno!=null&&!customerno.equals("")){
//			sb.append(" AND B.NODE1=?");
//			params.add(customerno);
//		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND B.NODE1=?");
			params.add("%"+zqdm+"%");
		}
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND B.NODE2=?");
			params.add("%"+zqjc+"%");
		}
//		if(zqdm!=null&&!"".equals(zqdm)){
//			sb.append(" AND K.NODE2 LIKE ?");
//			params.add("%"+zqdm+"%");
//		}
		
		if(startdate!=null&&!"".equals(startdate)){
			sb.append(" AND B.NODE8SFKF>=TO_DATE(?,'yyyy-MM-dd')");
			params.add(startdate);
		}
		if(enddate!=null&&!"".equals(enddate)){
			sb.append(" AND B.NODE8SFKF<=TO_DATE(?,'yyyy-MM-dd')");
			params.add(enddate);
		}
//		if(noticename!=null&&!"".equals(noticename)){
//			sb.append(" AND B.SXMC LIKE ?");
//			params.add("%"+noticename+"%");
//		}
		if(spzt!=null&&!"".equals(spzt)){
			sb.append("  AND B.NODE9SFKF =?");
			params.add(spzt);
		}
		sb.append(" ORDER BY B.NODE8SFKF DESC,B.ID DESC");
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					BigDecimal INSTANCEID = (BigDecimal)object[1];
					String NODE1 = (String)object[2];
					String NODE2 = (String)object[3];
					String NODE3 = (String)object[4];
					String NODE4 = (String)object[5];
					java.sql.Date NODE5 = (java.sql.Date)object[6];
					String NODE6 = (String)object[7];
					String NODE7 = (String)object[8];
					java.sql.Date NODE8 = (java.sql.Date)object[9];
					String NODE9 = (String)object[10];
					String NODE1SFKF = (String)object[11];
					String NODE2SFKF = (String)object[12];
					String NODE3SFKF = (String)object[13];
					String NODE4SFKF = (String)object[14];
					String NODE5SFKF = (String)object[15];
					String NODE6SFKF = (String)object[16];
					String NODE7SFKF = (String)object[17];
					java.sql.Date NODE8SFKF  = (java.sql.Date)object[18];
					String NODE9SFKF = (String)object[19];
					String NODE1SKP=(String) object[20];

					map.put("ID", ID.toString());
					map.put("INSTANCEID", INSTANCEID.toString());
					map.put("ZQJC", NODE1);
					map.put("NODE2", NODE2);
					map.put("NODE3", NODE3);
					map.put("NODE4", NODE4);
					map.put("NODE5", NODE5);
					map.put("NODE6", NODE6);
					map.put("NODE7", NODE7);
					map.put("NODE8", NODE8);
					map.put("NODE9", NODE9);
					map.put("NODE1SFKF", NODE1SFKF);
					map.put("NODE2SFKF", NODE2SFKF);
					map.put("LCBS", NODE3SFKF);
					map.put("NODE4SFKF",NODE4SFKF);
					map.put("NODE5SFKF",NODE5SFKF);
					map.put("CREATENAME", NODE6SFKF);
					map.put("NODE7SFKF", NODE7SFKF);
					map.put("CREATEDATE", NODE8SFKF);
					map.put("SPZT", NODE9SFKF);
					map.put("NODE1SKP", NODE1SKP);
					
					
//					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(NODE3SFKF, NODE2SFKF);
//				    if(pro!=null&pro.size()>0){
//				    	Long prc=pro.get(0).getPrcDefId();
//				    	map.put("PRCID", prc.toString());
//				    }
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getEventRunListSize(String userid, String customerno, Long orgRoleId, String zqdm, String zqjc,
			String startdate, String enddate, String noticename, String spzt) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.ID,B.INSTANCEID,B.NODE1,B.NODE2,B.NODE3,B.NODE4,B.NODE5,NODE6,B.NODE7,"
				+ "B.NODE8,B.NODE9,B.NODE1SFKF,B.NODE2SFKF,B.NODE3SFKF,B.NODE4SFKF,B.NODE5SFKF,B.NODE6SFKF,"
				+ "B.NODE7SFKF,B.NODE8SFKF,B.NODE9SFKF,B.NODE1SKP FROM BD_ZQB_GPJDNODE B WHERE 1=1 ");
				
	
		if(orgRoleId!=5l){
			sb.append(" AND B.NODE7SFKF =?");
			
		
			params.add(userid);
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND B.NODE1=?");
			params.add(customerno);
		}
//		if(zqdm!=null&&!"".equals(zqdm)){
//			sb.append(" AND K.NODE2 LIKE ?");
//			params.add("%"+zqdm+"%");
//		}
		
		if(startdate!=null&&!"".equals(startdate)){
			sb.append(" AND B.NODE8SFKF>=TO_DATE(?,'yyyy-MM-dd')");
			params.add(startdate);
		}
		if(enddate!=null&&!"".equals(enddate)){
			sb.append(" AND B.NODE8SFKF<=TO_DATE(?,'yyyy-MM-dd')");
			params.add(enddate);
		}
//		if(noticename!=null&&!"".equals(noticename)){
//			sb.append(" AND B.SXMC LIKE ?");
//			params.add("%"+noticename+"%");
//		}
		if(spzt!=null&&!"".equals(spzt)){
			sb.append("  AND B.NODE9SFKF =?");
			params.add(spzt);
		}
		sb.append(" ORDER BY B.NODE8SFKF DESC");
		
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
				
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					BigDecimal INSTANCEID = (BigDecimal)object[1];
					String NODE1 = (String)object[2];
					String NODE2 = (String)object[3];
					String NODE3 = (String)object[4];
					String NODE4 = (String)object[5];
					java.sql.Date NODE5 = (java.sql.Date)object[6];
					String NODE6 = (String)object[7];
					String NODE7 = (String)object[8];
					java.sql.Date NODE8 = (java.sql.Date)object[9];
					String NODE9 = (String)object[10];
					String NODE1SFKF = (String)object[11];
					String NODE2SFKF = (String)object[12];
					String NODE3SFKF = (String)object[13];
					String NODE4SFKF = (String)object[14];
					String NODE5SFKF = (String)object[15];
					String NODE6SFKF = (String)object[16];
					String NODE7SFKF = (String)object[17];
					java.sql.Date NODE8SFKF  = (java.sql.Date)object[18];
					String NODE9SFKF = (String)object[19];
					String NODE1SKP=(String) object[20];

					map.put("ID", ID.toString());
					map.put("INSTANCEID", INSTANCEID.toString());
					map.put("NODE1", NODE1);
					map.put("NODE2", NODE2);
					map.put("NODE3", NODE3);
					map.put("NODE4", NODE4);
					map.put("NODE5", NODE5);
					map.put("NODE6", NODE6);
					map.put("NODE7", NODE7);
					map.put("NODE8", NODE8);
					map.put("NODE9", NODE9);
					map.put("NODE1SFKF", NODE1SFKF);
					map.put("NODE2SFKF", NODE2SFKF);
					map.put("NODE3SFKF", NODE3SFKF);
					map.put("NODE4SFKF",NODE4SFKF);
					map.put("NODE5SFKF",NODE5SFKF);
					map.put("NODE6SFKF", NODE6SFKF);
					map.put("NODE7SFKF", NODE7SFKF);
					map.put("NODE8SFKF", NODE8SFKF);
					map.put("NODE9SFKF", NODE9SFKF);
					map.put("NODE1SKP", NODE1SKP);
//					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(NODE3SFKF, NODE2SFKF);
//				    if(pro!=null&pro.size()>0){
//				    	Long prc=pro.get(0).getPrcDefId();
//				    	map.put("PRCID", prc.toString());
//				    }
					l.add(map);
				}
				return l;
			}
		});
	}

}
