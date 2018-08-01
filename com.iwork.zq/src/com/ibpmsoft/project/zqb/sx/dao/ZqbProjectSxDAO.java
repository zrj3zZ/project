package com.ibpmsoft.project.zqb.sx.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;

public class ZqbProjectSxDAO extends HibernateDaoSupport {

	public List<HashMap> getSxQusans(String projectno) {
		StringBuffer sb = new StringBuffer("SELECT A.QINS,B.AINS,A.QUESTION,B.CONTENT,A.TASKNO,A.CREATEDATE FROM( SELECT S.INSTANCEID QINS,B.QUESTION,B.TASKNO,B.ID,B.XMBH,B.CREATEDATE FROM BD_ZQB_XMWTFK B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM A WHERE A.IFORM_TITLE='问题反馈表单')) A LEFT JOIN (SELECT C.CONTENT,S2.INSTANCEID AINS,C.QUESTIONNO FROM BD_ZQB_RETALK C LEFT JOIN SYS_ENGINE_FORM_BIND S2 ON C.ID=S2.DATAID WHERE S2.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM A WHERE A.IFORM_TITLE='问题回复表')) B ON A.ID=B.QUESTIONNO WHERE 1=1");
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		
		if(projectno!=null&&!projectno.equals("")){
			if(d.HasInjectionData(projectno)){
				return l;
			}
			sb.append(" AND A.XMBH=? ");
		}
		final String sql1 = sb.toString();
		final String pro=projectno;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setString(0, pro);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				
				for (Object[] obj : list) {
					map = new HashMap();
					BigDecimal qins = (BigDecimal)obj[0];
					BigDecimal ains = (BigDecimal)obj[1];
					String question = (String)obj[2];
					String content = (String)obj[3];
					String taskno = (String)obj[4];
					Date createdate = (Date)obj[5];
					
					map.put("QINS",qins.toString());
					map.put("AINS",ains.toString());
					map.put("QUESTION",question);
					map.put("CONTENT",content);
					map.put("TASKNO",taskno);
					map.put("CREATEDATE",UtilDate.dateFormat(createdate));
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> industryMsgAssociate(String xmbz) {
		StringBuffer sql = new StringBuffer();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		sql.append("SELECT ZHUHY,ZIHY FROM BD_XP_HYXXB WHERE 1=1");
		if(xmbz!=null&&!xmbz.equals("")){
			if(d.HasInjectionData(xmbz)){
				return l;
			}
			sql.append(" AND ZIHY LIKE ? ");
		}
		final String sql1 = sql.toString();
		final String xm=xmbz;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setString(0, "%"+xm+"%");
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					map = new HashMap();
					String zhuhy = (String)object[0];
					String zihy = (String)object[1];
					map.put("ZHUHY", zhuhy);
					map.put("ZIHY", zihy);
					l.add(map);
				}
				return l;
			}
		});
	}
}
