package com.iwork.plugs.itsArticles.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.itsArticles.model.ItsArticles;

public class ItsArticlesDao extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ItsArticlesDao.class);
	/**
	 * 插入
	 * 
	 * @param model
	 */
	public void addBoData(ItsArticles model) {
		this.getHibernateTemplate().save(model);
	}
	/**
	 * 查询
	 * @param type
	 * @return
	 */
	public List findByZdqdid(Long zdqdid) {
		log.debug("finding ItsArticles instances with zdqdid: " + zdqdid);
		try {
			StringBuffer queryStrB = new StringBuffer("SELECT fileUUID FROM ").append(ItsArticles.DATABASE_ENTITY).append(" WHERE ZDQDID = ?");
			Query queryObject = getSession().createQuery(queryStrB.toString());
			queryObject.setParameter(0, zdqdid);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by type failed", re);
			throw re;
		}
	}

	public List<HashMap<String, Object>> getItsList(final String khbh,final String zdmc,final String fjmc,
			 int pageNumber, int pageSize) {
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
		final int pageSize1 = pageNumber*pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		
		StringBuffer sql = new StringBuffer("SELECT ZD.ID,ZD.ZDMC,ZD.ZDWH,ZD.ZDXFRQ,ZD.ZDSFPL,ZD.FILEUUID,ZD.FILENAME,IASCOUNT.CNUM FROM (SELECT ZDQD.ID ID,ZDQD.ZDMC ZDMC,ZDQD.WH ZDWH,to_char(ZDQD.XFRQ,'yyyy-MM-dd') ZDXFRQ,ZDQD.SFGKPL ZDSFPL,IAS.FILEUUID FILEUUID,IAS.FILESRCNAME FILENAME,ROWNUM RNUM FROM BD_BASE_ZDQDXX ZDQD");
		sql.append(" LEFT JOIN BD_XP_ITSARTICLES IAS ON ZDQD.ID=IAS.ZDQDID WHERE ZDQD.KHBH=? ");
		if(zdmc!=null&&!"".equals(zdmc)){
			if(d.HasInjectionData(zdmc)){
				return lis;
			}
			sql.append(" AND ZDQD.ZDMC like ? ");
		}
		if(fjmc!=null&&!"".equals(fjmc)){
			if(d.HasInjectionData(fjmc)){
				return lis;
			}
			sql.append(" AND IAS.FILESRCNAME like ? ");
		}
		sql.append(" ) ZD");
		sql.append(" LEFT JOIN (SELECT  ID,COUNT(ID) CNUM FROM (SELECT ZDQD.ID,ROWNUM RNUM FROM BD_BASE_ZDQDXX ZDQD LEFT JOIN BD_XP_ITSARTICLES IAS ON ZDQD.ID=IAS.ZDQDID WHERE ZDQD.KHBH=?");
		if(zdmc!=null&&!"".equals(zdmc)){
			sql.append(" AND ZDQD.ZDMC like ? ");
		}
		if(fjmc!=null&&!"".equals(fjmc)){
			sql.append(" AND IAS.FILESRCNAME like ? ");
		}
		sql.append(" )");
		sql.append(" WHERE RNUM>? AND RNUM<=? GROUP BY ID) IASCOUNT ON ZD.ID=IASCOUNT.ID WHERE RNUM>? AND RNUM<=? ORDER BY ZD.ID");
		final String sql1 = sql.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i=0;
				query.setString(i,khbh);i++;
				if(zdmc!=null&&!"".equals(zdmc)){
					query.setString(i,"%"+zdmc+"%");i++;
				}
				
				if(fjmc!=null&&!"".equals(fjmc)){
					query.setString(i,"%"+fjmc+"%");i++;
				}
				query.setString(i, khbh);i++;
				if(zdmc!=null&&!"".equals(zdmc)){
					query.setString(i,"%"+zdmc+"%");i++;
				}
				if(fjmc!=null&&!"".equals(fjmc)){
					query.setString(i,"%"+fjmc+"%");i++;
				}
				query.setInteger(i, startRow1);i++;
				query.setInteger(i, pageSize1);i++;
				query.setInteger(i, startRow1);i++;
				query.setInteger(i, pageSize1);i++;
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String zdmc = (String) object[1];
					String zdwh = (String) object[2];
					String zdxfrq = (String) object[3];
					String zdsfpl = (String) object[4];
					String fileuuid = (String) object[5];
					String filename=(String)object[6];
					BigDecimal count = (BigDecimal) object[7];
					map.put("ID", id);
					map.put("ZDMC", zdmc);
					map.put("ZDWH", zdwh);
					map.put("ZDXFRQ", zdxfrq);
					map.put("ZDSFPL", zdsfpl);
					map.put("FILEUUID", fileuuid);
					map.put("FILENAME", filename);
					map.put("COUNT", count);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getItsListSize(String khbh,String zdmc,String fjmc) {
		int count=0;
		List parameter=new ArrayList();//存放参数
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) COUNT FROM BD_BASE_ZDQDXX ZDQD LEFT JOIN BD_XP_ITSARTICLES IAS ON ZDQD.ID=IAS.ZDQDID WHERE ZDQD.KHBH=? ");
		if(zdmc!=null&&!"".equals(zdmc)){
			sb.append(" AND ZDQD.ZDMC like ? ");
			parameter.add(zdmc);
		}
		if(fjmc!=null&&!"".equals(fjmc)){
			sb.append(" AND IAS.FILESRCNAME like ? ");
			parameter.add(fjmc);
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sb.toString());
			stmt.setString(1, khbh);
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+2, "%"+parameter.get(i).toString()+"%");
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				count = rset.getInt("COUNT");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return count;
	}

}
