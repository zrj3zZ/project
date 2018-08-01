package com.iwork.app.message.sysmsg.dao;

import com.iwork.app.message.sysmsg.model.SysMessageLog;
import com.iwork.core.db.DBUtil;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



public class SysMessageLogDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SysMessageLogDAO.class);
	// property constants
	public static final String RCVRANGE = "rcvrange";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String URL = "url";
	public static final String SENDDATE = "senddate";
	public static final String SENDER = "sender";
	
	public int getMsgLogRow() {
		int count = 0;
        String sql = "select count(*) as count FROM Sys_message_log";
        count = DBUtil.getInt(sql, "count");
        return count;
	}
	
	public List<SysMessageLog> getMsgLogList(int pageSize, int pageNow) {
		final int pageSize1 = pageSize;
		final int startRow1 = ( pageNow - 1 ) * pageSize;
		String sql = "FROM " + SysMessageLog.DATABASE_ENTITY + " where 1 = 1 ORDER BY  senddate desc";
		final String sql1 = sql;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});
	}

	public void save(SysMessageLog model) {
		log.debug("saving SysMessageLog instance");
		try {
			Date now = new Date();
            String nowStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(now);
            model.setSendDate(nowStr);
            this.getHibernateTemplate().save(model);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysMessageLog persistentInstance) {
		log.debug("deleting SysMessageLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysMessageLog findById(java.lang.Long id) {
		log.debug("getting SysMessageLog instance with id: " + id);
		try {
			SysMessageLog instance = (SysMessageLog) getSession().get(
					"com.iwork.app.message.sysmsg.model.SysMessageLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysMessageLog instance) {
		log.debug("finding SysMessageLog instance by example");
		try {
			List results = getSession()
					.createCriteria(
							"com.iwork.app.message.sysmsg.model.SysMessageLog")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding SysMessageLog instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from SysMessageLog as model where model.? = ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, propertyName);
			queryObject.setParameter(1, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRcvrange(Object rcvrange) {
		return findByProperty(RCVRANGE, rcvrange);
	}

	public List findByTitle(Object title) {
		return findByProperty(TITLE, title);
	}

	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List findByUrl(Object url) {
		return findByProperty(URL, url);
	}

	public List findBySenddate(Object senddate) {
		return findByProperty(SENDDATE, senddate);
	}

	public List findBySender(Object sender) {
		return findByProperty(SENDER, sender);
	}

	public List findAll() {
		log.debug("finding all SysMessageLog instances");
		try {
			String queryString = "from SysMessageLog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysMessageLog merge(SysMessageLog detachedInstance) {
		log.debug("merging SysMessageLog instance");
		try {
			SysMessageLog result = (SysMessageLog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysMessageLog instance) {
		log.debug("attaching dirty SysMessageLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysMessageLog instance) {
		log.debug("attaching clean SysMessageLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}
