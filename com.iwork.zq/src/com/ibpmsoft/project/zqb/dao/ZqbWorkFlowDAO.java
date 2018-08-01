package com.ibpmsoft.project.zqb.dao;

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
import com.iwork.core.db.DBUtil;


public class ZqbWorkFlowDAO extends HibernateDaoSupport{
	
	public List<HashMap> getCurrentFolwList(int pageSize, int pageNow){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		StringBuffer sb = new StringBuffer("select BOTABLE.CJR,to_char(BOTABLE.CJSJ, 'yyyy-mm-dd'),BOTABLE.LCSWBH,BOTABLE.TYPE,BOTABLE.SWMS,BOTABLE.ZYLC,BOTABLE.STATUS,BOTABLE.ID,BINDTABLE.INSTANCEID,a.EXTEND1 FROM BD_FLOW_LCSWZY BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131 inner join (SELECT *  FROM ORGUSER) a on BOTABLE.CJR = a.userid ORDER BY BOTABLE.ID DESC");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				// Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[0];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
					} else {
						m.put(username, new BigDecimal(1));
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String cjr = (String) object[0];
					String cjsj = (String) object[1];
					String lcswbh = (String) object[2];
					String type = (String) object[3];
					String swms = (String) object[4];
					String zylc = (String) object[5];
					BigDecimal status=(BigDecimal)object[6];
					Integer id = Integer.valueOf(object[7].toString());
					Integer instanceid = Integer.valueOf(object[8].toString());
					String extend1 = (String) object[9];
					map.put("CJR", cjr);
					map.put("CJSJ", cjsj);
					map.put("LCSWBH", lcswbh);
					map.put("TYPE", type);
					map.put("SWMS", swms);
					map.put("ZYLC", zylc);
					map.put("STATUS", status);
					map.put("ID", id);
					map.put("INSTANCEID", instanceid);
					map.put("EXTEND1", extend1);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getCurrentFolwList(int pageSize, int pageNow,String cjr){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select BOTABLE.CJR,to_char(BOTABLE.CJSJ, 'yyyy-mm-dd'),BOTABLE.LCSWBH,BOTABLE.TYPE,BOTABLE.SWMS,BOTABLE.ZYLC,BOTABLE.STATUS,BOTABLE.ID,BINDTABLE.INSTANCEID,a.EXTEND1 FROM BD_FLOW_LCSWZY BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131 and BOTABLE.CJR=? inner join (SELECT *  FROM ORGUSER) a on BOTABLE.CJR = a.userid ORDER BY BOTABLE.ID DESC");
		params.add(cjr);
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[0];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
					} else {
						m.put(username, new BigDecimal(1));
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String cjr = (String) object[0];
					String cjsj = (String) object[1];
					String lcswbh = (String) object[2];
					String type = (String) object[3];
					String swms = (String) object[4];
					String zylc = (String) object[5];
					BigDecimal status=(BigDecimal)object[6];
					Integer id = Integer.valueOf(object[7].toString());
					Integer instanceid = Integer.valueOf(object[8].toString());
					String extend1 = (String) object[9];
					map.put("CJR", cjr);
					map.put("CJSJ", cjsj);
					map.put("LCSWBH", lcswbh);
					map.put("TYPE", type);
					map.put("SWMS", swms);
					map.put("ZYLC", zylc);
					map.put("STATUS", status);
					map.put("ID", id);
					map.put("INSTANCEID", instanceid);
					map.put("EXTEND1", extend1);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public int getCurrentFolwListSize(){
		int count = 0;
		String sql = "select count(*) as NUM FROM BD_FLOW_LCSWZY BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131 inner join (SELECT *  FROM ORGUSER) a on BOTABLE.CJR = a.userid ORDER BY BOTABLE.ID DESC";
		count = DBUtil.getInt(sql.toString(), "NUM");
		return count;
	}
	
	public List<HashMap> getCurrentFolwList(){
		StringBuffer sb = new StringBuffer("select BOTABLE.CJR,to_char(BOTABLE.CJSJ, 'yyyy-mm-dd'),BOTABLE.LCSWBH,BOTABLE.TYPE,BOTABLE.SWMS,BOTABLE.ZYLC,BOTABLE.STATUS,BOTABLE.ID,BINDTABLE.INSTANCEID,a.EXTEND1 FROM BD_FLOW_LCSWZY BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131 inner join (SELECT *  FROM ORGUSER) a on BOTABLE.CJR = a.userid ORDER BY BOTABLE.ID DESC");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				// Query query = session.createQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[0];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
					} else {
						m.put(username, new BigDecimal(1));
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String cjr = (String) object[0];
					String cjsj = (String) object[1];
					String lcswbh = (String) object[2];
					String type = (String) object[3];
					String swms = (String) object[4];
					String zylc = (String) object[5];
					BigDecimal status=(BigDecimal)object[6];
					Integer id = Integer.valueOf(object[7].toString());
					Integer instanceid = Integer.valueOf(object[8].toString());
					String extend1 = (String) object[9];
					map.put("CJR", cjr);
					map.put("CJSJ", cjsj);
					map.put("LCSWBH", lcswbh);
					map.put("TYPE", type);
					map.put("SWMS", swms);
					map.put("ZYLC", zylc);
					map.put("STATUS", status);
					map.put("ID", id);
					map.put("INSTANCEID", instanceid);
					map.put("EXTEND1", extend1);
					l.add(map);
				}
				return l;
			}
		});
	}
}
 